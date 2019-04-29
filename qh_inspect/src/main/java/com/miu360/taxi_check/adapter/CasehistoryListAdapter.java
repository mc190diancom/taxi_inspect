package com.miu360.taxi_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.ArrayList;
import java.util.Date;

import timber.log.Timber;

public class CasehistoryListAdapter extends BaseAdapter {
	ArrayList<Case> list;
	LayoutInflater mInflater;

	public CasehistoryListAdapter(Context ctx, ArrayList<Case> list) {
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.adapter_case_history_item, parent, false);
			convertView.setTag(holder);
			holder.license = convertView.findViewById(R.id.license);
			holder.car_type = convertView.findViewById(R.id.car_type);
			holder.driver = convertView.findViewById(R.id.driver);
			holder.time = convertView.findViewById(R.id.time);
			holder.sort = convertView.findViewById(R.id.sort);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.sort.setText(String.valueOf(position + 1));
		holder.license.setText(list.get(position).getVNAME());
		holder.car_type.setText(list.get(position).getHYLB());
		holder.driver.setText(list.get(position).getBJCR());

		try {
			long seconds = Long.valueOf(list.get(position).getCREATEUTC());
			holder.time.setText(TimeTool.yyyyMMdd_HHmm.format(new Date(seconds * 1000)));
		} catch (Exception e) {
			Timber.w(e);
			holder.time.setText("");
		}
		return convertView;
	}

	class Holder {
		public TextView license;
		public TextView car_type;
		public TextView driver;
		public TextView time;
		public TextView sort;
	}
}
