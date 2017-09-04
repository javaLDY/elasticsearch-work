package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.DateUtil;

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
		if(map.get("creationTime") != null){
			json.put("creationTime", DateUtil.getDateOfHaveAllTime(map.get("creationTime").toString()));
		}
		
		if(map.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.getDateOfHaveAllTime(map.get("lastUpdatedTime").toString()));
		}
		json.put("parentCatalogId", map.get("parentCatalogId").toString());
		return json;
	}

}
