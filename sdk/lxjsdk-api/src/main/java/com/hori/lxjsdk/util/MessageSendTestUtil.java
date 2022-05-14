package com.hori.lxjsdk.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
/**
 * 消息发送测试客户端
 * @author wiliam
 *
 */
public class MessageSendTestUtil {
	public static SimpleDateFormat sf=new SimpleDateFormat("yyyyMMDDHHmmss");
	
	/**
	 * 请求服务器IP地址
	 */
	private static final String requestIP = "127.0.0.1";
	
	/**
	 * 请求端口
	 */
	private static final String requestPort = "8090";
	
	/**
	 * 请求全路径
	 */
	private String requestURL ;
	
	/**
	 * Post方法
	 */
	private PostMethod postMethod;
	
	/**
	 * httpclient客户端
	 */
	private HttpClient httpClient = new HttpClient();
	
	/**
	 * 初始化部分数据
	 * @param url
	 */
	public MessageSendTestUtil(String url){
		requestURL = url;
	}
	
	/**
	 * 终端向服务器发送请求,携带XML数据内容
	 * @param message
	 * @return
	 */
	public String sendMessageReq(String message){
		postMethod = new PostMethod(requestURL);
		
		//将消息内容转成字节码传输
		byte[] messageToByte = null;
		if(message == null){
			messageToByte = new byte[0];
		}else{
			messageToByte = message.getBytes();
		}
		
		//字节请求实体
		RequestEntity entity = new ByteArrayRequestEntity(messageToByte);
		postMethod.setRequestEntity(entity);
		
		try {
			//返回响应状态码
			int backCode =  httpClient.executeMethod(postMethod);
			System.out.println("---->"+backCode);
			//服务端响应的结果数据
			String backResult = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();
			return backResult;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 输出内容
	 */
	public static void printInfo(String info){
		System.out.println("********\n\t" + info + "\n********");
	}
	
	
	/**
	 * 服务端响应客户端最后的消息内容
	 * @param result
	 * @return
	 */
	public static void lastResultConvert(String result){
		int leng=0;
		leng=result.length();
		if(leng==0){
			printInfo("客户端收到服务器响应:空消息");
		}
		else{
			printInfo("客户端收到服务器响应:"+result);
		}
	}
}
