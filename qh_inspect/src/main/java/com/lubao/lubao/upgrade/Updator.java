package com.lubao.lubao.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.DException;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.CommonData;
import com.miu360.taxi_check.util.PackageUtil;
import com.miu360.taxi_check.util.SDInfo;

/**
 * @author shiner
 */
public class Updator {
	final int UPDATE_MODE_DELTA = 0;
	final int UPDATE_MODE_FULL = 1;

	private static Updator self;

	private boolean updating = false;
	final private String FLAG = "com.flr.flr";
	private static String storePath = "";

	private static Application ctx;

	public static synchronized Updator getInstance(Application app) {
		if (self == null) {
			self = new Updator();
			ctx = app;
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 没有内存卡,设置下载位置为本程序缓存位置
				storePath = ctx.getFilesDir().getAbsolutePath() + "/";
			} else {
				storePath = Environment.getExternalStorageDirectory() + "/" + ctx.getPackageName();
			}
		}
		return self;
	}

	private Updator() {
	}

	public void start(boolean notice) {
		if (updating) {
			if (notice) {
				Toast.makeText(ctx, "正在升级中", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		updating = true;
		update(ctx, notice);
	}

	private int getVersion(Context ctx) {
		int versionCode = 0;
		try {
			versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	private void update(final Application ctx, final boolean notice) {
		AsyncUtil.goAsync(new Callable<Result<UpdateBean>>() {

			@Override
			public Result<UpdateBean> call() throws Exception {
				return CommonData.update();
			}
		}, new Callback<Result<UpdateBean>>() {

			@Override
			public void onHandle(Result<UpdateBean> result) {
				final UpdateBean update = result.getData();
				if (result.ok()) {
					int localVersion = 0;
					try {
						localVersion = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}

					if (localVersion < update.version) {
						// 检查是否上次下载过,但没安装
						String savedPath = getPath(ctx, getFileName(update.fullUrl));
						if (PackageUtil.checkApkCorrect(ctx, savedPath) && localVersion == update.version) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(Uri.parse("file://" + savedPath),
									"application/vnd.android.package-archive");
							ctx.startActivity(intent);
							updating = false;
							return;
						}

						String msg = null;
						if (update.force) {
							msg = "此版本有重要更新，不升级将影响使用\r\n" + update.msg;
						} else {
							msg = update.msg;
						}
						Windows.confirm(ctx, "升级提醒", msg, "升级", new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								downloadUpdate(ctx, update);
							}
						}, "取消", new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								updating = false;
								if (update.force) {
									System.exit(0);
								}
							}
						}, 0, null);
					} else {
						updating = false;
						if (notice) {
							Intent intent = new Intent();
							intent.setAction(FLAG);
							intent.putExtra("Data", "已经是最新版本无需更新");
							LocalBroadcastManager.getInstance(App.self).sendBroadcast(intent);
							Toast.makeText(ctx, "已经是最新版本", Toast.LENGTH_LONG).show();
						}
					}
				} else {
					updating = false;
					if (notice) {
						Toast.makeText(ctx, "网络异常：" + result.getErrorMsg(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	private void downloadUpdate(final Application ctx, final UpdateBean update) {
		String url = update.fullUrl;
		int mode = UPDATE_MODE_FULL;
		if (!TextUtils.isEmpty(update.patchUrl)) {
			mode = UPDATE_MODE_DELTA;
			url = update.patchUrl;
		}
		final int finalMode = mode;
		String name;
		if (finalMode == UPDATE_MODE_FULL) {
			name = getFileName(update.fullUrl);
		} else {
			name = getFileName(update.patchUrl);
		}
		final String path = preparePath(ctx, name);
		final DownloadInfo dInfo = new DownloadInfo();
		dInfo.setFilename(name);
		dInfo.setUrl(url);
		dInfo.setDestPath(path);

		final AlertDialog.Builder adb = new Builder(ctx).setTitle("下载中");
		View root = LayoutInflater.from(ctx).inflate(R.layout.upgrade_progress, null);
		final ProgressBar pb = (ProgressBar) root.findViewById(R.id.download_progress);
		final TextView state = (TextView) root.findViewById(R.id.download_state);
		adb.setView(root);
		adb.setPositiveButton("停止", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dInfo.setState(DownloadInfo.PAUSE);
				if (update.force) {
					System.exit(0);
				}
			}
		});

		pb.setIndeterminate(true);
		adb.setCancelable(false);
		final AlertDialog pd = adb.create();

		TextView tip = (TextView) root.findViewById(R.id.download_tip);
		tip.setMovementMethod(LinkMovementMethod.getInstance());
		SpannableString ss = new SpannableString(ctx.getString(R.string.update_tip_lable));
		ss.setSpan(new URLSpan(update.fullUrl) {
			@Override
			public void onClick(View widget) {
				Uri uri = Uri.parse(getURL());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.putExtra(Browser.EXTRA_APPLICATION_ID, ctx.getPackageName());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(intent);

				pd.dismiss();
				dInfo.setState(DownloadInfo.PAUSE);
				if (update.force) {
					System.exit(0);
				}
			}
		}, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.GRAY), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tip.setText(ss);

		pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		pd.show();
		download(dInfo, path, new DownloadListener() {

			@Override
			public void update(final DownloadInfo dInfo) {
				switch (dInfo.getState()) {
					case DownloadInfo.PREPARE:
						state.setText("连接资源中...");
						break;
					case DownloadInfo.DOWNLOADING:
						String msg = "已下载" + SDInfo.showSize(dInfo.getCurrentLength());
						if (dInfo.getFullLength() > 0) {
							pb.setIndeterminate(false);
							pb.setMax((int) dInfo.getFullLength());
							pb.setProgress((int) dInfo.getCurrentLength());
							msg += "/共" + SDInfo.showSize(dInfo.getFullLength());
						} else {
							pb.setIndeterminate(true);
							msg += "/未知";
						}
						state.setText(msg);
						break;
					case DownloadInfo.PAUSE:
						updating = false;
						break;
					case DownloadInfo.ERROR:
						state.setText("下载出错");
						updating = false;
						break;
					case DownloadInfo.COMPLETE:
						pb.setIndeterminate(true);
						state.setText("下载完成，准备安装...");
						if (finalMode == UPDATE_MODE_FULL) {
							boolean go = PackageUtil.goInstall(ctx, path);
							if (!go) {
								state.setText("安装失败");
							}
							updating = false;
							pd.dismiss();
						} else {
							new Thread() {
								public void run() {
									String oldFile = PackageUtil.loadApk(ctx, ctx.getPackageName());
									String newFile = preparePath(ctx, getFileName(update.fullUrl));

									boolean b = patchApk(ctx, oldFile, newFile, dInfo.getDestPath());
									if (b) {
										boolean go = PackageUtil.goInstall(ctx, newFile);
										if (!go) {
											state.setText("安装失败");
										}
										updating = false;
										// pd.dismiss();
									} else {
										Log.d("", "合并失败");
										// 合并失败，下载完整包
										// reTip(ctx, update);
									}
								};
							}.start();
						}
						break;

					default:
						break;
				}
			}
		});

	}

	private String getFileName(String url) {
		String name = null;
		int index = url.lastIndexOf("/");
		if (index >= 0) {
			name = url.substring(index + 1);
			if (!name.endsWith(".apk")) {
				name += ".apk";
			}
		}
		if (name == null) {
			name = UUID.randomUUID().toString().substring(0, 5) + ".apk";
		}
		return name;
	}

	private String preparePath(Context ctx, String name) {
		File file = null;
		if (!checkSpace(ctx, true)) {
			return null;
		}

		if (SDInfo.checkSdcard(10)) {// SD卡
			file = new File(storePath + name);
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				if (!dir.mkdirs()) {// 创建文件夹失败
					updating = false;
					return null;
				}
			}

			if (file.exists()) {
				if (!file.delete()) {
					updating = false;
					return null;
				} else {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						updating = false;
						return null;
					}
				}
			} else {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					updating = false;
					return null;
				}
			}

		} else {// 内存
			try {
				file = new File(ctx.getFilesDir().getAbsolutePath() + "/" + name);
				file.delete();
				SDInfo.saveInMemory(ctx, name).close();
			} catch (Exception e) {
				e.printStackTrace();
				updating = false;
				return null;
			}
		}
		return file.getAbsolutePath();
	}

	private boolean checkSpace(final Context ctx, boolean notifiy) {
		int state = -1;
		if (SDInfo.hasSDCard()) {// 有sd卡，检查sd卡空间
			if (SDInfo.hasEnoughAvailableSize(10)) {// sd卡空间足够
				return true;
			} else {// sd卡空间不足
				state = 0;
			}
		} else {// 无卡
			state = 1;
		}

		// 无卡或卡空间不足，检查手机空间
		if (SDInfo.hasEnoughAvailableSizeInternal(10)) {// 手机空间足够
			return true;
		}
		if (notifiy) {
			String msg0 = null;
			if (state == 0) {
				msg0 = "内存卡空间不足，不能下载";
			} else if (state == 1) {
				msg0 = "手机空间不足，不能下载";
			}
			final String msg = msg0;
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Windows.alert(ctx, msg);
				}
			});
		}
		return false;
	}

	private String getPath(Context ctx, String name) {
		String path = null;
		if (!checkSpace(ctx, true)) {
			return null;
		}

		if (SDInfo.checkSdcard(10)) {// SD卡
			path = storePath + name;
		} else {// 内存
			try {
				path = ctx.getFilesDir().getAbsolutePath() + "/" + name;
			} catch (Exception e) {
				e.printStackTrace();
				updating = false;
				return null;
			}
		}
		return path;
	}

	private void download(final DownloadInfo dInfo, final String path, final DownloadListener listener) {
		new Thread() {
			public void run() {
				try {
					OutputStream out = new FileOutputStream(path);
					dInfo.setState(DownloadInfo.PREPARE);
					updateOnMainThread(dInfo, listener);

					HttpGet req = new HttpGet(dInfo.getUrl());
					HttpClient client = new DefaultHttpClient();
					HttpResponse res = client.execute(req);
					if (res.getStatusLine().getStatusCode() != 200) {
						throw new DException("连接资源失败：" + res.getStatusLine().getStatusCode());
					}

					InputStream in = res.getEntity().getContent();
					long length = res.getEntity().getContentLength();

					if (DownloadInfo.PREPARE != dInfo.getState()) {
						throw new DException("下载终止0");
					}

					dInfo.setCurrentLength(0);
					dInfo.setFullLength(length);
					dInfo.setState(DownloadInfo.DOWNLOADING);
					updateOnMainThread(dInfo, listener);

					byte[] buf = new byte[4096];
					int onceReadLength = -1;
					int allReadLength = 0;
					while ((onceReadLength = in.read(buf)) != -1 && dInfo.getState() == DownloadInfo.DOWNLOADING) {
						out.write(buf, 0, onceReadLength);
						allReadLength += onceReadLength;

						dInfo.setCurrentLength(allReadLength);
						dInfo.setState(DownloadInfo.DOWNLOADING);
						updateOnMainThread(dInfo, listener);
					}
					if (dInfo.getState() == DownloadInfo.DOWNLOADING) {
						dInfo.setState(DownloadInfo.COMPLETE);
						updateOnMainThread(dInfo, listener);
					} else {
						throw new DException("下载终止1");
					}
				} catch (Throwable e) {
					e.printStackTrace();
					dInfo.setState(DownloadInfo.ERROR);
					updateOnMainThread(dInfo, listener);
				}

			};
		}.start();
	}

	private Handler handler = new Handler(Looper.getMainLooper());

	private class UpdateThread implements Runnable {
		public DownloadInfo dInfo;
		public DownloadListener listener;

		@Override
		public void run() {
			listener.update(dInfo);
		}
	};

	private UpdateThread updateThread = new UpdateThread();

	private void updateOnMainThread(final DownloadInfo dInfo, final DownloadListener listener) {
		updateThread.dInfo = dInfo;
		updateThread.listener = listener;
		handler.removeCallbacks(updateThread);
		handler.post(updateThread);
	}

	private static boolean patchApk(Context ctx, String oldFile, String newFile, String patch) {
		try {
			int result = patch(oldFile, newFile, patch);
			return result == 0 && PackageUtil.checkApkCorrect(ctx, newFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private static native int patch(String oldFile, String newFile, String patch);

	static {
		System.loadLibrary("delta_update");
	}
}
