package com.hori.lxjsdk.webservice.impl;

import javax.annotation.Resource;

import com.hori.lxjsdk.service.PlatFormUserService;
import com.hori.lxjsdk.webservice.IBindOffThridUserService;

public class IBindOffThridUserServiceImpl implements IBindOffThridUserService {

	@Resource(name="platFormUserService")
	private PlatFormUserService  platFormUserService;
	
	/**
	 * 解绑第三方用户授权账号逻辑
	 * @param oauthAccount
	 * @return
	 */
	@Override
	public String bindOffThridUser(String oauthAccount,String householdSerial) {
		platFormUserService.bindOffThridUser(oauthAccount,householdSerial);
		 return "0";
	}

}
