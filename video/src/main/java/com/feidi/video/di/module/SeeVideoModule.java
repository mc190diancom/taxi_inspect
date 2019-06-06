package com.feidi.video.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.feidi.video.mvp.contract.SeeVideoContract;
import com.feidi.video.mvp.model.SeeVideoModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:46
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class SeeVideoModule {

    @Binds
    abstract SeeVideoContract.Model bindSeeVideoModel(SeeVideoModel model);
}