package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.ClearEditText;
import com.miu30.common.util.FullListView;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerDetainCarDecideComponent;
import com.miu360.legworkwrit.di.module.DetainCarDecideModule;
import com.miu360.legworkwrit.mvp.contract.DetainCarDecideContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.presenter.DetainCarDecidePresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 扣押车辆决定书
 */
public class DetainCarDecideActivity extends BaseInstrumentActivity<DetainCarDecidePresenter> implements DetainCarDecideContract.View, AdapterView.OnItemClickListener {
    //当事人
    @BindView(R2.id.et_litigant)
    @NotEmpty
    EditText etLitigant;
    //违反内容
    @BindView(R2.id.tv_violation_content)
    @NotEmpty
    TextView tvViolationContent;
    //当事人地址
    @BindView(R2.id.tv_litigant_address)
    @NotEmpty
    TextView tvLitigantAddress;
    //扣车地址
    @BindView(R2.id.tv_deduction_address)
    @NotEmpty
    TextView tvDeductionAddress;
    //扣车时间
    @BindView(R2.id.tv_deducation_time)
    @NotEmpty(message = "请确认扣车时间")
    TextView tvDeducationTime;
    //车辆品牌
    @BindView(R2.id.et_car_brand)
    @NotEmpty
    EditText etCarBrand;
    //车牌号
    @BindView(R2.id.et_car_license)
    @NotEmpty
    EditText etCarLicense;
    //车身颜色
    @BindView(R2.id.et_car_color)
    @NotEmpty
    EditText etCarColor;
    //车辆类型
    @BindView(R2.id.et_car_model)
    @NotEmpty
    EditText etCarModel;
    //车架号
    @BindView(R2.id.et_car_frame)
    @NotEmpty
    EditText etCarFrame;
    //车辆所有人
    @BindView(R2.id.tv_car_owner)
    @NotEmpty
    TextView tvCarOwner;
    @BindView(R2.id.et_car_owner_name)
    @NotEmpty
    EditText etCarOwnerName;
    @BindView(R2.id.divider_car_owner)
    View dividerCarOwner;
    @BindView(R2.id.ll_container_car_owner)
    LinearLayout llContainerCarOwner;
    //停车场
    @BindView(R2.id.tv_choose_park)
    @NotEmpty
    TextView tvChoosePark;
    //机关地址
    @BindView(R2.id.tv_agency_address)
    @NotEmpty
    TextView tvAgencyAddress;
    //机关电话
    @BindView(R2.id.tv_agency_phone)
    @NotEmpty
    TextView tvAgencyPhone;
    //文书送达日期
    @BindView(R2.id.tv_instrument_send_date)
    @NotEmpty(message = "请确认文书送达日期")
    TextView tvInstrumentSendDate;
    @BindView(R2.id.ll_instrument_send_date)
    LinearLayout llInstrumtnSendDate;
    @BindView(R2.id.line_instrument_send_date)
    View line;
    //文书是否送达
    @BindView(R2.id.rbtn_yes)
    RadioButton rbtnYes;
    @BindView(R2.id.rbtn_no)
    RadioButton rbtnNo;
    @NotEmpty(message = "请完善违反内容")
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
    @BindView(R2.id.ll_content)
    LinearLayout llContent;
    @BindView(R2.id.list_show_car_info)
    FullListView listShowCarInfo;
    @BindView(R2.id.list_show_car_info2)
    FullListView listShowCarInfo2;
    @BindView(R2.id.tv_car_info2)
    TextView tvCarInfo2;

    @NotEmpty
    @BindView(R2.id.et_car_license2)
    ClearEditText etCarLicense2;
    @NotEmpty
    @BindView(R2.id.et_car_brand2)
    ClearEditText etCarBrand2;
    @NotEmpty
    @BindView(R2.id.et_car_color2)
    ClearEditText etCarColor2;
    @NotEmpty
    @BindView(R2.id.et_car_model2)
    ClearEditText etCarModel2;
    @NotEmpty
    @BindView(R2.id.et_car_frame2)
    ClearEditText etCarFrame2;

    @BindView(R2.id.ll_decide_car_info)
    LinearLayout llDecideCarInfo;
    @BindView(R2.id.ll_car_info2)
    LinearLayout llCarInfo2;

