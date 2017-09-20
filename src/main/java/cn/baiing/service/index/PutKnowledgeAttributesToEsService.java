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
import cn.baiing.model.KnowledgeAttributesJsonBuilder;
import cn.baiing.model.KnowledgeJsonBuilder;

@Service
public class PutKnowledgeAttributesToEsService {
	
	/**
	 * 向es中灌入数据
	*/
	public void bulkPutBasicKlgAttributesToEs(List<Map<String, Object>> knowledges){
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.makeklgAttrBulkProcessor(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledges)){
				try {
					for(Map<String, Object> map : knowledges){
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_BASIC_ATTR_INDEX)
						.type(IndexRelationConstant.KLG_BASIC_ATTR_TYPE)
						.source(KnowledgeAttributesJsonBuilder.createBasicKlgAttrsJsonByKlgAndAttrMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
	
	/**
	 * 向es中灌入数据
	*/
	public void bulkPutKlgAttributesToEs(Map<String, Map<String, Object>> knowledges){
		String knowledgeId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.makeklgAttrBulkProcessor(client);
		try {
			if((knowledges != null) && (!knowledges.isEmpty())){
				try {
					for(Map.Entry<String, Map<String, Object>> map : knowledges.entrySet()){
						knowledgeId = map.getKey();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_ATTR_INDEX)
						.type(IndexRelationConstant.KLG_ATTR_TYPE).id(knowledgeId)
						.source(KnowledgeAttributesJsonBuilder.createKlgAndAttrJson(map.getValue()));
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
