package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

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
import com.miu360.taxi_check.model.LvyouYehuInfo;
import com.miu360.taxi_check.model.ShengJiWeiFaInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

public class WeiZhangShengJiInfoActivity extends BaseActivity implements OnClickListener {

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
	@ViewInject(R.id.qinxing)
	private TextView qinxing;// 违法情形
	@ViewInject(R.id.frdb)
	private TextView frdb;// 法人代表

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";
	private final String datePatter3 = "yyyyMMddHHmmss";
	private final String datePatter2 = "yyyyMMdd";
	private final String datePatterShow2 = "yyyy-MM-dd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_hangye_info);
		initView();
	}

	private void initData(ShengJiWeiFaInfo weifa) {
		user_nameTv.setText(isNull.isEmpty(weifa.getDanshiren()));
		number_carTv.setText(isNull.isEmpty(weifa.getVname()));
		ID_Number_CardTv.setText(isNull.isEmpty(weifa.getId()));
		number_cardTv.setText(isNull.isEmpty(weifa.getJdkh()));
		addressTv.setText(isNull.isEmpty(weifa.getAddress()));
		color_carTv.setText(isNull.isEmpty(weifa.getvColor()));
		vehicle_modelTv.setText(isNull.isEmpty(weifa.getvType()));
		unit_nameTv.setText(isNull.isEmpty(weifa.getCorpName()));
		frdb.setText(isNull.isEmpty(weifa.getFrdb()));
		if (unit_nameTv.getText().toString().equals("无")) {
			unit_nameTv.setClickable(false);
		} else {
			unit_nameTv.setClickable(true);
		}

		if (!TextUtils.isEmpty(weifa.getCheckTime())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(weifa.getCheckTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			check_timeTv.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
		} else {
			check_timeTv.setText(isNull.isEmpty(weifa.getCheckTime()));
		}

		check_placeTv.setText(isNull.isEmpty(weifa.getCheckAddress()));
		treatment_groupTv.setText(isNull.isEmpty(weifa.getProcessGroup()));

		if (weifa.getStatus().equals("结案")) {
			stateTv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
			stateTv.setText(isNull.isEmpty(weifa.getStatus()));
		} else {
			stateTv.setTextColor(getResources().getColor(R.color.register_btnn_color));
			stateTv.setText(isNull.isEmpty(weifa.getStatus()));
		}
		if (!TextUtils.isEmpty(weifa.getJaTime())) {
			Date d = null;
			if (weifa.getJaTime().length() > 8) {
				SimpleDateFormat sdf = new SimpleDateFormat(datePatter3);
				try {
					d = sdf.parse(weifa.getJaTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(datePatter2);
				try {
					d = sdf.parse(weifa.getJaTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				end_timeTv.setText(new SimpleDateFormat(datePatterShow2).format(new Date(d.getTime())));
			}

		} else {
			end_timeTv.setText(isNull.isEmpty(weifa.getJaTime()));
		}
		illegal_activitiesTv.setText(isNull.isEmpty(weifa.getWfxw()));
		if (TextUtils.isEmpty(weifa.getWzsy())) {
			reasonTv.setText("无");
			reasonTv.setGravity(Gravity.CENTER);
		} else {
			reasonTv.setText(weifa.getWzsy());
			reasonTv.setGravity(Gravity.LEFT);
		}

	}

	ShengJiWeiFaInfo weifa;
	UserPreference pref;

	private void initView() {
		ViewUtils.inject(self);
		Intent intent = getIntent();
		weifa = (ShengJiWeiFaInfo) intent.getSerializableExtra("ShengJiWeiFaInfo");
		unit_nameTv.setOnClickListener(this);
		unit_nameTv.setTextColor(Color.BLUE);
		unit_nameTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		unit_nameTv.getPaint().setAntiAlias(true);

		initData(weifa);
		pref = new UserPreference(self);
		new HeaderHolder().init(self, "违法信息");
		/*footer = new FooterHolder();
		footer.init(self);
		footer.left_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(pref.getBoolean("isLaw", false)){
					Intent intent = new Intent(self, LawInspectionActivity.class);
					intent.putExtra("Vname", weifa.getVname());
					intent.putExtra("DriverName", weifa.getDanshiren());
					intent.putExtra("ID", weifa.getId());
					intent.putExtra("vColor", weifa.getvColor());
					intent.putExtra("vType", weifa.getvType());
					intent.putExtra("CompanyName", weifa.getCorpName());
					intent.putExtra("CyzgNumber", weifa.getJdkh());
					intent.putExtra("HYLB", "省际客运");
					intent.putExtra("IsTurn", false);
					intent.putExtra("IsOverTurn", false);
					intent.putExtra("isTurnOver", false);
					startActivity(intent);
				}else{
					UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
				}
			}
		});
*/
	}

	ArrayList<LvyouYehuInfo> arrayList = new ArrayList<>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == unit_nameTv) {
			final MyProgressDialog pd = Windows.waiting(self);
			final LvyouYehuInfo info = new LvyouYehuInfo();
			info.setCompanyName(weifa.getCorpName());
			info.setStartIndex(0);
			info.setEndIndex(20);
			AsyncUtil.goAsync(new Callable<Result<List<LvyouYehuInfo>>>() {

				@Override
				public Result<List<LvyouYehuInfo>> call() throws Exception {
					return WeiZhanData.queryLvyouYehuInfo(info);
				}
			}, new Callback<Result<List<LvyouYehuInfo>>>() {

				@Override
				public void onHandle(Result<List<LvyouYehuInfo>> result) {
					pd.dismiss();
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
						return;
					}
					if (result.ok()) {
						arrayList.addAll(result.getData());
						Intent intent = new Intent(self, BasicCompanyInfoLvyouActivity.class);
						intent.putExtra("LvyouYehuInfo", arrayList.get(0));
						startActivity(intent);
					}
				}
			});
		}
	}

}
