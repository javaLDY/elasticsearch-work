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
import cn.baiing.model.TemplateKeyJsonBuilder;

@Service
public class PutTemplateKeyToEsService {

	public void bulkPutTemplateKeysToEs(List<Map<String, Object>> templateKeys){
		String keyId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(templateKeys)){
				try {
					for(Map<String, Object> map : templateKeys){
						keyId = map.get("id").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.TEMPLATE_KEY_INDEX).
						type(IndexRelationConstant.TEMPLATE_KEY_TYPR)
						.id(keyId)
						.source(TemplateKeyJsonBuilder.createTemplateKeyJsonByMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + keyId);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
}
