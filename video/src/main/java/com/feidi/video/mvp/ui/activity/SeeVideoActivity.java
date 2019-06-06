package com.feidi.video.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerSeeVideoComponent;
import com.feidi.video.mvp.contract.SeeVideoContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.presenter.SeeVideoPresenter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.view.TextSwitch;
import com.miu30.common.ui.widget.IncludeHeader;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:46
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SeeVideoActivity extends BaseMvpActivity<SeeVideoPresenter> implements SeeVideoContract.View {
    @BindView(R2.id.textSwitch)
    TextSwitch textSwitch;
    @BindView(R2.id.tv_location)
    TextView tvLocation;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSeeVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_see_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initHeader();
        tvLocation.setText(((CameraInfo) getIntent().getParcelableExtra("cameraInfo")).getLocation());
        textSwitch.setOnCheckedChangeListener(new TextSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextSwitch textSwitch, boolean isChecked) {

            }
        });
    }

    private void initHeader() {
        IncludeHeader header = new IncludeHeader();
        header.init(self, "视频查看");
        header.setRightTextViewText("列表");
        header.setRightTextViewVisibility(View.VISIBLE);
        header.setRightTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("你点击了列表");
            }
        });
    }

}
