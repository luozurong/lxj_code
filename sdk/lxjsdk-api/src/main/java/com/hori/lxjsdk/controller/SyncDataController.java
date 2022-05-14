package com.hori.lxjsdk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.hori.lxjsdk.enums.ResponseCode;
import com.hori.lxjsdk.message.ServletMessageCommon;
import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.redis.RedisCacheService;
import com.hori.lxjsdk.service.PlatFormJoinAreaService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.service.SyncDataService;
import com.hori.lxjsdk.utils.CollectionUtil;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.hori.lxjsdk.web.vo.SyncDataVo;
import com.jlit.hibernate.UUIDGeneratorUtil;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
@Controller
public class SyncDataController extends BaseApiRequestHelper{
	
	private static final Log log = LogFactory.getLog(SyncDataController.class);
	
	@Resource
	private PlatFormJoinService platFormJoinService;
	@Resource
	private PlatFormJoinAreaService platFormJoinAreaService;
	@Resource
	private SyncDataService syncDataService;
	@Resource
	private RedisCacheService redisCacheService;
	/**
	 * 数据同步接口(需验证token)
	 * 地址 : /lxjsdkApi/syncData
	 * 参数: 
	 *  "areaCode":"小区机构编号",
	 *	"operateType":"操作类型：1、新增  2、修改  3、删除",
	 *	"list":[{
	 *		"userId":"123",
	 *		"userName":"张三",
	 *		"mobile":"18719345678",
	 *		"areaName":"东方小区",
	 *		"regionName":"A区",
	 *		"buildingName":"1号楼",
	 *		"unitName":"1单元",
	 *		"floor":"01",
	 *		"room":"01",
	 *		"sex":"1",
	 *		"nation":"汉族",
	 *		"nationCode":"01",
	 *		"idCard":"xxxxxxxxxxxxxxxxx",
	 *		"registerAddress":"xxxxxxxxxxx",
	 *		"infoEnteringType":"2",
	 *		"liveDate":"2017-11-01",
	 *		"liveWay":"0",
	 *		"liveStatus":"1",
	 *		"updateTime":"2017-11-01 17:33:26",
	 *		"logoutTime":"2018-11-01 17:33:26"
     *   }]
	 * @param request
	 * @param response
	 */
	@RequestMapping("/syncData")
	public void syncDataAuthentication(HttpServletRequest request,HttpServletResponse response){
		
		processValidRequest(request, response, new RequetstHandler() {
			
			@Override
			public ResponseMsg processHandler(HttpServletRequest request, HttpServletResponse response, JSONObject paramsHeader,
					JSONObject paramsBody) {
				
				String token=paramsHeader.getString("token");
				//根据token获取平台账号
				String account=ServletMessageCommon.getAccountToken(token);
				
				//PlatFormJoin platFormJoin = platFormJoinService.findByAccount(account);
				
				//小区机构编号
				String areaCode=paramsBody.getString("areaCode");
				
				//对小区机构编号进行校验
				PlatFormJoinArea platFormJoinArea = platFormJoinAreaService.findByAccountAndAreaCode(account, areaCode);
				
				if(platFormJoinArea==null){
					return ResponseMsg.buildErrorResp(ResponseCode.NO_PERMISSION,"无权限访问未与平台账号关联的小区");
				}
				
				SyncDataVo syncDataVo=jsonObjectToBean(paramsBody, SyncDataVo.class);
				
				syncDataVo.setPlatFormAccount(account);
				
				//操作类型：1、新增  2、修改  3、删除
				Integer operateType=syncDataVo.getOperateType();
				
				List<PlatFormUserVo> users = syncDataVo.getList();
				
				if(operateType==null||users==null||users.size()==0||operateType<1||operateType>3){
					return ResponseMsg.buildErrorResp(ResponseCode.MESSAGE_FORMAT_ERROR);
				}
				
				//将第三方平台的住户地址转换为对讲地址
				setVdcsAddress(users);
				
				//存放所有没匹配到地址的用户id
				List<String> allNoMatchUserIds=new ArrayList<String>();
				//存放所有已经同步过数据的用户id
				List<String> allHasSaveUserIds=new ArrayList<String>();
				//存放此次同步任务需同步的数据
				Map<String,String> waitSyncDataMap=new HashMap<String,String>(users.size());
				//已经同步过的数据的map,key为(用户id+对讲地址)
				Map<String,String> hasSaveDataMap=null;
				
				//判断是否是新增操作,是新增操作，则需要将已经保存过的数据过滤掉
				if(operateType==1){
					//从redis中获取已经同步过数据的用户  
					String hasSaveUsersJson=redisCacheService.get(getSyncDataCacheKey(areaCode));
					
					//log.info("小区"+areaCode+"用户缓存:"+hasSaveUsersJson);
					
					if(!StringUtils.isEmpty(hasSaveUsersJson)){
						//已经同步过数据的用户map,key为用户id
						hasSaveDataMap=jsonStrToBean(hasSaveUsersJson,Map.class);
						
						//存放此次同步任务需同步的用户
						List<PlatFormUserVo> waitSyncDataUsers=new ArrayList<PlatFormUserVo>();

						for (PlatFormUserVo platFormUserVo : users) {
							
							//判断用户是否已经同步过数据
							if(hasSaveDataMap.containsKey(platFormUserVo.getId())){
								//已经同步过的数据不再处理
								allHasSaveUserIds.add(platFormUserVo.getUserId());
							}else{
								//未同步过的数据
								waitSyncDataMap.put(platFormUserVo.getId(), "");
								waitSyncDataUsers.add(platFormUserVo);
							}
						}

						if(waitSyncDataUsers.size()==0){
							return buildResp(allNoMatchUserIds,allHasSaveUserIds); 
						}

						//只同步还没同步过数据的用户
						syncDataVo.setList(waitSyncDataUsers);
						
					}else{

						for (PlatFormUserVo platFormUserVo : users) {
							waitSyncDataMap.put(platFormUserVo.getId(), "");
						}
					}
					
				}else{
					//删除和更新操作不需过滤数据
					for (PlatFormUserVo platFormUserVo : users) {
						waitSyncDataMap.put(platFormUserVo.getId(), "");
					}
					
					//如果是删除操作需要把缓存数据取出，当删除成功后要把删除的用户从缓存里清除
					if(operateType==3){
						//从redis中获取已经同步过数据的用户
						String hasSaveUsersJson=redisCacheService.get(getSyncDataCacheKey(areaCode));
						
						if(!StringUtils.isEmpty(hasSaveUsersJson)){
							//已经同步过数据的用户map,key为用户id
							hasSaveDataMap=jsonStrToBean(hasSaveUsersJson,Map.class);
						}
					}
				}
				
				
				if(hasSaveDataMap==null){
					hasSaveDataMap=new HashMap<String,String>(users.size());
				}
				
				//log.info("syncDataVo:"+JSON.toJSONString(syncDataVo));
				
				int maxProcessCount=500;
				/*
				 * 判断用户数据是否大于500,如果大于500则分批处理
				 */
				if(users.size()<maxProcessCount){
					List<PlatFormUserVo> noMatchUsers=syncDataService.syncData(syncDataVo);
					allNoMatchUserIds.addAll(getUserIds(noMatchUsers));
					
					//log.info("没匹配到的用户id:"+JSON.toJSONString(allNoMatchUserIds));
					
					//将没匹配到地址的数据，从waitSyncDataUserMap里移除
					for (PlatFormUserVo noMatchUser : noMatchUsers) {
						waitSyncDataMap.remove(noMatchUser.getId());
					}
					if(operateType==1){
						//新增操作需要把新增的数据更新到缓存里
						hasSaveDataMap.putAll(waitSyncDataMap);
						
						redisCacheService.set(getSyncDataCacheKey(areaCode),JSON.toJSONString(hasSaveDataMap));
					}
					
					if(operateType==3){  
						
						//删除操作需要把删除的数据从缓存里清除
						
						Set<String> dataIds = waitSyncDataMap.keySet();
						
						for (String dataId : dataIds) {
							hasSaveDataMap.remove(dataId);
						}
						
						redisCacheService.set(getSyncDataCacheKey(areaCode),JSON.toJSONString(hasSaveDataMap));
					}
					
					//log.info("数据同步后小区"+areaCode+"用户缓存:"+redisCacheService.get(getSyncDataCacheKey(areaCode)));
					
					return buildResp(allNoMatchUserIds,allHasSaveUserIds);
				}else{
					//总页数(将用户数据分页处理，每500条为1页)
					int totalpage=(int) Math.ceil(users.size()*1.0/maxProcessCount);
					
					for (int i = 1; i <= totalpage; i++) {
						
						//分页获取用户数据
						List<PlatFormUserVo> subList = CollectionUtil.subList(users, i, maxProcessCount);
						
						syncDataVo.setList(subList);
						
						//分批同步数据
						List<PlatFormUserVo> noMatchUsers=syncDataService.syncData(syncDataVo);
						
						if(noMatchUsers.size()>0){
							allNoMatchUserIds.addAll(getUserIds(noMatchUsers));
						}
						
						//将没匹配到地址的数据，从waitSyncDataUserMap里移除
						for (PlatFormUserVo noMatchUser : noMatchUsers) {
							waitSyncDataMap.remove(noMatchUser.getId());
						}
					}
					
					
					if(operateType==1){
						//新增操作需要把新增的数据更新到缓存里
						hasSaveDataMap.putAll(waitSyncDataMap);
						
						redisCacheService.set(getSyncDataCacheKey(areaCode),JSON.toJSONString(hasSaveDataMap));
					}
					
					if(operateType==3){
						
						//删除操作需要把删除的数据从缓存里清除
						Set<String> dataIds = waitSyncDataMap.keySet();
						
						for (String dataId : dataIds) {
							hasSaveDataMap.remove(dataId);
						}
						
						redisCacheService.set(getSyncDataCacheKey(areaCode),JSON.toJSONString(hasSaveDataMap));
					}

					return buildResp(allNoMatchUserIds,allHasSaveUserIds);
				}
			}
		});
	}
	
	
	private List<String> getUserIds(List<PlatFormUserVo> users){
		
		List<String> userIds=new ArrayList<String>();
		
		for (PlatFormUserVo platFormUserVo : users) {
			
			if(!StringUtils.isEmpty(platFormUserVo.getUserId())){
				userIds.add(platFormUserVo.getUserId());
			}
		}
		return userIds;
	}
	
