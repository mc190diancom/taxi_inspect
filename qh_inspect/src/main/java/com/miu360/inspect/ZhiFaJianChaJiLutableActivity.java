package com.miu360.inspect;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.Callback;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.MapPositionPreference;
import com.miu360.taxi_check.common.Windows;
import com.miu30.common.data.UserPreference;
import com.miu30.common.data.ZfryPreference;
import com.miu360.taxi_check.model.QueryFaZhiBanResult;
import com.miu360.taxi_check.model.QueryOneZhiFaBanModel;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.taxi_check.ui.SelectLocationActivity;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ZhiFaJianChaJiLutableActivity extends BaseActivity implements View.OnClickListener {
	@ViewInject(R.id.tv_bdbh)
	private TextView tv_bdbh;
	@ViewInject(R.id.tv_time)
	private TextView tv_time;
	@ViewInject(R.id.tv_dizhi)
	private TextView tv_dizhi;
	@ViewInject(R.id.ed_jcr_xm)
	private EditText ed_jcr_xm;
	@ViewInject(R.id.ed_jcr_zhengjian)
	private EditText ed_jcr_zhengjian;
	@ViewInject(R.id.ed_cp_ch)
	private EditText ed_cp_ch;
	@ViewInject(R.id.ed_cp_phone)
	private EditText ed_cp_phone;
	@ViewInject(R.id.ed_jcr_dwmc)
	private EditText ed_jcr_dwmc;
	@ViewInject(R.id.ed_jcr_zrr)
	private EditText ed_jcr_zrr;
	@ViewInject(R.id.ed_dwdz)
	private EditText ed_dwdz;
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	@ViewInject(R.id.tv_hangye)
	private TextView tv_hangye;
	@ViewInject(R.id.ll_jianchaxiang)
	private TextView ll_jianchaxiang;
	@ViewInject(R.id.tv_jcqk)
	private TextView tv_jcqk;
	@ViewInject(R.id.tv_zary1)
	private TextView tv_zary1;
	@ViewInject(R.id.tv_zary2)
	private TextView tv_zary2;
	@ViewInject(R.id.tv_hege_Yuanyin)
	private TextView tv_hege_Yuanyin;



	HeaderHolder holder;
	PositionBroadCast broadcast;
	private String zfdamc;
	UserPreference pref;
	ZfryPreference prez;
	ArrayList<queryZFRYByDWMC> listPeopleName = new ArrayList<>();
	MapPositionPreference ppfer;
	String Vname = "";
	ArrayList<QueryOneZhiFaBanModel> oneFaZhiBanList=new ArrayList<>();
	QueryFaZhiBanResult info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhi_fa_jian_cha_ji_lutable);
		this.registerReceiver(broadcast = new PositionBroadCast(),
				new IntentFilter(SelectLocationActivity.CHOSE_LOCATION));

		pref = new UserPreference(self);
		prez = new ZfryPreference(this);

		initView();
	}

	private void initView() {
		ViewUtils.inject(self);

		ppfer = new MapPositionPreference(this);
		holder = new HeaderHolder();

		holder.init(self, "执法检查记录表");
		holder.rightTextBtn.setVisibility(View.VISIBLE);
		holder.rightTextBtn.setText("");
		Vname = getIntent().getStringExtra("Vname");
		info=(QueryFaZhiBanResult) getIntent().getSerializableExtra("QueryFaZhiBanResult");

		if(info!=null){
			trimNullToEmpty(info);
			tv_bdbh.setText(info.getCHECK_LIST_CODE());
			tv_time.setText(info.getCHECK_DATE());
			tv_dizhi.setText(info.getCHECK_PLACE());
			ed_jcr_xm.setText(info.getCITIZEN_NAME());
			ed_jcr_zhengjian.setText(info.getSFZH());
			ed_cp_ch.setText(info.getVNAME());
			ed_cp_phone.setText(info.getCITIZEN_PHONE());
			ed_jcr_dwmc.setText(info.getUNIT_NAME());
			ed_jcr_zrr.setText(info.getLEGAL_PERSON());
			ed_dwdz.setText(info.getADDRESS());
			ed_phone.setText(info.getORGANIZATION_PHONE());
			tv_hangye.setText(info.getHYLB());
			ll_jianchaxiang.setText(info.getMODULE_NAME());
			tv_jcqk.setText(info.getCHECK_RESULT()==1?"合格":"不合格");
			try {
				tv_zary1.setText(info.getPERSON_NAME().split(",")[0]);
				tv_zary2.setText(info.getPERSON_NAME().split(",")[1]);
			} catch (Exception e) {
				// TODO: handle exception
				tv_zary1.setText("无");
				tv_zary2.setText("无");
			}

			String yuanyin;
			if(info.getIS_CHARGE_REFORM()==3){
				yuanyin="现场批评警告，责令改正";
			}else if(info.getIS_CHARGE_REFORM()==2){
				yuanyin="予以表扬";
			}else{
				yuanyin="未发现问题";
			}
			tv_hege_Yuanyin.setText(yuanyin);
		}else{
			UIUtils.toast(self,"没有详细信息", Toast.LENGTH_SHORT);
		}

	}





	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	private void chooseDate(final View v) {
		Windows.selectDateTime(self, (Long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
			}
		});
	}

	class PositionBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ppfer.setString("selectPosition", intent.getStringExtra("shoudong_location"));
			/*
			 * //如果自动定位也定到了，存一个地址进preference，因如果进定位详细地址把定位地址微改了下，其他界面获取的却是解析的
			 * if(MsgConfig.lat != 0.0){ ppfer.setString("selfPosition",
			 * intent.getStringExtra("shoudong_location")); }
			 */
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (broadcast != null) {
			this.unregisterReceiver(broadcast);
		}
	}
	public  void trimNullToEmpty(Object o) {
		try {
			Field[] fs = o.getClass().getDeclaredFields();
			for (Field field : fs) {
				if (field.getType() == String.class) {
					field.setAccessible(true);
					if (field.get(o)==null||field.get(o).equals("")) {
						field.set(o, "无");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
