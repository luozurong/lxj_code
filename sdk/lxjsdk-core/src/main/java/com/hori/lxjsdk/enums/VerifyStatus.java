package com.hori.lxjsdk.enums;

public enum VerifyStatus {
	
	/** 
	 * 审核中
	 */
	CHECKING("0","审核中"),
	
	/**
	 * 审核不通过
	 */
	UNSUCCESS("1","审核不通过"),

	/**
	 * 审核通过
	 */
	SUCCESS("2","审核通过");

	
	private String status;
	
	private String msg;

	
	private VerifyStatus(String status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
	
	public static VerifyStatus getByStatus(String status){
		
		VerifyStatus[] values = VerifyStatus.values();
		
		for (VerifyStatus verifyStatus : values) {
			if(verifyStatus.getStatus().equals(status)){
				return verifyStatus;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		VerifyStatus s=VerifyStatus.getByStatus("1");
		System.out.println(s);
	}
}
