package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class TemplateKeyJsonBuilder {

	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createTemplateKeyJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("keyId", map.get("id").toString());
		json.put("templateId", map.get("templateId").toString());
		json.put("name", map.get("name").toString());
		json.put("displayName", map.get("displayName").toString());
		json.put("sequence", map.get("sequence").toString());
		json.put("creationTime", map.get("creationTime").toString());
		json.put("lastUpdatedTime", map.get("lastUpdatedTime").toString());
		json.put("dataType", map.get("dataType").toString());
		json.put("options", map.get("options").toString());
		json.put("defaultUnit", map.get("defaultUnit").toString());
		return json;
	}
	
}
