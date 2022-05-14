package com.hori.lxjsdk.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hori.lxjsdk.enums.ResponseCode;
import com.hori.lxjsdk.message.HttpApiMessageReq;
import com.hori.lxjsdk.message.HttpApiUtil;
import com.hori.lxjsdk.message.ServletMessageCommon;
import com.hori.lxjsdk.web.vo.ResponseMsg;

import net.sf.json.JSONObject;

public class BaseApiRequestHelper {
	
	private static final Log log = LogFactory.getLog(BaseApiRequestHelper.class);

	public static interface RequetstHandler{
		public ResponseMsg processHandler(HttpServletRequest request, HttpServletResponse response,JSONObject paramsHeader,JSONObject paramsBody);
	}
	/**
	 * 处理无需token验证的请求
	 */
	public void processNoValidRequest(HttpServletRequest request, HttpServletResponse response,RequetstHandler requetstHandler){
		
		//html5回调参数
		String callbackName =  null;

		ResponseMsg respMsg = ResponseMsg.buildEmptyResp();
		
		String requestContent = "";
		
		try {
			long start = System.currentTimeMillis();

			requestContent = HttpApiUtil.praseRequst(request);
			
			if(StringUtils.isEmpty(requestContent)){
				callbackName =  request.getParameter("jsoncallback");
				requestContent = request.getParameter("str");
			}
			HttpApiMessageReq messageReq = new HttpApiMessageReq(requestContent);
			JSONObject body = messageReq.getBody();
			
			respMsg = requetstHandler.processHandler(request, response,messageReq.getHeader(),body);

			long end = System.currentTimeMillis();
			
			log.info("接口地址:"+request.getRequestURI()+",请求参数：header:"+messageReq.getHeader()+",body:"+messageReq.getBody()+
					"响应时间："+((end-start)*1.0/1000)+"秒");	

		}catch(NullPointerException e){
			e.printStackTrace();
			
			respMsg.setResponseCode(ResponseCode.MESSAGE_FORMAT_ERROR);
			
		}catch (Exception e) {
			e.printStackTrace();
			
			respMsg.setResponseCode(ResponseCode.SERVICE_RESPONSE_ERROR);
			
		}finally{
			log.info("响应："+respMsg.toJson());
			try {
				//html5回调
				if(null!=callbackName&&!"".equals(callbackName)){
					String renderStr = callbackName+"("+respMsg.toJson()+")"; 			
					sendResponse(response,renderStr);
				}else{
					sendResponse(response, respMsg.toJson());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	/**
	 * 处理需token验证的请求
	 */
	public void processValidRequest(HttpServletRequest request, HttpServletResponse response,RequetstHandler requetstHandler){
		
		//html5回调参数
		String callbackName =  request.getParameter("jsoncallback");
						
		String str = request.getParameter("str");
				
		String token="";
	
		ResponseMsg respMsg = ResponseMsg.buildEmptyResp();
		
		try {
			
			long start = System.currentTimeMillis();

			String requestContent = "";
			
			//客户端请求JSON串
			if(null==str){
				requestContent = HttpApiUtil.praseRequst(request);
			}else{
				requestContent  = str;
			}
			
			HttpApiMessageReq messageReq = new HttpApiMessageReq(requestContent);
			
			token = messageReq.getToken();

			String memcachedToken = ServletMessageCommon.getAccountToken(token);
			
			
			if(memcachedToken==null){
				
				respMsg.setResponseCode(ResponseCode.TOKEN_OVER_TIME_ERROR);
				
			}else if("".equals(memcachedToken)){
				
				respMsg.setResponseCode(ResponseCode.TOKEN_VALIDATE_ERROR);
				
			}else{	
				JSONObject body = messageReq.getBody();
				
				respMsg =  requetstHandler.processHandler( request, response,messageReq.getHeader(),body);
			}
			
			long end = System.currentTimeMillis();
			
//			log.info("接口地址:"+request.getRequestURI()+",请求参数：header:"+messageReq.getHeader()+",body:"+messageReq.getBody()+
//					"响应时间："+((end-start)*1.0/1000)+"秒");	
			
		}catch(NullPointerException e){
			e.printStackTrace();
			
			respMsg.setResponseCode(ResponseCode.MESSAGE_FORMAT_ERROR);
			
		}catch (Exception e) {
			e.printStackTrace();
			
			respMsg.setResponseCode(ResponseCode.SERVICE_RESPONSE_ERROR);
			
		}finally{
			log.info("响应："+respMsg.toJson());
			try {
				//html5回调
				if(null!=callbackName&&!"".equals(callbackName)){
					String renderStr = callbackName+"("+respMsg.toJson()+")"; 			
					sendResponse(response,renderStr);
				}else{
					sendResponse(response, respMsg.toJson());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 将请求参数封装到javabean
	 * @param clazz class
	 * @return
	 */
	public <T> T requestParamsToBean(HttpServletRequest request,Class<T> clazz){
		
		try {
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
	
	public static <T> T jsonObjectToBean(JSONObject jsonObj,Class<T> clazz){
		return JSON.parseObject(jsonObj.toString(),clazz);
	} 
	
	public static <T> T jsonStrToBean(String json,Class<T> clazz){
		return JSON.parseObject(json,clazz);
	} 
	
	
	private String sendResponse(HttpServletResponse resp,String jsonString) throws IOException{
		//log.info("发送给客户端接口响应"+jsonString);
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter printWriter = resp.getWriter();
		printWriter.write(jsonString);
		printWriter.flush();
		printWriter.close();
		return "success";
	}
}
