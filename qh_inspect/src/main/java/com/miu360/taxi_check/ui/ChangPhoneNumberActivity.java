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
import com.miu360.taxi_check.view.ClearEditText;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ChangPhoneNumberActivity extends BaseActivity {

	@ViewInject(R.id.tel)
	private ClearEditText tel;

	HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chang_phone_number);
		initView();
	}
	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "修改手机号码");
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("确认");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tel.length() != 11) {
					UIUtils.toast(self, "请输入正确的手机号码", Toast.LENGTH_LONG);
					return;
				} else {
					UserPreference pref = new UserPreference(self);
					final MyProgressDialog pd = Windows.waiting(self);
					final ChangeData data = new ChangeData();
					data.setZfzh(pref.getString("user_name", null));
					data.setPhone(tel.getText().toString());
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

								Intent intent = new Intent();
								intent.putExtra("phoneNumber", tel.getText().toString());
								setResult(RESULT_OK, intent);

								UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
								finish();
							} else {
								UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
							}
						}
					});
				}
			}
		});
	}
}
