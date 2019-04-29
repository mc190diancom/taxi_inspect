package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
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
import com.miu360.taxi_check.adapter.WeiZhangQueryShuiYunAndWeiXiuAdapter;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.WaterTranspt;
import com.miu360.taxi_check.model.WaterTransptQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class WeiZhangQueryShuiYunResultInfoActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {
	ListViewHolder holder;
	ArrayList<WaterTranspt> arrayList = new ArrayList<WaterTranspt>();
	private WeiZhangQueryShuiYunAndWeiXiuAdapter adapter;

	private HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_query_result_info);

		header = new HeaderHolder();
		header.init(self, "违法信息查询列表");
		holder = ListViewHolder.initList(self);
		adapter = new WeiZhangQueryShuiYunAndWeiXiuAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();
	}

	private void initData(final boolean refresh) {
		if (refresh) {
			arrayList.clear();
		}
		final MyProgressDialog pd = Windows.waiting(self);
		final ArrayList<WaterTranspt> arrayListInfo = new ArrayList<>();
		final WaterTransptQ info = new WaterTransptQ();
		info.setHylb(getIntent().getStringExtra("hylb"));
		info.setCompName(getIntent().getStringExtra("companyName"));
		AsyncUtil.goAsync(new Callable<Result<List<WaterTranspt>>>() {

			@Override
			public Result<List<WaterTranspt>> call() throws Exception {
				return WeiZhanData.queryWaterAndWeiXiuWeiFaInfo(info);
			}
		}, new Callback<Result<List<WaterTranspt>>>() {

			@Override
			public void onHandle(Result<List<WaterTranspt>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
						holder.list.onRefreshComplete();
						return;
					}
					holder.list.onRefreshComplete();
					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
					holder.mayShowEmpty(arrayList.size());
					header.setTitle(
							String.format("违法信息查询列表(%s条)", arrayList.isEmpty() ? 0 :arrayList.size()));
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
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
		AsyncUtil.goAsync(new Callable<Result<Void>>() {

			@Override
			public Result<Void> call() throws Exception {
				Result<Void> r = new Result<Void>(Result.OK, null, null, null);
				return r;
			}
		}, new Callback<Result<Void>>() {

			@Override
			public void onHandle(Result<Void> result) {
				if (result.ok()) {
					holder.list.onRefreshComplete();
				} else {
					holder.list.onRefreshComplete();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(self, WeiZhangShuiYunAndWeiXiuInfoActivity.class);
		intent.putExtra("WaterAndWeiXiuWeiFaInfo", (WaterTranspt) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
