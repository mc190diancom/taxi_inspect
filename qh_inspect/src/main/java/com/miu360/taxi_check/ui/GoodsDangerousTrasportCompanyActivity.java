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
import com.miu360.taxi_check.adapter.GoodsTrasportYeHuAdapter;
import com.miu360.taxi_check.adapter.GoodsTrasportYeHuAdapter2;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.HuoYunYeHuInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GoodsDangerousTrasportCompanyActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ArrayList<HuoYunYeHuInfo> arrayList = new ArrayList<HuoYunYeHuInfo>();
	GoodsTrasportYeHuAdapter2 adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_trasport_company);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		adapter = new GoodsTrasportYeHuAdapter2(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();
	}

	private void initData(final boolean refersh) {
		if (refersh) {
			arrayList.clear();
			adapter.notifyDataSetChanged();
		}
		final HuoYunYeHuInfo info = new HuoYunYeHuInfo();
		info.setYehuLicenceNumber(getIntent().getStringExtra("LicenceNumber"));
		info.setCompanyName(getIntent().getStringExtra("companyName"));

		info.setStartIndex(arrayList.size()+ 1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<HuoYunYeHuInfo>>>() {

			@Override
			public Result<List<HuoYunYeHuInfo>> call() throws Exception {
				return WeiZhanData.queryHuoYunYehuInfo(info);
			}
		}, new Callback<Result<List<HuoYunYeHuInfo>>>() {

			@Override
			public void onHandle(Result<List<HuoYunYeHuInfo>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {

					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
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
		// TODO Auto-generated method stub
		Intent intent = new Intent(self, DangerousGoodsTrasportCompanyDetailInfoActivity.class);
		intent.putExtra("HuoYunYeHuInfo", (HuoYunYeHuInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
