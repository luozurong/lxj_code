package com.hori.lxjsdk.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 基础ACTION,其他ACTION继承此ACTION来获得writeJson和ActionSupport的功能
 * 
 * @author 
 * 
 */
@ParentPackage("defaultPackage")
@Namespace("/")
public class BaseAction extends ActionSupport {

	private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);
	
	
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 */
	public void writeJson(Object object) {
		try {
			// SerializeConfig serializeConfig = new SerializeConfig();
			// serializeConfig.setAsmEnable(false);
			// String json = JSON.toJSONString(object);
			// String json = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			// String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将请求参数封装到javabean
	 * @param clazz class
	 * @return
	 */
	public <T> T requestParamsToBean(Class<T> clazz){
		
		try {
			
			HttpServletRequest request = getRequest();
			Enumeration<String> parameterNames = request.getParameterNames();
			
			Map<String,String> map=new HashMap<String,String>();
			
			while (parameterNames.hasMoreElements()) {
				String param = parameterNames.nextElement();
				map.put(param, request.getParameter(param));
			}
		
			if(map.size()>0){
				return JSON.parseObject(JSON.toJSONString(map), clazz);
			}
			
			return clazz.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected int getPageNumber() {
        String strPage = getRequest().getParameter("page");
        return StringUtils.isBlank(strPage) ||"NaN".equals(strPage) ? 1 : Integer.parseInt(strPage);
	}
}
