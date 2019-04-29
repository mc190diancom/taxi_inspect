package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.LiveCheckRecordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.LiveCheckRecordActivity;

@ActivityScope
@Component(modules = LiveCheckRecordModule.class, dependencies = AppComponent.class)
public interface LiveCheckRecordComponent {
    void inject(LiveCheckRecordActivity activity);
}