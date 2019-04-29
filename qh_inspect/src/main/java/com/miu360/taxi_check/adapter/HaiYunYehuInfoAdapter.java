package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.HaiYun;
import com.miu360.taxi_check.model.ShuiYun;
import com.miu360.taxi_check.model.WeiXiu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HaiYunYehuInfoAdapter extends BaseAdapter {

	ArrayList<HaiYun> list;
	LayoutInflater mInflater;

	public HaiYunYehuInfoAdapter(Context ctx, ArrayList<HaiYun> list) {
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
		HaiYunHolderView holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.basicresultlvyoucompanyadapter, parent, false);
			holder = new HaiYunHolderView();
			convertView.setTag(holder);
			holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
			holder.number_xuke = (TextView) convertView.findViewById(R.id.number_xuke);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);
			holder.number = (TextView) convertView.findViewById(R.id.xkz);
		} else {
			holder = (HaiYunHolderView) convertView.getTag();
		}
		holder.company_name.setText(list.get(position).getOWNER());
		holder.number_xuke.setText(list.get(position).getNAME());
		holder.sort.setText((position + 1) + "");
		holder.number.setText("船牌号:");

		return convertView;
	}

}

class HaiYunHolderView {
	public TextView company_name;
	public TextView number_xuke;
	public TextView sort;
	public TextView number;
}