package com.hori.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hori.dao.AuthorDao;
import com.hori.dao.MenuDao;
import com.hori.dao.OperationDao;
import com.hori.dao.RoleDao;
import com.hori.dao.SystemDao;
import com.hori.dao.UserDao;
import com.hori.dao.UserRoleDao;
import com.hori.dao.queryBean.RoleQueryBean;
import com.hori.dao.queryBean.UserManagementQueryBean;
import com.hori.db.support.DataGridPage;
import com.hori.model.Author;
import com.hori.model.Department;
import com.hori.model.Menu;
import com.hori.model.Operation;
import com.hori.model.Role;
import com.hori.model.SystemAdmin;
import com.hori.model.User;
import com.hori.model.UserRole;
import com.hori.pageModel.DataGrid;
import com.hori.service.RoleService;
import com.hori.vo.EasyUiTreeVo;
import com.hori.vo.RoleVo;
import com.hori.vo.SelectVo;


/**
 * 角色Service
 * 
 * @author 
 * 
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
	private static final Log log = LogFactory.getLog(RoleServiceImpl.class);
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private SystemDao systemDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private OperationDao operationDao;
	@Autowired
	private AuthorDao authorDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private	UserDao userDao;
	/**
	 * 根据参数分页查询人员管理页面
	 * @return
	 */
	public DataGridPage findRoleByParam(RoleQueryBean queryBean,String userType,byte dataArea,List<String> userList){
		DataGridPage dataGridPage=new DataGridPage();
		String roleIds="";
		if(userType.equals("0")||dataArea==2){
			dataGridPage=this.roleDao.findRolePage(queryBean,roleIds,userType,dataArea);
		}else{
			/*//只返回该用户或者下级用户创建的角色
			String userAccountIds="'"+queryBean.getUserAccount()+"'";
			//查找出当前帐号的下级帐号
			List<Map<String, String>>  userAccountList=userDao.getUserAccountNext(queryBean.getDepartId());*/
			String userAccountIds="";
			for(int i=0;i<userList.size();i++){
				if(i==0){
					userAccountIds="'"+userList.get(i)+"'";
				}else{
					userAccountIds=userAccountIds+",'"+userList.get(i)+"'";
				}
			}
			List<SelectVo> selectVoList=userDao.findRoleByAccount(queryBean.getSystemId(), userAccountIds,dataArea);		
			for(int i=0;i<selectVoList.size();i++){
				if(i==0){
					roleIds="'"+selectVoList.get(i).getId()+"'";
				}else{
					roleIds=roleIds+",'"+selectVoList.get(i).getId()+"'";
				}
			}
			if(!roleIds.equals("")){
				dataGridPage=this.roleDao.findRolePage(queryBean,roleIds,userType,dataArea);
			}
		}
		return dataGridPage;
	}
	
	//初始化页面
	@Override
	public List<Object>  initRoleBySystemId(String systemId){
		// TODO Auto-generated method stub
		List<Object> data=new ArrayList<Object>();
		List<Role> roleList=this.roleDao.findRoleBySytemId(systemId);
		List<SystemAdmin> systemList=this.systemDao.getSystemStart();
		data.add(roleList);
		data.add(systemList);
		return data;
	}
	/**
	 * 初始化页面管理员
	 * @return
	 */
	 @Override
	 public List<EasyUiTreeVo> initSystemMenu(String systemId,String menuApp){
		List<Menu> menuList=menuDao.getMenuStart(systemId,menuApp);
		List<Operation> operation=operationDao.getMenuStart(systemId,menuApp);
		
	    List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVo(menuList,operation);
	    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
		  // 查看结果
	    for (EasyUiTreeVo menu : easyUiTreeVoList) {
	        System.out.println(menu);
	    }
	    // 最后的结果
	    // 先找到所有的一级菜单
	    for (int i = 0; i < easyUiTreeVoList.size(); i++) {
	        // 一级菜单没有parentId
	        if (StringUtils.isBlank(easyUiTreeVoList.get(i).getParentId())) {
	        	menuBtnList.add(easyUiTreeVoList.get(i));
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (EasyUiTreeVo menu : menuBtnList) {
	        menu.setChildren(getChild(menu.getId(), easyUiTreeVoList));
	    }
	/*    Map<String,Object> jsonMap = new HashMap<>();
	    jsonMap.put("menu", menuBtnList);*/
	   
	
		return menuBtnList;
		
	}
	 	/**
		 * 初始化菜单添加非管理员
		 * @return
		 */
	 public List<EasyUiTreeVo> initSystemMenuPeople(String systemId,String menuApp,String userAccount){
		 
		User u=userDao.getUserByAccount(userAccount);
		UserRole useRole=userRoleDao.getUserRole(systemId, u.getUserId());
		 int menuAppTemp=Integer.valueOf(menuApp);
	    List<Author> authorList=authorDao.getAuthByRoleID(useRole.getRoleId(),menuAppTemp);
	    List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVoPeople(authorList);
	    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
		  // 查看结果
	    for (EasyUiTreeVo menu : easyUiTreeVoList) {
	        System.out.println(menu);
	    }
	    // 最后的结果
	    // 先找到所有的一级菜单
	    for (int i = 0; i < easyUiTreeVoList.size(); i++) {
	        // 一级菜单没有parentId
	        if (StringUtils.isBlank(easyUiTreeVoList.get(i).getParentId())) {
	        	menuBtnList.add(easyUiTreeVoList.get(i));
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (EasyUiTreeVo menu : menuBtnList) {
	        menu.setChildren(getChild(menu.getId(), easyUiTreeVoList));
	    }
	/*    Map<String,Object> jsonMap = new HashMap<>();
	    jsonMap.put("menu", menuBtnList);*/
	   
	
		return menuBtnList;
		
	}
	 	/**
		 * 初始化菜单修改非管理员
		 * @return
		 */
		 public List<EasyUiTreeVo> initSystemMenuAlterPeople(String systemId,String roleId,String menuApp,String userAccount){
			 User u=userDao.getUserByAccount(userAccount);
			 UserRole useRole=userRoleDao.getUserRole(systemId, u.getUserId());
			 int menuAppTemp=Integer.valueOf(menuApp);
			 List<Author> authorListUser=authorDao.getAuthByRoleID(useRole.getRoleId(),menuAppTemp);
			 List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVoPeople(authorListUser);
		    //勾选当前角色已经拥有的权限
		    List<Author> authorList=authorDao.getAuthByRoleID(roleId,menuAppTemp);
		    List<EasyUiTreeVo> authorUiTreeVoList=authorTransEasyUiVo(easyUiTreeVoList,authorList);
		    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
			  // 查看结果
		    for (EasyUiTreeVo menu : authorUiTreeVoList) {
		        System.out.println(menu);
		    }
		    // 最后的结果
		    // 先找到所有的一级菜单
		    for (int i = 0; i < authorUiTreeVoList.size(); i++) {
		        // 一级菜单没有parentId
		        if (StringUtils.isBlank(authorUiTreeVoList.get(i).getParentId())) {
		        	menuBtnList.add(authorUiTreeVoList.get(i));
		        }
		    }
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (EasyUiTreeVo menu : menuBtnList) {
		        menu.setChildren(getChild(menu.getId(), authorUiTreeVoList));
		    }
		/*    Map<String,Object> jsonMap = new HashMap<>();
		    jsonMap.put("menu", menuBtnList);*/
		   
		
			return menuBtnList;
			
		}
		 
		 /**
		     * 初始化菜单修改管理员
			 * @return
			 */
	 @Override
	 public List<EasyUiTreeVo> initSystemMenuAlter(String systemId,String roleId,String menuApp){
		List<Menu> menuList=menuDao.getMenuStart(systemId,menuApp);
		List<Operation> operation=operationDao.getMenuStart(systemId,menuApp);
		//把按钮菜单转化为UiTree对象
		 int menuAppTemp=Integer.valueOf(menuApp);
	    List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVo(menuList,operation);
	    //勾选当前角色已经拥有的权限
	    List<Author> authorList=authorDao.getAuthByRoleID(roleId,menuAppTemp);
	    List<EasyUiTreeVo> authorUiTreeVoList=authorTransEasyUiVo(easyUiTreeVoList,authorList);
	    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
		  // 查看结果
	    for (EasyUiTreeVo menu : authorUiTreeVoList) {
	        System.out.println(menu);
	    }
	    // 最后的结果
	    // 先找到所有的一级菜单
	    for (int i = 0; i < authorUiTreeVoList.size(); i++) {
	        // 一级菜单没有parentId
	        if (StringUtils.isBlank(authorUiTreeVoList.get(i).getParentId())) {
	        	menuBtnList.add(authorUiTreeVoList.get(i));
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (EasyUiTreeVo menu : menuBtnList) {
	        menu.setChildren(getChild(menu.getId(), authorUiTreeVoList));
	    }
	/*    Map<String,Object> jsonMap = new HashMap<>();
	    jsonMap.put("menu", menuBtnList);*/
	   
	
		return menuBtnList;
		
	}
	 
	 //初始化菜单修改(针对App菜单调整)
	 /**
		 * 根据菜单类型返回按钮
		 * @param 
		 * @return
		 */ 
	 public List<EasyUiTreeVo> initSystemMenuForApp(String systemId,String roleId,String menuApp,String menuType){
		List<Menu> menuList=menuDao.getMenuStart(systemId,menuApp);
		List<Operation> operationAll=operationDao.getMenuStart(systemId,menuApp);
		//根据菜单类型返回按钮
		List<Operation> operation=getDealButton(operationAll,menuType);
		//把按钮菜单转化为UiTree对象
	    List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVo(menuList,operation);
	    //勾选当前角色已经拥有的权限
		 int menuAppTemp=Integer.valueOf(menuApp);

	    List<Author> authorList=authorDao.getAuthByRoleID(roleId,menuAppTemp);
	    List<EasyUiTreeVo> authorUiTreeVoList=authorTransEasyUiVo(easyUiTreeVoList,authorList);
	    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
		  // 查看结果
	    for (EasyUiTreeVo menu : authorUiTreeVoList) {
	        System.out.println(menu);
	    }
	    // 最后的结果
	    // 先找到所有的一级菜单
	    for (int i = 0; i < authorUiTreeVoList.size(); i++) {
	        // 一级菜单没有parentId
	        if (StringUtils.isBlank(authorUiTreeVoList.get(i).getParentId())) {
	        	menuBtnList.add(authorUiTreeVoList.get(i));
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (EasyUiTreeVo menu : menuBtnList) {
	        menu.setChildren(getChild(menu.getId(), authorUiTreeVoList));
	    }
	/*    Map<String,Object> jsonMap = new HashMap<>();
	    jsonMap.put("menu", menuBtnList);*/
	   
	
		return menuBtnList;
		
	}
	 
		//初始化菜单添加
	
	 public List<EasyUiTreeVo> initSystemMenuApp(String systemId,String menuApp,String menuType){
		List<Menu> menuList=menuDao.getMenuStart(systemId,menuApp);
		List<Operation> operationAll=operationDao.getMenuStart(systemId,menuApp);
		//根据菜单类型返回按钮
		List<Operation> operation=getDealButton(operationAll,menuType);
	    List<EasyUiTreeVo> easyUiTreeVoList=transEasyUiVo(menuList,operation);
	    List<EasyUiTreeVo> menuBtnList = new ArrayList<EasyUiTreeVo>();
		  // 查看结果
	    for (EasyUiTreeVo menu : easyUiTreeVoList) {
	        System.out.println(menu);
	    }
	    // 最后的结果
	    // 先找到所有的一级菜单
	    for (int i = 0; i < easyUiTreeVoList.size(); i++) {
	        // 一级菜单没有parentId
	        if (StringUtils.isBlank(easyUiTreeVoList.get(i).getParentId())) {
	        	menuBtnList.add(easyUiTreeVoList.get(i));
	        }
	    }
	    // 为一级菜单设置子菜单，getChild是递归调用的
	    for (EasyUiTreeVo menu : menuBtnList) {
	        menu.setChildren(getChild(menu.getId(), easyUiTreeVoList));
	    }
	/*    Map<String,Object> jsonMap = new HashMap<>();
	    jsonMap.put("menu", menuBtnList);*/
	   
	
		return menuBtnList;
		
	}
	 //把easyTree对象转换成资源对象然后插入数据库,创建角色
	 public void createSystemRole(List<EasyUiTreeVo> easyUiTreeVo,Role role){
		 //填充角色对象
	    role.setCreateTime(new Date());
		role.setModifiedTime(new Date());
		roleDao.save(role);
		List<Author> authorlist=new ArrayList<Author>();
		actJson(easyUiTreeVo,authorlist,role.getRoleId());
		
		for(int i=0;i<authorlist.size();i++){
			authorDao.save(authorlist.get(i));
		}
 
	 }
	 
	//把easyTree对象转换成资源对象然后插入数据库,修改角色
	 public void updateSystemRole(List<EasyUiTreeVo> easyUiTreeVo,Role role){
		 //填充角色对象
		 role.setModifiedTime(new Date());
		 roleDao.update(role);
		 userRoleDao.upateRoleName(role.getRoleId(), role.getRoleName());
		 List<Author> authorlist=new ArrayList<Author>();
		 actJson(easyUiTreeVo,authorlist,role.getRoleId());
		 authorDao.deletRoleById(role.getRoleId());
		 for(int i=0;i<authorlist.size();i++){
				authorDao.save(authorlist.get(i));
			}
	 
	 }
	 @Override
	 public int delSystemRole(String roleId){
		 //检查对应角色有无关联帐号
		 int i=0;
		List<UserRole> userRoleList=userRoleDao.getByRoleId(roleId);
		if(userRoleList==null||userRoleList.size()==0){
			roleDao.deletRoleById(roleId);
			authorDao.deletRoleById(roleId);
			i=1;
		}
		return i;
		 
	 
	 }
     //检查角色是否重复
	 public boolean checkForRoleName(String roleName,String roleId,String systemId ){
		 return roleDao.isExistRoleByRoleName(roleName, roleId, systemId);
	 }
	 
        //转换对象网页
	    private void actJson(List<EasyUiTreeVo> items,List<Author> authorlist,String roleId){
	    	
	        for(EasyUiTreeVo treeVo:items){
	        		Author author=new Author();
	        		author.setResourceId(treeVo.getId());
	        		author.setResourceDesc(treeVo.getText());
	        		author.setResourceParentId(treeVo.getParentId());
	        		author.setRoleId(roleId);
	        		author.setResourseType(Short.valueOf(treeVo.getAttributes()));
	        		author.setCreateTime(new Date());
	        		author.setModifiedTime(new Date());
	        		//添加菜单
	        		if(treeVo.getAttributes().equals("0")){
		        		Menu menu=menuDao.getById(treeVo.getId());
		        		author.setResourceUrl(menu.getMenuUrl());
		        		author.setResourseIcon(menu.getMenuIcon());
		        		author.setResourceApp((int)menu.getMenuApp());
		        		if(menu.getMenuOrder()!=null){
		        			author.setResourseOrder((int)menu.getMenuOrder());
		        		}
		        	//添加按钮
	        		}else{
	        			Operation button=operationDao.getById(treeVo.getId());
	        			author.setResourceUrl(button.getBtnUrl());
		        		author.setResourseIcon(button.getBtnIcon());
		        		author.setResourceCode(button.getBtnCode());
		        		author.setResourceApp((int)button.getBtnApp());
		        		if(button.getBtnOrder()!=null){
		        			author.setResourseOrder((int)button.getBtnOrder());
		        		}
	        		}
	        		authorlist.add(author);
	
	        }
	     }
	
	
		//菜单按钮转换成easyUi
		private List<EasyUiTreeVo> transEasyUiVo(List<Menu> menuList,List<Operation> operation){
			List<EasyUiTreeVo> easyUiTreeVoList=new ArrayList<EasyUiTreeVo>();
			for(int i=0;i<menuList.size();i++){
				EasyUiTreeVo easyUiTreeVo=new EasyUiTreeVo();
				easyUiTreeVo.setId(menuList.get(i).getMenuId());
				easyUiTreeVo.setParentId(menuList.get(i).getMenuParentId());
				easyUiTreeVo.setText(menuList.get(i).getMenuname());
				
				//0为菜单，1为按钮
				easyUiTreeVo.setAttributes("0");
				easyUiTreeVoList.add(easyUiTreeVo);
			}
			
			for(int i=0;i<operation.size();i++){
				EasyUiTreeVo easyUiTreeVo=new EasyUiTreeVo();
				easyUiTreeVo.setId(operation.get(i).getBtnId());
				if(StringUtils.isNotBlank(operation.get(i).getBtnParentId())){
					easyUiTreeVo.setParentId(operation.get(i).getBtnParentId());
				}else{
					easyUiTreeVo.setParentId(operation.get(i).getMenuId());
				}
				easyUiTreeVo.setText(operation.get(i).getBtnName());
				//0为菜单，1为按钮
				easyUiTreeVo.setAttributes("1");
				easyUiTreeVoList.add(easyUiTreeVo);
			}
			return easyUiTreeVoList;
			
			
		}
		//菜单按钮转换成easyUi
		private List<EasyUiTreeVo> transEasyUiVoPeople(List<Author> authorList){
			List<EasyUiTreeVo> easyUiTreeVoList=new ArrayList<EasyUiTreeVo>();
			for(int i=0;i<authorList.size();i++){
				EasyUiTreeVo easyUiTreeVo=new EasyUiTreeVo();
				easyUiTreeVo.setId(authorList.get(i).getResourceId());
				easyUiTreeVo.setParentId(authorList.get(i).getResourceParentId());
				easyUiTreeVo.setText(authorList.get(i).getResourceDesc());
				
				//0为菜单，1为按钮
				easyUiTreeVo.setAttributes(String.valueOf(authorList.get(i).getResourseType()));
				easyUiTreeVoList.add(easyUiTreeVo);
			}
			
		
			return easyUiTreeVoList;
			
			
		}
		
		//实际权限转换为easyUi
		private List<EasyUiTreeVo> authorTransEasyUiVo(List<EasyUiTreeVo> easyUiTreeVoList,List<Author> authorList){
            for(int i=0;i<authorList.size();i++){
            	for(int j=0;j<easyUiTreeVoList.size();j++){
            		if(authorList.get(i).getResourceId().equals(easyUiTreeVoList.get(j).getId())){
            			easyUiTreeVoList.get(j).setChecked(true);
            			break;
            		}
            	}
            }		
			return easyUiTreeVoList;
			
			
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
		private List<EasyUiTreeVo> getChild(String id, List<EasyUiTreeVo> rootMenu) {
		    // 子菜单
		    List<EasyUiTreeVo> childList = new ArrayList<>();
		    for (EasyUiTreeVo menu : rootMenu) {
		        // 遍历所有节点，将父菜单id与传过来的id比较
		        if (StringUtils.isNotBlank(menu.getParentId())) {
		            if (menu.getParentId().equals(id)) {
		                childList.add(menu);
		            }
		        }
		    }
		    // 把子菜单的子菜单再循环一遍
		    for (EasyUiTreeVo menu : childList) {//
		            // 递归
		            menu.setChildren(getChild(menu.getId(), rootMenu));
		        
		    } // 递归退出条件
		    if (childList.size() == 0) {
		        return null;
		    }
		    return childList;
		}
		/**
		 * 处理按钮
		 * 
		 */
		private List<Operation> getDealButton(List<Operation>  operation,String menuType) {
			//管理员部分按钮不能使用
			if(menuType.equals("0")){
				Iterator<Operation> it = operation.iterator();
				while(it.hasNext()){
					Operation x = it.next();
				    if(x.getBtnCode().equals("jd")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("kscl")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("wccl")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("qxsq")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("yssq")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("bgsq")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("km")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("jk")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("scgd")){
				        it.remove();
				        continue;
				    }
				}
			}else{
				Iterator<Operation> it = operation.iterator();
				while(it.hasNext()){
					Operation x = it.next();
				    if(x.getBtnCode().equals("pd")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("tysq")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("btysq")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("gj")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("cxcl")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("tyys")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("btyys")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("bg")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("ch")){
				        it.remove();
				        continue;
				    }
				    if(x.getBtnCode().equals("btyb")){
				        it.remove();
				        continue;
				    }
				}
			}
		    return operation;
		}

	
}
