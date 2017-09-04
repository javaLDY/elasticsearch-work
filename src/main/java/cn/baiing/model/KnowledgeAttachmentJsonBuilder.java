package cn.baiing.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class KnowledgeAttachmentJsonBuilder {

	/**
	 * 构建json put 原型
	 * @param Map
	 * @return
	 */
	public static JSONObject createKlgAttachmentJsonByMap(Map<String, Object> map){
		JSONObject json = new JSONObject();
		json.put("knowledgeVersionedId", map.get("knowledgeVersionedId").toString());
		json.put("keyId", map.get("keyId").toString());
		json.put("attachmentId", map.get("attachmentId").toString());
		return json;
	}
}
