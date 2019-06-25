package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.LawToCase;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.FullListView;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerLiveCheckRecordComponent;
import com.miu360.legworkwrit.di.module.LiveCheckRecordModule;
import com.miu360.legworkwrit.mvp.contract.LiveCheckRecordContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.JCItemWrapper;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.presenter.LiveCheckRecordPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.BasicInfoAdapter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 现场检查笔录（路检）
 */
public class LiveCheckRecordActivity extends BaseInstrumentActivity<LiveCheckRecordPresenter> implements LiveCheckRecordContract.View, AdapterView.OnItemClickListener{
    @NotEmpty(message = "请确认开始时间")
    @BindView(R2.id.tv_start_time)
    TextView tvStartTime;
    @NotEmpty(message = "请确认结束时间")
    @BindView(R2.id.tv_end_time)
    TextView tvEndTime;
    @NotEmpty
    @BindView(R2.id.tv_inspect_district)
    TextView tvInspectDistrict;
    @NotEmpty
    @BindView(R2.id.tv_inspect_address)
    TextView tvInspectAddress;
    @BindView(R2.id.tv_select_inspect_address)
    TextView tvSelectInspectAddress;
    @NotEmpty
    @BindView(R2.id.et_inspect_name)
    EditText etInspectName;
    @BindView(R2.id.rbtn_man)
    RadioButton rbtnMan;
    @BindView(R2.id.rbtn_woman)
    RadioButton rbtnWoman;
    @NotEmpty(message = "请输入电话")
    @Length(min = 6, message = "电话长度不正确")
    @BindView(R2.id.et_number)
    EditText etNumber;
    @BindView(R2.id.tv_card)
    TextView tvCard;
    @NotEmpty
    @BindView(R2.id.et_card)
    EditText etCard;
    @NotEmpty
    @BindView(R2.id.tv_inspected_address)
    TextView tvInspectedAddress;
    @BindView(R2.id.tv_select_inspected_address)
    TextView tvSelectInspectedAddress;
    @NotEmpty
    @BindView(R2.id.et_car_license)
    EditText etCarLicense;
    @NotEmpty
    @BindView(R2.id.et_car_brand)
    EditText etCarBrand;
    @NotEmpty
    @BindView(R2.id.et_car_color)
    EditText etCarColor;
    @BindView(R2.id.tv_car_type_select)
    TextView tvCarTypeSelect;
    @BindView(R2.id.et_car_type)
    EditText etCarType;
    @NotEmpty
    @BindView(R2.id.tv_Illegal_behavior)
    TextView tvIllegalBehavior;
    @NotEmpty
    @BindView(R2.id.tv_Illegal_situation)
    TextView tvIllegalSituation;
    @BindView(R2.id.tv_step_organizations)
    TextView tvStepOrganizations;
    @BindView(R2.id.tv_step_industry)
    TextView tvStepIndustry;
    @BindView(R2.id.tv_step_type)
    TextView tvStepType;
    @BindView(R2.id.ll_step_one)
    LinearLayout llStepOne;
    @BindView(R2.id.tv_step_two)
    TextView tvStepTwo;
    @NotEmpty
    @BindView(R2.id.et_record_content)
    EditText etRecordContent;

    @BindView(R2.id.tv_start_time_des)
    TextView tvStartTimeDes;
    @BindView(R2.id.tv_end_time_des)
    TextView tvEndTimeDes;
    @BindView(R2.id.tv_compel_step)
    TextView tvCompelStep;
    @BindView(R2.id.ll_content)
    LinearLayout llContent;
    @BindView(R2.id.list_show_car_info)
    FullListView listShowCarInfo;
    @BindView(R2.id.list_show_driver_info)
    FullListView listShowDriverInfo;

    @Inject
    CarInfoAdapter mAdapter;

    @Inject
    BasicInfoAdapter mDriverAdapter;

