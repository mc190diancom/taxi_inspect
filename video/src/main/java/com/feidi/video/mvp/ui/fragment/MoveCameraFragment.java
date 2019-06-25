package com.feidi.video.mvp.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerMoveCameraComponent;
import com.feidi.video.mvp.contract.MoveCameraContract;
import com.feidi.video.mvp.model.entity.AlarmType;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.IndustyType;
import com.feidi.video.mvp.presenter.MoveCameraPresenter;
import com.feidi.video.mvp.ui.adapter.CameraListAdapter;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.feidi.video.mvp.ui.adapter.IndustryOrWarningTypeAdapter;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentSelectedChangeListener;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseMvpFragment;
import com.miu30.common.config.Constance;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.connect.ChannelManager;
import com.miu30.common.connect.entity.BindCameraRequest;
import com.miu30.common.connect.entity.CancelBindCameraRequest;
import com.miu30.common.data.MapPositionPreference;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.SelectLocationActivity;
import com.miu30.common.ui.entity.AlarmInfo;
import com.miu30.common.util.BaiduMapGPSUtil;
import com.miu30.common.util.isCommon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

import static com.miu30.common.util.BaiduMapGPSUtil.convertBaiduToGPS;


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
@SuppressWarnings("all")
public class MoveCameraFragment extends BaseMvpFragment<MoveCameraPresenter> implements MoveCameraContract.View, View.OnClickListener {
    @BindView(R2.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R2.id.cb_industry)
    CheckBox cbIndustry;
    @BindView(R2.id.cb_warning_type)
    CheckBox cbWarningType;
    @BindView(R2.id.toggle)
    ToggleButton toggleBtn;
    @BindView(R2.id.mapv_BaiduMap)
    TextureMapView mMapView;
    @BindView(R2.id.add_map)
    ImageButton addMap;
    @BindView(R2.id.reduce_map)
    ImageButton reduceMap;
    @BindView(R2.id.aim_location_return)
    ImageButton aimLocationReturn;
    @BindView(R2.id.mine_position)
    ImageView minePosition;
    @BindView(R2.id.tv_my_location)
    TextView tvMyLocation;
    @BindView(R2.id.btn_lock)
    TextView btnLock;

    private ViewStub vsCameraList;
    private ViewStub vsIndustryOrWarningType;
    private ViewStub vsCrime;
    private RecyclerView rvCamera;
    private BaiduMap mBaiduMap;
    private ArrayList<Marker> markerList = new ArrayList<>();

    private CameraListAdapter mAdapter;

    private IndustryOrWarningTypeAdapter industryOrWarningTypeAdapter;
    private String zfzh;
    private MapPositionPreference ppfer;

