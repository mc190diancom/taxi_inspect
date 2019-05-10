package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.AdministrativePenaltyContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailItem;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailParams;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.MapUtil;
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
public class AdministrativePenaltyPresenter extends BasePresenter<AdministrativePenaltyContract.Model, AdministrativePenaltyContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<DriverInfo> data;

    private Activity activity;

    private String[] itemCard = {"服务监督卡号", "网络预约出租汽车驾驶员证号"};

    private String[] itemWFQX;

    private List<IllegalDetailItem> IllegalDetailList;

    private Date sTime;//存储文书的开始时间
    private Map<String, Integer> updateMap = new HashMap<>();

    private Case mCase;
    private String zdlbid;

    @Inject
    public AdministrativePenaltyPresenter(AdministrativePenaltyContract.Model model, AdministrativePenaltyContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public String getXWID(){
        return zdlbid;
    }

    public void setXWID(String zdlbid){
       this.zdlbid = zdlbid;
    }

    /*
     * 初始化一些控件信息
     */
    private long time;

    public void init(Activity activity, TextView tvPunishTime, TextView tvSendTime, TextView tvChooseWrite, EditText etCarNumber, EditText etLitigant, TextView tvPunishAddress, Case mCase) {
        this.activity = activity;
        this.mCase = mCase;
        Date sDate = new Date();
        Date eDate = new Date();
        time = GetUTCUtil.getLiveCheckRecordEndTimeL(mCase) * 1000;
        sDate.setTime(time);
        sTime = sDate;
        tvPunishTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
        eDate.setTime(time + (Config.UTC_ADMINISTRATIVE - 60) * 1000);
        tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        tvChooseWrite.setText(itemCard[0]);
        etCarNumber.setText(mCase.getVNAME());
        etLitigant.setText(mCase.getBJCR());
        tvPunishAddress.setText(mCase.getJCDD());

        getInitInfo(mCase.getID());
        //setListToArray(CacheManager.getInstance().getIllegalDetail());
        getIllegalDetail();
    }

    /**
     * 获取上个文书的结束时间
     */
    public long lastEndTime() {
        return time;
    }

    private void setListToArray(List<IllegalDetail> illegalDetails) {
        if (illegalDetails == null) {
            return;
        }

//        IllegalDetailList = illegalDetails;
        List<String> IllegalDetailName = new ArrayList<>(illegalDetails.size());

        for (IllegalDetail illegalDetail : illegalDetails) {
            IllegalDetailName.add(illegalDetail.getWFXWMC());
        }

        itemWFQX = UIUtils.listToArray(IllegalDetailName);
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getXzcfjdsInfoById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<AdministrativePenalty>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AdministrativePenalty>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<AdministrativePenalty>> result) {
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

    /**
     * 处罚时间展示
     */
    public void showPunishTime(TextView tvPunishTime, boolean isUpdate) {
        String time = TextUtils.isEmpty(tvPunishTime.getText()) ? tvPunishTime.getHint().toString() : tvPunishTime.getText().toString();
        showDate("处罚时间", time, true);
    }

    /**
     * 文书送达时间展示
     */
    public void showSendTime(TextView tvPunishTime, TextView tvSendTime, boolean isUpdate) {
        if (TextUtils.isEmpty(tvPunishTime.getText())) {
            UIUtils.toast(activity, "请先确定处罚时间", Toast.LENGTH_SHORT);
            return;
        }
        String time = TextUtils.isEmpty(tvSendTime.getText()) ? tvSendTime.getHint().toString() : tvSendTime.getText().toString();
        showDate("文书送达时间", time, false);//第三个参数为处罚时间(开始）
    }

    /*
     * 展示时间控件
     */
    private void showDate(final String title, String time, final boolean isStart) {
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

    /*
     * 弹出选择卡号的下拉框
     */
    public void showSelectCardWindow(final TextView tvChooseWrite, final EditText et) {
        Windows.singleChoice(activity, "选择证件类型", itemCard, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvChooseWrite.setText(itemCard[position]);
                if (0 == position) {
                    et.setHint(activity.getResources().getString(R.string.input_supervision_hint));
                } else {
                    et.setHint(activity.getResources().getString(R.string.input_driving_license_hint));
                }
            }
        });
    }

    public void getIllegalSituationWindow(final TextView tvIllegalContent) {
        if (TextUtils.isEmpty(tvIllegalContent.getText())) {
            UIUtils.toast(activity, "请先选择违反内容", Toast.LENGTH_SHORT);
        }
    }

    public void submitAdministrativeData(AdministrativePenalty ap) {
        Map<String, Object> map = new MapUtil().getMap("addXzcfjdsInfo", RequestParamsUtil.RequestAdministrativePenaltyInfo(ap));
        submitData(map, ap, true, false, null);
    }

    /*
     * 行政处罚信息修改
     */
    public void UpdateData(AdministrativePenalty ap, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateXzcfjdsInfo", RequestParamsUtil.RequestAdministrativePenaltyInfo(ap));
        submitData(map, ap, false, isChange, followInstruments);
    }

    private void submitData(Map<String, Object> map, final AdministrativePenalty ap, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        mModel.submitAdministrativeData(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            putCashService(ap);
                            String id = result.getData().optString("id");
                            mRootView.getID(id);
                            mRootView.showMessage(result.getMsg());
                            if (isAdd) {
                                updateMap.put(Config.STR_ADMINISTRATIVE, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                                mRootView.startWebActivity(ap);
                            } else {
                                if (isChange && followInstruments != null) {
                                    setInstrumentsFlag(ap, followInstruments);
                                } else {
                                    mRootView.startWebActivity(ap);
                                }
                            }
                        }
                    }
                });
    }

    private void setInstrumentsFlag(final AdministrativePenalty ap, final ArrayList<CaseStatus> followInstruments) {
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.STR_ADMINISTRATIVE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            mRootView.startWebActivity(ap);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(ap.getZID(), "", "1", Config.ID_ADMINISTRATIVE, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if (Config.STR_ADMINISTRATIVE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(ap);
                        } else {
                            showReuploadPhotoDialog(ap, followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final AdministrativePenalty ap, final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(ap, followInstruments);
                        }
                    }
                });
    }

    /*
     * 文书创建或修改成功后，时间存缓存
     */
    private void putCashService(AdministrativePenalty ap) {
        CacheManager.getInstance().putUTC(Config.T_ADMINISTRATIVE, new UTC(Config.T_ADMINISTRATIVE,
                TimeTool.yyyyMMdd_HHmmss.format(sTime),
                TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(ap.getQSSJ()) * 1000))));
    }

    public void showIllegalDetailWindow(final TextView tvIllegalContent, final TextView tvIllegalQX) {
        if (itemWFQX == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getIllegalDetail();
            return;
        }

        Windows.singleChoice(activity, "请选择违反内容", itemWFQX, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvIllegalContent.setText(itemWFQX[position]);
                tvIllegalQX.setText(IllegalDetailList.get(position).getWFX());
                zdlbid = IllegalDetailList.get(position).getZDLBID();
            }
        });
    }

    /**
     * 获取违法内容
     */
    private void getIllegalDetail() {
        IllegalDetailParams params = new IllegalDetailParams(mCase.getHYLB(), "警告");
        Map<String, Object> map = new MapUtil().getMap("selectJcxByJg", BaseData.gson.toJson(params));

        mModel.getIllegalDetail(map)
                .compose(RxUtils.<Result<List<IllegalDetailItem>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<IllegalDetailItem>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<IllegalDetailItem>> listResult) throws Exception {
                        if (listResult.ok() && listResult.getData() != null && !listResult.getData().isEmpty()) {
                            List<IllegalDetailItem> data = listResult.getData();
                            List<String> des = new ArrayList<>(data.size());
                            IllegalDetailList = data;

                            for (IllegalDetailItem item : data) {
                                des.add(item.getXMMC() + "，构成" + item.getLBMC());
                            }

                            itemWFQX = UIUtils.listToArray(des);
                        }
                    }
                });
    }

    /**
     * 根据身份证或者监督卡号获取当事人具体信息
     *
     * @param type 用于判断是否是监督卡
     * @param card 监督卡号
     */
    public void getDriverInfoByCard(String type, String card) {
        DriverInfo info = new DriverInfo();
        if (itemCard[1].equals(type) || card.length() < 5) {
            return;
        }
        info.setJianduNumber(card);
        info.setStartIndex(0);
        info.setEndIndex(10);
        Map<String, Object> map = new MapUtil().getMap("queryDriverInfo", RequestParamsUtil.RequestDriverInfo(info));
        mModel.getDriverInfo(map)
                .compose(RxUtils.<Result<List<DriverInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DriverInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DriverInfo>> result) {
                        if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                            data.clear();
                            data.addAll(result.getData());
                            mRootView.notifyAdapter();
                        }
                    }

                });
    }
}
