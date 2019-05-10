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
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.TalkNoticeContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
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
public class TalkNoticePresenter extends BasePresenter<TalkNoticeContract.Model, TalkNoticeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private String[] allAgencyAddress;
    private String[] allAgencyPhone;
    private Activity activity;
    private long sTime;
    private Map<String, Integer> updateMap = new HashMap<>();

    @Inject
    public TalkNoticePresenter(TalkNoticeContract.Model model, TalkNoticeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void init(Activity activity, EditText etCarLicense, EditText etLitigant, Case c, TextView tvSendTime, long sTime) {
        this.activity = activity;
        this.sTime = sTime;
        etCarLicense.setText(c.getVNAME());
        etLitigant.setText(c.getBJCR());

        Date eDate = new Date();
        eDate.setTime(sTime * 1000);
        tvSendTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        setListToArray(CacheManager.getInstance().getAgencyInfo());
        getInitInfo(c.getID());
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getThtzsById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<TalkNoticeQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<TalkNoticeQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<TalkNoticeQ>> result) {
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
     * 文书送达时间展示
     */
    public void showSendTime(TextView tvSendTime) {
        String time = TextUtils.isEmpty(tvSendTime.getText()) ? tvSendTime.getHint().toString() : tvSendTime.getText().toString();
        Date date = TimeTool.getYYHHmm(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, "文书送达时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showTime(date, false);
            }
        });
    }

    /*
     * 展示时间控件
     */
    private void showDate(final String title, String time, final boolean isStart) {

    }

    private void getAgencyInfos() {
        mModel.getAgencyInfos("getDdddAndDhList")
                .compose(RxUtils.<Result<List<AgencyInfo>>>applySchedulers(mRootView, false))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AgencyInfo>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<AgencyInfo>> listResult) {
                        if (listResult.ok()) {
                            setListToArray(listResult.getData());
                        }
                    }
                });
    }

    private void setListToArray(List<AgencyInfo> agencyInfos){
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

    public void addTalkNotice(final TalkNoticeQ talkNoticeQ) {
        Map<String, Object> map = new MapUtil().getMap("addThtzsInfo", RequestParamsUtil.getJsonEmptyStr(talkNoticeQ));
        submitData(map, talkNoticeQ, true, false, null);
    }

    public void updateTalkNotice(final TalkNoticeQ talkNoticeQ, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateThtzsInfo", RequestParamsUtil.getJsonEmptyStr(talkNoticeQ));
        submitData(map, talkNoticeQ, false, isChange, followInstruments);
    }

    private void submitData(Map<String, Object> map, final TalkNoticeQ talkNoticeQ, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        mModel.addTalkNotice(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            putCashService(talkNoticeQ);

                            JSONObject data = result.getData();
                            if (data != null) {
                                String id = data.optString("id");
                                mRootView.getID(id);
                            }

                            if (isAdd) {
                                updateMap.put(Config.STR_TALKNOTICE, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                                mRootView.startWebActivity(talkNoticeQ);
                            } else {
                                if (isChange && followInstruments != null) {
                                    setInstrumentsFlag(talkNoticeQ, followInstruments);
                                } else {
                                    mRootView.startWebActivity(talkNoticeQ);
                                }
                            }

                        }
                        mRootView.showMessage(result.getMsg());
                    }
                });
    }

    private void setInstrumentsFlag(final TalkNoticeQ talkNoticeQ, final ArrayList<CaseStatus> followInstruments) {
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.STR_TALKNOTICE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            mRootView.startWebActivity(talkNoticeQ);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(talkNoticeQ.getZID(), "", "1", Config.ID_TALKNOTICE, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if (Config.STR_TALKNOTICE.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(talkNoticeQ);
                        } else {
                            showReuploadPhotoDialog(talkNoticeQ, followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final TalkNoticeQ talkNoticeQ, final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(talkNoticeQ, followInstruments);
                        }
                    }
                });
    }

    /*
     * 文书创建或修改成功后，时间存缓存
     */
    private void putCashService(TalkNoticeQ tnq) {
        CacheManager.getInstance().putUTC(Config.T_TALKNOTICE, new UTC(Config.T_TALKNOTICE,
                TimeTool.yyyyMMdd_HHmmss.format(new Date(sTime * 1000)),
                TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(tnq.getQSSJ()) * 1000))));
    }

}
