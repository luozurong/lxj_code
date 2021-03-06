package com.hori.grms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hori.grms.dao.AttachmentMapper;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.Contract;
import com.hori.grms.model.ContractApproveLog;
import com.hori.grms.model.ContractTemplate;
import com.hori.grms.pageModel.Json;
import com.hori.grms.queryBean.ContractProjectQueryBean;
import com.hori.grms.queryBean.ContractQueryBean;
import com.hori.grms.queryBean.ContractTemplateQueryBean;
import com.hori.grms.service.ContractService;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.vo.ContractProjectVo;
import com.hori.grms.vo.ContractVo;
import com.hori.vo.UserDetailLoginVo;

/**
 * 合同管理
 * @author hhb
 *
 */
@Controller
@RequestMapping("contract")
public class ContractController {
	@Autowired
	private ContractService contractService;
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	/**
	 * 分页获取合同列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getContractListData"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json getContractList(@RequestBody ContractQueryBean queryBean){
		//根据角色类型和数据域权限处理
		
		//首先判断是否是管理员
		String userType=(String)request.getSession().getAttribute("userType");
		byte roleType=(byte) request.getSession().getAttribute("roleType");
		if(userType.equals("0")){
			queryBean.setRoleType("-1");
			queryBean.setDataArea((byte)2);
		}else{
			//角色类型
			//数据域
			byte dataArea=(byte) request.getSession().getAttribute("dataArea");
			queryBean.setRoleType(String.valueOf(roleType));
			queryBean.setDataArea(dataArea);
			String userString="";
			if(dataArea!=2){
				List<String> userList=(List<String>)request.getSession().getAttribute("userList");
				for(int i=0;i<userList.size();i++){
					if(i==0){
						userString="'"+userList.get(i)+"'";
					}else{
						userString=userString+",'"+userList.get(i)+"'";
					}
				}
			}
			queryBean.setUserList(userString);
		}
		PageInfo<ContractVo> contractList=contractService.getContractList(queryBean);
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(contractList);
		json.setMsg(String.valueOf(roleType));
		return json;
	}
	/**
	 * 获取合同列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getContractOne"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json getContractOne(String id){
		ContractVo contract=contractService.getContractOne(id,request);
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(contract);
		return json;
	}
	/**
	 * 获取合同列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getContractByCode"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json getContractByCode(String code){
		ContractVo contract=contractService.getContractCode(code);
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(contract);
		return json;
	}
	
	/**
	 * 获取附件列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getAttachment"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json getAttachment(String id,String status){
		List<Attachment> attachment=contractService.getAttachment(id,status);
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(attachment);
		return json;
	}
	/**
	 * 保存合同数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/saveContract"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json saveContract(@RequestBody Contract contract){
		Json json=new Json();
		String userAccount=(String) request.getSession().getAttribute("userAccount");
		UserDetailLoginVo userDetailLoginVo=(UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
		String attchmentsStr=(String) request.getSession().getAttribute("attchmentsFromContract");
		List<Attachment> attchments=new ArrayList<Attachment>();
		attchments=JSON.parseArray(attchmentsStr, Attachment.class);
		try{
			if(attchments!=null&&attchments.size()>0){
				if(StringUtils.isNoneBlank(contract.getId())){
					contractService.saveContract(contract,attchments);
				}else{
					contract.setCreaterAccount(userAccount);
					contract.setCreaterName(userDetailLoginVo.getName());
					contractService.saveContract(contract,attchments);
					//新建合同从session里面获取附件数据保存到数据库
				
				}
			json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setMsg("合同附件不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	
	/**
	 * 分页获取项目列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getPorjectEdit"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json getPorjectEdit(@RequestBody ContractProjectQueryBean queryBean){
		//首先判断是否是管理员
		String userType=(String)request.getSession().getAttribute("userType");
		byte roleType=(byte) request.getSession().getAttribute("roleType");
		if(userType.equals("0")){
			queryBean.setRoleType("-1");
			queryBean.setDataArea((byte)2);
		}else{
			//角色类型
			//数据域
			byte dataArea=(byte) request.getSession().getAttribute("dataArea");
			queryBean.setRoleType(String.valueOf(roleType));
			queryBean.setDataArea(dataArea);
			String userString="";
			if(dataArea!=2){
				List<String> userList=(List<String>)request.getSession().getAttribute("userList");
				for(int i=0;i<userList.size();i++){
					if(i==0){
						userString="'"+userList.get(i)+"'";
					}else{
						userString=userString+",'"+userList.get(i)+"'";
					}
				}
			}
			queryBean.setUserList(userString);
		}
		PageInfo<ContractProjectVo> projectList=contractService.getProjectList(queryBean);
		
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(projectList);
		return json;
	}
	/**
	 * 分页获取合同模版列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/getContractTemplateListData"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json getContractTemplate(@RequestBody ContractTemplateQueryBean queryBean){
		//首先判断是否是管理员
		String userType=(String)request.getSession().getAttribute("userType");
		
		if(userType.equals("0")){
			queryBean.setRoleType("-1");
			queryBean.setDataArea((byte)2);
		}else{
			//角色类型
			byte roleType=(byte) request.getSession().getAttribute("roleType");
			//数据域
			byte dataArea=(byte) request.getSession().getAttribute("dataArea");
			queryBean.setRoleType(String.valueOf(roleType));
			queryBean.setDataArea(dataArea);
			String userString="";
			if(dataArea!=2){
				List<String> userList=(List<String>)request.getSession().getAttribute("userList");
				for(int i=0;i<userList.size();i++){
					if(i==0){
						userString="'"+userList.get(i)+"'";
					}else{
						userString=userString+",'"+userList.get(i)+"'";
					}
				}
			}
			queryBean.setUserList(userString);
		}
		PageInfo<ContractTemplate> contractTemplateList=contractService.getContractTemplate(queryBean);
		Json json=new Json();
		json.setSuccess(true);
		json.setObj(contractTemplateList);
		return json;
	}
	/**
	 * 保存合同模版列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/saveContractTemplate"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json saveContractTemplate(@RequestBody ContractTemplate contractTemplate){
		Json json=new Json();
		String userAccount=(String) request.getSession().getAttribute("userAccount");
		try{
			contractTemplate.setId(UUIDGenerator.generate());
			contractTemplate.setCreaterAccount(userAccount);
			contractTemplate.setCreateTime(new Date());
			contractService.saveContractTemplate(contractTemplate);
			json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 * 删除合同模版列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	@RequestMapping(value ={ "/delContractTemplate"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json delContractTemplate(String id){
		Json json=new Json();
		try{
			contractService.delContractTemplate(id);
			json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}

	/**
	 * 删除合同数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/delContract"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json delContract(String id){
		Json json=new Json();
		try{
			contractService.delContract(id);
			json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	
	/**
	 * 提交合同数据(已完成操作)
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/commitContract"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json commitContract(String id){
		Json json=new Json();
		try{
			Map<String,Object> map=contractService.updateCommitContract(id);
			if(map.get("status").equals(true)){
				json.setSuccess(true);

			}else{
				json.setSuccess(false);
				json.setMsg(String.valueOf(map.get("msg")));
			}
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 * 审批合同,撤回
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/approveContract"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json approveContract(String id,String desc,String status){
		Json json=new Json();
		try{
			Map<String,Object> map=contractService.updateApproveContract(id, desc, status, request);
			if(map.get("status").equals(true)){
				json.setSuccess(true);

			}else{
				json.setSuccess(true);
			}
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 * 查看审批日志
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/approveContractLog"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json approveContractLog(String contractCode){
		Json json=new Json();
		try{
			List<ContractApproveLog> approveList=contractService.selectApproveContractLog(contractCode);
			json.setObj(approveList);
		    json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	
	/**
	 * 找出该框架合同的其他执行合同
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/getContractOther"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json getContractOther(String id){
		Json json=new Json();
		try{
			List<ContractVo> approveList=contractService.selectContractOhter(id);
			json.setObj(approveList);
		    json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 * 添加附件
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/updateContractUrl"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json updateContractUrl(String id,String status,String atttempList){
		Json json=new Json();
		try{
			contractService.updateContractUrl(id,status,atttempList);
			json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 *  删除附件
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/delContractUrl"}, method = { RequestMethod.POST})
	@ResponseBody
	public Json delContractUrl(String[] id){
		Json json=new Json();
		try{
			contractService.delAttachment(id);
			json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
	/**
	 *  合同新建时候情况附件session数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value ={ "/clearSession"}, method = { RequestMethod.GET})
	@ResponseBody
	public Json clearSession(){
		Json json=new Json();
		try{
			 request.getSession().setAttribute("attchmentsFromContract", "");	
			 json.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);

		}
		return json;
	}
}
