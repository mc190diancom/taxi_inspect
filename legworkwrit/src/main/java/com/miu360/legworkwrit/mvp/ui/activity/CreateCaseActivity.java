package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.LawToCase;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu30.common.util.Store2SdUtil;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.validError;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCreateCaseComponent;
import com.miu360.legworkwrit.di.module.CreateCaseModule;
import com.miu360.legworkwrit.mvp.contract.CreateCaseContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.presenter.CreateCasePresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

//案件创建
public class CreateCaseActivity extends BaseMvpActivity<CreateCasePresenter> implements CreateCaseContract.View, Validator.ValidationListener {
    @BindView(R2.id.tv_industry_type)
    @NotEmpty
    TextView tvIndustryType;
    @NotEmpty(message = "请输入车牌号")
    @Length(min = 6, message = "车牌号长度不正确")
    @BindView(R2.id.et_car_license)
    EditText etCarLicense;
    @BindView(R2.id.ll_car_num)
    LinearLayout llCarNum;
    @BindView(R2.id.et_inspected)
    @NotEmpty
    EditText etInspected;
    @BindView(R2.id.tv_law_enforcement_personne1)
    TextView tvLawEnforcementPersonne1;
    @BindView(R2.id.tv_law_enforcement_personne2)
    @NotEmpty
    TextView tvLawEnforcementPersonne2;
    @BindView(R2.id.tv_law_enforcement_address)
    @NotEmpty
    TextView tvLawEnforcementAddress;
    @BindView(R2.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R2.id.tv_law_enforcement_district)
    @NotEmpty
    TextView tvLawEnforcementDistrict;
    @BindView(R2.id.tv_creation_time)
    @NotEmpty(message = "请确认创建时间")
    TextView tvCreationTime;
    @BindView(R2.id.tv_punishment_way)
    @NotEmpty
    TextView tvPunishmentWay;
    @BindView(R2.id.tv_source_case)
    TextView tvSourceCase;
    @BindView(R2.id.btn_choose_instrument)
    Button btnChooseInstrument;
    @BindView(R2.id.tv_city_abbreviation)
    TextView tvCityAbbreviation;

    @Inject
    HeaderHolder header;
    @Inject
    Validator mValidator;

