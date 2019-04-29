package com.miu360.taxi_check.util;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

import android.app.Activity;



public class MapUtils {
	// 定位相关
	public LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xAAFFFF88;
	private static final int accuracyCircleStrokeColor = 0xAA00FF00;

	MapView mMapView;
	public BaiduMap mBaiduMap;


	boolean isFirstLoc = true; // 是否首次定位
	public void initMap(Activity act,MapView mMapView){
		this.mMapView=mMapView;
		mCurrentMode = LocationMode.NORMAL;

		// 地图初始化
		mBaiduMap = mMapView.getMap();


		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(act);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		//option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void drawLine(List<LatLng> points){
		//构造纹理资源
//			BitmapDescriptor custom2 = BitmapDescriptorFactory
//							.fromResource(R.drawable.icon_road_green_arrow);

		//构造对象
		OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points);
		//添加到地图
		mBaiduMap.addOverlay(ooPolyline);
	}
	/*
     * 地图坐标转换方法
     */
	public static LatLng convertor(LatLng sourceLatLng){
		// 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
			/*CoordinateConverter converter  = new CoordinateConverter();
			converter.from(CoordType.COMMON);
			// sourceLatLng待转换坐标
			converter.coord(sourceLatLng);
			LatLng desLatLng = converter.convert();*/

		// 将GPS设备采集的原始GPS坐标转换成百度坐标
		CoordinateConverter converter  = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}
	/**
	 * 记录坐标
	 */
	public static void savePoints(){
		BDLocation location=new BDLocation();
		//TO-DO 弄个集合,封装起来,提交的时候转换为json
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
	}

}
