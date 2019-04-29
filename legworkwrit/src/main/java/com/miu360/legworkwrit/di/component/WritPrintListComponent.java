package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.WritPrintListModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.WritPrintListActivity;

@ActivityScope
@Component(modules = WritPrintListModule.class, dependencies = AppComponent.class)
public interface WritPrintListComponent {
    void inject(WritPrintListActivity activity);
}