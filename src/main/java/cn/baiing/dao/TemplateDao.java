package cn.baiing.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;

@Repository
public class TemplateDao extends BaseUtil{

	public List<Map<String, Object>> getTemplates(){
		String sql = "select id, name, display_name displayName, sequence, creation_time creationTime,"
				+ " last_updated_time lastUpdatedTime, parent_catalog_id parentCatalogId"
				+ " from templates where is_deleted=0";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
}
