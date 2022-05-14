package com.hori.grms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.PendingEvent;
import com.hori.grms.queryBean.PendingEventQueryBean;
import com.hori.grms.service.PendingEventService;

/**
 * 待办事项Controller类
 * @author FWQ
 *
 */
@Controller
@RequestMapping("/pendingEvent")
public class PendingEventController extends BaseController {
	private final Logger logger = Logger.getLogger(PendingEventController.class);

	@Autowired
	private PendingEventService pendingEventService;
	/**
	 * 跳转项目列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/pendingEventList")
	public String getProjectList(HttpServletRequest request, HttpServletResponse response) {

		return "/pendingEvent/pendingEventList.jsp";
	}
	
	
	/**
	 * 获取项目列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/getListData",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String getPendingEventListData(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String parameter = request.getParameter("pendingEventQueryBean");

			PendingEventQueryBean pendingEventQueryBean = JSON.parseObject(parameter, PendingEventQueryBean.class);
			
			//获取角色类型
			int roleType = -1;
//			Byte roleTypes = (Byte)request.getSession().getAttribute("roleType");
//			roleType = (int)roleTypes;
			Object roleTypeSession = request.getSession().getAttribute("roleType");
			ObjectMapper om = new ObjectMapper();
			String roleTypeVal = om.writeValueAsString(roleTypeSession);
			roleType = roleTypeVal !=null ||!"".equals(roleTypeVal)?Integer.parseInt(roleTypeVal):-100;
			pendingEventQueryBean.setRoleType(roleType);
			
			//获取登录用户账号
			String account = "";
			account = (String)request.getSession().getAttribute("userAccount");
			pendingEventQueryBean.setAccount(account);

			PageInfo<Map<String, Object>> listProject = pendingEventService.list(pendingEventQueryBean);
			
			resultMap.put("success", true);
			resultMap.put("rows", listProject.getList());
			resultMap.put("total", listProject.getTotal());
			
		} catch (Exception e) {
			
			logger.info("错误原因："+e.getMessage());
			resultMap.put("success", false);
		
		}
		
		
		return JSON.toJSONString(resultMap);
	}
	
}
