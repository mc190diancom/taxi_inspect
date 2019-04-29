package com.miu360.taxi_check.ui;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.miu360.taxi_check.util.MD5;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassWordActivity extends BaseActivity {

	@ViewInject(R.id.old_password)
	private TextView old_password;// 旧密码
	@ViewInject(R.id.new_password)
	private TextView new_password;// 新密码
	@ViewInject(R.id.new_password_again)
	private TextView new_password_again;// 确认新密码

	HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pass_word);
		initView();

	}

	UserPreference pre;
	Pattern pattern;
	Matcher matcher;
	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "修改密码");
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("保存");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String oldPass = old_password.getText().toString();
				String newPassAgain = new_password_again.getText().toString();
				final String newPass = new_password.getText().toString();

				String regEx = "^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{1,16}$";
				// 编译正则表达式
				pattern = Pattern.compile(regEx);

				if (TextUtils.isEmpty(oldPass) && TextUtils.isEmpty(newPassAgain) && TextUtils.isEmpty(newPass)) {
					UIUtils.toast(self, "密码不能为空，请重新输入", Toast.LENGTH_LONG);
					return;
				}
				matcher=pattern.matcher(newPassAgain);
				// 字符串是否与正则表达式相匹配
				if(!matcher.matches()){
					UIUtils.toast(self, "密码格式错误，请重新输入", Toast.LENGTH_SHORT);
					return;
				}
				pre = new UserPreference(self);
				String passWordCheck = pre.getString("user_psw", null);
				if (!oldPass.equals(passWordCheck)) {
					UIUtils.toast(self, "旧密码不正确", Toast.LENGTH_LONG);
					return;
				}

				if (!newPass.equals(newPassAgain)) {
					UIUtils.toast(self, "新密码请输入一致", Toast.LENGTH_LONG);
					return;
				}
				final MyProgressDialog pd = Windows.waiting(self);

				final ChangeData data = new ChangeData();
				data.setZfzh(pre.getString("user_name", null));
				data.setPassword(MD5.md5(newPass));
				data.setOld_pwd(MD5.md5(oldPass));
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
							//pre.setString("user_psw", newPass);
							UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
							finish();
						} else {
							UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});
			}
		});
	}

}
