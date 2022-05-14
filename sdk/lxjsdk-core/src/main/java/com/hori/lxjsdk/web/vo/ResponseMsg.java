package com.hori.lxjsdk.web.vo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.hori.lxjsdk.enums.ResponseCode;

public class ResponseMsg {
	
	/**
	 * 结果码
	 */
	private String result;
	
	
	/**
	 * 处理失败时，返回失败原因，处理成功时此字段为空
	 */
	private String reason;

	/**
	 * 返回的数据
	 */
	private Object data;
	
	
	private Map<String,Object> params=null;
	
	
	private ResponseMsg() {
	
	}
	
	private ResponseMsg(String result, String reason,Object data) {
		super();
		this.result = result;
		this.reason = reason;
		this.data=data;
	}
	
	private ResponseMsg(ResponseCode respCode,Object data) {
		super();
		this.result = respCode.getCode();
		this.reason = respCode.getMsg();
		this.data=data;
	}

	public String getResult() {
		return result;
	}

	public String getReason() {
		return reason;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String,Object> getParams() {
		return params;
	}

	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	
	public void setResponseCode(ResponseCode respCode){
		this.result = respCode.getCode();
		this.reason = respCode.getMsg();
	}

	
	public String toJson(){
		if(params==null){
			
			return JSON.toJSONString(this);
		}
		params.put("result", result);
		params.put("reason", reason);
		
		if(data!=null){
			params.put("data", data);
		}
		
		return JSON.toJSONString(params);
	}
	
	
	
	@Override
	public String toString() {
		return toJson();
	}

	public ResponseMsg put(String key,Object value){
		
		if(params==null){
			params=new HashMap<String,Object>();
		}
		params.put(key, value);
		
		return this;
	}
	
	public static ResponseMsg buildEmptyResp(){
		return new ResponseMsg("","",null);
	}
	
	public static ResponseMsg buildSuccessResp(){
		return new ResponseMsg(ResponseCode.RSP_SUCCESS,null);
	}
	
	public static ResponseMsg buildSuccessResp(Object data){
		return new ResponseMsg(ResponseCode.RSP_SUCCESS,data);
	}
	
	public static ResponseMsg buildErrorResp(ResponseCode respCode){
		return new ResponseMsg(respCode,null);
	}
	
	public static ResponseMsg buildErrorResp(ResponseCode respCode,String errorMsg){
		ResponseMsg responseMsg = new ResponseMsg(respCode,null);
		if(errorMsg!=null&&errorMsg.trim().length()>0){
			responseMsg.setReason(errorMsg);
		}
		return responseMsg;
	}
	
	
	public static void main(String[] args) {
		
		ResponseMsg resp = ResponseMsg.buildSuccessResp();
		
		resp.put("mobile", "18825195401");
		
		System.out.println(resp.toJson());
	}

}
