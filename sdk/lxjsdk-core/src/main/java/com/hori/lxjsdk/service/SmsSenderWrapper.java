package com.hori.lxjsdk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 发短信包装器，增加发短信接口功能
 * @author laizs
 * @time 2016-2-24下午2:26:44
 * @file SmsSenderWrapper.java
 *
 */
public interface SmsSenderWrapper  {
	/**
	 * 发短信接口
	 *@author laizs
	 *@param phone 手机号
	 *@param smsContent 短信内容
	 *@param request
	 *@return -1：短信网关发送异常  ，0：成功，1： 同一号码当天发送次数限制（5次），2：同一IP当天发送次数限制（10次） 3:同一号码发短信间隔时间超过限制（120s）
	 */
	int sendSms(String phone, String smsContent,HttpServletRequest request);

	/**
	 * 发短信接口
	 *@author linsp
	 *@param phone 手机号
	 *@param smsContent 短信内容
	 *@param request
	 *@return -1：当天发送同个模板超过10次；无法再次发送   1：成功
	 */
	 int sendSms(String phone, String smsContent,String flag,HttpServletRequest request);
	 
	 
	 /**
	  * 发短信接口(不对发送次数进行限制)
	  *@author sucs
	  *@param phone 手机号
	  *@param smsContent 短信内容
	  *@return true：发送成功,false:发送失败
	  */
	 public boolean sendSms(String phone, String smsContent);
}
