package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.FileUtil;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.ObtainImageContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhotoParams;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.MapUtil;
import com.miu360.legworkwrit.util.PictureUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ObtainImagePresenter extends BasePresenter<ObtainImageContract.Model, ObtainImageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private Activity activity;

    @Inject
    public ObtainImagePresenter(ObtainImageContract.Model model, ObtainImageContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void takePhoto(Activity activity, int requestCode, File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(MiuBaseApp.self.getPackageManager()) != null) {
            Uri uri = FileUtil.getFileUri(activity.getApplicationContext(), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public void choosePhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK
                , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(MiuBaseApp.self.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    private Case getCase() {
        return CacheManager.getInstance().getCase();
    }

    public void upload(final File file) {
        showLoading();
        Observable.just(file.getAbsolutePath())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return PictureUtil.bitmapToPath(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        startUpload(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideLoading();
                        UIUtils.toast(activity, "压缩图片失败,请重试", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startUpload(String path) {
        InquiryRecordPhoto photo = new InquiryRecordPhoto();
        photo.setSTARTUTC(activity.getIntent().getStringExtra("start_time"));
        photo.setENDUTC(activity.getIntent().getStringExtra("end_time"));
        photo.setZPLJ(path);

        File file = new File(path);

        if (getCase() == null) {
            //未创建案件，创建询问笔录，将图片保存到本地
            hideLoading();
            if (file.exists()) {
                new PhotoPreference().addPhoto(photo);
                mRootView.saveSuccess(photo);
            } else {
                mRootView.saveFailure();
            }
        } else {
            //已创建案件，创建询问笔录
            if (!TextUtils.isEmpty(getCase().getZHZH2()) && getCase().getZHZH2().equals(MiuBaseApp.user.getString("user_name", ""))) {
                //对于该案件来说，操作人是执法账号2，再增加询问笔录时需判断时间
                verifyTime(photo.getSTARTUTC(), photo.getENDUTC(), photo);
            } else {
                uploadPhotos(getCase().getID(), photo);
            }
        }
    }

    private void verifyTime(final String startTimeStr, final String endTimeStr, final InquiryRecordPhoto photo) {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", getCase().getID());
        Map<String, Object> map = new MapUtil().getMap("selectblTimesListByCaseId", BaseData.gson.toJson(params));

        mModel.getBlTime(map)
                .compose(RxUtils.<Result<List<UTC>>>applySchedulers(mRootView, false))
                .subscribe(new MyErrorHandleSubscriber<Result<List<UTC>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<UTC>> listResult) {
                        if (listResult.ok()) {
                            List<UTC> data = listResult.getData();

                            if (data != null && !data.isEmpty()) {
                                for (UTC utc : data) {
                                    if (utc != null && Config.T_LIVERECORD.equals(utc.getName())) {
                                        if (!"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())
                                                && !TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())) {
                                            CacheManager.getInstance().putUTC(Config.T_LIVERECORD, utc);
                                        }
                                    }
                                }
                            }

                            if (checkTime(startTimeStr, endTimeStr)) {
                                uploadPhotos(getCase().getID(), photo);
                            } else {
                                hideLoading();
                            }
                        } else {
                            hideLoading();
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });
    }

    private UTC getLiveCheckRecord() {
        return CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
    }

    private boolean checkTime(String startTimeStr, String endTimeStr) {
        long startTime = TimeTool.parseDate(startTimeStr).getTime() / 1000;
        long endTime = TimeTool.parseDate(endTimeStr).getTime() / 1000;

        if (getLiveCheckRecord() != null && !TextUtils.isEmpty(getLiveCheckRecord().getStartUTC())
                && !TextUtils.isEmpty(getLiveCheckRecord().getEndUTC())
                && !"无".equals(getLiveCheckRecord().getStartUTC())
                && !"无".equals(getLiveCheckRecord().getEndUTC())) {
            //已创建现场检查笔录，按照现场检查笔录的的起止时间来检查时间的合法性
            String liveStartTimeStr = getLiveCheckRecord().getStartUTC();
            String liveEndTimeStr = getLiveCheckRecord().getEndUTC();
            long liveStartTime = TimeTool.parseDate(liveStartTimeStr).getTime() / 1000;
            long liveEndTime = TimeTool.parseDate(liveEndTimeStr).getTime() / 1000;

            if (liveStartTime >= startTime || liveEndTime <= endTime) {
                DialogUtil.showTipDialog(activity, "询问笔录的起止时间必须在现场检查笔录的起止时间之内");
                return false;
            }
        } else {
            //未创建现场检查笔录,按照案件的创建时间来检查时间的合法性
            Long createTime = Long.valueOf(getCase().getCREATEUTC());//创建时间,单位秒
            if (startTime - createTime < 60) {
                DialogUtil.showTipDialog(activity, "询问笔录开始时间必须在案件创建时间之后");
                return false;
            }
        }

        return true;
    }

    private Dialog loading;

    private void hideLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }

        loading = null;
    }

    private void showLoading() {
        if (loading == null) {
            loading = DialogUtil.showUploadDialog(activity);
        } else {
            if (!loading.isShowing()) {
                loading.show();
            }
        }
    }

    private void uploadPhotos(final String caseId, final InquiryRecordPhoto info) {
        InquiryRecordPhotoParams params = new InquiryRecordPhotoParams();
        params.setZID(caseId);
        params.setSTARTUTC(String.valueOf(TimeTool.parseDate(info.getSTARTUTC()).getTime() / 1000));
        params.setENDUTC(String.valueOf(TimeTool.parseDate(info.getENDUTC()).getTime() / 1000));

        Map<String, Object> map = new MapUtil().getMap("upLoadXwblPhoto", BaseData.gson.toJson(params));
        mModel.inquiryRecordUploadPhotos(map, MapUtil.getPart(new File(info.getZPLJ())))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> result) {
                        hideLoading();

                        if (result.ok()) {
                            updateInquiryRecordTime(info);
                            mRootView.uploadSuccess();
                        } else {
                            mRootView.showMessage(result.getMsg());
                            mRootView.uploadFailure();
                        }
                    }
                });
    }

    /**
     * 更新询问笔录在缓存中的起止时间
     */
    private void updateInquiryRecordTime(InquiryRecordPhoto info) {
        UTC utc = CacheManager.getInstance().getUTC(Config.T_ZPDZ);

        if (utc != null && !TextUtils.isEmpty(utc.getStartUTC()) && !TextUtils.isEmpty(utc.getEndUTC())
                && !"无".equals(utc.getStartUTC()) && !"无".equals(utc.getEndUTC())) {
            UTC newUTC = new UTC();
            long oldStartTime = TimeTool.parseDate(utc.getStartUTC()).getTime() / 1000;
            long oldEndTime = TimeTool.parseDate(utc.getEndUTC()).getTime() / 1000;

            long currentStartTime = TimeTool.parseDate(info.getSTARTUTC()).getTime() / 1000;
            long currentEndTime = TimeTool.parseDate(info.getENDUTC()).getTime() / 1000;

            newUTC.setStartUTC(TimeTool.yyyyMMdd_HHmm.format(new Date(Math.min(oldStartTime, currentStartTime) * 1000)));
            newUTC.setEndUTC(TimeTool.yyyyMMdd_HHmm.format(new Date(Math.max(oldEndTime, currentEndTime) * 1000)));
            newUTC.setName(Config.T_ZPDZ);
            CacheManager.getInstance().putUTC(Config.T_ZPDZ, newUTC);
        } else {
            CacheManager.getInstance().putUTC(Config.T_ZPDZ, new UTC(Config.T_ZPDZ, info.getSTARTUTC(), info.getENDUTC()));
        }
    }

    public void init(Activity self) {
        this.activity = self;
    }
}
