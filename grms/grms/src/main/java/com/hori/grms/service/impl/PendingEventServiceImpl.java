package com.hori.grms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hori.grms.dao.BackMoneyPlanPeriodsMapper;
import com.hori.grms.dao.ContractMapper;
import com.hori.grms.dao.PendingEventMapper;
import com.hori.grms.dao.ProjectMapper;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.model.PendingEvent;
import com.hori.grms.model.Project;
import com.hori.grms.queryBean.PendingEventQueryBean;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.vo.ContractVo;

/**
 * 待办事项service实现类
 * @author FWQ
 *
 */
@Service("pendingEventService")
public class PendingEventServiceImpl implements PendingEventService{
	
	Logger logger = Logger.getLogger(PendingEventServiceImpl.class);
	
	@Autowired
	private PendingEventMapper pendingEventMapper;
	@Autowired
	private BackMoneyPlanPeriodsMapper backMoneyPlanPeriodsMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ContractMapper contractMapper;
	/**
	 * 三位一体服务地址
	 */
	@Value("${grms_server_address}")
	private String grmsServerAddress;
	
	/**
	 * 待办事件列表查询
	 * @param pendingEventQueryBean
	 * @return
	 */
	@Override
	public PageInfo<Map<String, Object>> list(PendingEventQueryBean pendingEventQueryBean){
		if (pendingEventQueryBean == null) {
			return null;
		}
		
		PageHelper.startPage(pendingEventQueryBean.getPageNumber(), pendingEventQueryBean.getPageSize());
		
		List<Map<String, Object>> list = pendingEventMapper.listForPendingEvent(pendingEventQueryBean);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return pageInfo;
	}
	
