package cn.baiing.service.mapping;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

@Service
public class CreateTemplateKeyIndexMappingService {
	
	public static void createTemplateIndexMapping(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.TEMPLATE_KEY_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.TEMPLATE_KEY_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("keyId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("templateId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("displayName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("sequence").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("creationTime").field("format", "yyyy-MM-dd HH:mm:ss").field("type", "date").endObject()
					.startObject("lastUpdatedTime").field("format", "yyyy-MM-dd HH:mm:ss").field("type", "date").endObject()
					.startObject("dataType").field("type", "integer").endObject()
					.startObject("options").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("defaultUnit").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.endObject().endObject().endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.TEMPLATE_KEY_INDEX).type(IndexRelationConstant.TEMPLATE_KEY_TYPE).source(klgMapping);
			 client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		createTemplateIndexMapping();
	}
}
