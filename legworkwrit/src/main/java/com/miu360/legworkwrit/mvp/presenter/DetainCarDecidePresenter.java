package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.contract.DetainCarDecideContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu30.common.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;
import com.miu360.legworkwrit.util.TimeTool;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class DetainCarDecidePresenter extends BasePresenter<DetainCarDecideContract.Model, DetainCarDecideContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<VehicleInfo> data;

    ArrayList<VehicleInfo> data2 = new ArrayList<>();


    private Activity activity;

    private String[] allPark;
    private List<Park> allParkList;//停车场列表
    private String[] allCarOwner = {"其他", "当事人本人"};
    private String[] allViolationContent = {"非法客运", "国道条"};
    private String[] itemFour = {"从事", "组织从事"};
    private String[] itemFive = {"巡游", "网络预约"};
    private String[] itemSix = {"第四条", "第五条"};
    private String[] allAgencyAddress;
    private String[] allAgencyPhone;
    private Date sTime;//文书开始时间
    private Map<String,Integer> updateMap = new HashMap<>();

    @Inject
    public DetainCarDecidePresenter(DetainCarDecideContract.Model model, DetainCarDecideContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    /*
     * 扣车时间选择
     */
    public void startCalendar(TextView textView) {
        String time = TextUtils.isEmpty(textView.getText()) ? textView.getHint().toString() : textView.getText().toString();
        chooseTime(time,"扣车时间",true);
    }
    /*
     * 文书送达时间选择
     */
    public void endCalendar(TextView tvStartTime,TextView tvEndTime) {
        if (TextUtils.isEmpty(tvStartTime.getText())) {
            UIUtils.toast(activity, "请先确定扣车时间", Toast.LENGTH_SHORT);
            return;
        }
        String time = TextUtils.isEmpty(tvEndTime.getText()) ? tvEndTime.getHint().toString() : tvEndTime.getText().toString();
        chooseTime(time,"文书送达时间",false);
    }

    private void chooseTime(String time,String title, final boolean isStart) {
        Date date = TimeTool.getYYHHmm(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, title, calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showTime(date, isStart);
            }
        });
    }

    /**
     * 选择违法内容
     */
    public void chooseViolationContent(final TextView textView, final LinearLayout ll, final TextView tv2) {
        Windows.singleChoice(activity, "选择措施类型", allViolationContent, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                textView.setText(allViolationContent[position]);
                if (0 == position) {
                    ll.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.GONE);
                    tv2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private long time;
    public void init(Activity activity, TextView tvInstrumentSendDate, TextView tvDeducationTime, EditText etLitigant, EditText etCarLicense,
                     TextView tvDeductionAddress,TextView tvStepIndustry, TextView tvStepType, Case mCase,LinearLayout llContent) {
        this.activity = activity;
        Date sDate = new Date();
        Date eDate = new Date();
        time = GetUTCUtil.getLiveCheckRecordEndTimeL(mCase) * 1000;
        sDate.setTime(time);
        sTime = sDate;
        tvDeducationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
        eDate.setTime(time + (Config.UTC_CARDECIDE - 60) * 1000);
        tvInstrumentSendDate.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        etLitigant.setText(mCase.getBJCR());
        etCarLicense.setText(mCase.getVNAME());
        tvDeductionAddress.setText(mCase.getJCDD());
        if (mCase.getHYLB().contains("网约")) {
            tvStepIndustry.setText(itemFive[1]);
            tvStepType.setText(itemSix[1]);
        } else if (mCase.getHYLB().contains("巡游") || mCase.getHYLB().contains("非法经营出租汽车")) {
            tvStepIndustry.setText(itemFive[0]);
            tvStepType.setText(itemSix[0]);
        }
        getInitInfo(mCase.getID());
        setListToArray1(CacheManager.getInstance().getParks());
        setListToArray2(CacheManager.getInstance().getAgencyInfo());
    }

    /**
     * 获取上个文书的结束时间
     */
    public long lastEndTime() {
        return time;
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getKycljdsInfoById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<DetainCarDecideQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarDecideQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarDecideQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.showResult(result.getData().get(0), Config.RESULT_SUCCESS);
                            } else {
                                mRootView.showResult(null, Config.RESULT_EMPTY);
                            }
                        } else {
                            mRootView.showResult(null, Config.RESULT_ERROR);
                        }
                    }

                });
    }

    /*
     * 根据车牌号查询车辆基础信息
     */
    public void getVehicleInfo(String carNumb) {
        VehicleInfo infoCar = new VehicleInfo();
        infoCar.setVname(carNumb);
        infoCar.setStartIndex(0);
        infoCar.setEndIndex(10);
        Map<String, Object> map = new MapUtil().getMap("queryVehicleInfo", RequestParamsUtil.RequestVehicleInfo(infoCar));
        mModel.getVehicleInfo(map)
                .compose(RxUtils.<Result<List<VehicleInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<VehicleInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<VehicleInfo>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && !result.getData().isEmpty()) {
                                mRootView.getVehicleInfo(result.getData().get(0));
                            }
                        }
                    }
                });
    }

    /**
     * 获取停车场列表
     */
    public void getParkList() {
        mModel.getParkList("getTccmcList")
                .compose(RxUtils.<Result<List<Park>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<Park>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<Park>> listResult) {
                        if (listResult.ok()) {
                            setListToArray1(listResult.getData());
                        }
                    }
                });
    }

    private void setListToArray1(List<Park> parks){
        List<String> allParkName = new ArrayList<>(parks.size());
        allParkList = parks;
        for (Park park : parks) {
            allParkName.add(park.getName());
        }

        allPark = UIUtils.listToArray(allParkName);
    }

    private void setListToArray2(List<AgencyInfo> agencyInfos){
        if(agencyInfos == null){
            return;
        }
        ArrayList<String> addressList = new ArrayList<>(agencyInfos.size());
        ArrayList<String> phoneList = new ArrayList<>(agencyInfos.size());

        for (AgencyInfo info : agencyInfos) {
            addressList.add(info.getDZ());
            phoneList.add(info.getDH());
        }

        allAgencyAddress = UIUtils.listToArray(addressList);
        allAgencyPhone = UIUtils.listToArray(phoneList);
    }

    public void showParkList(Activity activity, final TextView textView) {
        if (allPark == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getParkList();
            return;
        }

        Windows.singleChoice(activity, "选择检查区域", allPark, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                //textView.setText(allPark[position]);
                mRootView.setPark(allParkList.get(position));
            }
        });
    }

    public void chooseCarOwner(Context context) {
        Windows.singleChoice(context, "请选择车辆所有人", allCarOwner, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                mRootView.showCarOwner(allCarOwner[position]);
            }
        });
    }

    /*
     * 选择组织下拉框
     */
    public void showOrganizationsWindow(final TextView tvStepOrganizations) {
        Windows.singleChoice(activity, "选择从事类别", itemFour, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvStepOrganizations.setText(itemFour[position]);
            }
        });
    }

    /*
     * 选择行业下拉框
     */
    public void showIndustryWindow(final TextView tvStepIndustry) {
        Windows.singleChoice(activity, "选择行业", itemFive, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvStepIndustry.setText(itemFive[position]);
            }
        });
    }

    /*
     * 选择法规类型下拉框
     */
    public void showTypeWindow(final TextView tvStepType) {
        Windows.singleChoice(activity, "选择法规条例", itemSix, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvStepType.setText(itemSix[position]);
            }
        });
    }

    public void addDetainCarDecide(final DetainCarDecideQ detainCarDecideQ,ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> params = new MapUtil().getMap("addKycljdsInfo", BaseData.gson.toJson(detainCarDecideQ));
        submitData(detainCarDecideQ, params,true,false,followInstruments);
    }

    public void updateDetainCarDecide(final DetainCarDecideQ detainCarDecideQ,boolean isChange,ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> params = new MapUtil().getMap("updateKycljdsInfo", BaseData.gson.toJson(detainCarDecideQ));
        submitData(detainCarDecideQ, params,false,isChange,followInstruments);
    }

    private void submitData(final DetainCarDecideQ detainCarDecideQ, Map<String, Object> params,final boolean isAdd,final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        updateMap.clear();
        mModel.addDetainCarDecide(params)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            JSONObject data = result.getData();
                            if (data != null) {
                                String id = data.optString("id");
                                mRootView.getID(id);
                            }

                            if (isAdd) {
                                updateMap.put(Config.STR_CARDECIDE, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            }
                            if ((isAdd || isChange) && followInstruments != null) {
                                setInstrumentsFlag(detainCarDecideQ, followInstruments);
                            } else if (followInstruments != null && followInstruments.size() > 0 && !park.equals(detainCarDecideQ.getTCCMC())) {
                                boolean isEffct = false;
                                ArrayList<CaseStatus> list = new ArrayList<>();
                                for (int i = 0; i < followInstruments.size(); i++) {
                                    list.clear();
                                    list.add(followInstruments.get(i));
                                    if (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && !park.equals(detainCarDecideQ.getTCCMC())) {
                                        setInstrumentsFlag(detainCarDecideQ, list);
                                        isEffct = true;
                                    }
                                }
                                if (!isEffct) {
                                    mRootView.startWebActivity(detainCarDecideQ);
                                }
                            } else {
                                mRootView.startWebActivity(detainCarDecideQ);
                            }
                            putCashService(detainCarDecideQ);
                        }
                        mRootView.showMessage(result.getMsg());
                    }
                });
    }

    private void setInstrumentsFlag(final DetainCarDecideQ detainCarDecideQ, final ArrayList<CaseStatus> followInstruments) {
        StringBuilder blid = new StringBuilder();
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if ((Config.STR_LIVETRANSCRIPT.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) ||
                    (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus())) {
                blid.append(followInstruments.get(i).getId());
                if (i != followInstruments.size() - 1) {
                    blid.append(",");
                }
                isNeedUpdateFlag = true;
            } else if (Config.STR_CARDECIDE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            mRootView.startWebActivity(detainCarDecideQ);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(detainCarDecideQ.getZID(), blid.toString(), "1", Config.ID_CARDECIDE, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if ((Config.STR_LIVETRANSCRIPT.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) ||
                                        (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus())) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 2);
                                    CacheManager.getInstance().updateUTC(followInstruments.get(i).getBlType(),true);
                                }else if(Config.STR_CARDECIDE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()){
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(detainCarDecideQ);
                        }else{
                            showReuploadPhotoDialog(detainCarDecideQ,followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final DetainCarDecideQ detainCarDecideQ,final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(detainCarDecideQ,followInstruments);
                        }
                    }
                });
    }

    /*
     * 文书创建或修改成功后，信息保存到缓存
     */
    private void putCashService(DetainCarDecideQ detainCarDecideQ) {
        CacheManager.getInstance().putUTC(Config.T_CARDECIDE, new UTC(Config.T_CARDECIDE
                , TimeTool.yyyyMMdd_HHmmss.format(sTime)
                , TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(detainCarDecideQ.getQSSJ()) * 1000))));
        CacheManager.getInstance().putPark(detainCarDecideQ.getTCCMC());
        CacheManager.getInstance().putDetainTime(TimeTool.formatyyyyMMdd_HHmm(Long.valueOf(detainCarDecideQ.getJDSJ())*1000));
    }

    public String getEndTime(long startTime) {
        long endTime;

        if (CacheManager.getInstance().getUTC(Config.T_CARFORM) == null && CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT) == null) {
            endTime = startTime + 24 * 60;
        } else if (CacheManager.getInstance().getUTC(Config.T_CARFORM) != null && CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT) != null) {
            endTime = startTime + 8 * 60;
        } else {
            endTime = startTime + 16 * 60;
        }

        return String.valueOf(endTime);
    }

    private void getAgencyInfos() {
        mModel.getAgencyInfos("getDdddAndDhList")
                .compose(RxUtils.<Result<List<AgencyInfo>>>applySchedulers(mRootView, false))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AgencyInfo>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<AgencyInfo>> listResult) {
                        if (listResult.ok()) {
                            setListToArray2(listResult.getData());
                        }
                    }
                });
    }

    public void showAgencyAddress(Activity activity, final TextView tvAgencyAddress, final TextView tvAgencyPhone) {
        if (allAgencyAddress == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getAgencyInfos();
            return;
        }

        Windows.singleChoice(activity, "选择机关地址", allAgencyAddress, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvAgencyAddress.setText(allAgencyAddress[position]);
                tvAgencyPhone.setText(allAgencyPhone[position]);
            }
        });
    }

    public void showAgencyPhone(Activity activity, final TextView tvAgencyAddress, final TextView tvAgencyPhone) {
        if (allAgencyAddress == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getAgencyInfos();
            return;
        }

        Windows.singleChoice(activity, "选择机关电话", allAgencyPhone, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvAgencyAddress.setText(allAgencyAddress[position]);
                tvAgencyPhone.setText(allAgencyPhone[position]);
            }
        });
    }

    /**
     * 根据车牌号处查询车辆信息
     * @param carNumb 车牌号
     */
    public void getCarInfo(String carNumb,final boolean isFrist) {
        VehicleInfo infoCar = new VehicleInfo();
        infoCar.setVname(carNumb);
        infoCar.setStartIndex(0);
        infoCar.setEndIndex(10);
        Map<String, Object> map = new MapUtil().getMap("queryVehicleInfo", RequestParamsUtil.RequestVehicleInfo(infoCar));
        mModel.getVehicleInfo(map)
                .compose(RxUtils.<Result<List<VehicleInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<VehicleInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<VehicleInfo>> result) {
                        if (result.ok()) {
                            if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                                if(isFrist){
                                    data.clear();
                                    data.addAll(result.getData());
                                }else{
                                    data2.clear();
                                    data2.addAll(result.getData());
                                }
                                mRootView.notifyAdapter(isFrist);
                            }
                        }
                    }

                });
    }

    private String park;

    public void setEffectParam(String park) {
        this.park = TextUtils.isEmpty(park) ? "" : park;
    }

    public ArrayList<VehicleInfo> getData() {
        return data2;
    }
}
