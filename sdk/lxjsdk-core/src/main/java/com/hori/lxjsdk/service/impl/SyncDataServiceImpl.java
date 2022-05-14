package com.hori.lxjsdk.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hori.lxjsdk.dao.vdcs.HouseholdDao;
import com.hori.lxjsdk.dao.vdcs.PmsPeopleDao;
import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.service.PlatFormUserService;
import com.hori.lxjsdk.service.SyncDataService;
import com.hori.lxjsdk.utils.AsyncTaskExecutor;
import com.hori.lxjsdk.web.vo.SyncDataVo;
import com.hori.lxjsdk.webservice.ILXJSDKOauthService;
import com.jlit.hibernate.UUIDGeneratorUtil;
import com.hori.lxjsdk.web.vo.HouseholdVo;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;
import com.hori.lxjsdk.web.vo.PmsPeopleVo;

@Service("syncDataService")
public class SyncDataServiceImpl implements SyncDataService{

	@Resource
	private HouseholdDao householdDao;
	@Resource
	private PmsPeopleDao pmsPeopleDao;
	@Resource
	private PlatFormUserService platFormUserService;
	@Resource
	private ILXJSDKOauthService iLXJSDKOauthService;
	
	private Logger logger=Logger.getLogger(SyncDataServiceImpl.class);
	
	/**
	 * 存放当前正在执行的所有小区的同步数据任务，每个小区对应的Runnable里有各自的任务队列，
	 * 同个小区的同步任务会放到同一个Runnable里，当该小区的Runnable里的任务数为0时，该Runnable会从map里移除
	 */
	private ConcurrentHashMap<String, SyncDataRunnable> areaTaskMap=new ConcurrentHashMap<String,SyncDataRunnable>(3);
	
	private Lock lock=new ReentrantLock();
	
	@Override
	public List<PlatFormUserVo> syncData(SyncDataVo syncDataVo) {

		List<PlatFormUserVo> users = syncDataVo.getList();
		
		//将第三方平台地址转换为对讲这边的地址
		List<String> vdcsAddressList = getVdcsAddressList(users);
		
//		logger.info("转化后地址列表:"+vdcsAddressList);
		
		//根据地址获取住户信息
		List<HouseholdVo> householdVos = householdDao.findByAddress(vdcsAddressList);
		
		//住户信息Map,key为住户地址
		Map<String,HouseholdVo> householdMap=new HashMap<String,HouseholdVo>(householdVos.size());
		
		for (HouseholdVo household : householdVos) {
			
			householdMap.put(household.getAddress(), household);
		}
		
		//获取地址没有匹配到住户信息的user
		List<PlatFormUserVo> noMatchUsers = getNoMatchUsers(users, householdMap);
		
		List<PlatFormUserVo> matchUsers = getMatchUsers(users, householdMap);
		
//		logger.info("没匹配到地址的用户:"+JSON.toJSONString(noMatchUserIds));
//		logger.info("匹配到地址的住户:"+JSON.toJSONString(matchUsers));
		
		if(matchUsers.size()>0){
			syncDataVo.setList(matchUsers);
			//将同步第三方平台数据任务加到同步数据任务队列
			addTaskToQueue(syncDataVo);
		}
		
		
		//直接先返回成功(但数据还没同步完成)
		return noMatchUsers;
	}
	
	/**
	 * 将同步数据任务放到全局同步任务队列里，同时每个小区还有各自的对应的小区任务队列，同个小区的同步任务会放到小区任务队列里，
	 * 当该小区的小区任务队列里的任务数为0时，会从全局同步任务队列里移除
	 * @param syncDataVo
	 */
	public void addTaskToQueue(SyncDataVo syncDataVo){
		
		try {
			lock.lock();
			
			String areaCode = syncDataVo.getAreaCode();
			if (areaTaskMap.containsKey(areaCode)) {
				SyncDataRunnable syncDataRunnable = areaTaskMap.get(areaCode);

				syncDataRunnable.addToQueue(syncDataVo);

			} else {
				SyncDataRunnable syncDataRunnable = new SyncDataRunnable(syncDataVo);
				
				areaTaskMap.put(areaCode, syncDataRunnable);

				//同步第三方平台数据(后台异步执行数据同步)
				AsyncTaskExecutor.addAsyncTaskToQueue(syncDataRunnable);
			} 
		} finally {
			lock.unlock();
		}
	}
	
