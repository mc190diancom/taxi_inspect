package com.feidi.video.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.mvp.contract.MoveCameraContract;
import com.feidi.video.mvp.model.entity.AlarmType;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.IndustyType;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.data.MapPositionPreference;
import com.miu30.common.ui.SelectLocationActivity;
import com.miu30.common.ui.entity.AlarmDetailInfo;
import com.miu30.common.ui.entity.AlarmInfo;
import com.miu30.common.ui.widget.MultiVeriticalItemDecoration;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/03/2019 13:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class MoveCameraPresenter extends BasePresenter<MoveCameraContract.Model, MoveCameraContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private ArrayList<ISelector> data = new ArrayList<>();
    private ArrayList<ISelector> CameraData = new ArrayList<>();

    private ArrayList<IndustyType> industyTypes = new ArrayList<>();
    private ArrayList<AlarmType> AlarmTypes = new ArrayList<>();

    private CameraTCPReceiver broadCast;
    private ChooseLocationReceiver locationReceiver;

    @Inject
    public MoveCameraPresenter(MoveCameraContract.Model model, MoveCameraContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        if(broadCast != null){
            activity.unregisterReceiver(broadCast);
        }

        if(locationReceiver != null){
            activity.unregisterReceiver(locationReceiver);
        }
    }

    private Activity activity;
    public void registeBroadcast(Activity activity) {
        this.activity = activity;
        broadCast = new CameraTCPReceiver();
        IntentFilter filter = new IntentFilter("com.feidi.cameraInfo");
        activity.registerReceiver(broadCast, filter);

        locationReceiver = new ChooseLocationReceiver();
        filter = new IntentFilter(SelectLocationActivity.CHOSE_LOCATION);
        activity.registerReceiver(locationReceiver, filter);
    }

    public RecyclerView.ItemDecoration getCameraListDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(12))
                .setMarginRight(SizeUtils.dp2px(14))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public RecyclerView.ItemDecoration getIndustryOrWarningTypeListDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(10))
                .setMarginRight(SizeUtils.dp2px(17))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public RecyclerView.ItemDecoration getCrimeDecoration() {
        return new MultiVeriticalItemDecoration.Builder()
                .isDrawFristDivider(true)
                .isDrawLastDivider(true)
                .setMarginLeft(SizeUtils.dp2px(12))
                .setMarginRight(SizeUtils.dp2px(15))
                .setStartColor(Color.parseColor("#DDDDDD"))
                .setEndColor(Color.parseColor("#DDDDDD"))
                .build();
    }

    public void getCameraInfos(String zfzh) {
        Map<String, String> params = new HashMap<>();
        params.put("ZFZH", zfzh);
        Map<String, Object> map = new MapUtil().getMap("getCameraListByZfzh", BaseData.gson.toJson(params));
        mModel.getCameraList(map)
                .compose(RxUtils.<Result<List<CameraInfo>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<CameraInfo>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<CameraInfo>> result) {
                        if (result.ok() && result.getData() != null && result.getData().size() > 0) {
                            CameraData.clear();
                            CameraData.addAll(result.getData());
                            mRootView.notifyAdapter(result.getData());
                        }
                    }

                });
    }

    //测试数据
    public ArrayList<ISelector> getCameraData() {
        return CameraData;
    }


    public List<ISelector> getIndustryOrWarningTypeInfos() {
        return data;
    }

    //测试数据
    public void setIndustryOrWarningTypeInfos(boolean isIndustry) {
        data.clear();
        if (isIndustry) {
            data.add(new IndustyType("无选择"));
            data.addAll(industyTypes);
            if(industyTypes == null || industyTypes.isEmpty()){
                getIndustyType();
            }
        } else {
            data.add(new AlarmType("无选择"));
            data.addAll(AlarmTypes);
            if(AlarmTypes == null || AlarmTypes.isEmpty()){
                getAlarmType();
            }
        }
    }

    //------------------------------ 犯案次数相关 ---------------------------------------
    private CrimeListAdapter crimeListAdapter;
    private List<AlarmInfo> crimeInfos = new ArrayList<>();

    public void clearCrimeList() {
        crimeInfos.clear();
        crimeListAdapter.notifyDataSetChanged();
    }

    //通过GPS数据解析地址
    public void getGeoCode(double lat,double lon) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", String.valueOf(lat));
        params.put("lon", String.valueOf(lon));
        Map<String, Object> map = new MapUtil().getMap("queryPositionInfo", BaseData.gson.toJson(params));
        mModel.queryHistoryTrack(map)
                .compose(RxUtils.<Result<String>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<String>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<String> result) {
                        System.out.println("queryPositionInfo:"+result.getData());
                        JSONObject jobj = null;
                        try {
                            jobj = new JSONObject(result.getData());
                            result.setData(jobj.optJSONObject("result").optString("formatted_address"));
                            result.setError(Result.OK);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            result.setError(-1);
                            result.setMsg("数据解析异常");
                        }
                        mRootView.setLocationDetail(result.getData());
                    }

                });
    }

    //获取预警的行业类别
    public void getIndustyType() {
        mModel.getHangYeType("getAlarmType")
                .compose(RxUtils.<Result<List<IndustyType>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<IndustyType>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<IndustyType>> result) {
                        industyTypes.addAll(result.getData());
                    }

                });
    }

    //获取预警的行业类别
    public void getAlarmType() {
        mModel.getAlarmType("querySuspiciousType")
                .compose(RxUtils.<Result<List<AlarmType>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AlarmType>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<AlarmType>> result) {
                        AlarmTypes.addAll(result.getData());
                    }

                });
    }


    public CrimeListAdapter getCrimeListAdapter() {
        crimeListAdapter = new CrimeListAdapter(crimeInfos,activity);
        return crimeListAdapter;
    }

    //将报警信息转化为指定格式传送
    public AlarmDetailInfo turnAlarmInfo(AlarmInfo data) {
        AlarmDetailInfo alarmDetailInfo = new AlarmDetailInfo();
        alarmDetailInfo.setVname(data.getVehiclePlatNo());
        alarmDetailInfo.setADK_WFXW(data.getAlarmType());
        alarmDetailInfo.setRKSM(data.getAlarmType());
        alarmDetailInfo.setSpeed("100");
        alarmDetailInfo.setOccurTime(data.getOccurTime());
        alarmDetailInfo.setLat(data.getLatitude());
        alarmDetailInfo.setLon(data.getLongitude());
        return alarmDetailInfo;
    }


    class CameraTCPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            AlarmInfo message = intent.getParcelableExtra("data");
            crimeInfos.add(message);
            if(crimeListAdapter == null){
                getCrimeListAdapter();
            }else{
                crimeListAdapter.notifyDataSetChanged();
            }
            mRootView.setViewVisible();
        }
    }

    class ChooseLocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            new MapPositionPreference(activity).setString("selectPosition", intent.getStringExtra("shoudong_location"));
            mRootView.setPostion();
        }
    }

}
