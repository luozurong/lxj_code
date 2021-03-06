/**
 * 
 */
package com.hori.grms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.ProjectPeopleMapper;
import com.hori.grms.model.ProjectPeople;
import com.hori.grms.service.ProjectPeopleService;

/** 
 * @ClassName: ProjectPeopleServiceImpl 
 * @Description: 项目人员
 * @author zhuqiang
 * @date 2018年8月8日 下午7:01:08 
 */
@Service("projectPeopleService")
public class ProjectPeopleServiceImpl implements ProjectPeopleService {
	@Autowired
	private ProjectPeopleMapper projectPeopleMapper;

	@Override
	public List<ProjectPeople> findByProjectCode(String projectCode) {
		return projectPeopleMapper.findByProjectCode(projectCode);
	}
	
}
