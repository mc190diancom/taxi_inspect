package com.miu30.common.config;

import android.os.Environment;

import com.miu360.library.BuildConfig;

/**
 * Created by Murphy on 2018/10/8.
 */
public class Config {
    public final static String DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/jicha/";
    public final static String PATH = Environment.getExternalStorageDirectory().getPath() + "/qh_inspect/";
    public final static String PATHROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String FILE_NAME = "inspector.txt";
    public final static String CASE_TEMP = "case.txt";
    public final static String CASE_TEMPS = "cases.txt";
    public final static String FILE_NAME2 = "temp.txt";
    public final static String ID_FILE_NAME = "cardqr.txt";
    public static final String IP = BuildConfig.IP;
    public static final String SERVER_BASIC = "http://" + IP + BuildConfig.PORT;
    public static final String SERVER = SERVER_BASIC + "requestApi";
    public static final String SERVER_POSITION = SERVER + "/common";
    public static final String SERVER_SAVEINFO = SERVER + "/bs_Taxi";
    public static final String SERVER_ZFRY = SERVER + "/zfry";
    public static final String SERVER_OTHER = SERVER + "/other";
    public static final String SERVER_WAIQIN = SERVER + "/wqxcjcbl";
    public static final String SERVER_BLLIST = SERVER + "/bllist";
    public static final String SERVER_WEBSERVICE = SERVER + "/webservice";
    public static final String SERVER_DOWNLOAD = SERVER + "/transfers";
    public static final String SERVER_VIDEO = SERVER + "/video";
    public static final String SERVER_TAXIINFO = SERVER + "/app_Taxi";
    public static final String SERVER_SIGN = SERVER + "/sign";

    public static final int LAWLOCATION = 1;

    //缓存KEY
    public static final String CASEKEY = "case";//案件
    public static final String UTCKEY = "utc";//时间
    public static final String LIVER = "live";//现场检查笔录
    public static final String PARK = "park";//停车场
    public static final String TIME = "time";//扣车时间
    public static final String CAR = "car";//车辆信息
    public static final String DRIVER = "driver";//被检查人信息
    public static final String JCX = "jcx";//检查项信息
    public static final String LAWTOC = "lawToC";//现场检查笔录需要用到的执法稽查信息
    public static final String JDKH = "jdkh";//监督卡号

    public static final String DISTRICT = "district";//区域
    public static final String ILLEGALDETAIL = "illegalDetail";//违法行为
    public static final String AGENCYINFOS = "agencyInfos";//机关信息
    public static final String PARKS = "parks";//停车场
    public static final String AGENCYINFOBYZFZH = "agencyOfZFZH";//执法账号机关信息


    public static final int SERVICE_PRINT = 1;
    public static final int SERVICE_CAR = 2;

    public static final String CHOOSE_TYPE_KEY = "choose_type";
    public static final String PRINT_TIMES = "print_times";//打印次数

    //eventbus发送code
    public static final String SELECTADD = "HandLocation";//手动定位
    public static final String ILLEGAL = "Illegal";//违法行为
    public static final String UPDATECASE = "UpdateCase";//更新了案件信息过后，返回对应文书操作
    public static final String UPDATECASESTATUS = "UpdateCaseStatus";//修改通用数据或者文书时间后，后续文书状态改变

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_EMPTY = 1;
    public static final int RESULT_ERROR = 2;

    //约定的每个文书最小时长
    public static final long UTC_LIVERECORD = 11 * 60;//北京市交通执法总队现场检查笔录(路检)
    public static final long UTC_TALKNOTICE = 6 * 60;//北京市交通执法总队谈话通知书
    public static final long UTC_FRISTREGISTER = 8 * 60;//北京市交通执法总队证据先行登记保存通知书
    public static final long UTC_CARDECIDE = 8 * 60;//北京市交通执法总队扣押车辆决定书
    public static final long UTC_LIVETRANSCRIPT = 6 * 60;//北京市交通执法总队现场笔录
    public static final long UTC_ADMINISTRATIVE = 10 * 60;//北京市交通执法总队当场行政处罚决定书(警告)
    public static final long UTC_CARFORM = 5 * 60;//北京市交通执法总队执法暂扣车辆交接单
    public static final long UTC_ZPDZ = 3 * 60;//北京市交通执法总队现场拍照

    //与服务端约定的文书type;文书1.0用到(这个是用的表名；为什么有两个)
    public static final String T_LIVERECORD = "T_BJSJTZFZD_XCJCBL";//北京市交通执法总队现场检查笔录(路检)
    public static final String T_TALKNOTICE = "T_BJSJTZFZD_THTZS";//北京市交通执法总队谈话通知书
    public static final String T_FRISTREGISTER = "T_BJSJTZFZD_ZJXXDJ_BCTZS";//北京市交通执法总队证据先行登记保存通知书
    public static final String T_CARDECIDE = "T_BJSJTZFZD_KYCLJDS";//北京市交通执法总队扣押车辆决定书
    public static final String T_LIVETRANSCRIPT = "T_BJSJTZFZD_XCBL";//北京市交通执法总队现场笔录
    public static final String T_ADMINISTRATIVE = "T_BJSJTZFZD_DCXZCFJDS";//北京市交通执法总队当场行政处罚决定书(警告)
    public static final String T_CARFORM = "T_BJSJTZFZD_ZFZKCLJJD";//北京市交通执法总队执法暂扣车辆交接单
    public static final String T_ZPDZ = "T_BJSJTZFZD_ZPDZ";//北京市交通执法总队现场拍照

