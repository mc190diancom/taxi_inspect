package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.IllegalDetailActivityModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.IllegalDetailActivityActivity;

@ActivityScope
@Component(modules = {IllegalDetailActivityModule.class}, dependencies = AppComponent.class)
public interface IllegalDetailActivityComponent {
    void inject(IllegalDetailActivityActivity activity);
}