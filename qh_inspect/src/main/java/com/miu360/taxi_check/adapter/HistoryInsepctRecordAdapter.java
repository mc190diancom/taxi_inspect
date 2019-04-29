package com.miu360.taxi_check.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.HistoryInspectRecordModel;
import com.miu360.taxi_check.model.HistoryInspectRecordModelNew;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryInsepctRecordAdapter extends BaseAdapter {

	ArrayList<HistoryInspectRecordModelNew> list;
	LayoutInflater mInflater;
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	public HistoryInsepctRecordAdapter(Context ctx, ArrayList<HistoryInspectRecordModelNew> list) {
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
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

		viewHolderHistory holder = null;
		if (convertView == null) {
			holder = new viewHolderHistory();
			convertView = mInflater.inflate(R.layout.historyinspectrecordadapter, null);
			convertView.setTag(holder);
			holder.sort = (TextView) convertView.findViewById(R.id.sort);
			holder.vname = (TextView) convertView.findViewById(R.id.vname);
			holder.driverName = (TextView) convertView.findViewById(R.id.driver);
			holder.hylb = (TextView) convertView.findViewById(R.id.industry);
			holder.check_time = (TextView) convertView.findViewById(R.id.date);
			holder.bh = (TextView) convertView.findViewById(R.id.bh);
			holder.normal = (TextView) convertView.findViewById(R.id.result);

		} else {
			holder = (viewHolderHistory) convertView.getTag();
		}
		holder.sort.setText(position + 1 + "");
		holder.driverName.setText(list.get(position).getDriverName());
		holder.vname.setText(list.get(position).getVname().toUpperCase());
		holder.hylb.setText(list.get(position).getHylb());
		holder.bh.setText(list.get(position).getJcbh());
		holder.check_time.setText(list.get(position).getZfsj());
		if (!TextUtils.isEmpty(list.get(position).getZfsj())) {
			Log.e("time", "time:"+list.get(position).getZfsj());
			long time = Long.parseLong(list.get(position).getZfsj()+"000");
			holder.check_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time)));
		}

		if (list.get(position).getStatus().equals("0")) {
			holder.normal.setText("稽查结果：正常");
			holder.normal.setTextColor(android.graphics.Color.parseColor("#00A7D8"));
		}else if (list.get(position).getStatus().equals("2")) {
			holder.normal.setText("稽查结果：表扬");
			holder.normal.setTextColor(android.graphics.Color.parseColor("#89C100"));
		}else if (list.get(position).getStatus().equals("3")) {
			holder.normal.setText("稽查结果：批教");
			holder.normal.setTextColor(android.graphics.Color.parseColor("#A90CFD"));
		}else if (list.get(position).getStatus().equals("4")&& list.get(position).getZwstatus().equals("成功")) {
			if(list.get(position).getZcfstatus()!=null&&list.get(position).getZcfstatus().equals("已转")){
				holder.normal.setText("稽查结果：警告");
				holder.normal.setTextColor(android.graphics.Color.parseColor("#F3A401"));
			}else{
				holder.normal.setText("稽查结果：警告、待转外勤文书");
				holder.normal.setTextColor(android.graphics.Color.parseColor("#F3A401"));
			}
		}
		else {
			if(list.get(position).getZcfstatus()!=null&&list.get(position).getZcfstatus().equals("已转")){
				holder.normal.setText("稽查结果：处罚");
				holder.normal.setTextColor(android.graphics.Color.parseColor("#F54359"));
			}else{
				holder.normal.setText("稽查结果：处罚、待转外勤文书");
				holder.normal.setTextColor(android.graphics.Color.parseColor("#F54359"));
			}
		}

		return convertView;
	}

}

class viewHolderHistory {
	TextView sort;
	TextView vname;
	TextView driverName;
	TextView hylb;
	TextView check_time;
	TextView normal;
	TextView bh;

}