package com.miu360.legworkwrit.app;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/2/2.
 */

public class EncInterceptor implements Interceptor {

    private static final String TAG = "EncInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        try{
            if(oldRequest.body() instanceof MultipartBody){//这里主要做的操作是当为文件传输时延长超时时间，不然可能出现超时，其实服务器也接收完毕的情况
                OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
                okHttpClient.connectTimeout(30, TimeUnit.SECONDS);
                okHttpClient.readTimeout(30,TimeUnit.SECONDS);
                okHttpClient.writeTimeout(30,TimeUnit.SECONDS);
                Request.Builder builder1 = new Request.Builder();
                builder1.url(oldRequest.url());
                Request.Builder post = builder1.post(oldRequest.body());
                return okHttpClient.build().newCall(post.build()).execute();
            }else {
                return chain.proceed(oldRequest);
            }
        }catch (Exception e){
            return chain.proceed(oldRequest);
        }
    }

    /**
     * 根据所需接口、进行动态设置网络超时时间
     * @param oldRequest
     * @param retrofit
     */
    private void setDynamicConnectTimeout(Request oldRequest, Retrofit retrofit, String url) {
        //动态设置超时时间
        final String questUrl = oldRequest.url().url().toString();
        try {
            //1、private final okhttp3.Call.Factory callFactory;   Retrofit 的源码 构造方法中
            Field callFactoryField = retrofit.getClass().getDeclaredField("callFactory");
            callFactoryField.setAccessible(true);
            //2、callFactory = new OkHttpClient();   Retrofit 的源码 build()中
            OkHttpClient client = (OkHttpClient) callFactoryField.get(retrofit);
            //3、OkHttpClient(Builder builder)     OkHttpClient 的源码 构造方法中
            Field connectTimeoutField = client.getClass().getDeclaredField("connectTimeout");
            connectTimeoutField.setAccessible(true);
            //4、根据所需要的时间进行动态设置超时时间
            if (questUrl.contains(url)) {
                connectTimeoutField.setInt(client,30 * 1000);
            } else {
                connectTimeoutField.setInt(client,10 * 1000);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
