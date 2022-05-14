package com.hori.lxjsdk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hori.lxjsdk.dao.PlatFormJoinAreaDao;
import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.service.PlatFormJoinAreaService;
import com.hori.lxjsdk.web.vo.PlatFormJoinAreaVo;

@Service("platFormJoinAreaService")
public class PlatFormJoinAreaServiceImpl implements PlatFormJoinAreaService{

	@Resource 
	private PlatFormJoinAreaDao platFormJoinAreaDao;
	
	@Override
	public List<PlatFormJoinArea> findByAccount(String account) {
		return platFormJoinAreaDao.findBy("account", account);
	}

	@Override
	public PlatFormJoinArea findByAccountAndAreaCode(String account, String areaCode) {
		
		return platFormJoinAreaDao.findByAccountAndAreaCode(account, areaCode);
	}

	@Override
	public PlatFormJoinArea getById(String id) {
		return platFormJoinAreaDao.get(id);
	}

	@Override
	public PlatFormJoinAreaVo getPlatFormJoinAreaInfo(String id) {
		List<PlatFormJoinAreaVo> list = platFormJoinAreaDao.getPlatFormJoinAreaInfo(id);
		if(list !=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void update(PlatFormJoinArea formJoinArea) {
		platFormJoinAreaDao.update(formJoinArea);
	}

	@Override
	public void addPlatFormJoinArea(PlatFormJoinArea platFormJoinArea) {
		platFormJoinAreaDao.addPlatFormJoinArea(platFormJoinArea);
	}

	@Override
	public List<PlatFormJoinArea> getByPlatformJoinId(String id) {
		// TODO Auto-generated method stub
		return platFormJoinAreaDao.getByPlatformJoinId(id);
	}

}
