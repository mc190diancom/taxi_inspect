package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.FristRegisterNoticeModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.FristRegisterNoticeActivity;

@ActivityScope
@Component(modules = FristRegisterNoticeModule.class, dependencies = AppComponent.class)
public interface FristRegisterNoticeComponent {
    void inject(FristRegisterNoticeActivity activity);
}