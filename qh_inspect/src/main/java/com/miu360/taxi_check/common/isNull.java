package com.miu360.taxi_check.common;

import android.text.TextUtils;

public class isNull {
	public static String isEmpty(String str){
		if(TextUtils.isEmpty(str))
		{
			return "无";
		}
		if(str.equals("&nbsp;")){
			return "无";
		}
		return str;

	}

	public static String isnl(String str){
		if(TextUtils.isEmpty(str)){
			return "";
		}
		return str;
	}
}
