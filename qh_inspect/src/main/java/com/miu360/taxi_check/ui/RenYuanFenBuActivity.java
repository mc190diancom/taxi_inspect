package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.inspect.R.drawable;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.AllPosition;
import com.miu360.taxi_check.model.PersonPosition;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow.LayoutParams;

public class RenYuanFenBuActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.bmapView)
	private MapView mMapView;
	@ViewInject(R.id.car_number_two)
	private EditText number;
	@ViewInject(R.id.show_list)
	private ListView show_list;
	@ViewInject(R.id.search_btn)
	private ImageButton search_btn;
	@ViewInject(R.id.choose)
	private TextView choose;
	@ViewInject(R.id.ll_bottom_renyuan)
	private LinearLayout ll_bottom_renyuan;
	@ViewInject(R.id.ll_show_location)
	private LinearLayout ll_show_location;
	@ViewInject(R.id.ll_bottom_renyuans)
	private LinearLayout ll_bottom_renyuans;
	@ViewInject(R.id.renyuan_number)
	private TextView renyuan_number;
	@ViewInject(R.id.add_map)
	private ImageButton add_map;
	@ViewInject(R.id.reduce_map)
	private ImageButton reduce_map;
	@ViewInject(R.id.image_yujing_above)
	private ImageButton image_yujing_above;
	@ViewInject(R.id.list_RenYuanDetails)
	private ListView list_RenYuanDetails;
	@ViewInject(R.id.aim_location_return)
	private ImageButton aim_location_return;


	BaiduMap mBaiduMap;
	String[] itemOne = { "大队", "姓名" };
	//是否选择的是人员
	boolean isRY = false;
	//预选列表进行了选择
	boolean isPushChoose = false;
	RenYuanAdapter ryAdapter;

	simpleAdapter adapter;
	Marker mCurrentMarker;
	// View view;
	// String dadui;
	// String type;
	// ArrayList<PersonPosition> positionList;
	// ArrayList<PersonPosition> nameList = new ArrayList<>();
	// ArrayList<Marker> markerList = new ArrayList<>();

	private class simpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return nameList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return nameList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			NameHolder holder;
			if (arg1 == null) {
				holder = new NameHolder();
				arg1 = LayoutInflater.from(self).inflate(R.layout.listlicenceadapter, arg2, false);
				arg1.setTag(holder);
				holder.name = (TextView) arg1.findViewById(R.id.companyName);

			} else {
				holder = (NameHolder) arg1.getTag();
			}
			if(isRY){
				holder.name.setText(nameList.get(arg0).getPersonName());
			}else{
				holder.name.setText(nameList.get(arg0).getSsdd());
			}
			return arg1;
		}

	}

	private class NameHolder {
		TextView name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ren_yuan_fen_bu);
		initView();
		AllQuery();
		initData();
		ryAdapter = new RenYuanAdapter();
		//绑定人员详情信息list
		list_RenYuanDetails.setAdapter(ryAdapter);
		list_RenYuanDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				list_RenYuanDetails.setVisibility(View.GONE);
				ll_show_location.setVisibility(View.VISIBLE);
				ll_bottom_renyuans.setVisibility(View.VISIBLE);
				image_yujing_above.setVisibility(View.GONE);

				PersonPosition p = (PersonPosition) parent.getItemAtPosition(position);
				LatLng ll = new LatLng(p.getLat(), p.getLon());
				MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			}
		});

		adapter = new simpleAdapter();
		show_list.setAdapter(adapter);
		show_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				PersonPosition p = (PersonPosition) arg0.getItemAtPosition(arg2);
				//如果ListView显示的人员就利用人员查询
				if(isRY){
					dadui = p.getPersonName();
					isPushChoose = true;
					number.setText(dadui);
					NameQuery();
				}
				else {
					dadui = p.getSsdd();
					isPushChoose = true;
					number.setText(dadui);
					DaDuiQuery(p.getSsdd());
				}
				show_list.setVisibility(View.GONE);
			}
		});

	}

	private InfoWindow mInfoWindow;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "人员分布");
		aim_location_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Position();
			}
		});
		choose.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (choose.getText().toString().equals("姓名")) {
					number.setHint("请输入要查询的人员名称");
					isRY = true;
				} else {
					number.setHint("请输入要查询的大队名称");
					isRY = false;
				}
				number.setText("");
				mBaiduMap.clear();
				ll_bottom_renyuan.setVisibility(View.GONE);
			}
		});

		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		search_btn.setOnClickListener(this);
		choose.setOnClickListener(this);
		add_map.setOnClickListener(this);
		reduce_map.setOnClickListener(this);
		image_yujing_above.setOnClickListener(this);
		ll_bottom_renyuan.setOnClickListener(this);
		ll_bottom_renyuan.setVisibility(View.GONE);
		mBaiduMap.setMaxAndMinZoomLevel(18, 11);
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if(list_RenYuanDetails.getVisibility() == View.VISIBLE){
					list_RenYuanDetails.setVisibility(View.GONE);
					ll_show_location.setVisibility(View.VISIBLE);
					ll_bottom_renyuans.setVisibility(View.VISIBLE);
					image_yujing_above.setVisibility(View.GONE);
				}
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker markers) {
				view  = LayoutInflater.from(self).inflate(R.layout.renyuanfenbuwindow, null);
				if(mCurrentMarker == markers){
					mBaiduMap.hideInfoWindow();
					mCurrentMarker = null;
					return true;
				}
				for (int i = 0; i < markerList.size(); i++) {
					Marker marker = markerList.get(i);
					if (markers == marker) {
						//创建InfoWindow展示的view
						view.setLayoutParams(new LayoutParams());
						TextView name = (TextView) view.findViewById(R.id.zhifa_name);
						TextView dadui = (TextView) view.findViewById(R.id.zhifa_dadui);
						final TextView address = (TextView) view.findViewById(R.id.address);
						TextView time = (TextView) view.findViewById(R.id.time);
						Bundle b = marker.getExtraInfo();
						final PersonPosition p = (PersonPosition) b.getSerializable("" + i);
						/*LatLng llPerson = new LatLng(p.getLat(), p.getLon());
						CoordinateConverter cover = new CoordinateConverter();
						cover.from(CoordType.GPS);
						cover.coord(llPerson);
						LatLng des = cover.convert();*/
						name.setText(p.getPersonName());
						dadui.setText(p.getSsdd());
						time.setText(isNull.isEmpty(p.getTime()));
						Log.e("renyuan", "renyuan:"+p.getLat()+"==="+p.getLon());
						//reverseGeoCode1(address,p.getLat(), p.getLon());
						AsyncUtil.goAsync(new Callable<Result<String>>() {

							@Override
							public Result<String> call() throws Exception {
								return HistoryData.queryHistoryTrack1(p.getLat(),p.getLon());
							}
						}, new Callback<Result<String>>() {

							@Override
							public void onHandle(Result<String> result) {
								if (result.ok()) {
									address.setText(result.getData());
									mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), new LatLng(p.getLat(), p.getLon()), -90, null);
									mBaiduMap.showInfoWindow(mInfoWindow);
									mCurrentMarker = markers;
								}
							}
						});

					}
				}

				return true;
			}
		});

	}

	View view;


	private void initData() {
		number.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				//如果这个是预选框引起的改变
				if(isPushChoose){
					//show_list.setVisibility(View.GONE);
					isPushChoose = false;
				}else{
					nameList.clear();
					dadui = number.getText().toString();
					type = choose.getText().toString();
					show_list.setVisibility(View.GONE);
					if (type.equals("大队")) {
						if (dadui.length() < 1) {
							return;
						}
						final PersonPosition p = new PersonPosition();
						p.setSsdd(dadui);
						AsyncUtil.goAsync(new Callable<Result<List<PersonPosition>>>() {

							@Override
							public Result<List<PersonPosition>> call() throws Exception {
								return WeiZhanData.queryDaDuiNameInfo(p);
							}
						}, new Callback<Result<List<PersonPosition>>>() {

							@Override
							public void onHandle(Result<List<PersonPosition>> result) {
								if (result.ok()) {
									if (result.getData().toString().equals("[]")) {
										UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
										return;
									}
									show_list.setVisibility(View.VISIBLE);
									nameList.addAll(result.getData());
									adapter.notifyDataSetChanged();

								} else {
									UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});
					}else{
						if (dadui.length() < 1) {
							return;
						}
						positionList = new ArrayList<>();
						final PersonPosition info = new PersonPosition();
						info.setPersonName(dadui);
						AsyncUtil.goAsync(new Callable<Result<List<PersonPosition>>>() {

							@Override
							public Result<List<PersonPosition>> call() throws Exception {
								return WeiZhanData.queryPositionInfo(info);
							}
						}, new Callback<Result<List<PersonPosition>>>() {

							@Override
							public void onHandle(Result<List<PersonPosition>> result) {
								if (result.ok()) {
									if (result.getData().toString().equals("[]")) {
										UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
										return;
									}
									show_list.setVisibility(View.VISIBLE);
									nameList.addAll(result.getData());
									adapter.notifyDataSetChanged();
								} else {
									UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
								}
							}
						});
					}
				}

			}
		});

	}

	String dadui;
	String type;
	String typeName;
	ArrayList<PersonPosition> positionList = new ArrayList<>();;
	ArrayList<AllPosition.rows> positionList1;
	ArrayList<PersonPosition> nameList = new ArrayList<>();
	ArrayList<Marker> markerList = new ArrayList<>();
	BitmapDescriptor bdOnline = BitmapDescriptorFactory.fromResource(R.drawable.zfry_online);
	BitmapDescriptor bdOffline = BitmapDescriptorFactory.fromResource(R.drawable.zfry_offline);
	BitmapDescriptor ba = BitmapDescriptorFactory.fromResource(R.drawable.mine_renyuan);


	private void DaDuiQuery(String ssdd) {
		mBaiduMap.clear();
		final MyProgressDialog pd = Windows.waiting(self);
		positionList.clear();
		positionList1.clear();
		final PersonPosition info = new PersonPosition();
		info.setSsdd(ssdd);
		AsyncUtil.goAsync(new Callable<Result<List<PersonPosition>>>() {

			@Override
			public Result<List<PersonPosition>> call() throws Exception {
				return WeiZhanData.queryPositionInfo(info);
			}
		}, new Callback<Result<List<PersonPosition>>>() {

			@Override
			public void onHandle(Result<List<PersonPosition>> result) {
				pd.dismiss();

				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
						return;
					}
					positionList.addAll(result.getData());
					ll_bottom_renyuan.setVisibility(View.VISIBLE);
					LatLng ll = null;
					int num=0;
					if(!positionList.isEmpty()) {
						for (int i = 0; i < positionList.size(); i++) {
							ll = new LatLng(positionList.get(i).getLat(), positionList.get(i).getLon());
							MarkerOptions op = null;
							if (OnOrOffline(positionList.get(i).getTime())) {
								op = new MarkerOptions().position(ll).icon(bdOnline).zIndex(9).draggable(true);
							} else {
								op = new MarkerOptions().position(ll).icon(bdOffline).zIndex(9).draggable(true);
							}

							Marker marker = (Marker) mBaiduMap.addOverlay(op);
							Bundle b = new Bundle();
							b.putSerializable("" + markerList.size(), positionList.get(i));
							marker.setExtraInfo(b);
							markerList.add(marker);
							num++;
						}
					}
					renyuan_number.setText(num+"");
					MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(12).build();

					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	//判断位置信息上传时间是否是实时的
    public  SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean OnOrOffline(String time) {
        try {
            Date date = yyyyMMdd_HHmmss.parse(time);
            if((System.currentTimeMillis() - date.getTime()) < 300 * 1000){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void AllQuery() {
		//final MyProgressDialog pd = Windows.waiting(self);
		mBaiduMap.clear();
		positionList1 = new ArrayList<>();
		positionList.clear();
		AsyncUtil.goAsync(new Callable<Result<AllPosition>>() {

			@Override
			public Result<AllPosition> call() throws Exception {
				return WeiZhanData.queryAllPositionInfo();
			}
		}, new Callback<Result<AllPosition>>() {

			@Override
			public void onHandle(Result<AllPosition> result) {
				//pd.dismiss();
				if (result.ok()) {
					Log.e("position", result.getData().toString());
					if (result.getData()==null || result.getData().getRows()==null) {
						UIUtils.toast(self, "没有执法人员", Toast.LENGTH_LONG);
						return;
					}
					if (result.getData().getRows().size()==0) {
						UIUtils.toast(self, "没有执法人员", Toast.LENGTH_LONG);
						return;
					}
					positionList1.addAll(result.getData().getRows());
					ll_bottom_renyuan.setVisibility(View.VISIBLE);
					LatLng ll = null;
					PersonPosition p = null;
					int num =0;
					if(!positionList1.isEmpty()) {
						for (int i = 0; i < positionList1.size(); i++) {
							ll = new LatLng(positionList1.get(i).getLAT(), positionList1.get(i).getLON());

							MarkerOptions op;
							if (OnOrOffline(positionList1.get(i).getTIME())) {
								op = new MarkerOptions().position(ll).icon(bdOnline).zIndex(9).draggable(true);
							} else {
								op = new MarkerOptions().position(ll).icon(bdOffline).zIndex(9).draggable(true);
							}

							Marker marker = (Marker) mBaiduMap.addOverlay(op);
							Bundle b = new Bundle();
							p = new PersonPosition();
							p.setAccount(positionList1.get(i).getACCOUNT());
							p.setLat(positionList1.get(i).getLAT());
							p.setLon(positionList1.get(i).getLON());
							p.setPersonName(positionList1.get(i).getPERSONNAME());
							p.setSsdd(positionList1.get(i).getSSDD());
							p.setTime(positionList1.get(i).getTIME());
							positionList.add(p);
							b.putSerializable("" + markerList.size(), p);
							marker.setExtraInfo(b);
							markerList.add(marker);
							num++;
						}
						ryAdapter.notifyDataSetChanged();
					}
					renyuan_number.setText(num+"");
    					/*LatLng llMine = new LatLng(MsgConfig.lat, MsgConfig.lng);
    					MarkerOptions ops = new MarkerOptions().position(llMine).icon(ba).zIndex(5).draggable(true);
    					mBaiduMap.addOverlay(ops);*/
					MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(12).build();

					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	private void NameQuery() {
		if (TextUtils.isEmpty(dadui)) {
			//UIUtils.toast(self, "请输入执法人员姓名", Toast.LENGTH_LONG);
			AllQuery();
			return;
		}

		final MyProgressDialog pd = Windows.waiting(self);
		positionList.clear();
		final PersonPosition info = new PersonPosition();
		info.setPersonName(dadui);
		AsyncUtil.goAsync(new Callable<Result<List<PersonPosition>>>() {

			@Override
			public Result<List<PersonPosition>> call() throws Exception {
				return WeiZhanData.queryPositionInfo(info);
			}
		}, new Callback<Result<List<PersonPosition>>>() {

			@Override
			public void onHandle(Result<List<PersonPosition>> result) {
				pd.dismiss();

				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
						return;
					}
					positionList.addAll(result.getData());
					Log.e("positionList","positionList:"+positionList);
					ll_bottom_renyuan.setVisibility(View.VISIBLE);
					int num =0;
					if(!positionList.isEmpty()){
						for (int i = 0; i < positionList.size(); i++) {
							LatLng ll = new LatLng(positionList.get(i).getLat(), positionList.get(i).getLon());

							Log.e("转换前的坐标", ll.toString());
							MarkerOptions op;
							if(OnOrOffline(positionList.get(i).getTime())){
								op = new MarkerOptions().position(ll).icon(bdOnline).zIndex(9).draggable(true);
							}else{
								op = new MarkerOptions().position(ll).icon(bdOffline).zIndex(9).draggable(true);
							}

							Marker marker = (Marker) mBaiduMap.addOverlay(op);
							Bundle b = new Bundle();
							b.putSerializable("" + markerList.size(), positionList.get(i));
							marker.setExtraInfo(b);
							markerList.add(marker);
							MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(12).build();

							mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
							num++;
						}
					}
					renyuan_number.setText(num+"");
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == search_btn) {
			typeName = choose.getText().toString();
			mBaiduMap.clear();
			if (typeName.equals("姓名")) {
				NameQuery();
			} else {
				if (TextUtils.isEmpty(dadui)) {
					//UIUtils.toast(self, "请输入大队名称", Toast.LENGTH_LONG);
					AllQuery();
					return;
				}
				DaDuiQuery(dadui);
			}

		} else if (v == choose) {
			Windows.singleChoice(self, "请选择", itemOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					choose.setText(itemOne[position]);
				}
			});
		}else if (v == ll_bottom_renyuan) {
			//if (list_RenYuanDetails.getVisibility() == View.GONE) {
			ryAdapter.notifyDataSetChanged();
			ll_bottom_renyuans.setVisibility(View.GONE);
			list_RenYuanDetails.setVisibility(View.VISIBLE);
			image_yujing_above.setVisibility(View.VISIBLE);
			ll_show_location.setVisibility(View.GONE);
			//}
		}else if (v == image_yujing_above) {
			//if (list_RenYuanDetails.getVisibility() == View.VISIBLE) {
			list_RenYuanDetails.setVisibility(View.GONE);
			ll_show_location.setVisibility(View.VISIBLE);
			ll_bottom_renyuans.setVisibility(View.VISIBLE);
			image_yujing_above.setVisibility(View.GONE);
			//}
		} else if (v == add_map) {
			if(mBaiduMap != null){
				MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
				mBaiduMap.setMapStatus(zoomIn);
			}
		} else if (v == reduce_map) {
			if(mBaiduMap != null){
				MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
				mBaiduMap.setMapStatus(zoomOut);
			}
		}
	}

	String addressZF;

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
					addressZF = result.getData();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ptCenter, -90, null);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
			}
		});
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode1(final TextView tv, final double lat,final double lng) {
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return HistoryData.queryHistoryTrack1(lat,lng);
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
	private class RenYuanAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return positionList.size();
		}

		@Override
		public Object getItem(int position) {
			return positionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RenYuanHolder holder;
			if (convertView == null) {
				holder = new RenYuanHolder();
				convertView = LayoutInflater.from(self).inflate(R.layout.renyuan_details, parent, false);
				convertView.setTag(holder);
				holder.name = (TextView) convertView.findViewById(R.id.tv_renyuan_name);
				holder.img_zfry = (ImageView) convertView.findViewById(R.id.img_zfry);
				holder.address = (TextView) convertView.findViewById(R.id.tv_renyuan_address);
				holder.dd = (TextView) convertView.findViewById(R.id.tv_renyuan_dd);
				holder.time = (TextView) convertView.findViewById(R.id.tv_renyuan_time);
			} else {
				holder = (RenYuanHolder) convertView.getTag();
			}
			holder.name.setText(positionList.get(position).getPersonName());
			reverseGeoCode1(holder.address,positionList.get(position).getLat(),positionList.get(position).getLon());
			holder.dd.setText(positionList.get(position).getSsdd());
			holder.time.setText(positionList.get(position).getTime());
			if(OnOrOffline(positionList.get(position).getTime())){
				holder.img_zfry.setImageResource(drawable.zfry_online);
			}else{
				holder.img_zfry.setImageResource(drawable.zfry_offline);
			}

			return convertView;
		}

	}

	double lat,lon;
	//初始化定位
	private void Position(){
		if(mBaiduMap != null){
			if(!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0){
				lat = MsgConfig.select_lat;
				lon = MsgConfig.select_lng;
				LatLng ll = new LatLng(lat, lon);
				MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			}else if(!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0){
				lat = MsgConfig.lat;
				lon = MsgConfig.lng;
				LatLng ll = new LatLng(lat, lon);
				MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16).build();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
			}else{
				UIUtils.toast(self, "当前GPS信号弱，请到开阔地段", Toast.LENGTH_SHORT);
			}
		}
	}

	private class RenYuanHolder{
		TextView name;
		TextView address;
		TextView dd;
		TextView time;
		ImageView img_zfry;
	}
}


