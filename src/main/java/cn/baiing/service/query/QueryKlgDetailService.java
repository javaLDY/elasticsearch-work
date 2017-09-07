package cn.baiing.service.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class QueryKlgDetailService {
	
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
				IndexRelationConstant.KLG_ATTACHMENT_INDEX,
				IndexRelationConstant.KLG_BUSINESSTAG_INDEX,
				IndexRelationConstant.KLG_DATE_INDEX,
				IndexRelationConstant.KLG_NUMERIC_INDEX,
				IndexRelationConstant.KLG_SMS_INDEX,
				IndexRelationConstant.KLG_TEXT_INDEX)
		.setTypes(
				IndexRelationConstant.KLG_ATTACHMENT_TYPE,
				IndexRelationConstant.KLG_BUSINESSTAG_TYPE,
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

}
