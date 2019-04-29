package com.miu360.legworkwrit.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.di.module.UploadListModule;
import com.miu360.legworkwrit.mvp.ui.activity.UploadListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = UploadListModule.class, dependencies = AppComponent.class)
public interface UploadListComponent {
    void inject(UploadListActivity activity);
}