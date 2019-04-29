package com.miu360.legworkwrit.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.miu360.legworkwrit.di.module.InquiryRecordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.ui.activity.InquiryRecordActivity;

@ActivityScope
@Component(modules = InquiryRecordModule.class, dependencies = AppComponent.class)
public interface InquiryRecordComponent {
    void inject(InquiryRecordActivity activity);
}