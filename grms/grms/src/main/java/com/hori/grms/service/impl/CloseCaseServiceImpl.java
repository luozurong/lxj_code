package com.hori.grms.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.CloseCaseMapper;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.CloseCaseInfo;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectActionException;
import com.hori.grms.queryBean.CloseCaseQueryBean;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.service.ContractService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectActionExceptionService;
import com.hori.grms.service.ProjectProductMenuAreaService;
import com.hori.grms.service.ProjectProductService;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.CloseCaseVo;
import com.hori.grms.vo.ContractVo;
import com.hori.grms.vo.ProjectProductVo;

@Service
public class CloseCaseServiceImpl implements CloseCaseService {

	@Autowired
	private CloseCaseMapper closeCaseMapper;
	
	@Autowired
	private ProjectProductService projectProductService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private BackMoneyPlanService backMoneyPlanService;
	
	@Autowired
	private ProjectProductMenuAreaService projectProductMenuAreaService;
	
	@Autowired
	private PendingEventService pendingEventService;
	
	@Autowired
	private ProjectActionExceptionService projectActionExceptionService;
	
	@Override
	public List<CloseCaseVo> getCloseCases(CloseCaseQueryBean queryBean) {
		if(queryBean != null) {
			queryBean.setPageIndex((queryBean.getPageNumber() - 1) * queryBean.getPageSize());
			if(StringUtils.isNotBlank(queryBean.getKeyword())) {
				queryBean.setKeyword("%" + queryBean.getKeyword() + "%");
			}
			if(StringUtils.isNotBlank(queryBean.getCreateTimeEnd())) {
				queryBean.setCreateTimeEnd(queryBean.getCreateTimeEnd().trim() + " 23:59:59");
			}
			if(StringUtils.isNotBlank(queryBean.getActionTimeEnd())) {
				queryBean.setActionTimeEnd(queryBean.getActionTimeEnd().trim() + " 23:59:59");
			}
		}
		List<CloseCaseVo> ccis = closeCaseMapper.getCloseCases(queryBean);
		if(ccis != null && !ccis.isEmpty()) {
			for(CloseCaseVo cc : ccis) {
				//??????
				if(cc.getRoleType() == 0) {
					//?????????????????????????????????
					List<Attachment> attachments = new LinkedList<>();
					List<CloseCaseInfo> cinfos = closeCaseMapper.getCloseCaseByProjectCode(cc.getProjectCode());
					if(cinfos != null && !cinfos.isEmpty()) {
						List<Attachment> attachs = null;
						for(CloseCaseInfo cinfo : cinfos) {
							attachs = closeCaseMapper.getAttachmentByCloseCaseId(cinfo.getId());
							attachments.addAll(attachs);
						}
					}
					cc.setAttachments(attachments);
				//??????
				}else {
					//??????
					List<Attachment> attachments = closeCaseMapper.getAttachmentByCloseCaseId(cc.getId());
					cc.setAttachments(attachments);
				}
			}
		}
		return ccis;
	}

	@Override
	public int getCloseCaseCount(CloseCaseQueryBean queryBean) {
		if(queryBean != null) {
			if(StringUtils.isNotBlank(queryBean.getKeyword())) {
				queryBean.setKeyword("%" + queryBean.getKeyword() + "%");
			}
			if(StringUtils.isNotBlank(queryBean.getCreateTimeEnd())) {
				queryBean.setCreateTimeEnd(queryBean.getCreateTimeEnd().trim() + " 23:59:59");
			}
			if(StringUtils.isNotBlank(queryBean.getActionTimeEnd())) {
				queryBean.setActionTimeEnd(queryBean.getActionTimeEnd().trim() + " 23:59:59");
			}
		}
		return closeCaseMapper.getCloseCaseCount(queryBean);
	}
	
	@Override
	public Attachment getAttachmentById(String id) {
		return closeCaseMapper.getAttachmentById(id);
	}
	
	@Override
	public CloseCaseInfo getCloseCaseForBackMoney(String backMoneyPlanCode) {
		return closeCaseMapper.getCloseCaseForBackMoney(backMoneyPlanCode);
	}
	
	@Override
	public int updateCloseCaseForBackMoney(String backMoneyPlanCode) {
		return closeCaseMapper.updateCloseCaseForBackMoney(backMoneyPlanCode);
	}
	
