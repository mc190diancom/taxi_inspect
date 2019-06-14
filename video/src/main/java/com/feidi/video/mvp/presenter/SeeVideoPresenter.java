package com.feidi.video.mvp.presenter;

import android.app.Application;

import com.feidi.video.mvp.contract.SeeVideoContract;
import com.feidi.video.mvp.model.entity.VideoAddress;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
public class SeeVideoPresenter extends BasePresenter<SeeVideoContract.Model, SeeVideoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SeeVideoPresenter(SeeVideoContract.Model model, SeeVideoContract.View rootView) {
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
                        if (result.ok() && result.getData() != null) {
                            mRootView.getVideoAddressSuccess(result.getData().getRtspUrl());
                        }else{
                            mRootView.getVideoAddressSuccess(null);
                        }
                    }

                });
    }

    //通过GPS数据解析地址
    public void getGeoCode(double lat,double lon) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", String.valueOf(lat));
        params.put("lon", String.valueOf(lon));
        Map<String, Object> map = new MapUtil().getMap("queryPositionInfo", BaseData.gson.toJson(params));
        mModel.queryHistoryTrack(map)
                .compose(RxUtils.<Result<String>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<String>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<String> result) {
                        System.out.println("queryPositionInfo:"+result.getData());
                        JSONObject jobj = null;
                        try {
                            jobj = new JSONObject(result.getData());
                            result.setData(jobj.optJSONObject("result").optString("formatted_address"));
                            result.setError(Result.OK);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            result.setError(-1);
                            result.setMsg("数据解析异常");
                        }
                        mRootView.setLocationDetail(result.getData());
                    }

                });
    }
}
