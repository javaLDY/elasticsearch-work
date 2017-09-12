package cn.baiing.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.baiing.datatype.NumericWithUnit;
import cn.baiing.datatype.RichText;
import cn.baiing.datatype.SmsStructure;
import cn.baiing.db.model.ExceptionUnit;
import cn.baiing.db.model.Knowledge;
import cn.baiing.db.model.ParameterValue;
import cn.baiing.db.model.TemplateKey;
import cn.baiing.exception.BadRequestException;
import cn.baiing.exception.ForbiddenException;
import cn.baiing.exception.InternalServerException;
import cn.baiing.measure.BaseMeasure;
import cn.baiing.meta.BusinessTagWithTemplate;
import cn.baiing.meta.DataType;
import cn.baiing.meta.ExceptionType;
import cn.baiing.meta.VisibilityType;
import cn.baiing.util.SmsUtil;

/**
 * 将属性值转换成页面显示格式
 * @author baiing
 *
 */
public class AttributeValueConvertor {
	
	protected static final Log logger = LogFactory.getLog(AttributeValueConvertor.class);
	
	/* attribute section */
    private Map<Long, List<ParameterValue<Long>>> attachmentWithExpType;
	private Map<Long, List<ParameterValue<Date>>> datesWithExpType; //expType
    private Map<Long, List<ParameterValue<BaseMeasure>>> measuresWithExpType;
    private Map<Long, List<ParameterValue<NumericWithUnit>>> numericWithExpType;
	private Map<Long, List<ParameterValue<RichText>>> richTextesWithExpType;
    private Map<Long, List<ParameterValue<String>>> textesWithExpType;
    private Map<Long, List<ParameterValue<String>>> multiSelectionsWithExpType;
    private Map<Long, List<ParameterValue<String>>> singleSelectionsWithExpType;
    private Map<Long, List<BusinessTagWithTemplate> > businessTagWithTemplates; 
    private Map<Long, List<Long>> knowlgWithknowlgs;
    private Map<Long, String> sms;
    private Map<Long, List<Long>> faq;
    private Map<Long, List<ExceptionUnit>> exceptionUnits;
    
    // locationId -> templateKeyId -> value
	private Map<Long, Map<Long, String>> id2KeyValues = new HashMap<Long, Map<Long, String>>();
	// templateKey -> noteValues
	private Map<Long, List<String>> id2Note;
	
	private Knowledge klg;