    //与服务端约定的文书type;文书1.0用到
    public static final String LIVERECORD = "XCJCBL1";//北京市交通执法总队现场检查笔录(路检)
    public static final String TALKNOTICE = "THTZS";//北京市交通执法总队谈话通知书
    public static final String FRISTREGISTER = "BSCTZS";//北京市交通执法总队证据先行登记保存通知书
    public static final String CARDECIDE = "KYCLJDS";//北京市交通执法总队扣押车辆决定书
    public static final String LIVETRANSCRIPT = "XCBL";//北京市交通执法总队现场笔录
    public static final String ADMINISTRATIVE = "DCXZCFJDS";//北京市交通执法总队当场行政处罚决定书(警告)
    public static final String CARFORM = "CLJJD";//北京市交通执法总队执法暂扣车辆交接单
    public static final String ZPDZ = "ZPDZ";//北京市交通执法总队现场拍照

    //与服务端约定的文书type;文书1.0用到
    public static final String ID_LIVERECORD = "d6b562595aa04fb6b1d91e288c0f5846";//北京市交通执法总队现场检查笔录(路检)
    public static final String ID_TALKNOTICE = "e22671de33bd4e20a8f346f53d2ac44f";//北京市交通执法总队谈话通知书
    public static final String ID_FRISTREGISTER = "612bd85d96af4cebb264f265d2220e7e";//北京市交通执法总队证据先行登记保存通知书
    public static final String ID_CARDECIDE = "16e82e98ef4c4f7e90b08151d20f6f52";//北京市交通执法总队扣押车辆决定书
    public static final String ID_LIVETRANSCRIPT = "e6b230fb5c094aa2a0babc1aaa1eeed7";//北京市交通执法总队现场笔录
    public static final String ID_ADMINISTRATIVE = "575fd68709f246a6b0c068d254a75a04";//北京市交通执法总队当场行政处罚决定书(警告)
    public static final String ID_CARFORM = "a51d884d058344889bd7275b09fbab24";//北京市交通执法总队执法暂扣车辆交接单
    public static final String ID_ZPDZ = "5830a5fb061f40159e866e1124093b3c";//北京市交通执法总队现场拍照

    //与服务端约定的文书type;文书1.0用到
    public static final String STR_LIVERECORD = "现场检查笔录（路检）";//北京市交通执法总队现场检查笔录(路检)
    public static final String STR_TALKNOTICE = "谈话通知书";//北京市交通执法总队谈话通知书
    public static final String STR_FRISTREGISTER = "先行登记通知书";//北京市交通执法总队证据先行登记保存通知书
    public static final String STR_CARDECIDE = "扣押车辆决定书";//北京市交通执法总队扣押车辆决定书
    public static final String STR_LIVETRANSCRIPT = "现场笔录";//北京市交通执法总队现场笔录
    public static final String STR_ADMINISTRATIVE = "行政处罚决定书";//北京市交通执法总队当场行政处罚决定书(警告)
    public static final String STR_CARFORM = "扣押车辆交接单";//北京市交通执法总队执法暂扣车辆交接单
    public static final String STR_ZPDZ = "询问笔录";//北京市交通执法总队现场拍照

    //文书2.0用到
    public static final String ZLGZTZS = "ZLGZTZS";//北京市交通执法总队责令(限期)改正通知书
    public static final String XCJCBL2 = "XCJCBL2";//北京市交通执法总队现场检查笔录
    public static final String XWBL_HXY1 = "XWBL_HXY1";//北京市交通执法总队询问笔录1(黑巡游)
    public static final String XWBL_HWY1 = "XWBL_HWY1";//北京市交通执法总队询问笔录1(黑网约)
    public static final String XWBL1 = "XWBL1";//北京市交通执法总队询问笔录1
    public static final String XWBL_HXY2 = "XWBL_HXY2";//北京市交通执法总队询问笔录2(黑巡游)
    public static final String XWBL_HXY3 = "XWBL_HXY3";//北京市交通执法总队询问笔录3(黑巡游)
    public static final String XWBL_HWY2 = "XWBL_HWY2";//北京市交通执法总队询问笔录2(黑网约)
    public static final String XWBL_HWY3 = "XWBL_HWY3";//北京市交通执法总队询问笔录3(黑网约)
    public static final String XWBL2 = "XWBL2";//北京市交通执法总队询问笔录2
    public static final String XWBL3 = "XWBL3";//北京市交通执法总队询问笔录3

}
