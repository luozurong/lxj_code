package com.hori.lxjsdk.oauth.handler;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jlit.oauth.AbstractOauth2Handler;
import com.jlit.oauth.AccessTokenModel;
import com.jlit.oauth.OauthDataUtil;
import com.jlit.oauth.bean.UserVo;
import com.jlit.uums.webservice.bean.Platform;
import com.jlit.uums.webservice.bean.UserMenus;

/**
 * oauth2认证的处理实现类
 * @author laizs
 * @time 2017年8月6日下午8:31:12
 *
 */
@Service("lxjsdkOauth2Handler")
public class LxjsdkOauth2Handler extends AbstractOauth2Handler {
	private final static Logger logger=LoggerFactory.getLogger(LxjsdkOauth2Handler.class);
	/**
	 * 发起oauth认证的请求路径
	 */
	@Value(value="${to_authrize_uri}")
	private String toAuthorizeUri;
	/**
	 * oauth授权认证后回调地址
	 */
	@Value(value="${callback_uri}")
	private String callbackUri;
	/**
	 * oauth2服务器授权认证请求地址
	 */
	@Value(value="${oauth_authorize_uri}")
	private String authorizeUri;
	/**
	 * oauth2服务器换取access_token的地址
	 */
	@Value(value="${oauth_access_token_uri}")
	private String accessTokenUri;
	/**
	 * oauth2应用的client_id
	 */
	@Value(value="${oauth_client_id}")
	private String clientId;
	/**
	 * oauth2应用的client_id应用的密钥
	 */
	@Value(value="${oauth_client_secret}")
	private String clientSecret;
	/**
	 * oauth2查询用户基本信息的地址
	 */
	@Value(value="${oauth_me_uri}")
	private String userMeUri;
	/**
	 * oauth2查询用户权限菜单信息的地址
	 */
	@Value(value="${oauth_user_menus_uri}")
	private String userMemusUri;
	/**
	 * oauth2查询用户有权限访问的系统的uri
	 */
	@Value(value="${oauth_user_platforms_uri}")
	private String userPlatformsUri;
	/**
	 * 登录oauth2系统的uri,需要重定向或访问
	 */
	@Value(value="${oauth_logout_uri}")
	private String logoutUri;
	/**
	 * oauth服务器认证时是否强制登录，0：不强制，1：强制
	 */
	@Value(value="1")
	private String forcelogin;
	@Override
	public void beforeAuthrizedHandler(HttpServletRequest request,
			HttpServletResponse response) {
		 String fromURL = request.getHeader("Referer");   
		//logger.warn("来源url:"+fromURL);
		//清除会话信息
//		request.getSession().invalidate();
		
	}
	@Override
	public void afterAuthrizeHandler(AccessTokenModel accessTokenModel,
			HttpServletRequest request, HttpServletResponse response) {
		//获取用户信息
		try {
			String userJsonStr=getUserInfo(accessTokenModel.getAccessToken());
			
			//用户信息
			UserVo userVo=OauthDataUtil.Json2UserVo(userJsonStr);
			//保存用户session信息
			request.getSession().setAttribute("userId", userVo.getId());
			request.getSession().setAttribute("userAccount", userVo.getUserAccount());
			request.getSession().setAttribute("userName", userVo.getUserName());
			request.getSession().setAttribute("userType", userVo.getUserType());
			request.getSession().setAttribute("userVo", userVo);
			//获取用户拥有权限的可访问的平台列表
			String ps=getUserPlatForms(accessTokenModel.getAccessToken());
			logger.info("获取到用户有权限访问的平台："+ps);
			List<Platform> platforms=OauthDataUtil.Json2PlatForms(ps);
			request.getSession().setAttribute("platforms", platforms);
			
			String menus=getUserMemus(accessTokenModel.getAccessToken());
			
			UserMenus userMenus=OauthDataUtil.Json2UserMenus(menus);
			//如果获取到的菜单权限不为空，则跳转到对应的项目中去
			if(userMenus.getRootMenus().size()>0){
				//保存用户session信息
				request.getSession().setAttribute("userId", userVo.getId());
				request.getSession().setAttribute("userAccount", userVo.getUserAccount());
				request.getSession().setAttribute("userName", userVo.getUserName());
				request.getSession().setAttribute("userType", userVo.getUserType());
				request.getSession().setAttribute("userTypeName", userVo.getUserTypeName());
				request.getSession().setAttribute("userVo", userVo);
				request.getSession().setAttribute("authLevel", userVo.getAuthLevel());
				//保存菜单session信息
				request.getSession().setAttribute("rootMenus", userMenus.getRootMenus());
				request.getSession().setAttribute("subMenus", userMenus.getSubMenus());
				
				//保存权限session信息
				request.getSession().setAttribute("userPermissions", userMenus.getUserPermissions());
				
				//重定向
				response.sendRedirect("main.html");
			}else{
				//重定向到无权限提示页面
				response.sendRedirect("noPurview.html");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户信息异常，e:"+e.getMessage());
		} 
				
	}
	public String getToAuthorizeUri() {
		return toAuthorizeUri;
	}
	public void setToAuthorizeUri(String toAuthorizeUri) {
		this.toAuthorizeUri = toAuthorizeUri;
	}
	public String getCallbackUri() {
		return callbackUri;
	}
	public void setCallbackUri(String callbackUri) {
		this.callbackUri = callbackUri;
	}
	public String getAuthorizeUri() {
		return authorizeUri;
	}
	public void setAuthorizeUri(String authorizeUri) {
		this.authorizeUri = authorizeUri;
	}
	public String getAccessTokenUri() {
		return accessTokenUri;
	}
	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getUserMeUri() {
		return userMeUri;
	}
	public void setUserMeUri(String userMeUri) {
		this.userMeUri = userMeUri;
	}
	public String getUserMemusUri() {
		return userMemusUri;
	}
	public void setUserMemusUri(String userMemusUri) {
		this.userMemusUri = userMemusUri;
	}
	public String getUserPlatformsUri() {
		return userPlatformsUri;
	}
	public void setUserPlatformsUri(String userPlatformsUri) {
		this.userPlatformsUri = userPlatformsUri;
	}
	public String getLogoutUri() {
		return logoutUri;
	}
	public void setLogoutUri(String logoutUri) {
		this.logoutUri = logoutUri;
	}
	public String getForcelogin() {
		return forcelogin;
	}
	public void setForcelogin(String forcelogin) {
		this.forcelogin = forcelogin;
	}
	
	
	
	
	
	

}
