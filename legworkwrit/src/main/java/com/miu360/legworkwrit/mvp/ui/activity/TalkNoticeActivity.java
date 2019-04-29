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

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerTalkNoticeComponent;
import com.miu360.legworkwrit.di.module.TalkNoticeModule;
import com.miu360.legworkwrit.mvp.contract.TalkNoticeContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.JCItemWrapper;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.presenter.TalkNoticePresenter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 谈话通知书
 */
public class TalkNoticeActivity extends BaseInstrumentActivity<TalkNoticePresenter> implements TalkNoticeContract.View {
    //当事人
    @BindView(R2.id.et_litigant)
    @NotEmpty
    EditText etLitigant;
    //机关地址
    @BindView(R2.id.tv_agency_address)
    @NotEmpty
    TextView tvAgencyAddress;
    //机关电话
    @BindView(R2.id.tv_agency_phone)
    @NotEmpty
    TextView tvAgencyPhone;
    //车牌号
    @BindView(R2.id.et_car_license)
    @NotEmpty
    EditText etCarLicense;
    @BindView(R2.id.tv_illegal_reason)
    @NotEmpty
    TextView tvIllegalReason;
    @BindView(R2.id.rbtn_sure)
    RadioButton rbtnSure;
    @BindView(R2.id.rbtn_no)
    RadioButton rbtnNo;
    @NotEmpty(message = "请选择送达时间")
    @BindView(R2.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R2.id.ll_send_time)
    LinearLayout llSendTime;

