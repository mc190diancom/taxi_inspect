package com.miu360.taxi_check.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.lubao.lubao.async.DirectionUtil;
import com.lubao.lubao.async.DirectionUtil.DirectLatLng;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.VehicleStatus;
import com.miu360.taxi_check.data.device;
import com.miu360.taxi_check.model.LatLngDir;

import android.app.Activity;
import android.util.Log;

public class MapUtils {
	MapView mMapView;
	public BaiduMap mBaiduMap;

	public void initMap(Activity act, MapView mMapView, BaiduMap mBaiduMap) {
		this.mMapView = mMapView;
		this.mBaiduMap = mBaiduMap;

		// 地图初始化
		mBaiduMap = mMapView.getMap();
	}

	public Overlay drawLine(List<LatLng> points, int color, int width) {
		// 构造纹理资源
		// BitmapDescriptor custom2 = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_road_green_arrow);

		// 构造对象
		OverlayOptions ooPolyline = new PolylineOptions().width(width).color(color).points(points);
		// 添加到地图
		return mBaiduMap.addOverlay(ooPolyline);
	}

	BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.direction_biaoshi);
	BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.drawable.direction_biaoshi_other);

	/**
	 * 设置标示方向的图片
	 */
	public void drawPoint(List<LatLng> points,List<Short> direction, int index) {
		// 构造纹理资源
		// BitmapDescriptor custom2 = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_road_green_arrow);

		// 计算没有转过的角度
		// device.direct = (short) DirectionUtil.getAngle(
		// new DirectLatLng(device.lng / (double) 1E6, device.lat / (double)
		// 1E6),
		// new DirectLatLng(lng / (double) 1E6, lat / (double) 1E6));

		/*
		 * 计算两个坐标之间的方位角
		 */
//		ArrayList<Short> direction = new ArrayList<>();
//		for (int i = 1; i < points.size(); i++) {
//			DirectLatLng A = new DirectLatLng(points.get(i - 1).longitude, points.get(i - 1).latitude);
//			DirectLatLng B = new DirectLatLng(points.get(i).longitude, points.get(i).latitude);
//			short direct = (short) DirectionUtil.getAngle(A, B);
//			direction.add(direct);
//		}
//		MarkerOptions ooA = null;
		for (int i = 0; i < points.size(); i++) {
			MarkerOptions ooA = new MarkerOptions().position(points.get(i)).icon(bdB).zIndex(index).rotate(360 - direction.get(i));
			mBaiduMap.addOverlay(ooA);
		}
	}

	public Overlay drawLine2(List<LatLng> points, int color, int width, int index) {
		// 构造纹理资源
		// BitmapDescriptor custom2 = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_road_green_arrow);
		// 构造对象
		OverlayOptions ooPolyline = new PolylineOptions().width(width).color(color).points(points).zIndex(index);
		// 添加到地图
		return mBaiduMap.addOverlay(ooPolyline);
	}

	public Overlay drawLineKZ(List<List<LatLng>> allRoute,List<Integer> color, int width, int index) {
		// 构造纹理资源List<VehicleStatus>
		// BitmapDescriptor custom2 = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_road_green_arrow);
		List<LatLng> kPoints=new ArrayList<>();
		int colors=0;
		OverlayOptions ooPolyline=null;
		for (int i = 0; i < allRoute.size(); i++) {
			kPoints=allRoute.get(i);
			if(color.get(i)==0){
				colors=R.color.kong_lujin_color;
			}else{
				colors=R.color.zhong_lujin_color;
			}
			ooPolyline = new PolylineOptions().width(width).color(1).points(kPoints).zIndex(index);
		}
		// 构造对象
		// 添加到地图
		return mBaiduMap.addOverlay(ooPolyline);
	}
	/*
	 * 绘制最佳路线
	 */
	public Overlay drawLine3(List<LatLngDir> latLngDir, int color, int width, int index) {

		ArrayList<LatLng> points = new ArrayList<>();

		for (LatLngDir latLngDir2 : latLngDir) {
			LatLng latLng = new LatLng(latLngDir2.lat, latLngDir2.lng);
			points.add(latLng);
			MarkerOptions ooA = null;
			// 获取方位角
			if (latLngDir2.dir != -1) {
				float rot = 0;
				switch ((int) latLngDir2.dir) {
					case 0:
						rot = 0;
						break;
					case 1:
						rot = 30;
						break;
					case 2:
						rot = 60;
						break;
					case 3:
						rot = 90;
						break;
					case 4:
						rot = 120;
						break;
					case 5:
						rot = 150;
						break;
					case 6:
						rot = 180;
						break;
					case 7:
						rot = 210;
						break;
					case 8:
						rot = 240;
						break;
					case 9:
						rot = 270;
						break;
					case 10:
						rot = 300;
						break;
					case 11:
						rot = 330;
						break;

					default:
						break;
				}
				ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(index + 1).rotate((float) 360 - rot);
				mBaiduMap.addOverlay(ooA);
			}

		}

		// 构造对象
		OverlayOptions ooPolyline = new PolylineOptions().width(width).color(color).points(points).zIndex(index);
		// 添加到地图
		return mBaiduMap.addOverlay(ooPolyline);
	}

	/**
	 * 将地图缩放到可以显示整个路径的比例
	 *
	 * @param mBaiduMap
	 * @param overlayList
	 */
	public void zoomToSpan(BaiduMap mBaiduMap, List<Overlay> overlayList) {
		try {
			if (mBaiduMap == null) {
				return;
			}
			if (overlayList.size() > 0) {
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (Overlay overlay : overlayList) {
					if (overlay instanceof Marker) {
						builder.include(((Marker) overlay).getPosition());
					} else if (overlay instanceof Polyline) {
						List<LatLng> ps = ((Polyline) overlay).getPoints();
						for (LatLng latLng : ps) {
							builder.include(latLng);
						}
					}
				}
				mBaiduMap.setMaxAndMinZoomLevel(20, 12);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 地图坐标转换方法
	 */
	public static LatLng convertor(LatLng sourceLatLng) {
		// 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
		/*
		 * CoordinateConverter converter = new CoordinateConverter();
		 * converter.from(CoordType.COMMON); // sourceLatLng待转换坐标
		 * converter.coord(sourceLatLng); LatLng desLatLng =
		 * converter.convert();
		 */

		// 将GPS设备采集的原始GPS坐标转换成百度坐标
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}

	/**
	 * 记录坐标
	 */
	public static void savePoints() {
		BDLocation location = new BDLocation();
		// TO-DO 弄个集合,封装起来,提交的时候转换为json
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
	}

}
