package com.miu360.taxi_check.ui;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.ui.SelectLocationActivity;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.GetHead;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePosition;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VehiclePositionActivity extends BaseActivity implements OnClickListener {
	// @ViewInject(R.id.quanping_btn)
	// private CheckBox quanPing;
	@ViewInject(R.id.add_map)
	private ImageButton add_map;
	@ViewInject(R.id.minus_map)
	private ImageButton minus_map;
	@ViewInject(R.id.search_btn)
	private ImageButton search_btn;
	@ViewInject(R.id.ll_show_location)
	private LinearLayout ll_show_location;
	@ViewInject(R.id.car_detail_info_ll)
	private LinearLayout car_detail_info_ll;
	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.car_number)
	private TextView car_number;
	@ViewInject(R.id.company_name)
	private TextView company_name;
	@ViewInject(R.id.car_color)
	private TextView car_color;
	@ViewInject(R.id.car_type)
	private TextView car_type;
	@ViewInject(R.id.car_address)
	private TextView car_address;
	@ViewInject(R.id.time_last)
	private TextView last_time;
	@ViewInject(R.id.car_number_one)
	private TextView car_number_one;
	@ViewInject(R.id.car_number_two)
	private TextView car_number_two;
	@ViewInject(R.id.ll_alarmReson)
	private LinearLayout ll_alarmReson;
	@ViewInject(R.id.car_position_search)
	private LinearLayout car_position_search;
	@ViewInject(R.id.car_alarmReson)
	private TextView car_alarmReson;
	@ViewInject(R.id.car_keyi)
	private TextView car_keyi;
	@ViewInject(R.id.status)
	private TextView status;
	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;

	HeaderHolder header;
	boolean isShow = false;

	BaiduMap mBaiduMap;
	LocationClient mLocClient;
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	VehicleInfo iInfo;
	VehiclePosition info;
	String[] itemsOne = {"京","沪","津","渝","黑","吉","辽","蒙","冀","新","甘","青","陕","宁","豫","鲁","晋","皖", "鄂","湘","苏","川","黔","滇","桂","藏","浙","赣","粤","闽","台","琼","港","澳"};

	String vCarName = "";
	private boolean isDataHide = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_position);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);

		header = new HeaderHolder();
		header.init(self, "车辆定位");
		car_position_search.setOnClickListener(this);
		car_detail_info_ll.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		car_number_one.setOnClickListener(this);
		car_detail_info_ll.setVisibility(View.GONE);
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(false);
		mBaiduMap.setMaxAndMinZoomLevel(18, 11);
		add_map.setOnClickListener(this);
		minus_map.setOnClickListener(this);
		// 如果是跳转过来的
		if (getIntent().getBooleanExtra("isTurn", false)) {
			initTurn();
		}
		Position();
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if (car_position_search.getVisibility() == View.VISIBLE) {
					car_detail_info_ll.setVisibility(View.GONE);
					// header.root.setVisibility(View.GONE);
					car_position_search.setVisibility(View.GONE);
				} else if (car_position_search.getVisibility() == View.GONE) {
					if (isDataHide) {
						car_detail_info_ll.setVisibility(View.VISIBLE);
					}
					// header.root.setVisibility(View.VISIBLE);
					car_position_search.setVisibility(View.VISIBLE);
				}
			}
		});
		aim_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isLocationOk == false) {
					Windows.confirm(self, "GPS定位失败，是否手动定位？", new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(VehiclePositionActivity.this, SelectLocationActivity.class);
							startActivityForResult(intent, 1);
							// finish();
						}
					});
				} else {
					Position();
				}
			}
		});
		/*
		 * mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		 *
		 * @Override public boolean onMarkerClick(Marker theMarker) {
		 * if(theMarker == mA){ if(){ mB.remove(); }else{
		 *
		 * }
		 *
		 * } return true; } });
		 */
	}

	private void initTurn() {
		vCarName = getIntent().getStringExtra("Vname");
		String first = vCarName.substring(0, 1);
		String two = vCarName.substring(1);
		car_number_one.setText(first);
		car_number_two.setText(two);
		initGsonData();
	}

	Marker mA;
	Marker mB;
	View view;
	float zoom;
	BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.heade_roate_03);

	private void initData() {
		isDataHide = true;
		LatLng ll = new LatLng(info.getLat(), info.getLon());
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		converter.coord(ll);
		LatLng desLatLng = converter.convert();
		Log.e("desLatLng", "desLatLng:" + ll.latitude + "," + ll.longitude);
		Log.e("desLatLng", "desLatLng:" + desLatLng.latitude + "," + desLatLng.longitude);
		view = LayoutInflater.from(self).inflate(R.layout.infowindowlocation, null);
		TextView car_number_image = (TextView) view.findViewById(R.id.car_number);
		TextView fangxiang_location = (TextView) view.findViewById(R.id.fangxiang_location);
		TextView car_speed = (TextView) view.findViewById(R.id.car_speed);
		car_number_image.setText(info.getVname());
		// 当info.getHead()返回值为0或者大于360的情况
		int fx = info.getHead();
		if (info.getHead() < 0 || info.getHead() > 360) {
			fx = info.getHead() > 0 ? info.getHead() : 0;
			fx = fx > 360 ? fx % 360 : fx;
		}
		fangxiang_location.setText(GetHead.getHeadingDesc(fx));
		Log.e("", "req:" + info.getHead());
		car_speed.setText(info.getSpeed() + "km/h");
		// 车辆方向
		MarkerOptions ooB = new MarkerOptions().position(desLatLng).icon(bdA).zIndex(9).draggable(false)
				.rotate(360 - fx);
		MarkerOptions ooA = new MarkerOptions().position(desLatLng).icon(BitmapDescriptorFactory.fromView(view))
				.zIndex(8).draggable(false);

		mBaiduMap.setMaxAndMinZoomLevel(18, 11);
		MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).zoom(16).build();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
		if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
			LatLng pot = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
			MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			LatLng pot = new LatLng(MsgConfig.lat, MsgConfig.lng);
			MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
		} else {

		}
		car_number.setText(info.getVname());
		last_time.setText(info.getStrDate());
		car_alarmReson.setText(info.getAlarmReason());
		if (!"".equals(info.getvStatus())) {
			status.setText(info.getvStatus());
		} else {
			status.setVisibility(View.GONE);
		}
		if (iInfo != null) {
			company_name.setText(iInfo.getCompany());
			car_color.setText(iInfo.getVehicleColor());
			car_type.setText(iInfo.getVehicleType());
		}

		reverseGeoCode(car_address, desLatLng);
		mA = (Marker) mBaiduMap.addOverlay(ooA);
		mA.setAnchor(0f, 0.4f);
		mB = (Marker) mBaiduMap.addOverlay(ooB);

	}

	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_position);
	boolean isLocationOk = false;

	// 初始化定位
	private void Position() {
		double lat, lng;
		if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
			lat = MsgConfig.select_lat;
			lng = MsgConfig.select_lng;
			LatLng ll = new LatLng(lat, lng);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
			isLocationOk = true;
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			lat = MsgConfig.lat;
			lng = MsgConfig.lng;
			LatLng ll = new LatLng(lat, lng);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
			isLocationOk = true;
		} else {
			isLocationOk = false;

			// UIUtils.toast(self, "必须输入首字母", Toast.LENGTH_LONG);
		}
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

	String vname;

	private void initGsonData() {
		mBaiduMap.clear();
		vname = car_number_two.getText().toString();
		final MyProgressDialog pd = Windows.waiting(self);
		if (!TextUtils.isEmpty(vname)) {
			String first = vname.charAt(0) + "";
			if (!(Pattern.compile("[a-zA-Z]").matcher(first).matches())) {// 字母
				UIUtils.toast(self, "必须输入首字母", Toast.LENGTH_LONG);
				pd.cancel();
				return;
			}
		}

		if (vname.length() <= 5) {
			UIUtils.toast(self, "请输入完整的车牌号码!", Toast.LENGTH_LONG);
			pd.cancel();
			return;
		}
		vname = car_number_one.getText().toString() + vname;

		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return WeiZhanData.queryCarPositionInfo(vname.toUpperCase());
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					pd.dismiss();
					String res = result.getData();
					if (!TextUtils.isEmpty(res) && res.indexOf("无该车辆定位信息") == -1) {
						info = new Gson().fromJson(res, VehiclePosition.class);
						Log.e("pathQuery", "pathQuery" + info.getLat() + "===" + info.getLon());
						final VehicleInfo vinfo = new VehicleInfo();
						vinfo.setVname(vname.toUpperCase());
						vinfo.setStartIndex(0);
						vinfo.setEndIndex(10);
						AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

							@Override
							public Result<List<VehicleInfo>> call() throws Exception {
								return WeiZhanData.queryVehicleInfo(vinfo);
							}
						}, new Callback<Result<List<VehicleInfo>>>() {

							@Override
							public void onHandle(Result<List<VehicleInfo>> result) {
								if (result.ok()) {
									car_detail_info_ll.setVisibility(View.VISIBLE);
									if (!info.getAlarmReason().equals("")) {
										ll_alarmReson.setVisibility(View.VISIBLE);
										car_keyi.setText("(可疑车辆)");
										car_keyi.setTextColor(getResources().getColor(R.color.keyi_color));
									} else {
										ll_alarmReson.setVisibility(View.GONE);
										car_keyi.setText("(非可疑车辆)");
										car_keyi.setTextColor(getResources().getColor(R.color.unkeyi_color));
									}
									if (result.getData() != null && !result.getData().toString().equals("[]")) {
										iInfo = result.getData().get(0);
									}
                                    initData();
								} else {
									initData();
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

	@Override
	public void onClick(View v) {
		zoom = mBaiduMap.getMapStatus().zoom;
		/*
		 * if (v == add_map) { if (zoom >= 21) { add_map.setClickable(false); }
		 * else { add_map.setClickable(true); minus_map.setClickable(true); zoom
		 * += 1; mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new
		 * MapStatus.Builder().zoom(zoom).build())); } } else if (v ==
		 * minus_map) { if (zoom <= 3) { minus_map.setClickable(false); } else {
		 * add_map.setClickable(true); minus_map.setClickable(true); zoom -= 1;
		 * mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new
		 * MapStatus.Builder().zoom(zoom).build())); } }
		 */
		if (v == add_map) {
			if (mBaiduMap != null) {
				MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
				mBaiduMap.setMapStatus(zoomIn);
			}
		} else if (v == minus_map) {
			if (mBaiduMap != null) {
				MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
				mBaiduMap.setMapStatus(zoomOut);
			}
		} else if (v == search_btn) {
			initGsonData();
		} else if (v == car_number_one) {
			Windows.singleChoice(self, "请选择车牌", itemsOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					car_number_one.setText(itemsOne[position]);

				}
			});
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Position();
	}

	@Override
	protected void onPause() {
		// if(mMapView ==null){
		// mMapView=
		// }
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
