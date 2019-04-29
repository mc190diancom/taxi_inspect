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
import com.miu360.taxi_check.model.LvyouInfo;
import com.miu360.taxi_check.model.LvyouYehuInfo;
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

public class BasicCarInfoLvyouActivity extends BaseActivity implements OnClickListener {

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
	@ViewInject(R.id.number_baoce)
	private TextView number_baoce;// 包车证号
	@ViewInject(R.id.number_yunshu)
	private TextView number_yunshu;// 道路运输证号
	@ViewInject(R.id.location_current)
	private TextView location_current;// 当前位置
	@ViewInject(R.id.number_baoce_start)
	private TextView number_baoce_start;// 起始日期
	@ViewInject(R.id.number_baoce_end)
	private TextView number_baoce_end;// 截止日期
	@ViewInject(R.id.car_approvedLoad)
	private TextView car_approvedLoad;// 核定载重量
	@ViewInject(R.id.company_jingyinfanwei)
	private TextView company_jingyinfanwei;// 经营范围
	@ViewInject(R.id.twoMaintain)
	private TextView twoMaintain;// 二次维护
	@ViewInject(R.id.chejia_number)
	private TextView chejia_number;// 车架号
	@ViewInject(R.id.model_car)
	private TextView model_car;// 车辆型号
	@ViewInject(R.id.company_jingyinxuke)
	private TextView company_jingyinxuke;// 经营许可证

	LvyouInfo lvyouInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_car_info_lvyou);
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
		lvyouInfo = (LvyouInfo) intent.getSerializableExtra("LvyouInfo");
		initData(lvyouInfo);
	}

	private final String datePatter = "yyyy-MM-dd HH:ss";
	private final String datePatterShow = "yyyy-MM-dd HH:mm:ss.S";

	private void initData(LvyouInfo info) {
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

		if (TextUtils.isEmpty(info.getStartTime())) {
			number_baoce_start.setText(isNull.isEmpty(info.getStartTime()));
		} else {
			Date start = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				start = sf.parse(info.getStartTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			number_baoce_start.setText(new SimpleDateFormat(datePatter).format(new Date(start.getTime())));
		}

		if (TextUtils.isEmpty(info.getEndTime())) {
			number_baoce_end.setText(isNull.isEmpty(info.getEndTime()));
		} else {
			Date end = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				end = sf.parse(info.getEndTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			number_baoce_end.setText(new SimpleDateFormat(datePatter).format(new Date(end.getTime())));
		}

		if (TextUtils.isEmpty(info.getTwoMaintain())) {
			twoMaintain.setText(isNull.isEmpty(info.getTwoMaintain()));
		} else {
			Date end = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				end = sf.parse(info.getTwoMaintain());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			twoMaintain.setText(new SimpleDateFormat(datePatter).format(new Date(end.getTime())));
		}

		car_approvedLoad.setText(isNull.isEmpty(info.getApprovedLoad()));
		company_jingyinfanwei.setText(isNull.isEmpty(info.getJingYingFanWei()));
		chejia_number.setText(isNull.isEmpty(info.getVehicleNumber()));
		number_baoce.setText(isNull.isEmpty(info.getBaocheNumber()));
		number_yunshu.setText(isNull.isEmpty(info.getDaoluyunshuNumber()));
		model_car.setText(isNull.isEmpty(info.getVehicleModel()));
		type_car_tv.setText(isNull.isEmpty(info.getVehicleType()));
		color_car_tv.setText(isNull.isEmpty(info.getVehicleColor()));
		location_current.setText(isNull.isEmpty(info.getCurrentPostion()));
		company_jingyinxuke.setText(isNull.isEmpty(info.getJingYingXuKeNumber()));
	}

	ArrayList<LvyouYehuInfo> arrayListLvyou = new ArrayList<>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == company_name_tv) {

			final MyProgressDialog pd = Windows.waiting(self);
			final LvyouYehuInfo info = new LvyouYehuInfo();
			info.setCompanyName(lvyouInfo.getCompany());
			info.setStartIndex(0);
			info.setEndIndex(20);
			AsyncUtil.goAsync(new Callable<Result<List<LvyouYehuInfo>>>() {

				@Override
				public Result<List<LvyouYehuInfo>> call() throws Exception {
					return WeiZhanData.queryLvyouYehuInfo(info);
				}
			}, new Callback<Result<List<LvyouYehuInfo>>>() {

				@Override
				public void onHandle(Result<List<LvyouYehuInfo>> result) {
					pd.dismiss();
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}
						arrayListLvyou.addAll(result.getData());
						Intent intent = new Intent(self, BasicCompanyInfoLvyouActivity.class);
						intent.putExtra("LvyouYehuInfo", arrayListLvyou.get(0));
						startActivity(intent);
					}else{
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});

		}

	}
}
