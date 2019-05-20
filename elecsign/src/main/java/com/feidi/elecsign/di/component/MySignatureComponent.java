package com.feidi.elecsign.di.component;

import com.feidi.elecsign.di.module.MySignatureModule;
import com.feidi.elecsign.mvp.contract.MySignatureContract;
import com.feidi.elecsign.mvp.ui.activity.MySignatureActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/16/2019 14:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = MySignatureModule.class, dependencies = AppComponent.class)
public interface MySignatureComponent {
    void inject(MySignatureActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MySignatureComponent.Builder view(MySignatureContract.View view);

        MySignatureComponent.Builder appComponent(AppComponent appComponent);

        MySignatureComponent build();
    }
}