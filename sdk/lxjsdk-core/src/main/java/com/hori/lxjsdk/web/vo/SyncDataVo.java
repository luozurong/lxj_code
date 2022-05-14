package com.hori.lxjsdk.web.vo;

import java.util.List;
import java.util.Map;

/**
 * 同步数据vo
 * @author sucs
 *
 */
public class SyncDataVo {
	
	/**
	 * 小区机构编号
	 */
	private String areaCode;
	
	/**
	 * 操作类型：1、新增  2、修改  3、删除
	 */
	private Integer operateType;
	
	/**
	 * 平台账号
	 */
	private String platFormAccount;
	
	/**
	 * 第三方平台用户列表
	 */
	private List<PlatFormUserVo> list;

	/**
	 * 用于存放业主userId的map,同步第三方平台数据时同一住房地址默认第一个用户为业主,其余为家属
	 */
	private Map<String,String> householdMap;
	
	public Integer getOperateType() {
		return operateType;
	}

	public List<PlatFormUserVo> getList() {
		return list;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public void setList(List<PlatFormUserVo> list) {
		this.list = list;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPlatFormAccount() {
		return platFormAccount;
	}

	public void setPlatFormAccount(String platFormAccount) {
		this.platFormAccount = platFormAccount;
	}
	
	
	public Map<String, String> getHouseholdMap() {
		return householdMap;
	}

	public void setHouseholdMap(Map<String, String> householdMap) {
		this.householdMap = householdMap;
	}
}
