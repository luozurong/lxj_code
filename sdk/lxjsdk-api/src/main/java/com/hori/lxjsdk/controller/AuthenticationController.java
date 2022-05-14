package com.hori.lxjsdk.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hori.lxjsdk.dao.vdcs.TalkSerialDao;
import com.hori.lxjsdk.enums.ResponseCode;
import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.service.ApplicationService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.service.PlatFormUserService;
import com.hori.lxjsdk.util.UUIDString;
import com.hori.lxjsdk.utils.FuzzyQueryUtils;
import com.hori.lxjsdk.utils.RandomUtil;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.hori.lxjsdk.webservice.ILXJSDKOauthService;
import com.jlit.hibernate.UUIDGeneratorUtil;
import com.jlit.memcached.session.SessionService;

import net.sf.json.JSONObject;

@Controller
public class AuthenticationController extends BaseApiRequestHelper{
	
	@Resource
	private PlatFormJoinService platFormJoinService;
	@Resource
	private ApplicationService applicationService;
	@Resource
	private PlatFormUserService platFormUserService;
	@Resource
	private ILXJSDKOauthService iLXJSDKOauthService;
	@Resource
	private TalkSerialDao talkSerialDao;
	
	/**
	 * 数据同步鉴权接口(不需验证token)
	 * 地址 : /lxjsdkApi/syncDataAuthentication
	 * 参数: 
	 * {
	 * 		header:{token:"",time_stamp:""}
	 * 
	 * 	  	body:{
	 * 			"account":"第三方平台接入账号",
	 *			"password":"账号密码"
	 * 		}
	 * } 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/syncDataAuthentication")
	public void syncDataAuthentication(HttpServletRequest request,HttpServletResponse response){
		processNoValidRequest(request, response, new RequetstHandler() {

			@Override
			public ResponseMsg processHandler(HttpServletRequest request, HttpServletResponse response,
					JSONObject paramsHeader, JSONObject paramsBody) {
				
				String account=paramsBody.getString("account");
				String password=paramsBody.getString("password");
				
				PlatFormJoin platFormAccount = platFormJoinService.findByAccountAndPass(account, password);
				
				if(platFormAccount==null){
					return ResponseMsg.buildErrorResp(ResponseCode.ACCOUNT_OR_PASSWORD_ERROR);
				}
				
				String token=UUIDGeneratorUtil.generateUUID();
				
				SessionService.getInstance().save(token, account);
				
				return ResponseMsg.buildSuccessResp().put("token", token);
			}
		});
	}
	
	
	/**
	 * 联享家SDK鉴权接口(不需验证token)
	 * 地址 : /lxjsdkApi/authentication
	 * 参数: 
	 * {
	 * 		header:{token:"",time_stamp:""}
	 * 
	 * 	  	body:{
	 * 			"appKey":"第三方注册后返回的appKey",
	 *			"sdkPackage":"SDK包名",
	 *          "accountInfo":{
     *				"userId":"用户ID",
	 *				"enUserId":"加密后的用户ID,加密方式< MD5(appKey+userId）>",
     * 				"username":"用户姓名，没有则填写为空字符串",
     * 				"userPhone":"用户手机号，没有则填写为空字符串"
	 *			 },
	 * 		}
	 * } 
	 */
	@RequestMapping("/authentication")
	public void lxjSDKOauth(HttpServletRequest request,HttpServletResponse response){
		
		processNoValidRequest(request, response, new RequetstHandler() {

			@Override
			public ResponseMsg processHandler(HttpServletRequest request, HttpServletResponse response,
					JSONObject paramsHeader, JSONObject paramsBody) {
				
				
				ResponseMsg responseMsg = ResponseMsg.buildEmptyResp();
				responseMsg.setResponseCode(ResponseCode.RSP_SUCCESS);
				responseMsg.put("token","0");
				responseMsg.put("status", "0");
				responseMsg.put("userAccount", "not exist");
				String appKey = paramsBody.getString("appKey");
				String sdkPackage = paramsBody.getString("sdkPackage");
				
				String userName = "";
				String userPhone = "";
				String password = "";
				JSONObject accountInfo = JSONObject.fromObject(paramsBody.getString("accountInfo"));
				
				String userId = accountInfo.getString("userId");
				String enUserId = accountInfo.getString("enUserId");
				if(accountInfo.containsKey("userName")){
					userName = accountInfo.getString("userName");
				}
				if(accountInfo.containsKey("userPhone")){
					userPhone = accountInfo.getString("userPhone");
				}
				Application application = applicationService.getByAppKey(appKey);
				
				boolean isExistAppKey = applicationService.validAppKey(application);//验证appKey是否存在
				if(!isExistAppKey){//没有指定appKey
					responseMsg.setResponseCode(ResponseCode.SDK_PACKAGE_UN_VALID);
					return responseMsg;	
				}
				
				boolean validSDKPackage = applicationService.validSDKPackage(application.getAppPackage(),sdkPackage);
				if(!validSDKPackage){//sdk包名不正确
					responseMsg.setResponseCode(ResponseCode.SDK_PACKAGE_UN_VALID);

					return responseMsg;		
				}		
				
				int mum = platFormUserService.queryPlatformUserNum(application.getPlatformJoinAccount(),userId);
				if(mum==0){//不存在userId
					responseMsg.setResponseCode(ResponseCode.USER_ID_NOT_EXIST);
					return responseMsg;	
				}

				
				//授权关联住房操作
				String userAccount = dealUnRelatedAppOauth(application.getPlatformJoinAccount(), appKey, userId, enUserId);
				
				 password = talkSerialDao.getPassword(userAccount);
				String token = createToken(userAccount);//生成或者获取token
				responseMsg.put("token",token);
				responseMsg.put("status", "1");		
				responseMsg.put("userAccount", userAccount);
				responseMsg.put("password", password);
				return responseMsg;	
			}
		});
	}
	
