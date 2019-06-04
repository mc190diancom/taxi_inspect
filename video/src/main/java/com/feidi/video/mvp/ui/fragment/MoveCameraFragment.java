package com.feidi.video.mvp.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerMoveCameraComponent;
import com.feidi.video.mvp.contract.MoveCameraContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.Industry;
import com.feidi.video.mvp.model.entity.WarningType;
import com.feidi.video.mvp.presenter.MoveCameraPresenter;
import com.feidi.video.mvp.ui.adapter.CameraListAdapter;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.feidi.video.mvp.ui.adapter.IndustryOrWarningTypeAdapter;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentSelectedChangeListener;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseMvpFragment;
import com.miu30.common.connect.ChannelManager;
import com.miu30.common.connect.entity.BindCameraRequest;
import com.miu30.common.connect.entity.CancelBindCameraRequest;
import com.miu30.common.data.UserPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;


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
public class MoveCameraFragment extends BaseMvpFragment<MoveCameraPresenter> implements MoveCameraContract.View {
    @BindView(R2.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R2.id.cb_industry)
    CheckBox cbIndustry;
    @BindView(R2.id.cb_warning_type)
    CheckBox cbWarningType;
    @BindView(R2.id.toggle)
    ToggleButton toggleBtn;
    @BindView(R2.id.mapv_BaiduMap)
    MapView mMapView;

    private ViewStub vsCameraList;
    private ViewStub vsIndustryOrWarningType;
    private ViewStub vsCrime;
    private RecyclerView rvCamera;
    private BaiduMap mBaiduMap;
    private ArrayList<Marker> markerList = new ArrayList<>();

    private CameraListAdapter mAdapter;

    private IndustryOrWarningTypeAdapter industryOrWarningTypeAdapter;
    private String zfzh;
    private CameraTCPReceiver broadCast;

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
        mBaiduMap =  mMapView.getMap();

        broadCast = new CameraTCPReceiver();
        IntentFilter filter = new IntentFilter("com.feidi.cameraInfo");
        LocalBroadcastManager.getInstance(MiuBaseApp.self).registerReceiver(broadCast, filter);

        mPresenter.getCameraInfos(zfzh);


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

    private CameraInfo currentCamera = new CameraInfo();
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
                    updateMakerToMap(info,position);

                    //取绑之前那个摄像头，并绑定最新的摄像头
                    if(!info.getCAMERAID().equals(currentCamera.getCAMERAID())){
                        ChannelManager.getInstance().sendMessage(new CancelBindCameraRequest(zfzh, currentCamera.getCAMERAID()));
                        currentCamera = info;
                        ChannelManager.getInstance().sendMessage(new BindCameraRequest(zfzh, info.getCAMERAID()));
                    }
                }else{
                    ChannelManager.getInstance().sendMessage(new CancelBindCameraRequest(zfzh, info.getCAMERAID()));
                }
            }
        });
        rvCamera.setAdapter(mAdapter);
    }

    /**
     * 选中某个摄像头，在地图中作出相应的处理
     */
    private void selectCameraInMap(CameraInfo info) {
        addMakerToMap(info,sCamera);
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
                if (data instanceof Industry) {
                    value = ((Industry) data).getName();
                } else if(data instanceof CameraInfo){
                    value = ((CameraInfo) data).getNAME();
                } else{
                    value = ((WarningType) data).getType();
                }

                if (isSelected) {
                    ToastUtils.showShort("你选中了" + value);
                } else {
                    ToastUtils.showShort("你取消选中了" + value);
                }
            }
        });
        rvIndustryOrWarningType.setAdapter(industryOrWarningTypeAdapter);
    }

    /** 犯案次数列表当前是否处于展开状态 */
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
    }

    private void initCrimeRecyclerView(View rootView, CameraInfo info) {
        assert mPresenter != null;

        RecyclerView rvCrime = rootView.findViewById(R.id.rv_crime);
        rvCrime.addItemDecoration(mPresenter.getCrimeDecoration());
        rvCrime.setLayoutManager(new LinearLayoutManager(getActivity()));
        CrimeListAdapter adapter = mPresenter.getCrimeListAdapter(info);
        adapter.setOnLookClickListener(new OnItemContentViewClickListener<CrimeInfo>() {
            @Override
            public void onItemContentViewClick(View v, CrimeInfo data, int position) {
                ToastUtils.showShort("你点击了位置为" + position + "的查看按钮");
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
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化摄像头maker
     */
    private void initMaker(List<CameraInfo> cameraInfos){
        for (int i = 0; i < cameraInfos.size(); i++) {
            addMakerToMap(cameraInfos.get(i),nCamera);
        }
    }

    private void addMakerToMap(CameraInfo cameraInfo,BitmapDescriptor camera) {
        LatLng latLng = new LatLng(Double.valueOf(cameraInfo.getLAT()),Double.valueOf(cameraInfo.getLON()));
        MarkerOptions op = new MarkerOptions().position(latLng).icon(camera).zIndex(9)
                .draggable(false);
        Marker marker = (Marker) mBaiduMap.addOverlay(op);
        markerList.add(marker);
    }

    Marker lastMaker;
    private void updateMakerToMap(CameraInfo cameraInfo,int position){
        if(lastMaker != null){
            lastMaker.setIcon(nCamera);
        }
        lastMaker = markerList.get(position);
        lastMaker.setIcon(sCamera);
        LatLng latLng = new LatLng(Double.valueOf(cameraInfo.getLAT()),Double.valueOf(cameraInfo.getLON()));
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

    }

    private class CameraTCPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Data");

        }
    }
}
