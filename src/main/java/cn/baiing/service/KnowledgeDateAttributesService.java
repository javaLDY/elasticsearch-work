package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeDateAttributesDao;

@Service
public class KnowledgeDateAttributesService {

	@Autowired
	private KnowledgeDateAttributesDao knowledgeDateAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeDateAttributes(List<String> knowledgeVersionedIds){
		return knowledgeDateAttributesDao.getKnowledgeDateAttributeByKvid(knowledgeVersionedIds);
	}
}
