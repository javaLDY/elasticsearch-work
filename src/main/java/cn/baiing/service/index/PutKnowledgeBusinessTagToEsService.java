package cn.baiing.service.index;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;
import cn.baiing.model.KnowledgeAttachmentJsonBuilder;
import cn.baiing.model.KnowledgeBusinessTagJsonBuilder;

@Service
public class PutKnowledgeBusinessTagToEsService {
	
	public void bulkPutKnowledgeBusinessTagToEs(List<Map<String, Object>> knowledgeBusinessTags){
		String knowledgeVersionedId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledgeBusinessTags)){
				try {
					for(Map<String, Object> map : knowledgeBusinessTags){
						knowledgeVersionedId = map.get("knowledgeVersionedId").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_BUSINESSTAG_INDEX).type(IndexRelationConstant.KLG_BUSINESSTAG_TYPE)
						.source(KnowledgeBusinessTagJsonBuilder.createKlgBusinessTagJsonByMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + knowledgeVersionedId);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}

}
