package com.miu360.taxi_check.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.feidi.video.app.services.TCPConnectService;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.integration.AppManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.data.UserPreference;
import com.miu30.common.util.DownLoadUtil;
import com.miu30.common.util.FileUtil;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.inspect.R;
import com.miu30.common.app.EventBusTags;
import com.miu360.legworkwrit.app.service.GeneralInformationService;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.util.WritTemplateUtil;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.GpsManger;
import com.miu360.taxi_check.common.MapPositionPreference;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu360.taxi_check.common.PositionPreference;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.data.CameraPreference;
import com.miu360.taxi_check.data.InfoPerference;
import com.miu30.common.ui.entity.Inspector;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.data.WeiFaCheckPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.fragment.CarDistributionFragment;
import com.miu360.taxi_check.fragment.HomeFragment;
import com.miu360.taxi_check.fragment.IDFragment;
import com.miu360.taxi_check.fragment.MineFragment;
import com.miu360.taxi_check.fragment.PathQueryFragment;
import com.miu360.taxi_check.model.BindCamera;
import com.miu360.taxi_check.model.ExitStatus;
import com.miu360.taxi_check.model.UnboundCamera;
import com.miu360.taxi_check.model.ZFRYDetailInfo;
import com.miu360.taxi_check.service.LocationService;
import com.miu360.taxi_check.service.PushService;
import com.miu360.taxi_check.util.OfflionManager;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MainActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	@ViewInject(R.id.iv_mine)
	private LinearLayout iv_mine;
	@ViewInject(R.id.iv_title)
	private ImageView iv_title;
	/*
	 * @ViewInject(R.id.iv_setting) private ImageView iv_setting;
	 */
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.RG)
	private RadioGroup mRadioGroup;
	@ViewInject(R.id.title)
	public RelativeLayout title;
	@ViewInject(R.id.history)
	private TextView history;

	@ViewInject(R.id.b1)
	public RadioButton rb1;

	private Fragment mCurrentFragment;
	private List<Fragment> mFragments;
	private FragmentManager mFragmentManager;
	private int count = 0;

	OfflionManager offlion;
	ArrayList<UnboundCamera> exitCameraList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		startService(new Intent(self, LocationService.class));
		startService(new Intent(self, PushService.class));
		startService(new Intent(self, GeneralInformationService.class));
		startService(new Intent(this, TCPConnectService.class));
		initView();
		if(!GpsManger.isOPen(this)){
			Windows.confirm(self, "GPS未开启是否开启？", new OnClickListener() {

				@Override
				public void onClick(View v) {
					GpsManger.openGps(MainActivity.this);
				}
			});
		}

		pref = new UserPreference(self);
		new InfoPerference(self).setIsNormal(false);
		queryZFRY();
		// 启动定时器
		initHandler();
		initTemp();
		offlion = new OfflionManager();
		checkMap();
	}

	/**
	 * 拷贝文件
	 */
	protected void checkMap() {
		final int version = Build.VERSION.SDK_INT;
		String pathRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		final File file = new File(pathRoot + "/Android/data/"
				+ getPackageName() + "/files/BaiduMapSDKNew/vmp/DVUserdat.cfg");
		final File fileOff2 = new File(pathRoot + "/Android/data/"
				+ getPackageName() + "/files/BaiduMapSDKNew/vmp/quanguogailue.dat");

		final File tempFilecfg = new File(pathRoot + "/BaiduMapSDKNew/vmp/DVUserdat.cfg");
		final File tempFile2 = new File(pathRoot + "/BaiduMapSDKNew/vmp/quanguogailue.dat");

		final File fileOff = new File(pathRoot + "/Android/data/"
				+ getPackageName() + "/files/BaiduMapSDKNew/vmp/beijing_131.dat");
		final File tempFile = new File(pathRoot + "/BaiduMapSDKNew/vmp/beijing_131.dat");
		copyMapConf(version, file,fileOff2,tempFilecfg, tempFile2);
		downLoadMap(version,fileOff,tempFile);
		new WritTemplateUtil(self).init();
	}

	/*
	 * 下载资源较大的北京离线地图
	 */
	private void downLoadMap(final int version, final File fileOff,  final File tempFile) {
		if((version >= Build.VERSION_CODES.LOLLIPOP && !fileOff.exists()) || (version < Build.VERSION_CODES.LOLLIPOP && !tempFile.exists()) ||
				(version >= Build.VERSION_CODES.LOLLIPOP && (fileOff.exists() && fileOff.length() < 120*1000*1000)) ||
				(version < Build.VERSION_CODES.LOLLIPOP && (tempFile.exists() && tempFile.length() < 120*1000*1000))){
			Message message = new Message();
			if(version >= Build.VERSION_CODES.LOLLIPOP ){
				message.obj = fileOff.getAbsolutePath();
			}else{
				message.obj = tempFile.getAbsolutePath();
			}
			message.what = EventBusTags.DOWNLOAD_MAP;
			AppManager.post(message);
		}
	}

	/*
	 * 把本地存在的资源文件复制到相应目录下
	 */
	private void copyMapConf(final int version, final File file,  final File fileOff2,final File tempFilecfg, final File tempFile2) {
		AsyncUtil.goAsync(new Callable<Result<Void>>() {

			@Override
			public Result<Void> call() throws Exception {
				Result<Void> r = new Result<>(Result.OK, null, null, null);
				if(!fileOff2.exists() && version >=Build.VERSION_CODES.LOLLIPOP){
					FileUtil.copyAssrert2Sd(self,"BaiduMapSDKNew/vmp/quanguogailue.dat", fileOff2.getAbsolutePath());
				}
				if(version < Build.VERSION_CODES.LOLLIPOP && !tempFile2.exists()){
					FileUtil.copyAssrert2Sd(self,"BaiduMapSDKNew/vmp/quanguogailue.dat", tempFile2.getAbsolutePath());
				}
				if(!file.exists() && version >=Build.VERSION_CODES.LOLLIPOP || version >=Build.VERSION_CODES.LOLLIPOP && file.exists() && file.length() <1000){
					FileUtil.copyAssrert2Sd(self,"BaiduMapSDKNew/vmp/DVUserdat.cfg", file.getAbsolutePath());
				}
				if(version < Build.VERSION_CODES.LOLLIPOP && !tempFilecfg.exists() || version < Build.VERSION_CODES.LOLLIPOP && tempFilecfg.exists() && tempFilecfg.length() <1000 ){
					FileUtil.copyAssrert2Sd(self,"BaiduMapSDKNew/vmp/DVUserdat.cfg", tempFilecfg.getAbsolutePath());
				}
				return r;
			}
		}, new Callback<Result<Void>>() {

			@Override
			public void onHandle(Result<Void> result) {
			}
		});
	}

	private void initoff() {
		offlion.init(131);
	}

	public static String getErrorInfoFromException(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e2) {
			return "bad getErrorInfoFromException";
		}
	}

	ArrayList<ZFRYDetailInfo> arrayList;
	UserPreference pref;

	ArrayList<Inspector> inspectors = new ArrayList<>();
	Case mCase;

	// 如果稽查临时保存的inspector.txt存在，那么把它添加到临时保存temp.txt文件
	private void initTemp() {

		File file = new File(Config.PATH + Config.FILE_NAME);
		File file2 = new File(Config.PATH + com.miu30.common.config.Config.CASE_TEMP);
		// 提取inspector.txt数据，并删除文件
		if (file.exists()) {
			inspectors.clear();
			ArrayList<Inspector> list = Store2SdUtil.getInstance(self).readInArrayObject(Config.FILE_NAME,
					new TypeToken<ArrayList<Inspector>>() {
					});
			inspectors.addAll(list);
			file.delete();
		}
		// 提取inspector.txt数据，并删除文件
		if (file2.exists()) {
			mCase = Store2SdUtil.getInstance(self).readInObject(com.miu30.common.config.Config.CASE_TEMP,new TypeToken<Case>() {
			});
			file2.delete();
		}
		// 把inspector.txt数据写入temp.txt作为长久保存
		if (inspectors.size() != 0) {
			Store2SdUtil.getInstance(MainActivity.this).addOut(inspectors.get(0), Config.FILE_NAME2,Inspector.class);
			inspectors.clear();
		}
		// case.txt数据写入cases.txt作为长久保存
		if (mCase != null) {
			Store2SdUtil.getInstance(MainActivity.this).addOut(mCase, com.miu30.common.config.Config.CASE_TEMPS,Case.class);
			mCase = null;
		}

	}

	private void queryZFRY() {
		arrayList = new ArrayList<>();
		final ZFRYDetailInfo info = new ZFRYDetailInfo();
		info.setZfzh(pref.getString("user_name", null));
		AsyncUtil.goAsync(new Callable<Result<List<ZFRYDetailInfo>>>() {

			@Override
			public Result<List<ZFRYDetailInfo>> call() throws Exception {
				return WeiZhanData.queryZFRYinfo(info);
			}
		}, new Callback<Result<List<ZFRYDetailInfo>>>() {

			@Override
			public void onHandle(Result<List<ZFRYDetailInfo>> result) {

				if (result.ok()) {
					arrayList.addAll(result.getData());
					pref.setString("zfdwmc", arrayList.get(0).getZfdwmc());
					pref.setString("sscz", arrayList.get(0).getSscz());
					pref.setString("sszd", arrayList.get(0).getSszd());
					if(!TextUtils.isEmpty(arrayList.get(0).getName())){
						pref.setString("user_name_update_info", arrayList.get(0).getName());
					}
				} else {
					// UIUtils.toast(self, result.getErrorMsg(),
					// Toast.LENGTH_LONG);
					return;
				}
			}
		});
	}

	private void initView() {
		ViewUtils.inject(self);
		title.setVisibility(View.VISIBLE);
		// iv_mine.setOnClickListener(this);
		// iv_setting.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mFragmentManager = getSupportFragmentManager();
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new HomeFragment());
		mFragments.add(new IDFragment());
		mFragments.add(new CarDistributionFragment());
		mFragments.add(new PathQueryFragment());
		mFragments.add(new MineFragment());
		switchTab(0);
	}

	// 设置定时器，每隔一段时间重新保存一下，当出现程序崩溃等意外情况将数据临时保存
	Handler mHandler;
	Runnable mRunnable;

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					loginStatus();
				}
			}
		};
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
				mHandler.postDelayed(mRunnable, 30 * 1000);
			}
		};
		new Thread(mRunnable).start();
	}

	private void loginStatus() {
		final String zfzh = new UserPreference(self).getString("user_name", "11130511");
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return UserData.updateLoginStatus(zfzh);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {

				} else {
					// UIUtils.toast(self, "", Toast.LENGTH_LONG);
				}
			}
		});
	}

	private void switchTab(int position) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		Fragment fragment = mFragments.get(position);
		if (mCurrentFragment == null) {
			transaction.add(R.id.fragment_container, fragment, fragment.getClass().getName());
		} else {
			if (mFragmentManager.findFragmentByTag(fragment.getClass().getName()) == null) {
				transaction.add(R.id.fragment_container, fragment, fragment.getClass().getName());
				transaction.hide(mCurrentFragment);
			} else {
				transaction.hide(mCurrentFragment);
				transaction.show(fragment);
			}
		}
		mCurrentFragment = fragment;
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.iv_setting: startActivity(new Intent(this,
		 * SettingActivity.class)); break;
		 */
			// case R.id.iv_mine:
			// startActivity(new Intent(this, LoginActivity.class));
			// break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
			case R.id.b1:
				switchTab(0);
				iv_mine.setVisibility(View.GONE);
				history.setVisibility(View.GONE);
				iv_title.setVisibility(View.VISIBLE);
				tv_title.setText("出租汽车稽查系统");
				break;
			case R.id.b2:
				switchTab(1);
				iv_mine.setVisibility(View.VISIBLE);
				history.setVisibility(View.VISIBLE);
				iv_title.setVisibility(View.GONE);
				tv_title.setText("证件扫描");
				break;
			case R.id.b3:
				switchTab(2);
				iv_mine.setVisibility(View.GONE);
				// iv_setting.setVisibility(View.GONE);
				history.setVisibility(View.GONE);
				iv_title.setVisibility(View.GONE);
				tv_title.setText("可疑车辆分布");
				break;
			case R.id.b4:
				switchTab(3);
				iv_mine.setVisibility(View.GONE);
				// iv_setting.setVisibility(View.GONE);
				history.setVisibility(View.GONE);
				iv_title.setVisibility(View.GONE);
				tv_title.setText("轨迹查询");
				break;
			case R.id.b5:
				iv_mine.setVisibility(View.GONE);
				// iv_setting.setVisibility(View.GONE);
				history.setVisibility(View.GONE);
				iv_title.setVisibility(View.GONE);
				tv_title.setText("我的");
				switchTab(4);
				break;
		}
	}

	long exitTime = 0;

	@Override
	public void onBackPressed() {
		// if ((System.currentTimeMillis() - exitTime) > 2000) {
		// UIUtils.toast(self, "再按一次返回键退出", Toast.LENGTH_SHORT);
		// exitTime = System.currentTimeMillis();
		// } else {
		// super.onBackPressed();
		// }
		if (!(mCurrentFragment instanceof HomeFragment)) {
			switchTab(0);
			iv_mine.setVisibility(View.GONE);
			// iv_setting.setVisibility(View.GONE);
			history.setVisibility(View.GONE);
			iv_title.setVisibility(View.VISIBLE);
			tv_title.setText("出租汽车稽查系统");
			mRadioGroup.setVisibility(View.VISIBLE);
			rb1.setChecked(true);
			return;
		}

		Windows.confirm(self, "您确定要退出登录？", new OnClickListener() {

			@Override
			public void onClick(View v) {
				clear();
				finish();
			}
		});
	}

	private void ExitStatus() {
		final ExitStatus es = new ExitStatus();
		es.setZfzh(new UserPreference(self).getString("user_name", "11130511"));
		es.setSign("app退出登录");
		AsyncUtil.goAsync(new Callable<Result<Long>>() {

			@Override
			public Result<Long> call() throws Exception {
				return UserData.updateExitStatus(es);
			}
		}, new Callback<Result<Long>>() {

			@Override
			public void onHandle(Result<Long> result) {
				if (result.ok()) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);
						}
					}, 1000);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);
						}
					}, 1000);
				}
			}
		});
	}

	private void StopCamera(){
		final String zfzh = new UserPreference(self).getString("user_name", "");
		if(zfzh.equals("")){
			return;
		}
		final BindCamera cameraQ = new BindCamera();
		cameraQ.setZfzh(zfzh);
		AsyncUtil.goAsync(new Callable<Result<List<UnboundCamera>>>() {

			@Override
			public Result<List<UnboundCamera>> call() throws Exception {
				return WeiZhanData.getExistCamera(cameraQ);
			}
		}, new Callback<Result<List<UnboundCamera>>>() {

			@Override
			public void onHandle(Result<List<UnboundCamera>> result) {
				if (result.ok()) {
					if(result.getData()==null || "".equals(result.getData())){
						return;
					}
					exitCameraList.addAll(result.getData());
					if(exitCameraList.size()>0){
						//解绑查询出的已绑定的摄像头
						for(int i=0;i<exitCameraList.size();i++){
							final BindCamera info = new BindCamera();
							info.setZfzh(zfzh);
							info.setCamera(exitCameraList.get(i).getSXT_ID());
							AsyncUtil.goAsync(new Callable<Result<String>>() {

								@Override
								public Result<String> call() throws Exception {
									return UserData.removeBindCamera(info);
								}
							}, new Callback<Result<String>>() {

								@Override
								public void onHandle(Result<String> result) {
									if (result.ok()) {
									} else {
									}
								}
							});
						}

					}
				} else {
				}
			}
		});
	}

	public void clear2(){
		MsgConfig.lat = 0.0;
		MsgConfig.lng = 0.0;
		MsgConfig.select_lat = 0.0;
		MsgConfig.select_lng = 0.0;
		disposableCancelDownload();
		stopService(new Intent(self, LocationService.class));
		stopService(new Intent(self, PushService.class));
		new PositionPreference(self).clearPreference();
		new WeiFaCheckPreference(self).clearPreference();
		new CameraPreference(self).clearPreference();
		new YuJingPreference(self).clearPreference();
		new MapPositionPreference(self).clearPreference();
		mHandler.removeCallbacks(mRunnable);
	}

	private void clear(){
		MsgConfig.lat = 0.0;
		MsgConfig.lng = 0.0;
		MsgConfig.select_lat = 0.0;
		MsgConfig.select_lng = 0.0;
		disposableCancelDownload();
		stopService(new Intent(self, LocationService.class));
		stopService(new Intent(self, PushService.class));
		stopService(new Intent(self, GeneralInformationService.class));
		stopService(new Intent(self, TCPConnectService.class));
		new PositionPreference(self).clearPreference();
		new MapPositionPreference(self).clearPreference();
		new WeiFaCheckPreference(self).clearPreference();
		new CameraPreference(self).clearPreference();
		new YuJingPreference(self).clearPreference();
		mHandler.removeCallbacks(mRunnable);
		StopCamera();
		ExitStatus();
		LocalBroadcastManager.getInstance(self).sendBroadcast(new Intent("com.miu360.taxi_check.finshAll"));
		File file = new File(Config.PATH + "inspect.png");
		if (file.exists()) {
			file.delete();
		}
	}

	//取消正在下载的地图线程
	private void disposableCancelDownload() {
		String pathRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		final File fileOff = new File(pathRoot + "/Android/data/"
				+ getPackageName() + "/files/BaiduMapSDKNew/vmp/beijing_131.dat");
		final File tempFile = new File(pathRoot + "/BaiduMapSDKNew/vmp/beijing_131.dat");
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			DownLoadUtil.disposableCancel(self,tempFile.getAbsolutePath());
		}else{
			DownLoadUtil.disposableCancel(self,fileOff.getAbsolutePath());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
