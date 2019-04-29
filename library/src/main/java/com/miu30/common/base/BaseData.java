package com.miu30.common.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miu30.common.async.Result;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class BaseData {
	public static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.create();
	}

	/**
	 * 将结果转成Object对象
	 */
	public static <T> void parseObjectResult(Result<T> result, String key, String res, Type t) throws Throwable {
		JSONObject jObj = new JSONObject(res);
		result.setError(jObj.optInt("err", -1));
		result.setMsg(jObj.optString("errorMsg"));
		if (result.ok()) {
			T datas = null;
			if (TextUtils.isEmpty(key)) {
				datas = gson.fromJson(res, t);
			} else {
				datas = gson.fromJson(jObj.getString(key), t);
			}
			result.setData(datas);
		}

	}

	/**
	 * 将结果转成Object对象
	 */
	public static <T> void parseObjectResult2(Result<T> result, String key, String res, Type t) throws Throwable {
		JSONObject jObj = new JSONObject(res);
		result.setError(Integer.valueOf(jObj.optString("error", "-1")));
		result.setMsg(jObj.optString("msg"));
		if (result.ok()) {
			T datas = null;
			if (TextUtils.isEmpty(key)) {
				datas = gson.fromJson(res, t);
			} else {
				datas = gson.fromJson(jObj.getString(key), t);
			}
			result.setData(datas);
		}

	}

	public static <T> void parseArrayResult(Result<T> result, String res, Type t) throws Throwable {
		result.setError(Result.OK);
		result.setMsg("");
		if (result.ok()) {
			T datas = gson.fromJson(res, t);
			result.setData(datas);
		}
	}
	
	public static <T> void parseObjectResult1(Result<T> result, String key, String res, Type t) throws Throwable {
		JSONObject jObj = new JSONObject(res);
		result.setError(jObj.optInt("err", 0));
		result.setMsg(jObj.optString("errorMsg"));
		if (result.ok()) {
			T datas = null;
			if (TextUtils.isEmpty(key)) {
				datas = gson.fromJson(res, t);
			} else {
				datas = gson.fromJson(jObj.getString(key), t);
			}
			result.setData(datas);
		}

	}
	
	public static void trimEmptyToNull(Object o) {
		try {
			Field[] fs = o.getClass().getDeclaredFields();
			for (Field field : fs) {
				if (field.getType() == String.class) {
					field.setAccessible(true);
					if ("".equals(field.get(o))) {
						field.set(o, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void trimNullToEmpty(Object o) {
		try {
			Field[] fs = o.getClass().getDeclaredFields();
			for (Field field : fs) {
				if (field.getType() == String.class) {
					field.setAccessible(true);
					if (field.get(o)==null) {
						field.set(o, "");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
