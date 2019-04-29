package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.model.WeiFa_New;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WeiZhangInfoActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.user_name)
	private TextView user_nameTv;// 当事人
	@ViewInject(R.id.number_car)
	private TextView number_carTv;// 车牌号
	@ViewInject(R.id.ID_Number_Card)
	private TextView ID_Number_CardTv;// 身份证号
	@ViewInject(R.id.number_card)
	private TextView number_cardTv;// 监督卡号
	@ViewInject(R.id.address)
	private TextView addressTv;// 住址
	@ViewInject(R.id.color_car)
	private TextView color_carTv;// 车身颜色
	@ViewInject(R.id.vehicle_model)
	private TextView vehicle_modelTv;// 车辆型号
	@ViewInject(R.id.unit_name)
	private TextView unit_nameTv;// 单位名称
	@ViewInject(R.id.check_time)
	private TextView check_timeTv;// 检查时间
	@ViewInject(R.id.check_place)
	private TextView check_placeTv;// 检查地点
	@ViewInject(R.id.treatment_group)
	private TextView treatment_groupTv;// 处理大队
	@ViewInject(R.id.state)
	private TextView stateTv;// 状态
	@ViewInject(R.id.end_time)
	private TextView end_timeTv;// 结案时间
	@ViewInject(R.id.illegal_activities)
	private TextView illegal_activitiesTv;// 违法行为
	@ViewInject(R.id.reason)
	private TextView reasonTv;// 违章事由
	FooterHolder footer;

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";
	private final String datePatter3 = "yyyyMMddHHmmss";
	private final String datePatter2 = "yyyyMMdd";
	private final String datePatterShow2 = "yyyy-MM-dd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_info);
		initView();
	}
	private void initData(WeiFa_New weifa) {
		user_nameTv.setText(isNull.isEmpty(weifa.getDSR()));
		number_carTv.setText(isNull.isEmpty(weifa.getCLPH()));
		ID_Number_CardTv.setText(isNull.isEmpty(weifa.getSFZH()));
		number_cardTv.setText(isNull.isEmpty(weifa.getZJZH()));
		addressTv.setText(isNull.isEmpty(""));
		color_carTv.setText(isNull.isEmpty(weifa.getYS()));
		vehicle_modelTv.setText(isNull.isEmpty(weifa.getCX()));
		unit_nameTv.setText(isNull.isEmpty(weifa.getDWMC()));
		if (unit_nameTv.getText().toString().equals("无")) {
			unit_nameTv.setClickable(false);
		} else {
			unit_nameTv.setClickable(true);
		}

		if (!TextUtils.isEmpty(weifa.getJCSJ())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(weifa.getJCSJ());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			check_timeTv.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
		} else {
			check_timeTv.setText(isNull.isEmpty(weifa.getJCSJ()));
		}

		check_placeTv.setText(isNull.isEmpty(weifa.getJCDD()));
		treatment_groupTv.setText(isNull.isEmpty(weifa.getZFDDMC()));

		if (weifa.getAJZT().equals("结案")) {
			stateTv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
			stateTv.setText(isNull.isEmpty(weifa.getAJZT()));
		} else {
			stateTv.setTextColor(getResources().getColor(R.color.register_btnn_color));
			stateTv.setText(isNull.isEmpty(weifa.getAJZT()));
		}
		if (!TextUtils.isEmpty(weifa.getJASJ())) {
			Date d = null;
			if (weifa.getJASJ().length() > 8) {
				SimpleDateFormat sdf = new SimpleDateFormat(datePatter3);
				try {
					d = sdf.parse(weifa.getJASJ());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(datePatter2);
				try {
					d = sdf.parse(weifa.getJASJ());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
			}

		} else {
			end_timeTv.setText(isNull.isEmpty(weifa.getJASJ()));
		}
		illegal_activitiesTv.setText(isNull.isEmpty(weifa.getLBMC()));
		if (TextUtils.isEmpty(weifa.getWZSY())) {
			reasonTv.setText("无");
			reasonTv.setGravity(Gravity.CENTER);
		} else {
			reasonTv.setText(weifa.getWZSY());
			reasonTv.setGravity(Gravity.LEFT);
		}

	}
//	private void initData(WeiFa_New weifa) {
//		user_nameTv.setText(isNull.isEmpty(weifa.getDSR()));
//		number_carTv.setText(isNull.isEmpty(weifa.getCLPH()));
//		ID_Number_CardTv.setText(isNull.isEmpty(weifa.getSFZH()));
//		number_cardTv.setText(isNull.isEmpty(weifa.getZJZH()));
//		addressTv.setText(isNull.isEmpty(""));
//		color_carTv.setText(isNull.isEmpty(weifa.getYS()));
//		vehicle_modelTv.setText(isNull.isEmpty(weifa.getCX()));
//		unit_nameTv.setText(isNull.isEmpty(weifa.getDWMC()));
//		if (unit_nameTv.getText().toString().equals("无")) {
//			unit_nameTv.setClickable(false);
//		} else {
//			unit_nameTv.setClickable(true);
//		}
//
//		if (!TextUtils.isEmpty(weifa.getJCSJ())) {
//			Date d = null;
//			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
//			try {
//				d = sdf.parse(weifa.getCheckTime());
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			check_timeTv.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
//		} else {
//			check_timeTv.setText(isNull.isEmpty(weifa.getCheckTime()));
//		}
//
//		check_placeTv.setText(isNull.isEmpty(weifa.getCheckAddress()));
//		treatment_groupTv.setText(isNull.isEmpty(weifa.getProcessGroup()));
//
//		if (weifa.getStatus().equals("结案")) {
//			stateTv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
//			stateTv.setText(isNull.isEmpty(weifa.getStatus()));
//		} else {
//			stateTv.setTextColor(getResources().getColor(R.color.register_btnn_color));
//			stateTv.setText(isNull.isEmpty(weifa.getStatus()));
//		}
//		if (!TextUtils.isEmpty(weifa.getJaTime())) {
//			Date d = null;
//			if (weifa.getJaTime().length() > 8) {
//				SimpleDateFormat sdf = new SimpleDateFormat(datePatter3);
//				try {
//					d = sdf.parse(weifa.getJaTime());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
//			} else {
//				SimpleDateFormat sdf = new SimpleDateFormat(datePatter2);
//				try {
//					d = sdf.parse(weifa.getJaTime());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
//			}
//
//		} else {
//			end_timeTv.setText(isNull.isEmpty(weifa.getJaTime()));
//		}
//
//		if (TextUtils.isEmpty(weifa.getWzsy())) {
//			reasonTv.setText("无");
//			reasonTv.setGravity(Gravity.CENTER);
//		} else {
//			reasonTv.setText(weifa.getWzsy());
//			reasonTv.setGravity(Gravity.LEFT);
//		}
//
//	}

	WeiFa_New weifa;
	UserPreference pref;

	private void initView() {
		ViewUtils.inject(self);
		pref = new UserPreference(self);
//		Intent intent = getIntent();
//		weifa = (WeiFa_New) intent.getSerializableExtra("weifaInfo");

//		unit_nameTv.setOnClickListener(this);
//		unit_nameTv.setTextColor(Color.BLUE);
//		unit_nameTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//		unit_nameTv.getPaint().setAntiAlias(true);
//
//		new HeaderHolder().init(self, "违法信息");
//		new FooterHolder().init(self);
		Intent intent = getIntent();
		weifa = (WeiFa_New) intent.getSerializableExtra("weifaInfo");
		unit_nameTv.setOnClickListener(this);
		unit_nameTv.setTextColor(Color.BLUE);
		unit_nameTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		unit_nameTv.getPaint().setAntiAlias(true);

		new HeaderHolder().init(self, "违法信息");
		new FooterHolder().init(self);

		initData(weifa);
		footer = new FooterHolder();
		footer.init(self);
		footer.left_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(pref.getBoolean("isLaw", false)){
					Intent intent = new Intent(self, LawInpsectActivity.class);
					intent.putExtra("CompanyName", weifa.getDWMC());
					intent.putExtra("DriverName", weifa.getDSR());
					intent.putExtra("Vname", weifa.getCLPH());
//					intent.putExtra("JianduNumber", weifa.getJ);
					intent.putExtra("isTurn", true);
					intent.putExtra("isOver", true);
					intent.putExtra("isYeHu", true);
					startActivity(intent);
				}else{
					UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
				}
			}
		});
	}

	ArrayList<CompanyInfo> arrayList = new ArrayList<>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == unit_nameTv) {
			final MyProgressDialog pd = Windows.waiting(self);
			final CompanyInfo info = new CompanyInfo();
			info.setCompanyName(weifa.getDWMC());
			info.setStartIndex(0);
			info.setEndIndex(20);
			AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {

				@Override
				public Result<List<CompanyInfo>> call() throws Exception {
					return WeiZhanData.queryYeHuInfo(info);
				}
			}, new Callback<Result<List<CompanyInfo>>>() {

				@Override
				public void onHandle(Result<List<CompanyInfo>> result) {
					pd.dismiss();
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
						return;
					}
					if (result.ok()) {
						arrayList.addAll(result.getData());
						Intent intent = new Intent(getApplicationContext(), CompanyInfoActivity.class);
						intent.putExtra("CompanyInfo", arrayList.get(0));
						startActivity(intent);
					}
				}
			});
		}
	}

}
