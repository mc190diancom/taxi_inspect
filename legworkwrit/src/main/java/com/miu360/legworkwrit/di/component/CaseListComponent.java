package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.CaseListModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.CaseListActivity;

@ActivityScope
@Component(modules = CaseListModule.class, dependencies = AppComponent.class)
public interface CaseListComponent {
    void inject(CaseListActivity activity);
}