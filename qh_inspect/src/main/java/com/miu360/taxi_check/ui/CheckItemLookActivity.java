package com.miu360.taxi_check.ui;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.miu360.inspect.R;
import com.miu360.inspect.R.layout;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CheckItemLookActivity extends BaseActivity {

	ListViewHolder holder;
	View headerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_item_look);
		initView();
	}
	private void initView(){
		ViewUtils.inject(self);
		
		headerView=LayoutInflater.from(self).inflate(R.layout.checkitemadapterheader, null);
		
		
		new HeaderHolder().init(self, "查看");
		holder=ListViewHolder.initList(self);
		holder.list.setMode(Mode.DISABLED);
		holder.list.getRefreshableView().addHeaderView(headerView);
		
		
	}
}
