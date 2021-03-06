package com.hori.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.dao.AreaAuthorizeDao;
import com.hori.dao.AuthorDao;
import com.hori.dao.RoleDao;
import com.hori.dao.UserDao;
import com.hori.dao.UserDetailDao;
import com.hori.dao.UserRoleDao;
import com.hori.dao.queryBean.UserManagementQueryBean;
import com.hori.dao.queryBean.UserQueryBean;
import com.hori.db.support.DataGridPage;
import com.hori.db.support.PageBean;
import com.hori.model.Auth;
import com.hori.model.Author;
import com.hori.model.AuthorizeArea;
import com.hori.model.Role;
import com.hori.model.User;
import com.hori.model.UserDetail;
import com.hori.model.UserRole;
import com.hori.pageModel.DataGrid;
import com.hori.service.RedisCacheService;
import com.hori.service.UserService;
import com.hori.service.XmppAdvertisementService;
import com.hori.ums.webservice.bean.UserDto;
import com.hori.util.Encrypt;
import com.hori.utils.StaticValue;
import com.hori.utils.StringUrit;
import com.hori.vo.AddUserVo;
import com.hori.vo.AuthVo;
import com.hori.vo.LoginForSystemVo;
import com.hori.vo.OfUserVo;
import com.hori.vo.SelectVo;
import com.hori.vo.UserDetailVo;
import com.hori.vo.UserInfoDto;
import com.hori.vo.UserVo;
import com.jlit.vdcs.webservice.IWorkOrderService;



