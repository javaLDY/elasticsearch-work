package cn.baiing.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import cn.baiing.datatype.NumericWithUnit;
import cn.baiing.db.client.AttachmentServiceClient;
import cn.baiing.db.client.KnowledgeServiceClient;
import cn.baiing.db.client.TemplateKeyServiceClient;
import cn.baiing.db.client.TemplateServiceClient;
import cn.baiing.db.model.Attachment;
import cn.baiing.db.model.Knowledge;
import cn.baiing.db.model.ParameterValue;
import cn.baiing.db.model.Template;
import cn.baiing.db.model.TemplateKey;
import cn.baiing.exception.BadRequestException;
import cn.baiing.exception.ForbiddenException;
import cn.baiing.exception.InternalServerException;
import cn.baiing.measure.BaseMeasure;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GetKnowledgeAttrsFromDb {

	public static JSONArray getKnowledgeAttr(List<Long> knowledgeVersionedIds){
		JSONArray resultList = new JSONArray();
		List<Knowledge> klgList = new ArrayList<Knowledge>();
		try {
			klgList =  KnowledgeServiceClient.getInstance().getKnowledgesByKnowledgeVersionedIds(knowledgeVersionedIds);
		} catch (ForbiddenException e) {
			e.printStackTrace();
		} catch (BadRequestException e) {
			e.printStackTrace();
		} catch (InternalServerException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		if(CollectionUtils.isNotEmpty(klgList)){
			try {
				for(Knowledge klg : klgList){
					JSONObject json = getKnowledgeByKlgVersioned(klg);
					JSONArray attr = new JSONArray();
					Id2Entity id2Entity = new Id2Entity(klg);
					AttributeValueConvertor abc = new AttributeValueConvertor(klg,id2Entity);
					Map<Long, List<ParameterValue<Long>>> attachmentWithExpType = abc.getAttachmentWithExpType();
					if(attachmentWithExpType != null){
						for(Entry<Long, List<ParameterValue<Long>>> entry : attachmentWithExpType.entrySet()){
							List<Long> ids = new ArrayList<Long>();
							List<ParameterValue<Long>> value = entry.getValue();
							List<Attachment> attachment = new ArrayList<Attachment>();
							List<Long> attachmentIds = new ArrayList<Long>();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<Long> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(Long.valueOf(paraMap.getKeyId()));
								attachmentIds.add(Long.valueOf(paraMap.getValue()));
							}
							JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
							tempAttrValueJson.put("value", attachmentIds);
//							attachment = AttachmentServiceClient.getInstance().getAttachments(attachmentIds);
//							JSONObject tempAttrValueJson = createAttrAttachments(attachment, ids);
							attr.add(tempAttrValueJson);	
						}
					}
					
					//日期属性、属性值拼装
					Map<Long, List<ParameterValue<Date>>> dateWithExpType = abc.getDatesWithExpType();
					if(dateWithExpType != null){
						for(Entry<Long, List<ParameterValue<Date>>> entry : dateWithExpType.entrySet()){
							List<Long> ids = new ArrayList<Long>();
							List<ParameterValue<Date>> value = entry.getValue();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<Date> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(Long.valueOf(paraMap.getKeyId()));
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
								if(paraMap.getValue() != null){
									tempAttrValueJson.put("dateValue", DateUtil.simpleDateFormatAll.format(paraMap.getValue()));
								}else{
									tempAttrValueJson.put("dateValue", paraMap.getValue());
								}
								attr.add(tempAttrValueJson);		
							}
						}
					}
					
					Map<Long, List<ParameterValue<BaseMeasure>>> measureWithExpType = abc.getMeasuresWithExpType();
					if(measureWithExpType != null){
						for(Entry<Long, List<ParameterValue<BaseMeasure>>> entry : measureWithExpType.entrySet()){
							List<Long> ids = new ArrayList<Long>();
							List<ParameterValue<BaseMeasure>> value = entry.getValue();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<BaseMeasure> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(paraMap.getKeyId());
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
								if(tempAttrValueJson.getString("unit") != null){
									tempAttrValueJson.remove("unit");
								}
								tempAttrValueJson.put("value", paraMap.getValue().toString());
								attr.add(tempAttrValueJson);	
							}
						}
					}
					
					Map<Long, List<ParameterValue<String>>> multiSelectionWithExpType = abc.getMultiSelectionsWithExpType();
					if(multiSelectionWithExpType != null){
						for(Entry<Long, List<ParameterValue<String>>> entry : multiSelectionWithExpType.entrySet()){
							List<ParameterValue<String>> value = entry.getValue();
							for(int i = 0; i<value.size(); i++ ){
								List<Long> ids = new ArrayList<Long>();
								ParameterValue<String> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(paraMap.getKeyId());
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
								tempAttrValueJson.put("value", paraMap.getValue());
								attr.add(tempAttrValueJson);	
							}
						}
					}
					Map<Long, List<ParameterValue<NumericWithUnit>>> numericWithExpType = abc.getNumericWithExpType();
					if(numericWithExpType != null){
						for(Entry<Long, List<ParameterValue<NumericWithUnit>>> entry : numericWithExpType.entrySet()){
							List<ParameterValue<NumericWithUnit>> value = entry.getValue();
							List<Long> ids = new ArrayList<Long>();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<NumericWithUnit> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(paraMap.getKeyId());
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
									
								String unit = tempAttrValueJson.getString("unit");
								if(tempAttrValueJson.getString("unit") != null){
									tempAttrValueJson.remove("unit");
								}
								String currentUnit = paraMap.getValue().getCurrentUnit();
								tempAttrValueJson.put("currentUnit", currentUnit);
								if(!tempAttrValueJson.isEmpty()){
									if(paraMap.getValue() instanceof NumericWithUnit){
										tempAttrValueJson.put("integerValue", paraMap.getValue().getValueStr());
									}else{
										tempAttrValueJson.put("integerValue", paraMap.getValue().getValueStr());
									}
									attr.add(tempAttrValueJson);
								}
							}
						}
					}
					Map<Long, List<ParameterValue<String>>> singleSelectionWithExpType =  abc.getSingleSelectionsWithExpType();
					if(singleSelectionWithExpType != null){
						for(Entry<Long, List<ParameterValue<String>>> entry : singleSelectionWithExpType.entrySet()){
							List<ParameterValue<String>> value = entry.getValue();
							List<Long> ids = new ArrayList<Long>();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<String> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(paraMap.getKeyId());
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
								tempAttrValueJson.put("value", paraMap.getValue());
								attr.add(tempAttrValueJson);	
									
							}
						}
					}
					
					Map<Long, String> sms = abc.getSms();
					if(sms != null){
						for(Entry<Long, String> entry : sms.entrySet()){
							List<Long> ids = new ArrayList<Long>();
							long key = entry.getKey();
							ids.add(key);
							JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
							String value = entry.getValue();
							tempAttrValueJson.put("value", value);
							attr.add(tempAttrValueJson);	
						}
					}
					
				    Map<Long, List<ParameterValue<String>>> textesWithExpType = abc.getTextesWithExpType();
					if(textesWithExpType != null){
						for(Entry<Long, List<ParameterValue<String>>> entry : textesWithExpType.entrySet()){
							List<Long> ids = new ArrayList<Long>();
							List<ParameterValue<String>> value = entry.getValue();
							for(int i = 0; i<value.size(); i++ ){
								ParameterValue<String> paraMap = value.get(i);
								String exceptionId = String.valueOf(paraMap.getExceptionType().getId());
								if(exceptionId.equals("2")){
									continue;
								}
								ids.add(paraMap.getKeyId());
								JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
								if(tempAttrValueJson.getString("unit") != null){
									tempAttrValueJson.remove("unit");
								}
								tempAttrValueJson.put("value", paraMap.getValue());
								attr.add(tempAttrValueJson);		
							}
						}
					}
					json.put("attrs", attr);
					resultList.add(json);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("拼装属性所用时间:" + (endTime - startTime));
		return resultList;
	}
	
	/**
	 * 拼凑attr中的附件格式
	 * @param attachment
	 * @return
	 */
	private static JSONObject createAttrAttachments(List<Attachment> attachment, List<Long> ids){
		JSONArray attachArray = new JSONArray();
		JSONObject tempAttrValueJson = getTemplateAttrValueByTemplateId(ids);
		for(int i = 0;i<attachment.size(); i++){
			String suffix = attachment.get(i).getSuffix();
			if((suffix.contains("jpg"))||(suffix.contains("png"))){
				attachArray.add(attachment.get(i).getName());
				tempAttrValueJson.put("imgattr", attachArray);
			}else{
				attachArray.add(attachment.get(i).getName());
				tempAttrValueJson.put("attachments", attachArray);
			}
		}
		return tempAttrValueJson;
	}
	
	/**
	 * 获取模板属性的基本信息
	 * @param ids
	 * @return
	 */
	private static JSONObject getTemplateAttrValueByTemplateId(List<Long> ids){
		JSONObject tempalteJson = new JSONObject();
		try {
			
			List<TemplateKey> templateList = TemplateKeyServiceClient.getInstance()
												.getTemplateKeyByTemplateKeyIds(ids);
			for(TemplateKey tem : templateList){
				String name = tem.getName();
				String displayName = tem.getDisplayName();
				tempalteJson.put("id", tem.getId().toString());
				tempalteJson.put("name", name);
				tempalteJson.put("displayName", displayName);
				tempalteJson.put("dataType", tem.getDataType().getId());
				if(tem.getDefaultUnit() != null){
					tempalteJson.put("unit", tem.getDefaultUnit());
				}else{
					tempalteJson.put("unit", "");
				}
			}
		} catch (ForbiddenException e) {
			e.printStackTrace();
		} catch (BadRequestException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InternalServerException e) {
			e.printStackTrace();
		}
		return tempalteJson;
	}
	
	/**
	 * 封装knowledge的基本信息
	 * @param klgVersioned
	 * @return
	 */
	private static JSONObject getKnowledgeByKlgVersioned(Knowledge klg){
		JSONObject json = new JSONObject();
		if (klg != null) {
			json.put("name", klg.getName());
			json.put("vids", klg.getVids());
			json.put("knowledgeVersionedId", klg.getKnowledgeVersionedId());
			json.put("knowledgeId", klg.getKnowledgeId());
			json.put("locIds", klg.getLocIds());
			json.put("templateId", klg.getTemplateId()); 
			json.put("templateName", getTemplateById(klg.getTemplateId()).getName());
			json.put("templateDisplayName", getTemplateById(klg.getTemplateId()).getDisplayName());
			json.put("mongoId", klg.getMongoId());
			if(klg.getPublishTime() != null){
				json.put("publishTime", DateUtil.simpleDateFormat.format(klg.getPublishTime()));
			}else{
				json.put("publishTime", klg.getPublishTime());
			}
			if(klg.getStartTime() != null){
				json.put("startTime", DateUtil.simpleDateFormatAll.format(klg.getStartTime()));
			}else{
				json.put("startTime", klg.getStartTime()) ;
			}
			if(klg.getEndTime() != null){
				json.put("endTime", DateUtil.simpleDateFormatAll.format(klg.getEndTime()));
			}else{
				json.put("endTime", klg.getEndTime());
			}
			if(klg.getEffectStartTime() != null){
				json.put("effectStartTime", DateUtil.simpleDateFormatAll.format(klg.getEffectStartTime()));
			}else{
				json.put("effectStartTime", klg.getEffectStartTime());
			}
			if(klg.getEffectEndTime() != null){
				json.put("effectEndTime", DateUtil.simpleDateFormatAll.format(klg.getEffectEndTime()));
			}else{
				json.put("effectEndTime", klg.getEffectEndTime());
			}
		}
		return json;
	}
	
	private static Template getTemplateById(long templateId){
		Template tem = new Template();
		try {
			tem = TemplateServiceClient.getInstance().getTemplateById(templateId);
		} catch (ForbiddenException e) {
			e.printStackTrace();
		} catch (BadRequestException e) {
			e.printStackTrace();
		} catch (InternalServerException e) {
			e.printStackTrace();
		}
		return tem;
	} 
}
