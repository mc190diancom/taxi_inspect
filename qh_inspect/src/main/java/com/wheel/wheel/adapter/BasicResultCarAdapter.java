package com.wheel.wheel.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu30.common.ui.entity.VehicleInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BasicResultCarAdapter extends BaseAdapter {

	ArrayList<VehicleInfo> list;
	LayoutInflater mInflater;

	public BasicResultCarAdapter(Context ctx, ArrayList<VehicleInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public VehicleInfo getItem(int position) {
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
		// TODO Auto-generated method stub
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.basicresultcaradapter, parent, false);
			convertView.setTag(holder);
			holder.car_color = (TextView) convertView.findViewById(R.id.car_color);
			holder.car_number = (TextView) convertView.findViewById(R.id.car_number);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.car_color.setText(list.get(position).getVehicleColor());
		holder.car_number.setText(list.get(position).getVname());
		holder.company_name.setText(list.get(position).getCompany());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}

class HolderView {
	public TextView car_number;
	public TextView car_color;
	public TextView company_name;
	public TextView sort;

}
