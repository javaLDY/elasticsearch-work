package cn.test;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.alibaba.fastjson.JSONObject;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

public class EsTest {

	public static void main(String[] args) {
		TransportClient client = TransportUtil.buildClient();
//		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
//		JSONObject json = new JSONObject();
//		json.put("name", "dapan");
//		json.put("age", 22);
//		IndexRequest request = new IndexRequest();
//		request.index("cn").type("employee").parent("1").source(json);
//		bulkProcessor.add(request);
		
		client.admin().indices().prepareCreate("shengfen").execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject("shengfenType").startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("level").field("type", "integer").endObject()
					.endObject().endObject().endObject()
					.startObject("shiType")
					.startObject("parent").field("type","shengfenType").endObject()
					.startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("level").field("type", "integer").endObject()
					.endObject().endObject();
			PutMappingRequest mappingRequest = Requests.putMappingRequest("shengfen").source(klgMapping);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
