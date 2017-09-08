package cn.baiing.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;

@Repository
public class SummaryAttrDao extends BaseUtil{

	@Autowired
	private String sql_getSummaryAttrByTemplateId;
	
	public String getSummaryAttrByTemplateId(String templateId){
		paramMap = new HashMap<String, Object>();
		paramMap.put("templateId", templateId);
		return simpleDao.getNamedParameterJdbcTemplate().query(sql_getSummaryAttrByTemplateId, paramMap, new ResultSetExtractor<String>() {

			public String extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				String result = null;
				while(rs.next()){
					result = rs.getString("value");
				}
				return result;
			}
		});
	}
}
