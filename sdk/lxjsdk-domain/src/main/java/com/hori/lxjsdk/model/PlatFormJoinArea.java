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
 * 平台小区关联
 * @author dell
 *
 */
@Entity(name="PlatFormJoinArea")
@Table(name="platform_join_area")
public class PlatFormJoinArea {
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 接入账号
	 */
	private String joinAccount;
	/**
	 * 小区机构
	 */
	private String areaCode;	
	/**
	 * 添加时间
	 */
	private Date createTime;
	/**
	 * 小区地址
	 */
	private String areaAddress;
	/**
	 * 小区名称
	 */
	private String areaName;
	
	/**
	 * 审核状态,0:审核中 1：审核不通过   2：审核通过
	 */
	private String verifyStatus;
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	/**
	 * 关联平台ID
	 */
	private String platformJoinId;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.jlit.hibernate.UUIDGenerator")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Basic(optional = true)
	@Column(name = "join_account", insertable = true, updatable = true)
	public String getJoinAccount() {
		return joinAccount;
	}
	public void setJoinAccount(String joinAccount) {
		this.joinAccount = joinAccount;
	}
	
	@Basic(optional = true)
	@Column(name = "area_code", insertable = true, updatable = true)
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@Basic(optional = true)
	@Column(name = "area_address", insertable = true, updatable = true)
	public String getAreaAddress() {
		return areaAddress;
	}
	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
	}
	
	@Basic(optional = true)
	@Column(name = "area_name", insertable = true, updatable = true)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	@Column(name = "platform_join_id", insertable = true, updatable = true)
	public String getPlatformJoinId() {
		return platformJoinId;
	}
	public void setPlatformJoinId(String platformJoinId) {
		this.platformJoinId = platformJoinId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "verify_time")
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	
	
}
