package com.hori.lxjsdk.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.hori.lxjsdk.util.MessageSendTestUtil;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;

import net.sf.json.JSONObject;


public class ApiTest {
	
	// token
	String token = "test";
	// 时间戳
	String time_stamp = null;
	
	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMDDHHmmss");

	@Before
	public void before() {
		time_stamp = sf.format(new Date());
	}
	
	@Test
	public  void testSyncDataAuthentication(){
		//请求路径
		String servletURL = new String("http://sdk.hori-gz.com:8090/lxjsdkApi/syncDataAuthentication");
		
		MessageSendTestUtil messageInteractive = new MessageSendTestUtil(servletURL);
		
		Map<String,Object> header=new HashMap<String,Object>();
		header.put("token", "00000000000000000000000000000000");
		header.put("time_stamp", time_stamp);
		
		Map<String,Object> body=new HashMap<String,Object>();
		body.put("account", "dywidr9z");
		body.put("password", "90283434");

		
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("header", header);
		data.put("body", body);
		
		
		String content=JSONObject.fromObject(data).toString();
		System.out.println(content);
		String backResult = messageInteractive.sendMessageReq(content);
		MessageSendTestUtil.printInfo("服务端响应的数据：" + backResult);
	}
	
	
	@Test
	public  void testSyncSaveData(){
	
		long start = System.currentTimeMillis();
		//请求路径
		String servletURL = new String("http://sdk.hori-gz.com:8090/lxjsdkApi/syncData");

		MessageSendTestUtil messageInteractive = new MessageSendTestUtil(servletURL);

		Map<String,Object> header=new HashMap<String,Object>();
		header.put("token", "151176509139b7b0f826400d46ee8928");
		header.put("time_stamp", time_stamp);

		Map<String,Object> body=new HashMap<String,Object>();
		body.put("areaCode", "4400200284");
	
		//"操作类型：1、新增  2、修改  3、删除",
		body.put("operateType", "1");

		List<PlatFormUserVo> users=new ArrayList<PlatFormUserVo>(5000);

//		for (int i = 1; i < 10; i++) {
//			
//			PlatFormUserVo user=new PlatFormUserVo();
//			user.setUserId(i+"");
//			user.setUserName(i+"");
//			user.setAreaName("林SDK");
//			user.setRegionName("2区");
//			user.setBuildingName("2栋");
//			user.setUnitName("B单元");
//			user.setFloor("01");
//			user.setRoom("0"+i);
//			user.setMobile("1882519540"+i);
//			//入住日期（选填），格式"yyyy-MM-dd"
//			user.setLiveDate("2017-10-01");
//			//入住状态（选填），1:入住，0：注销
//			user.setLiveStatus("1");
//			
//			//民族（选填），系统默认"汉族"
//			user.setNation("汉族");
//			
//			users.add(user);
//		}
		
		PlatFormUserVo user=new PlatFormUserVo();
		user.setUserId("sdfdsfdsfdsds");
		user.setUserName("sdfdsfdsfdsds");
		user.setAreaName("林SDK");
		user.setRegionName("2区");
		user.setBuildingName("2栋");
		user.setUnitName("B单元");
		user.setFloor("02");
		user.setRoom("02");

		//入住日期（选填），格式"yyyy-MM-dd"
		user.setLiveDate("2017-10-01");
		//入住状态（选填），1:入住，0：注销
		user.setLiveStatus("1");
		
		//民族（选填），系统默认"汉族"
		user.setNation("汉族");
		
		users.add(user);
		
		body.put("list", users);


		Map<String,Object> data=new HashMap<String,Object>();
		data.put("header", header);
		data.put("body", body);


		String content=JSONObject.fromObject(data).toString();
		System.out.println(content);
		String backResult = messageInteractive.sendMessageReq(content);
		MessageSendTestUtil.printInfo("服务端响应的数据：" + backResult);
				
		long end = System.currentTimeMillis();

		System.out.println(((end-start)*1.0/1000));
	}
	
