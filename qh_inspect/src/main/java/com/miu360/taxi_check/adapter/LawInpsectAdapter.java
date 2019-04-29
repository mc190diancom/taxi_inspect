package com.miu360.taxi_check.adapter;

import java.util.List;

import com.miu360.inspect.R;
import com.miu360.taxi_check.bean.CarStateBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class LawInpsectAdapter extends BaseAdapter {
	private List<CarStateBean> datas;
	private LayoutInflater inflater;

	private OnSubItemsClickListener listener;

	public LawInpsectAdapter(Context context, List<CarStateBean> datas) {
		this.inflater = LayoutInflater.from(context);
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_lv_law_inpsect, parent, false);
			convertView.setTag(holder);

			holder.itemTvType = (TextView) convertView.findViewById(R.id.item_tv_type);
			holder.itemCbBreakLaw = (CheckBox) convertView.findViewById(R.id.item_cb_break_law);
			holder.itemCbNormal = (CheckBox) convertView.findViewById(R.id.item_cb_normal);
			holder.itemTvCheck = (TextView) convertView.findViewById(R.id.item_tv_check);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.itemTvType.setText(this.datas.get(position).getType());
		holder.itemCbBreakLaw.setChecked(this.datas.get(position).isCbBreakLawState());
		holder.itemCbNormal.setChecked(this.datas.get(position).isCbNormalState());
		
		holder.itemCbNormal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onCbNormalClick(holder.itemCbNormal, position);
			}
		});
		
		holder.itemCbBreakLaw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onCbBreakLawClick(holder.itemCbBreakLaw, position);
			}
		});

		holder.itemTvCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onTvCheckClick(holder.itemTvCheck, position);
			}
		});

		return convertView;
	}

	public void setOnSubItemsClickListener(OnSubItemsClickListener listener) {
		this.listener = listener;
	}
	
	private class ViewHolder {
		TextView itemTvType;
		CheckBox itemCbNormal;
		CheckBox itemCbBreakLaw;
		TextView itemTvCheck;
	}

	public interface OnSubItemsClickListener {
		void onCbNormalClick(View view, int position);

		void onCbBreakLawClick(View view, int position);

		void onTvCheckClick(View view, int position);
	}

}
