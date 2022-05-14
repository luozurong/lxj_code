package com.hori.lxjsdk.utils;

import java.io.IOException;
import java.util.Properties;

public class StaticValue {

	public static String LOGINPASSERRERTIMES = "|LoginPassErrer|msgErrer";
	/**
	 * 资源文件名
	 */
	private static final String GLOBAL_PROPERTIES = "global.properties";
	
	/**
	 * 持久属性集
	 */
	private static Properties properties;
	
	
	public static String MSGERRENUM;
	
	public static String CODEERRENUM;
	
	public static String FMS_SERVER_URL;
	
	public static String TERMINAL_PASSWORD;
	
	public static String upload_server_address;
	
		
	static {
		try {
			properties = new Properties();
			
			properties.load(StaticValue.class.getClassLoader().getResourceAsStream(GLOBAL_PROPERTIES));
			
			
			FMS_SERVER_URL = properties.getProperty("fms_server_address");
			
			upload_server_address = properties.getProperty("upload_server_address");
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print("获取全局变量失败;");
		}

	}

}