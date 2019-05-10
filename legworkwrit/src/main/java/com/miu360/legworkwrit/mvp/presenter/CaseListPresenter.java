package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.CaseListContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhotoParams;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CaseListPresenter extends BasePresenter<CaseListContract.Model, CaseListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private Activity activity;

    private MyProgressDialog waiting;

    private String startTime;
    private String endTime;
    private String carLicense;
    private String industry;

    @Inject
    public CaseListPresenter(CaseListContract.Model model, CaseListContract.View rootView) {
        super(model, rootView);
    }

    /*
     * 初始化案件列表细腻
     */
    public void init(Activity act) {
        this.activity = act;
        startTime = act.getIntent().getStringExtra("start_time");
        endTime = act.getIntent().getStringExtra("end_time");
        carLicense = act.getIntent().getStringExtra("license");
        industry = act.getIntent().getStringExtra("industry");

        if (!TextUtils.isEmpty(startTime)) {
            startTime = String.valueOf(TimeTool.parseDate(startTime).getTime() / 1000);
        }

        if (!TextUtils.isEmpty(endTime)) {
            endTime = String.valueOf(TimeTool.parseDate(endTime).getTime() / 1000);
        }

        getCaseListData(1);
    }

    public void getCaseListData(final int page) {
        Map<String, Object> map = new MapUtil().getMap("selectCaseAll", RequestParamsUtil.RequestCaseList(startTime, endTime, carLicense, industry, page));
        System.out.println("selectCaseAll:"+map);
        mModel.getCaseListData(map)
                .compose(RxUtils.<Result<List<Case>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<Case>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<Case>> result) {
                        if (result.ok()) {
                            if (result.getData() == null || result.getData().size() <= 0) {
                                UIUtils.toast(activity, "没有更多数据了", Toast.LENGTH_SHORT);
                            }

                            mRootView.getCaseListSuccess(result.getData(),page);
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    public void getCaseBlTimeFromUpload(final Case c) {
        waiting = Windows.waiting(activity);

        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("selectblTimesListByCaseId", BaseData.gson.toJson(params));

        mModel.getBlTime(map)
                .compose(RxUtils.<Result<List<UTC>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<UTC>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<UTC>> listResult) {
                        if (listResult.ok()) {
                            List<UTC> data = listResult.getData();

                            String liveRecordStartTime = "";
                            String liveRecordEndTime = "";

                            if (data != null && data.size() > 0) {
                                for (UTC utc : data) {
                                    if (Config.T_ZPDZ.equals(utc.getName())) {
                                        if (!TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())
                                                && !"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())) {
                                            //已创建了询问笔录
                                            hideWaiting();
                                            UIUtils.toast(activity, "该案件已创建了询问笔录", Toast.LENGTH_SHORT);
                                            return;
                                        }
                                    } else if (Config.T_LIVERECORD.equals(utc.getName())) {
                                        if (!TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())
                                                && !"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())) {
                                            //已创建了现场检查笔录
                                            liveRecordStartTime = utc.getStartUTC();
                                            liveRecordEndTime = utc.getEndUTC();
                                        }
                                    }
                                }
                            }

                            ArrayList<InquiryRecordPhoto> photos = activity.getIntent().getParcelableArrayListExtra("infos");

                            //验证询问笔录的时间是否在案件创建时间之后
                            if (!verifyCaseCreateTime(photos, c)) {
                                hideWaiting();
                                DialogUtil.showTipDialog(activity, "询问笔录的起止时间必须在案件创建时间之后");
                                return;
                            }

                            //验证询问笔录的时间是否在现场检查笔录的起止时间内
                            if (!verifyInquiryRecordTime(photos, liveRecordStartTime, liveRecordEndTime)) {
                                hideWaiting();
                                DialogUtil.showTipDialog(activity, "询问笔录的起止时间必须在现场检查笔录的起止时间之内");
                                return;
                            }

                            uploadPhotos(c, photos);
                        } else {
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });
    }

    private boolean verifyCaseCreateTime(ArrayList<InquiryRecordPhoto> photos, Case c) {
        long createTime = Long.valueOf(c.getCREATEUTC());

        for (InquiryRecordPhoto photo : photos) {
            long currentStart = TimeTool.parseDate(photo.getSTARTUTC()).getTime() / 1000;
            if (currentStart <= createTime) {
                return false;
            }
        }

        return true;
    }

    /**
     * 验证询问笔录的时间是否在现场检查笔录时间的范围内
     */
    private boolean verifyInquiryRecordTime(ArrayList<InquiryRecordPhoto> photos, String liveRecordStartTime, String liveRecordEndTime) {
        if (!TextUtils.isEmpty(liveRecordStartTime) && !TextUtils.isEmpty(liveRecordEndTime)) {
            long start = TimeTool.parseDate(liveRecordStartTime).getTime() / 1000;
            long end = TimeTool.parseDate(liveRecordEndTime).getTime() / 1000;

            for (InquiryRecordPhoto photo : photos) {
                long currentStart = TimeTool.parseDate(photo.getSTARTUTC()).getTime() / 1000;
                long currentEnd = TimeTool.parseDate(photo.getENDUTC()).getTime() / 1000;

                if (currentStart < start || currentEnd > end) {
                    return false;
                }
            }
        }

        return true;
    }

    public void getCaseBlTime(final Case c,final boolean isSendBackOffice) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("selectblTimesListByCaseId", BaseData.gson.toJson(params));
        waiting = Windows.waiting(activity);
        mModel.getBlTime(map)
                .compose(RxUtils.<Result<List<UTC>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<UTC>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<UTC>> listResult) {
                        if (listResult.ok()) {
                            if(isSendBackOffice && (listResult.getData() == null || listResult.getData().isEmpty())){
                                UIUtils.toast(activity,"当前案件还未绑定文书",Toast.LENGTH_SHORT);
                                hideWaiting();
                                return;
                            }
                            List<UTC> data = listResult.getData();
                            List<CaseStatus> caseStatusList = new ArrayList<>();

                            for (int i = 0; i < data.size(); i++) {
                                CacheManager.getInstance().putUTC(data.get(i).getName(), data.get(i));
                                caseStatusList.add(new CaseStatus(data.get(i).getName(),null,Integer.valueOf(data.get(i).getFlag()),data.get(i).getPrintTimes()));
                                if(Config.T_CARDECIDE.equals(data.get(i).getName()) && !"无".equals(data.get(i).getStartUTC())) {
                                    CacheManager.getInstance().putDetainTime(TimeTool.formatyyyyMMdd_HHmm(TimeTool.parseDate2(data.get(i).getStartUTC()).getTime()));
                                }
                            }
                            if(!isSendBackOffice){
                                GetUTCUtil.setUTCtoCache(c);
                            }
                            getBlType(c,caseStatusList,isSendBackOffice);
                        } else {
                            hideWaiting();
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });
    }

    public void getBlType(final Case c,final List<CaseStatus> caseStatusList,final boolean isSendBackOffice) {
        Map<String, Object> params = new MapUtil().getMap("selectCaseBlList", BaseData.gson.toJson(new CaseID(c.getID())));

        mModel.getBlType(params)
                .compose(RxUtils.<Result<ArrayList<BlType>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<ArrayList<BlType>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<ArrayList<BlType>> listResult) {
                        hideWaiting();
                        if (listResult.ok()) {
                            if(isSendBackOffice && (listResult.getData() == null || listResult.getData().isEmpty())){
                                UIUtils.toast(activity,"当前案件还未绑定文书",Toast.LENGTH_SHORT);
                                hideWaiting();
                                return;
                            }
                            ArrayList<BlType> types = new ArrayList<>();
                            ArrayList<String> colNames = new ArrayList<>();
                            boolean isCompleted = false;
                            for (int i = 0; i < listResult.getData().size(); i++) {
                                if (!colNames.contains(listResult.getData().get(i).getCOLUMNNAME())) {
                                    colNames.add(listResult.getData().get(i).getCOLUMNNAME());
                                    BlType blType = listResult.getData().get(i);
                                    for(int j = 0,len = caseStatusList.size();j < len;j++){
                                        if(!Config.T_ZPDZ.equals(blType.getTABLENAME())){//询问笔录不进行判断
                                            if(caseStatusList.get(j).getBlType().equals(blType.getTABLENAME())){
                                                blType.setPrintTimes(caseStatusList.get(j).getPrintTimes());
                                                if(caseStatusList.get(j).getStatus() == 0){//未填写
                                                    blType.setFLAG(0);
                                                    isCompleted = true;
                                                }else if(caseStatusList.get(j).getStatus() == 1 && blType.getFLAG() == 0){//已填写
                                                    blType.setFLAG(1);
                                                }else{//待确认
                                                    CacheManager.getInstance().updateUTC(caseStatusList.get(j).getBlType(),false);
                                                    isCompleted = true;
                                                    blType.setFLAG(2);
                                                }
                                            }
                                        }
                                    }
                                    types.add(blType);
                                }
                            }

                            if(isSendBackOffice){
                                if(isCompleted){
                                    mRootView.showMessage("还有未完成或者待确认的文书，不能送内勤");
                                }else{
                                    sendBackOffice(c);
                                }
                                return;
                            }

                            if (!"警告".equals(c.getCFFS())) {
                                for (BlType type : types) {
                                    if (Config.T_TALKNOTICE.equals(type.getTABLENAME())) {
                                        CacheManager.getInstance().putChooseInstrumentType(3);
                                        break;
                                    } else if (Config.T_FRISTREGISTER.equals(type.getTABLENAME())) {
                                        CacheManager.getInstance().putChooseInstrumentType(1);
                                        break;
                                    } else if (Config.T_LIVETRANSCRIPT.equals(type.getTABLENAME()) || Config.T_CARDECIDE.equals(type.getTABLENAME())) {
                                        CacheManager.getInstance().putChooseInstrumentType(2);
                                        break;
                                    }
                                }
                            }

                            mRootView.getCaseBlTypeSuccess(c, types);
                        } else {
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void getInquiryRecordPhotoList(final Case c) {
        waiting = Windows.waiting(activity);
        Map<String, Object> map = new MapUtil().getMap("getXwblPhotoList", BaseData.gson.toJson(new CaseID(c.getID())));

        mModel.getInquiryRecordPhotoList(map)
                .compose(RxUtils.<Result<List<InquiryRecordPhoto>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<InquiryRecordPhoto>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<InquiryRecordPhoto>> result) {
                        if (result.ok()) {
                            ArrayList<InquiryRecordPhoto> infos = activity.getIntent().getParcelableArrayListExtra("infos");

                            if (result.getData() != null && result.getData().size() > 0) {
                                hideWaiting();
                                UIUtils.toast(activity, "该案件已创建了询问笔录", Toast.LENGTH_SHORT);
                                return;
                            }

                            uploadPhotos(c, infos);
                        } else {
                            hideWaiting();
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    private int allPhotoCount;
    private ArrayList<InquiryRecordPhoto> infos;
    private PhotoPreference photoPreference = new PhotoPreference();

    private void uploadPhotos(Case c, ArrayList<InquiryRecordPhoto> infos) {
        this.infos = infos;
        this.allPhotoCount = infos.size();
        uploadInfo(c, 0);
    }

    private void uploadInfo(final Case c, final int index) {
        InquiryRecordPhotoParams params = new InquiryRecordPhotoParams();
        params.setZID(c.getID());
        params.setSTARTUTC(String.valueOf(TimeTool.parseDate(infos.get(index).getSTARTUTC()).getTime() / 1000));
        params.setENDUTC(String.valueOf(TimeTool.parseDate(infos.get(index).getENDUTC()).getTime() / 1000));

        Map<String, Object> map = new MapUtil().getMap("upLoadXwblPhoto", BaseData.gson.toJson(params));

        mModel.inquiryRecordUploadPhotos(map, MapUtil.getPart(new File(infos.get(index).getZPLJ())))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            //保存成功从本地移除
                            photoPreference.removePhoto(infos.get(index));

                            int count = index + 1;
                            if (count == allPhotoCount) {
                                hideWaiting();
                                mRootView.uploadPhotosSuccess();
                            } else {
                                uploadInfo(c, count);
                            }
                        } else {
                            showReuploadPhotoDialog(c, index);
                        }
                    }
                });
    }

    private void showReuploadPhotoDialog(final Case c, final int index) {
        hideWaiting();

        Windows.singleChoice(activity
                , "第" + (index + 1) + "份询问笔录上传失败,是否重新上传?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            waiting.show();
                            uploadInfo(c, index);
                        }
                    }
                });
    }

    /*
     * 送内勤
     */
    private void sendBackOffice(final Case c) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("deliverInternal", BaseData.gson.toJson(params));
        mModel.sendBackOffice(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView,true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            mRootView.showMessage("送达内勤成功");
                            getCaseListData(1);
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }
}
