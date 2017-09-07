package cn.baiing.model;

import java.util.Map;


import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.DateUtil;

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
		if(map.get("creationTime") != null){
			json.put("creationTime", DateUtil.getDateOfHaveAllTime(map.get("creationTime").toString()));
		}
		if(map.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.getDateOfHaveAllTime(map.get("lastUpdatedTime").toString()));
		}
		json.put("lastUpdatedTime", map.get("lastUpdatedTime").toString());
		json.put("dataType", map.get("dataType").toString());
		json.put("options", map.get("options") == null?"":map.get("options").toString());
		json.put("defaultUnit", map.get("defaultUnit") == null?"":map.get("defaultUnit").toString());
		return json;
	}
	
}
