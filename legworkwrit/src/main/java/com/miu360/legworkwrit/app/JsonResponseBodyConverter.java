package com.miu360.legworkwrit.app;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.miu30.common.async.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Murphy on 2018/2/2.
 * 参照GsonResponseBodyConverter，重写主要为了解析各种类型的返回数据
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    //private final TypeAdapter<T> adapter;
    private Type type;
    private Gson gson;

    JsonResponseBodyConverter(Gson gson, Type type) {
        this.type = type;
        this.gson = gson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Result result = new Result();
        try {
            String response = value.string();
            if (TextUtils.isEmpty(response)) {
                result.setError(0);
                result.setMsg("");
            } else {
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject jsonObject = new JSONObject(response);
                    result.setError(jsonObject.optInt("error", -1));
                    result.setMsg(jsonObject.optString("msg", ""));
                    Object data = jsonObject.opt("data");
                    if (type.equals(String.class)) {
                        if (data == null) {
                            result.setData(response);
                        } else {
                            result.setData(data.toString());
                        }
                    } else if (type.equals(JSONObject.class)) {
                        if (data == null || "null".equals(data.toString()) || "无".equals(data.toString())) {
                            Object json1 = new JSONTokener(response).nextValue();
                            if (json1 instanceof JsonArray) {
                                JSONObject jsonObject1 = new JSONObject(json1.toString());
                                result.setData(jsonObject1);
                            } else {
                                result.setData(jsonObject);
                            }
                        } else {
                            Object json1 = new JSONTokener(data.toString()).nextValue();
                            if (json1 instanceof JsonArray) {
                                JSONObject jsonObject1 = new JSONObject(json1.toString());
                                result.setData(jsonObject1);
                            } else {
                                JSONObject data1 = (JSONObject) data;
                                result.setData(data1);
                            }
                        }
                    } else if (type.equals(Void.class)) {
                        result.setData(null);
                    } else {
                        if (data == null) {
                            result.setData(gson.fromJson(response, type));
                        } else {
                            result.setData(gson.fromJson(data.toString(), type));
                        }
                    }
                } else if (json instanceof JSONArray) {
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    if (length <= 0) {
                        result.setError(0);
                        result.setData(null);
                        result.setMsg("暂无数据");
                        return (T) result;
                    } else if (type.equals(String.class)) {
                        result.setData(json.toString());
                        return (T) result;
                    }
                    result.setData(gson.fromJson(jsonArray.toString(), type));
                }
            }
        } catch (Exception e) {
            result.setMsg("系统出了点小错误,类型转换异常");
            if (result.ok()) {
                result.setThrowable(e);
            }
            result.setError(-1);
            e.printStackTrace();
        }


        /*try{
            return adapter.fromJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return (T) analysis(response);
        } finally {
            value.close();
        }*/
        return (T) result;
    }

}
