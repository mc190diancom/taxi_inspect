package com.miu360.legworkwrit.app;

import android.content.Context;

import com.jess.arms.di.module.ClientModule;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;

public class GlobalOkhttpConfiguration implements ClientModule.OkhttpConfiguration {
    private static final int DEFAULT_TIMEOUT = 10;

    @Override
    public void configOkhttp(Context context, OkHttpClient.Builder builder) {
        //time our
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.addInterceptor(new EncInterceptor());
        ProgressManager.getInstance().with(builder);
        //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
        RetrofitUrlManager.getInstance().with(builder);
    }
}