	private String getSyncDataCacheKey(String areaCode){
		return RedisCacheService.KEY_PREFIX+"syncData_"+areaCode;
	}
	
	
	private ResponseMsg buildResp(List<String> errorUserIds,List<String> hasSaveUserIds){
		
		List<Map<String,String>> errorList=new ArrayList<Map<String,String>>();

		if(errorUserIds!=null){
			for (String userId : errorUserIds) {
				
				Map<String,String> map=new HashMap<String,String>(2);
				
				map.put("userId", userId);
				map.put("errorMsg", "地址不匹配");
				
				errorList.add(map);
			}
		}
		
		if(hasSaveUserIds!=null){
			for (String userId : hasSaveUserIds) {
				
				Map<String,String> map=new HashMap<String,String>(2);
				
				map.put("userId", userId);
				map.put("errorMsg", "已经同步过数据,如须修改请调用修改接口");
				
				errorList.add(map);
			}
		}
		
		ResponseMsg resp = ResponseMsg.buildSuccessResp();
		
		if(errorList.size()>0){
			resp.put("errorList", errorList);
		}
		
		return resp;
	};
	
	
	
	/**
	 * 将第三方平台的住户地址转换为对讲地址
	 * @param list
	 * @return
	 */
	private void setVdcsAddress(List<PlatFormUserVo> list){
		
		for (PlatFormUserVo platFormUserVo : list) {
			
			String vdcsAddress=convertAddress(platFormUserVo);
			
			if(!StringUtils.isEmpty(platFormUserVo.getUserId())&&!StringUtils.isEmpty(vdcsAddress)){
				platFormUserVo.setVdcsAddress(vdcsAddress);
				
				platFormUserVo.setId(platFormUserVo.getUserId()+vdcsAddress);
			}
			
			if(platFormUserVo.getId()==null){
				platFormUserVo.setId(UUIDGeneratorUtil.generateUUID());
			}
		}
	}
	
