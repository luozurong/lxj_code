package com.hori.grms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.BackMoneyPlanPeriodsMapper;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.queryBean.BackMoneyPlanPeriodsQueryBean;
import com.hori.grms.service.BackMoneyPlanPeriodsService;
import com.hori.grms.vo.BackMoneyPlanPeriodsVo;
@Service
public class BackMoneyPlanPeriodsServiceImpl implements BackMoneyPlanPeriodsService {

	@Autowired
	private BackMoneyPlanPeriodsMapper backMoneyPlanPeriodsMapper;

	@Override
	public void insertSelective(BackMoneyPlanPeriods backMoneyPlanPeriods) {
		// TODO Auto-generated method stub
		backMoneyPlanPeriodsMapper.insertSelective(backMoneyPlanPeriods);
	}


	@Override
	public void updateByPrimaryKeySelective(
			BackMoneyPlanPeriods backMoneyPlanPeriods) {
		// TODO Auto-generated method stub
		backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(backMoneyPlanPeriods);
	}

	@Override
	public BackMoneyPlanPeriods selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BackMoneyPlanPeriods> getPeriodsListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getPeriodsListByPlanCode(backMoneyPlanCode);
	}

	@Override
	public void deleteByCode(String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		backMoneyPlanPeriodsMapper.deleteByCode(backMoneyPlanCode);
	}


	@Override
	public List<BackMoneyPlanPeriodsVo> getBackMoneyPlanListByCondition(BackMoneyPlanPeriodsQueryBean queryBean) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getBackMoneyPlanListByCondition(queryBean);
	}


	@Override
	public List<BackMoneyPlanPeriods> getNotBackPlanList() {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getNotBackPlanList();
	}


	@Override
	public List<BackMoneyPlanPeriods> getJDListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getJDListByPlanCode(backMoneyPlanCode);
	}


	@Override
	public List<BackMoneyPlanPeriods> getKKListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getKKListByPlanCode(backMoneyPlanCode);
	}


	@Override
	public List<BackMoneyPlanPeriods> getJSListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getJSListByPlanCode(backMoneyPlanCode);
	}


	@Override
	public List<BackMoneyPlanPeriods> getJDYCListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getJDYCListByPlanCode(backMoneyPlanCode);
	}


	@Override
	public List<BackMoneyPlanPeriods> getWSListByPlanCode(
			String backMoneyPlanCode) {
		// TODO Auto-generated method stub
		return backMoneyPlanPeriodsMapper.getWSListByPlanCode(backMoneyPlanCode);
	}
	
}
