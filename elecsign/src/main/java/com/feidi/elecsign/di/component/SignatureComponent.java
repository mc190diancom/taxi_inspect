package com.feidi.elecsign.di.component;

import com.feidi.elecsign.di.module.SignatureModule;
import com.feidi.elecsign.mvp.contract.SignatureContract;
import com.feidi.elecsign.mvp.ui.activity.SignatureActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/16/2019 13:55
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SignatureModule.class, dependencies = AppComponent.class)
public interface SignatureComponent {
    void inject(SignatureActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SignatureComponent.Builder view(SignatureContract.View view);

        SignatureComponent.Builder appComponent(AppComponent appComponent);

        SignatureComponent build();
    }
}