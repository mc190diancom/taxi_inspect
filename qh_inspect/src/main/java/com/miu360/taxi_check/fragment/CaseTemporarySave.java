package com.miu360.taxi_check.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.miu30.common.config.Config;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.ui.activity.CreateCaseActivity;
import com.miu360.legworkwrit.util.TimeTool;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.InfoPerference;
import com.miu360.taxi_check.view.ListViewHolder;

import java.io.File;
import java.util.ArrayList;

public class CaseTemporarySave extends BaseFragment {
	ListViewHolder holder;
	InfoPerference infoPerference;
	ArrayList<Case> cases = new ArrayList<>();
	SimpleAdapter adapter;
	View root;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		adapter = new SimpleAdapter();
		holder = ListViewHolder.initList(act, root);
		holder.list.setAdapter(adapter);
		holder.list.setMode(Mode.DISABLED);
		holder.list.setRefreshing();
		initData();
	}

	private void initData() {
		File file = new File(Config.PATH+ Config.CASE_TEMPS);
		if (file.exists()) {
			cases.clear();
			ArrayList<Case> list = Store2SdUtil.getInstance(act).readInArrayObject(Config.CASE_TEMPS,
					new TypeToken<ArrayList<Case>>() {
					});
			cases.addAll(list);
		}
		initEmpty();
	}

	private void initEmpty() {
		adapter.notifyDataSetChanged();
		holder.mayShowEmpty(cases.size());
		holder.list.onRefreshComplete();
	}

	private class SimpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cases.size();
		}

		@Override
		public Object getItem(int position) {
			return cases.get(position);
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
						R.layout.item_punish_layout, null);
				convertView.setTag(holder);
				holder.checkTime = convertView
						.findViewById(R.id.checkTime_tv);
				holder.nameOne = convertView
						.findViewById(R.id.nameOne_tv);
				holder.hylb = convertView.findViewById(R.id.hylb_tv);
				holder.driverName = convertView
						.findViewById(R.id.driverName_tv);
				holder.nameTwo = convertView
						.findViewById(R.id.nameTwo_tv);
				holder.vName = convertView
						.findViewById(R.id.vName_tv);
				holder.delete = convertView
						.findViewById(R.id.delete_button);
				holder.edit = convertView
						.findViewById(R.id.edit_button);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
			if(cases.get(position)!= null){
				if(cases.get(position).getCREATEUTC() !=null){
					holder.checkTime.setText(TimeTool.formatyyyyMMdd_HHmm(Long.valueOf(cases.get(position).getCREATEUTC()) * 1000));
				}
				if(cases.get(position).getZFRYNAME1() !=null){
					holder.nameOne.setText(cases.get(position).getZFRYNAME1());
				}
				if(cases.get(position).getZFRYNAME2() !=null){
					holder.nameTwo.setText(cases.get(position).getZFRYNAME2());
				}
				if(cases.get(position).getHYLB() !=null){
					holder.hylb.setText(cases.get(position).getHYLB());
				}
				if(cases.get(position).getBJCR() !=null){
					holder.driverName.setText(cases.get(position).getBJCR());
				}
				if(cases.get(position).getVNAME() !=null){
					holder.vName.setText(cases.get(position).getVNAME());
				}
			}
			final Case info = (Case) getItem(position);

			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Windows.confirm(act, "您确定要删除这条记录吗？", new OnClickListener() {
						@Override
						public void onClick(View v) {
							cases.remove(position);
							adapter.notifyDataSetChanged();
							initEmpty();
							Store2SdUtil.getInstance(act).storeOut(cases, Config.CASE_TEMPS);
						}
					});
				}
			});

			holder.edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(act, CreateCaseActivity.class);
					intent.putExtra("case", info);
					startActivity(intent);
					pos = position;
				}
			});

			return convertView;
		}

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

}
