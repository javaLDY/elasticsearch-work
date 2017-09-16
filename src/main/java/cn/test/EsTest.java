package cn.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.rounding.DateTimeUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.DateUtil;
import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

public class EsTest {

	public static void main(String[] args) {
		TransportClient client = TransportUtil.buildClient();
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("startTime",DateUtil.formatDateAllToString(new Date()));
		client.prepareIndex("test_index", "test_type","4").setSource(map).execute().actionGet();
//		GetResponse response = client.prepareGet().setIndex("test_index")
//				.setType("test_type").setId("2").execute().actionGet();
//		JSONObject json = JSONObject.parseObject(response.getSourceAsString());
//		String startTime = json.getString("startTime");
//		String time;
//		try {
//			time = DateUtil.simpleDateFormatAll.format(DateUtil.dateIos8601simpleDateFormatAll.parse(startTime));
//			System.out.println(time);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
////		client.admin().indices().prepareCreate("test_index").execute().actionGet();
//		
//		try {
//			String klgMapping = XContentFactory.jsonBuilder().startObject()
//					.startObject("test_type").startObject(IndexRelationConstant.PROPERTIES)
//					.startObject("updateTime").field("type", "date").field("format","yyyy-MM-dd HH:mm:ss").endObject()
//					.endObject().endObject().endObject().toString();
//			JSONObject mapping = new JSONObject();
//			
//			 PutMappingRequest mappingRequest = Requests.putMappingRequest("test_index")
//					 .type("test_index").source(klgMapping);
//			 client.admin().indices().putMapping(mappingRequest).actionGet();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
}
