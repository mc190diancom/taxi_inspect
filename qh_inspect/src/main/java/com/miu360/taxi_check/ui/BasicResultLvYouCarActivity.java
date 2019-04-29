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
import com.miu360.taxi_check.adapter.ToruistCarInfoAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.LvyouInfo;
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

public class BasicResultLvYouCarActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {
	ListViewHolder holder;
	ArrayList<LvyouInfo> arrayList = new ArrayList<LvyouInfo>();
	ToruistCarInfoAdapter adapter;
	HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_result_lv_you_car);
		initView();

	}

	private void initView() {
		header = new HeaderHolder();
		header.init(self, "查询列表");
		holder = ListViewHolder.initList(self);
		adapter = new ToruistCarInfoAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();

	}

	private void initData(final boolean refersh) {
		if (refersh) {
			arrayList.clear();
		}
		final LvyouInfo info = new LvyouInfo();
		info.setVname(getIntent().getStringExtra("number_car"));
		info.setCompany(getIntent().getStringExtra("company_name"));
		info.setDaoluyunshuNumber(getIntent().getStringExtra("trasport_number"));
		info.setBaocheNumber(getIntent().getStringExtra("baoche_Number"));
		info.setStartIndex(arrayList.size()+1);
		info.setEndIndex(arrayList.size() + 20);
		AsyncUtil.goAsync(new Callable<Result<List<LvyouInfo>>>() {

			@Override
			public Result<List<LvyouInfo>> call() throws Exception {
				return WeiZhanData.queryLvyouCarInfo(info);
			}
		}, new Callback<Result<List<LvyouInfo>>>() {

			@Override
			public void onHandle(Result<List<LvyouInfo>> result) {
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
		Intent intent = new Intent(self, BasicCarInfoLvyouActivity.class);
		intent.putExtra("LvyouInfo", (LvyouInfo) arg0.getItemAtPosition(arg2));
		startActivity(intent);

	}
}
