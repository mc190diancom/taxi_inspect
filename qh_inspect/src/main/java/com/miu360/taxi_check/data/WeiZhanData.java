package com.miu360.taxi_check.data;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.model.entity.AllCaseQ;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.model.AlarmReason;
import com.miu360.taxi_check.model.AllPosition;
import com.miu360.taxi_check.model.BindCamera;
import com.miu360.taxi_check.model.CPosition;
import com.miu360.taxi_check.model.Camera;
import com.miu360.taxi_check.model.CameraQ;
import com.miu360.taxi_check.model.CarCarInfo;
import com.miu360.taxi_check.model.CarNum;
import com.miu30.common.ui.entity.CheZuRequestModel;
import com.miu30.common.ui.entity.CheZuResultModel;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.model.CompanyNameByCode;
import com.miu360.taxi_check.model.Driver;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.taxi_check.model.DriverInfoByOther;
import com.miu360.taxi_check.model.DriverNameByCode;
import com.miu360.taxi_check.model.DriverNameInfoQ;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu360.taxi_check.model.FaGuiDetailAllInfo;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu360.taxi_check.model.FaGuiQueryInfo;
import com.miu360.taxi_check.model.FastInspectLvYouQueryWeiFaInfo;
import com.miu360.taxi_check.model.FazhiBanAllQuenyModel;
import com.miu360.taxi_check.model.HYLBInfo;
import com.miu30.common.ui.entity.HYTypeQ;
import com.miu360.taxi_check.model.HaiYun;
import com.miu360.taxi_check.model.HistoryIDCard;
import com.miu360.taxi_check.model.HistoryInspectCountRecordInfo;
import com.miu360.taxi_check.model.HistoryInspectRecordModel;
import com.miu360.taxi_check.model.HistoryInspectRecordModelNew;
import com.miu360.taxi_check.model.HuoYunInfo;
import com.miu360.taxi_check.model.HuoYunPeopleInfo;
import com.miu360.taxi_check.model.HuoYunWeiFaInfo;
import com.miu360.taxi_check.model.HuoYunYeHuInfo;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu360.taxi_check.model.InspectTongJi;
import com.miu360.taxi_check.model.InspectTongJiQ;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu360.taxi_check.model.JCLBQ;
import com.miu360.taxi_check.model.JclbModel;
import com.miu360.taxi_check.model.LvYouWeiFaInfo;
import com.miu360.taxi_check.model.LvyouDriverInfo;
import com.miu360.taxi_check.model.LvyouInfo;
import com.miu360.taxi_check.model.LvyouYehuInfo;
import com.miu360.taxi_check.model.NetPersonQrInfo;
import com.miu360.taxi_check.model.PersonPosition;
import com.miu360.taxi_check.model.QueryFaZhiBanResult;
import com.miu360.taxi_check.model.RoateInfo;
import com.miu360.taxi_check.model.ShengJiInfo;
import com.miu360.taxi_check.model.ShengJiPeopleInfo;
import com.miu360.taxi_check.model.ShengJiWeiFaInfo;
import com.miu360.taxi_check.model.ShuiYun;
import com.miu360.taxi_check.model.ShuiYunQ;
import com.miu360.taxi_check.model.TaxiCarInfo;
import com.miu360.taxi_check.model.TaxiCompany;
import com.miu360.taxi_check.model.TaxiCompanyQ;
import com.miu360.taxi_check.model.TaxiWfInfo;
import com.miu360.taxi_check.model.TaxiWfInfoQ;
import com.miu360.taxi_check.model.UnboundCamera;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.model.WaterTranspt;
import com.miu360.taxi_check.model.WaterTransptQ;
import com.miu360.taxi_check.model.WeiFaInfo;
import com.miu360.taxi_check.model.WeiFa_New;
import com.miu360.taxi_check.model.WeiFa_NewQ;
import com.miu360.taxi_check.model.WeiXiu;
import com.miu360.taxi_check.model.WeiXiuQ;
import com.miu360.taxi_check.model.ZFRYDetailInfo;
import com.miu360.taxi_check.model.ZfryInfo;
import com.miu360.taxi_check.model.ZhuLinYeHuInfo;
import com.miu360.taxi_check.model.ZuLinInfo;
import com.miu360.taxi_check.model.ZuLinWeiFaInfo;
import com.miu360.taxi_check.model.Zwstatus;
import com.miu360.taxi_check.model.checkItemQ;
import com.miu360.taxi_check.model.groupName;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.taxi_check.util.JacksonUtil;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

public class WeiZhanData extends BaseData {
	private final static String TAG = "HistoryData";

