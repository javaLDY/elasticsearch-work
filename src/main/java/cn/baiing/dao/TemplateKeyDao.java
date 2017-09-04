package cn.baiing.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.baiing.dao.util.BaseUtil;

@Repository
public class TemplateKeyDao extends BaseUtil{

	public List<Map<String, Object>> getTemplateKeys(){
		String sql = "SELECT id, NAME, display_name displayName,"
				+ " sequence, creation_time creationTime,"
				+ " last_updated_time lastUpdatedTime,"
				+ " template_id templateId, data_type dataType,"
				+ " OPTIONS, default_unit defaultUnit FROM template_keys WHERE is_deleted = 0";
		return simpleDao.getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
	
}
