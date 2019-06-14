package com.feidi.video.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerSeeVideoComponent;
import com.feidi.video.mvp.contract.SeeVideoContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.presenter.SeeVideoPresenter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.widget.IncludeHeader;
import com.miu30.common.util.BaiduMapGPSUtil;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.isCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:46
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SeeVideoActivity extends BaseMvpActivity<SeeVideoPresenter> implements SeeVideoContract.View, View.OnClickListener {
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.mapv_BaiduMap)
    MapView mapvBaiduMap;
    @BindView(R2.id.add_map)
    ImageButton addMap;
    @BindView(R2.id.reduce_map)
    ImageButton reduceMap;
    @BindView(R2.id.aim_location_return)
    ImageButton aimLocationReturn;
    @BindView(R2.id.start_play)
    View startPlay;

    private BaiduMap mBaiduMap;
    private Map<Marker, CameraInfo> markerMap = new HashMap<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSeeVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_see_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initHeader();
        mBaiduMap = mapvBaiduMap.getMap();
        mapvBaiduMap.showZoomControls(false);
        mBaiduMap.setMaxAndMinZoomLevel(18, 11);
        addMap.setOnClickListener(this);
        reduceMap.setOnClickListener(this);
        aimLocationReturn.setOnClickListener(this);
        startPlay.setOnClickListener(this);
        ArrayList<CameraInfo> cameraInfos = getIntent().getParcelableArrayListExtra("cameraInfo");
        initMaker(cameraInfos);
    }

    private void initHeader() {
        IncludeHeader header = new IncludeHeader();

        header.init(self, "视频查看");
        header.setRightTextViewText("列表");
        header.setRightTextViewVisibility(View.VISIBLE);
        header.setRightTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(-1);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.aim_location_return == id) {
            Position();
        } else if (v == addMap) {
            if (mBaiduMap != null) {
                MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
                mBaiduMap.setMapStatus(zoomIn);
            }
        } else if (v == reduceMap) {
            if (mBaiduMap != null) {
                MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
                mBaiduMap.setMapStatus(zoomOut);
            }
        } else if (v == startPlay) {
            if(!TextUtils.isEmpty(currentCameraId)){
                mPresenter.getVideoAddress(currentCameraId,new UserPreference(self).getString("user_name",""));
            }
        }
    }

    BitmapDescriptor nCamera = BitmapDescriptorFactory.fromResource(R.drawable.camera);
    BitmapDescriptor sCamera = BitmapDescriptorFactory.fromResource(R.drawable.select_camera);

    /**
     * 初始化摄像头maker
     */
    private void initMaker(List<CameraInfo> cameraInfos) {
        for (int i = 0; i < cameraInfos.size(); i++) {
            addMakerToMap(cameraInfos.get(i), nCamera,i);
        }
        setMapClickListener();//添加完后设置监听
    }

    /**
     * 将maker添加到地图上,并将地图移动到第一个maker上
     */
    private void addMakerToMap(CameraInfo cameraInfo, BitmapDescriptor camera,int position) {
        LatLng latLng = BaiduMapGPSUtil.gpsToConvertBaidu(new LatLng(Double.valueOf(cameraInfo.getLAT()), Double.valueOf(cameraInfo.getLON())));
        MarkerOptions op = new MarkerOptions().position(latLng).icon(camera).zIndex(9)
                .draggable(false);
        Marker marker = (Marker) mBaiduMap.addOverlay(op);
        markerMap.put(marker, cameraInfo);
        if(0 == position){
            marker.setIcon(sCamera);
            LatLng cLatLng = BaiduMapGPSUtil.gpsToConvertBaidu(new LatLng(Double.valueOf(cameraInfo.getLAT()), Double.valueOf(cameraInfo.getLON())));
            MapStatus mapStatus = new MapStatus.Builder().target(cLatLng).zoom(16).build();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            tvLocation.setText(cameraInfo.getNAME());
            mCurrentMarker = marker;
            currentCameraId = cameraInfo.getCAMERAID();
        }
    }

    /**
     * 设置maker点击监听
     */
    Marker mCurrentMarker;
    String currentCameraId;
    InfoWindow mInfoWindow;
    private void setMapClickListener() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker mark) {
                if (mCurrentMarker == mark) {
                    return true;
                }
                if (mCurrentMarker != null) {
                    mCurrentMarker.setIcon(nCamera);
                    mBaiduMap.hideInfoWindow();
                }
                tvLocation.setText("");

                View view = LayoutInflater.from(self).inflate(R.layout.layout_maker_popwindow, null);
                view.setLayoutParams(new TableRow.LayoutParams());
                TextView tvMakerInfo = view.findViewById(R.id.tv_maker_info);
                tvMakerInfo.setText(markerMap.get(mark).getNAME());
                LatLng latLng = BaiduMapGPSUtil.gpsToConvertBaidu(new LatLng(Double.valueOf(markerMap.get(mark).getLAT()),Double.valueOf(markerMap.get(mark).getLON())));
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view),latLng , -60,
                        null);
                mBaiduMap.showInfoWindow(mInfoWindow);

                mark.setIcon(sCamera);
                mCurrentMarker = mark;
                currentCameraId = markerMap.get(mark).getCAMERAID();
                mPresenter.getGeoCode(latLng.latitude,latLng.longitude);
                return true;
            }
        });
    }

    // 初始化定位
    private void Position() {
        if (mBaiduMap != null) {
            LatLng CorrectLatLng = null;
            if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
                CorrectLatLng = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
            } else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
                CorrectLatLng = new LatLng(MsgConfig.lat, MsgConfig.lng);
            } else {
                UIUtils.toast(self, "未定位到当前位置,请去其它地方选取", Toast.LENGTH_SHORT);
                return;
            }
            MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        }
    }

    @Override
    public void getVideoAddressSuccess(String rtspUrl) {
        if(!TextUtils.isEmpty(rtspUrl)){
            if(rtspUrl.contains("10.212.160.158")){
                rtspUrl.replace("10.212.160.158","10.252.16.81");
            }
            Intent intent = new Intent(self,VideoPlayActivity.class);
            intent.putExtra("VAddress",rtspUrl);
            startActivity(intent);
        }else{
            UIUtils.toast(self,"视频地址获取失败",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void setLocationDetail(String addressDetail) {
        tvLocation.setText(addressDetail);
    }

}
