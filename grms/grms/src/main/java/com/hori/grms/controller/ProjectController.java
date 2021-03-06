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

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectApproveLog;
import com.hori.grms.model.ProjectPeople;
import com.hori.grms.pageModel.Json;
import com.hori.grms.queryBean.ProjectQueryBean;
import com.hori.grms.service.AreaDataService;
import com.hori.grms.service.AttachmentService;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.service.ContractService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectApproveLogService;
import com.hori.grms.service.ProjectPeopleService;
import com.hori.grms.service.ProjectProductService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.vo.AreaDataRspVo;
import com.hori.grms.vo.BackMoneyPlanVo;
import com.hori.grms.vo.ProjectActionVo;
import com.hori.grms.vo.ProjectDetailsVo;
import com.hori.grms.vo.project.ProductMenuVo;
import com.hori.grms.vo.project.ProjectMenuVo;
import com.hori.grms.vo.project.ProjectVo;
import com.hori.vo.UserDetailLoginVo;

/**
 * ????????????controller
 * 
 * @author Chenrw
 * @date 2018???8???9???
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectPeopleService projectPeopleService;
	@Autowired
	private ProjectProductService projectProductService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ProjectActionService projectActionService;
	@Autowired
	private ProjectApproveLogService projectApproveLogService;
	@Autowired
	private AreaDataService areaDataService;
	@Autowired
	private CloseCaseService closeCaseService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BackMoneyPlanService backMoneyPlanService;

	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public String getProjectList(HttpServletRequest request, HttpServletResponse response) {
		// ??????????????????
		String projectCode = request.getParameter("projectCode");
		String projectStatus = request.getParameter("projectStatus");
		String pendingParamType = request.getParameter("pendingParamType");
		if ("1".equals(pendingParamType)) {
			String pendingStatus = "1";
			if ("0".equals(projectStatus)) {
				pendingStatus = "2";
			} else if ("1".equals(projectStatus)) {
				pendingStatus = "3";
			} else if ("2".equals(projectStatus)) {
				pendingStatus = "4";
			} else if ("3".equals(projectStatus)) {
				pendingStatus = "6";
			} else if ("4".equals(projectStatus)) {
				pendingStatus = "5";
			}
			request.setAttribute("pendingStatus", pendingStatus);
			request.getSession().setAttribute("projectCode", projectCode);
			request.getSession().setAttribute("projectStatus", projectStatus);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}

		HttpSession session = getSession();
		session.removeAttribute("projectQueryBeanS");
		Byte roleType = (Byte) session.getAttribute("roleType");
		String userType = (String) session.getAttribute("userType");

		request.setAttribute("userType", userType);
		request.setAttribute("roleType", roleType);

		return "/project/projectList.jsp";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/goback")
	public String goback(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = getSession();
		String querryBean = (String) session.getAttribute("projectQueryJsonStr");

		ProjectQueryBean projectQueryBean = JSON.parseObject(querryBean, ProjectQueryBean.class);

		Byte roleType = (Byte) session.getAttribute("roleType");

		request.setAttribute("roleType", roleType);

		String userType = (String) session.getAttribute("userType");

		request.setAttribute("userType", userType);

		request.setAttribute("querryBean", projectQueryBean);

		return "/project/projectList.jsp";
	}

	/**
	 * ????????????????????????
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

			HttpSession session = getSession();
			String userAccount = (String) session.getAttribute("userAccount");
			Byte roleType = (Byte) session.getAttribute("roleType");
			Byte dataArea = (Byte) session.getAttribute("dataArea");

			ProjectQueryBean projectQueryBean = JSON.parseObject(parameter, ProjectQueryBean.class);

			if (null == projectQueryBean) {
				projectQueryBean = new ProjectQueryBean();
			}

			if (null != dataArea && null != roleType && dataArea == 0 && roleType == 0) {

				projectQueryBean.setAccount(userAccount);

			}

			if (null != roleType) {
				projectQueryBean.setRoleType(roleType);
			}

			// ??????????????????
			String pendingParamType = (String) request.getSession().getAttribute("pendingParamType");
			if ("1".equals(pendingParamType) && pendingParamType != null) {
				String projectCode = (String) request.getSession().getAttribute("projectCode");
				String projectStatus = (String) request.getSession().getAttribute("projectStatus");
				projectQueryBean.setPageNumber(1);
				projectQueryBean.setPageSize(10);
				projectQueryBean.setProductCode(projectCode);
				projectQueryBean.setStatus(Integer.parseInt(projectStatus));

			}

			// ????????????,????????????
			session.setAttribute("projectQueryJsonStr", parameter);
			if (null != dataArea && null != roleType && dataArea != 2 && roleType != 0 && roleType != 1) {
				resultMap.put("success", true);
				resultMap.put("rows", new ArrayList<Project>());
				resultMap.put("total", 0);
			} else {

				List<Project> list = projectService.list(projectQueryBean);

				int total = projectService.total(projectQueryBean);

				resultMap.put("success", true);
				resultMap.put("rows", list);
				resultMap.put("total", total);

			}

			logger.info("????????????????????????");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("???????????????" + e.getMessage());
			resultMap.put("success", false);

		} finally {
			// ??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("projectCode");
			request.getSession().removeAttribute("projectStatus");
		}

		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd");
	}

	/**
	 * 
	 * @param type
	 *            0-??????,1-??????
	 * @param productCode
	 * @return
	 */
	@RequestMapping(value = "/addProject", produces = "text/html;charset=utf-8")
	public String addProject(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "productCode", required = false) String productCode) {
		HttpSession session = getSession();
		HttpServletRequest request = getRequest();

		// ??????
		session.removeAttribute("projectVoStr"); // ????????????
		session.removeAttribute("project1Str"); // 1
		session.removeAttribute("project2Str"); // 2
		session.removeAttribute("project3Str"); // 3
		session.removeAttribute("project4Str"); // 4
		session.removeAttribute("attchmentsFromStr"); // ??????????????????
		session.removeAttribute("projectMeauList"); // ??????????????????
		session.removeAttribute("attchmentsFromRMStr"); // ??????????????????
		if (type == 1) {
			// ??????,?????????????????????
			ProjectVo projectVo = projectService.findProjectModel(productCode);

			request.setAttribute("projectVo", projectVo);
			session.setAttribute("projectVoStr", JSON.toJSONString(projectVo));

			List<ProjectMenuVo> projectMs = projectProductService.findVoByProjectCode(productCode);

			List<ProjectMenuVo> project1 = new ArrayList<>();

			for (ProjectMenuVo pv : projectMs) {
				if (pv.getBusinessType().trim().equals("1")) {
					project1.add(pv);
				}

				// ??????2-????????????
				if (pv.getBusinessType().trim().equals("2")) {
					session.setAttribute("project2Str", JSON.toJSONString(pv));
				}

				// ??????3-????????????
				if (pv.getBusinessType().trim().equals("3")) {
					System.out.println("3???" + JSON.toJSONString(pv));
					session.setAttribute("project3Str", JSON.toJSONString(pv));
				}

				// ??????4-????????????
				if (pv.getBusinessType().trim().equals("4")) {
					session.setAttribute("project4Str", JSON.toJSONString(pv));
				}
			}
			// ??????1-????????????
			session.setAttribute("project1Str", JSON.toJSONString(project1));

			// ?????????????????????json
			session.setAttribute("projectMeauList", JSON.toJSONStringWithDateFormat(projectMs, "yyyy???MM???dd???   HH:mm:ss",
					SerializerFeature.DisableCircularReferenceDetect));

			// ???????????? attchments
			List<Attachment> attachments = attachmentService.findBycorrelationId(productCode, 1);

			request.setAttribute("attchmentsFrom", attachments);
			session.setAttribute("attchmentsFromStr", JSON.toJSONString(attachments));

		}

		return "/project/addProject.jsp";
	}

	@RequestMapping("/addProjectAction")
	public String addProjectAction() {
		return "/project/addProjectProdect.jsp";
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/revoke", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String revokeProject(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType = (String) session.getAttribute("userType");

			if ((!"0".equals(userType)) && roleType != null && roleType != 0) {

				logger.info("??????????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "??????????????????????????????????????????");

			} else {
				String productCode = request.getParameter("productCode");

				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				projectService.revoke(productCode, userName, userAccount);
				logger.info("??????????????????" + productCode + "?????????????????????");
				resultMap.put("success", true);

			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());
		}

		return JSON.toJSONString(resultMap);
	}

	@RequestMapping("/viewProject")
	public String viewProject(HttpServletRequest request, HttpServletResponse response) {
		try {
			String projectCode = request.getParameter("projectCode");
			if (null != projectCode && !projectCode.equals("")) { // ??????ID????????????
				ProjectDetailsVo projectVo = projectService.findProjectDetailsVoByProjectCode(projectCode); // ??????????????????
				List<ProjectPeople> projectPeopleList = projectPeopleService.findByProjectCode(projectCode); // ??????????????????
				projectVo.setRoles(projectPeopleList);
				request.setAttribute("projectVo", projectVo);

				List<ProjectMenuVo> projectProductList = projectProductService.findVoByProjectCode(projectCode); // ??????????????????
				request.setAttribute("projectProductListStr",
						JSON.toJSONStringWithDateFormat(projectProductList, "yyyy???MM???dd???   HH:mm:ss"));
				
				List<Attachment> projectAttachmentList = attachmentService.findBycorrelationId(projectCode, 1); // ??????????????????
				request.setAttribute("projectAttachmentList", projectAttachmentList);

				List<ProjectActionVo> projectActionList = projectActionService
						.findProjectActionVoByProjectCode(projectCode); // ??????????????????
				request.setAttribute("projectActionListStr",
						JSON.toJSONStringWithDateFormat(projectActionList, "yyyy-MM-dd"));

				List<BackMoneyPlanVo> backMoneyPlanlist = backMoneyPlanService
						.getBackMoneyPlanListByProjectCode(projectCode);// ??????????????????
				request.setAttribute("backMoneyPlanlistStr",
						JSON.toJSONStringWithDateFormat(backMoneyPlanlist, "yyyy???MM???dd???   HH:mm:ss"));

				List<Attachment> closeAttachmentList = attachmentService.findBycorrelationIdForClose(projectCode, 4); // ??????????????????
				request.setAttribute("closeAttachmentList", closeAttachmentList);

				List<ProjectApproveLog> projectApproveLogList = projectApproveLogService.findByProjectCode(projectCode); // ??????????????????
				request.setAttribute("projectApproveLogList", projectApproveLogList);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/project/projectView.jsp";
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delet", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String deletProject(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			HttpSession session = request.getSession();

			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType = (String) session.getAttribute("userType");

			if ((!"0".equals(userType)) && roleType != null && roleType != 0) {

				logger.info("??????????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "??????????????????????????????????????????");

			} else {

				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				String productCode = request.getParameter("productCode");
				logger.info("??????????????????" + productCode + "?????????????????????");
				projectService.delet(productCode, userName, userAccount);
				resultMap.put("success", true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());
		}

		return JSON.toJSONString(resultMap);
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/stopProject", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String stopProject(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			HttpSession session = request.getSession();

			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType = (String) session.getAttribute("userType");

			if ((!"0".equals(userType)) && roleType != null && roleType != 1) {

				logger.info("??????????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "??????????????????????????????????????????");

			} else {

				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");

				String productCode = request.getParameter("productCode");

				String remark = request.getParameter("remark");

				String flag = request.getParameter("flag");

				if ("1".equals(flag)) {

					boolean result = projectService.stop(productCode);

					resultMap.put("success", result);

				} else {

					projectService.stopProject(productCode, remark, userName, userAccount);
					logger.info("??????????????????" + productCode + "?????????????????????");
					resultMap.put("success", true);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());
		}

		return JSON.toJSONString(resultMap);
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exam", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String examProject(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			HttpSession session = request.getSession();

			Byte roleType = (Byte) session.getAttribute("roleType");

			String userType = (String) session.getAttribute("userType");

			if ((!"0".equals(userType)) && roleType != null && roleType != 1) {

				logger.info("??????????????????????????????????????????");
				resultMap.put("success", false);
				resultMap.put("msg", "??????????????????????????????????????????");

			} else {

				String productCode = request.getParameter("productCode");

				String remark = request.getParameter("remark");

				String flag = request.getParameter("flag");

				String userAccount = (String) session.getAttribute("userAccount");
				String userName = (String) session.getAttribute("userName");
				short examResult = Short.valueOf(request.getParameter("examResult"));

				List<ProjectMenuVo> projectMenuVos = projectProductService.findVoByProjectCode(productCode);

				if (!(projectMenuVos.size() > 0)) {

					resultMap.put("success", false);
					resultMap.put("msg", "?????????????????????????????????????????????????????????");

				} else {

					if ("1".equals(flag)) {

						boolean result = projectService.examDate(productCode, examResult);

						resultMap.put("success", result);
						if (!result) {
							resultMap.put("msg", "??????????????????????????????????????????????????????");
						}

					} else {

						projectService.examProject(productCode, remark, userName, userAccount, examResult);
						logger.info("??????????????????" + productCode + "???????????????????????????");
						resultMap.put("success", true);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", e.getMessage());
		}

		return JSON.toJSONString(resultMap);
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/createProject", produces = "text/html;charset=utf-8")
	@ResponseBody
	public String createProject(@RequestBody ProjectVo projectVo) {
		Json json = new Json();
		try {
			HttpSession session = getSession();
			String project1Str = (String) session.getAttribute("project1Str");

			List<ProjectMenuVo> project1 = null;
			if (StringUtils.isNotBlank(project1Str)) {
				project1 = JSON.parseArray(project1Str, ProjectMenuVo.class);
			}
			String project2Str = (String) session.getAttribute("project2Str");
			String project3Str = (String) session.getAttribute("project3Str");
			String project4Str = (String) session.getAttribute("project4Str");

			ProjectMenuVo project2 = getProjectMenuVo(project2Str);
			ProjectMenuVo project3 = getProjectMenuVo(project3Str);
			ProjectMenuVo project4 = getProjectMenuVo(project4Str);

			List<Attachment> attachments = null;
			String attchmentsFromStr = (String) session.getAttribute("attchmentsFromStr"); // ????????????
			if (StringUtils.isNotBlank(attchmentsFromStr)) {
				attachments = JSON.parseArray(attchmentsFromStr, Attachment.class);
			}
			/**
			 * ????????????????????????
			 */
			Set<String> orgS = new HashSet<>();

			// ????????????
			List<ProjectMenuVo> listCheck = new ArrayList<>();

			if (project1 != null) {
				projectVo.setProject1(project1);
				for (ProjectMenuVo pmv : project1) {
					orgS.add(pmv.getOrganizationSeq());
					listCheck.add(pmv);
				}
			}
			if (project2 != null) {
				projectVo.setProject2(project2);
				for (ProductMenuVo pdmv : project2.getProjectMenus()) {
					String[] orgStr = pdmv.getAreaIds().split(",");
					for (String str : orgStr) {
						orgS.add(str.trim());
					}
					listCheck.add(project2);
				}
			}
			if (project3 != null) {
				projectVo.setProject3(project3);
				for (ProductMenuVo pdmv : project3.getProjectMenus()) {
					String[] orgStr = pdmv.getAreaIds().split(",");
					for (String str : orgStr) {
						orgS.add(str.trim());
					}
				}
				listCheck.add(project3);
			}
			if (project4 != null) {
				projectVo.setProject4(project4);
				for (ProductMenuVo pdmv : project4.getProjectMenus()) {
					String[] orgStr = pdmv.getAreaIds().split(",");
					for (String str : orgStr) {
						orgS.add(str.trim());
					}
				}
				listCheck.add(project4);
			}
			if (listCheck.size() > 0) {
				if (!projectService.checkDate(listCheck)) {
					throw new RuntimeException("???????????????,????????????");
				}
			}else{
				throw new RuntimeException("????????????,????????????");
			}

			// ???????????????????????????????????????,??????
			/*
			 * StringBuilder checkBug=new StringBuilder(); //??????????????????????????? boolean
			 * checkfalg=true; //?????????????????? if (listCheck.size() > 0) {
			 * Map<Integer,Set<String>> listMIS=
			 * projectService.checkDateSave(listCheck); if(listMIS!=null){
			 * Set<String> set2=listMIS.get(2); if(set2!=null&&set2.size()>0){
			 * checkBug.append("??????:"+pingSet(set2)+"</br>"); checkfalg=false; }
			 * Set<String> set3=listMIS.get(3); if(set3!=null&&set3.size()>0){
			 * checkBug.append("????????????:"+pingSet(set3)+"</br>"); checkfalg=false;
			 * } Set<String> set4=listMIS.get(4); if(set4!=null&&set4.size()>0){
			 * checkBug.append("????????????:"+pingSet(set4)+"</br>"); checkfalg=false;
			 * } } } if(!checkfalg){ throw new
			 * RuntimeException(checkBug.toString()); }
			 */

			if (attachments != null)
				projectVo.setAttchments(attachments);
			Map<String, AreaDataRspVo.AreaData> map = areaDataService.getAreaDataByOrganizationSeqs(orgS);

			projectService.saveProject(projectVo, (String) session.getAttribute("userAccount"),
					(String) session.getAttribute("userName"),
					((UserDetailLoginVo) session.getAttribute("userDetailVo")).getDepartId(), map);

			json.setSuccess(true);
			json.setMsg("????????????");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
		} finally {
			return JSON.toJSONString(json);
		}
	}

	/**
	 * ???set????????????????????????","??????
	 */
	private String pingSet(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String str : set) {
			sb.append("," + str.trim());
		}
		return sb.toString().substring(1);
	}

	private ProjectMenuVo getProjectMenuVo(String projectMenuVo) {
		if (StringUtils.isBlank(projectMenuVo)) {
			return null;
		}
		return JSON.parseObject(projectMenuVo, ProjectMenuVo.class);
	}

	@RequestMapping(value = "/exportDetail")
	public void exportDetail(HttpServletRequest request, HttpServletResponse response) {
		String projectCode = request.getParameter("projectCode");
		String projectType = request.getParameter("projectType");
		List<ProjectMenuVo> projectProductList = new ArrayList<>();
		if (null != projectCode && !projectCode.equals("")) { // ??????ID????????????

			projectProductList = projectProductService.findVoByProjectCode(projectCode);
		}
		if (null != projectType && projectType.trim().equals("1")) {
			HttpSession session = request.getSession();
			// 1
			String project1Str = (String) session.getAttribute("project1Str");
			if (StringUtils.isNotBlank(project1Str)) {
				List<ProjectMenuVo> list1 = JSON.parseArray(project1Str, ProjectMenuVo.class);
				if (list1 != null && list1.size() > 0) {
					for (ProjectMenuVo projectMenuVo : list1) {
						projectProductList.add(projectMenuVo);
					}
				}
			}
			// 2
			String project2Str = (String) session.getAttribute("project2Str");
			if (StringUtils.isNotBlank(project2Str)) {
				ProjectMenuVo p2 = JSON.parseObject(project2Str, ProjectMenuVo.class);
				projectProductList.add(p2);
			}
			// 3
			String project3Str = (String) session.getAttribute("project3Str");
			if (StringUtils.isNotBlank(project3Str)) {
				ProjectMenuVo p3 = JSON.parseObject(project3Str, ProjectMenuVo.class);
				projectProductList.add(p3);
			}
			// 4
			String project4Str = (String) session.getAttribute("project4Str");
			if (StringUtils.isNotBlank(project4Str)) {
				ProjectMenuVo p3 = JSON.parseObject(project4Str, ProjectMenuVo.class);
				projectProductList.add(p3);
			}

		}
		if (projectProductList != null && projectProductList.size() > 0) {

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("????????????");
			/*
			 * ################################??????????????????START######################
			 * ##########
			 */
			// ??????????????????
			HSSFFont headerfont = workbook.createFont();
			// ????????????
			headerfont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// ??????style??????
			HSSFCellStyle headerStyle = workbook.createCellStyle();
			// ???????????????????????????
			headerStyle.setFont(headerfont);

			// ??????????????????
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// ???????????????????????????
			headerStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);

			// ????????????
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

			// ??????????????????
			headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			/*
			 * ################################??????????????????END########################
			 * ########
			 */

			/*
			 * ################################???????????????????????????START###################
			 * #############
			 */
			// ????????????
			HSSFFont commonfont = workbook.createFont();
			HSSFCellStyle commonStyle = workbook.createCellStyle();
			commonStyle.setWrapText(true);
			commonStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????
			commonStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// ????????????
			commonStyle.setFont(commonfont);
			// ??????????????????
			commonStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// ????????????
			commonStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			commonStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			commonStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			commonStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			/*
			 * ################################???????????????????????????END#####################
			 * ###########
			 */
			// ????????????????????????
			// XSSFRow fristHeadRow = sheet.createRow(0);

			// ????????????????????????
			String[] headers = { "????????????", "??????", "??????", "????????????", "????????????", "????????????", "??????????????????", "??????????????????", "????????????" };
			HSSFRow headerRow = sheet.createRow(0);
			HSSFRichTextString text;
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = headerRow.createCell(i);
				cell.setCellStyle(headerStyle);
				text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			int rowNum = 1;
			SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy???MM???dd???");
			SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy???MM???dd???   HH:mm");
			for (ProjectMenuVo pvo : projectProductList) {
				if (pvo.getBusinessType().equals("1")) {
					sheet.addMergedRegion(
							new CellRangeAddress(rowNum, rowNum + (pvo.getProjectMenus().size() - 1), 0, 0));
					sheet.addMergedRegion(
							new CellRangeAddress(rowNum, rowNum + (pvo.getProjectMenus().size() - 1), 1, 1));
					sheet.addMergedRegion(
							new CellRangeAddress(rowNum, rowNum + (pvo.getProjectMenus().size() - 1), 8, 8));
					int row1 = 1;
					for (ProductMenuVo vo : pvo.getProjectMenus()) {
						if (row1 == 1) {
							int s = 0;
							HSSFRow row = sheet.createRow(rowNum++);
							// ????????????
							HSSFCell cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getFieldName());
							// ??????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getBusinessName());
							// ??????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductType());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductMenu());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductSpec());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getBuyNum());
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt2.format(vo.getBeginTime()));
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt2.format(vo.getEndTime()));
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getAreaName());
						} else {
							int s = 2;
							HSSFRow row = sheet.createRow(rowNum++);
							// ????????????

							// ??????
							HSSFCell cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductType());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductMenu());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductSpec());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getBuyNum());
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt2.format(vo.getBeginTime()));
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt2.format(vo.getEndTime()));
						}
					}
				} else {
					sheet.addMergedRegion(
							new CellRangeAddress(rowNum, rowNum + (pvo.getProjectMenus().size() - 1), 1, 1));
					int row1 = 1;
					for (ProductMenuVo vo : pvo.getProjectMenus()) {
						if (row1 == 1) {
							int s = 0;
							HSSFRow row = sheet.createRow(rowNum++);
							// ????????????
							HSSFCell cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getFieldName());
							// ??????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getBusinessName());
							// ??????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductType());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductMenu());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductSpec());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getBuyNum());
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt1.format(vo.getBeginTime()));
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt1.format(vo.getEndTime()));
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getAreaNames());
						} else {
							int s = 0;
							HSSFRow row = sheet.createRow(rowNum++);
							// ????????????
							HSSFCell cell = row.createCell(s++);
							s++; // ????????????
							cell.setCellStyle(commonStyle);
							cell.setCellValue(pvo.getFieldName());

							// ??????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductType());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductMenu());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getProductSpec());
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getBuyNum());
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt1.format(vo.getBeginTime()));
							// ??????????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(fmt1.format(vo.getEndTime()));
							// ????????????
							cell = row.createCell(s++);
							cell.setCellStyle(commonStyle);
							cell.setCellValue(vo.getAreaNames());
						}
					}
				}
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
				if (sheet.getColumnWidth(i) < 20 * 256) {
					sheet.setColumnWidth(i, 20 * 256);
				}
			}
			ExportExcelUtil.downSpcialFile("????????????.xls", workbook, request, response);
		}

	}

	/**
	 * ????????????
	 * 
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/download")
	public void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		try {
			if (StringUtils.isNotBlank(id)) {
				Attachment attachment = closeCaseService.getAttachmentById(id);
				if (attachment != null) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
					ResponseEntity<byte[]> res = restTemplate.exchange(attachment.getFileUrl(), HttpMethod.GET,
							httpEntity, byte[].class);
					if (res.getStatusCode() == HttpStatus.OK) {
						String fileName = attachment.getFileName();
						// ????????????????????????
						response.setContentType("application/octet-stream;charset=ISO-8859-1");
						// ?????????????????????
						String agent = request.getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("MSIE")
								|| null != agent && -1 != agent.indexOf("Trident")) {// ie
							fileName = java.net.URLEncoder.encode(fileName, "UTF8");
							response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
						} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// ??????,chrome???
							fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
							response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
						}
						response.getOutputStream().write(res.getBody());
					}
				}
			}
		} catch (Exception e) {
			response.getWriter().println("?????????????????????????????????????????????");
			e.printStackTrace();
		}
	}

}
