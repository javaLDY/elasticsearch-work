package cn.baiing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.baiing.Util.GetKnowledgeAttrsFromDb;
import cn.baiing.model.SearchRequest;
import cn.baiing.service.query.QueryKlgListService;
import cn.baiing.vo.AjaxResponseModel;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/query")
public class QueryController {

	@Autowired
	private QueryKlgListService queryKlgListService;
	
	@RequestMapping("/queryKlgListByKeyword")
	@ResponseBody
	public AjaxResponseModel<Object> queryKlgListByKeyword(SearchRequest searchRequest){
		AjaxResponseModel<Object> result = new AjaxResponseModel<Object>();
		long startTime = System.currentTimeMillis();
		JSONObject searchResponse = queryKlgListService.queryKlgListByKeyword(searchRequest);
		long endTime = System.currentTimeMillis();
		System.out.println("本次搜索结果用时：" + (endTime - startTime));
		result.setMessage("查询成功");
		result.setValue(searchResponse);
		System.out.println(searchResponse);
		return result;
	}
	
	@RequestMapping("/getDetail")
	public void getDetail(long knowledgeVersionedId){
		List<Long> knowledgeVersionedIds = new ArrayList<Long>();
		knowledgeVersionedIds.add(knowledgeVersionedId);
		GetKnowledgeAttrsFromDb.getKnowledgeAttr(knowledgeVersionedIds);
	}
}
