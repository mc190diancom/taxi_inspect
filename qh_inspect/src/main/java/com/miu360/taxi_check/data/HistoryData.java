package com.miu360.taxi_check.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.google.gson.reflect.TypeToken;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.common.VehicleStatus;
import com.miu360.taxi_check.model.AddressComponent;
import com.miu360.taxi_check.model.GPSPoint;
import com.miu360.taxi_check.model.LatLngDir;
import com.miu360.taxi_check.model.PathLuWangQ;
import com.miu360.taxi_check.model.RoutePlanReq;
import com.miu360.taxi_check.util.JacksonUtil;

import android.util.Log;


public class HistoryData extends BaseData {
	private final static String TAG = "HistoryData";

	public static Result<List<LatLng>> queryHistoryTrack(String vname, long startTime, long endTime) {
		Result<List<LatLng>> result = new Result<List<LatLng>>();
		ArrayList<LatLng> datas = new ArrayList<>();
		try {
			HistoryQueryReq req = new HistoryQueryReq();
			req.setVname(vname);
			req.setStartTime(startTime);
			req.setEndTime(endTime);
			String jsonStr = gson.toJson(req);
			String param = "type=queryFilterHistoryTrack&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res:" + res);
			List<GPSPoint> points = gson.fromJson(res, new TypeToken<List<GPSPoint>>() {
			}.getType());
			// 将GPS设备采集的原始GPS坐标转换成百度坐标
			CoordinateConverter converter = new CoordinateConverter();
			converter.from(CoordType.GPS);
			for (GPSPoint point : points) {
				double lat = point.getLat();
				double lon = point.getLon();
				LatLng sourceLatLng = new LatLng(lat, lon);
				// sourceLatLng待转换坐标
				converter.coord(sourceLatLng);
				LatLng desLatLng = converter.convert();
				datas.add(desLatLng);
			}
			result.setData(datas);
			result.setError(Result.OK);

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	public static Result<List<VehicleStatus>> queryHistoryTrack2(String vname, long startTime, long endTime) {
		Result<List<VehicleStatus>> result = new Result<List<VehicleStatus>>();
		ArrayList<VehicleStatus> datas = new ArrayList<>();
		try {
			HistoryQueryReq req = new HistoryQueryReq();
			req.setVname(vname);
			req.setStartTime(startTime);
			req.setEndTime(endTime);
			String jsonStr = gson.toJson(req);
			String param = "type=queryFilterHistoryTrack&jsonStr=" + jsonStr;
			ELog.d(TAG, "req22:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "reslw:" + res);
			List<GPSPoint> points = gson.fromJson(res, new TypeToken<List<GPSPoint>>() {
			}.getType());
			// 将GPS设备采集的原始GPS坐标转换成百度坐标
			CoordinateConverter converter = new CoordinateConverter();
			converter.from(CoordType.GPS);
			for (GPSPoint point : points) {
				VehicleStatus vehicle=new VehicleStatus();
				double lat = point.getLat();
				double lon = point.getLon();
				/*LatLng sourceLatLng = new LatLng(lat, lon);
				// sourceLatLng待转换坐标
				converter.coord(sourceLatLng);
				LatLng desLatLng = converter.convert();*/

				vehicle.setLat(lat);
				vehicle.setLon(lon);
				vehicle.setVEHICLE_STATUS(point.getVEHICLE_STATUS());
				vehicle.GPS_TIME=new SimpleDateFormat("yyyyMMddHHmmss").parse(point.getGpstime()).getTime();
				vehicle.AZIMUTHS=point.getAZIMUTHS();
				datas.add(vehicle);
			}
			result.setData(datas);
			result.setError(Result.OK);

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}
	// private class LatLngDir{
	// public float dir=-1;
	// public float lat=0;
	// public float lng=0;
	// }

	public static Result<List<LatLngDir>> queryBestTrack2(double start_lat, double start_lng, double end_lat,
														  double end_lng) {
		Result<List<LatLngDir>> result = new Result<List<LatLngDir>>();
		ArrayList<LatLngDir> datas = new ArrayList<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryRoutePlan");
			RoutePlanReq req = new RoutePlanReq();
			req.setOrigin("起点|" + start_lat + "," + start_lng);
			req.setDestination("终点|" + end_lat + "," + end_lng + "");
			req.setMode("driving");
			req.setRegion("北京");
			req.setOrigin_region("北京");
			req.setDestination_region("北京");
			req.setCoord_type("bd09ll");
			req.setTactics("12");

			String jsonStr = JacksonUtil.writeEntity2JsonStr(req);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req244:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res244:" + res);
			JSONObject jobj = new JSONObject(res);
			JSONArray steps = new JSONArray();
			if(0 == jobj.optInt("status")){
				steps = jobj.optJSONObject("result").optJSONArray("routes").getJSONObject(0)
						.optJSONArray("steps");
			}
			for (int i = 0; i < steps.length(); i++) {
				JSONObject o = steps.getJSONObject(i);
				String[] l = o.optString("path").split(";");
				String[] dir = o.optString("direction").split(";");

				boolean first=true;
				for (String string : l) {
					LatLngDir latLngDir = new LatLngDir();
					if (first) {
						latLngDir.dir = Double.valueOf(dir[0]);
						first=false;
					}
					// LatLng ll = new
					// LatLng(Double.valueOf(string.split(",")[1]),
					// Double.valueOf(string.split(",")[0]));
					latLngDir.lat = Double.valueOf(string.split(",")[1]);
					latLngDir.latS = string.split(",")[1];
					latLngDir.lng = Double.valueOf(string.split(",")[0]);
					datas.add(latLngDir);
				}

			}
			ELog.d("data", datas+"");
			result.setData(datas);
			result.setError(jobj.optInt("status") == 0 && "ok".equals(jobj.optString("message")) ? Result.OK : -1);

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	public static Result<List<LatLng>> queryBestTrack(double start_lat, double start_lng, double end_lat,
													  double end_lng) {
		Result<List<LatLng>> result = new Result<List<LatLng>>();
		ArrayList<LatLng> datas = new ArrayList<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryRoutePlan");
			RoutePlanReq req = new RoutePlanReq();
			req.setOrigin("起点|" + start_lat + "," + start_lng);
			req.setDestination("终点|" + end_lat + "," + end_lng + "");
			req.setMode("driving");
			req.setRegion("北京");
			req.setOrigin_region("北京");
			req.setDestination_region("北京");
			req.setCoord_type("bd09ll");
			req.setTactics("12");

			String jsonStr = JacksonUtil.writeEntity2JsonStr(req);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req244:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res244:" + res);
			JSONObject jobj = new JSONObject(res);
			JSONArray steps = jobj.optJSONObject("result").optJSONArray("routes").getJSONObject(0)
					.optJSONArray("steps");
			for (int i = 0; i < steps.length(); i++) {

				JSONObject o = steps.getJSONObject(i);
				String[] l = o.optString("path").split(";");
				for (String string : l) {
					LatLng ll = new LatLng(Double.valueOf(string.split(",")[1]), Double.valueOf(string.split(",")[0]));
					datas.add(ll);
				}

			}
			result.setData(datas);
			result.setError(jobj.optInt("status") == 0 && "ok".equals(jobj.optString("message")) ? Result.OK : -1);

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	public static Result<String> queryHistoryTrack(LatLng loc) {
		Result<String> result = new Result<String>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryPositionInfo");
			PositionReq req = new PositionReq();
			req.setLat(loc.latitude);
			req.setLon(loc.longitude);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(req);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req55:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res55:" + res);
			JSONObject jobj = new JSONObject(res);

			result.setData(jobj.optJSONObject("result").optString("formatted_address"));
			result.setError(Result.OK);

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	public static Result<String> queryLuWang(PathLuWangQ lw,int flag) {
		Result<String> result = new Result<String>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("correctAnError");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(lw);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "reqlw:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "reslw:" + res+"|"+flag);
			result.setData(res+"|"+flag);
			result.setError(Result.OK);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
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
			ELog.d(TAG, "req555:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res555:" + res);
			JSONObject jobj = new JSONObject(res);
			result.setData(jobj.optJSONObject("result").optString("formatted_address"));
			result.setError(Result.OK);
		} catch (Throwable e) {
			try {
				String fileName = "Launcher_CRS_02205"+".txt";
				File cacheDir = new File(Config.PATH+File.separator+"Launcher_Log");
				if (!cacheDir.exists())
					cacheDir.mkdir();
				File filePath = new File(cacheDir + File.separator + fileName);
				FileOutputStream fos = null;
				fos = new FileOutputStream(filePath);
				fos.write(e.toString().getBytes());
				fos.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	public static Result<AddressComponent> queryAddrByll(double lat,double lng) {
		Result<AddressComponent> result = new Result<AddressComponent>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryPositionInfo");
			PositionReq req = new PositionReq();
			req.setLat(lat);
			req.setLon(lng);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(req);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req555:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res555:" + res);
			AddressComponent ac = new AddressComponent();
			JSONObject jobj = new JSONObject(res);
			ac.setFormatted_address(jobj.optJSONObject("result").optString("formatted_address"));
			ac.setCity(jobj.optJSONObject("result").getJSONObject("addressComponent").optString("city"));
			ac.setDistrict(jobj.optJSONObject("result").getJSONObject("addressComponent").optString("district"));
			result.setData(ac);
			result.setError(Result.OK);
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
