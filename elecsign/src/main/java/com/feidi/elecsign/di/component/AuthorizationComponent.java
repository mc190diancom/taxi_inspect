package com.feidi.elecsign.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.elecsign.di.module.AuthorizationModule;
import com.feidi.elecsign.mvp.contract.AuthorizationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.elecsign.mvp.ui.activity.AuthorizationActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/14/2019 18:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = AuthorizationModule.class, dependencies = AppComponent.class)
public interface AuthorizationComponent {
    void inject(AuthorizationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AuthorizationComponent.Builder view(AuthorizationContract.View view);

        AuthorizationComponent.Builder appComponent(AppComponent appComponent);

        AuthorizationComponent build();
    }
}