/**
 * 用户Service
 * 
 * @author 
 * 
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl  implements UserService {
	private final static Logger logger=Logger.getLogger(UserServiceImpl.class);
	/**
	 * 当天连续输入3密码错误后登录需要验证码
	 */
	private int passErrerTimsLimitVevifiCodeNeed= StringUtils.isNotBlank(StaticValue.MSGERRENUM)?Integer.parseInt(StaticValue.MSGERRENUM):3;
	private int getCodeByMoblieTimsLimitVevifiCodeNeed= 0;//StringUtils.isNotBlank(StaticValue.CODEERRENUM)?Integer.parseInt(StaticValue.CODEERRENUM):3;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDetailDao userDetailDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private AuthorDao authorDao;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private IWorkOrderService iWorkOrderService;
	

	public List<User> listAll(){
	    return  this.userDao.listAll();
	}
	
	/**
	 * 用户登录
	 */
	/*@Transactional(propagation = Propagation.SUPPORTS)*/
	public User login(String userAccount,String password) {
		return this.userDao.login(userAccount,password);
	}


	
	private List<UserVo> getUsersFromTusers(List<User> users) {
		List<UserVo> userVos = new ArrayList<UserVo>();
		//重新去匹配 简单的排序匹配
		List<Role> roleList =  roleDao.findAllRole();
		if (users != null && users.size() > 0) {
			for (User tu : users) {
				UserVo u = new UserVo();
				BeanUtils.copyProperties(tu, u);
                //找出
				//Set<Tusertrole> tusertroles = tu.getTusertroles();
				
				//取角色名称
				//Role  role = roleDao.get(tu.getRoleId());
				
		/*		User parentUser =  userDao.getById(tu.getParentId());
				u.setParentName(parentUser.getUserName());
				u.setUserTypeName(com.hori.enums.UserType.getName(u.getUserType()));
				for (Role role : roleList) {
					if(role.getId().equals(tu.getRoleId())){
						if (StringUtils.isBlank(tu.getRoleId())) {
							u.setRoleIds("");
							u.setRoleNames("");
						} else {
							u.setRoleIds(tu.getRoleId());
							u.setRoleNames(role.getName());
						}
					}
				}
				if (StringUtils.isBlank(tu.getRoleId())) {
					u.setRoleIds("");
					u.setRoleNames("");
				} else {
					u.setRoleIds(tu.getRoleId());
					u.setRoleNames(role.getName());
				}*/
				

				userVos.add(u);
			}
		}
		return userVos;
	}
	private List<UserVo> getBulidPage(List<UserVo> userVos) {
		List<UserVo> list = new ArrayList<UserVo>();
		//重新去匹配 简单的排序匹配  为了后面的扩产先这样不去关联查询
		List<Role> roleList =  roleDao.findAllRole();
		List<User> users =  userDao.listAll();
		if (userVos != null && userVos.size() > 0) {
			for (UserVo u : userVos) {//
				/*if(u.getUserType().equals(UserType.XTCJGLY.getValue()))
					continue;*/
				//Role  role = roleDao.get(u.getRoleId());
				if(StringUtils.isNotBlank(u.getParentId()))
				{   
					/*User parentUser =  userDao.getById(u.getParentId());
					if(null!=parentUser)
					u.setParentName(parentUser.getUserName());*/
					for (User user : users) {
						/*if(user.getId().equals(u.getParentId()))
							u.setParentName(user.getUserName());*/
					}
				}
				u.setUserTypeName(com.hori.enums.UserType.getName(u.getUserType()));;
				
				/*for (Role role : roleList) {
					if(role.getId().equals(u.getRoleId())){
						if (StringUtils.isBlank(u.getRoleId())) {
							u.setRoleIds("");
							u.setRoleNames("");
						} else {
							u.setRoleIds(u.getRoleId());
							u.setRoleNames(role.getName());
						}
					}
				}*/
				/*if (StringUtils.isBlank(u.getRoleId())) {
					u.setRoleIds("");
					u.setRoleNames("");
				} else {
					u.setRoleIds(u.getRoleId());
					u.setRoleNames(role.getName());
				}*/
				list.add(u);
			}
		}
		return list;
	}


	private Long total(UserQueryBean queryBean) {
		return userDao.total(queryBean);
	}

	/**
	 * 获得用户下拉列表
	 */
	/*@Transactional(propagation = Propagation.SUPPORTS)*/
	public List<UserVo> loginCombobox(String q ) {
	
		if (StringUtils.isNotBlank(q)) {
			q = q.trim();
		}
		List<Object> values = new ArrayList<Object>();
		values.add("%%" + q.trim() + "%%");
		return getUsersFromTusers(userDao.find("from User t where t.cname like ?", 1, 10, values));
	}

	
	

	
	

	

	//获取用户权限
	public List<Author> getAuthVos(User u) {
	    List<Author> menuList = new ArrayList<Author>();
		
		if (u != null) {
			List<Author> authorVos = authorDao.getAuthByUserID(u.getUserId());
			  // 查看结果
		    for (Author menu : authorVos) {
		        System.out.println(menu);
		    }
		    // 最后的结果
		    // 先找到所有的一级菜单
		    for (int i = 0; i < authorVos.size(); i++) {
		        // 一级菜单没有parentId
		        if (StringUtils.isBlank(authorVos.get(i).getResourceParentId())) {
		            menuList.add(authorVos.get(i));
		        }
		    }
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (Author menu : menuList) {
		        menu.setAuthVoList(getChild(menu.getResourceId(), authorVos));
		    }
		    Map<String,Object> jsonMap = new HashMap<>();
		    jsonMap.put("menu", menuList);
		  //  System.out.println(gson.toJson(jsonMap));
			
			
		}
		return menuList;
	}
	/**
	 * 递归查找子菜单
	 * 
	 * @param id
	 *            当前菜单id
	 * @param rootMenu
	 *            要查找的列表
	 * @return
	 */
	private List<Author> getChild(String id, List<Author> rootMenu) {
	    // 子菜单
	    List<Author> childList = new ArrayList<>();
	    for (Author menu : rootMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getResourceParentId())) {
	            if (menu.getResourceParentId().equals(id)) {
	                childList.add(menu);
	            }
	        }
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (Author menu : childList) {// 没有url子菜单还有子菜单
	        if (StringUtils.isBlank(menu.getResourceUrl())) {
	            // 递归
	            menu.setAuthVoList(getChild(menu.getResourceId(), rootMenu));
	        }
	    } // 递归退出条件
	    if (childList.size() == 0) {
	        return null;
	    }
	    return childList;
	}
	
	private AuthVo buildSetAuthList(AuthVo authVo,User u,List<Auth> auths) {
		// TODO Auto-generated method stub
		//List<Auth> authVoList =  authDao.getAuthByPid(authVo.getId());
		List<AuthVo> authVos = new ArrayList<AuthVo>();
		/*for (Auth auth : authVoList) {
			if(validateIsAddLevelMenu(auth,u))
				continue;
			AuthVo a = authChangeToAuthVo(auth);
			authVos.add(a);
			
		}*/
		
		for (Auth auth : auths) {
			if(null==auth.getAuth())
				continue;
			if(authVo.getId().equals(auth.getAuth().getId())){
				if(validateIsAddLevelMenu(auth,u))
					continue;
				AuthVo a = authChangeToAuthVo(auth);
				authVos.add(a);
			}
		}
		
		authVo.setAuthList(authVos);
		return authVo;
	}

	/**
	 * 因为还没有角色控制二级菜单所以先用代码控制  why？ 这个问产品
	 * @param auth
	 * @param u
	 */
	private boolean validateIsAddLevelMenu(Auth auth, User u) {
		/*String userType = RoleUNTowLevelMenu.getUserType(auth.getId());
		if(StringUtils.isNotBlank(userType)&&userType.equals(u.getUserType())){
			return true;
		}*/
	/*	if(RoleUNTowLevelMenu.isExistMenu(u.getUserType(), auth.getId())){
			return true;
		}*/
		return false;
	}

	/**
	 * 组装权限 add Author:gaoxingang  Date:20130308 
	 * @param tauth
	 * @return
	 */
	private AuthVo wrapAuth(Auth auth) {
		AuthVo a = new AuthVo();
		a.setId(auth.getId());
		a.setCname(auth.getCname());
		a.setCurl(auth.getCurl());
		a.setCiconCls(auth.getCiconcls());
		if (auth.getAuth() != null) {
			a.setCpid(auth.getAuth().getId());
			a.setCpname(auth.getAuth().getCname());
		}
		List<AuthVo> authVoList = new ArrayList<>();
		 
		for (Auth auth2 : auth.getAuths()) {
			authVoList.add(wrapAuth(auth2));
		}
		a.setAuthList(authVoList);
		return a;
	}
	
	/**
	 * 组装权限 add Author:gaoxingang  Date:20130308 
	 * @param tauth
	 * @return
	 */
	private AuthVo authChangeToAuthVo(Auth auth) {
		AuthVo a = new AuthVo();
		a.setId(auth.getId());
		a.setCname(auth.getCname());
		a.setCurl(auth.getCurl());
		a.setCiconCls(auth.getCiconcls());
		if (auth.getAuth() != null) {
			a.setCpid(auth.getAuth().getId());
			a.setCpname(auth.getAuth().getCname());
		}
		return a;
	}
	

	
	
	/**
	 * 判断当前权限集合是否包含某个权限 add Author:gaoxingang  Date:20130308 
	 * @param auths
	 * @param a
	 * @return
	 */
	private static boolean isAuthContains(List<Auth> auths, Auth a) {
		boolean exist = false;
		for (Auth _auth : auths) {
			if (_auth.getId().equals(a.getId())) {
				exist = true;
				break;
			}
		    else {
		    	continue;
		    }
		}
		return exist;
	}
	

	@Override
	public int getPassErrerTimsLimitVevifiCodeNeed() {
		logger.info("---------------------------------"+passErrerTimsLimitVevifiCodeNeed);
		return passErrerTimsLimitVevifiCodeNeed;
	}


	

	
	
	@Override
	public User getUserByAccount(String userAccount) {
		// TODO Auto-generated method stub
		return this.userDao.getUserByAccount(userAccount);
	}

	@Override
	public int getCodeByMoblieTimsLimitVevifiCodeNeed() {
		// TODO Auto-generated method stub
		return getCodeByMoblieTimsLimitVevifiCodeNeed;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		userDao.update(user);
	}


	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		try {
			User user =  this.userDao.get(id);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		this.userDao.save(user);
	}


	/**
	 * 生成唯一的用户等级 (递归公式)
	 */
	public String createUniqueLevelNo(String levelNo){
		
		String userLevelNo = (StringUtils.isBlank(levelNo)?"":levelNo)+StringUrit.getString(3);
		if(isUniqueLevelNo(userLevelNo)){
			createUniqueLevelNo(levelNo);
		}
		return userLevelNo;
	}


	@Override
	public List<User> getUserByUserType(String userType) {
		// TODO Auto-generated method stub
		return this.userDao.getUserByUserType(userType);
	}

	@Override
	public List<User> getSYSAndADSSYS() {
		// TODO Auto-generated method stub
		return this.userDao.getSYSAndADSSYS();
	}
	/**
	 * 根据参数分页查询人员管理页面
	 * @return
	 */
	public DataGridPage findUserManagementByParam(UserManagementQueryBean queryBean,String userType,byte dataArea,List<String> userList){
				
		DataGridPage dataGridPage=userDao.findUserManagementPage(queryBean,userType,dataArea,userList);
		return dataGridPage;
	}
	
	@Override
	public DataGridPage queryUsers(UserQueryBean queryBean) {
		
		DataGridPage dataGridPage = userDao.queryUsers(queryBean);
		
		List<UserDetailVo> rows = dataGridPage.getRows();
		
		List<UserDto> dtos=vosToDtos(rows);
		
		dataGridPage.setRows(dtos);
		
		return dataGridPage;
	}

	@Override
	public List<UserDto> getUsersByIds(String[] userIds) {
		// TODO Auto-generated method stub
		
		List<UserDetailVo> vos=userDao.getUsersByIds(userIds);
		
		List<UserDto> dtos=vosToDtos(vos);
		
		return dtos;
	}
	
	@Override
	public UserDto getUserDetailInfoById(String userId){
		
		UserDetailVo vo=userDao.getUserById(userId);
		
		UserDto dto=new UserDto(vo.getUserId(), vo.getUserAccount(), vo.getName(), 
				vo.getMobile(), vo.getRole(), vo.getDepartment());
		
		dto.setCountrys(vo.getCountrys());
		
		return dto;
	}
	

	@Override
	public UserDto getUserDetailInfoByMobile(String mobile) {
		
		UserDetailVo vo=userDao.getUserByMobile(mobile);
		
		if(vo==null){
			return null;
		}
		
		UserDto dto=new UserDto(vo.getUserId(), vo.getUserAccount(), vo.getName(), 
				vo.getMobile(), vo.getRole(), vo.getDepartment());
		
		dto.setCountrys(vo.getCountrys());
		dto.setPassword(vo.getPassword());
		return dto;
	}
	
	
	private List<UserDto> vosToDtos(List<UserDetailVo> vos){
		
		List<UserDto> dtos=new ArrayList<UserDto>();
		
		for (UserDetailVo vo : vos) {
			UserDto dto=new UserDto(vo.getUserId(), vo.getUserAccount(), vo.getName(), 
					vo.getMobile(), vo.getRole(), vo.getDepartment());
			dtos.add(dto);
		}
		
		return dtos;
	}

	@Override
	public List<String> getUserIds(UserQueryBean userQueryBean) {
		return userDao.getUserIds(userQueryBean);
	}

	/**
	 * 当前账号及下级账号创建的角色
	 * @return
	 */
	public List<SelectVo> roleSelect(String systemId,String departId,String userAccount,byte dataArea,List<String> userList){
		
		String userAccountIds="";
		//查找出当前帐号的下级帐号
        for(int i=0;i<userList.size();i++){
        	    if(i==0){
        	    	userAccountIds="'"+userAccountIds+"'";
        	    }else{
            		userAccountIds=userAccountIds+",'"+userList.get(i)+"'";

        	    }
        }
		List<SelectVo> selectVo=userDao.findRoleByAccount(systemId, userAccountIds,dataArea);
		
		return selectVo;
	}
	/**
	 * 根据部门Id获取信息
	 * @return
	 */
	public List<SelectVo> getUserDetailById(String departId){
		 List<SelectVo> selectVoList=new ArrayList<SelectVo>();
		 List<UserDetail> userDetailList=userDetailDao.getUserDetailById(departId);
		 for(int i=0;i<userDetailList.size();i++){
			 SelectVo selectVo=new SelectVo();
			 selectVo.setId(userDetailList.get(i).getUserDetailId());
			 selectVo.setText(userDetailList.get(i).getName());
			 selectVoList.add(selectVo);
		 }
		
		return selectVoList;
	}
	
	/**
	 * 保存或者修改帐号信息
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws ClientProtocolException 
	 */
	public Map<String,Object> saveOrUpdateAccount(AddUserVo addUserVo) throws ClientProtocolException, DocumentException, IOException{
		
		User oldUser=userDao.getUserByAccount(addUserVo.getUserAccount());
		Map<String,Object> map=new HashMap<String,Object>();
		Role role=roleDao.getById(addUserVo.getRoleId());
		List<String> userList=new ArrayList<String>();
		if(oldUser==null){
			//创建新帐号
			//批量创建账号
			//0 成功  1用户名已经存在 2服务器其他异常 3该员工已经拥有账号
			int status=0;
			for(int i=0;i<addUserVo.getIds().size();i++){
				User user=new User();
				user.setUserDetailId(addUserVo.getIds().get(i));
				UserDetail userDetail=userDetailDao.getById(addUserVo.getIds().get(i));
				//判断当前人员是否已经拥有帐号
				if(userDao.isExistUserDetail(addUserVo.getIds().get(i))){
					map.put("status", "3");
					map.put("msg", userDetail.getName()+" 已拥有帐号，不能再创建");
					return map;
				}
			    user.setStatus((byte)1);
				user.setMobile(userDetail.getMobile());
				user.setDr("0");
				String userAccount=null;
				//验证帐号是否重复
				int temp=1;
				while(temp==1){
					userAccount=addUserVo.getUserAccount()+getFixLenthString(3);
					User oldUser1=userDao.getUserByAccount(userAccount);
					//同步帐号到openfire
				
					if((oldUser1==null)){
						temp=0;

		/*				//同步帐号到openfire
						OfUserVo ofUserVo=new OfUserVo();
						ofUserVo.setUserAccount(userAccount);
						ofUserVo.setPassword("888888");
						ofUserVo.setUserName(userDetail.getName());
						status=xmppAdvertisementService.addUser(ofUserVo, "888888");
						System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+status);
						if(status==0){
							temp=0;
						}
						if(status==2){
							throw new RuntimeException( "同步到openfire帐号异常" );
							map.put("status", "2");
							map.put("msg", "同步到openfire帐号异常");
							return map;
						}*/
					}
					
				}
				userList.add(userAccount);
				user.setUserAccount(userAccount);
				user.setUserType((byte)1);
				//密码默认888888
				user.setPassword("21218CCA77804D2BA1922C33E0151105");
				user.setCreateTime(new Date());
				user.setModifiedTime(new Date());
				user.setDataArea(addUserVo.getDataArea());
				userDao.save(user);
				//插入用户角色关联表
				
				UserRole userRole=new UserRole();
				userRole.setUserId(user.getUserId());
				userRole.setSystemId(addUserVo.getSystemId());
				userRole.setRoleId(addUserVo.getRoleId());
				userRole.setRoleName(addUserVo.getRoleName());
				userRole.setRoleType(role.getRoleType());
				userRole.setCreateTime(new Date());
				userRole.setModifiedTime(new Date());
				userRoleDao.save(userRole);
			
				
			}
		}else{
			//批量创建账号
			//0 成功  1用户名不存在 2服务器其他异常 3该员工已经拥有账号 
			int status=0;
			userDao.updateUserDetailById(oldUser.getUserId(),addUserVo.getDataArea(),addUserVo.getIds().get(0));
			UserDetail userDetail=userDetailDao.getById(addUserVo.getIds().get(0));
            //判断修改后员工是否拥有帐号
			if(userDao.isExistUserDetail(addUserVo.getIds().get(0),oldUser.getUserId())){
				map.put("status", "3");
				map.put("msg", userDetail.getName()+" 已拥有帐号，不能更换帐号");
				return map;
			}
			
			//插入用户角色关联表
			userRoleDao.deletUserRoleById(addUserVo.getSystemId(),oldUser.getUserId());
			UserRole userRole=new UserRole();
			userRole.setUserId(oldUser.getUserId());
			userRole.setSystemId(addUserVo.getSystemId());
			userRole.setRoleId(addUserVo.getRoleId());
			userRole.setRoleName(addUserVo.getRoleName());
			userRole.setRoleType(role.getRoleType());
			userRole.setCreateTime(new Date());
			userRole.setModifiedTime(new Date());
			userRoleDao.save(userRole);
			//
		/*	status=xmppAdvertisementService.updateUser(oldUser, "", userDetail.getName());
			if(status==1){
				map.put("status", "1");
                map.put("msg", "同步到openfire帐号异常，用户不存在");
			}*/
		}
		map.put("status", "0");
		map.put("userAccountList", userList);
		return map;
	}

	/* 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 */  
	private static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	}  
	
	/**
	 * 登录接口
	 * @return LoginForSystemVo
	 */
	public LoginForSystemVo loginForSystemByUms(String userAccount,String password,String systemId,String resourceApp){
		LoginForSystemVo loginForSystemVo=new LoginForSystemVo();
		User u=userDao.getUserByAccount(userAccount);
		String password1=null;
		if(u==null){
			loginForSystemVo.setResult("1");
			loginForSystemVo.setReason("用户不存在");
			return loginForSystemVo;
		}else{
			password1=Encrypt.e(u.getPassword()+userAccount).toUpperCase();

		} 
		if(u.getStatus()==0){
			loginForSystemVo.setResult("3");
			loginForSystemVo.setReason("用户为停用状态请联系管理员");
			return loginForSystemVo;
		}else if(!password.equals(password1)){
			loginForSystemVo.setResult("2");
			loginForSystemVo.setReason("密码不正确");
			return loginForSystemVo;
		}
		//成功返回
		loginForSystemVo.setResult("0");
		if(u.getUserType()==0){
			loginForSystemVo.setUserTypeName("超级管理员");
		}else if(u.getUserType()==1){
			loginForSystemVo.setUserTypeName("运维人员");
		}else if(u.getUserType()==2){
			loginForSystemVo.setUserTypeName("管理人员");

		}
		
		UserRole userRole=userRoleDao.getUserRole(systemId, u.getUserId());
		if(userRole!=null){
			loginForSystemVo.setRoleId(userRole.getRoleId());
			loginForSystemVo.setRoleName(userRole.getRoleName());
		}
		loginForSystemVo.setUserType(String.valueOf(u.getUserType()));
		//
		loginForSystemVo.setRoleType(userRole.getRoleType());
		loginForSystemVo.setDataArea(u.getDataArea());
		//生成token
		String token=Encrypt.e(u.getUserAccount()+new Date().getTime()).toUpperCase();
		loginForSystemVo.setToken(token);
		loginForSystemVo.setUserAccount(u.getUserAccount());
		UserDetail userDetail=userDetailDao.getUserDetailByOne(u.getUserDetailId());
		loginForSystemVo.setUserDetail(userDetail);
		
		//根据机构和数据域找到对应的可以操作的数据权限帐号
		if(u.getDataArea()==0){
			loginForSystemVo.setUserList(userDao.getUserAccountForOne(userDetail.getDepartId(), userAccount));
			loginForSystemVo.setDepartList(userDao.getDepartForOne(userDetail.getDepartId()));

		}else if(u.getDataArea()==1){
			loginForSystemVo.setUserList(userDao.getUserAccountForDepart(userDetail.getDepartId()));
			loginForSystemVo.setDepartList(userDao.getDepartForAll(userDetail.getDepartId()));

		}
		List<Author> resourceList=authorDao.getAuthByUserID(u.getUserId(), systemId,resourceApp);
		List<Author> resourceMenu1=new ArrayList<Author>();
		List<Author> resourceMenu2=new ArrayList<Author>();
		List<Author> resourceButton=new ArrayList<Author>();
		
		for(int i=0;i<resourceList.size();i++){
			if((!StringUtils.isNotBlank(resourceList.get(i).getResourceParentId()))&&resourceList.get(i).getResourseType()==0){
				resourceMenu1.add(resourceList.get(i));
			}else if(resourceList.get(i).getResourseType()==0){
				resourceMenu2.add(resourceList.get(i));
			}else if(resourceList.get(i).getResourseType()==1){
				resourceButton.add(resourceList.get(i));
			}
		}
		if(resourceApp.equals("0")){
			loginForSystemVo.setResourceMenu1(resourceMenu1);	
			loginForSystemVo.setResourceMenu2(resourceMenu2);
			loginForSystemVo.setResourceButton(resourceButton);
		}else{
			loginForSystemVo.setResourceButton(resourceButton);
		}
		//把token和帐号存在redis
		//token生存时间
/*		redisCacheService.set(token, u.getUserAccount(), 60*60*8);
*/
		return loginForSystemVo;
		
	}
	/**
	 * 修改绑定手机号码
	 * @return 
	 */
	
	public void alterMobile(User user,String mobile){
		userDao.updateUserMobileById(user, mobile);
	}

	@Override
	public UserInfoDto getUserInfoByAccount(String account) {
		// TODO Auto-generated method stub
		return userDao.getUserInfoByAccount(account);
	}
	
	/**
	 * 删除当前系统帐号
	 * @return 
	 */
	
	public Map<String,String> deleteAccountRole(String[] idsArray,String systemId){
		//判断当前帐号是否存在没有归结工单
		Map<String,String> map=new HashMap<String,String>();
		for(int i=0;i<idsArray.length;i++){
			User user=userDao.getUserByUserId(idsArray[i]);
			long number=iWorkOrderService.getWorkOrderNumByUserId(idsArray[i]);
			if(number!=0L){
				map.put("status", "1");
                map.put("msg", user.getUserAccount()+",还有没有归结的工单");
                return map;
			}else{
				map.put("status", "0");
			}
		}
		for(int i=0;i<idsArray.length;i++){
			userDao.deleteUserRole(idsArray[i], systemId);
		}
		return map;
	}
	

	/**
	 * 发送手机验证码
	 * @return 
	 */
	
	public Map<String,String> sendMessge(String userAccount){
		Map<String,String> map=new HashMap<String,String>();
		int result=0;
		User user=userDao.getUserByAccount(userAccount);
		String cacheCodeTimeKey = user.getMobile()+"|"+"cache|getCodeByMobileTimes";
		String codeTimesStr =  redisCacheService.get(cacheCodeTimeKey);
		int codeTimes =StringUtils.isBlank(codeTimesStr)?0:Integer.parseInt(codeTimesStr);
		if(codeTimes>=5){//
			result=1;//请稍后再点击
			map.put("success", "1");
			return map;
		}
		if(result==0){
			String randomNum = Math.random()*9000+1000+"";
			randomNum = randomNum.substring(0, 4);
			redisCacheService.incr(cacheCodeTimeKey,  60*3, TimeUnit.SECONDS);
			//重置验证码，以防复用

		
		
			String content = randomNum+"（修改手机号验证码），5分钟内有效，超时请重新申请。";
		
			logger.info("----------randomNum-------:::"+randomNum);
			String cacheCodeKey =user.getMobile()+"|"+"cache|getCodeByMobile";
			redisCacheService.del(cacheCodeKey);
			redisCacheService.set(cacheCodeKey, randomNum, 60*5);
			map.put("success", "0");
            map.put("mobile", user.getMobile());
            map.put("content", content);
		}
		return map;
	
	}
	
	/**
	 * 修改绑定手机
	 * @return 
	 */
	public Map<String,String> alterMobile(String userAccount,String code,String mobileNew){
		Map<String,String> map=new HashMap<String,String>();
		
		User user=userDao.getUserByAccount(userAccount);
		User user2=userDao.getUserByAccount(mobileNew);
		if(user2!=null){
			map.put("success", "2");
            return map;
		}
		String cacheCodeKeyNew = user.getMobile()+"|"+"cache|getCodeByMobile";
		String randomNumNew= redisCacheService.get(cacheCodeKeyNew);
		if(randomNumNew!=null&&randomNumNew.equals(code)){
			userDao.updateUserMobileById(user, mobileNew);
			map.put("success", "0");
			redisCacheService.del(cacheCodeKeyNew);
		}else{
			map.put("success", "1");
		}

		return map;
		
	}
	/**
	 * 修改密码
	 * @return 
	 */
	public Map<String,String> alterPassword(String userAccount,String passwordNew,String passwordOld){
		Map<String,String> map=new HashMap<String,String>();
		User user=userDao.getUserByAccount(userAccount);
		String passwordOldMd5=Encrypt.e(passwordOld).toUpperCase();
		//重置验证码，以防复用
		//修改密码为新密码
	    if(passwordOldMd5.equals(user.getPassword())){
	    	String passwd =Encrypt.e(passwordNew).toUpperCase();
	    	user.setPassword(passwd);
	    	userDao.update(user);
			
			/*try {
				int i=xmppAdvertisementService.updateUser(user, passwordNew, "");
				System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+i);
				if(i!=0){
					if(i!=0){
						throw new DocumentException("同步openfire错误");
					}
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
			}*/
		
	    	map.put("success", "0");
	    }else{
	    	map.put("success", "1");

	    }
	    return map;
		
	}
	/**
	 * 根据帐号获取用户信息
	 * @param userAccount
	 * @return
	 */
	public Map<String, String> findUserDetailByUserAccount(String userAccount) {
		List<Map<String,String>> list = userDao.findUserDetail("1", userAccount);
		if (list!=null&&list.size()>0) {
			Map<String, String> map = list.get(0);
			return map;
		}else {
			return null;
		}
	}
	@Override
	public DataGrid datagrid(UserQueryBean queryBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserQueryBean> getAllbyType(String levelNo, String accuont) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGridPage getAllbyType(String levelNo, String accuont, PageBean pb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(String id, int status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUniqueLevelNo(String levelNo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExistUserByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataGridPage findUserPage(UserQueryBean queryBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
