package com.hori.lxjsdk.interceptor;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.hori.lxjsdk.utils.AjaxJsonAndXmlUtil;
import com.jlit.oauth.AbstractOauth2Handler;
import com.jlit.oauth.OauthDataUtil;
import com.jlit.oauth.bean.UserVo;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 用户会话和权限拦截器
 * @author laizs
 * @time 2017年8月6日下午8:28:03
 *
 */
@Component("userRoleAuthorizationInterceptor")
public class UserRoleAuthorizationInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 5067790608840427509L;
	/**
	 * log4j日志
	 */
	private static final Log logger = LogFactory.getLog(UserRoleAuthorizationInterceptor.class);
	/**
	 * oauth2客户端接入时提供授权接入信息及认证授权后处理对象
	 */
	@Resource(name="lxjsdkOauth2Handler")
	private AbstractOauth2Handler oauth2Handler;
	/**
	 * Intercept the action invocation and check to see if the user has the proper role.
	 *
	 * @param invocation the current action invocation
	 * @return the method's return value, or null after setting HttpServletResponse.SC_FORBIDDEN
	 * @throws Exception when setting the error on the response fails
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		boolean isSessionValid=false;//session是否有效
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String method = invocation.getProxy().getMethod();
		String actionName = invocation.getProxy().getActionName();
		String checkStr =actionName +"_"+method;
		logger.info("checkStr: "+checkStr);
		//使用url带过来的accessToken参数，去oauth2服务器换取用户信息
		String accessToken=request.getParameter("accessToken");
		//判断url带过来的accessToken参数和session中记录的参数是否一致，如果不一致，清除session信息
		String oldAccessToken=(String) request.getSession().getAttribute("accessToken");
		if(StringUtils.isNotBlank(accessToken)&&StringUtils.isNotBlank(oldAccessToken)&& !oldAccessToken.equals(accessToken)){
			//清除会话信息
			//request.getSession().invalidate();
			//删除session中存储的用户信息
			request.getSession().removeAttribute("userId");
			request.getSession().removeAttribute("userAccount");
			request.getSession().removeAttribute("userName");
			request.getSession().removeAttribute("userType");
			request.getSession().removeAttribute("userTypeName");
			request.getSession().removeAttribute("userVo");
			request.getSession().removeAttribute("authLevel");
			request.getSession().removeAttribute("accessToken");
			
		}
		String userAccount = (String)request.getSession().getAttribute("userAccount");
		UserVo userVo=(UserVo)request.getSession().getAttribute("userVo");
		if(userAccount == null || null==userVo){//session超时
			try {
				//使用url带过来的accessToken参数，去oauth2服务器换取用户信息
				if(StringUtils.isNotBlank(accessToken)){
					String userJsonStr=this.oauth2Handler.getUserInfo(accessToken);
					//用户信息
					userVo=OauthDataUtil.Json2UserVo(userJsonStr);
					//------------保存session信息---------------------------------------------------
					//保存用户session信息
					request.getSession().setAttribute("userId", userVo.getId());
					request.getSession().setAttribute("userAccount", userVo.getUserAccount());
					request.getSession().setAttribute("userName", userVo.getUserName());
					request.getSession().setAttribute("userType", userVo.getUserType());
					request.getSession().setAttribute("userTypeName", userVo.getUserTypeName());
					request.getSession().setAttribute("userVo", userVo);
					request.getSession().setAttribute("authLevel", userVo.getAuthLevel());
					request.getSession().setAttribute("accessToken", accessToken);//accessToken存到session
					isSessionValid=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("使用accessToken获取用户信息失败，e:"+e.getMessage());
			}
		}else{
			isSessionValid=true;
		}
		if(isSessionValid){
			return invocation.invoke();
		}else{
			//判断是否ajax请求，如果是，则返回特定的错误码，让前端js引导重新登录
			boolean isAjaxRequest=isAjaxRequest(request);
			logger.info("session过期，判断请求是否是ajax请求："+isAjaxRequest);
			if(isAjaxRequest){
				Map rspMap=new HashMap();
				rspMap.put("result", "888888");//返回特定的值，表示session过期
				AjaxJsonAndXmlUtil.writeJson(JSONObject.fromObject(rspMap).toString(),response);
				return null;
			}else{
				
				String nologinPage=request.getContextPath()+"/"+"logout.jsp";
				
				response.sendRedirect(nologinPage);//项目的session过期，使用accessToken换取用户信息又失效，只好返回未登陆页面发起重新授权认证
				
				return null;
			}
			
		}
	}
	/**
	 * 判断是否是ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) { 
	    String header = request.getHeader("X-Requested-With"); 
	    if (header != null && "XMLHttpRequest".equals(header)) 
	        return true; 
	    else 
	        return false; 
	}
	/**
	 * This method currently does nothing.
	 */
	public void destroy() {
	}

	/**
	 * This method currently does nothing.
	 */
	public void init() {


	}
}
