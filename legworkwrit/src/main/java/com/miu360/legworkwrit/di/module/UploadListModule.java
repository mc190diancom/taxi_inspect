package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.UploadListContract;
import com.miu360.legworkwrit.mvp.model.UploadListModel;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;


@Module
public class UploadListModule {
    private UploadListContract.View view;

    /**
     * 构建UploadListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UploadListModule(UploadListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UploadListContract.View provideUploadListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UploadListContract.Model provideUploadListModel(UploadListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    ArrayList<InquiryRecordPhoto> provideUploadInfoArrayList() {
        return new ArrayList<>();
    }

}