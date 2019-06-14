package com.miu360.taxi_check.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.config.MsgConfig;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.data.UserData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.model.CameraDetail;
import com.miu360.taxi_check.model.VehiclePositionModex;
import com.miu360.taxi_check.ui.MainActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PushService extends Service {
	private final static String TAG = "CoreService";
	private static final String FDSERVICE_NAME = PushService.class.getName();

	public  boolean started = false;
	double lat = 0 ;
	double lon = 0;
	YuJingPreference yuJingPer;
	UserPreference pref;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		Log.e("onCreate", "push:onCreate");
		yuJingPer = new YuJingPreference(App.self);
		pref = new UserPreference(App.self);
		init();
		registerMsgReceiver();
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		startReconnect();
		return START_STICKY;
	}


	private void startReconnect() {
		//固定延迟（时间）来执行线程任务
		scheduler = Executors.newSingleThreadScheduledExecutor();
		Log.e("onCreate", "push:startReconnect");
		scheduler.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				if (!client.isConnected()) {
					connect();
				} else {
//					refreshNotification("推送服务正在运行...");
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
		connect();
	}

	private void connect() {
		Log.e("onCreate", "push:connect");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);//pref.getString("user_name",myTopic)
				} catch (Exception e) {
					e.printStackTrace();
					if (e instanceof MqttException) {
						if (((MqttException) e).getReasonCode() == 32100) {// 已连接
							return;
						}
					}
				}
			}
		}).start();
	}

	private String host = "tcp://" + Config.IP + ":1883";

	private MqttClient client;

	private String myTopic = "11130515";

	private MqttConnectOptions options;

	private ScheduledExecutorService scheduler;

	String camera = "";

	private void init() {
		try {
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(host,
					new UserPreference(App.self).getString("user_name", myTopic)+ new Random().nextInt(10000),
					new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
			// 设置回调
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) {
					Log.e("onCreate", "push:init"+"丢失");
//					refreshNotification("正在重新连接推送服务器...");
				}
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					Log.e("onCreate", "push:init"+"完成？");
					System.out.println("deliveryComplete---------" + token.isComplete());
				}

				@Override
				public void messageArrived(String topicName, final MqttMessage message) throws Exception {
					Log.e("onCreate", "push:"+topicName);
					try {
						System.out.println("arrive::" + new String(message.getPayload(), "gbk"));
						if(topicName.contains("1113")){
//							这里是预警的回调解析
							Log.e("onCreate", "push:"+new String(message.getPayload(), "gbk"));
							JSONObject obj = new JSONObject(new String(message.getPayload(), "gbk"));
							onGetMsg(obj);
						}else{
//							这里是解析摄像头
							String obj = new String(message.getPayload(), "gbk");
							Log.e("onCreate", "push:obj="+obj);
							onGetCameraMsg(obj);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	DbUtils dbUtils;

	private void onGetMsg(JSONObject data) {
		Log.e("onCreate", "push:解析1");
		try {
			final VehiclePositionModex m = new Gson().fromJson(data.toString(), new TypeToken<VehiclePositionModex>() {
			}.getType());
			if (m != null) {
//				将返回的数据存储到本地，在预警信息里面读取
				dbUtils = DbUtils.create(VehiclePositionModex.getDaoConfig());
				dbUtils.configDebug(true);
				dbUtils.save(m);

				msgHandler.post(new Runnable() {

					@Override
					public void run() {
						Intent target = new Intent(ACTION_MSG);
						target.putExtra("msg", m);
						LocalBroadcastManager.getInstance(App.self).sendBroadcast(target);
					}
				});
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void onGetCameraMsg(String data) {
		Log.e("onCreate", "push:解析2");
		try {
			final CameraDetail m = new CameraDetail();
			String[] s = data.split("\\|");
			Log.e("onCreate", "push:"+s[0]+s[1]);
			m.setVname(s[0]);
			m.setLon(Double.parseDouble(s[1]));
			m.setLat(Double.parseDouble(s[2]));
			m.setAlarmReason(s[3]);
			m.setSXT_ID(s[4]);
			if (m != null) {
//				初始化数据库
				dbUtils = DbUtils.create(CameraDetail.getDaoConfig());
				dbUtils.configDebug(true);
				dbUtils.save(m);
				msgHandler.post(new Runnable() {

					@Override
					public void run() {
						Intent target = new Intent(ACTION_MAMERA);
						target.putExtra("msg", m);
						LocalBroadcastManager.getInstance(App.self).sendBroadcast(target);
					}
				});
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static String ACTION_MSG = App.self.getPackageName() + ".push_yu_jin";
	public static String ACTION_MAMERA = App.self.getPackageName() + ".push_camera";

	private final int N_ID = 454169;

	private Handler msgHandler = new Handler();

	private void refreshNotification(String content) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

		builder.setContentTitle(getResources().getString(R.string.app_name));
		builder.setContentText(content); // 设置内容文本信息
		builder.setSmallIcon(R.drawable.ic_launcher); // 设置小图标
		builder.setWhen(0); // 设置时间
		builder.setAutoCancel(false); // 默认点击对应的notification对象后，该对象消失
		Intent appIntent = new Intent(Intent.ACTION_MAIN);
		appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		appIntent.setComponent(new ComponentName(this, MainActivity.class));
		appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
//		该方法是设置为前台服务，避免服务被杀死
		startForeground(N_ID, notification);
	}


	private String username = myTopic;


	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		Log.e("stopservice:", "stopservice"+"pushservice");
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return UserData.queryCancalYuJing(username);
			}
		}, null);
		try {
			if (dbUtils != null) {
				dbUtils.deleteAll(VehiclePositionModex.class);
				dbUtils.deleteAll(CameraDetail.class);
			}
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		try {
			if (scheduler != null)
				scheduler.shutdown();
			if (client != null) {
				if(client.isConnected()){
					Log.e("stopservice:", "stopservice"+"client");
					client.disconnect();
				}
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		unregisterMsgReceiver();
	}

	public void registerMsgReceiver() {
		IntentFilter filter = new IntentFilter("com.miu360.camera");
		registerReceiver(msgReceiver,filter);
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
			String flag = intent.getStringExtra("WhatPush");
			if("1".equals(flag)){
				Log.e("onCreate", "push:摄像头");
				camera = intent.getStringExtra("camera");
				Log.e("onCreate", "push:camera");
				if(client != null && !"".equals(camera)){
					try {
						client.subscribe(camera,1);
					} catch (MqttException e) {
						e.printStackTrace();
					}
				    /*new Thread(){
				    	public void run(){
				    		try {
								Thread.sleep(1000);
								MqttMessage m = new MqttMessage();
								m.setPayload("xixixi".getBytes());
								client.publish(camera, m);
							} catch (Exception e) {
							}
				    	}
				    }.start();*/
				}
			}else if("2".equals(flag)){
				Log.e("onCreate", "push:预警");
				//收到发送的广播，然后调用订阅接口，如果订阅接口走通，订阅该主题
				if (yuJingPer.getBoolean("isChecked", false)) {
					username = pref.getString("user_name", myTopic);
					if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
						lat = MsgConfig.select_lat;
						lon = MsgConfig.select_lng;
					}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
						lat = MsgConfig.lat;
						lon = MsgConfig.lng;
					}
					AsyncUtil.goAsync(new Callable<Result<String>>() {

						@Override
						public Result<String> call() throws Exception {
							return UserData.queryDingYueYuJing(username, yuJingPer.getString("range", "500"), lat + "",
									lon + "");
						}
					}, new Callback<Result<String>>() {

						@Override
						public void onHandle(Result<String> result) {
							if (result.ok()) {
								try {
									client.subscribe(username, 1);
//									固定时间刷新预警
									msgHandler.postDelayed(new Runnable() {
										@Override
										public void run() {
											Intent intent = new Intent("");
											intent.setAction("com.miu360.push");
											intent.putExtra("Push", true);
											sendBroadcast(intent);
										}
									}, 2000);
								} catch (MqttException e) {
									e.printStackTrace();
								}
							} else {
							}
						}
					});
				} else {

				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
