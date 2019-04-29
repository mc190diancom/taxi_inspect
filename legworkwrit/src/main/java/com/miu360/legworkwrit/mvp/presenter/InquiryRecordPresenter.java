package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.TextView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.mvp.contract.InquiryRecordContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class InquiryRecordPresenter extends BasePresenter<InquiryRecordContract.Model, InquiryRecordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private Activity activity;
    private ArrayList<InquiryRecordPhoto> infos;

    @Inject
    public InquiryRecordPresenter(InquiryRecordContract.Model model, InquiryRecordContract.View rootView) {
        super(model, rootView);
    }

    private Case getCase() {
        return CacheManager.getInstance().getCase();
    }

    private UTC getLiveCheckRecord() {
        return CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
    }

    public void init(Activity activity, TextView tvStart, TextView tvEnd) {
        this.activity = activity;
        this.infos = activity.getIntent().getParcelableArrayListExtra("infos");

        if (getCase() == null) {
            //未创建案件
            if (infos != null && infos.size() > 0) {
                //上传列表已保存的有询问笔录,则根据列表询问笔录的最迟时间来决定当前添加询问笔录的起止时间
                long endTime = getEndTime();
                tvStart.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(endTime * 1000)));
                tvEnd.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(endTime * 1000 + Config.UTC_ZPDZ * 1000)));
            } else {
                //上传列表已保存的有询问笔录,则开始时间默认为现在时间，结束时间默认为开始时间3分钟以后
                long currentMillis = System.currentTimeMillis();
                tvStart.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(currentMillis)));
                tvEnd.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(currentMillis + Config.UTC_ZPDZ * 1000)));
            }
        } else {
            //已创建案件
            if (getLiveCheckRecord() != null && !TextUtils.isEmpty(getLiveCheckRecord().getStartUTC())
                    && !TextUtils.isEmpty(getLiveCheckRecord().getEndUTC())
                    && !"无".equals(getLiveCheckRecord().getStartUTC())
                    && !"无".equals(getLiveCheckRecord().getEndUTC())) {
                //已创建现场检查笔录，按照现场检查笔录的时间来设置询问笔录的默认起止时间
                String startTimeStr = getLiveCheckRecord().getStartUTC();
                long startTime = TimeTool.parseDate(startTimeStr).getTime();
                tvStart.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(startTime + 60 * 1000)));
                tvEnd.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(startTime + 60 * 1000 + Config.UTC_ZPDZ * 1000)));
            } else {
                //未创建现场检查笔录，按照案件的创建时间来设置询问笔录的默认起止时间
                long createTime = Long.valueOf(getCase().getCREATEUTC());//时间戳,单位秒
                tvStart.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(createTime * 1000 + 60 * 1000)));
                tvEnd.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(createTime * 1000 + 60 * 1000 + Config.UTC_ZPDZ * 1000)));
            }
        }
    }

    private long getEndTime() {
        long endTime = TimeTool.parseDate(this.infos.get(0).getENDUTC()).getTime() / 1000;

        for (InquiryRecordPhoto photo : infos) {
            long currentEndTime = TimeTool.parseDate(photo.getENDUTC()).getTime() / 1000;

            if (currentEndTime > endTime) {
                endTime = currentEndTime;
            }
        }

        return endTime;
    }

    public void chooseStartTime(String defaultTimeStr) {
        Date date = TimeTool.parseDate(defaultTimeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DialogUtil.TimePicker(activity, "开始时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showStartTime(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }

    public void chooseEndTime(String defaultTimeStr) {
        Date date = TimeTool.parseDate(defaultTimeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DialogUtil.TimePicker(activity, "结束时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showEndTime(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }

    public boolean checkTime(String startTimeStr, String endTimeStr) {
        long startTime = TimeTool.parseDate(startTimeStr).getTime() / 1000;
        long endTime = TimeTool.parseDate(endTimeStr).getTime() / 1000;

        if (endTime <= startTime) {
            DialogUtil.showTipDialog(activity, "结束时间不能在开始时间之前");
            return false;
        }

        if (endTime - startTime < 3 * 60) {
            DialogUtil.showTipDialog(activity, "询问笔录的时间不能低于3分钟");
            return false;
        }

        //检查所选时间段与上传列表的时间是否冲突
        if (infos != null && infos.size() > 0) {
            for (InquiryRecordPhoto info : infos) {
                long infoStartTime = TimeTool.parseDate(info.getSTARTUTC()).getTime() / 1000;
                long infoEndTime = TimeTool.parseDate(info.getENDUTC()).getTime() / 1000;

                if (startTime < infoStartTime && endTime > infoStartTime) {
                    DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                    return false;
                } else if (startTime >= infoStartTime && startTime < infoEndTime) {
                    DialogUtil.showTipDialog(activity, "当前所选时间段不能和已创建询问笔录的时间冲突");
                    return false;
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
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
