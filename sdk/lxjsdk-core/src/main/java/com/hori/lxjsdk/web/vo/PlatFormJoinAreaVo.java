package com.hori.lxjsdk.web.vo;

public class PlatFormJoinAreaVo {
	private String company;
	private String areaName;
	private String areaAddress;
	private String joinAccount;
	private String verifyStatus;
	private String verifyManager;
	private String verifyAccount;
	
	public PlatFormJoinAreaVo() {
		
	}
	
	public PlatFormJoinAreaVo(String company, String areaName, String areaAddress, String joinAccount,
			 String verifyStatus, String verifyManager,String verifyAccount) {
		super();
		this.company = company;
		this.areaName = areaName;
		this.areaAddress = areaAddress;
		this.joinAccount = joinAccount;
		this.verifyStatus = verifyStatus;
		this.verifyManager = verifyManager;
		this.verifyAccount = verifyAccount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaAddress() {
		return areaAddress;
	}
	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
	}
	public String getJoinAccount() {
		return joinAccount;
	}
	public void setJoinAccount(String joinAccount) {
		this.joinAccount = joinAccount;
	}
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	public String getVerifyManager() {
		return verifyManager;
	}
	public void setVerifyManager(String verifyManager) {
		this.verifyManager = verifyManager;
	}
	public String getVerifyAccount() {
		return verifyAccount;
	}
	public void setVerifyAccount(String verifyAccount) {
		this.verifyAccount = verifyAccount;
	}
	
}
