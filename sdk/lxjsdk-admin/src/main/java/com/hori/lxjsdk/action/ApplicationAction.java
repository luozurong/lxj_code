package com.hori.lxjsdk.action;

import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.service.ApplicationService;
import com.hori.lxjsdk.utils.PaginatedListHelper;
import com.hori.lxjsdk.web.queryBean.ApplicationQueryBean;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.jlit.hibernate.UUIDGenerator;
import com.jlit.hibernate.UUIDGeneratorUtil;

@Action(value = "applicationAction")
public class ApplicationAction extends BaseAction{
	private PaginatedListHelper plats;//封装的page对象
	private ApplicationQueryBean queryBean;
	private String id;
	private String verifyStatus;
	private Application application;
	
	@Resource
	private ApplicationService applicationService;
	
	@Action(value="application",results={@Result(name="success",location="/WEB-INF/pages/applicationList.jsp")})
	   public String list(){
		   int pageNumber = this.getPageNumber();
		   if(queryBean ==null){
			   queryBean = new ApplicationQueryBean();
		   }
		   queryBean.setPageNumber(pageNumber);
		   plats = new PaginatedListHelper(applicationService.search(queryBean));
		   return SUCCESS;
	   }
	
	@Action(value="viewApplication",results={@Result(name="success",location="/WEB-INF/pages/viewApplication.jsp")})
	   public String view(){
		application = applicationService.getById(id);
		   return SUCCESS;
	   }
	
	@Action(value="auditApplicationPage",results={@Result(name="success",location="/WEB-INF/pages/auditApplication.jsp")})
	   public String audit(){
		application = applicationService.getById(id);
		return SUCCESS;
	   }
	
	@Action(value="auditApplication")
	   public void auditOperator(){
		 HttpServletRequest request = getRequest();
		 String userAccount = (String) request.getSession().getAttribute("userAccount");//运营商账号
		 String userName = (String) request.getSession().getAttribute("userName");//登陆者名字
		 HttpServletResponse response = ServletActionContext.getResponse();
		 PrintWriter pw = null;
		 try {
			 pw = response.getWriter();
			 application = applicationService.getById(id);
			 if(application.getAppKey() ==null){
				 String appid = UUIDGeneratorUtil.generateUUID();
				 application.setAppKey(appid);
			 }
			 application.setVerifyStatus(verifyStatus);
			 application.setVerifyAccount(userAccount);
			 application.setVerifyManager(userName);
			 application.setVerifyTime(new Date());
			
			 applicationService.update(application);
			 pw.write("1");
		 }catch(Exception e){
			 e.printStackTrace();
			 pw.write("0");
		 }
		   finally {
				if (pw != null) {
					pw.flush();
					pw.close();
				}
			}
	   }
	public PaginatedListHelper getPlats() {
		return plats;
	}
	public void setPlats(PaginatedListHelper plats) {
		this.plats = plats;
	}
	public ApplicationQueryBean getQueryBean() {
		return queryBean;
	}
	public void setQueryBean(ApplicationQueryBean queryBean) {
		this.queryBean = queryBean;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	
}
