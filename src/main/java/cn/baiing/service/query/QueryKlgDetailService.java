package cn.baiing.service.query;

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
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

@Service
public class QueryKlgDetailService {
	
	private static Map<String, JSONObject> templateKeyAttrsMap = new HashMap<String, JSONObject>();
	
	/**
	 * 查询知识详情
	 * @param knowledgeVersionedId
	 */
	public static void getKnowledgeDetail(String knowledgeVersionedId){
		TransportClient client = TransportUtil.buildClient();
		JSONObject responseJson = new JSONObject();
		SearchResponse klgSearchResponse = client.prepareSearch(IndexRelationConstant.KLG_INDEX)
				.setTypes(IndexRelationConstant.KLG_TYPE)
				.setQuery(QueryBuilders.matchQuery("knowledgeVersionedId", knowledgeVersionedId))
				.execute()
				.actionGet();
		SearchHit[] searchHits = klgSearchResponse.getHits().getHits();
		if(searchHits.length > 0){
			for(SearchHit searchHit : searchHits){
				responseJson = JSONObject.parseObject(searchHit.getSourceAsString());
			}
		}
		SearchResponse KlgAttrSearchResponse = client.prepareSearch(
				IndexRelationConstant.KLG_DATE_INDEX,
				IndexRelationConstant.KLG_NUMERIC_INDEX,
				IndexRelationConstant.KLG_SMS_INDEX,
				IndexRelationConstant.KLG_TEXT_INDEX)
		.setTypes(
				IndexRelationConstant.KLG_DATE_TYPE,
				IndexRelationConstant.KLG_NUMERIC_TYPE,
				IndexRelationConstant.KLG_SMS_TYPE,
				IndexRelationConstant.KLG_TEXT_TYPE)
		.setQuery(QueryBuilders.matchQuery("knowledgeVersionedId", knowledgeVersionedId))
		.execute().actionGet();
		SearchHit[] klgAttrHits = KlgAttrSearchResponse.getHits().getHits();
		if(searchHits.length > 0){
			JSONArray attr = new JSONArray();
			for(SearchHit searchHit : klgAttrHits){
				JSONObject attrJson = JSONObject.parseObject(searchHit.getSourceAsString());
				attr.add(attrJson);
			}
			responseJson.put("klgAttr", attr);
		}
		client.close();
		System.out.println(responseJson.toJSONString());
	}
	
	public static JSONArray getKnowledges(List<String> ids){
		TransportClient client = TransportUtil.buildClient();
		MultiGetResponse multiGetResponse = client.prepareMultiGet().add(IndexRelationConstant.KLG_INDEX,
				IndexRelationConstant.KLG_TYPE, ids)
		.execute().actionGet();
		JSONArray klgList = new JSONArray();
		for (MultiGetItemResponse itemResponse : multiGetResponse) { 
		    GetResponse response = itemResponse.getResponse();
		    if (response.isExists()) {                      
		        JSONObject json = JSONObject.parseObject(response.getSourceAsString()); 
		        klgList.add(json);
		    }
		}
		System.out.println(klgList);
		return klgList;
	}
	
	public static JSONArray getKnowledgeAttr(List<String> ids){
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.filter(QueryBuilders.termsQuery("knowledgeVersionedId", ids));
		TransportClient client = TransportUtil.buildClient();
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
		.setQuery(boolQueryBuilder).setSize(100000)
		.execute().actionGet();
		
		SearchHit[] klgAttrs = searchKlgAttrResponse.getHits().getHits();
		JSONArray attr = new JSONArray();
		if(klgAttrs.length > 0){
			for(SearchHit searchHit : klgAttrs){
				JSONObject attrJson = JSONObject.parseObject(searchHit.getSourceAsString());
				String keyId = attrJson.getString("keyId");
				JSONObject templateKeysJson = templateKeyAttrsMap.get(keyId);
				attrJson.put("dataType", templateKeysJson.getString("dataType"));
				attrJson.put("name", templateKeysJson.getString("name"));
				attrJson.put("displayName", templateKeysJson.getString("displayName"));
				attrJson.put("templateId", templateKeysJson.getString("templateId"));
				attr.add(attrJson);
			}
			System.out.println(attr);
		}
		
		return attr;
	}
	
	public static void getTemplateKeys(){
		TransportClient client = TransportUtil.buildClient();
		long count = client.prepareSearch(IndexRelationConstant.TEMPLATE_KEY_INDEX)
				.setTypes(IndexRelationConstant.TEMPLATE_KEY_TYPE)
				.execute().actionGet().getHits().getTotalHits();
			System.out.println(count);
			
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
		client.close();
	}
	
	public static void putKlgAngKlgAttrToEs(){
		getTemplateKeys();
		TransportClient client = TransportUtil.buildClient();
		List<String> ids = new ArrayList<String>();
		ids.add("100022");
		ids.add("100048");
		ids.add("100020");
		ids.add("100044");
		ids.add("100024");
		ids.add("100068");
		JSONArray klgList = getKnowledges(ids);
		JSONArray klgAttrList = getKnowledgeAttr(ids);
		List<Map<String, Object>> klgAndKlgAttrList = new ArrayList<Map<String,Object>>(); 
		for(String ss : ids){
			List<Map> klg = JsonPath.parse(klgList.toJSONString()).read("$[?(@.knowledgeVersionedId == '"+ ss +"')]", List.class);
			Map<String, Object> klgMap = klg.get(0);
			List<Map> attrList = JsonPath.parse(klgAttrList.toJSONString()).read("$[?(@.knowledgeVersionedId == '"+ ss +"')]");
			if(CollectionUtils.isNotEmpty(attrList)){
				klgMap.put("attrs", attrList);
			}
			klgAndKlgAttrList.add(klgMap);
		}
		System.out.println(JSONObject.toJSON(klgAndKlgAttrList));
	}
	
	public static void main(String[] args) {
		getTemplateKeys();
//		getKnowledgeDetail("100036");
//		getTemplateKeys();
		List<String> ids = new ArrayList<String>();
		ids.add("100114");
		ids.add("100048");
		ids.add("100020");
		ids.add("100044");
		ids.add("100024");
		ids.add("100068");
		getKnowledgeAttr(ids);
//		putKlgAngKlgAttrToEs();
	}
}
