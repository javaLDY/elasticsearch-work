package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeNumericAttributesDao;

@Service
public class KnowledgeNumericAttributesService {

	@Autowired
	private KnowledgeNumericAttributesDao knowledgeNumericAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeNumberAttributes(List<String> knowledgeVersionedIds){
		return knowledgeNumericAttributesDao.getKnowledgeNumericAttributeByKvid(knowledgeVersionedIds);
	}
}
