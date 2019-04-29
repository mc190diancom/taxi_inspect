package com.miu360.legworkwrit.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.LiveTranscriptContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


@ActivityScope
public class LiveTranscriptModel extends BaseModel implements LiveTranscriptContract.Model {


    @Inject
    public LiveTranscriptModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<JSONObject>> submitLiveData(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getLiveData(map);
    }

    @Override
    public Observable<Result<List<LiveTranscript>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getLiveTranscript(map);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }
}