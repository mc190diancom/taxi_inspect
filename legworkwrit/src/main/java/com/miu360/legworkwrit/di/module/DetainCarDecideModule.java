package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.contract.DetainCarDecideContract;
import com.miu360.legworkwrit.mvp.model.DetainCarDecideModel;
import com.miu360.legworkwrit.mvp.ui.adapter.BasicInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


@Module
public class DetainCarDecideModule {
    private DetainCarDecideContract.View view;
    private Activity activity;

    /**
     * 构建DetainCarDecideModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DetainCarDecideModule(DetainCarDecideContract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    DetainCarDecideContract.View provideDetainCarDecideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DetainCarDecideContract.Model provideDetainCarDecideModel(DetainCarDecideModel model) {
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
    ArrayList<VehicleInfo> provideVehicleList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    CarInfoAdapter providesCarInfoAdapter(ArrayList<VehicleInfo> data) {
        return new CarInfoAdapter(data,activity);
    }

}