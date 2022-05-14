package com.hori.lxjsdk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.PlatFormJoinDao;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.service.PlatFormJoinService;
import com.hori.lxjsdk.utils.RandomUtil;
import com.hori.lxjsdk.web.queryBean.PlatFormJoinQueryBean;
import com.jlit.db.support.Page;

@Service("platFormJoinService")
public class PlatFormJoinServiceImpl implements PlatFormJoinService{
	@Resource(name="platFormJoinDao")
	private PlatFormJoinDao platFormJoinDao;
	
	@Override
	public Page search(PlatFormJoinQueryBean queryBean) {
		return platFormJoinDao.seach(queryBean);
	}

	@Override
	public PlatFormJoin findByAccountAndPass(String account,String password){
		return platFormJoinDao.findByAccountAndPass(account, password);
	}

	@Override
	public PlatFormJoin getById(String id) {
		return platFormJoinDao.get(id);
	}

	@Override
	public String[] createKey() {
		String key [] = new String[2];
		boolean createFlag = true;
		key[0] = RandomUtil.generateLowerString(8);
		while(createFlag){
			if(isExistAccount(key[0]) == 0){
				createFlag = false;
			}else{
				key[0] = RandomUtil.generateLowerString(8);
			}
		}
		key[1] = RandomUtil.generateNumString(8);
		return key;
	}

	@Override
	public void update(PlatFormJoin join) {
		platFormJoinDao.update(join);
	}
	

	@Override
	public PlatFormJoin findByAccount(String account) {
		return platFormJoinDao.findByAccount(account);
	}

	@Override
	public void addPlatFormJoin(PlatFormJoin platFormJoin) {
		platFormJoinDao.addPlatFormJoin(platFormJoin);
	}

	@Override
	public List<PlatFormJoin> getByCreateAccount(String creatAccount) {
		
		return platFormJoinDao.getByCreateAccount(creatAccount);
	}

	@Override
	public int isExistAccount(String account) {
		return platFormJoinDao.isExistAccount(account);
	}
}
