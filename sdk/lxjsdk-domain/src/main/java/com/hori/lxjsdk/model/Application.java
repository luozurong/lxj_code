package com.hori.lxjsdk.model;

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

/**
 * 应用实体
 * @author dell
 *
 */
@Entity(name="Application")
@Table(name="sdk_list")
public class Application {
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
	 * 审核人
	 */
	private String verifyManager;
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
	



	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.jlit.hibernate.UUIDGenerator")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Basic(optional = true)
	@Column(name = "company", insertable = true, updatable = true)
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Basic(optional = true)
	@Column(name = "verify_status", insertable = true, updatable = true)
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	
	@Basic(optional = true)
	@Column(name = "verify_manager", insertable = true, updatable = true)
	public String getVerifyManager() {
		return verifyManager;
	}

	public void setVerifyManager(String verifyManager) {
		this.verifyManager = verifyManager;
	}

	@Basic(optional = true)
	@Column(name = "verify_account", insertable = true, updatable = true)
	public String getVerifyAccount() {
		return verifyAccount;
	}
	public void setVerifyAccount(String verifyAccount) {
		this.verifyAccount = verifyAccount;
	}
	
	@Basic(optional = true)
	@Column(name = "app_name", insertable = true, updatable = true)
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Basic(optional = true)
	@Column(name = "app_package", insertable = true, updatable = true)
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
	@Basic(optional = true)
	@Column(name = "app_type", insertable = true, updatable = true)
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	@Basic(optional = true)
	@Column(name = "app_key", insertable = true, updatable = true)
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "verify_time")
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	
	@Basic(optional = true)
	@Column(name = "create_account", insertable = true, updatable = true)
	public String getCreateAccount() {
		return createAccount;
	}
	public void setCreateAccount(String createAccount) {
		this.createAccount = createAccount;
	}
	
	
	@Basic(optional = true)
	@Column(name = "platform_join_account", insertable = true, updatable = true)
	public String getPlatformJoinAccount() {
		return platformJoinAccount;
	}
	public void setPlatformJoinAccount(String platformJoinAccount) {
		this.platformJoinAccount = platformJoinAccount;
	}
	
	
	
}