	@Override
	public int updateStatusForAction(List<String> ids, Integer status, Integer roleType) {
		int count = 0;
		if(ids == null || ids.isEmpty()) {
			return count;
		}
		CloseCaseInfo info = null;
		Integer roleTypeTemp = roleType;
		for(String id : ids) {
			//?????????????????????????????????????????????????????????????????????
			info = closeCaseMapper.getCloseCaseById(id);
			if(info == null) {
				continue;
			}
			if(roleType == -1) {
				roleType = info.getRoleType();
			}
			//??????
			count += closeCaseMapper.commit(id, status);

			//???????????????????????????????????????????????????
			if(status == 5) {
				//???????????????????????????(??????????????????????????????)
				pendingEventService.updateCloseCasePendingEvent(info.getProjectCode(), info.getCloseCaseCode(), status, roleType);
				//??????????????????????????????????????????????????????????????????
				List<CloseCaseInfo> ccis = closeCaseMapper.getNotCommitCloseCases(id);
				if(ccis == null || ccis.isEmpty()) {
					//????????????????????????
					createCloseForBusiness(id, status);
					//????????????????????????
					pendingEventService.createCloseCasePendingEvent(info.getProjectName(), 
							info.getProjectCode(), info.getCloseCaseCode(), 0);
				}
			//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			}else {
				List<CloseCaseInfo> ccis = closeCaseMapper.getCloseCasesByIdAndStatus(id, 6);
				if(ccis != null && !ccis.isEmpty()) {
					for(CloseCaseInfo cc : ccis) {
						closeCaseMapper.delete(cc.getId());
						pendingEventService.updateCloseCasePendingEvent(cc.getProjectCode(), 
								cc.getCloseCaseCode(), cc.getStatus(), cc.getRoleType());
					}
				}
				//????????????????????????????????????????????????????????????????????????
				pendingEventService.createCloseCasePendingEvent(info.getProjectName(), 
						info.getProjectCode(), info.getCloseCaseCode(), roleType);
			}
			roleType = roleTypeTemp;
		}
		return count;
	}
	
