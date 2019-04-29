package com.miu360.taxi_check.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.model.CameraDetail;
import com.miu360.taxi_check.service.PushService;
import com.miu360.taxi_check.ui.CameraWarningDetailInfoActivity;
import com.miu360.taxi_check.util.UIUtils;

public class WarningCameraFragment extends BaseFragment {
	@ViewInject(R.id.list_warning_camera)
	private ListView list_warning_camera;
	@ViewInject(R.id.camera_details)
	private TextView camera_details;
	@ViewInject(R.id.camera_title)
	private TextView camera_title;

	private View view;
	private String camera = "";
	private boolean isOpen = false;
	YuJingPreference yuJingPer;
	private DbUtils dbUtils;
	private List<CameraDetail> datas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_warning_camera, null);
		Position();
		registerReceiver();
		initView();
		initData();
		return view;
	}

	private void initView() {
		ViewUtils.inject(this, view);
		// dbUtils = DbUtils.create(act);
		dbUtils = DbUtils.create(CameraDetail.getDaoConfig());
		dbUtils.configDebug(true);
		datas = new ArrayList<>();
		adapter = new SimpleAdapter();
		list_warning_camera.setAdapter(adapter);
		// 把存储在preference中的数据取出来
		yuJingPer = new YuJingPreference(act);
		camera = yuJingPer.getString("camera", "");
		isOpen = yuJingPer.getBoolean("isCameraChecked", false);
		if (isOpen) {
			String html = "<html><body>" + camera + "<font color=\"#9ACD32\">" + "(已开启)" + "</font>" + "</body></html>";
			camera_details.setText(Html.fromHtml(html));
		} else {
			camera_title.setText("");
			camera_details.setText("未开启摄像头预警");
		}

		list_warning_camera.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CameraDetail info = (CameraDetail) parent.getItemAtPosition(position);
				Intent intent = new Intent(act, CameraWarningDetailInfoActivity.class);
				intent.putExtra("cameraDetail", info);
				startActivity(intent);

			}
		});
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(PushService.ACTION_MAMERA);
		LocalBroadcastManager.getInstance(act).registerReceiver(msgReceiver, filter);
	}

	private BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (yuJingPer.getBoolean("isCameraChecked", false)) {
				CameraDetail m = (CameraDetail) intent.getSerializableExtra("msg");
				if (m != null) {
					datas.add(m);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	private void unregisterReceiver() {
		try {
			LocalBroadcastManager.getInstance(act).unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData() {
		if (isOpen) {
			AsyncUtil.goAsync(new Callable<Result<List<CameraDetail>>>() {

				@Override
				public Result<List<CameraDetail>> call() throws Exception {
					Result<List<CameraDetail>> ret = new Result<List<CameraDetail>>();
					try {
						List<CameraDetail> cache = dbUtils
								.findAll(Selector.from(CameraDetail.class).orderBy("id", false));
						if (cache == null) {
							cache = new ArrayList<CameraDetail>();
						}
						ret.setData(cache);
					} catch (Exception e) {
						ret.setThrowable(e);
						ExceptionHandler.handleException(act, ret);
					}
					return ret;
				}
			}, new Callback<Result<List<CameraDetail>>>() {

				@Override
				public void onHandle(Result<List<CameraDetail>> result) {
					if (result.ok()) {
						datas.clear();
						datas.addAll(result.getData());
						adapter.notifyDataSetChanged();
					} else {
						UIUtils.toast(act, "没有订阅到数据", Toast.LENGTH_SHORT);
					}
				}
			});

			/*
			 * final MyProgressDialog d = Windows.waiting(act);
			 * AsyncUtil.goAsync(new
			 * Callable<Result<List<VehiclePositionModex1>>>() {
			 *
			 * @Override public Result<List<VehiclePositionModex1>> call()
			 * throws Exception { return WeiZhanData.queryKeYiPositionInfo1(
			 * 116.359777 + "", 39.944206 + "",500+""); } }, new
			 * Callback<Result<List<VehiclePositionModex1>>>() {
			 *
			 * @Override public void
			 * onHandle(Result<List<VehiclePositionModex1>> result) { if
			 * (result.ok()) { d.dismiss(); if
			 * (result.getData().toString().equals("[]")) { return; }
			 * arrayList.addAll(result.getData());
			 * adapter.notifyDataSetChanged(); } else { UIUtils.toast(act,
			 * result.getErrorMsg(), Toast.LENGTH_SHORT); } } });
			 */
		}
	}

	int bestDistance = 0;
	SimpleAdapter adapter;
	double lat = 0.0;
	double lon = 0.0;

	private class SimpleAdapter extends BaseAdapter {

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

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(act).inflate(R.layout.checkdetailwarnning, null);
				convertView.setTag(holder);
				holder.car_yujing_distance = (TextView) convertView.findViewById(R.id.car_yujing_distance);
				holder.car_number_yujing = (TextView) convertView.findViewById(R.id.car_number_yujing);
				holder.car_yujing_type = (TextView) convertView.findViewById(R.id.car_yujing_type);
				holder.image_yujing_car = (ImageView) convertView.findViewById(R.id.image_yujing_car);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LatLng des = null;
			LatLng des1 = null;

			CameraDetail item = (CameraDetail) getItem(position);
			holder.car_number_yujing.setText(item.getVname());
			LatLng ll = new LatLng(item.getLat(), item.getLon());
			des = ll;
			des1 = new LatLng(lat, lon);
			bestDistance = 0;
			bestDistance += DistanceUtil.getDistance(des1, des);
			if (HasDigit(item.getAlarmReason())) {
				String disrance = GetNumber(item.getAlarmReason());
				if(item.getAlarmReason().indexOf("公里")!=-1){
					holder.car_yujing_distance.setText(disrance+"km");
				}else{
					holder.car_yujing_distance.setText(disrance+"m");
				}

			} else {
				holder.car_yujing_distance.setText(showDistance(bestDistance));
			}

			Log.e("push", "pushadapter:" + item.getAlarmReason());
			holder.car_yujing_type.setText(item.getAlarmReason());
			holder.car_yujing_type.setTextColor(Color.parseColor("#FF0000"));
			holder.image_yujing_car.setImageResource(R.drawable.weigui_yujing_kelong);

			// if(item.getAlarmReason().contains("克隆车")){
			// holder.car_yujing_type.setText("克隆车");
			// holder.car_yujing_type.setTextColor(Color.parseColor("#FF0000"));
			// holder.image_yujing_car.setImageResource(R.drawable.weigui_yujing_kelong);
			// }else{
			// holder.car_yujing_type.setText("可疑车辆");
			// holder.car_yujing_type.setTextColor(Color.parseColor("#ffa200"));
			// holder.image_yujing_car.setImageResource(R.drawable.weigui_yujing_hangye);
			// }
			return convertView;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	private class ViewHolder {
		TextView car_yujing_distance;
		TextView car_number_yujing;
		ImageView image_yujing_car;
		TextView car_yujing_type;
	}

	private void Position() {
		if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
			lat = MsgConfig.select_lat;
			lon = MsgConfig.select_lng;
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			lat = MsgConfig.lat;
			lon = MsgConfig.lng;
		} else {

		}
	}

	public static DecimalFormat distanceFormate = new DecimalFormat("0.0");

	/**
	 * 判断是M还是KM
	 */
	public static String showDistance(double d) {
		if (d < 1000) {
			return (int) d + "m";
		} else {
			String i = distanceFormate.format(d / 1000.0) + "km";
			return i;
		}
	}

	// 判断一个字符串是否含有数字
	public boolean HasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}

	public String GetNumber(String a) {

		char[] b = a.toCharArray();
		String result = "";
		for (int i = 0; i < b.length; i++) {
			if (("0123456789.").indexOf(b[i] + "") != -1) {
				result += b[i];
			}
		}
		return result;
	}
}
