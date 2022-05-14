package com.hori.grms.vo;

/**
 *
 * @author hehj
 * @datetime 2018年8月14日 上午10:36:34
 */
public class ProjectActionExceptionVo {
	// 部门id
	private String departmentId;
	// 部门名称
	private String departmentName;
	// 业务类型
	private String businessType;
	// 异常记录id
	private String exceptionId;
	// 清单ID
	private String projectActionCode;
	// 异常上报时间
	private String createTime;
	// 异常原因
	private String exceptionRemark;
	// 异常状态
	private String status;
	// 执行部门确认状态
	private String confirmStatus;
	// 异常处理结果
	private String result;
	// 执行清单领取人
	private String receiveAccount;
	
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
	public String getExceptionId() {
		return exceptionId;
	}
	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}
	public String getProjectActionCode() {
		return projectActionCode;
	}
	public void setProjectActionCode(String projectActionCode) {
		this.projectActionCode = projectActionCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getExceptionRemark() {
		return exceptionRemark;
	}
	public void setExceptionRemark(String exceptionRemark) {
		this.exceptionRemark = exceptionRemark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	
}