package com.hori.lxjsdk.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hori.lxjsdk.service.SmsSenderWrapper;
import com.hori.lxjsdk.utils.CookieUtil;
import com.jlit.memcached.session.SessionService;
import com.jlit.utils.DateUtil;
import com.trisun.message.sms.SmsSender;
/**
 * 发短信包装器，增加发短信接口功能
 * @author laizs
 * @time 2016-2-24下午2:33:53
 * @file SmsSenderWrapperImpl.java
 *
 */
@Service("smsSenderWrapper")
public class SmsSenderWrapperImpl implements SmsSenderWrapper {
	private final static Logger logger=Logger.getLogger(SmsSenderWrapperImpl.class);
	private final static SimpleDateFormat DATE_SDF=new SimpleDateFormat("yyyyMMdd");
	//同一IP当天发送次数限制
	private final static int IP_LIMIT_TIMES=10;
	//同一号码当天发送次数限制
	private final static int PHONE_LIMIT_TIMES=5;
	//同一号码发送短信时间间隔，120s
	private final static long TIME_LIMIT_SECOND=120;
	//同个模板当天发送次数限制
	private final static int PHONE_TEMPLATE_TIMES =10;
	@Resource
	private SmsSender smsSender;
	
	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
	
	/**
	 * 发短信接口
	 *@author linsp
	 *@param phone 手机号
	 *@param smsContent 短信内容
	 *@param flag 0 :注册获取验证码操作   1： 绑定房间获取验证码 2：解绑房间获取验证码 3：添加分机获取验证码 4：更换手机	 5:重置密码   6 申请成为分机  7：业主扫描二维码绑定单个房间
	 *@param request
	 *@return -1：当天发送同个模板超过10次；无法再次发送   1：成功
	 */
	@Override
	public int sendSms(String phone, String smsContent,String flag,
			HttpServletRequest request) {
		/*//获取ua，用来判断是否为移动端访问  
		String userAgent = "";
		if(request.getHeader( "USER-AGENT" ) != null){
			userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();   
		}
		if(userAgent.equals("")){
			userAgent = "android";//为空暂时默认android处理
		}*/

		String ip=CookieUtil.getIpAddr(request);
		logger.info("发送短信请求，phone："+phone+",flag:"+flag+",ip:"+ip);
		boolean checkPhoneLimit = checkPhoneTemplateLimit(phone,flag);
		if(!checkPhoneLimit){
			return -1;
		}
		try {
			this.smsSender.sendSms(phone, smsContent);
			//增加相关的记录的次数
			increPhoneTemplateTimes(phone,flag);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;//网关异常
		}
		return 1;//成功
	}	
	
	
	/**
	 * 发短信接口(不对发送次数进行限制)
	 *@author sucs
	 *@param phone 手机号
	 *@param smsContent 短信内容
	 *@return true：发送成功,false:发送失败
	 */
	@Override
	public boolean sendSms(String phone, String smsContent) {

		try {
			this.smsSender.sendSms(phone, smsContent);
		} catch (Exception e) {
			e.printStackTrace();
			return false;//网关异常
		}
		return true;//成功
	}	
	
