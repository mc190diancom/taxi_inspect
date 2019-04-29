package com.miu360.taxi_check.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.HtmlAdapter;
import com.miu360.taxi_check.adapter.PersonQrAdapter;
import com.miu360.taxi_check.common.Windows;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.HistoryIDCard;
import com.miu360.taxi_check.model.NetPersonQrInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryIDScanActivity extends BaseActivity {

	// @ViewInject(R.id.name)
	// private TextView name;
	// @ViewInject(R.id.sex)
	// private TextView sex;
	// @ViewInject(R.id.code)
	// private TextView code;
	// @ViewInject(R.id.time)
	// private TextView time;
	ListViewHolder holderID;
	UserPreference perf;
	BitmapUtils mBitmap;
	UserPreference pref;
	ArrayList<HistoryIDCard> list = new ArrayList<>();
	simpleAdapter mAdapter;
	ArrayList<Driver> list1 = new ArrayList<>();

	HtmlAdapter mAdapter3;
	ArrayList<String> list3 = new ArrayList<>();

	PersonQrAdapter mAdapter2;
	ArrayList<NetPersonQrInfo> list2 = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_idscan);
		perf = new UserPreference(self);
		init();
		initView();
	}

	private void init() {
		final HistoryIDCard info = new HistoryIDCard();
		info.setZFZH(perf.getString("user_name", null));
		AsyncUtil.goAsync(new Callable<Result<List<HistoryIDCard>>>() {

			@Override
			public Result<List<HistoryIDCard>> call() throws Exception {
				return WeiZhanData.queryHistoryIdInfo(info);
			}
		}, new Callback<Result<List<HistoryIDCard>>>() {

			@Override
			public void onHandle(Result<List<HistoryIDCard>> result) {
				holderID.list.onRefreshComplete();

				if (result.ok()) {
					if (result.getData() == null) {
						return;
					}
					list.addAll(result.getData());
					mAdapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
				holderID.mayShowEmpty(list.size());
			}
		});
	}

	private void initView() {
		ViewUtils.inject(self);
		mBitmap = new BitmapUtils(self);
		new HeaderHolder().init(self, "证件记录");
		pref = new UserPreference(self);
		mAdapter = new simpleAdapter();
		holderID = ListViewHolder.initList(self);
		holderID.list.setAdapter(mAdapter);
		holderID.list.setMode(Mode.DISABLED);
		holderID.list.setRefreshing();
		holderID.mayShowEmpty(list.size());

		String url = pref.getString("url", "");
		if(url.contains("219.232.196.42/read")){
			getDriverInfo(url);
		}else if(url.contains("219.232.196.42/wr")){
			getCarInfo(url);
		}else if(url.contains("219.232.196.21")){
			String res = "http://219.232.196.21/bktcglphone/wyAppqueryUserInfo.action?pcode="+url.split("=")[1];
			Log.e("resultQr", res);
			getNetDriverInfo(res);
		}else{

		}


	}

	String DriverImage="";
	private void getDriverInfo(final String resultString){
		DriverImage=resultString.split("=")[1];
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public com.lubao.lubao.async.Result<Driver> call() throws Exception {
				// TODO Auto-generated method stub
				return WeiZhanData.getDriverInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<Driver> result) {
				d.dismiss();

				if (result.ok()) {
					if (result.getData() == null) {
						return;
					}
					list1.add(result.getData());
					mAdapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}


	private void getCarInfo(final String resultString) {
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<String>>() {

			@Override
			public com.lubao.lubao.async.Result<String> call() throws Exception {
				// TODO Auto-generated method stub
				return WeiZhanData.getCarInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<String>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<String> result) {
				d.dismiss();
				if (result.ok()) {
					if (result.getData() == null) {
						return;
					}
					list3.add(result.getData());
					mAdapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}


	private void getNetDriverInfo(final String resultString) {
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<NetPersonQrInfo>>() {

			@Override
			public com.lubao.lubao.async.Result<NetPersonQrInfo> call() throws Exception {
				return WeiZhanData.getNetDriverInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<NetPersonQrInfo>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<NetPersonQrInfo> result) {
				d.dismiss();
				if (result.ok()) {
					if(result.getData() == null || "".equals(result.getData())){
						UIUtils.toast(self, "扫描失败，请重新扫描！", Toast.LENGTH_LONG);
						finish();
					}else{
						if (result.getData() == null) {
							return;
						}
						list2.add(result.getData());
						mAdapter.notifyDataSetChanged();
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}

			}
		});
	}

	private class simpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolder holder = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(self).inflate(R.layout.idscanleadapter, null);
				holder = new viewHolder();
				arg1.setTag(holder);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.sex = (TextView) arg1.findViewById(R.id.sex);
				holder.code = (TextView) arg1.findViewById(R.id.code);
				holder.time = (TextView) arg1.findViewById(R.id.time);
				holder.fangweima = (TextView) arg1.findViewById(R.id.fangweima);
				holder.corpName = (TextView) arg1.findViewById(R.id.corpName);
				holder.phoneNumber = (TextView) arg1.findViewById(R.id.phoneNumber);
				holder.header_image = (ImageView) arg1.findViewById(R.id.header_image);
			} else {
				holder = (viewHolder) arg1.getTag();
			}
			holder.name.setText(list.get(arg0).getDRIVERNAME());
			holder.sex.setText(list.get(arg0).getSEX());
			holder.code.setText(list.get(arg0).getJDKH());
			holder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.format(new Date(Long.parseLong(list.get(arg0).getSMSJ()))));
			holder.fangweima.setText(list.get(arg0).getFWM());
			holder.corpName.setText(list.get(arg0).getCOMPNAME());
			holder.phoneNumber.setText(list.get(arg0).getPHONE());
			mBitmap.display(holder.header_image, list.get(arg0).getIMAGE_PASH());
			return arg1;
		}

		private class viewHolder {
			TextView name;
			TextView sex;
			TextView code;
			TextView time;
			TextView fangweima;
			TextView corpName;
			TextView phoneNumber;
			ImageView header_image;
		}
	}
}
