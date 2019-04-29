package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.WeiZhangQueryZuLinAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.ZuLinWeiFaInfo;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class WeiZhangQueryZuLinResultInfoActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {
	ListViewHolder holder;
	ArrayList<ZuLinWeiFaInfo> arrayList = new ArrayList<ZuLinWeiFaInfo>();
	private WeiZhangQueryZuLinAdapter adapter;

	private HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_query_result_info);

		header = new HeaderHolder();
		header.init(self, "违法信息查询列表");

		holder = ListViewHolder.initList(self);

		adapter = new WeiZhangQueryZuLinAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();
	}

	private void initData(final boolean refresh) {
		if (refresh) {
			arrayList.clear();
			adapter.notifyDataSetChanged();
		}
		final ZuLinWeiFaInfo info = new ZuLinWeiFaInfo();
		Intent intent = getIntent();
		info.setVname(intent.getStringExtra("car_number"));
		info.setJdkh(intent.getStringExtra("ziGeZH"));
		info.setCorpName(intent.getStringExtra("companyName"));
		info.setCheckTime(intent.getStringExtra("start_time"));
		info.setOverTime(intent.getStringExtra("over_time"));
//		info.setHylb(intent.getStringExtra("hylb"));
		info.setStartIndex(arrayList.size() + 0);
		info.setEndIndex(arrayList.size() + 20);

		AsyncUtil.goAsync(new Callable<Result<List<ZuLinWeiFaInfo>>>() {

			@Override
			public Result<List<ZuLinWeiFaInfo>> call() throws Exception {
				return WeiZhanData.queryZuLinWeiFaInfo(info);
			}
		}, new Callback<Result<List<ZuLinWeiFaInfo>>>() {

			@Override
			public void onHandle(Result<List<ZuLinWeiFaInfo>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
				}
				holder.mayShowEmpty(arrayList.size());
				header.setTitle(
						String.format("违法信息查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
			}
		});
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData(true);
	}

	// 上拉刷新
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData(false);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(self, WeiZhangZuLinInfoActivity.class);
		intent.putExtra("ZuLinWeiFaInfo", (ZuLinWeiFaInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
