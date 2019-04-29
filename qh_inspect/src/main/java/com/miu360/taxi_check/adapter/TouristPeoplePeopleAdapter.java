package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.LvyouDriverInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TouristPeoplePeopleAdapter extends BaseAdapter {
	ArrayList<LvyouDriverInfo> list;
	LayoutInflater mInflater;

	public TouristPeoplePeopleAdapter(Context ctx, ArrayList<LvyouDriverInfo> list) {
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.basicresultinfoadapter2, parent, false);
			convertView.setTag(holder);
			holder.id_card_number = (TextView) convertView.findViewById(R.id.id_card_number);
			holder.zige_number = (TextView) convertView.findViewById(R.id.zige_number);
			holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.id_card_number.setText(list.get(position).getId());
		holder.zige_number.setText(list.get(position).getCyzgNumber());
		holder.user_name.setText(list.get(position).getDriverName());
		holder.sort.setText((position + 1) + "");
		return convertView;
	}

	private class Holder {
		TextView user_name;
		TextView id_card_number;
		TextView zige_number;
		TextView sort;
	}
}
