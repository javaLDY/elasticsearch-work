package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.service.index.PutKnowledgeAttachmentAttributesToEsService;
import cn.baiing.service.index.PutKnowledgeBusinessTagToEsService;
import cn.baiing.service.index.PutKnowledgeDateToEsService;
import cn.baiing.service.index.PutKnowledgeNumericToEsService;
import cn.baiing.service.index.PutKnowledgeSmsToEsService;
import cn.baiing.service.index.PutKnowledgeTextToEsService;
import cn.baiing.service.index.PutKnowledgesToEsService;
import cn.baiing.service.index.PutTemplateKeyToEsService;
import cn.baiing.service.index.PutTemplateToEsService;

@Service
public class StartPutAllDataToEsService {

	@Autowired
	private KnowledgeService knowledgeService;
	
	@Autowired
	private KnowledgeAttachmentAttributesService knowledgeAttachmentAttributesService;
	
	@Autowired
	private KnowledgeBussinessTagService knowledgeBussinessTagService;
	
	@Autowired
	private KnowledgeDateAttributesService knowledgeDateAttributesService;
	
	@Autowired
	private KnowledgeNumericAttributesService knowledgeNumericAttributesService;
	
	@Autowired
	private KnowledgeSmsAttributesService knowledgeSmsAttributesService;
	
	@Autowired
	private KnowledgeTextAttributesService knowledgeTextAttributesService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private TemplateKeyService templateKeyService;
	///////////////////////////////////////////////////灌入es Service///////////////////////////////////////
	@Autowired
	private PutKnowledgesToEsService putKnowledgesToEsService;
	
	@Autowired
	private PutKnowledgeAttachmentAttributesToEsService putKnowledgeAttachmentAttributesToEsService;
	
	@Autowired
	private PutKnowledgeBusinessTagToEsService putKnowledgeBusinessTagToEsService;
	
	@Autowired
	private PutKnowledgeDateToEsService putKnowledgeDateToEsService;
	
	@Autowired
	private PutKnowledgeNumericToEsService putKnowledgeNumericToEsService;
	
	@Autowired
	private PutKnowledgeSmsToEsService putKnowledgeSmsToEsService;
	
	@Autowired
	private PutKnowledgeTextToEsService putKnowledgeTextToEsService;
	
	@Autowired
	private PutTemplateToEsService putTemplateToEsService;
	
	@Autowired
	private PutTemplateKeyToEsService putTemplateKeyToEsService;
	
	/**
	 * 灌入知识主题和知识相关属性的数据
	 */
	public void startPullAllDataToEs(){
		int startPos = 0;
		int pageSize = 5000;
		int totalSize = 0;
		long startTime = System.currentTimeMillis();
		while(true){
			List<String> knowledgeVersionedIds = knowledgeService.getKnowledgeVersionedIds(startPos, pageSize);
			if(CollectionUtils.isEmpty(knowledgeVersionedIds)){
				break;
			}
			System.out.println("--------------------------------开始灌入知识主题数据----------------------------------------");
			//灌入知识主体数据
			List<Map<String, Object>> knowledges = knowledgeService.getKnowlegesByKvid(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledges)){
				putKnowledgesToEsService.bulkPutKnowledgesToEs(knowledges);
			}
			//灌入知识相关附件类型数据
			List<Map<String, Object>> knowledgeAttachments = knowledgeAttachmentAttributesService.getKnowledgeAttachmentAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeAttachments)){
				putKnowledgeAttachmentAttributesToEsService.bulkPutKnowledgeAttachmentAttributesToEs(knowledgeAttachments);
			}
			//灌入知识归属关系类型数据
			List<Map<String, Object>> knowledgeBusinessTags = knowledgeBussinessTagService.getKnowledgeBusinessTags(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeBusinessTags)){
				putKnowledgeBusinessTagToEsService.bulkPutKnowledgeBusinessTagToEs(knowledgeBusinessTags);
			}
			//灌入知识时间数据
			List<Map<String, Object>> knowledgeDates = knowledgeDateAttributesService.getKnowledgeDateAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeDates)){
				putKnowledgeDateToEsService.bulkPutKnowledgeDateToEs(knowledgeDates);
			}
			//灌入知识数值类型数据
			List<Map<String, Object>> knowledgeNumerices = knowledgeNumericAttributesService.getKnowledgeNumberAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeNumerices)){
				putKnowledgeNumericToEsService.bulkPutKnowledgeNumericToEs(knowledgeNumerices);
			}
			//灌入知识短信类型数据
			List<Map<String, Object>> knowledgeSmsList = knowledgeSmsAttributesService.getKnowledgeSmsAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeSmsList)){
				putKnowledgeSmsToEsService.bulkPutKnowledgeSmsToEs(knowledgeSmsList);
			}
			List<Map<String, Object>> knowledgeTextAttributes = knowledgeTextAttributesService.getKnowledgeTextAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeTextAttributes)){
				putKnowledgeTextToEsService.bulkPutKnowledgeSmsToEs(knowledgeTextAttributes);
			}
 			totalSize += knowledgeVersionedIds.size();
			if(knowledgeVersionedIds.size() < 5000){
				break;
			}
			
			startPos += 5000;
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("总共拉取了:" + totalSize);
		System.out.println("总共花了:" + (endTime - startTime) + "ms");
		System.out.println("--------------------------------结束灌入知识主题数据----------------------------------------");
	}
	
	/**
	 * 灌入模板和模板属性数据
	 */
	public void startPutAllTemplateAttrsToEs(){
		long startTime = System.currentTimeMillis();
		System.out.println("--------------------------------开始灌入模板属性数据----------------------------------------");
		//灌入模板数据
		List<Map<String, Object>> templates = templateService.getTemplates();
		if(CollectionUtils.isNotEmpty(templates)){
			putTemplateToEsService.bulkPutTemplateToEs(templates);
		}
		
		List<Map<String, Object>> templateKeys = templateKeyService.getTemplateKeys();
		if(CollectionUtils.isNotEmpty(templateKeys)){
			putTemplateKeyToEsService.bulkPutTemplateKeysToEs(templateKeys);
		}
		System.out.println("--------------------------------结束灌入模板属性数据----------------------------------------");
		long endTime = System.currentTimeMillis();
		System.out.println("总共花了:" + (endTime - startTime) + "ms");
	}
}
