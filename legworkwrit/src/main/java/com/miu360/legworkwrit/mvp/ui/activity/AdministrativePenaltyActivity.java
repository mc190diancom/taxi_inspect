package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.LawToCase;
import com.miu30.common.util.FullListView;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerAdministrativePenaltyComponent;
import com.miu360.legworkwrit.di.module.AdministrativePenaltyModule;
import com.miu360.legworkwrit.mvp.contract.AdministrativePenaltyContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.presenter.AdministrativePenaltyPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.BasicInfoAdapter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class AdministrativePenaltyActivity extends BaseInstrumentActivity<AdministrativePenaltyPresenter> implements AdministrativePenaltyContract.View, TextWatcher, AdapterView.OnItemClickListener {

    @NotEmpty
    @BindView(R2.id.et_litigant)
    EditText etLitigant;
    @BindView(R2.id.tv_choose_write)
    TextView tvChooseWrite;
    @NotEmpty
    @BindView(R2.id.et_supervision_card)
    EditText etSupervisionCard;
    @BindView(R2.id.rbtn_man)
    RadioButton rbtnMan;
    @BindView(R2.id.rbtn_woman)
    RadioButton rbtnWoman;
    @NotEmpty
    @BindView(R2.id.tv_inspect_address)
    TextView tvInspectAddress;
    @BindView(R2.id.tv_select_inspect_address)
    TextView tvSelectInspectAddress;
    @NotEmpty(message = "请确认处罚时间")
    @BindView(R2.id.tv_punish_time)
    TextView tvPunishTime;
    @NotEmpty
    @BindView(R2.id.tv_punish_address)
    TextView tvPunishAddress;
    @BindView(R2.id.tv_select_punish_address)
    TextView tvSelectPunishAddress;
    @BindView(R2.id.et_car_number)
    EditText etCarNumber;
    @NotEmpty
    @BindView(R2.id.tv_Illegal_content)
    TextView tvIllegalContent;
    @NotEmpty
    @BindView(R2.id.tv_Illegal_rules)
    TextView tvIllegalRules;
    @BindView(R2.id.rbtn_sure)
    RadioButton rbtnSure;
    @BindView(R2.id.rbtn_no)
    RadioButton rbtnNo;
    @NotEmpty(message = "请确认文书送达时间")
    @BindView(R2.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R2.id.ll_send_time)
    LinearLayout llSendTime;
    @BindView(R2.id.list_show_driverName)
    FullListView listShowDriverName;

    @Inject
    BasicInfoAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAdministrativePenaltyComponent
                .builder()
                .appComponent(appComponent)
                .administrativePenaltyModule(new AdministrativePenaltyModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_administrative_penalty;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "行政处罚决定书");
        instrumentType = Config.T_ADMINISTRATIVE;
        super.initData(savedInstanceState);
        etLitigant.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarNumber.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        assert mPresenter != null;
        mPresenter.init(this, tvPunishTime, tvSendTime, tvChooseWrite, etCarNumber, etLitigant, tvPunishAddress, mCase);
        cacheLawToLive();
    }

    /**
     * 把缓存中的执法稽查信息填写到文书中
     */
    private void cacheLawToLive() {
        if (CacheManager.getInstance().getJDKH() != null) {
            etSupervisionCard.setText(CacheManager.getInstance().getJDKH());
        }
        if(CacheManager.getInstance().getDriverInfo() != null && mCase.getBJCR().equals(CacheManager.getInstance().getDriverInfo().getDriverName())){
            setDriverInfo(CacheManager.getInstance().getDriverInfo());
        }
    }

    private void setDriverInfo(DriverInfo info) {
        etLitigant.setText(info.getDriverName());
        if("0".equals(info.getSex())){
            rbtnWoman.setChecked(true);
        }else{
            rbtnMan.setChecked(true);
        }
        etSupervisionCard.setText(info.getJianduNumber());
        tvInspectAddress.setText(info.getAddress());
        assert mPresenter != null;
    }

    /**
     * 监听在填写之后，不然填写缓存或者服务器上的内容会触发监听
     */
    @Override
    public void addListener() {
        etSupervisionCard.addTextChangedListener(this);
        listShowDriverName.setAdapter(mAdapter);
        listShowDriverName.setOnItemClickListener(this);
    }

    @OnClick({R2.id.tv_choose_write, R2.id.tv_inspect_address, R2.id.tv_select_inspect_address, R2.id.tv_punish_time, R2.id.tv_punish_address, R2.id.rbtn_sure,
            R2.id.rbtn_no, R2.id.tv_select_punish_address, R2.id.tv_Illegal_content, R2.id.tv_Illegal_rules, R2.id.tv_send_time})
    public void onViewClicked(View v) {
        if (mPresenter == null) return;
        int i = v.getId();
        if (i == R.id.tv_choose_write) {
            mPresenter.showSelectCardWindow(tvChooseWrite, etSupervisionCard);
        } else if (i == R.id.tv_inspect_address || i == R.id.tv_select_inspect_address) {
            ActivityUtils.startActivity(LawSelectLocationActivity.getLocationIntent(self, tvInspectAddress.getText().toString(), "tvInspectAddress"));
        } else if (i == R.id.tv_punish_time) {
            mPresenter.showPunishTime(tvPunishTime, isUpdate);
        } else if (i == R.id.tv_punish_address || i == R.id.tv_select_punish_address) {
            ActivityUtils.startActivity(LawSelectLocationActivity.getLocationIntent(self, tvPunishAddress.getText().toString(), "tvPunishAddress"));
        } else if (i == R.id.tv_Illegal_content) {
            mPresenter.showIllegalDetailWindow(tvIllegalContent, tvIllegalRules);
        } else if (i == R.id.tv_Illegal_rules) {
            mPresenter.getIllegalSituationWindow(tvIllegalContent);
        } else if (i == R.id.tv_send_time) {
            mPresenter.showSendTime(tvPunishTime, tvSendTime, isUpdate);
        } else if (i == R.id.rbtn_sure) {
            llSendTime.setVisibility(View.VISIBLE);
        } else if (i == R.id.rbtn_no) {
            llSendTime.setVisibility(View.GONE);

        }
    }

    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        AdministrativePenalty ap = new AdministrativePenalty();
        ap.setDSR(etLitigant.getText().toString());
        ap.setXB(rbtnMan.isChecked() ? "男" : "女");
        if ("服务监督卡号".equals(tvChooseWrite.getText().toString())) {
            ap.setFWJDKH(etSupervisionCard.getText().toString());
            ap.setWLYYCZJSYZH("");
        } else {
            ap.setWLYYCZJSYZH(etSupervisionCard.getText().toString());
        }

        ap.setDZ(tvInspectAddress.getText().toString());
        ap.setBCDD(tvPunishAddress.getText().toString());
        ap.setBCVNAME(etCarNumber.getText().toString());
        ap.setWFXW(tvIllegalContent.getText().toString());
        ap.setWFTK(tvIllegalRules.getText().toString());
        ap.setZID(mCase.getID());
        String endTime;
        ap.setBCSJ((TimeTool.getYYHHmm(tvPunishTime.getText().toString()).getTime() / 1000) + "");
        if (rbtnSure.isChecked()) {
            ap.setSDSJ((TimeTool.getYYHHmm(tvSendTime.getText().toString()).getTime() / 1000) + "");//作为结束时间前1分钟
            endTime = ap.getSDSJ();
        } else {
            ap.setSDSJ("");
            endTime = ap.getBCSJ();
        }
        ap.setXZJGSJ(GetUTCUtil.setEndTime(tvPunishTime.getHint().toString(), endTime, Config.UTC_ADMINISTRATIVE));//结束时间比送达时间晚1分钟
        ap.setQSSJ(GetUTCUtil.setEndTime(tvPunishTime.getHint().toString(), endTime, Config.UTC_ADMINISTRATIVE));//结束时间比送达时间晚1分钟
        ap.setQSRQZ("");
        ap.setWFXWID(mPresenter.getXWID());
        ap.setID(instrumentID);
        ap.setZFZH1(mCase.getZFZH1());
        ap.setZFZH2(mCase.getZHZH2());
        ap.setZH(mCase.getZH());
        ap.setZFRY1(mCase.getZFRYNAME1());
        ap.setZFRY2(mCase.getZFRYNAME2());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, ap, false), 0x0010);
        } else if (!isUpdate) {
            mPresenter.submitAdministrativeData(ap);
        } else {
            mPresenter.UpdateData(ap, checkChange(), followInstruments);
        }
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvInspectAddress".equals(addMsg.getType())) {
            tvInspectAddress.setText(addMsg.getAddr());
        } else if ("tvPunishAddress".equals(addMsg.getType())) {
            tvPunishAddress.setText(addMsg.getAddr());
        }
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            tvInspectAddress.setText(CacheManager.getInstance().getLiveCheckRecord().getDD());
            if ("男".equals(CacheManager.getInstance().getLiveCheckRecord().getSEX())) {
                rbtnMan.setChecked(true);
                rbtnWoman.setChecked(false);
            } else {
                rbtnMan.setChecked(false);
                rbtnWoman.setChecked(true);
            }
        }
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (isStart) {
            assert mPresenter != null;
            if (date.getTime() < mPresenter.lastEndTime()) {
                DialogUtil.showTipDialog(self, "处罚时间必须大于现场检查笔录时间和案件创建时间");
                return;
            }
            tvPunishTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            setEndTime();
        } else {
            if (!TextUtils.isEmpty(tvPunishTime.getText()) && !(TimeTool.getYYHHmm(tvPunishTime.getText().toString()).getTime() < date.getTime())) {
                DialogUtil.showTipDialog(self, "文书送达时间需大于处罚时间");
                return;
            }
            tvSendTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    public void showViewContent(ParentQ parentQ) {
        AdministrativePenalty info = (AdministrativePenalty) parentQ;
        assert mPresenter != null;
        this.instrumentID = info.getID();
        setViewTime(info);
        etLitigant.setText(info.getDSR());
        mPresenter.setXWID(info.getWFXWID());
        if (!TextUtils.isEmpty(info.getFWJDKH())) {
            etSupervisionCard.setText(info.getFWJDKH());
            tvChooseWrite.setText("服务监督卡号");
        } else {
            etSupervisionCard.setText(info.getWLYYCZJSYZH());
            tvChooseWrite.setText("网络预约出租汽车驾驶员证号");
        }
        tvInspectAddress.setText(info.getDZ());
        tvPunishAddress.setText(info.getBCDD());
        etCarNumber.setText(info.getBCVNAME());
        tvIllegalContent.setText(info.getWFXW());
        tvIllegalRules.setText(info.getWFTK());
        setEffctNr(info);
    }

    private void setEffctNr(AdministrativePenalty info) {
        if (isConfirmed() && CacheManager.getInstance().getLiveCheckRecord() != null) {//待确认状态时，性别受现场检查笔录影响
            if ("男".equals(CacheManager.getInstance().getLiveCheckRecord().getSEX())) {
                rbtnMan.setChecked(true);
                rbtnWoman.setChecked(false);
            } else {
                rbtnMan.setChecked(false);
                rbtnWoman.setChecked(true);
            }
        } else {
            if ("女".equals(info.getXB())) {
                rbtnWoman.setChecked(true);
            } else {
                rbtnMan.setChecked(true);
            }
        }
    }

    long Duration = (Config.UTC_ADMINISTRATIVE - 60) * 1000;//开始时间和结束时间的时间差

    //控件设置时间
    private void setViewTime(AdministrativePenalty info) {
        long duration = 0;
        if (isConfirmed()) {//如果是待确认的状态
            duration = TimeTool.parseDate2(info.getBCSJ()).getTime() - TimeTool.parseDate(tvPunishTime.getHint().toString()).getTime();
            if (duration > 0) {
                tvPunishTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getBCSJ())));
            }
        } else {
            tvPunishTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getBCSJ())));
            tvPunishTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getBCSJ())));
        }
        if (!TextUtils.isEmpty(info.getSDSJ())) {
            rbtnSure.setChecked(true);
            rbtnNo.setChecked(false);
            Duration = TimeTool.parseDate2(info.getSDSJ()).getTime() - TimeTool.parseDate2(info.getBCSJ()).getTime();
            if (isConfirmed()) {//如果是待确认的状态
                if (duration > 0) {
                    tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSDSJ())));
                } else {
                    setEndTime();
                }
            } else {
                tvSendTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getSDSJ())));
            }
        } else {
            llSendTime.setVisibility(View.GONE);
            rbtnSure.setChecked(false);
            rbtnNo.setChecked(true);
            setEndTime();
        }
    }

    /**
     * 设置界面上所谓的结束时间；因为已填写的时根据第一次填写的时间段，整段往后移
     */
    private void setEndTime() {
        tvSendTime.setText("");
        tvSendTime.setHint(TimeTool.formatEndTime(tvPunishTime.getText().toString(), tvPunishTime.getHint().toString(), Duration));
    }

    @Override
    public void showCaseContent() {
        etCarNumber.setText(mCase.getVNAME());
        etLitigant.setText(mCase.getBJCR());
        tvPunishAddress.setText(mCase.getJCDD());
    }

    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_ADMINISTRATIVE.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_ADMINISTRATIVE;
    }

    /*
     * 当输入服务监督卡号或者身份证号后，进行模糊搜索；
     */
    @Override
    public void getCarOrDriverInfo() {
        assert mPresenter != null;
        mPresenter.getDriverInfoByCard(tvChooseWrite.getText().toString(), etSupervisionCard.getText().toString());
    }

    @Override
    public void notifyAdapter() {
        listShowDriverName.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getID(String id) {
        if (TextUtils.isEmpty(id) || "null".equals(id)) return;
        this.instrumentID = id;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DriverInfo info = (DriverInfo) parent.getItemAtPosition(position);
        StopChange = true;
        etSupervisionCard.setText(info.getJianduNumber());
        etLitigant.setText(info.getDriverName());
        if ("0".equals(info.getSex())) {
            rbtnWoman.setChecked(true);
        } else {
            rbtnMan.setChecked(true);
        }
        listShowDriverName.setVisibility(View.GONE);
        tvInspectAddress.setText(info.getAddress());
    }
}
