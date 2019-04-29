package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.LiveTranscriptContract;
import com.miu360.legworkwrit.mvp.model.LiveTranscriptModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;

import dagger.Module;
import dagger.Provides;


@Module
public class LiveTranscriptModule {
    private LiveTranscriptContract.View view;
    private Activity activity;

    /**
     * 构建LiveTranscriptModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     */
    public LiveTranscriptModule(LiveTranscriptContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    LiveTranscriptContract.View provideLiveTranscriptView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LiveTranscriptContract.Model provideLiveTranscriptModel(LiveTranscriptModel model) {
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
}