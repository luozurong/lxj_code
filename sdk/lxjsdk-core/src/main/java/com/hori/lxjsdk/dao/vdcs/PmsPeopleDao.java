package com.hori.lxjsdk.dao.vdcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.model.PlatFormUser;
import com.hori.lxjsdk.utils.FuzzyQueryUtils;
import com.hori.lxjsdk.web.vo.PmsPeopleVo;
import com.jlit.hibernate.UUIDGeneratorUtil;

@Repository("pmsPeopleDao")
public class PmsPeopleDao {

	@Resource
	private JdbcTemplate vdcsJdbcTemplate;
	
	
	/**
	 * 根据住房序列号获取业主信息map,key为住房序列号，value为人员(pms_people)id
	 * @param householdSerials 住房序列号
	 * @return
	 */
	public Map<String,PmsPeopleVo> getProprietorPeopleMap(List<String> householdSerials){
	
		if(householdSerials==null||householdSerials.size()==0){
			return new HashMap<String,PmsPeopleVo>();
		}
		
		String ids = FuzzyQueryUtils.getIds(householdSerials);
		
		List<PmsPeopleVo> vos = vdcsJdbcTemplate.query("select id,people_id,householdSerial from pms_household_address where householdSerial in ("+ids+") and people_type=0 ", 
				new RowMapper<PmsPeopleVo>() {

			@Override
			public PmsPeopleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PmsPeopleVo rqb = new PmsPeopleVo();
			    rqb.setId(rs.getString("people_id"));
			    rqb.setHouseholdSerial(rs.getString("householdSerial"));
			    rqb.setPmsHouseholdAddressId(rs.getString("id"));
			    
				return rqb;
			}
		});
		
		Map<String,PmsPeopleVo> peopleMap=new HashMap<String,PmsPeopleVo>(vos.size());
		
		for (PmsPeopleVo pmsPeopleVo : vos) {
			peopleMap.put(pmsPeopleVo.getHouseholdSerial(), pmsPeopleVo);
		}
		 
