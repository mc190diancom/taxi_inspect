package com.miu360.legworkwrit.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.entity.HYTypeQ;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.WifiPreference;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.model.entity.PrintTimes;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;
import com.miu360.legworkwrit.mvp.model.entity.WifiRequestParams;
import com.miu30.common.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * 作者：wanglei on 2019/1/10.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 修改打印次数的服务
 */
public class GeneralInformationService extends Service {
    private static final String TAG = "ModifyPrintCountService";

    /**
     * 在这里对通用信息进行数据获取
     */
    @Override
    public void onCreate() {
        super.onCreate();
        getDistrict();
        getIllegalDetailList();
        getAgencyInfos();
        getParkList();
        getAgencyInfosByZFZH();
        getLawJCXLx();
        getWifiInfo();
    }

    /**
     * 获取检查项类型
     */
    private void getLawJCXLx() {
        HYTypeQ info = new HYTypeQ();
        info.setHylb("巡游车");
        Map<String, Object> params = new MapUtil().getMap("getJcxHyRylx"
                , BaseData.gson.toJson(info));
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getJCXData(params)
                .compose(RxUtils.<Result<List<HyRyFg>>>tryWhen(2, 3))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<HyRyFg>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<HyRyFg>> result) throws Exception {
                        if (result.ok()) {
                            CacheManager.getInstance().putJCX(result.getData());
                        } else {
                            Log.d(TAG, "检查项信息没获取到");
                        }
                    }
                });
    }

    /**
     * 获取区域信息
     */
    private void getDistrict() {
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getDistrict("getAreaInfo")
                .compose(RxUtils.<Result<List<District>>>tryWhen(10, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<District>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<District>> result) throws Exception {
                        if (result.ok()) {
                            CacheManager.getInstance().putDistrict(result.getData());
                        } else {
                            Log.d(TAG, "区域没获取到");
                        }
                    }
                });
    }

    /**
     * 获取违法行为信息
     */
    private void getIllegalDetailList() {
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getIllegalDetailList("getCfjdsWfxwList")
                .compose(RxUtils.<Result<List<IllegalDetail>>>tryWhen(10, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<IllegalDetail>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<IllegalDetail>> result) throws Exception {
                        if (result.ok()) {
                            CacheManager.getInstance().putIllegalDetailList(result.getData());
                        } else {
                            Log.d(TAG, "违反行为没获取到");
                        }
                    }
                });
    }

    /**
     * 获取机关地址和机关电话
     */
    private void getAgencyInfos() {
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getAgencyInfos("getDdddAndDhList")
                .compose(RxUtils.<Result<List<AgencyInfo>>>tryWhen(10, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<AgencyInfo>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<AgencyInfo>> result) throws Exception {
                        if (result.ok()) {
                            CacheManager.getInstance().putAgencyInfos(result.getData());
                        } else {
                            Log.d(TAG, "机关信息没获取到");
                        }
                    }
                });
    }

    /**
     * 获取停车场信息
     */
    private void getParkList() {
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getParkList("getTccmcList")
                .compose(RxUtils.<Result<List<Park>>>tryWhen(10, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<Park>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<Park>> result) throws Exception {
                        if (result.ok()) {
                            CacheManager.getInstance().putParkList(result.getData());
                        } else {
                            Log.d(TAG, "停车场没获取到");
                        }
                    }
                });
    }

    /**
     * 通过执法账号获取机关信息
     */
    private void getAgencyInfosByZFZH() {
        final UserPreference user = new UserPreference(MiuBaseApp.self);
        String userName = user.getString("user_name", "");
        Map<String, Object> params = new MapUtil().getMap("selectWifiByZfzh"
                , BaseData.gson.toJson(new WifiRequestParams(userName)));
        System.out.println("执法账号机关信息:" + params);
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getPrinterAgencyInfoMsg(params)
                .compose(RxUtils.<Result<List<AgencyInfo>>>tryWhen(30, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<AgencyInfo>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<AgencyInfo>> result) throws Exception {
                        if (result.ok()) {
                            Log.d(TAG, "执法账号机关信息:" + result.getData());
                            CacheManager.getInstance().putAgencyInfoByZFZH(result.getData());
                        } else {
                            Log.d(TAG, "执法账号机关信息没获取到");
                        }
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        int FLAG = intent.getIntExtra("Flag", -1);
        if (FLAG == 1) {
            PrintTimes times = intent.getParcelableExtra("PrintTimes");
            modifyPrintCount(times);
        } else if (FLAG == 2) {
            String vName = intent.getStringExtra("vname");
            getVehicleInfo(vName);
        } else if(FLAG == 3){
            String sfzh = intent.getStringExtra("sfzh");
            String caseId = intent.getStringExtra("caseId");
            getSfzhInfo(caseId,sfzh);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void modifyPrintCount(final PrintTimes times) {
        System.out.println("testtest:" + times);
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .modifyPrintTimes(new MapUtil().getMap("updatePrintTimes", BaseData.gson.toJson(times)))
                .compose(RxUtils.<Result<String>>tryWhen(3, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<String>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<String> stringResult) throws Exception {
                        if (stringResult.ok()) {
                            Log.d(TAG, "修改打印次数成功");
                        } else {
                            Log.d(TAG, stringResult.getMsg() + "." + stringResult.getError());
                        }
                    }
                });
    }

    private void getVehicleInfo(final String vname) {
        VehicleInfo infoCar = new VehicleInfo();
        infoCar.setVname(vname);
        infoCar.setStartIndex(0);
        infoCar.setEndIndex(10);
        Map<String, Object> map = new MapUtil().getMap("queryVehicleInfo", RequestParamsUtil.RequestVehicleInfo(infoCar));
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getVehicleInfo(map)
                .compose(RxUtils.<Result<List<VehicleInfo>>>tryWhen(3, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<VehicleInfo>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<VehicleInfo>> result) throws Exception {
                        if (result.ok()) {
                            Log.d(TAG, "获取车辆信息:" + result.getData());
                            CacheManager.getInstance().putCarInfo(result.getData());
                        } else {
                            Log.d(TAG, "获取车辆信息失败");
                        }
                    }
                });
    }

    private void getSfzhInfo(final String caseId,final String sfzh) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type","addIdCode");
        map.put("ajid",caseId);
        map.put("idcode",sfzh);
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .addIdCode(map)
                .compose(RxUtils.<Result<Void>>tryWhen(3, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<Void> result) throws Exception {
                        if (result.ok()) {
                            Log.d(TAG, "身份信息写入成功:" + result.getData());
                            CacheManager.getInstance().putSFZH(sfzh);
                        } else {
                            Log.d(TAG, "身份信息写入失败");
                        }
                    }
                });
    }

    private void getWifiInfo() {
        final WifiPreference wifiPreference = new WifiPreference(MiuBaseApp.self);
        /*if(wifiPreference.getWifis() != null && !wifiPreference.getWifis().isEmpty()){
            return;
        }*/
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager().obtainRetrofitService(MyApis.class)
                .getAllPrinterWifiConfig("selectWifiAll")
                .compose(RxUtils.<Result<List<WifiConfig>>>tryWhen(3, 10))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<WifiConfig>>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNextResult(Result<List<WifiConfig>> listResult) throws Exception {
                        if (listResult.ok()) {
                            wifiPreference.clearPreference();
                            List<WifiConfig> data = listResult.getData();
                            if (data != null && !data.isEmpty()) {
                                for (WifiConfig config : data) {
                                    wifiPreference.putWifi(config);
                                }
                            }
                        }
                    }
                });
    }

}
