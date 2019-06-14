package com.feidi.video.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.feidi.video.mvp.contract.VideoPlayContract;
import com.feidi.video.mvp.model.VideoPlayModel;


@Module
public class VideoPlayModule {

    private VideoPlayContract.View view;

    public VideoPlayModule(VideoPlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VideoPlayContract.View provideVideoPlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VideoPlayContract.Model provideVideoPlayModel(VideoPlayModel model) {
        return model;
    }

}