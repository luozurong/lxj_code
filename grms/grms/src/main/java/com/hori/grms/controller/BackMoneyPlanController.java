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
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.model.CloseCaseInfo;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectActionException;
import com.hori.grms.queryBean.BackMoneyPlanQueryBean;
import com.hori.grms.service.BackMoneyPlanPeriodsService;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectActionExceptionService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.ExportExcelUtil;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.BackMoneyPlanVo;
import com.hori.grms.vo.ContractDetailVo;
import com.hori.vo.UserDetailLoginVo;
/**
 * ????????????
 * @author dell
 *
 */
@Controller
@RequestMapping("backMoneyPlan")
public class BackMoneyPlanController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BackMoneyPlanService backMoneyPlanService;
	@Autowired
	BackMoneyPlanPeriodsService backMoneyPlanPeriodsService;
	@Autowired
	CloseCaseService closeCaseService;
	@Autowired
	ProjectActionExceptionService projectActionExceptionService;
	@Autowired
	ProjectService proService;
	@Autowired
	PendingEventService pendingEventService;
	@Autowired
	ProjectActionService projectActionService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request){	
		
		//??????????????????
	    String backMoneyCode = request.getParameter("backMoneyCode");
		String pendingParamType = request.getParameter("pendingParamType");
		if("1".equals(pendingParamType)){
			request.getSession().setAttribute("backMoneyCode", backMoneyCode);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}
		
		return "/backMoneyPlan/backMoneyPlan.jsp";
	}
	
	//??????????????????
	@RequestMapping("/getBackMoneyPlanList")
	@ResponseBody
	public String getBackMoneyPlanList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String selectCondition = request.getParameter("selectCondition");
			String condition = request.getParameter("condition");
			String backMoneyStatus = request.getParameter("backMoneyStatus");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String pageNoStr = request.getParameter("pageNo");
			String pageSizeStr =  request.getParameter("pageSize");
			int pageNo = (pageNoStr!=null&&!pageNoStr.equals(""))?Integer.parseInt(pageNoStr):1;
			int pageSize = (pageSizeStr!=null&&!pageSizeStr.equals(""))?Integer.parseInt(pageSizeStr):10;
			
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
			//??????????????????
			String pendingParamType = (String)request.getSession().getAttribute("pendingParamType");
			if("1".equals(pendingParamType) && pendingParamType != null){
				String backMoneyCode = (String)request.getSession().getAttribute("backMoneyCode");
				selectCondition = "0";
				condition = backMoneyCode;
			}
			BackMoneyPlanQueryBean queryBean = new BackMoneyPlanQueryBean();
			queryBean.setBackMoneyStatus(backMoneyStatus);
			queryBean.setCondition(condition);
			queryBean.setEndTime(endTime);
			queryBean.setSelectCondition(selectCondition);
			queryBean.setStartTime(startTime);
			queryBean.setUserString(userString);
			
			List<BackMoneyPlanVo> voList = new ArrayList<BackMoneyPlanVo>();
			List<BackMoneyPlanVo> list = backMoneyPlanService.getBackMoneyPlanListByCondition(queryBean);
			if(backMoneyStatus!=null && !backMoneyStatus.equals("")){//??????????????????
				for (BackMoneyPlanVo backMoneyPlanVo : list) {
					if(backMoneyStatus.equals("0")){//?????????
						if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
							voList.add(backMoneyPlanVo);
						}
					}else if(backMoneyStatus.equals("1")){//?????????
						if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
							voList.add(backMoneyPlanVo);
						}
					}else if(backMoneyStatus.equals("2")){//?????????
						if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
							voList.add(backMoneyPlanVo);
						}
					}else if(backMoneyStatus.equals("3")){//?????????
						if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
							voList.add(backMoneyPlanVo);
						}
					}else if(backMoneyStatus.equals("4")){//?????????
						if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
							voList.add(backMoneyPlanVo);
						}
					}
				}
				resultMap.put("total", voList.size());
				if( voList.size()>pageSize){
					resultMap.put("data", voList.size()>(pageNo*pageSize-1)?voList.subList((pageNo-1)*pageSize, pageNo*pageSize-1):voList.subList((pageNo-1)*pageSize, voList.size()-1));
				}else{
					resultMap.put("data", voList);
				}
			}else{
				if( list.size()>pageSize){
					resultMap.put("data", list.size()>(pageNo*pageSize-1)?list.subList((pageNo-1)*pageSize, pageNo*pageSize-1):list.subList((pageNo-1)*pageSize, list.size()-1));
				}else{
					resultMap.put("data", list);
				}
				resultMap.put("total", list.size());
			}
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}finally {
			//??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("backMoneyCode");
		}
		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	//????????????
	@RequestMapping("/getContractList")
	@ResponseBody
	public String getContractList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String condition = request.getParameter("condition");
			String pageNoStr = request.getParameter("pageNo");
			int pageNo = 1;
			if(pageNoStr!=null&&!pageNoStr.equals("")){
				pageNo = Integer.parseInt(pageNoStr);
			}
			List<ContractDetailVo> list = backMoneyPlanService.getContractLists(condition,(pageNo-1)*10);
			int total = backMoneyPlanService.getContractListTatal(condition);
			PageInfo<ContractDetailVo> pageInfo = new PageInfo<ContractDetailVo>(list);
			resultMap.put("rows", pageInfo.getList());
			resultMap.put("total", total);
			resultMap.put("success", true);
			resultMap.put("page", pageNo);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss");
	}
	
	//????????????
	@RequestMapping("/getContractDetail")
	@ResponseBody
	public String getContractDetail(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String contractCode = request.getParameter("contractCode");
			ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
			resultMap.put("contractVo", contractVo);
			resultMap.put("success", true);
		}catch(Exception e){
			logger.info("????????????"+e.getMessage());
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//?????????????????????
	@RequestMapping("/jumpToCreateBackMoneyPlan")
	public String jumpToCreateBackMoneyPlan(HttpServletRequest request){
		
		String contractCode = request.getParameter("contractCode");
		ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		request.setAttribute("money",nf.format(contractVo.getMoney()));
		request.setAttribute("contractVo", contractVo);
		return "/backMoneyPlan/createBackMoneyPlan.jsp";
	}
	
	//?????????????????????
	@RequestMapping("/jumpToEditBackMoneyPlan")
	public String jumpToEditBackMoneyPlan(HttpServletRequest request){
		String contractCode = request.getParameter("contractCode");
		String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
		List<BackMoneyPlanPeriods> list = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(backMoneyPlanCode);
		ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		request.setAttribute("money",nf.format(contractVo.getMoney()));
		request.setAttribute("contractVo", contractVo);
		request.setAttribute("backMoneyPlanCode", backMoneyPlanCode);
		request.setAttribute("list", list);
		return "/backMoneyPlan/editBackMoneyPlan.jsp";
	}
	
	//???????????????
	@RequestMapping("/jumpToCheckBackMoneyPlan")
	public String jumpToCheckBackMoneyPlan(HttpServletRequest request){
		//??????code
		String contractCode = request.getParameter("contractCode");
		String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
		//????????????
		ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
		List<BackMoneyPlanPeriods> list = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(backMoneyPlanCode);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		request.setAttribute("money",nf.format(contractVo.getMoney()));
		request.setAttribute("list", list);
		request.setAttribute("contractVo", contractVo);
		request.setAttribute("backMoneyPlanCode", backMoneyPlanCode);
		
		return "/backMoneyPlan/checkBackMoneyPlan.jsp";
	}
	
	//?????????????????????
	@RequestMapping("/jumpToPlanDetail")
	public String jumpToPlanDetail(HttpServletRequest request){
		//??????code
		String contractCode = request.getParameter("contractCode");
		//????????????code
		String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
		//????????????
		ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
		//??????????????????
		BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByCode(backMoneyPlanCode);
		//????????????
		byte roleType=(byte) request.getSession().getAttribute("roleType");
		request.setAttribute("roleType",roleType);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		request.setAttribute("money",nf.format(contractVo.getMoney()));
		request.setAttribute("createrName", backMoneyPlan.getCreaterName());
		request.setAttribute("createTime", backMoneyPlan.getCreateTime());
		request.setAttribute("allbackRemark", backMoneyPlan.getAllbackRemark());
		request.setAttribute("contractVo", contractVo);
		request.setAttribute("backMoneyPlanCode",backMoneyPlanCode);
		List<String> listStatus = new ArrayList<String>();
		List<BackMoneyPlanPeriods> list = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(backMoneyPlanCode);
		if(list!=null&&list.size()>0){
			for (BackMoneyPlanPeriods backMoneyPlanPeriods : list) {
				//???????????????????????????
				listStatus.add(backMoneyPlanPeriods.getStatus().toString());
			}
		}
		
		String planStatus = "";
		if(backMoneyPlan.getCheckStatus()==2){
			planStatus = "?????????";
		}else if(backMoneyPlan.getCheckStatus()==0){
			planStatus = "?????????";
		}else if(backMoneyPlan.getCheckStatus()==1){
			if(listStatus.get(0).equals("0") && (1 == new HashSet<Object>(listStatus).size())){//???set??????
				planStatus = "?????????";
			}else if(listStatus.get(0).equals("1") && (1 == new HashSet<Object>(listStatus).size())){
				planStatus = "?????????";
			}else{
				planStatus = "?????????";
			}
		}
		
		request.setAttribute("planStatus",planStatus);
		return "/backMoneyPlan/backMoneyPlanDetail.jsp";
	}
	
	//?????????????????????
	@RequestMapping("/jumpToPlanRegister")
	public String jumpToPlanRegister(HttpServletRequest request){
		//??????code
		String contractCode = request.getParameter("contractCode");
		//????????????code
		String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
		//????????????
		ContractDetailVo contractVo = backMoneyPlanService.getContractDetail(contractCode);
		//?????????????????????
		BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByCode(backMoneyPlanCode);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		request.setAttribute("money",nf.format(contractVo.getMoney()));
		request.setAttribute("createrName", backMoneyPlan.getCreaterName());
		request.setAttribute("createTime", backMoneyPlan.getCreateTime());
		request.setAttribute("contractVo", contractVo);
		request.setAttribute("contractCode", contractCode);
		request.setAttribute("backMoneyPlanCode", backMoneyPlanCode);
		List<BackMoneyPlanPeriods> backList = new ArrayList<BackMoneyPlanPeriods>();
		//?????????list
		List<BackMoneyPlanPeriods> jdList = backMoneyPlanPeriodsService.getJDListByPlanCode(backMoneyPlanCode);
		//??????list
		List<BackMoneyPlanPeriods> kkList = backMoneyPlanPeriodsService.getKKListByPlanCode(backMoneyPlanCode);
		//?????????list
		List<BackMoneyPlanPeriods> jsList = backMoneyPlanPeriodsService.getJSListByPlanCode(backMoneyPlanCode);
		//???????????????????????????????????? list
		List<BackMoneyPlanPeriods> jdycList = backMoneyPlanPeriodsService.getJDYCListByPlanCode(backMoneyPlanCode);
		//?????????????????????list
		List<BackMoneyPlanPeriods> wsList = backMoneyPlanPeriodsService.getWSListByPlanCode(backMoneyPlanCode);
		
		if(jsList.get(0).getStatus()==1){//????????????????????????????????????--????????????--?????????
			backList.addAll(jdList);
			backList.addAll(jsList);
			backList.addAll(kkList);
		}else if(jdList.size()==wsList.size()){//??????????????????????????????--????????????--????????????
			backList.addAll(kkList);
			backList.addAll(jdList);
			backList.addAll(jsList);
		}else{//???????????????????????????-->??????--????????????
			backList.addAll(jdycList);
			backList.addAll(kkList);
			backList.addAll(wsList);
			backList.addAll(jsList);
		}
		
		List<String> listStatus = new ArrayList<String>();
		if(backList!=null&&backList.size()>0){
			for (BackMoneyPlanPeriods backMoneyPlanPeriods : backList) {
				//???????????????????????????
				listStatus.add(backMoneyPlanPeriods.getStatus().toString());
			}
		}
		String planStatus = "";
		if(backMoneyPlan.getCheckStatus()==2){
			planStatus = "?????????";
		}else if(backMoneyPlan.getCheckStatus()==0){
			planStatus = "?????????";
		}else if(backMoneyPlan.getCheckStatus()==1){
			if(listStatus.get(0).equals("0") && (1 == new HashSet<Object>(listStatus).size())){//???set??????
				planStatus = "?????????";
			}else if(listStatus.get(0).equals("1") && (1 == new HashSet<Object>(listStatus).size())){
				planStatus = "?????????";
			}else{
				planStatus = "?????????";
			}
		}
		
		request.setAttribute("list", backList);
		request.setAttribute("planStatus",planStatus);
		return "/backMoneyPlan/backMoneyPlanRegister.jsp";
	}
	
	//??????????????????
	@RequestMapping(value="/saveBackMoneyPlan" )
	@ResponseBody
	public String saveBackMoneyPlan(HttpServletRequest request, HttpServletResponse response,@RequestBody List<BackMoneyPlanPeriods> planList){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		HttpSession session = getSession();
		try{
			//{"planList":[{"type":"1","planBackTime":"2018-08-24","planBackMoney":"120000","remark":"123"}]}
			String contractCode =  request.getParameter("contractCode");
			
			String userAccount = (String) session.getAttribute("userAccount");
			String userName = (String) session.getAttribute("userName");
			
			//??????32?????????????????????
			String backMoneyPlanCode = UUIDGeneratorUtil.generateUUID();
			BackMoneyPlan backMoneyPlan = new BackMoneyPlan();
			backMoneyPlan.setId(UUIDGeneratorUtil.generateUUID());
			backMoneyPlan.setBackMoneyPlanCode(backMoneyPlanCode);
			backMoneyPlan.setContractCode(contractCode);
			backMoneyPlan.setCreaterName(userName);
			backMoneyPlan.setCreaterAccount(userAccount);
			backMoneyPlan.setCreaterLevel("1");
			backMoneyPlan.setCreateTime(new Date());
			backMoneyPlan.setCheckStatus((short)0);
			backMoneyPlanService.insertSelective(backMoneyPlan,planList,session,contractCode);
			
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//??????????????????
	@RequestMapping("/editBackMoneyPlan")
	@ResponseBody
	public String editBackMoneyPlan(HttpServletRequest request, HttpServletResponse response,@RequestBody List<BackMoneyPlanPeriods> planList){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			//???????????????
			long time = System.currentTimeMillis();
			
			//????????????????????????????????????
			backMoneyPlanPeriodsService.deleteByCode(planList.get(0).getBackMoneyPlanCode());
			
			for (BackMoneyPlanPeriods planPeriods : planList) {
				time += 10000;
				BackMoneyPlanPeriods backMoneyPlanPeriods = new BackMoneyPlanPeriods();
				backMoneyPlanPeriods.setBackMoneyPlanCode(planPeriods.getBackMoneyPlanCode());
				backMoneyPlanPeriods.setId(UUIDGeneratorUtil.generateUUID());
				backMoneyPlanPeriods.setPlanBackMoney(planPeriods.getPlanBackMoney());
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				backMoneyPlanPeriods.setPlanBackTime(planPeriods.getPlanBackTime());
				backMoneyPlanPeriods.setRemark(planPeriods.getRemark());
				backMoneyPlanPeriods.setType(planPeriods.getType());
				backMoneyPlanPeriods.setStatus((short)0);
				backMoneyPlanPeriods.setCreateTime(new Date(time));
				backMoneyPlanPeriodsService.insertSelective(backMoneyPlanPeriods);
			}
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//??????????????????
	@RequestMapping("/delBackMoneyPlan")
	@ResponseBody
	public String delBackMoneyPlan(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
			String projectCode = request.getParameter("projectCode");
			//????????????
			backMoneyPlanService.deleteByCode(backMoneyPlanCode);
			//?????????
			backMoneyPlanPeriodsService.deleteByCode(backMoneyPlanCode);
			pendingEventService.updateBackMoneyPendingEventByShenHe(projectCode,backMoneyPlanCode,8);
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//??????
	@RequestMapping("/checkerBackMoneyPlan")
	@ResponseBody
	public String checkerBackMoneyPlan(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		HttpSession session = getSession();
		try{
			String type = request.getParameter("type");
			String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
			String userAccount = (String) session.getAttribute("userAccount");
			String userName = (String) session.getAttribute("userName");
			BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByCode(backMoneyPlanCode);
			Project project = proService.getByContractCode(backMoneyPlan.getContractCode());
			if(type.equals("1")){//????????????
				//back_money_plan????????????????????? ???????????????
				backMoneyPlan.setCheckerAccount(userAccount);
				backMoneyPlan.setCheckerName(userName);
				backMoneyPlan.setCheckerTime(new Date());
				backMoneyPlan.setCheckStatus((short)1);
				backMoneyPlanService.updateByPrimaryKeySelective(backMoneyPlan);
				//back_money_plan_periods??????????????????????????????
				List<BackMoneyPlanPeriods> backList = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(backMoneyPlanCode);
				for (BackMoneyPlanPeriods backMoneyPlanPeriods : backList) {
					backMoneyPlanPeriods.setStatus((short)0);
					backMoneyPlanPeriodsService.updateByPrimaryKeySelective(backMoneyPlanPeriods);
				}
				pendingEventService.updateBackMoneyPendingEventByShenHe(project.getProductCode(),backMoneyPlanCode,1);
				
			}else if(type.equals("2")){//???????????????
				pendingEventService.updateBackMoneyPendingEventByShenHe(project.getProductCode(),backMoneyPlanCode,2);
			}
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//??????
	@RequestMapping("/withdrawBackMoneyPlan")
	@ResponseBody
	public String withdrawBackMoneyPlan(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
			/**TODO**/
			//???????????????????????????????????????????????????????????????
			List<BackMoneyPlanPeriods> backList = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(backMoneyPlanCode);
			List<String> listStatus = new ArrayList<String>();
			if(backList!=null&&backList.size()>0){
				for (BackMoneyPlanPeriods backMoneyPlanPeriods : backList) {
					//???????????????????????????
					listStatus.add(backMoneyPlanPeriods.getStatus().toString());
				}
			}
			
			if(listStatus.get(0).equals("0") && (1 == new HashSet<Object>(listStatus).size())){//???set??????
				//back_money_plan????????????????????? ???????????????
				BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByCode(backMoneyPlanCode);
				backMoneyPlan.setCheckStatus((short)0);
				backMoneyPlanService.updateByPrimaryKeySelective(backMoneyPlan);
				//????????????
				Project project = proService.getByContractCode(backMoneyPlan.getContractCode());
				pendingEventService.updateBackMoneyPendingEventByShenHe(project.getProductCode(),backMoneyPlanCode,6);
				resultMap.put("title", "????????????");
			}else{
				resultMap.put("title", "????????????????????????????????????");
			}
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//????????????
	@RequestMapping("/getBackMoneyPlanDetail")
	@ResponseBody
	public String getBackMoneyPlanDetail(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			HttpSession session = getSession();
			String backMoneyPlanCode = request.getParameter("backMoneyPlanCode");
			List<BackMoneyPlanPeriods> backList = new ArrayList<BackMoneyPlanPeriods>();
			//?????????list
			List<BackMoneyPlanPeriods> jdList = backMoneyPlanPeriodsService.getJDListByPlanCode(backMoneyPlanCode);
			//??????list
			List<BackMoneyPlanPeriods> kkList = backMoneyPlanPeriodsService.getKKListByPlanCode(backMoneyPlanCode);
			//?????????list
			List<BackMoneyPlanPeriods> jsList = backMoneyPlanPeriodsService.getJSListByPlanCode(backMoneyPlanCode);
			//???????????????????????????????????? list
			List<BackMoneyPlanPeriods> jdycList = backMoneyPlanPeriodsService.getJDYCListByPlanCode(backMoneyPlanCode);
			//?????????????????????list
			List<BackMoneyPlanPeriods> wsList = backMoneyPlanPeriodsService.getWSListByPlanCode(backMoneyPlanCode);
			
			
			if(jsList.get(0).getStatus()==1){//????????????????????????????????????--????????????--?????????
				backList.addAll(jdList);
				backList.addAll(jsList);
				backList.addAll(kkList);
				session.setAttribute("end", "1");
			}else if(jdList.size()==wsList.size()){//??????????????????????????????--????????????--????????????
				backList.addAll(kkList);
				backList.addAll(jdList);
				backList.addAll(jsList);
				session.setAttribute("end", "0");
			}else{//???????????????????????????-->??????--????????????
				backList.addAll(jdycList);
				backList.addAll(kkList);
				backList.addAll(wsList);
				backList.addAll(jsList);
				session.setAttribute("end", "0");
			}
			resultMap.put("backList", backList);
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd");
	}
	
	//????????????/??????
	@RequestMapping("/enregisterMoney")
	@ResponseBody
	public String enregisterMoney(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		HttpSession session = getSession();
		String userAccount = (String) request.getSession().getAttribute("userAccount");
		String userName = (String) request.getSession().getAttribute("userName");
		UserDetailLoginVo userDetailLoginVo=(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		try{
			//1????????????2?????????
			String type = request.getParameter("type");
			//?????????ID
			String id = request.getParameter("id");
			//????????????/??????
			String realMoney = request.getParameter("realMoney");
			//????????????
			String realBackRemark = request.getParameter("realBackRemark");
			//????????????????????????
			String realBackTime = request.getParameter("realBackTime");
			//??????code
			String contractCode = request.getParameter("contractCode");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			BackMoneyPlanPeriods backMoneyPlanPeriods = backMoneyPlanPeriodsService.selectByPrimaryKey(id);
				
			//1.3????????????????????????????????????
			//????????????????????????????????????
			//????????????????????????????????????????????????????????????????????????
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			Project project = proService.getByContractCode(contractCode);
			if(type.equals("1")){//??????
				if(Double.parseDouble(realMoney)!=backMoneyPlanPeriods.getPlanBackMoney() || !realBackTime.equals(sdf.format(backMoneyPlanPeriods.getPlanBackTime()))){
					//???????????????????????????????????????
					backMoneyPlanPeriods.setStatus((short)2);
					ProjectActionException pae = new ProjectActionException();
					pae.setId(UUIDGeneratorUtil.generateUUID());
					pae.setProjectCode(project.getProductCode());
					pae.setPlanPeriodsId(id);
					pae.setType((short) 2);
					pae.setExceptionRemark(realBackRemark);
					pae.setCreateTime(new Date());
					pae.setDepartmentId(userDetailLoginVo.getDepartId());
					pae.setDepartmentName(userDetailLoginVo.getDepartName());
					
					pae.setBusinessType("0");
					pae.setStatus((short) 0);
					
					pae.setCreaterAccount(userAccount);
					pae.setCreaterName(userName);
					projectActionExceptionService.insertMoneyException(pae);
					
					//???????????????????????????
					project.setExceptionStatus((short)1);
					proService.update(project);
					//???????????????????????????????????????
					projectActionService.changStateBycode(project.getProductCode());
					//????????????????????????
					pendingEventService.createBackMoneyPendingEventByMoneyException(project.getName(),project.getProductCode(),id,14,1,"");
					
				}else{
					backMoneyPlanPeriods.setStatus((short)1);
				}
			}else{//??????
				if(Double.parseDouble(realMoney)!=backMoneyPlanPeriods.getPlanBackMoney()){
					//???????????????????????????????????????
					backMoneyPlanPeriods.setStatus((short)2);
					ProjectActionException pae = new ProjectActionException();
					pae.setId(UUIDGeneratorUtil.generateUUID());
					pae.setProjectCode(project.getProductCode());
					pae.setPlanPeriodsId(id);
					pae.setType((short) 2);
					pae.setExceptionRemark(realBackRemark);
					pae.setCreateTime(new Date());
					pae.setDepartmentId(userDetailLoginVo.getDepartId());
					pae.setDepartmentName(userDetailLoginVo.getDepartName());
					
					pae.setBusinessType("0");
					pae.setStatus((short) 0);
					
					pae.setCreaterAccount(userAccount);
					pae.setCreaterName(userName);
					projectActionExceptionService.insertMoneyException(pae);
					
					//???????????????????????????
					project.setExceptionStatus((short)1);
					proService.update(project);
					//???????????????????????????????????????
					projectActionService.changStateBycode(project.getProductCode());
					
					//????????????????????????
					pendingEventService.createBackMoneyPendingEventByMoneyException(project.getName(),project.getProductCode(),id,14,1,"");
					
				}else{
					backMoneyPlanPeriods.setStatus((short)1);
				}
			}
				
			//????????????????????????
			pendingEventService.updateBackMoneyPendingEventByTask(project.getProductCode(),id,0);
			pendingEventService.updateBackMoneyPendingEventByActionException(project.getProductCode(),id);
			
			if(realBackTime!=null){
				backMoneyPlanPeriods.setRealBackTime(sdf.parse(realBackTime));
			}
			backMoneyPlanPeriods.setRealBackMoney(Double.parseDouble(realMoney));
			backMoneyPlanPeriods.setRealBackRemark(realBackRemark);
			backMoneyPlanPeriods.setRegisterAccount(userAccount);
			backMoneyPlanPeriods.setRegisterTime(new Date());
			backMoneyPlanPeriodsService.updateByPrimaryKeySelective(backMoneyPlanPeriods);
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	//????????????
	@RequestMapping("/allToAccount")
	@ResponseBody
	public String allToAccount(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String allbackRemark = request.getParameter("allbackRemark");
			//?????????code
			String backMoneyPlanCodes = request.getParameter("backMoneyPlanCodes");
			String projectCodeArr = request.getParameter("projectCodeArr");
			
			String[] projectCode = projectCodeArr.split(",");
			String[] codes = backMoneyPlanCodes.split(",");
			byte roleType=(byte) request.getSession().getAttribute("roleType");
			
			for(int i = 0;i < codes.length;i++){
				if(!codes[i].equals("")&&codes[i]!=null){
					boolean flag = true;
					List<BackMoneyPlanPeriods> backList = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(codes[i]);
					for (BackMoneyPlanPeriods periods : backList) {
						if(periods.getStatus()!=1){//???????????????????????????????????????????????????
							flag = false;
						}
					}
					BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByCode(codes[i]);
					
					if(flag){//??????????????????????????????????????????????????????????????????
						CloseCaseInfo closeCaseInfo = closeCaseService.getCloseCaseForBackMoney(codes[i]);
						if(closeCaseInfo!=null){
							if(closeCaseInfo.getStatus()==7){//?????????
								backMoneyPlan.setCheckStatus((short)2);
								backMoneyPlan.setAllbackRemark(allbackRemark);
								backMoneyPlanService.updateByPrimaryKeySelective(backMoneyPlan);
								closeCaseService.updateCloseCaseForBackMoney(codes[i]);
								pendingEventService.updateCloseCasePendingEvent(projectCode[i],codes[i], 9, roleType);
							}
						}else if(backMoneyPlan.getCheckStatus()==2){
							resultMap.put("title", "?????????");
							resultMap.put("success", false);
							return JSON.toJSONString(resultMap);
						}else{
							resultMap.put("title", "???????????????,??????????????????");
							resultMap.put("success", false);
							return JSON.toJSONString(resultMap);
						}
					}else{
						resultMap.put("title", "?????????????????????????????????");
						resultMap.put("success", false);
						return JSON.toJSONString(resultMap);
					}
				}
			}
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
	
	
	
	//??????
	@RequestMapping(value ="/exportBackMoneyPlan")
	public void exportBackMoneyPlan(HttpServletRequest request, HttpServletResponse response) {
		String selectCondition = request.getParameter("selectCondition");
		String condition = request.getParameter("condition");
		String backMoneyStatus = request.getParameter("backMoneyStatus");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
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
		BackMoneyPlanQueryBean queryBean = new BackMoneyPlanQueryBean();
		queryBean.setBackMoneyStatus(backMoneyStatus);
		queryBean.setCondition(condition);
		queryBean.setEndTime(endTime);
		queryBean.setSelectCondition(selectCondition);
		queryBean.setStartTime(startTime);
		queryBean.setUserString(userString);
		
		List<BackMoneyPlanVo> voList = new ArrayList<BackMoneyPlanVo>();
		List<BackMoneyPlanVo> list = backMoneyPlanService.getBackMoneyPlanListByCondition(queryBean);
		if(backMoneyStatus!=null && !backMoneyStatus.equals("")){//??????????????????
			for (BackMoneyPlanVo backMoneyPlanVo : list) {
				if(backMoneyStatus.equals("0")){//?????????
					if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
						voList.add(backMoneyPlanVo);
					}
				}else if(backMoneyStatus.equals("1")){//?????????
					if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
						voList.add(backMoneyPlanVo);
					}
				}else if(backMoneyStatus.equals("2")){//?????????
					if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
						voList.add(backMoneyPlanVo);
					}
				}else if(backMoneyStatus.equals("3")){//?????????
					if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
						voList.add(backMoneyPlanVo);
					}
				}else if(backMoneyStatus.equals("4")){//?????????
					if(backMoneyPlanVo.getPlanStatus().equals("?????????")){
						voList.add(backMoneyPlanVo);
					}
				}
			}
			list = voList;
		}	
			
			if(list!=null&&list.size()>0) {
				
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("????????????");			
				/*################################??????????????????START################################*/
				// ??????????????????
				HSSFFont headerfont = workbook.createFont();
				// ???????????? 	
				headerfont.setBoldweight(Font.BOLDWEIGHT_BOLD);  
				
				//??????style??????
				HSSFCellStyle headerStyle = workbook.createCellStyle();
				// ???????????????????????????
				headerStyle.setFont(headerfont);
				
				//??????????????????
				headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// ???????????????????????????
				headerStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				//????????????
				headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				
				//??????????????????
				headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				/*################################??????????????????END################################*/  
				
				/*################################???????????????????????????START################################*/
				// ????????????
				HSSFFont commonfont = workbook.createFont();
				HSSFCellStyle commonStyle = workbook.createCellStyle();
				commonStyle.setWrapText(true);
				commonStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????    
				commonStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//???????????? 
				commonStyle.setFont(commonfont);
				//??????????????????
				commonStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				//????????????
				commonStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				commonStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				commonStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				commonStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				/*################################???????????????????????????END################################*/
				//????????????????????????
				//XSSFRow fristHeadRow = sheet.createRow(0);
				
				//????????????????????????
				String[] headers = {"????????????ID","??????ID","??????ID","????????????","????????????","?????????","???????????????","??????????????????","??????????????????","????????????","??????????????????","?????????ID","??????????????????","????????????","??????????????????","?????????/????????????","????????????","??????/?????????","????????????"};
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
			    SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy???MM???dd???   HH:mm:ss");
			    for(BackMoneyPlanVo vo : list) {
			    	//?????????????????????????????????
			    	List<BackMoneyPlanPeriods> periodsList = backMoneyPlanPeriodsService.getPeriodsListByPlanCode(vo.getBackMoneyPlanCode());
			    	
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),0,0));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),1,1));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),2,2));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),3,3));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),4,4));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),5,5));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),6,6));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),7,7));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),8,8));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),9,9));
			    		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+(periodsList.size()-1),10,10));
			    		int row1 = 1;
			    		for(BackMoneyPlanPeriods periods : periodsList) {
			    			
			    				int s=0;
			    				HSSFRow row = sheet.createRow(rowNum++);
			    				
			    				//????????????ID
			    				HSSFCell cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getBackMoneyPlanCode());
			    				//??????ID
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getProjectCode());
			    				//??????ID  
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getContractCode());
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getContractName());
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getName());
			    				//?????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getCreaterName());
			    				//???????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getMoney());
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getCollectedMoney());
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getUnCollectedMoney());
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getReduceMoney());
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(vo.getPlanStatus());
			    				//?????????ID
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(periods.getId());
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getPlanBackTime()!=null){
			    					cell.setCellValue(fmt1.format(periods.getPlanBackTime()));
			    				}else{
			    					cell.setCellValue("");
			    				}
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getType()==1||periods.getType()==2){
			    					cell.setCellValue("??????");
			    				}else if(periods.getType()==3){
			    					cell.setCellValue("??????");
			    				}
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getType()==1){
			    					cell.setCellValue("?????????");
			    				}else if(periods.getType()==2){
			    					cell.setCellValue("?????????");
			    				}else if(periods.getType()==3){
			    					cell.setCellValue("??????");
			    				}
			    				//??????????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				cell.setCellValue(periods.getPlanBackMoney());
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getRealBackMoney()==null){//?????????????????????
			    					cell.setCellValue(0.00);
			    				}else{
			    					cell.setCellValue(periods.getRealBackMoney());
			    				}
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getRealBackMoney()==null){//?????????????????????
			    					cell.setCellValue(periods.getPlanBackMoney());
			    				}else{
			    					cell.setCellValue(periods.getPlanBackMoney()-periods.getRealBackMoney());
			    				}
			    				//????????????
			    				cell = row.createCell(s++);
			    				cell.setCellStyle(commonStyle);
			    				if(periods.getStatus()==0){
			    					cell.setCellValue("?????????");
			    				}else if(periods.getStatus()==1){
			    					cell.setCellValue("?????????");
			    				}else if(periods.getStatus()==2){
			    					cell.setCellValue("????????????");
			    				}
			    		}
			    	}
			    
			    for (int i = 0; i < headers.length; i++) {
			    	sheet.autoSizeColumn(i);
			    	if(sheet.getColumnWidth(i) < 20*256){
			    		sheet.setColumnWidth(i, 20*256);
			    	}
				}
			    ExportExcelUtil.downSpcialFile("????????????.xls",workbook,request,response);
			}
		}

	//}
	
	//????????????????????????
	@RequestMapping(value="/getPlanBackTime" )
	@ResponseBody
	public String getPlanBackTime(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try{
			String id =  request.getParameter("id");
			BackMoneyPlanPeriods backMoneyPlanPeriods = backMoneyPlanPeriodsService.selectByPrimaryKey(id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			resultMap.put("planBackTime", sdf.format(backMoneyPlanPeriods.getPlanBackTime()));
			
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSON.toJSONString(resultMap);
	}
}
