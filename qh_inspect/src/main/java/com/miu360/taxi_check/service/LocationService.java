package com.miu360.taxi_check.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.data.UserPreference;
import com.miu30.common.config.MsgConfig;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.FenBuInfo;
import com.miu360.taxi_check.model.ZfryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class LocationService extends Service {

	private Handler handler = new Handler();
	private Handler handler1 = new Handler();
	ArrayList<ZfryInfo> list;
	ZfryInfo infoRY;
	boolean isFirst = true;
	UserPreference pref;
	double lat, lon;
	int runstate;// 运行状态：0，前台，1，后台
	Runnable mRunnable1 = new Runnable() {
		@Override
		public void run() {
			handler1.postDelayed(this, 3*1000);
			if (isBackground(LocationService.this) || !isLockScreen(LocationService.this)) {
				LocationModule.getInstance().onDestroy();
				if (runstate != 1) {
					runstate = 1;
					//System.err.println("转到后台，定位关闭...");
				}
				//System.err.println("后台运行中，定位暂停...");
				//return;
			} else {
				if (runstate != 0) {
					runstate = 0;
					LocationModule.getInstance().onCreate(getApplication(), true, 5 * 1000);
					//System.err.println("回到前台，定位重启...");
				} // 状态由后台转入前台
				//System.err.println("前台运行中");
			}

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		LocationModule.getInstance().onDestroy();
		LocationModule.getInstance().onCreate(getApplication(), true, 5 * 1000);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (isFirst) {
                    getZfryInfo();
				}

				if (infoRY != null) {
                    updateLocationData();
				}
				if (lat > 0 && lon > 0) {
					handler.postDelayed(this, delay_gps);
				} else {
					handler.postDelayed(this, delay_no_gps);
				}
			}
		};
		handler1.post(mRunnable1);
		handler.post(runnable);
	}

    private void updateLocationData() {
        if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng)
                && MsgConfig.select_lat != 0.0) {
            lat = MsgConfig.select_lat;
            lon = MsgConfig.select_lng;
        } else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
            lat = MsgConfig.lat;
            lon = MsgConfig.lng;
        } else {
			return;
        }
        final FenBuInfo info = new FenBuInfo();
        pref.setString("user_name_name", infoRY.getName());
        info.setAccount(infoRY.getZfzh());
        info.setLat(lat);
        info.setLon(lon);
        info.setPersonName(infoRY.getName());
        info.setSsdd(infoRY.getZfdwmc());
        AsyncUtil.goAsync(new Callable<Result<Long>>() {
            @Override
            public Result<Long> call() throws Exception {
                return UserData.shangChuanFenBuData(info);
            }
        }, new Callback<Result<Long>>() {

            @Override
            public void onHandle(Result<Long> result) {
                if (result.ok()) {
                }
            }
        });
    }

    private void getZfryInfo() {
        pref = new UserPreference(getApplicationContext());
        list = new ArrayList<>();

        final ZfryInfo infoZF = new ZfryInfo();

        AsyncUtil.goAsync(new Callable<Result<List<ZfryInfo>>>() {

            @Override
            public Result<List<ZfryInfo>> call() throws Exception {
                return WeiZhanData.queryZfryInfo(infoZF);
            }
        }, new Callback<Result<List<ZfryInfo>>>() {

            @Override
            public void onHandle(Result<List<ZfryInfo>> result) {
                if (result.ok()) {
                    list.addAll(result.getData());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getZfzh().equals(pref.getString("user_name", null))) {
                            infoRY = list.get(i);
                            isFirst = false;
                        }
                    }
                }
            }
        });
    }

    /**
	 * 应用程序是否转至后台运行
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
				 * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
				System.out.println(context.getPackageName() + ":此appimportace =" + appProcess.importance
						+ ",context.getClass().getName()=" + context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					System.out.println(context.getPackageName() + ":处于后台" + appProcess.processName);
					return true;
				} else {
					System.out.println(context.getPackageName() + ":处于前台" + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	private final long delay_no_gps = 10 * 1000;
	private final long delay_gps = 2 * 60 * 1000;

	private static boolean isLockScreen(Context context){
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
		return isScreenOn;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			LocationModule.getInstance().onDestroy();
			if(handler1 != null){
				handler1.removeCallbacks(mRunnable1);
			}
			if(handler != null){
				handler.removeCallbacks(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
