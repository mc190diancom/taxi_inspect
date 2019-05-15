package com.feidi.elecsign.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.feidi.elecsign.di.module.ElectronicSignatureModule;
import com.feidi.elecsign.mvp.contract.ElectronicSignatureContract;

import com.jess.arms.di.scope.ActivityScope;
import com.feidi.elecsign.mvp.ui.activity.ElectronicSignatureActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/14/2019 15:35
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ElectronicSignatureModule.class, dependencies = AppComponent.class)
public interface ElectronicSignatureComponent {
    void inject(ElectronicSignatureActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ElectronicSignatureComponent.Builder view(ElectronicSignatureContract.View view);

        ElectronicSignatureComponent.Builder appComponent(AppComponent appComponent);

        ElectronicSignatureComponent build();
    }
}