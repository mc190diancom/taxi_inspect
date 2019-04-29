package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.DetainCarFormModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.DetainCarFormActivity;

@ActivityScope
@Component(modules = DetainCarFormModule.class, dependencies = AppComponent.class)
public interface DetainCarFormComponent {
    void inject(DetainCarFormActivity activity);
}