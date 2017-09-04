package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeSmsAttributesDao;


@Service
public class KnowledgeSmsAttributesService {
	
	@Autowired
	private KnowledgeSmsAttributesDao knowledgeSmsAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeSmsAttributes(List<String> knowledgeVersionedIds){
		return knowledgeSmsAttributesDao.getKnowledgeSmsAttributesByKvid(knowledgeVersionedIds);
	}

}
