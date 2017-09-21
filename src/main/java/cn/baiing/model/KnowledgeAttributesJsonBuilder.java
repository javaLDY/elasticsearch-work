package cn.baiing.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.baiing.Util.DateUtil;
import cn.baiing.util.StringUtil;

public class KnowledgeAttributesJsonBuilder {

	private static Gson gson = new Gson();

	/**
	 * 构建json put 原型
	 * @param klgMap
	 * @return
	 */
	public static Map<String, Object> createBasicKlgAttrsJsonByKlgAndAttrMap(Map<String, Object> klgMap){
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("name", klgMap.get("name").toString());
		json.put("byName", klgMap.get("byName") == null?"":klgMap.get("byName").toString());
		json.put("byLabel", klgMap.get("byLabel") == null?"":klgMap.get("byLabel").toString());
		json.put("knowledgeId", klgMap.get("knowledgeId").toString());
		json.put("knowledgeVersionedId", klgMap.get("knowledgeVersionedId").toString());
		json.put("authority", klgMap.get("authority")== null?"":klgMap.get("authority").toString());
		if(klgMap.get("effectStartTime") != null){
			json.put("effectStartTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("effectStartTime").toString())));
		}
		
		if(klgMap.get("effectEndTime") != null){
			json.put("effectEndTime",DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("effectEndTime").toString())));
		}
		
		if(klgMap.get("startTime") != null){
			json.put("startTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("startTime").toString())));
		}

		if(klgMap.get("endTime") != null){
			json.put("endTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("endTime").toString())));
		}
		
		if(klgMap.get("publishTime") != null){
			json.put("publishTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("publishTime").toString())));
		}
		
		if(klgMap.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.formatDateAllToString(DateUtil.getDateOfHaveAllTime(klgMap.get("lastUpdatedTime").toString())));
		}
		
		json.put("channel", klgMap.get("channel").toString());
		json.put("mongoId", klgMap.get("mongoId").toString());
		json.put("templateId", klgMap.get("templateId").toString());
		json.put("locIds", klgMap.get("locIds").toString());
		json.put("value", klgMap.get("value").toString());
		json.put("dataType", klgMap.get("dataType") == null?"0":klgMap.get("dataType").toString());
		json.put("currentUnit", klgMap.get("currentUnit") == null?"":klgMap.get("currentUnit").toString());
		json.put("klgAttrId", klgMap.get("klgAttrId").toString());
		json.put("keyId", klgMap.get("keyId").toString());
		json.put("clickNum", klgMap.get("clickNum") == null?"0":klgMap.get("clickNum").toString());
		return json;
	}
	
	/**
	 * 构建json put 原型
	 * @param klgMap
	 * @return
	 */
	public static Map<String, Object> createKlgAndAttrJson(Map<String, Object> klgMap){
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("name", klgMap.get("name").toString());
		json.put("byName", klgMap.get("byName") == null?"":klgMap.get("byName").toString());
		json.put("byLabel", klgMap.get("byLabel") == null?"":klgMap.get("byLabel").toString());
		json.put("knowledgeId", klgMap.get("knowledgeId").toString());
		json.put("knowledgeVersionedId", klgMap.get("knowledgeVersionedId").toString());
		
		if(klgMap.get("authority") == null){
			json.put("authority", new ArrayList<Long>());
		}else if(StringUtils.isNotEmpty(klgMap.get("authority").toString())){
			String[] authority = klgMap.get("authority").toString().split(",");
			List<Long> newAuthority = new ArrayList<Long>();
			for(String ss : authority){
				newAuthority.add(Long.valueOf(ss));
			}
			json.put("authority", newAuthority);
		}else{
			json.put("authority", new ArrayList<Long>());
		}
		
		if(klgMap.get("effectStartTime") != null){
			json.put("effectStartTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("effectStartTime").toString())));
		}
		
		if(klgMap.get("effectEndTime") != null){
			json.put("effectEndTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("effectEndTime").toString())));
		}
		
		if(klgMap.get("startTime") != null){
			json.put("startTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("startTime").toString())));
		}

		if(klgMap.get("endTime") != null){
			json.put("endTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("endTime").toString())));
		}
		
		if(klgMap.get("publishTime") != null){
			json.put("publishTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("publishTime").toString())));
		}
		
		if(klgMap.get("lastUpdatedTime") != null){
			json.put("lastUpdatedTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("lastUpdatedTime").toString())));
		}
		
		if(klgMap.get("klgAttrUpdateTime") != null){
			json.put("klgAttrUpdateTime", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgMap.get("klgAttrUpdateTime").toString())));
		}
		
		String[] vids = klgMap.get("channel").toString().split(",");
		List<Long> newVids = new ArrayList<Long>();
		for(String ss : vids){
			newVids.add(Long.valueOf(ss));
		}
		
		json.put("channel", newVids);
		json.put("mongoId", klgMap.get("mongoId").toString());
		json.put("templateId", klgMap.get("templateId").toString());
		json.put("locIds", klgMap.get("locIds").toString());
		json.put("clickNum", klgMap.get("clickNum") == null?"0":klgMap.get("clickNum").toString());
		List<Map<String, Object>> attrsMap = (List<Map<String, Object>>) klgMap.get("attrs");
		json.put("attrs", attrsMap);
		return json;
	}
	
	public static void main(String[] args) {
		String time = "2017-03-21 22:45:05 ";
		try {
			System.out.println(DateUtil.simpleDateFormatAll.format(DateUtil.simpleDateFormatAll.parse(time)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
