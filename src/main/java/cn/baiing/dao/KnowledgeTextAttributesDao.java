package cn.baiing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;

@Repository
public class KnowledgeTextAttributesDao extends BaseUtil{
	
	public List<Map<String, Object>> getKnowledgeTextAttributesByKvid(List<String> knowledgeVersionedIds){
		String newKnowledgeIds = StringUtils.join(knowledgeVersionedIds, ",");
		String sql = "SELECT id, knowledge_versioned_id AS knowledgeVersionedId,"
				+ " key_id keyId, value FROM knowledge_text_attributes"
				+ " WHERE knowledge_versioned_id IN ("+newKnowledgeIds+") AND is_deleted = 0";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
	
	@Autowired
	private String sql_getKnowledgeTextByKnowledgeVersionedId;
	
	public List<Map<String, Object>> getKnowledgeTextByKnowledgeVersionedId(String knowledgeVersionedId){
		paramMap = new HashMap<String, Object>();
		paramMap.put("knowledgeVersionedId", knowledgeVersionedId);
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql_getKnowledgeTextByKnowledgeVersionedId, paramMap);
	}

}
