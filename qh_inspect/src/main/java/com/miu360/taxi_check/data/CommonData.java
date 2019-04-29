package com.miu360.taxi_check.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.lubao.lubao.upgrade.UpdateBean;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.model.LocationDetial;
import com.miu360.taxi_check.util.UIUtils;

public class CommonData extends BaseData {
	private final static String TAG = "CommonData";

	/**
	 * 检查升级
	 *
	 * @return
	 */
	public static Result<UpdateBean> update() {
		Result<UpdateBean> result = new Result<UpdateBean>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVersionInfo");
			String param = "type=" + commonRsp.getType();
			ELog.d(TAG, "update:req:" + param);
			String res = new HttpRequest().sendPost(Config.SERVER, param);
			ELog.d(TAG, "update:res:" + res);
			parseObjectResult(result, null, res, new TypeToken<UpdateBean>() {
			}.getType());
//			result.getData().version=211111111;
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	/**
	 * 模糊查询
	 *
	 * @return
	 */
	public static Result<ArrayList<LocationDetial>> qureyLocation(String address) {
		Result<ArrayList<LocationDetial>> result = new Result<ArrayList<LocationDetial>>();
		try {
			String param = "type=queryPositionInfoByAddress&address=_address".replace("_address", address);
			String res = new HttpRequest().sendPost(Config.SERVER_POSITION, param);
			ELog.d(TAG, "req111:" + param);
			JSONObject jsonObject = new JSONObject(res);
			ELog.d(TAG, "res111:" + res);
			parseArrayResult(result, jsonObject.getString("result"), new TypeToken<ArrayList<LocationDetial>>() {
			}.getType());
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

}
