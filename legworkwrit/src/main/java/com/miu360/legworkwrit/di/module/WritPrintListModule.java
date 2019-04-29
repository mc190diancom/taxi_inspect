package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu360.legworkwrit.mvp.contract.WritPrintListContract;
import com.miu360.legworkwrit.mvp.model.WritPrintListModel;
import com.miu360.legworkwrit.mvp.ui.adapter.ChooseInstrumentAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;


@Module
public class WritPrintListModule {
    private WritPrintListContract.View view;

    /**
     * 构建WritPrintListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WritPrintListModule(WritPrintListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WritPrintListContract.View provideWritPrintListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WritPrintListContract.Model provideWritPrintListModel(WritPrintListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    ArrayList<String> provideStringArrayList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }
}