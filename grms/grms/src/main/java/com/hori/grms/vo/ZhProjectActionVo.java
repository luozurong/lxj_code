package com.hori.grms.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 综合部门-用户媒体电商执行清单Vo
 * @author 
 * @datetime 
 */
public class ZhProjectActionVo {
	// 项目清单主键id
	private String projectProductId;
	// 执行开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date beginTime;
	// 执行结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date endTime;
	// 业务类型：1社区运营 2 媒管 3用户运营 4电商运营
	private String businessType;
	// 项目执行清单主键id
	private String actionCode;
	//场地数量
	private Long areaNum;
	// 项目名称
	private String projectName;
	// 执行状态（1：待确认 2：策划中 3：待执行 3：执行中  4：已完成）
	private Integer actionStatus;
	// 异常状态：1：正常 0：异常
	private Integer exceptionStatus;
	public String getProjectProductId() {
		return projectProductId;
	}
	public void setProjectProductId(String projectProductId) {
		this.projectProductId = projectProductId;
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
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	
	
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(Integer actionStatus) {
		this.actionStatus = actionStatus;
	}
	public Integer getExceptionStatus() {
		return exceptionStatus;
	}
	public void setExceptionStatus(Integer exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
	public Long getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(Long areaNum) {
		this.areaNum = areaNum;
	}
	
	
}
