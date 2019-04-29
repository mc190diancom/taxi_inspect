package com.miu360.legworkwrit.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseData;
import com.miu30.common.ui.entity.CheZuRequestModel;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu30.common.util.JacksonUtil;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.AllCaseQ;
import com.miu360.legworkwrit.mvp.model.entity.BLID;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.DriverInfoQ;
import com.miu360.legworkwrit.mvp.model.entity.InstrumentStateReq;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;

/**
 * Created by Murphy on 2018/10/11.
 */
public class RequestParamsUtil {

    public static String RequestCheZu() {
        CheZuRequestModel info = new CheZuRequestModel();
        info.setZFDWMC(MiuBaseApp.user.getString("zfdwmc", null));
        info.setSSCZ(MiuBaseApp.user.getString("sscz", null));
        info.setSSZD(MiuBaseApp.user.getString("sszd", null));
        return getJsonStr(info);
    }

    public static String RequestZFRYNameInfo() {
        queryZFRYByDWMC info = new queryZFRYByDWMC();
        info.setZFDWMC(MiuBaseApp.user.getString("zfdwmc", null));
        return getJsonStr(info);
    }

    public static String RequestCaseInfo(Case info) {
        return new Gson().toJson(info);
    }

    public static String RequestJclbInfo(JCItemQ info) {
        return BaseData.gson.toJson(info);
    }


    /*
     * 通过违法行为获取违法情形
     */
    public static String RequestWFQXInfo(String info) {
        FaGuiDetailQ fgQ = new FaGuiDetailQ();
        fgQ.setXqid(info);
        return BaseData.gson.toJson(fgQ);
    }

    /*
     * 获取案件列表
     */
    public static String RequestCaseList(String startTime, String endTime, String license, String industry, int page) {
        AllCaseQ allCaseQ = new AllCaseQ();
        if (!TextUtils.isEmpty(license)) {
            allCaseQ.setVNAME(license);
        }
        if (!TextUtils.isEmpty(startTime)) {
            allCaseQ.setSTARTUTC(startTime);
        }
        if (!TextUtils.isEmpty(endTime)) {
            allCaseQ.setENDUTC(endTime);
        }
        if (!TextUtils.isEmpty(industry)) {
            allCaseQ.setHYLB(industry);
        }
        allCaseQ.setZFZH(MiuBaseApp.user.getString("user_name", ""));
        allCaseQ.setSTARTINDEX(String.valueOf((page - 1) * 20 +1));
        allCaseQ.setSTATUS("0");
        allCaseQ.setENDINDEX(String.valueOf(page * 20));

        return getJsonEmptyStr(allCaseQ);
    }

    /*
     * 现场检查笔录
     */
    public static String RequestLiveRecordInfo(LiveCheckRecordQ info) {
        return getJsonEmptyStr(info);
    }

    /*
     * 修改文书状态
     */
    public static String RequestInstrumentState(InstrumentStateReq info) {
        return getJsonEmptyStr(info);
    }

    /*
     * 行政处罚笔录
     */
    public static String RequestAdministrativePenaltyInfo(AdministrativePenalty info) {
        return getJsonEmptyStr(info);
    }

    /*
     * 行政处罚笔录
     */
    public static String RequestLiveInfo(LiveTranscript info) {
        return getJsonEmptyStr(info);
    }

    public static String RequestBLInfo(BLID info) {
        return BaseData.gson.toJson(info);
    }

    public static String RequestVehicleInfo(VehicleInfo info) {
        return getJsonStr(info);
    }

    public static String RequestDriverInfo(DriverInfo info) {
        return getJsonStr(info);
    }

    private static String getJsonStr(Object o) {
        BaseData.trimEmptyToNull(o);
        return JacksonUtil.writeEntity2JsonStr(o);
    }

    public static String getJsonEmptyStr(Object o) {
        BaseData.trimNullToEmpty(o);
        return BaseData.gson.toJson(o);
    }
}
