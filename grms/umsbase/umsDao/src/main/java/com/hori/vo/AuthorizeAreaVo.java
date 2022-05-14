package com.hori.vo;

import java.util.Date;

public class AuthorizeAreaVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户责任范围ID
	 */
	private String authorizeAreaId;
	/**
	 * 所属系统ID
	 */
	private String systemId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 省编码
	 */
	private String provinceId;
	/**
	 * 省名称
	 */
	private String provinceName;
	/**
	 * 市编码
	 */
	private String cityId;
	/**
	 * 市名称
	 */
	private String cityName;
	/**
	 * 区名称
	 */
	private String areaName;
	/**
	 * 区编码
	 */
	private String areaId;
	
	private Date createTime;
	private Date modifiedTime;
	private String note;
	public AuthorizeAreaVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AuthorizeAreaVo(String authorizeAreaId, String systemId, String userId, String provinceId,
			String provinceName, String cityId, String cityName, String areaName, String areaId, Date createTime,
			Date modifiedTime, String note) {
		super();
		this.authorizeAreaId = authorizeAreaId;
		this.systemId = systemId;
		this.userId = userId;
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.cityId = cityId;
		this.cityName = cityName;
		this.areaName = areaName;
		this.areaId = areaId;
		this.createTime = createTime;
		this.modifiedTime = modifiedTime;
		this.note = note;
	}
	public String getAuthorizeAreaId() {
		return authorizeAreaId;
	}
	public void setAuthorizeAreaId(String authorizeAreaId) {
		this.authorizeAreaId = authorizeAreaId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
