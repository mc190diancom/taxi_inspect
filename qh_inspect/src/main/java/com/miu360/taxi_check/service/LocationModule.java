package com.miu360.taxi_check.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.util.Store2SdUtil;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationModule {
	private static LocationModule self;

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	private LocationModule() {
	}

	public static synchronized LocationModule getInstance() {
		if (self == null) {
			self = new LocationModule();
		}
		return self;
	}

	public void onCreate(Application app, boolean Hight_Accuracy, int scanSpan) {
		mLocationClient = new LocationClient(app);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(Hight_Accuracy);// 打开gps
		option.setCoorType(MAP_COOR_TYPE); // 设置坐标类型
		option.setScanSpan(scanSpan);
		//option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setLocationMode(LocationMode.Device_Sensors);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	public static boolean isGpgOpened(Context ctx) {
		LocationManager alm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}
//	private void checkGps() {
//		if (!Util.isGpgOpened(self)) {
// 			CommonDialog dialog=null;
//			if (dialog == null || !dialog.isShowing()) {
//				dialog = Windows.alert(self, "您未打开GPS，存在较大定位不准风险，请立即打开", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UIUtils.safeOpenLink(self, new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//					}
//				});
//				dialog.setCancelable(false);
//			}
//		}
//	}

	public void changeGps(boolean Hight_Accuracy) {
		if (mLocationClient != null) {
			try {
				mLocationClient.getLocOption().setOpenGps(Hight_Accuracy);
				//mLocationClient.getLocOption()
				//	.setLocationMode(Hight_Accuracy ? LocationMode.Hight_Accuracy : LocationMode.Battery_Saving);
				mLocationClient.getLocOption().setLocationMode(LocationMode.Device_Sensors);
				mLocationClient.stop();
				mLocationClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	public static final String MAP_COOR_TYPE = "bd09ll";

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			MsgConfig.lat = location.getLatitude();
			MsgConfig.lng = location.getLongitude();
			//LatLng latLng = new LatLng(MsgConfig.lat,MsgConfig.lng);
            //Store2SdUtil.getInstance(App.self).addOut(latLng, Config.ID_FILE_NAME3);
		}

	}
}
