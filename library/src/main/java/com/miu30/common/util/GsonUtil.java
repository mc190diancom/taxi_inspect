package com.miu30.common.util;

import com.google.gson.Gson;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
public class GsonUtil {
    private static Gson sGson = new Gson();

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T fromJson(String s, Class<T> clazz) {
        return sGson.fromJson(s, clazz);
    }
}
