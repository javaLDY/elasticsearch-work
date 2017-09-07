package cn.baiing.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.alibaba.fastjson.JSONArray;

import cn.baiing.Util.DateUtil;

public class KnowledgeJsonBuilder {

	/**
	 * 构建基础klg数据put原型
	 * @param klgMap
	 * @return
	 */
	public static XContentBuilder createKlgJsonBuilderByKlgMap(Map<String, Object> klgMap){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
				.field("name", klgMap.get("name").toString())
				.field("knowledgeId", klgMap.get("knowledgeId").toString())
				.field("knowledgeVersionedId", klgMap.get("knowledgeVersionedId").toString())
				.field("effectStartTime", klgMap.get("effectStartTime") == null?false:DateUtil.getDateOfHaveAllTime(klgMap.get("effectStartTime").toString()))
				.field("effectEndTime", klgMap.get("effectEndTime") == null?false:DateUtil.getDateOfHaveAllTime(klgMap.get("effectEndTime").toString()))
				.field("startTime", klgMap.get("startTime") == null?false:DateUtil.getDateOfHaveAllTime(klgMap.get("startTime").toString()))
				.field("endTime", klgMap.get("endTime") == null?false:DateUtil.getDateOfHaveAllTime(klgMap.get("endTime").toString()))
				.field("vids", klgMap.get("vids").toString())
				.field("mongoId", klgMap.get("mongoId").toString())
				.field("lastUpdatedTime", klgMap.get("lastUpdatedTime") == null?false:DateUtil.getDateOfHaveAllTime(klgMap.get("lastUpdatedTime").toString()))
				.field("templateId", Long.valueOf(klgMap.get("templateId").toString()))
				.endObject();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
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
			json.put("effectStartTime", DateUtil.getDateOfHaveAllTime(klgMap.get("effectStartTime").toString()));
		}
		
		if(klgMap.get("effectEndTime") != null){
			json.put("effectEndTime", DateUtil.getDateOfHaveAllTime(klgMap.get("effectEndTime").toString()));
		}
		
		if(klgMap.get("startTime") != null){
			json.put("startTime", DateUtil.getDateOfHaveAllTime(klgMap.get("startTime").toString()));
		}

		if(klgMap.get("endTime") != null){
			json.put("endTime", DateUtil.getDateOfHaveAllTime(klgMap.get("endTime").toString()));
		}
		
		if(klgMap.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.getDateOfHaveAllTime(klgMap.get("lastUpdatedTime").toString()));
		}
		String[] vids = klgMap.get("vids").toString().split(",");
		List<Long> newVids = new ArrayList<Long>();
		for(String ss : vids){
			newVids.add(Long.valueOf(ss));
		}
		json.put("vids", newVids);
		json.put("mongoId", klgMap.get("mongoId").toString());
		json.put("templateId", Long.valueOf(klgMap.get("templateId").toString()));
		
		return json;
	}
	
	public static void main(String[] args) {
		String ss = "1";
		String[] list = ss.split(",");
		List<Long> newVids = new ArrayList<Long>();
		for(String aa : list){
			newVids.add(Long.valueOf(aa));
		}
		System.out.println(JSONArray.toJSONString(newVids));
	}
}
