package com.miu360.taxi_check.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.WeiXiu;
import com.miu360.taxi_check.model.WeiXiuQ;
import com.miu360.taxi_check.ui.BasicCarInfoWeiXiuActivity;
import com.miu360.taxi_check.ui.BasicResultWeiXiuYehuActivity;
import com.miu360.taxi_check.util.UIUtils;

public class MotorVehicleMaintenanceFragment extends BaseFragment implements OnClickListener {
	String[] items = { "经营业户" };
	@ViewInject(R.id.Practitioners)
	private TextView Practitioners;
	@ViewInject(R.id.l3)
	private LinearLayout l3;
	@ViewInject(R.id.back)
	private Button back;
	@ViewInject(R.id.basic_query_btn)
	private Button query;
	@ViewInject(R.id.account_num)
	private EditText account_num_et;// 许可证号
	@ViewInject(R.id.user_name)
	private EditText user_name_et;// 业户名称

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_basic_info8, container, false);// 关联布局文件
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView(rootView);
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		arrayList = new ArrayList<>();
		back.setOnClickListener(this);
		Practitioners.setOnClickListener(this);
		query.setOnClickListener(this);
		chooseView(items[0]);
	}

	private void chooseView(CharSequence string) {
		if (string.equals(items[0])) {
			l3.setVisibility(View.VISIBLE);
		}
	}

	private final int COMPANY = 0;
	private int currentType = COMPANY;
	ArrayList<WeiXiu> arrayList;
	String account_num ="";
	String companyName ="";

	@Override
	public void onClick(View v) {
		if (v == Practitioners) {
			checkParNumber(Practitioners, items);
		} else if (v == back) {
			getActivity().finish();
		} else if (v == query) {
			if (currentType == COMPANY) {
				account_num = account_num_et.getText().toString();
				companyName = user_name_et.getText().toString();
				if (TextUtils.isEmpty(account_num) && TextUtils.isEmpty(companyName)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				if(account_num.length() < 3 && TextUtils.isEmpty(companyName)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(companyName.length() < 3 && TextUtils.isEmpty(account_num) ){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(TextUtils.isEmpty(account_num)){
					account_num = "";
				}
				if(TextUtils.isEmpty(companyName)){
					companyName = "";
				}
				arrayList.clear();
				final MyProgressDialog pd = Windows.waiting(act);
				final WeiXiuQ info = new WeiXiuQ();
				info.setName(companyName);
				info.setLicence(account_num);

				AsyncUtil.goAsync(new Callable<Result<List<WeiXiu>>>() {

					@Override
					public Result<List<WeiXiu>> call() throws Exception {
						return WeiZhanData.queryWeiXiuInfo(info);
					}
				}, new Callback<Result<List<WeiXiu>>>() {

					@Override
					public void onHandle(Result<List<WeiXiu>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayList.addAll(result.getData());
							if (1 == arrayList.size()) {
								Intent intent = new Intent(getActivity(), BasicCarInfoWeiXiuActivity.class);
								intent.putExtra("WeiXiuInfo", arrayList.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), BasicResultWeiXiuYehuActivity.class);
								intent.putExtra("company_name", companyName);
								intent.putExtra("account_num", account_num);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			}
		}
	}

	private void checkParNumber(final TextView tv, final String[] item) {
		Windows.singleChoice(act, "选择类型", item, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				tv.setText(item[position]);
				currentType = position;
				chooseView(items[position]);
			}
		});
	}

}
