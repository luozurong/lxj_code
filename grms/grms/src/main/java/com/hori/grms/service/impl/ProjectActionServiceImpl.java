/**
 * 
 */
package com.hori.grms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hori.grms.dao.AttachmentMapper;
import com.hori.grms.dao.ContractMapper;
import com.hori.grms.dao.DepartmentBusinessTypeMapper;
import com.hori.grms.dao.ProjectActionMapper;
import com.hori.grms.dao.ProjectProductMenuAreaMapper;
import com.hori.grms.dao.ProjectProductMenuMapper;
import com.hori.grms.enums.BusinessType;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.DepartmentBusinessType;
import com.hori.grms.model.Project;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.model.ProjectActionExample;
import com.hori.grms.model.ProjectProduct;
import com.hori.grms.page.PageResult;
import com.hori.grms.queryBean.ProjectActionDetailQueryBean;
import com.hori.grms.queryBean.ProjectActionQueryBean;
import com.hori.grms.queryBean.ZhUmbQueryBean;
import com.hori.grms.service.DepartmentBusinessService;
import com.hori.grms.service.PendingEventService;
import com.hori.grms.service.ProjectActionService;
import com.hori.grms.service.ProjectProductService;
import com.hori.grms.service.ProjectService;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.vo.CommunityProjectActionVo;
import com.hori.grms.vo.ContractVo;
import com.hori.grms.vo.ProjectActionDetailVo;
import com.hori.grms.vo.ProjectActionVo;
import com.hori.grms.vo.ProjectProductMenuAreaVo;
import com.hori.grms.vo.ZhProjectActionVo;

/** 
 * @ClassName: ProjectActionServiceImpl 
 * @Description:项目执行清单
 * @author zhuqiang
 * @date 2018年8月8日 下午6:55:24 
 */
@Service("projectActionService")
public class ProjectActionServiceImpl implements ProjectActionService{
	private final static Logger logger=Logger.getLogger(ProjectActionServiceImpl.class);
	@Autowired
	private ProjectActionMapper projectActionMapper;
	@Autowired
	private ProjectProductMenuMapper projectProductMenuMapper;
	@Autowired
	private ProjectProductService projectProductService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private DepartmentBusinessService departmentBusinessService;
	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
	private ProjectProductMenuAreaMapper projectProductMenuAreaMapper;
	@Autowired
	private DepartmentBusinessTypeMapper businessTypeMapper;
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private PendingEventService pendingEventService;

