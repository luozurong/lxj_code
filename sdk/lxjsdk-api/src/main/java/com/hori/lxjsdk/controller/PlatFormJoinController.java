package com.hori.lxjsdk.controller;

import java.util.ArrayList;
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

import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.model.ProCityAreaTown;
import com.hori.lxjsdk.service.PlatFormJoinAreaService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.service.ProCityAreaTownService;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.jlit.memcached.session.SessionService;

/**
 * 平台对接接口controller
 */
@Controller
public class PlatFormJoinController extends BaseApiRequestHelper{
	private static final Logger log = LoggerFactory.getLogger(PlatFormJoinController.class);
	
	@Resource
	private PlatFormJoinService platFormJoinService;
	@Resource
	private PlatFormJoinAreaService platFormJoinAreaService;
	@Resource
	private ProCityAreaTownService proCityAreaTownService; 
	/**
	 * 添加平台接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addPlatFormJoin")
	public void addPlatFormJoin(HttpServletRequest request,
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
					String areaAddress=paramsBody.getString("areaAddress");
					String areaName = paramsBody.getString("areaName");
					
					//根据账号获取平台账号、密码、公司
					List<PlatFormJoin> platFormJoinList = platFormJoinService.getByCreateAccount(userAccount);
					//查看账号下是否有已注册通过的公司
					boolean isRegisted = false;
					PlatFormJoin platFormJoin = new PlatFormJoin();
					if(platFormJoinList!=null&&platFormJoinList.size()>0){
						
						for (PlatFormJoin platFormJoin2 : platFormJoinList) {
							//已有注册的公司
							if(platFormJoin2.getVerifyStatus().equals("2") || platFormJoin2.getVerifyStatus()=="2"){
								isRegisted= true;
								platFormJoin = platFormJoin2;
							}
						}
					}
					
					//如已有注册过的公司，则不保存平台信息，只保存小区
					if(!isRegisted){
						//保存平台信息
						platFormJoin.setCompany(company);
						platFormJoin.setCreateTime(new Date());
						platFormJoin.setVerifyStatus("0");
						platFormJoin.setCreateAccount(userAccount);
						platFormJoinService.addPlatFormJoin(platFormJoin);
					}
						
					
					//保存小区信息
					PlatFormJoinArea platFormJoinArea = new PlatFormJoinArea();
					platFormJoinArea.setAreaAddress(areaAddress);
					platFormJoinArea.setAreaName(areaName);
					platFormJoinArea.setCreateTime(new Date());
					platFormJoinArea.setVerifyStatus("0");
					platFormJoinArea.setPlatformJoinId(platFormJoin.getId());
					platFormJoinAreaService.addPlatFormJoinArea(platFormJoinArea);
					return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
	/**
	 * 获取平台列表接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getPlatFormList")
	public void getPlatFormList(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				//是否有注册通过的公司(0为无，1为有)
				String isRegisted = "0";
				//获取账号
				//String userAccount=paramsBody.getString("userAccount");
				String token = paramsHeader.getString("token");
				String userAccount = (String) SessionService.getInstance().get(token);
				//根据账号获取平台账号、密码、公司
				List<PlatFormJoin> platFormJoinList = platFormJoinService.getByCreateAccount(userAccount);
				if(platFormJoinList!=null&&platFormJoinList.size()>0){
					//根据平台ID获取接入小区列表
					for (PlatFormJoin platFormJoin : platFormJoinList) {
						if(platFormJoin.getVerifyStatus().equals("2") || platFormJoin.getVerifyStatus()=="2"){
							isRegisted = "1";
						}
					}
				}else{
					platFormJoinList = new ArrayList<PlatFormJoin>();
				}
				
				return ResponseMsg.buildSuccessResp().put("platFormJoinList", platFormJoinList).put("isRegisted", isRegisted);
			}
		});
	
	}
	
	
	/**
	 * 初始化省份信息接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/initProvince")
	public void initProvince(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				List<ProCityAreaTown> provinces = proCityAreaTownService.getNoParentId();
				return ResponseMsg.buildSuccessResp().put("provinces", provinces);
			}
		});
	
	}
	
	
	/**
	 * 获取市级接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCitySelectData")
	public void getCitySelectData(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				String code=paramsBody.getString("code");
				List<ProCityAreaTown> citys = proCityAreaTownService.findProCityAreaTownByParentId(code);
				return ResponseMsg.buildSuccessResp().put("citys", citys);
			}
		});
	
	}
	
	/**
	 * 获取区级接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAreaSelectData")
	public void getAreaSelectData(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				String code=paramsBody.getString("code");
				List<ProCityAreaTown> areas = proCityAreaTownService.findProCityAreaTownByParentId(code);
				return ResponseMsg.buildSuccessResp().put("areas", areas);
			}
		});
	
	}
	
	/**
	 * 获取街道接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getStreetSelectData")
	public void getStreetSelectData(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				String code=paramsBody.getString("code");
				List<ProCityAreaTown> streets = proCityAreaTownService.findProCityAreaTownByParentId(code);
				return ResponseMsg.buildSuccessResp().put("streets", streets);
			}
		});
	
	}
}
