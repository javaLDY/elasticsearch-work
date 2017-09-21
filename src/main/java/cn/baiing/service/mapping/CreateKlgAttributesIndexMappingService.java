package cn.baiing.service.mapping;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;

public class CreateKlgAttributesIndexMappingService {
	
	/**
	 * 创建初始数据mapping
	 */
	public static void createBasicKlgAndAttrIndex(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.KLG_BASIC_ATTR_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.KLG_BASIC_ATTR_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("byLabel").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("byName").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("knowledgeId").field("type", "keyword").endObject()
					.startObject("knowledgeVersionedId").field("type", "keyword").endObject()
					.startObject("effectStartTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("effectEndTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("startTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("endTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("publishTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("channel").field("type", "keyword").endObject()
					.startObject("authority").field("type", "keyword").endObject()
					.startObject("mongoId").field("type", "keyword").endObject()
					.startObject("lastUpdatedTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("templateId").field("type", "keyword").endObject()
					.startObject("locIds").field("type", "keyword").endObject()
					.startObject("clickNum").field("type", "keyword").endObject()
					.startObject("value").field("type", "keyword").field("ignore_above", "256").endObject()
					.startObject("keyId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("dataType").field("type", "integer").endObject()
					.startObject("currentUnit").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.endObject()
					.endObject()
					.endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.KLG_BASIC_ATTR_INDEX)
					 .type(IndexRelationConstant.KLG_BASIC_ATTR_TYPE).source(klgMapping);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {      
			e.printStackTrace();
		}
	}
	
	/**
	 * 穿件组合知识主体和属性值mapping
	 */
	public static void createKlgAttributesIndex(){
		TransportClient client = TransportUtil.buildClient();
		client.admin().indices().prepareCreate(IndexRelationConstant.KLG_ATTR_INDEX).execute().actionGet();
		try {
			XContentBuilder klgMapping = XContentFactory.jsonBuilder().startObject()
					.startObject(IndexRelationConstant.KLG_ATTR_TYPE).startObject(IndexRelationConstant.PROPERTIES)
					.startObject("name").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("byLabel").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("byName").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD).endObject()
					.startObject("knowledgeId").field("type", "keyword").endObject()
					.startObject("knowledgeVersionedId").field("type", "keyword").endObject()
					.startObject("effectStartTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("effectEndTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("startTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("endTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("publishTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("channel").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("authority").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("mongoId").field("type", "keyword").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("lastUpdatedTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("templateId").field("type", "keyword").endObject()
					.startObject("locIds").field("type", "keyword").endObject()
					.startObject("clickNum").field("type", "integer").endObject()
					.startObject("attrs").field("type", "nested")
					.startObject(IndexRelationConstant.PROPERTIES)
					.startObject("keyId").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.startObject("integerValue").field("type", "integer").endObject()
					.startObject("dateValue").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
					.startObject("value").field("type", "text").field("analyzer", IndexRelationConstant.IK_ANALYZER_MAX_WORD)
					.startObject("fields")
					.startObject("valueKeyword").field("type", "keyword").field("ignore_above", "256")
					.endObject()
					.endObject()
					.endObject()
					.startObject("dataType").field("type", "integer").endObject()
					.startObject("currentUnit").field("type", "string").field("index",IndexRelationConstant.NOT_ANALYZED).endObject()
					.endObject()
					.endObject()
					.endObject()
					.endObject()
					.endObject();
			 PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexRelationConstant.KLG_ATTR_INDEX).type(IndexRelationConstant.KLG_ATTR_TYPE).source(klgMapping);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		createBasicKlgAndAttrIndex();
		createKlgAttributesIndex();
	}
}
