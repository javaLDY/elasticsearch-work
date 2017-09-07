package cn.baiing.service.query;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import cn.baiing.Util.CombineSearchRequstToEsUnit;
import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;
import cn.baiing.model.SearchRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class QueryKlgListService {

	/**
	 * 查询知识列表根据关键字
	 * @param searchRequest
	 */
	public static void queryKlgListByKeyword(SearchRequest searchRequest){
		TransportClient client = TransportUtil.buildClient();
		
		SearchResponse response = client.prepareSearch(IndexRelationConstant.KLG_INDEX)
							.setTypes(IndexRelationConstant.KLG_TYPE)
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
		InternalFilter isExpireAggregation = response.getAggregations().get("isExpire");
		System.out.println("历史库数量:" + isExpireAggregation.getDocCount());
		
		InternalFilter isStartAggregation = response.getAggregations().get("isStart");
		System.out.println("知识库数量:" + isStartAggregation.getDocCount());
		
		Terms isExpireAgg = response.getAggregations().get("template");
		for (Terms.Bucket entry : isExpireAgg.getBuckets()) {  
            String key = (String) entry.getKey().toString(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            System.out.println("key " + key + " doc_count " + docCount);  
        } 
	}
	
	public static void main(String[] args) {
//		getKnowledgeDetail("123344");
		SearchRequest request = new SearchRequest();
		request.setKeyword("套餐");
		request.setChannel("3");
		queryKlgListByKeyword(request);
	}
}
