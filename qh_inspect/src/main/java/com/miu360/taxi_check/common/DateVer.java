package com.miu360.taxi_check.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.miu360.taxi_check.util.UIUtils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 验证时间
 */
public class DateVer {

	public static boolean VerStartDate(Context ctx, long start, long end) {
		if (start > end) {
			UIUtils.toast(ctx, "结束时间不能早于开始时间", Toast.LENGTH_LONG);
			return true;
		}
		return false;
	}

	public static boolean VerStartCurrentDate(Context ctx, long start) {
		if (start > System.currentTimeMillis()) {
			UIUtils.toast(ctx, "开始时间不能大于当前时间", Toast.LENGTH_LONG);
			return true;
		}
		return false;

	}

	public static boolean VerEndCurrentDate(Context ctx, long end) {
		if (end > System.currentTimeMillis()) {
			UIUtils.toast(ctx, "结束时间不能大于当前时间", Toast.LENGTH_LONG);
			return true;
		}
		return false;
	}

	public static boolean VerOneDate(Context ctx, long start, long end) {
		if (end - start > 24 * 60 * 60 * 1000) {
			UIUtils.toast(ctx, "不能输入超过一天的时间", Toast.LENGTH_LONG);
			return true;
		}
		return false;
	}
	public static boolean IsSame(Context ctx, long start, long end) {
		if (end == start ) {
			UIUtils.toast(ctx, "不能输入相同的时间", Toast.LENGTH_LONG);
			return true;
		}
		return false;
	}
	// private final static String datePatterShow = "yyyy-MM-dd HH:mm";
	// private final static String datePatter = "yyyyMMddHHmm";
	//
	// public static String setSimpleDateFormatOne(TextView tv){
	// String start_time = new SimpleDateFormat(datePatter).format(new
	// Date((long) tv.getTag()));
	// return start_time;
	// }
	// public static String setSimpleDateFormatTwo(TextView tv){
	// String start_time = new SimpleDateFormat(datePatterShow).format(new
	// Date((long) tv.getTag()));
	// return start_time;
	// }
}
