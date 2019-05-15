package com.feidi.elecsign.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feidi.elecsign.R;
import com.feidi.elecsign.R2;
import com.feidi.elecsign.di.component.DaggerAuthorizationComponent;
import com.feidi.elecsign.mvp.contract.AuthorizationContract;
import com.feidi.elecsign.mvp.presenter.AuthorizationPresenter;
import com.feidi.elecsign.mvp.ui.view.TextSwitch;
import com.feidi.elecsign.mvp.ui.widget.ElecsignHeader;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;

import butterknife.BindView;


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
public class AuthorizationActivity extends BaseMvpActivity<AuthorizationPresenter> implements AuthorizationContract.View {
    @BindView(R2.id.textSwitch)
    TextSwitch textSwitch;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAuthorizationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_authorization; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new ElecsignHeader().init(self, "授权");
        textSwitch.setOnCheckedChangeListener(new TextSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextSwitch textSwitch, boolean isChecked) {

            }
        });

    }
}
