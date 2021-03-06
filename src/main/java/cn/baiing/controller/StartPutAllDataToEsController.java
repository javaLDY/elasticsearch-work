package cn.baiing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.baiing.service.StartPutAllDataToEsService;

@RequestMapping("/es")
@Controller
public class StartPutAllDataToEsController {

	@Autowired
	private StartPutAllDataToEsService startPutAllDataToEsService;
	
	@RequestMapping("/startPutAllDataToEsService")
	public void startPutAllDataToEsService(){
		startPutAllDataToEsService.startPullAllDataToEs();
	}
	
	@RequestMapping("/startPutAllTemplateAttrsToService")
	public void startPutAllTemplateAttrsToService(){
		startPutAllDataToEsService.startPutAllTemplateAttrsToEs();
	}
	
	@RequestMapping("/startPutKnowledgeAndKlgAttrToEs")
	public void startPutKnowledgeAndKlgAttrToEs(){
		startPutAllDataToEsService.startPutKnowledgeAndKlgAttrToEs();
	}
	
	@RequestMapping("/startPutBasicKnowledgeAttributesToEs")
	public void startPutBasicKnowledgeAttributesToEs(){
		startPutAllDataToEsService.startPutBasicKnowledgeAttributesToEs();
	}
	
	@RequestMapping("/startPutKnowledgeAttributesToEs")
	public void startPutKnowledgeAttributesToEs(){
		startPutAllDataToEsService.startPutKnowledgeAttributesToEs();
	}
}
