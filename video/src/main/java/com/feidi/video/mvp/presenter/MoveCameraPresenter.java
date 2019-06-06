package com.feidi.video.mvp.presenter;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.mvp.contract.MoveCameraContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.Industry;
import com.feidi.video.mvp.model.entity.WarningType;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.jess.arms.di.scope.FragmentScope;
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
 * Created by MVPArmsTemplate on 06/03/2019 13:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class MoveCameraPresenter extends BasePresenter<MoveCameraContract.Model, MoveCameraContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    ArrayList<ISelector> data = new ArrayList<>();
    ArrayList<ISelector> CameraData = new ArrayList<>();

    @Inject
    public MoveCameraPresenter(MoveCameraContract.Model model, MoveCameraContract.View rootView) {
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

    public RecyclerView.ItemDecoration getCameraListDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(12))
                .setMarginRight(SizeUtils.dp2px(14))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public RecyclerView.ItemDecoration getIndustryOrWarningTypeListDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(10))
                .setMarginRight(SizeUtils.dp2px(17))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public RecyclerView.ItemDecoration getCrimeDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(12))
                .setMarginRight(SizeUtils.dp2px(15))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    //测试数据
    public void getCameraInfos(String zfzh) {
        /*List<ISelector> infos = new ArrayList<>();
        infos.add(new CameraInfo("摄像头一"));
        infos.add(new CameraInfo("摄像头二"));
        infos.add(new CameraInfo("摄像头三"));
        infos.add(new CameraInfo("摄像头四"));
        infos.add(new CameraInfo("摄像头五"));
        infos.add(new CameraInfo("摄像头六"));
        infos.add(new CameraInfo("摄像头七"));
        infos.add(new CameraInfo("摄像头八"));
        infos.add(new CameraInfo("摄像头九"));
        infos.add(new CameraInfo("摄像头十"));
        Map<String, String> params = new HashMap<>();
        return infos;*/
        Map<String, String> params = new HashMap<>();
        params.put("ZFZH", zfzh);
        Map<String, Object> map = new MapUtil().getMap("getCameraListByZfzh", BaseData.gson.toJson(params));
        System.out.println("getCameraList:"+map);
        mModel.getCameraList(map)
                .compose(RxUtils.<Result<List<CameraInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<CameraInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<CameraInfo>> result) {
                        System.out.println("getCameraList:"+result.getData());
                        if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                            CameraData.clear();
                            CameraData.addAll(result.getData());
                            mRootView.notifyAdapter(result.getData());
                        }
                    }

                });
    }

    //测试数据
    public ArrayList<ISelector> getCameraData() {
        return CameraData;
    }


    public List<ISelector> getIndustryOrWarningTypeInfos() {
        return data;
    }

    //测试数据
    public void setIndustryOrWarningTypeInfos(boolean isIndustry) {
        data.clear();

        if (isIndustry) {
            data.add(new Industry("无选择"));
            data.add(new Industry("行业1"));
            data.add(new Industry("行业2"));
            data.add(new Industry("行业3"));
            data.add(new Industry("行业4"));
        } else {
            data.add(new WarningType("无选择"));
            data.add(new WarningType("交他人驾驶1"));
            data.add(new WarningType("交他人驾驶2"));
            data.add(new WarningType("交他人驾驶3"));
            data.add(new WarningType("交他人驾驶4"));
        }
    }

    //------------------------------ 犯案次数相关 ---------------------------------------
    private CrimeListAdapter crimeListAdapter;
    private List<CrimeInfo> crimeInfos = new ArrayList<>();

    public CrimeListAdapter getCrimeListAdapter(CameraInfo info) {
        //根据CameraInfo来获取犯案次数列表

        //以下为测试数据
        if (crimeListAdapter == null) {
            crimeInfos.add(new CrimeInfo(2, "京BP9680", 170));
            crimeInfos.add(new CrimeInfo(6, "京BP9680", 20));
            crimeInfos.add(new CrimeInfo(10, "京BP9680", 1000));
            crimeInfos.add(new CrimeInfo(8, "京BP9680", 998));
            crimeInfos.add(new CrimeInfo(3, "京BP9680", 165));
            crimeInfos.add(new CrimeInfo(7, "京BP9680", 300));
            crimeListAdapter = new CrimeListAdapter(crimeInfos);
        }

        return crimeListAdapter;
    }

    public void clearCrimeList() {
        crimeInfos.clear();
        crimeListAdapter.notifyDataSetChanged();
    }

    public void updateCrimeList(CameraInfo cameraInfo) {
        //次数通过选择的摄像头来处理相关逻辑

        //以下为测试数据
        crimeInfos.clear();
        /*crimeInfos.add(new CrimeInfo(1, "京BP2540", 90));
        crimeInfos.add(new CrimeInfo(1, "京BP3290", 7));
        crimeInfos.add(new CrimeInfo(3, "京BP1510", 320));*/
        crimeListAdapter.notifyDataSetChanged();
    }

}
