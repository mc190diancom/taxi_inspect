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
import com.miu360.taxi_check.adapter.ShengJiPeopleAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.ShengJiPeopleInfo;
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

public class ShengJiPeopleActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ShengJiPeopleAdapter adapter;

	ArrayList<ShengJiPeopleInfo> arrayList = new ArrayList<ShengJiPeopleInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_trasport_people);
		initView();
	}

	private HeaderHolder header;

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		adapter = new ShengJiPeopleAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();

	}

	private void initData(final Boolean refresh) {
		if (refresh) {
			arrayList.clear();
			adapter.notifyDataSetChanged();
		}
		final ShengJiPeopleInfo info = new ShengJiPeopleInfo();
		info.setDriverName(getIntent().getStringExtra("name"));
		info.setCyzgNumber(getIntent().getStringExtra("number_card"));
		info.setId(getIntent().getStringExtra("ID_number_card"));
		info.setStartIndex(arrayList.size() + 1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<ShengJiPeopleInfo>>>() {

			@Override
			public Result<List<ShengJiPeopleInfo>> call() throws Exception {
				return WeiZhanData.queryShengJiPeopleInfo(info);
			}
		}, new Callback<Result<List<ShengJiPeopleInfo>>>() {

			@Override
			public void onHandle(Result<List<ShengJiPeopleInfo>> result) {
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
		Intent intent = new Intent(self, ShengJiPeopleInfoActivity.class);
		intent.putExtra("ShengJiPeopleInfo", (ShengJiPeopleInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}
}
