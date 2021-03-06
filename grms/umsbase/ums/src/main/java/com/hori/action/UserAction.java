package com.hori.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.hori.adms.servlet.ValidateCodeServlet;
import com.hori.dao.queryBean.UserManagementQueryBean;
import com.hori.dao.queryBean.UserQueryBean;
import com.hori.db.support.DataGridPage;
import com.hori.enums.UserStatus;
import com.hori.enums.UserTypeSuper;
import com.hori.model.User;
import com.hori.model.UserType;
import com.hori.pageModel.Json;
import com.hori.pageModel.SessionInfo;
import com.hori.service.RedisCacheService;
import com.hori.service.RoleService;
import com.hori.service.UserService;
import com.hori.service.UserTypeService;
import com.hori.util.Encrypt;
import com.hori.util.IpUtil;
import com.hori.util.ResourceUtil;
import com.hori.utils.AjaxJsonAndXmlUtil;
import com.hori.utils.Md5;
import com.hori.utils.StaticValue;
import com.hori.vo.AddUserVo;
import com.hori.vo.SelectVo;
import com.hori.vo.UserDetailLoginVo;
import com.hori.vo.UserVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户管理（包括登录）ACTION
 * 
 * @author hhb
 *
 */
@Action(value = "userAction", results = {
										@Result(name = "user", location = "/system/accountmanagement.jsp"), 
										@Result(name = "showUserInfo", location = "/user/userInfo.jsp"),
										@Result(name = "retrievepassword", location = "/retrievepassword.jsp"),
										@Result(name = "login", location = "/login.jsp"),
										@Result(name = "passwordreset", location = "/system/passwordreset.jsp"),
										@Result(name = "addaccount", location = "/system/addaccount.jsp"),
										@Result(name = "accountedit", location = "/system/accountedit.jsp"),
										@Result(name = "systemnosuper", location = "/system/systemnosuper.jsp"),
										@Result(name = "index", location = "/index.jsp",type="redirect"),
										@Result(name = "userManagement", location = "/peopleManagement/accountManagement.jsp")})
					
