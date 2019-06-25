package com.feidi.video.mvp.model.service;

import com.feidi.video.mvp.model.entity.AlarmType;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.IndustyType;
import com.feidi.video.mvp.model.entity.VideoAddress;
import com.miu30.common.async.Result;
import com.miu30.common.config.Config;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Murphy on 2018/2/2.
 * 存放所有自己的接口
 */

public interface MyApis {

    @FormUrlEncoded
    @POST(Config.SERVER_VIDEO)
    Observable<Result<List<CameraInfo>>> getCameraList(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER)
    Observable<Result<String>> queryHistoryTrack(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_VIDEO)
    Observable<Result<VideoAddress>> getVideoAddress(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_VIDEO)
    Observable<Result<List<IndustyType>>> getHangYeType(@Field("type") String s);

    @FormUrlEncoded
    @POST(Config.SERVER_TAXIINFO)
    Observable<Result<List<AlarmType>>> getAlarmType(@Field("type") String type);
}
