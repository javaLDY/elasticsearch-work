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
	
	public List<Long> getknowledgeVersionedIds(int startPos, int pageSize){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final List<Long> resultList = new ArrayList<Long>();
		paramMap.put("startPos", startPos);
		paramMap.put("pageSize", pageSize);
		String sql = "select knowledge_versioned_id from knowledges WHERE is_deleted = 1 AND is_published = 1 AND state = 10 LIMIT :startPos, :pageSize";
		return simpleDao.getNamedParameterJdbcTemplate().query(sql, paramMap, new ResultSetExtractor<List<Long>>() {

			public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					resultList.add(rs.getLong("knowledge_versioned_id"));
				}
				return resultList;
			}
		});
	}
	
	public List<Map<String, Object>> getKnowledges(List<String> knowledgeVersionedIds){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String newKnowledgeIds = StringUtils.join(knowledgeVersionedIds, ",");
		String sql = "SELECT"
				+ " k.`name`,"
				+ " k.knowledge_id knowledgeId,"
				+ " k.knowledge_versioned_id AS knowledgeVersionedId,"
				+ " k.effect_start_time effectStartTime,"
				+ " k.effect_end_time effectEndTime,"
				+ " k.start_time startTime,"
				+ " k.end_time endTime,"
				+ " k.vids,"
				+ " k.mongo_id mongoId,"
				+ " k.last_updated_time lastUpdatedTime,"
				+ " k.template_id templateId,"
				+ " k.loc_ids locIds,"
				+ " loc.`name` AS locationName,"
				+ " t.display_name AS templateDisplayName,"
				+ " t.`name` AS templateName,"
				+ " pcn.click_num AS clickNum"
				+ " FROM knowledges k "
				+ "INNER JOIN locations loc ON k.loc_ids = loc.id "
				+ "INNER JOIN templates t ON k.template_id = t.id "
				+ "LEFT JOIN praise_click_number pcn ON k.knowledge_id = pcn.knowledge_id "
				+ "WHERE "
				+ "k.is_deleted = 1 "
				+ "AND k.is_published = 1 "
				+ "AND k.state = 10 "
				+ "AND k.knowledge_id IN ("+newKnowledgeIds+")";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
	
	public Map<String, Object> getKnowledgeByKvId(String knowledgeVersionedId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("knowledgeVersionedId", knowledgeVersionedId);
		String sql = "SELECT"
				+ " k.`name`,"
				+ " k.knowledge_id knowledgeId,"
				+ " k.knowledge_versioned_id AS knowledgeVersionedId,"
				+ " k.effect_start_time effectStartTime,"
				+ " k.effect_end_time effectEndTime,"
				+ " k.start_time startTime,"
				+ " k.end_time endTime,"
				+ " k.vids,"
				+ " k.mongo_id mongoId,"
				+ " k.last_updated_time lastUpdatedTime,"
				+ " k.template_id templateId,"
				+ " k.loc_ids locIds,"
				+ " loc.`name` AS locationName,"
				+ " t.display_name AS templateDisplayName,"
				+ " t.`name` AS templateName,"
				+ " pcn.click_num AS clickNum"
				+ " FROM knowledges k "
				+ "INNER JOIN locations loc ON k.loc_ids = loc.id "
				+ "INNER JOIN templates t ON k.template_id = t.id "
				+ "LEFT JOIN praise_click_number pcn ON k.knowledge_id = pcn.knowledge_id "
				+ "WHERE "
				+ "k.is_deleted = 1 "
				+ "AND k.is_published = 1 "
				+ "AND k.state = 10 "
				+ "AND k.knowledge_id =:knowledgeVersionedId";
		return simpleDao.getNamedParameterJdbcTemplate().queryForMap(sql, paramMap);
	}
}