	public AttributeValueConvertor(Knowledge klg, Id2Entity id2Entity) throws ForbiddenException, BadRequestException, InternalServerException {
		
		this.klg = klg;
		this.exceptionUnits = klg.getExceptionUnits();
		
		//附件
		//Map<Long, List<ParameterValue<List<Long>>>>
		attachmentWithExpType = klg.getAttachmentWithExpType();
		if(attachmentWithExpType != null && !attachmentWithExpType.isEmpty()){ 
			
			List<ParameterValue<Long>> attachments = null;
			for(Long keyId : attachmentWithExpType.keySet()){
				
				attachments = attachmentWithExpType.get(keyId);
				if(attachments == null) {
					continue;
				}
				
				//包括主知识和异常值
				for(ParameterValue<Long> attachItem : attachments) {
					// 注解值的处理
					if(attachItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(attachItem);
						continue;
					}
					
					StringBuffer attachNamesString = new StringBuffer();
					try {
						// 处理每个附件
						if (id2Entity.getId2attachmentFromKlg().get(attachItem.getValue()) != null) {
							attachNamesString.append(
									"<a href=\"" + 
											"" + 
											"/download/attachment?filename=" + 
											id2Entity.getId2attachmentFromKlg().get(attachItem.getValue()).getName() + 
											"\">" +
											id2Entity.getId2attachmentFromKlg().get(attachItem.getValue()).getOriginName() +
									"</a><br />");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(attachment) Convertor Error, templateKeyId:" + keyId + "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
					
					cacheValue(getLocationIdByPV(attachItem), attachItem.getKeyId(), attachNamesString.toString());
						
				}
			}
		}
		
		//日期
		datesWithExpType = klg.getDatesWithExpType();
		if(datesWithExpType != null && !datesWithExpType.isEmpty()) {
			List<ParameterValue<Date>> dates = null;
			String dateValue = "";
			
			for(Long keyId : datesWithExpType.keySet()) {
				dates = datesWithExpType.get(keyId);
				if(dates == null) {
					continue;
				}
				
				for(ParameterValue<Date> dateItem : dates) {
					// 注解值的处理
					if(dateItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(dateItem);
						continue;
					}
					
					try {
						Date date = dateItem.getValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						dateValue = "";
						dateValue = sdf.format(date);
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(date) Convertor Error, templateKeyId:" + keyId + "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
					
					cacheValue(getLocationIdByPV(dateItem), dateItem.getKeyId(), dateValue);
				}
				
			}
		}
		
		measuresWithExpType = klg.getMeasuresWithExpType();
		if(measuresWithExpType != null && !measuresWithExpType.isEmpty()) {
			
			Map<Long, TemplateKey> id2template = id2Entity.getId2templateKeyFromKlg();
			
			List<ParameterValue<BaseMeasure>> measures = null;
			for(Long keyId : measuresWithExpType.keySet()) {
				measures = measuresWithExpType.get(keyId);
				if(measures == null) {
					continue;
				}
				
				for(ParameterValue<BaseMeasure> measureItem : measures) {
					// 注解值的处理
					if(measureItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(measureItem);
						continue;
					}
					
					String value = "";
					try {
						if (id2template != null && !id2template.isEmpty() && id2template.get(keyId) != null 
								&& id2template.get(keyId).getOptions() != null 
								&& !id2template.get(keyId).getOptions().isEmpty()) 
						{
							value = measureItem.getValue().toString(Integer.parseInt(id2template.get(keyId).getOptions()));
						} else {
							value = measureItem.getValue().toString();
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(measures) Convertor Error, templateKeyId:" + keyId + "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
					
					cacheValue(getLocationIdByPV(measureItem), measureItem.getKeyId(), value);
				}
				
			}
			
		}
		
		numericWithExpType = klg.getNumericWithExpType();
		if(numericWithExpType != null && !numericWithExpType.isEmpty()) {
			Map<Long, TemplateKey> id2template = id2Entity.getId2templateKeyFromKlg();
			
			List<ParameterValue<NumericWithUnit>> numerics = null;
			for(Long keyId : numericWithExpType.keySet()) {
				numerics = numericWithExpType.get(keyId);
				if(numerics == null) {
					continue;
				}
				
				for(ParameterValue<NumericWithUnit> numericItem : numerics) {
					// 注解值的处理
					if(numericItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(numericItem);
						continue;
					}
					
					String value = "";
					try {
						if (id2template != null && !id2template.isEmpty() && id2template.get(keyId) != null 
								&& id2template.get(keyId).getOptions() != null 
								&& !id2template.get(keyId).getOptions().isEmpty() 
								&& id2template.get(keyId).getDataType() != DataType.PERCENTAGE) 
						{
							value = numericItem.getValue().toString(Integer.parseInt(id2template.get(keyId).getOptions()));
						} else {
							value = numericItem.getValue().toString();
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(numeric) Convertor Error, templateKeyId:" + keyId + "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
					
					cacheValue(getLocationIdByPV(numericItem), numericItem.getKeyId(), value);
				}
			}
		}
		
		richTextesWithExpType = klg.getRichTextesWithExpType();
		if(richTextesWithExpType != null && !richTextesWithExpType.isEmpty())
		{
			List<ParameterValue<RichText>> richTextes = null;
			String textValue = "";
			
			for(Long keyId : richTextesWithExpType.keySet()) {
				richTextes = richTextesWithExpType.get(keyId);
				if(richTextes == null) {
					continue;
				}
				
				for(ParameterValue<RichText> richTextItem : richTextes) {
					// 注解值的处理
					if(richTextItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(richTextItem);
						continue;
					}
					
					RichText rt = null;
					try {
						textValue = "";
						rt = richTextItem.getValue();
						textValue = rt.getDereferencedText();
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(richText) Convertor Error, templateKeyId:" + keyId + "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
					
					
					if(rt != null) {
						cacheValue(getLocationIdByPV(richTextItem), richTextItem.getKeyId(), textValue);
					}
				}
				
			}
		}
		
		textesWithExpType = klg.getTextesWithExpType(); 
		if(textesWithExpType != null && !textesWithExpType.isEmpty())
		{
			List<ParameterValue<String>> textes = null;
			for(Long keyId : textesWithExpType.keySet()) {
				textes = textesWithExpType.get(keyId);
				if(textes == null) {
					continue;
				}
				
				for(ParameterValue<String> textItem : textes) {
					// 注解值的处理
					if(textItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(textItem);
						continue;
					}
					
					cacheValue(getLocationIdByPV(textItem), textItem.getKeyId(), textItem.getValue());
				}
			}
		}
		
		multiSelectionsWithExpType = klg.getMultiSelectionsWithExpType(); 
		if(multiSelectionsWithExpType != null && !multiSelectionsWithExpType.isEmpty())
		{
			List<ParameterValue<String>> multiSelections = null;
			for(Long keyId : multiSelectionsWithExpType.keySet()) {
				multiSelections = multiSelectionsWithExpType.get(keyId);
				if(multiSelections == null) {
					continue;
				}
				
				for(ParameterValue<String> multiSelectionItem : multiSelections) {
					// 注解值的处理
					if(multiSelectionItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(multiSelectionItem);
						continue;
					}
					
					cacheValue(
							getLocationIdByPV(multiSelectionItem), 
							multiSelectionItem.getKeyId(), 
							multiSelectionItem.getValue());
				}
			}
		}
		
		singleSelectionsWithExpType = klg.getSingleSelectionsWithExpType(); 
		if(singleSelectionsWithExpType != null && !singleSelectionsWithExpType.isEmpty())
		{
			List<ParameterValue<String>> singleSelections = null;
			for(Long keyId : singleSelectionsWithExpType.keySet()) {
				singleSelections = singleSelectionsWithExpType.get(keyId);
				if(singleSelections == null) {
					continue;
				}
				
				for(ParameterValue<String> singleSelectionItem : singleSelections) {
					// 注解值的处理
					if(singleSelectionItem.getExceptionType() == ExceptionType.annotate) {
						cacheNoteValues(singleSelectionItem);
						continue;
					}
					
					cacheValue(
							getLocationIdByPV(singleSelectionItem), 
							singleSelectionItem.getKeyId(), 
							singleSelectionItem.getValue());
				}
			}
		}
		
		// 关联知识类型
		knowlgWithknowlgs = klg.getKnowlgWithknowlgs();
		if(knowlgWithknowlgs!=null){
			for(Long keyId : knowlgWithknowlgs.keySet()){
				StringBuffer knowledgeIds = new StringBuffer();
				for (Long knowledgeId : knowlgWithknowlgs.get(keyId)) {
					try {
						if (id2Entity.getId2knowledgeFromKlg() != null) {
							if(id2Entity.getId2knowledgeFromKlg().get(knowledgeId) != null){
								if (id2Entity.getId2knowledgeFromKlg().get(knowledgeId).getTemplateId() != null &&
										id2Entity.getAllId2templateFromKlg().get(id2Entity.getId2knowledgeFromKlg().get(knowledgeId).getTemplateId()) != null) {
									knowledgeIds.append("【" + id2Entity.getAllId2templateFromKlg().get(id2Entity.getId2knowledgeFromKlg().get(knowledgeId).getTemplateId()).getDisplayName() + "】");
								}
								knowledgeIds.append(id2Entity.getId2knowledgeFromKlg().get(knowledgeId).getName() + "<br />");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Value(knowlgWithknowlgs) Convertor Error, templateKeyId:" + keyId 
								+ "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
					}
				}
				cacheValue(klg.getLocationId(), keyId, knowledgeIds.substring(0, knowledgeIds.length()));
			}
		}
		
		businessTagWithTemplates = klg.getBusinessTagWithTemplates();
		if(businessTagWithTemplates!=null){
			StringBuffer bizTagWithTplNames = new StringBuffer();
			for(Long keyId : businessTagWithTemplates.keySet()){
				for (BusinessTagWithTemplate bizWithTmp : businessTagWithTemplates.get(keyId)) {
					// bizTagWithTplNames.append(id2Entity.getId2businessTag().get(bizWithTmp.getBusinessTagId()));
					for (Long tplId : bizWithTmp.getTemplateIds()) {
						try {
							if (id2Entity.getId2businessTagFromKlg().get(bizWithTmp.getBusinessTagId()) != null &&
									id2Entity.getAllId2templateFromKlg().get(tplId) != null) {
								bizTagWithTplNames.append("【" 
										+ id2Entity.getId2businessTagFromKlg().get(bizWithTmp.getBusinessTagId()).getDisplayName() + "】"
										+ id2Entity.getAllId2templateFromKlg().get(tplId).getDisplayName() + "<br />");
							}
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("Value(businessTagWithTemplates) Convertor Error, templateKeyId:" + keyId 
									+ "| templateKeyName:" + id2Entity.getId2templateKeyFromKlg().get(keyId).getName());
						}
					}
				}
				cacheValue(klg.getLocationId(), keyId, bizTagWithTplNames.toString());
			}
		}
		
		sms = klg.getSms();
		if(sms != null)
		{
			for(Long keyId : sms.keySet())
			{
				String nameString = "";
				try {
				     String smsTmp = (String)this.sms.get(keyId);
			         String smsValue = null;
					 if (smsTmp != null) {
						 smsValue = smsTmp.replaceAll("\\+", "%2B");
				     }
					 smsValue = smsValue.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
					 smsValue = smsValue.replaceAll("\\+", "%2B");
					nameString = URLDecoder.decode(smsValue,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<SmsStructure> smsStruture = SmsUtil.convert2Sms(nameString);
				String smsString =SmsUtil.convert2html(smsStruture);
				cacheValue(klg.getLocationId(), keyId, smsString);
			}
		}
		
		faq =klg.getFaq();
		if (faq !=null && !faq.isEmpty()) {
			for(Long keyId : faq.keySet()){
				StringBuffer knowledgeIds = new StringBuffer();
				for (Long knowledgeId : faq.get(keyId)) {
					if(id2Entity.getId2FaqknowledgeFromKlg() != null && id2Entity.getId2FaqknowledgeFromKlg().get(knowledgeId) != null) {
						knowledgeIds.append(id2Entity.getId2FaqknowledgeFromKlg().get(knowledgeId).getName() + "<br />");
					}
				}
				
				/*List<Knowledge> knowledges = KnowledgeServiceClient.getInstance().getKnowledgesByKnowledgeVersionedIds(faq.get(keyId));
				for (Knowledge knowledge : knowledges) {
					knowledgeIds.append(knowledge.getName() + "<br />");
				}*/
				
				cacheValue(klg.getLocationId(), keyId, knowledgeIds.substring(0, knowledgeIds.length()));
			}
		}
	}

	private Long getLocationIdByPV(ParameterValue<?> pv) {
		if(pv.getExceptionType() == null) {
			return klg.getLocationId();
		}
		switch (pv.getExceptionType()) {
			case defaultValue:
				return klg.getLocationId();
			case originalExp:
				List<ExceptionUnit> exs = this.exceptionUnits.get(pv.getKeyId());
				
				for(ExceptionUnit ex : exs) {
					if(pv.getId().equals(ex.getRefId())) {
						return ex.getExceptionSceneId();
					}
				}
				return null;
			default:
				return null;
		}
	}
	
	private void cacheValue(Long locationId, Long keyId, String value) {
		Map<Long, String> key2Value = id2KeyValues.get(locationId);
		if(key2Value == null) {
			key2Value = new HashMap<Long, String>();
			id2KeyValues.put(locationId, key2Value);
		}
		
		//附件的特殊处理
		if(key2Value.get(keyId) == null) {
			key2Value.put(keyId, value);
		} else {
			key2Value.put(keyId, key2Value.get(keyId) + value);
		}
		
	}
	
	public String getValue(Long locationId, Long keyId) {
		String empty = "";
		if(locationId == null || keyId == null) {
			return empty;
		}
		
		Map<Long, String> key2Value = id2KeyValues.get(locationId);
		if(key2Value == null) {
			return empty;
		}
		
		if(key2Value.get(keyId) == null) {
			return empty;
		}
		
		return key2Value.get(keyId);
	}
	
	public void cacheNoteValues(ParameterValue pv) throws ForbiddenException, BadRequestException, InternalServerException {
		
		if (this.id2Note == null) {
			this.id2Note = new HashMap<Long, List<String>>();
		}
			
		if(klg.getExceptionUnits() != null) {
			
			List<ExceptionUnit> euList = klg.getExceptionUnits().get(pv.getKeyId());
			if(pv == null || euList == null) {
				return;
			}
			
			List<String> channelNoteList = id2Note.get(pv.getKeyId());
			if(channelNoteList == null) {
				channelNoteList = new ArrayList<String>();
			}
			
			if(pv.getExceptionType() == ExceptionType.annotate) {
				for(ExceptionUnit ex : euList) {
					if(pv.getId().equals(ex.getRefId())) {
						Long channelId = ex.getExceptionSceneId();
						VisibilityType channel = VisibilityType.getTypeById(channelId.intValue());
						String chAndText = channel.getDisplayName() + "：" + pv.getNoteValue();
						channelNoteList.add(chAndText);
						break;
					}
				}
			}
			
			if(channelNoteList != null && !channelNoteList.isEmpty()) {
				id2Note.put(pv.getKeyId(), channelNoteList);
			}
		}
	}
	
	public Map<Long, List<ParameterValue<Long>>> getAttachmentWithExpType() {
		return attachmentWithExpType;
	}

	public void setAttachmentWithExpType(
			Map<Long, List<ParameterValue<Long>>> attachmentWithExpType) {
		this.attachmentWithExpType = attachmentWithExpType;
	}

	public Map<Long, List<ParameterValue<Date>>> getDatesWithExpType() {
		return datesWithExpType;
	}

	public void setDatesWithExpType(
			Map<Long, List<ParameterValue<Date>>> datesWithExpType) {
		this.datesWithExpType = datesWithExpType;
	}

	public Map<Long, List<ParameterValue<BaseMeasure>>> getMeasuresWithExpType() {
		return measuresWithExpType;
	}

	public void setMeasuresWithExpType(
			Map<Long, List<ParameterValue<BaseMeasure>>> measuresWithExpType) {
		this.measuresWithExpType = measuresWithExpType;
	}

	public Map<Long, List<ParameterValue<NumericWithUnit>>> getNumericWithExpType() {
		return numericWithExpType;
	}

	public void setNumericWithExpType(
			Map<Long, List<ParameterValue<NumericWithUnit>>> numericWithExpType) {
		this.numericWithExpType = numericWithExpType;
	}

	public Map<Long, List<ParameterValue<RichText>>> getRichTextesWithExpType() {
		return richTextesWithExpType;
	}

	public void setRichTextesWithExpType(
			Map<Long, List<ParameterValue<RichText>>> richTextesWithExpType) {
		this.richTextesWithExpType = richTextesWithExpType;
	}

	public Map<Long, List<ParameterValue<String>>> getTextesWithExpType() {
		return textesWithExpType;
	}

	public void setTextesWithExpType(
			Map<Long, List<ParameterValue<String>>> textesWithExpType) {
		this.textesWithExpType = textesWithExpType;
	}

	public Map<Long, List<ParameterValue<String>>> getMultiSelectionsWithExpType() {
		return multiSelectionsWithExpType;
	}

	public void setMultiSelectionsWithExpType(
			Map<Long, List<ParameterValue<String>>> multiSelectionsWithExpType) {
		this.multiSelectionsWithExpType = multiSelectionsWithExpType;
	}

	public Map<Long, List<ParameterValue<String>>> getSingleSelectionsWithExpType() {
		return singleSelectionsWithExpType;
	}

	public void setSingleSelectionsWithExpType(
			Map<Long, List<ParameterValue<String>>> singleSelectionsWithExpType) {
		this.singleSelectionsWithExpType = singleSelectionsWithExpType;
	}

	public Map<Long, List<BusinessTagWithTemplate>> getBusinessTagWithTemplates() {
		return businessTagWithTemplates;
	}

	public void setBusinessTagWithTemplates(
			Map<Long, List<BusinessTagWithTemplate>> businessTagWithTemplates) {
		this.businessTagWithTemplates = businessTagWithTemplates;
	}

	public Map<Long, List<Long>> getKnowlgWithknowlgs() {
		return knowlgWithknowlgs;
	}

	public void setKnowlgWithknowlgs(Map<Long, List<Long>> knowlgWithknowlgs) {
		this.knowlgWithknowlgs = knowlgWithknowlgs;
	}

	public Map<Long, String> getSms() {
		return sms;
	}

	public void setSms(Map<Long, String> sms) {
		this.sms = sms;
	}

	public Map<Long, List<Long>> getFaq() {
		return faq;
	}

	public void setFaq(Map<Long, List<Long>> faq) {
		this.faq = faq;
	}

	public Map<Long, Map<Long, String>> getId2KeyValues() {
		return id2KeyValues;
	}

	public void setId2KeyValues(Map<Long, Map<Long, String>> id2KeyValues) {
		this.id2KeyValues = id2KeyValues;
	}

	public Map<Long, List<ExceptionUnit>> getExceptionUnits() {
		return exceptionUnits;
	}

	public void setExceptionUnits(Map<Long, List<ExceptionUnit>> exceptionUnits) {
		this.exceptionUnits = exceptionUnits;
	}

	public Map<Long, List<String>> getId2Note() {
		return id2Note;
	}

	public void setId2Note(Map<Long, List<String>> id2Note) {
		this.id2Note = id2Note;
	}
	
}
