package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class KnowledgeNumericeJsonBuilder {
	
	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createKlgNumericJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("knowledgeVersionedId", map.get("knowledgeVersionedId").toString());
		json.put("keyId", map.get("keyId").toString());
		json.put("value", map.get("value") == null?"":map.get("value").toString());
		json.put("currentUnit", map.get("currentUnit") == null?"":map.get("currentUnit").toString());
		return json;
	}

}
