package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.InquiryRecordContract;
import com.miu360.legworkwrit.mvp.model.InquiryRecordModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import dagger.Module;
import dagger.Provides;


@Module
public class InquiryRecordModule {
    private InquiryRecordContract.View view;

    /**
     * 构建InquiryRecordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InquiryRecordModule(InquiryRecordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InquiryRecordContract.View provideInquiryRecordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InquiryRecordContract.Model provideInquiryRecordModel(InquiryRecordModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder provideHeaderHolder() {
        return new HeaderHolder();
    }
}