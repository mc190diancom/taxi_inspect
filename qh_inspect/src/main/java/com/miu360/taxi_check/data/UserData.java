package com.miu360.taxi_check.data;

import com.google.gson.reflect.TypeToken;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.model.BindCamera;
import com.miu360.taxi_check.model.ChangeData;
import com.miu360.taxi_check.model.ExitStatus;
import com.miu360.taxi_check.model.FenBuInfo;
import com.miu360.taxi_check.model.HistoryIDCard;
import com.miu360.taxi_check.model.NoticeImage;
import com.miu360.taxi_check.model.NoticeModel;
import com.miu360.taxi_check.model.PutInspectDataReturn;
import com.miu360.taxi_check.model.RegisterInfo;
import com.miu360.taxi_check.model.RegisterInfoNew;
import com.miu360.taxi_check.model.User;
import com.miu360.taxi_check.model.Version;
import com.miu360.taxi_check.model.ZhiFaJianChaModel;
import com.miu360.taxi_check.util.TelphoneUtil;

import org.json.JSONObject;

import java.util.List;

public class UserData extends BaseData {
    private final static String TAG = "UserData";

    public static Result<String> login(String username, String pwd) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            User req = new User();
            commonRsp.setType("login");
            req.setUserName(username);
            req.setPassword(pwd);
            String jsonStr = gson.toJson(req);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req247:" + param);
            String res = HttpRequest.sendPost(Config.SERVER, param);
            ELog.d(TAG, "res247:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);

            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<String> login_new(String username, String pwd) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            User req = new User();
            commonRsp.setType("login");
            req.setUserName(username);
            req.setPassword(pwd);
            String jsonStr = gson.toJson(req);
            /*
             * sim_code 电话号码
             * imei     Device id
             */
            //String param = "type=" + commonRsp.getType() + "&jsonStr=" + "{\"userName\":\"" + username + "\",\"password\":\"" + pwd + "\",\"sim_code\":null,\"imei_code\":null,\"login_update_time\":0}";

            String param = "type=" + commonRsp.getType() + "&jsonStr=" + "{\"userName\":\"" + username
                    + "\",\"password\":\"" + pwd
                    + "\",\"sim_code\":\"" + TelphoneUtil.getTelphoneNumber()
                    + "\",\"imei_code\":\"" + TelphoneUtil.getIMEI()
                    + "\",\"login_update_time\":0"
                    + ",\"sign\":\"app客户端登录\"}";

            ELog.d(TAG, "req247:" + param);

            String res = HttpRequest.sendPost(Config.SERVER, param);
            ELog.d(TAG, "res247:" + res);
            System.out.println("req2417:" + res);
            JSONObject jsonObject = new JSONObject(res);
            result.setError(jsonObject.optInt("err"));
            result.setErrorMsg(jsonObject.optString("errorMsg"));
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<Long> changePassWord(ChangeData data) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("updateZfryInfo");
            String jsonStr = gson.toJson(data);

            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_ZFRY, param);
            ELog.d(TAG, "res:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<Long> shangChuanFenBuData(FenBuInfo info) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("savePersonFbInfo");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_FENBU, param);
            ELog.d(TAG, "res:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    /**
     * 绑定摄像头
     *
     * @param info
     * @return
     */
    public static Result<String> bindCamera(BindCamera info) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("bindingCamera");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req66:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
            ELog.d(TAG, "res66:" + res);
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    /**
     * 解绑摄像头
     *
     * @param info
     * @return
     */
    public static Result<String> removeBindCamera(BindCamera info) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("removeBindingCamera");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req66:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
            ELog.d(TAG, "res66:" + res);
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<Long> shangChuanHistoryData(HistoryIDCard info) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("save_Certificates_Info");
            trimNullToEmpty(info);
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
            ELog.d(TAG, "res:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<Long> shangChuanHistoryQrData(HistoryIDCard info) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("saveCertificatesInfo");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req21:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
            ELog.d(TAG, "res21:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<Long> shangChuanData(RegisterInfo info) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("saveRegisterInfo_bs");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req1:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
            ELog.d(TAG, "res1:" + res);
            parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    /**
     * 执法稽查--登记
     *
     * @param info
     * @return
     */
    public static Result<PutInspectDataReturn> PutInspectData(RegisterInfo info) {
        Result<PutInspectDataReturn> result = new Result<PutInspectDataReturn>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("saveRegisterInfo");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req242:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_XINXI, param);
            ELog.d(TAG, "res242:" + res);
            parseObjectResult1(result, "", res, new TypeToken<PutInspectDataReturn>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    /**
     * 新的执法稽查--登记
     *
     * @param info
     * @return
     */
    public static Result<PutInspectDataReturn> PutInspectDataNew(RegisterInfoNew info) {
        Result<PutInspectDataReturn> result = new Result<PutInspectDataReturn>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("saveRegisterInfo_Attach");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req242new:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_XINXI, param);
            ELog.d(TAG, "res242new:" + res);
            parseObjectResult1(result, "", res, new TypeToken<PutInspectDataReturn>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    /**
     * 执法办上传接口
     */

    public static Result<PutInspectDataReturn> PutZhiFaBanData(ZhiFaJianChaModel info) {
        Result<PutInspectDataReturn> result = new Result<PutInspectDataReturn>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("saveFzbInfo");
            trimNullToEmpty(info);
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req555:" + Config.SERVER_FZBSAVE + "?" + param);
            String res = HttpRequest.sendPost(Config.SERVER_FZBSAVE, param);
            ELog.d(TAG, "res556:" + res);
            parseObjectResult1(result, "", res, new TypeToken<PutInspectDataReturn>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<String> queryDingYueYuJing(String userName, String range, String lat, String lon) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("queryDyMessage");
            String param = "type=" + commonRsp.getType() + "&username=" + userName + "&lon=" + lon + "&lat=" + lat
                    + "&range=" + range + "&flag=" + Config.FLAG;
            ELog.d(TAG, "req11:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
            ELog.d(TAG, "res11:" + res);
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    public static Result<String> queryCancalYuJing(String userName) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("queryQxMessage");
            String param = "type=" + commonRsp.getType() + "&username=" + userName + "&flag=" + Config.FLAG;
            ELog.d(TAG, "req:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
            ELog.d(TAG, "res:" + res);
            result.setData(res);
            // parseObjectResult(result, "userId", res, new TypeToken<Long>() {
            // }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }

        return result;

    }

    //定时，判断是否在线
    public static Result<String> updateLoginStatus(String zfzh) {
        Result<String> result = new Result<String>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("updateLoginConnectionStatus");
            String param = "type=" + commonRsp.getType() + "&zfzh=" + zfzh;
            ELog.d(TAG, "shq:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
            ELog.d(TAG, "she:" + res);
            result.setData(res);
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }
        return result;
    }

    //定时，判断是否在线
    public static Result<Long> updateExitStatus(ExitStatus zfzh) {
        Result<Long> result = new Result<Long>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("update_login_status");
            trimEmptyToNull(zfzh);
            String jsonStr = gson.toJson(zfzh);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req33:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
            ELog.d(TAG, "res33:" + res);
            parseObjectResult(result, null, res, new TypeToken<Long>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }
        return result;
    }

    //查询APK版本号
    public static Result<List<Version>> queryVersion() {
        Result<List<Version>> result = new Result<List<Version>>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("getDownloadApkInfo");
            String param = "type=" + commonRsp.getType() + "&flag=出租";
            ELog.d(TAG, "req110:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
            ELog.d(TAG, "res110:" + res);
            parseArrayResult(result, res, new TypeToken<List<Version>>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }
        return result;
    }


    public static Result<List<NoticeImage>> queryNotice(NoticeModel info) {
//		/requestApi_Taxi?type=AppNotice&jsonStr=
//		{""}

        Result<List<NoticeImage>> result = new Result<List<NoticeImage>>();
        try {
            RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
            commonRsp.setType("AppNotice");
            String jsonStr = gson.toJson(info);
            String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
            ELog.d(TAG, "req443:" + param);
            String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
            String resNew = res.split("\"rows\":")[1];
            resNew = resNew.substring(0, resNew.length() - 1);
            ELog.d(TAG, "res443:" + resNew);
            parseArrayResult(result, resNew, new TypeToken<List<NoticeImage>>() {
            }.getType());
            ELog.d(TAG, "res445:" + result.getData().size());
        } catch (Throwable e) {
            e.printStackTrace();
            result.setError(-1);
            result.setErrorMsg(App.self.getString(R.string.sys_err));
            result.setThrowable(e);
            ExceptionHandler.handleException(App.self, result);
        }
        return result;
    }
}
