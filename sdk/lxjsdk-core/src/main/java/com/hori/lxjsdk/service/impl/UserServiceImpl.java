package com.hori.lxjsdk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.UserDao;
import com.hori.lxjsdk.model.User;
import com.hori.lxjsdk.service.UserService;
import com.hori.lxjsdk.web.queryBean.UserQueryBean;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Resource(name="userDao")
	private UserDao userDao;

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		userDao.addUser(user);
	}

	@Override
	public User findUserByAccount(String userAccount) {
		
		return userDao.findUserByAccount(userAccount);
	}

	@Override
	public User login(String userAccount, String password) {
		
		return userDao.login(userAccount,password);
	}

	@Override
	public List<User> findUserByMobile(String mobile) {
		// TODO Auto-generated method stub
		return userDao.findUserByMobile(mobile);
	}

	@Override
	public void batchUpdate(List<User> users) {
		// TODO Auto-generated method stub
		userDao.batchUpdate(users);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		userDao.update(user);
	}
}
