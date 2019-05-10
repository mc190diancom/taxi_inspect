package com.miu360.taxi_check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

public class 	BaseActivity extends FragmentActivity {
	protected BaseActivity self;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		self = this;
		IntentFilter accountExitFilter = new IntentFilter("com.miu360.taxi_check.finshAll");
		LocalBroadcastManager.getInstance(self).registerReceiver(finishReceiver, accountExitFilter);
	}

	protected void onResume() {
		super.onResume();
		// MobclickAgent.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPause(this);
	}

	/**
	 * 注销账号监听
	 */
	private BroadcastReceiver finishReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!self.getClass().getName().equals(intent.getStringExtra("ignore"))) {
				finish();
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(self).unregisterReceiver(finishReceiver);
	};
}
