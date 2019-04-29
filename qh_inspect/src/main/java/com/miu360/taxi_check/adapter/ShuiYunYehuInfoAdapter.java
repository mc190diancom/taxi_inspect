package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.ShuiYun;
import com.miu360.taxi_check.model.WeiXiu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShuiYunYehuInfoAdapter extends BaseAdapter {

	ArrayList<ShuiYun> list;
	LayoutInflater mInflater;

	public ShuiYunYehuInfoAdapter(Context ctx, ArrayList<ShuiYun> list) {
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
		WaterHolderView holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.basicresultshuiyunadapter, parent, false);
			holder = new WaterHolderView();
			convertView.setTag(holder);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.number_xuke = (TextView) convertView.findViewById(R.id.number);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (WaterHolderView) convertView.getTag();
		}
		holder.company_name.setText(list.get(position).getSHIP_OWNER());
		holder.number_xuke.setText(list.get(position).getSHIP_NAME());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}

	class WaterHolderView {
		public TextView company_name;
		public TextView number_xuke;
		public TextView sort;
		public TextView company_address;
	}