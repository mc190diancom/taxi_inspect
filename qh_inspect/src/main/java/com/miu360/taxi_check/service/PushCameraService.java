package com.miu360.taxi_check.service;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Random;
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

import com.lidroid.xutils.DbUtils;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.ui.MainActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PushCameraService extends Service {
	private final static String TAG = "CameraService";
	private static final String FDSERVICE_NAME = PushCameraService.class.getName();

	public static boolean started = false;
	double lat = 0 ;
	double log = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		Log.e("onCreate", "push:onCreate");
		init();
	}

	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		Log.e("onCreate", "push:startReconnect");
		scheduler.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				if (!client.isConnected()) {
					connect();
				} else {
					refreshNotification("推送服务正在运行...");
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	private void connect() {
		Log.e("onCreate", "push:connect");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);
					client.subscribe(myTopic, 1);
					refreshNotification("摄像头订阅服务正在运行...");
				} catch (Exception e) {
					e.printStackTrace();
					if (e instanceof MqttException) {
						if (((MqttException) e).getReasonCode() == 32100) {// 已连接
							refreshNotification("摄像头订阅服务正在运行...");
							return;
						}
					}
					refreshNotification("正在重新连接摄像头订阅服务器...");
				}
			}
		}).start();
	}

	private String host = "tcp://" + Config.IP + ":1883";

	private MqttClient client;

	private String myTopic = "3";

	private MqttConnectOptions options;

	private ScheduledExecutorService scheduler;

	private void init() {
		new Thread() {
			public void run() {
				try {
					Socket s = new Socket(Config.IP, 1883);
					System.out.println("ssss:" + s.isConnected());
					Log.e("onCreate", "push:init");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("进入", "异常");
				}
			};
		}.start();
		try {//+ new Random().nextInt(10000)
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(host,
					new YuJingPreference(App.self).getString("camera", myTopic) + new Random().nextInt(10000),
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
					Log.e("onCreate", "push:init2"+"丢失");
					refreshNotification("正在重新连接摄像头订阅服务器...");
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					Log.e("onCreate", "push:init2"+"完成？");
					System.out.println("deliveryComplete---------" + token.isComplete());
				}

				@Override
				public void messageArrived(String topicName, final MqttMessage message) throws Exception {
					try {
						System.out.println("arrive::" + new String(message.getPayload(), "gbk"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					JSONObject obj = new JSONObject(new String(message.getPayload(), "gbk"));
					Log.e("onCreate", "push:init2"+obj.toString());
					onGetMsg(obj);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	DbUtils dbUtils;

	private void onGetMsg(JSONObject data) {
		Log.e("onCreate", "push:解析");
	}

	public static String ACTION_MSG = App.self.getPackageName() + ".push_yu_jin";

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
		startForeground(N_ID, notification);
	}

	private String username;

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Log.e("onCreate", "push:onStartCommand");
		startReconnect();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("stopservice:", "stopservice"+"pushservice");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
