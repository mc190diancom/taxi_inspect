package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.ZuLinInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CarRentaCarInfoAdapter extends BaseAdapter {
	ArrayList<ZuLinInfo> list;
	LayoutInflater mInflater;

	public CarRentaCarInfoAdapter(Context ctx, ArrayList<ZuLinInfo> list) {
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
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.basicresultzulincaradapter, parent, false);
			convertView.setTag(holder);
			holder.car_number = (TextView) convertView.findViewById(R.id.car_number);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.car_number.setText(list.get(position).getVname());
		holder.company_name.setText(list.get(position).getCompany());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}
