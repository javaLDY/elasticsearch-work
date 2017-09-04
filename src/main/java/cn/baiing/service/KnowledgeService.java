package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.KnowledgeDao;

@Service
public class KnowledgeService {
	
	@Autowired
	private KnowledgeDao knowledgeDao;
	
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
}