	private void saveData(SyncDataVo syncDataVo){
		
		List<PlatFormUserVo> list = syncDataVo.getList();
		
		List<String> householdSerials=getPmsPeopleHouseholdSerials(list);
		
		//业主Map
		Map<String,PmsPeopleVo> proprietorPeopleMap = pmsPeopleDao.getProprietorPeopleMap(householdSerials);
		
		//业主
		List<PmsPeopleVo> proprietorsPeoples=new ArrayList<PmsPeopleVo>(proprietorPeopleMap.size());
		//家属
		List<PmsPeopleVo> familyPeoples=new ArrayList<PmsPeopleVo>(list.size()-proprietorPeopleMap.size());

		List<PlatFormUser> platFormUsers=new ArrayList<PlatFormUser>(list.size());
		
		//用于存放业主userId的map,同步第三方平台数据时同一住房地址默认第一个用户为业主,其余为家属
		Map<String,String> householdMap=syncDataVo.getHouseholdMap();
		if(householdMap==null){
			householdMap=new HashMap<String,String>(list.size());
			syncDataVo.setHouseholdMap(householdMap);
		}
				
		
		for (PlatFormUserVo userVo : list) {
			String peopleId = UUIDGeneratorUtil.generateUUID();
			String pmsHouseholdAddressId=UUIDGeneratorUtil.generateUUID();
			
			HouseholdVo householdVo = userVo.getHouseholdVo();
			
			if(householdVo!=null){
				
				String householdSerial = householdVo.getHouseholdSerial();

				userVo.setPeopleId(peopleId);
				
				PmsPeopleVo peopleVo=new PmsPeopleVo();
				peopleVo.setId(peopleId);
				peopleVo.setAddress(householdVo.getAddress());
				peopleVo.setHouseholdSerial(householdSerial);
				peopleVo.setAreaCode(syncDataVo.getAreaCode());
				peopleVo.setXyid(householdVo.getXyid());
				peopleVo.setRegionSerial(householdVo.getRegionSerial());
				peopleVo.setUserId(userVo.getUserId());
				peopleVo.setUserName(userVo.getUserName());
				peopleVo.setMobile(userVo.getMobile());
				peopleVo.setSex(userVo.getSex());
				peopleVo.setNation(userVo.getNation());
				peopleVo.setNationCode(userVo.getNationCode());
				peopleVo.setIdCard(userVo.getIdCard());
				peopleVo.setRegisterAddress(userVo.getRegisterAddress());
				peopleVo.setInfoEnteringType(userVo.getInfoEnteringType());
				peopleVo.setLiveDate(userVo.getLiveDate());
				peopleVo.setLiveWay(userVo.getLiveWay());
				peopleVo.setLiveStatus(userVo.getLiveStatus());
				peopleVo.setUpdateTime(userVo.getUpdateTime());
				peopleVo.setLogoutTime(userVo.getLogoutTime());
				peopleVo.setPmsHouseholdAddressId(pmsHouseholdAddressId);
				
				PlatFormUser platFormUser=new PlatFormUser();
				platFormUser.setHouseholdSerial(householdSerial);
				platFormUser.setPlatformAccount(syncDataVo.getPlatFormAccount());
				platFormUser.setCreateTime(new Date());
				platFormUser.setMobile(userVo.getMobile());
				platFormUser.setPmsPeopleId(peopleId);
				platFormUser.setStatus(0);
				platFormUser.setUserId(userVo.getUserId());
				platFormUser.setUserName(userVo.getUserName());
				platFormUser.setAddress(userVo.getVdcsAddress());
				platFormUser.setAreaCode(syncDataVo.getAreaCode());
				platFormUser.setPmsHouseholdAddressId(pmsHouseholdAddressId);
				
				if(householdMap.containsKey(householdSerial) || !StringUtils.isEmpty(householdVo.getHouseholdName())){
					//家属
					platFormUser.setUserType(1);
					peopleVo.setPeopleType(1);
					
					platFormUsers.add(platFormUser);
					//家属
					familyPeoples.add(peopleVo);
					
				}else{
					householdMap.put(householdSerial, userVo.getUserId());
				   
					PmsPeopleVo pmsPeopleVo = proprietorPeopleMap.get(householdSerial);
					
					if(pmsPeopleVo!=null){
						peopleId=pmsPeopleVo.getId();
						//业主
						platFormUser.setUserType(0);
						peopleVo.setPeopleType(0);
						
						platFormUser.setPmsPeopleId(peopleId);
						platFormUser.setPmsHouseholdAddressId(pmsPeopleVo.getPmsHouseholdAddressId());
						
						peopleVo.setId(peopleId);
						peopleVo.setPmsHouseholdAddressId(pmsPeopleVo.getPmsHouseholdAddressId());
						
						platFormUsers.add(platFormUser);
						//业主
						proprietorsPeoples.add(peopleVo);
					}
				}
			}
		}
		
		/*因为在创建住房时会自动添加一个业主,所以业主为更新，家属为新增*/
		//新增家属
		pmsPeopleDao.batchSavePmsPeople(familyPeoples);
		//更新业主
		pmsPeopleDao.batchUpdatePmsPeople(proprietorsPeoples);
		//保存用户数据
		platFormUserService.batchSave(platFormUsers);
		//更新住户表
		householdDao.updateHousehold(proprietorsPeoples);
		
	}
	
