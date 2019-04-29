package com.miu360.taxi_check.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.ShengJiWeiFaInfo;

public class WeiZhangQueryShengJiAdapter extends BaseAdapter {
	ArrayList<ShengJiWeiFaInfo> list;
	Context ctx;
	public LayoutInflater mInflater;
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	public WeiZhangQueryShengJiAdapter(Context ctx, ArrayList<ShengJiWeiFaInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder1 holder = null;
		if (arg1 == null) {
			arg1 = (ViewGroup) mInflater.inflate(R.layout.queryresultinfo, arg2, false);
			holder = new ViewHolder1();
			arg1.setTag(holder);

			holder.sort = (TextView) arg1.findViewById(R.id.sort);
			holder.number_car = (TextView) arg1.findViewById(R.id.number_car_info);
			holder.user_name = (TextView) arg1.findViewById(R.id.usre_name_info);
			holder.state = (TextView) arg1.findViewById(R.id.jiean_tv);
			holder.check_time = (TextView) arg1.findViewById(R.id.check_time_info);
			holder.type_car = (TextView) arg1.findViewById(R.id.type_car);

		} else {
			holder = (ViewHolder1) arg1.getTag();
		}
		holder.sort.setText((arg0 + 1) + "");
		holder.number_car.setText(list.get(arg0).getVname());
		holder.user_name.setText(list.get(arg0).getDanshiren());
		holder.state.setText(list.get(arg0).getStatus());

		if (list.get(arg0).getStatus().equals("结案")) {
			holder.state.setTextColor(ctx.getResources().getColor(R.color.jiean_btn_color));
			holder.state.setText(list.get(arg0).getStatus());
		} else {
			holder.state.setTextColor(ctx.getResources().getColor(R.color.register_btnn_color));
			holder.state.setText(list.get(arg0).getStatus());
		}

		if (!TextUtils.isEmpty(list.get(arg0).getCheckTime())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(list.get(arg0).getCheckTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.check_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));

		} else {
			holder.check_time.setText(list.get(arg0).getCheckTime());
		}
		holder.type_car.setText(list.get(arg0).getHylb());
		return arg1;
	}

}
class ViewHolder1 {
	public TextView sort;
	public TextView number_car;
	public TextView user_name;
	public TextView state;
	public TextView check_time;
	public TextView type_car;

}
