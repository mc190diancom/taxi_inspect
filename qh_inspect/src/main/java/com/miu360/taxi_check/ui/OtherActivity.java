package com.miu360.taxi_check.ui;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.FaZhiBanQueayActivity;
import com.miu360.inspect.R;
import com.miu360.inspect.ZhiFaJianChaJiLutableActivity;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OtherActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.fagui_query)
	private TextView fagui_query;
	@ViewInject(R.id.fazhiban_query)
	private TextView fazhiban_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "其它");
		fagui_query.setOnClickListener(this);
		fazhiban_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == fagui_query) {
			Intent intent = new Intent(self, FaGuiActivity.class);
			startActivity(intent);
		}else if(fazhiban_query==v){
			Intent intent = new Intent(self, FaZhiBanQueayActivity.class);
			startActivity(intent);
		}

	}

}
