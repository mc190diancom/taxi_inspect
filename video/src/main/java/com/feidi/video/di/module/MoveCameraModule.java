package com.feidi.video.di.module;

import android.app.Activity;

import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.ui.adapter.CameraListAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.feidi.video.mvp.contract.MoveCameraContract;
import com.feidi.video.mvp.model.MoveCameraModel;
import com.miu30.common.ui.entity.DriverInfo;

import java.util.ArrayList;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/03/2019 13:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class MoveCameraModule {

    @Binds
    abstract MoveCameraContract.Model bindMoveCameraModel(MoveCameraModel model);

}