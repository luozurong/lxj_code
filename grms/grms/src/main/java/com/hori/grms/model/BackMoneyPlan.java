package com.hori.grms.model;

import java.util.Date;

public class BackMoneyPlan {
    private String id;

    private String backMoneyPlanCode;

    private String contractCode;

    private String createrName;

    private String createrAccount;

    private String createrLevel;

    private Date createTime;

    private Date updateTime;

    private String checkerAccount;

    private String checkerName;

    private Date checkerTime;

    private String checkerLevel;

    private Short checkStatus;

    private String allbackRemark;
    
    public String getAllbackRemark() {
		return allbackRemark;
	}

	public void setAllbackRemark(String allbackRemark) {
		this.allbackRemark = allbackRemark;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackMoneyPlanCode() {
        return backMoneyPlanCode;
    }

    public void setBackMoneyPlanCode(String backMoneyPlanCode) {
        this.backMoneyPlanCode = backMoneyPlanCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCreaterAccount() {
        return createrAccount;
    }

    public void setCreaterAccount(String createrAccount) {
        this.createrAccount = createrAccount;
    }

    public String getCreaterLevel() {
        return createrLevel;
    }

    public void setCreaterLevel(String createrLevel) {
        this.createrLevel = createrLevel;
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

    public String getCheckerAccount() {
        return checkerAccount;
    }

    public void setCheckerAccount(String checkerAccount) {
        this.checkerAccount = checkerAccount;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public Date getCheckerTime() {
        return checkerTime;
    }

    public void setCheckerTime(Date checkerTime) {
        this.checkerTime = checkerTime;
    }

    public String getCheckerLevel() {
        return checkerLevel;
    }

    public void setCheckerLevel(String checkerLevel) {
        this.checkerLevel = checkerLevel;
    }

    public Short getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Short checkStatus) {
        this.checkStatus = checkStatus;
    }
}