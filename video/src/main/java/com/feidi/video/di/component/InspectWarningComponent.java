package com.feidi.video.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.video.di.module.InspectWarningModule;
import com.feidi.video.mvp.contract.InspectWarningContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.video.mvp.ui.activity.InspectWarningActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/22/2019 09:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = InspectWarningModule.class, dependencies = AppComponent.class)
public interface InspectWarningComponent {
    void inject(InspectWarningActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        InspectWarningComponent.Builder view(InspectWarningContract.View view);

        InspectWarningComponent.Builder appComponent(AppComponent appComponent);

        InspectWarningComponent build();
    }
}