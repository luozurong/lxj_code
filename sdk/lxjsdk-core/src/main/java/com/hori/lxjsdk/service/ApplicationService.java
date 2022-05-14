package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.web.queryBean.ApplicationQueryBean;
import com.jlit.db.support.Page;

public interface ApplicationService {
	/**
	 * 根据条件获取应用列表（账号和审核状态）
	 * @param queryBean
	 * @return
	 */
	List<Application> getListByParam(String account,String verifyStatus);
	
	/**
	 * 添加应用
	 */
	void addApplication(Application application);
	
	/**
	 * 应用列表
	 * @param queryBean
	 * @return
	 */
	public Page search(ApplicationQueryBean queryBean);

	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	Application getById(String id);

	/**
	 * 审核应用
	 * @param application
	 */
	void update(Application application);

	/**
	 * 删除
	 * @param id
	 */
	void delById(String id);


	/**
	 * 查找审核通过的指定appKey的申请数量
	 * @param appKey appKey
	 * @return
	 */
	int queryAccessAppKeyNum(String appKey);

	/**
	 * 根据appKey查找信息
	 * @param appKey
	 * @return
	 */
	Application getByAppKey(String appKey);

	
	/**
	 * 验证sdk 包正确性
	 * @param appName 第三方物业APP名称
	 * @param sdkPackage
	 * @return
	 */
	boolean validSDKPackage(String appName,String sdkPackage);
	
	
	/**
	 * 验证appKey是否存在
	 * @param appKey
	 * @return 
	 */
	boolean validAppKey(Application application);
	
	
	/**
	 * 验证userId是否存在
	 * @param platformAccount 第三方平台账号
	 * @param appKey
	 * @param userId
	 * @return
	 */
/*	boolean validUserId(String platformAccount,String appKey, String userId);*/
}
