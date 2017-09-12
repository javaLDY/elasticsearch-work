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
import cn.baiing.model.KnowledgeJsonBuilder;

@Service
public class PutKnowledgesToEsService {

	/**
	 * 向es中灌入数据
	*/
	public void bulkPutKnowledgesToEs(List<Map> knowledges){
		String knowledgeId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledges)){
				try {
					for(Map<String, Object> map : knowledges){
						knowledgeId = map.get("knowledgeId").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_INDEX).type(IndexRelationConstant.KLG_TYPE).id(knowledgeId)
						.source(KnowledgeJsonBuilder.createKlgJsonByKlgMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + knowledgeId);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
	
	public void buldPutKnowledgesWithAttrToEs(List<Map<String, Object>> knowledges){
		String knowledgeId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledges)){
				try {
					for(Map<String, Object> map : knowledges){
						knowledgeId = map.get("knowledgeId").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_INDEX).type(IndexRelationConstant.KLG_TYPE).id(knowledgeId)
						.source(KnowledgeJsonBuilder.createKlgJsonByKlgMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + knowledgeId);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
}