    private queryZFRYByDWMC currentZFRY;//当前选择的执法人员
    private AddMsg mAddMsg;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCreateCaseComponent
                .builder()
                .appComponent(appComponent)
                .createCaseModule(new CreateCaseModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_case;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "案件创建");
        //注册验证
        mValidator.setValidationListener(this);
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 7));
        etInspected.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));

        Case c = getIntent().getParcelableExtra("case");//从外勤文书转过来的
        if (c != null) {
            initViewContent(c);
        }
        startTimer();//临时存储定时器
        assert mPresenter != null;
        mPresenter.init(self, tvLawEnforcementPersonne1, tvCreationTime, tvSourceCase);
    }

    /*
     * 从执法稽查转过来的数据
     */
    private void initViewContent(Case c) {
        tvIndustryType.setText(c.getHYLB());
        if (!TextUtils.isEmpty(c.getVNAME()) && c.getVNAME().length() > 1) {
            try {
                tvCityAbbreviation.setText(c.getVNAME().substring(0, 1));
                etCarLicense.setText(c.getVNAME().substring(1, c.getVNAME().length()));
            } catch (Exception e) {
                Timber.d(e.toString());
            }
        }
        etInspected.setText(c.getBJCR());
        assert mPresenter != null;
        mPresenter.setLawEnforcementDistrict(tvLawEnforcementDistrict, c.getJCQY());

        tvLawEnforcementAddress.setText(c.getJCDD());
        tvPunishmentWay.setText(c.getCFFS());

        if (!TextUtils.isEmpty(c.getZHZH2())) {//如果不为空一般是从执法稽查跳转过来，否则是从历史稽查记录跳转，这时需要查询出的数据当中进行匹配
            currentZFRY = new queryZFRYByDWMC();
            currentZFRY.setNAME(c.getZFRYNAME2());
            currentZFRY.setZFZH(c.getZHZH2());
            tvLawEnforcementPersonne2.setText(c.getZFRYNAME2());
        } else {
            mPresenter.getZfry2(true, c.getZFRYNAME2());
        }
        if (!TextUtils.isEmpty(c.getJCDD()) && !TextUtils.isEmpty(c.getLAT())) {
            mAddMsg = new AddMsg();
            mAddMsg.setAddr(c.getJCDD());
            mAddMsg.setLat(Long.valueOf(c.getLAT()));
            mAddMsg.setLon(Long.valueOf(c.getLON()));
        }
        if(!TextUtils.isEmpty(c.getCREATEUTC())){
            tvCreationTime.setHint(TimeTool.formatyyyyMMdd_HHmm(Long.valueOf(c.getCREATEUTC()) * 1000));
        }
    }

    @OnClick({R2.id.tv_law_enforcement_address, R2.id.tv_select_address, R2.id.btn_choose_instrument, R2.id.tv_law_enforcement_district,
            R2.id.tv_industry_type, R2.id.tv_creation_time, R2.id.tv_law_enforcement_personne2, R2.id.tv_punishment_way, R2.id.tv_city_abbreviation})
    public void onViewClicked(View v) {
        assert mPresenter != null;
        int i = v.getId();
        if (i == R.id.tv_law_enforcement_address || i == R.id.tv_select_address) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvLawEnforcementAddress.getText().toString(), "tvLawEnforcementAddress"), Config.LAWLOCATION);
        } else if (i == R.id.btn_choose_instrument) {
            mValidator.validate();
        } else if (i == R.id.tv_law_enforcement_district) {
            mPresenter.showInspectDistrict(tvLawEnforcementDistrict);
        } else if (i == R.id.tv_industry_type) {
            mPresenter.showWindowForHy(tvIndustryType);
        } else if (i == R.id.tv_creation_time) {
            mPresenter.showDate(TextUtils.isEmpty(tvCreationTime.getText()) ? tvCreationTime.getHint().toString() : tvCreationTime.getText().toString());
        } else if (i == R.id.tv_law_enforcement_personne2) {
            mPresenter.showWindowForZfry(tvLawEnforcementPersonne1.getText().toString(), tvLawEnforcementPersonne2);
        } else if (i == R.id.tv_punishment_way) {
            mPresenter.showPunishType(self);
        } else if (i == R.id.tv_city_abbreviation) {
            mPresenter.showWindowForCity(tvCityAbbreviation);
        }
    }

    /*
     * 创建时间
     */
    @Override
    public void showTime(String time) {
        tvCreationTime.setText(time);
    }

    /*
     * 处罚方式
     */
    @Override
    public void showPunishType(String type) {
        tvPunishmentWay.setText(type);
    }

    /*
     * 当前选择的执法人员2
     */
    @Override
    public void onReturnCurrentZFRY(queryZFRYByDWMC zfry) {
        currentZFRY = zfry;
    }

    /*
     * 输入框填写内容通过验证
     */
    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        if (!mPresenter.verifyCreateTime(tvCreationTime.getText().toString())) {
            return;
        }
        Case c = getaCase();
        mPresenter.createCase(c);
    }

    @NonNull
    private Case getaCase() {
        Case c = new Case();
        assert mPresenter != null;
        c.setZFZH1(mPresenter.user.getString("user_name", ""));
        c.setHYLB(tvIndustryType.getText().toString());
        c.setVNAME(tvCityAbbreviation.getText().toString() + etCarLicense.getText().toString().toUpperCase());
        c.setBJCR(etInspected.getText().toString());
        c.setZFRYNAME1(tvLawEnforcementPersonne1.getText().toString());
        if (currentZFRY != null) {
            c.setZHZH2(currentZFRY.getZFZH());
            c.setZFRYNAME2(currentZFRY.getNAME());
        }
        c.setJCQY(tvLawEnforcementDistrict.getText().toString());
        c.setJCDD(tvLawEnforcementAddress.getText().toString());
        c.setCFFS(tvPunishmentWay.getText().toString());
        c.setAJLY(tvSourceCase.getText().toString());
        if (!TextUtils.isEmpty(tvCreationTime.getText())) {
            c.setCREATEUTC((TimeTool.getYYHHmm(tvCreationTime.getText().toString()).getTime() / 1000) + "");
        }
        if (mAddMsg != null) {
            c.setLAT(mAddMsg.getLat() + "");
            c.setLON(mAddMsg.getLon() + "");
        }
        return c;
    }

    /*
     * 输入框填写内容未通过验证
     */
    @Override
    public void onValidationFailed(List<ValidationError> list) {
        UIUtils.toast(self, validError.getFailedContent(self, list), Toast.LENGTH_SHORT);
    }

    /*
     * 提交成功
     */
    @Override
    public void onCreateSuccess(Case c) {
        setResult(RESULT_OK);
        CacheManager.getInstance().putCase(c);
        CacheManager.getInstance().clearUTC();
        CacheManager.getInstance().resetPrintTimes();

        if(getIntent().getParcelableExtra("lawToLive") != null){
            CacheManager.getInstance().putLawToLive((LawToCase) getIntent().getParcelableExtra("lawToLive"));
        }

        ArrayList<InquiryRecordPhoto> infos = getIntent().getParcelableArrayListExtra("infos");

        if ("警告".equals(c.getCFFS())) {
            ActivityUtils.startActivity(SimpleProcessActivity.getCaseIntent(self, c, infos));
            finish();
        } else {
            ActivityUtils.startActivity(NormalProcessActivity.getCaseIntent(self, c, infos));
            finish();
        }
    }

    @Override
    public void uploadPhotosSuccess() {

    }

    //历史稽查跳转过来，把查询的zfzh2进行匹配
    @Override
    public void showZfry2(queryZFRYByDWMC zfry) {
        tvLawEnforcementPersonne2.setText(zfry.getNAME());
        currentZFRY = zfry;
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        tvLawEnforcementAddress.setText(addMsg.getAddr());
        mAddMsg = addMsg;
    }

    Timer timer;

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!(TextUtils.isEmpty(etCarLicense.getText().toString()))
                        || !(TextUtils.isEmpty(etInspected.getText().toString()))
                        || currentZFRY != null
                        || !(TextUtils.isEmpty(tvLawEnforcementDistrict.getText().toString()))
                        || !(TextUtils.isEmpty(tvLawEnforcementAddress.getText().toString()))
                        || !(TextUtils.isEmpty(tvPunishmentWay.getText().toString()))) {
                    Store2SdUtil.getInstance(self).storeOut(getaCase(), Config.CASE_TEMP);
                }
            }
        }, 0, 3000);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        File file = new File(Config.PATH + Config.CASE_TEMP);
        if (file.exists()) {
            file.delete();
        }
        super.onDestroy();
    }
}
