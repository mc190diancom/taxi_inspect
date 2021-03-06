package com.wheel.wheel.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.WeiFaInfo;
import com.miu360.taxi_check.model.WeiFa_New;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeiZhangQueryAdapter extends BaseAdapter {
	ArrayList<WeiFa_New> list;
	Context ctx;
	public LayoutInflater mInflater;
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	public WeiZhangQueryAdapter(Context ctx, ArrayList<WeiFa_New> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public WeiFa_New getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = (ViewGroup) mInflater.inflate(R.layout.queryresultinfo, arg2, false);
			holder = new ViewHolder();
			arg1.setTag(holder);

			holder.sort = (TextView) arg1.findViewById(R.id.sort);
			holder.number_car = (TextView) arg1.findViewById(R.id.number_car_info);
			holder.user_name = (TextView) arg1.findViewById(R.id.usre_name_info);
			holder.state = (TextView) arg1.findViewById(R.id.jiean_tv);
			holder.check_time = (TextView) arg1.findViewById(R.id.check_time_info);
			holder.type_car = (TextView) arg1.findViewById(R.id.type_car);

		} else {
			holder = (ViewHolder) arg1.getTag();
		}
//		holder.sort.setText((arg0 + 1) + "");
//		holder.number_car.setText(list.get(arg0).getVname());
//		holder.user_name.setText(list.get(arg0).getDanshiren());
//		holder.state.setText(list.get(arg0).getStatus());
		holder.sort.setText((arg0 + 1) + "");
		holder.number_car.setText(list.get(arg0).getCLPH());
		holder.user_name.setText(list.get(arg0).getDSR());
		holder.state.setText(list.get(arg0).getAJZT());
//
//		if (list.get(arg0).getStatus().equals("结案")) {
//			holder.state.setTextColor(ctx.getResources().getColor(R.color.jiean_btn_color));
//			holder.state.setText(list.get(arg0).getStatus());
//		} else {
//			holder.state.setTextColor(ctx.getResources().getColor(R.color.register_btnn_color));
//			holder.state.setText(list.get(arg0).getStatus());
//		}
		if (list.get(arg0).getAJZT().equals("结案")) {
			holder.state.setTextColor(ctx.getResources().getColor(R.color.jiean_btn_color));
			holder.state.setText(list.get(arg0).getAJZT());
		} else {
			holder.state.setTextColor(ctx.getResources().getColor(R.color.register_btnn_color));
			holder.state.setText(list.get(arg0).getAJZT());
		}


//		if (!TextUtils.isEmpty(list.get(arg0).getCheckTime())) {
//			Date d = null;
//			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
//			try {
//				d = sdf.parse(list.get(arg0).getCheckTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			holder.check_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
//
//		} else {
//			holder.check_time.setText(list.get(arg0).getCheckTime());
//		}
//
//		// holder.check_time.setText(list.get(arg0).getCheckTime());
//		holder.type_car.setText(list.get(arg0).getProcessGroup().replace(" ", ""));

		if (!TextUtils.isEmpty(list.get(arg0).getJCSJ())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(list.get(arg0).getJCSJ());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.check_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));

		} else {
			holder.check_time.setText(list.get(arg0).getJCSJ());
		}

		// holder.check_time.setText(list.get(arg0).getCheckTime());
		holder.type_car.setText(list.get(arg0).getHYLB());

		// holder.id.setText("1");
		// holder.number_car.setText("锟斤拷B2569");
		// holder.user_name.setText("锟筋春锟斤拷");
		// holder.state.setText("锟结案");
		// holder.check_time.setText("2016-03-17 12锟斤拷32");
		// holder.type_car.setText("锟斤拷锟解车");

		// arg1 = (ViewGroup) mInflater.inflate(R.layout.queryresultinfo, arg2,
		// false);
		// TextView id = (TextView) arg1.findViewById(R.id.id_car);
		// TextView number_car = (TextView)
		// arg1.findViewById(R.id.number_car_info);
		// TextView user_name = (TextView)
		// arg1.findViewById(R.id.usre_name_info);
		// TextView state = (TextView) arg1.findViewById(R.id.jiean_tv);
		// TextView check_time = (TextView)
		// arg1.findViewById(R.id.check_time_info);
		// TextView type_car = (TextView) arg1.findViewById(R.id.type_car);
		//
		// id.setText(list.get(0).get(0) + "");
		// number_car.setText(list.get(0).get(1) + "");
		// user_name.setText(list.get(0).get(2) + "");
		// state.setText(list.get(0).get(3) + "");
		// check_time.setText(list.get(0).get(4) + "");
		// type_car.setText(list.get(0).get(5) + "");

		return arg1;
	}

}

class ViewHolder {
	public TextView sort;
	public TextView number_car;
	public TextView user_name;
	public TextView state;
	public TextView check_time;
	public TextView type_car;

}
