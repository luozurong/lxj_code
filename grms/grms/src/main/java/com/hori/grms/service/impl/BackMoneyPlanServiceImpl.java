package com.hori.grms.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.BackMoneyPlanMapper;
import com.hori.grms.dao.BackMoneyPlanPeriodsMapper;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.model.Contract;
import com.hori.grms.model.Project;
import com.hori.grms.queryBean.BackMoneyPlanQueryBean;
import com.hori.grms.service.BackMoneyPlanPeriodsService;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.ContractService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.BackMoneyPlanVo;
import com.hori.grms.vo.ContractDetailVo;
import com.hori.grms.vo.ContractVo;
@Service
public class BackMoneyPlanServiceImpl implements BackMoneyPlanService {

	@Autowired
	private BackMoneyPlanMapper backMoneyPlanMapper;
	
	@Autowired
	private BackMoneyPlanPeriodsMapper backMoneyPlanPeriodsMapper;
	
	@Autowired
	private BackMoneyPlanPeriodsService backMoneyPlanPeriodsService;
	@Autowired
	ProjectService proService;
	@Autowired
	PendingEventService pendingEventService;
	@Autowired
	ContractService contractService;
	@Override
	public List<BackMoneyPlanVo> getBackMoneyPlanListByCondition(BackMoneyPlanQueryBean queryBean) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(queryBean.getStartTime())) {
			queryBean.setStartTime(queryBean.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(queryBean.getEndTime())) {
			queryBean.setEndTime(queryBean.getEndTime() + " 23:59:59");
		}
		List<BackMoneyPlanVo> list = backMoneyPlanMapper.getBackMoneyPlanListByCondition(queryBean);
		if(list!=null&&list.size()>0){
	
			for (BackMoneyPlanVo backMoneyPlanVo : list) {
				//????????????code???????????????
				List<BackMoneyPlanPeriods> periodsList = backMoneyPlanPeriodsMapper.getPeriodsListByPlanCode(backMoneyPlanVo.getBackMoneyPlanCode());
				List<String> listStatus = new ArrayList<String>();
				//???????????????
				Double money = backMoneyPlanVo.getMoney();
				//??????????????????
				Double collectedMoney = 0.00;
				//?????? 
				Double reduceMoney = 0.00;
				//???????????????
				if(periodsList!=null&&periodsList.size()>0){
					for (BackMoneyPlanPeriods backMoneyPlanPeriods : periodsList) {
						/**TODO**/
						//???????????????????????????
						listStatus.add(backMoneyPlanPeriods.getStatus().toString());
						//?????????????????????
						if(backMoneyPlanPeriods.getType()!=3 && backMoneyPlanPeriods.getRealBackMoney()!=null){
							collectedMoney = collectedMoney+backMoneyPlanPeriods.getRealBackMoney();
						}
						//??????
						if(backMoneyPlanPeriods.getType()==3 && backMoneyPlanPeriods.getRealBackMoney()!=null){
							reduceMoney = reduceMoney+backMoneyPlanPeriods.getRealBackMoney();
						}
					}
					//????????????
					backMoneyPlanVo.setCollectedMoney(collectedMoney);
					backMoneyPlanVo.setUnCollectedMoney((double)Math.round((money-collectedMoney)*1000)/1000);
					backMoneyPlanVo.setReduceMoney(reduceMoney);
					
					if(backMoneyPlanVo.getCheckStatus()==2){
						backMoneyPlanVo.setPlanStatus("?????????");
					}else if(backMoneyPlanVo.getCheckStatus()==0){
						backMoneyPlanVo.setPlanStatus("?????????");
					}else if(backMoneyPlanVo.getCheckStatus()==1){
						if(listStatus.get(0).equals("0") && 1 == new HashSet<Object>(listStatus).size()){//???set??????
							backMoneyPlanVo.setPlanStatus("?????????");
						}else if(listStatus.get(0).equals("1") && 1 == new HashSet<Object>(listStatus).size()){
							backMoneyPlanVo.setPlanStatus("?????????");
						}else{
							backMoneyPlanVo.setPlanStatus("?????????");
						}
					}
				}
			}
		}
		return list;
	}


	@Override
	public void insertSelective(BackMoneyPlan backMoneyPlan,List<BackMoneyPlanPeriods> list,HttpSession session,String contractCode) {
		//???????????????
		long time = System.currentTimeMillis();
		
		// TODO Auto-generated method stub
		backMoneyPlanMapper.insertSelective(backMoneyPlan);
		for (BackMoneyPlanPeriods planPeriods : list) {
			time += 10000;
			BackMoneyPlanPeriods backMoneyPlanPeriods = new BackMoneyPlanPeriods();
			backMoneyPlanPeriods.setBackMoneyPlanCode(backMoneyPlan.getBackMoneyPlanCode());
			backMoneyPlanPeriods.setId(UUIDGeneratorUtil.generateUUID());
			backMoneyPlanPeriods.setPlanBackMoney(planPeriods.getPlanBackMoney());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			backMoneyPlanPeriods.setPlanBackTime(planPeriods.getPlanBackTime());
			backMoneyPlanPeriods.setRemark(planPeriods.getRemark());
			backMoneyPlanPeriods.setType(planPeriods.getType());
			backMoneyPlanPeriods.setStatus((short)0);
			backMoneyPlanPeriods.setCreateTime(new Date(time));
			backMoneyPlanPeriodsService.insertSelective(backMoneyPlanPeriods);
		}
		
		String userAccount = (String) session.getAttribute("userAccount");
		String userName = (String) session.getAttribute("userName");
		//?????????????????????????????????
		Project project = proService.getByContractCode(contractCode);
		
		pendingEventService.createBackMoneyPendingEventByShenHe(project.getName(),project.getProductCode(),backMoneyPlan.getBackMoneyPlanCode(),
				userAccount,userName);
		pendingEventService.updateBackMoneyPendingEventByContractEnd(project.getProductCode());
	}



	@Override
	public ContractDetailVo getContractDetail(String contractCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanMapper.getContractDetail(contractCode);
	}


	@Override
	public BackMoneyPlan getBackMoneyPlanByCode(String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanMapper.getBackMoneyPlanByCode(backMoneyPlanCode);
	}


	@Override
	public void updateByPrimaryKeySelective(BackMoneyPlan backMoneyPlan) {
		// TODO Auto-generated method stub
		backMoneyPlanMapper.updateByPrimaryKeySelective(backMoneyPlan);
	}


	@Override
	public List<BackMoneyPlanVo> getBackMoneyPlanListByProjectCode(String projectCode) {
		BackMoneyPlanQueryBean queryBean = new BackMoneyPlanQueryBean();
		queryBean.setSelectCondition("1");
		queryBean.setCondition(projectCode);
		queryBean.setBackMoneyStatus(null);
		queryBean.setStartTime(null);
		queryBean.setEndTime(null);
		queryBean.setUserString(null);
		return this.getBackMoneyPlanListByCondition(queryBean);
	}



	@Override
	public BackMoneyPlan getBackMoneyPlanByContractCode(String contractCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanMapper.getBackMoneyPlanByContractCode(contractCode);
	}


	@Override
	public List<BackMoneyPlanVo> getBackMoneyPlanList() {
		List<BackMoneyPlanVo> list = backMoneyPlanMapper.getBackMoneyPlanList();
		if(list!=null&&list.size()>0){
			
			for (BackMoneyPlanVo backMoneyPlanVo : list) {
				//????????????code???????????????
				List<BackMoneyPlanPeriods> periodsList = backMoneyPlanPeriodsMapper.getPeriodsListByPlanCode(backMoneyPlanVo.getBackMoneyPlanCode());
				List<String> listStatus = new ArrayList<String>();
				//???????????????
				Double money = backMoneyPlanVo.getMoney();
				//??????????????????
				Double collectedMoney = 0.00;
				//?????? 
				Double reduceMoney = 0.00;
				//???????????????
				if(periodsList!=null&&periodsList.size()>0){
					for (BackMoneyPlanPeriods backMoneyPlanPeriods : periodsList) {
						/**TODO**/
						//???????????????????????????
						listStatus.add(backMoneyPlanPeriods.getStatus().toString());
						//?????????????????????
						if(backMoneyPlanPeriods.getRealBackMoney()!=null){
							collectedMoney = collectedMoney+backMoneyPlanPeriods.getRealBackMoney();
						}
						//??????
						if(backMoneyPlanPeriods.getType()==3&&backMoneyPlanPeriods.getRealBackMoney()!=null){
							reduceMoney = reduceMoney+backMoneyPlanPeriods.getRealBackMoney();
						}
					}
					//????????????
					backMoneyPlanVo.setCollectedMoney(collectedMoney);
					backMoneyPlanVo.setUnCollectedMoney(money-collectedMoney);
					backMoneyPlanVo.setReduceMoney(reduceMoney);
					
					if(backMoneyPlanVo.getCheckStatus()==2){
						backMoneyPlanVo.setPlanStatus("?????????");
					}else if(backMoneyPlanVo.getCheckStatus()==0){
						backMoneyPlanVo.setPlanStatus("?????????");
					}else if(backMoneyPlanVo.getCheckStatus()==1){
						if(listStatus.get(0).equals("0") && 1 == new HashSet<Object>(listStatus).size()){//???set??????
							backMoneyPlanVo.setPlanStatus("?????????");
						}else if(listStatus.get(0).equals("1") && 1 == new HashSet<Object>(listStatus).size()){
							backMoneyPlanVo.setPlanStatus("?????????");
						}else{
							backMoneyPlanVo.setPlanStatus("?????????");
						}
					}
				}
			}
		}
		return list;
	}


	@Override
	public void financeExceptionUpdateBackPlan(String type,String periodsCode) {
		//???????????????
		BackMoneyPlanPeriods plan = backMoneyPlanPeriodsMapper.selectByPrimaryKey(periodsCode);
		if(type.equals("1")){//(??????)
			if(plan.getType()!=2){//??????????????????????????????????????????
				//??????????????????????????????
				BackMoneyPlanPeriods jsPlan = backMoneyPlanPeriodsMapper.getJSPeriodsByPlanCode(plan.getBackMoneyPlanCode());
				double planBackMoney = plan.getPlanBackMoney()-plan.getRealBackMoney();
				jsPlan.setPlanBackMoney(jsPlan.getPlanBackMoney() + planBackMoney);
				backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(jsPlan);
			}
			//????????????????????????
			plan.setStatus((short)1);
			backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(plan);
		}else if(type.equals("2")){//??????
			List<BackMoneyPlanPeriods> list = backMoneyPlanPeriodsMapper.getPeriodsListByPlanCode(plan.getBackMoneyPlanCode());
			for (BackMoneyPlanPeriods backMoneyPlanPeriods : list) {
				if(backMoneyPlanPeriods.getStatus()!=1){//
					backMoneyPlanPeriods.setStatus((short)1);//???????????????
					backMoneyPlanPeriods.setRealBackMoney(0.00);
					backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(backMoneyPlanPeriods);
				}
			}
		}
			
	}

	/**
	 * ????????????????????????????????????(type:1?????????2?????? periodsCode:?????????id)
	 */
	@Override
	public void actionExceptionUpdateBackPlan(String type,String periodsCode,String money) {
		BackMoneyPlanPeriods plan = backMoneyPlanPeriodsMapper.selectByPrimaryKey(periodsCode);
		//?????????
		BackMoneyPlanPeriods jsPlan = backMoneyPlanPeriodsMapper.getJSPeriodsByPlanCode(plan.getBackMoneyPlanCode());
		if(jsPlan.getStatus()!=1){//??????????????????????????????????????????
			double jsmoney = jsPlan.getPlanBackMoney() - Double.parseDouble(money);
			jsPlan.setPlanBackMoney(jsmoney);
			backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(jsPlan);
		}
	}

	@Override
	public BackMoneyPlan getBackMoneyPlanByContractCodeAndStatus(String contractCode, int status) {
		
		return backMoneyPlanMapper.getBackMoneyPlanByContractCodeAndStatus(contractCode, status);
	}


	@Override
	public List<ContractDetailVo> getContractLists(String condition, int pageNo) {
		return backMoneyPlanMapper.getContractLists(condition,pageNo);
	}


	@Override
	public int getContractListTatal(String condition) {
		return backMoneyPlanMapper.getContractListTatal(condition);
	}


	@Override
	public void deleteByCode(String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		backMoneyPlanMapper.deleteByCode(backMoneyPlanCode);
	}


}
