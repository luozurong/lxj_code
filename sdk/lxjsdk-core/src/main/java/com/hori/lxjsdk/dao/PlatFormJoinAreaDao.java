package com.hori.lxjsdk.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.extend.HibernateEntityExtendDao;
import com.hori.lxjsdk.model.PlatFormJoin;
import com.hori.lxjsdk.model.PlatFormJoinArea;
import com.hori.lxjsdk.web.vo.PlatFormJoinAreaVo;

@Repository("platFormJoinAreaDao")
public class PlatFormJoinAreaDao  extends HibernateEntityExtendDao<PlatFormJoinArea>{
	
	public PlatFormJoinArea findByAccountAndAreaCode(String account, String areaCode) {
		
		List<PlatFormJoinArea> list = find("from PlatFormJoinArea where joinAccount=? and areaCode=?",account,areaCode);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	//添加关联小区
	public void addPlatFormJoinArea(PlatFormJoinArea platFormJoinArea) {
		this.save(platFormJoinArea);
	}
	
	//根据平台ID获取接入小区列表
	@SuppressWarnings("unchecked")
	public List<PlatFormJoinArea> getByPlatformJoinId(String id) {
		String hql =  "FROM PlatFormJoinArea WHERE platformJoinId = '"+id+"'";
		List<PlatFormJoinArea> list = find(hql);
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}

	public List<PlatFormJoinAreaVo> getPlatFormJoinAreaInfo(String id){
		StringBuffer hql = new StringBuffer();
		hql.append("select new com.hori.lxjsdk.web.vo.PlatFormJoinAreaVo(pj.company,pa.areaName,pa.areaAddress,"
				+ "pa.joinAccount,pa.verifyStatus,pj.verifyManager,pj.verifyAccount) "
				+ "from PlatFormJoin pj,PlatFormJoinArea pa where pj.id=pa.platformJoinId and pa.id = '"+id+"'");
		List<PlatFormJoinAreaVo> list = (List<PlatFormJoinAreaVo>) getHibernateTemplate().find(hql.toString());
		return list;
	}
}
