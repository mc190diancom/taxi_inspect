package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerLiveTranscriptComponent;
import com.miu360.legworkwrit.di.module.LiveTranscriptModule;
import com.miu360.legworkwrit.mvp.contract.LiveTranscriptContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.presenter.LiveTranscriptPresenter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 现场笔录
 */
public class LiveTranscriptActivity extends BaseInstrumentActivity<LiveTranscriptPresenter> implements LiveTranscriptContract.View {
    @NotEmpty(message = "请选择开始时间")
    @BindView(R2.id.tv_start_time)
    TextView tvStartTime;
    @NotEmpty(message = "请选择结束时间")
    @BindView(R2.id.tv_end_time)
    TextView tvEndTime;
    @NotEmpty
    @BindView(R2.id.tv_implement_address)
    TextView tvImplementAddress;
    @BindView(R2.id.tv_select_implement_address)
    TextView tvSelectImplementAddress;
    @BindView(R2.id.et_inspect)
    EditText etInspect;
    @BindView(R2.id.tv_inspect_address)
    TextView tvInspectAddress;
    @BindView(R2.id.tv_select_inspect_address)
    TextView tvSelectInspectAddress;
    @BindView(R2.id.rbtn_yes)
    RadioButton rbtnYes;
    @BindView(R2.id.rbtn_no)
    RadioButton rbtnNo;
    @BindView(R2.id.et_researchers_present)
    EditText etResearchersPresent;
    @BindView(R2.id.rbtn_man)
    RadioButton rbtnMan;
    @BindView(R2.id.rbtn_woman)
    RadioButton rbtnWoman;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_card)
    EditText etCard;
    @BindView(R2.id.tv_show_address)
    TextView tvShowAddress;
    @BindView(R2.id.tv_select_show_address)
    TextView tvSelectShowAddress;
    @BindView(R2.id.ll_witness)
    LinearLayout llWitness;
    @BindView(R2.id.tv_step)
    TextView tvStep;
    @NotEmpty
    @BindView(R2.id.et_content)
    EditText etContent;
    @BindView(R2.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R2.id.tv_select_documents)
    TextView tvSelectDocuments;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLiveTranscriptComponent
                .builder()
                .appComponent(appComponent)
                .liveTranscriptModule(new LiveTranscriptModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_live_transcript;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_LIVETRANSCRIPT;
        header.init(self, "现场笔录");
        super.initData(savedInstanceState);
        etInspect.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etResearchersPresent.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        assert mPresenter != null;
        mPresenter.init(this, tvStartTime, tvEndTime, etInspect, tvImplementAddress, mCase);
    }

    @OnCheckedChanged(R2.id.rbtn_yes)
    public void hasWitness(RadioButton button) {
        if (button.isChecked()) {
            llWitness.setVisibility(View.VISIBLE);
        } else {
            llWitness.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.ibtn_timeline)
    public void showTimeLineDialog() {
        DialogUtil.showTimeLineDialog(self, Config.T_LIVETRANSCRIPT, new DialogUtil.OnNoCaseListener() {
            @Override
            public void noCase() {
                UIUtils.toast(self, "未创建案件", Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick({R2.id.tv_start_time, R2.id.tv_end_time, R2.id.tv_implement_address, R2.id.tv_select_implement_address, R2.id.tv_inspect_address,
            R2.id.tv_select_inspect_address,R2.id.tv_select_documents, R2.id.tv_show_address, R2.id.tv_select_show_address, R2.id.tv_step})
    public void onViewClicked(View v) {
        assert mPresenter != null;
        int i = v.getId();
        if (i == R.id.tv_start_time) {
            mPresenter.startCalendar(TextUtils.isEmpty(tvStartTime.getText()) ? tvStartTime.getHint().toString() : tvStartTime.getText().toString(), isUpdate);

        } else if (i == R.id.tv_end_time) {
            mPresenter.endCalendar(tvStartTime, TextUtils.isEmpty(tvEndTime.getText()) ? tvEndTime.getHint().toString() : tvEndTime.getText().toString(), isUpdate);

        } else if (i == R.id.tv_implement_address || i == R.id.tv_select_implement_address) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvImplementAddress.getText().toString(), "tvImplementAddress"), Config.LAWLOCATION);

        } else if (i == R.id.tv_inspect_address || i == R.id.tv_select_inspect_address) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvInspectAddress.getText().toString(), "tvLTInspectAddress"), Config.LAWLOCATION);

        } else if (i == R.id.tv_show_address || i == R.id.tv_select_show_address) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvShowAddress.getText().toString(), "tvShowAddress"), Config.LAWLOCATION);

        } else if (i == R.id.tv_step) {
            mPresenter.showCompelWindow(tvStep, etContent);

        } else if (i == R.id.tv_select_documents){
            mPresenter.selectDocument(tvSelectDocuments);
        }
    }

    @Override
    public void onValidationSucceeded() {
        LiveTranscript lt = new LiveTranscript();
        if (rbtnYes.isChecked() && validDCRYInfo(lt)) {
            return;
        }
        assert mPresenter != null;
        lt.setSSSJ1((TimeTool.getYYHHmm(tvStartTime.getText().toString()).getTime() / 1000) + "");
        lt.setSSSJ2((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lt.setSSDD(tvImplementAddress.getText().toString());
        lt.setDSR(etInspect.getText().toString());
        lt.setDZ(tvInspectAddress.getText().toString());
        if ("其他".equals(tvStep.getText().toString())) {
            lt.setSSQZCSJL(etContent.getText().toString());
        } else {
            lt.setSSQZCSJL(tvStep.getText().toString());
        }
        if (!TextUtils.isEmpty(etContent.getText()) && TextUtils.isEmpty(etContent.getText().toString().trim())) {
            UIUtils.toast(self, "措施不能输入纯空格", Toast.LENGTH_SHORT);
            return;
        }
        lt.setZFSJ((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lt.setDSRQZSJ((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lt.setREADOUT(tvSelectDocuments.getText().toString());
        lt.setZID(mCase.getID());
        lt.setID(instrumentID);
        lt.setZFZH1(mCase.getZFZH1());
        lt.setZFZH2(mCase.getZHZH2());
        lt.setZFRY1(mCase.getZFRYNAME1());
        lt.setZFRY2(mCase.getZFRYNAME2());
        lt.setZH(mCase.getZH());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, lt, false), 0x0110);
        } else if (!isUpdate) {
            mPresenter.submitLiveData(lt, followInstruments);
        } else {
            //更新现场笔录
            mPresenter.UpdateData(lt, checkChange(), followInstruments);
        }

    }

    /*
     * 验证到场人员相关的信息
     */
    private boolean validDCRYInfo(LiveTranscript lt) {
        if (TextUtils.isEmpty(etResearchersPresent.getText())) {
            UIUtils.toast(self, etResearchersPresent.getHint().toString(), Toast.LENGTH_SHORT);
            return true;
        } else if (TextUtils.isEmpty(etCard.getText()) && etCard.getText().toString().length() != 18) {
            UIUtils.toast(self, "身份证号长度不正确", Toast.LENGTH_SHORT);
            return true;
        } else if (TextUtils.isEmpty(etPhone.getText()) && etPhone.getText().toString().length() < 6) {
            UIUtils.toast(self, "电话长度不正确", Toast.LENGTH_SHORT);
            return true;
        } else if (TextUtils.isEmpty(tvShowAddress.getText())) {
            UIUtils.toast(self, tvShowAddress.getHint().toString(), Toast.LENGTH_SHORT);
            return true;
        }
        lt.setDCRY(etResearchersPresent.getText().toString());
        lt.setXB(rbtnMan.isChecked() ? "男" : "女");
        lt.setDH(etPhone.getText().toString());
        lt.setSFZH(etCard.getText().toString());
        lt.setZZ(tvShowAddress.getText().toString());
        return false;
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvImplementAddress".equals(addMsg.getType())) {
            tvImplementAddress.setText(addMsg.getAddr());
        } else if ("tvLTInspectAddress".equals(addMsg.getType())) {
            tvInspectAddress.setText(addMsg.getAddr());
        } else if ("tvShowAddress".equals(addMsg.getType())) {
            tvShowAddress.setText(addMsg.getAddr());
        }
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        DriverInfo driverInfo = CacheManager.getInstance().getDriverInfo();
        if( driverInfo != null && mCase.getBJCR().equals(driverInfo.getDriverName())){
            tvInspectAddress.setText(driverInfo.getAddress());
        }
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            tvInspectAddress.setText(CacheManager.getInstance().getLiveCheckRecord().getDD());
        }
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (isStart) {
            assert mPresenter != null;
            if (date.getTime() < mPresenter.lastEndTime()) {
                DialogUtil.showTipDialog(self, "开始时间必须大于上一个文书时间和案件创建时间");
                return;
            }
            tvStartTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            setEndTime();
        } else {
            if (!TextUtils.isEmpty(tvStartTime.getText()) && TimeTool.getYYHHmm(tvStartTime.getText().toString()).getTime() > date.getTime() - Config.UTC_LIVETRANSCRIPT * 1000) {
                DialogUtil.showTipDialog(self, "结束时间应比开始时间大6分钟");
                return;
            }
            tvEndTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    public void showViewContent(ParentQ parentQ) {
        LiveTranscript info = (LiveTranscript) parentQ;
        this.instrumentID = info.getID();
        setViewTime(info);
        startUtc = tvStartTime.getText().toString();
        endUtc = tvEndTime.getText().toString();
        tvImplementAddress.setText(info.getSSDD());
        etInspect.setText(info.getDSR());
        tvInspectAddress.setText(info.getDZ());
        tvSelectDocuments.setText(info.getREADOUT());
        if (!TextUtils.isEmpty(info.getSSQZCSJL()) && "被检查人在场对车辆内饰和外观情况进行了核对确认，并取走车内财物。".equals(info.getSSQZCSJL())) {
            tvStep.setText(info.getSSQZCSJL());
            etContent.setVisibility(View.GONE);
        } else {
            tvStep.setText("其他");
            etContent.setText(info.getSSQZCSJL());
        }

        if (!TextUtils.isEmpty(info.getDCRY())) {
            rbtnYes.setChecked(true);
            etResearchersPresent.setText(info.getDCRY());
            if ("女".equals(info.getXB())) {
                rbtnWoman.setChecked(true);
            } else {
                rbtnMan.setChecked(true);
            }
            etPhone.setText(info.getDH());
            etCard.setText(info.getSFZH());
            tvShowAddress.setText(info.getZZ());
        } else {
            rbtnNo.setChecked(true);
            llWitness.setVisibility(View.GONE);
        }
    }

    long Duration = Config.UTC_LIVETRANSCRIPT * 1000;//开始时间和结束时间的时间差;

    //控件设置时间
    private void setViewTime(LiveTranscript info) {
        if (isConfirmed()) {//如果是待确认的状态
            if (TimeTool.parseDate2(info.getSSSJ1()).getTime() - TimeTool.parseDate(tvStartTime.getHint().toString()).getTime() > 0) {
                tvStartTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSSSJ1())));
                tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSSSJ1())));
            } else {
                Duration = TimeTool.parseDate2(info.getSSSJ2()).getTime() - TimeTool.parseDate2(info.getSSSJ1()).getTime();
                setEndTime();
            }
        } else {
            tvStartTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSSSJ1())));
            tvStartTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSSSJ1())));
            tvEndTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSSSJ2())));
        }
    }

    /**
     * 设置界面上所谓的结束时间；因为已填写的时根据第一次填写的时间段，整段往后移
     */
    private void setEndTime() {
        tvEndTime.setText("");
        tvEndTime.setHint(TimeTool.formatEndTime(tvStartTime.getText().toString(), tvStartTime.getHint().toString(), Duration));
    }

    @Override
    public void showCaseContent() {
        etInspect.setText(mCase.getBJCR());
        tvImplementAddress.setText(mCase.getJCDD());
    }

    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (getInstrumentId().equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return !startUtc.equals(tvStartTime.getText().toString()) || !endUtc.equals(tvEndTime.getText().toString()) || isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_LIVETRANSCRIPT;
    }

    @Override
    public void getID(String id) {
        if(TextUtils.isEmpty(id) || "null".equals(id)) return;
        this.instrumentID = id;
    }
}
