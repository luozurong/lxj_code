package com.hori.lxjsdk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.ProCityAreaTownDao;
import com.hori.lxjsdk.model.ProCityAreaTown;
import com.hori.lxjsdk.service.ProCityAreaTownService;
/**
 * 区域管理
 * @author liaowl
 *
 */
@Service("proCityAreaTownService")
public class ProCityAreaTownServiceImpl implements ProCityAreaTownService {
	@Autowired
	private ProCityAreaTownDao proCityAreaTownDao;
	

	
	public List<ProCityAreaTown> findProCityAreaTownByParentId(String parentId){
		return proCityAreaTownDao.findProCityAreaTownByParentId(parentId);
		
	}
	
	

	@Override
	public List<ProCityAreaTown> getNoParentId() {
		return proCityAreaTownDao.getNoParentId();
	}

	
}
