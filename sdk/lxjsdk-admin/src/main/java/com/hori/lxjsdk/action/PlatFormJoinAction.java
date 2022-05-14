package com.hori.lxjsdk.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.service.PlatFormJoinAreaService;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.utils.PaginatedListHelper;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.hori.lxjsdk.web.vo.PlatFormJoinAreaVo;
import com.hori.vdcs.model.Area;
import com.jlit.cms.webservice.IAreaManageService;
import com.jlit.db.support.PageWs;
import com.jlit.webservice.queryBean.cms.AreaManageQueryBean;

@Action(value = "platFormJoinAction")
public class PlatFormJoinAction extends BaseAction{
	private PaginatedListHelper plats;//封装的page对象
	private PlatFormJoinQueryBean queryBean;
	private String id;
	private String organizationSeq;
	private String result;
	private String verifyStatus;
	private PlatFormJoin formJoin;
	private PlatFormJoinAreaVo joinArea;
	@Resource
	private PlatFormJoinService platFormJoinService;
	
	@Resource
	private PlatFormJoinAreaService platFormJoinAreaService;
	
	@Resource
	private IAreaManageService iAreaManageService;
	
	@Action(value="platFormJoin",results={@Result(name="success",location="/WEB-INF/pages/platFormJoinList.jsp")})
	   public String list(){
		   int pageNumber = this.getPageNumber();
		   if(queryBean ==null){
			   queryBean = new PlatFormJoinQueryBean();
		   }
		   queryBean.setPageNumber(pageNumber);
		   plats = new PaginatedListHelper(platFormJoinService.search(queryBean));
		   return SUCCESS;
	   }
	
	@Action(value="viewPlatform",results={@Result(name="success",location="/WEB-INF/pages/viewPlatform.jsp")})
	   public String view(){
		   formJoin = platFormJoinService.getById(id);
		   return SUCCESS;
	   }
	
	@Action(value="auditPlatFormPage",results={@Result(name="success",location="/WEB-INF/pages/auditPlatForm.jsp")})
	   public String audit(){
		   formJoin = platFormJoinService.getById(id);
		   return SUCCESS;
	   }
	
	@Action(value="auditPlatForm")
	   public void auditOperator(){
		 HttpServletRequest request = getRequest();
		 String userAccount = (String) request.getSession().getAttribute("userAccount");//运营商账号
		 String userName = (String) request.getSession().getAttribute("userName");//登陆者名字
		 HttpServletResponse response = ServletActionContext.getResponse();
		 PrintWriter pw = null;
		 try {
			 pw = response.getWriter();
			 String key[] = platFormJoinService.createKey();
			 formJoin = platFormJoinService.getById(id);
			 formJoin.setVerifyStatus(verifyStatus);
			 if(verifyStatus.equals("2")){
				 formJoin.setJoinAccount(key[0]);
				 formJoin.setJoinPassword(key[1]);
			 }
			 formJoin.setVerifyAccount(userAccount);
			 formJoin.setVerifyManager(userName);
			 formJoin.setVerifyTime(new Date());
			 platFormJoinService.update(formJoin);
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
	
	@Action(value="auditAreasList",results={@Result(name="success",location="/WEB-INF/pages/auditAreasList.jsp")})
	public String auditAreasList(){
		formJoin = platFormJoinService.getById(id);
		List<PlatFormJoinArea> joinAreas = formJoin.getPlatFormJoinAreaList();
		String company = formJoin.getCompany();
		getRequest().setAttribute("joinAreasList",joinAreas);
		getRequest().setAttribute("company",company);
		return SUCCESS;
	}
	
	@Action(value="viewPlatformArea",results={@Result(name="success",location="/WEB-INF/pages/viewPlatformArea.jsp")})
	public String viewPlatformArea(){
		joinArea = platFormJoinAreaService.getPlatFormJoinAreaInfo(id);
		if(joinArea ==null){
			joinArea = new PlatFormJoinAreaVo();
		}
		return SUCCESS;
	}
	
	@Action(value="auditPlatformArea",results={@Result(name="success",location="/WEB-INF/pages/auditPlatformArea.jsp")})
	public String auditPlatformArea(){
		joinArea = platFormJoinAreaService.getPlatFormJoinAreaInfo(id);
		AreaManageQueryBean bean = new AreaManageQueryBean();
		bean.setAreaName(joinArea.getAreaName());
		//bean.setDetailAddress(joinArea.getAreaAddress());
		List<Area> areas = iAreaManageService.getAreas(bean);
		getRequest().setAttribute("areas",areas);
		return SUCCESS;
	}
	
	@Action(value="auditPass")
	public void auditPass(){
		 HttpServletResponse response = ServletActionContext.getResponse();
		 PrintWriter pw = null;
		 try {
			 pw = response.getWriter();
			 PlatFormJoinArea formJoinArea =  platFormJoinAreaService.getById(id);
			 formJoinArea.setVerifyStatus(result);
			 formJoinArea.setVerifyTime(new Date());
			 if(result.equals("2")){
				 formJoinArea.setAreaCode(organizationSeq);
				 formJoinArea.setJoinAccount(platFormJoinService.getById(formJoinArea.getPlatformJoinId()).getJoinAccount());
			 }
			 platFormJoinAreaService.update(formJoinArea);
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
	
	public PlatFormJoinQueryBean getQueryBean() {
		return queryBean;
	}
	public void setQueryBean(PlatFormJoinQueryBean queryBean) {
		this.queryBean = queryBean;
	}
	public PaginatedListHelper getPlats() {
		return plats;
	}
	public void setPlats(PaginatedListHelper plats) {
		this.plats = plats;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOrganizationSeq() {
		return organizationSeq;
	}

	public void setOrganizationSeq(String organizationSeq) {
		this.organizationSeq = organizationSeq;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public PlatFormJoin getFormJoin() {
		return formJoin;
	}
	public void setFormJoin(PlatFormJoin formJoin) {
		this.formJoin = formJoin;
	}

	public PlatFormJoinAreaVo getJoinArea() {
		return joinArea;
	}

	public void setJoinArea(PlatFormJoinAreaVo joinArea) {
		this.joinArea = joinArea;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	
}
