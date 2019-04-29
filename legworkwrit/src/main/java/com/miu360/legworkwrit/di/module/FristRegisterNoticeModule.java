package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.contract.FristRegisterNoticeContract;
import com.miu360.legworkwrit.mvp.model.FristRegisterNoticeModel;
import com.miu360.legworkwrit.mvp.ui.adapter.BasicInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;


@Module
public class FristRegisterNoticeModule {
    private FristRegisterNoticeContract.View view;
    private Activity activity;

    /**
     * 构建FristRegisterNoticeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FristRegisterNoticeModule(FristRegisterNoticeContract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    FristRegisterNoticeContract.View provideFristRegisterNoticeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FristRegisterNoticeContract.Model provideFristRegisterNoticeModel(FristRegisterNoticeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HeaderHolder provideHeaderHolder() {
        return new HeaderHolder();
    }

    @ActivityScope
    @Provides
    Validator providesValid() {
        return new Validator(activity);
    }

    @ActivityScope
    @Provides
    ArrayList<VehicleInfo> provideCaseList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    CarInfoAdapter providesBasicInfoAdapter(ArrayList<VehicleInfo> data) {
        return new CarInfoAdapter(data,activity);
    }
}