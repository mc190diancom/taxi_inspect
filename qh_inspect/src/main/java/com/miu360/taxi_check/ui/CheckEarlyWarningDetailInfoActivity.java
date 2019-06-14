package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.config.Constance;
import com.miu30.common.ui.entity.AlarmDetailInfo;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.data.HistoryData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CarPositionInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePosition;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@Route(path = Constance.ACTIVITY_URL_WARNINGDETAIL)
public class CheckEarlyWarningDetailInfoActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.ll_header)
	private LinearLayout ll_header;
	@ViewInject(R.id.car_vname)
	private TextView car_vname;
	@ViewInject(R.id.car_color)
	private TextView car_color;
	@ViewInject(R.id.car_type)
	private TextView car_type;
	@ViewInject(R.id.corp_name)
	private TextView corp_name;
	@ViewInject(R.id.car_speed)
	private TextView car_speed;
	@ViewInject(R.id.car_address)
	private TextView car_address;
	@ViewInject(R.id.car_alarmReason)
	private TextView car_alarmReason;
	@ViewInject(R.id.car_fangxiang)
	private TextView car_fangxiang;
	@ViewInject(R.id.car_time)
	private TextView car_time;
	@ViewInject(R.id.turn_law)
	private TextView turn_law;
	@ViewInject(R.id.turn_position)
	private TextView turn_position;
	@ViewInject(R.id.back_tv)
	private TextView back_tv;

	VehiclePositionModex1 info;
	CarPositionInfo iInfo;
	VehicleInfo iIInfo;
	BaiduMap mBaiduMap;
	ArrayList<String> Yj;
	UserPreference pref;

	@Autowired
	AlarmDetailInfo alarmDetailInfo;

	int type;//0为可疑车辆预警，1为摄像头预警

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_early_warning_detail_info);
		info = (VehiclePositionModex1) getIntent().getSerializableExtra("vehicleMap");
		if(info != null){
			alarmDetailInfo = new AlarmDetailInfo();
			alarmDetailInfo.setVname(info.getGSPDATA().getVname());
			alarmDetailInfo.setADK_WFXW(info.getADK_WFXW());
			alarmDetailInfo.setRKSM(info.getRKSM());
			alarmDetailInfo.setSpeed(info.getGSPDATA().getSpeed()+"");
			alarmDetailInfo.setOccurTime(info.getGSPDATA().getStrDate());
			alarmDetailInfo.setLat(info.getGSPDATA().getLat());
			alarmDetailInfo.setLon(info.getGSPDATA().getLon());
			type = 0;
		}else {
			type = 1;
			ARouter.getInstance().inject(this);
		}
		Yj = getIntent().getStringArrayListExtra("YuJingList");

		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "车辆详情");
		pref = new UserPreference(self);
		ll_header.setVisibility(View.GONE);
		turn_law.setOnClickListener(this);
		turn_position.setOnClickListener(this);
		back_tv.setOnClickListener(this);
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(false);
		if(0 == type){
			initData();
		}else{
			showInfo();
			getCarPositionInfo();
		}
	}

	private void showInfo() {
		ll_header.setVisibility(View.VISIBLE);
		car_vname.setText(alarmDetailInfo.getVname());
		LatLng ll = new LatLng(alarmDetailInfo.getLat(), alarmDetailInfo.getLon());
		reverseGeoCode(car_address, ll);
		car_alarmReason.setText(alarmDetailInfo.getADK_WFXW());

		LatLng des = null;
		CoordinateConverter cover = new CoordinateConverter();
		cover.from(CoordType.GPS);
		cover.coord(ll);
		des = cover.convert();

		MapStatus mapStatus = new MapStatus.Builder().target(des).zoom(16).build();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
		MarkerOptions op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(false);
		mBaiduMap.addOverlay(op);
	}

	private void getCarPositionInfo() {
		if(alarmDetailInfo == null) return;
		if(!alarmDetailInfo.getVname().startsWith("京")){
			alarmDetailInfo.setVname(alarmDetailInfo.getVname().substring(1));
		}
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return WeiZhanData.queryCarPositionInfo(alarmDetailInfo.getVname().toUpperCase());
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					String res = result.getData();
					if (!TextUtils.isEmpty(res) && res.indexOf("无该车辆定位信息") == -1) {
						VehiclePosition info = new Gson().fromJson(res, VehiclePosition.class);
						car_speed.setText(info.getSpeed() + "km/h");
						car_time.setText(info.getStrDate());
						corp_name.setText(info.getCorpName());
						car_color.setText(info.getColor());
						car_type.setText(info.getModel());
					} else {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi);
	BitmapDescriptor bd1 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi1);
	BitmapDescriptor bd2 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi2);
	BitmapDescriptor bd3 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi3);
	BitmapDescriptor bd4 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi4);

	private void initData() {
		final VehicleInfo infoS = new VehicleInfo();
		if(alarmDetailInfo == null) return;
		if(!alarmDetailInfo.getVname().startsWith("京")){
			alarmDetailInfo.setVname(alarmDetailInfo.getVname().substring(1));
		}
		infoS.setVname(alarmDetailInfo.getVname().toUpperCase());
		infoS.setStartIndex(0);
		infoS.setEndIndex(10);
		AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

			@Override
			public Result<List<VehicleInfo>> call() throws Exception {
				return WeiZhanData.queryVehicleInfo(infoS);
			}
		}, new Callback<Result<List<VehicleInfo>>>() {

			@Override
			public void onHandle(Result<List<VehicleInfo>> result) {
				if (result.ok()) {
					ll_header.setVisibility(View.VISIBLE);
					if (result.getData().toString().equals("[]")) {

					} else {
						iIInfo = result.getData().get(0);
						car_vname.setText(alarmDetailInfo.getVname());
						car_speed.setText(alarmDetailInfo.getSpeed() + "km/h");
						LatLng ll = new LatLng(alarmDetailInfo.getLat(), alarmDetailInfo.getLon());
						reverseGeoCode(car_address, ll);

						car_alarmReason.setText(alarmDetailInfo.getADK_WFXW());
						car_time.setText(alarmDetailInfo.getOccurTime());
						LatLng des = null;

						CoordinateConverter cover = new CoordinateConverter();
						cover.from(CoordType.GPS);
						cover.coord(ll);
						des = cover.convert();

						corp_name.setText(iIInfo.getCompany());
						car_color.setText(iIInfo.getVehicleColor());
						car_type.setText(iIInfo.getVehicleType());
						MapStatus mapStatus = new MapStatus.Builder().target(des).zoom(16).build();
						mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
						MarkerOptions op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(false);
						String s = alarmDetailInfo.getRKSM();
						if(Yj !=null&&!Yj.isEmpty()){
							for(int j=0,len = Yj.size();j<len;j++){
								if(j==0){
									if(s.equals(Yj.get(j))){
										op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(false);
										break;
									}
								}else if(j==1){
									if(s.equals(Yj.get(j))){
										op = new MarkerOptions().position(des).icon(bd1).zIndex(9).draggable(false);
										break;
									}
								}else if(j==2){
									if(s.equals(Yj.get(j))){
										op = new MarkerOptions().position(des).icon(bd2).zIndex(9).draggable(false);
										break;
									}
								}else if(j==3){
									if(s.equals(Yj.get(j))){
										op = new MarkerOptions().position(des).icon(bd3).zIndex(9).draggable(false);
										break;
									}
								}else{
									if(s.equals(Yj.get(j))){
										op = new MarkerOptions().position(des).icon(bd4).zIndex(9).draggable(false);
										break;
									}
								}
							}
						}
						mBaiduMap.addOverlay(op);

					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode(final TextView tv, final LatLng ptCenter) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return HistoryData.queryHistoryTrack(ptCenter);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					tv.setText(result.getData());
				}
			}
		});
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode1(final TextView tv, final double lat,final double lng) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return HistoryData.queryHistoryTrack1(lat,lng);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					tv.setText(result.getData());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == turn_law) {
			if(pref.getBoolean("isLaw", false)){
				Intent intent = new Intent(self, LawInpsectActivity.class);
				intent.putExtra("CompanyName", iIInfo.getCompany());
				intent.putExtra("Vname", alarmDetailInfo.getVname());
				intent.putExtra("isTurn", true);
				startActivity(intent);
			}else{
				UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
			}
		} else if(v == turn_position){
			Intent intent = new Intent(self, VehiclePositionActivity.class);
			intent.putExtra("Vname", alarmDetailInfo.getVname());
			intent.putExtra("isTurn", true);
			startActivity(intent);
		} else if (v == back_tv) {
			finish();
		}
	}
}