	@Override
	public PageInfo<CommunityProjectActionVo> listCommunityAction(ProjectActionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<CommunityProjectActionVo> list = projectActionMapper.listCommunityAction(queryBean);
		
		PageInfo<CommunityProjectActionVo> pageInfo = new PageInfo<CommunityProjectActionVo>(list);
		
		return pageInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void update(ProjectAction action) {
		Project project = projectService.getByCode(action.getProjectCode());
		if (2 == Integer.valueOf(action.getActionStatus()) && 0 == project.getActionStatus()) {
			project.setActionStatus((short) 1);
			projectService.update(project);
		}
		if (5 == Integer.valueOf(action.getActionStatus())) {
			List<ProjectAction> listByProjectCode = this.listByProjectCode(project.getProductCode());
			boolean isOtherCompleted = true;
			for (ProjectAction projectAction : listByProjectCode) {
				if (!action.getId().equals(projectAction.getId()) && projectAction.getActionStatus() != 5) {
					isOtherCompleted = false;
				}
			}
			if (isOtherCompleted) {
				project.setActionStatus((short) 2);
				projectService.update(project);
			}
			action.setBusinessActionStatus(2);
		}
		projectActionMapper.updateByPrimaryKeySelective(action);
		
		// 调待办接口，更新执行部门的关联执行清单状态的待办
		int status = -1;
		if (2 == Integer.valueOf(action.getActionStatus())) {// 确认领取
			status = 8;
		} else if (3 == Integer.valueOf(action.getActionStatus())) {// 完成策划
			status = 9;
		} else if (4 == Integer.valueOf(action.getActionStatus())) {// 进入执行
			status = 10;
		} else if (5 == Integer.valueOf(action.getActionStatus())) {// 执行完成
			status = 11;
		}
		int roleType = -1;
		
		ProjectProduct projectProduct = projectProductService.getById(action.getProjectProductId());
		if(BusinessType.COMMUNITY_OPT.getValue().equals(projectProduct.getBusinessType())){//社区运营
			roleType = 4;
		} else if (BusinessType.MEDIA_OPT.getValue().equals(projectProduct.getBusinessType())){// 媒管
			roleType = 7;
		} else if (BusinessType.USER_OPT.getValue().equals(projectProduct.getBusinessType())){// 用户运营
			roleType = 6;
		} else if (BusinessType.MALL_OPT.getValue().equals(projectProduct.getBusinessType())){// 电商
			roleType = 5;
		}
		pendingEventService.updateProjectActionPendingEvent(action.getProjectCode(), action.getActionCode(), null, status, roleType);
	}
	
	@Override
	public PageInfo<Map<String, Object>> listProjectAction(ProjectActionQueryBean queryBean) {
		
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		
		List<Map<String, Object>> list = projectActionMapper.listProjectAction(queryBean);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return pageInfo;
	}

	@Override
	public ProjectAction getById(String projectActionId) {
		return projectActionMapper.selectByPrimaryKey(projectActionId);
	}

	@Override
	public List<CommunityProjectActionVo> findCommunityProjectActionVoByProjectCode(String projectCode) {
		return projectActionMapper.findCommunityProjectActionVoByProjectCode(projectCode);
	}

	
	@Override
	public PageInfo<Map<String, Object>> listYMDAction(ProjectActionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<Map<String, Object>> list = projectActionMapper.listYMDAction(queryBean);
		if (list == null || list.size() < 0) {
			return null;
		}
		
		for (Map<String, Object> map : list) {
			String projectProductId = (String) map.get("projectProductId");
			long areaNum = projectProductMenuMapper.getAreaNumByPPId(projectProductId);
			map.put("areaNum", areaNum);
		}
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return pageInfo;
	}

	@Override
	public ProjectAction getByCode(String code) {
		
		return projectActionMapper.getByCode(code);
	}	
	@Override
	public PageResult queryPage(ProjectActionQueryBean qb) {
		Long count = projectActionMapper.queryPageCount(qb);
		if(count<=0){
			return new PageResult(0L, Collections.EMPTY_LIST);
		}
		List<CommunityProjectActionVo> result = projectActionMapper.selectZhongheDepartList(qb);
		PageResult pageResult = new PageResult(count,result);
		return pageResult;
	}

	@Override
	public PageResult queryUmbPage(ZhUmbQueryBean qb) {
		Long count = projectActionMapper.queryCount(qb);
		if(count<=0){
			return new PageResult(0L, Collections.EMPTY_LIST);
		}
		List<ZhProjectActionVo> result = projectActionMapper.selectZhUmbList(qb);
		for (int i = 0; i < result.size(); i++) {
			long areaNum = projectProductMenuMapper.getAreaNumByPPId(result.get(i).getProjectProductId());
			result.get(i).setAreaNum(areaNum);
		}
		PageResult pageResult = new PageResult(count,result);
		return pageResult;
	}
	@Override
	public String generateActionCode() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String code=sdf.format(new Date());
		ProjectAction pa=this.getByCode(code);
		if(null==pa){
			return code;
		}else{
			return generateActionCode();
		}
	}
	@Override
	public void createProjectActionsByProject(String projectCode) throws InterruptedException {
		//判断是否已经生产执行清单，生成过了就不生成
		ProjectActionExample example=new ProjectActionExample();
		example.or().andProjectCodeEqualTo(projectCode);
		List<ProjectAction> projectActions=this.projectActionMapper.selectByExample(example);
		if(null!=projectActions && projectActions.size()>0){
			logger.info("项目："+projectCode+"已经生成了执行清单，不能重复生成");
			return ;
		}
		Project project=this.projectService.getByCode(projectCode);
		//生成执行清单，根据项目清单表数据生成
		List<ProjectProduct> projectProducts=this.projectProductService.getListByProject(projectCode);
		if(null!=projectProducts && projectProducts.size()>0){
			for(ProjectProduct pp:projectProducts){
				ProjectAction projectAction=new ProjectAction();
				projectAction.setId(UUIDGenerator.generate());
				projectAction.setProjectCode(projectCode);
				projectAction.setProjectName(project.getName());
				projectAction.setProjectProductId(pp.getId());
				//不同的项目清单，生成的执行清单指定的执行部门逻辑不同
				//社区运营的清单，需要根据区域找到对应的执行部门
				//媒管、用户、商城的清单，则根据业务找到对应的执行部门即可
				List<DepartmentBusinessType> departmentBusinessTypeList=new ArrayList<DepartmentBusinessType>();
				if(BusinessType.COMMUNITY_OPT.getValue().equals(pp.getBusinessType())){//社区运营
					if(StringUtils.isNotBlank(pp.getCity())){//城市需要有
						// 查找城市所属的执行部门
						departmentBusinessTypeList=this.departmentBusinessService.findByBusinessAreaCity(pp.getCity(), pp.getBusinessType());
						if (departmentBusinessTypeList.size() < 1) {// 城市没有对应的执行部门，查找省的执行部门
							DepartmentBusinessType provinceDBT = this.departmentBusinessService.findByBusinessAreaProvince(pp.getBusinessType());
							if (provinceDBT != null) {
								departmentBusinessTypeList.add(provinceDBT);
							} else {// 查找全国的执行部门
								DepartmentBusinessType stateDBT = this.departmentBusinessService.findByBusinessAreaState(pp.getBusinessType());
								if (stateDBT != null) {
									departmentBusinessTypeList.add(stateDBT);
								}
							}
						}
					}
					
					
				}else if (BusinessType.MEDIA_OPT.getValue().equals(pp.getBusinessType())){
					departmentBusinessTypeList=departmentBusinessService.findByBusinessType(pp.getBusinessType());
				}else if (BusinessType.USER_OPT.getValue().equals(pp.getBusinessType())){
					departmentBusinessTypeList=departmentBusinessService.findByBusinessType(pp.getBusinessType());
				}else if (BusinessType.MALL_OPT.getValue().equals(pp.getBusinessType())){
					departmentBusinessTypeList=departmentBusinessService.findByBusinessType(pp.getBusinessType());
				}
				if(null!=departmentBusinessTypeList && departmentBusinessTypeList.size()>0){
					projectAction.setDepartmentId(departmentBusinessTypeList.get(0).getDepartmentId());
					projectAction.setDepartmentName(departmentBusinessTypeList.get(0).getDepartmentName());
				}
				projectAction.setBusinessActionStatus(1);//状态：执行中
				projectAction.setActionStatus(1);//1：待确认
				String projectActionCode = this.generateActionCode();
				projectAction.setActionCode(projectActionCode);//生成执行清单编号
				projectAction.setExceptionStatus(1);//是否异常 1：正常 0：异常
				this.projectActionMapper.insert(projectAction);
				
				int roleType = -1;
				String departId = null;
				if(BusinessType.COMMUNITY_OPT.getValue().equals(pp.getBusinessType())){//社区运营
					departId = projectAction.getDepartmentId();
					roleType = 4;
				} else if (BusinessType.MEDIA_OPT.getValue().equals(pp.getBusinessType())){// 媒管
					roleType = 7;
				} else if (BusinessType.USER_OPT.getValue().equals(pp.getBusinessType())){// 用户运营
					roleType = 6;
				} else if (BusinessType.MALL_OPT.getValue().equals(pp.getBusinessType())){// 电商
					roleType = 5;
				}
				logger.info("创建执行部门的待办，roleType: "+ roleType +"(4为社区运营，5为电商，6为用户运营，7为媒管)，执行清单编号：" + projectActionCode + "");
				pendingEventService.createProjectActionPendingEvent(project.getName(), project.getProductCode(), projectActionCode, roleType, departId);
				Thread.sleep(100);
			}
		}
		
	}

