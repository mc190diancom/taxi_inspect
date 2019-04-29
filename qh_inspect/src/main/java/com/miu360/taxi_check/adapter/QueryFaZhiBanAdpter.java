package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.QueryFaZhiBanResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QueryFaZhiBanAdpter extends BaseAdapter {
	ArrayList<QueryFaZhiBanResult> list;
	LayoutInflater mInflater;

	public QueryFaZhiBanAdpter(Context ctx, ArrayList<QueryFaZhiBanResult> list) {
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
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

		viewHolder holder = null;
		if (arg1 == null) {
			holder = new viewHolder();
			arg1 = mInflater.inflate(R.layout.fazhibanlist_item, null);
			arg1.setTag(holder);
			holder.sort = (TextView) arg1.findViewById(R.id.sort);
			holder.check_number = (TextView) arg1.findViewById(R.id.jcbh);
			holder.driverName = (TextView) arg1.findViewById(R.id.driverName);
			holder.context = (TextView) arg1.findViewById(R.id.jcnr);
		} else {
			holder = (viewHolder) arg1.getTag();
		}
		holder.sort.setText(arg0 + 1 + "");
		holder.driverName.setText(list.get(arg0).getCITIZEN_NAME());
		holder.context.setText(list.get(arg0).getIS_STANDARD()==0?"合格":"不合格");
		holder.check_number.setText(list.get(arg0).getCHECK_LIST_CODE());
		return arg1;
	}

	private class viewHolder {
		TextView sort;
		TextView driverName;
		TextView context;
		TextView check_number;
	}

}
