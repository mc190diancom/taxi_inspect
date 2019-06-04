package com.miu360.taxi_check.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.VehicleStatus;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.map.DrivingRouteOverlay;
import com.miu360.taxi_check.map.MapUtils;
import com.miu360.taxi_check.model.LatLngDir;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PathQueryActivity2 extends BaseActivity implements OnClickListener {
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

	@ViewInject(R.id.aim_location)
	private ImageButton aim_location;
	@ViewInject(R.id.map_reduce)
	private ImageButton map_reduce;
	@ViewInject(R.id.map_add)
	private ImageButton map_add;

	@ViewInject(R.id.ll_lw_hide)
	private LinearLayout ll_lw_hide;
	@ViewInject(R.id.toggle_One)
	private ToggleButton toggle_One;
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
	String car_Vname;

	private final String datePatterShow = "yyyy-MM-dd HH:mm";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		//SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_path_query2);
		ViewUtils.inject(this);

		Intent intent = getIntent();
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

		ll_lw_Show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				drawPath();
			}
		});
		aim_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Position();
			}
		});
		String s = new SimpleDateFormat(datePatterShow).format(new Date(startTime));
		String s2 = new SimpleDateFormat(datePatterShow).format(new Date(endTime));

		car_Vname = "京" + vname;

		tv_caridTv.setText(car_Vname);
		tv_start_time.setText(s);
		tv_end_time.setText(s2);
		holder = new HeaderHolder();
		holder.init(self, "轨迹查询");
		holder.rightTextBtn.setText("保存轨迹图片");
		holder.rightTextBtn.setVisibility(View.VISIBLE);
		//点击截图执行的操作，调用百度API
		holder.rightTextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				snapShotScope();
			}
		});
		holder.leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		ll_show_main.setOnClickListener(this);
		ll_show.setOnClickListener(this);
		ll_hide.setOnClickListener(this);
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
					//holder.root.setVisibility(View.GONE);
				} else if (ll_show_main.getVisibility() == View.GONE) {
					ll_show_main.setVisibility(View.VISIBLE);
					//holder.root.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	private void snapShotScope() {
		AsyncUtil.goAsync(new Callable<Result<Void>>() {

			@Override
			public Result<Void> call() throws Exception {
				Result<Void> r = new Result<>(Result.OK, null, null, null);
				mBaiduMap.snapshotScope(null,new SnapshotReadyCallback() {

					@Override
					public void onSnapshotReady(Bitmap snapshot) {
						File file = new File(Config.PATH+"inspect.png");
						FileOutputStream out;
						try {
							out = new FileOutputStream(file);
							if (snapshot.compress(Bitmap.CompressFormat.PNG, 100, out)) {
								out.flush();
								out.close();
							}
							UIUtils.toast(self, "保存轨迹图成功！", Toast.LENGTH_SHORT);
						} catch (FileNotFoundException e) {
							UIUtils.toast(self, "保存轨迹图失败！", Toast.LENGTH_SHORT);
							e.printStackTrace();
						} catch (IOException e) {
							UIUtils.toast(self, "保存轨迹图失败！", Toast.LENGTH_SHORT);
							e.printStackTrace();
						}
					}

				});
				return r;
			}
		}, new Callback<Result<Void>>() {

			@Override
			public void onHandle(Result<Void> result) {

			}
		});

	}

	/**
	 * 获取驾车路线推荐
	 */
	// public void bestDrivePath() {
	// mSearch = RoutePlanSearch.newInstance();
	//
	// OnGetRoutePlanResultListener listener = new
	// OnGetRoutePlanResultListener() {
	// public void onGetWalkingRouteResult(WalkingRouteResult result) {
	// // 获取步行线路规划结果
	// }
	//
	// public void onGetTransitRouteResult(TransitRouteResult result) {
	// // 获取公交换乘路径规划结果
	// }
	//
	// public void onGetDrivingRouteResult(DrivingRouteResult result) {
	// // 获取驾车线路规划结果
	// if (result == null
	// || result.error != SearchResult.ERRORNO.NO_ERROR) {
	// UIUtils.toast(PathQueryActivity.this, "抱歉，未找到结果", 0);
	// }
	// if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	// // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	// // result.getSuggestAddrInfo()
	// return;
	// }
	// if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	//
	// DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(
	// mBaiduMap);
	// // OverlayManager routeOverlay = overlay;
	// mBaiduMap.setOnMarkerClickListener(overlay);
	// DrivingRouteLine drivingRouteLine = result.getRouteLines().get(0);
	// overlay.setData(drivingRouteLine);
	// overlay.addToMap();
	// overlay.zoomToSpan();
	// float bestDistance = drivingRouteLine.getDistance()/1000f;
	//// tv_better.setText("最佳路线 "+bestDistance+"km");
	//// tv_different.setText("里程差额 "+(PathDistance-bestDistance)+"km");
	// }
	// }
	//
	// @Override
	// public void onGetBikingRouteResult(BikingRouteResult arg0) {
	//
	// }
	// };
	// mSearch.setOnGetRoutePlanResultListener(listener);
	//
	// PlanNode stNode = PlanNode.withLocation(start);
	// PlanNode enNode = PlanNode.withLocation(end);
	//
	// // 发起驾车线路规划检索
	// mSearch.drivingSearch((new
	// DrivingRoutePlanOption()).from(stNode).to(enNode));
	// }

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
	}

	ArrayList<VehicleStatus> vehicArrayList = new ArrayList<>();
	ArrayList<VehicleStatus> vehicArrayLists = new ArrayList<>();

	ArrayList<LatLng> zhongArrayList = new ArrayList<>();
	ArrayList<ArrayList<LatLng>> allArrayList = new ArrayList<>();

	private void drawPath() {
		mBaiduMap.clear();
		PathDistance = 0;
		bestDistance = 0;
		final MyProgressDialog d = Windows.waiting(self);
		d.setCancelable(true);
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
						File file1 = new File(Config.PATH + "inspect.png");
						if (file1.exists()) {
							file1.delete();
						}
						vehicArrayList.clear();
						vehicArrayLists.clear();
						vehicArrayLists.addAll(result.getData());
						CoordinateConverter converter = new CoordinateConverter();
						converter.from(CoordType.GPS);
						for (VehicleStatus point : vehicArrayLists) {
							VehicleStatus vehicle=new VehicleStatus();
							double lat = point.getLat();
							double lon = point.getLon();
							LatLng sourceLatLng = new LatLng(lat, lon);
							// sourceLatLng待转换坐标
							converter.coord(sourceLatLng);
							LatLng desLatLng = converter.convert();
							vehicle.setLat(desLatLng.latitude);
							vehicle.setLon(desLatLng.longitude);
							vehicle.setVEHICLE_STATUS(point.getVEHICLE_STATUS());
							vehicle.GPS_TIME=point.getGPS_TIME();
							vehicle.AZIMUTHS=point.getAZIMUTHS();
							vehicArrayList.add(vehicle);
						}
						// Log.e("1", vehicArrayList.get(0).toString());
						int state = 0;
						int direction = 0;
						List<List<LatLng>> allRoute = new ArrayList<>();
						List<LatLng> kongArrayList = new ArrayList<>();
						List<LatLng> route = new ArrayList<>();
						List<Integer> colors = new ArrayList<>();
						List<Short> dirInt = new ArrayList<>();
						List<Short> directionInt = new ArrayList<>();
						List<LatLng> dirLat = new ArrayList<>();
						List<Long> dirTime = new ArrayList<>();
						for (int i = 0; i < vehicArrayList.size() - 1; i++) {
							// DirectLatLng A = new
							// DirectLatLng(vehicArrayList.get(i - 1).getLon(),
							// vehicArrayList.get(i - 1).getLat());
							// DirectLatLng B = new
							// DirectLatLng(vehicArrayList.get(i).getLon(),
							// vehicArrayList.get(i).getLat());
							// Short direct = (short) DirectionUtil.getAngle(A,
							// B);
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
						for (int i = 0; i < dirInt.size(); i++) {
							if (!kongArrayList.isEmpty()) {
								if (Math.abs(DistanceUtil.getDistance(dirLat.get(i),
										kongArrayList.get(kongArrayList.size() - 1))) > 800) {
									// if
									// (Math.abs(dirTime.get(i)-directionInt.get(kongTimeArrayList.size()
									// - 1)) > 30*1000) {
									// if (Math.abs(dirInt.get(i) -
									// directionInt.get(directionInt.size() -
									// 1)) > 30) {
									directionInt.add(dirInt.get(i));
									kongArrayList.add(dirLat.get(i));
									// }
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

							LatLng zhong = new LatLng(vehicArrayList.get(i).getLat(), vehicArrayList.get(i).getLon());
							route.add(zhong);

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
						/*reverseGeoCode1(tv_start,vehicArrayLists.get(0).getLat(), vehicArrayLists.get(0).getLon());
						reverseGeoCode1(tv_end,vehicArrayLists.get(vehicArrayLists.size() - 1).getLat(),
								vehicArrayLists.get(vehicArrayLists.size() - 1).getLon());*/

						// 设置起点图片
						MarkerOptions placeHolder = new MarkerOptions().position(start)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_qidian_huan));
						mBaiduMap.addOverlay(placeHolder);
						// 设置终点图片
						MarkerOptions placeHolder1 = new MarkerOptions().position(end)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_zhongdian_huan));
						mBaiduMap.addOverlay(placeHolder1);
						mBaiduMap.setMaxAndMinZoomLevel(18, 12);
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
						// Overlay line = map.drawLineKZ(allRoute, color, 10,
						// 10);

						map.drawPoint(kongArrayList, directionInt, 10);
						ArrayList<Overlay> os = new ArrayList<>();
						for (int i = 0; i < overArrayList.size(); i++) {
							Overlay line = overArrayList.get(i);
							os.add(line);
						}
						map.zoomToSpan(mBaiduMap, os);
						// bestDrivePath();

						drawBest2(vehicArrayList.get(0).getLat(), vehicArrayList.get(0).getLon(),
								vehicArrayList.get(vehicArrayList.size() - 1).getLat(),
								vehicArrayList.get(vehicArrayList.size() - 1).getLon());
						initAddAndReduce();
					} else {
						UIUtils.toast(PathQueryActivity2.this, "无数据", Toast.LENGTH_SHORT);
						finish();
					}
				} else {
					UIUtils.toast(PathQueryActivity2.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}
		});
	}

	private void initAddAndReduce(){
		if(mBaiduMap != null){
			map_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
					mBaiduMap.setMapStatus(zoomIn);
				}
			});
			map_reduce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
					mBaiduMap.setMapStatus(zoomOut);
				}
			});
		}
	}
	// private void drawPath2() {
	// final MyProgressDialog d = Windows.waiting(self);
	// d.setCancelable(true);
	// AsyncUtil.goAsync(new Callable<Result<List<LatLng>>>() {
	//
	// @Override
	// public Result<List<LatLng>> call() throws Exception {
	// return HistoryData.queryHistoryTrack(vname,
	// getIntent().getLongExtra("startTime", System.currentTimeMillis() - 3600 *
	// 1000),
	// getIntent().getLongExtra("endTime", System.currentTimeMillis()));
	// }
	// }, new Callback<Result<List<LatLng>>>() {
	//
	// @Override
	// public void onHandle(Result<List<LatLng>> result) {
	// d.dismiss();
	// if (result.ok()) {
	// if (!result.getData().isEmpty()) {
	// mPoints.clear();
	// mPoints.addAll(result.getData());
	//
	// start = mPoints.get(0);
	// end = mPoints.get(mPoints.size() - 1);
	// reverseGeoCode(tv_start, start);
	// reverseGeoCode(tv_end, end);
	//
	// // 设置起点图片
	// MarkerOptions placeHolder = new MarkerOptions().position(start)
	// .icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_qidian_huan));
	// mBaiduMap.addOverlay(placeHolder);
	// // 设置终点图片
	// MarkerOptions placeHolder1 = new MarkerOptions().position(end)
	// .icon(BitmapDescriptorFactory.fromResource(R.drawable.guiji_zhongdian_huan));
	// mBaiduMap.addOverlay(placeHolder1);
	// // 计算距离
	// for (int i = 0; i < mPoints.size() - 1; i++) {
	// PathDistance += DistanceUtil.getDistance(mPoints.get(i), mPoints.get(i +
	// 1));
	// }
	//
	// tv_path.setText("行驶轨迹 " + showDistance(PathDistance));
	// Overlay line = map.drawLine2(mPoints,
	// getResources().getColor(R.color.xinshi_lujin_color), 10,
	// 10);
	// map.drawPoint(mPoints, 11);
	// ArrayList<Overlay> os = new ArrayList<>();
	// os.add(line);
	// map.zoomToSpan(mBaiduMap, os);
	//
	// // bestDrivePath();
	// drawBest2(result.getData().get(0).latitude,
	// result.getData().get(0).longitude,
	// result.getData().get(result.getData().size() - 1).latitude,
	// result.getData().get(result.getData().size() - 1).longitude);
	// // drawBest(result.getData().get(0).latitude,
	// // result.getData().get(0).longitude,
	// // result.getData().get(result.getData().size() -
	// // 1).latitude,
	// // result.getData().get(result.getData().size() -
	// // 1).longitude);
	// } else {
	// UIUtils.toast(PathQueryActivity.this, "无数据", Toast.LENGTH_SHORT);
	// finish();
	// }
	// } else {
	// UIUtils.toast(PathQueryActivity.this, result.getErrorMsg(),
	// Toast.LENGTH_SHORT);
	// finish();
	// }
	// }
	// });
	// }

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
	// private void drawBest(final double start_lat, final double start_lng,
	// final double end_lat, final double end_lng) {
	// AsyncUtil.goAsync(new Callable<Result<List<LatLng>>>() {
	//
	// @Override
	// public Result<List<LatLng>> call() throws Exception {
	// return HistoryData.queryBestTrack(start_lat, start_lng, end_lat,
	// end_lng);
	// }
	// }, new Callback<Result<List<LatLng>>>() {
	//
	// @Override
	// public void onHandle(Result<List<LatLng>> result) {
	// if (result.ok()) {
	// if (!result.getData().isEmpty()) {
	//
	// bPoints.clear();
	// bPoints.addAll(result.getData());
	// // int bestDistance = (int)
	// // (result.getRouteLines().get(0).getDistance() /
	// // 1000f);
	// result.getData().add(0, new LatLng(start_lat, start_lng));
	// result.getData().add(new LatLng(end_lat, end_lng));
	//
	// for (int i = 0; i < bPoints.size() - 1; i++) {
	// bestDistance += DistanceUtil.getDistance(bPoints.get(i), bPoints.get(i +
	// 1));
	// }
	//// map.drawPoint(bPoints, 9);
	// tv_better.setText("参考路线 " + showDistance(bestDistance));
	// tv_different.setText("里程差额 " + showDistance(PathDistance -
	// bestDistance));
	// map.drawLine2(result.getData(),
	// getResources().getColor(R.color.zuijia_lujin_color), 8, 8);
	// } else {
	// UIUtils.toast(PathQueryActivity.this, "无数据", Toast.LENGTH_SHORT);
	// finish();
	// }
	// } else {
	// UIUtils.toast(PathQueryActivity.this, result.getErrorMsg(),
	// Toast.LENGTH_SHORT);
	// finish();
	// }
	// }
	// });
	// }

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
						if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
							LatLng pot = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
							MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(true);
							mBaiduMap.addOverlay(ops);
						}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
							LatLng pot = new LatLng(MsgConfig.lat, MsgConfig.lng);
							MarkerOptions ops = new MarkerOptions().position(pot).icon(ba).zIndex(5).draggable(true);
							mBaiduMap.addOverlay(ops);
						}else{

						}
					} else {
						UIUtils.toast(PathQueryActivity2.this, "无数据", Toast.LENGTH_SHORT);
						finish();
					}
				} else {
					UIUtils.toast(PathQueryActivity2.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}
		});
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
			// case R.id.right_text_btn:
			//
			// Toast.makeText(self, "功能暂未开放", Toast.LENGTH_SHORT).show();
			// break;
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

	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_position);
	//初始化定位
	private void Position(){
		double lat,lng;
		if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
			lat = MsgConfig.select_lat;
			lng = MsgConfig.select_lng;
			LatLng ll = new LatLng(lat, lng);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
		}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
			lat = MsgConfig.lat;
			lng = MsgConfig.lng;
			LatLng ll = new LatLng(lat, lng);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			MarkerOptions ops = new MarkerOptions().position(ll).icon(ba).zIndex(5).draggable(false);
			mBaiduMap.addOverlay(ops);
		}else{

		}
	}

	@Override
	public void onBackPressed() {
		Intent intentLaw = new Intent();
		intentLaw.putExtra("tv_start", tv_start.getText().toString());
		intentLaw.putExtra("tv_end", tv_end.getText().toString());
		intentLaw.putExtra("tv_start_time", tv_start_time.getText().toString());
		intentLaw.putExtra("tv_end_time", tv_end_time.getText().toString());
		intentLaw.putExtra("car_number_history", tv_caridTv.getText().toString());
		intentLaw.putExtra("PathDistance", showDistance(PathDistance - bestDistance));
		//设置回传时的标志
		intentLaw.putExtra("isHistoryPath", true);
		intentLaw.putExtra("licences_jiandu", getIntent().getStringExtra("licences_jiandu"));
		intentLaw.putExtra("driver_name_et", getIntent().getStringExtra("driver_name_et"));
		intentLaw.putExtra("licences_lihu", getIntent().getStringExtra("licences_lihu"));
		intentLaw.putExtra("company_name_et", getIntent().getStringExtra("company_name_et"));
		intentLaw.setAction("com.miu360.path");
		intentLaw.putExtra("path", "successful");
		self.sendBroadcast(intentLaw);
		//startActivity(intentLaw);
		//setResult(12, intentLaw);
		finish();
	}
}
