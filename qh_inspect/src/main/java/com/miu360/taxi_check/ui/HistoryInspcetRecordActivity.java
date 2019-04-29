package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.miu360.taxi_check.adapter.HistoryInsepctRecordAdapter;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.taxi_check.model.HistoryInspectCountRecordInfo;
import com.miu360.taxi_check.model.HistoryInspectRecordModelNew;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryInspcetRecordActivity extends BaseActivity
		implements OnItemClickListener, OnRefreshListener2<ListView> {

	ArrayList<HistoryInspectRecordModelNew> arrayList = new ArrayList<>();
	HistoryInsepctRecordAdapter adapter;
	ListViewHolder holder;
	//根据车牌、准驾证号获取的信息
	String color;
	String carType;
	String sfzh;
	String company;
	String carCompany;
	String jyxkz;
	String frdb;
	boolean isSuccful = false;
	boolean isSfCompany = false;
	//对数据进行排序
	Comparator<HistoryInspectRecordModelNew> comparator = new Comparator<HistoryInspectRecordModelNew>() {
		public int compare(HistoryInspectRecordModelNew f1, HistoryInspectRecordModelNew f2) {
			// 先排JCLB  zfsj
			if (!f1.getZfsj().equals(f2.getZfsj())) {
				return f2.getZfsj().compareTo(f1.getZfsj());
			} else{

			}
			return 0;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_inspcet_record);
		initView();
	}

	private void initView() {
		new HeaderHolder().init(self, "历史稽查记录");
		holder = ListViewHolder.initList(self);
		adapter = new HistoryInsepctRecordAdapter(self, arrayList);
		holder.list.setAdapter(adapter);

		holder.list.setOnItemClickListener(this);
		holder.list.setOnRefreshListener(this);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();

	}

	private void initData(boolean refresh) {
		if (refresh) {
			arrayList.clear();
			adapter.notifyDataSetChanged();
		}
		final HistoryInspectCountRecordInfo info = new HistoryInspectCountRecordInfo();
		Intent intent = getIntent();

		info.setVname(intent.getStringExtra("car_number"));
		info.setDriverName(intent.getStringExtra("driver_name"));
		info.setHylb(intent.getStringExtra("type"));//intent.getStringExtra("type")
		info.setStartTime(intent.getLongExtra("start", 0) + "");
		info.setEndTime(intent.getLongExtra("end", 0) + "");
		info.setCyzgz(intent.getStringExtra("number_chongye"));
		info.setZfzh(new UserPreference(self).getString("user_name",null));
		info.setZfdwmc(new UserPreference(self).getString("zfdwmc", null));
		info.setStatus(intent.getStringExtra("status"));
		info.setStartIndex(1 + arrayList.size());
		info.setEndIndex(100 + arrayList.size());

		AsyncUtil.goAsync(new Callable<Result<List<HistoryInspectRecordModelNew>>>() {

			@Override
			public Result<List<HistoryInspectRecordModelNew>> call() throws Exception {
				return WeiZhanData.queryHistoryInsepctRecordInfoNew(info);
			}
		}, new Callback<Result<List<HistoryInspectRecordModelNew>>>() {

			@Override
			public void onHandle(Result<List<HistoryInspectRecordModelNew>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					arrayList.addAll(result.getData());
					//Log.e("arrayList", "arrayList:"+arrayList);
					Collections.sort(arrayList, comparator);
					adapter.notifyDataSetChanged();

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
				holder.mayShowEmpty(arrayList.size());
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
		HistoryInspectRecordModelNew info = (HistoryInspectRecordModelNew) arg0.getItemAtPosition(arg2);
		findInfo(info);
		//startActivity(intent);
	}

	private void findInfo(final HistoryInspectRecordModelNew record) {
		if (record.getVname() != null && !record.getVname().equals("")) {
			final VehicleInfo infox = new VehicleInfo();
			infox.setVname(record.getVname().toUpperCase());
			infox.setStartIndex(0);
			infox.setEndIndex(10);
			AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

				@Override
				public Result<List<VehicleInfo>> call() throws Exception {
					return WeiZhanData.queryVehicleInfo(infox);
				}
			}, new Callback<Result<List<VehicleInfo>>>() {

				@Override
				public void onHandle(Result<List<VehicleInfo>> result) {
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							// UIUtils.toast(act, "未查询到稽查记录",
							// Toast.LENGTH_SHORT);
							return;
						}
						if (result.getData().size() != 0) {
							color = result.getData().get(0).getVehicleColor();
							carType = result.getData().get(0).getVehicleType();
							carCompany = result.getData().get(0).getCompany();
							isSuccful = true;
						}

					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}

		if (record.getCorpname() != null&& !record.getCorpname().equals("")) {
			final CompanyInfo info = new CompanyInfo();
			info.setCompanyName(record.getCorpname());
			info.setStartIndex(0);
			info.setEndIndex(10);
			AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {

				@Override
				public Result<List<CompanyInfo>> call() throws Exception {
					return WeiZhanData.queryYeHuInfo(info);
				}
			}, new Callback<Result<List<CompanyInfo>>>() {

				@Override
				public void onHandle(Result<List<CompanyInfo>> result) {
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							// UIUtils.toast(act, "未查询到稽查记录",
							// Toast.LENGTH_SHORT);
							return;
						}
						if (0 != result.getData().size()) {
							isSfCompany = true;
							jyxkz = result.getData().get(0).getYehuLicenceNumber();
							frdb = result.getData().get(0).getFarenDb();
						}
						if (record.getJdkh() != null && !record.getJdkh().equals("")) {
							QueryDriverInfo(record,true);
						}else{
							QueryDriverInfo(record,false);
						}
					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}else{
			if (record.getJdkh() != null && !record.getJdkh().equals("")) {
				QueryDriverInfo(record,true);
			}else{
				QueryDriverInfo(record,false);
			}
		}

	}

	private void QueryDriverInfo(final HistoryInspectRecordModelNew record,boolean query){
		if(query){
			final DriverInfo info = new DriverInfo();
			// info.setDriverName(name);
			info.setJianduNumber(record.getJdkh());
			info.setStartIndex(0);
			info.setEndIndex(10);
			AsyncUtil.goAsync(new Callable<Result<List<DriverInfo>>>() {

				@Override
				public Result<List<DriverInfo>> call() throws Exception {
					return WeiZhanData.queryDriverInfo(info);
				}
			}, new Callback<Result<List<DriverInfo>>>() {

				@Override
				public void onHandle(Result<List<DriverInfo>> result) {
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
						}
						if (result.getData().size() != 0) {
							sfzh = result.getData().get(0).getId();
							company = result.getData().get(0).getCompanyName();
						}
						goTO(record);
					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}else{
			goTO(record);
		}
	}

	private void goTO(final HistoryInspectRecordModelNew record){
		Intent intent = new Intent(self, InspectRecordDetailActivity.class);
		intent.putExtra("HistoryInspectRecordModelNew", record);
		intent.putExtra("color", color);
		intent.putExtra("carType", carType);
		intent.putExtra("sfzh", sfzh);
		intent.putExtra("company", company);
		intent.putExtra("carCompany", carCompany);
		intent.putExtra("jyxkz", jyxkz);
		intent.putExtra("frdb", frdb);
		startActivity(intent);
	}
}
