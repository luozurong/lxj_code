package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.vo.AccountInfoVo;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;

public interface PlatFormUserService {

	void batchUpdate(List<PlatFormUser> platFormUsers);
	void batchSave(List<PlatFormUser> platformUsers);
	
	/**
	 * 根据第三方平台账号以及住户序列号列表查找住户信息
	 * @param platformAccount   第三方平台账号
	 * @param householdSerials  住户序列号列表
	 * @return
	 */
	public List<PlatFormUser> queryPlatFormUsers(String platformAccount, List<String> householdSerials);
	
	/**
	 * 查询userId对应的数量
	 * @param platformAccount 第三方平台账号 
	 * @param userId
	 * @return
	 */
	public int queryPlatformUserNum(String platformAccount,String userId);

	/**
	 * 根据第三方账号以及userId查找住户信息
	 * @param createAccount
	 * @param userId
	 * @return
	 */
	public List<PlatFormUser> queryPlatFormUsers(String createAccount, String userId);

	/**
	 * 更新数据
	 * @param user
	 */
	public void update(PlatFormUser user);

	/**
	 * 查找已生成的授权账号
	 * @param platformAccount 第三方应用账号
	 * @param userId  第三方userId
	 */
	public String queryAppOauthAccount(String platformAccount, String userId);

	/**
	 * 删除人员信息
	 * @param list
	 */
	public void deletePlatFormUser(String platformAccount,List<PlatFormUserVo> userVo);
	/**
	 * 解绑第三方用户授权账号逻辑
	 * @param oauthAccount
	 * @return
	 */
	public void bindOffThridUser(String oauthAccount,String householdSerial);
	
	/**
	 * 处理更换账号逻辑
	 * @param userId
	 * @param oldAccount
	 * @param userPhone
	 * @param platFormJonAccount
	 */
	public String changeUserAccout(String userId,String oldAccount,String userPhone,String platFormJonAccount);
	
	/**
	 * 变更授权账号
	 * @param userId
	 * @param appOauthAccount
	 * @param platFormAccount
	 */
	public void updateAppOauthAccount(String userId, String appOauthAccount, String platFormAccount);
}
