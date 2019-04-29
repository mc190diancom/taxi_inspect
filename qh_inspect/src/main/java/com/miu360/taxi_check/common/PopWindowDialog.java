package com.miu360.taxi_check.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.mapapi.model.LatLng;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.Inspector;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.data.ZfryPreference;
import com.miu30.common.ui.entity.CheZuResultModel;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.taxi_check.ui.LawInpsectSelectLocationActivity;
import com.miu360.taxi_check.util.UIUtils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PopWindowDialog extends Dialog implements android.view.View.OnClickListener {

	String zfzh2;//用于存储执法人员2的信息

	Context context;
	UserPreference pref;
	ZfryPreference prez;
	ArrayList<CheZuResultModel> arrayListCheZu;
	boolean isTemp;
	Inspector inspector;
	String zfry2name = null;
	boolean isChange;
	//是否选择了常用地址
	boolean iscommon;
	private PositionBroadCast broadcast;
	MapPositionPreference ppfer;
	public boolean ischongdingwei=false;
	public PopWindowDialog(Context context, ArrayList<CheZuResultModel> arrayListCheZu) {
		super(context, R.style.action_sheet);
		this.context = context;
		this.arrayListCheZu = arrayListCheZu;

		//userP.setString("zfry2", value);
		// this.restClient = restClient;
		// this.cache = cache;
	}
	public void  init(boolean isTemp,Inspector inspector) {
		this.isTemp=isTemp;
		this.inspector=inspector;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popwindowinfo);
		context.registerReceiver(broadcast = new PositionBroadCast(),
				new IntentFilter(LawInpsectSelectLocationActivity.CHOSE_LOCATION));
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		win.setAttributes(lp);
		ppfer = new MapPositionPreference(context);
		prez = new ZfryPreference(context);
		pref = new UserPreference(context);
		//如果当前账号为上次登录账号，则获取记录的执法人员2
		if(prez.getString("zfry1", null)!=null){
			if(pref.getString("user_name_update_info", null).contains(prez.getString("zfry1", null))){
				zfry2name = prez.getString("zfry2", null);
				zfzh2 = prez.getString("zfzh2", null);
			}
		}
		initView();
	}

	TextView zhifa_renyuan_one;
	EditText zhifa_renyuan_two;
	TextView zhifa_address;
	TextView zhifa_time;
	ImageButton renyuan_One;
	ImageButton renyuan_Two;
	ImageButton pop_location;
	Button pop_save;
	LatLng latLng = null;
	String name = null;
	String adds = "";

	private void initView() {
		zhifa_renyuan_one = (TextView) findViewById(R.id.renyuan1);
		zhifa_renyuan_two = (EditText) findViewById(R.id.renyuan2);
		zhifa_address = (TextView) findViewById(R.id.address);
		zhifa_time = (TextView) findViewById(R.id.zhifa_time);
		renyuan_One = (ImageButton) findViewById(R.id.renyuan_btn_one);
		renyuan_Two = (ImageButton) findViewById(R.id.renyuan_btn_two);
		pop_location = (ImageButton) findViewById(R.id.pop_location);
		pop_save = (Button) findViewById(R.id.pop_save);
		zhifa_renyuan_two.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(name != null && zhifa_renyuan_two.getText().toString()!=null){
					if(name.contains(zhifa_renyuan_two.getText().toString()) &&
							(zhifa_renyuan_two.getText().toString()).contains(name)){
						UIUtils.toast(context, "两个执法人员名字相等，请改正", Toast.LENGTH_SHORT);
					}else {

					}
				}
			}
		});
		/*else if(isArray(zhifa_renyuan_two.getText().toString(), arrayListCheZu)){
     	   UIUtils.toast(context, "执法人员2不属于该车组。。。", Toast.LENGTH_SHORT);
	    	}*/
		//如果是从临时保存那里跳转过来的
		if(isTemp){
			if(!TextUtils.isEmpty(inspector.getZfry2())){
				zhifa_renyuan_one.setText(inspector.getZfry1());
				zhifa_renyuan_two.setText(inspector.getZfry2());
				zfzh2 = inspector.getZfzh2();
			}else{
				if(zfry2name!=null){
					ELog.d("pop", "dialog:" + "进入");
					zhifa_renyuan_one.setText(inspector.getZfry1());
					zhifa_renyuan_two.setText(zfry2name);
				}else{
					name = pref.getString("user_name_update_info", null);
					zhifa_renyuan_one.setText(name);
					if(arrayListCheZu !=null){
						if (arrayListCheZu.size() > 1) {
							if(arrayListCheZu.get(0).getNAME() != null){
								if (arrayListCheZu.get(0).getNAME().equals(name)) {
									zhifa_renyuan_two.setText(arrayListCheZu.get(1).getNAME());
								} else {
									zhifa_renyuan_two.setText(arrayListCheZu.get(0).getNAME());
								}
							}else{
								zhifa_renyuan_two.setText(arrayListCheZu.get(1).getNAME());
							}
						}else{
							UIUtils.toast(context, "没有车组人员", Toast.LENGTH_SHORT);
						}
					}
				}
			}
			if (!TextUtils.isEmpty(inspector.getZfsj())) {
				Date d = null;
				SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
				try {
					d = sf.parse(inspector.getZfsj());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				zhifa_time.setTag(d.getTime());
				zhifa_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
			}
			zhifa_address.setText(inspector.getAddress());
		}else{
			name = pref.getString("user_name_update_info", null);
			zhifa_renyuan_one.setText(name);
			if(zfry2name!=null){
				zhifa_renyuan_two.setText(zfry2name);
			}else{
				if(arrayListCheZu !=null){
					if (arrayListCheZu.size() > 1) {
						if(arrayListCheZu.get(0).getNAME() != null && arrayListCheZu.get(1).getNAME() !=null){
							if (arrayListCheZu.get(0).getNAME().equals(name)) {
								zhifa_renyuan_two.setText(arrayListCheZu.get(1).getNAME());
							} else {
								zhifa_renyuan_two.setText(arrayListCheZu.get(0).getNAME());
							}
						}else{
							//UIUtils.toast(context, "为什么", Toast.LENGTH_SHORT);
							zhifa_renyuan_two.setText(arrayListCheZu.get(1).getNAME());
						}
					}else{
						UIUtils.toast(context, "没有车组人员", Toast.LENGTH_SHORT);
					}
				}
			}

			long time = System.currentTimeMillis();
			zhifa_time.setTag(time);
			zhifa_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
		}

		zhifa_time.setOnClickListener(this);
		renyuan_One.setOnClickListener(this);
		renyuan_Two.setOnClickListener(this);
		pop_location.setOnClickListener(this);
		pop_save.setOnClickListener(this);
		zhifa_address.setOnClickListener(this);
		if(isTemp){
			if (!TextUtils.isEmpty(inspector.getAddress())) {
				zhifa_address.setText(inspector.getAddress());
			}
		}
		else if(!adds.equals("")){
			zhifa_address.setText(adds);
			return;
		}else if (MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0) {
			zhifa_address.setText(ppfer.getString("selectPosition", ""));
		} else {
			if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
				if(!"".equals(ppfer.getString("selectPosition", ""))){
					zhifa_address.setText(ppfer.getString("selectPosition", ""));
				}else{
					reverseGeoCode1(zhifa_address,MsgConfig.lat,MsgConfig.lng);
				}
			} else {
			}
		}
	}

	//输入的人员是否是同一个车组里面的
	private boolean isArray(String s,ArrayList<CheZuResultModel> arrayListCheZu){
		for(int i=0,len = arrayListCheZu.size();i<len;i++){
			if(s.equals(arrayListCheZu.get(i).getNAME())){//只要有一个相等，就说明有了，那跳出返回true
				return true;
			}
		}
		return false;
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) ||
						MsgConfig.lat == 0.0) {
				}else{
					reverseGeoCode1(zhifa_address, MsgConfig.lat, MsgConfig.lng);
				}
			}

		};
	};

	private void initHandlerAdd(){
		new Handler().postDelayed(new Runnable(){
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 8000);
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

	int exitTime = 0;
	ArrayList<queryZFRYByDWMC> listPeopleName = new ArrayList<>();
	String zfzhtwo;

	@Override
	public void onClick(View v) {
		if (v == zhifa_time) {
			chooseDate(zhifa_time);
		} else if (v == renyuan_One) {
			exitTime++;
			if (exitTime > 1) {
				exitTime = 0;
				return;
			}
			renyuan_One.setClickable(false);
			listPeopleName.clear();
			final queryZFRYByDWMC info = new queryZFRYByDWMC();
			info.setZFDWMC(new UserPreference(context).getString("zfdwmc", null));

			AsyncUtil.goAsync(new Callable<Result<List<queryZFRYByDWMC>>>() {

				@Override
				public Result<List<queryZFRYByDWMC>> call() throws Exception {
					return WeiZhanData.queryZFRYNameinfo(info);
				}
			}, new Callback<Result<List<queryZFRYByDWMC>>>() {

				@Override
				public void onHandle(Result<List<queryZFRYByDWMC>> result) {

					if (result.ok()) {
						listPeopleName.addAll(result.getData());
						for(int i=0,len=listPeopleName.size();i<len;i++){

						}
						int len = listPeopleName.size();
						final String[] itemName = new String[len];
						final String[] itemNum = new String[len];
						for (int i = 0; i < len; i++) {
							itemName[i] = listPeopleName.get(i).getNAME();
							itemNum[i] = listPeopleName.get(i).getZFZH();
						}
						Windows.singleChoice(context, "请选择执法人员", itemName, new OnDialogItemClickListener() {

							@Override
							public void dialogItemClickListener(int position) {
								zhifa_renyuan_one.setText(itemName[position]);
								zfzhtwo = itemNum[position];
							}
						});
					} else {
						UIUtils.toast(context, result.getErrorMsg(), Toast.LENGTH_LONG);
						return;
					}
					renyuan_One.setClickable(true);
				}

			});
		} else if(v == pop_location) {
			Intent intent = new Intent(context, LawInpsectSelectLocationActivity.class);
			intent.putExtra("thisAdd", zhifa_address.getText().toString());
			context.startActivity(intent);
		}else if(v == pop_save) {
			if(name != null && zhifa_renyuan_two.getText().toString()!=null){
				if(name.contains(zhifa_renyuan_two.getText().toString()) &&
						(zhifa_renyuan_two.getText().toString()).contains(name)){
					UIUtils.toast(context, "两个执法人员名字相等，请改正", Toast.LENGTH_SHORT);
					return;
				}
			}
			UIUtils.toast(getContext(), "保存人员信息成功！", Toast.LENGTH_SHORT);
			this.dismiss();
		}else if(v == zhifa_address) {
			Intent intent = new Intent(context, LawInpsectSelectLocationActivity.class);
			intent.putExtra("thisAdd", zhifa_address.getText().toString());
			/*if(TextUtils.isEmpty(zhifa_address.getText().toString())){
				intent.putExtra("thisAdd", "common");
			}else{
				intent.putExtra("thisAdd", zhifa_address.getText().toString());
			}*/
			context.startActivity(intent);

		}else if (v == renyuan_Two) {
			exitTime++;
			if (exitTime > 1) {
				exitTime = 0;
				return;
			}
			renyuan_Two.setClickable(false);
			listPeopleName.clear();
			final queryZFRYByDWMC info = new queryZFRYByDWMC();
			info.setZFDWMC(new UserPreference(context).getString("zfdwmc", null));
			AsyncUtil.goAsync(new Callable<Result<List<queryZFRYByDWMC>>>() {

				@Override
				public Result<List<queryZFRYByDWMC>> call() throws Exception {
					return WeiZhanData.queryZFRYNameinfo(info);
				}
			}, new Callback<Result<List<queryZFRYByDWMC>>>() {

				@Override
				public void onHandle(Result<List<queryZFRYByDWMC>> result) {

					if (result.ok()) {
						listPeopleName.addAll(result.getData());
						String name1 = zhifa_renyuan_one.getText().toString();
						for(int i = 0; i < listPeopleName.size(); i++){
							if(name1.equals(listPeopleName.get(i).getNAME())){
								listPeopleName.remove(i);
							}
						}
						final String[] itemName = new String[listPeopleName.size()];
						for (int i = 0,len = listPeopleName.size(); i < len; i++) {
							itemName[i] = listPeopleName.get(i).getNAME();
						}
						Windows.singleChoice(context, "请选择执法人员", itemName, new OnDialogItemClickListener() {

							@Override
							public void dialogItemClickListener(int position) {
								zhifa_renyuan_two.setText(itemName[position]);
								try {
									zfzh2 = listPeopleName.get(position).getZFZH();
								}catch (Exception e){

								}
							}
						});
					} else {
						UIUtils.toast(context, result.getErrorMsg(), Toast.LENGTH_LONG);
						return;
					}
					renyuan_Two.setClickable(true);
				}

			});
		}
	}



	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	private void chooseDate(final View v) {
		Windows.selectDateTime(context, (long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);

			}
		});
	}

	public String getZfzhTwo() {
		return zfzhtwo;
	}

	public String getLawTime() {
		String result = null;
		result = zhifa_time.getText().toString();
		return result;
	}

	public String getAddressString() {
		String result = null;
		result = zhifa_address.getText().toString();
		return result;
	}

	public String getRenYuanOneString() {
		String result = null;
		result = zhifa_renyuan_one.getText().toString();
		return result;
	}

	public String getRenYuanTwoString() {
		String result = null;
		result = zhifa_renyuan_two.getText().toString();
		return result;
	}

	public String getZfzhtwo() {
		return zfzh2;
	}

	public long getTimeString() {
		long result = (long) zhifa_time.getTag();
		return result;
	}

	public boolean getIsCommon() {
		return iscommon;
	}

	public void setTime(String time) {
		if (!TextUtils.isEmpty(time)) {
			Date d = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				d = sf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			zhifa_time.setTag(d.getTime());
			zhifa_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
		}
	}

	public void setRenYuanOne(String renYuanOne) {
		if (!TextUtils.isEmpty(renYuanOne)) {
			zhifa_renyuan_one.setText(renYuanOne);
		}
	}

	public void setRenYuanTwo(String renYuanTwo) {
		if (!TextUtils.isEmpty(renYuanTwo)) {
			zhifa_renyuan_two.setText(renYuanTwo);
		}
	}

	/**
	 * 重定位广播
	 */
	class PositionBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adds = intent.getStringExtra("shoudong_location");
			iscommon = intent.getBooleanExtra("isCommon", false);
			ppfer.setString("selectPosition", adds);
			rePosition();
		}

	}

	/**
	 * 重定位
	 */
	protected void rePosition() {
		if(!adds.equals("")){
			zhifa_address.setText(adds);
			ischongdingwei=true;
			return;
		} else if(iscommon && MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0){
			zhifa_address.setText(adds);
			return;
		} else if (MsgConfig.select_lng != 0 && MsgConfig.select_lat != 0) {
			reverseGeoCode1(zhifa_address,MsgConfig.select_lat,MsgConfig.select_lng);
		} else {
			if (MsgConfig.lat != 0 && MsgConfig.lng != 0) {
				reverseGeoCode1(zhifa_address,MsgConfig.lat,MsgConfig.lng);
			} else {
				//reverseGeoCode1(zhifa_address,lat,lon);
			}
		}
	}

	public void PopunRegisterMsgReceiver() {
		if (broadcast != null) {
			context.unregisterReceiver(broadcast);
		}
	}



}
