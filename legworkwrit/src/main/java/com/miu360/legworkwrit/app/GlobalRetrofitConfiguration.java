package com.miu360.legworkwrit.app;

import android.content.Context;

import com.google.gson.Gson;
import com.jess.arms.di.module.ClientModule;
import com.jess.arms.utils.ArmsUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class GlobalRetrofitConfiguration implements ClientModule.RetrofitConfiguration {
    @Override
    public void configRetrofit(Context context, Retrofit.Builder builder) {
        Gson gson = ArmsUtils.obtainAppComponentFromContext(context).gson();
        builder.addConverterFactory(JsonConverterFactory.create(gson));//由于有时要对返回数据解密，这里自己模拟gson的解析方式，自定义了一个解析JsonConverterFactory
    }
}