    @Inject
    CarInfoAdapter mAdapter;

    CarInfoAdapter mAdapter2;

    private Park park;//选中的停车场

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDetainCarDecideComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .detainCarDecideModule(new DetainCarDecideModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_detain_car_decide; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_CARDECIDE;
        header.init(self, "扣押车辆决定书");
        super.initData(savedInstanceState);
        etLitigant.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        etCarColor.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.chineseRegex, 6));
        etCarOwnerName.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarBrand.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etCarFrame.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 20));

        assert mPresenter != null;
        mPresenter.init(self, tvInstrumentSendDate, tvDeducationTime, etLitigant, etCarLicense,
                tvDeductionAddress, tvStepIndustry, tvStepType, mCase, llContent);
        hideCarInfo2Layout();
    }

    private void hideCarInfo2Layout() {
        if(mCase.getHYLB() != null && (!mCase.getHYLB().contains("化危") || !mCase.getHYLB().contains("化危"))){
            llCarInfo2.setVisibility(View.GONE);
        }else{
            llCarInfo2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 监听在填写之后，不然填写缓存或者服务器上的内容会触发监听
     */
    @Override
    public void addListener() {
        etCarLicense.addTextChangedListener(this);
        listShowCarInfo.setAdapter(mAdapter);
        listShowCarInfo.setOnItemClickListener(this);

        etCarLicense2.addTextChangedListener(new TextWatcher() {
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
                if (etCarLicense2.getText().toString().length() < 6) {
                    listShowCarInfo2.setVisibility(View.GONE);
                    return;
                }
                mPresenter.getCarInfo(etCarLicense2.getText().toString(),false);
            }
        });
        mAdapter2 = new CarInfoAdapter(mPresenter.getData(),self);
        listShowCarInfo2.setAdapter(mAdapter2);
        listShowCarInfo2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleInfo vehicleInfo = (VehicleInfo) parent.getItemAtPosition(position);
                StopChange = true;
                etCarLicense2.setText(vehicleInfo.getVname());
                etCarBrand2.setText(vehicleInfo.getBrand());
                etCarColor2.setText(vehicleInfo.getVehicleColor());
                etCarModel2.setText(vehicleInfo.getVehicleType());
                etCarFrame2.setText(vehicleInfo.getChejiaNumber());
                listShowCarInfo2.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R2.id.tv_choose_park)
    public void choosePark(TextView textView) {
        assert mPresenter != null;
        mPresenter.showParkList(self, textView);
    }

    @OnClick(R2.id.tv_violation_content)
    public void chooseViolationContent(TextView textView) {
        assert mPresenter != null;
        mPresenter.chooseViolationContent(textView, llStepOne, tvStepTwo);
    }

    @OnClick({R2.id.tv_instrument_send_date, R2.id.tv_deducation_time})
    public void chooseTime(TextView textView) {
        int i = textView.getId();
        assert mPresenter != null;
        if (i == R.id.tv_deducation_time) {
            mPresenter.startCalendar(tvDeducationTime);
        } else {
            mPresenter.endCalendar(tvDeducationTime, tvInstrumentSendDate);
        }
    }

    @OnClick({R2.id.tv_agency_address})
    public void chooseAgencyAddress() {
        assert mPresenter != null;
        mPresenter.showAgencyAddress(self, tvAgencyAddress, tvAgencyPhone);
    }

    @OnClick({R2.id.tv_agency_phone})
    public void chooseAgencyPhone() {
        assert mPresenter != null;
        mPresenter.showAgencyPhone(self, tvAgencyAddress, tvAgencyPhone);
    }

    @OnClick({R2.id.tv_deduction_address, R2.id.tv_current_address_with_deducation})
    public void chooseDeducationAddress() {
        startActivityForResult(LawSelectLocationActivity.getLocationIntent(self
                , tvDeductionAddress.getText().toString()
                , "tvDeductionAddress")
                , Config.LAWLOCATION);
    }

    @OnClick({R2.id.tv_litigant_address, R2.id.tv_current_address_with_litigant})
    public void chooseLitigantAddress() {
        startActivityForResult(LawSelectLocationActivity.getLocationIntent(self
                , tvLitigantAddress.getText().toString()
                , "tvLitigantAddress")
                , Config.LAWLOCATION);
    }

    @OnClick(R2.id.tv_step_organizations)
    public void chooseOrganizations(TextView textView) {
        assert mPresenter != null;
        mPresenter.showOrganizationsWindow(textView);
    }

    @OnClick(R2.id.tv_step_industry)
    public void chooseIndustry(TextView textView) {
        assert mPresenter != null;
        mPresenter.showIndustryWindow(textView);
    }

    @OnClick(R2.id.tv_step_type)
    public void chooseType(TextView textView) {
        assert mPresenter != null;
        mPresenter.showTypeWindow(textView);
    }

    @OnClick(R2.id.tv_car_info2)
    public void chooseCarInfo2(TextView textView) {
        if(llDecideCarInfo.getVisibility() == View.VISIBLE){
            llDecideCarInfo.setVisibility(View.GONE);
        }else{
            llDecideCarInfo.setVisibility(View.VISIBLE);
        }
    }

    @OnCheckedChanged(R2.id.rbtn_yes)
    public void rbtnYesChanged() {
        if (rbtnYes.isChecked()) {
            llInstrumtnSendDate.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        } else {
            llInstrumtnSendDate.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvAgencyAddress".equals(addMsg.getType())) {
            tvAgencyAddress.setText(addMsg.getAddr());
        } else if ("tvLitigantAddress".equals(addMsg.getType())) {
            tvLitigantAddress.setText(addMsg.getAddr());
        } else if ("tvDeductionAddress".equals(addMsg.getType())) {
            tvDeductionAddress.setText(addMsg.getAddr());
        }
    }

    @Override
    public void onValidationSucceeded() {
        assert mPresenter != null;
        DetainCarDecideQ detainCarDecideQ = new DetainCarDecideQ();

        detainCarDecideQ.setDSR(etLitigant.getText().toString());
        detainCarDecideQ.setDD(tvLitigantAddress.getText().toString());
        if ("非法客运".equals(tvViolationContent.getText().toString())) {
            detainCarDecideQ.setXQ("因当事人涉嫌未经许可擅自" + tvStepOrganizations.getText().toString()
                    + tvStepIndustry.getText().toString() + "出租汽车客运经营，根据《北京市查处非法客运若干规定》"
                    + tvStepType.getText().toString() + "的规定");
        } else {
            detainCarDecideQ.setXQ(tvStepTwo.getText().toString());
        }

        long startTime = TimeTool.parseDate(tvDeducationTime.getText().toString()).getTime() / 1000;
        detainCarDecideQ.setJDSJ(String.valueOf(startTime));
        detainCarDecideQ.setJDDD(tvDeductionAddress.getText().toString());

        if(llDecideCarInfo.getVisibility() == View.VISIBLE){
            detainCarDecideQ.setV_VNAME(etCarLicense.getText().toString() + "&" + etCarLicense2.getText().toString());
            detainCarDecideQ.setV_YS(etCarColor.getText().toString() + "&" + etCarColor2.getText().toString());
            detainCarDecideQ.setV_PP(etCarBrand.getText().toString() + "&" + etCarBrand2.getText().toString());
            detainCarDecideQ.setV_CLLX(etCarModel.getText().toString() + "&" + etCarModel2.getText().toString());
            detainCarDecideQ.setV_CJH(etCarFrame.getText().toString() + "&" + etCarFrame2.getText().toString());
        }else{
            detainCarDecideQ.setV_VNAME(etCarLicense.getText().toString());
            detainCarDecideQ.setV_YS(etCarColor.getText().toString());
            detainCarDecideQ.setV_PP(etCarBrand.getText().toString());
            detainCarDecideQ.setV_CLLX(etCarModel.getText().toString());
            detainCarDecideQ.setV_CJH(etCarFrame.getText().toString());
        }

        if ("其他".equals(tvCarOwner.getText().toString())) {
            detainCarDecideQ.setCLSYR(etCarOwnerName.getText().toString());
        } else {
            detainCarDecideQ.setCLSYR(etLitigant.getText().toString());
        }

        detainCarDecideQ.setJGDH(tvAgencyPhone.getText().toString());
        detainCarDecideQ.setJGDD(tvAgencyAddress.getText().toString());
        detainCarDecideQ.setQSRQZ("");
        if(park == null || TextUtils.isEmpty(park.getId())){
            detainCarDecideQ.setTCCID("");
            detainCarDecideQ.setTCCMC(tvChoosePark.getText().toString());
        }else{
            detainCarDecideQ.setTCCID(park.getId());
            detainCarDecideQ.setTCCMC(tvChoosePark.getText().toString());
        }

        String endtime;
        if (rbtnYes.isChecked()) {
            detainCarDecideQ.setZFSJ(String.valueOf(TimeTool.parseDate(tvInstrumentSendDate.getText().toString()).getTime() / 1000));
            endtime = detainCarDecideQ.getZFSJ();
        } else {
            detainCarDecideQ.setZFSJ("");
            endtime = detainCarDecideQ.getJDSJ();
        }
        detainCarDecideQ.setQSSJ(GetUTCUtil.setEndTime(mPresenter.lastEndTime(), endtime, Config.UTC_CARDECIDE));
        detainCarDecideQ.setXZJGSJ(GetUTCUtil.setEndTime(mPresenter.lastEndTime(), endtime, Config.UTC_CARDECIDE));
        detainCarDecideQ.setZID(mCase.getID());
        detainCarDecideQ.setID(instrumentID);
        detainCarDecideQ.setZFZH1(mCase.getZFZH1());
        detainCarDecideQ.setZH(mCase.getZH());
        detainCarDecideQ.setZFZH2(mCase.getZHZH2());
        detainCarDecideQ.setZFRY1(mCase.getZFRYNAME1());
        detainCarDecideQ.setZFRY2(mCase.getZFRYNAME2());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, detainCarDecideQ, false), 0x0101);
        } else if (!isUpdate) {
            mPresenter.addDetainCarDecide(detainCarDecideQ, followInstruments);
        } else {
            //调用修改接口
            mPresenter.updateDetainCarDecide(detainCarDecideQ, checkChange(), followInstruments);
        }

    }

    @Override
    public void getVehicleInfo(VehicleInfo info) {
        if (!isUpdate && info != null) {
            etCarBrand.setText(info.getBrand());
            etCarColor.setText(info.getVehicleColor());
            etCarModel.setText(info.getVehicleType());
            etCarFrame.setText(info.getChejiaNumber());
        }
    }

    @OnClick(R2.id.tv_car_owner)
    public void chooseCarOwner() {
        assert mPresenter != null;
        mPresenter.chooseCarOwner(self);
    }

    @Override
    public void showCarOwner(String owner) {
        tvCarOwner.setText(owner);

        if ("其他".equals(owner)) {
            llContainerCarOwner.setVisibility(View.VISIBLE);
            dividerCarOwner.setVisibility(View.VISIBLE);
        } else {
            llContainerCarOwner.setVisibility(View.GONE);
            dividerCarOwner.setVisibility(View.GONE);
        }
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        if (CacheManager.getInstance().getCarInfo() != null) {
            getVehicleInfo(CacheManager.getInstance().getCarInfo().get(0));
        }
        List<AgencyInfo> agencyInfos = CacheManager.getInstance().getAgencyInfoByZFZH();
        if (agencyInfos != null && agencyInfos.size() > 0) {
            tvAgencyAddress.setText(agencyInfos.get(0).getDZ());
            tvAgencyPhone.setText(agencyInfos.get(0).getDH());
        }
        DriverInfo driverInfo = CacheManager.getInstance().getDriverInfo();
        if( driverInfo != null && mCase.getBJCR().equals(driverInfo.getDriverName())){
            tvLitigantAddress.setText(driverInfo.getAddress());
        }
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            LiveCheckRecordQ info = CacheManager.getInstance().getLiveCheckRecord();
            tvLitigantAddress.setText(CacheManager.getInstance().getLiveCheckRecord().getDD());

            etCarBrand.setText(info.getVLPP());
            etCarColor.setText(info.getVCOLOR());
            etCarModel.setText(info.getVTYPE());
            setWFNR(info.getQZCSSXGZ());
        }
    }

    private void setWFNR(String XQ) {
        if (!TextUtils.isEmpty(XQ) && XQ.contains("中华人民共和国道路运输条例")) {
            tvViolationContent.setText("国道条");
            llStepOne.setVisibility(View.GONE);
            tvStepTwo.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(XQ)) {
            tvViolationContent.setText("非法客运");
            llStepOne.setVisibility(View.VISIBLE);
            tvStepTwo.setVisibility(View.GONE);
            if (XQ.contains("组织从事")) {
                tvStepOrganizations.setText("组织从事");
            } else {
                tvStepOrganizations.setText("从事");
            }
            if (XQ.contains("巡游")) {
                tvStepIndustry.setText("巡游");
            } else {
                tvStepIndustry.setText("网络预约");
            }
            if (XQ.contains("第四条")) {
                tvStepType.setText("第四条");
            } else {
                tvStepType.setText("第五条");
            }
        }
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (isStart) {
            assert mPresenter != null;
            if (date.getTime() < mPresenter.lastEndTime()) {
                DialogUtil.showTipDialog(self, "扣车时间必须大于现场检查笔录时间和案件创建时间");
                return;
            }
            tvDeducationTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            setEndTime();
        } else {
            if (!TextUtils.isEmpty(tvDeducationTime.getText()) && !(TimeTool.getYYHHmm(tvDeducationTime.getText().toString()).getTime() < date.getTime())) {
                DialogUtil.showTipDialog(self, "文书送达时间需大于扣车时间");
                return;
            }
            tvInstrumentSendDate.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    public void showViewContent(ParentQ parentQ) {
        DetainCarDecideQ info = (DetainCarDecideQ) parentQ;
        this.instrumentID = info.getID();
        etLitigant.setText(info.getDSR());
        tvLitigantAddress.setText(info.getDD());
        try {
            if(!TextUtils.isEmpty(info.getV_VNAME()) && info.getV_VNAME().contains("&")){
                etCarLicense.setText(info.getV_VNAME().split("&")[0]);
                etCarLicense2.setText(info.getV_VNAME().split("&")[1]);
                llDecideCarInfo.setVisibility(View.VISIBLE);
            }else{
                etCarLicense.setText(info.getV_VNAME());
                llDecideCarInfo.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(info.getV_PP()) && info.getV_PP().contains("&")){
                etCarBrand.setText(info.getV_PP().split("&")[0]);
                etCarBrand2.setText(info.getV_PP().split("&")[1]);
            }else{
                etCarBrand.setText(info.getV_PP());
            }
            if(!TextUtils.isEmpty(info.getV_YS()) && info.getV_YS().contains("&")){
                etCarColor.setText(info.getV_YS().split("&")[0]);
                etCarColor2.setText(info.getV_YS().split("&")[1]);
            }else{
                etCarColor.setText(info.getV_YS());
            }
            if(!TextUtils.isEmpty(info.getV_CLLX()) && info.getV_CLLX().contains("&")){
                etCarModel.setText(info.getV_CLLX().split("&")[0]);
                etCarModel2.setText(info.getV_CLLX().split("&")[1]);
            }else{
                etCarModel.setText(info.getV_CLLX());
            }
            if(!TextUtils.isEmpty(info.getV_CJH()) && info.getV_CJH().contains("&")){
                etCarFrame.setText(info.getV_CJH().split("&")[0]);
                etCarFrame2.setText(info.getV_CJH().split("&")[1]);
            }else{
                etCarFrame.setText(info.getV_CJH());
            }
        }catch (Exception e){

        }
        setViewTime(info);
        park = new Park(info.getTCCID(),info.getTCCMC());
        tvDeductionAddress.setText(info.getJDDD());
        if (!TextUtils.isEmpty(info.getCLSYR()) && info.getCLSYR().equals(info.getDSR())) {
            tvCarOwner.setText("当事人本人");
            llContainerCarOwner.setVisibility(View.GONE);
        } else {
            tvCarOwner.setText("其他");
            etCarOwnerName.setText(info.getCLSYR());
        }
        tvChoosePark.setText(info.getTCCMC());
        tvAgencyPhone.setText(info.getJGDH());
        tvAgencyAddress.setText(info.getJGDD());
        startUtc = tvDeducationTime.getText().toString();
        endUtc = tvInstrumentSendDate.getText().toString();
        setEffctNr(info.getXQ());
        assert mPresenter != null;
        mPresenter.setEffectParam(info.getTCCMC());
    }

    private void setEffctNr(String XQ) {
        if (isConfirmed() &&  mCase.getHYLB().contains("非法") && CacheManager.getInstance().getLiveCheckRecord() != null) {
            setWFNR(CacheManager.getInstance().getLiveCheckRecord().getQZCSSXGZ());
        } else {
            setWFNR(XQ);
        }
    }

    long Duration = (Config.UTC_CARDECIDE - 60) * 1000;//开始时间和结束时间的时间差

    //控件设置时间
    private void setViewTime(DetainCarDecideQ info) {
        long duration = 0;
        if (isConfirmed()) {//如果是待确认的状态
            duration = TimeTool.parseDate2(info.getJDSJ()).getTime() - TimeTool.parseDate(tvDeducationTime.getHint().toString()).getTime();
            if (duration > 0) {
                tvDeducationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
            }
        } else {
            tvDeducationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
            tvDeducationTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
        }
        if (!TextUtils.isEmpty(info.getZFSJ())) {
            rbtnYes.setChecked(true);
            rbtnNo.setChecked(false);
            Duration = TimeTool.parseDate2(info.getZFSJ()).getTime() - TimeTool.parseDate2(info.getJDSJ()).getTime();
            if (isConfirmed()) {//如果是待确认的状态
                if (duration > 0) {
                    tvInstrumentSendDate.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getZFSJ())));
                } else {
                    setEndTime();
                }
            } else {
                tvInstrumentSendDate.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getZFSJ())));
            }
        } else {
            llInstrumtnSendDate.setVisibility(View.GONE);
            rbtnYes.setChecked(false);
            rbtnNo.setChecked(true);
            setEndTime();
        }
    }

    /**
     * 设置界面上所谓的结束时间；因为已填写的时根据第一次填写的时间段，整段往后移
     */
    private void setEndTime() {
        tvInstrumentSendDate.setText("");
        tvInstrumentSendDate.setHint(TimeTool.formatEndTime(tvDeducationTime.getText().toString(), tvDeducationTime.getHint().toString(), Duration));
    }

    @Override
    public void showCaseContent() {
        assert mPresenter != null;
        StopChange = true;
        mPresenter.getVehicleInfo(mCase.getVNAME());
        etLitigant.setText(mCase.getBJCR());
        etCarLicense.setText(mCase.getVNAME());
        tvDeductionAddress.setText(mCase.getJCDD());
    }


    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_CARDECIDE.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return !startUtc.equals(tvDeducationTime.getText().toString()) || !endUtc.equals(tvInstrumentSendDate.getText().toString()) || isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_CARDECIDE;
    }

    @Override
    public void getCarOrDriverInfo() {
        assert mPresenter != null;
        if (etCarLicense.getText().toString().length() < 6) {
            listShowCarInfo.setVisibility(View.GONE);
            return;
        }
        mPresenter.getCarInfo(etCarLicense.getText().toString(),true);
    }

    @Override
    public void notifyAdapter(boolean isFrist) {
        if(isFrist){
            listShowCarInfo.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }else{
            listShowCarInfo2.setVisibility(View.VISIBLE);
            mAdapter2.notifyDataSetChanged();
        }

    }

    @Override
    public void getID(String id) {
        if(TextUtils.isEmpty(id) || "null".equals(id)) return;
        this.instrumentID = id;
    }

    @Override
    public void setPark(Park park) {
        tvChoosePark.setText(park.getName());
        this.park = park;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VehicleInfo vehicleInfo = (VehicleInfo) parent.getItemAtPosition(position);
        StopChange = true;
        etCarLicense.setText(vehicleInfo.getVname());
        etCarBrand.setText(vehicleInfo.getBrand());
        etCarColor.setText(vehicleInfo.getVehicleColor());
        etCarModel.setText(vehicleInfo.getVehicleType());
        etCarFrame.setText(vehicleInfo.getChejiaNumber());
        listShowCarInfo.setVisibility(View.GONE);
    }

}