    private JCItem mJcitem;
    private String WFXWID;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLiveCheckRecordComponent
                .builder()
                .appComponent(appComponent)
                .liveCheckRecordModule(new LiveCheckRecordModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_live_check_record;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_LIVERECORD;
        header.init(self, "现场检查笔录(路检路查)");
        super.initData(savedInstanceState);
        etInspectName.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarColor.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.chineseRegex, 6));
        etCarBrand.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 6));
        etCarType.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 6));
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        etCard.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.idCardRegex, 18));
        etCard.setRawInputType(Configuration.KEYBOARD_QWERTY);
        assert mPresenter != null;
        mPresenter.init(this, llContent, tvStartTime, tvEndTime, etCarLicense, tvInspectDistrict, tvInspectAddress, etInspectName,
                tvStepIndustry, tvStepType, etRecordContent, mCase);
        cacheLawToLive();
    }

    /**
     * 把缓存中的执法稽查信息填写到文书中
     */
    private void cacheLawToLive() {
        if(mCase != null && "巡游车".equals(mCase.getHYLB())){
            tvCard.setText("监督卡");
            etCard.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 18));
            etCard.setHint("请输入监督卡号");
        }
        LawToCase lawToCase = CacheManager.getInstance().getLawToLive();
        if(lawToCase != null){
            etCard.setText(lawToCase.getJdkh());
            mJcitem = new JCItem();
            mJcitem.setLBMC(lawToCase.getWfxw());
            mJcitem.setZDLBID(lawToCase.getWfxwId());
            setRecordContent(mJcitem);
            tvIllegalBehavior.setText(lawToCase.getWfxw());
            assert mPresenter != null;
            mPresenter.getIllegalSituation(lawToCase.getWfxwId());
        }
    }

    /**
     * 监听在填写之后，不然填写缓存或者服务器上的内容会触发监听
     */
    @Override
    public void addListener(){
        listShowCarInfo.setAdapter(mAdapter);
        listShowDriverInfo.setAdapter(mDriverAdapter);
        listShowCarInfo.setOnItemClickListener(this);
        listShowDriverInfo.setOnItemClickListener(this);
        etCarLicense.addTextChangedListener(this);
        etCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StopChange) {
                    StopChange = false;
                    return;
                }
                assert mPresenter != null;
                if (s.length() < 5) {
                    listShowDriverInfo.setVisibility(View.GONE);
                    return;
                }
                mPresenter.getDriverInfoByCard(tvCard.getText().toString(),etCard.getText().toString());
            }
        });
    }

    @OnClick(R2.id.ibtn_timeline)
    public void showTimeLineDialog() {
        DialogUtil.showTimeLineDialog(self, Config.T_LIVERECORD, new DialogUtil.OnNoCaseListener() {
            @Override
            public void noCase() {
                UIUtils.toast(self, "未创建案件", Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick({R2.id.tv_end_time, R2.id.tv_start_time, R2.id.tv_start_time_des, R2.id.tv_card, R2.id.tv_inspected_address, R2.id.tv_inspect_district, R2.id.tv_inspect_address,
            R2.id.tv_select_inspect_address, R2.id.tv_select_inspected_address, R2.id.tv_car_type_select, R2.id.tv_Illegal_behavior, R2.id.tv_Illegal_situation, R2.id.tv_step_organizations,
            R2.id.tv_step_industry, R2.id.tv_step_type, R2.id.tv_end_time_des, R2.id.tv_compel_step})
    public void onViewClicked(View v) {
        if (mPresenter == null) return;
        int i = v.getId();
        if (i == R.id.tv_start_time || i == R.id.tv_start_time_des) {
            mPresenter.startCalendar(TextUtils.isEmpty(tvStartTime.getText()) ? tvStartTime.getHint().toString() : tvStartTime.getText().toString(), isUpdate);
        } else if (i == R.id.tv_end_time || i == R.id.tv_end_time_des) {
            mPresenter.endCalendar(tvStartTime, TextUtils.isEmpty(tvEndTime.getText()) ? tvEndTime.getHint().toString() : tvEndTime.getText().toString(), isUpdate);
        } else if (i == R.id.tv_card) {
            mPresenter.showCard(tvCard, etCard);
        } else if (i == R.id.tv_inspect_address || i == R.id.tv_select_inspect_address) {
            ActivityUtils.startActivity(LawSelectLocationActivity.getLocationIntent(self, tvInspectAddress.getText().toString(), "tvInspectAddress"));
        } else if (i == R.id.tv_inspected_address || i == R.id.tv_select_inspected_address) {
            ActivityUtils.startActivity(LawSelectLocationActivity.getLocationIntent(self, tvInspectedAddress.getText().toString(), "tvInspectedAddress"));
        } else if (i == R.id.tv_inspect_district) {
            mPresenter.showInspectDistrict(tvInspectDistrict);
        } else if (i == R.id.tv_car_type_select) {
            mPresenter.showCarType(tvCarTypeSelect, etCarType);
        } else if (i == R.id.tv_Illegal_behavior) {
            ActivityUtils.startActivity(IllegalDetailActivityActivity.class);
        } else if (i == R.id.tv_Illegal_situation) {
            mPresenter.getIllegalSituationWindow(mJcitem, tvIllegalSituation, etRecordContent);
        } else if (i == R.id.tv_compel_step) {
            mPresenter.showCompelWindow(tvCompelStep, llStepOne, tvStepTwo);
        } else if (i == R.id.tv_step_organizations) {
            mPresenter.showOrganizationsWindow(tvStepOrganizations);
        } else if (i == R.id.tv_step_industry) {
            mPresenter.showIndustryWindow(tvStepIndustry);
        } else if (i == R.id.tv_step_type) {
            mPresenter.showTypeWindow(tvStepType);
        }
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvInspectAddress".equals(addMsg.getType())) {
            tvInspectAddress.setText(addMsg.getAddr());
        } else if ("tvInspectedAddress".equals(addMsg.getType())) {
            tvInspectedAddress.setText(addMsg.getAddr());
        }
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.ILLEGAL)
    public void showIllegal(JCItemWrapper wrapper) {
        mJcitem = wrapper.getIllegalBehavior();
        tvIllegalBehavior.setText(mJcitem.getLBMC());
        String html = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + mCase.getBJCR()
                + "驾驶" + mCase.getVNAME() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人其行为涉嫌构成"
                + "<font color= '#4876FF'>" + mJcitem.getLBMC() + "</font>";
        etRecordContent.setText(Html.fromHtml(html));
        tvIllegalSituation.setText("");
        assert mPresenter != null;
        mPresenter.setIllegalSituation(wrapper.getIllegalSituation());
    }

    private void setRecordContent(JCItem jcItem) {
        String html = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + mCase.getBJCR()
                + "驾驶" + mCase.getVNAME() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人其行为涉嫌构成"
                + "<font color= '#4876FF'>" + jcItem.getLBMC() + "</font>";
        etRecordContent.setText(Html.fromHtml(html));
    }

    private void setRecordContent2() {
        String record = "";
        if ("无".equals(tvIllegalSituation.getText().toString())) {
            record = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + etInspectName.getText().toString()
                    + "驾驶" + etCarLicense.getText().toString() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人"
                    + "其行为涉嫌构成"
                    + "<font color= '#4876FF'>" + tvIllegalBehavior.getText().toString() + "</font>";
        } else {
            record = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + etInspectName.getText().toString()
                    + "驾驶" + etCarLicense.getText().toString() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人"
                    + "<font color= '#FF4500'>" + tvIllegalSituation.getText().toString() + "</font>" + "其行为涉嫌构成"
                    + "<font color= '#4876FF'>" + tvIllegalBehavior.getText().toString() + "</font>";
        }
        etRecordContent.setText(Html.fromHtml(record));
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (isStart) {
            assert mPresenter != null;
            if (date.getTime() < Long.valueOf(mCase.getCREATEUTC()) * 1000) {
                DialogUtil.showTipDialog(self, "开始时间不应该小于案件创建时间");
                return;
            }
            tvStartTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            if (!isUpdate) {
                tvEndTime.setText("");
                Date eDate = new Date();
                eDate.setTime(date.getTime() + mPresenter.getEndTime() * 1000);
                if (mPresenter.getEndTime() > Config.UTC_LIVERECORD && eDate.getTime() < mPresenter.getZPDZEndTime()) {//如果询问笔录大于11分钟，且重新选择开始时间调早后，需判断结束时间和询问笔录结束时间大小
                    eDate.setTime(mPresenter.getZPDZEndTime());
                }
                tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));
            } else {
                setEndTime();
            }
        } else {
            if (!TextUtils.isEmpty(tvStartTime.getText()) && TimeTool.getYYHHmm(tvStartTime.getText().toString()).getTime() > date.getTime() - Config.UTC_LIVERECORD * 1000) {
                DialogUtil.showTipDialog(self, "结束时间应大于开始时间后11分钟");
                return;
            }
            tvEndTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    public void onGetVehicleInfo(VehicleInfo vehicleInfo) {
        if(!isUpdate && vehicleInfo != null){
            etCarBrand.setText(vehicleInfo.getBrand());
            etCarColor.setText(vehicleInfo.getVehicleColor());
            if ("小型轿车".equals(vehicleInfo.getVehicleType())) {
                tvCarTypeSelect.setText("小型轿车");
                etCarType.setText("");
            } else {
                tvCarTypeSelect.setText("其他");
                etCarType.setText(vehicleInfo.getVehicleType());
            }
        }
    }

    /*
     * 把缓存中车辆信息进行更新
     */
    @Override
    public void showViewPartContent() {
        if(CacheManager.getInstance().getCarInfo() != null){
            onGetVehicleInfo(CacheManager.getInstance().getCarInfo().get(0));
        }
        if(CacheManager.getInstance().getDriverInfo() != null && mCase.getBJCR().equals(CacheManager.getInstance().getDriverInfo().getDriverName())){
            setDriverInfo(CacheManager.getInstance().getDriverInfo());
        }
    }

    /*
     * 把获取的信息展示在界面上
     */
    long Duration = Config.UTC_LIVERECORD * 1000;//开始时间和结束时间的时间差;

    @Override
    public void showViewContent(ParentQ parentQ) {
        LiveCheckRecordQ info = (LiveCheckRecordQ) parentQ;
        this.instrumentID = info.getID();
        Duration = TimeTool.parseDate2(info.getJCSJ2()).getTime() - TimeTool.parseDate2(info.getJCSJ1()).getTime();
        tvStartTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJCSJ1())));
        tvEndTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJCSJ2())));
        endUtc = tvEndTime.getText().toString();
        tvInspectDistrict.setText(info.getJCQY());
        tvInspectAddress.setText(info.getJCDD());
        etInspectName.setText(info.getBJCR());
        if ("男".equals(info.getSEX())) {
            rbtnMan.setChecked(true);
        } else {
            rbtnWoman.setChecked(true);
        }
        etNumber.setText(info.getTELLPHONE());
        if (!TextUtils.isEmpty(info.getSFZH())) {
            tvCard.setText("身份证");
            etCard.setText(info.getSFZH());
        } else {
            tvCard.setText("监督卡");
            etCard.setText(info.getCYZGZ());
        }
        etCarLicense.setText(info.getVNAME());
        etCarBrand.setText(info.getVLPP());
        etCarColor.setText(info.getVCOLOR());
        if (!TextUtils.isEmpty(info.getVTYPE()) && "小型轿车".equals(info.getVTYPE())) {
            tvCarTypeSelect.setText("小型轿车");
            etCarType.setEnabled(false);
            etCarType.setHint("");
        } else if (!TextUtils.isEmpty(info.getVTYPE())) {
            tvCarTypeSelect.setText("其他");
            etCarType.setText(info.getVTYPE());
            etCarType.setEnabled(true);
            etCarType.setHint(getResources().getString(R.string.please_input_your_car_model));
        }
        tvIllegalBehavior.setText(info.getWFXW());
        tvIllegalSituation.setText(info.getWFQX());
        mJcitem = new JCItem();
        mJcitem.setLBMC(info.getWFXW());
        mJcitem.setZDLBID(info.getWFXWID());
        mJcitem.setXMMC(info.getWFQX());
        paraseMeasures(info);
        tvInspectedAddress.setText(info.getDD());
        paraseContent(info);
        assert mPresenter != null;
        mPresenter.setEffectParam(info.getWFQX(), info.getQZCSSXGZ(), info.getSEX());
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
        StopChange = true;
        etCarLicense.setText(mCase.getVNAME());
        tvInspectAddress.setText(mCase.getJCDD());
        tvInspectDistrict.setText(mCase.getJCQY());
        etInspectName.setText(mCase.getBJCR());
        assert mPresenter != null;
        mPresenter.getVehicleInfo(mCase.getVNAME());
    }

    /**
     * 解析强制措施相关的内容返填回去
     */
    private void paraseMeasures(LiveCheckRecordQ info) {
        if (mCase != null && mCase.getHYLB().contains("非法")) {
            llContent.setVisibility(View.VISIBLE);
        } else {
            llContent.setVisibility(View.GONE);
            return;
        }
        if (!TextUtils.isEmpty(info.getQZCSSXGZ())) {
            if (info.getQZCSSXGZ().contains("中华人民共和国道路运输条例")) {
                tvCompelStep.setText("强制措施2");
                llStepOne.setVisibility(View.GONE);
                tvStepTwo.setVisibility(View.VISIBLE);
            } else {
                tvCompelStep.setText("强制措施1");
                llStepOne.setVisibility(View.VISIBLE);
                tvStepTwo.setVisibility(View.GONE);
                if (info.getQZCSSXGZ().contains("组织从事")) {
                    tvStepOrganizations.setText("组织从事");
                } else {
                    tvStepOrganizations.setText("从事");
                }
                if (info.getQZCSSXGZ().contains("巡游")) {
                    tvStepIndustry.setText("巡游");
                } else {
                    tvStepIndustry.setText("网络预约出租汽车客运经营");
                }
                if (info.getQZCSSXGZ().contains("第四条")) {
                    tvStepType.setText("第四条");
                } else {
                    tvStepType.setText("第五条");
                }
            }

        }
    }

    /**
     * 解析检查内容相关返填回去
     */
    private void paraseContent(LiveCheckRecordQ info) {
        String content;
        try {
            if (info.getJCNRJL().contains(info.getWFQX())) {
                content = info.getJCNRJL().split(info.getWFQX())[0] + "<font color= '#FF4500'>" + info.getWFQX() + "</font>";
                if (info.getJCNRJL().split(info.getWFQX()).length > 1 && info.getJCNRJL().split(info.getWFQX())[1].contains(info.getWFXW())) {
                    String content2 = info.getJCNRJL().split(info.getWFQX())[1];
                    if (content2.split(info.getWFXW()).length > 1) {
                        content += content2.split(info.getWFXW())[0] + "<font color= '#4876FF'>" + info.getWFXW() + "</font>" + content2.split(info.getWFXW())[1];
                    } else {
                        content += content2.split(info.getWFXW())[0] + "<font color= '#4876FF'>" + info.getWFXW() + "</font>";
                    }
                }else if(info.getJCNRJL().split(info.getWFQX()).length > 1 && isNoOne(info)){
                    String str = info.getJCNRJL().split(info.getJCNRJL().split(info.getWFQX())[0] + info.getWFQX())[1];
                    if(str.split(info.getWFXW()).length == 1){
                        content += str.split(info.getWFXW())[0] + "<font color= '#4876FF'>" + info.getWFXW() + "</font>";
                    }else{
                        content += str.split(info.getWFXW())[0] + "<font color= '#4876FF'>" + info.getWFXW() + "</font>" + str.split(info.getWFXW())[1];
                    }
                }else {
                    content += info.getJCNRJL().split(info.getJCNRJL().split(info.getWFQX())[0] + info.getWFQX())[1];
                }
            } else if (info.getJCNRJL().endsWith(info.getWFXW())) {
                content = info.getJCNRJL().split(info.getWFXW())[0] + "<font color= '#4876FF'>" + info.getWFXW() + "</font>";
            } else {
                content = info.getJCNRJL();
            }
        } catch (Exception e) {
            content = info.getJCNRJL();
        }
        etRecordContent.setText(Html.fromHtml(content));
    }

    public boolean isNoOne(LiveCheckRecordQ info){
        int length1 = info.getJCNRJL().length();
        int length2 = info.getJCNRJL().replaceAll(info.getWFXW(),"").length();
        return length1 > length2 + info.getWFXW().length();
    }

    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        mPresenter.verifyTime(tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim());
    }

    /**
     * 检测改变
     */
    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_LIVERECORD.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return !endUtc.equals(tvEndTime.getText().toString()) || isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_LIVERECORD;
    }

    @Override
    public void getCarOrDriverInfo() {
        assert mPresenter != null;
        if (etCarLicense.getText().toString().length() < 6) {
            listShowCarInfo.setVisibility(View.GONE);
            return;
        }
        mPresenter.getCarInfo(etCarLicense.getText().toString());
    }

    @Override
    public void notifyAdapter(boolean isCar) {
        if(isCar){
            listShowCarInfo.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }else{
            listShowDriverInfo.setVisibility(View.VISIBLE);
            mDriverAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getID(String id) {
        if(TextUtils.isEmpty(id) || "null".equals(id)) return;
        this.instrumentID = id;
    }

    @Override
    public void verifyTimeFinish(boolean result) {
        if (!result) {
            return;
        }

        final LiveCheckRecordQ lcrq = new LiveCheckRecordQ();
        lcrq.setJCSJ1((TimeTool.getYYHHmm(tvStartTime.getText().toString()).getTime() / 1000) + "");
        lcrq.setJCSJ2((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lcrq.setJCQY(tvInspectDistrict.getText().toString());
        lcrq.setJCDD(tvInspectAddress.getText().toString());
        lcrq.setBJCR(etInspectName.getText().toString());
        lcrq.setSEX(rbtnMan.isChecked() ? "男" : "女");
        lcrq.setTELLPHONE(etNumber.getText().toString());
        if ("身份证".equals(tvCard.getText().toString())) {
            if (etCard.getText().toString().length() != 18) {
                UIUtils.toast(self, "身份证长度不正确", Toast.LENGTH_SHORT);
                return;
            }
            lcrq.setSFZH(etCard.getText().toString());
        } else {
            lcrq.setCYZGZ(etCard.getText().toString());
        }
        lcrq.setVNAME(etCarLicense.getText().toString());
        lcrq.setVLPP(etCarBrand.getText().toString());
        lcrq.setVCOLOR(etCarColor.getText().toString());
        if ("其他".equals(tvCarTypeSelect.getText().toString())) {
            if (TextUtils.isEmpty(etCarType.getText())) {
                UIUtils.toast(self, "车型不能为空", Toast.LENGTH_SHORT);
                return;
            }
            lcrq.setVTYPE(etCarType.getText().toString());
        } else {
            lcrq.setVTYPE(tvCarTypeSelect.getText().toString());
        }
        lcrq.setWFXW(tvIllegalBehavior.getText().toString());
        lcrq.setWFQX(tvIllegalSituation.getText().toString());
        if (mCase.getHYLB().contains("非法")) {
            if ("强制措施1".equals(tvCompelStep.getText().toString()) && llContent.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(tvStepOrganizations.getText()) || TextUtils.isEmpty(tvStepIndustry.getText()) || TextUtils.isEmpty(tvStepType.getText())) {
                    UIUtils.toast(self, "请完善措施内容", Toast.LENGTH_SHORT);
                    return;
                }
                lcrq.setQZCSSXGZ("因当事人涉嫌未经许可擅自" + tvStepOrganizations.getText().toString() + tvStepIndustry.getText().toString() + "出租汽车客运经营，根据《北京市查处非法客运若干规定》" +
                        tvStepType.getText().toString()+"的规定");
            } else {
                lcrq.setQZCSSXGZ(tvStepTwo.getText().toString());
            }
        }
        lcrq.setDD(tvInspectedAddress.getText().toString());
        lcrq.setZFSJ((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lcrq.setBJCSJ((TimeTool.getYYHHmm(tvEndTime.getText().toString()).getTime() / 1000) + "");
        lcrq.setJCNRJL(etRecordContent.getText().toString());
        lcrq.setWFXWID(mJcitem.getZDLBID());
        lcrq.setZID(mCase.getID());
        lcrq.setID(instrumentID);
        lcrq.setZFZH1(mCase.getZFZH1());
        lcrq.setZFZH2(mCase.getZHZH2());
        lcrq.setZFRYXM2(mCase.getZFRYNAME2());
        lcrq.setZFRYXM1(mCase.getZFRYNAME1());
        lcrq.setZH(mCase.getZH());
        updateCheckContent(lcrq);
    }

    /**
     * 更新检查内容
     */
    private void updateCheckContent(final LiveCheckRecordQ lcrq) {
        if(!TextUtils.isEmpty(etRecordContent.getText()) && (!etRecordContent.getText().toString().contains(etCarLicense.getText().toString())
                || !etRecordContent.getText().toString().contains("当事人" + etInspectName.getText().toString() + "驾驶"))){
            Windows.confirm(self, "检测到车牌号或被检查人改变，是否更新检查内容?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRecordContent2();
                }
            }, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    submitData(lcrq);
                }
            });
        }else{
            submitData(lcrq);
        }
    }

    private void submitData(final LiveCheckRecordQ lcrq){
        if (1 == clickStatus) {
            startActivityForResult(CaseSignActivity.getIntent(self, lcrq, false), 0x0001);
        } else{
            if ("无".equals(tvIllegalSituation.getText().toString())) {
                Windows.confirm(self, "违法情形是否空置？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assert mPresenter != null;
                        if (!isUpdate) {
                            mPresenter.SubmitData(lcrq, followInstruments);
                        } else {
                            //调修改接口
                            mPresenter.UpdateData(lcrq, checkChange(), followInstruments);
                        }
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                assert mPresenter != null;
                if (!isUpdate) {
                    mPresenter.SubmitData(lcrq, followInstruments);
                } else {
                    //调修改接口
                    mPresenter.UpdateData(lcrq, checkChange(), followInstruments);
                }
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int idType = parent.getId();
        if(idType == R.id.list_show_car_info){
            VehicleInfo vehicleInfo = (VehicleInfo) parent.getItemAtPosition(position);
            StopChange = true;
            etCarLicense.setText(vehicleInfo.getVname());
            etCarBrand.setText(vehicleInfo.getBrand());
            etCarColor.setText(vehicleInfo.getVehicleColor());
            if ("小型轿车".equals(vehicleInfo.getVehicleType())) {
                tvCarTypeSelect.setText("小型轿车");
                etCarType.setText("");
            } else {
                tvCarTypeSelect.setText("其他");
                etCarType.setText(vehicleInfo.getVehicleType());
            }
            listShowCarInfo.setVisibility(View.GONE);
        }else if(idType == R.id.list_show_driver_info){
            DriverInfo info = (DriverInfo) parent.getItemAtPosition(position);
            StopChange = true;
            listShowDriverInfo.setVisibility(View.GONE);
            setDriverInfo(info);
        }
    }

    private void setDriverInfo(DriverInfo info) {
        etInspectName.setText(info.getDriverName());
        etNumber.setText(info.getTelphone());
        if("0".equals(info.getSex())){
            rbtnWoman.setChecked(true);
        }else{
            rbtnMan.setChecked(true);
        }
        if("身份证".equals(tvCard.getText().toString())){
            etCard.setText(info.getId());
        }else{
            etCard.setText(info.getJianduNumber());
        }
        tvInspectedAddress.setText(info.getAddress());
        assert mPresenter != null;
        mPresenter.showCardData(info.getId(),info.getJianduNumber());
    }

}
