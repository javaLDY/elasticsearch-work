package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.TemplateDao;

@Service
public class TemplateService {

	@Autowired
	private TemplateDao templateDao;
	
	public List<Map<String, Object>> getTemplates(){
		return templateDao.getTemplates();
	}
}
