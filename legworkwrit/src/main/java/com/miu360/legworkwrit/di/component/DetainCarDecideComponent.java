package com.miu360.legworkwrit.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.di.module.DetainCarDecideModule;
import com.miu360.legworkwrit.mvp.ui.activity.DetainCarDecideActivity;

import dagger.Component;

@ActivityScope
@Component(modules = DetainCarDecideModule.class, dependencies = AppComponent.class)
public interface DetainCarDecideComponent {
    void inject(DetainCarDecideActivity activity);
}