package com.feidi.video.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.feidi.video.mvp.contract.SeeVideoListContract;
import com.feidi.video.mvp.model.SeeVideoListModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class SeeVideoListModule {

    @Binds
    abstract SeeVideoListContract.Model bindSeeVideoListModel(SeeVideoListModel model);
}