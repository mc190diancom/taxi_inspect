package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.Html;
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
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.contract.LiveCheckRecordContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
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
public class LiveCheckRecordPresenter extends BasePresenter<LiveCheckRecordContract.Model, LiveCheckRecordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<VehicleInfo> data;

    @Inject
    ArrayList<DriverInfo> driverInfos;

    private Activity activity;
    private String[] itemTwo = {"小型轿车", "其他"};
    private String[] itemThree = {"强制措施1", "强制措施2"};
    private String[] itemFour = {"从事", "组织从事"};
    private String[] itemFive = {"巡游", "网络预约"};
    private String[] itemSix = {"第四条", "第五条"};
    private String[] itemCard = {"身份证", "监督卡"};

    private String[] itemDistrict;
    private String[] itemWFQX;

    private Case mCase;
    private long EndTime = Config.UTC_LIVERECORD;//现场检查笔录结束时间
    private long ZPDZEndTime;//询问笔录结束时间
    private Map<String, Integer> updateMap = new HashMap<>();
    private MyProgressDialog waiting;

    @Inject
    public LiveCheckRecordPresenter(LiveCheckRecordContract.Model model, LiveCheckRecordContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void init(Activity activity, LinearLayout llContent, TextView tvStartTime, TextView tvEndTime, EditText etCarLicense, TextView tvInspectDistrict, TextView tvInspectAddress,
                     TextView etInspectName, TextView tv_step_industry, TextView tv_step_type, EditText etRecordContent, Case mCase) {
        this.activity = activity;
        this.mCase = mCase;
        initDefaultTime(tvStartTime, tvEndTime);

        etCarLicense.setText(mCase.getVNAME());
        tvInspectDistrict.setText(mCase.getJCQY());
        tvInspectAddress.setText(mCase.getJCDD());
        etInspectName.setText(mCase.getBJCR());
        String content = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + ",当事人" + mCase.getBJCR()
                + "驾驶" + mCase.getVNAME() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人";
        etRecordContent.setText(content);
        if (mCase.getHYLB().contains("非法经营出租汽车")) {
            tv_step_industry.setText(itemFive[0]);
            tv_step_type.setText(itemSix[0]);
            llContent.setVisibility(View.VISIBLE);
        } else {
            llContent.setVisibility(View.GONE);
        }

        setListToArray(CacheManager.getInstance().getDistrict());
        getInitInfo(mCase.getID());
    }

    private void setListToArray(List<District> districts) {
        if (districts == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < districts.size(); i++) {
            list.add(districts.get(i).getLBMC());
        }
        itemDistrict = UIUtils.listToArray(list);
    }

    private void initDefaultTime(TextView tvStartTime, TextView tvEndTime) {
        UTC utc = CacheManager.getInstance().getUTC(Config.T_ZPDZ);

        if (utc == null || TextUtils.isEmpty(utc.getStartUTC()) || TextUtils.isEmpty(utc.getEndUTC())
                || "无".equals(utc.getStartUTC()) || "无".equals(utc.getEndUTC())) {
            //没有创建询问笔录，根据案件创建时间来设置现场检查笔录的默认起止时间
            Date sDate = new Date();
            Date eDate = new Date();
            sDate.setTime((Long.valueOf(mCase.getCREATEUTC())) * 1000);
            tvStartTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
            eDate.setTime(sDate.getTime() + Config.UTC_LIVERECORD * 1000);
            tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));
            EndTime = Config.UTC_LIVERECORD;
        } else {
            //已创建询问笔录，现场检查笔录的起止时间则需按照询问笔录的时间来确定
            //询问笔录开始时间
            long start = TimeTool.parseDate(utc.getStartUTC()).getTime() / 1000;
            //询问笔录结束时间
            long end = TimeTool.parseDate(utc.getEndUTC()).getTime() / 1000;

            //现场检查笔录的默认开始时间为询问笔录的前一分钟
            tvStartTime.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(start * 1000 - 60 * 1000)));

            //如果询问笔录的时间大于11分钟，则按照询问笔录的结束时间来设置现场检查笔录的结束时间，否则现场检查笔录的结束时间默认为11分钟以后
            if (end - start >= Config.UTC_LIVERECORD - 60) {
                tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(end * 1000 + 60 * 1000)));
                ZPDZEndTime = end * 1000 + 60 * 1000;
                EndTime = end - start + (2 * 60);//这里算的是开始时间和结束时间比较；由于开始时间是询问笔录前一分钟，结束时间是询问笔录结束时间后一分钟
            } else {
                tvEndTime.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(start * 1000 + (Config.UTC_LIVERECORD - 60) * 1000)));
                EndTime = Config.UTC_LIVERECORD;
            }
        }

    }

    public long getZPDZEndTime() {
        return ZPDZEndTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void verifyTime(final String startTimeStr, final String endTimeStr) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", mCase.getID());
        Map<String, Object> map = new MapUtil().getMap("selectblTimesListByCaseId", BaseData.gson.toJson(params));

        waiting = Windows.waiting(activity);

        mModel.getBlTime(map)
                .compose(RxUtils.<Result<List<UTC>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<UTC>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<UTC>> listResult) {
                        hideWaiting();

                        if (listResult.ok()) {
                            List<UTC> data = listResult.getData();

                            if (data != null && !data.isEmpty()) {
                                for (UTC utc : data) {
                                    if (utc != null && Config.T_ZPDZ.equals(utc.getName())) {
                                        if (!"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())
                                                && !TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())) {
                                            CacheManager.getInstance().putUTC(Config.T_ZPDZ, utc);
                                        }
                                    }
                                }
                            }

                            mRootView.verifyTimeFinish(checkTime(startTimeStr, endTimeStr));
                        } else {
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });

    }

    private boolean checkTime(String startTimeStr, String endTimeStr) {
        long startTime = TimeTool.parseDate(startTimeStr).getTime() / 1000;
        long endTime = TimeTool.parseDate(endTimeStr).getTime() / 1000;

        if (endTime <= startTime) {
            DialogUtil.showTipDialog(activity, "结束时间不能在开始时间之前");
            return false;
        }

        if (endTime - startTime < Config.UTC_LIVERECORD) {
            DialogUtil.showTipDialog(activity, "现场检查的时间不能低于11分钟");
            return false;
        }

        Case c = CacheManager.getInstance().getCase();

        if (c != null && !TextUtils.isEmpty(c.getCREATEUTC())) {
            long caseCreateTime = Long.valueOf(c.getCREATEUTC());
            if (startTime < caseCreateTime) {
                DialogUtil.showTipDialog(activity, "现场检查笔录开始时间不能小于案件创建时间");
                return false;
            }
        }

        UTC utc = CacheManager.getInstance().getUTC(Config.T_ZPDZ);
        if (utc != null && !TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())
                && !"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())) {
            //询问笔录的开始时间(单位：秒)
            long inquiryRecordStartTime = TimeTool.parseDate(utc.getStartUTC()).getTime() / 1000;
            //询问笔录的结束时间(单位：秒)
            long inquiryRecordEndTime = TimeTool.parseDate(utc.getEndUTC()).getTime() / 1000;

            if (startTime >= inquiryRecordStartTime) {
                DialogUtil.showTipDialog(activity, "现场检查笔录开始时间必须在询问笔录开始时间之前");
                return false;
            }

            if (endTime <= inquiryRecordEndTime) {
                DialogUtil.showTipDialog(activity, "现场检查笔录结束时间必须在询问笔录结束时间之后");
                return false;
            }
        }

        return true;
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
    private void showDate(String time, final String title, final boolean isStart) {
        Date date = new Date();
        if (!TextUtils.isEmpty(time)) {
            date = TimeTool.getYYHHmm(time);
        }
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
     * 选择区域下拉框
     */
    public void showInspectDistrict(final TextView tvInspectDistrict) {
        if (itemDistrict == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getDistrict();
            return;
        }
        Windows.singleChoice(activity, "选择执法区域", itemDistrict, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvInspectDistrict.setText(itemDistrict[position]);
            }
        });
    }

    /*
     * 车型选择
     */
    public void showCarType(final TextView tvCarTypeSelect, final EditText etCarType) {
        Windows.singleChoice(activity, "选择车型", itemTwo, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvCarTypeSelect.setText(itemTwo[position]);
                if (0 == position) {
                    etCarType.setEnabled(false);
                    etCarType.setHint("");
                    etCarType.setText("");
                } else {
                    etCarType.setEnabled(true);
                    etCarType.setHint(activity.getString(R.string.please_input_your_car_model));
                }

            }
        });
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getXcjcblInfoByZid", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<LiveCheckRecordQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveCheckRecordQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveCheckRecordQ>> result) {
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
                                mRootView.onGetVehicleInfo(result.getData().get(0));
                            }
                        }
                    }

                });
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
                            setListToArray(result.getData());
                        }
                    }

                });
    }

    /*
     * 选择措施类型
     */
    public void showCompelWindow(final TextView tvCompelStep, final LinearLayout ll, final TextView tv2) {
        Windows.singleChoice(activity, "选择措施类型", itemThree, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvCompelStep.setText(itemThree[position]);
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

    /*
     * 选择卡类别下拉框
     */
    String sfzh, jdkh;

    public void showCard(final TextView tvCard, final EditText et) {
        if ("身份证".equals(tvCard.getText().toString())) {
            sfzh = et.getText().toString();
        } else {
            jdkh = et.getText().toString();
        }
        Windows.singleChoice(activity, "选择证件类型", itemCard, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvCard.setText(itemCard[position]);
                if (0 == position) {
                    et.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.idCardRegex, 18));
                    et.setHint("请输入身份证号");
                    et.setText(sfzh);
                } else {
                    et.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 18));
                    et.setHint("请输入监督卡号");
                    et.setText(jdkh);
                }
            }
        });
    }

    public void showCardData(String sfzh, String jdkh) {
        this.sfzh = sfzh;
        this.jdkh = jdkh;
    }

    /*
     * 展示违法情形的弹窗
     */
    public void getIllegalSituationWindow(final JCItem mJcitem, final TextView tvIllegalSituation, final EditText tvRecord) {
        if (mJcitem == null) {
            UIUtils.toast(activity, "请先选择违法行为", Toast.LENGTH_SHORT);
            return;
        }
        if (itemWFQX == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getIllegalSituation(mJcitem.getZDLBID());
            return;
        }
        Windows.singleChoice(activity, "选择违法情形", itemWFQX, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                String record = "";
                if (!TextUtils.isEmpty(tvRecord.getText().toString()) && "无".equals(itemWFQX[position])) {
                    record = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + mCase.getBJCR()
                            + "驾驶" + mCase.getVNAME() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人"
                            + "其行为涉嫌构成"
                            + "<font color= '#4876FF'>" + mJcitem.getLBMC() + "</font>";
                } else if (!TextUtils.isEmpty(tvRecord.getText().toString())) {
                    record = TimeTool.formatStdChinese(Long.valueOf(mCase.getCREATEUTC()) * 1000) + "，当事人" + mCase.getBJCR()
                            + "驾驶" + mCase.getVNAME() + "号车行驶至" + mCase.getJCQY() + mCase.getJCDD() + "被检查人员示证检查，经查:当事人"
                            + "<font color= '#FF4500'>" + itemWFQX[position] + "</font>" + "其行为涉嫌构成"
                            + "<font color= '#4876FF'>" + mJcitem.getLBMC() + "</font>";
                }
                tvIllegalSituation.setText(itemWFQX[position]);
                tvRecord.setText(Html.fromHtml(record));
            }
        });
    }

    public void setIllegalSituation(List<String> illegalSituationList) {
        this.itemWFQX = UIUtils.listToArray(illegalSituationList);
    }

    /*
     * 通过违法行为id获取违法情形
     */
    public void getIllegalSituation(String jcItem) {
        itemWFQX = null;
        Map<String, Object> map = new MapUtil().getMap("getJcxHyRyFgXq", RequestParamsUtil.RequestWFQXInfo(jcItem));
        mModel.getJcxHyRyFgXq(map)
                .compose(RxUtils.<Result<List<FaGuiDetail>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<FaGuiDetail>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<FaGuiDetail>> result) {
                        if (result.ok()) {
                            List<String> list = new ArrayList<>();
                            list.add("无");
                            for (int i = 0; i < result.getData().size(); i++) {
                                list.add(result.getData().get(i).getXMMC());
                            }
                            itemWFQX = UIUtils.listToArray(list);
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }

                });
    }

    /*
     * 现场检查笔录信息保存
     */
    public void SubmitData(LiveCheckRecordQ lcrq, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("addXcjcblLjInfo", RequestParamsUtil.RequestLiveRecordInfo(lcrq));
        submitData(map, lcrq, true, false, followInstruments);
    }

    /*
     * 现场检查笔录信息修改
     */
    public void UpdateData(LiveCheckRecordQ lcrq, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateXcjcblInfo", RequestParamsUtil.RequestLiveRecordInfo(lcrq));
        submitData(map, lcrq, false, isChange, followInstruments);
    }

    private String WFQX = "", WFNR = "", SEX = "";

    public void setEffectParam(String WFQX, String WFNR, String SEX) {
        this.WFQX = TextUtils.isEmpty(WFQX) ? "" : WFQX;
        this.WFNR = TextUtils.isEmpty(WFNR) ? "" : WFNR;
        this.SEX = TextUtils.isEmpty(SEX) ? "" : SEX;
    }


    private void submitData(Map<String, Object> map, final LiveCheckRecordQ lcrq, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        updateMap.clear();

        if (waiting == null) {
            waiting = Windows.waiting(activity);
        } else {
            if (!waiting.isShowing()) {
                waiting.show();
            }
        }

        mModel.submitLiveRecordInfo(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            putCashService(lcrq);
                            String id = result.getData().optString("id");
                            mRootView.getID(id);

                            if (isAdd) {
                                updateMap.put(Config.STR_LIVERECORD, 1);
                                EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            }
                            if ((isAdd || isChange) && followInstruments != null) {
                                setInstrumentsFlag(lcrq, followInstruments);
                            } else if (followInstruments != null && followInstruments.size() > 0 && ((!WFQX.equals(lcrq.getWFQX())) || !WFNR.equals(lcrq.getQZCSSXGZ()) || !SEX.equals(lcrq.getSEX()))) {
                                boolean isEffct = false;
                                ArrayList<CaseStatus> list = new ArrayList<>();
                                for (int i = 0; i < followInstruments.size(); i++) {
                                    if (Config.STR_TALKNOTICE.equals(followInstruments.get(i).getBlType()) && !WFQX.equals(lcrq.getWFQX())) {
                                        list.add(followInstruments.get(i));
                                        isEffct = true;
                                    }
                                    if (Config.STR_CARDECIDE.equals(followInstruments.get(i).getBlType()) && !WFNR.equals(lcrq.getQZCSSXGZ())) {
                                        list.add(followInstruments.get(i));
                                        isEffct = true;
                                    }
                                    if (Config.STR_ADMINISTRATIVE.equals(followInstruments.get(i).getBlType()) && !SEX.equals(lcrq.getSEX())) {
                                        list.add(followInstruments.get(i));
                                        isEffct = true;
                                    }
                                }
                                if (!isEffct) {
                                    hideWaiting();
                                    mRootView.startWebActivity(lcrq);
                                } else {
                                    setInstrumentsFlag(lcrq, list);
                                }
                            } else {
                                hideWaiting();
                                mRootView.startWebActivity(lcrq);
                            }
                        } else {
                            hideWaiting();
                        }
                        mRootView.showMessage(result.getMsg());
                    }

                });
    }

    private void setInstrumentsFlag(final LiveCheckRecordQ lcrq, final ArrayList<CaseStatus> followInstruments) {
        StringBuilder blid = new StringBuilder();
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (!Config.STR_LIVERECORD.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) {
                blid.append(followInstruments.get(i).getId());
                if (i != followInstruments.size() - 1) {
                    blid.append(",");
                }
                isNeedUpdateFlag = true;
            } else if (Config.STR_LIVERECORD.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            hideWaiting();
            mRootView.startWebActivity(lcrq);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(lcrq.getZID(), blid.toString(), "1", Config.ID_LIVERECORD, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if (!Config.STR_LIVERECORD.equals(followInstruments.get(i).getBlType()) && 1 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 2);
                                    CacheManager.getInstance().updateUTC(followInstruments.get(i).getBlType(), true);
                                } else if (Config.STR_LIVERECORD.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            hideWaiting();
                            mRootView.startWebActivity(lcrq);
                        } else {
                            hideWaiting();
                            showReuploadPhotoDialog(lcrq, followInstruments);
                        }
                    }

                });
    }

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    private void showReuploadPhotoDialog(final LiveCheckRecordQ lcrq, final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            if (!waiting.isShowing()) {
                                waiting = Windows.waiting(activity);
                            }
                            setInstrumentsFlag(lcrq, followInstruments);
                        }
                    }
                });
    }

    /*
     * 文书创建或修改成功后，把时间和现场检查笔录部分信息保存到缓存
     */
    private void putCashService(LiveCheckRecordQ lcrq) {
        CacheManager.getInstance().putUTC(Config.T_LIVERECORD, new UTC(Config.T_LIVERECORD,
                TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(lcrq.getJCSJ1()) * 1000)),
                TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(lcrq.getJCSJ2()) * 1000))));
        CacheManager.getInstance().putJDKH(lcrq.getCYZGZ());
        CacheManager.getInstance().putLiveCheckRecord(lcrq);
    }

    /**
     * 根据车牌号处查询车辆信息
     *
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
                            if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                                data.clear();
                                data.addAll(result.getData());
                                mRootView.notifyAdapter(true);
                            }
                        }
                    }

                });
    }

    /**
     * 根据身份证或者监督卡号获取当事人具体信息
     *
     * @param cardType 卡类型
     * @param cardNum  身份证&监督卡
     */
    public void getDriverInfoByCard(String cardType, String cardNum) {
        DriverInfo info = new DriverInfo();
        if ("身份证".equals(cardType)) {
            info.setId(cardNum);
        } else {
            info.setJianduNumber(cardNum);
        }
        info.setStartIndex(0);
        info.setEndIndex(10);
        Map<String, Object> map = new MapUtil().getMap("queryDriverInfo", RequestParamsUtil.RequestDriverInfo(info));
        mModel.getDriverInfo(map)
                .compose(RxUtils.<Result<List<DriverInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DriverInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DriverInfo>> result) {
                        if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                            driverInfos.clear();
                            driverInfos.addAll(result.getData());
                            mRootView.notifyAdapter(false);
                        }
                    }

                });
    }
}