	/**
	 * 创建第三方用户账号 2开头
	 * @return
	 * @throws InterruptedException 
	 */
	private  synchronized String createThridUserAccount(String userId,String householdSerial){
		StringBuilder account = new StringBuilder();
		account.append("2");
		while(true){
			long currentTimeMillis = System.currentTimeMillis()/1000;
			account.append(currentTimeMillis);
			Object saveData = SessionService.getInstance().get("CREATE_THRID_ACCOUNT_"+account.toString());
			if(saveData==null){
				SessionService.getInstance().save("CREATE_THRID_ACCOUNT_"+account.toString(), account.toString());
				return account.toString();
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 授权关联住房操作
	 * @param platformJoinAccount  第三方平台接入账号
	 * @param appKey  第三方appKey
	 * @param userId    用户userId
	 * @param enUserId  加密后userId
	 */
	protected String  dealUnRelatedAppOauth(String platformJoinAccount,String appKey,String userId,String enUserId) {
		
		List<PlatFormUser> allUsers = platFormUserService.queryPlatFormUsers(platformJoinAccount,userId);
		//判断是否存在未关联的住房
		List<PlatFormUser> unOauthUsers = queryUnOauth(allUsers,platformJoinAccount,appKey,userId);
		
		//查询已创建的授权账号，没有表示第一次接入联享家平台，生成新账号
		String userAccount =queryAppOauthAccount(allUsers);
		if(FuzzyQueryUtils.isCondition(userAccount)){//有关联住房
			if("2".equals(userAccount.subSequence(0,1))){//授权账号非手机号
				String mobile = queryUserMobile(allUsers);
				if(FuzzyQueryUtils.isCondition(mobile)){
					//如果存在手机号；
					String result=iLXJSDKOauthService.changeAppAccount(userAccount,mobile);
					//多套房，变更对应的账号
					platFormUserService.updateAppOauthAccount(userId,mobile,platformJoinAccount);			
					
					userAccount = mobile;
				}
			}
		}else{//没有关联住房，查找是否有手机号，多套房都有手机号，以首次添加手机号为主
			userAccount = queryUserMobile(allUsers);
		}

		if(unOauthUsers.size()>0){//存在未开通房间，关联住房
			 	for(PlatFormUser user:unOauthUsers){
					if(!FuzzyQueryUtils.isCondition(userAccount)){
						if(FuzzyQueryUtils.isCondition(user.getMobile())){
							userAccount =user.getMobile();
						}else{
							userAccount =createThridUserAccount(user.getUserId(),user.getHouseholdSerial());
						}
					}
			 		String code = iLXJSDKOauthService.bindRoom(user.getUserId(),userAccount, user.getHouseholdSerial());
			 		if("0".equals(code)){
			 			user.setStatus(1);
			 			user.setAppOauthAccount(userAccount);
			 			platFormUserService.update(user);
			 			talkSerialDao.setJoinSdkFlag(userAccount,user.getHouseholdSerial(),"1");
			 		}
			 	}
		}
		return userAccount;
	}


	private String queryUserMobile(List<PlatFormUser> allUsers) {
		for(PlatFormUser user:allUsers){
			if(FuzzyQueryUtils.isCondition(user.getMobile())){
				return user.getMobile();
			}
		}
		//没有手机，返回空字符串
		return "";
	}


	/**
	 * 获取授权后的账号
	 * @param allUsers
	 * @return
	 */
	private String queryAppOauthAccount(List<PlatFormUser> allUsers) {
		for(PlatFormUser user:allUsers){
			if(FuzzyQueryUtils.isCondition(user.getAppOauthAccount())){
				return user.getAppOauthAccount();
			}
		}
		return null;
	}


	/**
	 * 处理更换账号逻辑
	 * @param userId
	 * @param oldAccount
	 * @param userPhone
	 * @param platFormJonAccount
	 */
	protected void dealChangeAppPhone(String userId,String oldAccount,String userPhone,String platFormJonAccount) {
		iLXJSDKOauthService.changeAppAccount(oldAccount, userPhone);
		List<PlatFormUser> platFormUsers = platFormUserService.queryPlatFormUsers(platFormJonAccount,userId);
		for(PlatFormUser pfu:platFormUsers){
			pfu.setAppOauthAccount(userPhone);
			platFormUserService.update(pfu);
		}
	}


	/**
	 * 
	 * @param userAccount
	 * @return  token
	 */
	private String createToken(String userAccount) {
		//生成token
		String token = UUIDString.getUUIDString();
		//把令牌做为key，账号为value
		SessionService.getInstance().save(token, userAccount);
		return token;
	}
	

	/**
	 * 判断是否存在未关联的住房
	 * @param appKey
	 * @param userId
	 * @param enUserId
	 * @return  true 存在未关联住房
	 */
	private List<PlatFormUser> queryUnOauth(List<PlatFormUser> allUsers,String platformAccount,String appKey, String userId) {
		List<PlatFormUser> users = new ArrayList<PlatFormUser>();
		for(PlatFormUser user:allUsers){
			if(user.getStatus()==0){
				users.add(user);
			}
		}
		return users;
	}
	
}
