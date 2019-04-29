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
import com.miu360.taxi_check.model.VehiclePositionModex;
import com.miu360.taxi_check.model.WeiFaInfo;
import com.miu360.taxi_check.model.WeiFa_New;
import com.miu360.taxi_check.model.WeiFa_NewQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;
import com.wheel.wheel.adapter.WeiZhangQueryAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class WeiZhangQueryResultInfoActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {
	ListViewHolder holder;
	ArrayList<WeiFa_New> arrayList = new ArrayList<WeiFa_New>();
	private WeiZhangQueryAdapter adapter;

	private HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_query_result_info);

		header = new HeaderHolder();
		header.init(self, "违法信息查询列表");

		holder = ListViewHolder.initList(self);
		hylb = getIntent().getStringExtra("hylb");
		if(hylb.equals("全部")){
			hylb = "";
		}
		adapter = new WeiZhangQueryAdapter(self, arrayList);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.PULL_DOWN_TO_REFRESH);
		holder.list.setRefreshing();
	}
	String hylb="";
	private void initData(final boolean refresh) {
		arrayList.clear();
		adapter.notifyDataSetChanged();
		final WeiFa_NewQ info = new WeiFa_NewQ();
		Intent intent = getIntent();
//		info.setVname(intent.getStringExtra("car_number"));
//		info.setDanshiren(intent.getStringExtra("name"));
//		info.setJdkh(intent.getStringExtra("number"));
//		info.setCorpName(intent.getStringExtra("unit_name"));
//		info.setCheckTime(intent.getStringExtra("start_time"));
//		info.setOverTime(intent.getStringExtra("over_time"));
//		info.setHylb(intent.getStringExtra("industry"));
//		info.setStartIndex(arrayList.size() + 0);
//		info.setEndIndex(arrayList.size() + 20);

		info.setHylb(hylb);
		/*info.setStartIndex(arrayList.size() + 0);
		info.setEndIndex(arrayList.size() + 20);*/
		info.setStartUtc(intent.getStringExtra("start_time"));
		info.setEndUtc(intent.getStringExtra("over_time"));
		info.setDsr(intent.getStringExtra("dangShiRen"));
		info.setZjzh(intent.getStringExtra("ziGeZH"));
		info.setClph(intent.getStringExtra("car_number"));
		info.setCompName(intent.getStringExtra("companyName"));
		AsyncUtil.goAsync(new Callable<Result<List<WeiFa_New>>>() {

			@Override
			public Result<List<WeiFa_New>> call() throws Exception {
				return WeiZhanData.queryWeiFaInfo_New(info);
			}
		}, new Callback<Result<List<WeiFa_New>>>() {

			@Override
			public void onHandle(Result<List<WeiFa_New>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					arrayList.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
				holder.mayShowEmpty(arrayList.size());
				header.setTitle(
						String.format("违法信息查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.size()));
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
		//initData(false);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//Toast.makeText(self, "why", 1).show();
		Intent intent = new Intent(self, WeiZhangInfoActivity.class);
		intent.putExtra("weifaInfo", (WeiFa_New) arg0.getItemAtPosition(arg2));
		startActivity(intent);
	}

}
