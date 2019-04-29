package com.miu360.taxi_check.util;

import java.util.ArrayList;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.miu360.taxi_check.App;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class OfflionManager {
	private int cityId;
	private MKOfflineMap mOffline;

	public void init(final int cityId) {
		this.cityId = cityId;
		mOffline = new MKOfflineMap();
		mOffline.init(new MKOfflineMapListener() {

			@Override
			public void onGetOfflineMapState(int type, int state) {
				switch (type) {
					case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
						Log.e("mOffline", "mOffline:update");
						MKOLUpdateElement update = mOffline.getUpdateInfo(state);
						// 处理下载进度更新提示
						if (update != null) {
							Log.d("Offline", String.format("%s : %d%%", update.cityName, update.ratio));
						}
					}
					break;
					case MKOfflineMap.TYPE_NEW_OFFLINE:
						Log.e("mOffline", "mOffline:type_new");
						// 有新离线地图安装
						Log.d("Offline", String.format("add offlinemap num:%d", state));
						break;
					case MKOfflineMap.TYPE_VER_UPDATE:
						// 版本更新提示
						// MKOLUpdateElement e = mOffline.getUpdateInfo(state);
						Log.e("mOffline", "mOffline:ver");
						break;
				}

			}
		});

		try {
			if (cityId != 0) {
				// 获取已下过的离线地图信息
				ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
				boolean download = true;
				if (localMapList != null) {
					for (MKOLUpdateElement mkolUpdateElement : localMapList) {
						if (mkolUpdateElement.cityID == cityId) {
							download = false;
							break;
						}
					}
				}
				if (download && isWifi(App.self)) {
					mOffline.start(cityId);
				}
				System.out.println("城市ID:"+cityId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		Log.e("mOffline", "mOffline:size");
		//@SuppressWarnings("deprecation")
		int size = mOffline.importOfflineData();
		System.out.println("offline_size:" + size);
		if (size > 0) {
			UIUtils.toast(App.self, "离线地图导入成功", Toast.LENGTH_LONG);
		}
	}

	public static boolean isWifi(Context ctx) {
		ConnectivityManager connectMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
	}

	public void onPause() {
		if (cityId != 0 && mOffline != null) {
			mOffline.pause(cityId);
		}
	}

	public void onDestroy() {
		if (mOffline != null) {
			mOffline.destroy();
		}
	}
}
