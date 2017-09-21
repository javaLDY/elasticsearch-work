package cn.baiing.service.query;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.Util.CombineSearchRequstToEsUnit;
import cn.baiing.Util.GeneralUtil;
import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;
import cn.baiing.model.SearchRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class QueryKlgListService {
	
	@Autowired
	private QueryContentService queryContentService;

	/**
	 * 查询知识列表根据关键字
	 * @param searchRequest
	 */
	public JSONObject queryKlgListByKeyword(SearchRequest searchRequest){
		JSONObject searchResult = new JSONObject();
		long total = 0;
		long isUsing = 0;
		long isExpire = 0;
		long isStart = 0;
		
		long startTime = System.currentTimeMillis();
		System.out.println();
		TransportClient client = TransportUtil.buildClient();
		long endTime = System.currentTimeMillis();
		System.out.println("建立连接:" + (endTime - startTime));
		
		long startTime1 = System.currentTimeMillis();
		SearchResponse response = client.prepareSearch(IndexRelationConstant.KLG_ATTR_INDEX)
							.setTypes(IndexRelationConstant.KLG_ATTR_TYPE)
							.setQuery(CombineSearchRequstToEsUnit.combineSelectKeywordQueryBuilder(searchRequest))
							.highlighter(CombineSearchRequstToEsUnit.combineHighLingtRule("name"))
							.addAggregation(CombineSearchRequstToEsUnit.combineTemplateAggregationBuilder())
							.addAggregation(CombineSearchRequstToEsUnit.combineIsExpireAggregationBuilder())
							.addAggregation(CombineSearchRequstToEsUnit.combineIsStartAggregationBuilder())
							.addSort(CombineSearchRequstToEsUnit.combineSortBuilder(searchRequest.getSortParam()))
							.setFrom(0)
							.setSize(15)
							.execute()
							.actionGet();
		long endTime1 = System.currentTimeMillis();
		System.out.println("搜索返回:" + (endTime1 - startTime1));
		
		total = response.getHits().getTotalHits();
		SearchHit[] klgListHits = response.getHits().getHits();
		
		long startTime2 = System.currentTimeMillis();
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
//				String knowledgeVersionedId = singleKlg.getString("knowledgeVersionedId");
//				String templateId = singleKlg.getString("templateId");
//				List<Map<String, Object>> content = queryContentService.getContentByTemplateId(templateId, knowledgeVersionedId);
//				singleKlg.put("name", name);
//				singleKlg.put("content", contentMapToString(content));
				resultArray.add(singleKlg);
			}
		}
		searchResult.put("list", resultArray);
		InternalFilter isExpireAggregation = response.getAggregations().get("isExpire");
		isExpire = isExpireAggregation.getDocCount();
		searchResult.put("isExpire", isExpire);
		
		InternalFilter isStartAggregation = response.getAggregations().get("isStart");
		isStart = isStartAggregation.getDocCount();
		searchResult.put("isStart", isStart);
		isUsing = total - (isExpire + isStart);
		searchResult.put("isUsing", isUsing);
		JSONArray templateAttregationNum = new JSONArray();
		Terms isExpireAgg = response.getAggregations().get("template");
		for (Terms.Bucket entry : isExpireAgg.getBuckets()) {  
			JSONObject json = new JSONObject();
            String key = (String) entry.getKey().toString(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            json.put(key, docCount);
            templateAttregationNum.add(json);
        }
		searchResult.put("templateAttregationNum", templateAttregationNum);
		long endTime2 = System.currentTimeMillis();
		System.out.println("逻辑处理:" + (endTime2 - startTime2));
		
		long startTime3 = System.currentTimeMillis();
		long endTime3 = System.currentTimeMillis();
		System.out.println("关闭连接:" + (endTime3 - startTime3));
		return searchResult;
	}
	
	public static void main(String[] args) {
//		getKnowledgeDetail("123344");
//		SearchRequest request = new SearchRequest();
//		request.setKeyword("4G套餐");
//		long startTime = System.currentTimeMillis();
//		String result = queryKlgListByKeyword(request);
//		long endTime = System.currentTimeMillis();
//		System.out.println("本次搜索用时:" + (endTime - startTime));
//		System.out.println(result);
//		queryKlgListByKeywordImproveAccuracy(request);
//		queryKlgListByAttrs();
		long startTime = System.currentTimeMillis();
		queryKlgListByAttrs();
		long endTime = System.currentTimeMillis();
		System.out.println("总时间:" + (endTime - startTime));
	}
	
	/**
	 * 通过mathc_phrase的slop提高搜索的精准度 slop 关键字的偏移量
	 * @param searchRequest
	 */
	public static void queryKlgListByKeywordImproveAccuracy(SearchRequest searchRequest){
		TransportClient client = TransportUtil.buildClient();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(QueryBuilders.matchQuery("name", searchRequest.getKeyword()));
		boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name", searchRequest.getKeyword()).slop(50));
//		QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("name", searchRequest.getKeyword()));
		SearchResponse searchResponse = client.prepareSearch(IndexRelationConstant.KLG_ATTR_INDEX).setTypes(IndexRelationConstant.KLG_ATTR_TYPE)
				.setQuery(boolQueryBuilder)
				.setFrom(0)
				.setSize(15)
				.execute().actionGet();
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		JSONArray jsonArray = new JSONArray();
		if(searchHits.length > 0){
			for(SearchHit searchHit : searchHits){
				JSONObject json = JSONObject.parseObject(searchHit.getSourceAsString());
				jsonArray.add(json);
			}
		}
		System.out.println(jsonArray);
	}
	
	public static void queryKlgListByAttrs(){
		TransportClient client = TransportUtil.buildClient();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(QueryBuilders.matchQuery("name", "国际漫游流量 一带一路多国流量包 5天不限量").analyzer(IndexRelationConstant.IK_ANALYZER_MAX_WORD));
//		boolQueryBuilder.should(QueryBuilders.nestedQuery("attrs",
//				QueryBuilders.matchQuery("attrs.value", "1643180").minimumShouldMatch("75%")
//				.analyzer(IndexRelationConstant.IK_ANALYZER_MAX_WORD),ScoreMode.Avg));
//		boolQueryBuilder.must(QueryBuilders.termQuery("templateId", 50));
		long startTime = System.currentTimeMillis();
		SearchResponse searchResponse = client.prepareSearch(IndexRelationConstant.KLG_ATTR_INDEX).setTypes(IndexRelationConstant.KLG_ATTR_TYPE)
				.setQuery(boolQueryBuilder)
				.setFrom(0)
				.setSize(30)
				.execute().actionGet();
		long endTime = System.currentTimeMillis();
		System.out.println("知识列表和属性结合时间:" + (endTime - startTime));
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		JSONArray jsonArray = new JSONArray();
		if(searchHits.length > 0){
			for(SearchHit searchHit : searchHits){
				JSONObject json = JSONObject.parseObject(searchHit.getSourceAsString());
				jsonArray.add(json);
			}
		}
		System.out.println(jsonArray);
	}
	
	public String contentMapToString(List<Map<String, Object>> contentListMap){
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(contentListMap)){
			for(Map<String, Object> map : contentListMap){
				sb.append(map.get("displayName").toString());
				sb.append(":");
				sb.append(GeneralUtil.cleanHtml(map.get("value").toString()));
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
}
