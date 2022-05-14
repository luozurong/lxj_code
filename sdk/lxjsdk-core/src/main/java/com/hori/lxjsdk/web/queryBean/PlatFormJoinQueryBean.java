package com.hori.lxjsdk.web.queryBean;

import com.jlit.webservice.queryBean.BaseQueryBean;

public class PlatFormJoinQueryBean extends BaseQueryBean{
	private String company;
	private String verifyStatus;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	
}
