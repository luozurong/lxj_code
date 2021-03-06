package com.hori.grms.service.impl;

import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.ProjectRoleMapper;
import com.hori.grms.model.ProjectRole;
import com.hori.grms.model.CustomerType;
import com.hori.grms.service.ProjectRoleService;
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {
	@Autowired
	private ProjectRoleMapper projectRoleMapper;
	
	@Override
	public List<Map<String, Object>> findProjectRoleList() {
		return projectRoleMapper.findProjectRoleList();
	}

	
	@Override
	public List<ProjectRole> getProjectRole() {
		return projectRoleMapper.selectAll();
	}
	
	@Override
	public void delAll() {
		// TODO Auto-generated method stub
		projectRoleMapper.delAll();
	}

	@Override
	public void insert(ProjectRole projectRole) {
		// TODO Auto-generated method stub
		projectRoleMapper.insert(projectRole);
	}


	@Override
	public ProjectRole getProjectRoleById(String id) {
		// TODO Auto-generated method stub
		return projectRoleMapper.selectByPrimaryKey(id);
	}


	@Override
	public void update(ProjectRole role) {
		// TODO Auto-generated method stub
		projectRoleMapper.updateByPrimaryKeySelective(role);
	}

}
