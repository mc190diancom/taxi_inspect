package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.validError;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseBasicComponent;
import com.miu360.legworkwrit.di.module.CaseBasicModule;
import com.miu360.legworkwrit.mvp.contract.CaseBasicContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.presenter.CaseBasicPresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class CaseBasicActivity extends BaseMvpActivity<CaseBasicPresenter> implements CaseBasicContract.View, Validator.ValidationListener {
    @NotEmpty
    @BindView(R2.id.tv_industry_type)
    TextView tvIndustryType;
    @BindView(R2.id.tv_city_abbreviation)
    TextView tvCityAbbreviation;
    @NotEmpty(message = "请输入车牌号")
    @Length(min = 6, message = "车牌号长度不正确")
    @BindView(R2.id.et_car_license)
    EditText etCarLicense;
    @BindView(R2.id.ll_car_num)
    LinearLayout llCarNum;
    @NotEmpty
    @BindView(R2.id.et_inspected)
    EditText etInspected;
    @NotEmpty
    @BindView(R2.id.tv_law_enforcement_personne1)
    TextView tvLawEnforcementPersonne1;
    @NotEmpty
    @BindView(R2.id.tv_law_enforcement_personne2)
    TextView tvLawEnforcementPersonne2;
    @NotEmpty
    @BindView(R2.id.tv_law_enforcement_address)
    TextView tvLawEnforcementAddress;
    @BindView(R2.id.tv_select_address)
    TextView tvSelectAddress;
    @NotEmpty
    @BindView(R2.id.tv_law_enforcement_district)
    TextView tvLawEnforcementDistrict;
    @NotEmpty(message = "请确认创建时间")
    @BindView(R2.id.tv_creation_time)
    TextView tvCreationTime;
    @NotEmpty
    @BindView(R2.id.tv_punishment_way)
    TextView tvPunishmentWay;
    @NotEmpty
    @BindView(R2.id.tv_source_case)
    TextView tvSourceCase;
    @BindView(R2.id.btn_choose_instrument)
    Button btnChooseInstrument;

    @Inject
    HeaderHolder header;

    @Inject
    Validator mValidator;

    private Case mCase;
    private AddMsg mAddMsg;
    public ArrayList<CaseStatus> followInstruments;
    public String currentInstrument;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseBasicComponent
                .builder()
                .appComponent(appComponent)
                .caseBasicModule(new CaseBasicModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_basic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "常规信息");
        //注册验证
        mValidator.setValidationListener(this);
        mCase = CacheManager.getInstance().getCase();
        if (mCase == null) {
            UIUtils.toast(self, "未获取到案件信息", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 7));
        etInspected.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        initViewWithCase();
        followInstruments = getIntent().getParcelableArrayListExtra("WritedInstrument");
        currentInstrument = getIntent().getStringExtra("CurrentInstrument");
        assert mPresenter != null;
        mPresenter.init(self);
    }

    private void initViewWithCase() {
        tvIndustryType.setText(mCase.getHYLB());
        if (mCase.getVNAME() != null && mCase.getVNAME().length() > 6) {
            tvCityAbbreviation.setText(mCase.getVNAME().substring(0, 1));
            etCarLicense.setText(mCase.getVNAME().substring(1));
        } else {
            tvCityAbbreviation.setText("京");
            etCarLicense.setText(mCase.getVNAME());
        }
        etInspected.setText(mCase.getBJCR());
        tvLawEnforcementPersonne1.setText(mCase.getZFRYNAME1());
        tvLawEnforcementPersonne2.setText(mCase.getZFRYNAME2());
        tvLawEnforcementAddress.setText(mCase.getJCDD());
        tvLawEnforcementDistrict.setText(mCase.getJCQY());
        Date date = new Date();
        date.setTime(Long.valueOf(mCase.getCREATEUTC() + "000"));
        tvCreationTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        tvPunishmentWay.setText(mCase.getCFFS());
        tvSourceCase.setText(mCase.getAJLY());
    }

    @OnClick({R2.id.tv_select_address, R2.id.tv_law_enforcement_address, R2.id.tv_law_enforcement_district, R2.id.tv_creation_time, R2.id.btn_choose_instrument})
    public void onViewClicked(View v) {
        assert mPresenter != null;
        int i = v.getId();
        if (i == R.id.tv_select_address || i == R.id.tv_law_enforcement_address) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvLawEnforcementAddress.getText().toString(), "tvLawEnforcementAddress"), Config.LAWLOCATION);
        } else if (i == R.id.tv_law_enforcement_district) {
            mPresenter.showInspectDistrict(tvLawEnforcementDistrict);
        } else if (i == R.id.btn_choose_instrument) {
            if(getIntent().getBooleanExtra("isZFRY2",false)){
                UIUtils.toast(self,"执法人员2不能进行案件编辑",Toast.LENGTH_SHORT);
                return;
            }
            mValidator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        Case c = new Case();
        c.setVNAME(tvCityAbbreviation.getText().toString() + etCarLicense.getText().toString().toUpperCase());
        c.setBJCR(etInspected.getText().toString());
        c.setJCQY(tvLawEnforcementDistrict.getText().toString());
        c.setJCDD(tvLawEnforcementAddress.getText().toString());
        if (mAddMsg != null) {
            c.setLAT(mAddMsg.getLat() + "");
            c.setLON(mAddMsg.getLon() + "");
        } else {
            c.setLAT(mCase.getLAT());
            c.setLON(mCase.getLON());
        }
        c.setID(mCase.getID());
        mPresenter.updateCase(c,followInstruments,currentInstrument);
    }

    @Override
    public void onValidationFailed(List<ValidationError> list) {
        UIUtils.toast(self, validError.getFailedContent(self, list), Toast.LENGTH_SHORT);
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        tvLawEnforcementAddress.setText(addMsg.getAddr());
        mAddMsg = addMsg;
    }
}
