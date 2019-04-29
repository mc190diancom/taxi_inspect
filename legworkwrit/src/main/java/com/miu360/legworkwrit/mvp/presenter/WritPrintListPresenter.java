package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.data.UserPreference;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.service.GeneralInformationService;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.WritPrintListContract;
import com.miu360.legworkwrit.mvp.data.WifiPreference;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.PrintTimes;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;
import com.miu360.legworkwrit.mvp.model.entity.WifiRequestParams;
import com.miu360.legworkwrit.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class WritPrintListPresenter extends BasePresenter<WritPrintListContract.Model, WritPrintListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<String> data;

    private Case c;
    private ArrayList<BlType> types;
    private Activity activity;

    @Inject
    public WritPrintListPresenter(WritPrintListContract.Model model, WritPrintListContract.View rootView) {
        super(model, rootView);
    }

    private void getTimes(ArrayList<BlType> types) {
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        for (int i = 0; i < types.size(); i++) {
            String printTimes = TextUtils.isEmpty(types.get(i).getPrintTimes()) ? "0" : types.get(i).getPrintTimes();
            if (Config.T_LIVERECORD.equals(types.get(i).getTABLENAME())) {
                data.set(0, printTimes);
                continue;
            }
            if (Config.T_FRISTREGISTER.equals(types.get(i).getTABLENAME())) {
                data.set(1, printTimes);
                continue;
            }
            if (Config.T_CARFORM.equals(types.get(i).getTABLENAME())) {
                data.set(2, printTimes);
                continue;
            }
            if (Config.T_LIVETRANSCRIPT.equals(types.get(i).getTABLENAME())) {
                data.set(3, printTimes);
                continue;
            }
            if (Config.T_TALKNOTICE.equals(types.get(i).getTABLENAME())) {
                data.set(4, printTimes);
                continue;
            }
            if (Config.T_CARDECIDE.equals(types.get(i).getTABLENAME())) {
                data.set(5, printTimes);
                continue;
            }
            if (Config.T_ADMINISTRATIVE.equals(types.get(i).getTABLENAME())) {
                data.set(6, printTimes);
            }
        }
        mRootView.onGetTimes(data);
    }

    public void init(Activity activity, Case c, ArrayList<BlType> types) {
        this.c = c;
        this.types = types;
        this.activity = activity;

        getTimes(types);
        getAllPrinterWifiConfig();
//        getPrinterWifiConfigMsg();
    }

    public void prepareModifyPrintTimes(String instrumentType, String times, String instrumentId) {
        Log.d(TAG, "准备修改打印次数....instrumentType = " + instrumentType + "....times = " + times + "....instrumentId = " + instrumentId);
        if (TextUtils.isEmpty(instrumentId)) {
            return;
        }

        if (types != null) {
            for (BlType type : types) {
                if (instrumentType.equals(type.getTABLENAME())) {
                    String oldPrintTimes = type.getPrintTimes();

                    try {
                        int oldPrintTimesInt = Integer.valueOf(oldPrintTimes);
                        int currentPrintTimesInt = Integer.valueOf(times);
                        int total = oldPrintTimesInt + currentPrintTimesInt;

                        mRootView.modifyPrintTimesSuccess(type.getTABLENAME(), String.valueOf(total));

                        final PrintTimes printTimes = new PrintTimes();
                        printTimes.setID(instrumentId);
                        printTimes.setTABLENAME(type.getTABLENAME());
                        printTimes.setPRINTTIMES(String.valueOf(total));
                        startModifyPrintTimesService(printTimes);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void startModifyPrintTimesService(PrintTimes printTimes) {
        Intent intent = new Intent(activity, GeneralInformationService.class);
        intent.putExtra("Flag", Config.SERVICE_PRINT);
        intent.putExtra("PrintTimes", printTimes);
        activity.startService(intent);
    }

    /**
     * 获取打印机wifi配置信息
     */
    private void getPrinterWifiConfigMsg() {
        final UserPreference user = new UserPreference(MiuBaseApp.self);
        String userName = user.getString("user_name", "");
        if ("0000000".equals(userName)) {
            userName = "00001";
        }
        Map<String, Object> params = new MapUtil().getMap("selectWifiByZfzh"
                , BaseData.gson.toJson(new WifiRequestParams(userName)));
        mModel.getPrinterWifiConfigMsg(params)
                .compose(RxUtils.<Result<List<WifiConfig>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<WifiConfig>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<WifiConfig>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
//                                user.setString("wifi_ssid", result.getData().get(0).getSsid());
//                                user.setString("wifi_password", result.getData().get(0).getPassword());
                            }
                        }
                    }
                });
    }

    /**
     * 获取所有配置的wifi信息
     */
    private void getAllPrinterWifiConfig() {
        mModel.getAllPrinterWifiConfig("selectWifiAll")
                .compose(RxUtils.<Result<List<WifiConfig>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<WifiConfig>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(final Result<List<WifiConfig>> listResult) throws Exception {
                        if (listResult.ok()) {
                            WifiPreference wifiPreference = new WifiPreference(MiuBaseApp.self);
                            wifiPreference.clearPreference();
                            List<WifiConfig> data = listResult.getData();

                            if (data != null && !data.isEmpty()) {
                                for (WifiConfig config : data) {
                                    wifiPreference.putWifi(config);
                                }
                            }
                        }
                    }
                });
    }

    private boolean isConfirm(String type, ArrayList<BlType> types) {
        boolean isC = false;
        for (int i = 0; i < types.size(); i++) {
            if (type.equals(types.get(i).getCOLUMNNAME()) && types.get(i).getFLAG() == 2) {
                isC = true;
                break;
            }
        }
        if (isC) {
            UIUtils.toast(activity, "待确认文书不能进行打印", Toast.LENGTH_SHORT);
        }
        return isC;
    }

    /*
     * 根据案件id查询谈话通知书
     */
    public void getTalkNotice() {
        if (isConfirm(Config.TALKNOTICE, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getThtzsById", BaseData.gson.toJson(params));
        mModel.getTalkNoticeById(map)
                .compose(RxUtils.<Result<List<TalkNoticeQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<TalkNoticeQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<TalkNoticeQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                TalkNoticeQ talkNoticeQ = result.getData().get(0);
                                talkNoticeQ.setZFRY1(c.getZFRYNAME1());
                                talkNoticeQ.setZFRY2(c.getZFRYNAME2());
                                talkNoticeQ.setZFZH1(c.getZFZH1());
                                talkNoticeQ.setZFZH2(c.getZHZH2());
                                mRootView.getTalkNoticeSuccess(talkNoticeQ);
                            } else {
                                mRootView.showMessage("该案件未填写谈话通知书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询现场检查笔录
     */
    public void getLiveCheckRecord() {
        if (isConfirm(Config.LIVERECORD, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXcjcblInfoByZid", BaseData.gson.toJson(params));
        mModel.getLiveCheckRecordById(map)
                .compose(RxUtils.<Result<List<LiveCheckRecordQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveCheckRecordQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveCheckRecordQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getLiveCheckRecordSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写现场检查笔录");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询行政处罚决定书
     */
    public void getAdministrativePenalty() {
        if (isConfirm(Config.ADMINISTRATIVE, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXzcfjdsInfoById", BaseData.gson.toJson(params));

        mModel.getAdministrativePenaltyById(map)
                .compose(RxUtils.<Result<List<AdministrativePenalty>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AdministrativePenalty>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<AdministrativePenalty>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getAdministrativePenaltySuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写行政处罚决定书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询先行登记通知书
     */
    public void getFristRegister() {
        if (isConfirm(Config.FRISTREGISTER, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getDjbctzsInfoById", BaseData.gson.toJson(params));

        mModel.getFristRegisterById(map)
                .compose(RxUtils.<Result<List<FristRegisterQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<FristRegisterQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<FristRegisterQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getFristRegisterSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写先行登记通知书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 通过案件id查询车辆交接单
     */
    public void getDetainCarForm() {
        if (isConfirm(Config.CARFORM, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getKycljjdInfoById", BaseData.gson.toJson(params));

        mModel.getDetainCarFormById(map)
                .compose(RxUtils.<Result<List<DetainCarFormQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarFormQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarFormQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getDetainCarFormSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写车辆交接单");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询扣押车辆决定书
     */
    public void getDetainCarDecide() {
        if (isConfirm(Config.CARDECIDE, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getKycljdsInfoById", BaseData.gson.toJson(params));

        mModel.getDetainCarDecideById(map)
                .compose(RxUtils.<Result<List<DetainCarDecideQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarDecideQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarDecideQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getDetainCarDecideSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写车辆决定书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询现场笔录
     */
    public void getLiveTranscript() {
        if (isConfirm(Config.LIVETRANSCRIPT, types)) return;
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXcblById", BaseData.gson.toJson(params));

        mModel.getLiveTranscriptById(map)
                .compose(RxUtils.<Result<List<LiveTranscript>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveTranscript>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveTranscript>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getLiveTranscriptSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写现场笔录");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
