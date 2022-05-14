package com.hori.lxjsdk.message;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlit.memcached.session.SessionService;

/**
 * servlet处理公共方法
 * @author viliam
 *
 */
public class ServletMessageCommon {
	private final static Logger log=LoggerFactory.getLogger(ServletMessageCommon.class);
	/**
	 * 游客模式的token
	 */
	public final static String visitor_token_prefix="_";
	/**
	 * 根据令牌取账号
	 * @param token
	 * @return
	 */
	public static String getAccountToken(String token){
		SessionService ses = SessionService.getInstance();
		String account = (String) ses.get(token, true);
		log.info("token："+token+"<|>acount:"+account);
		return account;
	}
	/**
	 * 删除用户登录的token
	 *@author laizs
	 *@param token
	 */
	public static void delAccountToken(String token){
		SessionService ses = SessionService.getInstance();
		String account = (String) ses.get(token, true);
		ses.remove(token);
		log.info("删除用户登录的token："+token+"<|>acount:"+account);
	}
	/**
	 * 判断token是否正确
	 * @param token
	 * @return -1 token异常  0 token正确  1游客模式
	 */
	public static TokenInfo validateToken(String token){
		if(token.startsWith(visitor_token_prefix)){
			return new TokenInfo(token, null, 1);
		}
		String account=getAccountToken(token);
		if(StringUtils.isBlank(account)){
			return new TokenInfo(token, null, -1);
		}else{
			return new TokenInfo(token, account, 0);
		}
		
				
	}
	

}
