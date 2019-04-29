package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu360.legworkwrit.mvp.contract.CaseSearchContract;
import com.miu360.legworkwrit.mvp.model.CaseSearchModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;


@Module
public class CaseSearchModule {
    private CaseSearchContract.View view;

    /**
     * 构建CaseSearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CaseSearchModule(CaseSearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CaseSearchContract.View provideCaseSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseSearchContract.Model provideCaseSearchModel(CaseSearchModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }
}