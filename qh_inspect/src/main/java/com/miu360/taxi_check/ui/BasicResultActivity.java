package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;
import com.wheel.wheel.adapter.BasicResultAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BasicResultActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	BasicResultAdapter adapter;

	ArrayList<DriverInfo> arrayList = new ArrayList<DriverInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_result);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		arrayList = (ArrayList<DriverInfo>) getIntent().getSerializableExtra("driverList");
		if(arrayList == null){
			arrayList = new ArrayList<>();
		}
		adapter = new BasicResultAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		//holder.list.setRefreshing();
		holder.mayShowEmpty(arrayList.size());
		header.setTitle(String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
	}

	private void initData(final Boolean refresh) {
		if (refresh) {
			arrayList.clear();
			adapter.notifyDataSetChanged();
		}
		final DriverInfo info = new DriverInfo();
		info.setDriverName(getIntent().getStringExtra("name"));
		info.setJianduNumber(getIntent().getStringExtra("number_card"));
		info.setId(getIntent().getStringExtra("ID_number_card"));
		info.setStartIndex(arrayList.size() + 1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<DriverInfo>>>() {

			@Override
			public Result<List<DriverInfo>> call() throws Exception {
				return WeiZhanData.queryDriverInfo(info);
			}
		}, new Callback<Result<List<DriverInfo>>>() {

			@Override
			public void onHandle(Result<List<DriverInfo>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}

				holder.mayShowEmpty(arrayList.size());
				header.setTitle(String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData(true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData(false);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(self, DriverInfoActivity.class);
		intent.putExtra("DriverInfo", (DriverInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
