package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
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
import com.miu360.legworkwrit.mvp.contract.FristRegisterNoticeContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
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

/**
 * 先行登记通知书
 */
@ActivityScope
public class FristRegisterNoticePresenter extends BasePresenter<FristRegisterNoticeContract.Model, FristRegisterNoticeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private String[] allSaveWay = {"原地", "异地"};
    private String[] allCarOwner = {"其他", "当事人本人"};
    private String[] allCarMsg = {"车牌号码", "车架号", "车辆颜色", "车辆品牌", "车辆型号", "证号", "编号"};
    private String[] allCardMsg = {"车辆信息","从业资格证", "服务监督卡", "网络预约出租汽车驾驶员证", "道路运输证", "客运标志牌","其他"};

    private String[] allAgencyAddress;
    private String[] allAgencyPhone;
    private String[] allPark;
    private List<Park> allParkList;//停车场列表
    private Activity activity;
    private Date sTime;//文书开始时间
    private Map<String,Integer> updateMap = new HashMap<>();

    @Inject
    ArrayList<VehicleInfo> data;


    @Inject
    public FristRegisterNoticePresenter(FristRegisterNoticeContract.Model model, FristRegisterNoticeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    private long time;

    public void init(Activity activity, TextView tvWriteTime, TextView tvSendTime, EditText etLitigant, TextView tvFristRegisterAddress, Case mCase) {
        this.activity = activity;

        Date sDate = new Date();
        Date eDate = new Date();
        time = GetUTCUtil.getLiveCheckRecordEndTimeL(mCase) * 1000;
        sDate.setTime(time);
        sTime = sDate;
        tvWriteTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
        eDate.setTime(time + (Config.UTC_FRISTREGISTER - 60) * 1000);
        tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        etLitigant.setText(mCase.getBJCR());
        tvFristRegisterAddress.setText(mCase.getJCDD());
        getInitInfo(mCase.getID());
        setListToArray(CacheManager.getInstance().getParks());
        setListToArray2(CacheManager.getInstance().getAgencyInfo());

    }

    /*
     * 根据车牌号查询车辆基础信息
     */
    /*public void getVehicleInfo(String carNumb) {
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
                            if (result.getData() != null && !result.getData().isEmpty() && result.getData().size() > 0) {
                                mRootView.onGetVehicleInfo(result.getData().get(0));
                            }
                        }
                    }

                });
    }*/

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
        Map<String, Object> map = new MapUtil().getMap("getDjbctzsInfoById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<FristRegisterQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<FristRegisterQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<FristRegisterQ>> result) {
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
     * 文书填写时间
     */
    public void chooseWriteTime(TextView tvWriteTime, boolean isUpdate) {
        if (TextUtils.isEmpty(tvWriteTime.getText().toString())) {
            chooseTime(activity, tvWriteTime.getHint().toString(), "请选择文书填写时间", true);
        } else {
            chooseTime(activity, tvWriteTime.getText().toString(), "请选择文书填写时间", true);
        }
    }

    /*
     * 文书送达时间
     */
    public void chooseInstrumentTime(TextView tvWriteTime, TextView tvSendTime, boolean isUpdate) {
        if (TextUtils.isEmpty(tvWriteTime.getText())) {
            UIUtils.toast(activity, "请先确定文书填写时间", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(tvSendTime.getText())) {
            chooseTime(activity, tvSendTime.getHint().toString(), "请选择文书送达时间", false);
        } else {
            chooseTime(activity, tvSendTime.getText().toString(), "请选择文书送达时间", false);
        }
    }

    /*
     * 选择时间控件
     */
    private void chooseTime(Activity activity, String defaultTime, String title, final boolean isWriteTime) {
        Date date = TimeTool.parseDate(defaultTime);
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        DialogUtil.TimePicker(activity, title, calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showTime(date, isWriteTime);
            }
        });
    }

    public void chooseSaveWay(Context context) {
        Windows.singleChoice(context, "请选择保存方式", allSaveWay, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                mRootView.showSaveWay(allSaveWay[position]);
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

    public void chooseCarMsg(Context context, List<String> Cars) {
        DialogUtil.showMultipleChoiceDialog(context, "车辆信息", allCarMsg, Cars, new DialogUtil.MultipleChoiceCallback() {
            @Override
            public void multipleChoice(List<String> choices) {
                mRootView.showChooseCarMsg(choices);
            }
        });
    }

    public void chooseCardMsg(Context context, List<String> Cards) {
        DialogUtil.showMultipleChoiceDialog(context, "车辆及证件信息", allCardMsg, Cards, new DialogUtil.MultipleChoiceCallback() {
            @Override
            public void multipleChoice(List<String> choices) {
                mRootView.showChooseCardMsg(choices);
            }
        });
    }

    public void getParkList() {
        mModel.getParkList("getTccmcList")
                .compose(RxUtils.<Result<List<Park>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<Park>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<Park>> listResult) {
                        if (listResult.ok()) {
                            setListToArray(listResult.getData());
                        }
                    }
                });
    }

    private void setListToArray(List<Park> listResult) {
        if(listResult == null){
            return;
        }
        allParkList = listResult;
        List<String> allParkName = new ArrayList<>(listResult.size());

        for (Park park : listResult) {
            allParkName.add(park.getName());
        }
        allPark = UIUtils.listToArray(allParkName);
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

    public void createFristRegister(final FristRegisterQ registerQ, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("addDjbctzsInfo", RequestParamsUtil.getJsonEmptyStr(registerQ));
        submitData(registerQ, map, true, false, followInstruments);
    }

    public void UpdateData(final FristRegisterQ registerQ, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateDjbctzsInfo", RequestParamsUtil.getJsonEmptyStr(registerQ));
        submitData(registerQ, map, false, isChange, followInstruments);
    }

    private void submitData(final FristRegisterQ fristRegisterQ, Map<String, Object> map, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        mModel.addFristRegister(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            putCashService(fristRegisterQ);

                            JSONObject data = result.getData();
                            if (data != null) {
                                String id = data.optString("id");
                                mRootView.getID(id);
                                fristRegisterQ.setID(id);
                            }

                            if (isAdd) {
                                updateMap.put(Config.STR_FRISTREGISTER, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            }
                            if ((isAdd || isChange) && followInstruments != null) {
                                setInstrumentsFlag(fristRegisterQ, followInstruments);
                            } else {
                                mRootView.startWebActivity(fristRegisterQ);
                            }
                        }
                        mRootView.showMessage(result.getMsg());
                    }
                });
    }

    private void setInstrumentsFlag(final FristRegisterQ registerQ, final ArrayList<CaseStatus> followInstruments) {
        StringBuilder blid = new StringBuilder();
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) {
                blid.append(followInstruments.get(i).getId());
                if (i != followInstruments.size() - 1) {
                    blid.append(",");
                }
                isNeedUpdateFlag = true;
            } else if (Config.STR_FRISTREGISTER.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            mRootView.startWebActivity(registerQ);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(registerQ.getZID(), blid.toString(), "1", Config.ID_FRISTREGISTER, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 2);
                                    CacheManager.getInstance().updateUTC(followInstruments.get(i).getBlType(),true);
                                }else if(Config.STR_FRISTREGISTER.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()){
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(registerQ);
                        } else {
                            showReuploadPhotoDialog(registerQ, followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final FristRegisterQ registerQ, final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(registerQ,followInstruments);
                        }
                    }
                });
    }

    public String getEndTime(long startTime) {
        long endTime;

        if (CacheManager.getInstance().getUTC(Config.T_CARFORM) != null) {
            endTime = startTime + 12 * 60;
        } else {
            endTime = startTime + 24 * 60;
        }

        return String.valueOf(endTime);
    }

    private void putCashService(FristRegisterQ fristRegisterQ) {
        CacheManager.getInstance().putUTC(Config.T_FRISTREGISTER, new UTC(Config.T_FRISTREGISTER
                , TimeTool.yyyyMMdd_HHmmss.format(sTime)
                , TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(fristRegisterQ.getQSSJ()) * 1000))));
        CacheManager.getInstance().putPark(fristRegisterQ.getTCCMC());
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
    public void getCarInfo(String carNumb) {
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
                            if (result.ok() && result.getData() != null && result.getData().size() >0 ) {
                                data.clear();
                                data.addAll(result.getData());
                                mRootView.notifyAdapter();
                            }
                        }
                    }

                });
    }
}
