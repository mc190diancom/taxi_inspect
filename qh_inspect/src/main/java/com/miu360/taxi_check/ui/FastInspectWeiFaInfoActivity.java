package com.miu360.taxi_check.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.FastInspectAdapter;
import com.miu360.taxi_check.model.FastInspectWeiFaInfo;
import com.miu360.taxi_check.view.HeaderHolder;

import java.util.ArrayList;

public class FastInspectWeiFaInfoActivity extends BaseActivity {

	@ViewInject(R.id.listShow)
	private ListView listShow;

	ArrayList<FastInspectWeiFaInfo> list;
	FastInspectAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fast_inspect_wei_fa_info);
		list = (ArrayList<FastInspectWeiFaInfo>) getIntent().getSerializableExtra("weifaInfo");

		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "违法信息");
		adapter = new FastInspectAdapter(self, list);
		listShow.setAdapter(adapter);
	}

}

