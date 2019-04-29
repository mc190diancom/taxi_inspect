package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.DatePatterCommon;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.HuoYunPeopleInfo;
import com.miu360.taxi_check.model.HuoYunYeHuInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsTrasportPeopleInfoActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.number_zige)
	private TextView number_zige;// 资格证号
	@ViewInject(R.id.name_driver)
	private TextView name_driver_tv;
	@ViewInject(R.id.ID_card_number)
	private TextView ID_card_number_tv;
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;
	@ViewInject(R.id.address)
	private TextView address_tv;
	@ViewInject(R.id.company_driver)
	private TextView company_driver_tv;
	@ViewInject(R.id.photo)
	private ImageView photo_iv;
	@ViewInject(R.id.type_chongye)
	private TextView type_chongye;
	@ViewInject(R.id.status_chongye)
	private TextView status_chongye;
	@ViewInject(R.id.birthday_people)
	private TextView birthday_people;
	@ViewInject(R.id.first_time)
	private TextView first_time;
	@ViewInject(R.id.start_time)
	private TextView start_time;
	@ViewInject(R.id.end_time)
	private TextView end_time;

	HuoYunPeopleInfo vehicleInfo;

	BitmapUtils bitmap;
	// private final String datePatterShow = "yyyy-MM-dd";
	// private final String datePatter = "yyyy-MM-dd HH:mm:ss.S";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_trasport_people_info);
		bitmap = new BitmapUtils(self);
		bitmap.configDefaultLoadFailedImage(R.drawable.default_photo);
		initView();
	}

	FooterHolder footer;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		footer = new FooterHolder();
		footer.init(self);
		company_driver_tv.setOnClickListener(this);
		Intent intent = getIntent();
		vehicleInfo = (HuoYunPeopleInfo) intent.getSerializableExtra("HuoYunPeopleInfo");
		initData(vehicleInfo);
	}

	private void initData(final HuoYunPeopleInfo info) {

		company_driver_tv.setText(isNull.isEmpty(info.getCorpName()));
		number_zige.setText(isNull.isEmpty(info.getCyzgNumber()));
		company_driver_tv.setTextColor(Color.BLUE);
		company_driver_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		if (company_driver_tv.getText().toString().equals("无")) {
			company_driver_tv.setClickable(false);
		} else {
			company_driver_tv.setClickable(true);
		}

		if (company_driver_tv.getText().toString().equals("无")) {
			company_driver_tv.setClickable(false);
		} else {
			company_driver_tv.setClickable(true);
		}

		type_chongye.setText(isNull.isEmpty(info.getType()));

		if (TextUtils.isEmpty(info.getState())) {
			status_chongye.setText(isNull.isEmpty(info.getState()));
		} else if (info.getState().equals("0")) {
			status_chongye.setText("正常");
		} else if (info.getState().equals("1")) {
			status_chongye.setText("已超期");
		} else if (info.getState().equals("2")) {
			status_chongye.setText("待注销");
		} else if (info.getState().equals("3")) {
			status_chongye.setText("注销");
		} else if (info.getState().equals("4")) {
			status_chongye.setText("吊销");
		}

		birthday_people.setText(DatePatterCommon.getTime(info.getBirthday()));
		first_time.setText(DatePatterCommon.getTime(info.getFirstCardTime()));
		start_time.setText(DatePatterCommon.getTime(info.getStartTime()));
		end_time.setText(DatePatterCommon.getTime(info.getEndTime()));

		company_driver_tv.getPaint().setAntiAlias(true);
		name_driver_tv.setText(isNull.isEmpty(info.getDriverName()));
		ID_card_number_tv.setText(isNull.isEmpty(info.getId()));
		telephone_tv.setText(isNull.isEmpty(info.getTelphone()));
		address_tv.setText(isNull.isEmpty(info.getAddress()));
	}

	ArrayList<HuoYunYeHuInfo> arrayList = new ArrayList<HuoYunYeHuInfo>();

	@Override
	public void onClick(View v) {
		if (v == company_driver_tv) {
			final MyProgressDialog pd = Windows.waiting(self);
			final HuoYunYeHuInfo info = new HuoYunYeHuInfo();
			info.setCompanyName(vehicleInfo.getCorpName());
			info.setStartIndex(0);
			info.setEndIndex(20);
			AsyncUtil.goAsync(new Callable<Result<List<HuoYunYeHuInfo>>>() {

				@Override
				public Result<List<HuoYunYeHuInfo>> call() throws Exception {
					return WeiZhanData.queryHuoYunYehuInfo(info);
				}
			}, new Callback<Result<List<HuoYunYeHuInfo>>>() {

				@Override
				public void onHandle(Result<List<HuoYunYeHuInfo>> result) {
					pd.dismiss();
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}
						arrayList.addAll(result.getData());
						Intent intent = new Intent(getApplicationContext(),
								GoodsTrasportCompanyDetailInfoActivity.class);
						intent.putExtra("HuoYunYeHuInfo", arrayList.get(0));
						startActivity(intent);
					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}
	}
}
