package com.miu30.common.ui.adapter;


import com.lidroid.xutils.ViewUtils;
import com.miu30.common.ui.entity.LocationDetial;
import com.miu360.library.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text1.setText(locationDetials.get(position).name);
		viewHolder.text2.setText(locationDetials.get(position).city + " " + locationDetials.get(position).district);
		return convertView;
	}

	 class ViewHolder {
		TextView text1,text2;
        ViewHolder(View view){
			text1 = view.findViewById(R.id.text1);
			text2 = view.findViewById(R.id.text2);
        }


	}
}
