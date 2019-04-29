package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.ZhuLinYeHuInfo;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CarRentaCompanyDetailInfoActivity extends BaseActivity {

	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 单位名称
	@ViewInject(R.id.legal_representative)
	private TextView legal_representative_tv;// 法人代表
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;// 业户联系方式
	@ViewInject(R.id.qiye_beian)
	private TextView qiye_beian;// 企业备案号
	@ViewInject(R.id.address)
	private TextView address_tv;// 业户住址
	@ViewInject(R.id.manager)
	private TextView manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_renta_company_detail_info);
		initView();
	}

	FooterHolder footer;
	ZhuLinYeHuInfo info;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		footer = new FooterHolder();
		footer.init(self);
		Intent intent = getIntent();
		info = (ZhuLinYeHuInfo) intent.getSerializableExtra("ZhuLinYeHuInfo");
		initData(info);
	}

	private void initData(ZhuLinYeHuInfo info) {

		company_name_tv.setText(isNull.isEmpty(info.getCompanyName()));
		legal_representative_tv.setText(isNull.isEmpty(info.getFarenDb()));
		telephone_tv.setText(isNull.isEmpty(info.getYehuTelphone()));
		qiye_beian.setText(isNull.isEmpty(info.getQiYeBeiAn()));
		address_tv.setText(isNull.isEmpty(info.getYehuAddress()));
		manager.setText(isNull.isEmpty(info.getYehuName()));
	}
}
