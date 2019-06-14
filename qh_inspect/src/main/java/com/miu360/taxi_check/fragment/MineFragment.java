package com.miu360.taxi_check.fragment;

import com.feidi.elecsign.mvp.ui.activity.AuthorizationActivity;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.UserData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.BindCamera;
import com.miu360.taxi_check.model.Camera;
import com.miu360.taxi_check.model.CameraQ;
import com.miu360.taxi_check.model.ExitStatus;
import com.miu360.taxi_check.model.UnboundCamera;
import com.miu360.taxi_check.ui.AboutActivity;
import com.miu360.taxi_check.ui.ChangePassWordActivity;
import com.miu360.taxi_check.ui.ChangePersonalInfoActivity;
import com.miu360.taxi_check.ui.MainActivity;
import com.miu360.taxi_check.util.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MineFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.image_header)
	private LinearLayout image_header;
	@ViewInject(R.id.change_password)
	private TextView change_password;
	@ViewInject(R.id.tv_authorization)
	private TextView tv_authorization;
	@ViewInject(R.id.inspect_camera)
	private CheckBox inspect_camera;
	@ViewInject(R.id.sounds)
	private CheckBox sounds;
	@ViewInject(R.id.shock)
	private CheckBox shock;
	@ViewInject(R.id.about)
	private TextView about;
	@ViewInject(R.id.exit_login)
	private TextView exit;
	@ViewInject(R.id.name)
	private TextView name;
	@ViewInject(R.id.header)
	private ImageView header;
	@ViewInject(R.id.toggle_one)
	private CheckBox toggle_one;

	BitmapUtils mBitMap;
	UserPreference pref;
	YuJingPreference yuJingPer;
	String[] cameraItems ;
	String[] cameraItemsSN_CODE;
	//String[] cameraItemCopy;
	ArrayList<Camera> cameraList = new ArrayList<>();
	ArrayList<UnboundCamera> exitCameraList = new ArrayList<>();
	String zfzh = "";
	int x;
	boolean isBind = true;
	boolean isCancel = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_mine, null);
		yuJingPer = new YuJingPreference(act);
		zfzh = new UserPreference(act).getString("user_name", null);
		initView(root);
		return root;
	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		/*toggle_one.setChecked(yuJingPer.getBoolean("isChecked", false));
		inspect_camera.setChecked(yuJingPer.getBoolean("isCameraChecked", false));*/
		mBitMap = new BitmapUtils(act);
		pref = new UserPreference(act);
		mBitMap.configDefaultLoadFailedImage(R.drawable.mine_image_big);
		image_header.setOnClickListener(this);
		change_password.setOnClickListener(this);
		tv_authorization.setOnClickListener(this);
		exit.setOnClickListener(this);
		about.setOnClickListener(this);
		toggle_one.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				yuJingPer.setBoolean("isChecked", isChecked);
