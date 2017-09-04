package cn.baiing.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static SimpleDateFormat simpleDateFormatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 获取yyyy-MM-dd HH:mm:ss 类型的Date
	 * @param time
	 * @return
	 */
	public static Date getDateOfHaveAllTime(String time){
		Date date = new Date();
		try {
			date = simpleDateFormatAll.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取yyyy-MM-dd 类型的Date
	 * @param time
	 * @return
	 */
	public static Date getDate(String time){
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取yyyy-MM-dd HH:mm:ss 类型的String
	 * @param date
	 * @return
	 */
	public static String formatDateAllToString(Date date){
		String time = null;
		time = simpleDateFormatAll.format(date);
		return time;
	}
	
	/**
	 * 获取yyyy-MM-dd 类型的String
	 * @param date
	 * @return
	 */
	public static String formatDateToString(Date date){
		String time = null;
		time = simpleDateFormat.format(date);
		return time;
	}

}
