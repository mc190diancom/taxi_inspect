package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.ClearEditText;
import com.miu30.common.util.FullListView;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerFristRegisterNoticeComponent;
import com.miu360.legworkwrit.di.module.FristRegisterNoticeModule;
import com.miu360.legworkwrit.mvp.contract.FristRegisterNoticeContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.presenter.FristRegisterNoticePresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class FristRegisterNoticeActivity extends BaseInstrumentActivity<FristRegisterNoticePresenter> implements FristRegisterNoticeContract.View, AdapterView.OnItemClickListener {
    @BindView(R2.id.ll_container)
    LinearLayout llContainer;

    //当事人
    @BindView(R2.id.et_litigant)
    @NotEmpty
    EditText etLitigant;
    //当事人地址
    @BindView(R2.id.tv_litigant_address)
    @NotEmpty
    TextView tvLitigantAddress;
    //文书填写时间
    @BindView(R2.id.tv_write_time)
    @NotEmpty(message = "请选择文书填写时间")
    TextView tvWriteTime;
    //先登地址
    @BindView(R2.id.tv_frist_register_address)
    @NotEmpty
    TextView tvFristRegisterAddress;
    //保存方式
    @BindView(R2.id.tv_save_way)
    TextView tvSaveWay;
    //车辆信息
    /*@BindView(R2.id.tv_car_msg)
    TextView tvCarMsg;*/
    //证件信息
    @BindView(R2.id.tv_credentials_info)
    TextView tvCardInfo;
    //车辆所有人
    @BindView(R2.id.tv_car_owner)
    TextView tvCarOwner;
    @NotEmpty
    @BindView(R2.id.et_car_owner_name)
    EditText etCarOwnerName;
    @BindView(R2.id.divider_car_owner)
    View dividerCarOwner;
    @BindView(R2.id.ll_container_car_owner)
    LinearLayout llContainerCarOwner;
    //停车场
    @BindView(R2.id.tv_choose_park)
    @NotEmpty
    TextView tvChoosePark;
    //本机关地址
    @BindView(R2.id.tv_agency_address)
    @NotEmpty
    TextView tvAgencyAddress;
    //本机关电话
    @BindView(R2.id.tv_agency_phone)
    @NotEmpty
    TextView tvAgencyPhone;
    //直接送达当事人
    @BindView(R2.id.rbtn_yes)
    RadioButton rbtnYes;
    @BindView(R2.id.rbtn_no)
    RadioButton rbtnNo;
    //文书送达时间
    @BindView(R2.id.tv_instrument_send_time)
    TextView tvSendTime;
    @NotEmpty
    @BindView(R2.id.et_car_vname)
    EditText etCarVname;
    @BindView(R2.id.ll_car_info)
    LinearLayout llCarInfo;
    @BindView(R2.id.et_car_cjh)
    EditText etCarCjh;
    @BindView(R2.id.et_car_color)
    EditText etCarColor;
    @BindView(R2.id.et_car_brand)
    EditText etCarBrand;
    @BindView(R2.id.et_car_model)
    EditText etCarModel;
    @BindView(R2.id.et_car_cardId)
    EditText etCarCardId;
    @BindView(R2.id.et_car_no)
    EditText etCarNo;
    @NotEmpty
    @BindView(R2.id.et_card_cyzgz)
    EditText etCardCyzgz;
    @BindView(R2.id.ll_container_card_cyzgz)
    LinearLayout llContainerCardCyzgz;
    @NotEmpty
    @BindView(R2.id.et_card_driver)
    EditText etCardDriver;
    @BindView(R2.id.ll_container_card_driver)
    LinearLayout llContainerCardDriver;
    @NotEmpty
    @BindView(R2.id.et_card_service)
    EditText etCardService;
    @BindView(R2.id.ll_container_card_service)
    LinearLayout llContainerCardService;
    @NotEmpty
    @BindView(R2.id.et_card_transport)
    EditText etCardTransport;
    @BindView(R2.id.ll_container_card_transport)
    LinearLayout llContainerCardTransport;
    @NotEmpty
    @BindView(R2.id.et_card_flag)
    EditText etCardFlag;
    @BindView(R2.id.ll_container_card_flag)
    LinearLayout llContainerCardFlag;
    @BindView(R2.id.ll_send_time)
    LinearLayout llSendTime;
    @BindView(R2.id.ll_car_owner)
    LinearLayout llCarOwner;
    @BindView(R2.id.ll_car_park)
    LinearLayout llCarPark;
    @BindView(R2.id.list_show_car_info)
    FullListView listShowCarInfo;
    @BindView(R2.id.et_note)
    EditText etNote;
    @BindView(R2.id.et_card_note1)
    ClearEditText etCardNote1;
    @BindView(R2.id.et_card_note2)
    ClearEditText etCardNote2;
    @BindView(R2.id.et_card_note3)
    ClearEditText etCardNote3;
    @BindView(R2.id.et_card_note4)
    ClearEditText etCardNote4;
    @BindView(R2.id.et_card_note5)
    ClearEditText etCardNote5;
    @NotEmpty
    @BindView(R2.id.et_other)
    ClearEditText etOther;
    @BindView(R2.id.et_card_note6)
    ClearEditText etCardNote6;
    @BindView(R2.id.ll_container_other)
    LinearLayout llContainerOther;

    @Inject
    CarInfoAdapter mAdapter;

    private List<String> mCards;
    private Park park;//选中的停车场

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFristRegisterNoticeComponent
                .builder()
                .appComponent(appComponent)
                .fristRegisterNoticeModule(new FristRegisterNoticeModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_frist_register_notice;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_FRISTREGISTER;
        header.init(self, "先行登记通知书");
        super.initData(savedInstanceState);
        etCardFlag.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etCarOwnerName.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etLitigant.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));

        etCarColor.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.chineseRegex, 6));
        etCarVname.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        etCarCjh.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 20));
        etCarCardId.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etCarBrand.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etCarModel.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));

        assert mPresenter != null;
        mPresenter.init(self, tvWriteTime, tvSendTime, etLitigant, tvFristRegisterAddress, mCase);
    }

    /**
     * 监听在填写之后，不然填写缓存或者服务器上的内容会触发监听
     */
    @Override
    public void addListener() {
        etCarVname.addTextChangedListener(this);
        listShowCarInfo.setAdapter(mAdapter);
        listShowCarInfo.setOnItemClickListener(this);
    }

    @Override
    public void showSaveWay(String saveWay) {
        tvSaveWay.setText(saveWay);
    }

    @OnClick({R2.id.tv_write_time, R2.id.tv_save_way, R2.id.tv_car_owner, R2.id.tv_instrument_send_time, R2.id.tv_credentials_info,
            R2.id.tv_choose_park, R2.id.tv_agency_address, R2.id.tv_litigant_address, R2.id.tv_current_address_with_litigant, R2.id.tv_frist_register_address,
            R2.id.tv_current_address_with_register, R2.id.tv_agency_phone, R2.id.rbtn_yes, R2.id.rbtn_no})
    public void onViewClicked(View v) {
        assert mPresenter != null;
        int i = v.getId();
        if (i == R.id.tv_write_time) {
            mPresenter.chooseWriteTime(tvWriteTime, isUpdate);

        } else if (i == R.id.tv_save_way) {
            mPresenter.chooseSaveWay(self);

        } else if (i == R.id.tv_car_owner) {
            mPresenter.chooseCarOwner(self);

        } else if (i == R.id.tv_instrument_send_time) {
            mPresenter.chooseInstrumentTime(tvWriteTime, tvSendTime, isUpdate);

        } else if (i == R.id.tv_credentials_info) {
            mPresenter.chooseCardMsg(self, mCards);

        } else if (i == R.id.tv_choose_park) {
            mPresenter.showParkList(self, tvChoosePark);

        } else if (i == R.id.tv_agency_address) {
            mPresenter.showAgencyAddress(self, tvAgencyAddress, tvAgencyPhone);

        } else if (i == R.id.tv_agency_phone) {
            mPresenter.showAgencyPhone(self, tvAgencyAddress, tvAgencyPhone);

        } else if (i == R.id.tv_litigant_address || i == R.id.tv_current_address_with_litigant) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvLitigantAddress.getText().toString(), "tvLitigantAddress"), Config.LAWLOCATION);

        } else if (i == R.id.tv_frist_register_address || i == R.id.tv_current_address_with_register) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self, tvFristRegisterAddress.getText().toString(), "tvFristRegisterAddress"), Config.LAWLOCATION);

        } else if (i == R.id.rbtn_yes) {
            llSendTime.setVisibility(View.VISIBLE);

        } else if (i == R.id.rbtn_no) {
            llSendTime.setVisibility(View.GONE);

        }
    }

    @Override
    public void showCarOwner(String carOwner) {
        tvCarOwner.setText(carOwner);
        if (carOwner.equals("其他")) {
            llContainerCarOwner.setVisibility(View.VISIBLE);
            dividerCarOwner.setVisibility(View.VISIBLE);
        } else {
            llContainerCarOwner.setVisibility(View.GONE);
            dividerCarOwner.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        if (isStart) {
            assert mPresenter != null;
            if (date.getTime() < mPresenter.lastEndTime()) {
                DialogUtil.showTipDialog(self, "文书填写时间必须大于现场检查笔录时间和案件创建时间");
                return;
            }
            tvWriteTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            setEndTime();
        } else {
            if (!TextUtils.isEmpty(tvWriteTime.getText()) && !(TimeTool.getYYHHmm(tvWriteTime.getText().toString()).getTime() < date.getTime())) {
                DialogUtil.showTipDialog(self, "文书送达时间需大于文书填写时间");
                return;
            }
            tvSendTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    public void showChooseCarMsg(List<String> carMsg) {

    }

    @Override
    public void showChooseCardMsg(List<String> cardMsg) {
        mCards = cardMsg;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cardMsg.size(); i++) {
            builder.append(cardMsg.get(i));
            if (i != cardMsg.size() - 1) {
                builder.append("、");
            }
        }
        tvCardInfo.setText(builder.toString());
        llCarInfo.setVisibility(View.GONE);
        llContainerCardCyzgz.setVisibility(View.GONE);
        llContainerCardDriver.setVisibility(View.GONE);
        llContainerCardService.setVisibility(View.GONE);
        llContainerCardTransport.setVisibility(View.GONE);
        llContainerCardFlag.setVisibility(View.GONE);
        llContainerOther.setVisibility(View.GONE);
        llCarOwner.setVisibility(View.GONE);
        llCarPark.setVisibility(View.GONE);
        for (int i = 0, len = cardMsg.size(); i < len; i++) {
            if (cardMsg.get(i).contains("车辆信息")) {
                llCarInfo.setVisibility(View.VISIBLE);
                llCarOwner.setVisibility(View.VISIBLE);
                llCarPark.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("从业资格证")) {
                llContainerCardCyzgz.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("网络预约出租汽车驾驶员证")) {
                llContainerCardDriver.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("服务监督卡")) {
                llContainerCardService.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("道路运输证")) {
                llContainerCardTransport.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("客运标志牌")) {
                llContainerCardFlag.setVisibility(View.VISIBLE);
            } else if (cardMsg.get(i).contains("其他")) {
                llContainerOther.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetVehicleInfo(VehicleInfo vehicleInfo) {
        if (!isUpdate && vehicleInfo != null) {
            etCarVname.setText(vehicleInfo.getVname());
            etCarCjh.setText(vehicleInfo.getChejiaNumber());
            etCarColor.setText(vehicleInfo.getVehicleColor());
            etCarBrand.setText(vehicleInfo.getBrand());
            etCarModel.setText(vehicleInfo.getVehicleType());
            etCarCardId.setText(vehicleInfo.getJingyinLicenceNumber());
        }
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            tvLitigantAddress.setText(CacheManager.getInstance().getLiveCheckRecord().getDD());
        }
        if (CacheManager.getInstance().getCarInfo() != null) {
            onGetVehicleInfo(CacheManager.getInstance().getCarInfo().get(0));
        }
        if (CacheManager.getInstance().getJDKH() != null) {
            etCardService.setText(CacheManager.getInstance().getJDKH());
        }
        List<AgencyInfo> agencyInfos = CacheManager.getInstance().getAgencyInfoByZFZH();
        if (agencyInfos != null && agencyInfos.size() > 0) {
            tvAgencyAddress.setText(agencyInfos.get(0).getDZ());
            tvAgencyPhone.setText(agencyInfos.get(0).getDH());
        }
    }

    /*
     * 把填写到服务器的信息重新填写回视图上
     */
    @Override
    public void showViewContent(ParentQ parentQ) {
        FristRegisterQ info = (FristRegisterQ) parentQ;
        this.instrumentID = info.getID();
        etLitigant.setText(info.getDSR());
        tvLitigantAddress.setText(info.getDZ());
        tvFristRegisterAddress.setText(info.getJDDD());
        tvSaveWay.setText(info.getSZD());
        mCards = new ArrayList<>();

        String cardName = "";
        if (!TextUtils.isEmpty(info.getV_VNAME())) {
            llCarInfo.setVisibility(View.VISIBLE);
            etCarVname.setText(info.getV_VNAME());
            etCarCjh.setText(info.getV_VJH());
            etCarColor.setText(info.getV_YS());
            etCarBrand.setText(info.getV_CX());
            etCarModel.setText(info.getV_XH());
            etCarCardId.setText(info.getV_ZH());
            etCarNo.setText(info.getV_BH());
            cardName += "车辆信息、";
            mCards.add("车辆信息");
            llCarOwner.setVisibility(View.VISIBLE);
            llCarPark.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(info.getCYZGZ())) {
            setInfo(llContainerCardCyzgz, etCardCyzgz, info.getCYZGZ());
            cardName += "从业资格证、";
            mCards.add("从业资格证");
        }
        if (!TextUtils.isEmpty(info.getFWJDK())) {
            setInfo(llContainerCardService, etCardService, info.getFWJDK());
            cardName += "服务监督卡、";
            mCards.add("服务监督卡");
        }
        if (!TextUtils.isEmpty(info.getWLYYCZQC())) {
            setInfo(llContainerCardDriver, etCardDriver, info.getWLYYCZQC());
            cardName += "网络预约出租汽车驾驶员证、";
            mCards.add("网络预约出租汽车驾驶员证");
        }
        if (!TextUtils.isEmpty(info.getDLYSZ())) {
            setInfo(llContainerCardTransport, etCardTransport, info.getDLYSZ());
            cardName += "道路运输证、";
            mCards.add("道路运输证");
        }
        if (!TextUtils.isEmpty(info.getKYBZP())) {
            setInfo(llContainerCardFlag, etCardFlag, info.getKYBZP());
            cardName += "客运标志牌、";
            mCards.add("客运标志牌");
        }
        if (!TextUtils.isEmpty(info.getQT())) {
            setInfo(llContainerOther, etOther, info.getQT());
            cardName += "其他、";
            mCards.add("其他");
        }
        tvCardInfo.setText(TextUtils.isEmpty(cardName) ? "车牌号码" : cardName.substring(0, cardName.length() - 1));
        park = new Park(info.getTCCID(),info.getTCCMC());
        tvChoosePark.setText(info.getTCCMC());
        tvAgencyAddress.setText(info.getJGDD());
        tvAgencyPhone.setText(info.getJGDH());
        setViewTime(info);

        if (!TextUtils.isEmpty(info.getCLSYR()) && info.getCLSYR().equals(info.getDSR())) {
            tvCarOwner.setText("当事人本人");
            llContainerCarOwner.setVisibility(View.GONE);
        } else {
            tvCarOwner.setText("其他");
            llContainerCarOwner.setVisibility(View.VISIBLE);
        }
        etCarOwnerName.setText(info.getCLSYR());
        etNote.setText(info.getV_BZ());
        etCardNote1.setText(info.getCYZGZ_BZ());
        etCardNote2.setText(info.getWLYYCZQC_BZ());
        etCardNote3.setText(info.getFWJDK_BZ());
        etCardNote4.setText(info.getDLYSZ_BZ());
        etCardNote5.setText(info.getKYBZP_BZ());
        etCardNote6.setText(info.getQT_BZ());
    }

    long Duration = (Config.UTC_FRISTREGISTER - 60) * 1000;//开始时间和结束时间的时间差

    //控件设置时间
    private void setViewTime(FristRegisterQ info) {
        long duration = 0;
        if (isConfirmed()) {//如果是待确认的状态
            duration = TimeTool.parseDate2(info.getJDSJ()).getTime() - TimeTool.parseDate(tvWriteTime.getHint().toString()).getTime();
            if (duration > 0) {
                tvWriteTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
            }
        } else {
            tvWriteTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
            tvWriteTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getJDSJ())));
        }
        if (!TextUtils.isEmpty(info.getZFSJ())) {
            rbtnYes.setChecked(true);
            rbtnNo.setChecked(false);
            Duration = TimeTool.parseDate2(info.getZFSJ()).getTime() - TimeTool.parseDate2(info.getJDSJ()).getTime();
            if (isConfirmed()) {//如果是待确认的状态
                if (duration > 0) {
                    tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getZFSJ())));
                } else {
                    setEndTime();
                }
            } else {
                tvSendTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getZFSJ())));
            }
        } else {
            llSendTime.setVisibility(View.GONE);
            rbtnYes.setChecked(false);
            rbtnNo.setChecked(true);
            setEndTime();
        }
    }

    /**
     * 设置界面上所谓的结束时间；因为已填写的时根据第一次填写的时间段，整段往后移
     */
    private void setEndTime() {
        tvSendTime.setText("");
        tvSendTime.setHint(TimeTool.formatEndTime(tvWriteTime.getText().toString(), tvWriteTime.getHint().toString(), Duration));
    }

    @Override
    public void showCaseContent() {
        etLitigant.setText(mCase.getBJCR());
        tvFristRegisterAddress.setText(mCase.getJCDD());
    }

    private void setInfo(LinearLayout ll, EditText et, String info) {
        ll.setVisibility(View.VISIBLE);
        et.setText(info);
    }

    @Override
    public void onValidationSucceeded() {
        if (Valid()) return;
        assert mPresenter != null;

        FristRegisterQ register = new FristRegisterQ();
        register.setDSR(etLitigant.getText().toString());
        register.setDZ(tvLitigantAddress.getText().toString());
        register.setJDDD(tvFristRegisterAddress.getText().toString());
        register.setSZD(tvSaveWay.getText().toString());

        register.setV_VNAME(llCarInfo.getVisibility() == View.VISIBLE ? etCarVname.getText().toString() : "");
        register.setV_VJH(llCarInfo.getVisibility() == View.VISIBLE ? etCarCjh.getText().toString() : "");
        register.setV_YS(llCarInfo.getVisibility() == View.VISIBLE ? etCarColor.getText().toString() : "");
        register.setV_CX(llCarInfo.getVisibility() == View.VISIBLE ? etCarBrand.getText().toString() : "");
        register.setV_XH(llCarInfo.getVisibility() == View.VISIBLE ? etCarModel.getText().toString() : "");
        register.setV_ZH(llCarInfo.getVisibility() == View.VISIBLE ? etCarCardId.getText().toString() : "");
        register.setV_BH(llCarInfo.getVisibility() == View.VISIBLE ? etCarNo.getText().toString() : "");
        register.setCYZGZ(llContainerCardCyzgz.getVisibility() == View.VISIBLE ? etCardCyzgz.getText().toString() : "");
        register.setFWJDK(llContainerCardService.getVisibility() == View.VISIBLE ? etCardService.getText().toString() : "");
        register.setWLYYCZQC(llContainerCardDriver.getVisibility() == View.VISIBLE ? etCardDriver.getText().toString() : "");
        register.setDLYSZ(llContainerCardTransport.getVisibility() == View.VISIBLE ? etCardTransport.getText().toString() : "");
        register.setKYBZP(llContainerCardFlag.getVisibility() == View.VISIBLE ? etCardFlag.getText().toString() : "");

        register.setJGDD(tvAgencyAddress.getText().toString());
        register.setJGDH(tvAgencyPhone.getText().toString());

        String endTime;
        register.setJDSJ(TimeTool.parseDate(tvWriteTime.getText().toString()).getTime() / 1000 + "");
        if (rbtnYes.isChecked()) {
            if (TextUtils.isEmpty(tvSendTime.getText())) {
                UIUtils.toast(self, "请选择文书送达时间", Toast.LENGTH_SHORT);
                return;
            }
            register.setZFSJ((TimeTool.getYYHHmm(tvSendTime.getText().toString()).getTime() / 1000) + "");
            endTime = register.getZFSJ();
        } else {
            endTime = register.getJDSJ();
        }
        if ("其他".equals(tvCarOwner.getText().toString())) {
            register.setCLSYR(etCarOwnerName.getText().toString());
        } else {
            register.setCLSYR(etLitigant.getText().toString());
        }
        register.setV_BZ(etNote.getText().toString());
        register.setCYZGZ_BZ(etCardNote1.getText().toString());
        register.setDLYSZ_BZ(etCardNote4.getText().toString());
        register.setFWJDK_BZ(etCardNote3.getText().toString());
        register.setKYBZP_BZ(etCardNote5.getText().toString());
        register.setWLYYCZQC_BZ(etCardNote2.getText().toString());
        register.setQT(etOther.getText().toString());
        register.setQT_BZ(etCardNote6.getText().toString());

        register.setZID(mCase.getID());
        register.setXZJGSJ(GetUTCUtil.setEndTime(tvWriteTime.getHint().toString(), endTime, Config.UTC_FRISTREGISTER));
        register.setQSSJ(GetUTCUtil.setEndTime(tvWriteTime.getHint().toString(), endTime, Config.UTC_FRISTREGISTER));
        if(llCarPark.getVisibility() == View.GONE  || park == null){
            register.setTCCID("");
            register.setTCCMC("");
        }else if(TextUtils.isEmpty(park.getId())){
            register.setTCCID("");
            register.setTCCMC(tvChoosePark.getText().toString());
        }else{
            register.setTCCID(park.getId());
            register.setTCCMC(tvChoosePark.getText().toString());
        }

        register.setID(instrumentID);
        register.setZH(mCase.getZH());
        register.setZFZH1(mCase.getZFZH1());
        register.setZFZH2(mCase.getZHZH2());
        register.setZFRY1(mCase.getZFRYNAME1());
        register.setZFRY2(mCase.getZFRYNAME2());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, register, false), 0x0011);
        } else if (!isUpdate) {
            register.setQSRQZ("");
            mPresenter.createFristRegister(register, followInstruments);
        } else {
            //调用更新接口
            mPresenter.UpdateData(register, checkChange(), followInstruments);
        }

    }

    /*
     * 这些控件不能统一的进行判断，这里单独判断
     */
    private boolean Valid() {
        if (mCards == null || mCards.size() <= 0) {
            UIUtils.toast(self, "请选择车辆及证件信息填写", Toast.LENGTH_SHORT);
            return true;
        }
        /*if (llContainerCarOwner.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(etCarOwnerName.getText().toString())) {
                UIUtils.toast(self, etCarOwnerName.getHint().toString(), Toast.LENGTH_SHORT);
                return true;
            }
        }*/
        return false;
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvAgencyAddress".equals(addMsg.getType())) {
            tvAgencyAddress.setText(addMsg.getAddr());
        } else if ("tvLitigantAddress".equals(addMsg.getType())) {
            tvLitigantAddress.setText(addMsg.getAddr());
        } else if ("tvFristRegisterAddress".equals(addMsg.getType())) {
            tvFristRegisterAddress.setText(addMsg.getAddr());
        }
    }

    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_FRISTREGISTER.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return !startUtc.equals(tvWriteTime.getText().toString()) || !endUtc.equals(tvSendTime.getText().toString()) || isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_FRISTREGISTER;
    }

    @Override
    public void getCarOrDriverInfo() {
        assert mPresenter != null;
        if (etCarVname.getText().toString().length() < 6) {
            listShowCarInfo.setVisibility(View.GONE);
            return;
        }
        mPresenter.getCarInfo(etCarVname.getText().toString());
    }

    @Override
    public void notifyAdapter() {
        listShowCarInfo.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getID(String id) {
        if (TextUtils.isEmpty(id) || "null".equals(id)) return;
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
        etCarVname.setText(vehicleInfo.getVname());
        etCarCjh.setText(vehicleInfo.getChejiaNumber());
        etCarColor.setText(vehicleInfo.getVehicleColor());
        etCarBrand.setText(vehicleInfo.getBrand());
        etCarModel.setText(vehicleInfo.getVehicleType());
        etCarCardId.setText(vehicleInfo.getJingyinLicenceNumber());
        listShowCarInfo.setVisibility(View.GONE);
    }

}