	/**
	 * 发送一次短信，计数+1
	 * @param phone
	 * @param flag
	 */
	private void increPhoneTemplateTimes(String phone, String flag) {
		String dateStr = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
		String key = new StringBuilder("PHONE_TEMPLATE_TIMES").append("-").append(dateStr).append("-").append(phone).append(flag).toString();
		Integer times=(Integer) SessionService.getInstance().get(key);	
		if(times ==null){
			times = 1;
		}else{
			times++;
		}
		SessionService.getInstance().save(key,times);
		//存memcache，有限期1天
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);// 有效期1天
		SessionService.getInstance().saveOutTime(key, times, calendar.getTime());
		logger.info("保存客户端当天发送短信模板次数memcached,key:"+key+",value:"+times);
	}
	/**
	 * 发送一次短信，ip计数+1
	 * @param phone
	 * @param flag
	 */
	private void increIpTimes(String ip) {
		String key = genIpLimitKey(ip);
		Integer times=(Integer) SessionService.getInstance().get(key);	
		if(times ==null){
			times = 1;
		}else{
			times++;
		}
		//存memcache，有限期1天
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);// 有效期1天
		SessionService.getInstance().saveOutTime(key, times, calendar.getTime());
		logger.info("保存客户端ip当天发送短信次数memcached,key:"+key+",value:"+times);
	}
	/**
	 * 生成ip限制的key
	 * @param ip
	 * @return
	 */
	private String genIpLimitKey(String ip){
		String dateStr = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
		String key = new StringBuilder("IP_TIMES").append("-").append(dateStr).append("-").append(ip).toString();
		return key;
	}

	/**
	 * 根据业务标志，判断当天同个模板发送次数是否超过10次
	 * @param phone
	 * @param flag
	 */
	private boolean checkPhoneTemplateLimit(String phone, String flag) {
		String dateStr = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
		
		//key:PHONE_TEMPLATE_TIMES-yyyy-mm-dd-phone-flag
		String key = new StringBuilder("PHONE_TEMPLATE_TIMES").append("-").append(dateStr).append("-").append(phone).append(flag).toString();
		Integer times=(Integer) SessionService.getInstance().get(key);
		if(times!=null && times>=PHONE_TEMPLATE_TIMES){
			logger.info("手机号超过当天模板限制（10）次数，phone："+phone+",flag:"+flag+",总次数："+times);
			return false;
		}
		return true;
	}

	@Override
	public int sendSms(String phone, String smsContent,
			HttpServletRequest request) {
		//检测同一号码每天发送短信次数超过限制
		boolean limitC=checkPhoneLimit(phone);
		if(!limitC){
			return 1;//同一号码当天发送次数超过限制
		}
		//检测同一号码发送短信时间间隔是否超过限制
		limitC=checkSendTimeLimit(phone);
		if(!limitC){
			return 3;//时间间隔超过限制
		}
		//检测客户端的ip每天发送短信次数超过限制
		//请求客户端的ip地址
		String ipAddr=CookieUtil.getIpAddr(request);
		limitC=checkIpAddrLimit(ipAddr);
		if(!limitC){
			return 2;//同一IP当天发送次数超过限制
		}
		try {
			this.smsSender.sendSms(phone, smsContent);
			//增加相关的记录的次数
			incrSmsSendPhoneTimes(phone);
			incrSmsSendIpAddrTimes(ipAddr);
			saveSendTime2mem(phone, new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;//网关异常
		}
		return 0;//成功
	}
	/**
	 * 判断手机号码是否超过每天发短息次数限制
	 *@author laizs
	 *@param phone
	 *@return true 没达到限制，可发送   false已达到限制，不可发送
	 */
	private boolean checkPhoneLimit(String phone){
		String key=genPhoneLimitKey2mem(phone);
		Integer times=(Integer) SessionService.getInstance().get(key);
		if(null==times){
			times=0;
		}
		if(times>=PHONE_LIMIT_TIMES){
			return false;
		}
		return true;
	}
	/**
	 * 判断客户端ip是否超过每天发短息次数限制
	 *@author laizs
	 *@param ipAddr
	 *@return true 没达到限制，可发送   false已达到限制，不可发送
	 */
	private boolean checkIpAddrLimit(String ipAddr){
		String key=genIpAddrLimitKey2mem(ipAddr);
		Integer times=(Integer) SessionService.getInstance().get(key);
		if(null==times){
			times=0;
		}
		if(times>=IP_LIMIT_TIMES){
			return false;
		}
		return true;
	}
	/**
	 * 判断同一号码发短信间隔时间超过限制
	 *@author laizs
	 *@param phone
	 *@return true 没达到限制，可发送   false已达到限制，不可发送
	 */
	private boolean checkSendTimeLimit(String phone){
		long nowTimeStamp=new Date().getTime();
		String key=genPhoneLastSendTimeKey2mem(phone);
		Long lastTimeStamp=(Long) SessionService.getInstance().get(key);
		if(null!=lastTimeStamp){
			long s=(nowTimeStamp-lastTimeStamp)/1000;//时间间隔，s
			logger.info("手机号"+phone+"与上次发短信时间间隔："+s+"s");
			if(s<=TIME_LIMIT_SECOND){
				return false;//超出时间间隔
			}
		}
		
		return true;
	}
	/**
	 * 自增手机号码当天发短信次数，并save到memcached
	 *@author laizs
	 *@param ipAddr
	 */
	private void incrSmsSendPhoneTimes(String phone){
		String key=genPhoneLimitKey2mem(phone);
		Integer times=(Integer) SessionService.getInstance().get(key);
		if(null==times){
			times=0;
		}
		++times;
		//存memcache，有限期2天
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);// 有效期2天
		SessionService.getInstance().saveOutTime(key, times, calendar.getTime());
		logger.info("保存手机号码当天发短信次数到memcached,key:"+key+",value:"+times);
	}
	/**
	 * 自增客户端ip当天发短信次数，并save到memcached
	 *@author laizs
	 *@param ipAddr
	 */
	private void incrSmsSendIpAddrTimes(String ipAddr){
		String key=genIpAddrLimitKey2mem(ipAddr);
		Integer times=(Integer) SessionService.getInstance().get(key);
		if(null==times){
			times=0;
		}
		++times;
		//存memcache，有限期2天
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);// 有效期2天
		SessionService.getInstance().saveOutTime(key, times, calendar.getTime());
		logger.info("保存客户端ip当天发短信次数到memcached,key:"+key+",value:"+times);
	}
	/**
	 * 保存发送短信时间的时间戳存到 memcached
	 *@author laizs
	 *@param phone
	 */
	private void saveSendTime2mem(String phone,Date sendTime){
		String key=genPhoneLastSendTimeKey2mem(phone);
		//存memcache，有限期2天
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);// 有效期2天
		long timeStamp=sendTime.getTime();
		SessionService.getInstance().saveOutTime(key, timeStamp, calendar.getTime());
		logger.info("保存发送短信时间的时间戳存到 memcached,key:"+key+",value:"+timeStamp);
	}
	
	/**
	 * 生成发短信客户端ip每天限制次数存到memcached的key
	 *@author laizs
	 *@param ipAddr
	 *@return
	 */
	private String genIpAddrLimitKey2mem(String ipAddr){
		String dateStr=DATE_SDF.format(new Date());
		String key="sms_send_ip_limit_"+ipAddr+"_"+dateStr;
		return key;
	}
	/**
	 * 生成发短信同一号码每天限制次数存到memcached的key
	 *@author laizs
	 *@param phone
	 *@return
	 */
	private String genPhoneLimitKey2mem(String phone){
		String dateStr=DATE_SDF.format(new Date());
		String key="sms_send_phone_limit_"+phone+"_"+dateStr;
		return key;
	}
	/**
	 * 同一号码发送短信时间时间戳到memcached的key
	 *@author laizs
	 *@param phone
	 *@return
	 */
	private String genPhoneLastSendTimeKey2mem(String phone){
		String key="sms_send_lasttime_"+phone;
		return key;
	}

}
