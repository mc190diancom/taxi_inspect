package com.miu360.taxi_check.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;

public class DatePatterCommon {
	private final static String datePatterShow = "yyyy-MM-dd HH:mm";
	private final static String datePatter = "yyyy-MM-dd HH:mm:ss";

	public static String getTime(String time) {
		String returnSting = null;
		if (TextUtils.isEmpty(time)) {
			returnSting = "无";
		} else {
			Date birthday = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatter);
			try {
				birthday = sf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			returnSting = new SimpleDateFormat(datePatterShow).format(new Date(birthday.getTime()));
		}

		return returnSting;

	}
}
