package com.miu360.taxi_check.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.ui.LawInpsectActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FooterHolder {

	@ViewInject(R.id.footer_layout)
	public View root;
	@ViewInject(R.id.left_btn_foot)
	public Button left_btn;
	@ViewInject(R.id.center_btn_foot)
	public Button center_btn;
	@ViewInject(R.id.right_btn)
	public Button right_btn;

	public void init(final Activity act) {
		ViewUtils.inject(this, act);
		right_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				act.finish();
			}
		});
		 left_btn.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 Intent intent = new Intent(act.getApplicationContext(),LawInpsectActivity.class);
		 act.startActivity(intent);
		 }
		 });
	}

}
