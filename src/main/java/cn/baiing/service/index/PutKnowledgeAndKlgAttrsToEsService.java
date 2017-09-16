package cn.baiing.service.index;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.Util.TransportUtil;
import cn.baiing.model.IndexRelationConstant;
import cn.baiing.model.KnowledgeJsonBuilder;
import cn.baiing.service.MakeKlgAndKlgAttrIndexService;

@Service
public class PutKnowledgeAndKlgAttrsToEsService {

	@Autowired
	private MakeKlgAndKlgAttrIndexService makeKlgAndKlgAttrIndexService;
	
	/**
	 * 将知识主体和属性放到es中去
	 * @param knowledgeVersionedIds
	 */
	public void putKnowledgeAndKlgAttrsToEs(List<String> knowledgeVersionedIds){
		List<Map<String, Object>> klgAndKlgAttrs = makeKlgAndKlgAttrIndexService
				.combineKlgAngKlgAttr(knowledgeVersionedIds);
		String knowledgeId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.makeklgAttrBulkProcessor(client);
		try {
			if(CollectionUtils.isNotEmpty(klgAndKlgAttrs)){
				try {
					for(Map<String, Object> map : klgAndKlgAttrs){
						knowledgeId = map.get("knowledgeId").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.KLG_ATTR_INDEX).type(IndexRelationConstant.KLG_ATTR_TYPE).id(knowledgeId)
						.source(KnowledgeJsonBuilder.createKlgJsonByKlgAndAttrMap(map));
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
