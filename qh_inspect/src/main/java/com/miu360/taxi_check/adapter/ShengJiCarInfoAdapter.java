package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.ShengJiInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShengJiCarInfoAdapter extends BaseAdapter {
	ArrayList<ShengJiInfo> list;
	LayoutInflater mInflater;

	public ShengJiCarInfoAdapter(Context ctx, ArrayList<ShengJiInfo> list) {
		mInflater = LayoutInflater.from(ctx);

		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		HolderView holder = null;
		if (arg1 == null) {
			holder = new HolderView();
			arg1 = mInflater.inflate(R.layout.basicresultshengjicaradapter, arg2, false);
			arg1.setTag(holder);
			holder.car_number_yunshu = (TextView) arg1.findViewById(R.id.car_number_yunshu);
			holder.car_number = (TextView) arg1.findViewById(R.id.car_number);
			holder.sort = (TextView) arg1.findViewById(R.id.sort);

		} else {
			holder = (HolderView) arg1.getTag();
		}
		holder.car_number_yunshu.setText(list.get(arg0).getDaoluyunshuNumber());
		holder.car_number.setText(list.get(arg0).getVname());
		holder.sort.setText((arg0 + 1) + "");

		return arg1;
	}

}
