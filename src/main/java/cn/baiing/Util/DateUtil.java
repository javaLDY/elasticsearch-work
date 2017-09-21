package cn.baiing.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.text.DateFormatter;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static SimpleDateFormat simpleDateFormatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat dateIos8601simpleDateFormatAll = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
	
	/**
	 * 判断是否是时间
	 * @param time
	 * @return
	 */
	public static boolean isDate(String time) {
		boolean isDate = false;
		if(StringUtils.isNotBlank(time)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				format.parse(time);
				isDate = true;
			} catch (ParseException e) {
				isDate = false;
			}
		}
		return isDate;
	}

	public static void main(String[] args) {
		LocalTime localTime = LocalTime.now();
		LocalDate localDate = LocalDate.now();
		System.out.println(localDate);
		System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
	}
}
