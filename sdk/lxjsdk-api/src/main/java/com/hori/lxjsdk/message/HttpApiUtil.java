package com.hori.lxjsdk.message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * http接口工具类
 * @author laizs
 * @time 2017年8月7日下午5:39:21
 *
 */
public class HttpApiUtil {
	private final static Logger  log =LoggerFactory.getLogger(HttpApiUtil.class);
	/**
	 * 解析请求数据
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

}
