package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.data.HistoryData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CarPositionInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_early_warning_detail_info);
		info = (VehiclePositionModex1) getIntent().getSerializableExtra("vehicleMap");
		Yj = getIntent().getStringArrayListExtra("YuJingList");
		initView();
		mBaiduMap = mMapView.getMap();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "车辆详情");
		pref = new UserPreference(self);
		ll_header.setVisibility(View.GONE);
		turn_law.setOnClickListener(this);
		turn_position.setOnClickListener(this);
		back_tv.setOnClickListener(this);
		mMapView.showZoomControls(false);
		initData();
	}

	//BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.taxi_warnning);
	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi);
	BitmapDescriptor bd1 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi1);
	BitmapDescriptor bd2 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi2);
	BitmapDescriptor bd3 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi3);
	BitmapDescriptor bd4 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi4);

	private void initData() {
		Log.e("", info == null ? "空" : "非空");
		final VehicleInfo infoS = new VehicleInfo();
		infoS.setVname(info.getGSPDATA().getVname().toUpperCase());
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
					Log.e("", "res:" + result.getData().toString());
					if (result.getData().toString().equals("[]")) {

					} else {
						iIInfo = result.getData().get(0);
						car_vname.setText(info.getGSPDATA().getVname());
						car_speed.setText(info.getGSPDATA().getSpeed() + "km/h");
						LatLng ll = new LatLng(info.getGSPDATA().getLat(), info.getGSPDATA().getLon());
						Log.e("", "res:" + ll.toString());
						reverseGeoCode(car_address, ll);

						car_alarmReason.setText(info.getADK_WFXW());
						car_time.setText(info.getGSPDATA().getStrDate());
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
						String s = info.getRKSM();
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
		// final CarCarInfo infoCar = new CarCarInfo();
		// infoCar.setZFZH(info.getVname().toUpperCase());
		// AsyncUtil.goAsync(new Callable<Result<String>>() {
		//
		// @Override
		// public Result<String> call() throws Exception {
		// return WeiZhanData.queryCarPositionInfo(infoCar);
		// }
		// }, new Callback<Result<String>>() {
		//
		// @Override
		// public void onHandle(Result<String> result) {
		// if (result.ok()) {
		// ll_header.setVisibility(View.VISIBLE);
		// String res = result.getData();
		// Log.e("返回值", res);
		// // || res.equals("{}")
		// if (!TextUtils.isEmpty(res)) {
		// iInfo = new Gson().fromJson(res, CarPositionInfo.class);
		//
		// car_vname.setText(info.getVname());
		// car_speed.setText(info.getSpeed() + "km/h");
		// corp_name.setText(iInfo.getName());
		// car_color.setText(iInfo.getColor());
		// car_type.setText(iInfo.getModel());
		// LatLng ll = new LatLng(info.getLat(), info.getLon());
		// reverseGeoCode(car_address, ll);
		// car_time.setText(info.getStrDate());
		// LatLng des = null;
		// CoordinateConverter cover = new CoordinateConverter();
		// cover.from(CoordType.GPS);
		// cover.coord(ll);
		// des = cover.convert();
		// MapStatus mapStatus = new
		// MapStatus.Builder().target(des).zoom(16).build();
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
		// MarkerOptions op = new
		// MarkerOptions().position(des).icon(bd).zIndex(9).draggable(true);
		// mBaiduMap.addOverlay(op);
		//
		// } else {
		// UIUtils.toast(self, "无返回数据", Toast.LENGTH_LONG);
		// }
		// } else {
		// UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
		// }
		// }
		// });
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
					Log.e("locationPosition", "locationPosition2");
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
				intent.putExtra("Vname", info.getGSPDATA().getVname());
				intent.putExtra("isTurn", true);
				startActivity(intent);
			}else{
				UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
			}
		} else if(v == turn_position){
			Intent intent = new Intent(self, VehiclePositionActivity.class);
			intent.putExtra("Vname", info.getGSPDATA().getVname());
			intent.putExtra("isTurn", true);
			startActivity(intent);
		} else if (v == back_tv) {
			finish();
		}
	}
}
