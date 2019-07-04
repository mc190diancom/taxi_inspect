package com.miu360.legworkwrit.mvp.data;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu30.common.ui.entity.LawToCase;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：wanglei on 2018/10/27.
 * 邮箱：forwlwork@gmail.com
 */
@SuppressWarnings("all")
public class CacheManager {
    private Cache cache;

    private CacheManager() {
        cache = ArmsUtils.obtainAppComponentFromContext(MiuBaseApp.self).extras();
    }

    public static CacheManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CacheManager INSTANCE = new CacheManager();
    }

    /**
     * 缓存案件
     */
    public void putCase(Case c) {
        cache.put(Config.CASEKEY, c);
    }

    public void removeCase() {
        cache.remove(Config.CASEKEY);
    }

    public Case getCase() {
        return (Case) cache.get(Config.CASEKEY);
    }


    /**
     * 缓存现场检查笔录需要用到的执法稽查信息
     */
    public void putLawToLive(LawToCase law) {
        cache.put(Config.LAWTOC, law);
    }

    public LawToCase getLawToLive() {
        return (LawToCase) cache.get(Config.LAWTOC);
    }

    /**
     * 缓存区域信息
     */
    public void putDistrict(List<District> c) {
        cache.put(Config.DISTRICT, c);
    }


    public List<District> getDistrict() {
        return (List<District>) cache.get(Config.DISTRICT);
    }

    /**
     * 缓存违反行为信息
     */
    public void putIllegalDetailList(List<IllegalDetail> c) {
        cache.put(Config.ILLEGALDETAIL, c);
    }

    public List<IllegalDetail> getIllegalDetail() {
        return (List<IllegalDetail>) cache.get(Config.ILLEGALDETAIL);
    }

    /**
     * 缓存机关地址和电话信息
     */
    public void putAgencyInfos(List<AgencyInfo> c) {
        cache.put(Config.AGENCYINFOS, c);
    }

    public List<AgencyInfo> getAgencyInfo() {
        return (List<AgencyInfo>) cache.get(Config.AGENCYINFOS);
    }

    /**
     * 缓存停车场信息
     */
    public void putParkList(List<Park> c) {
        cache.put(Config.PARKS, c);
    }

    public List<Park> getParks() {
        return (List<Park>) cache.get(Config.PARKS);
    }

    /**
     * 缓存执法人员机关信息
     */
    public void putAgencyInfoByZFZH(List<AgencyInfo> c) {
        cache.put(Config.AGENCYINFOBYZFZH, c);
    }

    public List<AgencyInfo> getAgencyInfoByZFZH() {
        return (List<AgencyInfo>) cache.get(Config.AGENCYINFOBYZFZH);
    }

    /**
     * 缓存车辆信息
     */
    public void putCarInfo(List<VehicleInfo> c) {
        cache.put(Config.CAR, c);
    }

    public List<VehicleInfo> getCarInfo() {
        return (List<VehicleInfo>) cache.get(Config.CAR);
    }

    /**
     * 缓存被检查人信息
     */
    public void putDriverInfo(DriverInfo c) {
        cache.put(Config.DRIVER, c);
    }

    public DriverInfo getDriverInfo() {
        return (DriverInfo) cache.get(Config.DRIVER);
    }

    /**
     * 缓存文书的起止时间
     *
     * @param type 文书类型
     *             {@link Config#T_ADMINISTRATIVE}
     *             {@link Config#T_CARDECIDE}
     *             {@link Config#T_CARFORM}
     *             {@link Config#T_FRISTREGISTER}
     *             {@link Config#T_LIVERECORD}
     *             {@link Config#T_TALKNOTICE}
     *             {@link Config#T_ZPDZ}
     *             {@link Config#T_LIVETRANSCRIPT}
     * @param utc  文书起止时间
     */
    public void putUTC(String type, UTC utc) {
        Map map = (Map) cache.get(Config.UTCKEY);

        if (map == null) {
            map = new LinkedHashMap();
        }

        map.put(type, utc);
        cache.put(Config.UTCKEY, map);
    }

    /**
     * @param type 文书类型
     * @param isNeedChange  是否需要进行转换
     * 主要是把待确认文书的标记更改下，在时间戳上就不进行展示
     */
    public void updateUTC(String type,boolean isNeedChange) {
        Map map = (Map) cache.get(Config.UTCKEY);

        if (map == null) {
            return;
        }
        if(isNeedChange){
            type = TypeChange(type);
        }

        UTC rUtc = (UTC) map.get(type);
        if(rUtc != null){
            rUtc.setFlag("2");
            map.put(type, rUtc);
            cache.put(Config.UTCKEY, map);
        }
    }

    /*
     *  进行类型转换，把STR的文书标识转换成T文书标识
     */
    public String TypeChange(String type){
        String result = "";
        if(Config.STR_LIVERECORD.equals(type)){
            result = Config.T_LIVERECORD;
        }else if(Config.STR_ADMINISTRATIVE.equals(type)){
            result = Config.T_ADMINISTRATIVE;
        }else if(Config.STR_FRISTREGISTER.equals(type)){
            result = Config.T_FRISTREGISTER;
        }else if(Config.STR_CARFORM.equals(type)){
            result = Config.T_CARFORM;
        }else if(Config.STR_CARDECIDE.equals(type)){
            result = Config.T_CARDECIDE;
        }else if(Config.STR_LIVETRANSCRIPT.equals(type)){
            result = Config.T_LIVETRANSCRIPT;
        }else if(Config.STR_TALKNOTICE.equals(type)){
            result = Config.T_TALKNOTICE;
        }

        return result;
    }

    public UTC getUTC(String type) {
        Map map = (Map) cache.get(Config.UTCKEY);

        if (map != null) {
            return (UTC) map.get(type);
        }

        return null;
    }

    public void removeUTC(String type) {
        Map map = (Map) cache.get(Config.UTCKEY);

        if (map != null) {
            map.remove(type);
        }
    }

    public void clearUTC() {
        Map map = (Map) cache.get(Config.UTCKEY);

        if (map != null) {
            map.remove(Config.T_ADMINISTRATIVE);
            map.remove(Config.T_CARDECIDE);
            map.remove(Config.T_CARFORM);
            map.remove(Config.T_FRISTREGISTER);
            map.remove(Config.T_LIVERECORD);
            map.remove(Config.T_LIVETRANSCRIPT);
            map.remove(Config.T_TALKNOTICE);
            map.remove(Config.T_ZPDZ);
        }
    }

    /**
     * 缓存现场检查笔录
     */
    public void putLiveCheckRecord(LiveCheckRecordQ liveCheckRecordQ) {
        cache.put(Config.LIVER, liveCheckRecordQ);
    }

    /**
     * 缓存从业资格证
     */
    public void putJDKH(String jdkh) {
        cache.put(Config.JDKH, jdkh);
    }

    /**
     * 获取从业资格证
     */
    public String getJDKH() {
        return (String) cache.get(Config.JDKH);
    }


    /**
     * 缓存身份证
     */
    public void putSFZH(String sfzh) {
        cache.put(Config.SFZH, sfzh);
    }

    /**
     * 获取身份证
     */
    public String getSFZH() {
        return (String) cache.get(Config.SFZH);
    }

    /**
     * 缓存停车场信息
     */
    public void putPark(String park) {
        cache.put(Config.PARK, park);
    }

    /**
     * 获取停车场信息
     */
    public String getPark() {
        return (String) cache.get(Config.PARK);
    }

    /**
     * 缓存检查项信息
     */
    public void putJCX(List<HyRyFg> jcx) {
        cache.put(Config.JCX, jcx);
    }

    /**
     * 获取检查项信息
     */
    public List<HyRyFg> getJCX() {
        return (List<HyRyFg>) cache.get(Config.JCX);
    }

    /**
     * 缓存扣车时间
     */
    public void putDetainTime(String time) {
        cache.put(Config.TIME, time);
    }

    /**
     * 获取扣车时间
     */
    public String getDetainTime() {
        return (String) cache.get(Config.TIME);
    }

    /**
     * 缓存谈话通知书
     */
    public void putTalkNotice(LiveCheckRecordQ liveCheckRecordQ) {
        cache.put(Config.LIVER, liveCheckRecordQ);
    }

    public LiveCheckRecordQ getLiveCheckRecord() {
        return (LiveCheckRecordQ) cache.get(Config.LIVER);
    }

    public void clear() {
        cache.clear();
    }

    public void removeKey() {
        cache.remove(Config.CASEKEY);
        cache.remove(Config.UTCKEY);
        cache.remove(Config.LIVER);
        cache.remove(Config.TIME);
        cache.remove(Config.CAR);
        cache.remove(Config.PARK);
        cache.remove(Config.DRIVER);
        cache.remove(Config.LAWTOC);
        cache.remove(Config.JDKH);
        cache.remove(Config.SFZH);
    }

    /**
     * 缓存处罚案件所选笔录类型
     * 1 现场检查笔录、询问笔录、先行登记通知书、扣押车辆交接单
     * 2 现场检查笔录、询问笔录、扣押车辆决定书、现场笔录、扣押车辆交接单
     * 3 现场检查笔录、询问笔录、谈话通知书
     */
    public void putChooseInstrumentType(int type) {
        cache.put(Config.CHOOSE_TYPE_KEY, type);
    }

    public int getChooseInstrumentType() {
        if (cache.get(Config.CHOOSE_TYPE_KEY) == null) {
            return 1;
        } else {
            return (int) cache.get(Config.CHOOSE_TYPE_KEY);
        }
    }

    //----------------------------------------- 缓存打印次数 ----------------------------------------

    /**
     * 缓存文书的打印次数
     *
     * @param type  文书类型
     *              {@link Config#T_ADMINISTRATIVE}
     *              {@link Config#T_CARDECIDE}
     *              {@link Config#T_CARFORM}
     *              {@link Config#T_FRISTREGISTER}
     *              {@link Config#T_LIVERECORD}
     *              {@link Config#T_TALKNOTICE}
     *              {@link Config#T_ZPDZ}
     *              {@link Config#T_LIVETRANSCRIPT}
     * @param times 打印次数
     */
    public void putPrintTimes(String type, String times) {
        Map map = (Map) cache.get(Config.PRINT_TIMES);

        if (map == null) {
            map = new LinkedHashMap();
        }

        map.put(type, times);

        cache.put(Config.PRINT_TIMES, map);
    }

    public void putPrintTimes(List<BlType> types) {
        if (types == null || types.isEmpty()) {
            return;
        }

        Map map = (Map) cache.get(Config.PRINT_TIMES);
        if (map == null) {
            map = new LinkedHashMap();
        }

        for (BlType type : types) {
            map.put(type.getTABLENAME(), type.getPrintTimes());
        }

        cache.put(Config.PRINT_TIMES, map);
    }

    public String getPrintTimes(String type) {
        Map map = (Map) cache.get(Config.PRINT_TIMES);

        if (map != null) {
            Object o = map.get(type);

            if (o != null && o instanceof String) {
                return (String) o;
            }
        }

        return "0";
    }

    public void resetPrintTimes() {
        Map map = (Map) cache.get(Config.PRINT_TIMES);

        if (map != null) {
            Set set = map.keySet();
            for (Object s : set) {
                map.put(s, "0");
            }
        }
    }

    //---------------------------------------------------------------------------------------------

}
