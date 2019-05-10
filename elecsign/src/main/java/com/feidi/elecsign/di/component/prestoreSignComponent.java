package com.feidi.elecsign.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.elecsign.di.module.prestoreSignModule;
import com.feidi.elecsign.mvp.contract.prestoreSignContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.elecsign.mvp.ui.activity.prestoreSignActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/07/2019 10:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = prestoreSignModule.class, dependencies = AppComponent.class)
public interface prestoreSignComponent {
    void inject(prestoreSignActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        prestoreSignComponent.Builder view(prestoreSignContract.View view);

        prestoreSignComponent.Builder appComponent(AppComponent appComponent);

        prestoreSignComponent build();
    }
}