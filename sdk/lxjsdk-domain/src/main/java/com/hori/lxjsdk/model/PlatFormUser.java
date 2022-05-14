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

@Entity(name="PlatFormUser")
@Table(name="platform_user")
public class PlatFormUser {
	
	/**
	 * 主键ID
	 */
	private String id;
	
	/**
	 * 接入账号
	 */
	private String platformAccount;
	
	/**
	 * 小区机构编号
	 */
	private String areaCode;
	
	/**
	 * 第三方平台用户id
	 */
	private String userId;
	
	/**
	 *住户编号
	 */
	private String householdSerial;	
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 用户类型（0：业主  1：家属）,同步第三方平台数据时同一住房地址默认第一个用户为业主,其余为家属
	 */
	private Integer userType;	
	
	/**
	 * 0:未绑定app账号 1:已绑定 2:已删除
	 */
	private Integer status;
	
	/**
	 * pms人员信息
	 */
	private String pmsPeopleId;
	/**
	 * pms人员住址id
	 */
	private String pmsHouseholdAddressId;
	
	/**
	 * app账号
	 */
	private String appOauthAccount;
	
	/**
	 * 住户地址
	 */
	private String address;
	
	
	/**
	 * 添加时间
	 */
	private Date createTime;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.jlit.hibernate.UUIDGenerator")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}
	
	@Column(name = "platform_account", insertable = true, updatable = true)
	public String getPlatformAccount() {
		return platformAccount;
	}
	
	@Column(name = "area_code", insertable = true, updatable = true)
	public String getAreaCode() {
		return areaCode;
	}
	
	@Column(name = "user_id", insertable = true, updatable = true)
	public String getUserId() {
		return userId;
	}
	
	@Column(name = "household_serial", insertable = true, updatable = true)
	public String getHouseholdSerial() {
		return householdSerial;
	}
	
	@Column(name = "user_name", insertable = true, updatable = true)
	public String getUserName() {
		return userName;
	}
	
	@Column(name = "mobile", insertable = true, updatable = true)
	public String getMobile() {
		return mobile;
	}
	@Column(name = "user_type", insertable = true, updatable = true)
	public Integer getUserType() {
		return userType;
	}
	
	@Column(name = "status", insertable = true, updatable = true)
	public Integer getStatus() {
		return status;
	}
	
	@Column(name = "pms_people_id", insertable = true, updatable = true)
	public String getPmsPeopleId() {
		return pmsPeopleId;
	}
	
	@Column(name = "app_oauth_account", insertable = true, updatable = true)
	public String getAppOauthAccount() {
		return appOauthAccount;
	}	
	
	@Column(name = "address", insertable = true, updatable = true)
	public String getAddress() {
		return address;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	@Column(name = "pms_household_address_id", insertable = true, updatable = true)
	public String getPmsHouseholdAddressId() {
		return pmsHouseholdAddressId;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public void setPlatformAccount(String platformAccount) {
		this.platformAccount = platformAccount;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setHouseholdSerial(String householdSerial) {
		this.householdSerial = householdSerial;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setPmsPeopleId(String pmsPeopleId) {
		this.pmsPeopleId = pmsPeopleId;
	}

	public void setAppOauthAccount(String appOauthAccount) {
		this.appOauthAccount = appOauthAccount;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setPmsHouseholdAddressId(String pmsHouseholdAddressId) {
		this.pmsHouseholdAddressId = pmsHouseholdAddressId;
	}
}
