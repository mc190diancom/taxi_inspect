package com.miu360.taxi_check.fragment;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.client.android.CaptureActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.model.Driver;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.ui.BasicCarInforActivity;
import com.miu360.taxi_check.ui.BasicResultActivity;
import com.miu360.taxi_check.ui.BasicResultCarActivity;
import com.miu360.taxi_check.ui.BasicResultCompanyActivity;
import com.miu360.taxi_check.ui.CompanyInfoActivity;
import com.miu360.taxi_check.ui.DriverInfoActivity;
import com.miu360.taxi_check.ui.QRcodeActivity;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaxiBasicInfoFragment extends BaseFragment implements OnClickListener {

	String[] items = { "营运车辆","从业人员",  "经营业户" };
	@ViewInject(R.id.Practitioners)
	private TextView Practitioners;
	@ViewInject(R.id.l1)
	private LinearLayout l1;
	@ViewInject(R.id.l2)
	private LinearLayout l2;
	@ViewInject(R.id.l3)
	private LinearLayout l3;
	@ViewInject(R.id.back)
	private Button back;
	@ViewInject(R.id.basic_query_btn)
	private Button query;
	@ViewInject(R.id.saomiao_one)
	private ImageButton saoMiaoOne;
	@ViewInject(R.id.saomiao_two)
	private ImageButton saoMiaoTwo;
	/*@ViewInject(R.id.saomiao_three)
	private ImageButton saoMiaoThree;*/
	@ViewInject(R.id.name)
	private EditText name_et;// 姓名
	@ViewInject(R.id.number_card)
	private EditText number_card_et;// 监督卡号
	@ViewInject(R.id.ID_number_card)
	private EditText ID_number_card_et;// 身份证号
	@ViewInject(R.id.number_car_three)
	private EditText number_car_three_et;// 车牌号
	@ViewInject(R.id.operating_number)
	private EditText operating_number_et;// 营运证号
	@ViewInject(R.id.company_name)
	private EditText company_name_et;// 单位名称
	@ViewInject(R.id.account_num)
	private EditText account_num_et;// 许可证号
	@ViewInject(R.id.user_name)
	private EditText user_name_et;// 业户名称
	@ViewInject(R.id.carNumber_one)
	private TextView carNumber_one;// 业户名称
	String[] itemsOne = {"京","沪","津","渝","黑","吉","辽","蒙","冀","新","甘","青","陕","宁","豫","鲁","晋","皖", "鄂","湘","苏","川","黔","滇","桂","藏","浙","赣","粤","闽","台","琼","港","澳"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_basic_info2, container, false);// 关联布局文件
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		initView(rootView);
		return rootView;
	}

	private BroadcastReceiver getIdCardReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String name = null;
			if (data != null && !TextUtils.isEmpty((name = data.getStringExtra("id_card")))) {
				ID_number_card_et.setText(name);
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(act).unregisterReceiver(getIdCardReceiver);
	}

	private void initViewPager() {

	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		new HeaderHolder().init(act, "基础信息");
		back.setOnClickListener(this);
		carNumber_one.setOnClickListener(this);
		saoMiaoOne.setOnClickListener(this);
		saoMiaoTwo.setOnClickListener(this);
		/*saoMiaoThree.setOnClickListener(this);*/
		Practitioners.setOnClickListener(this);
		query.setOnClickListener(this);
		chooseView(items[0]);
		ChooseInputType input = new ChooseInputType();
		input.init(name_et);
		input.init(company_name_et);
		input.init(user_name_et);
	}

	private void chooseView(CharSequence string) {
		if (string.equals(items[0])) {
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.VISIBLE);
			l3.setVisibility(View.GONE);
		} else if (string.equals(items[1])) {
			l1.setVisibility(View.VISIBLE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
		} else if (string.equals(items[2])) {
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.VISIBLE);
		}
	}

	private final int PEOPLE = 1;
	private final int CAR = 0;
	private final int COMPANY = 2;
	private int currentType = CAR;
	ArrayList<DriverInfo> arrayListDriver;
	ArrayList<VehicleInfo> arrayListVehicle;
	ArrayList<CompanyInfo> arrayListCompany;
	String name;
	String number_card;
	String ID_number_card;
	String number_car_three;
	String operating_number;
	String company_name;
	String account_num;
	String companyName;
	String ss;

	@Override
	public void onClick(View v) {
		if (v == Practitioners) {
			checkParNumber(Practitioners, items);
		} else if (v == back) {
			getActivity().finish();
		} else if (v == saoMiaoOne) {
			Intent intent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(intent, 9);
		} else if (v == saoMiaoTwo) {
			Intent intent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(intent, 9);
		} else if (v == carNumber_one) {
			Windows.singleChoice(act, "选择车牌", itemsOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					carNumber_one.setText(itemsOne[position]);
				}
			});
		} else if (v == query) {
			if (currentType == PEOPLE) {
				name = name_et.getText().toString();
				try {
					ss = URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				number_card = number_card_et.getText().toString();
				ID_number_card = ID_number_card_et.getText().toString();
				if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number_card) && TextUtils.isEmpty(ID_number_card)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				if(ID_number_card.length() < 3 && TextUtils.isEmpty(name)
						&& TextUtils.isEmpty(number_card)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(number_card.length() < 3 && TextUtils.isEmpty(name)
						&& TextUtils.isEmpty(ID_number_card)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				final MyProgressDialog pd = Windows.waiting(act);
				arrayListDriver = new ArrayList<>();
				final DriverInfo info = new DriverInfo();
				info.setDriverName(name);
				info.setJianduNumber(number_card);
				info.setId(ID_number_card);
				info.setStartIndex(0);
				info.setEndIndex(20);
				AsyncUtil.goAsync(new Callable<Result<List<DriverInfo>>>() {

					@Override
					public Result<List<DriverInfo>> call() throws Exception {
						return WeiZhanData.queryDriverInfo(info);
					}
				}, new Callback<Result<List<DriverInfo>>>() {

					@Override
					public void onHandle(Result<List<DriverInfo>> result) {
						pd.dismiss();

						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListDriver.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), DriverInfoActivity.class);
								intent.putExtra("DriverInfo", arrayListDriver.get(0));
								startActivity(intent);
							} else {
								Bundle bundle = new Bundle();
								bundle.putSerializable("driverList",arrayListDriver);
								Intent intent = new Intent(getActivity(), BasicResultActivity.class);
								intent.putExtras(bundle);
								intent.putExtra("name", name);
								intent.putExtra("number_card", number_card);
								intent.putExtra("ID_number_card", ID_number_card);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});
			} else if (currentType == CAR) {
				number_car_three =  carNumber_one.getText().toString()+ number_car_three_et.getText().toString();
				operating_number = operating_number_et.getText().toString();
				company_name = company_name_et.getText().toString();
				if (TextUtils.isEmpty(number_car_three) && TextUtils.isEmpty(operating_number)
						&& TextUtils.isEmpty(company_name)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				if(number_car_three.length() < 3 && TextUtils.isEmpty(operating_number)
						&& TextUtils.isEmpty(company_name)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(operating_number.length() < 3 && TextUtils.isEmpty(number_car_three)
						&& TextUtils.isEmpty(company_name)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(company_name.length() < 3 && TextUtils.isEmpty(operating_number)
						&& TextUtils.isEmpty(number_car_three)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				final MyProgressDialog pd = Windows.waiting(act);
				arrayListVehicle = new ArrayList<>();
				final VehicleInfo info = new VehicleInfo();
				info.setVname(number_car_three.toUpperCase());
				info.setYingyunNumber(operating_number);
				info.setCompany(company_name);
				info.setStartIndex(0);
				info.setEndIndex(20);
				AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

					@Override
					public Result<List<VehicleInfo>> call() throws Exception {
						return WeiZhanData.queryVehicleInfo(info);
					}
				}, new Callback<Result<List<VehicleInfo>>>() {

					@Override
					public void onHandle(Result<List<VehicleInfo>> result) {
						pd.dismiss();

						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListVehicle.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), BasicCarInforActivity.class);
								intent.putExtra("VehicleInfo", arrayListVehicle.get(0));
								startActivity(intent);
							} else {
								Bundle bundle = new Bundle();
								bundle.putSerializable("vehicleList",arrayListVehicle);
								Intent intent = new Intent(getActivity(), BasicResultCarActivity.class);
								intent.putExtras(bundle);
								intent.putExtra("number_car", number_car_three.toUpperCase());
								intent.putExtra("operating_number", operating_number);
								intent.putExtra("company_name", company_name);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			} else {
				account_num = account_num_et.getText().toString();
				companyName = user_name_et.getText().toString();
				if (TextUtils.isEmpty(account_num) && TextUtils.isEmpty(companyName)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				if(account_num.length() < 2 && TextUtils.isEmpty(companyName)){
					UIUtils.toast(act, "至少输入2位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				if(companyName.length() < 3 && TextUtils.isEmpty(account_num)){
					UIUtils.toast(act, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
					return;
				}
				final MyProgressDialog d = Windows.waiting(act);
				arrayListCompany = new ArrayList<>();
				final CompanyInfo info = new CompanyInfo();
				info.setLhh(account_num);
				info.setCompanyName(companyName);
				info.setStartIndex(0);
				info.setEndIndex(20);
				AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {

					@Override
					public Result<List<CompanyInfo>> call() throws Exception {
						return WeiZhanData.queryYeHuInfo(info);
					}
				}, new Callback<Result<List<CompanyInfo>>>() {

					@Override
					public void onHandle(Result<List<CompanyInfo>> result) {
						d.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListCompany.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), CompanyInfoActivity.class);
								intent.putExtra("CompanyInfo", arrayListCompany.get(0));
								startActivity(intent);
							} else {
								Bundle bundle = new Bundle();
								bundle.putSerializable("companyList",arrayListCompany);
								Intent intent = new Intent(getActivity(), BasicResultCompanyActivity.class);
								intent.putExtras(bundle);
								intent.putExtra("account_num", account_num);
								intent.putExtra("companyName", companyName);
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
			name_et.setText(d.getName());
			number_card_et.setText(d.getDriverLicence());
		}
		if (requestCode == 9) {
			if(data.getIntExtra("type", 0) == 1){
				String d = data.getStringExtra("result");
				doQRContent(d);
			}else{
				UIUtils.toast(act, "二维码不正确", Toast.LENGTH_SHORT);
			}
		}
	}

	private void doQRContent(String content) {
		JSONObject jsona = null;
		JSONArray jsonArray = null;
		try {
			jsona = new JSONObject(content);
			jsonArray = jsona.getJSONArray("info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for(int i=0,len=jsonArray.length();i<len;i++){
			if(jsonArray.optString(i).contains("服务监督卡号")){
				number_card_et.setText(split(jsonArray.optString(i)));
			}else if(jsonArray.optString(i).contains("驾驶员姓名")){
				name_et.setText(split(jsonArray.optString(i)));
			}
		}
	}

	private String split(String s) {
		if(s.split(":").length >=2){
			return s.split(":")[1];
		}
		return "";
	}
}
