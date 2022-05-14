package com.hori.lxjsdk.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author venkee
 *
 */
@Entity(name="PlatFormJoin")
@Table(name="platform_join_list")
public class PlatFormJoin {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 申请公司或企业
	 */
	private String company;
	/**
	 * 接入帐号(联享家生成)
	 */
	private String joinAccount;
	
	/**
	 * 接入密码(联享家生成)
	 */
	private String joinPassword;
	/**
	 * 记录创建时间
	 */
	private Date createTime;
	/**
	 * 记录修改时间
	 */
	private Date updateTime;
	
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	/**
	 * 审核状态,0:审核中 1：审核不通过   2：审核通过
	 */
	private String verifyStatus;
	/**
	 * 审核人
	 */
	private String verifyManager;
	/**
	 * 审核账号
	 */
	private String verifyAccount;
	
	/**
	 * 应用创建账号
	 */
	private String createAccount;
	
	private List<PlatFormJoinArea> platFormJoinAreaList;
	
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
	
	@Basic(optional = true)
	@Column(name = "join_account", insertable = true, updatable = true)
	public String getJoinAccount() {
		return joinAccount;
	}
	public void setJoinAccount(String joinAccount) {
		this.joinAccount = joinAccount;
	}
	
	@Basic(optional = true)
	@Column(name = "join_password", insertable = true, updatable = true)
	public String getJoinPassword() {
		return joinPassword;
	}
	public void setJoinPassword(String joinPassword) {
		this.joinPassword = joinPassword;
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "verify_time")
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
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
	@Column(name = "create_account", insertable = true, updatable = true)
	public String getCreateAccount() {
		return createAccount;
	}
	public void setCreateAccount(String createAccount) {
		this.createAccount = createAccount;
	}
	
	@OneToMany(fetch=FetchType.EAGER)
	@Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
	@JoinColumn(name="platform_join_id")
	public List<PlatFormJoinArea> getPlatFormJoinAreaList() {
		return platFormJoinAreaList;
	}
	public void setPlatFormJoinAreaList(List<PlatFormJoinArea> platFormJoinAreaList) {
		this.platFormJoinAreaList = platFormJoinAreaList;
	}
	
}
