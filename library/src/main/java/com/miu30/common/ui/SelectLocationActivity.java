package com.miu30.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.AsyncUtil;
import com.miu30.common.async.Callback;
import com.miu30.common.async.Result;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.data.CommonData;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.adapter.LocationAdapter;
import com.miu30.common.ui.entity.HistoryInspectCountRecordInfo;
import com.miu30.common.ui.entity.HistoryInspectRecordModel;
import com.miu30.common.ui.entity.LocationDetial;
import com.miu30.common.util.GpsManger;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.util.isCommon;
import com.miu360.library.R;
import com.miu360.library.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLocationActivity extends Activity implements TextWatcher, OnClickListener, OnItemClickListener {
	@BindView(R2.id.search_edit)
	EditText search_edit;
	@BindView(R2.id.common)
	LinearLayout common;
	@BindView(R2.id.common_address)
	TextView common_address;
	@BindView(R2.id.left_btn)
	ImageButton left_btn;
	@BindView(R2.id.aim_location)
	ImageView aim_location;
	@BindView(R2.id.address_list)
	ListView address_list;
	@BindView(R2.id.address_history)
	ListView addressHistory;
	@BindView(R2.id.sure)
	TextView sure;

	private LocationAdapter adapter;

	private String add = "";
	private ArrayList<LocationDetial> locationDetials;

	public static String CHOSE_LOCATION = "chose_location";
	boolean isChanged = false;
	private String contextAdd = "";
	private ArrayList<String> historyList = new ArrayList<>();
	private ArrayList<HistoryInspectRecordModel> historyCdList = new ArrayList<>();
	private ArrayList<HistoryInspectRecordModel> historyCordList = new ArrayList<>();
	private addressAdapter addAdapter;

	boolean IsFirst = true;
	boolean isOpenGps = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law_select_location);
		ButterKnife.bind(this);
		initView();
	}

	/**
	 * 初始化控件
	 */
	protected void initView() {
		locationDetials = new ArrayList<>();
		adapter = new LocationAdapter(this, locationDetials);
		address_list.setAdapter(adapter);
		common_address.setOnClickListener(this);
		aim_location.setOnClickListener(this);
		sure.setOnClickListener(this);
		address_list.setOnItemClickListener(this);
		left_btn.setOnClickListener(this);
		add = getIntent().getStringExtra("thisAdd");
		addAdapter = new addressAdapter();
		addressHistory.setAdapter(addAdapter);
		addressHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				isChanged = true;
				MsgConfig.select_lat = historyCordList.get(position).getLat();
				MsgConfig.select_lng = historyCordList.get(position).getLon();
				contextAdd = historyCordList.get(position).getAddress();
				if(0 == historyCordList.get(position).getLat()){
					UIUtils.toast(SelectLocationActivity.this, "获取的经纬度为0", Toast.LENGTH_SHORT);
				}else{
					finish();
				}
			}
		});
		if (TextUtils.isEmpty(add) || "".equals(add)) {
			initLocation(true);
		} else{
			search_edit.setText(add);
		}
		search_edit.addTextChangedListener(this);
	}

	private class addressAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return historyCordList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return historyCordList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolderLicence holder_driver;
			if (arg1 == null) {
				holder_driver = new viewHolderLicence();
				arg1 = LayoutInflater.from(SelectLocationActivity.this).inflate(R.layout.listaddressadapter,
						arg2, false);
				arg1.setTag(holder_driver);

				holder_driver.addText = (TextView) arg1.findViewById(R.id.addresName);

			} else {
				holder_driver = (viewHolderLicence) arg1.getTag();
			}
			holder_driver.addText.setText(historyCordList.get(arg0).getAddress());
			return arg1;
		}

	}

	private class viewHolderLicence {
		TextView addText;
	}

	/**
	 * 定位
	 */
	protected void initLocation(final boolean show) {
		search_edit.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
					search_edit.removeTextChangedListener(SelectLocationActivity.this);
					reverseGeoCode1(search_edit, MsgConfig.lat, MsgConfig.lng, show);
					MsgConfig.select_lng = 0;
					MsgConfig.select_lat = 0;
				}else{
					if(!IsFirst){
						UIUtils.toast(SelectLocationActivity.this, "GPS信号差，请拿到开阔地段！", Toast.LENGTH_SHORT);
					}
					IsFirst = false;
					isChanged = false;
				}
			}
		}, 1000);
	}

	/**
	 * 获取数据
	 */
	protected void searchData(final String text) {
		AsyncUtil.goAsync(new Callable<Result<ArrayList<LocationDetial>>>() {

			@Override
			public Result<ArrayList<LocationDetial>> call() throws Exception {
				return CommonData.qureyLocation(text);
			}
		}, new Callback<Result<ArrayList<LocationDetial>>>() {

			@Override
			public void onHandle(Result<ArrayList<LocationDetial>> result) {
				locationDetials.clear();
				if (result.ok()) {
					locationDetials.addAll(result.getData());
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(SelectLocationActivity.this, result.getMsg(), Toast.LENGTH_SHORT);
				}

			}
		});
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	private Runnable runnable;
	private int loc_type;// 状态记录，0，定位中，1，定位成功

	/**
	 * 文字改变
	 */
	@Override
	public void onTextChanged(final CharSequence s, int start, int before, int count) {
		if (s.length() == 0) {
			search_edit.getHandler().removeCallbacks(runnable);
			locationDetials.clear();
			adapter.notifyDataSetChanged();
			MsgConfig.select_lng = 0;
			MsgConfig.select_lat = 0;
		} else {
			search_edit.getHandler().removeCallbacks(runnable);
			runnable = new Runnable() {

				@Override
				public void run() {
					searchData(s.toString());
				}
			};
			search_edit.getHandler().postDelayed(runnable, 600);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 撤销
	 */
	@Override
	public void onClick(View v) {
		if (v == aim_location) {
			if (!GpsManger.isOPen(this)) {
				Windows.confirm(SelectLocationActivity.this, "请先开启GPS", new OnClickListener() {

					@Override
					public void onClick(View v) {
						isOpenGps=true;
						GpsManger.openGps(SelectLocationActivity.this);
					}
				});
			} else {
				isChanged = true;
				initLocation(true);
			}
		} else if (v == sure) {
			contextAdd = search_edit.getText().toString();
			if(TextUtils.isEmpty(contextAdd)){
				UIUtils.toast(SelectLocationActivity.this, "地址名不能设置为空", Toast.LENGTH_SHORT);
				return;
			}
			isChanged = true;
			finish();
		} else if (v == common_address) {
			if (addressHistory.getVisibility() == View.VISIBLE) {
				addressHistory.setVisibility(View.GONE);
				return;
			}
			final HistoryInspectCountRecordInfo info = new HistoryInspectCountRecordInfo();
			info.setStartTime(2649653 + "");
			info.setEndTime(System.currentTimeMillis() / 1000 + "");
			info.setStartIndex(0);
			info.setEndIndex(5);
			info.setZfzh(new UserPreference(SelectLocationActivity.this).getString("user_name", null));
			AsyncUtil.goAsync(new Callable<Result<List<HistoryInspectRecordModel>>>() {
				@Override
				public Result<List<HistoryInspectRecordModel>> call() throws Exception {
					return CommonData.queryHistoryInsepctRecordInfo(info);
				}
			}, new Callback<Result<List<HistoryInspectRecordModel>>>() {

				@Override
				public void onHandle(Result<List<HistoryInspectRecordModel>> result) {
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(SelectLocationActivity.this, "没有最近的执法地址", Toast.LENGTH_LONG);
							return;
						} else {
							historyCdList.addAll(result.getData());
							for (int i = 0, len = historyCdList.size(); i < len; i++) {
								if (!historyList.contains(historyCdList.get(i).getAddress())) {
									historyList.add(historyCdList.get(i).getAddress());
									historyCordList.add(historyCdList.get(i));
								}
							}
							addressHistory.setVisibility(View.VISIBLE);
							addAdapter.notifyDataSetChanged();
						}

					} else {
						UIUtils.toast(SelectLocationActivity.this, result.getMsg(), Toast.LENGTH_LONG);
					}
				}
			});
		} else if (v == left_btn) {
			finish();
		}

	}

	/**
	 * 通过经纬度获取位置
	 */
	public void reverseGeoCode1(final TextView tv, final double lat, final double lng, final boolean show) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return CommonData.queryHistoryTrack1(lat, lng);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					loc_type = 1;
					if (show) {
						tv.setText(result.getData());
						contextAdd = result.getData();
					}
				} else {
					loc_type = 0;
				}
				search_edit.addTextChangedListener(SelectLocationActivity.this);
			}
		});
	}

	/**
	 * 用户自选位置
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (locationDetials.get(position).location == null) {
			Toast.makeText(this, "无法获取经纬度！", Toast.LENGTH_SHORT).show();
		} else {
			isChanged = true;
			MsgConfig.select_lng = locationDetials.get(position).location.lng;
			MsgConfig.select_lat = locationDetials.get(position).location.lat;
			contextAdd = locationDetials.get(position).name;
			finish();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isOpenGps&&GpsManger.isOPen(this)){
			isChanged = true;
			initLocation(true);
		}else{
			isOpenGps=false;
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (isChanged) {
			Intent intent = new Intent(CHOSE_LOCATION);
			intent.putExtra("shoudong_location", contextAdd);
			this.sendBroadcast(intent);
		}
		super.onDestroy();
	}
}
