package com.hori.lxjsdk.webservice.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.hori.lxjsdk.webservice.IUpdatelxjsdkAreaInfoService;
import com.jlit.vdcs.webservice.Bean.SynTables;

public class IUpdatelxjsdkAreaInfoServiceImpl implements IUpdatelxjsdkAreaInfoService{

	private JdbcTemplate jdbcTemplate;
	@Override
	public void updatelxjsdkAreaInfo(List<String> sdkSql) {
		if(sdkSql==null||sdkSql.size()==0){
			return;
		}
		
		final String []mms = sdkSql.toArray(new String []{});
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				jdbcTemplate.batchUpdate(mms);
				System.out.println("SDK同步小区信息完毕！！！！");
			}
		}).start();
	}
	
	@Override
	public List<SynTables> getSynTotal(List<String> sdkTablesList) {
		List<SynTables> syns = new ArrayList<SynTables>();
		for (int i = 0; i < sdkTablesList.size(); i++) {
			Integer query = jdbcTemplate.query(sdkTablesList.get(i),new  ResultSetExtractor<Integer>(){
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						return rs.getInt(1);
					}
					return 0;
				}});
			SynTables syn = new SynTables();
			int lastIndex = sdkTablesList.get(i).indexOf("where")-1;
			syn.setTableName(sdkTablesList.get(i).substring(21, lastIndex));
			syn.setSynRecords(query.toString());
			syns.add(syn);
		}
		return syns;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