	public String getOrganizationSeqByActionCode(String actionCode) {
		
		return projectActionMapper.getOrganizationSeqByActionCode(actionCode);
	}


	@Override
	public List<ProjectActionVo> findProjectActionVoByProjectCode(String projectCode) {
		return projectActionMapper.findProjectActionVoByProjectCode( projectCode);
	}


	@Override
	public List<ProjectAction> listByProjectCode(String projectCode) {
		// TODO Auto-generated method stub
		return projectActionMapper.listByProjectCode(projectCode);
	}

	@Override
	public PageInfo<Map<String, Object>> listProject(ProjectActionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		
		List<Map<String, Object>> list = projectActionMapper.listProject(queryBean);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return pageInfo;
	}

	@Override
	public PageResult queryActionData(ProjectActionDetailQueryBean qb) {
		Long count = projectActionMapper.queryDetailCount(qb);
		if(count<=0){
			return new PageResult(0L, Collections.EMPTY_LIST);
		}
		List<ProjectActionDetailVo> result = projectActionMapper.selectActionDetailList(qb);
		for (int i = 0; i < result.size(); i++) {
			long areaNum = projectProductMenuMapper.getAreaNumByPPMId(result.get(i).getProjectProductMenuId());
			result.get(i).setAreaNum(areaNum);
		}
		PageResult pageResult = new PageResult(count,result);
		return pageResult;
	}

	@Override
	public PageResult queryAttachmentData(ProjectActionDetailQueryBean qb) {
		List<Attachment> attachments = attachmentMapper.findBycorrelationId(qb.getActionCode(),3);
		Long count = (long) (attachments.size());
		PageResult pageResult = new PageResult(count,attachments);
		return pageResult;
	}

	@Override
	public void insert(Attachment attachment) {
		attachmentMapper.insert(attachment);
		
	}

