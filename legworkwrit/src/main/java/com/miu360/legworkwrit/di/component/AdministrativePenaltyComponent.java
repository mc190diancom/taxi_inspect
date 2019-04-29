package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.AdministrativePenaltyModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.AdministrativePenaltyActivity;

@ActivityScope
@Component(modules = AdministrativePenaltyModule.class, dependencies = AppComponent.class)
public interface AdministrativePenaltyComponent {
    void inject(AdministrativePenaltyActivity activity);
}