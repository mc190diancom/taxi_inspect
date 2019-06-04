package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.YuJinPushListAdapter;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.model.VehiclePositionModex;
import com.miu360.taxi_check.service.PushService;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class YuJinPushListActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnItemClickListener {
	private YuJinPushListAdapter adapter;
	private List<VehiclePositionModex> datas;
	private ListViewHolder listHolder;

	private DbUtils dbUtils;
	YuJingPreference yuJingPer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbUtils = DbUtils.create(VehiclePositionModex.getDaoConfig());
		dbUtils.configDebug(true);
		setContentView(R.layout.common_list_activity);
		yuJingPer = new YuJingPreference(self);
		initView();
		listHolder.list.setRefreshing();
		registerReceiver();
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(PushService.ACTION_MSG);
		LocalBroadcastManager.getInstance(self).registerReceiver(msgReceiver, filter);
	}

	private BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(yuJingPer.getBoolean("isChecked", false)){
				VehiclePositionModex m = (VehiclePositionModex) intent.getSerializableExtra("msg");
				if (m != null) {
					datas.add(0, m);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	private void initData() {
		if(yuJingPer.getBoolean("isChecked", false)){
			listHolder.list.setMode(Mode.PULL_FROM_START);
			AsyncUtil.goAsync(new Callable<Result<List<VehiclePositionModex>>>() {

				@Override
				public Result<List<VehiclePositionModex>> call() throws Exception {
					Result<List<VehiclePositionModex>> ret = new Result<List<VehiclePositionModex>>();
					try {
						List<VehiclePositionModex> cache = dbUtils
								.findAll(Selector.from(VehiclePositionModex.class).orderBy("id", false));
						if (cache == null) {
							cache = new ArrayList<VehiclePositionModex>();
						}
						ret.setData(cache);
					} catch (Exception e) {
						ret.setThrowable(e);
						ExceptionHandler.handleException(self, ret);
					}
					return ret;
				}
			}, new Callback<Result<List<VehiclePositionModex>>>() {

				@Override
				public void onHandle(Result<List<VehiclePositionModex>> result) {
					if (result.ok()) {
						datas.clear();
						datas.addAll(result.getData());
						adapter.notifyDataSetChanged();
						listHolder.list.setMode(Mode.DISABLED);
					} else {
						new AlertDialog.Builder(self).setMessage(result.getErrorMsg()).show();
					}
					//				listHolder.mayShowEmpty(datas.size());
					listHolder.list.onRefreshComplete();

				}
			});
		}else{
			listHolder.list.postDelayed(new Runnable() {

				@Override
				public void run() {
					listHolder.list.onRefreshComplete();
				}
			}, 300);
		}

	}

	private void initView() {
		new HeaderHolder().init(self, "预警信息");
		listHolder = ListViewHolder.initList(self);
		datas = new ArrayList<VehiclePositionModex>();
		adapter = new YuJinPushListAdapter(self, datas);
		listHolder.list.setAdapter(adapter);
		listHolder.list.setOnRefreshListener(this);
		listHolder.list.setOnItemClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	private void unregisterReceiver() {
		try {
			LocalBroadcastManager.getInstance(self).unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		VehiclePositionModex m = (VehiclePositionModex) parent.getItemAtPosition(position);
	}

}
