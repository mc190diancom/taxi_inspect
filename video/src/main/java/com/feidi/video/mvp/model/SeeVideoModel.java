package com.feidi.video.mvp.model;

import android.app.Application;

import com.feidi.video.mvp.model.entity.VideoAddress;
import com.feidi.video.mvp.model.service.MyApis;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.feidi.video.mvp.contract.SeeVideoContract;
import com.miu30.common.async.Result;

import java.util.Map;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:46
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SeeVideoModel extends BaseModel implements SeeVideoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SeeVideoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Result<VideoAddress>> getVideoAddress(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getVideoAddress(map);
    }

    @Override
    public Observable<Result<String>> queryHistoryTrack(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).queryHistoryTrack(map);
    }
}