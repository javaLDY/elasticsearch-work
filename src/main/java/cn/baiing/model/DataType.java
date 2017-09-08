package cn.baiing.model;

public class DataType {
	
	//属于numeric数据类型
	public static String NUMERIC_DATATYPE = "1,2,3,4,5,6,7,8,9,11,17,18,19,20,21,22";
	
	//属于text数据类型
	public static String TEXT_DATATYPE = "10,12,13,14,28,29";
	
	//属于date数据类型
	public static String DATE_DATATYPE = "15,27";
	
	/**
	 * 判断属于哪个索引
	 * @param dataType
	 * @return
	 */
	public static String belongKeyAttrIndex(String dataType){
		if(DataType.NUMERIC_DATATYPE.contains(dataType)){
			return IndexRelationConstant.KLG_NUMERIC_INDEX;
		}
		
		if(DataType.TEXT_DATATYPE.contains(dataType)){
			return IndexRelationConstant.KLG_TEXT_INDEX;
		}
		
		if(DataType.DATE_DATATYPE.contains(dataType)){
			return IndexRelationConstant.KLG_DATE_INDEX;
		}
		return null;
	}

}
