package com.hori.grms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.model.ProjectPeople;
import com.hori.grms.model.ProjectProductMenuArea;
import com.hori.grms.page.AjaxResult;
import com.hori.grms.page.PageResult;
import com.hori.grms.queryBean.ProjectActionDetailQueryBean;
import com.hori.grms.queryBean.ProjectActionQueryBean;
import com.hori.grms.queryBean.ZhUmbQueryBean;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectPeopleService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.util.ExportExcelUtil.PoiCell;
import com.hori.grms.vo.CommunityProjectActionVo;
import com.hori.grms.vo.ExportMGVo;
import com.hori.grms.vo.ProjectActionDetailVo;
import com.hori.grms.vo.ProjectDetailsVo;
import com.hori.grms.vo.ProjectProductMenuAreaVo;
/**
 * ??????????????????????????????
 * @author dell
 *
 */
@Controller
@RequestMapping("/zhongheDepart")
public class ZhongheDepartController{
	private final Logger logger = LoggerFactory.getLogger(ProjectActionController.class);
	@Autowired
	private ProjectActionService projectActionService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectPeopleService projectPeopleService;
	
	//????????????
	@RequestMapping("/sq")
	public String sqIndex(){
		//return "/projectAction/zhongheDepartList.jsp";
		return "/projectAction/zhongheSqList.jsp";
	}
	//????????????????????????
	@RequestMapping("/umb")
	public String index(){
		return "/projectAction/zhongheUmbNewList.jsp";
	}
	
	/**
	 * ????????????????????????????????????
	 * @param queryBean
	 * @return
	 */
	/*@RequestMapping("/umbList")
	@ResponseBody
	public PageResult umbList(ZhUmbQueryBean qo){
		System.out.println(qo.toString());
		PageResult pageResult = null;
		pageResult = projectActionService.queryUmbPage(qo);
		return pageResult;
	}*/
	@RequestMapping(value = "/umbListNew", produces = "text/html;charset=UTF-8;")
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