		return peopleMap;
	}
	
	
	public List<PmsPeopleVo> getPmsPeopleList(List<String> householdSerials){
		
		if(householdSerials==null||householdSerials.size()==0){
			return new ArrayList<PmsPeopleVo>();
		}
		
		String ids = FuzzyQueryUtils.getIds(householdSerials);
		
		List<PmsPeopleVo> vos = vdcsJdbcTemplate.query("select id,people_id,householdSerial from pms_household_address where householdSerial in ("+ids+")", 
				new RowMapper<PmsPeopleVo>() {

			@Override
			public PmsPeopleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PmsPeopleVo rqb = new PmsPeopleVo();
			    rqb.setId(rs.getString("people_id"));
			    rqb.setHouseholdSerial(rs.getString("householdSerial"));
			    rqb.setPmsHouseholdAddressId(rs.getString("id"));
			    
				return rqb;
			}
		});
		 
		return vos;
	}
	
	
	public void batchSavePmsPeople(final List<PmsPeopleVo> pmsPeoples) {
		
		if(pmsPeoples.size()==0){
			return;
		}
		
		String areaCode = pmsPeoples.get(0).getAreaCode();
		
		final Map<String, Object> areaMap = vdcsJdbcTemplate.queryForMap("select area_type,street_office_info_id,address from area where organization_seq=?",new Object[]{areaCode});
		
		//因为在创建住房时会自动添加一个业主,所以业主为更新，家属为新增
		String insertSql1 = "insert into pms_people(id,peopleName,info_entering_style,contact,sex,nation,nation_num,register_address,idCard) value(?,?,?,?,?,?,?,?,?)";
		
		vdcsJdbcTemplate.batchUpdate(insertSql1, new BatchPreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				PmsPeopleVo pmsPeopleVo = pmsPeoples.get(i);
			
				String peopleName=StringUtils.isEmpty(pmsPeopleVo.getUserName())?pmsPeopleVo.getUserId():pmsPeopleVo.getUserName();
				//性别（选填），0：表示男 1：表示女 2：未知
				String sex=StringUtils.isEmpty(pmsPeopleVo.getSex())?null:pmsPeopleVo.getSex();
				//民族（选填），系统默认"汉族"
				String nation=StringUtils.isEmpty(pmsPeopleVo.getNation())?"汉族":pmsPeopleVo.getNation();
				// 民族代号（选填），系统默认"01"
				String nationCode=StringUtils.isEmpty(pmsPeopleVo.getNationCode())?"01":pmsPeopleVo.getNationCode();
				//身份证号码（选填）
				String idCard=StringUtils.isEmpty(pmsPeopleVo.getIdCard())?null:pmsPeopleVo.getIdCard();
				//户籍地址（选填），不超过200个字符
				String registerAddress=StringUtils.isEmpty(pmsPeopleVo.getRegisterAddress())?null:pmsPeopleVo.getRegisterAddress();
				
				//信息录入方式（选填），1：读卡录入，2：手工录入
				String infoEnteringType=StringUtils.isEmpty(pmsPeopleVo.getInfoEnteringType())?"2":pmsPeopleVo.getInfoEnteringType();
				
				//id
				ps.setString(1,pmsPeopleVo.getId());
				//人员姓名
				ps.setString(2,peopleName);
				//信息录入方式，1：读卡录入，2：手工录入
				ps.setString(3,infoEnteringType);
				
				ps.setString(4,pmsPeopleVo.getMobile());
				ps.setString(5,sex);
				ps.setString(6,nation);
				ps.setString(7,nationCode);
				ps.setString(8,registerAddress);
				ps.setString(9,idCard);
				
			}
			@Override
			public int getBatchSize() {
				return pmsPeoples.size();
			}
		});
		
		String insertSql2 = "insert into pms_household_address(id,live_status,people_type,people_id,address,area_type,householdSerial,organization_seq,upload_status,ryid,xyid,belong_region,street_office_info_id,region_serial,"
				+ "live_way,live_day,logout_day) value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		vdcsJdbcTemplate.batchUpdate(insertSql2,new BatchPreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException, DataAccessException {
				
				PmsPeopleVo pmsPeopleVo = pmsPeoples.get(i);
				
				//入住状态（选填），1:入住，0：注销
				String liveStatus=StringUtils.isEmpty(pmsPeopleVo.getLiveStatus())?"1":pmsPeopleVo.getLiveStatus();
				
				// 入住日期（选填），格式"yyyy-MM-dd"
				String liveDate=StringUtils.isEmpty(pmsPeopleVo.getLiveDate())?null:pmsPeopleVo.getLiveDate();
				// 居住方式（选填），0:不详,1:单身居住,2:合伙居住,3:家庭居住,4:集体居住,5:其它
				String liveWay=StringUtils.isEmpty(pmsPeopleVo.getLiveWay())?null:pmsPeopleVo.getLiveWay();
				// 注销日期（选填），格式"yyyy-MM-dd HH:mm:ss"
				String logoutTime=StringUtils.isEmpty(pmsPeopleVo.getLogoutTime())?null:pmsPeopleVo.getLogoutTime();
				
				// 更新日期（选填），格式"yyyy-MM-dd HH:mm:ss"
				String updateTime=pmsPeopleVo.getUpdateTime();
				
				
				ps.setString(1,pmsPeopleVo.getPmsHouseholdAddressId());
				//入住状态（1：已入住，0：注销）
				ps.setString(2,liveStatus);
				//人员类别(0:业主,1:业主家属,2:租客）,这里新增的都为家属，因为在创建住房时会自动添加一个业主
				ps.setString(3,"1");
				
				ps.setString(4,pmsPeopleVo.getId());
				
				ps.setString(5,pmsPeopleVo.getAddress());
				
				ps.setObject(6,areaMap.get("area_type"));
				
				ps.setString(7,pmsPeopleVo.getHouseholdSerial());
				
				ps.setString(8,pmsPeopleVo.getAreaCode());
				
				ps.setString(9,"-1");
				
				ps.setString(10,UUIDGeneratorUtil.generateUUID().substring(0, 30));
				
				ps.setString(11,pmsPeopleVo.getXyid());
				
				ps.setObject(12,areaMap.get("address"));
				
				ps.setObject(13,areaMap.get("street_office_info_id"));
				
				ps.setString(14,pmsPeopleVo.getRegionSerial());
				
				ps.setString(15,liveWay);
				ps.setString(16,liveDate);
				ps.setString(17,logoutTime);
			}
			
			@Override
			public int getBatchSize() {
				return pmsPeoples.size();
			}
		});
	}
	

	public void batchUpdatePmsPeople(final List<PmsPeopleVo> pmsPeoples) {

		if(pmsPeoples.size()==0){
			return;
		}
		
		String updateSql1 = "update pms_people set peopleName=?,contact=?,info_entering_style=?,sex=?,nation=?,nation_num=?,register_address=?,idCard=? where id=? ";

		vdcsJdbcTemplate.batchUpdate(updateSql1, new BatchPreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				PmsPeopleVo pmsPeopleVo = pmsPeoples.get(i);

				String peopleName=StringUtils.isEmpty(pmsPeopleVo.getUserName())?pmsPeopleVo.getUserId():pmsPeopleVo.getUserName();
				//性别（选填），0：表示男 1：表示女 2：未知
				String sex=StringUtils.isEmpty(pmsPeopleVo.getSex())?null:pmsPeopleVo.getSex();
				//民族（选填），系统默认"汉族"
				String nation=StringUtils.isEmpty(pmsPeopleVo.getNation())?"汉族":pmsPeopleVo.getNation();
				// 民族代号（选填），系统默认"01"
				String nationCode=StringUtils.isEmpty(pmsPeopleVo.getNationCode())?"01":pmsPeopleVo.getNationCode();
				//身份证号码（选填）
				String idCard=StringUtils.isEmpty(pmsPeopleVo.getIdCard())?null:pmsPeopleVo.getIdCard();
				//户籍地址（选填），不超过200个字符
				String registerAddress=StringUtils.isEmpty(pmsPeopleVo.getRegisterAddress())?null:pmsPeopleVo.getRegisterAddress();
				
				//信息录入方式（选填），1：读卡录入，2：手工录入
				String infoEnteringType=StringUtils.isEmpty(pmsPeopleVo.getInfoEnteringType())?"2":pmsPeopleVo.getInfoEnteringType();
				
				//人员姓名
				ps.setString(1,peopleName);
				ps.setString(2,pmsPeopleVo.getMobile());
				ps.setString(3,infoEnteringType);
				ps.setString(4,sex);
				ps.setString(5,nation);
				ps.setString(6,nationCode);
				ps.setString(7,registerAddress);
				ps.setString(8,idCard);
				//id
				ps.setString(9,pmsPeopleVo.getId());
			}
			@Override
			public int getBatchSize() {
				return pmsPeoples.size();
			}
		});
		

		String updateSql2 = "update pms_household_address set live_way=?,live_day=?,logout_day=?,live_status=? where id=? ";

		vdcsJdbcTemplate.batchUpdate(updateSql2, new BatchPreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				PmsPeopleVo pmsPeopleVo = pmsPeoples.get(i);

				//入住状态（选填），1:入住，0：注销
				String liveStatus=StringUtils.isEmpty(pmsPeopleVo.getLiveStatus())?"1":pmsPeopleVo.getLiveStatus();
				
				// 入住日期（选填），格式"yyyy-MM-dd"
				String liveDate=StringUtils.isEmpty(pmsPeopleVo.getLiveDate())?null:pmsPeopleVo.getLiveDate();
				// 居住方式（选填），0:不详,1:单身居住,2:合伙居住,3:家庭居住,4:集体居住,5:其它
				String liveWay=StringUtils.isEmpty(pmsPeopleVo.getLiveWay())?null:pmsPeopleVo.getLiveWay();
				// 注销日期（选填），格式"yyyy-MM-dd HH:mm:ss"
				String logoutTime=StringUtils.isEmpty(pmsPeopleVo.getLogoutTime())?null:pmsPeopleVo.getLogoutTime();
				
				//人员姓名
				ps.setString(1,liveWay);
				ps.setString(2,liveDate);
				ps.setString(3,logoutTime);
				ps.setString(4,liveStatus);

				ps.setString(5,pmsPeopleVo.getPmsHouseholdAddressId());
			}
			@Override
			public int getBatchSize() {
				return pmsPeoples.size();
			}
		});
	}


	public void deletePmsPeople(List<String> pmsPeopleIds, List<String> pmsHouseholdAddressIdIds) {

		if(pmsPeopleIds.size()>0){
			vdcsJdbcTemplate.update("delete from pms_people where id in ("+FuzzyQueryUtils.getIds(pmsPeopleIds)+")");
		}
		if(pmsHouseholdAddressIdIds.size()>0){
			vdcsJdbcTemplate.update("delete from pms_household_address where id in ("+FuzzyQueryUtils.getIds(pmsHouseholdAddressIdIds)+")");
		}
	}

}
