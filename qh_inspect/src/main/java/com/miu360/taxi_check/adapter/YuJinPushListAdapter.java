package com.miu360.taxi_check.adapter;

import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.mapapi.model.LatLng;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.model.VehiclePositionModex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class YuJinPushListAdapter extends ArrayAdapter<VehiclePositionModex> {
	Context ctx;
	List<VehiclePositionModex> datas;
	LayoutInflater mInflater;
	int layout;

	public YuJinPushListAdapter(Context ctx, List<VehiclePositionModex> datas) {
		super(ctx, android.R.layout.simple_list_item_1, android.R.id.text1);
		this.layout = R.layout.yujinpushlistadapter;
		this.ctx = ctx;
		this.datas = datas;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		return datas.size();
	}

	public VehiclePositionModex getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		VehiclePositionModex item = getItem(position);
		viewHolder holder = null;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = mInflater.inflate(layout, null);
			convertView.setTag(holder);
			holder.car_address = (TextView) convertView.findViewById(R.id.car_address);
			holder.car_name = (TextView) convertView.findViewById(R.id.car_name);
			holder.car_reason = (TextView) convertView.findViewById(R.id.car_reason);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		LatLng ll = new LatLng(item.getLat(), item.getLon());
		holder.car_name.setText(item.getVname());
		holder.car_reason.setText(item.getAlarmReason());
		reverseGeoCode(holder.car_address, ll);

		return convertView;
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode(final TextView tv, final LatLng ptCenter) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return HistoryData.queryHistoryTrack(ptCenter);
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {
					tv.setText(result.getData());
				}
			}
		});
	}

	private class viewHolder {
		TextView car_name;
		TextView car_address;
		TextView car_reason;
	}
}
