package com.miu360.taxi_check.thread;

import java.io.File;

import com.lidroid.xutils.BitmapUtils;
import com.miu360.taxi_check.data.HttpRequest;

import android.util.Log;

public class PhotoImageThread extends Thread {

	String url;
	String path;
	String postPath;
	BitmapUtils mBitMap;

	public PhotoImageThread(String url, String path, String postPath, BitmapUtils mBitMap) {
		this.path = path;
		this.url = url;
		this.mBitMap = mBitMap;
		this.postPath = postPath;
	}

	@Override
	public void run() {
		File file = new File(path);
		try {
			String result = HttpRequest.uploadFile(url, file);
			if(result.equals("true")){
				mBitMap.clearCache(postPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
