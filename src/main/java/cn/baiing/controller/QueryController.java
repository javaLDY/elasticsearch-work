package cn.baiing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.baiing.Util.GetKnowledgeAttrsFromDb;
import cn.baiing.model.SearchRequest;
import cn.baiing.service.query.QueryKlgListService;

@Controller
@RequestMapping("/query")
public class QueryController {

	@Autowired
	private QueryKlgListService queryKlgListService;
	
	@RequestMapping("/queryKlgListByKeyword")
	public void queryKlgListByKeyword(SearchRequest searchRequest){
		queryKlgListService.queryKlgListByKeyword(searchRequest);
	}
	
	@RequestMapping("/getDetail")
	public void getDetail(long knowledgeVersionedId){
		List<Long> knowledgeVersionedIds = new ArrayList<Long>();
		knowledgeVersionedIds.add(knowledgeVersionedId);
		GetKnowledgeAttrsFromDb.getKnowledgeAttr(knowledgeVersionedIds);
	}
}
