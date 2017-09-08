package cn.baiing.service.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.model.DataType;
import cn.baiing.model.IndexRelationConstant;
import cn.baiing.service.SummaryAttrService;

import com.alibaba.fastjson.JSONObject;

@Service
public class QueryContentService {

	@Autowired
	private QueryKlgAttrService queryKlgAttrService;
	
	@Autowired
	private SummaryAttrService summaryAttrService;
	
	@SuppressWarnings("static-access")
	public List<Map<String, Object>> getContentByTemplateId(String templateId, String knowledgeVersionedId){
		List<Map<String, Object>> result = new LinkedList<Map<String,Object>>();
		try {
			Map<String, Object> contentMap = summaryAttrService.getSummaryAttrByTemplateId(templateId);
			if(contentMap != null){
				for(Map.Entry<String, Object> entry : contentMap.entrySet()){
					String index = DataType.belongKeyAttrIndex(entry.getValue().toString());
					String keyId = entry.getKey();
					if(index != null){
						if(index.equals(IndexRelationConstant.KLG_NUMERIC_INDEX)){
							Map<String, Object> attrMap = queryKlgAttrService.getKnowledgeAttrs(IndexRelationConstant.KLG_NUMERIC_INDEX, IndexRelationConstant.KLG_NUMERIC_TYPE,
									keyId, knowledgeVersionedId);
							result.add(attrMap);
						}
						
						if(index.equals(IndexRelationConstant.KLG_TEXT_INDEX)){
							Map<String, Object> attrMap = queryKlgAttrService.getKnowledgeAttrs(IndexRelationConstant.KLG_TEXT_INDEX, IndexRelationConstant.KLG_TEXT_TYPE,
									keyId, knowledgeVersionedId);
							result.add(attrMap);
						}
						
						if(index.equals(IndexRelationConstant.KLG_DATE_INDEX)){
							Map<String, Object> attrMap = queryKlgAttrService.getKnowledgeAttrs(IndexRelationConstant.KLG_DATE_INDEX, IndexRelationConstant.KLG_DATE_TYPE,
									keyId, knowledgeVersionedId);
							result.add(attrMap);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
