package com.hori.grms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.DepartmentBusinessArea;
import com.hori.grms.model.DepartmentBusinessType;
import com.hori.grms.service.DepartmentBusinessService;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.DepartmentBusinessVo;

/**
 * 产品配置管理
 */
@Controller
@RequestMapping("/departmentBusiness")
public class DepartmentBusinessController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DepartmentBusinessService departmentBusinessService;
	
	/**
	 * 产品配置列表页面
	 */
	@RequestMapping("/list")
	public String getProjectList(HttpServletRequest request, HttpServletResponse response) {
		
		return "/systemSettings/departmentBusinessList.jsp";
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping("/jumpToAddPage")
	public String jumpToAddPage(HttpServletRequest request, HttpServletResponse response){
		
		String departmentName = request.getParameter("departmentName");
		request.setAttribute("departmentName",departmentName);
		String departmentId = request.getParameter("departmentId");
		request.setAttribute("departmentId",departmentId);
		return "/systemSettings/addDepartmentBusiness.jsp";
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequestMapping("/jumpToEditPage")
	public String jumpToEditPage(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		DepartmentBusinessType type = departmentBusinessService.selectTypeByPrimaryKey(id);
		request.setAttribute("businessTypeId",id);
		request.setAttribute("departmentId",type.getDepartmentId());
		request.setAttribute("departmentName",type.getDepartmentName());
		return "/systemSettings/editDepartmentBusiness.jsp";
	}
	
	/**
	 * 获取产品配置列表
	 */
	@RequestMapping(value ="/getBusinessTypeList")
	@ResponseBody
	public String getBusinessTypeList(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String condition = request.getParameter("condition");
			List<DepartmentBusinessVo> list = departmentBusinessService.getBusinessVoList(condition);
			
			resultMap.put("data", list);
			resultMap.put("success", true);
		
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}

	
	/**
	 * 删除
	 * @param request
	 * @param response
	 */
	@RequestMapping("/delDepartmentBusiness")
	@ResponseBody
	public String delDepartmentBusiness(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String ids = request.getParameter("ids");
			String[] idStr = ids.split(",");
			for (String id : idStr) {
				departmentBusinessService.delDepartmentBusinessType(id);
				departmentBusinessService.delDepartmentBusinessArea(id);
			}
			resultMap.put("success", true);
		}catch(Exception e){
			logger.info("发送异常"+e.getMessage());
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
		
	}
	
	/**
	 * 新增
	 * @param request
	 * @param response
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response){
		String departmentName = request.getParameter("departmentName");
		String departmentId = request.getParameter("departmentId");
		String businessType = request.getParameter("businessType");
		String province = request.getParameter("province");
		String provinceName = request.getParameter("provinceName");
		String city = request.getParameter("city");
		String cityName = request.getParameter("cityName");
		String areaFlag = request.getParameter("areaFlag");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			DepartmentBusinessType type = new DepartmentBusinessType();
			type.setId(UUIDGeneratorUtil.generateUUID());
			type.setDepartmentName(departmentName);
			type.setDepartmentId(departmentId);
			type.setBusinessType(businessType);
			type.setAreaFlag(Integer.parseInt(areaFlag));
			departmentBusinessService.insertType(type);
			
			DepartmentBusinessArea area = new DepartmentBusinessArea();
			area.setId(UUIDGeneratorUtil.generateUUID());
			area.setDepartmentId(departmentId);
			area.setBusinessTypeId(type.getId());
			area.setBusinessType(businessType);
			
			area.setCity(city);
			area.setCityName(cityName);
			area.setProvince(province);
			area.setProvinceName(provinceName);
			departmentBusinessService.insertArea(area);
			
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 获取地区树回显数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getDepartmentAreaById")
	@ResponseBody
	public String getDepartmentAreaById(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String id = request.getParameter("id");
			DepartmentBusinessVo vo = departmentBusinessService.getDepartmentAreaById(id);
			
			resultMap.put("vo", vo);
			resultMap.put("success", true);
		
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	
	
	
	/**
	 * 更新
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateDepartmentBusiness")
	@ResponseBody
	public String updateDepartmentBusiness(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String businessTypeId = request.getParameter("businessTypeId");
		String businessType = request.getParameter("businessType");
		String province = request.getParameter("province");
		String provinceName = request.getParameter("provinceName");
		String city = request.getParameter("city");
		String cityName = request.getParameter("cityName");
		String areaFlag = request.getParameter("areaFlag");
		try{
			DepartmentBusinessType type = departmentBusinessService.selectTypeByPrimaryKey(businessTypeId);
			type.setBusinessType(businessType);
			type.setAreaFlag(Integer.valueOf(areaFlag));
			departmentBusinessService.updateType(type);
			
			DepartmentBusinessArea area = departmentBusinessService.selectAreaByBusinessTypeId(businessTypeId);
			area.setBusinessType(businessType);
			area.setCity(city);
			area.setCityName(cityName);
			area.setProvince(provinceName);
			area.setProvinceName(provinceName);
			departmentBusinessService.updateArea(area);
			
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
}
