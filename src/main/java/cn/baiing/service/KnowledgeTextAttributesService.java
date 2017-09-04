package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeTextAttributesDao;

@Service
public class KnowledgeTextAttributesService {

	@Autowired
	private KnowledgeTextAttributesDao knowledgeTextAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeTextAttributes(List<String> knowledgeVersionedIds){
		return knowledgeTextAttributesDao.getKnowledgeTextAttributesByKvid(knowledgeVersionedIds);
	}
}
