package com.feidi.video.mvp.presenter;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.mvp.contract.SeeVideoListContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
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
        List<CameraInfo> infos = new ArrayList<>();
        infos.add(new CameraInfo("摄像头1", "北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头2", "北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头3", "北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头4", "北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头5", "北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头6", "北京大兴区荣华南路北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头7", "北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头8", "北京大兴区荣华南路北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头9", "北京大兴区荣华南路北京大兴区荣华南路"));
        infos.add(new CameraInfo("摄像头10", "北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路北京大兴区荣华南路"));
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

}
