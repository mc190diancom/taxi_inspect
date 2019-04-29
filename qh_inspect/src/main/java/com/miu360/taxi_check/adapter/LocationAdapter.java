package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.model.LocationDetial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<LocationDetial> locationDetials;

	public LocationAdapter(Context context, ArrayList<LocationDetial> locationDetials) {
		inflater = LayoutInflater.from(context);
		this.locationDetials = locationDetials;
	}

	@Override
	public int getCount() {
		return locationDetials.size();
	}

	@Override
	public Object getItem(int position) {
		return locationDetials.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.location_item, parent, false);
			viewHolder = new ViewHolder();
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text1.setText(locationDetials.get(position).name);
		viewHolder.text2.setText(locationDetials.get(position).city + " " + locationDetials.get(position).district);
		return convertView;
	}

	static class ViewHolder {
		@ViewInject(R.id.text1)
		TextView text1;
		@ViewInject(R.id.text2)
		TextView text2;
	}
}
