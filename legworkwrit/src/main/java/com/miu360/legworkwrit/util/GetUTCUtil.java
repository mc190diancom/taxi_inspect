package com.miu360.legworkwrit.util;

import android.util.Log;

import com.miu30.common.config.Config;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Murphy on 2018/10/23.
 */
public class GetUTCUtil {

    /*
     * 谈话通知书、行政处罚决定书、先行登记通知书、扣押车辆决定书
     */
    public static long getLiveCheckRecordEndTimeL(Case c) {
        long time;
        UTC t;
        try {
            if (CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null
                    && !"无".equals(CacheManager.getInstance().getUTC(Config.T_LIVERECORD).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            } else {
                time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
            }
        } catch (Exception e) {
            time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
        }
        return time;
    }

    /*
     * 扣押车辆交接单
     */
    public static long DetainCarForm(Case c) {
        long time;
        UTC t;
        try {
            if (CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            }else if (CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            }  else if (CacheManager.getInstance().getUTC(Config.T_CARDECIDE) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_CARDECIDE).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_CARDECIDE);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            } else if (CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_LIVERECORD).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            } else {
                time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
            }
        } catch (Exception e) {
            time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
        }
        return time;
    }

    /*
     * 现场笔录
     */
    public static long LiveTranScript(Case c) {
        long time;
        UTC t;
        try {
            if (CacheManager.getInstance().getUTC(Config.T_CARDECIDE) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_CARDECIDE).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_CARDECIDE);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            } else if (CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_LIVERECORD).getEndUTC())) {
                t = CacheManager.getInstance().getUTC(Config.T_LIVERECORD);
                time = TimeTool.parseDate2(t.getEndUTC()).getTime() / 1000 + 60;
            } else {
                time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
            }
        } catch (Exception e) {
            time = Long.valueOf(c.getCREATEUTC()) + 12 * 60;
        }

        return time;
    }

    /**
     * @param lastEndTime 上一个文书结束时间
     * @param sendTime    这个文书的送达时间，如果没有送达是决定
     * @param duration    文书时长
     * @return 结束时间 UTC
     */
    public static String setEndTime(String lastEndTime, String sendTime, long duration) {
        String time;
        long lastTime = TimeTool.getYYHHmm(lastEndTime).getTime() / 1000;
        long minTime = Long.valueOf(sendTime);
        if (lastTime + duration > minTime + 60) {//如果文书UI上的时间比文书特定时间大，取它加1分钟，否则取特定时间即可
            time = String.valueOf(lastTime + duration);
        } else {
            time = String.valueOf(minTime + 60);
        }
        return time;
    }

    /**
     * @param lastEndTime 上一个文书结束时间
     * @param sendTime    这个文书的送达时间，如果没有送达是决定
     * @param duration    文书时长
     * @return 结束时间 UTC
     */
    public static String setEndTime(long lastEndTime, String sendTime, long duration) {
        String time;
        long lastTime = lastEndTime / 1000;
        long minTime = Long.valueOf(sendTime);
        if (lastTime + duration > minTime + 60) {//如果文书UI上的时间比文书特定时间大，取它加1分钟，否则取特定时间即可
            time = String.valueOf(lastTime + duration);
        } else {
            time = String.valueOf(minTime + 60);
        }
        return time;
    }

    /**
     * 更新现场检查笔录的时间
     */
    public static void updateInquiryRecordUTC(ArrayList<InquiryRecordPhoto> photos) {
        if (photos == null || photos.size() <= 0) {
            CacheManager.getInstance().removeUTC(Config.T_ZPDZ);
            return;
        }

        long start = TimeTool.parseDate(photos.get(0).getSTARTUTC()).getTime();
        long end = TimeTool.parseDate(photos.get(0).getENDUTC()).getTime();

        for (InquiryRecordPhoto photo : photos) {
            long currentStart = TimeTool.parseDate(photo.getSTARTUTC()).getTime();
            long currentEnd = TimeTool.parseDate(photo.getENDUTC()).getTime();

            if (currentStart < start) {
                start = currentStart;
            }

            if (currentEnd > end) {
                end = currentEnd;
            }
        }

        CacheManager.getInstance().putUTC(Config.T_ZPDZ, new UTC(Config.T_ZPDZ
                , TimeTool.yyyyMMdd_HHmm.format(new Date(start))
                , TimeTool.yyyyMMdd_HHmm.format(new Date(end))));
    }

    /*
     * 把服务器返回的时间重新排下，因为某些文书决定时间不能用于开始时间
     */
    public static void setUTCtoCache(Case c) {
        if(CacheManager.getInstance().getUTC(Config.T_ADMINISTRATIVE) != null && CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_ADMINISTRATIVE).getEndUTC())){
            LiveRecordAffect(c, Config.T_ADMINISTRATIVE);
        }
        if(CacheManager.getInstance().getUTC(Config.T_TALKNOTICE) != null && CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_TALKNOTICE).getEndUTC())){
            LiveRecordAffect(c, Config.T_TALKNOTICE);
        }
        if(CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER) != null && CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER).getEndUTC())){
            LiveRecordAffect(c, Config.T_FRISTREGISTER);
        }
        if(CacheManager.getInstance().getUTC(Config.T_CARDECIDE) != null && CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_CARDECIDE).getEndUTC())){
            LiveRecordAffect(c, Config.T_CARDECIDE);
        }
        if(CacheManager.getInstance().getUTC(Config.T_CARFORM) != null && CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_CARFORM).getEndUTC())){
            RsetCarForm(c, Config.T_CARFORM);
        }
        if(CacheManager.getInstance().getUTC(Config.T_CARFORM) != null && CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT) != null && !"无".equals(CacheManager.getInstance().getUTC(Config.T_CARFORM).getEndUTC())){
            RsetCarForm(c, Config.T_CARFORM);
        }
    }

    /*
     * 扣押车辆交接单
     */
    private static void RsetCarForm(Case c, String key) {
        String sDate = LongToStr(DetainCarForm(c));
        UTC utc = CacheManager.getInstance().getUTC(key);
        utc.setStartUTC(sDate);
        CacheManager.getInstance().putUTC(key, utc);
    }

    /*
     * 谈话通知书、行政处罚决定书、先行登记通知书、扣押车辆决定书等与现场检查笔录相关的
     */
    private static void LiveRecordAffect(Case c, String key) {
        String sDate = LongToStr(getLiveCheckRecordEndTimeL(c));
        UTC utc = CacheManager.getInstance().getUTC(key);
        utc.setStartUTC(sDate);
        CacheManager.getInstance().putUTC(key, utc);
    }

    private static String LongToStr(long time){
        Date date = new Date();
        date.setTime(time * 1000);
        return TimeTool.yyyyMMdd_HHmmss.format(date);
    }
}
