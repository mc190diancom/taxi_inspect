package com.feidi.video.mvp.presenter;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.mvp.contract.SeeVideoListContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.VideoAddress;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.widget.MultiVeriticalItemDecoration;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.RxUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SeeVideoListPresenter extends BasePresenter<SeeVideoListContract.Model, SeeVideoListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private List<CameraInfo> infos = new ArrayList<>();

    @Inject
    public SeeVideoListPresenter(SeeVideoListContract.Model model, SeeVideoListContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //测试数据
    public List<CameraInfo> getCameraInfos() {
        return infos;
    }

    public RecyclerView.ItemDecoration getSeeCameraRecyclerViewDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(12))
                .setMarginRight(SizeUtils.dp2px(15))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public void getCameraInfos(String zfzh) {
        Map<String, String> params = new HashMap<>();
        params.put("ZFZH", zfzh);
        Map<String, Object> map = new MapUtil().getMap("getCameraListByZfzh", BaseData.gson.toJson(params));
        mModel.getCameraList(map)
                .compose(RxUtils.<Result<List<CameraInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<CameraInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<CameraInfo>> result) {
                        if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                            infos.clear();
                            infos.addAll(result.getData());
                            mRootView.notifyAdapter();
                        }
                    }

                });
    }

    public void getVideoAddress(String cameraid,String zfzh) {
        Map<String, String> params = new HashMap<>();
        params.put("CAMERAID", cameraid);
        params.put("ZFZH", zfzh);
        Map<String, Object> map = new MapUtil().getMap("tokenBindCamera", BaseData.gson.toJson(params));
        mModel.getVideoAddress(map)
                .compose(RxUtils.<Result<VideoAddress>>applySchedulers(mRootView,true))
                .subscribe(new MyErrorHandleSubscriber<Result<VideoAddress>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<VideoAddress> result) {
                        if (result.getData() != null) {
                            mRootView.getVideoAddressSuccess(result.getData().getRtspUrl());
                        }else{
                            mRootView.getVideoAddressSuccess(null);
                        }
                    }

                });
    }
}
