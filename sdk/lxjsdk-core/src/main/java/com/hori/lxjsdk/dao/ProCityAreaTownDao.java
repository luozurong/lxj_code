package com.hori.lxjsdk.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.ProCityAreaTown;



/**
 * 区域管理
 * @author liaowl
 *
 */
@Repository
public class ProCityAreaTownDao extends HibernateEntityExtendDao<ProCityAreaTown> {
	
	
	
	/**
	 * 通过父类parentId查出所有的数据
	 * @param parentId
	 * @return
	 */
	public List<ProCityAreaTown> findProCityAreaTownByParentId(String parentId){
		String hql="FROM ProCityAreaTown p where parentId =?";
		//return this.find(hql);
		String[] param={parentId};
		List<ProCityAreaTown> list= this.find(hql, param);
		return list;
	}
	
	
	/**
	 * 查询所有没有父ID的数据(即查询Provence)
	 * @return
	 */
	public List<ProCityAreaTown> getNoParentId(){
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT pcat FROM ProCityAreaTown pcat WHERE pcat.parentId = '0086'");
		List<Object> conditionvalues = new ArrayList<Object>();
		List<ProCityAreaTown> list = this.find(hql.toString(), conditionvalues.toArray());
		return list;
	}
	
	
}
