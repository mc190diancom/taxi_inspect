package com.miu30.common.api;

import com.miu30.common.config.Config;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownLoadService {

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}