	@Test
	public  void testSyncUpdateData(){
	
		long start = System.currentTimeMillis();
		//请求路径
		String servletURL = new String("http://sdk.hori-gz.com:8090/lxjsdkApi/syncData");

		MessageSendTestUtil messageInteractive = new MessageSendTestUtil(servletURL);

		Map<String,Object> header=new HashMap<String,Object>();
		header.put("token", "151080023525ab3ac758ca0f43b78f51");
		header.put("time_stamp", time_stamp);

		Map<String,Object> body=new HashMap<String,Object>();
		body.put("areaCode", "4400100292");
	
		//"操作类型：1、新增  2、修改  3、删除",
		body.put("operateType", "2");

		List<PlatFormUserVo> users=new ArrayList<PlatFormUserVo>(5000);

		PlatFormUserVo user=new PlatFormUserVo();
		user.setUserId("1");
		user.setUserName("scs");
		user.setAreaName("sdk小区");
		user.setRegionName("B区");
		user.setBuildingName("1栋");
		user.setUnitName("1单元");
		user.setFloor("01");
		user.setRoom("01");
		
		user.setMobile("18825195400");
		
		//入住日期（选填），格式"yyyy-MM-dd"
		user.setLiveDate("2017-11-01");
		//入住状态（选填），1:入住，0：注销
		user.setLiveStatus("1");
		//民族（选填），系统默认"汉族"
		user.setNation("汉族");
		
		users.add(user);
		
//		for (int i = 1; i < 10; i++) {
//			
//			PlatFormUserVo user=new PlatFormUserVo();
//			user.setUserId(i+"");
//			user.setUserName(i+"");
//			user.setAreaName("sdk小区");
//			user.setRegionName("A区");
//			user.setBuildingName("1栋");
//			user.setUnitName("A单元");
//			user.setFloor("01");
//			user.setRoom("0"+i);
//			users.add(user);
//		}
		
		body.put("list", users);


		Map<String,Object> data=new HashMap<String,Object>();
		data.put("header", header);
		data.put("body", body);


		String content=JSONObject.fromObject(data).toString();
		System.out.println(content);
		String backResult = messageInteractive.sendMessageReq(content);
		MessageSendTestUtil.printInfo("服务端响应的数据：" + backResult);
				
		long end = System.currentTimeMillis();

		System.out.println(((end-start)*1.0/1000));
	}
	
	@Test
	public  void testSyncDelData(){
	
		long start = System.currentTimeMillis();
		//请求路径
		String servletURL = new String("http://sdk.hori-gz.com:8090/lxjsdkApi/syncData");

		MessageSendTestUtil messageInteractive = new MessageSendTestUtil(servletURL);

		Map<String,Object> header=new HashMap<String,Object>();
		header.put("token", "151080023525ab3ac758ca0f43b78f51");
		header.put("time_stamp", time_stamp);

		Map<String,Object> body=new HashMap<String,Object>();
		body.put("areaCode", "4400100292");
	
		//"操作类型：1、新增  2、修改  3、删除",
		body.put("operateType", "3");

		List<PlatFormUserVo> users=new ArrayList<PlatFormUserVo>(5000);
		
		PlatFormUserVo user=new PlatFormUserVo();
		user.setUserId("1");
		user.setUserName("scs");
		user.setAreaName("sdk小区");
		user.setRegionName("B区");
		user.setBuildingName("1栋");
		user.setUnitName("1单元");
		user.setFloor("01");
		user.setRoom("01");
		
		users.add(user);
		
//		for (int i = 1; i < 5000; i++) {
//			
//			PlatFormUserVo user=new PlatFormUserVo();
//			user.setUserId(i+"");
//			user.setUserName(i+"");
//			user.setAreaName("sdk小区");
//			user.setRegionName("A区");
//			user.setBuildingName("1栋");
//			user.setUnitName("A单元");
//			user.setFloor("01");
//			user.setRoom("0"+i);
//			users.add(user);
//		}
		
		body.put("list", users);


		Map<String,Object> data=new HashMap<String,Object>();
		data.put("header", header);
		data.put("body", body);


		String content=JSONObject.fromObject(data).toString();
		System.out.println(content);
		String backResult = messageInteractive.sendMessageReq(content);
		MessageSendTestUtil.printInfo("服务端响应的数据：" + backResult);
				
		long end = System.currentTimeMillis();

		System.out.println(((end-start)*1.0/1000));
	}
	//151003483183f43e10ea6b714b1ea907
	
	public static void main(String[] args) {
		Long.parseLong("1645065097319022592");
	}
}

