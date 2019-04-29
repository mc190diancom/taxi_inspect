package com.miu360.taxi_check;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.epson.isv.eprinterdriver.Ctrl.EPrintManager;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.CrashHandler;
import com.miu360.inspect.BuildConfig;

public class App extends MiuBaseApp {
	public static App self;

	@Override
	public void onCreate() {
		super.onCreate();
		self = this;
		SDKInitializer.initialize(getApplicationContext());
		EPrintManager epManager = EPrintManager.instance();
		epManager.initEscprLib(getApplicationContext());
//		SpeechUtility.createUtility(self, "appid=" + Config.IFLYTEK);
//		if (UIUtils.isMainProcess(self)) {
//			ShareSDK.initSDK(self);
//		}
		//CrashHandler.getInstance().init(self);
		if (BuildConfig.DEBUG) {
			/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());*/
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this) ;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		EPrintManager epManager = EPrintManager.instance();
		epManager.releaseEscprLib(getApplicationContext());
	}
}
