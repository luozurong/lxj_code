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

/**
 * 平台小区关联QueryBean
 * @author dell
 *
 */
public class PlatFormJoinAreaQueryBean {
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 接入账号
	 */
	private String joinAccount;
	/**
	 * 申请公司或企业
	 */
	private String company;	
	/**
	 * 小区机构
	 */
	private String areaCode;	
	/**
	 * 添加时间
	 */
	private Date createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJoinAccount() {
		return joinAccount;
	}
	public void setJoinAccount(String joinAccount) {
		this.joinAccount = joinAccount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
