package com.miu360.legworkwrit.di.module;

import android.support.v7.widget.LinearLayoutManager;

import com.jess.arms.di.scope.ActivityScope;
import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.mvp.contract.IllegalDetailActivityContract;
import com.miu360.legworkwrit.mvp.model.IllegalDetailActivityModel;
import com.miu360.legworkwrit.mvp.ui.adapter.IllegalDetailAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class IllegalDetailActivityModule {
    private IllegalDetailActivityContract.View view;

    /**
     * 构建IllegalDetailActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public IllegalDetailActivityModule(IllegalDetailActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    IllegalDetailActivityContract.View provideIllegalDetailActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    IllegalDetailActivityContract.Model provideIllegalDetailActivityModel(IllegalDetailActivityModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder proiverHeader() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    List<JCItem> provideList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    IllegalDetailAdapter provideAdatper(List<JCItem> jcItems) {
        return new IllegalDetailAdapter(jcItems, true);
    }

    @ActivityScope
    @Provides
    LinearLayoutManager provideLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }
}