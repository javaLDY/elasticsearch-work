package cn.baiing.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.baiing.datatype.RichText;
import cn.baiing.db.client.AttachmentServiceClient;
import cn.baiing.db.client.BusinessTagServiceClient;
import cn.baiing.db.client.KnowledgeServiceClient;
import cn.baiing.db.client.TemplateServiceClient;
import cn.baiing.db.model.Attachment;
import cn.baiing.db.model.BusinessTag;
import cn.baiing.db.model.ExceptionUnit;
import cn.baiing.db.model.Knowledge;
import cn.baiing.db.model.ParameterValue;
import cn.baiing.db.model.Template;
import cn.baiing.db.model.TemplateKey;
import cn.baiing.db.model.channelMerge.KlgBundle;
import cn.baiing.db.model.comb.KnowledgeBlock;
import cn.baiing.db.model.comb.KnowledgeBundle;
import cn.baiing.exception.BadRequestException;
import cn.baiing.exception.ForbiddenException;
import cn.baiing.exception.InternalServerException;
import cn.baiing.meta.BusinessTagWithTemplate;
import cn.baiing.meta.ExceptionType;
import cn.baiing.meta.VisibilityType;

/**
 * DESC：查看知识详情时做前期处理，做的主要工作是将关联类型的属性或一个属性存在多个值得情况
 * 例如：知识关联类型，知识采编时某个知识关联类型的属性关联了两个知识，在数据库里存储时只存储了两个知识的ID
 * 而在知识详情页面显示时要显示那两个知识的名称，所以预先将知识ID和知识对象缓存起来以便后期获取方便
 * 
 * @author baiing
 *
 */
public class Id2Entity {

	private KnowledgeBundle knowledgeBundle;
	
	private Knowledge klg;

	private static Map<Long, Template> allId2template;
	
	private Map<Long, TemplateKey> id2templateKey;
	private Map<Long, Knowledge> id2knowledge;
	private Map<Long, BusinessTag> id2businessTag;
	private Map<Long, Attachment> id2attachment;
	private Map<Long, Knowledge> id2FaqKnowledge;
	private Map<Long, List<String>> id2Note;
		
	public Id2Entity(KnowledgeBundle knowledgeBundle) {
		this.knowledgeBundle = knowledgeBundle;
	}
	
	public Id2Entity(Knowledge klg) {
		this.klg = klg;
	}
	
	@Deprecated
	public Map<Long, Template> getAllId2template() throws ForbiddenException, BadRequestException, InternalServerException {
		if (Id2Entity.allId2template == null) {
			Id2Entity.allId2template = new HashMap<Long, Template>();
			List<Template> templates = TemplateServiceClient.getInstance().getAll();
			if (templates != null && !templates.isEmpty()) {
				for (Template template : templates) {
					Id2Entity.allId2template.put(template.getId(), template);
				}
			}
		}
		return Id2Entity.allId2template;
	}
	
	/**
	 * add by zhangyingxuna 20141221
	 * 获取系统中所有模板
	 */
	public Map<Long, Template> getAllId2templateFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {
		if (Id2Entity.allId2template == null) {
			Id2Entity.allId2template = new HashMap<Long, Template>();
			List<Template> templates = TemplateServiceClient.getInstance().getAll();
			if (templates != null && !templates.isEmpty()) {
				for (Template template : templates) {
					Id2Entity.allId2template.put(template.getId(), template);
				}
			}
		}
		return Id2Entity.allId2template;
	}
	
	@Deprecated
	public Map<Long, Knowledge> getId2knowledge() throws ForbiddenException, BadRequestException, InternalServerException {
		if (id2knowledge == null) {
			this.id2knowledge = new HashMap<Long, Knowledge>();
			List<Long> extractAllKnowledgeIds = knowledgeBundle.extractAllKnowledgeIds();
			if (extractAllKnowledgeIds != null && !extractAllKnowledgeIds.isEmpty()) {
				List<Knowledge> knowledges = KnowledgeServiceClient.getInstance().getKnowledgesByKnowledgeIds(extractAllKnowledgeIds);
				if (knowledges != null && !knowledges.isEmpty()) {
					for (Knowledge knowledge : knowledges) {
						// if (knowledge.getIsPublished() != null && knowledge.getIsPublished()) {
							this.id2knowledge.put(knowledge.getKnowledgeId(), knowledge);
						// }
					}
				}
			}
		}
		return id2knowledge;
	}
	
