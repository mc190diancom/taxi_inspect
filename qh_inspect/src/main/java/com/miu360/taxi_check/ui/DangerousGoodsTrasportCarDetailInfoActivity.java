package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.miu360.taxi_check.model.HuoYunInfo;
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
import android.widget.TextView;
import android.widget.Toast;

public class DangerousGoodsTrasportCarDetailInfoActivity extends BaseActivity implements OnClickListener {

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
	@ViewInject(R.id.number_yunshu)
	private TextView number_yunshu;// 道路运输证号
	@ViewInject(R.id.location_current)
	private TextView location_current;// 当前位置
	@ViewInject(R.id.model_car)
	private TextView model_car;// 车辆型号
	@ViewInject(R.id.jingyin_xuke_number)
	private TextView jingyin_xuke_number;// 经营许可证号
	@ViewInject(R.id.approvedLoad_car)
	private TextView approvedLoad_car;// 核定载重量
	@ViewInject(R.id.jingyin_fanwei)
	private TextView jingyin_fanwei;// 经营范围
	@ViewInject(R.id.twoMaintain_car)
	private TextView twoMaintain_car;// 二次维护

	HuoYunInfo lvyouInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_trasport_car1_detail_info);
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
		lvyouInfo = (HuoYunInfo) intent.getSerializableExtra("HuoYunInfo");
		initData(lvyouInfo);
	}

	private final String datePatter = "yyyy-MM-dd HH:ss";
	private final String datePatterShow = "yyyy-MM-dd HH:mm:ss.S";

	private void initData(HuoYunInfo info) {
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
		jingyin_xuke_number.setText(isNull.isEmpty(info.getJingYingXuKeNumber()));
		approvedLoad_car.setText(isNull.isEmpty(info.getApprovedLoad()));
		jingyin_fanwei.setText(isNull.isEmpty(info.getJingYingFanWei()));

		if (TextUtils.isEmpty(info.getTwoMaintain())) {

			twoMaintain_car.setText(isNull.isEmpty(info.getTwoMaintain()));
		} else {
			Date start = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				start = sf.parse(info.getTwoMaintain());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			twoMaintain_car.setText(new SimpleDateFormat(datePatter).format(new Date(start.getTime())));
		}

		number_yunshu.setText(isNull.isEmpty(info.getDaoluyunshuNumber()));

		type_car_tv.setText(isNull.isEmpty(info.getVehicleType()));
		color_car_tv.setText(isNull.isEmpty(info.getVehicleColor()));
		location_current.setText(isNull.isEmpty(info.getCurrentPostion()));
	}

	ArrayList<HuoYunYeHuInfo> arrayList = new ArrayList<HuoYunYeHuInfo>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == company_name_tv) {

			final MyProgressDialog pd = Windows.waiting(self);
			final HuoYunYeHuInfo info = new HuoYunYeHuInfo();
			info.setCompanyName(lvyouInfo.getCompany());
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
