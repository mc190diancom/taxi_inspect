package com.feidi.elecsign.mvp.model;

import android.app.Application;

import com.feidi.elecsign.mvp.model.service.MyApis;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.feidi.elecsign.mvp.contract.AuthorizationContract;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.queryZFRYByDWMC;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/14/2019 18:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class AuthorizationModel extends BaseModel implements AuthorizationContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AuthorizationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Result<List<queryZFRYByDWMC>>> getCheZuList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getCheZuList(map);
    }
}