    private JCItem mJcItem;
    private long sTime;//获取默认的开始时间

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTalkNoticeComponent
                .builder()
                .appComponent(appComponent)
                .talkNoticeModule(new TalkNoticeModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_talk_notice;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_TALKNOTICE;
        header.init(self, "谈话通知书");
        super.initData(savedInstanceState);
        sTime = GetUTCUtil.getLiveCheckRecordEndTimeL(mCase);
        etLitigant.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        assert mPresenter != null;
        mPresenter.init(this, etCarLicense, etLitigant, mCase,tvSendTime, sTime);
    }

    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        TalkNoticeQ talkNoticeQ = new TalkNoticeQ();
        talkNoticeQ.setDSR(etLitigant.getText().toString());
        talkNoticeQ.setVNAME(etCarLicense.getText().toString());
        talkNoticeQ.setJGDZ(tvAgencyAddress.getText().toString());
        talkNoticeQ.setJGDH(tvAgencyPhone.getText().toString());
        if (mJcItem != null) {
            talkNoticeQ.setSXWFSYBC(mJcItem.getLBMC());
        } else {
            talkNoticeQ.setSXWFSYBC(tvIllegalReason.getText().toString());
        }
        talkNoticeQ.setZID(mCase.getID());
        talkNoticeQ.setID(instrumentID);
        if(rbtnSure.isChecked()){
            talkNoticeQ.setSTATUS("1");
            long endT = TimeTool.getYYHHmm(tvSendTime.getText().toString()).getTime();
            talkNoticeQ.setSDSJ(( endT / 1000) + "");
            talkNoticeQ.setXZJGSJ(Long.valueOf(GetUTCUtil.setEndTime(sTime*1000,( endT / 1000) + "",Config.UTC_TALKNOTICE)) +"");
            talkNoticeQ.setQSSJ(Long.valueOf(GetUTCUtil.setEndTime(sTime*1000,( endT / 1000) + "",Config.UTC_TALKNOTICE)) +"");
        }else{
            talkNoticeQ.setSTATUS("0");
            talkNoticeQ.setXZJGSJ(String.valueOf(sTime + Config.UTC_TALKNOTICE));
            talkNoticeQ.setQSSJ(String.valueOf(sTime + Config.UTC_TALKNOTICE));
        }
        talkNoticeQ.setZFZH1(mCase.getZFZH1());
        talkNoticeQ.setZFZH2(mCase.getZHZH2());
        talkNoticeQ.setZFRY1(mCase.getZFRYNAME1());
        talkNoticeQ.setZFRY2(mCase.getZFRYNAME2());
        talkNoticeQ.setZH(mCase.getZH());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, talkNoticeQ, false), 0x0111);
        } else if (!isUpdate) {
            mPresenter.addTalkNotice(talkNoticeQ);
        } else {
            //调更新接口
            mPresenter.updateTalkNotice(talkNoticeQ, checkChange(), followInstruments);
        }

    }

    @OnClick({R2.id.tv_illegal_reason, R2.id.tv_agency_address, R2.id.tv_agency_phone,R2.id.tv_send_time, R2.id.rbtn_sure,
            R2.id.rbtn_no})
    public void onViewClicked(View v) {
        assert mPresenter != null;
        int i = v.getId();
        if (i == R.id.tv_illegal_reason) {
            ActivityUtils.startActivity(IllegalDetailActivityActivity.class);
        } else if (i == R.id.tv_agency_address) {
            mPresenter.showAgencyAddress(self, tvAgencyAddress, tvAgencyPhone);
        } else if (i == R.id.tv_agency_phone) {
            mPresenter.showAgencyPhone(self, tvAgencyAddress, tvAgencyPhone);
        } else if(i == R.id.tv_send_time){
            mPresenter.showSendTime(tvSendTime);
        } else if (i == R.id.rbtn_sure) {
            llSendTime.setVisibility(View.VISIBLE);
        } else if (i == R.id.rbtn_no) {
            llSendTime.setVisibility(View.GONE);

        }
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.ILLEGAL)
    public void showIllegal(JCItemWrapper wrapper) {
        mJcItem = wrapper.getIllegalBehavior();
        tvIllegalReason.setText(mJcItem.getLBMC());
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        if (CacheManager.getInstance().getLawToLive() != null) {
            tvIllegalReason.setText(CacheManager.getInstance().getLawToLive().getWfxw());
        }
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            tvIllegalReason.setText(CacheManager.getInstance().getLiveCheckRecord().getWFXW());
        }
        List<AgencyInfo> agencyInfos = CacheManager.getInstance().getAgencyInfoByZFZH();
        if (agencyInfos != null && agencyInfos.size() > 0) {
            tvAgencyAddress.setText(agencyInfos.get(0).getDZ());
            tvAgencyPhone.setText(agencyInfos.get(0).getDH());
        }
    }

    /*
     * 把填写到服务器的信息回填到视图上
     */
    @Override
    public void showViewContent(ParentQ parentQ) {
        TalkNoticeQ info = (TalkNoticeQ) parentQ;
        this.instrumentID = info.getID();
        etLitigant.setText(info.getDSR());
        etCarLicense.setText(info.getVNAME());
        tvAgencyAddress.setText(info.getJGDZ());
        tvAgencyPhone.setText(info.getJGDH());
        if ("0".equals(info.getSTATUS())) {
            rbtnNo.setChecked(true);
        } else {
            rbtnSure.setChecked(true);
        }
        setEffctNr(info);
        setViewTime(info);
    }

    //控件设置时间
    private void setViewTime(TalkNoticeQ info) {
        long duration = 0;
        if (isConfirmed()) {//如果是待确认的状态
            duration = TimeTool.parseDate2(info.getSDSJ()).getTime() - sTime*1000;
        }

        if (!"0".equals(info.getSTATUS())) {
            rbtnSure.setChecked(true);
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
            rbtnNo.setChecked(true);
            setEndTime();
        }
    }

    /**
     * 设置界面上所谓的结束时间；因为已填写的时根据第一次填写的时间段，整段往后移
     */
    private void setEndTime() {
        tvSendTime.setText("");
        tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(sTime*1000)));
    }

    /*
     * 当待确认状态时设置违法情形
     */
    private void setEffctNr(TalkNoticeQ info) {
        mJcItem = new JCItem();
        if (isConfirmed() && CacheManager.getInstance().getLiveCheckRecord() != null) {
            tvIllegalReason.setText(CacheManager.getInstance().getLiveCheckRecord().getWFXW());
            mJcItem.setLBMC(CacheManager.getInstance().getLiveCheckRecord().getWFXW());
        } else {
            tvIllegalReason.setText(info.getSXWFSYBC());
            mJcItem.setLBMC(info.getSXWFSYBC());
        }
    }

    @Override
    public void showCaseContent() {
        etCarLicense.setText(mCase.getVNAME());
        etLitigant.setText(mCase.getBJCR());
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (date.getTime() < sTime * 1000) {
            DialogUtil.showTipDialog(self, "送达时间必须大于现场检查笔录时间和案件创建时间");
            return;
        }
        tvSendTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
    }

    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_TALKNOTICE.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_TALKNOTICE;
    }

    @Override
    public void getID(String id) {
        if (TextUtils.isEmpty(id) || "null".equals(id)) return;
        this.instrumentID = id;
    }

}
