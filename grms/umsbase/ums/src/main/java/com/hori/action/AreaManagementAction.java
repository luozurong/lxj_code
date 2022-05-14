package com.hori.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.hori.dao.queryBean.AreaQueryBean;
import com.hori.db.support.DataGridPage;
import com.hori.model.AuthorizeArea;
import com.hori.pageModel.Json;
import com.hori.service.AreaManagementService;
import com.hori.vo.EasyUiTreeVo;
import com.hori.vo.UserDetailLoginVo;
/**
 * 责任区域管理
 * 
 * @author 
 * 
 */

@Action(value = "areaManagementAction", 
  results = { @Result(name = "areaManagement", location = "/peopleManagement/areaManagement.jsp"),
  
      })
public class AreaManagementAction extends BaseAction {
	
	@Autowired
	AreaManagementService areaManagementService;
	HttpServletRequest request = ServletActionContext.getRequest();

	/**
	 * 跳转责任区域管理页面
	 * @return
	 */
	public String goAreaManagementList(){
		
		return "areaManagement";
	}
	/**
	 * 根据参数分页查询
	 * @return
	 */
	public void getUserAccountByDepartId(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String areaQueryBean=getRequest().getParameter("areaQueryBean");
		String userType=(String)request.getSession().getAttribute("userType");
		String userAccount=(String)request.getSession().getAttribute("userAccount");
		String newStr = areaQueryBean.replaceAll("“","\"");
		AreaQueryBean data=JSON.parseObject(newStr, AreaQueryBean.class);  
		data.setSystemId(systemId);
		//从session里面获取登录机构
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		DataGridPage dataGridPage=areaManagementService.findUserByParam(data,userType,userAccount,userDetailVo.getDepartId());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(dataGridPage);
		writeJson(j);

	}
	/**
	 * 管理员权限可以查看所有区域，运维权限只可以查看自己的区域
	 * @return
	 */
	public void getSystemArea(){
		String id=getRequest().getParameter("id");
		String layer=getRequest().getParameter("layer");
		String userId=getRequest().getParameter("userId");
		//状态
		String status=getRequest().getParameter("status");
		String userType=(String)request.getSession().getAttribute("userType");
		String userAccount=(String)request.getSession().getAttribute("userAccount");
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		Map<String,Object> map=new HashMap<String,Object>();
    	Json j = new Json();
        if(userType.equals("0"))
        {
        	map= areaManagementService.dealAuthorAreaAdmin(userAccount,systemId,userId,status);
    		j.setObj(map);

        }else{
        	//返回该人员的组织
        	map= areaManagementService.dealAuthorArea(userAccount,systemId,userId,status);
    		j.setObj(map);

        }
		j.setSuccess(true);
		j.setMsg(userType);
		writeJson(j);
	}
	/**
	 * 根据省市加载区域，填充右边
	 * @return
	 */
	public void getSystemAreaRight(){
		String id=getRequest().getParameter("id");
		String layer=getRequest().getParameter("layer");
	}
	
	/**
	 * 动态加载省市
	 * @return
	 */
	public void getSystemAreaDy(){
		//String provinceId,String provinceName,String cityId,String cityName,String layer
		String provinceId=getRequest().getParameter("provinceId");
		String provinceName=getRequest().getParameter("provinceName");
		String cityId=getRequest().getParameter("cityId");
		String cityName=getRequest().getParameter("cityName");
		String layer=getRequest().getParameter("layer");
		String userType=(String)request.getSession().getAttribute("userType");
		String userAccount=(String)request.getSession().getAttribute("userAccount");
		String systemId = (String)request.getSession().getAttribute("selected_platform");
        
		List<AuthorizeArea> areaList=new ArrayList<AuthorizeArea>();
        if(userType.equals("0"))
        {
        	areaList = areaManagementService.initUserAreaByCity(provinceId, provinceName, cityId, cityName, layer);
        }else{
        	
        }
    	Json j = new Json();
		j.setSuccess(true);
		j.setMsg(userType);
		j.setObj(areaList);
		writeJson(j);
	}
	public void addUserArea(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String ids=getRequest().getParameter("ids"); 
		String areaList=getRequest().getParameter("areaList");
		areaManagementService.addUserAutoroizeArea(ids,areaList,systemId);
	
/*        System.out.println(ids+"   "+areaList);
*/        
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		writeJson(j);
		String operationModule = "人员管理-责任区域管理";
		String operationType = "新增或者修改";
	}
	
	public void initUserArea(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userId=getRequest().getParameter("userId");
		List<AuthorizeArea> authorizeAreaList=areaManagementService.initUserAreaById(userId, systemId);
	    Json j = new Json();
		j.setSuccess(true);
		j.setObj(authorizeAreaList);
		j.setMsg("sucess");
		writeJson(j);
	}
	//更改用户类型
	public void changeUserType(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userId=getRequest().getParameter("userId");
		String userType=getRequest().getParameter("userType");
		areaManagementService.changeUserType(userId, userType);
	    Json j = new Json();
		j.setSuccess(true);
		j.setObj("");
		j.setMsg("sucess");
		writeJson(j);
	}
	
	
/*	public void initUserAreaLeft(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userId=getRequest().getParameter("userId");
		List<EasyUiTreeVo> authorizeAreaList=areaManagementService.initUserAreaByIdLeft(userId, systemId);
	    Json j = new Json();
		j.setSuccess(true);
		j.setObj(authorizeAreaList);
		j.setMsg("sucess");
		writeJson(j);
	}*/
	
	
/*	public void saveUserArea(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userId=getRequest().getParameter("userId");
		String areaList=getRequest().getParameter("areaList");
		String newStr = areaList.replaceAll("“","\"");
		List<AuthorizeArea> dataList=JSON.parseArray(newStr, AuthorizeArea.class);
		areaManagementService.deleUserAreaById(userId, systemId);
		areaManagementService.addUserAutoroizeArea(userId,dataList,systemId);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		writeJson(j);

	}*/
	
	

}
