package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.CaseSignModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.CaseSignActivity;

@ActivityScope
@Component(modules = CaseSignModule.class, dependencies = AppComponent.class)
public interface CaseSignComponent {
    void inject(CaseSignActivity activity);

}