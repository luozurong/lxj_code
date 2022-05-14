package com.hori.lxjsdk.util; 

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**   
 * 类名称：ServletUtil   
 * 类描述：servlet辅助工具
 */
public class ServletUtil {
	private static final Log log = LogFactory.getLog(ServletUtil.class);
   
   /**
	* 获取客户端的请求消息内容
	*/
	public static String getRequestContent(HttpServletRequest req) {

		String message = "";
		InputStreamReader isr = null;
		
		try {
			req.setCharacterEncoding("GBK");
			message = processInputStream(req.getInputStream());
			message = new String(message.getBytes(),"GBK");
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return message;
	}
	
	public static String processInputStream(InputStream input) throws Exception{
		byte[] response = null;
		if(input != null){
			ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			int len = 0;
			while((len = input.read(temp)) > 0){
				byteArrayOutput.write(temp, 0, len);
			}
			byteArrayOutput.close();
			input = null;
			response = byteArrayOutput.toByteArray();
		}
		return new String(response,"UTF-8").trim();
	}
	
	/**
	 * 发送消息对象
	 * @param rsp
	 * @param msgOut
	 * 待发送的消息对象
	 */
	public static void send(HttpServletResponse rsp, String msgOut) {

		rsp.setCharacterEncoding("UTF-8");
		rsp.setContentType("text/xml;charset=UTF-8");
		OutputStreamWriter ow = null;
		ServletOutputStream servletOut = null;
		try {
			
			servletOut = rsp.getOutputStream();
			ow = new OutputStreamWriter(servletOut, "UTF-8");

			ow.write(msgOut);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ow != null) {
					ow.close();
				}
				if (servletOut != null) {
					servletOut.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
				log.info(e);
			}
		}
	}
	
	/**
	 * @affect 在一组cookies里面，通过name取value
	 * @param cookies
	 * @param cookieName
	 * @return String
	 * @exception 无
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName) {
		if (cookies == null)
			return null;
		if (cookieName == null)
			return null;
		String cookieValue = "";
		for (int i = 0; i < cookies.length; i++) {
			if (cookieName.equals(cookies[i].getName())) {
				cookieValue = cookies[i].getValue();
			}
		}
		return cookieValue;

	}
	
	/**
	 * 解析请求数据
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 */
	 public static String praseRequst(HttpServletRequest req) throws IOException{
		 BufferedReader rd = new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
		 StringBuffer sb = new StringBuffer();
		 int ch;
		 while ((ch = rd.read()) > -1){
		 sb.append((char) ch);
		 }
		 return sb.toString();
	 }
	 
	 /**
	  * 发送响应数据
	  * @param resp
	  * @param jsonString
	  * @return
	  * @throws IOException
	  */
	public static String sendResponse(HttpServletResponse resp,String jsonString) throws IOException{
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter printWriter = resp.getWriter();
		printWriter.write(jsonString);
		printWriter.flush();
		printWriter.close();
		return "success";
	}
	
}
 