package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.UploadListContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.PhotoID;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.mvp.model.entity.UploadInfo;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.MapUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


@ActivityScope
public class UploadListPresenter extends BasePresenter<UploadListContract.Model, UploadListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<InquiryRecordPhoto> photos;

    private Activity activity;
    private PhotoPreference photoPreference;

    @Inject
    public UploadListPresenter(UploadListContract.Model model, UploadListContract.View rootView) {
        super(model, rootView);
    }

    public Case getCase() {
        return CacheManager.getInstance().getCase();
    }


    private UTC getLiveCheckRecord() {
        return CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
    }

    public void init(Activity activity, LinearLayout llButtonContainer) {
        this.activity = activity;
        this.photoPreference = new PhotoPreference();
        if (getCase() != null) {
            llButtonContainer.setVisibility(View.GONE);
        }

        loadUploadInfos();
    }

    public void loadUploadInfos() {
        if (getCase() != null) {
            getUploadInfoFromServer();
        } else {
            getUploadInfoFromLocal();
        }
    }

    private void getUploadInfoFromServer() {
        Map<String, Object> map = new MapUtil().getMap("getXwblPhotoList", BaseData.gson.toJson(new CaseID(getCase().getID())));

        mModel.getInquiryRecordPhotoList(map)
                .compose(RxUtils.<Result<List<InquiryRecordPhoto>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<InquiryRecordPhoto>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<InquiryRecordPhoto>> result) {
                        if (result.ok()) {
                            List<InquiryRecordPhoto> data = result.getData();
                            if (data != null) {
                                for (InquiryRecordPhoto photo : data) {
                                    photo.setSTARTUTC(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(photo.getSTARTUTC())));
                                    photo.setENDUTC(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(photo.getENDUTC())));
                                }

                                photos.addAll(result.getData());
                                Collections.sort(photos, comparator);
                                GetUTCUtil.updateInquiryRecordUTC(photos);
                            }

                            mRootView.loadUploadInfoSuccess();
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    private Comparator<InquiryRecordPhoto> comparator = new Comparator<InquiryRecordPhoto>() {
        @Override
        public int compare(InquiryRecordPhoto o1, InquiryRecordPhoto o2) {
            long i1 = TimeTool.parseDate(o1.getSTARTUTC()).getTime() / 1000;
            long i2 = TimeTool.parseDate(o2.getSTARTUTC()).getTime() / 1000;
            return (int) (i1 - i2);
        }
    };

    private void getUploadInfoFromLocal() {
        Set<String> value = photoPreference.getPhotos();

        if (value != null) {
            for (String s : value) {
                try {
                    InquiryRecordPhoto photo = BaseData.gson.fromJson(s, InquiryRecordPhoto.class);
                    photos.add(photo);
                } catch (JsonSyntaxException e) {
                    Timber.w(e);
                }
            }

            Collections.sort(photos, comparator);
            mRootView.loadUploadInfoSuccess();
        }
    }

    public ArrayList<InquiryRecordPhoto> getPhotos() {
        return photos;
    }

    public int getPhotosCount() {
        if (photos != null) {
            return photos.size();
        } else {
            return 0;
        }
    }

    public int getCheckedCount() {
        int count = 0;

        if (photos == null || photos.size() <= 0) {
            count = -1;
        } else {
            for (InquiryRecordPhoto photo : photos) {
                if (photo.isChecked()) {
                    count++;
                }
            }
        }

        return count;
    }

    public ArrayList<InquiryRecordPhoto> getCheckedInfos() {
        ArrayList<InquiryRecordPhoto> checkedUploadInfo = new ArrayList<>(photos.size());

        for (InquiryRecordPhoto photo : photos) {
            if (photo.isChecked()) {
                checkedUploadInfo.add(photo);
            }
        }

        return checkedUploadInfo;
    }

    public void deleteUploadInfo(InquiryRecordPhoto photo) {
        if (getCase() == null) {
            photoPreference.removePhoto(photo);
            photos.remove(photo);
            UIUtils.toast(activity, "删除询问笔录成功", Toast.LENGTH_SHORT);
            mRootView.deleteUploadInfoSuccess();
        } else {
            //从服务器删除询问笔录信息
            deleteInquiryRecordFromServer(photo);
        }
    }

    private void deleteInquiryRecordFromServer(final InquiryRecordPhoto photo) {
        Map<String, Object> params = new MapUtil().getMap("deleteXwblPhotoById", BaseData.gson.toJson(new PhotoID(photo.getID())));

        mModel.deleteInquiryRecord(params)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        if (voidResult.ok()) {
                            UIUtils.toast(activity, "删除询问笔录成功", Toast.LENGTH_SHORT);

                            photos.remove(photo);
                            //删除成功以后，更新缓存中的询问笔录的开始和结束时间
                            GetUTCUtil.updateInquiryRecordUTC(photos);
                            mRootView.deleteUploadInfoSuccess();
                        } else {
                            mRootView.showMessage(voidResult.getMsg());
                        }
                    }
                });
    }

    public void deleteAllPhotos() {
        if (getCase() == null) {
            photoPreference.clearPreference();
            photos.clear();
            UIUtils.toast(activity, "删除所有询问笔录成功", Toast.LENGTH_SHORT);
            mRootView.deleteUploadInfoSuccess();
        } else {
            //从服务器删除所有询问笔录信息
            deleteAllInquiryRecordFromServer();
        }
    }

    private void deleteAllInquiryRecordFromServer() {
        if (photos == null || photos.size() <= 0) {
            UIUtils.toast(activity, "没有询问笔录可供删除", Toast.LENGTH_SHORT);
            return;
        }

        Map<String, Object> params = new MapUtil().getMap("deleteXwblPhotoByZid", BaseData.gson.toJson(new CaseID(getCase().getID())));

        mModel.deleteAllInquiryRecord(params)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        if (voidResult.ok()) {
                            UIUtils.toast(activity, "删除所有询问笔录成功", Toast.LENGTH_SHORT);

                            photos.clear();
                            //删除成功以后，更新缓存中的询问笔录的开始和结束时间
                            GetUTCUtil.updateInquiryRecordUTC(photos);
                            mRootView.deleteUploadInfoSuccess();
                        } else {
                            mRootView.showMessage(voidResult.getMsg());
                        }
                    }
                });
    }

    public void modifyStartTime(final InquiryRecordPhoto photo) {
        Calendar calendar = Calendar.getInstance();
        Date date = TimeTool.parseDate(photo.getSTARTUTC());
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, "开始时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                long chooseStartSeconds = date.getTime() / 1000;

                if (checkStartTime(chooseStartSeconds, photo)) {
                    String newStartTime = TimeTool.yyyyMMdd_HHmm.format(new Date(chooseStartSeconds * 1000));

                    if (getCase() == null) {
                        //更新本地保存的数据
                        photoPreference.removePhoto(photo);
                        photo.setSTARTUTC(newStartTime);
                        photoPreference.addPhoto(photo);
                        UIUtils.toast(activity, "修改开始时间成功", Toast.LENGTH_SHORT);
                        mRootView.modifyStartTimeSuccess();
                    } else {
                        //更新服务器保存的数据
                        updateTimeToServer(photo, chooseStartSeconds, true);
                    }
                }
            }
        });
    }

    public void modifyEndTime(final InquiryRecordPhoto photo) {
        Date date = TimeTool.parseDate(photo.getENDUTC());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, "结束时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                long chooseEndSeconds = date.getTime() / 1000;

                if (checkEndTime(chooseEndSeconds, photo)) {
                    String newEndTime = TimeTool.yyyyMMdd_HHmm.format(new Date(chooseEndSeconds * 1000));

                    if (getCase() == null) {
                        //更新本地保存的数据
                        photoPreference.removePhoto(photo);
                        photo.setENDUTC(newEndTime);
                        photoPreference.addPhoto(photo);
                        UIUtils.toast(activity, "修改结束时间成功", Toast.LENGTH_SHORT);
                        mRootView.modifyEndTimeSuccess();
                    } else {
                        //更新服务器保存的数据
                        updateTimeToServer(photo, chooseEndSeconds, false);
                    }
                }
            }
        });
    }

    private void updateTimeToServer(final InquiryRecordPhoto photo, final long time, final boolean isStart) {
        UploadInfo info = new UploadInfo();
        info.setID(photo.getID());
        if (isStart) {
            info.setSTARTUTC(String.valueOf(time));
            info.setENDUTC(String.valueOf(TimeTool.parseDate(photo.getENDUTC()).getTime() / 1000));
        } else {
            info.setSTARTUTC(String.valueOf(TimeTool.parseDate(photo.getSTARTUTC()).getTime() / 1000));
            info.setENDUTC(String.valueOf(time));
        }
        Map<String, Object> params = new MapUtil().getMap("updateXwblInfoByPhotoId", BaseData.gson.toJson(info));
        mModel.updateInquiryRecord(params)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        if (voidResult.ok()) {
                            if (isStart) {
                                UIUtils.toast(activity, "修改开始时间成功", Toast.LENGTH_SHORT);
                                photo.setSTARTUTC(TimeTool.yyyyMMdd_HHmm.format(new Date(time * 1000)));
                                mRootView.modifyStartTimeSuccess();
                            } else {
                                UIUtils.toast(activity, "修改结束时间成功", Toast.LENGTH_SHORT);
                                photo.setENDUTC(TimeTool.yyyyMMdd_HHmm.format(new Date(time * 1000)));
                                mRootView.modifyEndTimeSuccess();
                            }

                            //修改成功以后，更新缓存中的询问笔录的开始和结束时间
                            GetUTCUtil.updateInquiryRecordUTC(photos);
                        } else {
                            if (isStart) {
                                UIUtils.toast(activity, "修改开始时间失败", Toast.LENGTH_SHORT);
                            } else {
                                UIUtils.toast(activity, "修改结束时间失败", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });
    }

    private boolean checkStartTime(long chooseStartSeconds, InquiryRecordPhoto photo) {
        long oldEndSeconds = TimeTool.parseDate(photo.getENDUTC()).getTime() / 1000;

        if (oldEndSeconds <= chooseStartSeconds) {
            DialogUtil.showTipDialog(activity, "结束时间不能在开始时间之前");
            return false;
        }

        if (oldEndSeconds - chooseStartSeconds < 3 * 60) {
            DialogUtil.showTipDialog(activity, "询问笔录的时间不能低于3分钟");
            return false;
        }

        if (photos != null && photos.size() > 0) {
            for (InquiryRecordPhoto item : photos) {
                if (item != photo) {
                    long otherStartTime = TimeTool.parseDate(item.getSTARTUTC()).getTime() / 1000;
                    long otherEndTime = TimeTool.parseDate(item.getENDUTC()).getTime() / 1000;

                    if (chooseStartSeconds < otherStartTime && oldEndSeconds > otherStartTime) {
                        DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                        return false;
                    } else if (chooseStartSeconds >= otherStartTime && chooseStartSeconds < otherEndTime) {
                        DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                        return false;
                    }
                }
            }
        }

        if (getCase() != null) {
            //有案件
            if (getLiveCheckRecord() != null && !TextUtils.isEmpty(getLiveCheckRecord().getStartUTC())
                    && !TextUtils.isEmpty(getLiveCheckRecord().getEndUTC())
                    && !"无".equals(getLiveCheckRecord().getStartUTC())
                    && !"无".equals(getLiveCheckRecord().getEndUTC())) {
                //已创建现场检查笔录，按照现场检查笔录的的起止时间来检查时间的合法性
                String liveStartTimeStr = getLiveCheckRecord().getStartUTC();
                String liveEndTimeStr = getLiveCheckRecord().getEndUTC();
                long liveStartTime = TimeTool.parseDate(liveStartTimeStr).getTime() / 1000;
                long liveEndTime = TimeTool.parseDate(liveEndTimeStr).getTime() / 1000;

                if (chooseStartSeconds >= liveEndTime || chooseStartSeconds <= liveStartTime) {
                    DialogUtil.showTipDialog(activity, "询问笔录的开始时间必须在现场检查笔录的起止时间之内");
                    return false;
                }
            } else {
                //未创建现场检查笔录,按照案件的创建时间来检查时间的合法性
                Long createTime = Long.valueOf(getCase().getCREATEUTC());//创建时间,单位秒
                if (chooseStartSeconds <= createTime) {
                    DialogUtil.showTipDialog(activity, "询问笔录开始时间必须在案件创建时间之后");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkEndTime(long chooseEndSeconds, InquiryRecordPhoto photo) {
        long oldStartSeconds = TimeTool.parseDate(photo.getSTARTUTC()).getTime() / 1000;

        if (chooseEndSeconds <= oldStartSeconds) {
            DialogUtil.showTipDialog(activity, "结束时间不能在开始时间之前");
            return false;
        }

        if (chooseEndSeconds - oldStartSeconds < 3 * 60) {
            DialogUtil.showTipDialog(activity, "询问笔录的时间不能低于3分钟");
            return false;
        }

        if (photos != null && photos.size() > 0) {
            for (InquiryRecordPhoto item : photos) {
                if (item != photo) {
                    long otherStartTime = TimeTool.parseDate(item.getSTARTUTC()).getTime() / 1000;
                    long otherEndTime = TimeTool.parseDate(item.getENDUTC()).getTime() / 1000;

                    if (oldStartSeconds < otherStartTime && chooseEndSeconds > otherStartTime) {
                        DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                        return false;
                    } else if (oldStartSeconds >= otherStartTime && oldStartSeconds < otherEndTime) {
                        DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                        return false;
                    }
                }
            }
        }

        if (getCase() != null) {
            //有案件
            if (getLiveCheckRecord() != null && !TextUtils.isEmpty(getLiveCheckRecord().getStartUTC())
                    && !TextUtils.isEmpty(getLiveCheckRecord().getEndUTC())
                    && !"无".equals(getLiveCheckRecord().getStartUTC())
                    && !"无".equals(getLiveCheckRecord().getEndUTC())) {
                //已创建现场检查笔录，按照现场检查笔录的的起止时间来检查时间的合法性
                String liveStartTimeStr = getLiveCheckRecord().getStartUTC();
                String liveEndTimeStr = getLiveCheckRecord().getEndUTC();
                long liveStartTime = TimeTool.parseDate(liveStartTimeStr).getTime() / 1000;
                long liveEndTime = TimeTool.parseDate(liveEndTimeStr).getTime() / 1000;

                if (chooseEndSeconds >= liveEndTime || chooseEndSeconds <= liveStartTime) {
                    DialogUtil.showTipDialog(activity, "询问笔录的结束时间必须在现场检查笔录的起止时间之内");
                    return false;
                }
            } else {
                //未创建现场检查笔录,按照案件的创建时间来检查时间的合法性
                Long createTime = Long.valueOf(getCase().getCREATEUTC());//创建时间,单位秒
                if (chooseEndSeconds < createTime) {
                    DialogUtil.showTipDialog(activity, "询问笔录结束时间必须在案件创建时间之后");
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

}
