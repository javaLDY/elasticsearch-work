package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeAttachmentAttributesDao;

@Service
public class KnowledgeAttachmentAttributesService {

	@Autowired
	private KnowledgeAttachmentAttributesDao knowledgeAttachmentAttributesDao;
	
	public List<Map<String, Object>> getKnowledgeAttachmentAttributes(List<String> knowledgeVersionedIds){
		return knowledgeAttachmentAttributesDao.getKnowledgeAttachmentAttributesByKvid(knowledgeVersionedIds);
	}
}
