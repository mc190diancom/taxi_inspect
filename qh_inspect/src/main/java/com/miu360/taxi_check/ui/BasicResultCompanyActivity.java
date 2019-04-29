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
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;
import com.wheel.wheel.adapter.BasicResultCompanyAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BasicResultCompanyActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ArrayList<CompanyInfo> arrayList = new ArrayList<CompanyInfo>();
	BasicResultCompanyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_result_people);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		arrayList = (ArrayList<CompanyInfo>) getIntent().getSerializableExtra("companyList");
		if(arrayList == null){
			arrayList = new ArrayList<>();
		}
		adapter = new BasicResultCompanyAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		//holder.list.setRefreshing();
		holder.mayShowEmpty(arrayList.size());
		header.setTitle(String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
	}

	private void initData(final boolean refersh) {
		if (refersh) {
			arrayList.clear();
		}
		final CompanyInfo info = new CompanyInfo();
		info.setLhh(getIntent().getStringExtra("account_num"));
		info.setCompanyName(getIntent().getStringExtra("companyName"));
		info.setStartIndex(arrayList.size() + 1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {

			@Override
			public Result<List<CompanyInfo>> call() throws Exception {
				return WeiZhanData.queryYeHuInfo(info);
			}
		}, new Callback<Result<List<CompanyInfo>>>() {

			@Override
			public void onHandle(Result<List<CompanyInfo>> result) {
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
		// TODO Auto-generated method stub
		Intent intent = new Intent(self, CompanyInfoActivity.class);
		intent.putExtra("CompanyInfo", (CompanyInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
