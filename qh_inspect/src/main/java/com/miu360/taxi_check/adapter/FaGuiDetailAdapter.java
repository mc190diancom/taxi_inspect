package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu30.common.ui.entity.FaGuiDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FaGuiDetailAdapter extends BaseAdapter {

	ArrayList<FaGuiDetail> list;
	LayoutInflater mInflater;

	public FaGuiDetailAdapter(Context ctx, ArrayList<FaGuiDetail> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FaGuiHolderView holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fagui_detail_adapter, parent, false);
			holder = new FaGuiHolderView();
			convertView.setTag(holder);
			holder.xm_name = (TextView) convertView.findViewById(R.id.xm_name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);
		} else {
			holder = (FaGuiHolderView) convertView.getTag();
		}
		holder.number.setText(list.get(position).getXMBM());
		holder.xm_name.setText(list.get(position).getXMMC());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}

	class FaGuiHolderView {
		public TextView xm_name;
		public TextView number;
		public TextView sort;
	}