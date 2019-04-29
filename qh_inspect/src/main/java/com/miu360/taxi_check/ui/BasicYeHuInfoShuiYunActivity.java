package com.miu360.taxi_check.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.ShuiYun;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BasicYeHuInfoShuiYunActivity extends BaseActivity{

	@ViewInject(R.id.ship_name)
	private TextView ship_name;
	@ViewInject(R.id.ship_port)
	private TextView ship_port;
	@ViewInject(R.id.total_weight)
	private TextView total_weight;
	@ViewInject(R.id.capacity)
	private TextView capacity;
	@ViewInject(R.id.own_company)
	private TextView own_company;
	@ViewInject(R.id.business_company)
	private TextView business_company;
	@ViewInject(R.id.manager_license)
	private TextView manager_license;
	@ViewInject(R.id.boat_license)
	private TextView boat_license;
	@ViewInject(R.id.jinying_starttime)
	private TextView jinying_starttime;
	@ViewInject(R.id.jinying_endtime)
	private TextView jinying_endtime;

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_yehu_info_shuiyun);
		initView();
	}

	ShuiYun info;
	FooterHolder footer;

	private void initView() {
		new HeaderHolder().init(self, "基础信息");
		ViewUtils.inject(self);
		Intent intent = getIntent();
		info = (ShuiYun) intent.getSerializableExtra("ShuiYunInfo");
		initData();
	}

	private void initData() {
		ship_name.setText(isNull.isEmpty(info.getSHIP_NAME()));
		ship_port.setText(isNull.isEmpty(info.getSHIP_PORT()));
		total_weight.setText(isNull.isEmpty(info.getSHIP_TOTAL_WEIGHT()));
		capacity.setText(isNull.isEmpty(info.getSHIP_CAPACITY()));
		own_company.setText(isNull.isEmpty(info.getSHIP_OWNER()));
		business_company.setText(isNull.isEmpty(info.getSHIP_MANAGER()));
		manager_license.setText(isNull.isEmpty(info.getSHIP_MANAGER_LICENSE_NUMBER()));
		boat_license.setText(isNull.isEmpty(info.getSHIP_LICENSE_NUMBER()));
		String s = "",e="";
		if(info.getSHIP_LICENSE_STARTTIME() != null){
			s = new SimpleDateFormat(datePatterShow).format(new Date(Long.parseLong(info.getSHIP_LICENSE_STARTTIME())));
			e = new SimpleDateFormat(datePatterShow).format(new Date(Long.parseLong(info.getSHIP_LICENSE_DEADLINE())));
		}else{

		}
		jinying_starttime.setText(isNull.isEmpty(s));
		jinying_endtime.setText(isNull.isEmpty(e));
	}
}
