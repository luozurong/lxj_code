package com.hori.lxjsdk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.ApplicationDao;
import com.hori.lxjsdk.dao.PlatFormJoinDao;
import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.service.ApplicationService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.service.PlatFormUserService;
import com.hori.lxjsdk.web.queryBean.ApplicationQueryBean;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.jlit.db.support.Page;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService{
	@Resource(name="applicationDao")
	private ApplicationDao applicationDao;
	
	@Resource
	private PlatFormUserService platFormUserService;
	
	@Override
	public List<Application> getListByParam(String account,String verifyStatus) {
		return applicationDao.getListByParam(account,verifyStatus);
	}

	@Override
	public void addApplication(Application application) {
		applicationDao.addApplication(application);
	}

	@Override
	public Page search(ApplicationQueryBean queryBean) {
		return applicationDao.seach(queryBean);
	}

	@Override
	public Application getById(String id) {
		
		return applicationDao.get(id);
	}

	@Override
	public void update(Application application) {
		
		applicationDao.update(application);
	}

	@Override
	public void delById(String id) {
		applicationDao.removeById(id);
	}
	
	/**
	 * 查找审核通过的指定appKey的申请数量
	 * @param appKey appKey
	 * @return
	 */
	@Override
	public int queryAccessAppKeyNum(String appKey){
		return applicationDao.queryAccessAppKeyNum(appKey);
	}
	
	/**
	 * 根据appKey查找信息
	 * @param appKey
	 * @return
	 */
	public Application getByAppKey(String appKey){
		return applicationDao.getByAppKey(appKey);
	}
	
	/**
	 * 验证sdk 包正确性
	 * @param appName 第三方物业APP名称
	 * @param sdkPackage
	 * @return
	 */
	public boolean validSDKPackage(String appName,String sdkPackage) {
		if(appName==null || "".equals(appName)){
			return false;
		}
		if(sdkPackage.equals(appName)){
			return true;
		}
		return false;
	}
	
	/**
	 * 验证appKey是否存在
	 * @param appKey
	 * @return
	 */
	public boolean validAppKey(Application application) {
	
		if(application!=null && "2".equals(application.getVerifyStatus())){
			return true;
		}
		return false;
	}
	
	/**
	 * 验证userId是否存在
	 * @param platformAccount 第三方平台账号
	 * @param appKey
	 * @param userId
	 * @return
	 */
/*	public boolean validUserId(String platformAccount,String appKey, String userId) {
		int number = platFormUserService.queryPlatformUserNum(platformAccount,userId);
		if(number==0){
			return false;
		}
		return true;
	}*/
}
