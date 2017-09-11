package cn.baiing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeDao;
import cn.baiing.dao.KnowledgeNumericAttributesDao;

@Service
public class KnowledgeService {
	
	@Autowired
	private KnowledgeDao knowledgeDao;
	
	@Autowired
	private KnowledgeNumericAttributesDao knowledgeNumericAttributesDao;
	
	/**
	 * 获取上线的只是ID
	 * @return
	 */
	public List<String> getKnowledgeVersionedIds(int startPos, int pageSize){
		List<String> knowledgeVersionedIds = knowledgeDao.getknowledgeVersionedIds(startPos, pageSize);
		return knowledgeVersionedIds;
	}

	/**
	 * 根据只是kvid获取知识主体
	 * @param knowledgeVersionedIds
	 * @return
	 */
	public List<Map<String, Object>> getKnowlegesByKvid(List<String> knowledgeVersionedIds){
		List<Map<String, Object>> knowledges = knowledgeDao.getKnowledges(knowledgeVersionedIds);
		return knowledges;
	}
	
	public List<Map<String, Object>> getKnowledgeWithAttrsByKvId(List<String> knowledgeVersionedIds){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if(CollectionUtils.isNotEmpty(knowledgeVersionedIds)){
			for(String knowledgeVersionedId : knowledgeVersionedIds){
				List<Map<String, Object>> attrs = new ArrayList<Map<String,Object>>();
				Map<String, Object> klgMap = knowledgeDao.getKnowledgeByKvId(knowledgeVersionedId);
				List<Map<String, Object>> numericAttrs = knowledgeNumericAttributesDao
						.getKnowledgeNumericAttrByKnowledgeversionedId(knowledgeVersionedId);
				if(CollectionUtils.isNotEmpty(numericAttrs)){
					attrs.addAll(numericAttrs);
				}
				klgMap.put("attrs", attrs);
				result.add(klgMap);
			}
		}
		return result;
	}
}
