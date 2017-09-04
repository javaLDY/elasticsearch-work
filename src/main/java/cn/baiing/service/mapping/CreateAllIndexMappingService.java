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
	
	@SuppressWarnings("static-access")
	public void createAllIndexMapping(){
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
