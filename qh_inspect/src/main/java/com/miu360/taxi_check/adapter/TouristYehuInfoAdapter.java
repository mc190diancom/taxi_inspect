package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.LvyouYehuInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TouristYehuInfoAdapter extends BaseAdapter {

	ArrayList<LvyouYehuInfo> list;
	LayoutInflater mInflater;

	public TouristYehuInfoAdapter(Context ctx, ArrayList<LvyouYehuInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderViews holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.basicresultlvyoucompanyadapter, parent, false);
			holder = new HolderViews();
			convertView.setTag(holder);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.number_xuke = (TextView) convertView.findViewById(R.id.number_xuke);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (HolderViews) convertView.getTag();
		}
		holder.company_name.setText(list.get(position).getCompanyName());
		holder.number_xuke.setText(list.get(position).getYehuLicenceNumber());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

	class HolderViews {
		public TextView company_name;
		public TextView number_xuke;
		public TextView sort;
		public TextView company_address;
	}
}
