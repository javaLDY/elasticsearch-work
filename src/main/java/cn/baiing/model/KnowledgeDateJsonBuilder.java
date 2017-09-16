package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.DateUtil;

public class KnowledgeDateJsonBuilder {

	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createKlgDateJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("knowledgeVersionedId", map.get("knowledgeVersionedId").toString());
		json.put("keyId", map.get("keyId").toString());
		if(map.get("date") != null){
			json.put("date", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(map.get("date").toString())));
		}
		return json;
	}
	
}
