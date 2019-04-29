package com.miu360.taxi_check.util;

import java.util.List;
import java.util.concurrent.Callable;

import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.model.Version;
import com.miu360.taxi_check.service.DownloadService;
import com.miu360.taxi_check.util.UpdateUtils.OnUpdateClicklsener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class UpdateUtils {
	Version vs;
	// 文件大小
	private int mLength = 0;

	String mVersion = "";

	private Context mContext;

	public UpdateUtils(Context mContext) {
		this.mContext = mContext;
		queryVersion();
	}

	public void queryVersion() {
		if (MsgConfig.isUpdate) {
//			UIUtils.toast(mContext, "最新版本正在下载中，请稍后~~", Toast.LENGTH_SHORT);
			return;
		}

		// 获取从接口返回的版本号对比，如果不一样提示更新操作
		vs = new Version();
		AsyncUtil.goAsync(new Callable<Result<List<Version>>>() {

			@Override
			public Result<List<Version>> call() throws Exception {

				return UserData.queryVersion();
			}
		}, new Callback<Result<List<Version>>>() {

			@Override
			public void onHandle(Result<List<Version>> result) {

				if (result.ok()) {
					int version = 0;
					try {
						version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					if ("[]".equals(result.getData().toString()) || result.getData() == null) {
						return;
					}
					vs = result.getData().get(0);
					mVersion = vs.getVERSION();
					mLength = Integer.parseInt(result.getData().get(0).getSIZES());
					Log.e("zar", "mVersion" + mVersion + "," + version);

					if(vs.getVERSION().equals("0.9.8")){//为了兼容之前传错的版本号格式
						Onupdate.onUpdate(false, true, null, 0, null);
						return;
					}
					if (version<Integer.valueOf(vs.getVERSION())) {
						Onupdate.onUpdate(true, true, vs.getLOG(), mLength, mVersion);
						//Onupdate.onUpdate(false, true, null, 0, null);
					} else {
						Onupdate.onUpdate(false, true, null, 0, null);
					}
				} else {

					Onupdate.onUpdate(false, false, null, 0, null);
				}

			}
		});
	}

	public void showDownload(String versioin) {
		String url = Config.SERVER_SPECIAL + "?type=downloadApk&version=" + versioin;
		Intent myIntent = new Intent(mContext, DownloadService.class);
		myIntent.putExtra("URL", url);
		myIntent.putExtra("length", mLength);
		mContext.startService(myIntent);
	}

	private OnUpdateClicklsener Onupdate;

	public void setOnUpdateCliclenser(OnUpdateClicklsener Onupdate) {
		this.Onupdate = Onupdate;
	}

	public interface OnUpdateClicklsener {
		void onUpdate(boolean isUpdate, boolean isqueryOk, String LOG, int length, String VSCODE);
	}
}