	/**
	 * 将第三方平台的住户地址转换为对讲地址
	 * @return
	 */
	private String convertAddress(PlatFormUserVo  platFormUserVo){
		
		try {
			
			String areaName = platFormUserVo.getAreaName();
			String regionName = platFormUserVo.getRegionName();
			String buildingName = platFormUserVo.getBuildingName();
			String unitName = platFormUserVo.getUnitName();
			String floor = platFormUserVo.getFloor();
			String room = platFormUserVo.getRoom();
			
			if(StringUtils.isEmpty(areaName)){
				return null;
			}
			
			StringBuilder address=new StringBuilder();
			
			//判断是否是别墅,如果房间号为空则为别墅
			if(StringUtils.isEmpty(room)){
				
				if(StringUtils.isEmpty(buildingName)){
					return null;
				}
				
				address.append(areaName);
				
				if(!StringUtils.isEmpty(regionName)){
					address.append(regionName);
				}
				
				address.append(buildingName);
				
			}else{
				
				if(StringUtils.isEmpty(floor)||StringUtils.isEmpty(room)||!room.matches("((0[1-9])|([1-9][0-9]))[A-Z]?")){
					return null;
				}
				
				address.append(areaName);
				
				if(!StringUtils.isEmpty(regionName)){
					address.append(regionName);
				}
				if(!StringUtils.isEmpty(buildingName)){
					address.append(buildingName);
				}
				if(!StringUtils.isEmpty(unitName)){
					address.append(unitName);
				}
				
				floor=floor.length()==1?"0"+floor:floor;
				
				address.append(floor);
				
				address.append(room);

				address.append("房");
			}
			
			return address.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