	private void updateData(SyncDataVo syncDataVo){
		List<PlatFormUserVo> list = syncDataVo.getList();
		
		List<String> householdSerials=getPmsPeopleHouseholdSerials(list);

		List<PlatFormUser> platFormUsers = platFormUserService.queryPlatFormUsers(syncDataVo.getPlatFormAccount(), householdSerials);
		
		Map<String,PlatFormUser> platFormUserMap=new HashMap<String,PlatFormUser>(platFormUsers.size());
		
		for (PlatFormUser platformUser : platFormUsers) {
			platFormUserMap.put(platformUser.getUserId(), platformUser);
		}
		 
		List<PmsPeopleVo> peoples=new ArrayList<PmsPeopleVo>(list.size());
		List<PlatFormUser> updateUsers=new ArrayList<PlatFormUser>(list.size());
		List<PmsPeopleVo> proprietorsPeoples=new ArrayList<PmsPeopleVo>(list.size());

		for (PlatFormUserVo userVo : list) {

			PlatFormUser platFormUser=platFormUserMap.get(userVo.getUserId());
			
			if(platFormUser!=null){
				
				logger.info("更新用户数据:"+JSON.toJSONString(userVo));
				
				String mobile = StringUtils.isEmpty(userVo.getMobile())?platFormUser.getMobile():userVo.getMobile();

				PmsPeopleVo peopleVo=new PmsPeopleVo();
				//peopleVo.setId(peopleId);
				peopleVo.setId(platFormUser.getPmsPeopleId());
				peopleVo.setUserId(userVo.getUserId());
				peopleVo.setUserName(userVo.getUserName());
				peopleVo.setMobile(mobile);
				
				peopleVo.setSex(userVo.getSex());
				peopleVo.setNation(userVo.getNation());
				peopleVo.setNationCode(userVo.getNationCode());
				peopleVo.setIdCard(userVo.getIdCard());
				peopleVo.setRegisterAddress(userVo.getRegisterAddress());
				peopleVo.setInfoEnteringType(userVo.getInfoEnteringType());
				peopleVo.setLiveDate(userVo.getLiveDate());
				peopleVo.setLiveWay(userVo.getLiveWay());
				peopleVo.setLiveStatus(userVo.getLiveStatus());
				peopleVo.setUpdateTime(userVo.getUpdateTime());
				peopleVo.setLogoutTime(userVo.getLogoutTime());
				peopleVo.setPmsHouseholdAddressId(platFormUser.getPmsHouseholdAddressId());
				peopleVo.setHouseholdSerial(platFormUser.getHouseholdSerial());
				
				peoples.add(peopleVo);

				if(0==platFormUser.getUserType()){
					proprietorsPeoples.add(peopleVo);
				}
				
				if(!StringUtils.isEmpty(platFormUser.getAppOauthAccount()) && !StringUtils.isEmpty(userVo.getMobile()) && !userVo.getMobile().equals(platFormUser.getMobile())){
					try {
						
						String result=iLXJSDKOauthService.changeAppAccount(platFormUser.getAppOauthAccount(), userVo.getMobile());
					
						logger.info("更换手机号<"+platFormUser.getAppOauthAccount()+">:"+platFormUser.getMobile()+"->"+userVo.getMobile()+",结果:"+result);
						
						if("0".equals(result)){
							platFormUser.setAppOauthAccount(userVo.getMobile());
							//多套房，变更对应的账号
							platFormUserService.updateAppOauthAccount(userVo.getUserId(),userVo.getMobile(),syncDataVo.getPlatFormAccount());
						}
						
					} catch (Exception e) {
						logger.info("更换手机号失败:"+platFormUser.getMobile()+"->"+userVo.getMobile());
						e.printStackTrace();
					}
				}
				platFormUser.setUserName(userVo.getUserName());
				platFormUser.setMobile(mobile);
				
				updateUsers.add(platFormUser);
				
			}
		}
		
		pmsPeopleDao.batchUpdatePmsPeople(peoples);
		platFormUserService.batchUpdate(updateUsers);
		householdDao.updateHousehold(proprietorsPeoples);
	}
	
