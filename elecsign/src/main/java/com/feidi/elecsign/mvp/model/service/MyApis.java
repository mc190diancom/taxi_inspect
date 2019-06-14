package com.feidi.elecsign.mvp.model.service;

import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.queryZFRYByDWMC;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Murphy on 2018/2/2.
 * 存放所有自己的接口
 */

public interface MyApis {

    @FormUrlEncoded
    @POST(Config.SERVER_OTHER)
    Observable<Result<List<queryZFRYByDWMC>>> getCheZuList(@FieldMap Map<String, Object> map);
}
