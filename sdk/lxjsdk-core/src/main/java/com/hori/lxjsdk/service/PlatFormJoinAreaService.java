package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.web.vo.PlatFormJoinAreaVo;

public interface PlatFormJoinAreaService {

	/**
	 * 根据平台接入账号查找该账号关联的小区
	 * @param account 账号
	 * @return
	 */
	List<PlatFormJoinArea> findByAccount(String account);
	
	
	/**
	 * 根据平台接入账号和小区机构编号查找该账号关联的小区
	 * @param account 账号
	 * @param areaCode 小区机构编号
	 * @return
	 */
	PlatFormJoinArea findByAccountAndAreaCode(String account,String areaCode);
	
	/**
	 * 根据id获取记录
	 * @param id
	 * @return
	 */
	PlatFormJoinArea getById(String id);
	
	/**
	 * 根据id获取接入小区详细信息
	 * @param id
	 * @return
	 */
	PlatFormJoinAreaVo getPlatFormJoinAreaInfo(String id);
	
	/**
	 * 更新
	 * @param formJoinArea
	 */
	void update(PlatFormJoinArea formJoinArea);
	

	/**
	 * 添加小区
	 * @param platFormJoinArea
	 */
	void addPlatFormJoinArea(PlatFormJoinArea platFormJoinArea);

	//根据平台ID获取接入小区列表
	List<PlatFormJoinArea> getByPlatformJoinId(String id);
	
	
}
