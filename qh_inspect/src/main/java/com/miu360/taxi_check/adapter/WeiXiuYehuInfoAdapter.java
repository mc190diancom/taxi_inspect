package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.WeiXiu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeiXiuYehuInfoAdapter extends BaseAdapter {

	ArrayList<WeiXiu> list;
	LayoutInflater mInflater;

	public WeiXiuYehuInfoAdapter(Context ctx, ArrayList<WeiXiu> list) {
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
		WeiXiuHolderView holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.basicresultlvyoucompanyadapter, parent, false);
			holder = new WeiXiuHolderView();
			convertView.setTag(holder);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.number_xuke = (TextView) convertView.findViewById(R.id.number_xuke);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);

		} else {
			holder = (WeiXiuHolderView) convertView.getTag();
		}
		holder.company_name.setText(list.get(position).getNAME());
		holder.number_xuke.setText(list.get(position).getLICENCE());
		holder.sort.setText((position + 1) + "");

		return convertView;
	}

}

	class WeiXiuHolderView {
		public TextView company_name;
		public TextView number_xuke;
		public TextView sort;
		public TextView company_address;
	}