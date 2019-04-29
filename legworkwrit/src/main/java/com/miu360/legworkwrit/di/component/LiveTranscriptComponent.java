package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.LiveTranscriptModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.LiveTranscriptActivity;

@ActivityScope
@Component(modules = LiveTranscriptModule.class, dependencies = AppComponent.class)
public interface LiveTranscriptComponent {
    void inject(LiveTranscriptActivity activity);
}