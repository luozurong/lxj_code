package com.hori.lxjsdk.enums;

public enum ResponseCode {
	
	/**
	 *  处理请求成功
	 */
	RSP_SUCCESS("0",""),
	
	/**
	 * 消息格式错误
	 */
	MESSAGE_FORMAT_ERROR("001","消息格式错误"),

	/**
	 * Token验证错误
	 */
	TOKEN_VALIDATE_ERROR("002","Token验证错误"),


	/**
	 * Token过期错误Code
	 */
	TOKEN_OVER_TIME_ERROR("003","Token过期"),
	
	
	/**
	 * 找不到对应的方法
	 */
	NO_EXIST_METHOD_ERROR("004","找不到对应的方法"),

	
	/**
	 * 服务器响应异常
	 */
	SERVICE_RESPONSE_ERROR("005","服务器响应异常"),
	
	
	/**
	 * 服务器处理任务超时
	 */
	SERVICE_TIMEOUT_ERROR("006","服务器处理任务超时异常"),

	
	/**
	 * 无权限访问
	 */
	NO_PERMISSION("007","无权限访问"),
	

	/**
	 * 第三方接入账号或密码错误
	 */
	ACCOUNT_OR_PASSWORD_ERROR("008","账号或密码错误"),

	/**
	 * 注册的账号已经存在
	 */
	ACCOUNT_REGISTED("009","用户已经存在"),
	
	/**
	 * 用户不存在
	 */

	ACCOUNT_NOT_EXIST("010","用户不存在"),
	
	/**
	 * 验证码不正确
	 */
	CODE_IS_INCORRECT("011","验证码不正确"),
	
	/**
	 * 验证码已过期
	 */
	CODE_IS_EXPIRE("011","验证码已过期"),
	
	/**
	 * 第三方appKey不存在
	 */
	APP_KEY_UN_VALID("017","appKey无效"),
	
	/**
	 * 第三方userId不存在
	 */
	USER_ID_NOT_EXIST("018","用户ID不存在"),
	
	/**
	 * SDK包名不正确与APP Key不匹配
	 */
	SDK_PACKAGE_UN_VALID("019","SDK包名不正确与APP Key不匹配"),
	
	/**
	 * 原始密码输入有误
	 */
	OLDPASSWORD_NOT_TRUE("020","原始密码输入有误"),
	
	/**
	 * 手机号未绑定用户
	 */

	MOBILE_UNBIND_ACCOUNT("021","手机号未绑定用户");
	
	private String code;
	
	private String msg;

	
	private ResponseCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
}
