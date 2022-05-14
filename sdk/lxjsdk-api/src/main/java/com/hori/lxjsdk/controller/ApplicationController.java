package com.hori.lxjsdk.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.service.ApplicationService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.jlit.memcached.session.SessionService;

/**
 * 应用接口controller
 */
@Controller
public class ApplicationController extends BaseApiRequestHelper{
	private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	@Resource
	private ApplicationService applicationService;
	
	@Resource
	private PlatFormJoinService platFormJoinService;
	/**
	 * 创建应用接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addApplication")
	public void addApplication(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
					//获取账号
					//String userAccount=paramsBody.getString("userAccount");
					String token = paramsHeader.getString("token");
					String userAccount = (String) SessionService.getInstance().get(token);
					
					String company=paramsBody.getString("company");
					String appName=paramsBody.getString("appName");
					String appPackage=paramsBody.getString("appPackage");
					String appType = paramsBody.getString("appType");
					String platformJoinAccount = "";
					//根据账号获取平台账号、密码、公司
					List<PlatFormJoin> platFormJoinList = platFormJoinService.getByCreateAccount(userAccount);
					//查看账号下是否有已注册通过的公司
					if(platFormJoinList!=null&&platFormJoinList.size()>0){
						
						for (PlatFormJoin platFormJoin2 : platFormJoinList) {
							//已有注册的公司
							if(platFormJoin2.getVerifyStatus().equals("2") || platFormJoin2.getVerifyStatus()=="2"){
								platformJoinAccount = platFormJoin2.getJoinAccount();
							}
						}
					}
					
					Application application = new Application();
					application.setAppName(appName);
					application.setAppPackage(appPackage);
					
					application.setPlatformJoinAccount(platformJoinAccount);
					application.setCompany(company);
					application.setCreateAccount(userAccount);
					application.setVerifyStatus("0");
					application.setCreateTime(new Date());
					application.setAppType(appType);
					applicationService.addApplication(application);
					
					return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
	/**
	 * 获取应用列表接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getApplicationList")
	public void getApplicationList(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				//获取账号
				String token = paramsHeader.getString("token");
				String userAccount = (String) SessionService.getInstance().get(token);
				
				//String userAccount=paramsBody.getString("userAccount");
				String verifyStatus = paramsBody.getString("verifyStatus");

				List<Application> list = applicationService.getListByParam(userAccount,verifyStatus);
				
				return ResponseMsg.buildSuccessResp().put("list", list);
			}
		});
	
	}
	
	/**
	 * 删除应用接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delApplication")
	public void delApplication(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				//获取id
				String id = paramsBody.getString("id");

				applicationService.delById(id);
				
				return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
}
