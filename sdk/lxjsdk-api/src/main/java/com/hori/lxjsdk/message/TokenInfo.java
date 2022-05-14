package com.hori.lxjsdk.message;
/**
 * token封装的信息
 * @author laizs
 * @time 2017年8月7日下午6:46:03
 *
 */
public class TokenInfo {
	private String token;
	/**
	 * token对应的账号
	 */
	private String account;
	/**
	 * -1 token异常  0 token正确  1游客模式
	 */
	private int validateResult=0;
	
	public TokenInfo(String token, String account, int validateResult) {
		super();
		this.token = token;
		this.account = account;
		this.validateResult = validateResult;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getValidateResult() {
		return validateResult;
	}
	public void setValidateResult(int validateResult) {
		this.validateResult = validateResult;
	}
	
}
