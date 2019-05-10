package com.miu360.taxi_check.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.FileProvider;

import com.miu360.inspect.BuildConfig;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.MsgConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 开启服务下载升级apk
 *
 * @author Administrator
 *
 */
public class DownloadService extends IntentService {
	private static final String TAG = "DownloadService";

	private static final int NOTIFICATION_ID = 0;

	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private int mProgress = 0;
	private int mLength = 0;
	private String versioin;
	private String oldmVersion;
	// 是否取消下载
	public boolean interceptFlag = false;

	public DownloadService() {
		super("DownloadService");
	}

	/**
	 * 开启意图服务时初始调用的方法
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		MsgConfig.isUpdate = true;
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		String appName = getString(getApplicationInfo().labelRes);
		int icon = getApplicationInfo().icon;
		mBuilder.setContentTitle(appName).setSmallIcon(icon);

		// 接收 开启此服务的activity传递过来的信息
		String url = intent.getStringExtra("URL");
		mLength = intent.getIntExtra("length", 0);
		System.err.println("返回码为：" + url + "=====" + mLength);
		versioin = intent.getStringExtra("versioin");
		oldmVersion = intent.getStringExtra("oldversioin");
		fileIsdelete(oldmVersion);

		DownLoadApk(url);
	}

	/**
	 * 开启post请求下载apk
	 */
	private void DownLoadApk(final String url) {
		File file = new File(Config.PATH + versioin + ".apk");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(12 * 1000);
			connection.setReadTimeout(12 * 1000);
			// 建立实际的连接
			// connection.connect();
			System.err.println("返回码为：" + mLength + "");
			// 获取文件大小
			// (这里到时候直接从接口中拉取文件大小)
			int length = mLength;
			// 创建输入流
			is = connection.getInputStream();
			fos = new FileOutputStream(file);
			int count = 0;
			int code = connection.getResponseCode();
			System.err.println("返回码为：" + code);
			// 缓存
			byte buf[] = new byte[1024];
			// 写入到文件中
			do {
				int numread = is.read(buf);
				count += numread;
				// 计算进度条位置
				int progress = (int) (((float) count / length) * 100);
				if (mProgress != progress) {
					updateProgress(progress);
					mProgress = progress;
					Intent intent = new Intent();
					intent.setAction("com.miu360.update");
					intent.putExtra("update", progress);
					sendBroadcast(intent);
				}
				if (numread <= 0) {
					MsgConfig.isUpdate = false;
					installApk();
					mNotifyManager.cancel(NOTIFICATION_ID);
					break;
				}
				// 写入文件
				fos.write(buf, 0, numread);
			} while (MsgConfig.isUpdate);// 点击取消就停止下载.
			if (!MsgConfig.isUpdate) {
				mNotifyManager.cancel(NOTIFICATION_ID);
				if (mProgress < 99) {
					fileIsdelete(versioin);
				}
				stopSelf();
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			mNotifyManager.cancel(NOTIFICATION_ID);
			Intent intent = new Intent();
			intent.setAction("com.miu360.update");
			intent.putExtra("update", -1);
			sendBroadcast(intent);
			e.printStackTrace();
			if (mProgress < 99) {
				fileIsdelete(versioin);
			}
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 开启通知栏更新下载进度
	 */
	private void updateProgress(int progress) {
		mBuilder.setContentText(this.getString(R.string.android_auto_update_download_progress, progress))
				.setProgress(100, progress, false);
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(),
				PendingIntent.FLAG_CANCEL_CURRENT);
		mBuilder.setContentIntent(pendingintent);
		mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(Config.PATH + versioin + ".apk");

		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(DownloadService.this, BuildConfig.APPLICATION_ID+".fileProvider", apkfile);
			i.setDataAndType(contentUri, "application/vnd.android.package-archive");
		}else{
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		}
		startActivity(i);
	}

	public boolean fileIsdelete(String versioin) {

		try {
			File f = new File(Config.PATH + versioin + ".apk");
			if (!f.exists()) {
				return false;
			}
			f.delete();
		} catch (Exception e) {
			// TODO: handle exceptionreturn false;}return true;}
			return false;
		}
		return true;
	}

}
