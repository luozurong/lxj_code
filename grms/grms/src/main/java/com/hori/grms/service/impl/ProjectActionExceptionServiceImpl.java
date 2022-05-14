package com.hori.grms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hori.grms.dao.BackMoneyPlanPeriodsMapper;
import com.hori.grms.dao.FieldExchangeLogMapper;
import com.hori.grms.dao.ProjectActionExceptionMapper;
import com.hori.grms.dao.ProjectProductMapper;
import com.hori.grms.dao.ProjectProductMenuMapper;
import com.hori.grms.enums.BusinessType;
import com.hori.grms.model.BackMoneyPlan;
import com.hori.grms.model.BackMoneyPlanPeriods;
import com.hori.grms.model.FieldExchangeLog;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.model.ProjectActionException;
import com.hori.grms.model.ProjectActionExceptionExample;
import com.hori.grms.model.ProjectActionExceptionExample.Criteria;
import com.hori.grms.model.ProjectProduct;
import com.hori.grms.queryBean.PAExceptionQueryBean;
import com.hori.grms.service.BackMoneyPlanService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectActionExceptionService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.UUIDGeneratorUtil;
import com.hori.grms.vo.ProjectActionExceptionVo;

/**
 * 执行清单异常Service实现
 * 
 * @author hehj
 * @datetime 2018年8月14日 上午10:54:42
 */
@Service("projectActionExceptionService")
public class ProjectActionExceptionServiceImpl implements ProjectActionExceptionService {
	private final Logger logger = LoggerFactory.getLogger(ProjectActionExceptionServiceImpl.class);
	@Autowired
	private ProjectActionExceptionMapper projectActionExceptionMapper;
	@Autowired
	private ProjectActionService projectActionService;
	@Autowired
	private ProjectProductMenuMapper projectProductMenuMapper;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private BackMoneyPlanService backMoneyPlanService;
	@Autowired
	private BackMoneyPlanPeriodsMapper backMoneyPlanPeriodsMapper;
	@Autowired
	private ProjectProductMapper projectProductMapper;
	@Autowired
	private FieldExchangeLogMapper fieldExchangeLogMapper;
	@Autowired
	private PendingEventService pendingEventService;

