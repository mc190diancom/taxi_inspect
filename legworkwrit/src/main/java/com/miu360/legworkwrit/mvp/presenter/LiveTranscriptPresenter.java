package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.contract.LiveTranscriptContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
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
public class LiveTranscriptPresenter extends BasePresenter<LiveTranscriptContract.Model, LiveTranscriptContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private Activity activity;

    private String[] items = {"其他", "当事人在场对车辆内饰和外观情况进行了核对确认，并取走车内财物。"};
    private String[] itemDocument = {"扣押车辆决定书", "扣押（查封）物品决定书"};
    private Map<String, Integer> updateMap = new HashMap<>();

    @Inject
    public LiveTranscriptPresenter(LiveTranscriptContract.Model model, LiveTranscriptContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    private long time;

    public void init(Activity activity, TextView tvStartTime, TextView tvEndTime, EditText etInspect, TextView tvImplementAddress, Case mCase) {
        this.activity = activity;
        Date sDate = new Date();
        Date eDate = new Date();
        time = GetUTCUtil.LiveTranScript(mCase);
        sDate.setTime(time * 1000);
        eDate.setTime(time * 1000 + Config.UTC_LIVETRANSCRIPT * 1000);
        tvStartTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
        tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        etInspect.setText(mCase.getBJCR());
        tvImplementAddress.setText(mCase.getJCDD());

        getInitInfo(mCase.getID());
    }

    /**
     * 获取上个文书的结束时间
     */
    public long lastEndTime() {
        return time * 1000;
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getXcblById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<LiveTranscript>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveTranscript>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveTranscript>> result) {
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
     * 开始时间选择
     */
    public void startCalendar(String time, boolean isUpdate) {
        showDate(time, "开始时间", true);
    }

    /*
     * 结束时间选择
     */
    public void endCalendar(TextView tvStartTime, String time, boolean isUpdate) {
        if (TextUtils.isEmpty(tvStartTime.getText())) {
            UIUtils.toast(activity, "请先确定开始时间", Toast.LENGTH_SHORT);
            return;
        }
        showDate(time, "结束时间", false);
    }

    /*
     * 展示时间控件
     */
    public void showDate(String time, final String title, final boolean isStart) {
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
     * 选择措施类型
     */
    public void showCompelWindow(final TextView tvCompelStep, final EditText etContent) {
        Windows.singleChoice(activity, "选择措施类型", items, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvCompelStep.setText(items[position]);
                if (0 == position) {
                    etContent.setVisibility(View.VISIBLE);
                } else {
                    etContent.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
     * 选择文书类型
     */
    public void selectDocument(final TextView tvSelectDocuments) {
        Windows.singleChoice(activity, "选择宣读文书", itemDocument, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvSelectDocuments.setText(itemDocument[position]);
            }
        });
    }

    public void submitLiveData(LiveTranscript lt, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("addXcblInfo", RequestParamsUtil.RequestLiveInfo(lt));
        submitData(lt, map, true, false, followInstruments);
    }

    public void UpdateData(LiveTranscript lt, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateXcblInfo", RequestParamsUtil.RequestLiveInfo(lt));
        submitData(lt, map, false, isChange, followInstruments);
    }

    private void submitData(final LiveTranscript liveTranscript, Map<String, Object> map, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        mModel.submitLiveData(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            putCashService(liveTranscript);

                            JSONObject data = result.getData();
                            if (data != null) {
                                String id = data.optString("id");
                                mRootView.getID(id);
                                liveTranscript.setID(id);
                            }

                            if (isAdd) {
                                updateMap.put(Config.STR_LIVETRANSCRIPT, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            }
                            if ((isAdd || isChange) && followInstruments != null) {
                                setInstrumentsFlag(liveTranscript, followInstruments);
                            } else {
                                mRootView.startWebActivity(liveTranscript);
                            }
                        }
                        mRootView.showMessage(result.getMsg());
                    }

                });
    }

    private void setInstrumentsFlag(final LiveTranscript liveTranscript,final ArrayList<CaseStatus> followInstruments){
        StringBuilder blid = new StringBuilder();
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if(Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()){
                blid.append(followInstruments.get(i).getId());
                if(i != followInstruments.size() -1){
                    blid.append(",");
                }
                isNeedUpdateFlag = true;
            }else if(Config.STR_LIVETRANSCRIPT.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()){
                isNeedUpdateFlag = true;
            }
        }
        if(!isNeedUpdateFlag){
            mRootView.startWebActivity(liveTranscript);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(liveTranscript.getZID(), blid.toString(),"1", Config.ID_LIVETRANSCRIPT,"0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if(Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 2);
                                    CacheManager.getInstance().updateUTC(followInstruments.get(i).getBlType(),true);
                                }else if(Config.STR_LIVETRANSCRIPT.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()){
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(liveTranscript);
                        }else{
                            showReuploadPhotoDialog(liveTranscript,followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final LiveTranscript liveTranscript,final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(liveTranscript,followInstruments);
                        }
                    }
                });
    }

    private void putCashService(LiveTranscript liveTranscript) {
        CacheManager.getInstance().putUTC(Config.T_LIVETRANSCRIPT, new UTC(Config.T_LIVETRANSCRIPT
                , TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(liveTranscript.getSSSJ1()) * 1000))
                , TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(liveTranscript.getSSSJ2()) * 1000))));
    }

}