	/**
	 * add by zhangyingxuan 20141221
	 * 关联知识类型
	 * @return
	 * @throws ForbiddenException
	 * @throws BadRequestException
	 * @throws InternalServerException
	 */
	public Map<Long, Knowledge> getId2knowledgeFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {
		if (id2knowledge == null) {
			this.id2knowledge = new HashMap<Long, Knowledge>();
			List<Long> extractAllKnowledgeIds = new ArrayList<Long>();
			
			if(klg.getKnowlgWithknowlgs()!=null && !klg.getKnowlgWithknowlgs().isEmpty()) {
				
				for (List<Long> klgWithKlgIds : klg.getKnowlgWithknowlgs().values()) {
					if (klgWithKlgIds != null && !klgWithKlgIds.isEmpty()) {
						extractAllKnowledgeIds.addAll(klgWithKlgIds);
					}
				}
				
				if (extractAllKnowledgeIds != null && !extractAllKnowledgeIds.isEmpty()) {
					List<Knowledge> knowledges = KnowledgeServiceClient.getInstance().getKnowledgesByKnowledgeIds(extractAllKnowledgeIds);
					if (knowledges != null && !knowledges.isEmpty()) {
						for (Knowledge knowledge : knowledges) {
							// if (knowledge.getIsPublished() != null && knowledge.getIsPublished()) {
								this.id2knowledge.put(knowledge.getKnowledgeId(), knowledge);
							// }
						}
					}
				}
			}
		}
		return id2knowledge;
	}
	
	@Deprecated
	public Map<Long, Knowledge> getId2Faqknowledge() throws ForbiddenException, BadRequestException, InternalServerException {
		if (id2FaqKnowledge == null) {
			this.id2FaqKnowledge = new HashMap<Long, Knowledge>();
			List<Long> extractAllKnowledgeIds = knowledgeBundle.faqAllKnowledgeIds();
			Set<Long> set = new HashSet<Long>(extractAllKnowledgeIds);
			
			if (extractAllKnowledgeIds != null && !extractAllKnowledgeIds.isEmpty()) {
				List<Knowledge> knowledges = KnowledgeServiceClient.getInstance().getFaqKlgssByVersionIds(new ArrayList<Long>(set));
				if (knowledges != null && !knowledges.isEmpty()) {
					for (Knowledge knowledge : knowledges) {
						 if (knowledge.getIsPublished() != null && knowledge.getIsPublished()) {
							this.id2FaqKnowledge.put(knowledge.getKnowledgeVersionedId(), knowledge);
						 }
					}
				}
			}
		}
		return id2FaqKnowledge;
	}

	/**
	 * add by zhangyingxuan 20141221
	 * FAQ类型
	 * @return
	 * @throws ForbiddenException
	 * @throws BadRequestException
	 * @throws InternalServerException
	 */
	public Map<Long, Knowledge> getId2FaqknowledgeFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {
		if (id2FaqKnowledge == null) {
			this.id2FaqKnowledge = new HashMap<Long, Knowledge>();
			List<Long> extractAllKnowledgeIds = new ArrayList<Long>();
			if(klg.getFaq() != null && !klg.getFaq().isEmpty()) {
				for (List<Long> faqKlgIds : klg.getFaq().values()) {
					if (faqKlgIds != null && !faqKlgIds.isEmpty()) {
						extractAllKnowledgeIds.addAll(faqKlgIds);
					}
				}
			}
			
			if (extractAllKnowledgeIds != null && !extractAllKnowledgeIds.isEmpty()) {
				List<Knowledge> knowledges = KnowledgeServiceClient.getInstance().getFaqKlgssByVersionIds(extractAllKnowledgeIds);
				if (knowledges != null && !knowledges.isEmpty()) {
					for (Knowledge knowledge : knowledges) {
						 //if (knowledge.getIsPublished() != null && knowledge.getIsPublished()) {
							this.id2FaqKnowledge.put(knowledge.getKnowledgeVersionedId(), knowledge);
						 //}
					}
				}
			}
		}
		return id2FaqKnowledge;
	}
	