    public static MoveCameraFragment newInstance() {
        MoveCameraFragment fragment = new MoveCameraFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMoveCameraComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_move_camera, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        vsCameraList = getActivity().findViewById(R.id.view_stub_camera);
        vsIndustryOrWarningType = getActivity().findViewById(R.id.view_stub_industry_or_warningtype);
        vsCrime = getActivity().findViewById(R.id.view_stub_crime);
        zfzh = new UserPreference(MiuBaseApp.self).getString("user_name", "");
        cbIndustry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedInner(buttonView, isChecked, cbWarningType.isChecked());
            }
        });

        cbWarningType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedInner(buttonView, cbIndustry.isChecked(), isChecked);
            }
        });
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mBaiduMap.setMaxAndMinZoomLevel(18, 11);

        aimLocationReturn.setOnClickListener(this);
        tvMyLocation.setOnClickListener(this);
        addMap.setOnClickListener(this);
        reduceMap.setOnClickListener(this);
        ppfer = new MapPositionPreference(getActivity());
        Position();
        setMapMoveListner();
        mPresenter.registeBroadcast(getActivity());
        mPresenter.getCameraInfos(zfzh);
        mPresenter.getIndustyType();
        mPresenter.getAlarmType();
    }

    @OnCheckedChanged(R2.id.toggle)
    public void toggle(boolean isChecked) {
        if (isChecked) {
            if (viewSwitcher.getDisplayedChild() != 0) {
                viewSwitcher.showPrevious();
            }
        } else {
            if (viewSwitcher.getDisplayedChild() != 1) {
                if (vsCameraList.getParent() != null) {
                    vsCameraList.inflate();
                    initCameraRecyclerView();
                }

                viewSwitcher.showNext();
            }
        }
    }

    private CameraInfo currentCamera;

    private void initCameraRecyclerView() {
        assert mPresenter != null;
        rvCamera = getActivity().findViewById(vsCameraList.getInflatedId())
                .findViewById(R.id.rv_camera);
        rvCamera.addItemDecoration(mPresenter.getCameraListDecoration());
        rvCamera.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CameraListAdapter(mPresenter.getCameraData());
        mAdapter.setOnItemContentSelectedChangeListener(new OnItemContentSelectedChangeListener<ISelector>() {
            @Override
            public void onSelectedChange(View v, ISelector data, int position, boolean isSelected) {
                final CameraInfo info = (CameraInfo) data;
                if (isSelected) {
                    clickCameraItem(position, info);
                } else {
                    getActivity().findViewById(vsCrime.getInflatedId()).setVisibility(View.GONE);
                    ChannelManager.getInstance().sendMessage(new CancelBindCameraRequest(zfzh, info.getCAMERAID()));
                }
            }
        });
        rvCamera.setAdapter(mAdapter);
    }

    //点击相应摄像头执行操作
    private void clickCameraItem(int position, final CameraInfo info) {
        toggleBtn.toggle();

        if (vsCrime.getParent() != null) {
            vsCrime.inflate();
            final View rootView = getActivity().findViewById(vsCrime.getInflatedId());
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    initCrimeView(rootView, info);
                }
            });
        } else {
            mPresenter.clearCrimeList();
        }
        updateMakerToMap(info, position);

        //取绑之前那个摄像头，并绑定最新的摄像头
        if (currentCamera != null && !TextUtils.isEmpty(currentCamera.getCAMERAID())) {
            ChannelManager.getInstance().sendMessage(new CancelBindCameraRequest(zfzh, currentCamera.getCAMERAID()));
        }
        //绑定摄像头，这里因为可能取绑还未成功，所以延迟发送
        if (currentCamera == null || (!TextUtils.isEmpty(info.getCAMERAID()) && !info.getCAMERAID().equals(currentCamera.getCAMERAID()))) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        currentCamera = info;
                        ChannelManager.getInstance().sendMessage(new BindCameraRequest(zfzh, "11000000001325291355"));//info.getCAMERAID()
                }
            },300);
        }
    }

    /**
     * 选中某个摄像头，在地图中作出相应的处理
     */
    private void selectCameraInMap(CameraInfo info) {
        addMakerToMap(info, sCamera);
    }

    private void checkedInner(CompoundButton button, boolean isIndustryChecked, boolean isWarningTypeChecked) {
        assert mPresenter != null;

        if (isIndustryChecked || isWarningTypeChecked) {
            if (vsIndustryOrWarningType.getParent() != null) {
                vsIndustryOrWarningType.inflate();
                initIndustryOrWarningTypeRecyclerView();
            }

            if (!isIndustryChecked) {
                //仅选中预警类别
                mPresenter.setIndustryOrWarningTypeInfos(false);
            } else if (!isWarningTypeChecked) {
                //仅选中行业
                mPresenter.setIndustryOrWarningTypeInfos(true);
            } else {
                //行业、预警类别均选中
                if (button == cbIndustry) {
                    mPresenter.setIndustryOrWarningTypeInfos(true);
                } else {
                    mPresenter.setIndustryOrWarningTypeInfos(false);
                }
            }

            industryOrWarningTypeAdapter.notifyDataSetChanged();
            //显示列表界面
            getActivity().findViewById(vsIndustryOrWarningType.getInflatedId()).setVisibility(View.VISIBLE);
        } else {
            //行业和预警类别都未选中，则隐藏列表界面
            getActivity().findViewById(vsIndustryOrWarningType.getInflatedId()).setVisibility(View.GONE);
        }

    }

    private void initIndustryOrWarningTypeRecyclerView() {
        assert mPresenter != null;
        RecyclerView rvIndustryOrWarningType = getActivity().findViewById(vsIndustryOrWarningType.getInflatedId())
                .findViewById(R.id.rv_industry_and_warningtype);
        rvIndustryOrWarningType.addItemDecoration(mPresenter.getIndustryOrWarningTypeListDecoration());
        rvIndustryOrWarningType.setLayoutManager(new LinearLayoutManager(getActivity()));
        industryOrWarningTypeAdapter = new IndustryOrWarningTypeAdapter(mPresenter.getIndustryOrWarningTypeInfos());
        industryOrWarningTypeAdapter.setOnItemContentSelectedChangeListener(new OnItemContentSelectedChangeListener<ISelector>() {
            @Override
            public void onSelectedChange(View v, ISelector data, int position, boolean isSelected) {
                String value;
                if (data instanceof IndustyType) {
                    value = ((IndustyType) data).getHYLX();
                    if (isSelected) {
                        if("无选择".equals(value)){
                            cbIndustry.setText("行业");
                        }else{
                            cbIndustry.setText(value);
                        }
                        cbIndustry.setChecked(false);
                    } else {
                    }
                } else {
                    value = ((AlarmType) data).getRKSM();
                    if (isSelected) {
                        if("无选择".equals(value)){
                            cbWarningType.setText("预警类型");
                        }else{
                            cbWarningType.setText(value);
                        }
                        cbWarningType.setChecked(false);
                    } else {
                    }
                }


            }
        });
        rvIndustryOrWarningType.setAdapter(industryOrWarningTypeAdapter);
    }

    /**
     * 犯案次数列表当前是否处于展开状态
     */
    private boolean isExpend = false;

    /**
     * 初始化犯案次数列表所在的View
     *
     * @param rootView 根View
     */
    private void initCrimeView(View rootView, CameraInfo info) {
        final ImageView ivDirection = rootView.findViewById(R.id.iv_direction);
        final View dividerView = rootView.findViewById(R.id.divider);
        final LinearLayout llContainer = rootView.findViewById(R.id.ll_container);
        final int maxHeight = rootView.getHeight() - dividerView.getHeight();
        final int minHeight = llContainer.getHeight();

        ivDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(llContainer, ivDirection, dividerView, minHeight, maxHeight);
            }
        });

        initCrimeRecyclerView(rootView, info);
        getActivity().findViewById(vsCrime.getInflatedId()).setVisibility(View.GONE);
    }

    private void initCrimeRecyclerView(View rootView, CameraInfo info) {
        assert mPresenter != null;

        RecyclerView rvCrime = rootView.findViewById(R.id.rv_crime);
        rvCrime.addItemDecoration(mPresenter.getCrimeDecoration());
        rvCrime.setLayoutManager(new LinearLayoutManager(getActivity()));
        final CrimeListAdapter adapter = mPresenter.getCrimeListAdapter();
        adapter.setOnLookClickListener(new OnItemContentViewClickListener<AlarmInfo>() {
            @Override
            public void onItemContentViewClick(View v, AlarmInfo data, int position) {
                ARouter.getInstance().build(Constance.ACTIVITY_URL_WARNINGDETAIL).withParcelable("alarmDetailInfo",mPresenter.turnAlarmInfo(data)).navigation();
            }

            @Override
            public void onItemVideoViewClick(View v, AlarmInfo data, int position) {

            }
        });
        rvCrime.setAdapter(adapter);
    }

    private void startAnimator(final LinearLayout llContainer, final ImageView ivDirection, final View dividerView, int minHeight, int maxHeight) {
        ValueAnimator animator;
        if (isExpend) {
            animator = ValueAnimator.ofInt(maxHeight, minHeight);
        } else {
            animator = ValueAnimator.ofInt(minHeight, maxHeight);
        }
        animator.setDuration(250);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isExpend = !isExpend;

                if (isExpend) {
                    dividerView.setVisibility(View.VISIBLE);
                    ivDirection.setImageResource(R.drawable.ic_direction_down);
                } else {
                    dividerView.setVisibility(View.INVISIBLE);
                    ivDirection.setImageResource(R.drawable.ic_direction_up);
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = llContainer.getLayoutParams();
                params.height = value;
                llContainer.setLayoutParams(params);
            }
        });
        animator.start();
    }


    BitmapDescriptor nCamera = BitmapDescriptorFactory.fromResource(R.drawable.camera);
    BitmapDescriptor sCamera = BitmapDescriptorFactory.fromResource(R.drawable.select_camera);

    @Override
    public void notifyAdapter(List<CameraInfo> cameraInfos) {
        initMaker(cameraInfos);
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPostion() {
        rePosition();
    }

    @Override
    public void setLocationDetail(String data) {
        tvMyLocation.setText(data);
        ppfer.setString("selectPosition", data);
    }

    @Override
    public void setViewVisible() {
        if(getActivity().findViewById(vsCrime.getInflatedId()) != null){
            getActivity().findViewById(vsCrime.getInflatedId()).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化摄像头maker
     */
    private void initMaker(List<CameraInfo> cameraInfos) {
        for (int i = 0; i < cameraInfos.size(); i++) {
            addMakerToMap(cameraInfos.get(i), nCamera);
        }
    }

    private void addMakerToMap(CameraInfo cameraInfo, BitmapDescriptor camera) {
        LatLng latLng = BaiduMapGPSUtil.gpsToConvertBaidu(new LatLng(Double.valueOf(cameraInfo.getLAT()), Double.valueOf(cameraInfo.getLON())));
        MarkerOptions op = new MarkerOptions().position(latLng).icon(camera).zIndex(9)
                .draggable(false);
        Marker marker = (Marker) mBaiduMap.addOverlay(op);
        markerList.add(marker);
    }

    Marker lastMaker;

    private void updateMakerToMap(CameraInfo cameraInfo, int position) {
        if (lastMaker != null) {
            lastMaker.setIcon(nCamera);
        }
        lastMaker = markerList.get(position);
        lastMaker.setIcon(sCamera);
        LatLng latLng = BaiduMapGPSUtil.gpsToConvertBaidu(new LatLng(Double.valueOf(cameraInfo.getLAT()), Double.valueOf(cameraInfo.getLON())));
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    @Override
    public void onDestroy() {
        if (currentCamera != null && !TextUtils.isEmpty(currentCamera.getCAMERAID())) {
            ChannelManager.getInstance().sendMessage(new CancelBindCameraRequest(zfzh, "11000000001325291355"));//currentCamera.getCAMERAID()
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.aim_location_return == id) {
            Position();
            btnLock.setVisibility(View.GONE);
        } else if(R.id.tv_my_location == id){
            Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
            intent.putExtra("thisAdd", tvMyLocation.getText().toString());
            startActivity(intent);
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
        }
    }

    // 这里都是代表，GPS原始坐标
    double lat = 0.0;
    double lon = 0.0;
    // 百度坐标点
    double bLat = 0.0;
    double bLon = 0.0;

    // 初始化定位
    private void Position() {
        if (mBaiduMap != null) {
            if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
                LatLng CorrectLatLng = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
                LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
                bLat = MsgConfig.select_lat;
                bLon = MsgConfig.select_lng;

                lat = AdjustLatLng.latitude;
                lon = AdjustLatLng.longitude;
                MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                minePosition.setVisibility(View.VISIBLE);
                tvMyLocation.setText(ppfer.getString("selectPosition", ""));// &&
            } else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
                LatLng CorrectLatLng = new LatLng(MsgConfig.lat, MsgConfig.lng);
                LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
                bLat = MsgConfig.lat;
                bLon = MsgConfig.lng;
                lat = AdjustLatLng.latitude;
                lon = AdjustLatLng.longitude;

                MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                minePosition.setVisibility(View.VISIBLE);
                if (!"".equals(ppfer.getString("selectPosition", ""))) {
                    tvMyLocation.setText(ppfer.getString("selectPosition", ""));
                } else {
                    mPresenter.getGeoCode(bLat,bLon);
                }
            }
        }
    }

    /**
     * 设置地图移动监听和锁定当前位置监听
     */
    LatLng cucrrentLatLng;
    public void setMapMoveListner(){
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                LatLng centerLatLng = status.target;
                if (lat != 0) {
                    btnLock.setVisibility(View.VISIBLE);
                    cucrrentLatLng = centerLatLng;
                }
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cucrrentLatLng == null){
                    return;
                }
                MsgConfig.select_lat = cucrrentLatLng.latitude;
                MsgConfig.select_lng = cucrrentLatLng.longitude;
                bLat = MsgConfig.select_lat;
                bLon = MsgConfig.select_lng;

                LatLng AdjustLatLng = convertBaiduToGPS(new LatLng(bLat, bLon));
                lat = AdjustLatLng.latitude;
                lon = AdjustLatLng.longitude;
                mPresenter.getGeoCode(bLat,bLon);
                btnLock.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 重定位
     */
    protected void rePosition() {
        if (MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0) {
            LatLng CorrectLatLng = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
            bLat = MsgConfig.select_lat;
            bLon = MsgConfig.select_lng;

            LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
            lat = AdjustLatLng.latitude;
            lon = AdjustLatLng.longitude;
            tvMyLocation.setText(ppfer.getString("selectPosition", ""));

            MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            minePosition.setVisibility(View.VISIBLE);
        } else {
            if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
                bLat = MsgConfig.lat;
                bLon = MsgConfig.lng;
                LatLng CorrectLatLng = new LatLng(MsgConfig.lat, MsgConfig.lng);
                LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
                lat = AdjustLatLng.latitude;
                lon = AdjustLatLng.longitude;

                if (!"".equals(ppfer.getString("selectPosition", ""))) {
                    tvMyLocation.setText(ppfer.getString("selectPosition", ""));
                } else {
                    mPresenter.getGeoCode( MsgConfig.lat, MsgConfig.lng);
                }

                MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                minePosition.setVisibility(View.VISIBLE);
            }
        }
    }
}
