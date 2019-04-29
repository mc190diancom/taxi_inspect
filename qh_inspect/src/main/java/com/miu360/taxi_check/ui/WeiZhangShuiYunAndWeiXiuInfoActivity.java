package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.model.WaterTranspt;
import com.miu360.taxi_check.view.HeaderHolder;

public class WeiZhangShuiYunAndWeiXiuInfoActivity extends BaseActivity {
	@ViewInject(R.id.jcbh)
	private TextView jcbh;// 检查编号
	@ViewInject(R.id.user_name)
	private TextView user_nameTv;// 当事人
	@ViewInject(R.id.ID_Number_Card)
	private TextView ID_Number_CardTv;// 身份证号
	@ViewInject(R.id.number_card)
	private TextView number_cardTv;// 监督卡号
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
	/*@ViewInject(R.id.illegal_activities)
	private TextView illegal_activitiesTv;// 违法行为
*/	@ViewInject(R.id.reason)
	private TextView reasonTv;// 违章事由
	/*@ViewInject(R.id.qinxing)
	private TextView qinxing;// 违法情形
*/	@ViewInject(R.id.frdb)
	private TextView frdb;//
	@ViewInject(R.id.jutileibie)
	private TextView jutileibie;//

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";
	private final String datePatter3 = "yyyyMMddHHmmss";
	private final String datePatter2 = "yyyyMMdd";
	private final String datePatterShow2 = "yyyy-MM-dd";
	WaterTranspt weifa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_zhang_shuiyun_info);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		Intent intent = getIntent();
		weifa = (WaterTranspt) intent.getSerializableExtra("WaterAndWeiXiuWeiFaInfo");
		new HeaderHolder().init(self, "违法信息");
		initData(weifa);
	}

	private void initData(WaterTranspt weifa) {
		jcbh.setText(isNull.isEmpty(weifa.getJCBH()));
		user_nameTv.setText(isNull.isEmpty(weifa.getDSR()));
		ID_Number_CardTv.setText(isNull.isEmpty(weifa.getSFZH()));
		number_cardTv.setText(isNull.isEmpty(weifa.getZJZH()));
		unit_nameTv.setText(isNull.isEmpty(weifa.getDWMC()));
		frdb.setText(isNull.isEmpty(weifa.getJCR1()+"  "+weifa.getJCR2()));
		jutileibie.setText(isNull.isEmpty(weifa.getHYLB()));

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
		if (TextUtils.isEmpty(weifa.getWZSY())) {
			reasonTv.setText("无");
			reasonTv.setGravity(Gravity.CENTER);
		} else {
			reasonTv.setText(weifa.getWZSY());
			reasonTv.setGravity(Gravity.LEFT);
		}

	}

}
