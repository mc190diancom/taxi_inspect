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
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu30.common.ui.entity.VehicleInfo;
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

public class BasicCarInforActivity extends BaseActivity implements OnClickListener{

	@ViewInject(R.id.number_car)
	private TextView number_car_tv;// 车号
	@ViewInject(R.id.path_query_tv)
	private TextView path_query_tv;// 轨迹查询
	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 所属公司
	@ViewInject(R.id.company_number)
	private TextView company_number_tv;// 厂牌
	@ViewInject(R.id.type_car)
	private TextView type_car_tv;// 车辆类型
	@ViewInject(R.id.model_car)
	private TextView model_car_tv;// 车辆型号
	@ViewInject(R.id.color_car)
	private TextView color_car_tv;// 车身颜色
	@ViewInject(R.id.dan_shuang_ban)
	private TextView dan_shuang_ban_tv;// 单双班
	@ViewInject(R.id.operating_number)
	private TextView operating_number_tv;// 营运证号
	@ViewInject(R.id.frame_number)
	private TextView frame_number_tv;// 车架号
	@ViewInject(R.id.travel_mileage)
	private TextView travel_mileage_tv;// 行驶里程
	@ViewInject(R.id.permit_number)
	private TextView permit_number_tv;// 经营许可证编号
	@ViewInject(R.id.state)
	private TextView state_tv;// 运营状态
	@ViewInject(R.id.driving_number)
	private TextView driving_number_tv;// 行驶证编号
	VehicleInfo vehicleInfo;
	UserPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_car_infor);
		initView();
	}
	FooterHolder footer;
	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		pref = new UserPreference(self);
		footer = new FooterHolder();
		footer.init(self);
		Intent intent = getIntent();
		company_name_tv.setOnClickListener(this);
		path_query_tv.setOnClickListener(this);
		vehicleInfo = (VehicleInfo) intent.getSerializableExtra("VehicleInfo");
		initData(vehicleInfo);
		footer.left_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(pref.getBoolean("isLaw", false)){
					Intent intent = new Intent(self, LawInpsectActivity.class);
					intent.putExtra("Vname", vehicleInfo.getVname());
					intent.putExtra("isYeHu", true);
					startActivity(intent);
				}else{
					UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void initData(VehicleInfo info) {
//		isNull.isEmpty(info.getDriverNumber())
		number_car_tv.setText(isNull.isEmpty(info.getVname()));
		company_name_tv.setText(isNull.isEmpty(info.getCompany()));
		company_name_tv.setTextColor(Color.BLUE);
		company_name_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		company_name_tv.getPaint().setAntiAlias(true);

		if(company_name_tv.getText().toString().equals("无")){
			company_name_tv.setClickable(false);
		}else{
			company_name_tv.setClickable(true);
		}
		company_number_tv.setText(isNull.isEmpty(info.getBrand()));
		type_car_tv.setText(isNull.isEmpty(info.getVehicleType()));
		model_car_tv.setText(isNull.isEmpty(info.getVehicleXH()));
		color_car_tv.setText(isNull.isEmpty(info.getVehicleColor()));
		dan_shuang_ban_tv.setText(isNull.isEmpty(info.getDanshuangBan()));
		operating_number_tv.setText(isNull.isEmpty(info.getYingyunNumber()));
		frame_number_tv.setText(isNull.isEmpty(info.getChejiaNumber()));
		travel_mileage_tv.setText(isNull.isEmpty(info.getDriverMile()));
		permit_number_tv.setText(isNull.isEmpty(info.getJingyinLicenceNumber()));
		state_tv.setText(isNull.isEmpty(info.getYunyingStatus()));
		driving_number_tv.setText(isNull.isEmpty(info.getDriverNumber()));
	}

	ArrayList<CompanyInfo>arrayList=new ArrayList<CompanyInfo>();

	@Override
	public void onClick(View v) {
		if(v==company_name_tv){
			final MyProgressDialog pd = Windows.waiting(self);
			final CompanyInfo info = new CompanyInfo();
			info.setCompanyName(vehicleInfo.getCompany());
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
					pd.dismiss();
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}
						arrayList.addAll(result.getData());
						Intent intent=new Intent(getApplicationContext(),CompanyInfoActivity.class);
						intent.putExtra("CompanyInfo", arrayList.get(0));
						startActivity(intent);
					}else{
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}else if(v==path_query_tv){
			String vname=number_car_tv.getText().toString();
			Intent intent=new Intent(self,PathActivity.class);
			intent.putExtra("vName", vname);
			startActivity(intent);
		}
	}
}
