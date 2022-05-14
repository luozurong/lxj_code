package com.hori.lxjsdk.dao.vdcs;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.hori.lxjsdk.utils.FuzzyQueryUtils;

@Repository("talkSerialDao")
public class TalkSerialDao {

	@Resource
	private JdbcTemplate vdcsJdbcTemplate;
	
	public String getPassword(String userAccount){
		if(FuzzyQueryUtils.isCondition(userAccount)){
			String sql = "select password from talk_serial where user_account=?";
			String data =this.vdcsJdbcTemplate.query(sql,new String[]{ userAccount},new ResultSetExtractor<String>(){
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						return rs.getString(1);
					}
					return null;
				}
			});
			return data;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @param userAccount
	 * @param string
	 */
	public void setJoinSdkFlag(String userAccount,String householdSerial, String sdkAccountStatus) {
		String sql = "update talk_serial set sdk_account=? where user_account=? and household_serial=?";
		this.vdcsJdbcTemplate.update(sql, sdkAccountStatus,userAccount,householdSerial);
	}
}
