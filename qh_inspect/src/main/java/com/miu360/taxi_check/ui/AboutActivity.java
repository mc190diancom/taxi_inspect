package com.miu360.taxi_check.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.feidi.video.mvp.ui.activity.ArouterTestActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.config.MsgConfig;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.model.Version;
import com.miu360.taxi_check.service.DownloadService;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.util.UiTool;
import com.miu360.taxi_check.view.HeaderHolder;

import java.util.List;
import java.util.concurrent.Callable;

public class AboutActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.version)
	private TextView version;
	@ViewInject(R.id.features)
	private TextView features;
	@ViewInject(R.id.question_feedback)
	private TextView question_feedback;
	@ViewInject(R.id.banben)
	private TextView banben;
	@ViewInject(R.id.phone_number)
	private TextView phone_number;
	@ViewInject(R.id.iv_red_circle)
	private ImageView iv_red_circle;

	@ViewInject(R.id.ll_banben)
	private LinearLayout ll_banben;
	@ViewInject(R.id.update)
	private LinearLayout update;// 更新
	@ViewInject(R.id.update_cancle)
	private Button update_cancle;// 取消
	@ViewInject(R.id.update_Progress)
	private ProgressBar update_Progress;// 进度条
	@ViewInject(R.id.update_Description)
	private TextView update_Description;// 显示当前更新进度

	final String FLAG = "com.flr.flr";
	MyReceiver broadCast;
	Version vs;
	String mVersion = "";
	// 文件大小
	private int mLength = 0;
	private String oldmVersion;
	private int LocalVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
		try {
			String versions = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			// version.setText("v" + versions + "版本");
			if (Config.IP.equals("10.252.2.68")) {
				version.setText("测试v" + versions + "版本");
			} else if (Config.IP.equals("10.252.2.67")) {
				version.setText("正式v" + versions + "版本");
			} else if (Config.IP.equals("10.212.160.180")) {
				version.setText("测式(180)v" + versions + "版本");
			} else if (Config.IP.equals("10.212.160.137")) {
				version.setText("正式(137)v" + versions + "版本");
			} else if (Config.IP.equals("123.57.236.212")) {
				version.setText("外网v" + versions + "版本");
			} else {
				version.setText("v" + versions + "版本");
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		broadCast = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(FLAG);
		LocalBroadcastManager.getInstance(App.self).registerReceiver(broadCast, filter);
		queryVersion();
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String message = intent.getStringExtra("Data");
			banben.setText(message);
		}
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "关于");
		question_feedback.setOnClickListener(this);
		//banben.setOnClickListener(this);
		ll_banben.setOnClickListener(this);
		phone_number.setOnClickListener(this);
		features.setOnClickListener(this);
		update_cancle.setOnClickListener(this);
		registerMsgReceiver();
	}

	@Override
	public void onClick(View v) {
		if (v == question_feedback) {
			Intent intent = new Intent(self, FeedBackActivity.class);
			startActivity(intent);
		} else if (v == ll_banben) {
			startActivity(new Intent(self,ArouterTestActivity.class));
			//checkVersion();
		} else if (v == phone_number) {
			UiTool.call(self, getString(R.string.tel_official));
		} else if (v == features) {

		} else if (v == update_cancle) {
			MsgConfig.isUpdate = false;
			update.setVisibility(View.GONE);
		}
	}

	private void queryVersion() {
		if (MsgConfig.isUpdate) {
			UIUtils.toast(self, "最新版本正在下载中，请稍后~~", Toast.LENGTH_SHORT);
			return;
		}
		// 获取从接口返回的版本号对比，如果不一样提示更新操作
		vs = new Version();
		AsyncUtil.goAsync(new Callable<Result<List<Version>>>() {

			@Override
			public Result<List<Version>> call() throws Exception {
				return UserData.queryVersion();
			}
		}, new Callback<Result<List<Version>>>() {

			@Override
			public void onHandle(Result<List<Version>> result) {
				if (result.ok()) {
					String version = null;

					try {
						oldmVersion = version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode+"";
						LocalVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					if (result.getData() == null || "[]".equals(result.getData().toString())) {
						return;
					}
					vs = result.getData().get(0);
					mVersion = vs.getVERSION();
					mLength = Integer.parseInt(result.getData().get(0).getSIZES());
					if(vs.getVERSION().equals("0.9.8")){//为了兼容之前传错的版本号格式
						return;
					}
					if (LocalVersion < Integer.valueOf(vs.getVERSION())) {
						iv_red_circle.setVisibility(View.VISIBLE);
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void checkVersion(){
		if(vs.getVERSION().equals("0.9.8")){//为了兼容之前传错的版本号格式
			UIUtils.toast(self, "这已经是最新的版本", Toast.LENGTH_SHORT);
			return;
		}
		if (LocalVersion < Integer.valueOf(vs.getVERSION())) {
			Windows.confirm(self, 0, "检查到新版本，是否更新？", vs.getLOG(), "现在更新", new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDownload(mVersion);
					update.setVisibility(View.VISIBLE);
				}

			}, "稍后下载", new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			}, 0, null);
		} else {
			UIUtils.toast(self, "这已经是最新的版本", Toast.LENGTH_SHORT);
		}

	}

	/**
	 * 开启意图服务下载apk
	 *
	 * @param versioin
	 */
	public void showDownload(String versioin) {
		String url = Config.SERVER_SPECIAL + "?type=downloadApk&version=" + versioin;
		Intent myIntent = new Intent(self, DownloadService.class);
		myIntent.putExtra("URL", url);
		myIntent.putExtra("length", mLength);
		myIntent.putExtra("versioin", versioin);
		myIntent.putExtra("oldversioin", oldmVersion);
		startService(myIntent);
	}

	/**
	 * 开启广播接收升级时服务传递的进度更新
	 */
	public void registerMsgReceiver() {
		IntentFilter filter = new IntentFilter("com.miu360.update");
		registerReceiver(msgReceiver, filter);
	}

	public void unregisterMsgReceiver() {
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (update.getVisibility() == View.GONE) {
				update.setVisibility(View.VISIBLE);
			}
			int progress = intent.getIntExtra("update", 0);
			update_Progress.setProgress(progress);
			update_Description.setText("当前下载进度:" + progress + "%");
			if (progress == 99) {
				update.setVisibility(View.GONE);
			}
		}
	};

	@Override
	protected void onDestroy() {
		unregisterMsgReceiver();
		super.onDestroy();
		LocalBroadcastManager.getInstance(App.self).unregisterReceiver(broadCast);
	}

}
