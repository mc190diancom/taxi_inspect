package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.CaseBasicContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
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

import static android.app.Activity.RESULT_OK;


@ActivityScope
public class CaseBasicPresenter extends BasePresenter<CaseBasicContract.Model, CaseBasicContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    private String[] itemDistrict;
    private String[] itemCityAbbreviation = {"京", "沪", "津", "渝", "黑", "吉", "辽", "蒙", "冀", "新", "甘", "青", "陕", "宁", "豫", "鲁", "晋", "皖", "鄂", "湘", "苏", "川", "黔", "滇", "桂", "藏", "浙", "赣", "粤", "闽", "台", "琼", "港", "澳"};
    private Activity activity;
    private Map<String,Integer> updateMap = new HashMap<>();

    @Inject
    public CaseBasicPresenter(CaseBasicContract.Model model, CaseBasicContract.View rootView) {
        super(model, rootView);
    }


    public void init(Activity activity) {
        this.activity = activity;
        getDistrict();
    }

    /*
     * 区域选择
     */
    private void getDistrict() {
        mModel.getDistrictInfo("getAreaInfo")
                .compose(RxUtils.<Result<List<District>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<District>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<District>> result) {
                        if (result.ok()) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < result.getData().size(); i++) {
                                list.add(result.getData().get(i).getLBMC());
                            }
                            itemDistrict = UIUtils.listToArray(list);
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }

                });
    }

    /*
     * 展示时间控件
     */
    public void showDate(String time, final TextView tv) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        date.setTime(Long.valueOf(time));
        ;
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, "创建时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                tv.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }

    /*
     * 选择区域下拉框
     */
    public void showInspectDistrict(final TextView tvLawEnforcementDistrict) {
        if (itemDistrict == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getDistrict();
            return;
        }
        Windows.singleChoice(activity, "选择执法区域", itemDistrict, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvLawEnforcementDistrict.setText(itemDistrict[position]);
            }
        });
    }

    /*
     * 更新案件
     */
    public void updateCase(final Case c,final ArrayList<CaseStatus> followInstruments,final String currentInstrument) {
        Map<String, Object> map = new MapUtil().getMap("updateCase", BaseData.gson.toJson(c));
        mModel.updateCase(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            try {
                                upload(c);
                                if(followInstruments != null){
                                    setInstrumentsFlag(c.getID(),followInstruments,currentInstrument);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showMessage("生成id有误，请重试");
                            }
                        }
                        mRootView.showMessage(result.getMsg());
                    }

                });
    }

    private void setInstrumentsFlag(final String ZID,final ArrayList<CaseStatus> followInstruments,final String currentInstrument){
        StringBuilder blid = new StringBuilder();
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if(1 == followInstruments.get(i).getStatus()){
                blid.append(followInstruments.get(i).getId());
                if(i != followInstruments.size() -1){
                    blid.append(",");
                }
                isNeedUpdateFlag = true;
            }
        }
        if(!isNeedUpdateFlag){
            activity.finish();
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(ZID, blid.toString(),"1", currentInstrument,"1")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if(1 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 2);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            activity.setResult(RESULT_OK);
                            activity.finish();
                        }else{
                            showReuploadPhotoDialog(ZID,followInstruments,currentInstrument);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final String ZID,final ArrayList<CaseStatus> followInstruments,final String currentInstrument) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(ZID,followInstruments,currentInstrument);
                        }
                    }
                });
    }

    /*
     * 更新缓存中的数据
     */
    private void upload(Case c2) {
        Case c = CacheManager.getInstance().getCase();
        if (c == null) return;
        c.setJCQY(c2.getJCQY());
        c.setBJCR(c2.getBJCR());
        c.setVNAME(c2.getVNAME());
        c.setJCDD(c2.getJCDD());
        c.setLAT(c2.getLAT());
        c.setLON(c2.getLON());
        CacheManager.getInstance().putCase(c);
        EventBus.getDefault().post(true, Config.UPDATECASE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
