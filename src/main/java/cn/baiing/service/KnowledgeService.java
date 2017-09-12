package cn.baiing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.Util.GetKnowledgeAttrsFromDb;
import cn.baiing.dao.KnowledgeDao;
import cn.baiing.dao.KnowledgeNumericAttributesDao;
import cn.baiing.util.Constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

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
	public List<Long> getKnowledgeVersionedIds(int startPos, int pageSize){
		List<Long> knowledgeVersionedIds = knowledgeDao.getknowledgeVersionedIds(startPos, pageSize);
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
	
	public List<Map> getKnowledgeWithAttrsByKvId(List<Long> knowledgeVersionedIds){
		List<Map> result = new ArrayList<Map>();
		if(CollectionUtils.isNotEmpty(knowledgeVersionedIds)){
			long startTime = System.currentTimeMillis();
//			for(String knowledgeVersionedId : knowledgeVersionedIds){
//				List<Map<String, Object>> attrs = new ArrayList<Map<String,Object>>();
//				Map<String, Object> klgMap = knowledgeDao.getKnowledgeByKvId(knowledgeVersionedId);
//				List<Map<String, Object>> numericAttrs = knowledgeNumericAttributesDao
//						.getKnowledgeNumericAttrByKnowledgeversionedId(knowledgeVersionedId);
//				if(CollectionUtils.isNotEmpty(numericAttrs)){
//					attrs.addAll(numericAttrs);
//				}
//				klgMap.put("attrs", attrs);
//				result.add(klgMap);
//			}
			JSONArray resultList = GetKnowledgeAttrsFromDb.getKnowledgeAttr(knowledgeVersionedIds);
			Gson gson = new Gson();
			result = JSON.parseArray(resultList.toJSONString(), Map.class);
			long endTime = System.currentTimeMillis();
			System.out.println("每批数据循环所用的时间" + (endTime - startTime));
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(System.getenv("baiingRoot"));
		System.out.println(System.getenv("JAVA_HOME"));
	}
}
