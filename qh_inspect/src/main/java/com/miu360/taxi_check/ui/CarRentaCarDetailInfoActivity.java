package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
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
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.ZhuLinYeHuInfo;
import com.miu360.taxi_check.model.ZuLinInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CarRentaCarDetailInfoActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.number_car)
	private TextView number_car_tv;// 车号
	// @ViewInject(R.id.path_query_tv)
	// private TextView path_query_tv;// 轨迹查询
	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 所属公司
	@ViewInject(R.id.type_car)
	private TextView type_car_tv;// 车辆类型
	@ViewInject(R.id.color_car)
	private TextView color_car_tv;// 车身颜色
	@ViewInject(R.id.model_car)
	private TextView model_car;// 车辆型号
	@ViewInject(R.id.beian_car)
	private TextView beian_car;// 车辆备案
	@ViewInject(R.id.location_current)
	private TextView location_current;// 当前位置

	ZuLinInfo lvyouInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_renta_car_detail_info);
		initView();
	}

	FooterHolder footer;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		footer = new FooterHolder();
		footer.init(self);
		Intent intent = getIntent();
		company_name_tv.setOnClickListener(this);
		// path_query_tv.setOnClickListener(this);
		lvyouInfo = (ZuLinInfo) intent.getSerializableExtra("ZuLinInfo");
		initData(lvyouInfo);
	}

	private void initData(ZuLinInfo info) {
		// isNull.isEmpty(info.getDriverNumber())
		number_car_tv.setText(isNull.isEmpty(info.getVname()));
		company_name_tv.setText(isNull.isEmpty(info.getCompany()));
		company_name_tv.setTextColor(Color.BLUE);
		company_name_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		company_name_tv.getPaint().setAntiAlias(true);

		if (company_name_tv.getText().toString().equals("无")) {
			company_name_tv.setClickable(false);
		} else {
			company_name_tv.setClickable(true);
		}

		model_car.setText(isNull.isEmpty(info.getVehicleModel()));
		beian_car.setText(isNull.isEmpty(info.getCheLiangBeiAn()));

		type_car_tv.setText(isNull.isEmpty(info.getVehicleType()));
		color_car_tv.setText(isNull.isEmpty(info.getVehicleColor()));
		location_current.setText(isNull.isEmpty(info.getCurrentPostion()));
	}

	ArrayList<ZhuLinYeHuInfo> arrayList = new ArrayList<>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == company_name_tv) {
			final MyProgressDialog d = Windows.waiting(self);
			final ZhuLinYeHuInfo info = new ZhuLinYeHuInfo();
			info.setCompanyName(lvyouInfo.getCompany());
			info.setStartIndex(0);
			info.setEndIndex(20);
			AsyncUtil.goAsync(new Callable<Result<List<ZhuLinYeHuInfo>>>() {

				@Override
				public Result<List<ZhuLinYeHuInfo>> call() throws Exception {
					return WeiZhanData.queryZhuLinYehuInfo(info);
				}
			}, new Callback<Result<List<ZhuLinYeHuInfo>>>() {

				@Override
				public void onHandle(Result<List<ZhuLinYeHuInfo>> result) {
					d.dismiss();
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}
						arrayList.addAll(result.getData());
						Intent intent = new Intent(getApplicationContext(),
								CarRentaCompanyDetailInfoActivity.class);
						intent.putExtra("ZhuLinYeHuInfo", arrayList.get(0));
						startActivity(intent);
					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}

	}
}
