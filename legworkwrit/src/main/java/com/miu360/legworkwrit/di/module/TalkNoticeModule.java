package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.TalkNoticeContract;
import com.miu360.legworkwrit.mvp.model.TalkNoticeModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.Validator;

import dagger.Module;
import dagger.Provides;


@Module
public class TalkNoticeModule {
    private TalkNoticeContract.View view;
    private Activity activity;

    /**
     * 构建TalkNoticeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TalkNoticeModule(TalkNoticeContract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    TalkNoticeContract.View provideTalkNoticeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TalkNoticeContract.Model provideTalkNoticeModel(TalkNoticeModel model) {
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