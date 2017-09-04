package cn.baiing.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.baiing.dao.TemplateKeyDao;

@Service
public class TemplateKeyService {

	@Autowired
	private TemplateKeyDao templateKeyDao;
	
	public List<Map<String, Object>> getTemplateKeys(){
		return templateKeyDao.getTemplateKeys();
	}
}
