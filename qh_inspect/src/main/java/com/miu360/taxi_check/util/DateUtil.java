package com.miu360.taxi_check.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 返回指定格式的日期字符串
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToStr(Date date, String format) {
		SimpleDateFormat dateFormater = new SimpleDateFormat(format);
		return dateFormater.format(date);
	}

	/**
	 * 返回当前时间 "yyyy-MM-dd HH:mm:ss" 格式的字符串
	 *
	 * @return
	 */
	public static String dateNowToStr() {
		SimpleDateFormat dateFormater = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormater.format(new Date());
	}
}
