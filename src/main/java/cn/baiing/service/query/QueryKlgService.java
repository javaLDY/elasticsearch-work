package cn.baiing.service.query;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("name");
		highlightBuilder.preTags("<span stype=\"color:red\">");
		highlightBuilder.postTags("</span>");
		AggregationBuilder aggregation = AggregationBuilders  
                .terms("template")  
                .field("templateId");  
		SearchResponse response = client.prepareSearch(IndexRelationConstant.KLG_INDEX)
		.setTypes(IndexRelationConstant.KLG_TYPE)
		.setQuery(QueryBuilders.matchQuery("name", keyword))
		.highlighter(highlightBuilder)
		.addAggregation(aggregation)
		.execute()
		.actionGet();
		SearchHit[] klgListHits = response.getHits().getHits();
		JSONArray resultArray = new JSONArray();
		if(klgListHits.length > 0){
			for(SearchHit searchHit : klgListHits){
				String sources = searchHit.getSourceAsString();
				JSONObject singleKlg = JSONObject.parseObject(sources);
				Map<String, HighlightField> result = searchHit.getHighlightFields();
				HighlightField highlightField = result.get("name");
				Text[] nameTexts = highlightField.getFragments();
				String name = "";
				for(Text text : nameTexts){
					name += text;
				}
				singleKlg.put("name", name);
				resultArray.add(singleKlg);
			}
		}
		System.out.println(resultArray.toJSONString());
		Terms aggregation2 = response.getAggregations().get("template");
		for (Terms.Bucket entry : aggregation2.getBuckets()) {  
            String key = (String) entry.getKey().toString(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            System.out.println("key " + key + " doc_count " + docCount);  
        }  
	}
	
	public static void main(String[] args) {
//		getKnowledgeDetail("123344");
		queryKlgListByKeyword("中国移动 VoLTE、4G+功能指导");
	}
}
