package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerInquiryRecordComponent;
import com.miu360.legworkwrit.di.module.InquiryRecordModule;
import com.miu360.legworkwrit.mvp.contract.InquiryRecordContract;
import com.miu360.legworkwrit.mvp.presenter.InquiryRecordPresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.DialogUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 询问笔录
 */
public class InquiryRecordActivity extends BaseMvpActivity<InquiryRecordPresenter> implements InquiryRecordContract.View {
    public static final int FINISH_CODE = 0x0010;

    @BindView(R2.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R2.id.tv_end_time)
    TextView tvEndTime;

    @BindView(R2.id.btn_go_upload)
    Button btnGoUpload;

    @Inject
    HeaderHolder header;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInquiryRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inquiryRecordModule(new InquiryRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inquiry_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "询问笔录");
        btnGoUpload.setClickable(false);

        mPresenter.init(self, tvStartTime, tvEndTime);
    }

    @OnClick(R2.id.tv_start_time)
    public void chooseStartTime() {
        mPresenter.chooseStartTime(tvStartTime.getHint().toString());
    }

    @OnClick(R2.id.tv_end_time)
    public void chooseEndTime() {
        mPresenter.chooseEndTime(tvEndTime.getHint().toString());
    }

    @OnClick(R2.id.btn_go_upload)
    public void goUpload() {
        assert mPresenter != null;
        if (mPresenter.checkTime(tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim())) {
            Intent intent = new Intent(this, ObtainImageActivity.class);
            intent.putExtra("start_time", tvStartTime.getText().toString().trim());
            intent.putExtra("end_time", tvEndTime.getText().toString().trim());
            startActivityForResult(intent, InquiryRecordActivity.FINISH_CODE);
        }
    }

    @OnClick(R2.id.ibtn_timeline)
    public void showTimeLineDialog() {
        DialogUtil.showTimeLineDialog(self, Config.T_ZPDZ, new DialogUtil.OnNoCaseListener() {
            @Override
            public void noCase() {
                UIUtils.toast(self, "未创建案件", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FINISH_CODE) {
                finish();
            }
        }
    }

    @Override
    public void showStartTime(String time) {
        tvStartTime.setText(time);

        if (!TextUtils.isEmpty(tvEndTime.getText().toString())) {
            btnGoUpload.setBackgroundResource(R.drawable.bg_common_button_shape);
            btnGoUpload.setClickable(true);
        }
    }

    @Override
    public void showEndTime(String time) {
        tvEndTime.setText(time);

        if (!TextUtils.isEmpty(tvStartTime.getText().toString())) {
            btnGoUpload.setBackgroundResource(R.drawable.bg_common_button_shape);
            btnGoUpload.setClickable(true);
        }
    }
}