	@Override
	public PageInfo<ProjectActionExceptionVo> listException(PAExceptionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<ProjectActionExceptionVo> list = projectActionExceptionMapper.listException(queryBean);

		PageInfo<ProjectActionExceptionVo> pageInfo = new PageInfo<ProjectActionExceptionVo>(list);

		return pageInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void changeConfirmStatus(String exceptionRecordId, Short confirmStatus) throws Exception {
		if (StringUtils.isBlank(exceptionRecordId) || confirmStatus == null) {
			throw new Exception("参数有误");
		}
		ProjectActionException pae = projectActionExceptionMapper.selectByPrimaryKey(exceptionRecordId);
		pae.setStatus((short) 2);
		pae.setConfirmStatus(confirmStatus);
		pae.setConfirmTime(new Date());
		projectActionExceptionMapper.updateByPrimaryKeySelective(pae);

		if (pae.getType() == 2 && StringUtils.isNotBlank(pae.getParentExceptionId())) {// 子财务异常
			// -------------------------------- 修改父异常
			// ------------------------------------------
			List<ProjectActionException> lists = projectActionExceptionMapper
					.listByParentExceptionId(pae.getParentExceptionId());
			boolean isOtherConfirm = true;
			for (ProjectActionException projectActionException : lists) {
				if (!exceptionRecordId.equals(projectActionException.getId())
						&& projectActionException.getConfirmStatus() != 1) {
					// 有子财务异常未确认
					isOtherConfirm = false;
					break;
				}
			}
			if (isOtherConfirm) {
				ProjectActionException parentException = projectActionExceptionMapper
						.selectByPrimaryKey(pae.getParentExceptionId());
				if (4 != pae.getResult()) {// 异常处理结果不是暂停
					parentException.setStatus((short) 2);// 修改父异常状态 --> 已完成
				}
				parentException.setConfirmStatus((short) 1);
				parentException.setConfirmTime(new Date());
				projectActionExceptionMapper.updateByPrimaryKeySelective(parentException);
				
				backMoneyPlanService.financeExceptionUpdateBackPlan("1",parentException.getPlanPeriodsId());
			}
		}

		ProjectAction action = projectActionService.getByCode(pae.getProjectActionCode());
		if (6 == pae.getResult() || 4 == pae.getResult()) {// 当前异常的处理结果是终止或暂停
			if (6 == pae.getResult()) {
				if (action.getActionStatus() != 5) {// 当前执行清单不是已完成
					action.setExceptionStatus(0);// 异常
					action.setActionStatus(5);// 已完成
					action.setBusinessActionStatus(2);// 业务部门状态变为已完成
					projectActionService.update(action);
				} 
				List<ProjectAction> listByProjectCode = projectActionService.listByProjectCode(action.getProjectCode());
				boolean isOtherCompleted = true;
				for (ProjectAction projectAction : listByProjectCode) {
					if (!action.getId().equals(projectAction.getId()) && projectAction.getActionStatus() != 5) {
						// 有执行清单未完成
						isOtherCompleted = false;
						break;
					}
				}
				if (isOtherCompleted) {
					Project project = projectService.getByCode(action.getProjectCode());
					project.setActionStatus((short) 2);
					project.setExceptionStatus((short) 1);
					project.setUpdateTime(new Date());
					projectService.update(project);
				}
			}
		} else {
			// 判断执行清单还有没有其他异常未处理
			boolean hasOtherException = false;
			if (pae.getType() == 1) {// 执行异常
				List<ProjectActionException> executeExceptions = this.listExceptionByActionCode(action.getActionCode(), (short) 1);
				if (executeExceptions != null && executeExceptions.size() > 0) {
					for (ProjectActionException executeException : executeExceptions) {
						if (!pae.getId().equals(executeException.getId()) && executeException.getStatus() != 2) {
							// 有其他执行异常未处理
							hasOtherException = true;
							break;
						}
						
					}
				}
				
				if (!hasOtherException) {// 没有未处理的执行异常
					List<ProjectActionException> sonMoneyExceptions = this.listExceptionByActionCode(action.getActionCode(), (short) 2);
					if (sonMoneyExceptions != null && sonMoneyExceptions.size() > 0) {// 有子财务异常
						// 执行清单关联的父财务异常的id集合
						List<String> parentMoneyExceptionIds = new ArrayList<String>();
						for (ProjectActionException sonMoneyException : sonMoneyExceptions) {
							if (sonMoneyException.getResult() == 4) {
								// 财务异常为暂停
								hasOtherException = true;
								break;
							} else if (!pae.getId().equals(sonMoneyException.getId()) && sonMoneyException.getStatus() != 2) {
								// 有其他子财务异常未处理
								hasOtherException = true;
								break;
							}
							parentMoneyExceptionIds.add(sonMoneyException.getParentExceptionId());
						}
						
						if (!hasOtherException) {// 有子财务异常，但均已处理
							// 项目的所有财务异常
							List<ProjectActionException> moneyExceptions = projectActionExceptionMapper.listParentExceptionByProjectCode(action.getProjectCode());
							
							for (ProjectActionException moneyException : moneyExceptions) {
								if (!parentMoneyExceptionIds.contains(moneyException.getId())) {
									// 项目有其他父异常
									hasOtherException = true;
									break;
								}
							}
						}
					} else {// 没有子财务异常
						List<ProjectActionException> moneyExceptions = projectActionExceptionMapper.listParentExceptionByProjectCode(action.getProjectCode());
						if (moneyExceptions != null && moneyExceptions.size() > 0) {
							for (ProjectActionException moneyException : moneyExceptions) {
								if (moneyException.getStatus() != 2) {
									// 有父财务异常未处理
									hasOtherException = true;
									break;
								}
							}
						}
					}
				}
				
			} else if (pae.getType() == 2){// 财务异常
				List<ProjectActionException> executeExceptions = this.listExceptionByActionCode(action.getActionCode(), (short) 1);
				if (executeExceptions != null && executeExceptions.size() > 0) {
					for (ProjectActionException executeException : executeExceptions) {
						if (!pae.getId().equals(executeException.getId()) && executeException.getStatus() != 2) {
							// 有执行异常未处理
							hasOtherException = true;
							break;
						}
						
					}
				}
				
				if (!hasOtherException) {// 没有未处理的执行异常
					List<ProjectActionException> sonMoneyExceptions = this.listExceptionByActionCode(action.getActionCode(), (short) 2);
					// 执行清单关联的父财务异常的id集合
					List<String> parentMoneyExceptionIds = new ArrayList<String>();
					if (sonMoneyExceptions != null && sonMoneyExceptions.size() > 0) {
						for (ProjectActionException sonMoneyException : sonMoneyExceptions) {
							if (sonMoneyException.getResult() == 4) {
								// 财务异常为暂停
								hasOtherException = true;
								break;
							} else if (!pae.getId().equals(sonMoneyException.getId()) && sonMoneyException.getStatus() != 2) {
								// 有其他子财务异常处理
								hasOtherException = true;
								break;
							}
							parentMoneyExceptionIds.add(sonMoneyException.getParentExceptionId());
						}
						
						if (!hasOtherException) {// 没有未处理的子财务异常
							// 项目的所有财务异常
							List<ProjectActionException> moneyExceptions = projectActionExceptionMapper.listParentExceptionByProjectCode(action.getProjectCode());
							
							for (ProjectActionException moneyException : moneyExceptions) {
								if (!parentMoneyExceptionIds.contains(moneyException.getId())) {
									// 项目有其他父异常
									hasOtherException = true;
									break;
								}
							}
						}
						
					}
				}
			}
			
			if (!hasOtherException) {// 当前执行清单没有任何异常（执行异常、财务异常）
				action.setExceptionStatus(1);// 异常状态变为正常
				action.setBusinessActionStatus(1);// 业务部门状态变为进行中
				projectActionService.update(action);
				
				List<ProjectAction> listByProjectCode = projectActionService.listByProjectCode(action.getProjectCode());
				boolean isOtherNormal = true;
				for (ProjectAction projectAction : listByProjectCode) {
					if (!action.getId().equals(projectAction.getId()) && projectAction.getExceptionStatus() != 1) {
						// 有执行清单有异常
						isOtherNormal = false;
						break;
					}
				}
				if (isOtherNormal) {
					Project project = projectService.getByCode(action.getProjectCode());
					project.setExceptionStatus((short) 0);
					project.setUpdateTime(new Date());
					projectService.update(project);
				}
			}
		}
		
		// 更新执行部门的待办
		int roleType = -1;
		if(BusinessType.COMMUNITY_OPT.getValue().equals(pae.getBusinessType())){//社区运营
			roleType = 4;
		} else if (BusinessType.MEDIA_OPT.getValue().equals(pae.getBusinessType())){// 媒管
			roleType = 7;
		} else if (BusinessType.USER_OPT.getValue().equals(pae.getBusinessType())){// 用户运营
			roleType = 6;
		} else if (BusinessType.MALL_OPT.getValue().equals(pae.getBusinessType())){// 电商
			roleType = 5;
		}
		logger.info("更新roleType: " + roleType + "(4为社区运营，5为电商，6为用户运营，7为媒管 )" + "projectCode: " + action.getProjectCode() + "actionCode: " + action.getActionCode() + "的待办");
		pendingEventService.updateProjectActionPendingEvent(action.getProjectCode(), action.getActionCode(), pae.getId(), pae.getResult(), roleType);
	}

	@Override
	public PageInfo<Map<String, Object>> listProjectActionExceptions(PAExceptionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<Map<String, Object>> list = projectActionExceptionMapper.listProjectActionExceptions(queryBean);

		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);

		return pageInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void insertExcuteException(ProjectActionException pae) {
		logger.info("------上报执行异常！！！------");
		projectActionExceptionMapper.insert(pae);

		ProjectAction projectAction = projectActionService.getByCode(pae.getProjectActionCode());
		projectAction.setExceptionStatus(0);
		projectActionService.update(projectAction);

		Project project = projectService.getByCode(projectAction.getProjectCode());
		project.setExceptionStatus((short) 1);
		project.setUpdateTime(new Date());
		projectService.update(project);
		
		// 调待办接口，生成业务管理员的待办
		pendingEventService.createProjectActionExceptionPendingEvent(projectAction.getProjectName(), projectAction.getProjectCode(), projectAction.getActionCode(), pae.getId(), 13, 1, null);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void stopProjectAction(String exceptionId, String resultRemark, String dealName, String dealAccount,String money)
			throws Exception {

		ProjectActionException exception = projectActionExceptionMapper.selectByPrimaryKey(exceptionId);
		// 项目执行清单编号（财务异常不与执行编号关联，而是与项目编号关联）
		String projectActionCode = exception.getProjectActionCode();
		// 项目编号
		String code = exception.getProjectCode();

		if (!exception.getStatus().equals(new Short((short) 0))) {
			throw new Exception("该异常状态无法进行操作");
		}
		// type为1是执行异常
		if (exception.getType().equals(new Short((short) 1))) {

			logger.info("异常类型为执行异常");
			
			Project project = projectService.getByCode(code);

			BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByContractCode(project.getContractCode());

			if (null == backMoneyPlan) {
				throw new Exception("项目没有建立相关收款计划，终止操作失败！！！");
			}
			
			// 生成扣款计划
			BackMoneyPlanPeriods backMoneyPlanPeriods = new BackMoneyPlanPeriods();

			backMoneyPlanPeriods.setId(UUIDGeneratorUtil.generateUUID());

			backMoneyPlanPeriods.setBackMoneyPlanCode(backMoneyPlan.getBackMoneyPlanCode());

			backMoneyPlanPeriods.setPlanBackMoney(Double.parseDouble(money));

			backMoneyPlanPeriods.setRemark(resultRemark);
			
			Date date = new Date();
			
			backMoneyPlanPeriods.setCreateTime(date);
			
			backMoneyPlanPeriods.setStatus((short) 0);

			backMoneyPlanPeriods.setType((short) 3);
	
			backMoneyPlanPeriodsMapper.insertSelective(backMoneyPlanPeriods);
			
			backMoneyPlanService.actionExceptionUpdateBackPlan("2", backMoneyPlanPeriods.getId(), money);
			
			// 生成财务待办事件
			pendingEventService.createBackMoneyPendingEventByActionException(project.getName(),
					code, backMoneyPlanPeriods.getId());
			
			ProjectAction projectAction = projectActionService.getByCode(projectActionCode);

			String projectProductId = projectAction.getProjectProductId();

			logger.info("异常所属项目的项目清单编号为：" + projectProductId);
			//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
			String businessType = exception.getBusinessType();
			//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
			short roleType = 1;
			
			if("1".equals(businessType)){
				roleType = 4;
			}else if("2".equals(businessType)){
				roleType = 7;
			}else if("3".equals(businessType)){
				roleType = 6;
			}else if("4".equals(businessType)){
				roleType = 5;
			}
			
			// 生成待办事件
			pendingEventService.createProjectActionExceptionPendingEvent(projectAction.getProjectName(),
					projectAction.getProjectCode(), projectActionCode, exceptionId, 6, roleType,exception.getDepartmentId());
			// 处理待办事件
			pendingEventService.updateProjectActionPendingEvent(projectAction.getProjectCode(), projectActionCode, exceptionId, 6, 1);
			// 释放资源
			projectProductMenuMapper.releaseResources(projectProductId);
		} else {

			logger.info("异常类型为财务异常");

			Project project = projectService.getByCode(code);
			
			List<ProjectActionException> projectActionExceptions = projectActionExceptionMapper
					.listByParentExceptionId(exception.getId());
			
			for (ProjectActionException projectActionException : projectActionExceptions) {
				if (projectActionException.getConfirmStatus().equals(new Short((short) 0))) {
					throw new Exception("财务异常结果未确定不能进行下一步操作！！");
				}
			}

			//财务异常终止操作，项目终止，改变所有执行清单状态
			projectActionService.changeByStopProject(code);
			
			//处理已操作的异常，把待确定改为已确定
			projectActionExceptionMapper.sureException(code,new Date());
			
			//把其它财务异常关闭
			projectActionExceptionMapper.closeOterException(code,exception.getId(),dealAccount,dealName,new Date());
			
			projectActionExceptions = projectActionExceptionMapper
					.listByParentExceptionId(exception.getId());
			
			if (projectActionExceptions.size() > 0) {

				// 存在子异常处理记录，不新增只修改
				for (ProjectActionException projectActionException : projectActionExceptions) {
					
					ProjectAction projectAction = projectActionService.getByCode(projectActionException.getProjectActionCode());
					
					projectActionException.setDealAccount(dealAccount);

					projectActionException.setDealName(dealName);

					projectActionException.setDealTime(exception.getDealTime());

					projectActionException.setConfirmStatus((short) 0);

					projectActionException.setStatus((short) 1);

					projectActionException.setResult((short) 6);

					projectActionException.setResultRemark(resultRemark);

					projectActionException.setConfirmTime(null);

					projectActionExceptionMapper.updateByPrimaryKey(projectActionException);
					
					//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
					String businessType = projectActionException.getBusinessType();
					//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
					short roleType = 1;
					
					if("1".equals(businessType)){
						roleType = 4;
					}else if("2".equals(businessType)){
						roleType = 7;
					}else if("3".equals(businessType)){
						roleType = 6;
					}else if("4".equals(businessType)){
						roleType = 5;
					}
					
					// 生成相关的待办事件
					pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), code,
							projectActionException.getProjectActionCode(), projectActionException.getId(), 6, roleType,projectAction.getDepartmentId());
				}

			} else {

				String projectCode = exception.getProjectCode();

				List<ProjectAction> projectActions = projectActionService.listByProjectCode(projectCode);

				// 遍历执行清单，生成对应部门的异常清单
				for (ProjectAction projectAction : projectActions) {

					ProjectActionException projectActionException = new ProjectActionException();

					ProjectProduct projectProduct = projectProductMapper
							.selectByPrimaryKey(projectAction.getProjectProductId());

					projectActionException.setBusinessType(projectProduct.getBusinessType());

					projectActionException.setCreaterAccount(exception.getCreaterAccount());

					projectActionException.setCreaterName(exception.getCreaterName());

					projectActionException.setCreateTime(exception.getCreateTime());

					projectActionException.setDealAccount(dealAccount);

					projectActionException.setDealName(dealName);

					projectActionException.setDealTime(exception.getDealTime());

					projectActionException.setDepartmentId(exception.getDepartmentId());

					projectActionException.setDepartmentName(exception.getDepartmentName());
					
					projectActionException.setConfirmStatus((short) 0);

					projectActionException.setExceptionRemark(exception.getExceptionRemark());

					projectActionException.setParentExceptionId(exceptionId);

					projectActionException.setId(UUIDGeneratorUtil.generateUUID());

					projectActionException.setProjectActionCode(projectAction.getActionCode());

					projectActionException.setProjectCode(projectCode);

					projectActionException.setResult((short) 6);

					projectActionException.setResultRemark(resultRemark);

					projectActionException.setStatus((short) 1);

					projectActionException.setType((short) 2);
					
					projectActionExceptionMapper.insert(projectActionException);
					
					//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
					String businessType = projectProduct.getBusinessType();
					//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
					short roleType = 1;
					
					if("1".equals(businessType)){
						roleType = 4;
					}else if("2".equals(businessType)){
						roleType = 7;
					}else if("3".equals(businessType)){
						roleType = 6;
					}else if("4".equals(businessType)){
						roleType = 5;
					}
					
					// 生成相关的待办事件
					pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), code,
							projectActionException.getProjectActionCode(), projectActionException.getId(), 6, roleType,projectAction.getDepartmentId());
					logger.info("执行类型为：" + projectProduct.getBusinessType() + "的相关子异常生成成功");
				}

			}
			
