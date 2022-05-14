package com.hori.lxjsdk.service;


import java.util.List;

import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.jlit.db.support.Page;

public interface PlatFormJoinService {
	/**
	 * 平台接入列表
	 * @param queryBean
	 * @return
	 */
	public Page search(PlatFormJoinQueryBean queryBean);
	
	
	/**
	 * 根据账号和密码查找
	 * @param account  账号
	 * @param password 密码
	 * @return
	 */
	public PlatFormJoin findByAccountAndPass(String account,String password);


	/**
	 * 保存记录
	 * @param join
	 */
	public void update(PlatFormJoin join);
	
	/**
	 * 估计id获取记录
	 * @param id
	 * @return
	 */
	public PlatFormJoin getById(String id);
	
	/**
	 * 生成账号和密码
	 * @return
	 */
	public String[] createKey();
	
	/**
	 * 根据账号查找
	 * @param account  账号
	 * @return
	 */
	public PlatFormJoin findByAccount(String account);
	
	/**
	 * 添加平台
	 * @param platFormJoin
	 */
	public void addPlatFormJoin(PlatFormJoin platFormJoin);

	/**
	 * 根据添加账号查找平台接入信息
	 * @param string
	 * @return
	 */
	public List<PlatFormJoin> getByCreateAccount(String creatAccount);
	
	/**
	 * 根据生成的账号判断数据库是否已存在（理论上不能生成和之前相同的账号）
	 * @param account
	 * @return
	 */
	public int isExistAccount(String account);
}
