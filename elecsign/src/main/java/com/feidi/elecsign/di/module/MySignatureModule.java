package com.feidi.elecsign.di.module;

import com.feidi.elecsign.mvp.contract.MySignatureContract;
import com.feidi.elecsign.mvp.model.MySignatureModel;

import dagger.Binds;
import dagger.Module;


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
@Module
public abstract class MySignatureModule {

    @Binds
    abstract MySignatureContract.Model bindMySignatureModel(MySignatureModel model);
}