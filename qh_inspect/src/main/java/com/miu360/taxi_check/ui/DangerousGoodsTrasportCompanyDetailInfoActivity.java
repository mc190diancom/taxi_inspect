package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.HuoYunYeHuInfo;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DangerousGoodsTrasportCompanyDetailInfoActivity extends BaseActivity {
	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 单位名称
	@ViewInject(R.id.legal_representative)
	private TextView legal_representative_tv;// 法人代表
	@ViewInject(R.id.business_license)
	private TextView business_license_tv;// 业户经营许可证
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;// 业户联系方式
	@ViewInject(R.id.jinying_fanwei)
	private TextView jinying_fanwei;// 经营范围
	@ViewInject(R.id.address)
	private TextView address_tv;// 业户住址
	@ViewInject(R.id.manager)
	private TextView manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_trasport_car_detail_info);
		initView();
	}

	FooterHolder footer;
	HuoYunYeHuInfo info;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		footer = new FooterHolder();
		footer.init(self);
		Intent intent = getIntent();
		info = (HuoYunYeHuInfo) intent.getSerializableExtra("HuoYunYeHuInfo");
		initData(info);
	}

	private void initData(HuoYunYeHuInfo info) {

		company_name_tv.setText(isNull.isEmpty(info.getCompanyName()));

		legal_representative_tv.setText(isNull.isEmpty(info.getFarenDb()));
		business_license_tv.setText(isNull.isEmpty(info.getYehuLicenceNumber()));
		telephone_tv.setText(isNull.isEmpty(info.getYehuTelphone()));
		jinying_fanwei.setText(isNull.isEmpty(info.getJingYingFanWei()));
		address_tv.setText(isNull.isEmpty(info.getYehuAddress()));
		manager.setText(isNull.isEmpty(info.getYehuName()));
	}
}
