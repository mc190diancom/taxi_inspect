package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.CaseSearchModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.CaseSearchActivity;

@ActivityScope
@Component(modules = CaseSearchModule.class, dependencies = AppComponent.class)
public interface CaseSearchComponent {
    void inject(CaseSearchActivity activity);
}