package cn.baiing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.service.index.PutKnowledgeAndKlgAttrsToEsService;
import cn.baiing.service.index.PutKnowledgeAttachmentAttributesToEsService;
import cn.baiing.service.index.PutKnowledgeAttributesToEsService;
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
	
	@Autowired
	private KnowledgeAttributesService knowledgeAttributesService;
	
	@Autowired
	private MakeKlgAttributesIndexService makeKlgAttributesIndexService;
	
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
	
	@Autowired
	private PutKnowledgeAndKlgAttrsToEsService putKnowledgeAndKlgAttrsToEsService;
	
	@Autowired
	private PutKnowledgeAttributesToEsService putKnowledgeAttributesToEsService;
	
	/**
	 * 灌入知识主题和知识相关属性的数据
	 */
	public void startPullAllDataToEs(){
		int startPos = 0;
		int pageSize = 3000;
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
				System.out.println("拉完了---knowledges");
			}
			//灌入知识相关附件类型数据
			List<Map<String, Object>> knowledgeAttachments = knowledgeAttachmentAttributesService.getKnowledgeAttachmentAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeAttachments)){
				putKnowledgeAttachmentAttributesToEsService.bulkPutKnowledgeAttachmentAttributesToEs(knowledgeAttachments);
				System.out.println("拉完了---attachment");
			}
			//灌入知识归属关系类型数据
			List<Map<String, Object>> knowledgeBusinessTags = knowledgeBussinessTagService.getKnowledgeBusinessTags(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeBusinessTags)){
				putKnowledgeBusinessTagToEsService.bulkPutKnowledgeBusinessTagToEs(knowledgeBusinessTags);
				System.out.println("拉完了---business");
			}
			//灌入知识时间数据
			List<Map<String, Object>> knowledgeDates = knowledgeDateAttributesService.getKnowledgeDateAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeDates)){
				putKnowledgeDateToEsService.bulkPutKnowledgeDateToEs(knowledgeDates);
				System.out.println("拉完了---date");
			}
			//灌入知识数值类型数据
			List<Map<String, Object>> knowledgeNumerices = knowledgeNumericAttributesService.getKnowledgeNumberAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeNumerices)){
				putKnowledgeNumericToEsService.bulkPutKnowledgeNumericToEs(knowledgeNumerices);
				System.out.println("拉完了---numeric");
			}
			//灌入知识短信类型数据
			List<Map<String, Object>> knowledgeSmsList = knowledgeSmsAttributesService.getKnowledgeSmsAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeSmsList)){
				putKnowledgeSmsToEsService.bulkPutKnowledgeSmsToEs(knowledgeSmsList);
				System.out.println("拉完了---sms");
			}
			//灌入知识文本类型数据
			List<Map<String, Object>> knowledgeTextAttributes = knowledgeTextAttributesService.getKnowledgeTextAttributes(knowledgeVersionedIds);
			if(CollectionUtils.isNotEmpty(knowledgeTextAttributes)){
				putKnowledgeTextToEsService.bulkPutKnowledgeTextToEs(knowledgeTextAttributes);
				System.out.println("拉完了---text");
			}
 			totalSize += knowledgeVersionedIds.size();
			if(knowledgeVersionedIds.size() < 3000){
				break;
			}
			
			startPos += 3000;
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
	
	/**
	 * 将知识主体和属性放到es中去
	 */
	public void startPutKnowledgeAndKlgAttrToEs(){
		int startPos = 0;
		int pageSize = 1000;
		int totalSize = 0;
		long startTime = System.currentTimeMillis();
		while(true){
			List<String> knowledgeVersionedIds = knowledgeService.getKnowledgeVersionedIds(startPos, pageSize);
			if(CollectionUtils.isEmpty(knowledgeVersionedIds)){
				break;
			}
			System.out.println("--------------------------------开始灌入知识主体和知识相关属性数据----------------------------------------");
			putKnowledgeAndKlgAttrsToEsService.putKnowledgeAndKlgAttrsToEs(knowledgeVersionedIds);
			totalSize += knowledgeVersionedIds.size();
			if(knowledgeVersionedIds.size() < 1000){
				break;
			}
			
			startPos += 1000;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共拉取了:" + totalSize);
		System.out.println("总共花了:" + (endTime - startTime) + "ms");
		System.out.println("--------------------------------结束灌入知识主体和知识相关属性数据----------------------------------------");
//		List<String> knowledgeVersionedIds = new ArrayList<String>();
//		knowledgeVersionedIds.add("104280");
//		putKnowledgeAndKlgAttrsToEsService.putKnowledgeAndKlgAttrsToEs(knowledgeVersionedIds);
	}
	
	/**
	 * 将知识主体和属性得基本数据放到es中去
	 */
	public void startPutBasicKnowledgeAttributesToEs(){
		int startPos = 0;
		int pageSize = 1000;
		int totalSize = 0;
		long startTime = System.currentTimeMillis();
		while(true){
			List<String> knowledgeVersionedIds = knowledgeService.getKnowledgeVersionedIds(startPos, pageSize);
			if(CollectionUtils.isEmpty(knowledgeVersionedIds)){
				break;
			}
			System.out.println("--------------------------------开始灌入 "+startPos+" - "+pageSize+" 知识主体和知识相关属性数据----------------------------------------");
			List<Map<String, Object>> knowledges = knowledgeAttributesService.getKnowledgeAttributes(knowledgeVersionedIds);
			putKnowledgeAttributesToEsService.bulkPutBasicKlgAttributesToEs(knowledges);
			totalSize += knowledgeVersionedIds.size();
			if(knowledgeVersionedIds.size() < 1000){
				break;
			}
			
			startPos += 1000;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共拉取了:" + totalSize);
		System.out.println("总共花了:" + (endTime - startTime) + "ms");
		System.out.println("--------------------------------结束灌入知识主体和知识相关属性数据----------------------------------------");
	}
	
	/**
	 * 将知识主体和属性的组合数据放到es中去
	 */
	public void startPutKnowledgeAttributesToEs(){
		int startPos = 0;
		int pageSize = 1000;
		int totalSize = 0;
		long startTime = System.currentTimeMillis();
		while(true){
			List<String> knowledgeVersionedIds = knowledgeService.getKnowledgeVersionedIds(startPos, pageSize);
			if(CollectionUtils.isEmpty(knowledgeVersionedIds)){
				break;
			}
			System.out.println("--------------------------------开始灌入 "+startPos+" - "+pageSize+" 知识主体和知识相关属性数据----------------------------------------");
			Map<String, Map<String, Object>> knowledges = makeKlgAttributesIndexService.getKnowledgeAttr(knowledgeVersionedIds);
			putKnowledgeAttributesToEsService.bulkPutKlgAttributesToEs(knowledges);
			totalSize += knowledgeVersionedIds.size();
			if(knowledgeVersionedIds.size() < 1000){
				break;
			}
			
			startPos += 1000;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共拉取了:" + totalSize);
		System.out.println("总共花了:" + (endTime - startTime) + "ms");
		System.out.println("--------------------------------结束灌入知识主体和知识相关属性数据----------------------------------------");
	}
}
