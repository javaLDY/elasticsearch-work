package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.DateUtil;

public class KnowledgeBusinessTagJsonBuilder {
	
	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createKlgBusinessTagJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("knowledgeVersionedId", map.get("knowledgeVersionedId").toString());
		json.put("businessTagId", map.get("businessTagId").toString());
		if(map.get("updateTime") != null){
			json.put("updateTime", DateUtil.getDateOfHaveAllTime(map.get("updateTime").toString()));
		}
		return json;
	}

}
