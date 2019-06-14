package com.feidi.video.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.video.di.module.VideoPlayModule;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.video.mvp.ui.activity.VideoPlayActivity;

@ActivityScope
@Component(modules = VideoPlayModule.class, dependencies = AppComponent.class)
public interface VideoPlayComponent {
    void inject(VideoPlayActivity activity);

}