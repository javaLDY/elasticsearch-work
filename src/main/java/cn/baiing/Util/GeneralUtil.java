package cn.baiing.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GeneralUtil {

	/**
	 * 取出html标签
	 * @param value
	 * @return
	 */
	public static String cleanHtml(String value){
		Document doc = Jsoup.parse(value);
		return doc.text();
	}
	
}
