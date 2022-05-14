package com.hori.grms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hori.grms.model.DepartmentBusinessType;
import com.hori.grms.vo.DepartmentBusinessVo;


public interface DepartmentBusinessTypeMapper {

	List<DepartmentBusinessType> getBusinessTypeList(@Param("condition")String condition);

	void delDepartmentBusinessType(String id);

	void insert(DepartmentBusinessType type);
	/**
	 * 根据业务类型查询
	 * @param businessType
	 * @return
	 */
	List<DepartmentBusinessType> findByBusinessType(@Param("businessType")String businessType);
	/**
	 * 根据城市查询负责此城市对应业务的执行部门列表
	 * @param city
	 * @param businessType
	 * @return
	 */
	List<DepartmentBusinessType> findByBusinessAreaCity(@Param("city") String city,@Param("businessType")String businessType);
	
	DepartmentBusinessType selectByPrimaryKey(String id);

	List<DepartmentBusinessVo> getBusinessVoList(@Param("condition")String condition);

	void updateByPrimaryKeySelective(DepartmentBusinessType type);

	/**
	 * 查找省级的执行部门
	 * @param businessType
	 * @return
	 */
	DepartmentBusinessType findByBusinessAreaProvince(@Param("businessType") String businessType);
	/**
	 * 查找国级的执行部门
	 * @param businessType
	 * @return
	 */
	DepartmentBusinessType findByBusinessAreaState(@Param("businessType") String businessType);

	/**
	 * 根据部门id查出部门类型
	 * @param departmentId
	 * @return
	 */
	String getDepartTypeByDepartId(String departmentId);
}