package cn.baiing.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.baiing.dao.SummaryAttrDao;

@Service
public class SummaryAttrService {

	@Autowired
	private SummaryAttrDao summaryAttrDao;
	
	public Map<String, Object> getSummaryAttrByTemplateId(String templateId){
		String content = summaryAttrDao.getSummaryAttrByTemplateId(templateId);
		Map<String, Object> contentMap = new LinkedHashMap<String, Object>();
		if(StringUtils.isNotEmpty(content)){
			JSONArray contentArray = JSONObject.parseArray(content);
			for(int i = 0; i<contentArray.size();i++){
				JSONObject json = contentArray.getJSONObject(i);
				contentMap.put(json.getString("keyId"), json.getString("dataType"));
			}
		}
		return contentMap;
	}
}
