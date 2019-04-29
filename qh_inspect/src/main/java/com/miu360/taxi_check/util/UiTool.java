package com.miu360.taxi_check.util;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.ui.MainActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;


public class UiTool {
	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasJellyBeanMr1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isHoneycombTablet(Context context) {
		return hasHoneycomb() && isTablet(context);
	}

	public static int dip2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dip(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static String sign(long timestamp, Object... params) {
		StringBuffer buf = new StringBuffer();
		for (Object param : params) {
			buf.append(param.toString());
		}
		buf.append(timestamp);
		buf.append("8iu*d7&i327^&%&)");
		return MD5.md5(buf.toString());
	}

	public static boolean isAction(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static File createFilePath() {
		return createFilePath(null);
	}

	public static File createFilePath(String fileName) {
		if (fileName == null) {
			fileName = UUID.randomUUID().toString();
		}
		File dir = new File(Config.DIR_PATH);
		if (!dir.exists()) {
			dir.getPath();
			dir.mkdir();
		}
		File f = new File(Config.DIR_PATH + fileName);
		return f;
	}

	public static int getFileSize() {
		File dir = new File(Config.DIR_PATH);
		if (dir.exists()) {
			return dir.list().length;
		} else {
			return 0;
		}
	}

	public static int clearFiles() {
		File dir = new File(Config.DIR_PATH);
		if (dir.exists()) {
			for (File f : dir.listFiles()) {
				f.delete();
			}
		}
		return 0;
	}

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static void goMain(Context ctx) {
		Intent target = new Intent(ctx, MainActivity.class);
		target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ctx.startActivity(target);
	}

	public static void call(Context ctx, String tel) {
		try {
			Intent target = new Intent(Intent.ACTION_VIEW);
			target.setData(Uri.parse("tel:" + tel));
			ctx.startActivity(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void timeCounter(final TextView tv, final String textLeft,
			final String textRight, final int timeout,
			final OnCompleteListener listener, final Object obj) {
		if (timeout >= 0) {
			Handler timer = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					tv.setText(textLeft + msg.arg1 + textRight);
					if (msg.arg1 == 0) {
						removeMessages(0);
						if (listener != null) {
							listener.onComplete(obj);
						}
					} else {
						Message newMsg = obtainMessage(0, --msg.arg1, 0);
						sendMessageDelayed(newMsg, 1000);
					}
				}
			};
			int timeCount = timeout;
			Message msg = timer.obtainMessage(0, timeCount--, 0);
			timer.sendMessage(msg);
		}
	}

	public interface OnCompleteListener {
		public void onComplete(Object obj);
	}
}
