package com.feidi.video.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.video.di.module.MoveCameraModule;
import com.feidi.video.mvp.contract.MoveCameraContract;

import com.jess.arms.di.scope.FragmentScope;
import com.feidi.video.mvp.ui.fragment.MoveCameraFragment;


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
@FragmentScope
@Component(modules = MoveCameraModule.class, dependencies = AppComponent.class)
public interface MoveCameraComponent {
    void inject(MoveCameraFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MoveCameraComponent.Builder view(MoveCameraContract.View view);

        MoveCameraComponent.Builder appComponent(AppComponent appComponent);

        MoveCameraComponent build();
    }
}