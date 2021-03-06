package com.hori.grms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.model.ProjectActionException;
import com.hori.grms.model.ProjectProduct;
import com.hori.grms.queryBean.BackMoneyPlanPeriodsQueryBean;
import com.hori.grms.queryBean.BackMoneyPlanQueryBean;
import com.hori.grms.service.BackMoneyPlanPeriodsService;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.service.ProjectActionExceptionService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.util.ExportExcelUtil.PoiCell;
import com.hori.grms.vo.BackMoneyPlanPeriodsVo;
import com.hori.grms.vo.BackMoneyPlanVo;
import com.hori.grms.vo.ContractDetailVo;
import com.hori.grms.vo.ContractVo;
import com.hori.grms.vo.project.ProductMenuVo;
import com.hori.grms.vo.project.ProjectMenuVo;
/**
 * ???????????????????????????
 * @author dell
 *
 */
@Controller
@RequestMapping("backMoneyPlanPeriods")
public class BackMoneyPlanPeriodsController{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BackMoneyPlanService backMoneyPlanService;
	@Autowired
	BackMoneyPlanPeriodsService backMoneyPlanPeriodsService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request){		
		
		return "/backMoneyPlan/backMoneyPlanPeriods.jsp";
	}
	
	//???????????????
	@RequestMapping("/getBackMoneyPlanPeriodsList")
	@ResponseBody
	public String getBackMoneyPlanList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String selectCondition = request.getParameter("selectCondition");
			String condition = request.getParameter("condition");
			//?????????????????????
			String selectCondition2 = request.getParameter("selectCondition2");
			String condition2 = request.getParameter("condition2");
			//??????????????????
			String userString="";
			String userType=(String)request.getSession().getAttribute("userType");
			byte roleType=(byte) request.getSession().getAttribute("roleType");
			if(roleType==0){//?????????
				//????????????
				//?????????
				byte dataArea=(byte) request.getSession().getAttribute("dataArea");
				
				if(dataArea!=2){
					List<String> userList=(List<String>)request.getSession().getAttribute("userList");
					for(int i=0;i<userList.size();i++){
						if(i==0){
							userString="'"+userList.get(i)+"'";
						}else{
							userString=userString+",'"+userList.get(i)+"'";
						}
					}
				}
			}
			BackMoneyPlanPeriodsQueryBean queryBean = new BackMoneyPlanPeriodsQueryBean();
			queryBean.setCondition(condition);
			queryBean.setCondition2(condition2);
			queryBean.setSelectCondition(selectCondition);
			queryBean.setSelectCondition2(selectCondition2);
			queryBean.setUserString(userString);
			List<BackMoneyPlanPeriodsVo> list = backMoneyPlanPeriodsService.getBackMoneyPlanListByCondition(queryBean);
			
			List<String> listStatus = new ArrayList<String>();
			for (BackMoneyPlanPeriodsVo backMoneyPlanPeriodsVo : list) {
				//????????????????????????
				if(list!=null&&list.size()>0){
					listStatus.add(backMoneyPlanPeriodsVo.getStatus().toString());
				}
				boolean flag = true;
				for (String status : listStatus) {
					if(status.equals("0")){//??????????????????????????????
						flag = false;
					}
				}
				if(flag){
					backMoneyPlanPeriodsVo.setPlanStatus("?????????");
				}else{
					backMoneyPlanPeriodsVo.setPlanStatus("?????????");
				}
				//????????????????????????
				if(backMoneyPlanPeriodsVo.getType()==3){
					backMoneyPlanPeriodsVo.setBackMoneyType("??????");
				}else{
					backMoneyPlanPeriodsVo.setBackMoneyType("??????");
				}
			}
			
			
			resultMap.put("data", list);
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	//??????
	@RequestMapping(value ="/exportBackMoneyPlanPeriods")
	@ResponseBody
	public void exportBackMoneyPlanPeriods(HttpServletRequest request, HttpServletResponse response) {
		String selectCondition = request.getParameter("selectCondition");
		String condition = request.getParameter("condition");
		//?????????????????????
		String selectCondition2 = request.getParameter("selectCondition2");
		String condition2 = request.getParameter("condition2");
		//??????????????????
		String userString="";
		String userType=(String)request.getSession().getAttribute("userType");
		byte roleType=(byte) request.getSession().getAttribute("roleType");
		if(roleType==0){//?????????
			//????????????
			//?????????
			byte dataArea=(byte) request.getSession().getAttribute("dataArea");
			
			if(dataArea!=2){
				List<String> userList=(List<String>)request.getSession().getAttribute("userList");
				for(int i=0;i<userList.size();i++){
					if(i==0){
						userString="'"+userList.get(i)+"'";
					}else{
						userString=userString+",'"+userList.get(i)+"'";
					}
				}
			}
		}
		
		BackMoneyPlanPeriodsQueryBean queryBean = new BackMoneyPlanPeriodsQueryBean();
		queryBean.setCondition(condition);
		queryBean.setCondition2(condition2);
		queryBean.setSelectCondition(selectCondition);
		queryBean.setSelectCondition2(selectCondition2);
		queryBean.setUserString(userString);
		List<BackMoneyPlanPeriodsVo> list = backMoneyPlanPeriodsService.getBackMoneyPlanListByCondition(queryBean);
		List<String> listStatus = new ArrayList<String>();
		for (BackMoneyPlanPeriodsVo backMoneyPlanPeriodsVo : list) {
			//????????????????????????
			if(list!=null&&list.size()>0){
				listStatus.add(backMoneyPlanPeriodsVo.getStatus().toString());
			}
			boolean flag = true;
			for (String status : listStatus) {
				if(status.equals("0")){//??????????????????????????????
					flag = false;
				}
			}
			if(flag){
				backMoneyPlanPeriodsVo.setPlanStatus("?????????");
			}else{
				backMoneyPlanPeriodsVo.setPlanStatus("?????????");
			}
			//????????????????????????
			if(backMoneyPlanPeriodsVo.getType()==3){
				backMoneyPlanPeriodsVo.setBackMoneyType("??????");
			}else{
				backMoneyPlanPeriodsVo.setBackMoneyType("??????");
			}
		}
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy???MM???dd???");
	    SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy???MM???dd???   HH:mm:ss");

		try {
			String title = "????????????";
			String [] headers = {"??????ID","????????????ID","????????????ID","????????????","????????????","?????????","??????????????????","??????????????????","??????????????????","????????????",
					"??????????????????","????????????","???????????????","????????????"};
			String fileName = "????????????.xls";
			
			ExportExcelUtil.experotExcelFor2003(title, headers, list, fileName, new PoiCell() {
				@Override
				public void setCellData(Object obj, HSSFRow row, HSSFCellStyle style) {
					BackMoneyPlanPeriodsVo vo = (BackMoneyPlanPeriodsVo) obj;
					@SuppressWarnings("unchecked")
					int i = 0;
					HSSFCell cell = row.createCell(i++);
					//??????ID
					cell.setCellStyle(style);
					cell.setCellValue(vo.getBackMoneyPlanCode());
					cell = row.createCell(i++);
					//????????????ID
					cell.setCellStyle(style);
					cell.setCellValue(vo.getProjectCode());
					cell = row.createCell(i++);
					//????????????ID
					cell.setCellStyle(style);
					cell.setCellValue(vo.getContractCode());
					cell = row.createCell(i++);
					//????????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getContractName());
					cell = row.createCell(i++);
					//????????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getName());
					cell = row.createCell(i++);
					//?????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCreaterName());
					cell = row.createCell(i++);
					//??????????????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getPlanStatus());
					cell = row.createCell(i++);
					//??????????????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getBackMoneyType());
					cell = row.createCell(i++);
					//??????????????????
					cell.setCellStyle(style);
					cell.setCellValue(vo.getPlanBackMoney());
					cell = row.createCell(i++);
					//????????????
					cell.setCellStyle(style);
					if(vo.getRealBackMoney()==null){
						cell.setCellValue(0.00);
					}else{
						cell.setCellValue(vo.getRealBackMoney());
					}
					cell = row.createCell(i++);
					//??????????????????
					cell.setCellStyle(style);
					if(vo.getPlanBackTime()!=null){
    					cell.setCellValue(fmt1.format(vo.getPlanBackTime()));
    				}else{
    					cell.setCellValue("");
    				}
					cell = row.createCell(i++);
					//????????????
					cell.setCellStyle(style);
					if(vo.getRealBackTime()!=null){
    					cell.setCellValue(fmt1.format(vo.getRealBackTime()));
    				}else{
    					cell.setCellValue("");
    				}
					cell = row.createCell(i++);
					//???????????????
					cell.setCellStyle(style);
					if(vo.getRegisterAccount()==null){
						cell.setCellValue("");
					}else{
						cell.setCellValue(vo.getRegisterAccount());
					}
					cell = row.createCell(i++);
					//????????????
					cell.setCellStyle(style);
					if(vo.getRegisterTime()!=null){
    					cell.setCellValue(fmt2.format(vo.getRegisterTime()));
    				}else{
    					cell.setCellValue("");
    				}
					cell = row.createCell(i++);
				}
			}, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
