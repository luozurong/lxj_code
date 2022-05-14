package com.hori.lxjsdk.enums;
/**
 * PAD端请求错误码
 * @author wiliam
 *
 */
public class ServletRspErrorCode {
	
	/**
	 * 消息解析成功
	 */
	public final static String MESSAGE_RSP_SUCCESS = "0";
	
	/**
	 * 游客
	 */
	public final static String MESSAGE_VISITOR_ERROT_CODE = "1";
	public final static String MESSAGE_VISITOR_ERROT_CONTENT = "未注册用户视为游客，无权操作";
	
	/**
	 * 消息格式错误Code
	 */
	public final static String MESSAGE_FORMAT_ERROR_CODE = "001";

	/**
	 * 消息格式错误Content
	 */
	public final static String MESSAGE_FORMAT_ERROR_CONTENT = "消息格式错误";
	
	/**
	 * Token验证错误Code
	 */
	public final static String MESSAGE_TOKEN_VALIDATE_ERROR_CODE = "002";

	/**
	 * Token验证错误Content
	 */
	public final static String MESSAGE_TOKEN_VALIDATE_ERROR_CONTENT = "Token验证错误";
	
	/**
	 * Token过期错误Code
	 */
	public final static String MESSAGE_TOKEN_OVER_TIME_ERROR_CODE = "003";

	/**
	 * Token过期错误Content
	 */
	public final static String MESSAGE_TOKEN_OVER_TIME_ERROR_CONTENT = "Token过期";
	
	/**
	 * 找不到对应的方法错误Code
	 */
	public final static String MESSAGE_NO_EXIST_METHOD_ERROR_CODE = "004";

	/**
	 * 找不到对应的方法错误Content
	 */
	public final static String MESSAGE_NO_EXIST_METHOD_ERROR_CONTENT = "找不到对应的方法";
	
	/**
	 * 服务器响应异常Code
	 */
	public final static String MESSAGE_SERVICE_RESPONSE_ERROR_CODE = "005";
	
	/**
	 * 服务器响应异常Content
	 */
	public final static String MESSAGE_SERVICE_RESPONSE_ERROR_CONTENT = "服务器响应异常";
	/**
	 * 服务器处理任务超时Code
	 */
	public final static String MESSAGE_SERVICE_TIMEOUT_ERROR_CODE = "006";
	
	/**
	 * 服务器处理任务超时Content
	 */
	public final static String MESSAGE_SERVICE_TIMEOUT_ERROR__CONTENT = "服务器处理任务超时异常";
	
	
	/**
	 * 第三方appKey不存在
	 */
	public final static String APP_KEY_UN_VALID = "018";
	public final static String APP_KEY_UN_VALID_CONTENT = "appKey无效";
	
	/**
	 * 第三方userId不存在
	 */
	public final static String USER_ID_NOT_EXIST = "019";
	public final static String USER_ID_NOT_EXIST_CONTENT = "用户ID不存在";
	
	/**
	 * 第三方userId不存在
	 */
	public final static String SDK_PACKAGE_UN_VALID = "020";
	public final static String SDK_PACKAGE_UN_VALID_CONTENT = "SDK包名不正确与APP Key不匹配";
	
}
