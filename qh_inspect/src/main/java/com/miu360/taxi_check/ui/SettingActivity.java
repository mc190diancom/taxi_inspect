package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.exit)
	private TextView exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "设置");
		exit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == exit) {

			Windows.confirm(self, "您确定要退出登录？", new OnClickListener() {

				@Override
				public void onClick(View v) {
					LocalBroadcastManager.getInstance(self).sendBroadcast(new Intent("com.miu360.taxi_check.finshAll"));

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							Intent intent = new Intent(self, LoginActivity.class);
							startActivity(intent);
						}
					}, 200);

				}
			});

		}
	}
}
