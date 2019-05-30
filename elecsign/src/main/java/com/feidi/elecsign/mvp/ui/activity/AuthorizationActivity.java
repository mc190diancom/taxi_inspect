package com.feidi.elecsign.mvp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.elecsign.R;
import com.feidi.elecsign.R2;
import com.feidi.elecsign.di.component.DaggerAuthorizationComponent;
import com.feidi.elecsign.mvp.contract.AuthorizationContract;
import com.feidi.elecsign.mvp.presenter.AuthorizationPresenter;
import com.feidi.elecsign.mvp.ui.adapter.AuthMyAdapter;
import com.feidi.elecsign.mvp.ui.adapter.MyAuthAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.view.TextSwitch;
import com.miu30.common.ui.widget.IncludeHeader;
import com.miu30.common.ui.widget.MultiVeriticalItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R2.id.rv_info)
    RecyclerView recyclerView;

    private MyAuthAdapter myAuthAdapter;
    private AuthMyAdapter authMyAdapter;

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
        new IncludeHeader().init(self, "授权");

        textSwitch.setOnCheckedChangeListener(new TextSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextSwitch textSwitch, boolean isChecked) {
                if (isChecked) {
                    recyclerView.setAdapter(myAuthAdapter);
                } else {
                    if (authMyAdapter == null) {
                        assert mPresenter != null;
                        authMyAdapter = mPresenter.getAuthMyAdapter();
                    }
                    recyclerView.setAdapter(authMyAdapter);
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        assert mPresenter != null;
        myAuthAdapter = mPresenter.getMyAuthAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分隔线
        recyclerView.addItemDecoration(new MultiVeriticalItemDecoration.Builder()
                .isDrawLastDivider(false)
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .setMarginLeft(SizeUtils.dp2px(13))
                .setMarginRight(SizeUtils.dp2px(13))
                .build());
        recyclerView.setAdapter(myAuthAdapter);
    }

    @OnClick(R2.id.tv_auth_switch)
    public void showAuthDes() {
        assert mPresenter != null;
        mPresenter.showAuthDescriptionDialog();
    }

}
