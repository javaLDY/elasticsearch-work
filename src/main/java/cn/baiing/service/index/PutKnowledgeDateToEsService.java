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
import cn.baiing.model.KnowledgeDateJsonBuilder;

@Service
public class PutKnowledgeDateToEsService {

	public void bulkPutKnowledgeDateToEs(List<Map<String, Object>> knowledgeDates){
		String id = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(knowledgeDates)){
				try {
					for(Map<String, Object> map : knowledgeDates){
						id = map.get("id").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_DATE_INDEX)
						.type(IndexRelationConstant.KLG_DATE_TYPE)
						.id(id)
						.source(KnowledgeDateJsonBuilder.createKlgDateJsonByMap(map));
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
