package com.miu360.inspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;

import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu360.taxi_check.common.DateVer;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.FazhiBanAllQuenyModel;
import com.miu360.taxi_check.model.QueryFaZhiBanResult;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FaZhiBanQueayActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.ed_jcx)
	private EditText ed_jcx;
	@ViewInject(R.id.driverName)
	private EditText driverName_et;
	@ViewInject(R.id.car_name)
	private TextView car_name_tv;
	@ViewInject(R.id.vname)
	private EditText vname_et;
	@ViewInject(R.id.startTime)
	private TextView startTime_tv;
	@ViewInject(R.id.endTime)
	private TextView endTime_tv;
	@ViewInject(R.id.query)
	private TextView query;
	@ViewInject(R.id.car_name_tow)
	private TextView car_name_tow;
	@ViewInject(R.id.choose_hangye_tv)
	private TextView choose_hangye_tv;
	@ViewInject(R.id.rb_qiyong)
	private RadioButton rb_qiyong;
	@ViewInject(R.id.rb_buqiyong)
	private RadioButton rb_buqiyong;

	String[] itemsOne = { "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣" };
	String[] itemsTwo = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	String[] itemsThree = { "全部", "巡游车", "非法经营巡游汽车", "旅游客运", "省际客运", "普通货物运输", "危险品货物运输", "汽车租赁", "业外旅游客运", "业外省际客运",
			"业外普通货物运输", "业外危险品货物运输", "业外汽车租赁" };
	String[] chuzu = { "全部", "巡游汽车经营者", "巡游汽车驾驶员", "设立巡游汽车经营站的单位" };
	String[] feifachuzu = { "非法巡游客运经营者" };
	String[] lvyouJiancha = { "全部", "旅游客运经营者", "从业人员", "旅游车辆驾驶员", "道路运输企业", "任何单位或者个人" };
	String[] shengjijiancha = { "全部", "道路客运经营者", "客运车辆驾驶员", "客运站经营者", "包车客运经营者", "班线客运经营者", "道路运输企业", "任何单位个人" };
	String[] daoluyunshujiancha = { "全部", "道路货运经营者", "货运车辆驾驶员", "货运站经营者", "货代经营者", "道路运输企业", "任何单位个人" };
	String[] sxpyunshujiancha = { "全部", "危险货物运输从业人员", "危险货物运输企业或者单位", "托运人", "道路运输企业", "任何单位个人" };
	String[] qczhulinjianca = { "汽车租赁经营者" };
	String[] ywlvyoujiancha = { "全部", "旅游客运经营者", "非法旅游客运经营者" };
	String[] ywshegnjijiancha = { "全部", "道路客运经营者", "非法客运站经营者", "非法道路客运单位或个人", "客运站经营者" };
	String[] ywputonghuoyun = { "全部", "货运站经营者", "道路货运经营者", "非法道路货运单位或个人", "非法货运经营者", "货运经营者", "非法货运站经营者" };
	String[] ywweixianping = { "全部", "危险货物运输企业或者单位", "非法危险货物运输单位或个人" };
	String[] yewaizhuliin = { "汽车租赁经营者" };

	String[] qaunbu = { "全部","巡游汽车经营者", "巡游汽车驾驶员", "设立巡游汽车经营站的单位", "非法巡游客运经营者", "旅游客运经营者", "从业人员", "旅游车辆驾驶员", "道路运输企业",
			"任何单位或者个人", "道路客运经营者", "客运车辆驾驶员", "客运站经营者", "包车客运经营者", "班线客运经营者", "任何单位个人", "道路货运经营者", "货运车辆驾驶员", "货运站经营者",
			"货代经营者", "危险货物运输从业人员", "危险货物运输企业或者单位", "托运人", "汽车租赁经营者", "旅游客运经营者", "非法旅游客运经营者", "非法客运站经营者", "非法道路客运单位或个人",
			"非法道路货运单位或个人", "非法货运经营者", "货运经营者", "非法货运站经营者", "非法危险货物运输单位或个人", };

	String[][] allhangyejiancha = {qaunbu, chuzu, feifachuzu, lvyouJiancha, shengjijiancha, daoluyunshujiancha,
			sxpyunshujiancha, qczhulinjianca, ywlvyoujiancha, ywshegnjijiancha, ywputonghuoyun, ywweixianping,
			yewaizhuliin };
	HeaderHolder header;
	String[] itemsJCX = { "", "" };

	String jczt = "";
	String isQiyong = "";
	int HylbPosition = 0;
	boolean isTurn = false;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fa_zhi_ban_queay);
		initView();
		if (isTurn) {
			initTurnData();
		}

	}

	// 从历史界面跳转过来
	private void initTurnData() {

		String Vname = getIntent().getStringExtra("Vname");
		initData(Vname);
	}

	private void initData(final String Vname) {
		final MyProgressDialog pd = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<Result<List<QueryFaZhiBanResult>>>() {

			@Override
			public Result<List<QueryFaZhiBanResult>> call() throws Exception {
				return WeiZhanData.queryFaZhiBanOne(Vname);
			}
		}, new Callback<Result<List<QueryFaZhiBanResult>>>() {

			@Override
			public void onHandle(Result<List<QueryFaZhiBanResult>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
						return;
					}

					if (result.getData().size() == 1) {
						Intent intent = new Intent(getApplicationContext(), ZhiFaJianChaJiLutableActivity.class);
						intent.putExtra("QueryFaZhiBanResult", result.getData().get(0));
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(), FaZhiBanListActivity.class);
						String info = new Gson().toJson(result.getData());
						Log.e("zar", "info==" + info);
						intent.putExtra("infoGson", info);
						intent.putExtra("isOneTurn", true);
						startActivity(intent);
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					Log.e("zar", "ErrorMsg" + result.getErrorMsg());
				}
			}
		});

	}

	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "执法检查记录表");
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("");

		query.setOnClickListener(this);
		startTime_tv.setOnClickListener(this);
		endTime_tv.setOnClickListener(this);
		car_name_tow.setOnClickListener(this);
		choose_hangye_tv.setOnClickListener(this);
		ed_jcx.setOnClickListener(this);

		startTime_tv.setTag(System.currentTimeMillis());
		startTime_tv.setText(new SimpleDateFormat(datePatterShow).format(new Date(System.currentTimeMillis())));
		endTime_tv.setTag(System.currentTimeMillis());
		endTime_tv.setText(new SimpleDateFormat(datePatterShow).format(new Date()));

		ChooseInputType input = new ChooseInputType();

		input.init(driverName_et);
		isTurn = getIntent().getBooleanExtra("isTurn", false);
	}

	String corpName;
	String driverName;
	String car_name;
	long startTime;
	long endTime;
	String hylb;
	long start;
	long end;
	String jcx;
	@Override
	public void onClick(View v) {
		if (v == query) {
			if (TextUtils.isEmpty(vname_et.getText().toString())) {
				car_name = null;
			} else {
				car_name = vname_et.getText().toString().toUpperCase();
			}
			if (!TextUtils.isEmpty(car_name)) {
				String first = car_name.charAt(0) + "";
				if (!(Pattern.compile("[a-zA-Z]").matcher(first).matches())) {// 字母
					UIUtils.toast(self, "必须输入首字母", Toast.LENGTH_SHORT);
					return;
				}
			}

			driverName = driverName_et.getText().toString();
			hylb = choose_hangye_tv.getText().toString().equals("全部") ? "" : choose_hangye_tv.getText().toString();
			jcx=ed_jcx.getText().toString().equals("全部") ? "" : ed_jcx.getText().toString();
			/*
			 * if (TextUtils.isEmpty(car_name) && TextUtils.isEmpty(corpName) &&
			 * TextUtils.isEmpty(driverName)) { UIUtils.toast(self, "至少输入一项",
			 * Toast.LENGTH_SHORT); return; }
			 */

			start = (long) startTime_tv.getTag();
			end = (long) endTime_tv.getTag();

			if (DateVer.VerEndCurrentDate(self, end)) {
				return;
			}
			if (DateVer.VerStartCurrentDate(self, start)) {
				return;
			}
			if (DateVer.VerStartDate(self, start, end)) {
				return;
			}

			final MyProgressDialog pd = Windows.waiting(self);
			final FazhiBanAllQuenyModel quenyModel = new FazhiBanAllQuenyModel();
			quenyModel.setHYLB(hylb);
			quenyModel.setCITIZEN_NAME(driverName);
			quenyModel.setMODULE_NAME(ed_jcx.getText().toString().equals("全部") ? "" : ed_jcx.getText().toString());
			quenyModel.setIS_ENABLE((rb_qiyong.isChecked() ? 1 : 2) + "");
			quenyModel.setSTARTDATE(df.format(start));
			quenyModel.setENDDATE(df.format(end));

			AsyncUtil.goAsync(new Callable<Result<List<QueryFaZhiBanResult>>>() {

				@Override
				public Result<List<QueryFaZhiBanResult>> call() throws Exception {
					return WeiZhanData.queryFaZhiBan(quenyModel);
				}
			}, new Callback<Result<List<QueryFaZhiBanResult>>>() {

				@Override
				public void onHandle(Result<List<QueryFaZhiBanResult>> result) {
					pd.dismiss();
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
							return;
						}

						if (result.getData().size() == 1) {
							Intent intent = new Intent(getApplicationContext(), ZhiFaJianChaJiLutableActivity.class);
							intent.putExtra("QueryFaZhiBanResult", result.getData().get(0));
							startActivity(intent);
						} else {
							Log.e("zar", "startdate==" + df.format(start) + "..." + "enddate===" + df.format(end));
							Intent intent = new Intent(getApplicationContext(), FaZhiBanListActivity.class);
							intent.putExtra("corpName", corpName);
							intent.putExtra("driverName", driverName);
							intent.putExtra("startTime", df.format(start));
							intent.putExtra("endTime", df.format(end));
							intent.putExtra("Hylb", hylb);
							intent.putExtra("jcx", jcx);
							startActivity(intent);
						}

					} else {
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});

		} else if (v == car_name_tv) {
			Windows.singleChoice(self, "请选择车牌", itemsOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					car_name_tv.setText(itemsOne[position]);
				}
			});
		} else if (v == choose_hangye_tv) {
			Windows.singleChoice(self, "请选择行业", itemsThree, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					choose_hangye_tv.setText(itemsThree[position]);
					HylbPosition = position;
				}
			});
		} else if (v == startTime_tv){
			chooseDate(startTime_tv);
		} else if (v == endTime_tv) {
			chooseDate(endTime_tv);
		} else if (v == car_name_tow) {
			Windows.singleChoice(self, "请选择车牌", itemsTwo, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					car_name_tow.setText(itemsTwo[position]);
				}
			});
		} else if (ed_jcx == v) {
			if (choose_hangye_tv.getText().toString().isEmpty()) {
				UIUtils.toast(self, "请选择行业类别", Toast.LENGTH_SHORT);
				return;
			}
			Windows.singleChoice(self, "请选择检查主体", allhangyejiancha[HylbPosition], new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					ed_jcx.setText(allhangyejiancha[HylbPosition][position]);
				}
			});

		}
	}

	private final String datePatterShow = "yyyy-MM-dd HH:mm:ss";
	private final String datePatter = "yyyyMMddHHmmss";

	private void chooseDate(final View v) {
		Windows.selectDateTime(self, (Long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
			}
		});
	}
}