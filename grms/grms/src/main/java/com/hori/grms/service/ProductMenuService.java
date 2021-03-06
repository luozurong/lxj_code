/**
 * 
 */
package com.hori.grms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hori.grms.dao.ProductMenuMapper;
import com.hori.grms.model.ProductMenu;
import com.hori.grms.vo.project.ProductMenuVo;

/** 
 * @ClassName: ProductMenuService 
 * @Description: 产品清单
 * @author zhuqiang
 * @date 2018年8月8日 下午6:43:19 
 */
public interface ProductMenuService {

	/**
	 * 根据id查找项目菜单
	 * @param id
	 * @return
	 */
	ProductMenu findProductMenuById(String id);

	/**
	 * 点击煤管 :默认小区按钮触发事件  --筛选可用的清单操作
	 * @param organizationSeqs  小区组织机构编号拼接的字符串，用,分隔
	 * @param endTime  开始时间
	 * @param beginTime 结束时间
	 * @return
	 */
	List<Map<String, Object>> screen2Prodect(String organizationSeqs, Date beginTime, Date endTime);

	/**
	 * 煤管： 查看指定清单 的小区 在 指定时间段是否可用
	 * @param id  清单表id
	 * @param orGList 小区机构编号列表
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	List<Map<String, Object>> findAreaByTimeMeanId(String id, List<String> orGList, Date beginTime, Date endTime);

	/**
	 * 封装3,4 清单对应的剩余可创建次数
	 * @param listNums
	 * @return
	 */
	Map<String, Integer> findAddMeas(List<ProductMenuVo> listNums);

	/**
	 * 根据清单id,开始时间 查看 3，4每月的剩余次数
	 * @param meanId
	 * @param beginTime
	 * @return
	 */
	Integer findAddNumsByMeanIdAndBeginTime(String meanId, Date beginTime);

	/**
	 * 检测清单id是否:煤管类型清单是否可选
	 * @param meanId
	 * @param startTime
	 * @param endTime
	 * @param orsQ  组织机构编号，用,隔开
	 * @return
	 */
	boolean checkProduc2tByMeadId(String meanId, Date startTime, Date endTime, String orsQ);

	

   
}
