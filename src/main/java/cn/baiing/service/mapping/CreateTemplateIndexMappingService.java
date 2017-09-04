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
public class CreateTemplateIndexMappingService {

	public static void createTemplateIndexMapping(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.TEMPLATE_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.TEMPLATE_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("templateId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("displayName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("sequence").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("creationTime").field("type", "date").endObject()
					.startObject("lastUpdatedTime").field("type", "date").endObject()
					.startObject("parentCatalogId").field("type", "long").endObject()
					.endObject().endObject().endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.TEMPLATE_INDEX).type(IndexRelationConstant.TEMPLATE_TYPE).source(klgMapping);
			 client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
