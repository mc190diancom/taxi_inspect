package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu30.common.data.UserPreference;
import com.miu360.legworkwrit.mvp.contract.CreateCaseContract;
import com.miu360.legworkwrit.mvp.model.CreateCaseModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;


@Module
public class CreateCaseModule {
    private CreateCaseContract.View view;
    private Activity activity;

    /**
     * 构建CreateCaseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CreateCaseModule(CreateCaseContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    CreateCaseContract.View provideCreateCaseView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CreateCaseContract.Model provideCreateCaseModel(CreateCaseModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    UserPreference providesUser() {
        return new UserPreference(activity);
    }

    @ActivityScope
    @Provides
    Validator providesValid() {
        return new Validator(activity);
    }
}