package com.hori.grms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hori.grms.dao.AttachmentMapper;
import com.hori.grms.dao.ContractApproveLogMapper;
import com.hori.grms.dao.ContractMapper;
import com.hori.grms.dao.ContractTemplateMapper;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.Contract;
import com.hori.grms.model.ContractApproveLog;

import com.hori.grms.model.ContractTemplate;
import com.hori.grms.queryBean.ContractProjectQueryBean;
import com.hori.grms.queryBean.ContractQueryBean;
import com.hori.grms.queryBean.ContractTemplateQueryBean;
import com.hori.grms.service.ContractService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.util.DateUtils;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.vo.ContractProjectVo;
import com.hori.grms.vo.ContractVo;
import com.hori.vo.UserDetailLoginVo;

@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractTemplateMapper contractTemplateMapper;
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private ContractApproveLogMapper contractApproveLogMapper;
	@Autowired
	private ProjectActionService projectActionService;
	@Autowired
	private PendingEventService pendingEventService;
	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	public PageInfo<ContractVo> getContractList(ContractQueryBean queryBean) {
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		if (StringUtils.isNotBlank(queryBean.getStartTime())) {
			queryBean.setStartTime(queryBean.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(queryBean.getEndTime())) {
			queryBean.setEndTime(queryBean.getEndTime() + " 23:59:59");
		}
		List<ContractVo> contractList = contractMapper.selectByContract(queryBean);
		// ??????
		for (int i = 0; i < contractList.size(); i++) {
			List<Attachment> attachments = attachmentMapper.findBycorrelationId(contractList.get(i).getId(), 2);
			contractList.get(i).setAttachments(attachments);
		}

		// ??????????????????
		PageInfo<ContractVo> pageInfo = new PageInfo<>(contractList);

		return pageInfo;
	}

	public List<Attachment> getAttachment(String id, String status) {
		// ??????
		List<Attachment> attachments = new ArrayList<Attachment>();
		if (status.equals("a")) {
			attachments = contractMapper.findBycorrelationId(id, "2", null);

		} else if (status.equals("s")) {
			attachments = contractMapper.findBycorrelationId(id, "2", "0");

		} else if (status.equals("c")) {
			attachments = contractMapper.findBycorrelationId(id, "2", "1");

		}
		return attachments;
	}

	@Override
	public PageInfo<ContractProjectVo> getProjectList(ContractProjectQueryBean queryBean) {
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<ContractProjectVo> projectList = contractMapper.selectProject(queryBean);
		PageInfo<ContractProjectVo> pageInfo = new PageInfo<>(projectList);

		return pageInfo;
	}

	@Override
	public PageInfo<ContractTemplate> getContractTemplate(ContractTemplateQueryBean queryBean) {
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		if (StringUtils.isNotBlank(queryBean.getStartTime())) {
			queryBean.setStartTime(queryBean.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(queryBean.getEndTime())) {
			queryBean.setEndTime(queryBean.getEndTime() + " 23:59:59");
		}
		List<ContractTemplate> contractTemplateList = contractTemplateMapper.selectByQueryBean(queryBean);
		PageInfo<ContractTemplate> pageInfo = new PageInfo<>(contractTemplateList);

		return pageInfo;
	}

	@Override
	public void delContractTemplate(String id) {
		contractTemplateMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void saveContractTemplate(ContractTemplate contractTemplate) {
		// TODO Auto-generated method stub
		contractTemplateMapper.insert(contractTemplate);
	}

	@Override
	// ?????????????????????
	public void saveContract(Contract contract, List<Attachment> attachments) {
		if (StringUtils.isNotBlank(contract.getId())) {
			ContractVo contractOld = contractMapper.selectByPrimaryKey(contract.getId());
			contract.setUpdateTime(new Date());
			contract.setCreaterAccount(contractOld.getCreaterAccount());
			contract.setCreaterName(contractOld.getCreaterName());
			contract.setCreaterTime(contractOld.getCreaterTime());
			contract.setContractCode(contractOld.getContractCode());

			contract.setStatus(1);
			contract.setId(contract.getId());
			contractMapper.updateByPrimaryKeySelective(contract);
			// ??????????????????
			attachmentMapper.deleteByCorrelationId(contract.getId());
			// ??????????????????
			for (int i = 0; i < attachments.size(); i++) {
				attachments.get(i).setCorrelationId(contract.getId());
				attachmentMapper.insertSelective(attachments.get(i));
			}
			//????????????????????????
			if(StringUtils.isNotBlank(contractOld.getProjectCode())){
				contractMapper.updateCodeForProject(contractOld.getProjectCode(), "");
			}
			// ?????????????????????????????????????????????????????????
			if (contract.getContractType() == 1) {
				contractMapper.updateCodeForProject(contract.getProjectCode(), contract.getContractCode());

			}
			// ????????????????????????
			pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 6, 0);
			// ?????????????????????????????????
			pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), 1, 0, contract.getCreaterAccount(), contract.getCreaterName());
		} else {
			contract.setCreaterTime(new Date());
			contract.setUpdateTime(new Date());
			contract.setStatus(1);
			contract.setId(UUIDGenerator.generate());
			contract.setContractCode(UUIDGenerator.generate());
			contractMapper.insertSelective(contract);
			// ??????????????????
			for (int i = 0; i < attachments.size(); i++) {
				attachments.get(i).setCorrelationId(contract.getId());
				attachmentMapper.insertSelective(attachments.get(i));
			}
			// ?????????????????????????????????????????????????????????
			if (contract.getContractType() == 1) {
				contractMapper.updateCodeForProject(contract.getProjectCode(), contract.getContractCode());

			}
			// ?????????????????????????????????
			pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), 1, 0, contract.getCreaterAccount(), contract.getCreaterName());

			

		}
	}

	@Override
	public ContractVo getContractOne(String id, HttpServletRequest request) {
		ContractVo contract = contractMapper.selectByPrimaryKey(id);
		List<Attachment> attachments = attachmentMapper.findBycorrelationId(id, 2);
		request.getSession().setAttribute("attchmentsFromContract", JSON.toJSONString(attachments));
		contract.setAttachments(attachments);
		return contract;
	}
	@Override
	public ContractVo getContractCode(String code) {
		ContractVo contract = contractMapper.selectExecuteContractByContractCode(code);

		return contract;
	}
	@Override
	public void delContract(String id) {
		ContractVo contract = contractMapper.selectByPrimaryKey(id);
		//??????????????????????????????
		contractMapper.updateCodeForProject(contract.getProjectCode(), "");
		// ????????????
		attachmentMapper.deleteByCorrelationId(id);
		// ????????????
		contractMapper.deleteByPrimaryKey(id);
	   //????????????
		pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(),
				10, 0);

	}

	@Override
	public Map<String, Object> updateCommitContract(String id) throws InterruptedException {
		// ??????
		Map<String, Object> map = new HashMap<String, Object>();
		ContractVo contract = contractMapper.selectByPrimaryKey(id);
		if (contract.getStatus() == 1) {
			int i = contractMapper.updateCommitContract(id, "2", new Date());
			// ????????????????????????
			pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 1, 0);
			// ??????????????????
			pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
					contract.getContractCode(), 2, 1, contract.getCreaterAccount(), contract.getCreaterName());

		}
		if (contract.getStatus() == 5) {

			// ??????????????????????????????
			if (contract.getContractType() == 1) {
				ContractProjectVo contractProjectVo = contractMapper.selectProjectStatus(contract.getProjectCode());
				if (contractProjectVo.getStatus().equals("3")) {
					map.put("status", false);
					map.put("msg", "?????????????????????????????????");
					return map;
				}
			}
			int i = contractMapper.updateCommitContract(id, "7", new Date());
			// ????????????????????????????????????
			projectActionService.createProjectActionsByProject(contract.getProjectCode());

			// ????????????????????????
			pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 5, 0);
			
			pendingEventService.createBackMoneyPendingEventByContractEnd(contract.getProjectName(),contract.getProjectCode());
			// ??????????????????
	/*		pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
					contract.getContractCode(), 7, 0, "", "");
*/
		}
		map.put("status", true);
		return map;
	}

	// ???????????????????????????????????????
	@Override
	public Map<String, Object> updateApproveContract(String id, String desc, String status,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// ??????
		ContractApproveLog contractApproveLog = new ContractApproveLog();
		ContractVo contract = contractMapper.selectByPrimaryKey(id);
		int oldStatus = contract.getStatus();
		if (status.equals("y")) {
			if (contract.getStatus() == 2 || contract.getStatus() == 3 || contract.getStatus() == 4) {
				int statusTemp = contract.getStatus() + 1;
				int i = contractMapper.updateCommitContract(id, String.valueOf(statusTemp), new Date());
				contract.setStatus(statusTemp);
				//??????????????????
				contractApproveLog.setApproveStatus(oldStatus);
				if(oldStatus!=4){
				//????????????????????????
				pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), statusTemp-1, statusTemp-2);
				//??????????????????
				pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(), 
						contract.getContractCode(), statusTemp, statusTemp-1, contract.getCreaterAccount(), contract.getCreaterName());
				
				}else{
					//????????????????????????
					pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), statusTemp-1, statusTemp-2);
					//??????????????????
					pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(), 
							contract.getContractCode(), statusTemp, 0,contract.getCreaterAccount(), contract.getCreaterName());
				}

                //??????????????????
				contractMapper.updateAttment(contract.getId());
				
			}
		} else if (status.equals("n")) {

			// ????????????????????????
			pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(),
					contract.getStatus(), contract.getStatus() - 1);
			// ??????????????????
			pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
					contract.getContractCode(), 6, 0, contract.getCreaterAccount(), contract.getCreaterName());

			// ????????????????????????
			String tempStatus = "1";
			if (contract.getStatus() == 2) {
				tempStatus = "6";
			}
			if (contract.getStatus() == 3) {
				tempStatus = "8";
			}
			if (contract.getStatus() == 4) {
				tempStatus = "9";
			}
			contractApproveLog.setApproveStatus(Integer.valueOf(tempStatus));
			int i = contractMapper.updateCommitContract(id, tempStatus, new Date());
			contract.setStatus(6);

		} else if (status.equals("c")) {
			if (contract.getStatus() == 2 || contract.getStatus() == 3 || contract.getStatus() == 4
					|| contract.getStatus() == 5) {
				int statusTemp = contract.getStatus() - 1;
				int i = contractMapper.updateCommitContract(id, String.valueOf(statusTemp), new Date());
				
				ContractApproveLog vo=contractApproveLogMapper.selectApproveLogNew(contract.getContractCode(), String.valueOf(contract.getStatus() - 1));
				if(vo!=null){
					contractApproveLogMapper.deleteByPrimaryKey(vo.getId());

				}

				if(contract.getStatus() == 5){
				// ????????????????????????
					pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(),
							contract.getStatus(), 0);
				}else{
					pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(),
							contract.getStatus(), contract.getStatus() - 1);
				}
				// ??????????????????
				pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), statusTemp, statusTemp - 1, contract.getCreaterAccount(),
						contract.getCreaterName());
			}
			if (contract.getStatus() == 6) {
				// 6??????????????????????????? ?????????-????????????
				int i = contractMapper.updateCommitContract(id, "2", new Date());
				// ?????????????????????????????????
				ContractApproveLog vo=contractApproveLogMapper.selectApproveLogNew(contract.getContractCode(), 
						"6");
				if(vo!=null){
					contractApproveLogMapper.deleteByPrimaryKey(vo.getId());

				}
				// ????????????????????????
				pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 6,
						0);
				// ??????????????????
				pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), 2, 1, contract.getCreaterAccount(), contract.getCreaterName());
			}
			if (contract.getStatus() == 8) {
				// 8??????????????????????????? ?????????-????????????
				int i = contractMapper.updateCommitContract(id, "3", new Date());
				// ?????????????????????????????????
				ContractApproveLog vo=contractApproveLogMapper.selectApproveLogNew(contract.getContractCode(), 
						"8");
				if(vo!=null){
					contractApproveLogMapper.deleteByPrimaryKey(vo.getId());

				}			// ????????????????????????
				pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 6,
						0);
				// ??????????????????
				pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), 3, 2, contract.getCreaterAccount(), contract.getCreaterName());

			}
			if (contract.getStatus() == 9) {
				// 9??????????????????????????? ?????????-????????????
				int i = contractMapper.updateCommitContract(id, "4", new Date());
				// ?????????????????????????????????
				ContractApproveLog vo=contractApproveLogMapper.selectApproveLogNew(contract.getContractCode(), 
						"9");
				if(vo!=null){
					contractApproveLogMapper.deleteByPrimaryKey(vo.getId());

				}
				// ????????????????????????
				pendingEventService.updateContractPendingEvent(contract.getProjectCode(), contract.getContractCode(), 6,
						0);
				// ??????????????????
				pendingEventService.createContractPendingEvent(contract.getProjectName(), contract.getProjectCode(),
						contract.getContractCode(), 4, 3, contract.getCreaterAccount(), contract.getCreaterName());
			}

		}
		if (status.equals("y") || status.equals("n")) {
			String userAccount = (String) request.getSession().getAttribute("userAccount");
			String roleName = (String) request.getSession().getAttribute("roleName");

			UserDetailLoginVo userDetailLoginVo = (UserDetailLoginVo) request.getSession().getAttribute("userDetailVo");
			contractApproveLog.setApproveAccount(userAccount);
			contractApproveLog.setApproveDesc(desc);
			contractApproveLog.setApproveName(userDetailLoginVo.getName());
			contractApproveLog.setApproveRoleName(roleName);
			contractApproveLog.setApproveTime(new Date());
			contractApproveLog.setContractCode(contract.getContractCode());
			contractApproveLog.setId(UUIDGenerator.generate());
			contractApproveLogMapper.insertSelective(contractApproveLog);
		}
		map.put("status", true);
		return map;
	}

	@Override
	public List<ContractApproveLog> selectApproveContractLog(String contractCode) {
		List<ContractApproveLog> approveLogList = contractApproveLogMapper.selectByContractCode(contractCode);
		return approveLogList;
	}

	@Override
	public List<ContractVo> selectContractOhter(String id) {
		List<ContractVo> contractList = contractMapper.selectByContractOther(id);
		return contractList;
	}

	@Override
	public ContractVo getExecuteContract(String projectCode) {
		return contractMapper.selectExecuteContractByProjectCode(projectCode);
	}

	@Override
	public void updateContractUrl(String id, String status, String atttempList) {
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????
		List<Attachment> attchments = new ArrayList<Attachment>();
		attchments = JSON.parseArray(atttempList, Attachment.class);
		for (int i = 0; i < attchments.size(); i++) {
			attchments.get(i).setCorrelationId(id);
			if (status.equals("5") || status.equals("7")) {
				attchments.get(i).setIsExtra(1);
			} else {
				attchments.get(i).setIsExtra(0);

			}
			attchments.get(i).setType(2);
			attchments.get(i).setId(UUIDGenerator.generate());
			attachmentMapper.insertSelective(attchments.get(i));
		}

	}

	@Override
	public void delAttachment(String[] id) {
		for (int i = 0; i < id.length; i++) {
			attachmentMapper.deleteByPrimaryKey(id[i]);
		}

	}

	@Override
	public ContractVo getExecuteContractByStatus(String projectCode, int status) {
		return contractMapper.selectECByProjectCodeAndStatus(projectCode, status);
	}

	@Override
	public void delContractInfoById(String id) {
		// ????????????
		attachmentMapper.deleteByCorrelationId(id);
		// ????????????
		contractMapper.deleteByPrimaryKey(id);

	}
}
