package com.miu360.taxi_check.fragment;

import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.adapter.HtmlAdapter;
import com.miu360.taxi_check.adapter.PersonQrAdapter;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.model.CardQr;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.NetPersonQrInfo;
import com.miu360.taxi_check.ui.QueryQrRecordActivity;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.taxi_check.view.ListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.miu360.inspect.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class IDFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener2<ListView> {

	ListViewHolder holderID;
	ArrayList<String> arrayList = new ArrayList<>();

	simpleAdapter mAdapter;
	ArrayList<Driver> list = new ArrayList<>();

	HtmlAdapter mAdapter3;
	ArrayList<String> list3 = new ArrayList<>();

	PersonQrAdapter mAdapter2;
	ArrayList<NetPersonQrInfo> list2 = new ArrayList<>();

	UserPreference pref;
	BitmapUtils mBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_id, null);
		pref = new UserPreference(act);
		initView(view);
		initData();
		Intent intent = new Intent(act, CaptureActivity.class);
		startActivityForResult(intent, 0);
		return view;
	}

	private void initView(View root) {
		ViewUtils.inject(this, root);
		mBitmap = new BitmapUtils(act);
		mAdapter = new simpleAdapter();

		holderID = ListViewHolder.initList(act, root);
		holderID.list.setAdapter(mAdapter);
		holderID.list.setMode(Mode.DISABLED);
		holderID.list.setOnItemClickListener(this);
		holderID.list.setRefreshing();

		getActivity().findViewById(R.id.iv_mine).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act, CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		getActivity().findViewById(R.id.history).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act, QueryQrRecordActivity.class);
				startActivity(intent);
			}
		});

	}

	/*//检测相机是否开启
	private boolean isOpenCamera(){
		PackageManager pm = act.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.INTERNET", "packageName"));
        if (permission) {
        	return true;
        }else {
        	return false;
        }
	}*/

	private void initData() {
		holderID.mayShowEmpty(list.size());
		holderID.list.onRefreshComplete();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != android.app.Activity.RESULT_OK) {
			return;
		}
		CardQr cardQr = new CardQr();
		list3.clear();
		String carQrInfo = data.getStringExtra("result");
		if(carQrInfo.isEmpty()){
			return;
		}
		list3.add(carQrInfo);
		mAdapter3 = new HtmlAdapter(act, list3);
		holderID.list.setAdapter(mAdapter3);
		mAdapter.notifyDataSetChanged();
		holderID.mayShowEmpty(list3.size());
		holderID.list.onRefreshComplete();
		cardQr.setPath(carQrInfo);
		if(2==data.getIntExtra("type", 1)){
			cardQr.setId("2");
		}else if(3==data.getIntExtra("type", 1)){
			cardQr.setId("3");
		}else{
			cardQr.setId("1");
		}
		ArrayList<CardQr> qrlist = Store2SdUtil.getInstance(act)
				.readInArrayObject(Config.ID_FILE_NAME,
						new TypeToken<ArrayList<CardQr>>() {
						});
		for(int i=0;i<qrlist.size();i++){
			if(i>8){
				qrlist.remove(i);
			}else{
				if(carQrInfo.equals(qrlist.get(i).getPath())){
					Log.e("cardqr", "cardqr:"+i);
					qrlist.remove(i);
				}
			}
		}

		qrlist.add(0,cardQr);
		Store2SdUtil.getInstance(act).addOut2(qrlist, Config.ID_FILE_NAME);
		/*if("person_info".equals(data.getStringExtra("type"))){
			list.clear();
			list2.clear();
			list3.clear();
			holderID.list.setAdapter(mAdapter);
			Driver d = (Driver) data.getSerializableExtra("result");
			Log.e("返回值", "返回值："+d.toString());
			long time = System.currentTimeMillis();
			d.setTime(time);
			list.add(d);
			Log.e("路径", "路径："+list.get(0).getHead());
			holderID.list.onRefreshComplete();
			mAdapter.notifyDataSetChanged();
			initData();
			final HistoryIDCard info = new HistoryIDCard();
			info.setCOMPNAME(isNull.isnl(d.getCompany()));
			info.setDRIVERNAME(isNull.isnl(d.getName()));
			info.setFWM(isNull.isnl(d.getFangweima()));
			info.setPHONE(isNull.isnl(d.getPhoneNumber()));
			info.setIMAGE_PASH(isNull.isnl(d.getHead()));
			info.setSEX(isNull.isnl(d.getSex()));
			info.setZFZH(isNull.isnl(pref.getString("user_name", null)));
			info.setSMSJ(isNull.isnl(d.getTime() + ""));
			info.setJDKH(isNull.isnl(d.getDriverLicence()));
			info.setDRIV_AGE("");
			info.setDRIV_FWM("");
			info.setDRIV_NAME("");
			info.setDRIV_SEX("");
			info.setDRIV_TYPE("");
			info.setDRIV_VALIDITY("");
			info.setDRIV_VNAME("");
			info.setVEH_COLOR("");
			info.setVEH_CJH("");
			info.setVEH_CORPNAME("");
			info.setVEH_FDJH("");
			info.setVEH_FWM("");
			info.setVEH_PTNAME("");
			info.setVEH_STATUS("");
			info.setVEH_TYPE("");
			info.setVEH_VALIDITY(isNull.isnl(d.getTime() + ""));
			info.setVEH_VNAME("");
			info.setFLAG("1");
			AsyncUtil.goAsync(new Callable<Result<Long>>() {

				@Override
				public Result<Long> call() throws Exception {
					return UserData.shangChuanHistoryQrData(info);
				}
			}, new Callback<Result<Long>>() {

				@Override
				public void onHandle(Result<Long> result) {
					if (result.ok()) {
					} else {
						UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}else if("net_car_info".equals(data.getStringExtra("type"))){
			list.clear();
			list2.clear();
			list3.clear();
			String carQrInfo = data.getStringExtra("result");
			list3.add(carQrInfo);
			mAdapter3 = new HtmlAdapter(act, list3);
			holderID.list.setAdapter(mAdapter3);
			mAdapter.notifyDataSetChanged();
			holderID.mayShowEmpty(list3.size());
			holderID.list.onRefreshComplete();
			final HistoryIDCard info = new HistoryIDCard();
			info.setVEH_COLOR(isNull.isnl(carQrInfo.getCarColor()));
			info.setVEH_CJH(isNull.isnl(carQrInfo.getVIN()));
			info.setVEH_CORPNAME(isNull.isnl(carQrInfo.getCarOwn()));
			info.setVEH_FDJH(isNull.isnl(carQrInfo.getEngineNO()));
			info.setVEH_FWM(isNull.isnl(carQrInfo.getSecurityCode()));
			info.setVEH_PTNAME(isNull.isnl(carQrInfo.getPlatformName()));
			info.setVEH_STATUS(isNull.isnl(carQrInfo.getCarState()));
			info.setVEH_TYPE(isNull.isnl(carQrInfo.getCarModel()));
			info.setVEH_VALIDITY(isNull.isnl(carQrInfo.getDate()));
			info.setVEH_VNAME(isNull.isnl(carQrInfo.getVName()));

			info.setDRIV_AGE("");
			info.setDRIV_FWM("");
			info.setDRIV_NAME("");
			info.setDRIV_SEX("");
			info.setDRIV_TYPE("");
			info.setDRIV_VALIDITY("");
			info.setDRIV_VNAME("");
			info.setDRIVERNAME("");

			info.setCOMPNAME("");
			info.setDRIVERNAME("");
			info.setFWM("");
			info.setPHONE("");
			info.setIMAGE_PASH("");
			info.setSEX("");
			info.setZFZH(isNull.isnl(pref.getString("user_name", null)));
			info.setSMSJ(System.currentTimeMillis()+"");
			info.setJDKH("");
			info.setFLAG("3");
			AsyncUtil.goAsync(new Callable<Result<Long>>() {

				@Override
				public Result<Long> call() throws Exception {
					return UserData.shangChuanHistoryQrData(info);
				}
			}, new Callback<Result<Long>>() {

				@Override
				public void onHandle(Result<Long> result) {
					if (result.ok()) {
					} else {
						UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}else if("net_person_info".equals(data.getStringExtra("type"))){
			list.clear();
			list2.clear();
			list3.clear();
			String personQrInfo = data.getStringExtra("result");
			list3.add(personQrInfo);
			mAdapter3 = new HtmlAdapter(act, list3);
			holderID.list.setAdapter(mAdapter3);
			mAdapter.notifyDataSetChanged();
			holderID.mayShowEmpty(list3.size());
			holderID.list.onRefreshComplete();
			list.clear();
			list2.clear();
			list3.clear();
			NetPersonQrInfo personQrInfo = (NetPersonQrInfo)data.getSerializableExtra("result");
			list2.add(personQrInfo);
			mAdapter2 = new PersonQrAdapter(act, list2);
			holderID.list.setAdapter(mAdapter2);
			mAdapter.notifyDataSetChanged();
			holderID.mayShowEmpty(list2.size());
			holderID.list.onRefreshComplete();
			final HistoryIDCard info = new HistoryIDCard();
			info.setDRIV_AGE(isNull.isnl(personQrInfo.getAGE()));
			info.setDRIV_NAME(isNull.isnl(personQrInfo.getNM()));
			info.setDRIV_FWM(isNull.isnl(personQrInfo.getFWM()));
			info.setDRIV_SEX(isNull.isnl(personQrInfo.getXB()));
			info.setDRIV_TYPE(isNull.isnl(personQrInfo.getPERSONTYPE()));
			info.setDRIV_VALIDITY(isNull.isnl(personQrInfo.getZCYXQ()));
			info.setDRIV_VNAME(isNull.isnl(personQrInfo.getCPH()));
			info.setPHONE(isNull.isnl(personQrInfo.getPCODE()));//这里由于数据库忘了给字段，这里的其实是pcode
			info.setSMSJ(System.currentTimeMillis()+"");//这里由于数据库忘了给字段，这里的其实是pictime

			info.setVEH_COLOR("");
			info.setVEH_CJH("");
			info.setVEH_CORPNAME("");
			info.setVEH_FDJH("");
			info.setVEH_FWM("");
			info.setVEH_PTNAME("");
			info.setVEH_STATUS("");
			info.setVEH_TYPE("");
			info.setVEH_VALIDITY(isNull.isnl(personQrInfo.getPICTIME()));
			info.setVEH_VNAME("");

			info.setCOMPNAME("");
			info.setDRIVERNAME("");
			info.setFWM("");
			info.setIMAGE_PASH("");
			info.setSEX("");
			info.setZFZH(isNull.isnl(pref.getString("user_name", null)));
			info.setJDKH("");
			info.setFLAG("2");
			AsyncUtil.goAsync(new Callable<Result<Long>>() {

				@Override
				public Result<Long> call() throws Exception {
					return UserData.shangChuanHistoryQrData(info);
				}
			}, new Callback<Result<Long>>() {

				@Override
				public void onHandle(Result<Long> result) {
					if (result.ok()) {
					} else {
						UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}*/

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	private class simpleAdapter extends BaseAdapter {

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
			viewHolder holder = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(act).inflate(R.layout.idscanleadapter, null);
				holder = new viewHolder();
				arg1.setTag(holder);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.sex = (TextView) arg1.findViewById(R.id.sex);
				holder.code = (TextView) arg1.findViewById(R.id.code);
				holder.time = (TextView) arg1.findViewById(R.id.time);
				holder.fangweima = (TextView) arg1.findViewById(R.id.fangweima);
				holder.corpName = (TextView) arg1.findViewById(R.id.corpName);
				holder.phoneNumber = (TextView) arg1.findViewById(R.id.phoneNumber);
				holder.header_image = (ImageView) arg1.findViewById(R.id.header_image);
			} else {
				holder = (viewHolder) arg1.getTag();
			}
			holder.name.setText(list.get(arg0).getName());

			holder.sex.setText(list.get(arg0).getSex());
			holder.code.setText(list.get(arg0).getDriverLicence());
			holder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(list.get(0).getTime())));
			holder.fangweima.setText(list.get(arg0).getFangweima());
			holder.corpName.setText(list.get(arg0).getCompany());
			holder.phoneNumber.setText(list.get(arg0).getPhoneNumber());//list.get(arg0).getHead()
			Log.e("图片路径", "图片路径"+Config.SERVER_TAXIINFO+"?type="+"queryIdPhoto"
					+"&pcode=362263&picctime=2017-03-06%12:03:00");
			mBitmap.display(holder.header_image,list.get(arg0).getHead());

			return arg1;
		}

		private class viewHolder {
			TextView name;
			TextView sex;
			TextView code;
			TextView time;
			TextView fangweima;
			TextView corpName;
			TextView phoneNumber;
			ImageView header_image;

		}
	}
}
