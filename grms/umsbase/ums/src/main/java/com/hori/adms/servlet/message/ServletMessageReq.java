package com.hori.adms.servlet.message;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * api  servlet接口请求消息bean
 * @author laizs
 * @time 2014-5-14 上午10:06:13
 * @file ServletMessageReq.java
 */
public class ServletMessageReq {
	private JSONObject header;
	private JSONObject body ;
	private String token;
	private String time_stamp;
	public ServletMessageReq(){
		
	}
	/**
	 * 构造函数
	 * @param request
	 * @throws IOException
	 */
	public ServletMessageReq(String jsonStr) throws IOException{
		JSONObject json=JSONObject.fromObject(jsonStr);
		init(json);
		
	}
	
	/**
	 * 根据json格式对象初始化属性
	 * @param json
	 */
	private  void init(JSONObject json){
		this.header=json.getJSONObject("header");
		this.body=json.getJSONObject("body");
		if(null!=this.header){
			this.token=this.header.getString("token");
			this.time_stamp=this.header.getString("time_stamp");
		}
		
	}
	
	public String getBodyParamAsString(String paramName){
		try {
			return body.getString(paramName);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public Integer getBodyParamAsInt(String paramName){
		try {
			return body.getInt(paramName);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public JSONArray getBodyParamAsJsonArray(String paramName){
		try {
			return body.getJSONArray(paramName);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public JSONObject getHeader() {
		return header;
	}
	public void setHeader(JSONObject header) {
		this.header = header;
	}
	public JSONObject getBody() {
		return body;
	}
	public void setBody(JSONObject body) {
		this.body = body;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
	
	
	public static void main(String[] args) {
		String s="{\"header\":{\"token\":\"1235812asdfws\",\"time_stamp\":\"156132562\"},\"body\":{}}";
		s="{\"body\":{\"pageSize\":\"10\",\"pageNum\":\"1\"},\"header\":{\"token\":\"test\",\"time_stamp\":\"13454354\"}}";
		ServletMessageReq smq=new ServletMessageReq();
		smq.init(JSONObject.fromObject(s));
		System.out.println(smq.getHeader());
		System.out.println(smq.getBody());
		System.out.println(smq.getToken());
		System.out.println(smq.getTime_stamp());
	}
	
	
}
