package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.CaseListContract;
import com.miu360.legworkwrit.mvp.model.CaseListModel;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class CaseListModule {
    private CaseListContract.View view;

    /**
     * 构建CaseListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     */
    public CaseListModule(CaseListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CaseListContract.View provideCaseListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseListContract.Model provideCaseListModel(CaseListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<Case> provideCaseList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }
}