	/**
	 * ????????????????????????
	 * @param id
	 * @param status
	 * @return
	 */
	private int createCloseForBusiness(String id, Integer status) {
		List<CloseCaseInfo> ccis = closeCaseMapper.getCloseCasesByIdAndStatus(id, status);
		Set<String> actionAreaSet = new HashSet<>();
		LocalDateTime newActionTime = null;
		String timeString = null;
		for(int i = 0; i < ccis.size(); i++) {
			CloseCaseInfo cc = ccis.get(i);
			actionAreaSet.add(cc.getActionArea());
			if(cc.getActionTime().length() > 19) {
				timeString = cc.getActionTime().substring(0, cc.getActionTime().lastIndexOf("."));
			}else {
				timeString = cc.getActionTime();
			}
			//??????????????????
			if(i == 0) {
				newActionTime = LocalDateTime.parse(timeString,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}else {
				LocalDateTime nextDate = LocalDateTime.parse(timeString,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if(nextDate.isBefore(newActionTime)) {
					newActionTime = nextDate;
				}
			}
		}
		StringBuffer newActionArea = new StringBuffer();
		if(!actionAreaSet.isEmpty()) {
			for(String s : actionAreaSet) {
				newActionArea.append(s);
				newActionArea.append(",");
			}
		}
		String newActionAreaString = null;
		if(newActionArea.length() > 0) {
			newActionAreaString = newActionArea.toString().substring(0,newActionArea.length() - 1);
		}
		CloseCaseInfo caseInfo = new CloseCaseInfo();
		caseInfo.setId(UUIDGenerator.generate());
		caseInfo.setCloseCaseCode(ccis.get(0).getCloseCaseCode());
		caseInfo.setProjectCode(ccis.get(0).getProjectCode());
		caseInfo.setProjectName(ccis.get(0).getProjectName());
		caseInfo.setContractCode(ccis.get(0).getContractCode());
		caseInfo.setContractName(ccis.get(0).getContractName());
		caseInfo.setCreaterAccount(ccis.get(0).getProjectCreaterAccount());
		caseInfo.setProjectCreaterAccount(ccis.get(0).getProjectCreaterAccount());
		caseInfo.setBackMoneyPlanCode(ccis.get(0).getBackMoneyPlanCode());
		caseInfo.setType(ccis.get(0).getType());
		caseInfo.setActionArea(newActionAreaString);
		caseInfo.setActionTime(newActionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		caseInfo.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		caseInfo.setRoleType(0);
		caseInfo.setStatus(6);
		return closeCaseMapper.save(caseInfo);
	}
	
	@Override
	public int updateStatusForBusiness(List<String> ids, Integer status) {
		int count = 0;
		if(ids != null && !ids.isEmpty()) {
			CloseCaseInfo ccinfo = null;
			for(String id : ids) {
				ccinfo = closeCaseMapper.getCloseCaseById(id);
				if(ccinfo == null) {
					continue;
				}
				//???????????????
				if(status == 7) {
					//????????????????????????????????????????????????????????????
					count += closeCaseMapper.handle(ccinfo.getProjectCode(), status, 0);
					//??????????????????????????????????????????
					closeCaseMapper.handle(ccinfo.getProjectCode(), status, 1);
					//?????????????????????????????????????????????
					pendingEventService.updateCloseCasePendingEvent(ccinfo.getProjectCode(), 
							ccinfo.getCloseCaseCode(), status, 0);
					//???????????????????????????
					pendingEventService.createCloseCasePendingEvent(ccinfo.getProjectName(), 
							ccinfo.getProjectCode(), ccinfo.getBackMoneyPlanCode(), 3);
				//???????????????
				}else {
					//???????????????????????????????????????????????????????????????
					count += closeCaseMapper.handle(ccinfo.getProjectCode(), status, 0);
					//???????????????????????????????????????
					closeCaseMapper.handle(ccinfo.getProjectCode(), 5, 1);
					//?????????????????????????????????????????????
					pendingEventService.updateCloseCasePendingEvent(ccinfo.getProjectCode(), 
							ccinfo.getBackMoneyPlanCode(), status, 3);
					//????????????????????????????????????
					pendingEventService.createCloseCasePendingEvent(ccinfo.getProjectName(), 
							ccinfo.getProjectCode(), ccinfo.getCloseCaseCode(), 0);
				}
			}
		}
		return count;
	}

	@Override
	public int uploadAttachment(List<Attachment> attachments) {
		int count = 0;
		if(attachments != null && !attachments.isEmpty()) {
			for(Attachment attachment : attachments) {
				count += closeCaseMapper.uploadAttachment(attachment);
			}
		}
		return count;
	}
	
	@Override
	public int deleteAttachments(List<String> ids) {
		int count = 0;
		if(ids != null && !ids.isEmpty()) {
			for(String id : ids) {
				if(StringUtils.isNotBlank(id)) {
					count += closeCaseMapper.deleteAttachment(id);
				}
			}
		}
		return count;
	}
	
	@Override
	public int save(CloseCaseInfo closeCaseInfo) {
		return closeCaseMapper.save(closeCaseInfo);
	}

	@Override
	public void createCloseCaseInfo(Project project, String exceptionRecordId) {
		ContractVo contract = contractService.getExecuteContract(project.getProductCode());
		BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByContractCode(contract.getContractCode());
		// ???????????????????????????????????????
		List<ProjectProductVo> projectProducts = projectProductService.getProductsOrderBy(project.getProductCode());
		
		if (projectProducts != null && projectProducts.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			String closeCaseCode = sdf.format(new Date());// ????????????
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf2.format(new Date());
			List<CloseCaseInfo> nullCloseCases = new ArrayList<>();
			for (ProjectProductVo projectProduct : projectProducts) {
				String actionTime = sdf2.format(projectProduct.getBeginTime());
				CloseCaseInfo closeCase = new CloseCaseInfo();
				closeCase.setId(UUIDGeneratorUtil.generateUUID());
				closeCase.setCloseCaseCode(closeCaseCode);
				closeCase.setProjectCode(project.getProductCode());
				closeCase.setProjectName(project.getName());
				closeCase.setContractCode(contract.getContractCode());
				closeCase.setContractName(contract.getContractName());
				closeCase.setActionTime(actionTime);
				closeCase.setBackMoneyPlanCode(backMoneyPlan.getBackMoneyPlanCode());
				
				if ("1".equals(projectProduct.getBusinessType())) {// ????????????
					closeCase.setActionArea(projectProduct.getAreaName());
					closeCase.setStatus(1);// 1????????????????????????
					closeCase.setRoleType(4);//?????????????????????
				} else if ("2".equals(projectProduct.getBusinessType())) {// ??????
					String areaNames = projectProductMenuAreaService.getAreaNamesByPPID(projectProduct.getId());
					closeCase.setActionArea(areaNames);
					closeCase.setStatus(4);// 4??????????????????
					closeCase.setRoleType(7);//???????????????
				} else if ("3".equals(projectProduct.getBusinessType())) {// ????????????
					String areaNames = projectProductMenuAreaService.getAreaNamesByPPID(projectProduct.getId());
					closeCase.setActionArea(areaNames);
					closeCase.setStatus(2);// 2????????????????????????
					closeCase.setRoleType(6);//?????????????????????
				} else if ("4".equals(projectProduct.getBusinessType())) {// ????????????
					String areaNames = projectProductMenuAreaService.getAreaNamesByPPID(projectProduct.getId());
					closeCase.setActionArea(areaNames);
					closeCase.setStatus(3);// 3????????????????????????
					closeCase.setRoleType(5);//?????????????????????
				}
				//????????????
				if (project.getExceptionStatus() == 0) {
					closeCase.setType(1);// ????????????
				} else if (project.getExceptionStatus() == 1) {
					closeCase.setType(0);// ????????????
				}
				//?????????????????????????????????????????????????????????
				if(closeCase.getType() == 0) {
					pendingEventService.updateProjectActionPendingEvent(closeCase.getProjectCode(), 
							projectProduct.getActionCode(), "", 6, closeCase.getRoleType());
				}
				
				closeCase.setProjectCreaterAccount(project.getCreaterAccount());
				closeCase.setCreateTime(createTime);
				//????????????????????????
				if(StringUtils.isBlank(projectProduct.getReceiveAccount())) {
					nullCloseCases.add(closeCase);
					continue;
				}
				closeCase.setCreaterAccount(projectProduct.getReceiveAccount());
				
				closeCaseMapper.save(closeCase);
				//?????????????????????????????????
				pendingEventService.createCloseCasePendingEvent(closeCase.getProjectName(), 
						closeCase.getProjectCode(), closeCaseCode, closeCase.getRoleType());
			}
			//?????????????????????????????????
			if(!nullCloseCases.isEmpty()) {
				Set<String> actionAreaSet = new HashSet<>();
				LocalDateTime newActionTime = null;
				String timeString = null;
				boolean isAll = false;
				//??????????????????????????????????????????
				if(nullCloseCases.size() == projectProducts.size()) {
					isAll = true;
				}
				//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				for(int i = 0; i < nullCloseCases.size(); i++) {
					CloseCaseInfo cci = nullCloseCases.get(i);
					if(isAll) {
						actionAreaSet.add(cci.getActionArea());
						if(cci.getActionTime().length() > 19) {
							timeString = cci.getActionTime().substring(0, cci.getActionTime().lastIndexOf("."));
						}else {
							timeString = cci.getActionTime();
						}
						//??????????????????
						if(i == 0) {
							newActionTime = LocalDateTime.parse(timeString,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						}else {
							LocalDateTime nextDate = LocalDateTime.parse(timeString,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
							if(nextDate.isBefore(newActionTime)) {
								newActionTime = nextDate;
							}
						}
						cci.setStatus(6);
						cci.setRoleType(0);
					}else {
						cci.setStatus(5);
						closeCaseMapper.save(cci);
					}
				}
				if(isAll) {
					StringBuffer newActionArea = new StringBuffer();
					if(!actionAreaSet.isEmpty()) {
						for(String s : actionAreaSet) {
							newActionArea.append(s);
							newActionArea.append(",");
						}
					}
					String newActionAreaString = null;
					if(newActionArea.length() > 0) {
						newActionAreaString = newActionArea.toString().substring(0, newActionArea.length() - 1);
					}
					CloseCaseInfo cInfo = nullCloseCases.get(0);
					cInfo.setCreaterAccount(cInfo.getProjectCreaterAccount());
					cInfo.setActionArea(newActionAreaString);
					cInfo.setActionTime(newActionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					closeCaseMapper.save(cInfo);
					//????????????????????????????????????
					pendingEventService.createCloseCasePendingEvent(cInfo.getProjectName(), 
							cInfo.getProjectCode(), cInfo.getCloseCaseCode(), 0);
				}
			}
			//??????????????????
			if(StringUtils.isNotBlank(exceptionRecordId)) {
				ProjectActionException pae = projectActionExceptionService.getById(exceptionRecordId);
				if(pae.getType() == 1) {
					projectActionExceptionService.updateConfirmStatus(pae.getProjectActionCode(), 1);
				}else if(pae.getType() == 2) {
					projectActionExceptionService.updateConfirmStatus(pae.getProjectCode(), 2);
				}
			}
		}
	}

	@Override
	public long countByProjectCode(String projectCode) {
		return closeCaseMapper.countByProjectCode(projectCode);
	}

	@Override
	public void changeStatusByProjectCode(String projectCode, int type) {
		closeCaseMapper.changeStatusByProjectCode(projectCode, type);
	}
}
