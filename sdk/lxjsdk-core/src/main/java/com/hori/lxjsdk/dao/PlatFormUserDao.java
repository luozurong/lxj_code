package com.hori.lxjsdk.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.utils.FuzzyQueryUtils;

@Repository("platFormUserDao")
public class PlatFormUserDao  extends HibernateEntityExtendDao<PlatFormUser>{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public int queryPlatformUserNum(String platformAccount,String userId) {
		String hql = "select count(*) from PlatFormUser  where platformAccount=? and userId=?";
		List<Long> find = this.find(hql,platformAccount,userId);
		if(find.size()>0){
			return find.get(0).intValue();
		}
		return 0;
	}
	
	public void batchUpdate(final List<PlatFormUser> platFormUsers){
		
		if(platFormUsers!=null&&platFormUsers.size()>0){

			String updateSql = "update platform_user set user_name=?,mobile=?,app_oauth_account=? where id=? ";

			jdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					PlatFormUser user = platFormUsers.get(i);

					ps.setString(1,user.getUserName());
					ps.setString(2,user.getMobile());
					ps.setString(3,user.getAppOauthAccount());
					ps.setString(4,user.getId());

				}
				@Override
				public int getBatchSize() {
					return platFormUsers.size();
				}
			});
		}
	}

	public void batchSave(final List<PlatFormUser> platFormUsers){
		
		if(platFormUsers.size()==0){
			return;
		}
		
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session){
				
				for(int i=0;i<platFormUsers.size();i++){
					session.save(platFormUsers.get(i));
					if((i+1)%50==0){
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				
				return null;
			}
		});	
	}

	/**
	 * 根据第三方账号以及userId查找住户信息
	 * @param createAccount
	 * @param userId
	 * @return
	 */
	public List<PlatFormUser> queryPlatFormUsers(String createAccount, String userId) {
		String hql = "from PlatFormUser where platformAccount=? and userId=?";
		return find(hql,createAccount,userId);
	}

	/**
	 * 查找已生成的授权账号
	 * @param platformAccount 第三方应用账号
	 * @param appKey  第三方appKey
	 * @param userId  第三方userId
	 */
	public String queryAppOauthAccount(String platformAccount,String userId) {
		String hql  = " from PlatFormUser where  platformAccount=? and userId=? and appOauthAccount is not null";
		List<PlatFormUser> find = find(hql,platformAccount,userId);
		if(find.size()>0){
			return find.get(0).getAppOauthAccount();
		}
		return null;
	}

	public void deletePlatFormUser(String thridAccount, String userId, String householdSerial) {
		if(FuzzyQueryUtils.isCondition(thridAccount) 
				&& FuzzyQueryUtils.isCondition(userId)
				&& FuzzyQueryUtils.isCondition(householdSerial)){
			String hql = "delete PlatFormUser where platformAccount=? and userId=? and householdSerial=?";
			this.executeUpdate(hql, thridAccount,userId,householdSerial);
		}
	}

	public List<PlatFormUser> queryPlatFormUsers(String platformAccount, List<String> householdSerials) {
		
		String hql = "from PlatFormUser where platformAccount=? and householdSerial in ("+FuzzyQueryUtils.getIds(householdSerials)+")";
		
		return find(hql,platformAccount);
	}

	public void bindOffAppOauthAccount(String oauthAccount,String householdSerial) {
			String sql = "update platform_user set status=0,app_oauth_account=null where household_serial=? and app_oauth_account=?";
			this.jdbcTemplate.update(sql,householdSerial, oauthAccount);
	}

	public PlatFormUser queryAppOauthAccount(String platformAccount, String userId, String householdSerial) {
		if(FuzzyQueryUtils.isCondition(platformAccount) 
				&& FuzzyQueryUtils.isCondition(userId)
				&& FuzzyQueryUtils.isCondition(householdSerial)){
			String hql = "from PlatFormUser where platformAccount=? and userId=? and householdSerial=?";
			List<PlatFormUser> find = this.find(hql, platformAccount,userId,householdSerial);
			if(find.size()>0){
				return find.get(0);
			}
			return null;
		}
		return null;
	}

	public void updateAppOauthAccount(String userId, String appOauthAccount, String platFormAccount) {
		if(FuzzyQueryUtils.isCondition(userId) 
				&& FuzzyQueryUtils.isCondition(appOauthAccount)
				&& FuzzyQueryUtils.isCondition(platFormAccount)){
			String sql = "update platform_user set app_oauth_account=? where user_id=? and platform_account=?";
			this.jdbcTemplate.update(sql, appOauthAccount,userId,platFormAccount);
		}
	}
}
