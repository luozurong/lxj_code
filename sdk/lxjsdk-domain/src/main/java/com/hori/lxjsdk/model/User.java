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
 * 用户
 * @author dell
 *
 */
@Entity(name="User")
@Table(name="user")
public class User {
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 用户创建时间
	 */
	private Date createTime;
	/**
	 * 用户注册账号
	 */
	private String userAccount;
	/**
	 * 用户密码
	 */
	private String password;
	/**
	 * 用户手机号码
	 */
	private String mobile;
	
	
	
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
	@Column(name = "user_account", insertable = true, updatable = true)
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	@Basic(optional = true)
	@Column(name = "password", insertable = true, updatable = true)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Basic(optional = true)
	@Column(name = "mobile", insertable = true, updatable = true)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
