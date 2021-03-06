package com.hori.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hori.model.Department;
import com.hori.pageModel.Json;
import com.hori.service.OrganizationService;
import com.hori.vo.AuthorLoginVo;
import com.hori.vo.EasyUiTreeVo;
import com.hori.vo.UserDetailLoginVo;
import com.opensymphony.xwork2.ModelDriven;

@Action(value = "organizationAction", results = {
		@Result(name = "organization", location = "/peopleManagement/organization.jsp")
      })
public class OrganizationAction extends BaseAction implements ModelDriven<Department> {

	private Department departmentVo ;
	@Autowired
	private OrganizationService organizationService;
	@Override
	public Department getModel() {
		if(null==departmentVo) departmentVo= new Department();
		return departmentVo;
	}
	HttpServletRequest request = ServletActionContext.getRequest();
	/**
	 * 跳转到组织架构页面
	 * @return
	 */
	public String goOrganizationList(){
		return "organization";
	}
	/**
	 * 获取组织架构基本信息（组织机构页面）
	 * @return
	 */
	public void getSystemOrganization(){

		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userType=(String)request.getSession().getAttribute("userType");
		List<EasyUiTreeVo> departmentList = organizationService.dealDepartmentBySystemId(systemId);
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(departmentList);
		writeJson(j);

	}
	/**
	 * 获取组织架构基本信息（组织机构页面的其他页面）
	 * @return
	 */
	public void getSystemOrganizationOthers(){
		//从session里面获取登录机构
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		//获取当前登录人员的机构
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String userType=(String)request.getSession().getAttribute("userType");
		byte dataArea=(byte)request.getSession().getAttribute("dataArea");

		List<EasyUiTreeVo> departmentList=new ArrayList<EasyUiTreeVo>();
		if(userType.equals("0")||dataArea==2){
		       departmentList = organizationService.dealDepartmentBySystemId(systemId);
		}else{
		       departmentList = organizationService.dealDepartmentBySystemIdNext(systemId,userDetailVo.getDepartId());

		}
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		j.setObj(departmentList);
		writeJson(j);

	}
	/**
	 * 增加机构信息
	 * @return
	 */
	public void addSystemOrganization(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String parentOrgaId = getRequest().getParameter("parentOrgaId");
		String orga = getRequest().getParameter("orga");
		String orgaDesc = getRequest().getParameter("orgaDesc");
		organizationService.saveDepartment(parentOrgaId,orga,orgaDesc,systemId);
		
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		writeJson(j);
		String operationModule = "人员管理-组织架构管理";
		String operationType = "新增";
	}
	/**
	 * 删除机构信息
	 * @return
	 */
	public void delSystemOrganization(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String orgaId = getRequest().getParameter("orgaId");
		Json j = new Json();
		if(organizationService.delCheckDepartment(orgaId)){
			j.setSuccess(false);
			j.setMsg("机构下有人员不能删除该机构");

		}else{
			organizationService.delDepartment(orgaId,systemId);
			j.setSuccess(true);
			j.setMsg("sucess");
		}
		
	
		writeJson(j);
		String operationModule = "人员管理-组织架构管理";
		String operationType = "删除";
	}
	/**
	 * 修改机构信息
	 * @return
	 */
	public void editSystemOrganization(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String orgaId = getRequest().getParameter("orgaId");
		String orga = getRequest().getParameter("orga");
		String orgaDesc = getRequest().getParameter("orgaDesc");
		organizationService.editDepartment(orgaId,systemId,orga,orgaDesc);	
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("sucess");
		writeJson(j);
		String operationModule = "人员管理-组织架构管理";
		String operationType = "修改";
	}
	/**
	 * 校验操作权限
	 * @return
	 */
	public void checkAuth(){
		String systemId = (String)request.getSession().getAttribute("selected_platform");
		String orgaId = getRequest().getParameter("orgaId");
		String status=getRequest().getParameter("status");
		Json j = new Json();
		//从session里面获取登录机构
		UserDetailLoginVo userDetailVo =(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		//只有增加
		if(status.equals("create")){
			if(orgaId!=null){
				boolean check=organizationService.checkForDepart(orgaId, userDetailVo.getDepartId());
				j.setSuccess(check);
			}else{
				j.setSuccess(true);

			}
			
		}else{
			if(orgaId.equals(userDetailVo.getDepartId())){
				j.setSuccess(false);			
			}else{
				boolean check=organizationService.checkForDepart(orgaId, userDetailVo.getDepartId());
				j.setSuccess(check);
			}
		}
		
		writeJson(j);

	}
	

}
