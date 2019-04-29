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
import com.miu360.taxi_check.adapter.CarRentaCompanyInfoAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.ZhuLinYeHuInfo;
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

public class CarRentaCompanyInfoActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ArrayList<ZhuLinYeHuInfo> arrayList = new ArrayList<ZhuLinYeHuInfo>();
	CarRentaCompanyInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_renta_company_info);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		adapter = new CarRentaCompanyInfoAdapter(self, arrayList);
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
		final ZhuLinYeHuInfo info = new ZhuLinYeHuInfo();
		info.setCompanyName(getIntent().getStringExtra("companyName"));
		info.setQiYeBeiAn(getIntent().getStringExtra("beian_number"));
		info.setStartIndex(arrayList.size() + 1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<ZhuLinYeHuInfo>>>() {

			@Override
			public Result<List<ZhuLinYeHuInfo>> call() throws Exception {
				return WeiZhanData.queryZhuLinYehuInfo(info);
			}
		}, new Callback<Result<List<ZhuLinYeHuInfo>>>() {

			@Override
			public void onHandle(Result<List<ZhuLinYeHuInfo>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {

					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				holder.mayShowEmpty(arrayList.size());
				header.setTitle(
						String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
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
		Intent intent = new Intent(self, CarRentaCompanyDetailInfoActivity.class);
		intent.putExtra("ZhuLinYeHuInfo", (ZhuLinYeHuInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
