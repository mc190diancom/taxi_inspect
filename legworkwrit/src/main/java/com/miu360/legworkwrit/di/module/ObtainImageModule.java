package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu360.legworkwrit.mvp.contract.ObtainImageContract;
import com.miu360.legworkwrit.mvp.model.ObtainImageModel;


@Module
public class ObtainImageModule {
    private ObtainImageContract.View view;

    /**
     * 构建ObtainImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ObtainImageModule(ObtainImageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ObtainImageContract.View provideObtainImageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ObtainImageContract.Model provideObtainImageModel(ObtainImageModel model) {
        return model;
    }
}