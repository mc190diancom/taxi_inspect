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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.FaGuiDetailAdapter;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class FaGuiDetailListActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ArrayList<FaGuiDetail> arrayList = new ArrayList<>();
	FaGuiDetailAdapter adapter;
	String lbms="无";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_result_lv_you_yehu);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		lbms = getIntent().getStringExtra("lbms");
		adapter = new FaGuiDetailAdapter(self, arrayList);
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
		final MyProgressDialog d = Windows.waiting(self);
		final FaGuiDetailQ fgQ = new FaGuiDetailQ();
		fgQ.setXqid(getIntent().getStringExtra("xqid"));
		AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetail>>>() {

			@Override
			public Result<List<FaGuiDetail>> call() throws Exception {
				return WeiZhanData.getFaGuiDetails(fgQ);
			}
		}, new Callback<Result<List<FaGuiDetail>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetail>> result) {
				d.dismiss();
				if (result.ok()) {
					if(result.getData().toString().equals("[]") || result.getData() == null){
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
						finish();
						return;
					}
					holder.list.onRefreshComplete();
					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
					holder.mayShowEmpty(arrayList.size());
					header.setTitle(String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.size()));
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData(false);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(self, CheckDetailWeiFaActivity.class);
		intent.putExtra("FaGuiDetailAllInfo", (FaGuiDetail) arg0.getItemAtPosition(arg2));
		intent.putExtra("lbms", lbms);
		startActivity(intent);
	}
}
