package elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.lucene.queryparser.xml.QueryBuilderFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class TestService {
	
	private static String index = "ldy_index";
	private static String type = "ldy_type";
	
	public static void main(String[] args) {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		try {
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
			queryByField(client, "是个", "title");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 范围查询
	 * @param from
	 * @param to
	 * @param client
	 */
	public static void rangeQuery(int from, int to, TransportClient client){
		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.rangeQuery("price").from(from).to(to))
				.addSort("price",SortOrder.DESC).execute().actionGet();
				SearchHit[] searchHits = response.getHits().getHits();
				for(SearchHit searchHit : searchHits){
					System.out.println(searchHit.getSource().toString());
					System.out.println(searchHit.getHighlightFields().toString());
				}
	}
	
	/**
	 * 多个字段联合查询，排序，高亮
	 * @param fields
	 * @param keyword
	 */
	public static void queryByfields(TransportClient client, String[] fields, String keyword){
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<red>").postTags("</red>").field("title").field("content");
		
		SearchResponse response = client.prepareSearch(index).setTypes(type)
			.setQuery(QueryBuilders.multiMatchQuery(keyword, fields))
			.addSort("price", SortOrder.DESC)
			.highlighter(highlightBuilder)
			.execute().actionGet();
		SearchHit[] searchHits = response.getHits().getHits();
		for(SearchHit searchHit : searchHits){
			System.out.println(searchHit.getSource().toString());
			System.out.println(searchHit.getHighlightFields().toString());
		}
	}
	
	/**
	 * 单一字段查询，分页，高亮
	 * @param client
	 * @param keyword
	 */
	public static void queryByField(TransportClient client, String keyword, String field){
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<red>").postTags("</red>").field("title");
		SearchResponse response = client.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchQuery(field, keyword))
				.setFrom(0).setSize(10).highlighter(highlightBuilder)
				.execute().actionGet();
		SearchHit[] searchHits = response.getHits().getHits();
		for(SearchHit searchHit : searchHits){
			System.out.println(searchHit.getSource().toString());
			System.out.println(searchHit.getHighlightFields().toString());
		}
	}
	
	/**
	 * 根据id修改对应字段的数据
	 * @param client
	 * @param id
	 */
	public static void updateDataById(TransportClient client, String id, String field, String updateContent){
		try {
			client.prepareUpdate(index, type, id).setDoc(XContentFactory.jsonBuilder().startObject()
					.field(field, updateContent).endObject()).execute().actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据id获取doc
	 * @param client
	 * @param id
	 */
	public static void getDataById(TransportClient client, String id){
		GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
		System.out.println(response.getSourceAsString());
		
	}
	/**
	 * 向索引中增加数据
	 * @param client
	 */
	public static void  addData(TransportClient client){
//		client.prepareIndex(index, type, "1").setSource(createDataToIndex1()).execute().actionGet();
//		client.prepareIndex(index, type, "2").setSource(createDataToIndex2()).execute().actionGet();
//		client.prepareIndex(index, type, "3").setSource(createDataToIndex3()).execute().actionGet();
		client.prepareIndex(index, type, "4").setSource(createDataToIndex4()).execute().actionGet();
		client.prepareIndex(index, type, "5").setSource(createDataToIndex5()).execute().actionGet();
		client.prepareIndex(index, type, "6").setSource(createDataToIndex6()).execute().actionGet();
		client.prepareIndex(index, type, "7").setSource(createDataToIndex7()).execute().actionGet();
	}
	
	/**
	 * 构建索引对应的字段限制
	 * @return
	 */
	public static XContentBuilder createMapping(){
		XContentBuilder contentBuilder = null;
		try {
			contentBuilder = XContentFactory.jsonBuilder()
					.startObject()
					.startObject(type)
					.startObject("properties")
					.startObject("title").field("type", "text").endObject()
					.startObject("price").field("type", "long").endObject()
					.startObject("content").field("type", "text").endObject()
					.startObject("address").field("type", "string").field("index","not_analyzed").endObject()
					.endObject().endObject().endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder;
	}
	
	/**
	 * 创建索引
	 * @param client
	 */
	public static void createIndex(TransportClient client){
		client.admin().indices().prepareCreate(index).addMapping(type, createMapping()).execute().actionGet();
	}

	/**
	 * 构建数据1
	 * @return
	 */
	public static XContentBuilder createDataToIndex1(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "my a little dog")
			.field("price", 32)
			.field("content", "it is a beautiful dog")
			.field("address", "fengrun")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	/**
	 * 构建数据2
	 * @return
	 */
	public static XContentBuilder createDataToIndex2(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "my a little cat")
			.field("price", 50)
			.field("content", "it is a beautiful cat")
			.field("address", "tangshan")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	/**
	 * 构建数据3
	 * @return
	 */
	public static XContentBuilder createDataToIndex3(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "my a little bird")
			.field("price", 65)
			.field("content", "it is a beautiful bird")
			.field("address", "fengnan")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	public static XContentBuilder createDataToIndex7(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "我的名字叫刘董杨")
			.field("price", 365)
			.field("content", "我的英文名字还是刘董杨")
			.field("address", "丰润")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	public static XContentBuilder createDataToIndex4(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "胡玲是一个吃货")
			.field("price", 135)
			.field("content", "但是她有点小胖")
			.field("address", "丰润")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	public static XContentBuilder createDataToIndex5(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "胡丹是个笨蛋")
			.field("price", 165)
			.field("content", "但是呢他有点小聪明")
			.field("address", "遵化")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
	
	public static XContentBuilder createDataToIndex6(){
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject()
			.field("title", "爱犬是个狗吗")
			.field("price", 265)
			.field("content", "其实他是个人")
			.field("address", "遵化")
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xContentBuilder;
	}
}
