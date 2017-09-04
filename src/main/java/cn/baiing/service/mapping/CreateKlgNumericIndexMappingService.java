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
public class CreateKlgNumericIndexMappingService {

	public static void createNumericIndexMapping(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.KLG_NUMERIC_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.KLG_NUMERIC_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("knowledgeVersionedId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("keyId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("value").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("currentUnit").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.endObject().endObject().endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.KLG_NUMERIC_INDEX).type(IndexRelationConstant.KLG_NUMERIC_TYPE).source(klgMapping);
			 client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
