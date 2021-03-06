package cn.baiing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.baiing.Util.DateUtil;

public class KnowledgeJsonBuilder {
	
	private static Gson gson = new Gson();

	/**
	 * 构建json put 原型
	 * @param klgMap
	 * @return
	 */
	public static Map<String, Object> createKlgJsonByKlgAndAttrMap(Map<String, Object> klgMap){
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("name", klgMap.get("name").toString());
		json.put("knowledgeId", klgMap.get("knowledgeId").toString());
		json.put("knowledgeVersionedId", klgMap.get("knowledgeVersionedId").toString());
		if(klgMap.get("effectStartTime") != null){
			json.put("effectStartTime", klgMap.get("effectStartTime").toString());
		}
		
		if(klgMap.get("effectEndTime") != null){
			json.put("effectEndTime", klgMap.get("effectEndTime").toString());
		}
		
		if(klgMap.get("startTime") != null){
			json.put("startTime", klgMap.get("startTime").toString());
		}

		if(klgMap.get("endTime") != null){
			json.put("endTime", klgMap.get("endTime").toString());
		}
		
		if(klgMap.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", klgMap.get("lastUpdatedTime").toString());
		}
		String vids = klgMap.get("vids").toString();
		List<Long> newVids = gson.fromJson(vids, new TypeToken<ArrayList<String>>(){}.getType());
		List<Map<String, Object>> attrsMap = (List<Map<String, Object>>) klgMap.get("attrs");
		json.put("attrs", attrsMap);
		json.put("vids", newVids);
		json.put("mongoId", klgMap.get("mongoId").toString());
		json.put("templateId", Long.valueOf(klgMap.get("templateId").toString()));
		json.put("locIds", Long.valueOf(klgMap.get("locIds").toString()));
//		json.put("locationName", klgMap.get("locationName").toString());
		json.put("templateDisplayName", klgMap.get("templateDisplayName").toString());
		json.put("templateName", klgMap.get("templateName").toString());
		json.put("clickNum", klgMap.get("clickNum") == null?0:klgMap.get("clickNum").toString());
		return json;
	}
	
	/**
	 * 构建json put 原型
	 * @param klgMap
	 * @return
	 */
	public static Map<String, Object> createKlgJsonByKlgMap(Map<String, Object> klgMap){
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("name", klgMap.get("name").toString());
		json.put("knowledgeId", klgMap.get("knowledgeId").toString());
		json.put("knowledgeVersionedId", klgMap.get("knowledgeVersionedId").toString());
		if(klgMap.get("effectStartTime") != null){
			json.put("effectStartTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("effectStartTime").toString())));
		}
		
		if(klgMap.get("effectEndTime") != null){
			json.put("effectEndTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("effectEndTime").toString())));
		}
		
		if(klgMap.get("startTime") != null){
			json.put("startTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("startTime").toString())));
		}

		if(klgMap.get("endTime") != null){
			json.put("endTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("endTime").toString())));
		}
		
		if(klgMap.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("lastUpdatedTime").toString())));
		}
		String[] vids = klgMap.get("vids").toString().split(",");
		List<Long> newVids = new ArrayList<Long>();
		for(String ss : vids){
			newVids.add(Long.valueOf(ss));
		}
		json.put("vids", newVids);
		json.put("mongoId", klgMap.get("mongoId").toString());
		json.put("templateId", Long.valueOf(klgMap.get("templateId").toString()));
		json.put("locIds", Long.valueOf(klgMap.get("locIds").toString()));
//		json.put("locationName", klgMap.get("locationName").toString());
		json.put("templateDisplayName", klgMap.get("templateDisplayName").toString());
		json.put("templateName", klgMap.get("templateName").toString());
		json.put("clickNum", klgMap.get("clickNum") == null?0:klgMap.get("clickNum").toString());
		return json;
	}
	
}
