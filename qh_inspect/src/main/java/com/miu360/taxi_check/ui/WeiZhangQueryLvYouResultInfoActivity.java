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
import com.miu360.taxi_check.adapter.WeiZhangQueryLvYouAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.LvYouWeiFaInfo;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class WeiZhangQueryLvYouResultInfoActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {
	ListViewHolder holder;
	ArrayList<LvYouWeiFaInfo> arrayList = new ArrayList<LvYouWeiFaInfo>();
	private WeiZhangQueryLvYouAdapter adapter;

	private HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_query_result_info);

		header = new HeaderHolder();
		header.init(self, "违法信息查询列表");

		holder = ListViewHolder.initList(self);

		adapter = new WeiZhangQueryLvYouAdapter(self, arrayList);
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
		final LvYouWeiFaInfo info = new LvYouWeiFaInfo();
		Intent intent = getIntent();
		info.setVname(intent.getStringExtra("car_number"));
		info.setDanshiren(intent.getStringExtra("dangShiRen"));
		info.setJdkh(intent.getStringExtra("ziGeZH"));
		info.setCorpName(intent.getStringExtra("companyName"));
		info.setCheckTime(intent.getStringExtra("start_time"));
		info.setOverTime(intent.getStringExtra("over_time"));
		info.setHylb(intent.getStringExtra("hylb"));
		info.setStartIndex(arrayList.size() + 0);
		info.setEndIndex(arrayList.size() + 20);

		AsyncUtil.goAsync(new Callable<Result<List<LvYouWeiFaInfo>>>() {

			@Override
			public Result<List<LvYouWeiFaInfo>> call() throws Exception {
				return WeiZhanData.queryLvYouWeiFaInfo(info);
			}
		}, new Callback<Result<List<LvYouWeiFaInfo>>>() {

			@Override
			public void onHandle(Result<List<LvYouWeiFaInfo>> result) {
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
		Intent intent = new Intent(self, WeiZhangLvYouInfoActivity.class);
		intent.putExtra("LvYouWeiFaInfo", (LvYouWeiFaInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
