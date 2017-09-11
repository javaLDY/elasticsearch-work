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
		
		client.admin().indices().prepareCreate("company").execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject("bumen").startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("address").field("type", "string").endObject()
					.startObject("employee").field("type", "nested")
					.startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("age").field("type", "integer").endObject()
					.endObject()
					.endObject()
					.endObject().endObject().endObject();
			PutMappingRequest mappingRequest = Requests.putMappingRequest("company").type("bumen").source(klgMapping);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
