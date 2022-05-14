package com.hori.lxjsdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.jlit.db.support.Page;

@Repository("platFormJoinDao")
public class PlatFormJoinDao extends HibernateEntityExtendDao<PlatFormJoin>{
	public Page seach(PlatFormJoinQueryBean queryBean){
		StringBuffer hql = new StringBuffer();
		List<Object> conditionsValue = new ArrayList<Object>();
		int pageNum = queryBean.getPageNumber();
		Page dataPage = null;
		hql.append("from PlatFormJoin pm where 1=1");
		if(StringUtils.isNotBlank(queryBean.getCompany())){
			hql.append(" and pm.company like ?");
			conditionsValue.add("%" + queryBean.getCompany() + "%");
		}
		if(StringUtils.isNotBlank(queryBean.getVerifyStatus())){
			if(queryBean.getVerifyStatus().equals("0")){
				hql.append(" and (pm.verifyStatus is null or pm.verifyStatus = ?)");
			}else {
				hql.append(" and pm.verifyStatus = ?");
			}
			
			conditionsValue.add(queryBean.getVerifyStatus());
		}
		hql.append(" order by pm.createTime desc");
		dataPage= pagedQuery(hql.toString(), pageNum, 10, conditionsValue.toArray());
		return dataPage;
	}
	
	public PlatFormJoin findByAccountAndPass(String account,String password){
		
		List<PlatFormJoin> list = find("from PlatFormJoin where joinAccount=? and joinPassword=?",account,password);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public PlatFormJoin findByAccount(String account) {
		
		List<PlatFormJoin> list = find("from PlatFormJoin where joinAccount=?",account);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 添加平台
	 * @param platFormJoin
	 */
	public void addPlatFormJoin(PlatFormJoin platFormJoin) {
		this.save(platFormJoin);
	}
	
	/**
	 * 根据添加账号查找平台接入信息
	 * @param creatAccount
	 * @return
	 */
	public List<PlatFormJoin> getByCreateAccount(String creatAccount) {
		// TODO Auto-generated method stub
		
		List<PlatFormJoin> list = find("from PlatFormJoin where createAccount=?",creatAccount);
		
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	} 
	
	public int isExistAccount(String account){
		String sql = "select count(*) from platform_join_list where create_account = '"+account+"' or join_account = '"+account+"'";
		List<Integer> query = this.getJdbcTemplate().query(sql, new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		});
		if(query==null||query.size()==0){
			return 0;
		}else{
			return query.get(0);
		}
	}
}
