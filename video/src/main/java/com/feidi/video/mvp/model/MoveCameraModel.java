package com.feidi.video.mvp.model;

import android.app.Application;

import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.service.MyApis;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.feidi.video.mvp.contract.MoveCameraContract;
import com.miu30.common.async.Result;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/03/2019 13:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class MoveCameraModel extends BaseModel implements MoveCameraContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MoveCameraModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Result<List<CameraInfo>>> getCameraList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getCameraList(map);
    }

    @Override
    public Observable<Result<String>> queryHistoryTrack(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).queryHistoryTrack(map);
    }

}