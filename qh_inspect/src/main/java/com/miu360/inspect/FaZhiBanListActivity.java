package com.miu360.inspect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;

import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.QueryFaZhiBanAdpter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.FazhiBanAllQuenyModel;
import com.miu360.taxi_check.model.QueryFaZhiBanResult;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FaZhiBanListActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holder;
	ArrayList<QueryFaZhiBanResult> listArray = new ArrayList<>();
	QueryFaZhiBanAdpter adapter;
	HeaderHolder header;
	String mHylb;
	String driverName;
	String jcx;
	String struts;
	String enddate;
	String startdate;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_xie_tiao_biao);
		header = new HeaderHolder();
		header.init(self, "执法检查记录表");
		holder = ListViewHolder.initList(self);
		if(getIntent().getBooleanExtra("isOneTurn", false)){
			String info=getIntent().getStringExtra("infoGson");
			listArray=new Gson().fromJson(info, new TypeToken<ArrayList<QueryFaZhiBanResult>>() {
			}.getType());
			Log.e("zar", "listArray"+listArray.size());
		}
		adapter = new QueryFaZhiBanAdpter(self, listArray);
		holder.list.setAdapter(adapter);
		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);


		mHylb = getIntent().getStringExtra("Hylb");
		driverName=getIntent().getStringExtra("driverName");
		jcx=getIntent().getStringExtra("jcx");
		struts=1+"";
		enddate=getIntent().getStringExtra("endTime");
		startdate=getIntent().getStringExtra("startTime");

		initData(true);

	}

	private void initData(boolean refresh) {

		if(getIntent().getBooleanExtra("isOneTurn", false)){
			holder.list.postDelayed(new Runnable() {
				@Override
				public void run() {
					holder.list.onRefreshComplete();
				}
			}, 1000);
			adapter.notifyDataSetChanged();
			holder.mayShowEmpty(listArray.size());
			return;
		}
		if (refresh) {
			listArray.clear();
			adapter.notifyDataSetChanged();
		}
		final FazhiBanAllQuenyModel quenyModel = new FazhiBanAllQuenyModel();
		quenyModel.setHYLB(mHylb);
		quenyModel.setCITIZEN_NAME(driverName);
		quenyModel.setMODULE_NAME(jcx);
		quenyModel.setIS_ENABLE(struts);
		quenyModel.setSTARTDATE(startdate);
		quenyModel.setENDDATE(enddate);

		AsyncUtil.goAsync(new Callable<Result<List<QueryFaZhiBanResult>>>() {

			@Override
			public Result<List<QueryFaZhiBanResult>> call() throws Exception {
				return WeiZhanData.queryFaZhiBan(quenyModel);
			}
		}, new Callback<Result<List<QueryFaZhiBanResult>>>() {

			@Override
			public void onHandle(Result<List<QueryFaZhiBanResult>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					listArray.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				holder.mayShowEmpty(listArray.size());
//				header.setTitle(
//						String.format("危险品名录列表(%s条)", listArray.isEmpty() ? 0 : listArray.get(0).getTotalNum()));
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

		initData(true);


	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		initData(true);


	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		QueryFaZhiBanResult info = (QueryFaZhiBanResult) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(self, ZhiFaJianChaJiLutableActivity.class);
		intent.putExtra("QueryFaZhiBanResult", info);
		intent.putExtra("Vname", info.getVNAME());
		startActivity(intent);
	}

}