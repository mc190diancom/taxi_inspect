package com.miu360.taxi_check.fragment;

import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.common.GetHead;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.ui.CheckEarlyWarningDetailInfoActivity;
import com.miu30.common.ui.SelectLocationActivity;
import com.miu360.taxi_check.util.UIUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.view.ViewGroup;

public class YuJingCarDistributionFragment extends BaseFragment implements OnClickListener {
	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;
	@ViewInject(R.id.my_location_tv)
	private TextView my_location_tv;
	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.query)
	private TextView query;
	@ViewInject(R.id.header_ll)
	private LinearLayout header_ll;
	@ViewInject(R.id.car_number_yujing)
	private TextView car_number_yujing;
	@ViewInject(R.id.car_yujing_distance)
	private TextView car_yujing_distance;
	@ViewInject(R.id.check_more_yujing)
	private TextView check_more_yujing;
	@ViewInject(R.id.zhifa_reason)
	private TextView zhifa_reason;
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

	@ViewInject(R.id.add_map)
	private ImageButton add_map;
	@ViewInject(R.id.reduce_map)
	private ImageButton reduce_map;

	BaiduMap mBaiduMap;;

	ArrayList<VehiclePositionModex1> arrayList = new ArrayList<>();
	ArrayList<Marker> markerList = new ArrayList<>();
	private InfoWindow mInfoWindow;

	LocationClient mLocClient;
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	double lat = 0.0;
	double lon = 0.0;
	private PositionBroadCast broadcast;

	TextView name;
	TextView corpName;
	TextView speed;
	TextView color;
	TextView direction;
	LatLng des;
	VehiclePositionModex1 p;
	VehicleInfo iInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().registerReceiver(broadcast = new PositionBroadCast(),
				new IntentFilter(SelectLocationActivity.CHOSE_LOCATION));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_yujing_distribution, null);
		initView(root);
		return root;
	}

	View view;

	private void initView(View root) {
		ViewUtils.inject(this, root);
		query.setOnClickListener(this);
		my_location_tv.setOnClickListener(this);
		add_map.setOnClickListener(this);
		reduce_map.setOnClickListener(this);
		mCurrentMode = LocationMode.NORMAL;
		list_show.setVisibility(View.GONE);

		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.showZoomControls(false);
		mBaiduMap.setMaxAndMinZoomLevel(18, 11);
		// 定位初始化
		mLocClient = new LocationClient(act);
		//mLocClient.registerLocationListener(myListener);
		Position();
		startTimer();
		LocationClientOption option = new LocationClientOption();

		mLocClient.setLocOption(option);

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));

		aim_location.setOnClickListener(this);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				for (int i = 0; i < markerList.size(); i++) {
					Marker marker = markerList.get(i);
					if (arg0 == marker) {
						view = LayoutInflater.from(act).inflate(R.layout.yujingcarbuwindow, null);
						view.setLayoutParams(new LayoutParams());
						name = (TextView) view.findViewById(R.id.zhifa_name);
						//address = (TextView) view.findViewById(R.id.zhifa_address);
						corpName = (TextView) view.findViewById(R.id.zhifa_corpName);
						speed = (TextView) view.findViewById(R.id.zhifa_speed);
						color = (TextView) view.findViewById(R.id.zhifa_color);
						direction = (TextView) view.findViewById(R.id.zhifa_fangxiang);
						zhifa_reason = (TextView) view.findViewById(R.id.zhifa_reason);
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
						zhifa_reason.setText(p.getKYX_MCS());

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
									UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});
					}
				}
				return true;
			}
		});
		check_more_yujing.setOnClickListener(this);
		ll_yujing_detail_info.setVisibility(View.GONE);
		image_yujing_above.setOnClickListener(this);
		adapter = new SimpleAdapter();
		list_show.setAdapter(adapter);
		list_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VehiclePositionModex1 info = (VehiclePositionModex1) parent.getItemAtPosition(position);
				Intent intent = new Intent(act, CheckEarlyWarningDetailInfoActivity.class);
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
	}

	String range = "1000";
	SimpleAdapter adapter;
	int bestDistance = 0;
	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_renyuan);

	private class ViewHolder {
		TextView car_yujing_distance;
		TextView car_number_yujing;
	}

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
				convertView = LayoutInflater.from(act).inflate(R.layout.checkdetailwarnning, null);
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

			/*CoordinateConverter cover = new CoordinateConverter();
			cover.from(CoordType.GPS);
			cover.coord(ll);
			des = cover.convert();

			cover.coord(new LatLng(lat, lon));
			des1 = cover.convert();*/
			des = ll;
			des1 = new LatLng(lat, lon);
			bestDistance = 0;
			bestDistance += DistanceUtil.getDistance(des1, des);
			holder.car_yujing_distance.setText(showDistance(bestDistance));

			return convertView;
		}

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

	public void startTimer() {
		Timer timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				if(lat == 0){
					Position();
				}else{
					task.cancel();
				}
			}
		};
		timer.schedule(task, 0, 5 * 1000);
	}

	/**
	 * 重定位广播
	 */
	class PositionBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
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
			reverseGeoCode1(my_location_tv,MsgConfig.select_lat,MsgConfig.select_lng);
		} else {
			if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
				LatLng locationPosition = new LatLng(lat = MsgConfig.lat, lon = MsgConfig.lng);
				ll = locationPosition;
				reverseGeoCode1(my_location_tv,MsgConfig.lat,MsgConfig.lng);
			} else {
				/*Log.e("locationPosition", "locationPosition");
				LatLng locationPosition = new LatLng(lat, lon);
				reverseGeoCode1(my_location_tv,lat,lon);*/
			}
		}
		if (ll != null) {
			ll_yujing_detail_info.setVisibility(View.GONE);
			arrayList.clear();
			markerList.clear();
			mBaiduMap.clear();
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(true);
			mBaiduMap.addOverlay(ops);
		}
	}

	TimerTask task;

	/**
	 * 定位SDK监听函数
	 *//*
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// Log.e("location",
			// "location"+MsgConfig.lat+"---"+"MsgConfig.lng");
			if (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) || MsgConfig.lat == 0.0) {
				lat = 39.944206;
				lon = 116.359777;
			} else {
				lat = MsgConfig.lat;
				lon = MsgConfig.lng;
			}
			if (MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0) {
				lat = MsgConfig.select_lat;
				lon = MsgConfig.select_lng;
			}
			// isDethData = false;
			AddressResolution(location);

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}*/

	/*private void AddressResolution(BDLocation location) {
		LatLng ll = new LatLng(lat, lon);
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		converter.coord(ll);
		LatLng desLatLng = converter.convert();
		Log.e("坐标", "ll" + ll.toString());
		Log.e("坐标", "ll" + desLatLng.toString());
		if (location != null) {
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(desLatLng.latitude).longitude(desLatLng.longitude).build();

			mBaiduMap.setMyLocationData(locData);
			mLocClient.stop();
		}
		if (ll != null) {
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			reverseGeoCode1(my_location_tv, lat, lon);
			// UIUtils.toast(act, ll.toString(), Toast.LENGTH_SHORT);
		}
	}*/

	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.keyi_car);

	@Override
	public void onClick(View v) {
		if (v == aim_location) {
			/*Log.e("aim_location", "location" + MsgConfig.lat + "---" + MsgConfig.lng);
			if (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) || MsgConfig.lat == 0.0) {
				//UIUtils.toast(act, "当前GPS信息", Toast.LENGTH_SHORT);
				AddressResolution(null);
			} else {
				initMapView();
			}*/
			Intent intent = new Intent(act, SelectLocationActivity.class);
			intent.putExtra("thisAdd", "");
			startActivity(intent);
		} else if (v == add_map) {
			if(mBaiduMap != null){
				MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
				mBaiduMap.setMapStatus(zoomIn);
			}
		} else if (v == reduce_map) {
			if(mBaiduMap != null){
				MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
				mBaiduMap.setMapStatus(zoomOut);
			}
		} else if (v == query) {
			if (isCommon.outOfChina(lat, lon)) {
				UIUtils.toast(act, "坐标不正确，请确认定位是否成功", Toast.LENGTH_SHORT);
				return;
			}
			final MyProgressDialog d = Windows.waiting(act);
			ll_yujing_detail_info.setVisibility(View.GONE);
			arrayList.clear();
			markerList.clear();
			mBaiduMap.clear();
			AsyncUtil.goAsync(new Callable<Result<List<VehiclePositionModex1>>>() {

				@Override
				public Result<List<VehiclePositionModex1>> call() throws Exception {
					// return WeiZhanData.queryKeYiPositionInfo( "" +
					// MsgConfig.lng,"" + MsgConfig.lat,"1000");
					return WeiZhanData.queryKeYiPositionInfo1("" + lon, "" + lat, range);
				}
			}, new Callback<Result<List<VehiclePositionModex1>>>() {

				@Override
				public void onHandle(Result<List<VehiclePositionModex1>> result) {
					d.dismiss();
					LatLng des1 = null;
					des1 = new LatLng(lat, lon);
					MarkerOptions ops = new MarkerOptions().position(des1).icon(ba).zIndex(5).draggable(true);
					mBaiduMap.addOverlay(ops);
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(act, "查不到此信息", Toast.LENGTH_LONG);
							return;
						}
						ELog.d("keyi:", "keyi:" + result.getData().toString());
						arrayList.addAll(result.getData());
						ll_yujing_detail_info.setVisibility(View.VISIBLE);
						if (arrayList.size() == 0) {
							UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}
						LatLng des = null;
						listMap = new HashMap<>();
						for (int i = 0; i < arrayList.size(); i++) {

							LatLng ll = new LatLng(arrayList.get(i).getGSPDATA().getLat(),
									arrayList.get(i).getGSPDATA().getLon());
							des = ll;
							MarkerOptions op = new MarkerOptions().position(des).icon(bd).zIndex(9).draggable(true);
							Marker marker = (Marker) mBaiduMap.addOverlay(op);
							Bundle b = new Bundle();
							key = markerList.size();
							b.putSerializable("" + markerList.size(), arrayList.get(i));
							marker.setExtraInfo(b);
							markerList.add(marker);
							listMap.put("" + key, arrayList.get(i));
						}
						bestDistance = 0;
						car_number_yujing.setText(arrayList.get(arrayList.size() - 1).getGSPDATA().getVname());
						bestDistance += DistanceUtil.getDistance(des1, des);
						car_yujing_distance.setText(showDistance(bestDistance));
					} else {
						UIUtils.toast(act, "查不到此信息", Toast.LENGTH_LONG);
					}
				}
			});
		} else if (v == check_more_yujing) {
			Intent intent = new Intent(act, CheckEarlyWarningDetailInfoActivity.class);
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
		} else if (v == my_location_tv) {
			Intent intent = new Intent(act, SelectLocationActivity.class);
			intent.putExtra("thisAdd", "");
			startActivity(intent);
		}
	}

	HashMap<String, VehiclePositionModex1> listMap;
	int key;

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		if (broadcast != null) {
			getActivity().unregisterReceiver(broadcast);
		}
		if(task != null){
			task.cancel();
		}
		super.onDestroy();
	}

	//初始化定位
	private void Position(){
		//double lat,lng;
		if(mBaiduMap != null){
			if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
				lat = MsgConfig.select_lat;
				lon = MsgConfig.select_lng;
				LatLng ll = new LatLng(lat, lon);
				MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
				MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(true);
				mBaiduMap.addOverlay(ops);
				reverseGeoCode1(my_location_tv, lat, lon);
			}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
				lat = MsgConfig.lat;
				lon = MsgConfig.lng;
				LatLng ll = new LatLng(lat, lon);
				MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
				MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(true);
				mBaiduMap.addOverlay(ops);
				reverseGeoCode1(my_location_tv, lat, lon);
			}else{

			}
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
					Log.e("locationPosition", "locationPosition2");
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

}