			String planPeriodsId = exception.getPlanPeriodsId();
			
			BackMoneyPlanPeriods backMoneyPlanPeriods = backMoneyPlanPeriodsMapper.selectByPrimaryKey(planPeriodsId);
			
			backMoneyPlanPeriods.setStatus((short) 1);
			
			backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(backMoneyPlanPeriods);
			
			backMoneyPlanService.financeExceptionUpdateBackPlan("2",planPeriodsId);
			
			// 处理待办事件
			pendingEventService.updateBackMoneyPendingEventByMoneyException(project.getProductCode(),exception.getPlanPeriodsId(),6,1);
			// type为2是财务异常，释放项目相关资源
			projectProductMenuMapper.releaseLock(code);
			
		}

		// 改变项目清单状态
		exception.setStatus((short) 1);

		exception.setDealAccount(dealAccount);

		exception.setDealName(dealName);

		exception.setResult((short) 6);

		exception.setDealTime(new Date());

		exception.setConfirmStatus((short) 0);

		exception.setResultRemark(resultRemark);
		// 更新异常信息
		projectActionExceptionMapper.updateByPrimaryKeySelective(exception);

		logger.info("异常处理成功！！！");
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void goOnProjectAction(String exceptionId, String resultRemark, String dealName, String dealAccount)
			throws Exception {

		ProjectActionException exception = projectActionExceptionMapper.selectByPrimaryKey(exceptionId);

		// 项目执行清单编号（财务异常不与执行编号关联，而是与项目编号关联）
		String projectActionCode = exception.getProjectActionCode();
		// 项目编号
		String code = exception.getProjectCode();

		Project project = projectService.getByCode(code);

		if (!exception.getStatus().equals(new Short((short) 0))) {
			throw new Exception("该异常状态无法进行操作");
		}

		// 改变项目清单状态
		exception.setStatus((short) 1);

		exception.setDealAccount(dealAccount);

		exception.setResult((short) 3);

		exception.setDealName(dealName);

		exception.setDealTime(new Date());

		exception.setConfirmStatus((short) 0);

		exception.setResultRemark(resultRemark);
		// 异常为财务异常时
		if (exception.getType() == (short) 2) {

			logger.info("异常类型为财务异常");

			List<ProjectActionException> projectActionExceptions = projectActionExceptionMapper
					.listByParentExceptionId(exception.getId());

			if (projectActionExceptions.size() > 0) {

				for (ProjectActionException projectActionException : projectActionExceptions) {
					if (projectActionException.getConfirmStatus().equals(new Short((short) 0))) {
						throw new Exception("财务异常结果未确定不能进行下一步操作！！");
					}
				}

				// 存在子异常处理记录，不新增只修改
				for (ProjectActionException projectActionException : projectActionExceptions) {
					
					ProjectAction projectAction = projectActionService.getByCode(projectActionException.getProjectActionCode());
					
					projectActionException.setDealAccount(dealAccount);

					projectActionException.setDealName(dealName);

					projectActionException.setDealTime(exception.getDealTime());

					projectActionException.setConfirmStatus((short) 0);

					projectActionException.setStatus((short) 1);

					projectActionException.setResult((short) 3);

					projectActionException.setResultRemark(resultRemark);

					projectActionException.setConfirmTime(null);

					projectActionExceptionMapper.updateByPrimaryKey(projectActionException);
					
					//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
					String businessType = projectActionException.getBusinessType();
					//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
					short roleType = 1;
					
					if("1".equals(businessType)){
						roleType = 4;
					}else if("2".equals(businessType)){
						roleType = 7;
					}else if("3".equals(businessType)){
						roleType = 6;
					}else if("4".equals(businessType)){
						roleType = 5;
					}
					
					// 生成相关的待办事件
					pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), code,
							projectActionException.getProjectActionCode(), projectActionException.getId(), 3, roleType,projectAction.getDepartmentId());
				}

			} else {

				String projectCode = exception.getProjectCode();

				List<ProjectAction> projectActions = projectActionService.listByProjectCode(projectCode);

				// 遍历执行清单，生成对应部门的异常清单
				for (ProjectAction projectAction : projectActions) {

					ProjectActionException projectActionException = new ProjectActionException();

					ProjectProduct projectProduct = projectProductMapper
							.selectByPrimaryKey(projectAction.getProjectProductId());

					projectActionException.setBusinessType(projectProduct.getBusinessType());

					projectActionException.setCreaterAccount(exception.getCreaterAccount());

					projectActionException.setCreaterName(exception.getCreaterName());

					projectActionException.setCreateTime(exception.getCreateTime());

					projectActionException.setDealAccount(dealAccount);

					projectActionException.setDealName(dealName);

					projectActionException.setDealTime(exception.getDealTime());

					projectActionException.setDepartmentId(exception.getDepartmentId());

					projectActionException.setDepartmentName(exception.getDepartmentName());
					
					projectActionException.setConfirmStatus((short) 0);

					projectActionException.setExceptionRemark(exception.getExceptionRemark());

					projectActionException.setParentExceptionId(exceptionId);

					projectActionException.setId(UUIDGeneratorUtil.generateUUID());

					projectActionException.setProjectActionCode(projectAction.getActionCode());

					projectActionException.setProjectCode(projectCode);

					projectActionException.setResult((short) 3);

					projectActionException.setResultRemark(resultRemark);

					projectActionException.setStatus((short) 1);

					projectActionException.setType((short) 2);

					projectActionExceptionMapper.insert(projectActionException);
					
					//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
					String businessType = projectProduct.getBusinessType();
					//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
					short roleType = 1;
					
					if("1".equals(businessType)){
						roleType = 4;
					}else if("2".equals(businessType)){
						roleType = 7;
					}else if("3".equals(businessType)){
						roleType = 6;
					}else if("4".equals(businessType)){
						roleType = 5;
					}
					
					// 生成相关的待办事件
					pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), code,
							projectActionException.getProjectActionCode(), projectActionException.getId(), 3, roleType,projectAction.getDepartmentId());

					logger.info("执行类型为：" + projectProduct.getBusinessType() + "的相关子异常生成成功");
				}

			}
			
			String planPeriodsId = exception.getPlanPeriodsId();
			
			BackMoneyPlanPeriods backMoneyPlanPeriods = backMoneyPlanPeriodsMapper.selectByPrimaryKey(planPeriodsId);
			
			backMoneyPlanPeriods.setStatus((short) 1);
			
			backMoneyPlanPeriodsMapper.updateByPrimaryKeySelective(backMoneyPlanPeriods);
			
			// 处理相关的待办事件
			pendingEventService.updateBackMoneyPendingEventByMoneyException(project.getProductCode(),exception.getPlanPeriodsId(),3,1);

		} else {
			
			//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
			String businessType = exception.getBusinessType();
			//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
			short roleType = 1;
			
			if("1".equals(businessType)){
				roleType = 4;
			}else if("2".equals(businessType)){
				roleType = 7;
			}else if("3".equals(businessType)){
				roleType = 6;
			}else if("4".equals(businessType)){
				roleType = 5;
			}

			// 生成相关的待办事件
			pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), code, projectActionCode,
					exceptionId, 3, roleType,exception.getDepartmentId());
			// 处理相关的待办事件
			pendingEventService.updateProjectActionPendingEvent(code, projectActionCode,
					exceptionId, 3, 1);
		}

		// 更新异常信息
		projectActionExceptionMapper.updateByPrimaryKeySelective(exception);

		logger.info("异常处理成功！！！");
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void pauseProjectAction(String exceptionId, String resultRemark, String dealName, String dealAccount)
			throws Exception {
		ProjectActionException exception = projectActionExceptionMapper.selectByPrimaryKey(exceptionId);

		String projectCode = exception.getProjectCode();

		Project project = projectService.getByCode(projectCode);

		if (!exception.getStatus().equals(new Short((short) 0))) {
			throw new Exception("该异常状态无法进行操作");
		}

		if (null!=exception.getResult()&&exception.getResult()==(short) 4) {
			throw new Exception("该异常状态处理结果已经是暂停不能重复操作");
		}

		exception.setDealAccount(dealAccount);

		exception.setResult((short) 4);

		exception.setDealName(dealName);

		exception.setDealTime(new Date());

		exception.setConfirmStatus((short) 0);

		exception.setResultRemark(resultRemark);
		
		List<ProjectAction> projectActions = projectActionService.listByProjectCode(projectCode);

		// 遍历执行清单，生成对应部门的异常清单
		for (ProjectAction projectAction : projectActions) {

			ProjectActionException projectActionException = new ProjectActionException();

			ProjectProduct projectProduct = projectProductMapper
					.selectByPrimaryKey(projectAction.getProjectProductId());

			projectActionException.setBusinessType(projectProduct.getBusinessType());

			projectActionException.setCreaterAccount(exception.getCreaterAccount());

			projectActionException.setCreaterName(exception.getCreaterName());

			projectActionException.setCreateTime(exception.getCreateTime());

			projectActionException.setDealAccount(dealAccount);

			projectActionException.setDealName(dealName);

			projectActionException.setDealTime(exception.getDealTime());

			projectActionException.setDepartmentId(exception.getDepartmentId());

			projectActionException.setDepartmentName(exception.getDepartmentName());
			
			projectActionException.setConfirmStatus((short) 0);

			projectActionException.setExceptionRemark(exception.getExceptionRemark());

			projectActionException.setParentExceptionId(exceptionId);

			projectActionException.setId(UUIDGeneratorUtil.generateUUID());

			projectActionException.setProjectActionCode(projectAction.getActionCode());

			projectActionException.setProjectCode(projectCode);

			projectActionException.setResult((short) 4);

			projectActionException.setResultRemark(resultRemark);

			projectActionException.setStatus((short) 1);

			projectActionException.setType((short) 2);

			projectActionExceptionMapper.insert(projectActionException);
			
			//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
			String businessType = projectProduct.getBusinessType();
			//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
			short roleType = 1;
			
			if("1".equals(businessType)){
				roleType = 4;
			}else if("2".equals(businessType)){
				roleType = 7;
			}else if("3".equals(businessType)){
				roleType = 6;
			}else if("4".equals(businessType)){
				roleType = 5;
			}
			
			// 生成相关的待办事件
			pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), projectCode,
					projectActionException.getProjectActionCode(), projectActionException.getId(), 4, roleType,projectAction.getDepartmentId());

			logger.info("执行类型为：" + projectProduct.getBusinessType() + "的相关子异常生成成功");
		}
		
		projectActionExceptionMapper.updateByPrimaryKey(exception);
		
		// 处理相关的待办事件
		pendingEventService.updateBackMoneyPendingEventByMoneyException(project.getProductCode(),exception.getPlanPeriodsId(),4,1);

		logger.info("异常处理成功！！！");
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deduction(String exceptionId, String resultRemark, String money, String dealName, String dealAccount)
			throws Exception {

		ProjectActionException exception = projectActionExceptionMapper.selectByPrimaryKey(exceptionId);
		// 项目执行清单编号
		String projectActionCode = exception.getProjectActionCode();

		ProjectAction projectAction = projectActionService.getByCode(projectActionCode);
		// 项目清单id
		String projectProductId = projectAction.getProjectProductId();
		// 项目编号
		String projectCode = projectAction.getProjectCode();

		Project project = projectService.getByCode(projectCode);

		BackMoneyPlan backMoneyPlan = backMoneyPlanService.getBackMoneyPlanByContractCode(project.getContractCode());

		if (null == backMoneyPlan) {
			throw new Exception("项目没有建立相关收款计划，扣款操作失败！！！");
		}

		logger.info("异常所属项目的项目清单编号为：" + projectProductId);

		// 改变项目清单状态
		exception.setStatus((short) 1);

		exception.setDealAccount(dealAccount);

		exception.setResult((short) 2);

		exception.setDealName(dealName);

		exception.setDealTime(new Date());

		exception.setConfirmStatus((short) 0);

		exception.setResultRemark(resultRemark);

		// 生成扣款计划
		BackMoneyPlanPeriods backMoneyPlanPeriods = new BackMoneyPlanPeriods();

		backMoneyPlanPeriods.setId(UUIDGeneratorUtil.generateUUID());

		backMoneyPlanPeriods.setBackMoneyPlanCode(backMoneyPlan.getBackMoneyPlanCode());

		backMoneyPlanPeriods.setPlanBackMoney(Double.parseDouble(money));

		backMoneyPlanPeriods.setRemark(resultRemark);
		
		Date date = new Date();
		
		backMoneyPlanPeriods.setCreateTime(date);
		
		backMoneyPlanPeriods.setStatus((short) 0);

		backMoneyPlanPeriods.setType((short) 3);

		// 释放资源
		projectProductMenuMapper.releaseResources(projectProductId);

		// 更新异常信息
		projectActionExceptionMapper.updateByPrimaryKeySelective(exception);
		
		backMoneyPlanPeriodsMapper.insertSelective(backMoneyPlanPeriods);
		
		backMoneyPlanService.actionExceptionUpdateBackPlan("1",backMoneyPlanPeriods.getId(),money);
		
		//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
		String businessType = exception.getBusinessType();
		//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
		short roleType = 1;
		
		if("1".equals(businessType)){
			roleType = 4;
		}else if("2".equals(businessType)){
			roleType = 7;
		}else if("3".equals(businessType)){
			roleType = 6;
		}else if("4".equals(businessType)){
			roleType = 5;
		}
		
		// 生成待办事件
		pendingEventService.createProjectActionExceptionPendingEvent(project.getName(), projectCode, projectActionCode,
				exceptionId, 2, roleType,exception.getDepartmentId());
		// 处理待办事件
		pendingEventService.updateProjectActionPendingEvent(projectCode, projectActionCode,
				exceptionId, 2, 1);
		// 生成财务待办事件
		pendingEventService.createBackMoneyPendingEventByActionException(projectAction.getProjectName(),
				projectAction.getProjectCode(), backMoneyPlanPeriods.getId());

		logger.info("异常处理成功！！！");

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void substitution(String exceptionId, ProjectProduct projectProduct, String dealName, String dealAccount, ProjectAction projectAction) {

		logger.info("对异常ID：" + exceptionId + "的异常进行场地置换操作");

		ProjectActionException exception = projectActionExceptionMapper.selectByPrimaryKey(exceptionId);

		// 项目编号
		String projectCode = projectAction.getProjectCode();

		Project project = projectService.getByCode(projectCode);

		FieldExchangeLog fieldExchangeLog = new FieldExchangeLog();

		fieldExchangeLog.setId(UUIDGeneratorUtil.generateUUID());

		fieldExchangeLog.setCreaterAccount(dealAccount);

		fieldExchangeLog.setCreaterName(dealName);

		fieldExchangeLog.setProjectActionCode(exception.getProjectActionCode());

		fieldExchangeLog.setCreateTime(projectProduct.getFieldExchangeTime());

		// 改变项目清单状态
		exception.setStatus((short) 1);

		exception.setDealAccount(dealAccount);

		exception.setResult((short) 1);

		exception.setDealName(dealName);

		exception.setConfirmStatus((short) 0);

		exception.setDealTime(new Date());

		// 更新异常信息
		projectActionExceptionMapper.updateByPrimaryKeySelective(exception);
		// 更新执行清单信息信息
		projectProductMapper.updateByPrimaryKeySelective(projectProduct);
		// 插入场地置换日志记录
		fieldExchangeLogMapper.insert(fieldExchangeLog);
		
		//'业务类型（冗余）：1社区运营 2 媒管 3用户运营 4电商运营 0财务'
		String businessType = exception.getBusinessType();
		//4为社区运营执行管理员，5为电商运营管理员，6为用户运营执行管理员，7为媒管执行管理员
		short roleType = 1;
		
		if("1".equals(businessType)){
			roleType = 4;
		}else if("2".equals(businessType)){
			roleType = 7;
		}else if("3".equals(businessType)){
			roleType = 6;
		}else if("4".equals(businessType)){
			roleType = 5;
		}
		
		//置换不同城市，要更换执行部门，更新执行清单数据，这期不做，代码保留
		//projectActionService.updatePA(projectAction);
		
		// 处理待办事件
		pendingEventService.updateProjectActionPendingEvent(projectProduct.getProjectCode(),
				exception.getProjectActionCode(), exceptionId, 1, 1);
		// 生成待办事件
		pendingEventService.createProjectActionExceptionPendingEvent(project.getName(),projectProduct.getProjectCode(),
				exception.getProjectActionCode(), exceptionId, 1, roleType,exception.getDepartmentId());

		logger.info("置换场地操作完成！！！");
	}

	@Override
	public void insertMoneyException(ProjectActionException pae) {
		// TODO Auto-generated method stub
		projectActionExceptionMapper.insert(pae);
	}

	@Override
	public List<ProjectActionException> listExceptionByActionCode(String actionCode, short type) {
		ProjectActionExceptionExample example = new ProjectActionExceptionExample();
		Criteria criteria = example.createCriteria();
		criteria.andProjectActionCodeEqualTo(actionCode);
		criteria.andTypeEqualTo(type);
		return projectActionExceptionMapper.selectByExample(example);
	}
	
	@Override
	public int updateConfirmStatus(String code, Integer type) {
		return projectActionExceptionMapper.updateConfirmStatus(code, type);
	}

	@Override
	public ProjectActionException getById(String id) {
		return projectActionExceptionMapper.selectByPrimaryKey(id);
	}
}