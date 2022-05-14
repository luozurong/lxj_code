package com.hori.lxjsdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.Application;
import com.hori.lxjsdk.web.queryBean.ApplicationQueryBean;
import com.jlit.db.support.Page;

@Repository("applicationDao")
public class ApplicationDao extends HibernateEntityExtendDao<Application>{

	/**
	 * 创建新应用
	 * @param application
	 */
	public void addApplication(Application application) {
		this.save(application);
	}
	
	public Page seach(ApplicationQueryBean queryBean){
		StringBuffer hql = new StringBuffer();
		List<Object> conditionsValue = new ArrayList<Object>();
		int pageNum = queryBean.getPageNumber();
		Page dataPage = null;
		hql.append("from Application pm where 1=1");
		if(StringUtils.isNotBlank(queryBean.getCompany())){
			hql.append(" and pm.company like ?");
			conditionsValue.add("%" + queryBean.getCompany() + "%");
		}
		if(StringUtils.isNotBlank(queryBean.getVerifyStatus())){
			if(queryBean.getVerifyStatus().equals("0")){
				hql.append(" and (pm.verifyStatus is null or pm.verifyStatus = ?)");
			}
			else {
				hql.append(" and pm.verifyStatus = ?");
			}
			conditionsValue.add(queryBean.getVerifyStatus());
		}
		hql.append(" order by pm.createTime desc");
		dataPage= pagedQuery(hql.toString(), pageNum, 10, conditionsValue.toArray());
		return dataPage;
	}
	/**
	 * 根据条件获取应用列表（账号和审核状态）
	 * @param account
	 * @return
	 */
	public List<Application> getListByParam(String account,String verifyStatus) {
		List list = new ArrayList<Application>();
		if(verifyStatus.equals("-1")){
			String hql = "FROM Application a WHERE a.createAccount =? ";
			list = find(hql,account);
		}else{
		   String hql = "FROM Application a WHERE a.createAccount =? AND a.verifyStatus = ?";
		   list = find(hql,account,verifyStatus);
		}
		return list;
	}

	/**
	 * 查找审核通过的指定appKey的申请数量
	 * @param appKey appKey
	 * @return
	 */
	public int queryAccessAppKeyNum(String appKey) {
		String sql = "select count(*) from sdk_list where verify_status='2' and appKey=?";
		Integer query = this.getJdbcTemplate().query(sql, new ResultSetExtractor<Integer>(){
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()){
					return rs.getInt(1);
				}else{
					return 0;
				}
			}
		});
		return query;
	}

	public Application getByAppKey(String appKey) {
		String hql = "from Application where appKey=?";
		List<Application> find = this.find(hql,appKey);
		if(find.size()>0){
			return find.get(0);
		}
		return null;
	}
	
	/**
	 * 删除应用
	 */
	
}
