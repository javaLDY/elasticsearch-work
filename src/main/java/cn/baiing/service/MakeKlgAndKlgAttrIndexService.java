package cn.baiing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.DataType;
import cn.baiing.model.IndexRelationConstant;

@Service
public class MakeKlgAndKlgAttrIndexService {
	
	private static Map<String, JSONObject> templateKeyAttrsMap = new HashMap<String, JSONObject>();
	
	/**
	 * 根据知识klgversionedId,得到知识主体
	 * @param ids
	 * @return
	 */
	public static JSONArray getKnowledges(List<String> knowledgeVersionedIds){
		TransportClient client = TransportUtil.buildClient();
		MultiGetResponse multiGetResponse = client.prepareMultiGet().add(IndexRelationConstant.KLG_INDEX,
				IndexRelationConstant.KLG_TYPE, knowledgeVersionedIds)
		.execute().actionGet();
		JSONArray klgList = new JSONArray();
		for (MultiGetItemResponse itemResponse : multiGetResponse) { 
		    GetResponse response = itemResponse.getResponse();
		    if (response.isExists()) {                      
		        JSONObject json = JSONObject.parseObject(response.getSourceAsString()); 
		        klgList.add(json);
		    }
		}
		return klgList;
	}
	
	/**
	 * 获取知识的属性
	 * @param ids
	 * @return
	 */
	public static JSONArray getKnowledgeAttr(List<String> knowledgeVersionedIds){
		getTemplateKeys();
		TransportClient client = TransportUtil.buildClient();
		JSONArray attr = new JSONArray();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.filter(QueryBuilders.termsQuery("knowledgeVersionedId", knowledgeVersionedIds)).hasClauses();
		try {
			SearchResponse searchKlgAttrResponse = client.prepareSearch(
					IndexRelationConstant.KLG_DATE_INDEX,
					IndexRelationConstant.KLG_NUMERIC_INDEX,
					IndexRelationConstant.KLG_SMS_INDEX,
					IndexRelationConstant.KLG_TEXT_INDEX)
			.setTypes(
					IndexRelationConstant.KLG_DATE_TYPE,
					IndexRelationConstant.KLG_NUMERIC_TYPE,
					IndexRelationConstant.KLG_SMS_TYPE,
					IndexRelationConstant.KLG_TEXT_TYPE)
			.setQuery(boolQueryBuilder)
			.setSize(1000000)
			.execute().actionGet();
			
			SearchHit[] klgAttrs = searchKlgAttrResponse.getHits().getHits();
			if(klgAttrs.length > 0){
				for(SearchHit searchHit : klgAttrs){
					JSONObject attrJson = JSONObject.parseObject(searchHit.getSourceAsString());
					if(attrJson.containsKey("keyId")){
						String keyId = attrJson.getString("keyId");
						JSONObject templateKeysJson = templateKeyAttrsMap.get(keyId);
						//判断属性类型，放入对应的值中，方便以后筛选，目前只针对数值和时间做特殊处理，其他均为-1
						String index = DataType.belongKeyAttrIndex(templateKeysJson.getString("dataType"));
						if(!index.equals("-1")){
							if(index.equals(IndexRelationConstant.KLG_NUMERIC_INDEX)){
								attrJson.put("integerValue", attrJson.getString("value"));
							}
							
							if(index.equals(IndexRelationConstant.KLG_DATE_INDEX)){
								attrJson.put("dateValue", attrJson.getString("date"));
							}
						}
						attrJson.put("dataType", templateKeysJson.getString("dataType"));
						attrJson.put("name", templateKeysJson.getString("name"));
						attrJson.put("displayName", templateKeysJson.getString("displayName"));
						attrJson.put("templateId", templateKeysJson.getString("templateId"));
						attr.add(attrJson);
					}else if(attrJson.containsKey("businessTagId")){
						
						attrJson.put("dataType", "");
						attrJson.put("name", "");
						attrJson.put("displayName", "");
						attrJson.put("templateId", "");
						attrJson.put("businessTagValue", attrJson.getString("date"));
						attr.add(attrJson);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attr;
	}
	
	/**
	 * 获取所有的知识属性信息
	 */
	public static void getTemplateKeys(){
		if(templateKeyAttrsMap == null || templateKeyAttrsMap.isEmpty()){
			TransportClient client = TransportUtil.buildClient();
			
			long count = client.prepareSearch(IndexRelationConstant.TEMPLATE_KEY_INDEX)
					.setTypes(IndexRelationConstant.TEMPLATE_KEY_TYPE)
					.execute().actionGet().getHits().getTotalHits();
			
			SearchResponse searchTemplateKeyAttrResponse = client.prepareSearch(IndexRelationConstant.TEMPLATE_KEY_INDEX)
				.setTypes(IndexRelationConstant.TEMPLATE_KEY_TYPE)
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize((int)count)
				.execute().actionGet();
			SearchHit[] templateKeys = searchTemplateKeyAttrResponse.getHits().getHits();
			if(templateKeys.length > 0){
				for(SearchHit searchHit : templateKeys){
					JSONObject attrJson = JSONObject.parseObject(searchHit.getSourceAsString());
					String keyId = attrJson.getString("keyId");
					templateKeyAttrsMap.put(keyId, attrJson);
				}
			}
		}
	}
	
	/**
	 * 组装知识主体和知识属性
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> combineKlgAngKlgAttr(List<String> knowledgeVersionedIds){
		Gson gson = new Gson();
		JSONArray klgList = getKnowledges(knowledgeVersionedIds);
		JSONArray klgAttrList = getKnowledgeAttr(knowledgeVersionedIds);
		List<Map<String, Object>> klgAndKlgAttrList = new ArrayList<Map<String,Object>>(); 
		for(String ss : knowledgeVersionedIds){
			List<Map> klg = JsonPath.parse(klgList.toJSONString()).read("$[?(@.knowledgeVersionedId == '"+ ss +"')]", List.class);
			if(CollectionUtils.isNotEmpty(klg)){
				Map<String, Object> klgMap = klg.get(0);
				List<String> attrList = JsonPath.parse(klgAttrList.toJSONString()).read("$[?(@.knowledgeVersionedId == '"+ ss +"')]");
				if(CollectionUtils.isNotEmpty(attrList)){
					klgMap.put("attrs", gson.fromJson(attrList.toString(), new TypeToken<List<Map<String, Object>>>(){}.getType()));
				}
				klgAndKlgAttrList.add(klgMap);
			}
		}
		return klgAndKlgAttrList;
	}
	
//	public List<String> getBusinessTagByKnowledgeVersionedIds(){
//		
//	}
	
}