	@Override
	public List<ProjectProductMenuAreaVo> getShowAreaDataByPPMID(String projectProductMenuId) {
		List<ProjectProductMenuAreaVo> lists = projectProductMenuAreaMapper.getAreaByPPMId(projectProductMenuId);
		for (int i = 0; i < lists.size(); i++) {
			lists.get(i).setPpmas(projectProductMenuAreaMapper.getAreaNamesByPPMAVo(lists.get(i))); 
			
		}
		return lists;
	}

	@Override
	public void deleteAttatment(String id) {
		attachmentMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public PageResult queryActionDataYW(ProjectActionDetailQueryBean qb) {
		/*Long count = projectActionMapper.queryDetailCount(qb);
		if(count<=0){
			return new PageResult(0L, Collections.EMPTY_LIST);
		}*/
		List<ProjectActionDetailVo> result = projectActionMapper.selectActionDetaiYWlList(qb);
		for (int i = 0; i < result.size(); i++) {
			long areaNum = projectProductMenuMapper.getAreaNumByPPMId(result.get(i).getProjectProductMenuId());
			result.get(i).setAreaNum(areaNum);
		}
		Long count = (long) (result.size());
		PageResult pageResult = new PageResult(count,result);
		return pageResult;
	}

	@Override
	public PageResult queryAllAttachmentData(ProjectActionDetailQueryBean qb) {
		//先查出项目关联的附件
		List<Attachment> aList = attachmentMapper.findBycorrelationId(qb.getProjectCode(),1);
		//再查出项目里执行清单关联的附件
		List<Attachment> aList2 = attachmentMapper.queryByProjectCode(qb.getProjectCode());
		//再查出项目合同关联的附件
		    //根据项目编号查出合同信息
			ContractVo contractVo = contractMapper.selectExecuteContractByProjectCode(qb.getProjectCode());
			//根据合同id查出项目合同附件
			List<Attachment> aList3 = attachmentMapper.findBycorrelationId(contractVo.getId(),2);
		//拼接在一起
		for (int i = 0; i < aList2.size(); i++) {
			aList.add(aList2.get(i));
		}
		for (int i = 0; i < aList3.size(); i++) {
			aList.add(aList3.get(i));
		}
		Long count = (long) (aList.size());
		PageResult pageResult = new PageResult(count,aList);
		return pageResult;
	}

	@Override
	public PageInfo<CommunityProjectActionVo> listZHCommunityAction(ProjectActionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<CommunityProjectActionVo> list = projectActionMapper.listZHCommunityAction(queryBean);
		
		PageInfo<CommunityProjectActionVo> pageInfo = new PageInfo<CommunityProjectActionVo>(list);
		
		return pageInfo;
	}

	@Override
	public PageInfo<Map<String, Object>> listUmbAction(ProjectActionQueryBean queryBean) {
		if (queryBean == null) {
			return null;
		}
		PageHelper.startPage(queryBean.getPageNumber(), queryBean.getPageSize());
		List<Map<String, Object>> list = projectActionMapper.listUmbAction(queryBean);
		if (list == null || list.size() < 0) {
			return null;
		}
		
		for (Map<String, Object> map : list) {
			String projectProductId = (String) map.get("projectProductId");
			long areaNum = projectProductMenuMapper.getAreaNumByPPId(projectProductId);
			map.put("areaNum", areaNum);
		}
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return pageInfo;
	}

	@Override
	public List<ProjectActionDetailVo> queryActionDataList(ProjectActionDetailQueryBean qb) {
		List<ProjectActionDetailVo> result = projectActionMapper.selectActionDataList(qb);
		for (int i = 0; i < result.size(); i++) {
			long areaNum = projectProductMenuMapper.getAreaNumByPPMId(result.get(i).getProjectProductMenuId());
			result.get(i).setAreaNum(areaNum);
			result.get(i).setAreas(projectProductMenuAreaMapper.getAreaNamesByPPMId(result.get(i).getProjectProductMenuId()));
		}
		return result;
	}

	@Override
	public String getTypeByDeptId(String departmentId) {
		return businessTypeMapper.getDepartTypeByDepartId(departmentId);
	}

	@Override
	public String getTypeByActionCode(String actionCode) {
		
		return this.projectActionMapper.getTypeByActionCode(actionCode);
	}

	@Override
	public void changStateBycode(String code) {
		
		projectActionMapper.changStateBycode(code);
		
	}

	@Override
	public void changeByStopProject(String code) {
		projectActionMapper.changeByStopProject(code);
	}

	@Override
	public void updatePA(ProjectAction projectAction) {
		projectActionMapper.updateByPrimaryKeySelective(projectAction);
		
	}

}
