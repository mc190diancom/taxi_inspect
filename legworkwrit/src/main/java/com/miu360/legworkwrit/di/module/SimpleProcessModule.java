package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.SimpleProcessContract;
import com.miu360.legworkwrit.mvp.model.SimpleProcessModel;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.ui.adapter.ChooseInstrumentAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;


@Module
public class SimpleProcessModule {
    private SimpleProcessContract.View view;
    private Activity activity;

    /**
     * 构建SimpleProcessModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SimpleProcessModule(SimpleProcessContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    SimpleProcessContract.View provideSimpleProcessView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SimpleProcessContract.Model provideSimpleProcessModel(SimpleProcessModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder provideHeaderHolder() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    ArrayList<CaseStatus> provideStringArrayList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    ChooseInstrumentAdapter providesChooseInstrumentAdapter(ArrayList<CaseStatus> instruments) {
        return new ChooseInstrumentAdapter(instruments,activity);
    }
}