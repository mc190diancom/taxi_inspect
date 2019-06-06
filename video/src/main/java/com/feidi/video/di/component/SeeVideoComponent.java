package com.feidi.video.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.video.di.module.SeeVideoModule;
import com.feidi.video.mvp.contract.SeeVideoContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.video.mvp.ui.activity.SeeVideoActivity;


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
@ActivityScope
@Component(modules = SeeVideoModule.class, dependencies = AppComponent.class)
public interface SeeVideoComponent {
    void inject(SeeVideoActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SeeVideoComponent.Builder view(SeeVideoContract.View view);

        SeeVideoComponent.Builder appComponent(AppComponent appComponent);

        SeeVideoComponent build();
    }
}