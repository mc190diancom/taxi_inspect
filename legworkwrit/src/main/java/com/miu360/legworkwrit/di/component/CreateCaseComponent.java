package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.CreateCaseModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.CreateCaseActivity;

@ActivityScope
@Component(modules = CreateCaseModule.class, dependencies = AppComponent.class)
public interface CreateCaseComponent {
    void inject(CreateCaseActivity activity);
}