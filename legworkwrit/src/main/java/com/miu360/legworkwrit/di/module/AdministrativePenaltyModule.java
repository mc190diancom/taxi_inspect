package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.legworkwrit.mvp.contract.AdministrativePenaltyContract;
import com.miu360.legworkwrit.mvp.model.AdministrativePenaltyModel;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.ui.adapter.BasicInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.List;


@Module
public class AdministrativePenaltyModule {
    private AdministrativePenaltyContract.View view;
    private Activity activity;

    /**
     * 构建AdministrativePenaltyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AdministrativePenaltyModule(AdministrativePenaltyContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    AdministrativePenaltyContract.View provideAdministrativePenaltyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AdministrativePenaltyContract.Model provideAdministrativePenaltyModel(AdministrativePenaltyModel model) {
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

    @ActivityScope
    @Provides
    ArrayList<DriverInfo> provideCaseList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    BasicInfoAdapter providesBasicInfoAdapter(ArrayList<DriverInfo> data) {
        return new BasicInfoAdapter(data,activity);
    }
}