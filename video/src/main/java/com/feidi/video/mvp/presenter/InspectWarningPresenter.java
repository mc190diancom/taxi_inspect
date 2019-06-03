package com.feidi.video.mvp.presenter;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.mvp.contract.InspectWarningContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.feidi.video.mvp.ui.adapter.IndustryOrWarningTypeAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.ui.widget.MultiVeriticalItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/22/2019 09:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class InspectWarningPresenter extends BasePresenter<InspectWarningContract.Model, InspectWarningContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private IndustryOrWarningTypeAdapter adapter;

    @Inject
    public InspectWarningPresenter(InspectWarningContract.Model model, InspectWarningContract.View rootView) {
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
    public List<CameraInfo> getCameraInfos() {
        List<CameraInfo> infos = new ArrayList<>();
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
        return infos;
    }

    private List<String> data = new ArrayList<>();

    public List<String> getIndustryOrWarningTypeInfos() {
        return data;
    }

    //测试数据
    public void setIndustryOrWarningTypeInfos(boolean isIndustry) {
        data.clear();

        if (isIndustry) {
            data.add("无选择");
            data.add("行业1");
            data.add("行业2");
            data.add("行业3");
            data.add("行业4");
        } else {
            data.add("无选择");
            data.add("交他人驾驶");
            data.add("交他人驾驶");
            data.add("交他人驾驶");
            data.add("交他人驾驶");
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

    public void updateCrimeList(CameraInfo cameraInfo) {
        //次数通过选择的摄像头来处理相关逻辑

        //以下为测试数据
        crimeInfos.clear();
        crimeInfos.add(new CrimeInfo(1, "京BP2540", 90));
        crimeInfos.add(new CrimeInfo(1, "京BP3290", 7));
        crimeInfos.add(new CrimeInfo(3, "京BP1510", 320));
        crimeListAdapter.notifyDataSetChanged();
    }
}
