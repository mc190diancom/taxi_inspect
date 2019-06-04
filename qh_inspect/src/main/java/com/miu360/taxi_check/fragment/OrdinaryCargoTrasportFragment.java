package com.miu360.taxi_check.fragment;

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
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.DriverInfoByOther;
import com.miu360.taxi_check.model.DriverNameInfoQ;
import com.miu360.taxi_check.model.HuoYunInfo;
import com.miu360.taxi_check.model.HuoYunPeopleInfo;
import com.miu360.taxi_check.model.HuoYunYeHuInfo;
import com.miu360.taxi_check.ui.CreateCaseActivity;
import com.miu360.taxi_check.ui.GoodsTrasportCarDetailInfoActivity;
import com.miu360.taxi_check.ui.GoodsTrasportCarInfoActivity;
import com.miu360.taxi_check.ui.GoodsTrasportCompanyActivity;
import com.miu360.taxi_check.ui.GoodsTrasportCompanyDetailInfoActivity;
import com.miu360.taxi_check.ui.GoodsTrasportPeopleActivity;
import com.miu360.taxi_check.ui.GoodsTrasportPeopleInfoActivity;
import com.miu360.taxi_check.ui.QRcodeActivity;
import com.miu360.taxi_check.util.UIUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

public class OrdinaryCargoTrasportFragment extends BaseFragment implements OnClickListener {
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
	@ViewInject(R.id.saomiao_three)
	private ImageButton saoMiaoThree;
	@ViewInject(R.id.name)
	private EditText name_et;// 姓名
	@ViewInject(R.id.number_card)
	private EditText number_card_et;// 资格证号
	@ViewInject(R.id.ID_number_card)
	private EditText ID_number_card_et;// 身份证号
	@ViewInject(R.id.number_car_three)
	private EditText number_car_three_et;// 车牌号
	@ViewInject(R.id.trasport_number)
	private EditText trasport_number_et;// 道路运输证号
	@ViewInject(R.id.company_name)
	private EditText company_name_et;// 单位名称
	@ViewInject(R.id.account_num)
	private EditText account_num_et;// 许可证号
	@ViewInject(R.id.user_name)
	private EditText user_name_et;// 业户名称

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_basic_info5, container, false);// 关联布局文件
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

	private void initView(View root) {
		ViewUtils.inject(this, root);
		back.setOnClickListener(this);
		saoMiaoOne.setOnClickListener(this);
		saoMiaoTwo.setOnClickListener(this);
		saoMiaoThree.setOnClickListener(this);
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
	String NameForCompany;
	String companyName;
	ArrayList<HuoYunYeHuInfo> arrayListYehu;
	ArrayList<HuoYunInfo> arrayListCar;
	ArrayList<HuoYunPeopleInfo> arrayListLvyouPeople;
	String trasport_number;
	HuoYunInfo infoCar;
	HuoYunYeHuInfo infoCompany;
	HuoYunPeopleInfo infoPeople;
	final static String GOODS = "经营性道路货物运输驾驶员";

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
		} else if (v == saoMiaoThree) {
			Intent intent = new Intent(getActivity(), CreateCaseActivity.class);
			intent.putExtra("takephoto_direct", true);
			startActivity(intent);
		} else if (v == query) {
			if (currentType == PEOPLE) {
				name = name_et.getText().toString();
				number_card = number_card_et.getText().toString();
				ID_number_card = ID_number_card_et.getText().toString();
				if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number_card) && TextUtils.isEmpty(ID_number_card)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				arrayListLvyouPeople = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoPeople = new HuoYunPeopleInfo();
				infoPeople.setStartIndex(0);
				infoPeople.setEndIndex(10);
				infoPeople.setDriverName(name);
				infoPeople.setCyzgNumber(number_card);
				infoPeople.setId(ID_number_card);
				infoPeople.setType(GOODS);

				AsyncUtil.goAsync(new Callable<Result<List<HuoYunPeopleInfo>>>() {

					@Override
					public Result<List<HuoYunPeopleInfo>> call() throws Exception {
						return WeiZhanData.queryHuoYunPeopleInfo(infoPeople);
					}
				}, new Callback<Result<List<HuoYunPeopleInfo>>>() {

					@Override
					public void onHandle(Result<List<HuoYunPeopleInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListLvyouPeople.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), GoodsTrasportPeopleInfoActivity.class);
								intent.putExtra("HuoYunPeopleInfo", arrayListLvyouPeople.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), GoodsTrasportPeopleActivity.class);
								intent.putExtra("name", name);
								intent.putExtra("number_card", number_card);
								intent.putExtra("ID_number_card", ID_number_card);
								intent.putExtra("goods", GOODS);
								startActivity(intent);
							}
						} else {
							UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});

			} else if (currentType == CAR) {
				number_car_three = number_car_three_et.getText().toString();
				company_name = company_name_et.getText().toString();
				trasport_number = trasport_number_et.getText().toString();
				if (TextUtils.isEmpty(number_car_three) && TextUtils.isEmpty(company_name)
						&& TextUtils.isEmpty(trasport_number)) {
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
				arrayListCar = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoCar = new HuoYunInfo();
				infoCar.setVname(number_car_three.toUpperCase());
				infoCar.setDaoluyunshuNumber(trasport_number);
				infoCar.setCompany(company_name);
				infoCar.setStartIndex(0);
				infoCar.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<HuoYunInfo>>>() {

					@Override
					public Result<List<HuoYunInfo>> call() throws Exception {
						return WeiZhanData.queryHuoYunCarInfo(infoCar);
					}
				}, new Callback<Result<List<HuoYunInfo>>>() {

					@Override
					public void onHandle(Result<List<HuoYunInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListCar.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {
								Intent intent = new Intent(getActivity(), GoodsTrasportCarDetailInfoActivity.class);
								intent.putExtra("HuoYunInfo", arrayListCar.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), GoodsTrasportCarInfoActivity.class);
								intent.putExtra("number_car", number_car_three.toUpperCase());
								intent.putExtra("company_name", company_name);
								intent.putExtra("trasport_number", trasport_number);

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
				try {
					NameForCompany = URLDecoder.decode(companyName, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (TextUtils.isEmpty(LicenceNumber) && TextUtils.isEmpty(companyName)) {
					UIUtils.toast(act, "至少填写一项", Toast.LENGTH_SHORT);
					return;
				}
				arrayListYehu = new ArrayList<>();
				final MyProgressDialog pd = Windows.waiting(act);
				infoCompany = new HuoYunYeHuInfo();
				infoCompany.setCompanyName(NameForCompany);
				infoCompany.setYehuLicenceNumber(LicenceNumber);
				infoCompany.setStartIndex(0);
				infoCompany.setEndIndex(10);

				AsyncUtil.goAsync(new Callable<Result<List<HuoYunYeHuInfo>>>() {

					@Override
					public Result<List<HuoYunYeHuInfo>> call() throws Exception {
						return WeiZhanData.queryHuoYunYehuInfo(infoCompany);
					}
				}, new Callback<Result<List<HuoYunYeHuInfo>>>() {

					@Override
					public void onHandle(Result<List<HuoYunYeHuInfo>> result) {
						pd.dismiss();
						if (result.ok()) {
							if (result.getData().toString().equals("[]")) {
								UIUtils.toast(act, "查不到此信息", Toast.LENGTH_SHORT);
								return;
							}
							arrayListYehu.addAll(result.getData());
							if (1 == result.getData().get(0).getTotalNum()) {

								Intent intent = new Intent(getActivity(), GoodsTrasportCompanyDetailInfoActivity.class);
								intent.putExtra("HuoYunYeHuInfo", arrayListYehu.get(0));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), GoodsTrasportCompanyActivity.class);
								intent.putExtra("LicenceNumber", LicenceNumber);
								intent.putExtra("companyName", NameForCompany);
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
			// name_et.setText(d.getName());
			// number_card_et.setText(d.getDriverLicence());
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
				ID_number_card_et.setText(jsonArray.optString(i));
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
		driverName.setSfzh(ID_number_card_et.getText().toString());
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
