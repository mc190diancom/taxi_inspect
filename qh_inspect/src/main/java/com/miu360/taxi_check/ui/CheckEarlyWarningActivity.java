package com.miu360.taxi_check.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
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
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.service.PushService;
import com.miu360.taxi_check.util.SortUtil;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class CheckEarlyWarningActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;
	@ViewInject(R.id.my_location_tv)
	private TextView my_location_tv;
	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.query)
	private TextView query;
	@ViewInject(R.id.car_number_yujing)
	private TextView car_number_yujing;
	@ViewInject(R.id.car_yujing_distance)
	private TextView car_yujing_distance;
	@ViewInject(R.id.check_more_yujing)
	private TextView check_more_yujing;
	@ViewInject(R.id.ll_yujing_detail_info)
	private LinearLayout ll_yujing_detail_info;
	@ViewInject(R.id.list_show)
	private ListView list_show;
	@ViewInject(R.id.one_show)
	private LinearLayout one_show;
	@ViewInject(R.id.image_yujing_above)
	private ImageButton image_yujing_above;
	@ViewInject(R.id.rg)
	private RadioGroup radioGroup;
	@ViewInject(R.id.header_ll)
	private LinearLayout header_ll;

	BaiduMap mBaiduMap;
	// 设置Marker的点击事件时，加个tag来进行区分

	LocationClient mLocClient;
	//public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private InfoWindow mInfoWindow;
	private View view;
	VehicleInfo iInfo;
	TextView name;
	//TextView address;
	TextView corpName;
	TextView speed;
	TextView color;
	TextView direction;
	int bestDistance = 0;
	LatLng des;
	VehiclePositionModex1 p;
	HashMap<String, VehiclePositionModex1> listMap;
	SimpleAdapter adapter;
	double lat = 0.0;
	double lon = 0.0;
	int key;
	private PositionBroadCast broadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_early_warning);
		File file = new File("");
		registerReceiver(broadcast = new PositionBroadCast(),
				new IntentFilter(SelectLocationActivity.CHOSE_LOCATION));
		initView();
		//initHanlder();
		list_show.setVisibility(View.GONE);

		mCurrentMode = LocationMode.NORMAL;

		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.showZoomControls(false);
		// 定位初始化
		mLocClient = new LocationClient(this);
		//mLocClient.registerLocationListener(myListener);

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				for (int i = 0; i < markerList.size(); i++) {
					Marker marker = markerList.get(i);
					if (arg0 == marker) {
						Log.e("2", i + "");
						view = LayoutInflater.from(self).inflate(R.layout.yujingcarbuwindow, null);
						view.setLayoutParams(new LayoutParams());
						name = (TextView) view.findViewById(R.id.zhifa_name);
						//address = (TextView) view.findViewById(R.id.zhifa_address);
						corpName = (TextView) view.findViewById(R.id.zhifa_corpName);
						speed = (TextView) view.findViewById(R.id.zhifa_speed);
						color = (TextView) view.findViewById(R.id.zhifa_color);
						direction = (TextView) view.findViewById(R.id.zhifa_fangxiang);

						//type = (TextView) view.findViewById(R.id.zhifa_type);
						MarkerOptions ooA = null;

						Bundle b = marker.getExtraInfo();
						p = (VehiclePositionModex1) b.getSerializable("" + i);
						key = i;
						des = marker.getPosition();
						//reverseGeoCode(address, des);
						name.setText(p.getGSPDATA().getVname());
						speed.setText(p.getGSPDATA().getSpeed() + "km/h");
						double fx = p.getGSPDATA().getHead();
						if(p.getGSPDATA().getHead()<0||p.getGSPDATA().getHead()>360){
							fx = p.getGSPDATA().getHead()>0?p.getGSPDATA().getHead():0;
							fx = fx>360?fx%360:fx;
						}
						direction.setText(GetHead.getHeadingDesc(fx));


						car_number_yujing.setText(p.getGSPDATA().getVname());
						bestDistance = 0;
						bestDistance += DistanceUtil.getDistance(new LatLng(lat, lon),
								new LatLng(des.latitude, des.longitude));
						car_yujing_distance.setText(showDistance(bestDistance));

						final VehicleInfo info = new VehicleInfo();
						info.setVname(p.getGSPDATA().getVname().toUpperCase());
						info.setStartIndex(0);
						info.setEndIndex(10);
						AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

							@Override
							public Result<List<VehicleInfo>> call() throws Exception {
								return WeiZhanData.queryVehicleInfo(info);
							}
						}, new Callback<Result<List<VehicleInfo>>>() {

							@Override
							public void onHandle(Result<List<VehicleInfo>> result) {
								if (result.ok()) {
									if (result.getData().equals("[]")) {

									} else {
										iInfo = result.getData().get(0);
										corpName.setText(iInfo.getCompany());
										color.setText(iInfo.getVehicleColor());
										//type.setText(iInfo.getVehicleType());
										mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), des, -47,
												null);
										mBaiduMap.showInfoWindow(mInfoWindow);
									}
								} else {
									UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});
					}
				}
				return true;
			}
		});
		/*//如果在中国的话
		if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng)) {
			LatLng ll = new LatLng(MsgConfig.lat, MsgConfig.lng);
			reverseGeoCode(my_location_tv, ll);
		}else {
			//UIUtils.toast(self, "当前卫星信号差，请拿到室外或者卫星信号好的地方！", Toast.LENGTH_SHORT);
		}*/
		//initHandler();
		//initMapView();
		//rePosition();
		Position();
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) ||
						MsgConfig.lat == 0.0) {
	            		 /*lat = 39.944206;
	            		 lon = 116.359777;
	            		 AddressResolution(null);*/
					UIUtils.toast(self, "当前GPS信号差，请拿到室外等开阔的地方！", Toast.LENGTH_SHORT);
				}
			}

		};
	};

	void initHanlder(){
		new Handler().postDelayed(new Runnable(){
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 8000);
	}

	public static DecimalFormat distanceFormate = new DecimalFormat("0.0");

	/**
	 * 判断是M还是KM
	 */
	public static String showDistance(double d) {
		if (d < 1000) {
			return (int) d + "m";
		} else {
			String i = distanceFormate.format(d / 1000.0) + "km";
			return i;
		}
	}

	String range = "1000";

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "稽查预警");
		aim_location.setOnClickListener(this);
		my_location_tv.setOnClickListener(this);
		query.setOnClickListener(this);
		check_more_yujing.setOnClickListener(this);
		ll_yujing_detail_info.setVisibility(View.GONE);
		image_yujing_above.setOnClickListener(this);
		adapter = new SimpleAdapter();
		list_show.setAdapter(adapter);
		list_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VehiclePositionModex1 info = (VehiclePositionModex1) parent.getItemAtPosition(position);
				Intent intent = new Intent(self, CheckEarlyWarningDetailInfoActivity.class);
				intent.putExtra("vehicleMap", info);
				startActivity(intent);

			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_button_One) {
					range = "500";
				} else if (checkedId == R.id.radio_button_Two) {
					range = "1000";
				} else if (checkedId == R.id.radio_button_Three) {
					range = "3000";
				}
			}
		});

		IntentFilter filter = new IntentFilter(PushService.ACTION_MSG);
		LocalBroadcastManager.getInstance(self).registerReceiver(getWarningInfoReceiver, filter);
	}

	private BroadcastReceiver getWarningInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String info = null;
			if (data != null && !TextUtils.isEmpty((info = data.getStringExtra("msg")))) {
				Log.e("推送消息", info);
			}
		}
	};
	LatLng position;
	ArrayList<VehiclePositionModex1> arrayList = new ArrayList<>();
	ArrayList<Marker> markerList = new ArrayList<>();
	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.keyi_car);//weigui_yujing_hangye


	/*public void startTimer() {
		Timer timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
					LatLng locationPosition = new LatLng(MsgConfig.lat, MsgConfig.lng);
					//reverseGeoCode(my_location_tv, locationPosition);
					task.cancel();
				}
			}
		};
		timer.schedule(task, 0, 10*1000);
	}*/

	TimerTask task;
	/**
	 * 定位SDK监听函数
	 *//*
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// mapView 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			if(isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) ||
    				MsgConfig.lat == 0.0){
				lat = 39.944206;
				lon = 116.359777;
			}else{
				lat = MsgConfig.lat;
				lon = MsgConfig.lng;
			}
			AddressResolution(location);

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void AddressResolution(BDLocation location){
		LatLng ll = new LatLng(lat, lon);
		position = ll;
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		converter.coord(ll);
		LatLng desLatLng = converter.convert();

		Log.e("源坐标", ll + "");
		Log.e("转换后坐标", desLatLng + "");
		if(location!=null){
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(desLatLng.latitude).longitude(desLatLng.longitude).build();

			mBaiduMap.setMyLocationData(locData);

			mLocClient.stop();
		}
		if (ll != null) {

			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();

			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

			reverseGeoCode(my_location_tv, ll);
		}

	}*/


	LatLng ll = null;
	//初始化定位
	private void Position(){
		//double lat,lng;
		if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
			lat = MsgConfig.select_lat;
			lon = MsgConfig.select_lng;
			ll = new LatLng(lat, lon);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			reverseGeoCode1(my_location_tv, lat, lon);
		}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
			lat = MsgConfig.lat;
			lon = MsgConfig.lng;
			ll = new LatLng(lat, lon);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			reverseGeoCode1(my_location_tv, lat, lon);
		}else{

		}
	}

	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_renyuan);

	@Override
	public void onClick(View v) {
		if (v == aim_location) {
			Intent intent = new Intent(self, SelectLocationActivity.class);
			intent.putExtra("thisAdd", "");
			startActivity(intent);
			//initMapView();
		} else if (v == my_location_tv) {
			Intent intent = new Intent(self, SelectLocationActivity.class);
			intent.putExtra("thisAdd", "");
			startActivity(intent);
		}else if (v == query) {
			if (isCommon.outOfChina(lat, lon)) {
				UIUtils.toast(self, "坐标不正确，请确认定位是否成功", Toast.LENGTH_SHORT);
				return;
			}
			final MyProgressDialog d = Windows.waiting(self);
			ll_yujing_detail_info.setVisibility(View.GONE);
			markerList.clear();
			mBaiduMap.clear();
			arrayList.clear();
			AsyncUtil.goAsync(new Callable<Result<List<VehiclePositionModex1>>>() {

				@Override
				public Result<List<VehiclePositionModex1>> call() throws Exception {
					//return WeiZhanData.queryKeYiPositionInfo(range, MsgConfig.lat + "", MsgConfig.lng + "");
					return WeiZhanData.queryKeYiPositionInfo1( lon + "", lat + "",range);
				}
			}, new Callback<Result<List<VehiclePositionModex1>>>() {

				@Override
				public void onHandle(Result<List<VehiclePositionModex1>> result) {
					if (result.ok()) {
						d.dismiss();
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
							return;
						}
						ll_yujing_detail_info.setVisibility(View.VISIBLE);
						arrayList.addAll(result.getData());
						LatLng des = null;
						LatLng des1 = null;
						listMap = new HashMap<>();
						SortUtil.sortListNyDistance(lat, lon, arrayList);
						for (int i = 0; i < arrayList.size(); i++) {

							LatLng ll = new LatLng(arrayList.get(i).getGSPDATA().getLat(), arrayList.get(i).getGSPDATA().getLon());

							/*CoordinateConverter cover = new CoordinateConverter();
							cover.from(CoordType.GPS);
							cover.coord(ll);
							des = cover.convert();*/
							if(i == 0){
								des = ll;
							}
							MarkerOptions op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(true);
							Marker marker = (Marker) mBaiduMap.addOverlay(op);

							Bundle b = new Bundle();
							key = markerList.size();
							b.putSerializable("" + markerList.size(), arrayList.get(i));
							marker.setExtraInfo(b);
							markerList.add(marker);
							listMap.put("" + key, arrayList.get(i));
						}
						/*CoordinateConverter cover = new CoordinateConverter();
						cover.from(CoordType.GPS);
						cover.coord(new LatLng(lat, lon));
						des1 = cover.convert();*/
						des1 = new LatLng(lat, lon);
						bestDistance = 0;
						car_number_yujing.setText(arrayList.get(0).getGSPDATA().getVname());
						bestDistance += DistanceUtil.getDistance(des1, des);
						car_yujing_distance.setText(showDistance(bestDistance));

						MarkerOptions ops = new MarkerOptions().position(des1).icon(ba).zIndex(5).draggable(true);
						mBaiduMap.addOverlay(ops);

					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
					}
				}
			});
		} else if (v == check_more_yujing) {
			Intent intent = new Intent(self, CheckEarlyWarningDetailInfoActivity.class);
			intent.putExtra("vehicleMap", listMap.get(key + ""));
			startActivity(intent);
		} else if (v == image_yujing_above) {
			if (list_show.getVisibility() == View.GONE) {
				header_ll.setVisibility(View.GONE);
				one_show.setVisibility(View.GONE);
				list_show.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangxiazhankai));
			} else {
				header_ll.setVisibility(View.VISIBLE);
				one_show.setVisibility(View.VISIBLE);
				list_show.setVisibility(View.GONE);
				image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangshangzhankai));
			}
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		if (broadcast != null) {
			unregisterReceiver(broadcast);
		}
		LocalBroadcastManager.getInstance(self).unregisterReceiver(getWarningInfoReceiver);
	}

	/**
	 * 反Geo搜索
	 */
	/*public void reverseGeoCode(final TextView tv, final LatLng ptCenter) {
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
	}*/

	private class SimpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(self).inflate(R.layout.checkdetailwarnning, null);
				convertView.setTag(holder);
				holder.car_yujing_distance = (TextView) convertView.findViewById(R.id.car_yujing_distance);
				holder.car_number_yujing = (TextView) convertView.findViewById(R.id.car_number_yujing);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LatLng des = null;
			LatLng des1 = null;

			VehiclePositionModex1 item = (VehiclePositionModex1) getItem(position);

			holder.car_number_yujing.setText(item.getGSPDATA().getVname());
			LatLng ll = new LatLng(item.getGSPDATA().getLat(), item.getGSPDATA().getLon());
			CoordinateConverter cover = new CoordinateConverter();
			cover.from(CoordType.GPS);
			cover.coord(ll);
			des = cover.convert();

			cover.coord(new LatLng(lat, lon));
			des1 = cover.convert();
			bestDistance = 0;
			bestDistance += DistanceUtil.getDistance(des1, des);
			holder.car_yujing_distance.setText(showDistance(bestDistance));

			return convertView;
		}

	}


	/**
	 * 重定位广播
	 */
	class PositionBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("jinlemem", "jinlemem");
			rePosition();
		}

	}

	/**
	 * 重定位
	 */
	protected void rePosition() {
		LatLng ll = null;
		if (MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0) {
			LatLng locationPosition = new LatLng(lat = MsgConfig.select_lat, lon = MsgConfig.select_lng);
			ll = locationPosition;
			//reverseGeoCode(my_location_tv, locationPosition);
			reverseGeoCode1(my_location_tv,MsgConfig.select_lat,MsgConfig.select_lng);
		} else {
			if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
				LatLng locationPosition = new LatLng(lat = MsgConfig.lat, lon = MsgConfig.lng);
				ll = locationPosition;
				//reverseGeoCode(my_location_tv, locationPosition);
				reverseGeoCode1(my_location_tv,MsgConfig.lat,MsgConfig.lng);
			} else {
				Log.e("locationPosition", "locationPosition");
				LatLng locationPosition = new LatLng(lat, lon);
				//reverseGeoCode(my_location_tv, locationPosition);
				reverseGeoCode1(my_location_tv,lat,lon);
			}
		}
		if (ll != null) {
			ll_yujing_detail_info.setVisibility(View.GONE);
			arrayList.clear();
			markerList.clear();
			mBaiduMap.clear();
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			//reverseGeoCode1(my_location_tv, ll);
			// UIUtils.toast(act, ll.toString(), Toast.LENGTH_SHORT);
		}
	}

	private class ViewHolder {
		TextView car_yujing_distance;
		TextView car_number_yujing;
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
}
