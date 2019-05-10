package com.miu30.common.data;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.ExceptionHandler;
import com.miu30.common.async.HttpRequest;
import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.HistoryInspectCountRecordInfo;
import com.miu30.common.ui.entity.HistoryInspectRecordModel;
import com.miu30.common.ui.entity.LocationDetial;
import com.miu30.common.ui.entity.Template;
import com.miu30.common.util.JacksonUtil;
import com.miu360.library.R;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static com.miu30.common.base.BaseData.parseArrayResult;
import static com.miu30.common.base.BaseData.parseObjectResult2;
import static com.miu30.common.base.BaseData.trimEmptyToNull;

/**
 * Created by Murphy on 2018/10/8.
 */
public class CommonData {
    public static String TAG = "CommonData";


    /**
     * 模糊查询
     *
     * @return
     */
    public static Result<ArrayList<LocationDetial>> qureyLocation(String address) {
        Result<ArrayList<LocationDetial>> result = new Result<>();
        try {
            String param = "type=queryPositionInfoByAddress&address=_address".replace("_address", address);
            String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
            if(TextUtils.isEmpty(res)){
                result.setError(-1);
                result.setMsg("地址获取失败");
            }else{
                JSONObject jsonObject = new JSONObject(res);
                parseArrayResult(result, jsonObject.getString("result"), new TypeToken<ArrayList<LocationDetial>>() {
                }.getType());
            }

        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setMsg(MiuBaseApp.self.getString(R.string.unknown_error));
            result.setThrowable(e);
            ExceptionHandler.handleException(MiuBaseApp.self, result);
        }

        return result;

    }

    // 查询稽查记录
    public static Result<List<HistoryInspectRecordModel>> queryHistoryInsepctRecordInfo(
            HistoryInspectCountRecordInfo info) {
        Result<List<HistoryInspectRecordModel>> result = new Result<List<HistoryInspectRecordModel>>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("queryRegisterInfoByName_Taxi");
            trimEmptyToNull(info);
            String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
            parseArrayResult(result, res, new TypeToken<List<HistoryInspectRecordModel>>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setMsg(MiuBaseApp.self.getString(R.string.unknown_error));
            result.setThrowable(e);
            ExceptionHandler.handleException(MiuBaseApp.self, result);
        }
        return result;
    }

    public static Result<String> queryHistoryTrack1(double lat,double lng) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("queryPositionInfo");
            PositionReq req = new PositionReq();
            req.setLat(lat);
            req.setLon(lng);
            String jsonStr = JacksonUtil.writeEntity2JsonStr(req);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            String res = HttpRequest.sendPost(Config.SERVER, param);
            JSONObject jobj = new JSONObject(res);
            result.setData(jobj.optJSONObject("result").optString("formatted_address"));
            result.setError(Result.OK);

        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setMsg(MiuBaseApp.self.getString(R.string.unknown_error));
            result.setThrowable(e);
            ExceptionHandler.handleException(MiuBaseApp.self, result);
        }

        return result;

    }

    public static Result<List<Template>> selectTemplateAll() {
        Result<List<Template>> result = new Result<>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("selectTemplateList");
            String param = "type=" + commonRsp.getType();
            String res = HttpRequest.sendPost(Config.SERVER_WAIQIN, param);
            parseObjectResult2(result, "data", res, new TypeToken<List<Template>>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setMsg(MiuBaseApp.self.getString(R.string.unknown_error));
            result.setThrowable(e);
            ExceptionHandler.handleException(MiuBaseApp.self, result);
        }

        return result;

    }
}