	private void deleteData(SyncDataVo syncDataVo){
		
		List<PlatFormUserVo> list = syncDataVo.getList();
		
		List<String> householdSerials=getPmsPeopleHouseholdSerials(list);

		List<PlatFormUser> platFormUsers = platFormUserService.queryPlatFormUsers(syncDataVo.getPlatFormAccount(), householdSerials);
		
		Map<String,PlatFormUser> platFormUserMap=new HashMap<String,PlatFormUser>(platFormUsers.size());
		
		for (PlatFormUser platformUser : platFormUsers) {
			platFormUserMap.put(platformUser.getUserId(), platformUser);
		}

		List<String> pmsPeopleIds=new ArrayList<String>(list.size());
		List<String> pmsHouseholdAddressIdIds=new ArrayList<String>(list.size());
		//业主住房序列号(存放删除的数据里哪些是业主,如果是业主要把household表的业主信息设为空,但不删除pms上的记录)
		List<String> propPeopleHouseholdSerials=new ArrayList<String>(list.size());

		
		for (PlatFormUserVo userVo : list) {

			PlatFormUser platFormUser=platFormUserMap.get(userVo.getUserId());
			
			if(platFormUser!=null){
				
				if(0==platFormUser.getUserType()){
					propPeopleHouseholdSerials.add(platFormUser.getHouseholdSerial());
				}else{
					if(platFormUser.getPmsPeopleId()!=null){
						pmsPeopleIds.add(platFormUser.getPmsPeopleId());
					}
					if(platFormUser.getPmsHouseholdAddressId()!=null){
						pmsHouseholdAddressIdIds.add(platFormUser.getPmsHouseholdAddressId());
					}
				}
			}
		}
		
		pmsPeopleDao.deletePmsPeople(pmsPeopleIds,pmsHouseholdAddressIdIds);
		
		platFormUserService.deletePlatFormUser(syncDataVo.getPlatFormAccount(), list);
		
		householdDao.clearHouseholdName(propPeopleHouseholdSerials);
	}
	
	/**
	 * 获取住房序列号
	 * @param list
	 * @return
	 */
	private List<String> getPmsPeopleHouseholdSerials(List<PlatFormUserVo> list){
		
		Set<String> householdSerials=new HashSet<String>();
		for (PlatFormUserVo userVo : list) {
			
			HouseholdVo householdVo = userVo.getHouseholdVo();
			
			if(householdVo!=null){
				householdSerials.add(householdVo.getHouseholdSerial());
			}
		}
		List<String> householdSerialList = new ArrayList<String>();
		householdSerialList.addAll(householdSerials);
		
		return householdSerialList;
	}
	
	/**
	 * 将第三方平台的住户地址转换为对讲地址
	 * @param list
	 * @return
	 */
	private List<String> getVdcsAddressList(List<PlatFormUserVo> list){
		
		List<String> addressList=new ArrayList<String>();
		
		for (PlatFormUserVo platFormUserVo : list) {
			
//			String vdcsAddress=convertAddress(platFormUserVo);
//			
//			if(!StringUtils.isEmpty(vdcsAddress)){
//				platFormUserVo.setVdcsAddress(vdcsAddress);
//				
//				addressList.add(vdcsAddress);
//			}
			
			if(!StringUtils.isEmpty(platFormUserVo.getVdcsAddress())){
				addressList.add(platFormUserVo.getVdcsAddress());
			}
				
		}
		
		return addressList;
	}
	
