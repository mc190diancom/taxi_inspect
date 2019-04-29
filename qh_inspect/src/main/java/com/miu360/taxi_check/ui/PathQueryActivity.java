package com.miu360.taxi_check.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.VehicleStatus;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.map.DrivingRouteOverlay;
import com.miu360.taxi_check.map.MapUtils;
import com.miu360.taxi_check.model.LatLngDir;
import com.miu360.taxi_check.model.LuWangOrder;
import com.miu360.taxi_check.model.PathLuWangQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class PathQueryActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_start_time)
	private TextView tv_start_time;
	@ViewInject(R.id.tv_end_time)
	private TextView tv_end_time;
	@ViewInject(R.id.tv_end)
	private TextView tv_end;
	@ViewInject(R.id.tv_better)
	private TextView tv_better;
	@ViewInject(R.id.tv_path)
	private TextView tv_path;
	@ViewInject(R.id.tv_different)
	private TextView tv_different;
	@ViewInject(R.id.ll_show)
	private LinearLayout ll_show;
	@ViewInject(R.id.ll_hide)
	private LinearLayout ll_hide;
	@ViewInject(R.id.ll_show_main)
	private LinearLayout ll_show_main;
	@ViewInject(R.id.tv_carid)
	private TextView tv_caridTv;
	@ViewInject(R.id.chekBox_One)
	private CheckBox chekBox_One;
	@ViewInject(R.id.chekBox_Two)
	private CheckBox chekBox_Two;
	@ViewInject(R.id.ll_lw_Show)
	private ImageButton ll_lw_Show;
	@ViewInject(R.id.ll_lw_hide)
	private LinearLayout ll_lw_hide;
	@ViewInject(R.id.toggle_One)
	private ToggleButton toggle_One;
	@ViewInject(R.id.toggle_Two)
	private ToggleButton toggle_Two;

	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;
	@ViewInject(R.id.add_map)
	private ImageButton add_map;
	@ViewInject(R.id.reduce_map)
	private ImageButton reduce_map;

	MapUtils map;
	RoutePlanSearch mSearch;
	List<LatLng> mPoints = new ArrayList<>();
	List<LatLng> bPoints = new ArrayList<>();
	BaiduMap mBaiduMap;
	LatLng start, end;
	int PathDistance = 0;
	int bestDistance = 0;
	String PathDis;
	String BestDis;

	private String vname;
	private long startTime;
	private long endTime;
	HeaderHolder holder;
	Overlay overlay;
	int current = 0;
	int currentCount = current;
	private boolean currentIsChecked = true;
	private boolean isLuWang = false;// 是否开启路网匹配
	String car_Vname;

	private final String datePatterShow = "yyyy-MM-dd HH:mm";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		// SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_path_query);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		Log.e("PathQueryActivity", "从新进入");
		vname = intent.getStringExtra("vname");
		startTime = intent.getLongExtra("startTime", 0);
		endTime = intent.getLongExtra("endTime", 0);
		if (TextUtils.isEmpty(vname)) {
			Toast.makeText(this, "车牌不正确", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		toggle_One.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				currentIsChecked = isChecked;
				drawPath();
			}
		});
		toggle_Two.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isLuWang = isChecked;
				drawPath();
			}
		});
		ll_lw_Show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ll_lw_hide.getVisibility() == View.VISIBLE) {
					ll_lw_hide.setVisibility(View.GONE);
					ll_lw_Show.setImageResource(R.drawable.dianji_show);
				} else {
					ll_lw_hide.setVisibility(View.VISIBLE);
					ll_lw_Show.setImageResource(R.drawable.dianji_hide);
				}
			}
		});
		chekBox_One.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				drawPath();
			}
		});

		aim_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isLoctionOk) {
					Windows.confirm(self, "GPS未开启是否开启？", new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PathQueryActivity.this, SelectLocationActivity.class);
							startActivityForResult(intent, 0);
						}
					});
				} else {
					Position();
				}

			}
		});
		String s = new SimpleDateFormat(datePatterShow).format(new Date(startTime));
		String s2 = new SimpleDateFormat(datePatterShow).format(new Date(endTime));

		car_Vname = "京" + vname;

		tv_caridTv.setText(car_Vname);
		holder = new HeaderHolder();
		holder.init(self, "轨迹查询");
		holder.rightTextBtn.setText("违法违章查询");
		// 修改点
		holder.rightTextBtn.setVisibility(View.GONE);
		holder.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(self, WeiZhangQueryActivity.class);
				intent.putExtra("car_Vname", car_Vname);
				startActivity(intent);
			}
		});
		holder.leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ll_show_main.setOnClickListener(this);
		ll_show.setOnClickListener(this);
		ll_hide.setOnClickListener(this);
		add_map.setOnClickListener(this);
		reduce_map.setOnClickListener(this);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		map = new MapUtils();
		map.initMap(this, mMapView, mBaiduMap);
		drawPath();// 查询历史轨迹并绘制
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if (ll_show_main.getVisibility() == View.VISIBLE) {
					ll_show_main.setVisibility(View.GONE);
				} else if (ll_show_main.getVisibility() == View.GONE) {
					ll_show_main.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 获取驾车路线推荐
	 */

	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
	}

	ArrayList<VehicleStatus> vehicArrayList = new ArrayList<>();
	ArrayList<VehicleStatus> vehicArrayLists = new ArrayList<>();
	ArrayList<LatLng> zhongArrayList = new ArrayList<>();
	ArrayList<LuWangOrder> luWangOrderArrayList = new ArrayList<>();// 存储返回的路网点加标志
	ArrayList<LatLng> luWangArrayList = new ArrayList<>();// 存储返回的路网点
	ArrayList<ArrayList<LatLng>> allArrayList = new ArrayList<>();
	PathLuWangQ lw;
	int x;// 存储执行到的集合位置

	private void drawPath() {
		mBaiduMap.clear();
		final MyProgressDialog d = Windows.waiting(self);
		d.setCancelable(true);
		PathDistance = 0;
		bestDistance = 0;
		AsyncUtil.goAsync(new Callable<Result<List<VehicleStatus>>>() {

			@Override
			public Result<List<VehicleStatus>> call() throws Exception {
				return HistoryData.queryHistoryTrack2(vname,
						getIntent().getLongExtra("startTime", System.currentTimeMillis() - 3600 * 1000),
						getIntent().getLongExtra("endTime", System.currentTimeMillis()));
			}
		}, new Callback<Result<List<VehicleStatus>>>() {

			@Override
			public void onHandle(Result<List<VehicleStatus>> result) {
				d.dismiss();
				if (result.ok()) {
					if (!result.getData().isEmpty()) {
						clearList();//每次选择模式时，清理下集合数据
						Log.e("path","path:"+result.getData());
						vehicArrayLists.addAll(result.getData());

						CoordinateConverter converter = new CoordinateConverter();
						converter.from(CoordType.GPS);
						for (VehicleStatus point : vehicArrayLists) {
							ConvertGPS(converter, point);//转换坐标系
						}

						if (isLuWang) {
							luWang();//是否进行路网匹配，这里的数据还有问题
						}
						int state = 0;
						List<List<LatLng>> allRoute = new ArrayList<>();
						List<LatLng> kongArrayList = new ArrayList<>();
						List<LatLng> route = new ArrayList<>();
						List<Integer> colors = new ArrayList<>();
						List<Short> dirInt = new ArrayList<>();
						List<Short> directionInt = new ArrayList<>();
						List<LatLng> dirLat = new ArrayList<>();
						List<Long> dirTime = new ArrayList<>();

						for (int i = 0, lens = vehicArrayList.size() - 1; i < lens; i++) {
							Short direct = (short) vehicArrayList.get(i + 1).AZIMUTHS;
							if (!dirInt.isEmpty()) {
								if (direct != dirInt.get(dirInt.size() - 1)) {
									LatLng latLng = new LatLng(vehicArrayList.get(i).getLat(),
											vehicArrayList.get(i).getLon());
									dirLat.add(latLng);
									dirInt.add(direct);
									dirTime.add(vehicArrayList.get(i).GPS_TIME);
								}
							} else {
								LatLng latLng = new LatLng(vehicArrayList.get(i).getLat(),
										vehicArrayList.get(i).getLon());
								dirLat.add(latLng);
								dirInt.add(direct);
								dirTime.add(vehicArrayList.get(i).GPS_TIME);
							}
						}

						Log.e("point", "point:" + dirInt.size() + "");
						for (int i = 0; i < dirInt.size(); i++) {
							if (!kongArrayList.isEmpty()) {
								if (Math.abs(DistanceUtil.getDistance(dirLat.get(i),
										kongArrayList.get(kongArrayList.size() - 1))) > 800) {
									directionInt.add(dirInt.get(i));
									kongArrayList.add(dirLat.get(i));
								}
							} else {
								directionInt.add(dirInt.get(i));
								kongArrayList.add(dirLat.get(i));
							}
						}

						for (int i = 0; i < vehicArrayList.size(); i++) {
							VehicleStatus s = vehicArrayList.get(i);
							if (s.getVEHICLE_STATUS() != state) {
								colors.add(state);
								allRoute.add(route);
								route = new ArrayList<>();
								state = s.getVEHICLE_STATUS();
							}

							LatLng ll = new LatLng(vehicArrayList.get(i).getLat(), vehicArrayList.get(i).getLon());
							route.add(ll);

							if (i == vehicArrayList.size() - 1 && s.getVEHICLE_STATUS() == state) {
								allRoute.add(route);
								colors.add(state);
							}
						}

						start = new LatLng(vehicArrayList.get(0).getLat(), vehicArrayList.get(0).getLon());

						end = new LatLng(vehicArrayList.get(vehicArrayList.size() - 1).getLat(),
								vehicArrayList.get(vehicArrayList.size() - 1).getLon());
						reverseGeoCode(tv_start, start);
						reverseGeoCode(tv_end, end);

						tv_start_time.setText(new SimpleDateFormat(datePatterShow)
								.format(new Date(vehicArrayList.get(0).getGPS_TIME())));
						tv_end_time.setText(new SimpleDateFormat(datePatterShow)
								.format(new Date(vehicArrayList.get(vehicArrayList.size() - 1).getGPS_TIME())));
						// 设置起点图片
						MarkerOptions placeHolder = new MarkerOptions().position(start)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_qidian_huan));
						mBaiduMap.addOverlay(placeHolder);
						// 设置终点图片
						MarkerOptions placeHolder1 = new MarkerOptions().position(end)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_zhongdian_huan));
						mBaiduMap.addOverlay(placeHolder1);
						// 计算距离
						for (int i = 0; i < vehicArrayList.size() - 1; i++) {
							PathDistance += DistanceUtil.getDistance(
									new LatLng(vehicArrayList.get(i).getLat(), vehicArrayList.get(i).getLon()),
									new LatLng(vehicArrayList.get(i + 1).getLat(), vehicArrayList.get(i + 1).getLon()));
						}

						ArrayList<Overlay> overArrayList = new ArrayList<>();
						tv_path.setText("行驶轨迹 " + showDistance(PathDistance));
						for (int i = 0; i < allRoute.size(); i++) {
							List<LatLng> arrayListKZ = new ArrayList<>();
							arrayListKZ = allRoute.get(i);
							int color = 0;
							if (colors.get(i) == 1) {
								color = getResources().getColor(R.color.zhong_lujin_color);
							} else {
								color = getResources().getColor(R.color.kong_lujin_color);
							}
							if (i < allRoute.size() - 1) {
								LatLng next = allRoute.get(i + 1).get(0);
								arrayListKZ.add(next);
							}
							if (arrayListKZ.size() < 2) {
								System.out.println(" less than 2");
								continue;
							}
							Overlay line2 = map.drawLine2(arrayListKZ, color, 10, 10);
							overArrayList.add(line2);
						}
						map.drawPoint(kongArrayList, directionInt, 10);
						ArrayList<Overlay> os = new ArrayList<>();
						for (int i = 0; i < overArrayList.size(); i++) {
							Overlay line = overArrayList.get(i);
							os.add(line);
						}
						map.zoomToSpan(mBaiduMap, os);
						// bestDrivePath();
						converter.coord(new LatLng(result.getData().get(0).getLat(), result.getData().get(0).getLon()));
						LatLng startLatLng = converter.convert();
						converter.coord(new LatLng(result.getData().get(result.getData().size() - 1).getLat(),
								result.getData().get(result.getData().size() - 1).getLon()));
						LatLng endLatLng = converter.convert();
						drawBest2(startLatLng.latitude, startLatLng.longitude, endLatLng.latitude, endLatLng.longitude);
					} else {
						UIUtils.toast(PathQueryActivity.this, "无数据", Toast.LENGTH_SHORT);
						finish();
					}
				} else {
					UIUtils.toast(PathQueryActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}

		});
	}

	/**
	 * TODO 把获取的原始GPS坐标转百度坐标
	 */
	private void ConvertGPS(CoordinateConverter converter, VehicleStatus point) {
		VehicleStatus vehicle = new VehicleStatus();
		double lat = point.getLat();
		double lon = point.getLon();
		LatLng sourceLatLng = new LatLng(lat, lon);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		vehicle.setLat(desLatLng.latitude);
		vehicle.setLon(desLatLng.longitude);
		vehicle.setVEHICLE_STATUS(point.getVEHICLE_STATUS());
		vehicle.GPS_TIME = point.getGPS_TIME();
		vehicle.AZIMUTHS = point.getAZIMUTHS();
		vehicArrayList.add(vehicle);
	}

	/**
	 * TODO 每次选择模式，清理下点集合的数据
	 */
	private void clearList() {
		x = 0;
		luWangOrderArrayList.clear();
		luWangArrayList.clear();
		vehicArrayList.clear();
		vehicArrayLists.clear();
	}

	private void luWang() {
		for (int i = 0, lens = vehicArrayLists.size(); i < lens; i++) {
			lw = new PathLuWangQ();
			lw.setName(vname);
			lw.setLat84(vehicArrayLists.get(i).getLat() + "");
			lw.setLon84(vehicArrayLists.get(i).getLon() + "");
			lw.setGpsUtc((vehicArrayLists.get(i).getGPS_TIME()) / 1000 + "");
			final int flag = i;
			AsyncUtil.goAsync(new Callable<Result<String>>() {

				@Override
				public Result<String> call() throws Exception {
					return HistoryData.queryLuWang(lw, flag);
				}
			}, new Callback<Result<String>>() {

				@Override
				public void onHandle(Result<String> result) {
					if (result.ok()) {
						x++;
						if (result.getData().contains(".")) {
							String[] s = result.getData().split("\\|");
							LuWangOrder luWangOrder = new LuWangOrder();
							luWangOrder.setLat(Double.parseDouble(s[1]));
							luWangOrder.setLon(Double.parseDouble(s[0]));
							luWangOrder.setOrder(Integer.parseInt(s[2]));
							luWangOrderArrayList.add(luWangOrder);
							if (x == (vehicArrayLists.size() - 1)) {
								Collections.sort(luWangOrderArrayList,
										new Comparator<LuWangOrder>() {
											public int compare(LuWangOrder o1, LuWangOrder o2) {
												// 按照flag进行升序排列
												if (o1.getOrder() > o2.getOrder()) {
													return 1;
												}
												if (o1.getOrder() == o2.getOrder()) {
													return 0;
												}
												return -1;
											}
										});
								Log.e("isLuWang", "isLuWang:" + luWangOrderArrayList);
								for (int i = 0, len = luWangOrderArrayList.size(); i < len; i++) {
									double lat = luWangOrderArrayList.get(i).getLat();
									double lon = luWangOrderArrayList.get(i).getLon();
									luWangArrayList.add(new LatLng(lat, lon));
								}
								drawBest3(luWangArrayList);
							}
						} else {

						}
					} else {

					}
				}
			});
		}
	}

	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.guiji_luwang);
	ArrayList<Overlay> overArrayList = new ArrayList<>();
	ArrayList<LatLng> lWArrayList;

	/**
	 * 画路线
	 *
	 * @param latlngs
	 */
	private void drawBest3(ArrayList<LatLng> latlngs) {
		/*
		 * Overlay line = map.drawLine2(latlngs,
		 * getResources().getColor(R.color.red), 10, 10);
		 * overArrayList.add(line);
		 */
		for (int i = 0, len = latlngs.size(); i < len; i++) {
			MarkerOptions op = new MarkerOptions().position(latlngs.get(i)).icon(bd).zIndex(9).draggable(false);
			Marker marker = (Marker) mBaiduMap.addOverlay(op);
			/*
			 * lWArrayList = new ArrayList<>(); lWArrayList.add(latlngs.get(i));
			 * lWArrayList.add(latlngs.get(i+1)); Overlay line =
			 * map.drawLine2(lWArrayList, getResources().getColor(R.color.red),
			 * 5, 10); overArrayList.add(line);
			 */
		}
		/*
		 * ArrayList<Overlay> os = new ArrayList<>(); os.addAll(overArrayList);
		 * map.zoomToSpan(mBaiduMap, os);
		 */
		/*
		 * if (os.size() > 1) { map.zoomToSpan(mBaiduMap, os); }
		 */
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

	ArrayList<LatLngDir> lPoints = new ArrayList<>();

	private void drawBest2(final double start_lat, final double start_lng, final double end_lat, final double end_lng) {
		AsyncUtil.goAsync(new Callable<Result<List<LatLngDir>>>() {

			@Override
			public Result<List<LatLngDir>> call() throws Exception {
				return HistoryData.queryBestTrack2(start_lat, start_lng, end_lat, end_lng);
			}
		}, new Callback<Result<List<LatLngDir>>>() {

			@Override
			public void onHandle(Result<List<LatLngDir>> result) {
				if (result.ok()) {
					if (!result.getData().isEmpty()) {

						lPoints.clear();
						lPoints.addAll(result.getData());

						// int bestDistance = (int)
						// (result.getRouteLines().get(0).getDistance() /
						// 1000f);

						result.getData().add(0, new LatLngDir(-1, start_lat, start_lng));
						result.getData().add(new LatLngDir(-1, end_lat, end_lng));

						for (int i = 0; i < lPoints.size() - 1; i++) {
							// bestDistance +=
							// DistanceUtil.getDistance(bPoints.get(i),
							// bPoints.get(i + 1));
							bestDistance += DistanceUtil.getDistance(new LatLng(lPoints.get(i).lat, lPoints.get(i).lng),
									new LatLng(lPoints.get(i + 1).lat, lPoints.get(i + 1).lng));
						}
						tv_better.setText("参考路线 " + showDistance(bestDistance));
						tv_different.setText("里程差额 " + showDistance(PathDistance - bestDistance));

						if (currentIsChecked) {
							map.drawLine3(result.getData(), getResources().getColor(R.color.zuijia_lujin_color), 10, 8);
						}
						if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng)
								&& MsgConfig.select_lat != 0.0) {
							LatLng pot = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
							MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(true);
							mBaiduMap.addOverlay(ops);
						} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
							LatLng pot = new LatLng(MsgConfig.lat, MsgConfig.lng);
							MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(true);
							mBaiduMap.addOverlay(ops);
						} else {

						}
					} else {
						UIUtils.toast(PathQueryActivity.this, "无数据", Toast.LENGTH_SHORT);
						finish();
					}
				} else {
					UIUtils.toast(PathQueryActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Position();
	}

	@Override
	protected void onDestroy() {

		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		if (mSearch != null) {
			mSearch.destroy();
		}

		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_show:
				ll_hide.setVisibility(View.VISIBLE);
				ll_show.setVisibility(View.GONE);
				break;
			case R.id.ll_hide:
				ll_hide.setVisibility(View.GONE);
				ll_show.setVisibility(View.VISIBLE);
				break;
			case R.id.add_map:
				if (mBaiduMap != null) {
					MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
					mBaiduMap.setMapStatus(zoomIn);
				}
				break;
			case R.id.reduce_map:
				if (mBaiduMap != null) {
					MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
					mBaiduMap.setMapStatus(zoomOut);
				}
				break;
			default:
				break;
		}

	}
	// device.direct = (short) DirectionUtil.getAngle(
	// new DirectLatLng(device.lng / (double) 1E6, device.lat / (double) 1E6),
	// new DirectLatLng(lng / (double) 1E6, lat / (double) 1E6));

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
	public void reverseGeoCode1(final TextView tv, final double lat, final double lng) {
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
				}
			}
		});
	}

	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_position);
	boolean isLoctionOk = false;

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
			isLoctionOk = true;
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			lat = MsgConfig.lat;
			lng = MsgConfig.lng;
			LatLng ll = new LatLng(lat, lng);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
			isLoctionOk = true;
		} else {
			isLoctionOk = false;
		}
	}

	@Override
	public void onBackPressed() {

		finish();
	}

}
