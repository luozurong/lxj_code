package com.hori.grms.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hori.grms.httpclient.HttpResult;
import com.hori.grms.service.AreaDataService;
import com.hori.grms.service.HttpClientApiService;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.AreaDataReqVo;
import com.hori.grms.vo.AreaDataRspVo;
import com.hori.grms.vo.AreaDataRspVo.AreaData;
/**
 * 小区数据服务
 * @author laizs
 * @time 2018年8月15日下午7:49:29
 */
@Service("areaDataService")
public class AreaDataServiceImpl implements AreaDataService {
	private static final Logger logger=Logger.getLogger(AreaDataService.class);
	@Autowired
	private HttpClientApiService httpClientApiService;
	/**
	 * 大数据平台服务地址
	 */
	@Value("${big_data_server_address}")
	private String bigDataServerAddress;
	private String countAreaDataByGrms_url="/servlet/countAreaDataByGrms";
	@SuppressWarnings("unchecked")
	@Override
	public AreaDataRspVo searchAreaDataList(AreaDataReqVo reqVo) {
		AreaDataRspVo rsp=new AreaDataRspVo();
		rsp.setResult("005");
		try {
			Map header=new HashMap();
			header.put("time_stamp", System.currentTimeMillis()+"");
			header.put("token", "");
			Map reqMap=new HashMap();
			reqMap.put("header", header);
			reqMap.put("body", reqVo);
			String req=JSON.toJSONString(reqMap);
			logger.info("查询小区数据请求数据："+req);
			String url=this.bigDataServerAddress+countAreaDataByGrms_url;
			HttpResult rs=this.httpClientApiService.doPostJson(url, req);
			logger.info("查询小区数据请求响应："+rs);
			if(rs.getStatus()==200){//请求成功
				 rsp=JSONObject.parseObject(rs.getData(), AreaDataRspVo.class);
				 
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rsp;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, AreaData> getAreaDataByOrganizationSeqs(Set<String> organizationSeqs) {
		Map<String, AreaData> resultMap=new TreeMap<String, AreaData>();
		try {
			Map header=new HashMap();
			header.put("time_stamp", System.currentTimeMillis()+"");
			header.put("token", "");
			Map reqMap=new HashMap();
			reqMap.put("header", header);
			AreaDataReqVo reqVo=new AreaDataReqVo(1, Integer.MAX_VALUE);
			reqVo.setList(organizationSeqs);//机构编号数组
			reqMap.put("body", reqVo);
			reqMap.put(UUIDGeneratorUtil.generateUUID(), UUIDGeneratorUtil.generateUUID());
			String req=JSON.toJSONString(reqMap);
			logger.info("根据小区机构编号列表查询小区数据请求："+req);
			String url=this.bigDataServerAddress+countAreaDataByGrms_url;
			HttpResult rs=this.httpClientApiService.doPostJson(url, req);
			logger.info("根据小区机构编号列表查询小区数据响应："+rs);
			if(rs.getStatus()==200){//请求成功
				AreaDataRspVo rsp=new AreaDataRspVo();
				rsp.setResult("005");
				rsp=JSONObject.parseObject(rs.getData(), AreaDataRspVo.class);
				//请求结果正常
				if(StringUtils.isNotBlank(rsp.getResult())&&"0".equals(rsp.getResult())){
					List<AreaData> dataList=rsp.getList();
					for(AreaData ad:dataList){
						resultMap.put(ad.getOrganizationSeq(), ad);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}
