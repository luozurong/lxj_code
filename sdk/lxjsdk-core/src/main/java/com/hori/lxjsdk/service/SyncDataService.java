package com.hori.lxjsdk.service;

import java.util.List;

import com.hori.lxjsdk.web.vo.PlatFormUserVo;
import com.hori.lxjsdk.web.vo.ResponseMsg;
import com.hori.lxjsdk.web.vo.SyncDataVo;

/**
 * 同步第三方平台数据service
 * @author sucs
 *
 */
public interface SyncDataService {

	/**
	 * 同步第三方平台数据(后台异步执行数据同步)
	 * @param syncDataVo
	 * @return 返回没匹配到地址的用户id
	 */
	List<PlatFormUserVo> syncData(SyncDataVo syncDataVo);
}