	/**
	 * 获取地址匹配到住户信息的用户
	 * @return
	 */
	private List<PlatFormUserVo> getMatchUsers(List<PlatFormUserVo> users,Map<String,HouseholdVo> householdMap){
		
		List<PlatFormUserVo> matchUsers=new ArrayList<PlatFormUserVo>();
		
		for (PlatFormUserVo user : users) {
			
			if(user.getVdcsAddress()!=null&&householdMap.containsKey(user.getVdcsAddress())){
				
				HouseholdVo householdVo = householdMap.get(user.getVdcsAddress());
				user.setHouseholdVo(householdVo);
				user.setHouseholdSerial(householdVo.getHouseholdSerial());
				matchUsers.add(user);
			}
		}
		
		return matchUsers;
	};
	
	
	
	/**
	 * 获取地址没有匹配到住户信息的user
	 * @return
	 */
	private List<PlatFormUserVo> getNoMatchUsers(List<PlatFormUserVo> users,Map<String,HouseholdVo> householdMap){
		
		List<PlatFormUserVo> noMatchUsers=new ArrayList<PlatFormUserVo>();
		
		for (PlatFormUserVo user : users) {
			
			if(StringUtils.isEmpty(user.getVdcsAddress())||!householdMap.containsKey(user.getVdcsAddress())){
				noMatchUsers.add(user);
			}
		}
		
		return noMatchUsers;
	};
	
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
	
	
	class SyncDataRunnable implements Runnable{
		
		private String areaCode;
		
		/**
		 * 同个小区的多个同步任务会加到队列里排队执行，即同一小区的同步数据的任务还没执行完时，不会执行该小区的其他同步任务
		 */
		private LinkedBlockingQueue<SyncDataVo> syncDataTaskQueue=new LinkedBlockingQueue<SyncDataVo>();
		
		
		public SyncDataRunnable(SyncDataVo syncDataVo) {
			super();
			areaCode=syncDataVo.getAreaCode();
			addToQueue(syncDataVo);
		}
		
		public SyncDataVo get(){
			
			SyncDataVo syncDataVo = null;
			
			try {
				syncDataVo = syncDataTaskQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return syncDataVo;
		}
		
	
		public void addToQueue(SyncDataVo syncDataVo){
			try{
				syncDataTaskQueue.put(syncDataVo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 取任务队列的任务数量
		 * @return
		 */
		public int getSyncDataTaskQueueSize() {
			return syncDataTaskQueue.size();
		}
		
		/**
		 * 取任务队列的任务数量并且当任务队列的任务数量为0时，结束任务
		 * @return
		 */
		public int getQueueSizeAndRemoveTask(){
			
			try {
				
				lock.lock();
				
				int size = getSyncDataTaskQueueSize();
				
				if(size<=0){
					areaTaskMap.remove(areaCode);
				}
				
				return size;
				
			} catch(Exception e){
				logger.error(e);
				return 0;
			}finally {
				lock.unlock();
			}
		}
		
		
		@Override
		public void run() {
			
			while (true) {
				
				SyncDataVo syncDataVo = get();
				//操作类型：1、新增  2、修改  3、删除
				Integer operateType = syncDataVo.getOperateType();
				
				logger.info("开始同步数据<小区机构编号:"+areaCode+",平台账号:"+syncDataVo.getPlatFormAccount()+",类型:"+operateType+">");

				try {
				
					if (1 == operateType) {
						saveData(syncDataVo);
					}
					if (2 == operateType) {
						updateData(syncDataVo);
					}
					if (3 == operateType) {
						deleteData(syncDataVo);
					}
					
				} catch(Exception e){
					logger.error("同步数据失败<小区机构编号:"+areaCode+",平台账号:"+syncDataVo.getPlatFormAccount()+",类型:"+operateType+">", e);
				}
				
				if (getQueueSizeAndRemoveTask() <= 0) {
					break;
				} 
			}
		}

		public String getAreaCode() {
			return areaCode;
		}
	}
}
