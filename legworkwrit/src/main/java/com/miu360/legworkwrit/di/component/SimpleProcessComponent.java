package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.SimpleProcessModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.SimpleProcessActivity;

@ActivityScope
@Component(modules = SimpleProcessModule.class, dependencies = AppComponent.class)
public interface SimpleProcessComponent {
    void inject(SimpleProcessActivity activity);
}