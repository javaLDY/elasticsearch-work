package cn.baiing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.baiing.service.mapping.CreateAllIndexMappingService;

@Controller
@RequestMapping("mapping")
public class StartCreateAllIndexMappingController {

	@Autowired
	private CreateAllIndexMappingService createAllIndexMappingService;
	
	@RequestMapping("createAllIndexMappingService")
	public void createAllIndexMappingService(){
		createAllIndexMappingService.createAllIndexMapping();
	}
}
