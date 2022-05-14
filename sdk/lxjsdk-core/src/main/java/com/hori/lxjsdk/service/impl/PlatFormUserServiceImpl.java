package com.hori.lxjsdk.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.PlatFormUserDao;
import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.service.PlatFormUserService;
import com.hori.lxjsdk.utils.FuzzyQueryUtils;
import com.hori.lxjsdk.vo.AccountInfoVo;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;
import com.hori.lxjsdk.webservice.ILXJSDKOauthService;
import com.hori.lxjsdk.webservice.IThridHouseholdService;

@Service("platFormUserService")
public class PlatFormUserServiceImpl implements PlatFormUserService{
         
	@Resource
	private PlatFormUserDao platFormUserDao;
	
	@Resource
	private IThridHouseholdService iThridHouseholdService;
	
	@Resource	
	private ILXJSDKOauthService iLXJSDKOauthService;

	@Override
	public void batchSave(List<PlatFormUser> platformUsers) {
		platFormUserDao.batchSave(platformUsers);
	}

	@Override
	public void batchUpdate(List<PlatFormUser> platFormUsers) {
		platFormUserDao.batchUpdate(platFormUsers);
	}
	/**
	 * 根据第三方平台账号以及住户序列号列表查找住户信息
	 * @param platformAccount   第三方平台账号
	 * @param householdSerials  住户序列号列表
	 * @return
	 */
	public List<PlatFormUser> queryPlatFormUsers(String platformAccount, List<String> householdSerials){
		return platFormUserDao.queryPlatFormUsers(platformAccount, householdSerials);
	}	
	
	/**
	 * 查询userId对应的数量
	 * @param platformAccount 第三方平台账号
	 * @param userId
	 * @return
	 */
	public int queryPlatformUserNum(String platformAccount,String userId){
		return platFormUserDao.queryPlatformUserNum(platformAccount,userId);
	}
	

	/**
	 * 根据第三方账号以及userId查找住户信息
	 * @param createAccount
	 * @param userId
	 * @return
	 */
	public List<PlatFormUser> queryPlatFormUsers(String createAccount, String userId){
		return platFormUserDao.queryPlatFormUsers( createAccount, userId);
	}	
	
	/**
	 * 更新数据
	 * @param user
	 */
	public void update(PlatFormUser user){
		platFormUserDao.update(user);
	}
	
	/**
	 * 查找已生成的授权账号
	 * @param platformAccount 第三方应用账号
	 * @param userId  第三方userId
	 */
	public String queryAppOauthAccount(String platformAccount, String userId){
		return platFormUserDao.queryAppOauthAccount( platformAccount,  userId);
	}	
	
	/**
	 * 删除人员信息
	 * @param list
	 */
	public void deletePlatFormUser(String platformAccount,List<PlatFormUserVo> userVos){
		List<AccountInfoVo> vos = new ArrayList<AccountInfoVo>(userVos.size());
		for(PlatFormUserVo userVo:userVos){
			PlatFormUser user = platFormUserDao.queryAppOauthAccount(platformAccount, userVo.getUserId(),userVo.getHouseholdSerial());
			if(user!=null){
				platFormUserDao.remove(user);
				if(user.getStatus()==1){//已关联住房
					AccountInfoVo vo = new AccountInfoVo();
					vo.setHouseholdSerial(userVo.getHouseholdSerial());
					vo.setThridAccount(platformAccount);
					vo.setUserId(userVo.getUserId());
					vo.setUserAccount(user.getAppOauthAccount());
					vos.add(vo);		
				}
			}
		}
		iThridHouseholdService.removeHouseholdAccounts(vos);
	}

	/**
	 * 解绑第三方用户授权账号逻辑
	 * @param oauthAccount
	 * @return
	 */
	public void bindOffThridUser(String oauthAccount,String householdSerial){
		platFormUserDao.bindOffAppOauthAccount(oauthAccount,householdSerial);
	}
	
	/**
	 * 处理更换账号逻辑
	 * @param userId
	 * @param oldAccount
	 * @param userPhone
	 * @param platFormJonAccount
	 */
	public String changeUserAccout(String userId,String oldAccount,String userPhone,String platFormJonAccount){
			iLXJSDKOauthService.changeAppAccount(oldAccount, userPhone);
			return "0";
	}
	
	
	/**
	 * 变更授权账号
	 * @param userId
	 * @param appOauthAccount
	 * @param platFormAccount
	 */
	public void updateAppOauthAccount(String userId, String appOauthAccount, String platFormAccount){
		platFormUserDao.updateAppOauthAccount( userId,  appOauthAccount,  platFormAccount);
	}
}
