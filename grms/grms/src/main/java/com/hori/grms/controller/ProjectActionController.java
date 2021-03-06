package com.hori.grms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.model.ProjectActionException;
import com.hori.grms.model.ProjectProduct;
import com.hori.grms.queryBean.PAExceptionQueryBean;
import com.hori.grms.queryBean.ProjectActionQueryBean;
import com.hori.grms.service.AreaDataService;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.service.ContractService;
import com.hori.grms.service.ProjectActionExceptionService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectProductMenuAreaService;
import com.hori.grms.service.ProjectProductService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.util.ExportExcelUtil.PoiCell;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.AreaDataReqVo;
import com.hori.grms.vo.AreaDataRspVo;
import com.hori.grms.vo.CommunityProjectActionVo;
import com.hori.grms.vo.ContractVo;
import com.hori.grms.vo.ExportMGVo;
import com.hori.grms.vo.ProjectActionExceptionVo;
import com.hori.vo.UserDetailLoginVo;

/**
 * ????????????Controller
 * 
 * @author hehj
 * @datetime 2018???8???9??? ??????11:44:46
 */
@Controller
@RequestMapping("/projectAction")
public class ProjectActionController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(ProjectActionController.class);

	@Autowired
	private ProjectActionService projectActionService;
	@Autowired
	private ProjectActionExceptionService projectActionExceptionService;
	@Autowired
	private ProjectProductService projectProductService;
	@Autowired
	private AreaDataService areaDataService;
	@Autowired
	private ProjectProductMenuAreaService projectProductMenuAreaService;
	@Autowired
	private BackMoneyPlanService backMoneyPlanService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private CloseCaseService closeCaseService;
	@Autowired
	private ProjectService projectService;

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/communityActionList")
	public String goCommunityPAList(HttpServletRequest request, HttpServletResponse response) {
		String jumpStr = request.getParameter("jumpStr");

		request.setAttribute("jumpStr", jumpStr);

		// ??????????????????
		String projectActionCodeForPending = request.getParameter("projectActionCodeForPending");
		String pendingParamType = request.getParameter("pendingParamType");
		if ("1".equals(pendingParamType)) {
			request.getSession().setAttribute("projectActionCodeForPending", projectActionCodeForPending);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}

		return "/projectAction/community/actionList.jsp";
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCommunityPAListData", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String communityPAListData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("projectActionQueryBean");

			ProjectActionQueryBean queryBean = JSON.parseObject(parameter, ProjectActionQueryBean.class);
			if (queryBean == null) {
				queryBean = new ProjectActionQueryBean();
			}
			logger.info("*********" + queryBean.toString());

			byte dataArea = (byte) request.getSession().getAttribute("dataArea");
			queryBean.setDataArea(dataArea);
			if (dataArea == 0) {// ????????????????????????0??????????????????1??????????????????2???????????????
				List<String> userList = (List<String>) request.getSession().getAttribute("userList");
				queryBean.setUserList(userList);
				UserDetailLoginVo userDetailVo = (UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
				queryBean.setDepartmentId(userDetailVo.getDepartId());
				queryBean.setDepartmentName(userDetailVo.getDepartName());
			} else if (dataArea == 1) {
				List<String> departList = (List<String>) request.getSession().getAttribute("departList");
				queryBean.setDepartList(departList);
			}

			// ??????????????????
			String pendingParamType = (String) request.getSession().getAttribute("pendingParamType");
			if ("1".equals(pendingParamType) && pendingParamType != null) {
				String projectActionCodeForPending = (String) request.getSession()
						.getAttribute("projectActionCodeForPending");
				queryBean.setPageNumber(1);
				queryBean.setPageSize(10);
				queryBean.setActionCode(projectActionCodeForPending);

			}

			PageInfo<CommunityProjectActionVo> pageInfo = projectActionService.listCommunityAction(queryBean);
			if (pageInfo == null) {
				resultMap.put("success", false);
			} else {
				resultMap.put("success", true);
				resultMap.put("rows", pageInfo.getList());
				resultMap.put("total", pageInfo.getTotal());
			}
		} catch (Exception e) {
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
		} finally {
			// ??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("projectActionCodeForPending");
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/changeStatus", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String changeActionStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String projectActionId = request.getParameter("projectActionId");
			if (StringUtils.isBlank(projectActionId)) {
				throw new Exception("??????????????????id???????????????");
			}
			String actionStatus = request.getParameter("actionStatus");
			String userAccount = (String) request.getSession().getAttribute("userAccount");
			String userName = (String) request.getSession().getAttribute("userName");

			ProjectAction action = projectActionService.getById(projectActionId);

			if (5 == Integer.valueOf(actionStatus)) {// ???????????????
				// ?????????????????????????????????
				ContractVo contract = contractService.getExecuteContractByStatus(action.getProjectCode(), 7);
				if (contract == null) {
					resultMap.put("success", false);
					resultMap.put("hasNoContract", true);
					return JSON.toJSONString(resultMap);
				}
				BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByContractCodeAndStatus(contract.getContractCode(), 1);
				if (backMoneyPlan == null) {// ?????????????????????????????????
					resultMap.put("success", false);
					resultMap.put("hasNobackMoneyPlan", true);
					return JSON.toJSONString(resultMap);
				}
			}
			action.setActionStatus(Integer.valueOf(actionStatus));
			action.setReceiveAccount(userAccount);
			action.setReceiveName(userName);
			action.setRecevieTime(new Date());
			projectActionService.update(action);

			if (5 == Integer.valueOf(actionStatus)) {
				Project project = projectService.getByCode(action.getProjectCode());
				long num = closeCaseService.countByProjectCode(project.getProductCode());
				if (num == 0L) {// ???????????????????????????
					if (project.getActionStatus() == 2) {// ????????????
						closeCaseService.createCloseCaseInfo(project, null);
					}
				}
			}

			resultMap.put("success", true);
		} catch (Exception e) {
			logger.info(e.getMessage());
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exceptionList")
	public String goExceptionList(HttpServletRequest request, HttpServletResponse response) {
		String jumpStr = request.getParameter("jumpStr");

		request.setAttribute("jumpStr", jumpStr);
		return "/projectAction/exceptionList.jsp";
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getExceptionListData", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String exceptionList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("paExceptionQueryBean");

			PAExceptionQueryBean queryBean = JSON.parseObject(parameter, PAExceptionQueryBean.class);
			if (queryBean == null) {
				queryBean = new PAExceptionQueryBean();
			}
			logger.info("*********" + queryBean.toString());

			byte dataArea = (byte) request.getSession().getAttribute("dataArea");
			queryBean.setDataArea(dataArea);
			if (dataArea != 2) {// ????????????????????????0??????????????????1??????????????????2???????????????
				List<String> userList = (List<String>) request.getSession().getAttribute("userList");
				queryBean.setUserList(userList);
				List<String> departList = (List<String>) request.getSession().getAttribute("departList");
				queryBean.setDepartList(departList);
			}

			PageInfo<ProjectActionExceptionVo> pageInfo = projectActionExceptionService.listException(queryBean);
			if (pageInfo == null) {
				resultMap.put("success", false);
			} else {
				resultMap.put("success", true);
				resultMap.put("rows", pageInfo.getList());
				resultMap.put("total", pageInfo.getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/changeConfirmStatus", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String changeConfirmStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String confirmStatus = request.getParameter("confirmStatus");
			String exceptionRecordId = request.getParameter("id");
			String result = request.getParameter("result");
			String projectActionCode = request.getParameter("projectActionCode");

			projectActionExceptionService.changeConfirmStatus(exceptionRecordId, Short.valueOf(confirmStatus));
			
			// ????????????
			if (6 == Short.valueOf(result)) {// ????????????
				ProjectAction projectAction = projectActionService.getByCode(projectActionCode);
				List<ProjectAction> listByProjectCode = projectActionService.listByProjectCode(projectAction.getProjectCode());
				boolean isOtherCompleted = true;
				for (ProjectAction action : listByProjectCode) {
					if (!projectAction.getId().equals(action.getId()) && action.getActionStatus() != 5) {
						// ????????????????????????
						isOtherCompleted = false;
						break;
					}
				}
				if (isOtherCompleted) {
					Project project = projectService.getByCode(projectAction.getProjectCode());
					long num = closeCaseService.countByProjectCode(project.getProductCode());
					if (num == 0L) {// ???????????????????????????
						if (project.getActionStatus() == 2) {// ????????????
							closeCaseService.createCloseCaseInfo(project, exceptionRecordId);
						}
					} else {
						closeCaseService.changeStatusByProjectCode(project.getProductCode(), 0);
					}
				}
			}
			
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			resultMap.put("success", false);
		}

		return JSON.toJSONString(resultMap);
	}

	@RequestMapping("/projectActionList")
	public String getProjectActionList(HttpServletRequest request, HttpServletResponse response) {

		// ??????????????????
		String projectActionCodeForPending = request.getParameter("projectActionCodeForPending");
		String pendingParamType = request.getParameter("pendingParamType");
		if ("1".equals(pendingParamType)) {
			request.getSession().setAttribute("projectActionCodeForPending", projectActionCodeForPending);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}

		HttpSession session = getSession();
		session.removeAttribute("projectActionSearchJsonVos");

		return "/projectAction/projectActionList.jsp";
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getlistData", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String getProjectListData(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("projectQueryBean");

			ProjectActionQueryBean queryBean = JSON.parseObject(parameter, ProjectActionQueryBean.class);

			if (null == queryBean) {
				queryBean = new ProjectActionQueryBean();
			}

			// ??????????????????
			String pendingParamType = (String) request.getSession().getAttribute("pendingParamType");
			if ("1".equals(pendingParamType) && pendingParamType != null) {
				String projectActionCodeForPending = (String) request.getSession()
						.getAttribute("projectActionCodeForPending");
				queryBean.setPageNumber(1);
				queryBean.setPageSize(10);
				queryBean.setProjectCode(projectActionCodeForPending);

			}
			
			HttpSession session = getSession();
			
			String userAccount = (String) session.getAttribute("userAccount");
			Byte roleType = (Byte) session.getAttribute("roleType");
			Byte dataArea = (Byte) session.getAttribute("dataArea");
			
			if (null!=dataArea&&null!=roleType&&dataArea == 0 && roleType == 0) {

				queryBean.setAccount(userAccount);

			}
			
			// ????????????,????????????
			session.setAttribute("projectActionSearchJsonVos", parameter);
			
			if(null!=dataArea&&null!=roleType&&dataArea!=2&&roleType!=0&&roleType!=1){
				
				resultMap.put("success", true);
				resultMap.put("rows", new ArrayList<>());
				resultMap.put("total", 0);
				
			}else{
				
				PageInfo<Map<String, Object>> listProject = projectActionService.listProject(queryBean);
				
				resultMap.put("success", true);
				resultMap.put("rows", listProject.getList());
				resultMap.put("total", listProject.getTotal());
			}
			

		} catch (Exception e) {

			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);

		} finally {
			// ??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("projectActionCodeForPending");
		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd");
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/gobackProject")
	public String goback(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = getSession();
		String searchJsonVo = (String) session.getAttribute("projectActionSearchJsonVos");

		ProjectActionQueryBean querryBean = JSON.parseObject(searchJsonVo, ProjectActionQueryBean.class);
		
		Byte roleType = (Byte) session.getAttribute("roleType");

		request.setAttribute("roleType", roleType);

		request.setAttribute("querryBean", querryBean);

		return "/projectAction/projectActionList.jsp";
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/gobackPEList")
	public String gobackPEList(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = getSession();
		String searchJsonVo = (String) session.getAttribute("pAExceptionSearchJsonVos");

		PAExceptionQueryBean querryBean = JSON.parseObject(searchJsonVo, PAExceptionQueryBean.class);
		
		request.setAttribute("productCode", querryBean.getProductCode());

		request.setAttribute("querryBean", querryBean);

		return "/projectAction/projectActionExceptionList.jsp";
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAllPAExceptionList")
	public String getAllPAExceptionList(HttpServletRequest request, HttpServletResponse response) {

		String productCode = request.getParameter("productCode");

		request.setAttribute("productCode", productCode);
		
		HttpSession session = getSession();
		session.removeAttribute("pAExceptionSearchJsonVos");
		
		return "/projectAction/projectActionExceptionList.jsp";

	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/substitutionArea")
	public String substitutionArea(HttpServletRequest request, HttpServletResponse response) {
		
		String actionCode = request.getParameter("actionCode");

		String exceptionId = request.getParameter("exceptionId");

		request.setAttribute("actionCode", actionCode);

		request.setAttribute("exceptionId", exceptionId);

		return "/projectAction/substitutionArea.jsp";

	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getPAExceptionlistData", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String getPAExceptionlistData(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			
			String parameter = request.getParameter("queryBean");

			PAExceptionQueryBean queryBean = JSON.parseObject(parameter, PAExceptionQueryBean.class);

			if (null == queryBean) {
				queryBean = new PAExceptionQueryBean();
			}
			
			HttpSession session = getSession();
			// ????????????,????????????
			session.setAttribute("pAExceptionSearchJsonVos", parameter);
			
			PageInfo<Map<String, Object>> listProjectAction = projectActionExceptionService
					.listProjectActionExceptions(queryBean);

			resultMap.put("success", true);
			resultMap.put("rows", listProjectAction.getList());
			resultMap.put("total", listProjectAction.getTotal());

		} catch (Exception e) {

			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAreaInfo", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String getAreaInfo(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			
			String parameter = request.getParameter("actionCode");
			
			ProjectAction projectAction = projectActionService.getByCode(parameter);
			
			if(null!=projectAction){
				ProjectProduct projectProduct = projectProductService.getById(projectAction.getProjectProductId());
				
				if(null!=projectProduct){
					
					resultMap.put("success", true);
					resultMap.put("obeject", projectProduct);

				}else{
					resultMap.put("success", false);
				}
					
			}else{
				resultMap.put("success", false);
			}
			
			
		} catch (Exception e) {
			
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			
		}
		
		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSubstitutionAreaDate", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String getSubstitutionAreaDate(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("areaDataReqVo");

			String actionCode = request.getParameter("actionCode");

			AreaDataReqVo areaDataReqVo = JSON.parseObject(parameter, AreaDataReqVo.class);

			if (null == areaDataReqVo) {
				areaDataReqVo = new AreaDataReqVo();
			}

			// ?????????????????????????????????????????????
			String organizationSeq = projectActionService.getOrganizationSeqByActionCode(actionCode);

			if (StringUtils.isNotBlank(organizationSeq)) {
				List<String> filterList = areaDataReqVo.getFilterList();
				filterList.add(organizationSeq);
			}

			logger.info("????????????????????????????????????????????????????????????" + areaDataReqVo.toString());
			// ??????????????????????????????
			AreaDataRspVo areaDataRspVo = areaDataService.searchAreaDataList(areaDataReqVo);

			String result = areaDataRspVo.getResult();

			if (result.equals("0")) {
				resultMap.put("success", true);
				resultMap.put("rows", areaDataRspVo.getList());
				resultMap.put("total", areaDataRspVo.getTotalCount());
				logger.info("???????????????????????????????????????");
			} else {
				String reason = areaDataRspVo.getReason();
				logger.info("??????????????????????????????????????????????????????" + reason);
				resultMap.put("success", false);
				resultMap.put("msg", reason);
			}

		} catch (Exception e) {

			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/substitution", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String substitution(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			HttpSession session = request.getSession();
			
			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType=(String) session.getAttribute("userType");
			
			if((!"0".equals(userType))&&roleType!=null&&roleType!=1){
				
				logger.info("????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "????????????????????????????????????");
			
			}else{
				
				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				
				String actionCode = request.getParameter("actionCode");
				String exceptionId = request.getParameter("exceptionId");
				String areaName = request.getParameter("areaName");
				String organizationSeq = request.getParameter("organizationSeq");
				String province = request.getParameter("province");
				String provinceName = request.getParameter("provinceName");
				String city = request.getParameter("city");
				String cityName = request.getParameter("cityName");
				String countryName = request.getParameter("countryName");
				String areaAddress = request.getParameter("areaAddress");
				
				ProjectAction projectAction = projectActionService.getByCode(actionCode);
				
				String projectProductId = projectAction.getProjectProductId();
				
				ProjectProduct projectProduct = projectProductService.getById(projectProductId);
				
				projectProduct.setAreaName(areaName);
				
				projectProduct.setOrganizationSeq(organizationSeq);
				
				projectProduct.setProvince(province);
				
				projectProduct.setProvinceName(provinceName);
				
				projectProduct.setCity(city);
				
				projectProduct.setCityName(cityName);
				
				projectProduct.setCountryName(countryName);
				
				projectProduct.setAreaAddress(areaAddress);
				
				projectProduct.setFieldExchangeTime(new Date());
				
				//????????????????????????????????????????????????????????????????????????
				/*// ?????????????????????????????????
				List<DepartmentBusinessType> departmentBusinessTypeList=departmentBusinessService.findByBusinessAreaCity(city,"1");
				if (departmentBusinessTypeList.size() < 1) {// ????????????????????????????????????????????????????????????
					DepartmentBusinessType provinceDBT = this.departmentBusinessService.findByBusinessAreaProvince("1");
					if (provinceDBT != null) {
						departmentBusinessTypeList.add(provinceDBT);
					} else {// ???????????????????????????
						DepartmentBusinessType stateDBT = this.departmentBusinessService.findByBusinessAreaState("1");
						if (stateDBT != null) {
							departmentBusinessTypeList.add(stateDBT);
						}
					}
				}
				
				if(null!=departmentBusinessTypeList && departmentBusinessTypeList.size()>0){
					projectAction.setDepartmentId(departmentBusinessTypeList.get(0).getDepartmentId());
					projectAction.setDepartmentName(departmentBusinessTypeList.get(0).getDepartmentName());
				}*/
				
				projectActionExceptionService.substitution(exceptionId, projectProduct, userName, userAccount,projectAction);
				
				resultMap.put("success", true);
			}
			

		} catch (Exception e) {

			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/stopProjectAction", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String stopProjectAction(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			HttpSession session = request.getSession();
			
			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType=(String) session.getAttribute("userType");
			
			if((!"0".equals(userType))&&roleType!=null&&roleType!=1){
				
				logger.info("????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "????????????????????????????????????");
			
			}else{
				
				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				
				String exceptionId = request.getParameter("exceptionId");
				
				String money = request.getParameter("money");
				
				String resultRemark = request.getParameter("resultRemark");
				
				projectActionExceptionService.stopProjectAction(exceptionId, resultRemark, userName, userAccount,money);
				
				logger.info("??????ID:" + exceptionId + "????????????????????????????????????????????????????????????");
				
				resultMap.put("success", true);
				
			}
			
			

		} catch (Exception e) {

			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());
		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/goOnProjectAction", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String goOnProjectAction(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			HttpSession session = request.getSession();
			
			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType=(String) session.getAttribute("userType");
			
			if((!"0".equals(userType))&&roleType!=null&&roleType!=1){
				
				logger.info("????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "????????????????????????????????????");
			
			}else{
				
				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				
				String exceptionId = request.getParameter("exceptionId");
				
				String resultRemark = request.getParameter("resultRemark");
				
				projectActionExceptionService.goOnProjectAction(exceptionId, resultRemark, userName, userAccount);
				
				logger.info("??????ID:" + exceptionId + "????????????????????????????????????????????????????????????");
				
				resultMap.put("success", true);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pauseProjectAction", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String pauseProjectAction(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			HttpSession session = request.getSession();
			
			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType=(String) session.getAttribute("userType");
			
			if((!"0".equals(userType))&&roleType!=null&&roleType!=1){
				
				logger.info("??????????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "??????????????????????????????????????????");
			
			}else{
				
				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				
				String exceptionId = request.getParameter("exceptionId");
				
				String resultRemark = request.getParameter("resultRemark");
				
				projectActionExceptionService.pauseProjectAction(exceptionId, resultRemark, userName, userAccount);
				
				logger.info("??????ID:" + exceptionId + "????????????????????????????????????????????????????????????");
				
				resultMap.put("success", true);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deduction", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String deduction(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			HttpSession session = request.getSession();
			
			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType=(String) session.getAttribute("userType");
			
			if((!"0".equals(userType))&&roleType!=null&&roleType!=1){
				
				logger.info("????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "????????????????????????????????????");
			
			}else{
				
				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				
				String exceptionId = request.getParameter("exceptionId");
				
				String resultRemark = request.getParameter("resultRemark");
				
				String money = request.getParameter("money");
				
				projectActionExceptionService.deduction(exceptionId, resultRemark, money, userName, userAccount);
				
				logger.info("??????ID:" + exceptionId + "????????????????????????????????????????????????????????????");
				
				resultMap.put("success", true);
			
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);

		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reportException", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String reportException(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			logger.info("---???---???---???---???---");
			String projectActionId = request.getParameter("projectActionId");
			if (StringUtils.isBlank(projectActionId)) {
				throw new Exception("??????????????????id???????????????");
			}
			
			ProjectAction pa = projectActionService.getById(projectActionId);
			
			// ?????????????????????????????????
			ContractVo contract = contractService.getExecuteContractByStatus(pa.getProjectCode(), 7);
			if (contract == null) {
				resultMap.put("success", false);
				resultMap.put("hasNoContract", true);
				return JSON.toJSONString(resultMap);
			}
			BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByContractCodeAndStatus(contract.getContractCode(), 1);
			if (backMoneyPlan == null) {// ?????????????????????????????????
				resultMap.put("success", false);
				resultMap.put("hasNobackMoneyPlan", true);
				return JSON.toJSONString(resultMap);
			}
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/saveException", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String saveException(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			logger.info("???????????????????????????");
			String projectActionId = request.getParameter("projectActionId");
			String exRemark = request.getParameter("exRemark");
			ProjectAction pa = projectActionService.getById(projectActionId);
			
			ProjectActionException pae = new ProjectActionException();
			String exceptionId = UUIDGeneratorUtil.generateUUID();
			pae.setId(exceptionId);
			pae.setProjectCode(pa.getProjectCode());
			pae.setType((short) 1);
			pae.setProjectActionCode(pa.getActionCode());
			pae.setExceptionRemark(exRemark);
			pae.setCreateTime(new Date());
			
			UserDetailLoginVo userDetailVo = (UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
			pae.setDepartmentId(userDetailVo.getDepartId());
			pae.setDepartmentName(userDetailVo.getDepartName());
	
			ProjectProduct pp = projectProductService.getById(pa.getProjectProductId());
			pae.setBusinessType(pp.getBusinessType());
			pae.setStatus((short) 0);
	
			String userAccount = (String) request.getSession().getAttribute("userAccount");
			String userName = (String) request.getSession().getAttribute("userName");
			pae.setCreaterAccount(userAccount);
			pae.setCreaterName(userName);
			projectActionExceptionService.insertExcuteException(pae);
	
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * ?????????????????????/??????/????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/ymdActionList")
	public String goYMDPAList(HttpServletRequest request, HttpServletResponse response) {
		String projectActionCode = request.getParameter("projectActionCode");
		String jumpStr = request.getParameter("jumpStr");
		request.setAttribute("projectActionCode", projectActionCode);
		request.setAttribute("jumpStr", jumpStr);

		// ??????????????????
		String projectActionCodeForPending = request.getParameter("projectActionCodeForPending");
		String pendingParamType = request.getParameter("pendingParamType");
		if ("1".equals(pendingParamType)) {
			request.getSession().setAttribute("projectActionCodeForPending", projectActionCodeForPending);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}

		return "/projectAction/ymd/actionList.jsp";
	}

	/**
	 * ????????????/??????/??????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getYMDPAListData", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String ymdPAListData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("projectActionQueryBean");

			ProjectActionQueryBean queryBean = JSON.parseObject(parameter, ProjectActionQueryBean.class);
			if (queryBean == null) {
				queryBean = new ProjectActionQueryBean();
			}
			logger.info("*********" + queryBean.toString());
			
			byte dataArea = (byte) request.getSession().getAttribute("dataArea");
			queryBean.setDataArea(dataArea);
			if (dataArea == 0) {// ????????????????????????0??????????????????1??????????????????2???????????????
				List<String> userList = (List<String>) request.getSession().getAttribute("userList");
				queryBean.setUserList(userList);
				UserDetailLoginVo userDetailVo = (UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
				queryBean.setDepartmentId(userDetailVo.getDepartId());
				queryBean.setDepartmentName(userDetailVo.getDepartName());
			} else if (dataArea == 1) {
				List<String> departList = (List<String>) request.getSession().getAttribute("departList");
				queryBean.setDepartList(departList);
			}

			// ??????????????????
			String pendingParamType = (String) request.getSession().getAttribute("pendingParamType");
			if ("1".equals(pendingParamType) && pendingParamType != null) {
				String projectActionCodeForPending = (String) request.getSession()
						.getAttribute("projectActionCodeForPending");
				queryBean.setPageNumber(1);
				queryBean.setPageSize(10);
				queryBean.setActionCode(projectActionCodeForPending);

			}

			PageInfo<Map<String, Object>> pageInfo = projectActionService.listYMDAction(queryBean);
			if (pageInfo == null) {
				resultMap.put("success", false);
			} else {
				resultMap.put("success", true);
				resultMap.put("rows", pageInfo.getList());
				resultMap.put("total", pageInfo.getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);
		} finally {
			// ??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("projectActionCodeForPending");
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMGData", produces = "text/html;charset=UTF-8;")
	public void export(HttpServletRequest request, HttpServletResponse response) {
		String parameter = request.getParameter("projectActionQueryBean");
		ProjectActionQueryBean queryBean = JSON.parseObject(parameter, ProjectActionQueryBean.class);
		if (queryBean == null) {
			queryBean = new ProjectActionQueryBean();
		}
		byte dataArea = (byte) request.getSession().getAttribute("dataArea");
		queryBean.setDataArea(dataArea);
		if (dataArea == 0) {// ????????????????????????0??????????????????1??????????????????2???????????????
			List<String> userList = (List<String>) request.getSession().getAttribute("userList");
			queryBean.setUserList(userList);
			UserDetailLoginVo userDetailVo = (UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
			queryBean.setDepartmentId(userDetailVo.getDepartId());
			queryBean.setDepartmentName(userDetailVo.getDepartName());
		} else if (dataArea == 1) {
			List<String> departList = (List<String>) request.getSession().getAttribute("departList");
			queryBean.setDepartList(departList);
		}

		List<ExportMGVo> mgVos = projectProductMenuAreaService.listMGData(queryBean);
		if (mgVos != null && mgVos.size() > 0) {
			// ??????????????????
			Set<String> organizationSeqs = new HashSet<String>();
			for (ExportMGVo exportMGVo : mgVos) {
				organizationSeqs.add(exportMGVo.getOrganizationSeq());
			}
			// Map<String, AreaData> areaData =
			// areaDataService.getAreaDataByOrganizationSeqs(organizationSeqs);

			// ????????????
			String title = "??????????????????????????????";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = "??????????????????????????????." + sdf.format(new Date()) + ".xls";
			String[] headers = { "????????????", "????????????", "??????????????????", "??????????????????", "???????????????", "????????????", "????????????", "????????????" };
			ExportExcelUtil.experotExcelFor2003(title, headers, mgVos, fileName, new PoiCell() {
				@Override
				public void setCellData(Object obj, HSSFRow row, HSSFCellStyle style) {
					ExportMGVo exportMGVo = (ExportMGVo) obj;

					int i = 0;
					// ????????????
					HSSFCell cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(exportMGVo.getAreaName());
					// ????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(exportMGVo.getOrganizationSeq());
					// ??????????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(sdf.format(exportMGVo.getBeginTime()));
					// ??????????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(sdf.format(exportMGVo.getEndTime()));
					// ???????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					// cell.setCellValue(areaData.get(exportMGVo.getOrganizationSeq()).getAdvertisingTerminalNum());
					cell.setCellValue(10);
					// ????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(exportMGVo.getProductType());
					// ????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(exportMGVo.getProductMenu());
					// ????????????
					cell = row.createCell(i++);
					cell.setCellStyle(style);
					cell.setCellValue(exportMGVo.getProductSpec());
				}
			}, request, response);
		}
	}
}
