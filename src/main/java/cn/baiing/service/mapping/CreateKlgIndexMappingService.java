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
public class CreateKlgIndexMappingService {
	
	public static void createKlgIndex(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.KLG_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.KLG_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("knowledgeId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("knowledgeVersionedId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("effectStartTime").field("type", "date").endObject()
					.startObject("effectEndTime").field("type", "date").endObject()
					.startObject("startTime").field("type", "date").endObject()
					.startObject("endTime").field("type", "date").endObject()
					.startObject("vids").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("mongoId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("lastUpdatedTime").field("type", "date").endObject()
					.startObject("templateId").field("type", "long").endObject()
					.startObject("locIds").field("type", "long").endObject()
					.startObject("locationName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("templateDisplayName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("templateName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("clickNum").field("type", "integer").endObject()
					.startObject("attrs").field("type", "nested")
					.startObject(IndexRelationConstant.PROPERTIES)
					.startObject("keyId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("value").field("type", "text").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("knowledgeVersionedId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("dataType").field("type", "integer").endObject()
					.startObject("name").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("currentUnit").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("sequence").field("type", "integer").endObject()
					.startObject("displayName").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.endObject()
					.endObject()
					.endObject().endObject().endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.KLG_INDEX).type(IndexRelationConstant.KLG_TYPE).source(klgMapping);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		createKlgIndex();
	}
}
