package cn.baiing.service.query;


import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

@Service
public class QueryKlgAttrService {

	public static Map<String, Object> getKnowledgeAttrs(String index, String type, String keyId, String knowledgeVersionedId){
		Map<String, Object> result = new HashMap<String, Object>();
		TransportClient client = TransportUtil.buildClient();
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		booleanQueryBuilder.must(QueryBuilders.matchQuery("keyId", keyId));
		booleanQueryBuilder.must(QueryBuilders.termQuery("knowledgeVersionedId", knowledgeVersionedId));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.docValueField("value");
		SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
							.setQuery(booleanQueryBuilder)
							.execute().actionGet();
		SearchResponse templateKeyResponse = client.prepareSearch(IndexRelationConstant.TEMPLATE_KEY_INDEX)
				.setTypes(IndexRelationConstant.TEMPLATE_KEY_TYPE)
				.setQuery(QueryBuilders.termQuery("keyId", keyId))
				.execute().actionGet();
		if(searchResponse.getHits().getHits().length > 0){
			result.put("value", searchResponse.getHits().getHits()[0].getSource().get("value"));
		}
		
		if(templateKeyResponse.getHits().getHits().length > 0){
			result.put("displayName", templateKeyResponse.getHits().getHits()[0].getSource().get("displayName"));
			result.put("keyId", templateKeyResponse.getHits().getHits()[0].getSource().get("keyId"));
			result.put("name", templateKeyResponse.getHits().getHits()[0].getSource().get("name"));
		}
		return result;
	}
	
	public static void main(String[] args) {
		getKnowledgeAttrs(IndexRelationConstant.KLG_TEXT_INDEX,IndexRelationConstant.KLG_TEXT_TYPE,"9217425643859847679","104912");
	}
}
