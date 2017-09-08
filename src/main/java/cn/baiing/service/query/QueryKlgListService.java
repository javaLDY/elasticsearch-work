package cn.baiing.service.query;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
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
	public void queryKlgListByKeyword(SearchRequest searchRequest){
		JSONObject searchResult = new JSONObject();
		long total = 0;
		long isUsing = 0;
		long isExpire = 0;
		long isStart = 0;
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
		total = response.getHits().getTotalHits();
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
				String knowledgeVersionedId = singleKlg.getString("knowledgeVersionedId");
				String templateId = singleKlg.getString("templateId");
				List<Map<String, Object>> content = queryContentService.getContentByTemplateId(templateId, knowledgeVersionedId);
				singleKlg.put("name", name);
				singleKlg.put("content", contentMapToString(content));
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
		searchResult.put("isUsing", total - (isExpire + isStart));
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
		System.out.println(searchResult.toJSONString());
		client.close();
	}
	
	public static void main(String[] args) {
//		getKnowledgeDetail("123344");
		SearchRequest request = new SearchRequest();
		request.setKeyword("套餐");
		request.setChannel("3");
		request.setLocationId("31410");
//		queryKlgListByKeyword(request);
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
