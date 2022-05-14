package com.hori.grms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.CloseCaseMapper;
import com.hori.grms.dao.CustomerMapper;
import com.hori.grms.dao.CustomerTurnLogMapper;
import com.hori.grms.dao.ProjectMapper;
import com.hori.grms.model.Customer;
import com.hori.grms.model.CustomerTurnLog;
import com.hori.grms.service.CustomerManagementSerivce;
import com.hori.grms.util.StringUtils;
import com.hori.grms.util.UUIDGeneratorUtil;
@Service("customerManagementSerivce")
public class CustomerManagementSerivceImpl implements CustomerManagementSerivce {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerTurnLogMapper customerTurnLogMapper;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private CloseCaseMapper closeCaseMapper;
	
	@Override
	public List<Map<String, Object>> getAll(String condition, String userAccount,String roleType) {
		//超级管理员与业务管理员能看到所有客户列表信息
		if ("-1".equals(roleType.trim()) || "1".equals(roleType)) {
			userAccount = "";
		}
		
		List<Map<String,Object>> list = customerMapper.getAll(condition,userAccount);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String province = (String) map.get("province");
				String city = (String) map.get("city");
				String district = (String) map.get("district");
				String addr = (String) map.get("address");
				map.put("address", province+city+district+addr);
			}
		}
		return list;
	}

	@Override
	public void save(Customer customer,String userAccount) {
		if (StringUtils.isNotBlank(customer.getId())) {
			customer.setUpdateTime(new Date());
			customerMapper.updateByPrimaryKeySelective(customer);
		}else{
			//创建新的客户时，创建者的账户和拥有者的账户一致,客户可能会转移给其他拥有者
			customer.setCreatorAccount(userAccount);
			customer.setId(UUIDGeneratorUtil.generateUUID());
			customer.setCreatorName(customer.getOwnerName());
			customer.setCreateTime(new Date());
			customer.setOwnerAccount(userAccount);
			customer.setIsDel("0");
			customerMapper.insert(customer);
		}
	}

	@Override
	public void deleteById(List<String> idList) {
		//customerMapper.deleteById(idList);
		//逻辑上的删除
		List<Customer> custList = new ArrayList<Customer>();
		for (String id : idList) {
			Customer customer = customerMapper.selectByPrimaryKey(id);
			customer.setIsDel("1");
			custList.add(customer);
			customerMapper.updateByPrimaryKeySelective(customer);
		}
	}

	@Override
	public Map<String, Object> fetchCustomerById(String id) {
		Map<String, Object> map = customerMapper.fetchCustomerById(id);
		String province = (String) map.get("province");
		String city = (String) map.get("city");
		String district = (String) map.get("district");
		String addr = (String) map.get("address");
		map.put("addressDetail", province+city+district+addr);
		return map;
	}

	@Override
	public List<Map<String, Object>> getExport(String condition, String userAccount, String roleType) {
		//超级管理员与业务管理员能看到所有客户列表信息
		if ("-1".equals(roleType.trim()) || "1".equals(roleType)) {
			userAccount = "";
		}
		List<Map<String, Object>> exportList = customerMapper.getExport(condition,userAccount);
		if (exportList != null && exportList.size() > 0) {
			for (Map<String, Object> map : exportList) {
				String province = (String) map.get("province");
				String city = (String) map.get("city");
				String district = (String) map.get("district");
				String addr = (String) map.get("address");
				map.put("address", province+city+district+addr);
			}
		}
		return exportList;
	}

	@Override
	public List<Map<String, Object>> getAllByName(String condition) {
		if (StringUtils.isBlank(condition)) {
			return new ArrayList<>();
		}
		return customerMapper.fetchCompanyByName(condition);
	}

	@Override
	public List<Map<String, Object>> transferInfoById(String id) {
		return customerMapper.transferInfoById(id);
	}
	@Override
	public void transferCustomer(String custId, String acceptAccount, String acceptName, String operAccount, String operName){
		Customer customer = customerMapper.selectByPrimaryKey(custId);
		//向customer_turn_log表插入一条记录
		CustomerTurnLog ctl = new CustomerTurnLog();
		ctl.setId(UUIDGeneratorUtil.generateUUID());
		ctl.setCustomerId(customer.getId());
		ctl.setAccepterName(acceptName);
		ctl.setAccepterAccount(acceptAccount);
		ctl.setFormerName(customer.getOwnerName());
		ctl.setFormerAccount(customer.getOwnerAccount());
		ctl.setCreateTime(new Date());
		ctl.setCreaterName(operName);
		ctl.setCreaterAccount(operAccount);
		customerTurnLogMapper.insert(ctl);
		//更新客户信息
		customer.setOwnerName(acceptName);
		customer.setOwnerAccount(acceptAccount);
		customer.setUpdateTime(new Date());
		customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public String projectStatus(String[] ids) {
		List<String> trueOrNot = new ArrayList<String>();
		String judge = "";
		for (String custId : ids) {
			List<Map<String,Object>> projectStatus = projectMapper.projectStatus(custId);
			StringBuffer flag = new StringBuffer("");
			List<String> projectCode = new ArrayList<String>();
			if (projectStatus != null && projectStatus.size() > 0) {
				for (Map<String, Object> map : projectStatus) {
					String status = map.get("status").toString();
					String code = (String) map.get("code");
					projectCode.add(code);
					//该客户下所有项目都删除-1、审核都不通过2、立项都终止3、都撤回4，客户可删可转移
					if ("-1".equals(status) || "3".equals(status) || "4".equals(status)) {
						flag.append("0");
					}else{
						//1表示含有审核通过的项目和待审核的项目和审核不通过的项目
						flag.append("1");
					}
				}
			}else{
				flag.append("0");
			}
			String strFlag = flag.toString();
			if (strFlag.contains("1") && !strFlag.contains("0")) {
				//待审核和审核通过，直接查看所有的项目是否都已结案，若都结案，则可以转移也可以删除，若存在没有结案的则不可转移和不可删除
				trueOrNot.add(judgeForCloseCase(projectCode));
			}else if (strFlag.contains("0") && !strFlag.contains("1")){
				//不含有审核通过的项目和待审核的项目 ，可删可转移
				trueOrNot.add("true") ;
			}else{
				//既含有审核通过的项目和待审核的项目，也含有部分项目的状态是删除-1、审核都不通过2、立项都终止3、都撤回4，客户不可删和不可转移
				trueOrNot.add("false") ;
			}
		}
		for (String string : trueOrNot) {
			if ("false".equals(string)) {
				judge = "false";
				break;
			}else{
				judge = "true";
			}
		}
		return judge;
	}

	private String judgeForCloseCase(List<String> projectCode) {
		//通过项目编号找出对应的结案状态
		List<Map<String,Integer>> statusList = closeCaseMapper.fecthStatasByProCodes(projectCode);
		String strflag = "";
		if (statusList != null && statusList.size() > 0) {
			if (statusList.size() < projectCode.size()) {
				strflag = "false";
			}else{
				boolean flag = false;
				for (Map<String, Integer> map : statusList) {
					Integer status = map.get("status");
					if (status != 8) {
						flag = true;
						break;
					}
				}
				if (flag) {
					strflag = "false";
				}else{
					strflag = "true";
				}
			}
		}else{
			strflag = "false";
		}	
		return strflag;
		}
	}
