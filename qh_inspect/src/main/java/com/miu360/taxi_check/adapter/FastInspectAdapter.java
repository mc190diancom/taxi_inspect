package com.miu360.taxi_check.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.miu360.inspect.R;
import com.miu360.taxi_check.model.FastInspectWeiFaInfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FastInspectAdapter extends BaseAdapter {

	ArrayList<FastInspectWeiFaInfo> list;
	LayoutInflater mInflater;

	public FastInspectAdapter(Context ctx, ArrayList<FastInspectWeiFaInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
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
		ViewHolderWeiFa holder = null;
		if (arg1 == null) {
			holder = new ViewHolderWeiFa();
			arg1 = mInflater.inflate(R.layout.fastinspectadapter, null);
			arg1.setTag(holder);

			holder.check_context = (TextView) arg1.findViewById(R.id.check_context);
			holder.check_time = (TextView) arg1.findViewById(R.id.check_time);

		} else {
			holder = (ViewHolderWeiFa) arg1.getTag();
		}

		if (TextUtils.isEmpty(list.get(list.size()-1-arg0).getCheckTime())) {
			holder.check_time.setText(list.get(list.size()-1-arg0).getCheckTime());
		} else {
			Date d = null;
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
			try {
				d = sf.parse(list.get(list.size()-1-arg0).getCheckTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.check_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(d.getTime())));
		}

		holder.check_context.setText(list.get(list.size()-1-arg0).getWfxw());

		return arg1;
	}
}

class ViewHolderWeiFa {
	TextView check_time;
	TextView check_context;
}