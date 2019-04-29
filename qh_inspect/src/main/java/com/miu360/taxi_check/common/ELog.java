package com.miu360.taxi_check.common;

import com.miu360.inspect.BuildConfig;

import android.util.Log;

public class ELog {
	final String TAG = "ELog.java";

	public static void d(String TAG, String msg) {
		Log.d(TAG, ">>" + TAG + ":" + msg);
	}

	public static void dAlways(String TAG, String msg) {
		Log.d(TAG, ">>" + TAG + ":" + msg);
	}

	public static void e(String TAG, String msg) {
		Log.e(TAG, ">>" + TAG + ":" + msg);
	}

	public static void e(String TAG, String msg, Throwable tr) {
		Log.e(TAG, ">>" + TAG + ":" + msg, tr);
	}
}
