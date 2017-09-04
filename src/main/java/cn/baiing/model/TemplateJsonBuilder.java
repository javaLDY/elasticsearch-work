package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class TemplateJsonBuilder {
	
	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createTemplateJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("templateId", map.get("id").toString());
		json.put("name", map.get("name").toString());
		json.put("displayName", map.get("displayName").toString());
		json.put("sequence", map.get("sequence").toString());
		json.put("creationTime", map.get("creationTime").toString());
		json.put("lastUpdatedTime", map.get("lastUpdatedTime").toString());
		json.put("parentCatalogId", map.get("parentCatalogId").toString());
		return json;
	}

}
