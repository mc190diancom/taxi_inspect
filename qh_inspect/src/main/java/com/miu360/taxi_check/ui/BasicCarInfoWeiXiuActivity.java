package com.miu360.taxi_check.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.WeiXiu;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BasicCarInfoWeiXiuActivity extends BaseActivity{

	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 单位名称
	@ViewInject(R.id.legal_representative)
	private TextView legal_representative_tv;// 法人代表
	@ViewInject(R.id.business_license)
	private TextView business_license_tv;// 业户经营许可证
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;// 业户联系方式
	@ViewInject(R.id.jinying_endtime)
	private TextView jinying_endtime;// 经营范围
	@ViewInject(R.id.address)
	private TextView address_tv;// 业户住址
	/*@ViewInject(R.id.manager)
	private TextView manager;*/
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_car_info_weixiu);
		initView();
	}

	WeiXiu info;
	FooterHolder footer;

	private void initView() {
		new HeaderHolder().init(self, "基础信息");
		ViewUtils.inject(self);
		Intent intent = getIntent();
		info = (WeiXiu) intent.getSerializableExtra("WeiXiuInfo");
		initData();
	}

	private void initData() {
		company_name_tv.setText(isNull.isEmpty(info.getNAME()));
		legal_representative_tv.setText(isNull.isEmpty(info.getLEGAL()));
		business_license_tv.setText(isNull.isEmpty(info.getLICENCE()));
		telephone_tv.setText(isNull.isEmpty(info.getMANAGER_PHONE()));
		String s = "";
		if(info.getLICENCE_END_TIME() != null){
			s = new SimpleDateFormat(datePatterShow).format(new Date(Long.parseLong(info.getLICENCE_END_TIME())));
		}else{

		}
		jinying_endtime.setText(isNull.isEmpty(s));
		address_tv.setText(isNull.isEmpty(info.getADDRESS()));
	}
}
