package cn.baiing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.baiing.Util.DateUtil;
import cn.baiing.Util.TransportUtil;
import cn.baiing.Util.Unit;
import cn.baiing.model.DataType;
import cn.baiing.model.IndexRelationConstant;

@Service
public class MakeKlgAttributesIndexService {
	
	private static Map<String, JSONObject> templateKeyAttrsMap = new HashMap<String, JSONObject>();
	
	/**
	 * 获取知识的属性
	 * @param ids
	 * @return
	 */
	public Map<String, Map<String, Object>> getKnowledgeAttr(List<String> knowledgeVersionedIds){
		Gson gson = new Gson();
		getTemplateKeys();
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		TransportClient client = TransportUtil.buildClient();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.filter(QueryBuilders.termsQuery("knowledgeVersionedId", knowledgeVersionedIds)).hasClauses();
		try {
			SearchResponse searchKlgAttrResponse = client.prepareSearch(IndexRelationConstant.KLG_BASIC_ATTR_INDEX)
			.setTypes(IndexRelationConstant.KLG_BASIC_ATTR_TYPE)
			.setQuery(boolQueryBuilder)
			.setSize(1000000)
			.execute().actionGet();
			
			SearchHit[] klgAttrs = searchKlgAttrResponse.getHits().getHits();
			if(klgAttrs.length > 0){
				for(SearchHit searchHit : klgAttrs){
					Map<String, Object> klgAttrMap =  gson.fromJson(searchHit.getSourceAsString(), new TypeToken<Map>(){}.getType());
					String knowledgeVersionedId = klgAttrMap.get("knowledgeVersionedId").toString();
					Map<String, Object> attrMap = new HashMap<String, Object>();
					if(!result.isEmpty() && result.containsKey(knowledgeVersionedId)){
						Map<String, Object> newKlgAttrMap = result.get(knowledgeVersionedId);
						List<Map<String, Object>> attr = (List<Map<String, Object>>) newKlgAttrMap.get("attrs");
						if(newKlgAttrMap.containsKey("keyId") && !newKlgAttrMap.get("keyId").equals("0")){
							String keyId = klgAttrMap.get("keyId").toString();
							if(templateKeyAttrsMap.containsKey(keyId)){
								JSONObject templateKeysJson = templateKeyAttrsMap.get(keyId);
								attrMap.put("keyId", keyId);
								attrMap.put("value", klgAttrMap.get("value").toString());
								//判断属性类型，放入对应的值中，方便以后筛选，目前只针对数值和时间做特殊处理，其他均为-1
								String index = DataType.belongKeyAttrIndex(klgAttrMap.get("dataType").toString());
								String currentUnit = klgAttrMap.get("currentUnit") == null?"":klgAttrMap.get("currentUnit").toString();
								if(!index.equals("-1")){
									String value = klgAttrMap.get("value").toString();
									if(index.equals(IndexRelationConstant.KLG_NUMERIC_INDEX)){
										attrMap.put("integerValue", Unit.parse(value, currentUnit));
										attrMap.put("value", Unit.parse(value, currentUnit));
									}
									
									if(index.equals(IndexRelationConstant.KLG_DATE_INDEX)){
										boolean isDate = DateUtil.isDate(value);
										if(isDate){
											attrMap.put("dateValue", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgAttrMap.get("value").toString())));
										}
									}
								}
								attrMap.put("currentUnit", currentUnit);
								try {
									attrMap.put("name", templateKeysJson.getString("name"));
								} catch (Exception e) {
									e.printStackTrace();
								}
								attrMap.put("displayName", templateKeysJson.getString("displayName"));
								String templateId = templateKeysJson.getString("templateId");
//								if(templateId.contains(".")){
//									templateId = templateId.split(",")[0];
//								}
								attrMap.put("templateId", templateId);
								attr.add(attrMap);
								//移除无用的value
								newKlgAttrMap.remove("value");
								newKlgAttrMap.put("attrs", attr);
								result.put(knowledgeVersionedId, newKlgAttrMap);
							}
						}
					}else{
						List<Map<String, Object>> attr = new ArrayList<Map<String,Object>>();
						if(klgAttrMap.containsKey("keyId") && !klgAttrMap.get("keyId").equals("0")){
							String keyId = klgAttrMap.get("keyId").toString();
							if(templateKeyAttrsMap.containsKey(keyId)){
								JSONObject templateKeysJson = templateKeyAttrsMap.get(keyId);
								attrMap.put("keyId", keyId);
								attrMap.put("value", klgAttrMap.get("value").toString());
								//判断属性类型，放入对应的值中，方便以后筛选，目前只针对数值和时间做特殊处理，其他均为-1
								String index = DataType.belongKeyAttrIndex(klgAttrMap.get("dataType").toString());
								String currentUnit = klgAttrMap.get("currentUnit") == null?"":klgAttrMap.get("currentUnit").toString();
								try {
								if(!index.equals("-1")){
									String value = klgAttrMap.get("value").toString();
									if(index.equals(IndexRelationConstant.KLG_NUMERIC_INDEX)){
										attrMap.put("integerValue", Unit.parse(value, currentUnit));
										attrMap.put("value", Unit.parse(value, currentUnit));
									}
									if(index.equals(IndexRelationConstant.KLG_DATE_INDEX)){
										boolean isDate = DateUtil.isDate(value);
										if(isDate){
											attrMap.put("dateValue", DateUtil.simpleDateFormatAll.format(DateUtil.getDateOfHaveAllTime(klgAttrMap.get("value").toString())));
										}
									}
								}
								} catch (Exception e) {
									e.printStackTrace();
								}
								attrMap.put("currentUnit", currentUnit);
								attrMap.put("name", templateKeysJson.getString("name"));
								attrMap.put("displayName", templateKeysJson.getString("displayName"));
								String templateId = templateKeysJson.getString("templateId");
//								if(templateId.contains(".")){
//									templateId = templateId.split(",")[0];
//								}
								attrMap.put("templateId", templateId);
								attr.add(attrMap);
								//移除无用的value
								klgAttrMap.remove("value");
								klgAttrMap.put("attrs", attr);
								result.put(knowledgeVersionedId, klgAttrMap);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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

//	public static void main(String[] args) {
//		List<String> ids = new ArrayList<String>();
//		ids.add("7734");
//		getKnowledgeAttr(ids);
//	}
}
