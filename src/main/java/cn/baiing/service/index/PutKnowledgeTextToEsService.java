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
import cn.baiing.model.KnowledgeTextJsonBuilder;

@Service
public class PutKnowledgeTextToEsService {

	public void bulkPutKnowledgeTextToEs(List<Map<String, Object>> knowledgeTextAttributes){
		String id = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledgeTextAttributes)){
				try {
					for(Map<String, Object> map : knowledgeTextAttributes){
						id = map.get("id").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_TEXT_INDEX).
						type(IndexRelationConstant.KLG_TEXT_TYPE)
						.id(id)
						.source(KnowledgeTextJsonBuilder.createKlgTextJsonByMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + id);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
	
}
