package cn.baiing.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;

@Repository
public class KnowledgeAttributesDao extends BaseUtil{
	
	public List<Map<String, Object>> getKnowledgeAttributes(List<String> knowledgeVersionedIds){
		String newKnowledgeIds = StringUtils.join(knowledgeVersionedIds, ",");
		String sql = "SELECT"
				+ " k.knowledge_id knowledgeId,"
				+ " k.knowledge_versioned_id knowledgeVersionedId,"
				+ " k.`name`, k.authority,"
				+ " k.loc_ids locIds,"
				+ " k.effect_end_time effectEndTime,"
				+ " k.effect_start_time effectStartTime,"
				+ " k.start_time startTime,"
				+ " k.end_time endTime,"
				+ " k.by_label byLabel,"
				+ " k.by_name byName,"
				+ " k.last_updated_time lastUpdatedTime,"
				+ " k.publish_time publishTime,"
				+ " k.mongo_id mongoId,"
				+ " k.template_id templateId,"
				+ " k.vids channel,"
				+ " kat.`value`,"
				+ " kat.data_type dataType,"
				+ " kat.current_unit currentUnit,"
				+ " kat.id klgAttrId,"
				+ " kat.key_id keyId,"
				+ " kat.update_time klgAttrUpdateTime"
				+ " FROM knowledges k, knowledge_attributes kat"
				+ " WHERE k.knowledge_versioned_id = kat.knowledge_versioned_id"
				+ " AND k.is_deleted = 1 AND k.is_published = 1"
				+ " AND k.state = 10"
				+ " AND kat.is_deleted = 0"
				+ " AND k.knowledge_versioned_id IN ("+ newKnowledgeIds +")";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}

}
