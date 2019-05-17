package com.feidi.elecsign.mvp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feidi.elecsign.R;
import com.feidi.elecsign.R2;
import com.feidi.elecsign.di.component.DaggerAuthorizationComponent;
import com.feidi.elecsign.mvp.contract.AuthorizationContract;
import com.feidi.elecsign.mvp.model.entity.MyAuth;
import com.feidi.elecsign.mvp.presenter.AuthorizationPresenter;
import com.feidi.elecsign.mvp.ui.adapter.AuthMyAdapter;
import com.feidi.elecsign.mvp.ui.adapter.MyAuthAdapter;
import com.feidi.elecsign.mvp.ui.view.TextSwitch;
import com.feidi.elecsign.mvp.ui.view.ThreeStateSwitch;
import com.feidi.elecsign.mvp.ui.widget.ElecsignHeader;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;

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
        new ElecsignHeader().init(self, "授权");

        textSwitch.setOnCheckedChangeListener(new TextSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextSwitch textSwitch, boolean isChecked) {

            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
        if (drawable != null) {
            //decoration.setDrawable(new ColorDrawable(Color.RED));
            //设置分割线
            recyclerView.addItemDecoration(decoration);
        }

        //测试数据
        List<MyAuth> myAuths = new ArrayList<>();
        myAuths.add(new MyAuth("李毅", "00012221", "3天后到期", ThreeStateSwitch.STATE_ON));
        myAuths.add(new MyAuth("三三", "00012024", "2天后到期", ThreeStateSwitch.STATE_CRITICAL));
        myAuths.add(new MyAuth("四四", "01012021", "1天后到期", ThreeStateSwitch.STATE_ON));
        myAuths.add(new MyAuth("呜呜", "00013021", "今日到期", ThreeStateSwitch.STATE_OFF));
        myAuths.add(new MyAuth("溜溜", "10012021", "", ThreeStateSwitch.STATE_ON));
        myAuthAdapter = new MyAuthAdapter(myAuths);
        recyclerView.setAdapter(myAuthAdapter);
    }

    @OnClick(R2.id.tv_auth_switch)
    public void showAuthDes() {
        assert mPresenter != null;
        mPresenter.showAuthDescriptionDialog();
    }

}
