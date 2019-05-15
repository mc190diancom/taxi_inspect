package com.feidi.elecsign.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.feidi.elecsign.R;
import com.feidi.elecsign.R2;
import com.feidi.elecsign.di.component.DaggerElectronicSignatureComponent;
import com.feidi.elecsign.mvp.contract.ElectronicSignatureContract;
import com.feidi.elecsign.mvp.presenter.ElectronicSignaturePresenter;
import com.feidi.elecsign.mvp.ui.widget.ElecsignHeader;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;

import butterknife.OnClick;


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
 * <p>
 * 电子签名
 */
public class ElectronicSignatureActivity extends BaseMvpActivity<ElectronicSignaturePresenter> implements ElectronicSignatureContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerElectronicSignatureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_electronic_signature; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new ElecsignHeader().init(self, "电子签名");
    }

    @OnClick(R2.id.iv_authorization)
    public void goAuthorization(View view) {
        ActivityUtils.startActivity(AuthorizationActivity.class);
    }

    @OnClick(R2.id.iv_my_signature)
    public void goMySignature(View view) {

    }

}
