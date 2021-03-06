/*
 * ProjectProductMenu.java
 * Copyright(C) 20xx-2015 xxxxxx公司
 * All rights reserved.
 * -----------------------------------------------
 * 2018-08-10 Created
 */
package com.hori.grms.model;

import java.util.Date;

/**
 * 项目产品明细
 * 
 * @author 
 * @version 1.0 2018-08-10
 */
public class ProjectProductMenu {

    //id
    private String id;
    //项目产品id（基础信息）
    private String projectProductId;
    //产品明细id
    private String productMenuId;
    //购买数量
    private Integer buyNum;
    //项目编号
    private String projectCode;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //关联小区名称列表
    private String areaNames;
    //资源是否已经被锁住，0 否 1是
    private Short locked;    
    //其他备用字段1
    private Integer otherNum1;
    

	public Integer getOtherNum1() {
		return otherNum1;
	}
	public void setOtherNum1(Integer otherNum1) {
		this.otherNum1 = otherNum1;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProjectProductId() {
        return projectProductId;
    }
    public void setProjectProductId(String projectProductId) {
        this.projectProductId = projectProductId;
    }
    public String getProductMenuId() {
        return productMenuId;
    }
    public void setProductMenuId(String productMenuId) {
        this.productMenuId = productMenuId;
    }
    public Integer getBuyNum() {
        return buyNum;
    }
    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }
    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public Date getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getAreaNames() {
        return areaNames;
    }
    public void setAreaNames(String areaNames) {
        this.areaNames = areaNames;
    }
    public Short getLocked() {
        return locked;
    }
    public void setLocked(Short locked) {
        this.locked = locked;
    }
}