	/**
	 *            京BH8038
	 *            200508170000
	 *            200806220849
	 * @return
	 */
	public static Result<List<WeiFaInfo>> queryWeiFaInfo(WeiFaInfo info) {
		Result<List<WeiFaInfo>> result = new Result<List<WeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req34:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res34:" + res);
			parseArrayResult(result, res, new TypeToken<List<WeiFaInfo>>() {
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


	//	查询违法信息
	public static Result<List<WeiFa_New>> queryWeiFaInfo_New(WeiFa_NewQ info) {
		Result<List<WeiFa_New>> result = new Result<List<WeiFa_New>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req141:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_DAOLUINFO, param);
			ELog.d(TAG, "res141:" + res);
			if(res.getBytes().length > 10 * 1024 * 1024){
				result.setError(-1);
				result.setErrorMsg("数据量过大，请重新检索查询");
			}else{
				parseArrayResult(result, res, new TypeToken<List<WeiFa_New>>() {
				}.getType());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			if(e.toString() != null && e.toString().contains("OutOfMemoryError")){
				result.setErrorMsg("数据量过大，请重新检索查询");
			}
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;
	}

	//	查询行业类别
	public static Result<List<HYLBInfo>> queryHYLB() {
		Result<List<HYLBInfo>> result = new Result<List<HYLBInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("loadHylb");
			String param = "type=" + commonRsp.getType();
			ELog.d(TAG, "req141:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_DAOLUINFO, param);
			ELog.d(TAG, "res141:" + res);
			parseArrayResult(result, res, new TypeToken<List<HYLBInfo>>() {
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
	// 查询车辆信息
	public static Result<TaxiCarInfo> queryTaxiCarInfo(CarNum carNum) {
		Result<TaxiCarInfo> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("taxi_info");
			// trimEmptyToNull(Vname);
			String jsonStr = gson.toJson(carNum);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "re57:" + param);
			long t =System.currentTimeMillis();
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "re57:" + res);
			ELog.d(TAG, "re57:" + (System.currentTimeMillis()-t));
			parseObjectResult1(result, "", res, new TypeToken<TaxiCarInfo>() {
			}.getType());

			/*
			 * parseArrayResult(result, res, new TypeToken<List<TaxiCarInfo>>()
			 * { }.getType());
			 */
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	// 查询违法车辆信息
	public static Result<List<TaxiCarInfo.Ssp>> queryKeYiTaxiCarInfo(CarNum carNum) {
		Result<List<TaxiCarInfo.Ssp>> result = new Result<List<TaxiCarInfo.Ssp>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaCar_Taxi");
			// trimEmptyToNull(Vname);
			String jsonStr = gson.toJson(carNum);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);

			parseArrayResult(result, res, new TypeToken<List<TaxiCarInfo.Ssp>>() {
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

	public static Result<List<VehicleInfo>> queryVehicleInfo(VehicleInfo info) {
		Result<List<VehicleInfo>> result = new Result<List<VehicleInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVehicleInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			System.out.println(result);

			ELog.d(TAG, "req26:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res26:" + res);
			parseArrayResult(result, res, new TypeToken<List<VehicleInfo>>() {
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

	public static Result<List<DriverInfo>> queryDriverInfo(DriverInfo info) {
		Result<List<DriverInfo>> result = new Result<List<DriverInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryDriverInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req55:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res55:" + res);
			parseArrayResult(result, res, new TypeToken<List<DriverInfo>>() {
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
	 * 根据姓名、身份证、从业资格证号查人员信息--执法稽查
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<DriverInfoByOther>> queryDriverInfo(DriverNameInfoQ info) {
		Result<List<DriverInfoByOther>> result = new Result<List<DriverInfoByOther>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getBasicInfo");
			//trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req13:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_DAOLUINFO, param);
			ELog.d(TAG, "res13:" + res);
			parseArrayResult(result, res, new TypeToken<List<DriverInfoByOther>>() {
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

	public static Result<List<AlarmReason>> queryAlarmType() {
		Result<List<AlarmReason>> result = new Result<List<AlarmReason>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("querySuspiciousType");
			String param = "type=" + commonRsp.getType();
			ELog.d(TAG, "req155:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res155:" + res);
			parseArrayResult(result, res, new TypeToken<List<AlarmReason>>() {
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

	public static Result<List<CompanyInfo>> queryYeHuInfo(CompanyInfo info) {
		Result<List<CompanyInfo>> result = new Result<List<CompanyInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryYeHuInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<CompanyInfo>>() {
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

	public static Result<List<PersonPosition>> queryDaDuiNameInfo(PersonPosition info) {
		Result<List<PersonPosition>> result = new Result<List<PersonPosition>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCorpName");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_FENBU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<PersonPosition>>() {
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

	public static Result<AllPosition> queryAllPositionInfo() {
		Result<AllPosition> result = new Result<AllPosition>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("zfryfb_Info_All");
			String param = "type=" + commonRsp.getType();
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);
			parseObjectResult1(result, "", res, new TypeToken<AllPosition>() {
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

	public static Result<List<PersonPosition>> queryPositionInfo(PersonPosition info) {
		Result<List<PersonPosition>> result = new Result<List<PersonPosition>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryPersonPosition");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_FENBU, param);
			ELog.d(TAG, "res:" + res);
			Log.e("positionList","positionList:"+res);
			parseArrayResult(result, res, new TypeToken<List<PersonPosition>>() {
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

	public static Result<List<DriverNameByCode>> queryDriverNameInfo(DriverNameByCode info) {
		Result<List<DriverNameByCode>> result = new Result<List<DriverNameByCode>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryNameByCode");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<DriverNameByCode>>() {
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

	public static Result<List<FastInspectLvYouQueryWeiFaInfo>> queryFastLvYouWeiFaInfo(
			FastInspectLvYouQueryWeiFaInfo info) {
		Result<List<FastInspectLvYouQueryWeiFaInfo>> result = new Result<List<FastInspectLvYouQueryWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaCar");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<FastInspectLvYouQueryWeiFaInfo>>() {
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

	public static Result<List<FastInspectLvYouQueryWeiFaInfo>> queryLvYouPeopleWeiFaInfo(
			FastInspectLvYouQueryWeiFaInfo info) {
		Result<List<FastInspectLvYouQueryWeiFaInfo>> result = new Result<List<FastInspectLvYouQueryWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaPerson");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<FastInspectLvYouQueryWeiFaInfo>>() {
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

	public static Result<TaxiWfInfo> queryTaxiPeopleWeiFaInfo(TaxiWfInfoQ info) {
		Result<TaxiWfInfo> result = new Result<TaxiWfInfo>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("taxi_Wf_Info");
			// trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req57:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res57:" + res);
			parseObjectResult1(result, "", res, new TypeToken<TaxiWfInfo>() {
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

	public static Result<TaxiCompany> queryTaxiCompanyInfo(TaxiCompanyQ info) {
		Result<TaxiCompany> result = new Result<TaxiCompany>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("basicInfo_AndWfInfo_Taxi");
			// trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res23:" + res);
			parseObjectResult1(result, "", res, new TypeToken<TaxiCompany>() {
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

	public static Result<List<FastInspectLvYouQueryWeiFaInfo>> queryLvYouCompanyWeiFaInfo(
			FastInspectLvYouQueryWeiFaInfo info) {
		Result<List<FastInspectLvYouQueryWeiFaInfo>> result = new Result<List<FastInspectLvYouQueryWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaYeHu");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res23:" + res);
			parseArrayResult(result, res, new TypeToken<List<FastInspectLvYouQueryWeiFaInfo>>() {
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

	public static Result<List<ZFRYDetailInfo>> queryZFRYinfo(ZFRYDetailInfo info) {
		Result<List<ZFRYDetailInfo>> result = new Result<List<ZFRYDetailInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getZfryInfoByZfzh");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req222:" + param);
			System.out.println("req2417:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZFRY, param);
			ELog.d(TAG, "res222:" + res);
			System.out.println("req2417:" + res);
			parseArrayResult(result, res, new TypeToken<List<ZFRYDetailInfo>>() {
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

	/*
	 * public static Result<List<HistoryInspectRecordModel>>
	 * queryHistoryInsepctRecordInfo( HistoryInspectCountRecordInfo info) {
	 * Result<List<HistoryInspectRecordModel>> result = new
	 * Result<List<HistoryInspectRecordModel>>(); try {
	 * RequestResponseCommonObject commonRsp = new
	 * RequestResponseCommonObject();
	 * commonRsp.setType("queryRegisterInfoByName_App"); trimEmptyToNull(info);
	 * String jsonStr = JacksonUtil.writeEntity2JsonStr(info); String param =
	 * "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr; ELog.d(TAG, "req:"
	 * + param); String res = HttpRequest.sendPost(Config.SERVER_XINXI, param);
	 * ELog.d(TAG, "res:" + res); parseArrayResult(result, res, new
	 * TypeToken<List<HistoryInspectRecordModel>>() { }.getType()); } catch
	 * (Throwable e) { e.printStackTrace(); result.setError(-1);
	 * result.setErrorMsg(App.self.getString(R.string.sys_err));
	 * result.setThrowable(e); ExceptionHandler.handleException(App.self,
	 * result); } return result; }
	 */

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
			ELog.d(TAG, "req91:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
			ELog.d(TAG, "res91:" + res);
			parseArrayResult(result, res, new TypeToken<List<HistoryInspectRecordModel>>() {
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

	// 新的——查询稽查记录
	public static Result<List<HistoryInspectRecordModelNew>> queryHistoryInsepctRecordInfoNew(
			HistoryInspectCountRecordInfo info) {
		Result<List<HistoryInspectRecordModelNew>> result = new Result<List<HistoryInspectRecordModelNew>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryRegisterInfoByName_Attach_Taxi");
			//trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req233:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_XINXI, param);
			ELog.d(TAG, "res233:" + res);
			parseArrayResult(result, res, new TypeToken<List<HistoryInspectRecordModelNew>>() {
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

	// 新的——查询案件列表
	public static Result<List<Case>> queryHistoryCaseRecordInfo(AllCaseQ info) {
		Result<List<Case>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("selectCaseAll");
			trimNullToEmpty(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req233:" + param);
			String res = HttpRequest.sendPost(com.miu30.common.config.Config.SERVER_WAIQIN, param);
			ELog.d(TAG, "res233:" + res);
			parseObjectResult2(result, "data", res, new TypeToken<List<Case>>() {
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

	// 查询文书是否已填写
	public static Result<List<UTC>> queryBlTimeInfo(CaseID info) {
		Result<List<UTC>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("selectblTimesListByCaseId");
			trimNullToEmpty(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req244:" + param);
			String res = HttpRequest.sendPost(com.miu30.common.config.Config.SERVER_WAIQIN, param);
			ELog.d(TAG, "res244:" + res);
			parseObjectResult2(result, "data", res, new TypeToken<List<UTC>>() {
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

	// 查询案件绑定的文书
	public static Result<List<BlType>> queryCaseBlInfo(CaseID info) {
		Result<List<BlType>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("selectCaseBlList");
			trimNullToEmpty(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req244:" + param);
			String res = HttpRequest.sendPost(com.miu30.common.config.Config.SERVER_WAIQIN, param);
			ELog.d(TAG, "res244:" + res);
			parseObjectResult2(result, "data", res, new TypeToken<List<BlType>>() {
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

//		public static Result<List<HistoryInspectRecordModelNew>> queryHistoryInsepctRecordInfoNew(
//				HistoryInspectCountRecordInfo info) {
//			Result<List<HistoryInspectRecordModelNew>> result = new Result<List<HistoryInspectRecordModelNew>>();
//			try {
//				RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
//				commonRsp.setType("queryRegisterInfoByName_Attach");
//				//trimEmptyToNull(info);
//				String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
//				String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
//				ELog.d(TAG, "req91:" + param);
//				String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
//				ELog.d(TAG, "res91:" + res);
//				parseArrayResult(result, res, new TypeToken<List<HistoryInspectRecordModelNew>>() {
//				}.getType());
//			} catch (Throwable e) {
//				e.printStackTrace();
//				result.setError(-1);
//				result.setErrorMsg(App.self.getString(R.string.sys_err));
//				result.setThrowable(e);
//				ExceptionHandler.handleException(App.self, result);
//			}
//			return result;
//		}

	public static Result<Long> updateZfryZwstatus(Zwstatus z) {
		Result<Long> result = new Result<Long>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("update_Zfry_Zwstatus");
			String jsonStr = gson.toJson(z);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);
			parseObjectResult(result, "", res, new TypeToken<Long>() {
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

	public static Result<List<ZfryInfo>> queryZfryInfo(ZfryInfo info) {
		Result<List<ZfryInfo>> result = new Result<List<ZfryInfo>>();

		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getZfryInfoByAll");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZFRY, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ZfryInfo>>() {
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

	public static Result<List<CompanyNameByCode>> queryCompanyNameByCode(CompanyNameByCode info) {
		Result<List<CompanyNameByCode>> result = new Result<List<CompanyNameByCode>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCorpNameByCode");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<CompanyNameByCode>>() {
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

	public static Result<List<queryZFRYByDWMC>> queryZFRYNameinfo(queryZFRYByDWMC info) {
		Result<List<queryZFRYByDWMC>> result = new Result<List<queryZFRYByDWMC>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("query_Zfry_Name");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<queryZFRYByDWMC>>() {
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

	public static Result<List<HistoryIDCard>> queryHistoryIdInfo(HistoryIDCard info) {
		Result<List<HistoryIDCard>> result = new Result<List<HistoryIDCard>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("query_Certificates_Info");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HistoryIDCard>>() {
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

	public static Result<List<FaGuiDetailAllInfo>> queryCheckItemCopyInfo(FaGuiQueryInfo info) {
		Result<List<FaGuiDetailAllInfo>> result = new Result<List<FaGuiDetailAllInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCheckXiang_Taxi");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res23:" + res);
			parseArrayResult(result, res, new TypeToken<List<FaGuiDetailAllInfo>>() {
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

	public static Result<List<FaGuiDetailAllInfo>> queryCheckItemInfo(FaGuiQueryInfo info) {
		Result<List<FaGuiDetailAllInfo>> result = new Result<List<FaGuiDetailAllInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCheckXiang_Taxi_GQ");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res23:" + res);
			parseArrayResult(result, res, new TypeToken<List<FaGuiDetailAllInfo>>() {
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

	public static Result<List<FaGuiDetailAllInfo>> queryFaGuiAllInfo(checkItemQ info) {
		Result<List<FaGuiDetailAllInfo>> result = new Result<List<FaGuiDetailAllInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCheckXiangAll_Taxi");
			/*
			 * trimEmptyToNull(info); String jsonStr =
			 * JacksonUtil.writeEntity2JsonStr(info);
			 */
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res23:" + res);
			parseArrayResult(result, res, new TypeToken<List<FaGuiDetailAllInfo>>() {
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

	public static Result<String> queryCarPositionInfo(String vname) {
		Result<String> result = new Result<String>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCurrentPostion");
			String param = "type=" + commonRsp.getType() + "&vname=" + vname + "&flag=" + Config.FLAG;
			ELog.d(TAG, "req26:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
			ELog.d(TAG, "res26:" + res);
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

	public static Result<CPosition> queryCarPosition(String vname) {
		Result<CPosition> result = new Result<CPosition>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCurrentPostion");
			String param = "type=" + commonRsp.getType() + "&vname=" + "京" + vname + "&flag=" + Config.FLAG;
			ELog.d(TAG, "req9:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
			ELog.d(TAG, "res9:" + res);
			parseObjectResult1(result, "", res, new TypeToken<TaxiCarInfo>() {
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

	public static Result<String> queryCarPositionInfo(CarCarInfo info) {
		Result<String> result = new Result<String>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("query_Car");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
			ELog.d(TAG, "res:" + res);
			result.setData(res);
			;

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;

	}

	/*
	 * public static Result<List<VehiclePositionModex>>
	 * queryKeYiPositionInfo(String lon,String lat,String range) {
	 * Result<List<VehiclePositionModex>> result = new
	 * Result<List<VehiclePositionModex>>(); try { RequestResponseCommonObject
	 * commonRsp = new RequestResponseCommonObject();
	 * commonRsp.setType("queryVlistByrange"); String param = "type=" +
	 * commonRsp.getType() + "&lon=" + lon + "&lat=" + lat + "&range=" + range +
	 * "&flag="+ Config.FLAG; ELog.d(TAG, "req:" + param); String res =
	 * HttpRequest.sendPost(Config.SERVER_POSITION, param); ELog.d(TAG, "res:" +
	 * res); parseArrayResult(result, res, new
	 * TypeToken<List<VehiclePositionModex>>() { }.getType()); } catch
	 * (Throwable e) { e.printStackTrace(); result.setError(-1);
	 * result.setErrorMsg(App.self.getString(R.string.sys_err));
	 * result.setThrowable(e); ExceptionHandler.handleException(App.self,
	 * result); }
	 *
	 * return result;
	 *
	 * }
	 */

	public static Result<List<VehiclePositionModex1>> queryKeYiPositionInfo1(String lon, String lat, String range) {
		Result<List<VehiclePositionModex1>> result = new Result<List<VehiclePositionModex1>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVlistByrange");
			String param = "type=" + commonRsp.getType() + "&lon=" + lon + "&lat=" + lat + "&range=" + range + "&flag="
					+ Config.FLAG + "&kytype=0";
			ELog.d(TAG, "req34:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
			ELog.d(TAG, "res34:" + res);
			parseArrayResult(result, res, new TypeToken<List<VehiclePositionModex1>>() {
			}.getType());
			// parseArrayResult1(List<VehiclePositionModex1>, res, new
			// TypeToken<List<VehiclePositionModex1>>() {
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

	public static Result<List<VehiclePositionModex1>> queryKeYiPositionInfo2(String lon, String lat, String range,
																			 String kytype) {
		Result<List<VehiclePositionModex1>> result = new Result<List<VehiclePositionModex1>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVlistByrange");
			String param = "type=" + commonRsp.getType() + "&lon=" + lon + "&lat=" + lat + "&range=" + range + "&flag="
					+ Config.FLAG + "&kytype=" + kytype;
			ELog.d(TAG, "req34:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_POSITION, param);
			ELog.d(TAG, "res34:" + res);
			parseArrayResult(result, res, new TypeToken<List<VehiclePositionModex1>>() {
			}.getType());
			// parseArrayResult1(List<VehiclePositionModex1>, res, new
			// TypeToken<List<VehiclePositionModex1>>() {
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

	public static Result<List<RoateInfo>> queryZFRYRoteinfo(RoateInfo info) {
		Result<List<RoateInfo>> result = new Result<List<RoateInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryPermission");
//			commonRsp.setType("getAcl");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
//			String param = "type=" + commonRsp.getType() + "&jsonStr={\"MARK\":\"taxi\",\"ZFZH\":\"11131007\"}" ;
			String param = "type=" + commonRsp.getType() + "&jsonStr="+jsonStr;


			ELog.d(TAG, "req133:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ROATE, param);
			ELog.d(TAG, "res133:" + res);
			parseArrayResult(result, res, new TypeToken<List<RoateInfo>>() {
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

	public static Result<List<RoateInfo>> queryZFRYRoteinfo_new(String info) {
		Result<List<RoateInfo>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getFunctionByRolId");
//			commonRsp.setType("getAcl");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
//			String param = "type=" + commonRsp.getType() + "&jsonStr={\"MARK\":\"taxi\",\"ZFZH\":\"11131007\"}" ;
			String param = "type=" + commonRsp.getType() + "&jsonStr="+"{\"id\":\""+info+"\"}";
			ELog.d(TAG, "req133:" + param);
			System.out.println("req24171:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_POWER, param);
			ELog.d(TAG, "res133:" + res);
			System.out.println("req24171:" + res);
			parseArrayResult(result, res, new TypeToken<List<RoateInfo>>() {
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

	public static Result<List<JclbModel>> getJclb_old(JCLBQ jclbq) {
		Result<List<JclbModel>> result = new Result<List<JclbModel>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("get_jclb");
			String jsonStr = gson.toJson(jclbq);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req23:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res23:" + res);
			parseArrayResult(result, res, new TypeToken<List<JclbModel>>() {
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

	// 获取当前账号没绑定的所有摄像头信息
	public static Result<List<Camera>> getCamera(CameraQ cameraq) {
		Result<List<Camera>> result = new Result<List<Camera>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getUnboundCameraInfo");
			String jsonStr = gson.toJson(cameraq);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req66:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res66:" + res);
			parseArrayResult(result, res, new TypeToken<List<Camera>>() {
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

	// 获取当前账号已绑定的所有摄像头信息
	public static Result<List<UnboundCamera>> getExistCamera(BindCamera cameraq) {
		Result<List<UnboundCamera>> result = new Result<List<UnboundCamera>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getMyCameraInfo");
			trimEmptyToNull(cameraq);
			String jsonStr = gson.toJson(cameraq);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req66:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res66:" + res);
			parseArrayResult(result, res, new TypeToken<List<UnboundCamera>>() {
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

	public static Result<List<CheZuResultModel>> queryCheZuZFRYNameinfo(CheZuRequestModel info) {
		Result<List<CheZuResultModel>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getPartner");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req33:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZFRY, param);
			ELog.d(TAG, "res33:" + res);
			parseArrayResult(result, res, new TypeToken<List<CheZuResultModel>>() {
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
	 * 法制办表信息--其他
	 *
	 * @param info
	 * @return
	 */

	public static Result<List<QueryFaZhiBanResult>> queryFaZhiBan(FazhiBanAllQuenyModel info) {
		Result<List<QueryFaZhiBanResult>> result = new Result<List<QueryFaZhiBanResult>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getZfbInfo_App");
			trimNullToEmpty(info);
			String jsonStr = new Gson().toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req666:" +Config.SERVER_FZBSAVE+"?"+ param);
			String res = HttpRequest.sendPost(Config.SERVER_FZBSAVE, param);
			ELog.d(TAG, "res667:" + res);
			parseArrayResult(result, res, new TypeToken<List<QueryFaZhiBanResult>>() {
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
	 * 旅游人员信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<LvyouDriverInfo>> queryLvyouPeopleInfo(LvyouDriverInfo info) {
		Result<List<LvyouDriverInfo>> result = new Result<List<LvyouDriverInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryDriverInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<LvyouDriverInfo>>() {
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
	 * 法制办表信息--其他--单个
	 *
	 * @return
	 */

	public static Result<List<QueryFaZhiBanResult>> queryFaZhiBanOne(String Vname) {
		Result<List<QueryFaZhiBanResult>> result = new Result<List<QueryFaZhiBanResult>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getZfbInfoByVname");
//			trimNullToEmpty(info);
//			String jsonStr = new Gson().toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr={\"VNAME\":\""+Vname+"\"}";
			ELog.d(TAG, "req777:" +Config.SERVER_FZBSAVE+"?"+ param);
			String res = HttpRequest.sendPost(Config.SERVER_FZBSAVE, param);
			ELog.d(TAG, "res777:" + res);
			parseArrayResult(result, res, new TypeToken<List<QueryFaZhiBanResult>>() {
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
	 * 旅游车辆信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<LvyouInfo>> queryLvyouCarInfo(LvyouInfo info) {
		Result<List<LvyouInfo>> result = new Result<List<LvyouInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVehicleInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<LvyouInfo>>() {
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
	 * 旅游业户信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<LvyouYehuInfo>> queryLvyouYehuInfo(LvyouYehuInfo info) {
		Result<List<LvyouYehuInfo>> result = new Result<List<LvyouYehuInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryYeHuInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<LvyouYehuInfo>>() {
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
	 * 省际人员信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ShengJiPeopleInfo>> queryShengJiPeopleInfo(ShengJiPeopleInfo info) {
		Result<List<ShengJiPeopleInfo>> result = new Result<List<ShengJiPeopleInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryDriverInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SHENGJI, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ShengJiPeopleInfo>>() {
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
	 * 省际车辆信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ShengJiInfo>> queryShengJiCarInfo(ShengJiInfo info) {
		Result<List<ShengJiInfo>> result = new Result<List<ShengJiInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVehicleInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SHENGJI, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ShengJiInfo>>() {
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
	 * 货运人员信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HuoYunPeopleInfo>> queryHuoYunPeopleInfo(HuoYunPeopleInfo info) {
		Result<List<HuoYunPeopleInfo>> result = new Result<List<HuoYunPeopleInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryDriverInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_HUOYUN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HuoYunPeopleInfo>>() {
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
	 * 货运车辆信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HuoYunInfo>> queryHuoYunCarInfo(HuoYunInfo info) {
		Result<List<HuoYunInfo>> result = new Result<List<HuoYunInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVehicleInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_HUOYUN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HuoYunInfo>>() {
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
	 * 货运业户信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HuoYunYeHuInfo>> queryHuoYunYehuInfo(HuoYunYeHuInfo info) {
		Result<List<HuoYunYeHuInfo>> result = new Result<List<HuoYunYeHuInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryYeHuInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_HUOYUN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HuoYunYeHuInfo>>() {
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
	 * 维修信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<WeiXiu>> queryWeiXiuInfo(WeiXiuQ info) {
		Result<List<WeiXiu>> result = new Result<List<WeiXiu>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getRepairInfo");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req334:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LINE, param);
			ELog.d(TAG, "res334:" + res);
			parseArrayResult(result, res, new TypeToken<List<WeiXiu>>() {
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
	 * 水运信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ShuiYun>> queryShuiYunInfo(ShuiYunQ info) {
		Result<List<ShuiYun>> result = new Result<List<ShuiYun>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getWaterTransport");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req334:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LINE, param);
			ELog.d(TAG, "res334:" + res);
			parseArrayResult(result, res, new TypeToken<List<ShuiYun>>() {
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
	 * 海运信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HaiYun>> queryHaiYunInfo(ShuiYunQ info) {
		Result<List<HaiYun>> result = new Result<List<HaiYun>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getSeaTransport");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req334:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LINE, param);
			ELog.d(TAG, "res334:" + res);
			parseArrayResult(result, res, new TypeToken<List<HaiYun>>() {
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
	 * 通过行业查询类型--检查项（执法稽查）
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HyRyFg>> queryCheckItemLXByHY(HYTypeQ info) {
		Result<List<HyRyFg>> result = new Result<List<HyRyFg>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getJcxHyRylx");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req235:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "req235:" + res);
			parseArrayResult(result, res, new TypeToken<List<HyRyFg>>() {
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
	 * 法规详情--检查项
	 *
	 * @return
	 */

	public static Result<List<FaGuiDetail>> getFaGuiDetails(FaGuiDetailQ jcQ) {
		Result<List<FaGuiDetail>> result = new Result<>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getJcxHyRyFgXq");
			String jsonStr = gson.toJson(jcQ);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req111:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res111:" + res);
			parseArrayResult(result, res, new TypeToken<List<FaGuiDetail>>() {
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
	 * 检查类别--检查项
	 *
	 * @return
	 */

	public static Result<List<JCItem>> getJclb(JCItemQ jcQ) {
		Result<List<JCItem>> result = new Result<List<JCItem>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getJcxHyRyFg");
			String jsonStr = gson.toJson(jcQ);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req1112:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res1112:" + res);
			parseArrayResult(result, res, new TypeToken<List<JCItem>>() {
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
	 * 模糊查询法规--检查项（执法稽查）
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<JCItem>> queryCheckItemByInfo(JCItemQ info) {
		Result<List<JCItem>> result = new Result<List<JCItem>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getJcxHyRyFg_New");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			//String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req235:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res235:" + res);
			parseArrayResult(result, res, new TypeToken<List<JCItem>>() {
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
	 * 查询检查类别
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<JCItem>> queryCheckItem(JCItemQ info) {
		Result<List<JCItem>> result = new Result<List<JCItem>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("get_XYC_Jclb");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			String res = HttpRequest.sendPost(Config.SERVER_OTHER, param);
			ELog.d(TAG, "res235:" + res);
			parseArrayResult(result, res, new TypeToken<List<JCItem>>() {
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
	 * 化危人员信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HuoYunPeopleInfo>> queryHuoYunDangerousPeopleInfo(HuoYunPeopleInfo info) {
		Result<List<HuoYunPeopleInfo>> result = new Result<List<HuoYunPeopleInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryDriverInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_HUOYUN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HuoYunPeopleInfo>>() {
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
	 * 租赁车辆信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ZuLinInfo>> queryZhulinCarInfo(ZuLinInfo info) {
		Result<List<ZuLinInfo>> result = new Result<List<ZuLinInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryVehicleInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZULIN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ZuLinInfo>>() {
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
	 * 租赁业户信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ZhuLinYeHuInfo>> queryZhuLinYehuInfo(ZhuLinYeHuInfo info) {
		Result<List<ZhuLinYeHuInfo>> result = new Result<List<ZhuLinYeHuInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryYeHuInfo");
			trimEmptyToNull(info);
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZULIN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ZhuLinYeHuInfo>>() {
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
	 * 租赁业户信息--基础信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<InspectTongJi> queryInspectTongJiInfo(InspectTongJiQ info) {
		Result<InspectTongJi> result = new Result<InspectTongJi>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("CzLawAllList");
			String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req112:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SAVEINFO, param);
			ELog.d(TAG, "res112:" + res);
			parseObjectResult1(result, "", res, new TypeToken<InspectTongJi>() {
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
	 * 查询所属大队
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<groupName>> querysSDD(String info) {
		Result<List<groupName>> result = new Result<List<groupName>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("getGroupByZfzh");
			//String jsonStr = JacksonUtil.writeEntity2JsonStr(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + "{\"zfzh\":\""+info+"\"}";
			ELog.d(TAG, "req112:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_GROUP, param);
			ELog.d(TAG, "res112:" + res);
			parseArrayResult(result, res, new TypeToken<List<groupName>>() {
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

	/**************************************** 迁移行业到巡游 **********************************************************/

	/**
	 * 旅游车辆违法--违法信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<LvYouWeiFaInfo>> queryLvYouWeiFaInfo(LvYouWeiFaInfo info) {
		Result<List<LvYouWeiFaInfo>> result = new Result<List<LvYouWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LVYOU, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<LvYouWeiFaInfo>>() {
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
	 * 省际违法--违法信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ShengJiWeiFaInfo>> queryShengJiWeiFaInfo(ShengJiWeiFaInfo info) {
		Result<List<ShengJiWeiFaInfo>> result = new Result<List<ShengJiWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SHENGJI, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ShengJiWeiFaInfo>>() {
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
	 * 货运违法（普通、化危）
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<HuoYunWeiFaInfo>> queryHuoYunWeiFaInfo(HuoYunWeiFaInfo info) {
		Result<List<HuoYunWeiFaInfo>> result = new Result<List<HuoYunWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_HUOYUN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HuoYunWeiFaInfo>>() {
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
	 * 水运和维修
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<WaterTranspt>> queryWaterAndWeiXiuWeiFaInfo(WaterTransptQ info) {
		Result<List<WaterTranspt>> result = new Result<List<WaterTranspt>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_LINE, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<WaterTranspt>>() {
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
	 * 租赁车辆违法--违法信息
	 *
	 * @param info
	 * @return
	 */
	public static Result<List<ZuLinWeiFaInfo>> queryZuLinWeiFaInfo(ZuLinWeiFaInfo info) {
		Result<List<ZuLinWeiFaInfo>> result = new Result<List<ZuLinWeiFaInfo>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryWeiFaInfo");
			trimEmptyToNull(info);
			String jsonStr = gson.toJson(info);
			String param = "type=" + commonRsp.getType() + "&jsonStr=" + jsonStr;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_ZULIN, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<ZuLinWeiFaInfo>>() {
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

	// 二维码请求接口
	public static Result<Driver> getDriverInfo(String url) {
		Result<Driver> result = new Result<Driver>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryJanDuKaInfo");
			String param = "type=" + commonRsp.getType() + "&url=" + url;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);
			result = HtmlDataEweiweima.getDriverInfo(res);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;
	}

	/*// 二维码请求接口
	public static Result<NetCarQrInfo> getCarInfo(String url) {
		Result<NetCarQrInfo> result = new Result<NetCarQrInfo>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryJanDuKaInfo");
			String param = "type=" + commonRsp.getType() + "&url=" + url;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);
			result = HtmlDataEweiweima.getCarInfo(res);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;
	}*/

	// 二维码请求接口
	public static Result<String> getCarInfo(String url) {
		Result<String> result = new Result<String>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryRqCodeInfo");
			String param = "type=" + commonRsp.getType() + "&url=" + url;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res);
			//result = HtmlDataEweiweima.getCarInfo(res);
			if(TextUtils.isEmpty(res)){
				result.setError(-1);
				result.setErrorMsg("证件二维码错误");
			}else{
				JSONObject jObj = new JSONObject(res);
				result.setError(jObj.optInt("error", 0));
				result.setErrorMsg(jObj.optString("errorMsg"));
				result.setData(res);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;
	}

	// 二维码请求接口
	public static Result<NetPersonQrInfo> getNetDriverInfo(String url) {
		Result<NetPersonQrInfo> result = new Result<NetPersonQrInfo>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryJanDuKaInfo");
			String param = "type=" + commonRsp.getType() + "&url=" + url;
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_TAXIINFO, param);
			ELog.d(TAG, "res:" + res+"===");
			if(res != null && !"".equals(res)){
				String res1 = res.split("\\[")[1];
				String res2 = res1.split("\\]")[0];
				parseObjectResult1(result, "", res2, new TypeToken<NetPersonQrInfo>() {
				}.getType());
			}else{
				result.setData(null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.sys_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}

		return result;
	}

	// 二维码查询接口
	public static Result<List<HistoryIDCard>> getQrRecordInfo(String zfzh) {
		Result<List<HistoryIDCard>> result = new Result<List<HistoryIDCard>>();
		try {
			RequestResponseCommonObject commonRsp = new RequestResponseCommonObject();
			commonRsp.setType("queryCertificatesInfo");
			String param = "type=" + commonRsp.getType() + "&jsonStr={\"ZFZH\":\"" + zfzh+"\"}";
			ELog.d(TAG, "req:" + param);
			String res = HttpRequest.sendPost(Config.SERVER_SPECIAL, param);
			ELog.d(TAG, "res:" + res);
			parseArrayResult(result, res, new TypeToken<List<HistoryIDCard>>() {
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
}
