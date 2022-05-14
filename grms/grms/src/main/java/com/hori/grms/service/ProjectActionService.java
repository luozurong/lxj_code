/**
 * 
 */
package com.hori.grms.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.ProjectAction;
import com.hori.grms.page.PageResult;
import com.hori.grms.queryBean.ProjectActionDetailQueryBean;
import com.hori.grms.queryBean.ProjectActionQueryBean;
import com.hori.grms.queryBean.ZhUmbQueryBean;
import com.hori.grms.vo.CommunityProjectActionVo;
import com.hori.grms.vo.ProjectActionDetailVo;
import com.hori.grms.vo.ProjectActionVo;
import com.hori.grms.vo.ProjectProductMenuAreaVo;

/** 
 * @ClassName: ProjectActionService 
 * @Description: 项目执行清单
 * @author zhuqiang
 * @date 2018年8月8日 下午6:53:32 
 */
public interface ProjectActionService {

	/**
	 * 根据参数查询社区运营清单列表
	 * @param queryBean
	 * @return
	 */
	PageInfo<CommunityProjectActionVo> listCommunityAction(ProjectActionQueryBean queryBean);
	/**
	 * 获取项目执行清单
	 * @param queryBean
	 * @return
	 */
	PageInfo<Map<String, Object>> listProjectAction(ProjectActionQueryBean queryBean);

	/**
	 * 更新执行清单实体
	 * @param projectAction 
	 */
	void update(ProjectAction projectAction);
	
	/**
	 * 根据主键获取实体
	 * @param projectActionId
	 * @return
	 */
	ProjectAction getById(String projectActionId);
	
	/**
	 * 通过项目编码查询社区运营清单列表
	 * @param projectCode
	 * @return
	 */
	List<CommunityProjectActionVo> findCommunityProjectActionVoByProjectCode(String projectCode);
	
	/**
	 * 查询用户运营/媒管/电商的执行清单列表
	 * @param queryBean
	 * @return
	 */
	PageInfo<Map<String, Object>> listYMDAction(ProjectActionQueryBean queryBean);
	/**
	 * 通过执行清单编号获取
	 * @param code 执行清单编号
	 * @return
	 */
	ProjectAction getByCode(String code);
	
	/**
	 * 综合部门-查询社区列表
	 * @param qo
	 * @return
	 */
	PageResult queryPage(ProjectActionQueryBean qo);
	
	/**
	 * 综合部门-查询用户媒管电商列表
	 * @param qo
	 * @return
	 */
	PageResult queryUmbPage(ZhUmbQueryBean qo);
	/**
	 * 生成执行清单编号
	 * @return
	 */
	String generateActionCode();
	/**
	 * 根据项目编号生成各执行部门的执行清单<br>
	 * 生成的时候最多只生成一次
	 * @param projectCode 项目编号
	 * @throws InterruptedException 
	 */
	void createProjectActionsByProject(String projectCode) throws InterruptedException;
	/**
	 * 获取项目执行清单(社区运营)的小区机构编码
	 * @param actionCode 项目执行编号
	 * @return
	 */
	String getOrganizationSeqByActionCode(String actionCode);

	/**
	 * 根据项目编码获取所有项目执行清单
	 * @param projectCode
	 * @return
	 */
	List<ProjectActionVo> findProjectActionVoByProjectCode(String projectCode);

	/**
	 * 通过项目编号获取执行清单列表
	 * @param projectCode
	 * @return
	 */
	List<ProjectAction> listByProjectCode(String projectCode);
	
	/**
	 * 获取项目列表
	 * @param queryBean
	 * @return
	 */
	PageInfo<Map<String, Object>> listProject(ProjectActionQueryBean queryBean);
	
	/**
	 * 执行详情-资源列表数据
	 * @param qb
	 * @return
	 */
	PageResult queryActionData(ProjectActionDetailQueryBean qb);
	/**
	 * 执行详情-附件列表数据
	 * @param qb
	 * @return
	 */
	PageResult queryAttachmentData(ProjectActionDetailQueryBean qb);
	/**
	 * 插入附件信息
	 * @param attachment
	 */
	void insert(Attachment attachment);
	/**
	 * 获取项目产品明细小区
	 * @param projectProductMenuId
	 * @return
	 */
	List<ProjectProductMenuAreaVo> getShowAreaDataByPPMID(String projectProductMenuId);
	/**
	 * 删除附件信息
	 * @param id
	 */
	void deleteAttatment(String id);
	/**
	 * 执行详情-资源列表数据(业务部门查看)
	 * @param qb
	 * @return
	 */
	PageResult queryActionDataYW(ProjectActionDetailQueryBean qb);
	/**
	 * 执行详情-附件列表数据（整个项目，包括关联项目的和执行清单的）
	 * @param qb
	 * @return
	 */
	PageResult queryAllAttachmentData(ProjectActionDetailQueryBean qb);
	/**
	 * 综合部门-根据参数查询社区运营清单列表
	 * @param queryBean
	 * @return
	 */
	PageInfo<CommunityProjectActionVo> listZHCommunityAction(ProjectActionQueryBean queryBean);
	/**
	 * 综合部门-查询用户运营/媒管/电商的执行清单列表
	 * @param queryBean
	 * @return
	 */
	PageInfo<Map<String, Object>> listUmbAction(ProjectActionQueryBean queryBean);
	/**
	 * 查询资源列表数据
	 * @param qb
	 * @return
	 */
	List<ProjectActionDetailVo> queryActionDataList(ProjectActionDetailQueryBean qb);
	/**
	 * 根据部门id获得部门类型
	 * @param departmentId
	 * @return
	 */
	String getTypeByDeptId(String departmentId);
	/**
	 * 根据执行清单编码查出业务类型
	 * @param actionCode
	 * @return
	 */
	String getTypeByActionCode(String actionCode);
	/**
	 * 上报财务异常修改项目清单状态
	 * @param code 项目编号
	 */
	void changStateBycode(String code);
	/**
	 * 财务异常终止项目改变执行清单
	 * @param code
	 */
	void changeByStopProject(String code);
	/**
	 * 更新执行清单
	 * @param projectAction
	 */
	void updatePA(ProjectAction projectAction);

}
