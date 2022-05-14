package com.hori.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.hori.dao.queryBean.RoleQueryBean;
import com.hori.dao.queryBean.UserManagementQueryBean;
import com.hori.db.support.DataGridPage;
import com.hori.model.Auth;
import com.hori.model.Role;
import com.hori.model.RoleAuth;
import com.hori.model.User;
import com.hori.pageModel.DataGrid;
import com.hori.pageModel.Json;
import com.hori.service.AuthService;
import com.hori.service.RoleAuthService;
import com.hori.service.RoleService;
import com.hori.service.UserService;
import com.hori.utils.AjaxJsonAndXmlUtil;
import com.hori.vo.EasyUiTreeVo;
import com.hori.vo.RoleVo;
import com.hori.vo.UserDetailLoginVo;
import com.hori.vo.UserVo;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;

/**
 * @description 角色ACTION
 * @date 2017年5月26日 下午 8:05:30
 * @author hhb
 * 
 */
@Action(value = "roleAction", results = { @Result(name = "role", location = "/peopleManagement/roleManagement.jsp")
                                        })
public class RoleAction extends BaseAction implements ModelDriven<RoleVo> {

	private static final Logger logger = Logger.getLogger(RoleAction.class);

	private RoleVo roleVo ;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AuthService authService;
	@Autowired
	private UserService userService;
	HttpServletRequest request = ServletActionContext.getRequest();
	public RoleVo getModel() {
		if(null==roleVo) roleVo= new RoleVo();
		return roleVo;
	}
	/**
	 * 根据参数分页查询角色管理页面
	 * @return
	 */
	public void getRoleManagementParam(){
		String roleQueryBean=getRequest().getParameter("roleQueryBean");
		String newStr = roleQueryBean.replaceAll("“","\"");
		RoleQueryBean data=JSON.parseObject(newStr, RoleQueryBean.class); 
		//从session里面获取登录机构
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		data.setDepartId(userDetailVo.getDepartId());
		HttpServletRequest request = ServletActionContext.getRequest();
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userAccount = (String)request.getSession().getAttribute("userAccount");
		//用户类型
		String userType = (String)request.getSession().getAttribute("userType");
        //数据域（0表示个人数据，1表示部门数据，2表示全局数据）
		byte dataArea=(byte)request.getSession().getAttribute("dataArea");
		//数据域下的用户范围
		List<String> userList=(List<String>)request.getSession().getAttribute("userList");
		data.setSystemId(systemId);
		data.setUserAccount(userAccount);
		DataGridPage dataGridPage=roleService.findRoleByParam(data,userType,dataArea,userList);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataGridPage);
		writeJson(j);
	}
	//初始化页面
	public void initUserRole(){
		String systemId =(String) request.getSession().getAttribute("selected_platform");
		List<Object> dataList = roleService.initRoleBySystemId(systemId);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataList);
		writeJson(j);
	}
	
	//初始化对应系统菜单
	public void initSystemMenu(){
		//String systemId = getRequest().getParameter("systemId");
		String systemId = (String) request.getSession().getAttribute("selected_platform");
		String type=getRequest().getParameter("type");
		String menuApp=getRequest().getParameter("menuApp");
		List<EasyUiTreeVo> dataList;
		String userType = (String)request.getSession().getAttribute("userType");
		String userAccount = (String)request.getSession().getAttribute("userAccount");
		//管理员
		if(type.equals("add")&&userType.equals("0")){
			dataList = roleService.initSystemMenu(systemId,menuApp);
		}else if(userType.equals("0")){
			String roleId = getRequest().getParameter("roleId");
            
			dataList =roleService.initSystemMenuAlter(systemId,roleId,menuApp);
		}else if(type.equals("add")){
			dataList = roleService.initSystemMenuPeople(systemId,menuApp,userAccount);

		}else {
			String roleId = getRequest().getParameter("roleId");
            
			dataList =roleService.initSystemMenuAlterPeople(systemId,roleId,menuApp,userAccount);
		}
        

		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataList);
		writeJson(j);
	}
	
	//初始化对应系统菜单(针对app部分按钮需要不展示)
	public void initSystemMenuApp(){
		//String systemId = getRequest().getParameter("systemId");
		String systemId = (String) request.getSession().getAttribute("selected_platform");
		String type=getRequest().getParameter("type");
		String menuApp=getRequest().getParameter("menuApp");
		String menuType=getRequest().getParameter("menuType");
		List<EasyUiTreeVo> dataList;
		if(type.equals("add")){
			dataList = roleService.initSystemMenuApp(systemId,menuApp,menuType);
		}else{
			String roleId = getRequest().getParameter("roleId");
            
			dataList =roleService.initSystemMenuForApp(systemId,roleId,menuApp,menuType);
		}
        

		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataList);
		writeJson(j);
	}
	
	//创建或修改角色对象
	public void addSystemMenu(){
		String useraccount=(String) request.getSession().getAttribute("userAccount");
		String systemId=(String) request.getSession().getAttribute("selected_platform");
		String nodesWeb = getRequest().getParameter("nodesWeb");
		String newStrWeb = nodesWeb.replaceAll("“","\"");
		
		List<EasyUiTreeVo> dataListWeb=JSON.parseArray(newStrWeb, EasyUiTreeVo.class);  
	/*	String nodesApp = getRequest().getParameter("nodesApp");
		String newStrApp = nodesApp.replaceAll("“","\"");
		List<EasyUiTreeVo> dataListApp=JSON.parseArray(newStrApp, EasyUiTreeVo.class); 
		//合并数组
		dataListWeb.addAll(dataListApp);*/
		String roleVoPrarm = getRequest().getParameter("roleVo");
		String newStr2 = roleVoPrarm.replaceAll("“","\"");
		Role roleVo=JSON.parseObject(newStr2, Role.class);  
		roleVo.setSystemId(systemId);
		roleVo.setCreateAccount(useraccount);
        //角色名称不能重复校验
		int status=0;
       if(StringUtils.isNotBlank(roleVo.getRoleId())){
    	    if(!roleService.checkForRoleName(roleVo.getRoleName(), roleVo.getRoleId(), systemId)){
    	    	roleService.updateSystemRole(dataListWeb,roleVo);
    	    }else{
    	    	status=1;
    	    }

       }else{
    	   if(!roleService.checkForRoleName(roleVo.getRoleName(),"", systemId)){
    		    roleVo.setCreateAccount(useraccount);
       			roleService.createSystemRole(dataListWeb,roleVo);
       	  }else{
   	    	status=1;
   	      }
       }
		Json j = new Json();

        if(status==0){
        	j.setSuccess(true);
    		j.setMsg("sucess");
        }else{
        	j.setSuccess(false);
    		j.setMsg("角色重复，请重新命名");
        }
		
		writeJson(j);
		String operationModule = "人员管理-角色管理";
		String operationType = "新增或者修改";
	}
	//删除角色
	
	public void deleteRoleById(){
		String systemId=(String) request.getSession().getAttribute("selected_platform");
		String roleId=getRequest().getParameter("roleId");
		int i=roleService.delSystemRole(roleId);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(i);
		writeJson(j);
		String operationModule = "人员管理-角色管理";
		String operationType = "删除";
		
	}
	
	/**
	 * 验证是否存在广告管理员和系统管理员
	 * @param userType 角色里的用户类型
	 * @param type 用户的类型
	 * @return
	 */
	private boolean verificationgetUserIsExistAdminAndADSSYS(String userType,String type) {
		
		
		if((userType.equals(com.hori.enums.UserType.GGGLY.getValue()))||userType.equals(com.hori.enums.UserType.XTCJGLY.getValue())){
			if(StringUtils.isNotBlank(type)){//编辑的时候要显示
				if(userType.equals(type))
					return false;
			}
			//查找是否存在广告管理员这个角色或者admin 存在了就不显示
    		List<User> list =  userService.getUserByUserType(userType);
    		if(null!=list&&list.size()>0){
    			return true;
    		}
    			
    	}
    	return false;
	}

	/**
	 * 调转到权限和角色
	 * @return
	 */
	public String goRoleList(){
		return "role";
	}
	
	/**
	 * 跳转到添加角色的页面
	 * @return
	 */
	public String goAddRole(){
		List<Auth> authsList = authService.getTopMenu();
		getRequest().setAttribute("authsList", authsList);
		return "addrole";
	}
	


	
	public boolean equalList(List list1, List list2) {
        if (list1.size() != list2.size())
            return false;
        if (list2.containsAll(list1))
            return true;
        return false;
     }

	

}
