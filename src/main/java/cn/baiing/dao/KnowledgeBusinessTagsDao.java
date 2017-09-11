package cn.baiing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;
import cn.baiing.dao.util.SimpleDao;

@Repository
public class KnowledgeBusinessTagsDao extends BaseUtil{

	@Autowired
	private SimpleDao simpleDao;
	
	public List<Map<String, Object>> getbussinessIdByKvId(List<String> knowledgeVersionedIds){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String newKnowledgeIds = StringUtils.join(knowledgeVersionedIds, ",");
		String sql = "SELECT id, knowledge_versioned_id AS knowledgeVersionedId,"
				+ " business_tag_id businessTagId, update_time updateTime"
				+ " FROM knowledge_business_tags"
				+ " WHERE knowledge_versioned_id IN ("+ newKnowledgeIds +") AND is_deleted = 0";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
	
	@Autowired
	private String sql_getKnowledgeBusinessTagByKnowledgeId;
	
	public List<Map<String, Object>> getKnowledgeBusinessTagByKnowledgeId(String knowledgeVersionedId){
		paramMap = new HashMap<String, Object>();
		paramMap.put("knowledgeVersionedId", knowledgeVersionedId);
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql_getKnowledgeBusinessTagByKnowledgeId, paramMap);
	}
}
