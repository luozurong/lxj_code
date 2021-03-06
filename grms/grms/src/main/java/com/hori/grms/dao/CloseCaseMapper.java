package com.hori.grms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hori.grms.model.Attachment;
import com.hori.grms.model.CloseCaseInfo;
import com.hori.grms.queryBean.CloseCaseQueryBean;
import com.hori.grms.vo.CloseCaseVo;

public interface CloseCaseMapper {
	
	/**
	 * 获取结案列表
	 * @param queryBean
	 * @return
	 */
	List<CloseCaseVo> getCloseCases(CloseCaseQueryBean queryBean);
	
	/**
	 * 获取结案列表总数
	 * @param queryBean
	 * @return
	 */
	int getCloseCaseCount(CloseCaseQueryBean queryBean);
	
	/**
	 * 根据附件id获取附件
	 * @param id
	 * @return
	 */
	Attachment getAttachmentById(@Param("id")String id);
	
	/**
	 * 根据
	 * @param closeCaseId
	 * @return
	 */
	List<Attachment> getAttachmentByCloseCaseId(@Param("closeCaseId")String closeCaseId);
	
	/**
	 * 根据id获取结案
	 * @param id
	 * @return
	 */
	CloseCaseInfo getCloseCaseById(@Param("id")String id);
	
	/**
	 * 根据项目id获取结案
	 * @param projectCode
	 * @return
	 */
	List<CloseCaseInfo> getCloseCaseByProjectCode(@Param("projectCode")String projectCode);
	
	/**
	 * 根据id获取未提交结案
	 * @param id
	 * @return
	 */
	List<CloseCaseInfo> getNotCommitCloseCases(@Param("id")String id);
	
	/**
	 * 根据id，状态获取结案
	 * @param id
	 * @param status
	 * @return
	 */
	List<CloseCaseInfo> getCloseCasesByIdAndStatus(@Param("id")String id, @Param("status")Integer status);
	
	/**
	 * 根据收款计划编号获取待结案状态的结案
	 * @param backMoneyPlanCode
	 * @return
	 */
	CloseCaseInfo getCloseCaseForBackMoney(@Param("backMoneyPlanCode")String backMoneyPlanCode);
	
	/**
	 * 将待结案状态更新为已结案
	 * @param backMoneyPlanCode
	 * @return
	 */
	int updateCloseCaseForBackMoney(@Param("backMoneyPlanCode")String backMoneyPlanCode);
	
	/**
	 * 提交/撤回提交结案
	 * @param id
	 * @param status
	 * @return
	 */
	int commit(@Param("id")String id, @Param("status")Integer status);
	
	/**
	 * 处理/撤回处理结案
	 * @param projectCode
	 * @param status
	 * @param userType
	 * @return
	 */
	int handle(@Param("projectCode")String projectCode, 
			@Param("status")Integer status,
			@Param("userType")Integer userType);
	
	/**
	 * 上传附件
	 * @param attachment
	 * @return
	 */
	int uploadAttachment(Attachment attachment);
	
	/**
	 * 删除附件
	 * @param id
	 * @return
	 */
	int deleteAttachment(@Param("id")String id);
	
	/**
	 * 保存
	 * @param closeCaseInfo
	 * @return
	 */
	int save(CloseCaseInfo closeCaseInfo);

	/**
	 * 根据项目编码查找结案记录的数量
	 * @param projectCode
	 * @return
	 */
	long countByProjectCode(@Param("projectCode") String projectCode);
	
	/**
	 * 删除结案，用于执行撤回时删除业务待处理记录
	 * @param id
	 * @return
	 */
	int delete(@Param("id")String id);

	/**
	 * 根据项目编码修改结案状态
	 * @param projectCode
	 * @param type 1正常结案  0：异常结案
	 */
	void changeStatusByProjectCode(@Param("projectCode") String projectCode, @Param("type") int type);
	
	/**
	 * 根据项目编号查找其状态
	 * @param projectCode
	 * @return
	 */
	List<Map<String, Integer>> fecthStatasByProCodes(List<String> projectCode);
}
