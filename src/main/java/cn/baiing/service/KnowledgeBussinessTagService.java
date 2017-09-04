package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeBusinessTagsDao;

@Service
public class KnowledgeBussinessTagService {

	@Autowired
	private KnowledgeBusinessTagsDao knowledgeBusinessTagsDao;
	
	public List<Map<String, Object>> getKnowledgeBusinessTags(List<String> knowledgeVersionedIds){
		return knowledgeBusinessTagsDao.getbussinessIdByKvId(knowledgeVersionedIds);
	}
}
