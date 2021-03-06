package com.hori.grms.service.impl;

import java.util.List;

import org.apache.cxf.Bus.BusState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hori.grms.dao.DepartmentBusinessAreaMapper;
import com.hori.grms.dao.DepartmentBusinessTypeMapper;
import com.hori.grms.model.DepartmentBusinessArea;
import com.hori.grms.model.DepartmentBusinessType;
import com.hori.grms.service.DepartmentBusinessService;
import com.hori.grms.vo.DepartmentBusinessVo;
@Service
public class DepartmentBusinessServiceImpl implements DepartmentBusinessService {

	@Autowired
	private DepartmentBusinessTypeMapper businessTypeMapper;
	
	@Autowired
	private DepartmentBusinessAreaMapper businessAreaMapper;
	
	@Override
	public List<DepartmentBusinessType> getBusinessTypeList(String condition) {
		// TODO Auto-generated method stub
		return businessTypeMapper.getBusinessTypeList(condition);
	}

	@Override
	public void delDepartmentBusinessType(String id) {
		// TODO Auto-generated method stub
		businessTypeMapper.delDepartmentBusinessType(id);
	}

	@Override
	public void delDepartmentBusinessArea(String id) {
		// TODO Auto-generated method stub
		businessAreaMapper.delDepartmentBusinessArea(id);
	}
	@Override
	public void insertType(DepartmentBusinessType type) {
		// TODO Auto-generated method stub
		businessTypeMapper.insert(type);
	}

	@Override
	public void insertArea(DepartmentBusinessArea area) {
		// TODO Auto-generated method stub
		businessAreaMapper.insert(area);
	}

	@Override
	public List<DepartmentBusinessType> findByBusinessType(String businessType) {
		Assert.notNull(businessType, "businessType 不能为空");
		return this.businessTypeMapper.findByBusinessType(businessType);
	}

	@Override
	public List<DepartmentBusinessType> findByBusinessAreaCity(String city, String businessType) {
		Assert.notNull(city, "city不能为空");
		Assert.notNull(businessType, "businessType不能为空");
		return this.businessTypeMapper.findByBusinessAreaCity(city, businessType);
	}

	@Override
	public DepartmentBusinessType selectTypeByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return businessTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public DepartmentBusinessArea selectAreaByBusinessTypeId(String businessTypeId) {
		// TODO Auto-generated method stub
		return businessAreaMapper.selectAreaByBusinessTypeId(businessTypeId);
	}
	
	@Override
	public List<DepartmentBusinessVo> getBusinessVoList(String condition) {
		// TODO Auto-generated method stub
		return businessTypeMapper.getBusinessVoList(condition);
	}
	
	@Override
	public DepartmentBusinessVo getDepartmentAreaById(String businessTypeId) {
		// TODO Auto-generated method stub
		return businessAreaMapper.getDepartmentAreaById(businessTypeId);
	}

	@Override
	public void updateType(DepartmentBusinessType type) {
		// TODO Auto-generated method stub
		businessTypeMapper.updateByPrimaryKeySelective(type);
	}

	@Override
	public void updateArea(DepartmentBusinessArea area) {
		// TODO Auto-generated method stub
		businessAreaMapper.updateByPrimaryKeySelective(area);
	}

	@Override
	public DepartmentBusinessType findByBusinessAreaProvince(String businessType) {
		return businessTypeMapper.findByBusinessAreaProvince(businessType);
	}

	@Override
	public DepartmentBusinessType findByBusinessAreaState(String businessType) {
		return businessTypeMapper.findByBusinessAreaState(businessType);
	}

	
}
