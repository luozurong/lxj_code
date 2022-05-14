package com.hori.grms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hori.grms.model.Customer;
import com.hori.grms.model.CustomerType;
import com.hori.grms.service.CustomerManagementSerivce;
import com.hori.grms.service.CustomerTypeService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.util.ExportExcelUtil.PoiCell;

/**
 * 客户管理
 * @author shejun
 *	
 */
@Controller
@RequestMapping("/customerManagement")
public class CustomerManagementController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerManagementSerivce customerManagementSerivce;
	@Autowired
	private CustomerTypeService customerTypeService;
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	
	/**
	 * 列表跳转
	 */
	@RequestMapping("/list")
	public String getCustomerList(){
		return "customerManagement/customerManage.jsp";
	}
	/**
	 * 展示列表，附带条件查询
	 */
	@RequestMapping(value="/getAllList",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String getAllCustomer(){
		String userAccount = (java.lang.String) request.getSession().getAttribute("userAccount");
		Object roleTypeSession = request.getSession().getAttribute("roleType");
		String condition = request.getParameter("condition");
		//logger.info("condition:"+condition);
		List<Map<String, Object>> list = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ObjectMapper om = new ObjectMapper();
			String roleType = om.writeValueAsString(roleTypeSession);
			list = customerManagementSerivce.getAll(condition,userAccount,roleType);
			map.put("data",list);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	/**
	 * 保存页面跳转
	 */
	@RequestMapping("/toSaveCust")
	public String toSaveCust(){
		String userName = (java.lang.String) request.getSession().getAttribute("userName");
		List<CustomerType> type = customerTypeService.getCustomerType();
		request.setAttribute("type", type);
		request.setAttribute("salesman", userName);
		return "customerManagement/addCustomer.jsp";
	}
	
	/**
	 * 保存、编辑后更新
	 */
	@RequestMapping(value="/save",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String saveCustomer(Customer customer){
		String userAccount = (java.lang.String) request.getSession().getAttribute("userAccount");
		//logger.info("id:"+customer.getId());
		Map<String,Boolean> map =new HashMap<String,Boolean>();
		try {
			customerManagementSerivce.save(customer,userAccount);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	/**
	 * 单个删除、批量删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String batchDelete(){
		Map<String,Object> map = new HashMap<String,Object>();
		String ids = request.getParameter("ids");
		logger.info("ids:"+ids);
		List<String> idList = new ArrayList<String>();
		String[] idStr = ids.split(",");
		for (String id : idStr) {
			idList.add(id.trim());
		}
		try {
			customerManagementSerivce.deleteById(idList);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	/**
	 * 客户编辑页面跳转,客户编辑展示,数据回显
	 * @return
	 */
	@RequestMapping("/toEditCust")
	public String editCustomer(){
		String id = request.getParameter("id");
		Map<String,Object> custMap = customerManagementSerivce.fetchCustomerById(id.trim());
		//类型去重
		List<CustomerType> type = rebuildType(custMap,customerTypeService.getCustomerType());
		request.setAttribute("type", type);
		request.setAttribute("custMap", custMap);	
		request.setAttribute("id", id);
		return "customerManagement/editCustomer.jsp";
	}
	/**
	 * 客户详情页面跳转
	 * @return
	 */
	@RequestMapping("/toCustDetail")
	public String showCustomer(){
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
		String userAccount = (java.lang.String) request.getSession().getAttribute("userAccount");
		request.setAttribute("currentId", id);
		request.setAttribute("flag", flag);
		request.setAttribute("currentAccount", userAccount);
		return "customerManagement/showCustomer.jsp";
	}
	/**
	 * 客户详情展示
	 * @return
	 */
	@RequestMapping(value="/getDetailById",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String showDetial(){
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("id");
		Map<String,Object> custMap = null;
		try {
			custMap = customerManagementSerivce.fetchCustomerById(id.trim());
			map.put("data", custMap);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 弹框模糊查询相同字的公司名
	 */
	@RequestMapping(value="/getSimilarNameList",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String getSimilarNameList(){
		Map<String,Object> map = new HashMap<String,Object>();
		String condition = request.getParameter("condition");
		//logger.info("condition:"+condition);
		List<Map<String, Object>> list = null;
		try {
			list = customerManagementSerivce.getAllByName(condition);
			map.put("data",list);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 展示客户移交
	 */
	@RequestMapping(value="/transferInfo",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String transferInfo(){
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("id");
		List<Map<String, Object>> list = null;
		try {
			list = customerManagementSerivce.transferInfoById(id);
			map.put("data",list);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	/**
	 * 客户转移更新
	 */
	@RequestMapping(value="transferCustomer",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String transferCustomer(){
		Map<String,Object> map = new HashMap<String,Object>();
		String custId = request.getParameter("custId");
		String acceptName = request.getParameter("acceptName");
		String acceptAccount = request.getParameter("acceptAccount");
		//当前操作此次转移事件的操作人的信息
		String operator = (String) request.getSession().getAttribute("userAccount");
		String operatorName = (String) request.getSession().getAttribute("userName");
		try {
			customerManagementSerivce.transferCustomer(custId,acceptAccount,acceptName,operator,operatorName);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	/**
	 * 导出
	 */
	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(){
		Map<String,Object> map = new HashMap<String,Object>();
		String userAccount = (java.lang.String) request.getSession().getAttribute("userAccount");
		String condition = request.getParameter("condition");
		Object roleTypeSession = request.getSession().getAttribute("roleType");
		List<Map<String, Object>> list = null;
		try {
			ObjectMapper om = new ObjectMapper();
			String roleType = om.writeValueAsString(roleTypeSession);
			list = customerManagementSerivce.getExport(condition,userAccount,roleType);
			
			String title = "客户管理";
			String [] headers = {"公司名称","公司地址","公司类型","主要经营","部门","主要联系人名称","主要联系人电话","负责人名称","负责人电话","业务员"};
			String fileName = "客户信息列表.xls";
			
			ExportExcelUtil.experotExcelFor2003(title, headers, list, fileName, new PoiCell() {
				@Override
				public void setCellData(Object obj, HSSFRow row, HSSFCellStyle style) {
					@SuppressWarnings("unchecked")
					Map<String,Object> map = (Map<java.lang.String, Object>) obj;
					int i = 0;
					HSSFCell cell = row.createCell(i++);
					//公司名称
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("name"));
					cell = row.createCell(i++);
					//公司地址
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("address"));
					cell = row.createCell(i++);
					//公司类型
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("customerType"));
					cell = row.createCell(i++);
					//主要经营
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("industry"));
					cell = row.createCell(i++);
					//部门
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("department"));
					cell = row.createCell(i++);
					//主要联系人名称
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("contactor"));
					cell = row.createCell(i++);
					//主要联系人电话
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("contactorPhone"));
					cell = row.createCell(i++);
					//负责人名称
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("dutyName"));
					cell = row.createCell(i++);
					//负责人电话
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("dutyPhone"));
					cell = row.createCell(i++);
					//业务员
					cell.setCellStyle(style);
					cell.setCellValue((String)map.get("salesman"));
					cell = row.createCell(i++);
				}
			}, request, response);
			
			map.put("data",list);
			map.put("succ", true);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断能否删除和转移客户
	 */
	@RequestMapping(value="/transferAndDelete",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String transferAndDelete(){
		Map<String,Object> map = new HashMap<String,Object>();
		String ids = request.getParameter("id");
		//可转移条件：一个客户下所有项目都执行完了；部分项目正在执行中，不可转
		String[] idArr = ids.split(",");
		try {
			boolean flag = istransfer(customerManagementSerivce.projectStatus(idArr));
			map.put("succ", true);
			map.put("flag", flag);
		} catch (Exception e) {
			map.put("succ", false);
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	private boolean istransfer(String projectStatus) {
		if ("true".equals(projectStatus)) {
			return true;
		}
		return false;
	}
	
	private List<CustomerType> rebuildType(Map<String, Object> custMap, List<CustomerType> type) {
		String custType = (String) custMap.get("customerType");
		Iterator<CustomerType> iterator = type.iterator();
		while(iterator.hasNext()){
			CustomerType next = iterator.next();
			if (custType.trim().equals(next.getName().trim())) {
				iterator.remove();
			}
		}
		return type;
	}
}
