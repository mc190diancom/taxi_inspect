package com.wheel.wheel.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu30.common.ui.entity.DriverInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BasicResultAdapter extends BaseAdapter {

	ArrayList<DriverInfo> list;
	Context context;
	LayoutInflater mInflater;

	public BasicResultAdapter(Context ctx, ArrayList<DriverInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public DriverInfo getItem(int position) {
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.basicresultinfoadapter, parent, false);
			convertView.setTag(holder);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.supervisory_card_number = (TextView) convertView.findViewById(R.id.supervisory_card_number);
			holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.address.setText(list.get(position).getCompanyName());
		holder.supervisory_card_number.setText(list.get(position).getJianduNumber());
		holder.user_name.setText(list.get(position).getDriverName());
		holder.sort.setText((position + 1) + "");
		return convertView;
	}

}

class Holder {
	public TextView user_name;
	public TextView supervisory_card_number;
	public TextView address;
	public TextView sort;
}
