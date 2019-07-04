package com.miu360.legworkwrit.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.FileUtil;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.contract.DetainCarFormContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AccordRule;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarForm;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormID;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu30.common.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class DetainCarFormPresenter extends BasePresenter<DetainCarFormContract.Model, DetainCarFormContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<VehicleInfo> data;

    private String[] allCount = new String[]{"0", "1", "2", "3", "4", "5"};
    private String[] allPark;
    private String[] photoSource = new String[]{"拍照", "从相册选取"};
    private String[] allAccordRule;

    private File takePhotoFile;
    private Activity activity;
    private Date sTime;//文书开始时间
    private Map<String, Integer> updateMap = new HashMap<>();
    private String hylb;

    @Inject
    public DetainCarFormPresenter(DetainCarFormContract.Model model, DetainCarFormContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void init(Activity activity, Case mCase, TextView tvDeducationTime, TextView tvEscortTime, EditText etCarLicense, TextView etDriverName, TextView tvDeducationAddress) {
        this.activity = activity;
        hylb = mCase.getHYLB();
        Date sDate = new Date();
        Date eDate = new Date();
        sDate.setTime(GetUTCUtil.DetainCarForm(mCase) * 1000);
        sTime = sDate;
        tvDeducationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(sDate));
        eDate.setTime(sDate.getTime() + Config.UTC_CARFORM * 1000);
        tvEscortTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));

        etCarLicense.setText(mCase.getVNAME());
        etDriverName.setText(mCase.getBJCR());
        tvDeducationAddress.setText(mCase.getJCDD());

        setListToArray(CacheManager.getInstance().getParks());
        getAccordRules();
        getInitInfo(mCase.getID());
    }

    //上个文书结束时间加了1分钟(实际上是这个文书默认开始时间)
    public long getLastEndTime() {
        return sTime.getTime();
    }

    /*
     * 根据开始时间默认的一个结束时间
     */
    public Date getEndTime() {
        Date eDate = new Date();
        eDate.setTime(sTime.getTime() + Config.UTC_CARFORM * 1000);
        return eDate;
    }

    /*
     * 根据案件id，获取这个界面是否已经保存过；保存过，数据展示在界面上
     */
    private void getInitInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", id);
        Map<String, Object> map = new MapUtil().getMap("getKycljjdInfoById", BaseData.gson.toJson(params));
        mModel.getInitInfo(map)
                .compose(RxUtils.<Result<List<DetainCarFormQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarFormQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarFormQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.showResult(result.getData().get(0), Config.RESULT_SUCCESS);

                                List<String> photoIDs = new ArrayList<>(6);
                                for (DetainCarFormQ detainCarFormQ : result.getData()) {
                                    photoIDs.add(detainCarFormQ.getTPID() + "," + detainCarFormQ.getZPSM());
                                }

                                mRootView.showPhotos(photoIDs);
                            } else {
                                mRootView.showResult(null, Config.RESULT_EMPTY);
                            }
                        } else {
                            mRootView.showResult(null, Config.RESULT_ERROR);
                        }
                    }

                });
    }

    public void chooseCount(Activity activity, final TextView textView) {
        Windows.singleChoice(activity, "请选择数量", allCount, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                textView.setText(allCount[position]);
            }
        });
    }

    /*
     * 扣车时间选择
     */
    public void startCalendar(TextView textView, boolean isUpdate) {
        String time = TextUtils.isEmpty(textView.getText()) ? textView.getHint().toString() : textView.getText().toString();
        chooseTime(time, "扣车时间", true);
    }

    /*
     * 押送时间选择
     */
    public void endCalendar(TextView tvStartTime, TextView tvEndTime, boolean isUpdate) {
        if (TextUtils.isEmpty(tvStartTime.getText())) {
            UIUtils.toast(activity, "请先确定扣车时间", Toast.LENGTH_SHORT);
            return;
        }
        String time = TextUtils.isEmpty(tvEndTime.getText()) ? tvEndTime.getHint().toString() : tvEndTime.getText().toString();
        chooseTime(time, "押送时间", false);
    }

    public void chooseTime(String time, String title, final boolean isStart) {
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
     * 获取停车场列表
     */
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

    private void setListToArray(List<Park> parks) {
        if (parks == null) {
            return;
        }
        List<String> allParkName = new ArrayList<>(parks.size());

        for (Park park : parks) {
            allParkName.add(park.getName());
        }

        allPark = UIUtils.listToArray(allParkName);
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

    public void showParkList(Activity activity, final TextView textView) {
        if (allPark == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getParkList();
            return;
        }

        Windows.singleChoice(activity, "选择检查区域", allPark, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                textView.setText(allPark[position]);
            }
        });
    }

    public File getTakePhotoFile() {
        return takePhotoFile;
    }

    public void getPhoto(final Activity activity) {
        Windows.singleChoice(activity, "请选择照片来源", photoSource, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                if (position == 0) {
                    takingPic(activity);
                } else {
                    choosePic(activity);
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    public void choosePic(final Activity activity) {
        new RxPermissions(activity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent intent = new Intent(Intent.ACTION_PICK
                                    , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            if (intent.resolveActivity(MiuBaseApp.self.getPackageManager()) != null) {
                                activity.startActivityForResult(intent, 1002);
                            }
                        } else {
                            mRootView.showMessage("权限被拒绝,无法选取照片");
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void takingPic(final Activity activity) {
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            takePhotoFile = new File(FileUtil.getDir(Environment.getExternalStorageDirectory(), "taxi_inspect")
                                    , System.currentTimeMillis() + ".png");

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (intent.resolveActivity(MiuBaseApp.self.getPackageManager()) != null) {
                                Uri uri = FileUtil.getFileUri(activity.getApplicationContext(), takePhotoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                activity.startActivityForResult(intent, 1001);
                            }
                        } else {
                            mRootView.showMessage("权限被拒绝,无法进行拍照");
                        }
                    }
                });
    }

    private Dialog uploadLoading;

    private void hideUploadLoading() {
        if (uploadLoading != null && uploadLoading.isShowing()) {
            uploadLoading.dismiss();
        }
    }

    public void uploadPhotos(List<File> photos, final DetainCarForm form) {
        MapUtil mapUtil = new MapUtil();
        Map<String, Object> paramsMap = mapUtil.getMap("upLoadKycljjdPhoto", BaseData.gson.toJson(new DetainCarFormID(form.getId())));
        uploadLoading = DialogUtil.showUploadDialog(activity);
        mModel.detainCarFormUploadPhotos(paramsMap, MapUtil.getParts(photos))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        hideUploadLoading();
                        if (voidResult.ok()) {
                            mRootView.startWebActivity(form.getDetainCarFormQ());
                            mRootView.uploadSuccess();
                        } else {
                            mRootView.uploadFailure(voidResult.getMsg());
                        }
                    }
                });
    }

    private void getAccordRules2() {
        HashMap<String, String> param = new HashMap<>();
        param.put("HYLB", hylb);
        MapUtil mapUtil = new MapUtil();
        Map<String, Object> paramsMap = mapUtil.getMap("selectJcxXqList", BaseData.gson.toJson(param));
        mModel.getAccordRuleList("selectJcxXqList")
                .compose(RxUtils.<Result<List<AccordRule>>>applySchedulers(mRootView, false))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AccordRule>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<AccordRule>> listResult) {
                        if (listResult.ok()) {
                            ArrayList<String> accordList = new ArrayList<>(listResult.getData().size());
                            for (AccordRule rule : listResult.getData()) {
                                accordList.add(rule.getSYFLFG());
                            }
                            accordList.add("其他");
                            allAccordRule = UIUtils.listToArray(accordList);
                        }
                    }
                });
    }

    private void getAccordRules() {
        mModel.getAccordRuleList("getXcblReadOutFg")
                .compose(RxUtils.<Result<List<AccordRule>>>applySchedulers(mRootView, false))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AccordRule>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<AccordRule>> listResult) {
                        if (listResult.ok()) {
                            ArrayList<String> accordList = new ArrayList<>(listResult.getData().size());
                            for (AccordRule rule : listResult.getData()) {
                                accordList.add(rule.getNAME());
                            }
                            allAccordRule = UIUtils.listToArray(accordList);
                        }
                    }
                });
    }

    public void showAccordRule(Activity activity, final EditText editText) {
        if (allAccordRule == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getAccordRules();
            return;
        }

        Windows.singleChoice(activity, "选择依据条例", allAccordRule, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                if("其他".equals(allAccordRule[position])){
                    editText.setText("");
                    editText.setFocusableInTouchMode(true);
                    editText.setFocusable(true);
                    editText.requestFocus();
                }else{
                    editText.setText(allAccordRule[position]);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                }
            }
        });
    }

    /**
     * 添加扣押车辆交接单
     */
    public void addDetainCarForm(final DetainCarFormQ detainCarFormQ) {
        Map<String, Object> params = new MapUtil().getMap("addKycljjdInfo", BaseData.gson.toJson(detainCarFormQ));
        submitData(detainCarFormQ, params, true, false, null);
    }

    /*
     * 车辆交接单信息修改
     */
    public void UpdateData(final DetainCarFormQ detainCarFormQ, boolean isChange, ArrayList<CaseStatus> followInstruments) {
        Map<String, Object> map = new MapUtil().getMap("updateKycljjdInfo", RequestParamsUtil.getJsonEmptyStr(detainCarFormQ));
        submitData(detainCarFormQ, map, false, isChange, followInstruments);
    }

    private MyProgressDialog waiting;

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    private void submitData(final DetainCarFormQ detainCarFormQ, Map<String, Object> map, final boolean isAdd, final boolean isChange, final ArrayList<CaseStatus> followInstruments) {
        waiting = Windows.waiting(activity);
        mModel.addDetainCarForm(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<JSONObject> result) throws Exception {
                        hideWaiting();
                        if (result.ok()) {
                            String id;
                            try {
                                putCashService(detainCarFormQ);
                                id = result.getData().optString("id");
                                detainCarFormQ.setID(id);
                                mRootView.onCreateSuccess(new DetainCarForm(id, detainCarFormQ));
                                if (isAdd) {
                                    updateMap.put(Config.STR_CARFORM, 1);
                                    EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                                } else {
                                    if (isChange && followInstruments != null) {
                                        setInstrumentsFlag(detainCarFormQ, followInstruments);
                                    } else {
                                        mRootView.startWebActivity(detainCarFormQ);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showMessage("生成id有误，请重试");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    private void setInstrumentsFlag(final DetainCarFormQ detainCarFormQ, final ArrayList<CaseStatus> followInstruments) {
        boolean isNeedUpdateFlag = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateFlag = true;
            }
        }
        if (!isNeedUpdateFlag) {
            mRootView.startWebActivity(detainCarFormQ);
            return;
        }
        Map<String, Object> map = new MapUtil().getMap("updateCaseBlstate", RequestParamsUtil.RequestInstrumentState(new InstrumentStateReq(detainCarFormQ.getZID(), "", "1", Config.ID_CARFORM, "0")));
        mModel.setInstrumentsFlag(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            for (int i = 0; i < followInstruments.size(); i++) {
                                if (Config.STR_CARFORM.equals(followInstruments.get(i).getBlType()) && 2 == followInstruments.get(i).getStatus()) {
                                    updateMap.put(followInstruments.get(i).getBlType(), 1);
                                }
                            }
                            EventBus.getDefault().post(updateMap, Config.UPDATECASESTATUS);
                            mRootView.startWebActivity(detainCarFormQ);
                        } else {
                            showReuploadPhotoDialog(detainCarFormQ, followInstruments);
                        }
                    }

                });
    }

    private void showReuploadPhotoDialog(final DetainCarFormQ detainCarFormQ, final ArrayList<CaseStatus> followInstruments) {
        Windows.singleChoice(activity
                , "后续文书状态更新失败,是否重新提交?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            setInstrumentsFlag(detainCarFormQ, followInstruments);
                        }
                    }
                });
    }

    /*
     * 文书创建或修改成功后，信息保存到缓存
     */
    private void putCashService(final DetainCarFormQ detainCarFormQ) {
        CacheManager.getInstance().putUTC(Config.T_CARFORM, new UTC(Config.T_CARFORM
                , TimeTool.yyyyMMdd_HHmmss.format(sTime)
                , TimeTool.yyyyMMdd_HHmmss.format(new Date(Long.valueOf(detainCarFormQ.getZFDWQZSJ()) * 1000))));
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
                                mRootView.notifyAdapter();
                            }
                        }
                    }

                });
    }

}
