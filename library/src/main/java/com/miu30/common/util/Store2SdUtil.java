package com.miu30.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.Inspector;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Store2SdUtil {

	private static String HOME_PATH;
	private static Context context;
	private Gson gson;

	/**
	 * 初始化
	 */
	private Store2SdUtil() {
		gson = new Gson();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			HOME_PATH = Config.PATH;
			File dir = new File(HOME_PATH);
			if (!dir.exists())
				dir.mkdirs();
		} else {
			UIUtils.toast(context, "无法读取SD卡，请确认插入SD卡并开启权限！", Toast.LENGTH_SHORT);

		}

	}

	/**
	 * 单例工厂
	 */
	private static class SingleFactory {
		private static Store2SdUtil instance = new Store2SdUtil();
	}

	/**
	 * 获取单例
	 */
	public static Store2SdUtil getInstance(Context context) {
		Store2SdUtil.context = context;
		return SingleFactory.instance;
	}

	/**
	 * 存储单个对象
	 */
	public <T> boolean storeOut(T t, String fileName) {
		String json = gson.toJson(t);
		writeJson(json, fileName);
		return true;
	}

	/**
	 * 添加对象
	 */
	public <T> boolean addOut(T t, String fileName,Class<T> cls) {
		ArrayList<T> arrayList = readInArrayObject(fileName,cls);
		if (arrayList == null)
			arrayList = new ArrayList<>();
		arrayList.add(t);
		String json = gson.toJson(arrayList);
		writeJson(json, fileName);
		return true;
	}

	/**
	 * 添加对象组，对象传进来转换成json
	 */
	public <T> boolean addOut2(ArrayList<T> t, String fileName) {
		if(t == null){
			return false;
		}
		String json = gson.toJson(t);
		writeJson(json, fileName);
		return true;
	}

	/**
	 * 读取对象
	 */
	public <T> T readInObject(String fileName,TypeToken typeToken) {
		return gson.fromJson(getJsonByFileName(fileName), typeToken.getType());
	}

	/**
	 * 读取对象组
	 */
	private <T> ArrayList<T> readInArrayObject1(String fileName) {
		return gson.fromJson(getJsonByFileName(fileName), new TypeToken<ArrayList<T>>() {
		}.getType());
	}

	/**
	 * 转成list
	 */
	private <T> ArrayList<T> readInArrayObject(String fileName, Class<T> cls) {
		ArrayList<T> list = new ArrayList<>();
		if (gson != null) {
			JsonArray array = new JsonParser().parse(getJsonByFileName(fileName)).getAsJsonArray();
			for (final JsonElement elem : array) {
				list.add(gson.fromJson(elem, cls));
			}
		}
		return list;
	}

	/**
	 * 读取对象组
	 */
	public <T> ArrayList<T> readInArrayObject(String fileName, TypeToken typeToken) {
		return gson.fromJson(getJsonByFileName(fileName), typeToken.getType());
	}

	/**
	 * 获得文件
	 */
	private File getFileByPath(String path) {
		return new File(path);
	}

	/**
	 * 通过文件名获取json
	 */
	private String getJsonByFileName(String fileName) {
		String json = "";
		try {
			File file = getFileByPath(HOME_PATH + fileName);
			if (!file.exists())
				return "[]";
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader in = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(in);
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				json += temp;
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.toast(context, "解析异常", Toast.LENGTH_SHORT);
		}
		return json;
	}

	/**
	 * 写入json
	 */
	private boolean writeJson(String json, String fileName) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(getFileByPath(HOME_PATH + fileName));
			fileOutputStream.write(json.getBytes("UTF-8"));
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.toast(context, "写出异常", Toast.LENGTH_SHORT);
		}
		return false;
	}
}
