package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.CaseBasicModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.CaseBasicActivity;

@ActivityScope
@Component(modules = CaseBasicModule.class, dependencies = AppComponent.class)
public interface CaseBasicComponent {
    void inject(CaseBasicActivity activity);
}