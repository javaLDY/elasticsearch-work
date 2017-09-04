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
import cn.baiing.model.TemplateJsonBuilder;

@Service
public class PutTemplateToEsService {

	public void bulkPutTemplateToEs(List<Map<String, Object>> templates){
		String templateId = null;
		TransportClient client = TransportUtil.buildClient();
		BulkProcessor bulkProcessor = TransportUtil.bulkProcess(client);
		try {
			if(CollectionUtils.isNotEmpty(templates)){
				try {
					for(Map<String, Object> map : templates){
						templateId = map.get("id").toString();
						IndexRequest request = new IndexRequest();
						request.index(IndexRelationConstant.TEMPLATE_INDEX).
						type(IndexRelationConstant.TEMPLATE_TYPR)
						.id(templateId)
						.source(TemplateJsonBuilder.createTemplateJsonByMap(map));
	                    bulkProcessor.add(request);
					}
				} catch (Exception e) {
					System.out.println("失败的知识ID：" + templateId);
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bulkProcessor.close();
	}
}