	@Deprecated
	public Map<Long, BusinessTag> getId2businessTag() throws ForbiddenException, BadRequestException, InternalServerException {
		if (this.id2businessTag == null) {
			this.id2businessTag = new HashMap<Long, BusinessTag>();
			List<Long> extractAllBusinessTagIds = knowledgeBundle.extractAllBusinessTagIds();
			if (extractAllBusinessTagIds != null && !extractAllBusinessTagIds.isEmpty()) {
				List<BusinessTag> businessTags = BusinessTagServiceClient.getInstance().getBusinessTags(extractAllBusinessTagIds);
				if (businessTags != null && !businessTags.isEmpty()) {
					for (BusinessTag businessTag : businessTags) {
						this.id2businessTag.put(businessTag.getId(), businessTag);
					}
				}
			}
		}
		return id2businessTag;
	}
	
	/**
	 * 系列关联类型
	 * add by zhangyingxuan
	 * @return
	 * @throws ForbiddenException
	 * @throws BadRequestException
	 * @throws InternalServerException
	 */
	public Map<Long, BusinessTag> getId2businessTagFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {
		if (this.id2businessTag == null) {
			this.id2businessTag = new HashMap<Long, BusinessTag>();
			List<Long> extractAllBusinessTagIds = new ArrayList<Long>();
			
			if (klg.getBusinessTagWithTemplates() != null && !klg.getBusinessTagWithTemplates().isEmpty()) {
				// loop through BusinessTagWithTemplate keys
				for (List<BusinessTagWithTemplate> bizTagWithTplWithKey : klg.getBusinessTagWithTemplates().values()) {
					if (bizTagWithTplWithKey != null && !bizTagWithTplWithKey.isEmpty()) {
						// /loop through keys
						for (BusinessTagWithTemplate businessTagWithTemplate : bizTagWithTplWithKey) {
							if (businessTagWithTemplate.getBusinessTagId() != null) {
								// get businessTagId
								extractAllBusinessTagIds.add(businessTagWithTemplate.getBusinessTagId());
							}
						}
					}
				}
			}
			
			if (extractAllBusinessTagIds != null && !extractAllBusinessTagIds.isEmpty()) {
				List<BusinessTag> businessTags = BusinessTagServiceClient.getInstance().getBusinessTags(extractAllBusinessTagIds);
				if (businessTags != null && !businessTags.isEmpty()) {
					for (BusinessTag businessTag : businessTags) {
						this.id2businessTag.put(businessTag.getId(), businessTag);
					}
				}
			}
		}
		return id2businessTag;
	}
	
	@Deprecated
	public Map<Long, Attachment> getId2attachment() throws ForbiddenException, BadRequestException, InternalServerException {
		if (this.id2attachment == null) {
			this.id2attachment = new HashMap<Long, Attachment>();
			List<Long> extractAllAttachmentIds = this.knowledgeBundle.extractAllAttachmentIds();
			if (extractAllAttachmentIds != null && !extractAllAttachmentIds.isEmpty()) {
				List<Attachment> attachments = AttachmentServiceClient.getInstance().getAttachments(extractAllAttachmentIds);
				if (attachments != null && !attachments.isEmpty()) {
					for (Attachment attachment : attachments) {
						this.id2attachment.put(attachment.getId(), attachment);
					}
				}
			}
		}
		return this.id2attachment;
	}
	
	/**
	 * 附件类型包括图片类型
	 * add by zhangyingxuan 20121221
	 * @return
	 * @throws ForbiddenException
	 * @throws BadRequestException
	 * @throws InternalServerException
	 */
	public Map<Long, Attachment> getId2attachmentFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {
		if (this.id2attachment == null) {
			this.id2attachment = new HashMap<Long, Attachment>();
			List<Long> extractAllAttachmentIds = new ArrayList<Long>();
			
			if (klg.getAttachmentWithExpType() != null && !klg.getAttachmentWithExpType().isEmpty()) {
				for (List<ParameterValue<Long>> attachmentIdsWithKey : klg.getAttachmentWithExpType().values()) {
					for(ParameterValue<Long> pv : attachmentIdsWithKey) {
						// 注解不需要解析
						if(pv.getExceptionType() == ExceptionType.annotate) {
							continue;
						}
						if (pv.getValue() != null) {
							extractAllAttachmentIds.add(pv.getValue());
						}
					}
				}
			}
			// ID转换成对象
			if (extractAllAttachmentIds != null && !extractAllAttachmentIds.isEmpty()) {
				List<Attachment> attachments = AttachmentServiceClient.getInstance().getAttachments(extractAllAttachmentIds);
				if (attachments != null && !attachments.isEmpty()) {
					for (Attachment attachment : attachments) {
						this.id2attachment.put(attachment.getId(), attachment);
					}
				}
			}
		}
		return this.id2attachment;
	}
	
