package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.model.CaseSignModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.WebViewFactory;


@Module
public class CaseSignModule {

    private CaseSignContract.View view;

    public CaseSignModule(CaseSignContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CaseSignContract.View provideCaseSignView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseSignContract.Model provideCaseSignModel(CaseSignModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    WebViewFactory provideWebViewFactory() {
        return WebViewFactory.create(view.getActivity(), R.id.wv_document);
    }

    @ActivityScope
    @Provides
    HeaderHolder provideHeaderHolder() {
        return new HeaderHolder();
    }

}