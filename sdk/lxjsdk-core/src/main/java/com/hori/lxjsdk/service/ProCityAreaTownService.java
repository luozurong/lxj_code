package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.model.ProCityAreaTown;


/**
 * 区域管理
 * @author liaowl
 *
 */

public interface ProCityAreaTownService {
	
	/**
	 * 通过父类的parentId查找到相关的ProCityAreaTown集合数据
	 * @param parentId
	 * @return
	 */
	
	public List<ProCityAreaTown> findProCityAreaTownByParentId(String parentId);
	

	
	/**
	 * 查询所有省份provence
	 * @return
	 */
	public List<ProCityAreaTown> getNoParentId();

	
	
}
