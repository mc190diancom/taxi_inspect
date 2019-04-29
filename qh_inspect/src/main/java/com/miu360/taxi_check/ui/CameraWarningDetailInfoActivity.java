package com.miu360.taxi_check.ui;

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
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CameraDetail;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex;
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

public class CameraWarningDetailInfoActivity extends BaseActivity implements OnClickListener {
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

	CameraDetail info;
	BaiduMap mBaiduMap;
	VehicleInfo iInfo;
	VehiclePositionModex vpinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_early_warning_detail_info);
		info = (CameraDetail) getIntent().getSerializableExtra("cameraDetail");
		initView();
		mBaiduMap = mMapView.getMap();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "车辆详情");
		ll_header.setVisibility(View.GONE);
		turn_law.setOnClickListener(this);
		back_tv.setOnClickListener(this);
		turn_position.setOnClickListener(this);
		mMapView.showZoomControls(false);
		initData();
	}

	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.taxi_warnning);

	private void initData() {
		if(info==null){
			return;
		}
		final MyProgressDialog pd = Windows.waiting(self);
		final String vname = info.getVname();

		AsyncUtil.goAsync(new Callable<Result<String>>() {
			@Override
			public Result<String> call() throws Exception {
				return WeiZhanData.queryCarPositionInfo(vname.toUpperCase());
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					String res = result.getData();
					if (!TextUtils.isEmpty(res)) {
						vpinfo = new Gson().fromJson(res, VehiclePositionModex.class);
						final VehicleInfo vInfo = new VehicleInfo();

						vInfo.setVname(vname.toUpperCase());
						vInfo.setStartIndex(0);
						vInfo.setEndIndex(10);
						AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

							@Override
							public Result<List<VehicleInfo>> call() throws Exception {
								return WeiZhanData.queryVehicleInfo(vInfo);
							}
						}, new Callback<Result<List<VehicleInfo>>>() {

							@Override
							public void onHandle(Result<List<VehicleInfo>> result) {
								pd.dismiss();
								if (result.ok()) {
									ll_header.setVisibility(View.VISIBLE);
									if (result.getData().toString().equals("[]")) {

									}
									iInfo = result.getData().get(0);
									car_vname.setText(info.getVname());
									car_speed.setText(vpinfo.getSpeed() + "km/h");
									LatLng ll = new LatLng(info.getLat(), info.getLon());
									Log.e("", "res:" + ll.toString());
									reverseGeoCode1(car_address, info.getLat(), info.getLon());
									car_alarmReason.setText(info.getAlarmReason());
									car_time.setText(vpinfo.getStrDate());
									LatLng des = null;
									des = ll;
									corp_name.setText(iInfo.getCompany());
									car_color.setText(iInfo.getVehicleColor());
									car_type.setText(iInfo.getVehicleType());
									MapStatus mapStatus = new MapStatus.Builder().target(des).zoom(16).build();
									mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
									MarkerOptions op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(true);
									mBaiduMap.addOverlay(op);
								} else {
									UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});

					} else {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
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
			Intent intent = new Intent(self, LawInpsectActivity.class);
			intent.putExtra("CompanyName", iInfo.getCompany());
			intent.putExtra("Vname", info.getVname());
			intent.putExtra("isTurn", true);
			startActivity(intent);
		} else if(v == turn_position){
			Intent intent = new Intent(self, VehiclePositionActivity.class);
			intent.putExtra("Vname", info.getVname());
			intent.putExtra("isTurn", true);
			startActivity(intent);
		}else if (v == back_tv) {
			finish();
		}
	}
}
