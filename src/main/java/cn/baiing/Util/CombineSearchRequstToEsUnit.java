package cn.baiing.Util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import cn.baiing.model.SearchRequest;

public class CombineSearchRequstToEsUnit {

	/**
	 * 构建关键字插叙的boolQuery
	 * @param searchRequest
	 * @return
	 */
	public static BoolQueryBuilder combineSelectKeywordQueryBuilder(SearchRequest searchRequest){
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		
		booleanQueryBuilder.must(QueryBuilders.matchQuery("name", searchRequest.getKeyword()));
		if(StringUtils.isNotBlank(searchRequest.getTemplateId())){
			booleanQueryBuilder.must(QueryBuilders.matchQuery("templateId", searchRequest.getTemplateId()));
		}
		
		if(StringUtils.isNotBlank(searchRequest.getLocationId())){
			booleanQueryBuilder.must(QueryBuilders.termsQuery("locIds", searchRequest.getLocationId()));
		}
		
		if(StringUtils.isNotBlank(searchRequest.getChannel())){
			booleanQueryBuilder.must(QueryBuilders.termsQuery("vids", searchRequest.getChannel()));
		}
		
		return booleanQueryBuilder;
	}
	
	/**
	 * 构建通用sortBuilder
	 * 排序参数定义，查询名称=排序规则
	 * @param searchRequest
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public static SortBuilder combineSortBuilder(String sortParam){
		SortBuilders sortBuilders = new SortBuilders();
		SortBuilder sortBuilder = null;
		if(StringUtils.isEmpty(sortParam)){
			sortBuilder = sortBuilders.fieldSort("lastUpdatedTime").order(SortOrder.DESC);
		}else{
			String[] sorts = sortParam.split("=");
			String sortName = sorts[0];
			String orderName = sorts[1];
			if(orderName.equals("desc")){
				sortBuilder = sortBuilders.fieldSort(sortName).order(SortOrder.DESC);
			}else if(orderName.equals("asc")){
				sortBuilder = sortBuilders.fieldSort(sortName).order(SortOrder.ASC);
			}
		}
		return sortBuilder;
	}
	
	/**
	 * 构建高亮规则
	 * @param field 那个字段显示高亮
	 * @return
	 */
	public static HighlightBuilder combineHighLingtRule(String field){
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field(field);
		highlightBuilder.preTags("<span stype=\"color:red\">");
		highlightBuilder.postTags("</span>");
		return highlightBuilder;
	}
	
	/**
	 * 模板聚合规则
	 * @return
	 */
	public static AggregationBuilder combineTemplateAggregationBuilder(){
		//模板聚合
		AggregationBuilder templateAggregation = AggregationBuilders.terms("template").field("templateDisplayName");
		return templateAggregation;
	}
	
	/**
	 * 历史库聚合规则
	 * @return
	 */
	public static AggregationBuilder combineIsExpireAggregationBuilder(){
		AggregationBuilder isExpireAggregation = AggregationBuilders.filter("isExpire", QueryBuilders.rangeQuery("endTime").lte(new Date().getTime()));
		return isExpireAggregation;
	}
	
	/**
	 * 预备库聚合规则
	 * @return
	 */
	public static AggregationBuilder combineIsStartAggregationBuilder(){
		AggregationBuilder isExpireAggregation = AggregationBuilders.filter("isStart", QueryBuilders.rangeQuery("startTime").gte(new Date().getTime()));
		return isExpireAggregation;
	}
}
