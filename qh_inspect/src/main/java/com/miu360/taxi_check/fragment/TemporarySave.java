package com.miu360.taxi_check.fragment;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.InfoPerference;
import com.miu360.taxi_check.data.Inspector;
import com.miu360.taxi_check.ui.LawInpsectActivity;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.taxi_check.view.ListViewHolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class TemporarySave extends BaseFragment {
	ListViewHolder holder;
	InfoPerference infoPerference;
	ArrayList<TestTempSave> list = new ArrayList<>();
	ArrayList<Inspector> inspectors = new ArrayList<>();
	SimpleAdapter adapter;
	Handler hanlder = new Handler();
	private View root;
	//存放从临时保存里跳转到稽查的list位置
	int pos = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.temporarysavefragment, null);
		infoPerference = new InfoPerference(act);
		initView(root);
		return root;
	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		adapter = new SimpleAdapter();
		holder = ListViewHolder.initList(act, root);
		holder.list.setAdapter(adapter);
		holder.list.setMode(Mode.DISABLED);
		holder.list.setRefreshing();
		initData();
		//initEmpty();
	}

	private void initData() {
		File file = new File(Config.PATH+Config.FILE_NAME2);
		if (file.exists()) {
			inspectors.clear();
			ArrayList<Inspector> list = Store2SdUtil.getInstance(act).readInArrayObject(Config.FILE_NAME2,
					new TypeToken<ArrayList<Inspector>>() {
					});
			inspectors.addAll(list);
			//Log.e("inspectors","inspectors:"+ inspectors.toString());
		}
		initEmpty();
	}

	private void initEmpty() {
		adapter.notifyDataSetChanged();
		holder.mayShowEmpty(inspectors.size());
		holder.list.onRefreshComplete();
	}

	private class SimpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return inspectors.size();
		}

		@Override
		public Object getItem(int position) {
			return inspectors.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			viewHolder holder = null;
			if (convertView == null) {
				holder = new viewHolder();
				convertView = LayoutInflater.from(act).inflate(
						R.layout.tempsave, null);
				convertView.setTag(holder);
				holder.checkTime = (TextView) convertView
						.findViewById(R.id.checkTime_tv);
				holder.nameOne = (TextView) convertView
						.findViewById(R.id.nameOne_tv);
				holder.hylb = (TextView) convertView.findViewById(R.id.hylb_tv);
				holder.driverName = (TextView) convertView
						.findViewById(R.id.driverName_tv);
				holder.nameTwo = (TextView) convertView
						.findViewById(R.id.nameTwo_tv);
				holder.vName = (TextView) convertView
						.findViewById(R.id.vName_tv);
				holder.delete = (ImageButton) convertView
						.findViewById(R.id.delete_button);
				holder.edit = (ImageButton) convertView
						.findViewById(R.id.edit_button);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
			holder.checkTime.setText(inspectors.get(position).getZfsj());
			holder.nameOne.setText(inspectors.get(position).getZfry1());
			holder.nameTwo.setText(inspectors.get(position).getZfry2());
			holder.hylb.setText(inspectors.get(position).getHylb());
			holder.driverName.setText(inspectors.get(position).getDriveName());
			holder.vName.setText(inspectors.get(position).getvNumber());

			// TestTempSave info = (TestTempSave) getItem(position);

			final Inspector info = (Inspector) getItem(position);

			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Windows.confirm(act, "您确定要删除这条记录吗？", new OnClickListener() {
						@Override
						public void onClick(View v) {
							inspectors.remove(position);
							adapter.notifyDataSetChanged();
							initEmpty();
							Store2SdUtil.getInstance(act).storeOut(inspectors, Config.FILE_NAME2);
						}
					});
				}
			});

			holder.edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(act, LawInpsectActivity.class);
					intent.putExtra("IsTemp", true);
					intent.putExtra("Inspector", info);
					startActivity(intent);
					pos = position;
				}
			});

			return convertView;
		}

	}


	private void registerMsgReceiver() {
		IntentFilter filter = new IntentFilter("tempsave");
		act.registerReceiver(msgReceiver,filter);

	}

	public void unregisterMsgReceiver() {
		try {
			act.unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getBooleanExtra("IsTempDelete", false)){
				inspectors.remove(pos);
				Store2SdUtil.getInstance(act).storeOut(inspectors, Config.FILE_NAME2);
				adapter.notifyDataSetChanged();
			}
		}
	};

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != getActivity().RESULT_OK) {
			return;
		}
		initData();
	}*/

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		/*IntentFilter intentFilter = new IntentFilter("com.TemporarySave");
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
		            @Override
		            public void onReceive(Context context, Intent intent){
		                //收到广播后所作的操作
		            	initData();
		            }
		 };  
		 LocalBroadcastManager.getInstance(act).registerReceiver(mReceiver, intentFilter);  */
	}


	private class viewHolder {
		TextView nameOne;
		TextView nameTwo;
		TextView hylb;
		TextView driverName;
		TextView checkTime;
		TextView vName;
		ImageButton delete;
		ImageButton edit;
	}

	private class TestTempSave {
		private String nameOne;
		private String nameTwo;
		private String checkTime;
		private String vName;
		private String driverName;
		private String hylb;

		public String getNameOne() {
			return nameOne;
		}

		public void setNameOne(String nameOne) {
			this.nameOne = nameOne;
		}

		public String getNameTwo() {
			return nameTwo;
		}

		public void setNameTwo(String nameTwo) {
			this.nameTwo = nameTwo;
		}

		public String getCheckTime() {
			return checkTime;
		}

		public void setCheckTime(String checkTime) {
			this.checkTime = checkTime;
		}

		public String getvName() {
			return vName;
		}

		public void setvName(String vName) {
			this.vName = vName;
		}

		public String getDriverName() {
			return driverName;
		}

		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}

		public String getHylb() {
			return hylb;
		}

		public void setHylb(String hylb) {
			this.hylb = hylb;
		}

	}
}
