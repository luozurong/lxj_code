package com.hori.lxjsdk.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hori.lxjsdk.enums.ResponseCode;
import com.hori.lxjsdk.model.User;
import com.hori.lxjsdk.service.SmsSenderWrapper;
import com.hori.lxjsdk.service.UserService;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.jlit.hibernate.UUIDGeneratorUtil;
import com.jlit.memcached.session.SessionService;
import com.trisun.message.sms.SmsSender;

/**
 * 用户接口controller
 */
@Controller
public class UserController extends BaseApiRequestHelper{
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	private static int LENGTH = 6;
	@Resource
	private UserService userService;
	@Resource
	private SmsSender smsSender;
	
	@Resource
	private SmsSenderWrapper smsSenderWrapper;
	/**
	 * 新增用户接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addUser")
	public void addUser(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
					String userAccount=paramsBody.getString("userAccount");
					
					String password=paramsBody.getString("password");
					String mobile=paramsBody.getString("mobile");
					
					//判断用户是否注册过
					User user = userService.findUserByAccount(userAccount);
					if(user!=null){
						return ResponseMsg.buildErrorResp(ResponseCode.ACCOUNT_REGISTED);
					}else{
						User newUser = new User();
						newUser.setUserAccount(userAccount);
						newUser.setPassword(password);
						newUser.setMobile(mobile);
						newUser.setCreateTime(new Date());
						userService.addUser(newUser);
					}
				return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
	
	/**
	 * 用户修改密码接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updatePassword")
	public void updatePassword(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
					String token = paramsHeader.getString("token");
					String userAccount = (String) SessionService.getInstance().get(token);
					String oldPassword=paramsBody.getString("oldPassword");
					String newPassword=paramsBody.getString("newPassword");
					
					//判断用户原始密码是否正确
					User user = userService.findUserByAccount(userAccount);
					if(!user.getPassword().equals(oldPassword)){
						return ResponseMsg.buildErrorResp(ResponseCode.OLDPASSWORD_NOT_TRUE);
					}else{
						user.setPassword(newPassword);
						userService.update(user);
					}
				return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
	
	/**
	 * 用户登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/login")
	public void login(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				String userAccount=paramsBody.getString("userAccount");
				String password=paramsBody.getString("password");
				String token = "";
					//用户登录
					User user = userService.login(userAccount,password);
					if(user==null){
						//登录账号或者密码错误
						return ResponseMsg.buildErrorResp(ResponseCode.ACCOUNT_OR_PASSWORD_ERROR);
					}else{
						token=UUIDGeneratorUtil.generateUUID();
						SessionService.getInstance().save(token, userAccount);
						
					}
				return ResponseMsg.buildSuccessResp().put("userAccount", user.getUserAccount()).put("token", token);
			}
		});
	
	}
	
	
	
	/**
	 * 验证用户接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/validateUser")
	public void validateUser(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				boolean registed = false;
				
				String userAccount=paramsBody.getString("userAccount");
					//判断用户是否注册过
					User user = userService.findUserByAccount(userAccount);
					if(user!=null){
						//用户名已注册
						registed = true;
					}
				return ResponseMsg.buildSuccessResp().put("registed", registed);
			}
		});
	
	}
	
	
	/**
	 * 用户注销接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request,
			HttpServletResponse response) {
		processValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				String token = paramsHeader.getString("token");
				SessionService.getInstance().remove(token);
				return ResponseMsg.buildSuccessResp();
				
			}
		});
	
	}
	
	/**
	 * 判断是否已有用户登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/isLogined")
	public void isLogined(HttpServletRequest request,
			HttpServletResponse response) {
		processValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				String token = paramsHeader.getString("token");
				boolean isLogined = SessionService.getInstance().sessionExists(token);
				String userAccount = "";
				//如果已有用户登录
				if(isLogined){
					userAccount = (String) SessionService.getInstance().get(token);
					return ResponseMsg.buildSuccessResp().put("isLogined", isLogined).put("userAccount", userAccount).put("token", token);
				}
				
				return ResponseMsg.buildSuccessResp().put("isLogined", isLogined).put("userAccount", userAccount).put("token", null);
				
			}
		});
	
	}
	
	
	/**
	 * 用户找回密码(获取验证码)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getMessageCode")
	public void getMessageCode(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				   String mobile=paramsBody.getString("mobile");
				   String code = "";
				   //根据手机号查找用户
				   List<User> users = userService.findUserByMobile(mobile);
				   if(users!=null&&users.size()>0){
					  // String randomCode=generateCheckCode();
					   //SessionService.getInstance().saveOutTime(randomCode, mobile, getExpiryDate(5));
					   //产生新验证码时先获取旧验证码，移除该旧验证码(否则定时器无法移除旧验证码，导致仍然可以用旧验证码登录),viliam,2016-03-07
					   if(SessionService.getInstance().get(mobile) != null){
						   String oldRandomCode = (String)SessionService.getInstance().get(mobile);
						   SessionService.getInstance().remove(mobile+oldRandomCode);
						   SessionService.getInstance().remove(mobile);
					   }
					   String randomCode = generateCheckCode();//修改为每次获取新的验证码
					   final String keyPhone = mobile+randomCode;
					   SessionService.getInstance().save(keyPhone, randomCode);
					   SessionService.getInstance().save(mobile, randomCode);//用来作为历史验证码
					   Timer timer = new Timer();//设置定时器5分钟后删除验证码
					   timer.schedule(new TimerTask() {
						   public void run() {
							   SessionService.getInstance().remove(keyPhone);
						   }
					   }, 1000*60*5);
					   
					   //smsSender.sendSms(mobile, randomCode + "（关联手机号验证码），5分钟内有效，超时请重新申请。");
					   int sendResult=smsSenderWrapper.sendSms(mobile, randomCode + "（关联手机号验证码），5分钟内有效，超时请重新申请。", request);
						switch (sendResult) {
						case 0:
							code = "发送成功";
							break;
						case 1:
							code = "同一号码当天发送次数限制（5次）";
							break;
						case 2:
							code = "同一IP当天发送次数超过限制（10次）";
							break;
						case 3:
							code = "同一号码发短信间隔时间超过限制（120s）";
							break;
						case -1:
							code = "调用短信网关发生异常";
							break;
						default:
							break;
						}
				   }else{
					   return ResponseMsg.buildErrorResp(ResponseCode.MOBILE_UNBIND_ACCOUNT);
				   }
			   return ResponseMsg.buildSuccessResp().put("code", code);
			}
		});
	
	}
	
	/**
	 * 用户获取新密码(短信发送)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getNewPassword")
	public void getNewPassword(HttpServletRequest request,
			HttpServletResponse response) {
		processNoValidRequest(request,response, new RequetstHandler(){

			@Override
			public ResponseMsg processHandler(HttpServletRequest request,
					HttpServletResponse response, JSONObject paramsHeader,JSONObject paramsBody) {
				
				   String messageCode=paramsBody.getString("code");
				   String mobile=paramsBody.getString("mobile");
				   if(mobile!=null&&!mobile.equals("")){
					   String keyPhone = mobile+messageCode;
					   if(!SessionService.getInstance().sessionExists(keyPhone)){
						  return ResponseMsg.buildErrorResp(ResponseCode.CODE_IS_EXPIRE);
					   }
					   String newPassword = generateCheckCode();
					   //验证码正确
					   if(SessionService.getInstance().get(keyPhone)!=null && SessionService.getInstance().get(keyPhone).equals(messageCode)){
						   smsSender.sendSms(mobile, "您的密码是："+newPassword+"，请登录后立即修改密码!");
						   //通过手机号查找所有用户
						   List<User> users = userService.findUserByMobile(mobile);
						   for (User user : users) {
							  user.setPassword(newPassword);
						   }
						   //更新用户密码
						   userService.batchUpdate(users);
						   
					   }else{
						   return ResponseMsg.buildErrorResp(ResponseCode.CODE_IS_INCORRECT);
					   }
					   
				   }else{
					   return ResponseMsg.buildErrorResp(ResponseCode.ACCOUNT_NOT_EXIST);
				   }
			   return ResponseMsg.buildSuccessResp();
			}
		});
	
	}
	
	
	
	private static String generateCheckCode() {
		// 定义验证码的字符表
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer rands = new StringBuffer();
		//Random random = new Random();
		for (int i = 0; i < LENGTH; i++) {
			int rand = (int) (Math.random() * 10);
			//int rand=random.nextInt(chars.length());
			rands.append(chars.charAt(rand));
		}

		
		if(SessionService.getInstance().sessionExists(rands.toString())){
			generateCheckCode();
		}else{
			return rands.toString();
		}
		return null;
	}
	
	private static Date getExpiryDate(int min){
		Calendar calendar = Calendar.getInstance();
		long time = calendar.getTimeInMillis();
		time += min * 60 * 1000;
		calendar.setTimeInMillis(time);

		return calendar.getTime();
	}
	
	
}
