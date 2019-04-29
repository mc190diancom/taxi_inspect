package com.miu360.taxi_check.ui;

import com.miu360.inspect.R;
import com.miu360.inspect.R.id;
import com.miu360.inspect.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CompanyDetailInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail_info);
		
		TextView company=(TextView) findViewById(R.id.company);
		
		Intent intent=getIntent();
		String company_name=intent.getStringExtra("company");
		company.setText(company_name);
		
	}
}
