package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.TalkNoticeModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.TalkNoticeActivity;

@ActivityScope
@Component(modules = TalkNoticeModule.class, dependencies = AppComponent.class)
public interface TalkNoticeComponent {
    void inject(TalkNoticeActivity activity);
}