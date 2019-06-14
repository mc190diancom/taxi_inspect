package com.feidi.video.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.feidi.video.mvp.contract.VideoPlayContract;


@ActivityScope
public class VideoPlayPresenter extends BasePresenter<VideoPlayContract.Model, VideoPlayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;


    @Inject
    public VideoPlayPresenter(VideoPlayContract.Model model, VideoPlayContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
