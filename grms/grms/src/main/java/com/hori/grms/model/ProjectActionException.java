package com.hori.grms.model;

import java.util.Date;

public class ProjectActionException {
    // 主键id
    private String id;
    // 项目编号
    private String projectCode;
    // 异常类型：1执行异常 2财务异常
    private Short type;
    // 执行清单编号（财务异常不与执行编号关联，而是与项目编号关联）
    private String projectActionCode;
    // 异常备注
    private String exceptionRemark;
    // 上报人姓名
    private String createrName;
    // 上报人账号
    private String createrAccount;
    // 创建时间(异常上报时间)
    private Date createTime;
    // 上报人部门id（冗余）
    private String departmentId;
    // 上报人部门名称（冗余）
    private String departmentName;
    // 业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务
    private String businessType;
    // 状态：0未处理 1处理中 2已处理
    private Short status;
    // 处理结果： 1:置换场地 2:扣款 3:继续 4:暂停 5:恢复 6终止
    private Short result;
    // 处理结果备注
    private String resultRemark;
    // 处理人姓名
    private String dealName;
    // 处理人账号
    private String dealAccount;
    // 处理时间
    private Date dealTime;
    // 处理结果是否确认：0否 1是
    private Short confirmStatus;
    // 处理结果确认时间
    private Date confirmTime;
    //关联上级异常Id（异常类型为财务异常使用到）
    private String parentExceptionId;
    //收款计划分期详情id(财务异常需要关联)
    private String planPeriodsId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public Short getType() {
        return type;
    }
    public void setType(Short type) {
        this.type = type;
    }
    public String getProjectActionCode() {
        return projectActionCode;
    }
    public void setProjectActionCode(String projectActionCode) {
        this.projectActionCode = projectActionCode;
    }
    public String getExceptionRemark() {
        return exceptionRemark;
    }
    public void setExceptionRemark(String exceptionRemark) {
        this.exceptionRemark = exceptionRemark;
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
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public Short getStatus() {
        return status;
    }
    public void setStatus(Short status) {
        this.status = status;
    }
    public Short getResult() {
        return result;
    }
    public void setResult(Short result) {
        this.result = result;
    }
    public String getResultRemark() {
        return resultRemark;
    }
    public void setResultRemark(String resultRemark) {
        this.resultRemark = resultRemark;
    }
    public String getDealName() {
        return dealName;
    }
    public void setDealName(String dealName) {
        this.dealName = dealName;
    }
    public String getDealAccount() {
        return dealAccount;
    }
    public void setDealAccount(String dealAccount) {
        this.dealAccount = dealAccount;
    }
    public Date getDealTime() {
        return dealTime;
    }
    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
    public Short getConfirmStatus() {
        return confirmStatus;
    }
    public void setConfirmStatus(Short confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
    public Date getConfirmTime() {
        return confirmTime;
    }
    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }
	public String getParentExceptionId() {
		return parentExceptionId;
	}
	public void setParentExceptionId(String parentExceptionId) {
		this.parentExceptionId = parentExceptionId;
	}
	public String getPlanPeriodsId() {
		return planPeriodsId;
	}
	public void setPlanPeriodsId(String planPeriodsId) {
		this.planPeriodsId = planPeriodsId;
	}
    
}