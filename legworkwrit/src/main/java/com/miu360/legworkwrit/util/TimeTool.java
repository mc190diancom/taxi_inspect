package com.miu360.legworkwrit.util;

import android.text.TextUtils;

import com.miu30.common.config.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTool {
    // 将时间格式化为如下格式
    // 1. 今天 上午 11:00
    // 2. 04-27 下午 5:00
    // 3. 2011-03-18 上午 8:00
    public static String format(Date time, boolean showMinute) {
        Calendar c = Calendar.getInstance(); // 今天
        long jt = c.getTimeInMillis();
        int jyear = c.get(Calendar.YEAR);
        int jmonth = c.get(Calendar.MONTH);
        int jday = c.get(Calendar.DATE);

        c.add(Calendar.DATE, 1); // 明天
        int myear = c.get(Calendar.YEAR);
        int mmonth = c.get(Calendar.MONTH);
        int mday = c.get(Calendar.DATE);

        c.add(Calendar.DATE, 1); // 后天
        int hyear = c.get(Calendar.YEAR);
        int hmonth = c.get(Calendar.MONTH);
        int hday = c.get(Calendar.DATE);

        c.add(Calendar.DATE, -3); // 昨天
        int zyear = c.get(Calendar.YEAR);
        int zmonth = c.get(Calendar.MONTH);
        int zday = c.get(Calendar.DATE);

        c.add(Calendar.DATE, -1); // 前天
        int qyear = c.get(Calendar.YEAR);
        int qmonth = c.get(Calendar.MONTH);
        int qday = c.get(Calendar.DATE);

        c.setTime(time);
        long t = c.getTimeInMillis();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        // int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        StringBuffer buffer = new StringBuffer();
        if (year != jyear) {
            buffer.append(year + "-");
        }

        if (year == jyear && month == jmonth && day == jday) {
            buffer.append("今天 ");
        } else if (year == zyear && month == zmonth && day == zday) {
            buffer.append("昨天 ");
        } else if (year == myear && month == mmonth && day == mday) {
            buffer.append("明天 ");
        } else if (year == hyear && month == hmonth && day == hday) {
            buffer.append("后天 ");
        } else if (year == qyear && month == qmonth && day == qday) {
            buffer.append("前天 ");
        } else {
            buffer.append((month + 1) + "-" + day + " ");
        }

        // if (hh < 6) {
        // buffer.append("凌晨 ");
        // } else if (hh < 9) {
        // buffer.append("早上 ");
        // } else if (hh < 12) {
        // buffer.append("上午 ");
        // } else if (hh < 14) {
        // buffer.append("中午 ");
        // } else if (hh < 19) {
        // buffer.append("下午 ");
        // } else {
        // buffer.append("晚上 ");
        // }

        if (showMinute) {
            buffer.append((hh < 10 ? ("0" + hh) : hh) + ":" + (minute < 10 ? ("0" + minute) : minute));
        }
        return buffer.toString();
    }

    public static final SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat yyyyMMdd_HHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat MMdd = new SimpleDateFormat("MM-dd HH:mm:ss");
    public static final SimpleDateFormat MMddHHmm = new SimpleDateFormat("MM-dd HH:mm");
    public static final SimpleDateFormat MMddHHmm_chinese2 = new SimpleDateFormat("MM月dd日 HH:mm");
    public static final SimpleDateFormat MMddHHmm_chinese = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static final SimpleDateFormat MMddHHmm_chineseM = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    public static final SimpleDateFormat MMddHHmm_chinese1 = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat yyyyMMdd_HHmm_point = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
    public static final SimpleDateFormat HH_mm = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");

    public static String format(String timeStr, boolean showMinute) {
        try {
            return format(yyyyMMdd_HHmmss.parse(timeStr), showMinute);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatDateToyyyyMMdd_HHmm(Date date) {
        return yyyyMMdd_HHmm.format(date);
    }

    public static String formatStdChinese(String timeStr, boolean showMinute) {
        try {
            if (showMinute) {
                return MMddHHmm_chinese.format(yyyyMMdd_HHmmss.parse(timeStr));
            } else {
                return MMdd.format(yyyyMMdd_HHmmss.parse(timeStr));
            }
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * @param date yyyy-MM-dd HH:mm:ss
     * @return MM/dd HH:mm:ss
     */
    public static Date getYYHHmm(String date) {
        try {
            Date parse = yyyyMMdd_HHmm.parse(date);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String formatStdChinese(long timeStr) {
        try {
            return MMddHHmm_chineseM.format(new Date(timeStr));
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatyyyyMMdd_HHmm(long timeStr) {
        try {
            return yyyyMMdd_HHmm.format(new Date(timeStr));
        } catch (Exception e) {
            return yyyyMMdd_HHmm.format(new Date());
        }
    }

    public static String formatEndTime(String startTime,String startHintTime,long duration) {
        String sTime = TextUtils.isEmpty(startTime) ? startHintTime :startTime;
        Date sDate = new Date();
        Date eDate = new Date();
        try {
            sDate =  yyyyMMdd_HHmm.parse(sTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eDate.setTime(sDate.getTime() + duration);
        return yyyyMMdd_HHmm.format(eDate);
    }

    public static String formatStd(String timeStr, boolean showMinute) {
        try {
            if (showMinute) {
                return MMddHHmm.format(yyyyMMdd_HHmmss.parse(timeStr));
            } else {
                return MMdd.format(yyyyMMdd_HHmmss.parse(timeStr));
            }
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatStd(Date date, boolean showMinute) {
        if (showMinute) {
            return MMddHHmm.format(date);
        } else {
            return MMdd.format(date);
        }
    }

    public static Date parseDate(String s) {
        try {
            return yyyyMMdd_HHmm.parse(s);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String parseStr(String s) {
        try {
            return String.valueOf(yyyyMMdd_HHmmss.parse(s).getTime()/1000);
        } catch (Exception e) {
            return String.valueOf(new Date().getTime());
        }
    }

    public static Date parseDate2(String s) {
        try {
            return yyyyMMdd_HHmmss.parse(s);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static Date parseEndDate(String s) {
        try {
            return yyyyMMdd_HHmmss.parse(s);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static long getLongTime(String[] param) {
        long time = 0;

        Date date = new Date();
        // 设置day
        if (param[0].equals("现在")) {
            return 0;
        } else if (param[0].equals("明天")) {
            date.setDate(date.getDate() + 1);
        } else if (param[0].equals("后天")) {
            date.setDate(date.getDate() + 2);
        }

        date.setHours(Integer.parseInt(param[1]));
        date.setMinutes(Integer.parseInt(param[2]));
        date.setSeconds(0);
        time = date.getTime();
        return time;
    }

    public static Calendar getCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = yyyyMMdd_HHmmss.parse(time);
            calendar.setTime(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar;
    }

    final static int dayNames[] = {7, 1, 2, 3, 4, 5, 6};
    final static String dayNamesString[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    public static int getNowWeek() {
        int dayOfWeek = getDayOfWeek(new Date());
        return dayNames[dayOfWeek];
    }

    public static String getWeekString(Date date) {
        int dayOfWeek = getDayOfWeek(date);
        return dayNamesString[dayOfWeek];
    }

    private static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayOfWeek;
    }

    public static int getDayString(Date startDate) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (startDate.getTime() + offSet) / 86400000;
        return (int) (start - today);
    }

    public static String getFormatDate(Date date) {

        int extracted = getDayString(date);
        if (extracted > 0) {
            return MMddHHmm_chinese.format(date);
        } else if (extracted == 0) {
            return "今天 " + HH_mm.format(date);
        } else if (extracted == -1) {
            return "昨天 " + HH_mm.format(date);
        } else {
            int s = getNowWeek();
            if (s - Math.abs(extracted) >= 0) {
                return getWeekString(date) + HH_mm.format(date);
            } else {
                if (Math.abs(extracted) < (s + 7)) {
                    return "上" + getWeekString(date) + " " + HH_mm.format(date);
                } else {
                    String format = MMddHHmm_chinese.format(date);
                    String format1 = yyyy.format(new Date());
                    if (format.startsWith(format1)) {
                        return MMddHHmm_chinese2.format(date);
                    }
                    return format;

                }
            }
        }
    }

    /**
     * 返回日期格式为今天、明天、后天 + 时分
     */
    public static String getFormatDate2(Date date) {
        int extracted = getDayString(date);
        if (extracted == 0) {
            return "今天  " + HH_mm.format(date);
        } else if (extracted == 1) {
            return "明天  " + HH_mm.format(date);
        } else if (extracted == 2) {
            return "后天  " + HH_mm.format(date);
        } else {
            return MMddHHmm_chinese.format(date);
        }
    }

    public static Calendar getCalendarFromStr(String s) {
        if (!TextUtils.isEmpty(s)) {
            Date date = parseDate(s);

            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        }

        return null;
    }
}