	/**
	 * 新添项目模块的待办事件
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param userAccount	业务员账号
	 * @param userName		业务员名称
	 */
	@Override
	public int createProjectPendingEvent(String projectName,String projectCode,
			String userAccount,String userName){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelCode(projectCode);
		pe.setModelName("项目管理");
		pe.setStatus(0);
		pe.setRoleType(1);
		
		pe.setCreateTime(new Date());
//		pe.setCreatorName(userName);
//		pe.setCreatorAccount(userAccount);
		pe.setIsShow(1);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/project/list.html?projectCode=")
			.append(projectCode).append("&projectStatus=0").append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“项目管理”待办事件状态
	 * @param projectCode 项目编号
	 * @param status      项目管理 0待审核 1审核通过 2审核不通过 3立项终止 4撤回 5编辑
	 * @return
	 */
	@Override
	public void updateProjectPendingEvent(String projectCode,int status){
		
		PendingEvent pendingEvent = this.pendingEventMapper.selectByModelCodeAndName(projectCode, projectCode, "项目管理","");
		if(pendingEvent != null){
			if(status == 1){
				this.pendingEventMapper.deleteByCodeNameType(projectCode, projectCode,"项目管理", "", "");
				return;
			}else if(status == 3){
				//立项终止,删除该项目所有待办
				this.pendingEventMapper.deleteByCodeNameType(projectCode, "","", "", "");
				return;
			}else if(status == 4){
				this.pendingEventMapper.deleteByCodeNameType(projectCode, projectCode, "项目管理", 1+"", "");
				return;
			}else if(status == 2){
				pendingEvent.setRoleType(0);
				pendingEvent.setStatus(2);
			}else if(status == 0){
				pendingEvent.setRoleType(1);
			}else if(status == 5){
				this.pendingEventMapper.deleteByCodeNameType(projectCode, projectCode, "项目管理", 0+"", "");
				return;
			}
			
			pendingEvent.setStatus(status);
			pendingEvent.setUpdateTime(new Date());
			
			String url = "";
			StringBuffer sb = new StringBuffer();
			sb.append(this.grmsServerAddress).append("/project/list.html?projectCode=")
				.append(projectCode).append("&projectStatus=").append(status).append("&pendingParamType=1");
			url = sb.toString();
			pendingEvent.setUrl(url);
			
			this.pendingEventMapper.updateByModelCode(pendingEvent);
		}
		
	}
	
	/**
	 * 新添合同模块的待办事件
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param contractCode  合同编号
	 * @param status        合同状态（1：未提交 2：待审批-业务管理 3:待审批-合同管理 4:待审批-财务管理 
	 * 							5:审批通过 6:审批不通过 7:已完成
	 * @param roleType      0为业务员，1为业务管理员，2为合同管理员，3为财务管理员
	 * @param userAccount	业务员账号 (roleType = 0 时传入，否则传空)
	 * @param userName		业务员名称 (roleType = 0 时传入，否则传空)
	 */
	@Override
	public int createContractPendingEvent(String projectName,String projectCode,String contractCode,
			int status,int roleType,String userAccount,String userName){
		logger.info("createContractPendingEvent  projectName:::"+ projectName+" projectCode:::"
			+  projectCode+" contractCode:::"+  contractCode
			+" status:::"+  status+" roleType:::"+  roleType+" userAccount:::"+  userAccount
			+" userName:::"+  userName);
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(StringUtils.isNotBlank(projectName)?projectName:"此为框架合同，暂无关联项目");
		pe.setProjectCode(projectCode);
		pe.setModelCode(contractCode);
		pe.setModelName("合同管理");
		pe.setStatus(status);
		pe.setRoleType(roleType);
		
		pe.setCreateTime(new Date());
//		pe.setCreatorName(userName);
//		pe.setCreatorAccount(userAccount);
		pe.setIsShow(1);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		//项目编号为空为框架合同
		if(StringUtils.isNotBlank(projectCode)){
			Project project = this.projectMapper.selectByProductCode(projectCode);
			if(project != null){
				userAccountVal  = project.getCreaterAccount();
				userNameVal = project.getCreaterName();
				pe.setCreatorAccount(userAccountVal);
				pe.setCreatorName(userNameVal);
			}
		}else{
			ContractVo vo = contractMapper.selectExecuteContractByContractCode(contractCode);
			pe.setCreatorName(vo!=null?vo.getCreaterName():"");
			pe.setCreatorAccount(vo!=null?vo.getCreaterAccount():"");
		}
		
		
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/contract/contract.html?contractCode=")
			.append(contractCode).append("&contractStatus=").append(status).append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“合同管理”待办事件状态
	 * @param projectName 项目名称
	 * @param projectCode 合同编号
	 * @param status      合同管理（1：未提交 2：待审批-业务管理 3:待审批-合同管理 4:待审批-财务管理 
	 * 						5:审批通过 6:审批不通过 7:已完成 10:删除合同）
	 * @param roleType      0为业务员，1为业务管理员，2为合同管理员，3为财务管理员
	 * @return
	 */
	@Override
	public void updateContractPendingEvent(String projectCode,String contractCode,int status,int roleType){
		logger.info("updateContractPendingEvent  projectName:::"+ projectCode+" contractCode:::"+  contractCode
				+" status:::"+  status+" roleType:::"+  roleType);
		if(status!=10){
			this.pendingEventMapper.deleteByCodeNameType(projectCode, contractCode, "合同管理", roleType+"", "");
		}else{
			this.pendingEventMapper.deleteByCodeNameType(projectCode, contractCode, "合同管理", "", "");
		}
		
		
	}
	
	
	
	
	/**
	 * 新添项目执行模块的待办事件
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param projectActionCode  执行编号
	 * @param roleType	      执行角色 1为业务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 * @param departmentId 所属部门ID 社区运营时才传，否则为空
	 */
	@Override
	public int createProjectActionPendingEvent(String projectName,String projectCode,
			String projectActionCode,int roleType,String departmentId){
		logger.info("createProjectActionPendingEvent  projectName:::"+ projectName+" projectCode:::"
				+  projectCode+" projectActionCode:::"+  projectActionCode
				+" roleType:::"+  roleType+" departmentId:::"+  departmentId);
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelName("执行管理");
		pe.setModelCode(projectActionCode);
		pe.setStatus(7);
		
		pe.setRoleType(roleType);
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setDepartmentId(departmentId);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String urlCode = "";
		if(roleType == 4){
			urlCode = "/projectAction/communityActionList.html";
		}else if(roleType == 5 || roleType == 6 || roleType == 7){
			urlCode = "/projectAction/ymdActionList.html";
		}
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append(urlCode +"?projectActionCodeForPending=")
			.append(projectActionCode).append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 新添项目执行异常模块的待办事件
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param projectActionCode  执行编号
	 * @param exceptionId  项目执行异常ID
	 * @param status        执行管理 1:置换场地 2:扣款 3:继续 4:暂停 5:恢复 6终止 7执行状态-待确认 8执行状态-策划中 
								9执行状态-未执行 10执行状态-执行中 11执行状态-已完成 12项目异常-正常 13执行异常
	 * @param roleType	      执行角色 1为业务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 * @param departmentId 所属部门ID 社区运营时才传，否则为空
	 */
	@Override
	public int createProjectActionExceptionPendingEvent(String projectName,String projectCode,
			String projectActionCode,String exceptionId,int status,int roleType,String departmentId){
		logger.info("createProjectActionExceptionPendingEvent  projectName:::"+ projectName+" projectCode:::"
				+  projectCode+" projectActionCode:::"+  projectActionCode
				+" exceptionId:::"+  exceptionId
				+" status:::"+  status+" roleType:::"+  roleType+" departmentId:::"+  departmentId);
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelName("执行管理");
		pe.setModelCode(projectActionCode);
		if(roleType == 1){
			//执行部门上报异常，添加业务员待办
			pe.setStatus(13);
			
		}else if(roleType == 4 || roleType == 5 || roleType == 6 || roleType == 7){
			//执行部门在异常上报中选择了：场地置换、 扣款、 终止、继续   ；添加对应执行部门异常确认待办
			pe.setStatus(status);
		}
		pe.setRoleType(roleType);
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setExceptionId(exceptionId);
		pe.setDepartmentId(departmentId);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String urlCode = "";
		String projectActionCodeVal = "";
		if(roleType == 1){
			urlCode = "/projectAction/projectActionList.html";
			projectActionCodeVal = projectCode;
		}else if(roleType == 4){
			urlCode = "/projectAction/communityActionList.html";
			projectActionCodeVal = projectActionCode;
		}else if(roleType == 5 || roleType == 6 || roleType == 7){
			urlCode = "/projectAction/ymdActionList.html";
			projectActionCodeVal = projectActionCode;
		}
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append(urlCode +"?projectActionCodeForPending=")
			.append(projectActionCodeVal).append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	
	/**
	 * 修改“执行管理”待办事件状态
	 * @param projectCode 项目编号
	 * @param projectActionCode	 执行编号
	 * @param exceptionId  项目执行异常ID (项目执行异常 才传，否则为空)
	 * @param status      执行管理 1:置换场地 2:扣款 3:继续 4:暂停 5:恢复 6终止 7执行状态-待确认 8执行状态-策划中 
							9执行状态-未执行 10执行状态-执行中 11执行状态-已完成 12项目异常-正常 13执行异常
	 * @param roleType	      执行角色 1为业务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员					
	 */
	@Override
	public void updateProjectActionPendingEvent(String projectCode,String projectActionCode,
			String exceptionId,int status,int roleType){
		logger.info("updateProjectActionPendingEvent  projectCode:::"
				+  projectCode+" projectActionCode:::"+  projectActionCode
				+" exceptionId:::"+  exceptionId
				+" status:::"+  status+" roleType:::"+  roleType);
		PendingEvent pendingEvent = null;
		if(status == 8 || status == 9 || status == 10 || status == 11){
			//执行部门正常流程的状态上报
			pendingEvent = this.pendingEventMapper.
					selectByModelCodeAndName(projectCode,projectActionCode,"执行管理",roleType+"");
			
			if(pendingEvent != null){
				if(status == 11){
					//删除对应执行部门的执行待办
					this.pendingEventMapper.deleteByCodeNameType(projectCode, projectActionCode, "执行管理", roleType+"", "");
				}else if(status == 7 || status == 8 || status == 9  || status == 10){
					//修改对应执行部门的执行待办的状态
					pendingEvent.setStatus(status);
					pendingEvent.setUpdateTime(new Date());
					this.pendingEventMapper.updateByModelCode(pendingEvent);
				}
				
			}
		}else if(status == 1 || status == 2 || status == 3 || status == 6){
			if(roleType == 1){
				//业务部门已点确认，删除业务部门对应的待办
				this.pendingEventMapper.deleteByCodeNameType(projectCode, projectActionCode, "执行管理", "1", exceptionId);
			}else{
				/**
				 * 执行部门上报异常后，确定业务管理员的操作,
				 * 删除自己的异常待办
				 */
				this.pendingEventMapper.deleteByCodeNameType(projectCode, projectActionCode, "执行管理", roleType+"", exceptionId);
			}
			
		}
	}
	
	
	/**
	 * 新添收款模块的待办事件-审核逻辑
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param backMoneyCode  收款编号
	 * @param userAccount	业务员账号
	 * @param userName		业务员名称
	 */
	@Override
	public int createBackMoneyPendingEventByShenHe(String projectName,String projectCode,String backMoneyCode,
			String userAccount,String userName){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelCode(backMoneyCode);
		pe.setModelName("收款管理");
		pe.setStatus(0);
		pe.setRoleType(3);
		
		pe.setCreateTime(new Date());
//		pe.setCreatorName(userName);
//		pe.setCreatorAccount(userAccount);
		pe.setIsShow(1);
		pe.setExceptionId("shenHe");
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/backMoneyPlan/list.html?backMoneyCode=")
			.append(backMoneyCode).append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“收款管理”待办事件状态-审核逻辑
	 * @param projectName 项目名称
	 * @param backMoneyCode 收款编号
	 * @param status      收款管理（0待审核 1已审核 2审核不通过  3登记中-代收款 5登记中-已付款 6扣款（执行发起的）7待新建收款计划 8业务员删除）
	 * @return
	 */
	@Override
	public void updateBackMoneyPendingEventByShenHe(String projectCode,String backMoneyCode,int status){
		
		PendingEvent pendingEvent = this.pendingEventMapper.selectByProjectCodeAndRoleTypeExceptionId(projectCode,backMoneyCode,"收款管理","","shenHe");
		if(pendingEvent != null){
			if(status == 1 || status == 8){
				//已审核，删除对应的待办事件
				this.pendingEventMapper.deleteByCodeNameType(projectCode, backMoneyCode, "收款管理", "", "shenHe");
				return;
			}else if(status == 2){
				pendingEvent.setRoleType(0);
				pendingEvent.setStatus(status);
			}else if(status == 0){
				//审核不通过后，业务员修改提交 状态为0待审核 
				pendingEvent.setRoleType(3);
				pendingEvent.setStatus(status);
			}
			
			pendingEvent.setUpdateTime(new Date());
			
			this.pendingEventMapper.updateByModelCode(pendingEvent);
		}
		
	}
	
	
	/**
	 * 新添收款模块的待办事件-定时提醒
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param backMoneyPlanCode  收款计划编号(子计划ID)
	 */
	@Override
	public int createBackMoneyPendingEventByTask(String projectName,String projectCode
			,String backMoneyPlanCode){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelCode(backMoneyPlanCode);
		pe.setModelName("收款管理");
		pe.setStatus(3);
		
		pe.setRoleType(3);
		
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setExceptionId("task");
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		//通过子计划获取对应的收款主计划
		BackMoneyPlanPeriods bpp = backMoneyPlanPeriodsMapper.selectByPrimaryKey(backMoneyPlanCode);
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/backMoneyPlan/list.html?backMoneyCode=")
			.append(bpp!=null?bpp.getBackMoneyPlanCode():"").append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“收款管理”待办事件状态-定时提醒
	 * @param projectCode 项目编号
	 * @param backMoneyPlanCode 收款计划编号(子计划ID)
	 * @param status      收款管理（0 已收款）
	 * @return
	 */
	@Override
	public void updateBackMoneyPendingEventByTask(String projectCode,String backMoneyPlanCode,int status){
		
		//财务点击“收款登记”操作，要删除对应的超过提醒待办
		this.pendingEventMapper.deleteByCodeNameType(projectCode, backMoneyPlanCode, "收款管理", "", "task");
	}
	
	/**
	 * 新添收款模块的待办事件-执行异常－扣款
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param backMoneyPlanCode  收款计划编号(子计划ID) 包含执行异常的扣款和点项目终止的扣款
	 */
	@Override
	public int createBackMoneyPendingEventByActionException(String projectName,String projectCode
			,String backMoneyPlanCode){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelCode(backMoneyPlanCode);
		pe.setModelName("收款管理");
		pe.setStatus(6);
		pe.setRoleType(3);
		
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setExceptionId("actionException");
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		//通过子计划获取对应的收款主计划
		BackMoneyPlanPeriods bpp = backMoneyPlanPeriodsMapper.selectByPrimaryKey(backMoneyPlanCode);
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/backMoneyPlan/list.html?backMoneyCode=")
			.append(bpp!=null?bpp.getBackMoneyPlanCode():"").append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“收款管理”待办事件状态-执行异常－扣款
	 * @param projectName 项目名称
	 * @param backMoneyPlanCode 收款计划编号(子计划ID)
	 * @return
	 */
	@Override
	public void updateBackMoneyPendingEventByActionException(String projectCode,String backMoneyPlanCode){
		logger.info("updateBackMoneyPendingEventByActionException  projectCode:::"+  projectCode
				+" backMoneyPlanCode:::"+  backMoneyPlanCode);
		//财务点击“收款登记”操作，要删除对应的扣款提醒待办
		this.pendingEventMapper.deleteByCodeNameType(projectCode, backMoneyPlanCode, "收款管理", "", "actionException");
	}
	
	
	/**
	 * 新添收款模块的待办事件-财务收款异常
	 * 用于上报财务收款异常，新添业务管理员待办;业务管理员点处理后，生成4个执行部门待办
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param backMoneyPlanCode  收款计划编号(子计划ID)
	 * @param status        执行管理 1:置换场地 2:扣款 3:继续 4:暂停 5:恢复 6终止 7执行状态-待确认 8执行状态-策划中 
								9执行状态-未执行 10执行状态-执行中 11执行状态-已完成 12项目异常-正常 13执行异常
	 * @param roleType	      执行角色 1为业务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 * @param departmentId 所属部门ID 社区运营时才传，否则为空
	 */
	@Override
	public int createBackMoneyPendingEventByMoneyException(String projectName,String projectCode
			,String backMoneyPlanCode,int status,int roleType,String departmentId){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelCode(backMoneyPlanCode);
		pe.setModelName("执行管理");
		if(roleType == 1){
			//执行部门上报异常，添加业务员待办
			pe.setStatus(14);
			
		}else if(roleType == 4 || roleType == 5 || roleType == 6 || roleType == 7){
			//执行部门在异常上报中选择了：场地置换、 扣款、 终止、继续   ；添加对应执行部门异常确认待办
			pe.setStatus(status);
		}
		pe.setRoleType(roleType);
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setExceptionId("moneyException");
		pe.setDepartmentId(departmentId);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String urlCode = "";
		String projectActionCodeVal = "";
		if(roleType == 1){
			urlCode = "/projectAction/projectActionList.html";
			projectActionCodeVal = projectCode;
		}else if(roleType == 4){
			urlCode = "/projectAction/communityActionList.html";
			projectActionCodeVal = backMoneyPlanCode;
		}else if(roleType == 5 || roleType == 6 || roleType == 7){
			urlCode = "/projectAction/ymdActionList.html";
			projectActionCodeVal = backMoneyPlanCode;
		}
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append(urlCode +"?projectActionCodeForPending=")
			.append(projectActionCodeVal).append("&pendingParamType=1");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“收款管理”待办事件状态-财务收款异常
	 * @param projectCode 项目编号
	 * @param backMoneyPlanCode 收款计划编号(子计划ID)
	 * @param status      业务员操作状态  3:继续 4:暂停  6终止
	 * @param roleType	      执行角色 1为业务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 * @return
	 */
	@Override
	public void updateBackMoneyPendingEventByMoneyException(String projectCode,String backMoneyPlanCode,int status,int roleType){
		if(status == 3 || status == 4 || status == 6){
			
			if(roleType == 1){
				if(status != 4){
					/**
					 * 业务部门已点确认，删除业务部门对应的待办
					 * 点暂停时，不删除业务部门待办，只有做其它操作才删除
					 */
					this.pendingEventMapper.deleteByCodeNameType(projectCode, backMoneyPlanCode, "执行管理", "1", "moneyException");
				}
			}else{
				if(status != 6){
					/**
					 * 执行部门上报异常后，确定业务管理员的操作,
					 * 删除自己的异常待办
					 */
					this.pendingEventMapper.deleteByCodeNameType(projectCode, backMoneyPlanCode, "执行管理", roleType+"", "moneyException");
				}else{
					//执行部门有一个部门点了终止的确认，删除该项目所有的待办
					this.pendingEventMapper.deleteByCodeNameType(projectCode, "", "", "", "");
				}
				
				
			}
		}
		
	}
	
	
	/**
	 * 新添结案模块的待办事件
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 * @param closeCaseCode 结案编号 （roleType == 3 参数为 对应的收款计划code）
	 * @param roleType	            结案角色  0为业务员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 */
	@Override
	public int createCloseCasePendingEvent(String projectName,String projectCode,
			String closeCaseCode,int roleType){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		
		pe.setModelCode(closeCaseCode);
		if(roleType == 4){
			pe.setModelName("结案管理");
			pe.setStatus(1);
		}else if(roleType == 5){
			pe.setModelName("结案管理");
			pe.setStatus(2);
		}else if(roleType == 6){
			pe.setModelName("结案管理");
			pe.setStatus(3);
		}else if(roleType == 7){
			pe.setModelName("结案管理");
			pe.setStatus(4);
		}else if(roleType == 0){
			pe.setModelName("结案管理");
			pe.setStatus(6);
		}else if(roleType == 3){
			pe.setModelName("收款管理");
			pe.setStatus(9);
			pe.setExceptionId("closeCase");
		}
		
		pe.setRoleType(roleType);
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		if(roleType != 3){
			String url = "";
			StringBuffer sb = new StringBuffer();
			sb.append(this.grmsServerAddress).append("/closeCase/list.html?keyword=")
				.append(closeCaseCode).append("&pendingParamType=1");
			url = sb.toString();
			pe.setUrl(url);
		}else{
			//收款跳转到到款页面
			String url = "";
			StringBuffer sb = new StringBuffer();
			sb.append(this.grmsServerAddress).append("/backMoneyPlan/list.html?backMoneyCode=")
				.append(closeCaseCode).append("&pendingParamType=1");
			url = sb.toString();
			pe.setUrl(url);
		}
		
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“结案管理”待办事件状态
	 * @param projectCode 	项目编号
	 * @param closeCaseCode 结案编号（roleType == 3 参数为 对应的收款计划Code）
	 * @param status        结案管理（1：待提交社区运营 2：待提交用户运营 3：待提交电商运营 4：待提交媒管 5：已提交 
	 * 							6：待处理 9：待结案 8：已结案 ）
	 * @param roleType      0为业务员，1为业务管理员，3为财务管理员，4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
	 * @return
	 */
	@Override
	public void updateCloseCasePendingEvent(String projectCode,String closeCaseCode,int status,int roleType){
		if(roleType != 3){
			this.pendingEventMapper.deleteByCodeNameType(projectCode, closeCaseCode, "结案管理", roleType+"", "");

		}else{
			this.pendingEventMapper.deleteByCodeNameType(projectCode, closeCaseCode, "收款管理", roleType+"", "closeCase");

		}
	}
	
	
	/**
	 * 新添收款模块的待办事件-合同结束后待新建收款计划
	 * @param projectName	项目名称
	 * @param projectCode	项目编号
	 */
	@Override
	public int createBackMoneyPendingEventByContractEnd(String projectName,String projectCode){
		PendingEvent pe = new PendingEvent();
		pe.setId(UUIDGenerator.generate());
		pe.setProjectName(projectName);
		pe.setProjectCode(projectCode);
		pe.setModelName("收款管理");
		pe.setStatus(7);
		pe.setRoleType(0);
		
		pe.setCreateTime(new Date());
		pe.setIsShow(1);
		pe.setExceptionId("createNew");
		
		//通过项目编号获取创建项目的人员
		String userAccountVal = "";
		String userNameVal = "";
		Project project = this.projectMapper.selectByProductCode(projectCode);
		if(project != null){
			userAccountVal  = project.getCreaterAccount();
			userNameVal = project.getCreaterName();
		}
		pe.setCreatorAccount(userAccountVal);
		pe.setCreatorName(userNameVal);
		
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append(this.grmsServerAddress).append("/backMoneyPlan/list.html?backMoneyCode=")
			.append("").append("&pendingParamType=0");
		url = sb.toString();
		pe.setUrl(url);
		
		return this.pendingEventMapper.insert(pe);
	}
	
	/**
	 * 修改“收款管理”待办事件状态-合同结束后待新建收款计划
	 * @param projectName 项目名称
	 * @return
	 */
	@Override
	public void updateBackMoneyPendingEventByContractEnd(String projectCode){
		//业务员新建收款计划后，删除对应的待办事件
		this.pendingEventMapper.deleteByCodeNameType(projectCode, "", "收款管理", "", "createNew");
	}
}