			PageInfo<Map<String, Object>> pageInfo = projectActionService.listUmbAction(queryBean);
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
		} 
		return JSON.toJSONString(resultMap);
	}
	
	
	/**
	 * ??????????????????????????????
	 * @param queryBean
	 * @return
	 */
	/*@RequestMapping("/sqList")
	@ResponseBody
	public PageResult sqList(ProjectActionQueryBean qo){
		PageResult pageResult = null;
		pageResult = projectActionService.queryPage(qo);
		return pageResult;
	}*/
	@RequestMapping(value = "/sqListNew", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String sqList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String parameter = request.getParameter("projectActionQueryBean");

			ProjectActionQueryBean queryBean = JSON.parseObject(parameter, ProjectActionQueryBean.class);
			if (queryBean == null) {
				queryBean = new ProjectActionQueryBean();
			}
			logger.info("*********" + queryBean.toString());
	
			PageInfo<CommunityProjectActionVo> pageInfo = projectActionService.listZHCommunityAction(queryBean);
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
		} 
		return JSON.toJSONString(resultMap);
	}
	
	
	
	
	//?????????????????????
	@RequestMapping("/actionDetail")
	public String actionDetail(HttpServletRequest request, String projectCode){
		String actionCode = request.getParameter("actionCode");//??????????????????
		ProjectAction projectAction = projectActionService.getByCode(actionCode);
		//????????????id??????????????????
		//String type = projectActionService.getTypeByDeptId(projectAction.getDepartmentId());
		if (actionCode!=null&&actionCode!="") {
			//????????????????????????????????????
			ProjectDetailsVo projectVo =  projectService.findProjectDetailsVoByProjectCode(projectAction.getProjectCode());
			List<ProjectPeople> projectPeopleList = projectPeopleService.findByProjectCode(projectAction.getProjectCode()); // ??????????????????
			projectVo.setRoles(projectPeopleList);
			request.setAttribute("actionCode", actionCode);
			request.setAttribute("projectVo", projectVo);
			
		}
		//String projectCode = (String) request.getAttribute("projectCode");
		System.out.println(projectCode);
		if (projectCode!=null&&projectCode!="") {//????????????
			ProjectDetailsVo projectVo =  projectService.findProjectDetailsVoByProjectCode(projectCode);
			List<ProjectPeople> projectPeopleList = projectPeopleService.findByProjectCode(projectCode); // ??????????????????
			projectVo.setRoles(projectPeopleList);
			request.setAttribute("projectVo", projectVo);
			request.setAttribute("projectCode", projectCode);
			//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			return "/projectAction/projectActionYWDetail.jsp";
		}else if (projectActionService.getTypeByActionCode(actionCode).equals("1")) {//????????????????????????-????????????
			return "/projectAction/projectActionSQDetail.jsp";
		}else {
			return "/projectAction/projectActionDetail.jsp";
		}
	}

	/**
	 * ????????????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getActionData")
	@ResponseBody
	public PageResult viewProject(ProjectActionDetailQueryBean qb,HttpServletRequest request){
		Byte roleType = (Byte) request.getSession().getAttribute("roleType");
		//System.out.println(roleType);
		//???????????????1???????????? 2 ?????? 3???????????? 4???????????????
		/*if (roleType.equals("4")) {//????????????
			qb.setBusinessType("1");
		}else if (roleType.equals("5")) {//????????????
			qb.setBusinessType("4");
		}else if (roleType.equals("6")) {//????????????
			qb.setBusinessType("3");
		}else if (roleType.equals("7")) {//??????
			qb.setBusinessType("2");
		}else if (roleType.equals("8")) {//????????????
			
		}else {
			qb.setBusinessType("");
			//return null;
		}*/
		PageResult pageResult = null;
		pageResult = projectActionService.queryActionData(qb);
		return pageResult;
	}
	/**
	 * ????????????-????????????????????????
	 * @param request ??????????????????????????????2?????????
	 * @param response
	 * @return
	 */
	@RequestMapping("/getActionDataYW")
	@ResponseBody
	public PageResult viewProjectYW(ProjectActionDetailQueryBean qb,HttpServletRequest request){
		PageResult pageResult = null;
		pageResult = projectActionService.queryActionDataYW(qb);
		return pageResult;
	}
	@RequestMapping("/getActionAttachmentData")
	@ResponseBody
	public PageResult viewProjeActionAttachment(ProjectActionDetailQueryBean qb,HttpServletRequest request){
		PageResult pageResult = null;
		if (qb.getActionCode()!=null&&qb.getActionCode()!="") {
			ProjectAction projectAction = projectActionService.getByCode(qb.getActionCode());
			qb.setProjectCode(projectAction.getProjectCode());
			//pageResult = projectActionService.queryAttachmentData(qb);
		}
			pageResult = projectActionService.queryAllAttachmentData(qb);
		
		return pageResult;
	}
	
	@RequestMapping("/uploadAttachment")
	@ResponseBody
	public AjaxResult update(Attachment attachment){
		AjaxResult result = null;
		try{
			attachment.setId(UUID.randomUUID().toString().replace("-", ""));
			projectActionService.insert(attachment);
			result = new AjaxResult(true,"????????????");
		}catch(Exception e){
			e.printStackTrace();
			result = new AjaxResult(false,"???????????????");
		}
		return result;
	}
	
	@RequestMapping("/showArea")
	@ResponseBody
	public String showArea(String projectProductMenuId,HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<ProjectProductMenuAreaVo> ppmavo = projectActionService.getShowAreaDataByPPMID(projectProductMenuId);
			request.setAttribute("areaList", ppmavo);
			resultMap.put("success", true);
			resultMap.put("areaList", ppmavo);
		} catch (Exception e) {
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	@RequestMapping("/deleteAttatment")
	@ResponseBody
	public AjaxResult deleteAttatment(String id){
		AjaxResult result = null;
		try{
			projectActionService.deleteAttatment(id);
			result = new AjaxResult(true,"????????????");
		}catch(Exception e){
			e.printStackTrace();
			result = new AjaxResult(false,"???????????????");
		}
		return result;
	}
	
	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMGData", produces = "text/html;charset=UTF-8;")
	public void export(HttpServletRequest request, HttpServletResponse response,ProjectActionDetailQueryBean qb) {
		List<ProjectActionDetailVo> vos = projectActionService.queryActionDataList(qb);
		if(projectActionService.getTypeByActionCode(qb.getActionCode()).equals("1")) {//????????????????????????????????????????????????????????????
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("????????????");
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
			// ????????????????????????
			String[] headers = { "????????????","????????????", "??????", "????????????", "????????????", "????????????", "??????????????????", "??????????????????", "????????????" };
			SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy???MM???dd???   HH:mm:ss");
			HSSFRow headerRow = sheet.createRow(0);
			HSSFRichTextString text;
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = headerRow.createCell(i);
				cell.setCellStyle(headerStyle);
				text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			int rowNum = 1;
			sheet.addMergedRegion(
					new CellRangeAddress(rowNum, vos.size(), 0, 0));
			sheet.addMergedRegion(
					new CellRangeAddress(rowNum, vos.size(), 1, 1));
			sheet.addMergedRegion(
					new CellRangeAddress(rowNum, vos.size(), 8, 8));
			for (ProjectActionDetailVo vo : vos) {
				if(rowNum==1) {
					int s = 0;
					HSSFRow row = sheet.createRow(rowNum++);
					// ????????????
					HSSFCell cell = row.createCell(s++);
					cell.setCellStyle(commonStyle);
					if (vo.getBusinessType().equals("1")) {
						cell.setCellValue("????????????");
					}else if (vo.getBusinessType().equals("2")) {
						cell.setCellValue("?????? ");
					}else if (vo.getBusinessType().equals("3")) {
						cell.setCellValue("????????????");
					}else {
						cell.setCellValue("????????????");
					}
					// ????????????
					cell = row.createCell(s++);
					cell.setCellStyle(commonStyle);
					cell.setCellValue(vo.getFieldName());
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
					cell.setCellValue(vo.getAreaName());
				}else {
					int s = 0;
					HSSFRow row = sheet.createRow(rowNum++);
					// ????????????
					HSSFCell cell = row.createCell(s++);
					cell.setCellStyle(commonStyle);
					// ????????????
					cell = row.createCell(s++);
					cell.setCellStyle(commonStyle);
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
					
				}
			}
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
				if (sheet.getColumnWidth(i) < 20 * 256) {
					sheet.setColumnWidth(i, 20 * 256);
				}
			}
			ExportExcelUtil.downSpcialFile("????????????.xls", workbook, request, response);
			
		}else {
			if (vos != null && vos.size() > 0) {
				// ????????????
				String title = "????????????";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String fileName = "??????????????????." + sdf.format(new Date()) + ".xls";
				String[] headers = { "????????????", "??????","????????????","????????????", "????????????","??????????????????", "??????????????????", "??????????????????","????????????" };
				ExportExcelUtil.experotExcelFor2003(title, headers, vos, fileName, new PoiCell() {
					@Override
					public void setCellData(Object obj, HSSFRow row, HSSFCellStyle style) {
						ProjectActionDetailVo exportMGVo = (ProjectActionDetailVo) obj;

						int i = 0;
						// ????????????
						HSSFCell cell = row.createCell(i++);
						cell.setCellStyle(style);
						System.out.println(exportMGVo.getBusinessType());
						if (exportMGVo.getBusinessType().equals("1")) {
							cell.setCellValue("????????????");
						}else if (exportMGVo.getBusinessType().equals("2")) {
							cell.setCellValue("?????? ");
						}else if (exportMGVo.getBusinessType().equals("3")) {
							cell.setCellValue("????????????");
						}else {
							cell.setCellValue("????????????");
						}
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
						// ????????????
						cell = row.createCell(i++);
						cell.setCellStyle(style);
						cell.setCellValue(exportMGVo.getBuyNum());
						
						// ??????????????????
						cell = row.createCell(i++);
						cell.setCellStyle(style);
						cell.setCellValue(sdf.format(exportMGVo.getBeginTime()));
						// ??????????????????
						cell = row.createCell(i++);
						cell.setCellStyle(style);
						cell.setCellValue(sdf.format(exportMGVo.getEndTime()));
						// ??????????????????
						cell = row.createCell(i++);
						cell.setCellStyle(style);
						cell.setCellValue(exportMGVo.getAreaNum());
						// ????????????
						cell = row.createCell(i++);
						cell.setCellStyle(style);
						StringBuffer sb = new StringBuffer();
						for (int j = 0; j < exportMGVo.getAreas().size(); j++) {
							sb.append(exportMGVo.getAreas().get(j));
							sb.append("???");
						}
						cell.setCellValue(sb.toString());
					}
				}, request, response);
			}
		}
		
		
		
	}
	
	
	
}
