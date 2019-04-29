package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.HaiYun;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BasicYeHuInfoHaiYunActivity extends BaseActivity{

	@ViewInject(R.id.owner_company)
	private TextView owner_company;// 拥有者
	@ViewInject(R.id.business_company)
	private TextView business_company;// 建造者
	@ViewInject(R.id.business_license)
	private TextView business_license;// 牌照

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_yehu_info_haiyun);
		initView();
	}

	HaiYun info;
	FooterHolder footer;

	private void initView() {
		new HeaderHolder().init(self, "基础信息");
		ViewUtils.inject(self);
		Intent intent = getIntent();
		info = (HaiYun) intent.getSerializableExtra("HaiYunInfo");
		initData();
	}

	private void initData() {
		business_license.setText(isNull.isEmpty(info.getNAME()));
		owner_company.setText(isNull.isEmpty(info.getOWNER()));
		business_company.setText(isNull.isEmpty(info.getBUILDER()));
	}
}
