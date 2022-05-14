/**
 * 
 */
package com.hori.grms.service;

import java.util.List;

import com.hori.grms.model.ProjectPeople;

/** 
 * @ClassName: ProjectPeopleService 
 * @Description: 项目人员
 * @author zhuqiang
 * @date 2018年8月8日 下午7:00:21 
 */

public interface ProjectPeopleService {
	/**
	 * 通过项目Code获取项目人员表
	 * @param projectId
	 * @return
	 */
	List<ProjectPeople> findByProjectCode(String projectCode);

}
