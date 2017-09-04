package cn.baiing.service.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

@Service
public class QueryKlgService {

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
	
	public static void queryKlgListByKeyword(String keyword){
		TransportClient client = TransportUtil.buildClient();
		AggregationBuilder aggregation = AggregationBuilders  
                .terms("template")  
                .field("templateId");  
		SearchResponse response = client.prepareSearch(IndexRelationConstant.KLG_INDEX)
		.setTypes(IndexRelationConstant.KLG_TYPE)
		.setQuery(QueryBuilders.matchQuery("name", keyword))
		.addAggregation(aggregation)
		.execute()
		.actionGet();
		SearchHit[] klgListHits = response.getHits().getHits();
		Terms aggregation2 = response.getAggregations().get("template");
		for (Terms.Bucket entry : aggregation2.getBuckets()) {  
            String key = (String) entry.getKey().toString(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            System.out.println("key " + key + " doc_count " + docCount);  
        }  
		System.out.println(aggregation2.getBuckets());
//		if(klgListHits.length > 0){
//			for(SearchHit searchHit : klgListHits){
//				System.out.println(searchHit.getSourceAsString());
//			}
//		}
	}
	
	public static void main(String[] args) {
//		getKnowledgeDetail("123344");
		queryKlgListByKeyword("中国移动 VoLTE、4G+功能指导");
	}
}
