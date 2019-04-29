package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.NormalProcessModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.NormalProcessActivity;

@ActivityScope
@Component(modules = NormalProcessModule.class, dependencies = AppComponent.class)
public interface NormalProcessComponent {
    void inject(NormalProcessActivity activity);
}