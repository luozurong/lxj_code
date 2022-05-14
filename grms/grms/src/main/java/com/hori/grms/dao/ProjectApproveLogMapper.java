/*
 * ProjectApproveLogMapper.java
 * Copyright(C) 20xx-2015 xxxxxx公司
 * All rights reserved.
 * -----------------------------------------------
 * 2018-08-10 Created
 */
package com.hori.grms.dao;

import com.hori.grms.model.ProjectApproveLog;
import com.hori.grms.model.ProjectApproveLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectApproveLogMapper {
    int countByExample(ProjectApproveLogExample example);

    int deleteByExample(ProjectApproveLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(ProjectApproveLog record);

    int insertSelective(ProjectApproveLog record);

    List<ProjectApproveLog> selectByExample(ProjectApproveLogExample example);

    ProjectApproveLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ProjectApproveLog record, @Param("example") ProjectApproveLogExample example);

    int updateByExample(@Param("record") ProjectApproveLog record, @Param("example") ProjectApproveLogExample example);

    int updateByPrimaryKeySelective(ProjectApproveLog record);

    int updateByPrimaryKey(ProjectApproveLog record);
    /**
     * 通过项目编码获取项目审核日志
     * @param projectCode
     * @return
     */
	List<ProjectApproveLog> findByProjectCode(String projectCode);
}