package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.model.User;

public interface UserService {
	/**
	 * 添加用户
	 */
	void addUser(User newUser);
	/**
	 * 查找账号是否注册过
	 * @param userAccount
	 * @return
	 */
	User findUserByAccount(String userAccount);
	/**
	 * 用户登录
	 * @param userAccount
	 * @param password
	 * @return
	 */
	User login(String userAccount, String password);
	/**
	 * 根据手机号查找所有用户
	 * @param mobile
	 * @return
	 */
	List<User> findUserByMobile(String mobile);
	/**
	 * 批量修改用户密码
	 * @param platFormUsers
	 */
	void batchUpdate(List<User> users);
	/**
	 * 更新数据
	 * @param user
	 */
	public void update(User user);
}
