/**
 * 
 */
package com.hori.grms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.ProductMenuMapper;
import com.hori.grms.model.ProductMenu;
import com.hori.grms.service.ProductMenuService;
import com.hori.grms.vo.project.ProductMenuVo;

/**
 * @ClassName: ProductMenuServiceImpl
 * @Description: 产品清单服务层
 * @author zhuqiang
 * @date 2018年8月8日 下午6:44:45
 */
@Service("productMenuService")
public class ProductMenuServiceImpl implements ProductMenuService {
	@Autowired
	private ProductMenuMapper productMenuMapper;

	@Override
	public ProductMenu findProductMenuById(String id) {
		return productMenuMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Map<String, Object>> screen2Prodect(String organizationSeqs, Date beginTime, Date endTime) {
		List<Map<String, Object>> listR = new ArrayList<>();
		List<ProductMenu> list = productMenuMapper.findProductMenuList((short) 1, "2");
		

		if (StringUtils.isBlank(organizationSeqs)) {
			Map<String, Object> map = null;
			if (list != null && list.size() > 0) {
				for (ProductMenu productMenu : list) {
					map = new HashMap<>();
					map.put("meanId", productMenu.getId());
					map.put("result", true);
					listR.add(map);
				}
			}			

			return listR;
		}
		String[] oStrings = organizationSeqs.split(",");

		List<Map<String, Object>> results = productMenuMapper.filterMeauByorganizationSeqs(oStrings, beginTime,
				endTime);

		Map<String, Object> map = null;
		boolean falg = true;
		if (list != null && list.size() > 0) {
			for (ProductMenu pm : list) {

				map = new HashMap<>();
				for (int i = 0; i < results.size(); i++) {
					if (((String) results.get(i).get("menuId")).equals(pm.getId())) {
						if (((Long) results.get(i).get("count")) >= pm.getNumLimit()) {
							falg = false;
						}
						results.remove(i);
						break;
					}
				}
				map.put("meanId", pm.getId());
				map.put("result", falg);
				listR.add(map);
				falg = true;
			}
		}		
		
		ProductMenu pmg=new ProductMenu();
		pmg.setBusinessType("2");
		pmg.setNumLimit(0);
		List<ProductMenu> pms0=productMenuMapper.findListByProductMenu(pmg);
		for (ProductMenu productMenu : pms0) {
			map=new HashMap<>();
			map.put("meanId", productMenu.getId());
			map.put("result", true);
			listR.add(map);
		}
		
		
		return listR;
	}

	@Override
	public List<Map<String, Object>> findAreaByTimeMeanId(String id, List<String> orGList, Date beginTime,
			Date endTime) {
		List<Map<String, Object>> list=new ArrayList<>();
		
		ProductMenu  productMenu=productMenuMapper.selectByPrimaryKey(id);
		Map<String, Object> map=new HashMap<>();
		if(productMenu.getNumLimit()==0){
			for (String org : orGList) {
				map=new HashMap<>();
				map.put("org", org);
				map.put("result", true);
				list.add(map);
			}
			return list;
		}
		List<Map<String, Object>>  orgNums=productMenuMapper.findAreaByTimeMeanId(id, orGList, beginTime,endTime);
		
		boolean flag=true; //表示默认可选
		for (String org : orGList) {
			map=new HashMap<>();
			for (int i = 0; i < orgNums.size(); i++) {
				if(((String)orgNums.get(i).get("organizationSeq")).equals(org)){
					if(((Long)orgNums.get(i).get("count"))>=productMenu.getNumLimit()){
						flag=false;
					}
					orgNums.remove(i);
					i--;
				}
			}
			map.put("org", org);
			map.put("result",flag);
			list.add(map);
			flag=true;
		}
		
		return list;
	}

	
	@Override
	public Map<String, Integer> findAddMeas(List<ProductMenuVo> listNums) {
		Map<String, Integer>  map=new HashMap<>();
		if(listNums.size()==0) return map;
		for (ProductMenuVo pmv: listNums) {
			if(pmv.getBeginTime()!=null&&pmv.getEndTime()!=null){
				Integer numb=findAddNumsByMeanIdAndBeginTime(pmv.getId(),pmv.getBeginTime());
                map.put(pmv.getId(), numb);
			}
		}
		
		return map;
	}
	
	@Override
	public Integer findAddNumsByMeanIdAndBeginTime(String meanId,Date beginTime){
		Integer num=productMenuMapper.findNumsByMeanIdAndBeginTime(meanId, beginTime);
		if(num==null) num=0;
		return productMenuMapper.selectByPrimaryKey(meanId).getNumLimit()-num;
	}

	//检测清单id是否:煤管类型清单是否可选
	@Override
	public boolean checkProduc2tByMeadId(String meanId, Date startTime, Date endTime, String orsQ) {
		ProductMenu productMenu=productMenuMapper.selectByPrimaryKey(meanId);
		if(productMenu.getNumLimit()==0) return true;
		orsQ.split(",");
		List<String> orGList=Arrays.asList(orsQ.split(","));
		List<Map<String, Object>>  orgNums=productMenuMapper.findAreaByTimeMeanId(meanId,orGList, startTime, endTime);
			
					
		for (int i = 0; i < orgNums.size(); i++) {			
			if(((Long)orgNums.get(i).get("count"))>=productMenu.getNumLimit()){
					return false;
			}				
		}							
		return true;
	}

	

}
