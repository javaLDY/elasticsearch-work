package cn.baiing.service.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAllIndexMappingService {

	@Autowired
	private CreateKlgAttachmentIndexMappingService createKlgAttachmentIndexMappingService;
	
	@Autowired
	private CreateklgBusinessTagIndexMappingService createklgBusinessTagIndexMappingService;
	
	@Autowired
	private CreateKlgDateIndexMappingService createKlgDateIndexMappingService;
	
	@Autowired
	private CreateKlgIndexMappingService createKlgIndexMappingService;
	
	@Autowired
	private CreateKlgTextIndexMappingService createKlgTextIndexMappingService;
	
	@Autowired
	private CreateKlgSmsIndexMappingService createKlgSmsIndexMappingService;
	
	@Autowired
	private CreateKlgNumericIndexMappingService createKlgNumericIndexMappingService;
	
	@Autowired
	private CreateTemplateIndexMappingService createTemplateIndexMappingService;
	
	@Autowired
	private CreateTemplateKeyIndexMappingService createTemplateKeyIndexMappingService;
	
	@SuppressWarnings("static-access")
	public void createAllIndexMapping(){
		try {
			System.out.println("--------------------------------开始创建知识mapping----------------------------------------");
			//创建知识主体Mapping
			createKlgIndexMappingService.createKlgIndex();
			//创建知识附件Mapping
			createKlgAttachmentIndexMappingService.createAttachmentIndexMapping();
			//创建知识归属关系Mapping
			createklgBusinessTagIndexMappingService.createklgBusinessTagIndexMapping();
			//创建知识日期Mapping
			createKlgDateIndexMappingService.createKlgDateIndexMapping();
			//创建知识短信Mapping
			createKlgSmsIndexMappingService.createKnowledgeSmsIndexMapping();
			//创建知识文本Mapping
			createKlgTextIndexMappingService.createKnowledgeTextIndexMapping();
			//创建知识数值Mapping
			createKlgNumericIndexMappingService.createNumericIndexMapping();
			//创建知识主体和知识attr-Mapping
			createKlgIndexMappingService.createKlgAndAttrIndex();
			System.out.println("--------------------------------创建知识mapping成功----------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public void createTemplateAttrsIndexMapping(){
		try {
			System.out.println("--------------------------------开始创建模板属性mapping----------------------------------------");
			//创建模板mapping
			createTemplateIndexMappingService.createTemplateIndexMapping();
			//创建模板属性mapping
			createTemplateKeyIndexMappingService.createTemplateIndexMapping();
			System.out.println("--------------------------------创建模板属性mapping成功----------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
