package com.hori.lxjsdk.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.model.User;
import com.hori.lxjsdk.web.queryBean.UserQueryBean;

@Repository("userDao")
public class UserDao extends HibernateEntityExtendDao<User>{
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * 用户注册
	 * @param application
	 */
	public void addUser(User user) {
		this.save(user);
	}
	/**
	 * 根据账号查找用户
	 * @param userAccount
	 * @return
	 */
	public User findUserByAccount(String userAccount) {
		
		String hql = "FROM User WHERE userAccount ='"+userAccount+"'";
		List<User> list = this.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 用户登录
	 * @param userAccount
	 * @param password
	 * @return
	 */
	public User login(String userAccount, String password) {
		String hql = "FROM User WHERE userAccount ='"+userAccount+"' AND password = '"+password+"'";
		List<User> list = this.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据手机号查找所有用户
	 * @param mobile
	 * @return
	 */
	public List<User> findUserByMobile(String mobile) {
		String hql = "FROM User WHERE mobile ='"+mobile+"'";
		List<User> list = this.find(hql);
		
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	/**
	 * 批量修改用户密码
	 * @param users
	 */
	public void batchUpdate(final List<User> users) {
		if(users!=null&&users.size()>0){

			String hql = "UPDATE user SET password = ? WHERE id = ?";

			jdbcTemplate.batchUpdate(hql, new BatchPreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					User user = users.get(i);

					ps.setString(1,user.getPassword());
					ps.setString(2,user.getId());

				}
				@Override
				public int getBatchSize() {
					return users.size();
				}
			});
		}
	}
	
}
