package com.hori.grms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Contract {
    private String id;

    private String contractCode;

    private String projectCode;

    private String projectName;

    private Integer contractType;

    private String contractName;

    private String fromContractCode;
    
    private String fromContractName;
    
    private String money;
    
    private String customerResource;

    private String createrName;

    private String createrAccount;

    private Date createrTime;

    private Date updateTime;

    private Integer status;

    private String attachmentDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getFromContractCode() {
        return fromContractCode;
    }

    public void setFromContractCode(String fromContractCode) {
        this.fromContractCode = fromContractCode;
    }

    

    public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreaterTime() {
        return createrTime;
    }

    public void setCreaterTime(Date createrTime) {
        this.createrTime = createrTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAttachmentDetail() {
        return attachmentDetail;
    }

    public void setAttachmentDetail(String attachmentDetail) {
        this.attachmentDetail = attachmentDetail;
    }

	public String getFromContractName() {
		return fromContractName;
	}

	public void setFromContractName(String fromContractName) {
		this.fromContractName = fromContractName;
	}

	public String getCustomerResource() {
		return customerResource;
	}

	public void setCustomerResource(String customerResource) {
		this.customerResource = customerResource;
	}
	
    
}