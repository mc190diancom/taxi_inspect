package com.miu30.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	private static ObjectMapper objectMapper = null;

	static {
		objectMapper = new ObjectMapper();
	}

	public static String writeEntity2JsonStr(Object object) {
		String jsonStr = "";
		try {
			jsonStr = objectMapper.writeValueAsString(object);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	/**
	 * 浣跨敤娉涘瀷鏂规硶锛屾妸json瀛楃涓茶浆鎹负鐩稿簲鐨凧avaBean瀵硅薄锟�?
	 * (1)杞崲涓烘櫘閫欽avaBean锛歳eadValue(json,Student.class) (2)杞崲涓篖ist,濡侺ist
	 * <Student>,灏嗙浜屼釜鍙傛暟浼狅拷?涓篠tudent
	 * [].class.鐒跺悗浣跨敤Arrays.asList();鏂规硶鎶婂緱鍒扮殑鏁扮粍杞崲涓虹壒瀹氱被鍨嬬殑List
	 * 
	 * @param jsonStr
	 * @param valueType
	 * @return
	 */
	public static <T> T readValue(String jsonStr, Class<T> valueType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * json鏁扮粍杞琇ist
	 * 
	 * @param jsonStr
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueTypeRef);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 鎶奐avaBean杞崲涓簀son瀛楃锟�?
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSon(Object object) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
