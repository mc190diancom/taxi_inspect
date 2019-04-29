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
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.ZhuLinYeHuInfo;
import com.miu360.taxi_check.model.ZuLinInfo;
import com.miu360.taxi_check.ui.CarRentaCarDetailInfoActivity;
import com.miu360.taxi_check.ui.CarRentaCarInfolActivity;
import com.miu360.taxi_check.ui.CarRentaCompanyDetailInfoActivity;
import com.miu360.taxi_check.ui.CarRentaCompanyInfoActivity;
import com.miu360.taxi_check.util.UIUtils;

public class CarRentalFragment extends BaseFragment implements OnClickListener {
	String[] items = { "营运车辆", "经营业户" };
	@ViewInject(R.id.Practitioners)
	private TextView Practitioners;
	@ViewInject(R.id.l2)
	private LinearLayout l2;
	@ViewInject(R.id.l3)
	private LinearLayout l3;
	@ViewInject(R.id.back)
	private Button back;
	@ViewInject(R.id.basic_query_btn)
	private Button query;
	@ViewInject(R.id.number_car_three)
	private EditText number_car_three_et;// 车牌号
	@ViewInject(R.id.company_name)
	private EditText company_name_et;// 单位名称
	@ViewInject(R.id.beian_company)
	private EditText beian_company;// 企业备案号
	@ViewInject(R.id.user_name)
	private EditText user_name_et;// 业户名称

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_basic_info7, container, false);// 关联布局文件
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
		back.setOnClickListener(this);
		Practitioners.setOnClickListener(this);
		query.setOnClickListener(this);
		chooseView(items[0]);
		ChooseInputType input = new ChooseInputType();
		input.init(company_name_et);
		input.init(user_name_et);
	}

	private void chooseView(CharSequence string) {
		if (string.equals(items[0])) {
			l2.setVisibility(View.VISIBLE);
			l3.setVisibility(View.GONE);
		} else if (string.equals(items[1])) {
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.VISIBLE);
		}
	}

	private final int CAR = 0;
	private int currentType = CAR;
	ArrayList<ZuLinInfo> arrayListVehicle;
	ArrayList<ZhuLinYeHuInfo> arrayListCompany;
	String name;
	String number_card;
	String ID_number_card;
	String number_car_three;
	String operating_number;
	String company_name;
	String beian_number;
	String companyName;
	ZuLinInfo infoCar;
	ZhuLinYeHuInfo infoCompany;

	@Override
	public void onClick(View v) {
		if (v == Practitioners) {
			checkParNumber(Practitioners, items);
		} else if (v == back) {
			getActivity().finish();
		} else if (v == query) {
			if (currentType == CAR) {

				number_car_three = number_car_three_et.getText().toString();
				company_name = company_name_et.getText().toString();
				if (TextUtils.isEmpty(number_car_three) && TextUtils.isEmpty(company_name)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
//				if (!TextUtils.isEmpty(number_car_three)) {
//					String first = number_car_three.charAt(0) + "";
//					if (!(Pattern.compile("[a-zA-Z]").matcher(first).matches())) {// 字母
//						UIUtils.toast(act, "必须输入首字母", Toast.LENGTH_SHORT);
//						return;
//					}
//				}
				arrayListVehicle = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoCar = new ZuLinInfo();
				infoCar.setVname(number_car_three.toUpperCase());
				infoCar.setCompany(company_name);
				infoCar.setStartIndex(0);
				infoCar.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<ZuLinInfo>>>() {

					@Override
					public Result<List<ZuLinInfo>> call() throws Exception {
						return WeiZhanData.queryZhulinCarInfo(infoCar);
					}
				}, new Callback<Result<List<ZuLinInfo>>>() {

					@Override
					public void onHandle(Result<List<ZuLinInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListVehicle.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {

								Intent intent = new Intent(getActivity(), CarRentaCarDetailInfoActivity.class);
								intent.putExtra("ZuLinInfo", arrayListVehicle.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), CarRentaCarInfolActivity.class);
								intent.putExtra("number_car", number_car_three.toUpperCase());
								intent.putExtra("company_name", company_name);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});
			} else {
				// UIUtils.toast(act, "没有数据，暂时未开发", Toast.LENGTH_SHORT);
				companyName = user_name_et.getText().toString();
				beian_number = beian_company.getText().toString();
				if (TextUtils.isEmpty(companyName) && TextUtils.isEmpty(beian_number)) {
					UIUtils.toast(act, "请填写业户名称", Toast.LENGTH_SHORT);
					return;
				}
				// if (!TextUtils.isEmpty(number_car_three)) {
				// String first = number_car_three.charAt(0) + "";
				// if (!(Pattern.compile("[a-zA-Z]").matcher(first).matches()))
				// {// 字母
				// UIUtils.toast(act, "必须输入首字母", Toast.LENGTH_SHORT);
				// return;
				// }
				// }
				arrayListCompany = new ArrayList<>();
				final MyProgressDialog d = Windows.waiting(act);
				infoCompany = new ZhuLinYeHuInfo();
				infoCompany.setCompanyName(companyName);
				infoCompany.setQiYeBeiAn(beian_number);
				infoCompany.setStartIndex(0);
				infoCompany.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<ZhuLinYeHuInfo>>>() {

					@Override
					public Result<List<ZhuLinYeHuInfo>> call() throws Exception {
						return WeiZhanData.queryZhuLinYehuInfo(infoCompany);
					}
				}, new Callback<Result<List<ZhuLinYeHuInfo>>>() {

					@Override
					public void onHandle(Result<List<ZhuLinYeHuInfo>> result) {
						d.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListCompany.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {

								Intent intent = new Intent(getActivity(), CarRentaCompanyDetailInfoActivity.class);
								intent.putExtra("ZuLInYeHuInfo", arrayListCompany.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), CarRentaCompanyInfoActivity.class);
								intent.putExtra("companyName", companyName);
								intent.putExtra("beian_number", beian_number);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != getActivity().RESULT_OK) {
			return;
		}
		if (requestCode == 1 || requestCode == 2) {
			Driver d = (Driver) data.getSerializableExtra("result");
		}
	}

}
