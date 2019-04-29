package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu30.common.data.UserPreference;
import com.miu360.legworkwrit.mvp.contract.CaseBasicContract;
import com.miu360.legworkwrit.mvp.model.CaseBasicModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;


@Module
public class CaseBasicModule {
    private CaseBasicContract.View view;
    private Activity activity;

    /**
     * 构建CaseBasicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CaseBasicModule(CaseBasicContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    CaseBasicContract.View provideCaseBasicView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseBasicContract.Model provideCaseBasicModel(CaseBasicModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    Validator providesValid() {
        return new Validator(activity);
    }
}