public class UserAction extends BaseAction implements ModelDriven<UserVo> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private static final long ONEHOURS  = 60*60L;
	
	private static final String SAVETYPE  = "SAVETYPE";
	private static final String EDITTYPE  = "EDITTYPE";

	private static final Logger logger = Logger.getLogger(UserAction.class);

	private UserVo userVo ;
	@Autowired
	private UserService userService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private UserTypeService userTypeService;
	@Autowired
	private RoleService roleService;

	public UserVo getModel() {
		 if(userVo==null) userVo=new UserVo();
		return userVo;
	}
	public String retrievepassword(){
		return "retrievepassword";
	}
    
	/**
	 * 用户登录
	 */
	public void login() {
		//判断账号和密码是否为空
	    HttpServletRequest request =  getRequest(); 
		String userAccount= request.getParameter("userAccount");
		String password = request.getParameter("password");
		String sessionVevifiCode=(String) request.getSession().getAttribute(ValidateCodeServlet.LOGIN_VEVIFICODE);
		Json j = new Json();
		if(StringUtils.isBlank(userAccount)||StringUtils.isBlank(password)){
			//getRequest().setAttribute("msg", "用户名和密码不能为空!");
			j.setMsg("用户名和密码不能为空");
			writeJson(j);
			return;
		}
		User u = userService.login(userAccount,Encrypt.e(password).toUpperCase());
		
		if (u != null) {
			if(u.getStatus()!=UserStatus.ON.getValue()){
				//getRequest().setAttribute("msg", "该账号已禁用，请联系管理员");
				long msgErrer =  redisCacheService.incr(userAccount+StaticValue.LOGINPASSERRERTIMES, ONEHOURS, TimeUnit.SECONDS);
				logger.info("-----------------"+msgErrer);
				j.setMsg("该账号已禁用，请联系管理员");
				writeJson(j);
				return;
			}
			SessionInfo sessionInfo = saveSessionInfo(u);
			changeUserAuths(u);
			redisCacheService.del(userAccount+StaticValue.LOGINPASSERRERTIMES);
		} else {
			//getRequest().setAttribute("msg", "用户名/密码错误");
			j.setMsg("用户名/密码错误");
			long msgErrer =  redisCacheService.incr(userAccount+StaticValue.LOGINPASSERRERTIMES, ONEHOURS, TimeUnit.SECONDS);
			logger.info("-----------------"+msgErrer);
			writeJson(j);
			return;
		}
		j.setSuccess(true);
		writeJson(j);
	}

	/**
	 * 登录成功将用户信息保存到SESSION中
	 * 
	 * @param u
	 *            用户对象
	 * @return
	 */
	private SessionInfo saveSessionInfo(User u) {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUserId(u.getUserId());
		sessionInfo.setLoginName(u.getUserAccount());
		sessionInfo.setLoginPassword(userVo.getPassword());
		sessionInfo.setUserType(u.getUserType());
		sessionInfo.setIp(IpUtil.getIpAddr(ServletActionContext.getRequest()));
		
		sessionInfo.setSystemId(u.getSystemId());
		ServletActionContext.getRequest().getSession().setAttribute(ResourceUtil.getSessionInfoName(), sessionInfo);
		ServletActionContext.getRequest().getSession().setAttribute("usertype", u.getUserType());
		//添加用户信息到session
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().setAttribute("userId", u.getUserId());
		request.getSession().setAttribute("userPassword", u.getPassword());
/*		request.getSession().setAttribute("userRoleId", u.getRoleId());
*/		request.getSession().setAttribute("userAccount", u.getUserAccount());
		request.getSession().setAttribute("userType", u.getUserType());
		request.getSession().setAttribute("systemId", u.getSystemId());

		return sessionInfo;
	}

	/**
	 * 退出系统
	 */
	public void logout() {
		Json j = new Json();
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			//redisCacheService.del(session.getId());
			session.removeAttribute(ResourceUtil.getSessionInfoName());
			session.invalidate();
		}
		j.setSuccess(true);
		writeJson(j);
	}


	/**
	 * 跳转到用户管理页面
	 * 
	 * @return
	 */
	public String goUser() {
		return "user";
	}
	
	/**
	 * 跳转到修改密码页面
	 * 
	 * @return
	 */
	public String passwordReset() {
		return "passwordreset";
	}
	
	

	/**
	 * 获得用户datagrid
	 */
	public void datagrid() {
		try {
			UserQueryBean queryBean = new UserQueryBean();
			queryBean.setPageNumber(userVo.getPage());
			queryBean.setPageSize(userVo.getRows());
			if (StringUtils.isNotBlank(userVo.getForwardParams())) {
				queryBean.setForwardParams(userVo.getForwardParams());
			}
			writeJson(userService.datagrid(queryBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 跳转到当前用户信息页面
	 * 
	 * @return
	 */
	public String showUserInfo() {
		return "showUserInfo";
	}

	
	/**
	 * 改变用户的权限列表
	 * 
	 * @param u
	 */
	private void changeUserAuths(User u) {
		
		SessionInfo sessionInfo = (SessionInfo) ServletActionContext.getRequest().getSession().getAttribute(ResourceUtil.getSessionInfoName());
		sessionInfo.setAuthVos(userService.getAuthVos(u));
	
	}
	
	/**
	 * 修改用户密码
	 */
    public void modifyPwd(){
    	//AjaxJsonAndXmlUtil.writeJson(modifyPwdJudge(), getResponse());
    	writeJson(modifyPwdJudge());
    }
    
    /**
     * 更改密码的时候判断响应数据
     * @return
     */
    private String modifyPwdJudge(){
    	String error = "";
    	if(StringUtils.isBlank(userVo.getPassword())){
    		error = "登录密码不可以为空！";
    		return error;
    	}
    	if(StringUtils.isBlank(userVo.getResetPassword())){
    		error = "确认密码不可以为空！";
    		return error;
    	}
    	if(!(userVo.getPassword().equals(userVo.getResetPassword()))){
    		error = "两次输入密码不一致";
    		return error;
    	}
    	User user  = userService.getUserById(userVo.getId());
    	if(null==user){
    		error = "该用户不存在!";
    		return error;
    	}
    	if(StringUtils.isNotBlank(userVo.getOldPwd())){
    		if(!StringUtils.equals(user.getPassword(), Encrypt.e(userVo.getOldPwd()).toUpperCase())){
    			error = "原始密码错误!";
        		return error;
    		}
    	}
    	
    	Md5 md5 = new Md5();
		String passwd = md5.getMD5Str(userVo.getPassword());
		user.setPassword(passwd);
		userService.update(user);
		error = "success";
		return error;
    }
    
    public void updateStatus(){
    	String error = "";
    	try {
    		userService.updateStatus(userVo.getId(), userVo.getStatus());
    		error="success";
		} catch (Exception e) {
			error = "更改状态失败！请联系管理员";
		}
    	AjaxJsonAndXmlUtil.writeJson(error, getResponse());
    }
    
    
    public String gotoSave(){
    	//验证账号查找是否已经存在超管和广告管理员
    	List<UserType> userTypeList = userTypeService.findAll();
    	//重新封装数据
    	userTypeList = getUserTypeViewData(userTypeList,"");
    	getRequest().setAttribute("userTypeList", userTypeList);
		return "addaccount";
    }
    
    /**
     * 处理添加用户页面所需要的用户类型 存在admin 和广告管理员后就不能显示了 重新处理显示的数据
     * @return
     */
    private List<UserType> getUserTypeViewData(List<UserType> userTypeList,String type) {
    	List<User> userList  =  userService.getSYSAndADSSYS();
    	List<UserType> userTypeListCopy = new ArrayList<UserType>();
    	userTypeListCopy.addAll(userTypeList);
    	for (User user : userList) {//遍历移除对象
			for (UserType userType : userTypeList) {
				if(userType.getId().toString().equals(user.getUserType())){
					if(StringUtils.isNotBlank(type)){
						if(user.getUserType().equals(type))
							continue;
						
						if(user.getUserType().equals(type))
							continue;
						//userTypeListCopy.remove(userType);
						/*continue;
						if(type.equals(com.hori.enums.UserType.GGGLY.getValue()))
						continue;*/
					}
					userTypeListCopy.remove(userType);
				}
			}
		}
		return userTypeListCopy;
	}
	public String gotoEdit(){
    	
    /*	String id = getRequest().getParameter("id");
    	User user = userService.getUserById(id);
    	getRequest().setAttribute("phone", user.getPhone());
    	getRequest().setAttribute("userName", user.getUserName());
    	getRequest().setAttribute("userType", user.getUserType());
    	getRequest().setAttribute("id", id);
    	getRequest().setAttribute("parentId", user.getParentId());
    	getRequest().setAttribute("roleId", user.getRoleId());
    	
    	List<UserType> userTypeList = userTypeService.findAll();
    	userTypeList = getUserTypeViewData(userTypeList, user.getUserType());
    	getRequest().setAttribute("userTypeList", userTypeList);*/
		return "accountedit";
    }
    
    public void getUserRole(){
    	//查找是否存在该角色
    	List<UserType> userTypeList = userTypeService.findAll();
    	List list = new ArrayList();
    	for (UserType userType : userTypeList) {
    		Map<String, Object>  map = new HashMap<>();
    		if(verificationRoleIsExistAdminAndADSSYS(userType.getId()))
    			continue;
    			
    		map.put("id", userType.getId());
    		map.put("text", userType.getName());
    		list.add(map);
		}
    	AjaxJsonAndXmlUtil.writeJson(JSONArray.fromObject(list).toString(), getResponse());
    }
    
    
    /**
     * 验证是角色否存在了广告管理员和超管的类型
     * @param id
     * @return
     */
    private boolean verificationRoleIsExistAdminAndADSSYS(Integer id) {
/*    	if((id.toString().equals(com.hori.enums.UserType.GGGLY.getValue()))||id.toString().equals(com.hori.enums.UserType.XTCJGLY.getValue())){
    		//查找是否存在广告管理员这个角色
    		List<Role> list =  roleService.findRoleByUserType(id.toString());
    		if(null!=list&&list.size()>0){
    			if(StringUtils.isNotBlank(userVo.getUserType()))//编辑的的话就显示 添加的时候是不显示
    				return false;
    			return true;
    		}
    			
    	}*/
    	return false;
		
	}
	public void getAllUser(){
    	//List<User> userList =  userService.listAll();
    	String id = getRequest().getParameter("id");
    	String userType =  UserTypeSuper.getSuperValue(id);
    	List<User> userList =  userService.getUserByUserType(userType);
    	List list = new ArrayList();
    	for (User user : userList) {
    		Map<String, Object>  map = new HashMap<>();
    		map.put("id", user.getUserId());
    	//	map.put("text", user.getUserName()+"("+com.hori.enums.UserType.getName(user.getUserType())+")");
    		list.add(map);
		}
    	AjaxJsonAndXmlUtil.writeJson(JSONArray.fromObject(list).toString(), getResponse());
    }
    
    /**
     * 保存用户
     */
    public void saveUser(){
    	//int aa =1/0;
    	try {
    		String error = "";
    		//Thread.sleep(10000);
        	//验证前端
        	error = verificationSaveUser(userVo,SAVETYPE);
        	if(StringUtils.isNotBlank(error)){
        		writeJson(error);
        		return ;
        	}
			
        	
        	
        	User user = new User();
        	user.setUserAccount(userVo.getPhone());
        	/*user.setPhone(userVo.getPhone());
        	User isUser =  userService.getUserByAccount(userVo.getPhone());
        	if(null!=isUser){
        		error="该账号已经存在！";
        		writeJson(error);
        		return ;
        	}
        	User parentUser  = userService.getUserById(userVo.getParentId());
        	if(null!=parentUser){
        		user.setLevelNo(userService.createUniqueLevelNo(parentUser.getLevelNo()));
        		user.setParentId(parentUser.getId());
        	}else {
        		if(userVo.getUserType().equals(com.hori.enums.UserType.GGGLY.getValue())){
        			user.setLevelNo(userService.createUniqueLevelNo("000"));
        		}else{
        			List<User> list =  userService.getUserByUserType(com.hori.enums.UserType.GGGLY.getValue());
        			if(null!=list&&list.size()>0){
        				user.setLevelNo(userService.createUniqueLevelNo(list.get(0).getLevelNo()));
        			}else{
        				user.setLevelNo(userService.createUniqueLevelNo("000"));
        			}
        				
        		}
        		
        	}
        	
        	user.setUserType(userVo.getUserType());
        	user.setPassword(Encrypt.e(userVo.getPassword()).toUpperCase());
        	user.setUserName(userVo.getUserName());
        	user.setRoleId(userVo.getRoleId());
        	user.setCreateTime(new Date());*/
        	//user.setStatus(0);
        	logger.info("------------------------"+user.toString());
        	userService.saveUser(user);
        	error = "success";
        	writeJson(error);
        	//AjaxJsonAndXmlUtil.writeJson(JSONObject.fromObject(error).toString(), getResponse());
		} catch (Exception e) {
			//AjaxJsonAndXmlUtil.writeJson(JSONObject.fromObject("网络出现异常!请联系管理员").toString(), getResponse());
			writeJson("网络出现异常!请联系管理员");
			e.printStackTrace();
		}
    	
    	
    }
    /**
     * 编辑用户
     */
    public void editUser(){
    	
    	try {
    		String error = "";
    		//验证前端
    		error = verificationSaveUser(userVo,EDITTYPE);
    		if(StringUtils.isNotBlank(error)){
    			writeJson(error);
    			return ;
    		}
    		User isUser =  userService.getUserByAccount(userVo.getPhone());
    		if(null!=isUser){
    			if(!(userVo.getId().equals(isUser.getUserId()))){
    				error="该账号已经存在！";
        			writeJson(error);
        			return ;
    			}
    		}
    		User user = userService.getUserById(userVo.getId());
    		
    		/*User parentUser  = userService.getUserById(userVo.getParentId());
    		if(null!=parentUser&&(!parentUser.getUserAccount().equals("admin"))){
    			if(!user.getLevelNo().contains(parentUser.getLevelNo()))
    			user.setLevelNo(userService.createUniqueLevelNo(parentUser.getLevelNo()));
    		}*//*else{
    			if (!userVo.getUserType().equals(com.hori.enums.UserType.XTCJGLY.getValue())) {
    				if(!userVo.getUserType().equals(com.hori.enums.UserType.GGGLY.getValue())){
            			user.setLevelNo(userService.createUniqueLevelNo("000"));
            		}else{
            			List<User> list =  userService.getUserByUserType(com.hori.enums.UserType.GGGLY.getValue());
            			if(null!=list&&list.size()>0){
            				user.setLevelNo(userService.createUniqueLevelNo(list.get(0).getLevelNo()));
            			}else{
            				user.setLevelNo(userService.createUniqueLevelNo("000"));
            			}
            				
            		}
				}
    			
    		}*/
    		
    		
    		/*user.setParentId(userVo.getParentId());
    		user.setUserType(userVo.getUserType());
    		user.setUserName(userVo.getUserName());*/
/*    		user.setRoleId(userVo.getRoleId());
*/    		logger.info("------------------------"+user.toString());
    		userService.update(user);
    		error = "success";
    		writeJson(error);
    	} catch (Exception e) {
    		writeJson("网络出现异常!请联系管理员");
    		e.printStackTrace();
    	}
    	
    	
    }
    
    /**
     * 验证表单
     * @param userVo
     * @param type
     * @return
     */
	private String verificationSaveUser(UserVo userVo,String type) {
		String error = "";
		if(StringUtils.isBlank(userVo.getPhone())){
			error="手机号码不能为空！";
			return error;
		}
		if(StringUtils.isBlank(userVo.getUserName())){
			error="姓名不能为空！";
			return error;
		}
		if(type.equals(SAVETYPE)){
			if(StringUtils.isBlank(userVo.getPassword())){
				error="密码不能为空！";
				return error;
			}
			if(StringUtils.isBlank(userVo.getResetPassword())){
				error="确认密码不能为空！";
				return error;
			}
			if(!userVo.getResetPassword().equals(userVo.getPassword())){
				error="两次输入密码不一致！";
				return error;
			}
			if(StringUtils.isBlank(userVo.getRoleId())){
				error="请选择角色！";
				return error;
			}
		}
		
		
		if(StringUtils.isBlank(userVo.getUserType())){
			error="请选择用户类型！";
			return error;
		}
		
		/*if(StringUtils.isBlank(userVo.getParentId())){
			error="请选择所属上级！";
			return error;
		}*/
		return error;
	}
	
	/**
	 * 点击编辑密码……
	 * @return
	 */
	public String  editPwd(){
		String id=(String) getRequest().getSession().getAttribute("userId");
		getRequest().setAttribute("id", id);
		return "systemnosuper";
	}
	
	/**
	 * 跳转人员管理页面
	 * @return
	 */
	public String goUserManagementList(){
		return "userManagement";
	}
	
	/**
	 * 根据参数分页查询人员管理页面
	 * @return
	 */
	public void getUserManagementByDepartParam(){
		String userManagementQueryBean=getRequest().getParameter("userManagementQueryBean");
		String newStr = userManagementQueryBean.replaceAll("“","\"");
		UserManagementQueryBean data=JSON.parseObject(newStr, UserManagementQueryBean.class); 
		HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userType=(String)request.getSession().getAttribute("userType");
		
		data.setSystemId(systemId);
		//从session里面获取登录机构
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
        //数据域（0表示个人数据，1表示部门数据，2表示全局数据）
		byte dataArea=(byte)request.getSession().getAttribute("dataArea");
		//数据域下的用户范围
		List<String> userList=(List<String>)request.getSession().getAttribute("userList");
		DataGridPage dataGridPage=userService.findUserManagementByParam(data,userType,dataArea,userList);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataGridPage);
		writeJson(j);
	}
	/**
	 * 根据查询用户角色
	 * @return
	 */
	public void getRole(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userAccount=(String)request.getSession().getAttribute("userAccount");
		byte dataArea=(byte)request.getSession().getAttribute("dataArea");
		List<String> userList=(List<String>)request.getSession().getAttribute("userList");
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		List<SelectVo> selectVoList=userService.roleSelect(systemId, userDetailVo.getDepartId(),userAccount,dataArea,userList);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(selectVoList);
		writeJson(j);

	}
	
	/**
	 * 根据部门id获取人员信息
	 * @return
	 */
	public void getUserDetail(){
		/*HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("systemId");
		String departId = (String)request.getSession().getAttribute("departId");
		//暂时写死
*/		String departId=getRequest().getParameter("departId");

		List<SelectVo> selectVoList=userService.getUserDetailById(departId);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(selectVoList);
		writeJson(j);

	}
	/**
	 * 保存或者修改帐号信息
	 * @return
	 */
	public void saveOrUpdateAccount(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		List<String> userListSession=(List<String>) request.getSession().getAttribute("userList");
		String addUserVo=getRequest().getParameter("addUserVo");
		String newStr = addUserVo.replaceAll("“","\"");
		AddUserVo data=JSON.parseObject(newStr, AddUserVo.class); 
		data.setSystemId(systemId);
		Map<String,Object> map=new HashMap<String,Object>();
	    try {
			 map=userService.saveOrUpdateAccount(data);
			 List<String> userList=(List<String>) map.get("userAccountList");
			 if(userList!=null){
				 userListSession.addAll(userList);
				 request.getSession().setAttribute("userList",userListSession);
			 }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Json j = new Json();

	    if(map.get("status").equals("0")){
			j.setSuccess(true);
			j.setMsg("sucess");

	    }else{
			j.setSuccess(false);
			j.setMsg((String)map.get("msg"));

	    }
		writeJson(j);
		String operationModule = "人员管理-帐号管理";
		String operationType = "新增或者修改";
	}
	/**
	 * 删除当前系统帐号关联角色
	 * @return
	 */
	public void deleteAccountRole(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String ids=getRequest().getParameter("ids"); 
		String[] idsArray= ids.split("'");
		Map<String,String> map= userService.deleteAccountRole(idsArray,systemId);
		Json j = new Json();
		if(map.get("status").equals("0")){
			j.setSuccess(true);
			j.setMsg("sucess");
		}else{
			j.setSuccess(false);
			j.setMsg(map.get("msg"));
		}
	
		writeJson(j);
		String operationModule = "人员管理-帐号管理";
		String operationType = "删除";
	}
}
