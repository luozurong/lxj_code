package com.hori.lxjsdk.web.queryBean;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.jlit.webservice.queryBean.BaseQueryBean;

/**
 * 应用QueryBean
 * @author dell
 *
 */
public class ApplicationQueryBean extends BaseQueryBean{
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 申请公司或企业
	 */
	private String company;
	/**
	 * 应用名称
	 */
	private String appName;
	/**
	 * app包名
	 */
	private String appPackage;
	/**
	 * 应用类型 1:android  2：苹果
	 */
	private String appType;
	/**
	 * 应用key
	 */
	private String appKey;
	
	/**
	 * 审核状态,0:审核中 1：审核不通过   2：审核通过
	 */
	private String verifyStatus;
	
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	
	/**
	 * 审核账号
	 */
	private String verifyAccount;
	/**
	 * 应用创建时间
	 */
	private Date createTime;
	/**
	 * 应用修改时间
	 */
	private Date updateTime;
	/**
	 * 应用创建帐号
	 */
	private String createAccount;
	
	/**
	 * 平台接入帐号(联享家生成)
	 * @return
	 */
	private String platformJoinAccount;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getAppPackage() {
		return appPackage;
	}


	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}


	public String getAppType() {
		return appType;
	}


	public void setAppType(String appType) {
		this.appType = appType;
	}


	public String getAppKey() {
		return appKey;
	}


	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}


	public String getVerifyStatus() {
		return verifyStatus;
	}


	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}


	public Date getVerifyTime() {
		return verifyTime;
	}


	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}


	public String getVerifyAccount() {
		return verifyAccount;
	}


	public void setVerifyAccount(String verifyAccount) {
		this.verifyAccount = verifyAccount;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getCreateAccount() {
		return createAccount;
	}


	public void setCreateAccount(String createAccount) {
		this.createAccount = createAccount;
	}


	public String getPlatformJoinAccount() {
		return platformJoinAccount;
	}


	public void setPlatformJoinAccount(String platformJoinAccount) {
		this.platformJoinAccount = platformJoinAccount;
	}


}
