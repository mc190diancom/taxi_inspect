package com.miu360.taxi_check.ui;

import java.util.concurrent.Callable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.UserData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.model.ChangeData;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends BaseActivity {

	@ViewInject(R.id.edit_feedback)
	private EditText edit_feedback;

	HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "反馈");
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("提交");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String feedback = edit_feedback.getText().toString();
				final MyProgressDialog pd = Windows.waiting(self);
				final ChangeData data = new ChangeData();
				UserPreference pref = new UserPreference(self);
				data.setZfzh(pref.getString("user_name", null));
				data.setFeedback(feedback);
				AsyncUtil.goAsync(new Callable<Result<Long>>() {

					@Override
					public Result<Long> call() throws Exception {
						return UserData.changePassWord(data);
					}
				}, new Callback<Result<Long>>() {

					@Override
					public void onHandle(Result<Long> result) {
						pd.dismiss();
						if (result.ok()) {
							UIUtils.toast(self, "提交成功", Toast.LENGTH_SHORT);
						} else {
							UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			}
		});
	}
}
