package com.miu360.legworkwrit.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.di.module.ObtainImageModule;
import com.miu360.legworkwrit.mvp.ui.activity.ObtainImageActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ObtainImageModule.class, dependencies = AppComponent.class)
public interface ObtainImageComponent {
    void inject(ObtainImageActivity activity);
}