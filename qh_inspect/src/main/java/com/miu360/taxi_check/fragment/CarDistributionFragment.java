package com.miu360.taxi_check.fragment;

import com.baidu.mapapi.map.TextureMapView;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.GetHead;
import com.miu30.common.data.MapPositionPreference;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.AlarmReason;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex1;
import com.miu360.taxi_check.ui.CheckEarlyWarningDetailInfoActivity;
import com.miu30.common.ui.SelectLocationActivity;
import com.miu360.taxi_check.util.SortUtil;
import com.miu360.taxi_check.util.UIUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
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
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class CarDistributionFragment extends BaseFragment implements OnClickListener, OnTouchListener {
	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;
	@ViewInject(R.id.my_location_tv)
	private TextView my_location_tv;
	@ViewInject(R.id.bmapView)
	private TextureMapView mMapView;
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
	@ViewInject(R.id.car_yujing_type)
	private TextView car_yujing_type;
	@ViewInject(R.id.zhifa_reason)
	private TextView zhifa_reason;
	@ViewInject(R.id.ll_yujing_detail_info)
	private LinearLayout ll_yujing_detail_info;
	@ViewInject(R.id.list_show)
	private ListView list_show;
	@ViewInject(R.id.one_show)
	private LinearLayout one_show;
	/*
	 * @ViewInject(R.id.lock) private LinearLayout lock;
	 */
	@ViewInject(R.id.image_yujing_above)
	private ImageButton image_yujing_above;
	@ViewInject(R.id.rg)
	private RadioGroup radioGroup;

	@ViewInject(R.id.add_reduce_map)
	private LinearLayout add_reduce_map;
	@ViewInject(R.id.aim_location_return)
	private ImageButton aim_location_return;
	@ViewInject(R.id.add_map)
	private ImageButton add_map;
	@ViewInject(R.id.reduce_map)
	private ImageButton reduce_map;
	@ViewInject(R.id.mine_position)
	private ImageView mine_position;
	@ViewInject(R.id.image_yujing_car)
	private ImageView image_yujing_car;
	@ViewInject(R.id.btn_lock)
	private Button btn_lock;
	/*
	 * @ViewInject(R.id.btn_cancel) private Button btn_cancel;
	 */
	@ViewInject(R.id.alarmReason)
	private TextView alarmReason;

	BaiduMap mBaiduMap;;

	ArrayList<VehiclePositionModex1> arrayList = new ArrayList<>();
	ArrayList<Marker> markerList = new ArrayList<>();
	private InfoWindow mInfoWindow;
	MapPositionPreference ppfer;
	Marker mCurrentMarker;
	// 这里都是代表，GPS原始坐标
	double lat = 0.0;
	double lon = 0.0;
	// 百度坐标点
	double bLat = 0.0;
	double bLon = 0.0;

	private PositionBroadCast broadcast;

	TextView name;
	TextView corpName;
	TextView speed;
	TextView color;
	TextView direction;
	LatLng des;
	VehiclePositionModex1 p;
	VehicleInfo iInfo;
	boolean ishide = false;
	ArrayList<AlarmReason> AlarmReasonList;
	ArrayList<String> YuJingList;
	ArrayList<String> ssList = new ArrayList<>();

	String[] Items = new String[1];
	String[] Items1 = new String[]{"1","2"};
	boolean isKeYi = false;
	YuJingPreference YuJing;
	String alarmR = "";
	// 对获取的数据进行排序
	Comparator<VehiclePositionModex1> comparator = new Comparator<VehiclePositionModex1>() {
		public int compare(VehiclePositionModex1 f1, VehiclePositionModex1 f2) {
			String s1 = DistanceUtil.getDistance(new LatLng(f1.getGSPDATA().getLat(), f1.getGSPDATA().getLon()),
					new LatLng(lat, lon)) + "";
			String s2 = DistanceUtil.getDistance(new LatLng(f2.getGSPDATA().getLat(), f2.getGSPDATA().getLon()),
					new LatLng(lat, lon)) + "";
			// 先排JCLB
			if (!s1.equals(s2)) {
				return s1.compareTo(s2);
			}
			return 0;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().registerReceiver(broadcast = new PositionBroadCast(),
				new IntentFilter(SelectLocationActivity.CHOSE_LOCATION));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_suspicious_vehicle_distribution, null);
		initView(root);
		return root;
	}

	View view;

	private void initView(View root) {
		ViewUtils.inject(this, root);
		// rl_head = (RelativeLayout)act.findViewById(R.id.title);
		query.setOnClickListener(this);
		header_ll.setOnClickListener(this);
		my_location_tv.setOnClickListener(this);
		add_map.setOnClickListener(this);
		reduce_map.setOnClickListener(this);
		list_show.setVisibility(View.GONE);
		ppfer = new MapPositionPreference(act);
		YuJing = new YuJingPreference(act);
		YuJingList = new ArrayList<>();
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.showZoomControls(false);
		mBaiduMap.setMaxAndMinZoomLevel(18, 11);
		// 定时获取是否已经定位
		startTimer();
		aim_location.setOnClickListener(this);
		// 设置地图的一些监听
		initMapListener();
		check_more_yujing.setOnClickListener(this);
		ll_yujing_detail_info.setVisibility(View.GONE);
		ll_yujing_detail_info.setOnTouchListener(this);
		image_yujing_above.setOnClickListener(this);
		adapter = new SimpleAdapter();
		list_show.setAdapter(adapter);
		list_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VehiclePositionModex1 info = (VehiclePositionModex1) parent.getItemAtPosition(position);
				Intent intent = new Intent(act, CheckEarlyWarningDetailInfoActivity.class);
				intent.putExtra("vehicleMap", info);
				intent.putStringArrayListExtra("YuJingList", YuJingList);
				startActivity(intent);
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_button_One) {
					range = "500";
					YuJing.setString("range", "500");
				} else if (checkedId == R.id.radio_button_Two) {
					range = "1000";
					YuJing.setString("range", "1000");
				} else if (checkedId == R.id.radio_button_Three) {
					range = "3000";
					YuJing.setString("range", "3000");
				}
			}
		});
	}

	private void initMapListener() {
		AlarmReasonList = new ArrayList<>();
		AsyncUtil.goAsync(new Callable<Result<List<AlarmReason>>>() {

			@Override
			public Result<List<AlarmReason>> call() throws Exception {
				return WeiZhanData.queryAlarmType();
			}
		}, new Callback<Result<List<AlarmReason>>>() {

			@Override
			public void onHandle(Result<List<AlarmReason>> result) {
				if (result.ok()) {
					if (result.getData() == null || "[]".equals(result.getData())) {
						return;
					}
					for(int i=0,len =result.getData().size();i<len;i++){
						if(result.getData().get(i).getRKSM()!=null){
							AlarmReasonList.add(result.getData().get(i));
						}
					}
					Items = new String[AlarmReasonList.size() + 1];
					if (AlarmReasonList.size() > 0) {
						for (int i = 0; i < AlarmReasonList.size(); i++) {
							YuJingList.add(AlarmReasonList.get(i).getRKSM());
						}
						AlarmReason ar = new AlarmReason();
						ar.setRKSM("全部");
						AlarmReasonList.add(0, ar);
						Log.e("AlarmReasonList", "res155" + AlarmReasonList);
						for (int i = 0; i < AlarmReasonList.size(); i++) {
							Items[i] = AlarmReasonList.get(i).getRKSM();
						}
					}
					if(!AlarmReasonList.isEmpty()){
						alarmReason.setText(AlarmReasonList.get(0).getRKSM());
					}
				} else {
					UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				if (mCurrentMarker == arg0) {
					mBaiduMap.hideInfoWindow();
					mCurrentMarker = null;
					return true;
				}
				for (int i = 0; i < markerList.size(); i++) {
					Marker marker = markerList.get(i);
					if (arg0 == marker) {
						view = LayoutInflater.from(act).inflate(R.layout.yujingcarbuwindow, null);
						view.setLayoutParams(new LayoutParams());
						name = (TextView) view.findViewById(R.id.zhifa_name);
						// address = (TextView)
						// view.findViewById(R.id.zhifa_address);
						corpName = (TextView) view.findViewById(R.id.zhifa_corpName);
						speed = (TextView) view.findViewById(R.id.zhifa_speed);
						color = (TextView) view.findViewById(R.id.zhifa_color);
						direction = (TextView) view.findViewById(R.id.zhifa_fangxiang);
						zhifa_reason = (TextView) view.findViewById(R.id.zhifa_reason);
						// type = (TextView) view.findViewById(R.id.zhifa_type);
						MarkerOptions ooA = null;
						Bundle b = marker.getExtraInfo();
						p = (VehiclePositionModex1) b.getSerializable("" + i);
						key = i;
						des = marker.getPosition();
						// reverseGeoCode(address, des);
						name.setText(p.getGSPDATA().getVname());
						speed.setText(p.getGSPDATA().getSpeed() + "km/h");

						double fx = p.getGSPDATA().getHead();
						if (p.getGSPDATA().getHead() < 0 || p.getGSPDATA().getHead() > 360) {
							fx = p.getGSPDATA().getHead() > 0 ? p.getGSPDATA().getHead() : 0;
							fx = fx > 360 ? fx % 360 : fx;
						}
						direction.setText(GetHead.getHeadingDesc(fx));
						String s = p.getRKSM();
						if (YuJingList != null && !YuJingList.isEmpty()) {
							for (int j = 0, len = YuJingList.size(); j < len; j++) {
								if (j == 0) {
									if (s.equals(YuJingList.get(j))) {
										image_yujing_car.setImageResource(R.drawable.yujing_ttaxi);
										car_yujing_type.setTextColor(android.graphics.Color.parseColor("#00A7D8"));
										break;
									}
								} else if (j == 1) {
									if (s.equals(YuJingList.get(j))) {
										image_yujing_car.setImageResource(R.drawable.yujing_ttaxi1);
										car_yujing_type.setTextColor(android.graphics.Color.parseColor("#89C100"));
										break;
									}
								} else if (j == 2) {
									if (s.equals(YuJingList.get(j))) {
										image_yujing_car.setImageResource(R.drawable.yujing_ttaxi2);
										car_yujing_type.setTextColor(android.graphics.Color.parseColor("#A90CFD"));
										break;
									}
								} else if (j == 3) {
									if (s.equals(YuJingList.get(j))) {
										image_yujing_car.setImageResource(R.drawable.yujing_ttaxi3);
										car_yujing_type.setTextColor(android.graphics.Color.parseColor("#F3A401"));
										break;
									}
								} else {
									if (s.equals(YuJingList.get(j))) {
										image_yujing_car.setImageResource(R.drawable.yujing_ttaxi4);
										car_yujing_type.setTextColor(android.graphics.Color.parseColor("#F54359"));
										break;
									}
								}
							}
						}
						car_yujing_type.setText(p.getRKSM());
						zhifa_reason.setText(p.getRKSM());
						car_number_yujing.setText(p.getGSPDATA().getVname());
						bestDistance = 0;
						bestDistance += DistanceUtil.getDistance(new LatLng(bLat, bLon),
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
										// type.setText(iInfo.getVehicleType());
										mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), des, -47,
												null);
										mBaiduMap.showInfoWindow(mInfoWindow);
									}
								} else {
									UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});
						mCurrentMarker = arg0;
					}
				}
				return true;
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
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
					if (add_reduce_map.getVisibility() == View.VISIBLE) {
						btn_lock.setVisibility(View.VISIBLE);
						lockClick(centerLatLng);
					}
				}
			}

			@Override
			public void onMapStatusChange(MapStatus status) {
			}
		});
		aim_location_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Position();
				btn_lock.setVisibility(View.GONE);
			}
		});
		alarmReason.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (1 == Items.length) {
					UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
					return;
				}
				Windows.singleChoice(act, "请选择预警原因", Items, new OnDialogItemClickListener() {
					@Override
					public void dialogItemClickListener(int position) {
						alarmReason.setText(Items[position]);
					}
				});
			}
		});

		final RelativeLayout rl = (RelativeLayout) (act).findViewById(R.id.title);
		final RadioGroup rg = (RadioGroup) act.findViewById(R.id.RG);

		/*
		 * final LinearLayout ly_yujing =
		 * (LinearLayout)act.findViewById(R.id.ly_yujing); final HeaderHolder
		 * header = ((WarningInspectActivity)act).header;
		 */
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if (header_ll.getVisibility() == View.VISIBLE) {
					header_ll.setVisibility(View.GONE);
					ll_yujing_detail_info.setVisibility(View.GONE);
					if (rl != null) {
						rg.setVisibility(View.GONE);
					}
					/*
					 * if(ly_yujing != null){
					 * ly_yujing.setVisibility(View.GONE); }
					 */
				} else if (header_ll.getVisibility() == View.GONE) {
					header_ll.setVisibility(View.VISIBLE);
					if (isKeYi) {
						ll_yujing_detail_info.setVisibility(View.VISIBLE);
					}
					if (rl != null) {
						rg.setVisibility(View.VISIBLE);
					}
					/*
					 * if(ly_yujing != null){
					 * ly_yujing.setVisibility(View.VISIBLE); }
					 */
				}
			}
		});
	}

	public void lockClick(final LatLng centerLatLng) {
		btn_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgConfig.select_lat = centerLatLng.latitude;
				MsgConfig.select_lng = centerLatLng.longitude;
				bLat = MsgConfig.select_lat;
				bLon = MsgConfig.select_lng;

				LatLng AdjustLatLng = convertBaiduToGPS(new LatLng(bLat, bLon));
				lat = AdjustLatLng.latitude;
				lon = AdjustLatLng.longitude;
				reverseGeoCode1(my_location_tv, bLat, bLon, true);
				btn_lock.setVisibility(View.GONE);
			}
		});

	}

	String range = "1000";
	SimpleAdapter adapter;
	int bestDistance = 0;
	// BitmapDescriptor ba =
	// BitmapDescriptorFactory.fromResource(R.drawable.mine_renyuan);

	private class ViewHolder {
		TextView car_yujing_distance;
		TextView car_number_yujing;
		TextView car_yujing_type;
		ImageView image_yujing_car;
	}

	String s;

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
				holder.car_yujing_type = (TextView) convertView.findViewById(R.id.car_yujing_type);
				holder.image_yujing_car = (ImageView) convertView.findViewById(R.id.image_yujing_car);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LatLng des = null;
			LatLng des1 = null;
			VehiclePositionModex1 item = (VehiclePositionModex1) getItem(position);
			s = item.getRKSM();
			if (!YuJingList.isEmpty() && YuJingList != null) {
				for (int j = 0, len = YuJingList.size(); j < len; j++) {
					if (j == 0) {
						if (s.equals(YuJingList.get(j))) {
							holder.car_yujing_type.setTextColor(android.graphics.Color.parseColor("#00A7D8"));
							holder.image_yujing_car.setImageResource(R.drawable.yujing_ttaxi);
							break;
						}
					} else if (j == 1) {
						if (s.equals(YuJingList.get(j))) {
							holder.car_yujing_type.setTextColor(android.graphics.Color.parseColor("#89C100"));
							holder.image_yujing_car.setImageResource(R.drawable.yujing_ttaxi1);
							break;
						}
					} else if (j == 2) {
						if (s.equals(YuJingList.get(j))) {
							holder.car_yujing_type.setTextColor(android.graphics.Color.parseColor("#A90CFD"));
							holder.image_yujing_car.setImageResource(R.drawable.yujing_ttaxi2);
							break;
						}
					} else if (j == 3) {
						if (s.equals(YuJingList.get(j))) {
							holder.car_yujing_type.setTextColor(android.graphics.Color.parseColor("#F3A401"));
							holder.image_yujing_car.setImageResource(R.drawable.yujing_ttaxi3);
							break;
						}
					} else {
						if (s.equals(YuJingList.get(j))) {
							holder.car_yujing_type.setTextColor(android.graphics.Color.parseColor("#F54359"));
							holder.image_yujing_car.setImageResource(R.drawable.yujing_ttaxi4);
							break;
						}
					}
				}
			}

