package com.feidi.video.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.video.di.module.SeeVideoListModule;
import com.feidi.video.mvp.contract.SeeVideoListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.video.mvp.ui.activity.SeeVideoListActivity;


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
@ActivityScope
@Component(modules = SeeVideoListModule.class, dependencies = AppComponent.class)
public interface SeeVideoListComponent {
    void inject(SeeVideoListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SeeVideoListComponent.Builder view(SeeVideoListContract.View view);

        SeeVideoListComponent.Builder appComponent(AppComponent appComponent);

        SeeVideoListComponent build();
    }
}