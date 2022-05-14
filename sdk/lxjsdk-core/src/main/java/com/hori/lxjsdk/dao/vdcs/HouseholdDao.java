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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.utils.FuzzyQueryUtils;
import com.hori.lxjsdk.web.vo.HouseholdVo;
import com.hori.lxjsdk.web.vo.PlatFormUserVo;
import com.hori.lxjsdk.web.vo.PmsPeopleVo;

@Repository("householdDao")
public class HouseholdDao {

	@Resource
	private JdbcTemplate vdcsJdbcTemplate;
	
	public void updateHousehold(final List<PmsPeopleVo> proprietorsPeoples){
		
		if(proprietorsPeoples!=null&&proprietorsPeoples.size()>0){

			String updateSql = "update household set householdName=?,contact=? where householdSerial=? ";

			vdcsJdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					PmsPeopleVo pmsPeopleVo = proprietorsPeoples.get(i);

					String peopleName=StringUtils.isEmpty(pmsPeopleVo.getUserName())?pmsPeopleVo.getUserId():pmsPeopleVo.getUserName();

					ps.setString(1,peopleName);
					ps.setString(2,pmsPeopleVo.getMobile());
					ps.setString(3,pmsPeopleVo.getHouseholdSerial());

				}
				@Override
				public int getBatchSize() {
					return proprietorsPeoples.size();
				}
			});
		}
	}
	
	public List<HouseholdVo> findByAddress(List<String> addressList){
		
		if(addressList==null||addressList.size()==0){
			return new ArrayList<HouseholdVo>();
		}
		
		String ids = FuzzyQueryUtils.getIds(addressList);
		
		List<HouseholdVo> vos = vdcsJdbcTemplate.query("select id,householdSerial,householdName,address,organizationSeq,unit_serial,xyid from household where address in ("+ids+")", 
				new RowMapper<HouseholdVo>() {

			@Override
			public HouseholdVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				HouseholdVo rqb = new HouseholdVo();
			    rqb.setId(rs.getString("id"));
			    rqb.setHouseholdSerial(rs.getString("householdSerial"));
			    rqb.setHouseholdName(rs.getString("householdName"));
			    rqb.setAddress(rs.getString("address"));
			    rqb.setOrganizationSeq(rs.getString("organizationSeq"));
			    rqb.setUnitSerial(rs.getString("unit_serial"));
			    
			    String householdSerial = rqb.getHouseholdSerial();
			    String regionSerial=householdSerial.substring(3, 8)+householdSerial.substring(9, 11);
			    rqb.setRegionSerial(regionSerial);
			    
			    rqb.setXyid(rs.getString("xyid"));
			    
				return rqb;
			}
			
		});
		
		return vos;
	}

	public void clearHouseholdName(List<String> propPeopleHouseholdSerials) {
		
		if(propPeopleHouseholdSerials==null||propPeopleHouseholdSerials.size()==0){
			return;
		}
		String updateSql = "update household set householdName=null,contact=null where householdSerial in("+FuzzyQueryUtils.getIds(propPeopleHouseholdSerials)+")";

		vdcsJdbcTemplate.update(updateSql);
	}
}