//				选着打开预警，通过发送广播打开预警信息提示
				if (isChecked) {
					double lat =0,lon =0;
					if(MsgConfig.select_lat != 0.0 &&
							!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng)){
						lat = MsgConfig.select_lat;
						lon = MsgConfig.select_lng;
						Intent intent = new Intent();
						intent.setAction("com.miu360.camera");
						intent.putExtra("WhatPush", "2");
						intent.putExtra("camera", "");
						act.sendBroadcast(intent);
					}else if(MsgConfig.lat != 0.0 &&
							!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng)){
						lat = MsgConfig.lat;
						lon = MsgConfig.lng;
						Intent intent = new Intent();
						intent.setAction("com.miu360.camera");
						intent.putExtra("WhatPush", "2");
						intent.putExtra("camera", "");
						act.sendBroadcast(intent);
					}else{
						UIUtils.toast(act, "请先定位或者手动选择好位置，再进行预警!", Toast.LENGTH_SHORT);
						toggle_one.setChecked(false);
						return;
					}
				} else {
					Log.e("stopservice:", "stopservice");
					Intent intent = new Intent();
					intent.setAction("com.miu360.push");
					intent.putExtra("Push", false);
					act.sendBroadcast(intent);
				}
			}
		});

		//移动摄像头开启与否的相关操作
		inspect_camera.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				yuJingPer.setBoolean("isCameraChecked", isChecked);
				if(isChecked && !isCancel){
					//如果开启摄像头，将获取摄像头信息
					cameraList.clear();
					final CameraQ cameraQ = new CameraQ();
					cameraQ.setZfzh(zfzh);
					cameraQ.setStartIndex(0);
					cameraQ.setEndIndex(30);
					final MyProgressDialog pd = Windows.waiting(act);
					AsyncUtil.goAsync(new Callable<Result<List<Camera>>>() {

						@Override
						public Result<List<Camera>> call() throws Exception {
//							获取摄像头列表
							return WeiZhanData.getCamera(cameraQ);
						}
					}, new Callback<Result<List<Camera>>>() {

						@Override
						public void onHandle(Result<List<Camera>> result) {
							pd.dismiss();
							if (result.ok()) {
								cameraList.addAll(result.getData());
								cameraItems = new String[cameraList.size()];
								cameraItemsSN_CODE=new String[cameraList.size()];
								if(cameraList.size()>0){
									for (int i = 0; i < cameraList.size(); i++) {
//		                        		摄像头名称
										if(cameraList.get(i).getCAMERA_NAME()!=null){
											cameraItems[i] = cameraList.get(i).getCAMERA_NAME();
											cameraItemsSN_CODE[i]=cameraList.get(i).getSN_CODE();
										}else{
											cameraItems[i] = "未知摄像头";
										}

										//cameraItemCopy[i] = "摄像头"+cameraList.get(i).getSN_CODE();
									}
									initCamera();
								}else{
//		                        	没查询到摄像头，将是否绑定标识设为false
									cameraItems = new String[0];
									UIUtils.toast(act, "没有获取到摄像头", Toast.LENGTH_LONG);
									inspect_camera.setChecked(false);
									isBind= false;
								}
							} else {
								UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
								isBind= false;
								//inspect_camera.setChecked(false);
							}
						}
					});
					isBind = false;
				}else{
					if(isBind){
						Log.e("isBind", "isBind:1");
						//关闭摄像头将取消绑定
						Windows.confirm(act, 0, "确定关闭摄像头吗？", "关闭预警将解绑现有摄像头","确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								isCancel = false;
								//先查询现在绑定的摄像头
								final BindCamera cameraQ = new BindCamera();
								cameraQ.setZfzh(zfzh);
								final MyProgressDialog pd = Windows.waiting(act);
								AsyncUtil.goAsync(new Callable<Result<List<UnboundCamera>>>() {

									@Override
									public Result<List<UnboundCamera>> call() throws Exception {
//									根据执法账号查询绑定的摄像头
										return WeiZhanData.getExistCamera(cameraQ);
									}
								}, new Callback<Result<List<UnboundCamera>>>() {

									@Override
									public void onHandle(Result<List<UnboundCamera>> result) {
										if (result.ok()) {
											if(result.getData()==null || "".equals(result.getData())){
												isCancel = true;
												inspect_camera.setChecked(true);
												pd.dismiss();
												return;
											}
											exitCameraList.addAll(result.getData());
											if(exitCameraList.size()>0){
												//解绑查询出的已绑定的摄像头
												for(int i=0;i<exitCameraList.size();i++){
													x =i;
													final BindCamera info = new BindCamera();
													info.setZfzh(zfzh);
//												将查询到的需要解除绑定的摄像头ID，设置给INfo
													info.setCamera(exitCameraList.get(i).getSXT_ID());
													AsyncUtil.goAsync(new Callable<Result<String>>() {

														@Override
														public Result<String> call() throws Exception {
//														从绑定中删除
															return UserData.removeBindCamera(info);
														}
													}, new Callback<Result<String>>() {

														@Override
														public void onHandle(Result<String> result) {
															if (result.ok()) {
																if(x == exitCameraList.size()-1){
																	pd.dismiss();
																	UIUtils.toast(act, "解绑成功", Toast.LENGTH_SHORT);
															   /*Intent server = new Intent(act,PushCameraService.class);
															   act.stopService(server);*/
																}
															} else {
																isCancel = true;
																inspect_camera.setChecked(true);
																pd.dismiss();
																UIUtils.toast(act, "解绑失败:"+result.getErrorMsg(), Toast.LENGTH_SHORT);
															}
														}
													});
												}

											}
										} else {
											isCancel = true;
											inspect_camera.setChecked(true);
											pd.dismiss();
											UIUtils.toast(act, "解绑失败:"+result.getErrorMsg(), Toast.LENGTH_LONG);
										}
									}
								});
							}
						}, "取消", new OnClickListener() {
							@Override
							public void onClick(View v) {
								isCancel = true;
								inspect_camera.setChecked(true);
							}
						}, 0, null);
					}
				}
			}
		});
		initData();
	}

	//选择性绑定某个摄像头
	private void initCamera(){
		Windows.singleChoice(act, "请匹配一个摄像头", cameraItems, new OnDialogItemClickListener() {
			@Override
			public void dialogItemClickListener(final int position) {
				yuJingPer.setString("camera", cameraItems[position]);
//				绑定相机对象
				final BindCamera info = new BindCamera();
				info.setZfzh(zfzh);
				info.setCamera(cameraItems[position]);
				final MyProgressDialog pd = Windows.waiting(act);
				AsyncUtil.goAsync(new Callable<Result<String>>() {

					@Override
					public Result<String> call() throws Exception {
//						绑定相机，并返回绑定是否成功信息
						return UserData.bindCamera(info);
					}
				}, new Callback<Result<String>>() {

					@Override
					public void onHandle(Result<String> result) {
						pd.dismiss();
						if (result.ok()) {
							Intent intent = new Intent();
							intent.setAction("com.miu360.camera");
							intent.putExtra("WhatPush", "1");
							intent.putExtra("camera", cameraItemsSN_CODE[position]);
							act.sendBroadcast(intent);
							isBind = true;
							UIUtils.toast(act, "绑定摄像头"+cameraItems[position]+"成功", Toast.LENGTH_SHORT);
						} else {
							//inspect_camera.setChecked(false);
							isBind= false;
							UIUtils.toast(act, "绑定摄像头失败", Toast.LENGTH_SHORT);
						}
					}
				});

			}
		});
		isBind= false;
	}

	private void initData() {
//		根据执法账号下载头像
		mBitMap.display(header, Config.SERVER_ZFRY_PHOTO + pref.getString("user_name", null));
		name.setText(pref.getString("user_name_update_info", null));
	}

	@Override
	public void onClick(View v) {
		if (v == exit) {
			Windows.confirm(act, "您确定要退出执法稽查？", new OnClickListener() {

				@Override
				public void onClick(View v) {
					LocalBroadcastManager.getInstance(act).sendBroadcast(new Intent("com.miu360.taxi_check.finshAll"));
					((MainActivity)getActivity()).clear2();
					Exit();
					StopCamera();
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							File file = new File(Config.PATH + Config.FILE_NAME);
							if (file.exists()) {
								file.delete();
							}
							act.finish();
						}
					}, 200);
				}
			});
		} else if (v == change_password) {
			Intent intent = new Intent(act, ChangePassWordActivity.class);
			startActivity(intent);
		} else if(v == tv_authorization){
			Intent intent = new Intent(act, AuthorizationActivity.class);
			startActivity(intent);
		} else if (v == image_header) {
			Intent intent = new Intent(act, ChangePersonalInfoActivity.class);
			startActivity(intent);
		} else if (v == about) {
			Intent intent = new Intent(act, AboutActivity.class);
			startActivity(intent);
		}
	}
	/*private void clear(){
		MsgConfig.lat = 0.0;
		MsgConfig.lng = 0.0;
		MsgConfig.select_lat = 0.0;
		MsgConfig.select_lng = 0.0;
		act.stopService(new Intent(act, LocationService.class));
		act.stopService(new Intent(act, PushService.class));
		new PositionPreference(act).clearPreference();
		new WeiFaCheckPreference(act).clearPreference();
		new CameraPreference(act).clearPreference();
		new YuJingPreference(act).clearPreference();
		new MapPositionPreference(act).clearPreference();
	}*/

	private void StopCamera(){
		final String zfzh = new UserPreference(act).getString("user_name", "");
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

	@Override
	public void onResume() {
		super.onResume();
		mBitMap.display(header, Config.SERVER_ZFRY_PHOTO + pref.getString("user_name", null));
	}
	//	发送退出账号
	private void Exit(){
		final ExitStatus es = new ExitStatus();
		es.setZfzh(new UserPreference(act).getString("user_name", "11130511"));
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

				} else {
					//UIUtils.toast(act, "", Toast.LENGTH_LONG);
				}
			}
		});
	}


}
