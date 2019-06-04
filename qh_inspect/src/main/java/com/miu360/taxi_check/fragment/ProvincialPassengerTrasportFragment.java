package com.miu360.taxi_check.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.zxing.client.android.CaptureActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.DriverInfoByOther;
import com.miu360.taxi_check.model.DriverNameInfoQ;
import com.miu360.taxi_check.model.LvyouYehuInfo;
import com.miu360.taxi_check.model.ShengJiInfo;
import com.miu360.taxi_check.model.ShengJiPeopleInfo;
import com.miu360.taxi_check.ui.BasicResultLvYouYehuActivity;
import com.miu360.taxi_check.ui.BasicShengjiCompanyInfoActivity;
import com.miu360.taxi_check.ui.ShengJiCarActivity;
import com.miu360.taxi_check.ui.ShengJiCarDetailInfoActivity;
import com.miu360.taxi_check.ui.ShengJiPeopleActivity;
import com.miu360.taxi_check.ui.ShengJiPeopleInfoActivity;
import com.miu360.taxi_check.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ProvincialPassengerTrasportFragment extends BaseFragment implements OnClickListener {
	String[] items = { "营运车辆", "从业人员", "经营业户" };
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
	@ViewInject(R.id.name)
	private EditText name_et;// 姓名
	@ViewInject(R.id.number_card)
	private EditText number_card_et;// 资格证号
	@ViewInject(R.id.number_car_three)
	private EditText number_car_three_et;// 车牌号
	@ViewInject(R.id.trasport_road_number)
	private EditText trasport_road_number_et;// 道路运输证
	@ViewInject(R.id.company_name)
	private EditText company_name_et;// 单位名称
	@ViewInject(R.id.account_num)
	private EditText account_num_et;// 许可证号
	@ViewInject(R.id.user_name)
	private EditText user_name_et;// 业户名称
	@ViewInject(R.id.id_driver)
	private EditText id_driver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_basic_info4, container, false);// 关联布局文件
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
		saoMiaoOne.setOnClickListener(this);
		saoMiaoTwo.setOnClickListener(this);
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
	String name;
	String number_card;
	String ID_number_card;
	String number_car_three;
	String operating_number;
	String company_name;
	String LicenceNumber;
	String companyName;
	String trasport_road_number;
	String trasport_passenger_number;
	ArrayList<LvyouYehuInfo> arrayListLvyouYehu;
	ArrayList<ShengJiPeopleInfo> arrayListLvyouPeople;
	ArrayList<ShengJiInfo> arrayListShengJi;
	LvyouYehuInfo infoCompany;
	ShengJiPeopleInfo infoPeople;
	ShengJiInfo infoCar;
	String type = "客运及客运站经营 ";

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
		} else if (v == query) {
			if (currentType == PEOPLE) {
				name = name_et.getText().toString();
				number_card = number_card_et.getText().toString();
				ID_number_card = id_driver.getText().toString();
				if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number_card) && TextUtils.isEmpty(ID_number_card)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				arrayListLvyouPeople = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoPeople = new ShengJiPeopleInfo();
				infoPeople.setStartIndex(0);
				infoPeople.setEndIndex(10);
				infoPeople.setDriverName(name);
				infoPeople.setCyzgNumber(number_card);
				infoPeople.setId(ID_number_card);
				// infoPeople.setType(type);
				AsyncUtil.goAsync(new Callable<Result<List<ShengJiPeopleInfo>>>() {

					@Override
					public Result<List<ShengJiPeopleInfo>> call() throws Exception {
						return WeiZhanData.queryShengJiPeopleInfo(infoPeople);
					}
				}, new Callback<Result<List<ShengJiPeopleInfo>>>() {

					@Override
					public void onHandle(Result<List<ShengJiPeopleInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListLvyouPeople.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), ShengJiPeopleInfoActivity.class);
								intent.putExtra("ShengJiPeopleInfo", arrayListLvyouPeople.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), ShengJiPeopleActivity.class);
								intent.putExtra("name", name);
								intent.putExtra("number_card", number_card);
								intent.putExtra("ID_number_card", ID_number_card);
								// intent.putExtra("goods", type);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			} else if (currentType == CAR) {
				number_car_three = number_car_three_et.getText().toString();
				trasport_road_number = trasport_road_number_et.getText().toString();
				company_name = company_name_et.getText().toString();
				if (TextUtils.isEmpty(number_car_three) && TextUtils.isEmpty(company_name)
						&& TextUtils.isEmpty(trasport_road_number)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
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
				final MyProgressDialog pd = Windows.waiting(act);
				arrayListShengJi = new ArrayList<>();
				infoCar = new ShengJiInfo();
				infoCar.setDaoluyunshuNumber(trasport_road_number);
				infoCar.setVname(number_car_three.toUpperCase());
				infoCar.setCompany(company_name);
				infoCar.setStartIndex(0);
				infoCar.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<ShengJiInfo>>>() {

					@Override
					public Result<List<ShengJiInfo>> call() throws Exception {
						return WeiZhanData.queryShengJiCarInfo(infoCar);
					}
				}, new Callback<Result<List<ShengJiInfo>>>() {

					@Override
					public void onHandle(Result<List<ShengJiInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListShengJi.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {

								Intent intent = new Intent(getActivity(), ShengJiCarDetailInfoActivity.class);
								intent.putExtra("ShengJiInfo", arrayListShengJi.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), ShengJiCarActivity.class);
								intent.putExtra("number_car", number_car_three.toUpperCase());
								intent.putExtra("company_name", company_name);
								intent.putExtra("trasport_number", trasport_road_number);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			} else {
				LicenceNumber = account_num_et.getText().toString();
				companyName = user_name_et.getText().toString();
				if (TextUtils.isEmpty(LicenceNumber) && TextUtils.isEmpty(companyName)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				arrayListLvyouYehu = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoCompany = new LvyouYehuInfo();
				infoCompany.setYehuLicenceNumber(LicenceNumber);
				infoCompany.setCompanyName(companyName);
				infoCompany.setStartIndex(0);
				infoCompany.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<LvyouYehuInfo>>>() {

					@Override
					public Result<List<LvyouYehuInfo>> call() throws Exception {
						return WeiZhanData.queryLvyouYehuInfo(infoCompany);
					}
				}, new Callback<Result<List<LvyouYehuInfo>>>() {

					@Override
					public void onHandle(Result<List<LvyouYehuInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListLvyouYehu.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {

								Intent intent = new Intent(getActivity(), BasicShengjiCompanyInfoActivity.class);
								intent.putExtra("LvyouInfo", arrayListLvyouYehu.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), BasicResultLvYouYehuActivity.class);
								intent.putExtra("LicenceNumber", LicenceNumber);
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
			if(data.getIntExtra("type", 0) == 4){
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
			Log.e("res:", "res:"+jsonArray.optString(i));
			if(i==0){
				name_et.setText(jsonArray.optString(i));
			}else if(i==2){
				id_driver.setText(jsonArray.optString(i));
			}else if(i>2){
				break;
			}
		}
		getnumber();
	}

	private void getnumber(){
		final DriverNameInfoQ driverName = new DriverNameInfoQ();
		driverName.setCyzgz("");
		driverName.setName("");
		driverName.setSfzh(id_driver.getText().toString());
		driverName.setStartIndex(0);
		driverName.setEndIndex(8);
		AsyncUtil.goAsync(new Callable<Result<List<DriverInfoByOther>>>() {
			@Override
			public Result<List<DriverInfoByOther>> call() throws Exception {
				return WeiZhanData.queryDriverInfo(driverName);
			}
		}, new Callback<Result<List<DriverInfoByOther>>>() {

			@Override
			public void onHandle(Result<List<DriverInfoByOther>> result) {
				if (result.ok()) {
					if (result.getData() == null) {
						return;
					}
					if (result.getData().toString().equals("[]")) {
					} else {
						number_card_et.setText(result.getData().get(0).getCERTIFICATE_NO());
					}
				} else {
				}
			}
		});
	}

}
