package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyInfoActivity extends BaseActivity {

	@ViewInject(R.id.company_name)
	private TextView company_name_tv;// 单位名称
	@ViewInject(R.id.legal_representative)
	private TextView legal_representative_tv;// 法人代表
	@ViewInject(R.id.business_license)
	private TextView business_license_tv;// 业户经营许可证
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;// 业户联系方式
	@ViewInject(R.id.address)
	private TextView address_tv;// 业户住址
	@ViewInject(R.id.lhh_number)
	private TextView lhh_tv;// 立户号
	FooterHolder footer;
	UserPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_info);
		initView();
	}

	private void initView() {
		new HeaderHolder().init(self, "基础信息");
		footer = new FooterHolder();
		footer.init(self);
		pref = new UserPreference(self);
		ViewUtils.inject(self);
		Intent intent = getIntent();
		final CompanyInfo info = (CompanyInfo) intent.getSerializableExtra("CompanyInfo");
		initData(info);
		footer.left_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(pref.getBoolean("isLaw", false)){
					Intent intent = new Intent(self, LawInpsectActivity.class);
					intent.putExtra("CompanyName", info.getCompanyName());
					intent.putExtra("Lhh", info.getLhh());
					intent.putExtra("FRDB", info.getFarenDb());
					intent.putExtra("isTurn", true);
					intent.putExtra("isYeHu", true);
					startActivity(intent);
				}else{
					UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void initData(CompanyInfo info) {
		company_name_tv.setText(isNull.isEmpty(info.getCompanyName()));

		legal_representative_tv.setText(isNull.isEmpty(info.getFarenDb()));
		business_license_tv.setText(isNull.isEmpty(info.getYehuLicenceNumber()));
		telephone_tv.setText(isNull.isEmpty(info.getYehuTelphone()));
		address_tv.setText(isNull.isEmpty(info.getYehuAddress()));
		lhh_tv.setText(isNull.isEmpty(info.getLhh()));
	}
}
