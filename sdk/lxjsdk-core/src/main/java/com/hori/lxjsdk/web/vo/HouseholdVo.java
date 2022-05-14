package com.hori.lxjsdk.web.vo;

public class HouseholdVo {
	
	private String id;

	//住户姓名
	private String householdName;
	
	//住户编号
	private String householdSerial;
	
	//住户地址
	private String address;
	
	//区域序列号
	private String regionSerial;
	
	//单元序列号
	private String unitSerial;
	
	//机构序列号
	private String organizationSeq;
	
	//匹配公安系统的30位id
	private String xyid;

	public String getId() {
		return id;
	}

	public String getHouseholdName() {
		return householdName;
	}

	public String getHouseholdSerial() {
		return householdSerial;
	}

	public String getAddress() {
		return address;
	}

	public String getRegionSerial() {
		return regionSerial;
	}

	public String getUnitSerial() {
		return unitSerial;
	}

	public String getOrganizationSeq() {
		return organizationSeq;
	}

	public String getXyid() {
		return xyid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setHouseholdName(String householdName) {
		this.householdName = householdName;
	}

	public void setHouseholdSerial(String householdSerial) {
		this.householdSerial = householdSerial;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setRegionSerial(String regionSerial) {
		this.regionSerial = regionSerial;
	}

	public void setUnitSerial(String unitSerial) {
		this.unitSerial = unitSerial;
	}

	public void setOrganizationSeq(String organizationSeq) {
		this.organizationSeq = organizationSeq;
	}

	public void setXyid(String xyid) {
		this.xyid = xyid;
	}
	
}
