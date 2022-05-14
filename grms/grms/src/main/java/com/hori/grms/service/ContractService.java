package com.hori.grms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.Contract;
import com.hori.grms.model.ContractApproveLog;
import com.hori.grms.model.ContractTemplate;
import com.hori.grms.queryBean.ContractProjectQueryBean;
import com.hori.grms.queryBean.ContractQueryBean;
import com.hori.grms.queryBean.ContractTemplateQueryBean;
import com.hori.grms.vo.ContractProjectVo;
import com.hori.grms.vo.ContractVo;

public interface ContractService {
	/**
	 * 分页获取合同列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	public PageInfo<ContractVo> getContractList(ContractQueryBean queryBean);
	/**
	 * 分页获取项目列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	public PageInfo<ContractProjectVo> getProjectList(ContractProjectQueryBean queryBean);
	/**
	 * 分页获取合同模版列表数据
	 * @param ContractTemplateQueryBean
	 * @return
	 */
	public PageInfo<ContractTemplate> getContractTemplate(ContractTemplateQueryBean queryBean);
	/**
	 * 删除合同模版列表数据
	 * @param String
	 * @return
	 */
	public void delContractTemplate(String id);
	/**
	 * 保存合同模版数据
	 * @param ContractTemplate
	 * @return
	 */
	public void saveContractTemplate(ContractTemplate contractTemplate);
	/**
	 * 根据项目编号获取【执行合同】
	 * @param projectCode
	 * @return
	 */
	ContractVo getExecuteContract(String projectCode);
	/**
	 * 保存合同数据
	 * @param ContractVo
	 * @return
	 */
	public void saveContract(Contract contract,List<Attachment> attachments);
	
	/**
	 * 获取单个合同信息
	 * @param ContractVo
	 * @return
	 */
	public ContractVo getContractOne(String id,HttpServletRequest request);
	/**
	 * 获取单个合同信息
	 * @param ContractVo
	 * @return
	 */
	public ContractVo getContractCode(String code);
	/**
	 * 删除合同数据
	 * @param String
	 * @return
	 */
	public void delContract(String id);
	/**
	 * 提交合同和已经完成操作
	 * @param String
	 * @return
	 * @throws InterruptedException 
	 */
	public Map<String,Object> updateCommitContract(String id) throws InterruptedException;
	/**
	 * 审核成功和审核不成功和撤回
	 * @param String
	 * @return
	 */
	public Map<String,Object> updateApproveContract(String id,String desc,String status,HttpServletRequest request);
	/**
	 * 查询审批意见
	 * @param String
	 * @return
	 */
	public List<ContractApproveLog> selectApproveContractLog(String contractCode);
	/**
	 * 查询其他框架合同
	 * @param String
	 * @return
	 */
	public List<ContractVo> selectContractOhter(String id);
	
	/**
	 * 更新合同附件
	 * @param String
	 * @return
	 */
	public void updateContractUrl(String id,String status,String atttempList);
	
	/**
	 * 查找合同附件
	 * @param String
	 * @return
	 */
	public List<Attachment> getAttachment(String id,String status);
	
	/**
	 * 删除合同附件
	 * @param String
	 * @return
	 */
	public void delAttachment(String[] id);
	
	
	/**
	 * 根据项目编号获取【执行合同】
	 * @param projectCode
	 * @param status 合同状态，1：未提交 2：待审批-业务管理 3:待审批-合同管理 4:待审批-财务管理 5:审批通过 6:业务-审批不通过 7:已完成 8 合同-审批不通过 9 财务-审批不通过
	 * @return
	 */
	public ContractVo getExecuteContractByStatus(String projectCode, int status);
	/**
	 * 项目终止删除相关的合同及附件
	 * @param id 合同id
	 */
	public void delContractInfoById(String id);
}