/*
 * 这里就是出现乱码的地方
 */

			holder.car_number_yujing.setText(item.getGSPDATA().getVname());
			LatLng ll = new LatLng(item.getGSPDATA().getLat(), item.getGSPDATA().getLon());

			CoordinateConverter cover = new CoordinateConverter();
			cover.from(CoordType.GPS);
			cover.coord(ll);
//			des是获取到的预警车辆位置坐标
			des = cover.convert();
//			des1是执法人员定位的坐标
			des1 = new LatLng(bLat, bLon);
			bestDistance = 0;
			bestDistance += DistanceUtil.getDistance(des1, des);
//			这里是显示距离的地方
			holder.car_yujing_distance.setText(showDistance(bestDistance));
			holder.car_yujing_type.setText(item.getRKSM());
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

	Handler mHandler;
	Runnable mRunnable;

	private void startTimer() {
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					if (lat == 0) {
						Position();
					} else {
						mHandler.removeCallbacks(mRunnable);
					}
				}
			}
		};
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (lat == 0) {
					mHandler.sendEmptyMessage(1);
					mHandler.postDelayed(mRunnable, 5 * 1000);
				}
			}
		};
		new Thread(mRunnable).start();
	}

	/**
	 * 重定位广播
	 */
	class PositionBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ppfer.setString("selectPosition", intent.getStringExtra("shoudong_location"));
			rePosition();
		}

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
			my_location_tv.setText(ppfer.getString("selectPosition", ""));
			ll_yujing_detail_info.setVisibility(View.GONE);
			arrayList.clear();
			markerList.clear();
			mBaiduMap.clear();
			MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			mine_position.setVisibility(View.VISIBLE);
		} else {
			if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
				bLat = MsgConfig.lat;
				bLon = MsgConfig.lng;
				LatLng CorrectLatLng = new LatLng(MsgConfig.lat, MsgConfig.lng);
				LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
				lat = AdjustLatLng.latitude;
				lon = AdjustLatLng.longitude;

				if (!"".equals(ppfer.getString("selectPosition", ""))) {
					my_location_tv.setText(ppfer.getString("selectPosition", ""));
				} else {
					reverseGeoCode1(my_location_tv, MsgConfig.lat, MsgConfig.lng, false);
				}
				ll_yujing_detail_info.setVisibility(View.GONE);
				arrayList.clear();
				markerList.clear();
				mBaiduMap.clear();
				MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
				mine_position.setVisibility(View.VISIBLE);
			} else {
			}
		}
	}

	/**
	 * 定位SDK监听函数
	 *//*
		 * public class MyLocationListenner implements BDLocationListener {
		 *
		 * @Override public void onReceiveLocation(BDLocation location) { // map
		 * view 销毁后不在处理新接收的位置 if (location == null || mMapView == null) {
		 * return; } // Log.e("location", //
		 * "location"+MsgConfig.lat+"---"+"MsgConfig.lng"); if
		 * (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) || MsgConfig.lat
		 * == 0.0) { lat = 39.944206; lon = 116.359777; } else { lat =
		 * MsgConfig.lat; lon = MsgConfig.lng; } if (MsgConfig.select_lng != 0
		 * && MsgConfig.select_lat != 0) { lat = MsgConfig.select_lat; lon =
		 * MsgConfig.select_lng; } // isDethData = false;
		 * AddressResolution(location);
		 *
		 * }
		 *
		 * public void onReceivePoi(BDLocation poiLocation) { } }
		 */

	/*
	 * private void AddressResolution(BDLocation location) { LatLng ll = new
	 * LatLng(lat, lon); CoordinateConverter converter = new
	 * CoordinateConverter(); converter.from(CoordType.GPS);
	 * converter.coord(ll); LatLng desLatLng = converter.convert(); Log.e("坐标",
	 * "ll" + ll.toString()); Log.e("坐标", "ll" + desLatLng.toString()); if
	 * (location != null) { MyLocationData locData = new
	 * MyLocationData.Builder().accuracy(location.getRadius()) //
	 * 此处设置开发者获取到的方向信息，顺时针0-360
	 * .direction(100).latitude(desLatLng.latitude).longitude(desLatLng.
	 * longitude).build();
	 *
	 * mBaiduMap.setMyLocationData(locData); mLocClient.stop(); } if (ll !=
	 * null) { MapStatus mapStatus = new
	 * MapStatus.Builder().target(ll).zoom(16).build();
	 * mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
	 * reverseGeoCode1(my_location_tv, lat, lon); // UIUtils.toast(act,
	 * ll.toString(), Toast.LENGTH_SHORT); } }
	 */

	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi);
	BitmapDescriptor bd1 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi1);
	BitmapDescriptor bd2 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi2);
	BitmapDescriptor bd3 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi3);
	BitmapDescriptor bd4 = BitmapDescriptorFactory.fromResource(R.drawable.yujing_ttaxi4);

	@Override
	public void onClick(View v) {
		if (v == aim_location) {
			Intent intent = new Intent(act, SelectLocationActivity.class);
			intent.putExtra("thisAdd", my_location_tv.getText().toString());
			startActivity(intent);
		} else if (v == add_map) {
			if (mBaiduMap != null) {
				MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
				mBaiduMap.setMapStatus(zoomIn);
			}
		} else if (v == reduce_map) {
			if (mBaiduMap != null) {
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
			isKeYi = false;

			if ("全部".equals(alarmReason.getText().toString())) {
				alarmR = "all";
			} else {
				alarmR = alarmReason.getText().toString();
			}
			AsyncUtil.goAsync(new Callable<Result<List<VehiclePositionModex1>>>() {

				@Override
				public Result<List<VehiclePositionModex1>> call() throws Exception {
					return WeiZhanData.queryKeYiPositionInfo2("" + lon, "" + lat, range, alarmR);
				}
			}, new Callback<Result<List<VehiclePositionModex1>>>() {

				@Override
				public void onHandle(Result<List<VehiclePositionModex1>> result) {
					d.dismiss();
					LatLng des1 = null;
					des1 = new LatLng(bLat, bLon);
					if (result.ok()) {
						Log.e("result", "result:"+result.getData().size());
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(act, "未发现可疑车辆", Toast.LENGTH_LONG);
							return;
						}
						arrayList.addAll(result.getData());
						ll_yujing_detail_info.setVisibility(View.VISIBLE);
						if (arrayList.size() == 0) {
							UIUtils.toast(act, "未发现可疑车辆", Toast.LENGTH_SHORT);
							return;
						}
						isKeYi = true;
						LatLng startdes = null;
						listMap = new HashMap<>();
						SortUtil.sortListNyDistance(bLat, bLon, arrayList);
						String s;
						for (int i = 0; i < arrayList.size(); i++) {
							LatLng ll = new LatLng(arrayList.get(i).getGSPDATA().getLat(),
									arrayList.get(i).getGSPDATA().getLon());
							CoordinateConverter converter = new CoordinateConverter();
							converter.from(CoordType.GPS);
							converter.coord(ll);
							LatLng desLatLng = converter.convert();
							if (i == 0) {
								startdes = desLatLng;
							}
							s = arrayList.get(i).getRKSM();
							MarkerOptions op = new MarkerOptions().position(desLatLng).icon(bd).zIndex(9)
									.draggable(false);
							if (YuJingList != null && !YuJingList.isEmpty()) {
								for (int j = 0, len = YuJingList.size(); j < len; j++) {
									if (j == 0) {
										if (s.equals(YuJingList.get(j))) {
											op = new MarkerOptions().position(desLatLng).icon(bd).zIndex(9)
													.draggable(false);
											if (i == 0) {
												image_yujing_car.setImageResource(R.drawable.yujing_ttaxi);
												car_yujing_type
														.setTextColor(android.graphics.Color.parseColor("#00A7D8"));
											}
											break;
										}
									} else if (j == 1) {
										if (s.equals(YuJingList.get(j))) {
											op = new MarkerOptions().position(desLatLng).icon(bd1).zIndex(9)
													.draggable(false);
											if (i == 0) {
												image_yujing_car.setImageResource(R.drawable.yujing_ttaxi1);
												car_yujing_type
														.setTextColor(android.graphics.Color.parseColor("#89C100"));
											}
											break;
										}
									} else if (j == 2) {
										if (s.equals(YuJingList.get(j))) {
											op = new MarkerOptions().position(desLatLng).icon(bd2).zIndex(9)
													.draggable(false);
											if (i == 0) {
												image_yujing_car.setImageResource(R.drawable.yujing_ttaxi2);
												car_yujing_type
														.setTextColor(android.graphics.Color.parseColor("#A90CFD"));
											}
											break;
										}
									} else if (j == 3) {
										if (s.equals(YuJingList.get(j))) {
											op = new MarkerOptions().position(desLatLng).icon(bd3).zIndex(9)
													.draggable(false);
											if (i == 0) {
												image_yujing_car.setImageResource(R.drawable.yujing_ttaxi3);
												car_yujing_type
														.setTextColor(android.graphics.Color.parseColor("#F3A401"));
											}
											break;
										}
									} else {
										if (s.equals(YuJingList.get(j))) {
											op = new MarkerOptions().position(desLatLng).icon(bd4).zIndex(9)
													.draggable(false);
											if (i == 0) {
												image_yujing_car.setImageResource(R.drawable.yujing_ttaxi4);
												car_yujing_type
														.setTextColor(android.graphics.Color.parseColor("#F54359"));
											}
											break;
										}
									}
								}
							}
							Marker marker = (Marker) mBaiduMap.addOverlay(op);
							Bundle b = new Bundle();
							key = markerList.size();
							b.putSerializable("" + markerList.size(), arrayList.get(i));
							marker.setExtraInfo(b);
							markerList.add(marker);
							listMap.put("" + key, arrayList.get(i));
						}
						key = 0;
						bestDistance = 0;
						car_number_yujing.setText(arrayList.get(0).getGSPDATA().getVname());
						bestDistance += DistanceUtil.getDistance(des1, startdes);
						car_yujing_distance.setText(showDistance(bestDistance));
						car_yujing_type.setText(arrayList.get(0).getRKSM());
					} else {
						if (range.equals("3000") && result.getErrorMsg().equals("系统错误")) {
							UIUtils.toast(act, "查询车辆过多，请选择小范围预警", Toast.LENGTH_SHORT);
						}
					}
				}
			});
		} else if (v == check_more_yujing) {
			Intent intent = new Intent(act, CheckEarlyWarningDetailInfoActivity.class);
			intent.putExtra("vehicleMap", listMap.get(key + ""));
			intent.putStringArrayListExtra("YuJingList", YuJingList);
			startActivity(intent);
		} else if (v == image_yujing_above) {
			if (list_show.getVisibility() == View.GONE) {
				header_ll.setVisibility(View.GONE);
				one_show.setVisibility(View.GONE);
				mine_position.setVisibility(View.GONE);
				list_show.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				add_reduce_map.setVisibility(View.GONE);
				aim_location_return.setVisibility(View.GONE);
				btn_lock.setVisibility(View.GONE);
				image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangxiazhankai));
			} else {
				header_ll.setVisibility(View.VISIBLE);
				one_show.setVisibility(View.VISIBLE);
				mine_position.setVisibility(View.VISIBLE);
				list_show.setVisibility(View.GONE);
				add_reduce_map.setVisibility(View.VISIBLE);
				aim_location_return.setVisibility(View.VISIBLE);
				image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangshangzhankai));
			}
		} else if (v == my_location_tv) {
			Intent intent = new Intent(act, SelectLocationActivity.class);
			intent.putExtra("thisAdd", my_location_tv.getText().toString());
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
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		if (broadcast != null) {
			getActivity().unregisterReceiver(broadcast);
		}
		if (mHandler != null && mRunnable != null) {
			mHandler.removeCallbacks(mRunnable);
		}
		super.onDestroy();
	}

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
				mine_position.setVisibility(View.VISIBLE);
				my_location_tv.setText(ppfer.getString("selectPosition", ""));// &&
			} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
				LatLng CorrectLatLng = new LatLng(MsgConfig.lat, MsgConfig.lng);
				LatLng AdjustLatLng = convertBaiduToGPS(CorrectLatLng);
				bLat = MsgConfig.lat;
				bLon = MsgConfig.lng;
				lat = AdjustLatLng.latitude;
				lon = AdjustLatLng.longitude;

				MapStatus mapStatus = new MapStatus.Builder().target(CorrectLatLng).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
				mine_position.setVisibility(View.VISIBLE);
				if (!"".equals(ppfer.getString("selectPosition", ""))) {
					my_location_tv.setText(ppfer.getString("selectPosition", ""));
				} else {
					reverseGeoCode1(my_location_tv, MsgConfig.lat, MsgConfig.lng, false);
				}
			} else {

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
	 * 百度坐标转GPS坐标
	 *
	 * @param sourceLatLng
	 * @return
	 */
	public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
		// 将GPS设备采集的原始GPS坐标转换成百度坐标
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
		double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
		BigDecimal bdLatitude = new BigDecimal(latitude);
		bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
		BigDecimal bdLongitude = new BigDecimal(longitude);
		bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
		return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode1(final TextView tv, final double lat, final double lng, final boolean isLock) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return HistoryData.queryHistoryTrack1(lat, lng);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					Log.e("locationPosition", "locationPosition2");
					tv.setText(result.getData());
					if (isLock) {
						ppfer.setString("selectPosition", result.getData());
					}
				}
			}
		});
	}

	private int _lastX;// 点击x坐标
	private int _lastY;// 点击y坐标
	private int _move;// 移动记录，0，不移动，1，上移，2，下移

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		View pressButton = v.findViewById(R.id.image_yujing_above);
		if (pressButton != null) {
			int left = pressButton.getLeft();
			int right = pressButton.getRight();
			int top = pressButton.getTop();
			int bottow = pressButton.getBottom();
			if (event.getRawX() > left && event.getRawX() < right && event.getRawY() > top
					&& event.getRawY() < bottow) {
				return false;
			}
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				_lastX = (int) event.getRawX();
				_lastY = (int) event.getRawY();
				_move = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(_lastX - event.getRawX()) < 30 && Math.abs(_lastY - event.getRawY()) > 10) {
					if (_lastY - event.getRawY() < 0) {
						_move = 2;
					} else {
						_move = 1;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (_move == 1) {
					header_ll.setVisibility(View.GONE);
					one_show.setVisibility(View.GONE);
					mine_position.setVisibility(View.GONE);
					list_show.setVisibility(View.VISIBLE);
					add_reduce_map.setVisibility(View.GONE);
					aim_location_return.setVisibility(View.GONE);
					btn_lock.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangxiazhankai));
				} else if (_move == 2) {
					header_ll.setVisibility(View.VISIBLE);
					one_show.setVisibility(View.VISIBLE);
					list_show.setVisibility(View.GONE);
					mine_position.setVisibility(View.VISIBLE);
					add_reduce_map.setVisibility(View.VISIBLE);
					aim_location_return.setVisibility(View.VISIBLE);
					image_yujing_above.setBackground(getResources().getDrawable(R.drawable.xiangshangzhankai));
				}
				v.performClick();
				break;
			case MotionEvent.ACTION_CANCEL:
				_move = 0;
				break;
		}
		return true;
	}

}
