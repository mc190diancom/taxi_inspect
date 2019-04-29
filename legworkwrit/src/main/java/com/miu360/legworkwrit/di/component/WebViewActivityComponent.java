package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.WebViewActivityModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.WebViewActivity;

@ActivityScope
@Component(modules = WebViewActivityModule.class, dependencies = AppComponent.class)
public interface WebViewActivityComponent {
    void inject(WebViewActivity activity);
}