	@Deprecated
	public Map<Long, TemplateKey> getId2templateKey() throws ForbiddenException, BadRequestException, InternalServerException {

		if (this.id2templateKey == null) {
			Long templateId = null;
			for (Map.Entry<VisibilityType, KnowledgeBlock> entry: this.knowledgeBundle.getType2Block().entrySet()) {
				if (entry.getValue().getMainKnowledge() != null && entry.getValue().getMainKnowledge().getTemplateId() != null) {
					templateId = entry.getValue().getMainKnowledge().getTemplateId();
					break;
				}
			}
			
			this.id2templateKey = new HashMap<Long, TemplateKey>();
			if (templateId == null) {
				return this.id2templateKey;
			}
			
			List<TemplateKey> templateKeys = TemplateServiceClient.getInstance().getTemplateKeys(templateId);
			if (templateKeys == null || templateKeys.isEmpty()) {
				return this.id2templateKey;
			}
			
			for (TemplateKey templateKey : templateKeys) {
				this.id2templateKey.put(templateKey.getId(), templateKey);
			}
		}

		return this.id2templateKey;
	}
	
	public Map<Long, TemplateKey> getId2templateKeyFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {

		if (this.id2templateKey == null) {
			Long templateId = klg.getTemplateId();
			
			this.id2templateKey = new HashMap<Long, TemplateKey>();
			if (templateId == null) {
				return this.id2templateKey;
			}
			
			List<TemplateKey> templateKeys = TemplateServiceClient.getInstance().getTemplateKeys(templateId);
			if (templateKeys == null || templateKeys.isEmpty()) {
				return this.id2templateKey;
			}
			
			for (TemplateKey templateKey : templateKeys) {
				this.id2templateKey.put(templateKey.getId(), templateKey);
			}
		}

		return this.id2templateKey;
	}
	
	/**
	 * 注解
	 * @return
	 * @throws ForbiddenException
	 * @throws BadRequestException
	 * @throws InternalServerException
	 */
	public Map<Long, List<String>> getId2NoteFromKlg() throws ForbiddenException, BadRequestException, InternalServerException {

		if (this.id2Note == null) {
			this.id2Note = new HashMap<Long, List<String>>();
			
			Map<Long, List<ParameterValue<RichText>>> richTextesWithExpType = klg.getRichTextesWithExpType();
			
			if(klg.getExceptionUnits() != null && richTextesWithExpType != null) {
				for(Map.Entry<Long, List<ExceptionUnit>> exEntry : klg.getExceptionUnits().entrySet()) {
					
					List<ParameterValue<RichText>> pvList = richTextesWithExpType.get(exEntry.getKey());
					List<String> channelNoteList = new ArrayList<String>();
					
					if(pvList != null && !pvList.isEmpty()) {
						
						
						for(ParameterValue<RichText> pv : pvList) {
							if(pv.getExceptionType() == ExceptionType.annotate) {
								for(ExceptionUnit ex : exEntry.getValue()) {
									if(pv.getId().equals(ex.getRefId())) {
										Long channelId = ex.getExceptionSceneId();
										VisibilityType channel = VisibilityType.getTypeById(channelId.intValue());
										String chAndText = channel.getDisplayName() + "：" + pv.getValue().getDereferencedText();
										channelNoteList.add(chAndText);
										break;
									}
								}
							}
						}
						
						if(!channelNoteList.isEmpty()) {
							id2Note.put(exEntry.getKey(), channelNoteList);
						}
					}
				}
			}
			
			
		}
		return this.id2Note;
	}
	
}
