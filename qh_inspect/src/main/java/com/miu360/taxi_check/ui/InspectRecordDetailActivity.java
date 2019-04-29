package com.miu360.taxi_check.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

import com.blankj.utilcode.util.ActivityUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.FaZhiBanQueayActivity;
import com.miu360.inspect.R;

import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.ui.activity.CreateCaseActivity;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.HistoryInspectRecordModelNew;
import com.miu360.taxi_check.model.Zwstatus;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class InspectRecordDetailActivity extends BaseActivity implements OnRefreshListener2<ScrollView>{

	@ViewInject(R.id.code)
	private TextView code;// 稽查编号
	@ViewInject(R.id.vname)
	private TextView vname;// 车牌编号
	@ViewInject(R.id.vcolor)
	private TextView vcolor;// 车身颜色
	@ViewInject(R.id.vtype)
	private TextView vtype;// 车辆型号
	@ViewInject(R.id.company_car)
	private TextView company_car;// 车辆信息-公司名称
	@ViewInject(R.id.driverName)
	private TextView driverName;// 驾驶员
	@ViewInject(R.id.id_card)
	private TextView id_card;// 身份证号
	@ViewInject(R.id.cyzgz)
	private TextView cyzgz;// 从业资格证号
	@ViewInject(R.id.company_people)
	private TextView company_people;// 人员信息-公司名称
	@ViewInject(R.id.company_yehu)
	private TextView company_yehu;// 业户信息-公司名称
	@ViewInject(R.id.jinyingxuke)
	private TextView jinyingxuke;// 经营许可证号
	@ViewInject(R.id.frdb)
	private TextView frdb;// 法人代表
	@ViewInject(R.id.zfry_one)
	private TextView zfry_one;// 执法人员1
	@ViewInject(R.id.zfry_two)
	private TextView zfry_two;// 执法人员1
	@ViewInject(R.id.ssdd)
	private TextView ssdd;// 所属大队
	@ViewInject(R.id.sszd)
	private TextView sszd;//
	@ViewInject(R.id.sscz)
	private TextView sscz;// 稽查行业
	@ViewInject(R.id.yujing_content)
	private TextView yujing_content;// 预警内容

	@ViewInject(R.id.guiji_xingshi)
	private TextView guiji_xingshi;//行驶轨迹
	@ViewInject(R.id.pull_refresh_scrollview)
	private PullToRefreshScrollView pull_refresh_scrollview;
	@ViewInject(R.id.check_time)
	private TextView check_time;// 稽查时间
	@ViewInject(R.id.address)
	private TextView address;// 稽查地点
	@ViewInject(R.id.inspect_hangye)
	private TextView inspect_hangye;// 稽查行业
	@ViewInject(R.id.weifa_itme)
	private TextView weifa_itme;// 违法项
	@ViewInject(R.id.inspect_result)
	private TextView inspect_result;// 稽查结果
	@ViewInject(R.id.waiqin)
	private TextView waiqin;// 转外勤文书
	@ViewInject(R.id.ly_waiqin)
	private LinearLayout ly_waiqin;// 转外勤文书
	@ViewInject(R.id.tv_trunFaZhiBan)
	private TextView tv_trunFaZhiBan;

	HistoryInspectRecordModelNew info;
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	int rank;
	String color;
	String carType;
	String sfzh;
	String company;
	String carCompany;
	String fardb;
	String jyxkz;
	BitmapUtils bitmap;
	UserPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect_record_detail);
		info = (HistoryInspectRecordModelNew) getIntent().getSerializableExtra("HistoryInspectRecordModelNew");

		color = info.getColor();
		carType = info.getModel();
		sfzh =info.getSfzh();
		company =info.getPcompany();
		carCompany =info.getVcompany();
		fardb = info.getFrdb();
		jyxkz = info.getJyxkz();
		pref = new UserPreference(self);

		color = getIntent().getStringExtra("color");
		carType = getIntent().getStringExtra("carType");
		sfzh = getIntent().getStringExtra("sfzh");
		company = getIntent().getStringExtra("company");
		carCompany = getIntent().getStringExtra("carCompany");
		fardb = getIntent().getStringExtra("frdb");
		jyxkz = getIntent().getStringExtra("jyxkz");
		pref = new UserPreference(self);

		initView();
		registerMsgReceiver();
		waiqin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					turnWaiQin();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					UIUtils.toast(self, "请先安装该APP", Toast.LENGTH_SHORT);
				}
			}

		});
		tv_trunFaZhiBan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(InspectRecordDetailActivity.this,FaZhiBanQueayActivity.class);
				intent.putExtra("isTurn", true);
				intent.putExtra("Vname", isNull.isEmpty(info.getVname()));
				startActivity(intent);
			}
		});

	}

	public void turnWaiQin() {
		Case c = new Case();
		c.setHYLB(info.getHylb());
		c.setVNAME(info.getVname());
		c.setBJCR(info.getDriverName());
		c.setZFRYNAME2(info.getZfry2());
		c.setJCDD(info.getAddress());
		if("4".equals(info.getStatus())){
			c.setCFFS("警告");
		}else if("5".equals(info.getStatus())){
			c.setCFFS("处罚");
		}else{
			UIUtils.toast(self,"正常案件不允许转外勤",Toast.LENGTH_SHORT);
			return;
		}
		c.setLAT((long)(info.getLat()*1E6d)+"");
		c.setLON((long)(info.getLon()*1E6d)+"");
		Intent intent = new Intent(self, CreateCaseActivity.class);
		intent.putExtra("case",c);
		ActivityUtils.startActivity(intent);
		finish();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "稽查详情");
		bitmap = new BitmapUtils(self);
		bitmap.configDefaultLoadFailedImage(R.drawable.default_photo);
		if((info.getStatus().equals("4")||info.getStatus().equals("5"))&&info.getZwstatus().equals("成功")&&(info.getZcfstatus().equals("")||info.getZcfstatus().equals("未转"))){
			waiqin.setVisibility(View.VISIBLE);
		}else{
			waiqin.setVisibility(View.GONE);
		}
		initData();
	}

	private void initData() {
		sszd.setText(pref.getString("sszd", "无"));
		sscz.setText(pref.getString("sscz", "无"));
		code.setText(isNull.isEmpty(info.getJcbh()));
		vname.setText(isNull.isEmpty(info.getVname()));
		vcolor.setText(isNull.isEmpty(color));
		vtype.setText(isNull.isEmpty(carType));
		company_car.setText(isNull.isEmpty(info.getVcompany()));
		driverName.setText(isNull.isEmpty(info.getDriverName()));
		guiji_xingshi.setText("点击查看轨迹图");
		guiji_xingshi.setTextColor(Color.BLUE);
		guiji_xingshi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		guiji_xingshi.getPaint().setAntiAlias(true);
		id_card.setText(isNull.isEmpty(sfzh));
		cyzgz.setText(isNull.isEmpty(info.getJdkh()));
		company_people.setText(isNull.isEmpty(info.getPcompany()));
		company_yehu.setText(isNull.isEmpty(info.getCorpname()));
		jinyingxuke.setText(isNull.isEmpty(jyxkz));
		frdb.setText(isNull.isEmpty(fardb));
		zfry_one.setText(isNull.isEmpty(info.getZfry1()));
		zfry_two.setText(isNull.isEmpty(info.getZfry2()));
		ssdd.setText(isNull.isEmpty(info.getZfdwmc()));
		if (!TextUtils.isEmpty(info.getZfsj())) {
			long time = Long.parseLong(info.getZfsj());
			check_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time * 1000)));
		} else {
			check_time.setText("无");
		}

		yujing_content.setText(isNull.isEmpty(info.getVehicle_check_items()));
		address.setText(isNull.isEmpty(info.getAddress()));
		inspect_hangye.setText(isNull.isEmpty(info.getHylb()));
		/*if ("".equals(info.getPerson_check_items()) && "".equals(info.getYehu_check_items())) {
			weifa_itme.setText("正常");
		} else {
			weifa_itme.setText(info.getPerson_check_items() + info.getYehu_check_items());
		}*/
		weifa_itme.setText(isNull.isEmpty(info.getPerson_check_items()));
		if (!TextUtils.isEmpty(info.getStatus())) {
			if (info.getStatus().equals("4")) {
				inspect_result.setText("警告");
			} else if(info.getStatus().equals("5")){
				inspect_result.setText("处罚");
			}else if(info.getStatus().equals("2")){
				inspect_result.setText("表扬");
			}else if(info.getStatus().equals("3")){
				inspect_result.setText("批教");
			}else{
				inspect_result.setText("正常");
			}

		} else {
			inspect_result.setText("无");
		}

		guiji_xingshi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final View view = LayoutInflater.from(self).inflate(R.layout.show_image_view, null);
				LinearLayout ll_main = (LinearLayout) view.findViewById(R.id.show_imageView_main);

				final ImageView imageView = (ImageView) view.findViewById(R.id.show_imageView);
				final MyProgressDialog pd = Windows.waiting(self);
				AsyncUtil.goAsync(new Callable<Result<Void>>() {
					@Override
					public Result<Void> call() throws Exception {
						Result<Void> r = new Result<Void>(Result.OK, null, null,
								null);
						bitmap.display(imageView,Config.SERVER_XINXI+"/queryCasePhoto/"+info.getId());
						return r;
					}
				}, new Callback<Result<Void>>() {

					@Override
					public void onHandle(Result<Void> result) {
						if (result.ok()) {
							pd.dismiss();
							Builder d = new AlertDialog.Builder(self);
							final AlertDialog dialog = d.create();

							dialog.setView(view);
							dialog.show();

							WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
							WindowManager wm = (WindowManager) self
									.getSystemService(Context.WINDOW_SERVICE);

							int width = wm.getDefaultDisplay().getWidth();
							int height = wm.getDefaultDisplay().getHeight();
							params.width = width;
							params.height = height;
							dialog.getWindow().setAttributes(params);
						} else {
							UIUtils.toast(self, "获取图片失败", Toast.LENGTH_SHORT);
						}
					}
				});
			}
		});
	}




	public void registerMsgReceiver() {
		IntentFilter filter = new IntentFilter("com.xiazdong");
		registerReceiver(msgReceiver,filter);
	}

	public void unregisterMsgReceiver() {
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterMsgReceiver();
	}

	public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String ranks = intent.getStringExtra("RankID");
			final Zwstatus z = new Zwstatus();
			z.setZwstatus("成功");
			z.setId(info.getId());
			if (Integer.parseInt(ranks) == rank) {

				AsyncUtil.goAsync(new Callable<Result<Long>>() {

					@Override
					public Result<Long> call() throws Exception {
						return WeiZhanData.updateZfryZwstatus(z);
					}
				}, new Callback<Result<Long>>() {

					@Override
					public void onHandle(Result<Long> result) {
						if (result.ok()) {
							waiqin.setVisibility(View.GONE);
						} else {
							waiqin.setVisibility(View.GONE);
						}
					}
				});
			}
		}
	};

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

	}


	/*public boolean onKeyDown(int keyCode,KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
		//这里重写返回键
			 Intent intent = new Intent();

			 setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
			 finish();
		return true;
		}
		 return false;
		}*/
}
