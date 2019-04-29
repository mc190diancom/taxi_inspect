package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseSearchComponent;
import com.miu360.legworkwrit.di.module.CaseSearchModule;
import com.miu360.legworkwrit.mvp.contract.CaseSearchContract;
import com.miu360.legworkwrit.mvp.presenter.CaseSearchPresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

//文书打印——案件检索
public class CaseSearchActivity extends BaseMvpActivity<CaseSearchPresenter> implements CaseSearchContract.View {
    @BindView(R2.id.tv_industry)
    TextView tvIndustry;
    @BindView(R2.id.et_car_license)
    EditText etCarLicense;
    @BindView(R2.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R2.id.tv_end_time)
    TextView tvEndTime;

    @Inject
    HeaderHolder header;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseSearchComponent
                .builder()
                .appComponent(appComponent)
                .caseSearchModule(new CaseSearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "案件检索");
        mPresenter.init(self);
    }

    @OnClick(R2.id.tv_industry)
    public void chooseIndustry() {
        mPresenter.chooseIndustryCategory(tvIndustry);
    }

    @OnClick(R2.id.tv_start_time)
    public void chooseStartTime(TextView textView) {
        mPresenter.chooseStartTime(textView);
    }

    @OnClick(R2.id.tv_end_time)
    public void chooseEndTime(TextView textView) {
        mPresenter.chooseEndTime(textView);
    }

    @OnClick(R2.id.btn_search)
    public void search() {
        Intent intent = new Intent(self, CaseListActivity.class);
        intent.putExtra("from", CaseListActivity.FROM_PRINTER);

        String industry = tvIndustry.getText().toString();
        if (TextUtils.isEmpty(industry) || "全部".equals(industry)) {
            intent.putExtra("industry", "");
        } else {
            intent.putExtra("industry", industry);
        }

        intent.putExtra("license", TextUtils.isEmpty(etCarLicense.getText().toString()) ? "" : etCarLicense.getText().toString().toUpperCase());
        intent.putExtra("start_time", tvStartTime.getText().toString());
        intent.putExtra("end_time", tvEndTime.getText().toString());
        ActivityUtils.startActivity(intent);
    }

}
