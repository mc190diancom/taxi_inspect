package com.wheel.wheel.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.CompanyInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BasicResultCompanyAdapter extends BaseAdapter {

	ArrayList<CompanyInfo> list;
	LayoutInflater mInflater;

	public BasicResultCompanyAdapter(Context ctx, ArrayList<CompanyInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CompanyInfo getItem(int position) {
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
		HolderViews holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.basictaxiresultcompanyadapter, parent, false);
			holder = new HolderViews();
			convertView.setTag(holder);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.lhh = (TextView) convertView.findViewById(R.id.lhh);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (HolderViews) convertView.getTag();
		}
		holder.company_name.setText(list.get(position).getCompanyName());
		holder.lhh.setText(list.get(position).getLhh());
		holder.address.setText(list.get(position).getYehuAddress());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}

class HolderViews {
	TextView company_name;
	TextView lhh;
	TextView address;
	public TextView sort;

}
