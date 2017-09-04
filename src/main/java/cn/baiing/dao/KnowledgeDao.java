package cn.baiing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.SimpleDao;

@Repository
public class KnowledgeDao {
	
	@Autowired
	private SimpleDao simpleDao;
	
	public List<String> getknowledgeVersionedIds(int startPos, int pageSize){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final List<String> resultList = new ArrayList<String>();
		paramMap.put("startPos", startPos);
		paramMap.put("pageSize", pageSize);
		String sql = "select knowledge_versioned_id from knowledges WHERE is_deleted = 1 AND is_published = 1 AND state = 10 LIMIT :startPos, :pageSize";
		return simpleDao.getNamedParameterJdbcTemplate().query(sql, paramMap, new ResultSetExtractor<List<String>>() {

			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					resultList.add(rs.getString("knowledge_versioned_id"));
				}
				return resultList;
			}
		});
	}
	
	public List<Map<String, Object>> getKnowledges(List<String> knowledgeVersionedIds){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String newKnowledgeIds = StringUtils.join(knowledgeVersionedIds, ",");
		String sql = "SELECT "
				+ "k.`name`,"
				+ " k.knowledge_id knowledgeId,"
				+ " k.knowledge_versioned_id AS knowledgeVersionedId,"
				+ " k.effect_start_time effectStartTime,"
				+ " k.effect_end_time effectEndTime,"
				+ " k.start_time startTime,"
				+ " k.end_time endTime,"
				+ " k.vids,"
				+ " k.mongo_id mongoId,"
				+ " k.last_updated_time lastUpdatedTime,"
				+ " k.template_id templateId"
				+ " FROM knowledges k WHERE is_deleted = 1 AND is_published = 1 AND state = 10 AND knowledge_id in ("+newKnowledgeIds+")";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
}
