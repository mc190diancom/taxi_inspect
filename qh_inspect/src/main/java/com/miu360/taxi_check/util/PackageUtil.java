package com.miu360.taxi_check.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author shiner
 */
public class PackageUtil {

	public static String loadApk(Context ctx, String packageName) {
		try {
			ApplicationInfo ai = ctx.getPackageManager().getPackageInfo(
					packageName, 0).applicationInfo;
			return !TextUtils.isEmpty(ai.publicSourceDir) ? ai.publicSourceDir
					: ai.sourceDir;
		} catch (Exception e) {
			return null;
		}
	}

	public static int getApkVersion(Context ctx, String path) {
		try {
			PackageInfo pi = ctx.getPackageManager().getPackageArchiveInfo(
					path, 0);
			return pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean checkApkCorrect(Context ctx, String path) {
		try {
			PackageInfo pi = ctx.getPackageManager().getPackageArchiveInfo(
					path, 0);
			return pi != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean goInstall(Application ctx, String path) {
		boolean ret = false;
		if (PackageUtil.checkApkCorrect(ctx, path)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + path),
					"application/vnd.android.package-archive");
			ctx.startActivity(intent);
			ret = true;
		}
		return ret;
	}
}
