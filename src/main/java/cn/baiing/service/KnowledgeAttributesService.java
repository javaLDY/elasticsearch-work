package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeAttributesDao;

@Service
public class KnowledgeAttributesService {
	
	@Autowired
	private KnowledgeAttributesDao knowledgeAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeAttributes(List<String> knowledgeVersionedIds){
		return knowledgeAttributesDao.getKnowledgeAttributes(knowledgeVersionedIds);
	}

}
