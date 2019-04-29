package com.miu360.legworkwrit.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.mvp.contract.IllegalDetailActivityContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


@ActivityScope
public class IllegalDetailActivityModel extends BaseModel implements IllegalDetailActivityContract.Model {


    @Inject
    public IllegalDetailActivityModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<JCItem>>> getIllegalDetail(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getIllegalDetail(map);
    }
}