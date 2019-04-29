package com.miu360.taxi_check.ui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.ui.entity.LawToCase;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.ui.activity.CreateCaseActivity;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.LawInpsectAdapter;
import com.miu360.taxi_check.bean.CarStateBean;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.MapPositionPreference;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.PopWindowDialog;
import com.miu360.taxi_check.common.PositionPreference;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isCommon;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.HttpRequest;
import com.miu360.taxi_check.data.InfoPerference;
import com.miu360.taxi_check.data.Inspector;
import com.miu360.taxi_check.data.UserData;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.data.ZfryPreference;
import com.miu360.taxi_check.model.AddressComponent;
import com.miu360.taxi_check.model.CarNum;
import com.miu30.common.ui.entity.CheZuRequestModel;
import com.miu30.common.ui.entity.CheZuResultModel;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.taxi_check.model.FastInspectLvYouQueryWeiFaInfo;
import com.miu360.taxi_check.model.FastInspectWeiFaInfo;
import com.miu30.common.ui.entity.HYTypeQ;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu360.taxi_check.model.PutInspectDataReturn;
import com.miu360.taxi_check.model.RegisterInfoNew;
import com.miu360.taxi_check.model.TaxiCarInfo;
import com.miu360.taxi_check.model.TaxiCompany;
import com.miu360.taxi_check.model.TaxiCompanyQ;
import com.miu360.taxi_check.model.TaxiWfInfo;
import com.miu360.taxi_check.model.TaxiWfInfoQ;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.taxi_check.model.VehiclePositionModex;
import com.miu360.taxi_check.model.ZhiFaJianChaModel;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.BaseListView;
import com.miu30.common.util.FullListView;
import com.miu360.taxi_check.view.HeaderHolder;

public class LawInpsectActivity extends BaseActivity implements OnClickListener, TextWatcher, OnFocusChangeListener, OnItemClickListener {

	@ViewInject(R.id.query_guiji)
	private TextView query_guiji;
	@ViewInject(R.id.check_item)
	private LinearLayout check_item;// 检查项
	@ViewInject(R.id.check_item_tv)
	private TextView check_item_tv;
	@ViewInject(R.id.save_info)
	private TextView save_info;// 保存
	@ViewInject(R.id.cancal_info)
	private TextView cancal_info;// 取消

	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;

	@ViewInject(R.id.linear_register)
	private LinearLayout linear_register;
	@ViewInject(R.id.fast_inspect)
	private LinearLayout fast_inspect;
	@ViewInject(R.id.car_number_weifa)
	private LinearLayout vnumber_weifa;// 车辆违法大
	@ViewInject(R.id.car_weifa)
	private LinearLayout car_weifa;// 车辆违法小
	@ViewInject(R.id.driver_weifa)
	private LinearLayout driver_weifa;// 人违法大
	@ViewInject(R.id.people_weifa)
	private LinearLayout people_weifa;// 人违法小
	@ViewInject(R.id.company_weifa)
	private LinearLayout company_weifa;// 业户违法大
	@ViewInject(R.id.yehu_weifa)
	private LinearLayout yehu_weifa;// 业户违法小

	@ViewInject(R.id.fast_inspect_tv)
	private TextView fast_inspect_tv;
	@ViewInject(R.id.register_info)
	private TextView register_info;
	@ViewInject(R.id.hylb)
	private TextView hylb;

	@ViewInject(R.id.shaomiao_name)
	private ImageButton shaomiao_name;
	@ViewInject(R.id.shaomiao_id)
	private ImageButton shaomiao_id;

	@ViewInject(R.id.car_weifa_info)
	private TextView car_weifa_info;//
	@ViewInject(R.id.people_weifa_info)
	private TextView people_weifa_info;//
	@ViewInject(R.id.yehu_weifa_info)
	private TextView yehu_weifa_info;//

	@ViewInject(R.id.weifa_data_car)
	private TextView weifa_data_car;// 车辆是否有违法数据
	@ViewInject(R.id.weifa_count_car)
	private TextView weifa_count_car;// 车辆违法次数
	@ViewInject(R.id.weifa_data_people)
	private TextView weifa_data_people;// 人员是否有违法数据
	@ViewInject(R.id.weifa_count_people)
	private TextView weifa_count_people;// 人员违法次数
	@ViewInject(R.id.weifa_data_yehu)
	private TextView weifa_data_yehu;// 业户是否有违法数据
	@ViewInject(R.id.weifa_count_yehu)
	private TextView weifa_count_yehu;// 业户违法次数

	@ViewInject(R.id.list_show_corpName)
	private FullListView list_show_corpName;
	@ViewInject(R.id.list_show_driverName)
	private FullListView list_show_driverName;
	@ViewInject(R.id.show_corpName_by_corpName)
	private FullListView show_corpName_by_corpName;
	@ViewInject(R.id.list_show_zJZH)
	private FullListView list_show_zJZH;

	@ViewInject(R.id.licences_lihu)
	private EditText licences_lihu;
	@ViewInject(R.id.licences_jiandu)
	private EditText licences_jiandu;
	@ViewInject(R.id.choose_peopletype_tv)
	private TextView choose_peopletype_tv;// 当事人选择

	@ViewInject(R.id.driver_name_et)
	private EditText driver_name_et;// 人员名称
	@ViewInject(R.id.company_name_et)
	private EditText company_name_et;// 公司名称
	@ViewInject(R.id.carNumber_one)
	private TextView carNumber_one;// 车牌号码首字母
	@ViewInject(R.id.carNumber_two)
	private EditText carNumber_two;// 车牌号码后几位

	@ViewInject(R.id.car_number_fast)
	private TextView car_number_fast;// 快速稽查-车牌号码
	@ViewInject(R.id.company_name_fast)
	private TextView company_name_fast;// 快速稽查-公司名称
	@ViewInject(R.id.current_location)
	private TextView current_location;// 快速稽查-当前位置
	@ViewInject(R.id.car_type)
	private TextView car_type;// 快速稽查-车辆型号
	@ViewInject(R.id.car_color)
	private TextView car_color;// 快速稽查-车身颜色
	@ViewInject(R.id.weigui_time_car)
	private TextView weigui_time_car;// 快速稽查-车辆违规时间
	@ViewInject(R.id.weigui_context_car)
	private TextView weigui_context_car;// 快速稽查-车辆违规内容

	@ViewInject(R.id.driver_name_fast)
	private TextView driver_name_fast;// 快速稽查-人员姓名
	@ViewInject(R.id.licences_driver_fast)
	private TextView licences_driver_fast;// 快速稽查-监督卡号
	@ViewInject(R.id.company_name_driver_fast)
	private TextView company_name_driver_fast;// 快速稽查-人员公司名称
	@ViewInject(R.id.photo_driver)
	private ImageView photo_driver;// 快速稽查-人员头像
	@ViewInject(R.id.weigui_time_people)
	private TextView weigui_time_people;// 快速稽查-人员违规时间
	@ViewInject(R.id.weigui_context_people)
	private TextView weigui_context_people;// 快速稽查-人员违规内容

	@ViewInject(R.id.company_name_yehu_fast)
	private TextView company_name_yehu_fast;// 快速稽查-业户名称
	@ViewInject(R.id.jingying_licences)
	private TextView jingying_licences;// 快速稽查-经营许可证
	@ViewInject(R.id.faren)
	private TextView faren;// 快速稽查-法人名称
	@ViewInject(R.id.weigui_time_yehu)
	private TextView weigui_time_yehu;// 快速稽查-业户违规时间
	@ViewInject(R.id.weigui_context_yehu)
	private TextView weigui_context_yehu;// 快速稽查-业户违规内容

	@ViewInject(R.id.keyi)
	private TextView keyi;
	@ViewInject(R.id.keyi_image)
	private ImageView keyi_image;

	@ViewInject(R.id.turn_waiqing)
	private TextView turn_waiqing;
	@ViewInject(R.id.ll1)
	private LinearLayout ll1;
	@ViewInject(R.id.ll2)
	private LinearLayout ll2;
	@ViewInject(R.id.ll3)
	private LinearLayout ll3;
	@ViewInject(R.id.ll4)
	private LinearLayout ll4;

	@ViewInject(R.id.history_path)
	private LinearLayout history_path;
	@ViewInject(R.id.car_number_history)
	private TextView car_number_history;
	@ViewInject(R.id.tv_start_time)
	private TextView tv_start_time;
	@ViewInject(R.id.tv_end_time)
	private TextView tv_end_time;
	@ViewInject(R.id.iv_setting)
	private ImageView iv_setting;
	@ViewInject(R.id.image_see)
	private Button image_see;
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_end)
	private TextView tv_end;
	@ViewInject(R.id.PathDistance)
	private TextView PathDistance;
	// 行业类别
	@ViewInject(R.id.check_industry)
	private TextView check_industry;

	@ViewInject(R.id.ll_yehu_phone)
	private LinearLayout ll_yehu_phone;
	@ViewInject(R.id.business_phone_et)
	private TextView business_phone_et;// 司机资格证
	@ViewInject(R.id.xukezheng_number)
	private TextView xukezheng_number;// 司机资格证
	@ViewInject(R.id.yehu_name)
	private TextView yehu_name;// 司机资格证
	@ViewInject(R.id.driver_phone_et)
	private EditText driver_phone_et;

	@ViewInject(R.id.check_ll)
	private LinearLayout check_ll;// 人员检查内容布局
	@ViewInject(R.id.check_context)
	private TextView check_context;// 人员检查项内容

	@ViewInject(R.id.lv_law_inpsect)
	private BaseListView lvCheckItem;

	private List<CarStateBean> checkDatas;
	private LawInpsectAdapter checkAdapter;

	BitmapUtils bitmap;
	boolean isYeHu = false;
	boolean issj = false;
	boolean iszjzh = false;
	boolean isR = false;
	boolean isBeiJia = false;
	boolean isDriver = true;

	boolean isDriverCompany = false;
	boolean isDriverLhh = false;

	boolean isYHToLH = false;
	boolean isLHToYH = false;
	boolean isYHOver = false;
	boolean isLHOver = false;

	/** 这些是从临时跳转过来，初始时不能让一些显示框弹出ListView */
	boolean isTempId = false;
	boolean isTempName = false;
	boolean isTempCpLicense = false;
	boolean isTempCpName = false;

	// 是否沿用了常用地址
	boolean iscommon = false;
	// 是否已转外勤
	boolean isTurnWaiQin = false;
	// 是否安装外勤软件
	boolean isHaveWaiqin = false;

	String sfzh = "";
	String jclb = "";
	String id_card = "";
	String WeiFaStatus = "";
	int colors = Color.RED;
	String html = "";
	String htmls = "";
	String yujingNR;
	PositionPreference posiPrefer;
	MapPositionPreference mapPPrefer;

	String[] itemOne = { "巡游车","网约车","非法经营出租汽车" };
	String JingChaNR = "服饰不整洁未保持车辆整洁在出租汽车计价器损坏、失准、显示不全时仍营运载客收款后未向乘客开具项目填写齐全并与实收金额相符的专用收费凭证"
			+ "驾驶员营运时身体异味驾驶员营运时未按要求穿着工作服男驾驶员蓄胡须、留盖耳长发货剃光头车内有异味未按规定位置张贴价标车窗有遮挡物"
			+ "计价器显示时间误差在5分钟以内专用收费凭证打印项目格式不对应但打印内容清晰、不影响辨认";
	String[] itemsFour = { "公民", "法人" };
	String[] itemfive = {"京","沪","津","渝","黑","吉","辽","蒙","冀","新","甘","青","陕","宁","豫","鲁","晋","皖", "鄂","湘","苏","川","黔","滇","桂","藏","浙","赣","粤","闽","台","琼","港","澳"};

	HeaderHolder holder;
	ArrayList<CompanyInfo> arrayListCompany = new ArrayList<>();
	ArrayList<String> jclbList = new ArrayList<>();// 存储检查项传递过来的检查类别

	ArrayList<String> wfxwIdList = new ArrayList<>();// 违法行为id
	ArrayList<String> wfqxIdList = new ArrayList<>();// 违法情形id

	String jclbResult = "";// 存储jclbList转换String格式
	String wfxwIdResult = "";// 存储wfxwList转换String格式
	String wfqxIdResult = "";// 存储wfqxList转换String格式
	PutInspectDataReturn pdr;
	TimerTask task;
	int hegeMC = 1;

	/*用于适配器需要的一些成员变量*/
	listAdapter adapter;
	String company_by_query;
	companyCorpNameAdapter adapterCorpName;
	driverNameAdapter adapterName;
	zjzhAdapter zjzhadapter;
	ArrayList<CompanyInfo> list_licence = new ArrayList<>();
	ArrayList<DriverInfo> list_driverName = new ArrayList<>();
	ArrayList<DriverInfo> list_zjzh = new ArrayList<>();

    ZfryPreference zfryPreference;

	/*用于判断监听的当前控件是哪个*/
	int whoHasFocus = -1;//判断当前是哪个editText获取监听
	static int VNAME = 0;
	static int JDKH = 1;
	static int DRIVER_NAME = 2;
	static int LHH = 3;
	static int COMPANY = 4;

	// 对数据进行排序
	Comparator<FastInspectWeiFaInfo> comparator = new Comparator<FastInspectWeiFaInfo>() {
		public int compare(FastInspectWeiFaInfo f1, FastInspectWeiFaInfo f2) {
			// 先排JCLB
			if (!f1.getCheckTime().equals(f2.getCheckTime())) {
				return f1.getCheckTime().compareTo(f2.getCheckTime());
			}

			return 0;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law_inpsect);
		STARTDATE = new Date();
		registerMsgReceiver();
		registerPathReceiver();
		bitmap = new BitmapUtils(self);
		bitmap.configDefaultLoadFailedImage(R.drawable.default_photo);
		initZfzh2();
		initView();
		startTimer();
		history_path.setVisibility(View.GONE);
		vnumber_weifa.setVisibility(View.GONE);
		driver_weifa.setVisibility(View.GONE);
		company_weifa.setVisibility(View.GONE);
		check_item.setVisibility(View.GONE);
		fast_inspect.setVisibility(View.GONE);
		linear_register.setVisibility(View.VISIBLE);
		iv_setting.setOnClickListener(this);
		image_see.setOnClickListener(this);
		initTurn();
		isTemp = getIntent().getBooleanExtra("IsTemp", false);
		if (isTemp) {
			initData();
		}
		Position();
	}

	private void initZfzh2() {
		ZfryPreference prez = new ZfryPreference(self);
		if(prez.getString("zfry1", null)!=null){
			if(new UserPreference(self).getString("user_name_update_info", null).contains(prez.getString("zfry1", null))){
				//people_two = prez.getString("zfry2", "");
				zfzh2 = prez.getString("zfzh2", "");
			}
		}
	}

	boolean isTemp = false;
	Inspector Inspectors;

	/**
	 * TODO 因为有很多地方跳转到这个界面，每个地方传入的值不一样，在这里统一进行处理
	 */
	private void initTurn() {
		Intent intent = getIntent();
		isYeHu = intent.getBooleanExtra("isYeHu", false);
		String vName = intent.getStringExtra("Vname");
		if (!TextUtils.isEmpty(vName)) {
			carNumber_one.setText(vName.substring(0, 1));
			carNumber_two.setText(vName.substring(1));
		}
		isOver = intent.getBooleanExtra("isOver", false);
		isTurn = intent.getBooleanExtra("isTurn", false);
		liHuHao = intent.getStringExtra("Lhh");
		corpNameYeHu = intent.getStringExtra("CompanyName");
		faRenDaiBiao = intent.getStringExtra("FRDB");

		String DriverName = intent.getStringExtra("DriverName");
		cyzgz = intent.getStringExtra("JianduNumber");
		imageId = intent.getStringExtra("imageUrl");

		licences_jiandu.setText(cyzgz);
		driver_name_et.setText(DriverName);
		licences_lihu.setText(liHuHao);
		company_name_et.setText(corpNameYeHu);
		if (isOver) {
			queryDriverInfo(cyzgz,1);
		}
	}

	/**
	 * TODO 查询司机信息
	 */
	private void queryDriverInfo(String param,final int type) {
		final DriverInfo info = new DriverInfo();
		if(type ==3){
			info.setDriverName(param);
		}else{
			info.setJianduNumber(param);
		}
		info.setStartIndex(0);
		info.setEndIndex(10);
		AsyncUtil.goAsync(new Callable<Result<List<DriverInfo>>>() {

			@Override
			public Result<List<DriverInfo>> call() throws Exception {
				return WeiZhanData.queryDriverInfo(info);
			}
		}, new Callback<Result<List<DriverInfo>>>() {

			@Override
			public void onHandle(Result<List<DriverInfo>> result) {
				if (result.ok()) {
					if(type ==0){//当扫描二维码再返回时进入这个条件
						if (result.getData().equals("[]")) {
							UIUtils.toast(self, "查不到对应准驾证号！", Toast.LENGTH_SHORT);
							fastPeopleWeiFa();
						} else {
							imageId = result.getData().get(0).getImageID();
							fastPeopleWeiFa();
						}
						return;
					}

					if (result.getData().equals("[]")) {
						return;
					}

					/**
					 * 其他几种情况
					 */
					if(type ==1){//有的界面跳转时
						imageId = result.getData().get(0).getImageID();
						fastPeopleWeiFa();
					}else if(type ==2){//当准驾证改变引起的查询
						list_driverName.clear();
						list_driverName.addAll(result.getData());
						adapterName.notifyDataSetChanged();
					}else if(type ==3){//当司机姓名改变引起的查询
						list_zjzh.clear();
						list_zjzh.addAll(result.getData());
						zjzhadapter.notifyDataSetChanged();
					}

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	/**
	 * TODO 设置定时器，每隔一段时间重新保存一下，当出现程序崩溃等意外情况将数据临时保存
	 */
	public void startTimer() {
		Timer timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				File file = new File(Config.PATH + Config.FILE_NAME);
				if (file.exists()) {
					file.delete();
				}
				if (!(TextUtils.isEmpty(carNumber_two.getText().toString()))
						|| !(TextUtils.isEmpty(licences_jiandu.getText().toString()))
						|| !(TextUtils.isEmpty(driver_name_et.getText().toString()))
						|| !(TextUtils.isEmpty(licences_lihu.getText().toString()))
						|| !(TextUtils.isEmpty(company_name_et.getText().toString()))) {
					Store2SdUtil.getInstance(LawInpsectActivity.this).addOut(getInspector(), Config.FILE_NAME);
				}
			}
		};
		timer.schedule(task, 0, 3000);
	}

	/**
	 * TODO 从临时保存跳转过来的数据，执行这个方法
	 */
	private void initData() {
		isTempId = true;
		isTempName = true;
		isTempCpLicense = true;
		isTempCpName = true;
		Inspectors = (Inspector) getIntent().getSerializableExtra("Inspector");
		p.init(isTemp, Inspectors);
		if (Inspectors != null) {
			people_one = Inspectors.getZfry1();
			people_two = Inspectors.getZfry2();
			zfzh2 = Inspectors.getZfzh2();
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			Date d = null;
			try {
				d = sf.parse(Inspectors.getZfsj());
				time = (d.getTime()) / 1000 + "";
			} catch (Throwable e) {
				e.printStackTrace();
			}

			hylb.setText(Inspectors.getHylb());
			String carNumber_temp = Inspectors.getvNumber();
			if (!TextUtils.isEmpty(carNumber_temp)) {
				if (carNumber_temp.length() > 1) {
					String one = carNumber_temp.substring(0, 1);
					String two = carNumber_temp.substring(1);
					carNumber_one.setText(one);
					carNumber_two.setText(two);
				}
			}
			licences_jiandu.setText(Inspectors.getJdkh());
			driver_name_et.setText(Inspectors.getDriveName());
			licences_lihu.setText(Inspectors.getLhh());
			company_name_et.setText(Inspectors.getCompanyName());
			if (licences_jiandu.getText().toString().length() == 6) {
				fastPeopleWeiFa();
			}
			if (company_name_et.getText().toString().length() > 4) {
				fastCompanyWeiFa();
			}

			if (Inspectors.getTempLat() == 0.0) {
				Windows.confirm(self, "执法地址为空，请选择执法地址", new OnClickListener() {

					@Override
					public void onClick(View v) {
						p.show();
						isTemp = false;
					}
				});
			}
		}
	}

	/**
	 * TODO 当有无违法数据时，控制界面的显示
	 */
	private void checkData() {
		if (weifa_data_car.getText().toString().equals("无违法数据")) {
			car_weifa_info.setClickable(false);
			weifa_count_car.setVisibility(View.GONE);
			weifa_data_car.setVisibility(View.VISIBLE);
			car_weifa.setVisibility(View.GONE);
		} else {
			car_weifa_info.setClickable(true);
			weifa_count_car.setVisibility(View.VISIBLE);
			weifa_data_car.setVisibility(View.GONE);
			car_weifa.setVisibility(View.VISIBLE);
		}
		if (weifa_data_people.getText().toString().equals("无违法数据")) {
			people_weifa_info.setClickable(false);
			weifa_count_people.setVisibility(View.GONE);
			weifa_data_people.setVisibility(View.VISIBLE);
			people_weifa.setVisibility(View.GONE);
		} else {
			people_weifa_info.setClickable(true);
			weifa_count_people.setVisibility(View.VISIBLE);
			weifa_data_people.setVisibility(View.GONE);
			people_weifa.setVisibility(View.VISIBLE);
		}
		if (weifa_data_yehu.getText().toString().equals("无违法数据")) {
			yehu_weifa_info.setClickable(false);
			weifa_count_yehu.setVisibility(View.GONE);
			weifa_data_yehu.setVisibility(View.VISIBLE);
			yehu_weifa.setVisibility(View.GONE);
		} else {
			yehu_weifa_info.setClickable(true);
			weifa_count_yehu.setVisibility(View.VISIBLE);
			weifa_data_yehu.setVisibility(View.GONE);
			yehu_weifa.setVisibility(View.VISIBLE);
		}
	}



	private String initTextChanged(String str) {
		String returnString = "";
		int len = str.length();

		for (int i = 0; i < len; i++) {
			String s = str.substring(i, i + 1);
			if (Pattern.compile("[\u4e00-\u9fa5]").matcher(s).matches()) {// 汉字
				returnString = returnString + s;
			}
		}

		return returnString;
	}

	ArrayList<CheZuResultModel> arrayListCheZu;
	PopWindowDialog p;

	/**
	 * TODO 获取同一车组人员
	 */
	private void initCheZuData() {
		arrayListCheZu = new ArrayList<>();
		final CheZuRequestModel info = new CheZuRequestModel();
		info.setZFDWMC(new UserPreference(self).getString("zfdwmc", null));
		info.setSSCZ(new UserPreference(self).getString("sscz", null));
		info.setSSZD(new UserPreference(self).getString("sszd", null));
		AsyncUtil.goAsync(new Callable<Result<List<CheZuResultModel>>>() {

			@Override
			public Result<List<CheZuResultModel>> call() throws Exception {
				return WeiZhanData.queryCheZuZFRYNameinfo(info);
			}
		}, new Callback<Result<List<CheZuResultModel>>>() {

			@Override
			public void onHandle(Result<List<CheZuResultModel>> result) {

				if (result.ok()) {
					arrayListCheZu.addAll(result.getData());

				} else {
					/*
					 * UIUtils.toast(self, result.getErrorMsg(),
					 * Toast.LENGTH_SHORT);
					 */
				}
			}

		});
		p = new PopWindowDialog(self, arrayListCheZu);
		p.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				address = p.getAddressString();
				time = (p.getTimeString() / 1000) + "";
				law_time = p.getLawTime();
				numberTwo = p.getZfzhTwo();
				people_one = p.getRenYuanOneString();
				people_two = p.getRenYuanTwoString();
                zfzh2 = p.getZfzhtwo();
				iscommon = p.getIsCommon();
				// 如果不是从临时保存跳转过来的
				if (!isTemp) {
                    zfryPreference = new ZfryPreference(self);
                    zfryPreference.setString("zfry1", people_one);
                    zfryPreference.setString("zfry2", people_two);
                    zfryPreference.setString("zfzh2", zfzh2);
				}
				// 关闭弹出窗体后，根据经纬度获取区域信息
				if (p.ischongdingwei) {
					isTemp = false;
				}
				getDistrict();
			}
		});
	}


	private void initView() {
		ViewUtils.inject(self);
		//carNumber_two.requestFocus();
		holder = new HeaderHolder();
		holder.init(self, "执法稽查");
		posiPrefer = new PositionPreference(self);
		mapPPrefer = new MapPositionPreference(self);

		/*if (!isInstallApp(this, "com.example.zfzd_project")) {
			turn_waiqing.setText("处罚保存");// 外勤软件未安装
			isHaveWaiqin = false;
		} else {

		}*/
		turn_waiqing.setText("转外勤文书");
		isHaveWaiqin = true;
		holder.setUpRightBtn2(getResources().getDrawable(R.drawable.zhifarenyuan), new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (p != null) {
					p.show();
				} else {
					UIUtils.toast(self, "网络问题", Toast.LENGTH_SHORT);
				}

			}
		});
		holder.leftBtn.setOnClickListener(this);
		initListView();//初始化检查项的ListView
		initCacheJCX();//先把缓存中的检查项用着
		initCheZuData();//初始化车组信息
		queryLX();//初始化检查项类型
		setListener();//初始化控件监听
	}

	/**
	 * 用缓存中的检查项信息进行展示
	 */
	private void initCacheJCX() {
		checkDatas.clear();
		List<HyRyFg> jcxList =  CacheManager.getInstance().getJCX();
		if(jcxList == null) return;
		for (int i = 0; i < jcxList.size(); i++) {
			CarStateBean bean = new CarStateBean();

			bean.setCbBreakLawState(false);
			bean.setCbNormalState(false);
			bean.setType(jcxList.get(i).getLBMC());

			checkDatas.add(bean);
			checkAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * TODO 设置各种控件监听
	 */
	private void setListener() {
		fast_inspect_tv.setOnClickListener(this);
		car_weifa_info.setOnClickListener(this);
		people_weifa_info.setOnClickListener(this);
		yehu_weifa_info.setOnClickListener(this);
		register_info.setOnClickListener(this);
		weifa_count_car.setOnClickListener(this);
		weifa_count_people.setOnClickListener(this);
		weifa_count_yehu.setOnClickListener(this);
		choose_peopletype_tv.setOnClickListener(this);
		shaomiao_name.setOnClickListener(this);
		shaomiao_id.setOnClickListener(this);
		check_item_tv.setOnClickListener(this);
		query_guiji.setOnClickListener(this);
		save_info.setOnClickListener(this);
		cancal_info.setOnClickListener(this);
		turn_waiqing.setOnClickListener(this);
		hylb.setOnClickListener(this);
		carNumber_one.setOnClickListener(this);

		carNumber_two.setOnFocusChangeListener(this);
		carNumber_two.addTextChangedListener(this);

		licences_jiandu.setOnFocusChangeListener(this);
		licences_jiandu.addTextChangedListener(this);

		driver_name_et.setOnFocusChangeListener(this);
		driver_name_et.addTextChangedListener(this);

		licences_lihu.setOnFocusChangeListener(this);
		licences_lihu.addTextChangedListener(this);

		company_name_et.setOnFocusChangeListener(this);
		company_name_et.addTextChangedListener(this);

		adapterCorpName = new companyCorpNameAdapter();
		show_corpName_by_corpName.setAdapter(adapterCorpName);
		show_corpName_by_corpName.setOnItemClickListener(this);

		adapter = new listAdapter();
		list_show_corpName.setAdapter(adapter);
		list_show_corpName.setOnItemClickListener(this);

		adapterName = new driverNameAdapter();
		list_show_driverName.setAdapter(adapterName);
		list_show_driverName.setOnItemClickListener(this);

		zjzhadapter = new zjzhAdapter();
		list_show_zJZH.setAdapter(zjzhadapter);
		list_show_zJZH.setOnItemClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		switch (parent.getId()) {
			case R.id.show_corpName_by_corpName:
				CompanyInfo info = (CompanyInfo) parent.getItemAtPosition(position);
				isChoose = true;
				isYHOver = true;
				isYHToLH = true;
				setCompanyInfo(info,1);
				break;
			case R.id.list_show_corpName:
				CompanyInfo info2 = (CompanyInfo) parent.getItemAtPosition(position);
				//isTurn = true;
				isLHOver = true;
				isLHToYH = true;
				setCompanyInfo(info2,2);
				break;
			case R.id.list_show_driverName:
				DriverInfo info3 = (DriverInfo) parent.getItemAtPosition(position);
				issj = true;
				setDriverInfo(info3,1);
				break;
			case R.id.list_show_zJZH:
				DriverInfo info4 = (DriverInfo) parent.getItemAtPosition(position);
				iszjzh = true;
				setDriverInfo(info4,2);
				break;
		}
	}

	/**
	 * TODO 设置关于单位的信息
	 */
	private void setCompanyInfo(CompanyInfo info,int type){
		liHuHao = info.getLhh();
		corpNameYeHu = info.getCompanyName();
		faRenDaiBiao = info.getFarenDb();
		licences_lihu.setText(info.getLhh());
		company_name_et.setText(info.getCompanyName());
		if(type == 1){
			show_corpName_by_corpName.setVisibility(View.GONE);
		}else{
			list_show_corpName.setVisibility(View.GONE);
		}
		fastCompanyWeiFa();
	}

	/**
	 * TODO 设置关于司机的信息
	 */
	DriverInfo driverInfo;
	private void setDriverInfo(DriverInfo info,int type){
		this.driverInfo = info;
		cyzgz = info.getJianduNumber();
		corpName = info.getCompanyName();
		imageId = info.getImageID();
		driver_phone_et.setText(info.getTelphone());
		licences_jiandu.setText(info.getJianduNumber());
		driver_name_et.setText(info.getDriverName());
		if(type ==1){
			list_show_driverName.setVisibility(View.GONE);
		}
		list_show_zJZH.setVisibility(View.GONE);
		fastPeopleWeiFa();
	}

	/**
	 * TODO 当车牌号编辑框变更时执行的操作
	 */
	private void vnameChange(){
    	/*if (hylb.getText().toString().equals(itemOne[1])) {
			return;
		}*/
		carNumber = carNumber_two.getText().toString();
		car_number = (carNumber_one.getText().toString() + carNumber).toUpperCase();
		if (carNumber.length() == 6 || carNumber.length() == 7) {
			fastCarWeiFa();
		}
	}


	/**
	 * TODO 当准驾证号编辑框变更时执行的操作
	 */
	private void zjzhChangeInfo() {
		// 从临时跳转
		if (isTempId) {
			isTempId = false;
			return;
		}
		if (isOver) {
			return;
		}
		if (iszjzh) {
			iszjzh = false;
			return;
		}

		list_show_driverName.setVisibility(View.VISIBLE);
		list_driverName.clear();
		adapterName.notifyDataSetChanged();
		String jdkh = licences_jiandu.getText().toString();
		if (jdkh.length() < 5) {
			list_show_driverName.setVisibility(View.GONE);
		}
		if (jdkh.length() > 4) {
			queryDriverInfo(jdkh,2);
		}
	}

	/**
	 * TODO 当司机姓名编辑框变更时执行的操作
	 */
	private void driverNameChange() {
		if (hylb.getText().toString().equals(itemOne[1])) {
			return;
		}
		if (TextUtils.isEmpty(driver_name_et.getText().toString())
				|| driver_name_et.getText().toString().length() < 2) {
			list_show_zJZH.setVisibility(View.GONE);
			return;
		}
		// 从临时跳转
		if (isTempName) {
			isTempName = false;
			return;
		}

		// 从背夹跳转
		if (isBeiJia) {
			return;
		}

		if (isOver) {
			return;
		}
		// 如果弹出过显示司机名字的下拉框则不执行下面的
		if (issj) {
			issj = false;
			return;
		}
		// 修改名字，弹出下拉框
		list_show_zJZH.setVisibility(View.VISIBLE);
		list_zjzh.clear();
		zjzhadapter.notifyDataSetChanged();
		String drivername = driver_name_et.getText().toString();
		if (drivername.getBytes().length < 3) {
			list_show_zJZH.setVisibility(View.GONE);
		}
		if (drivername.getBytes().length > 3) {
			queryDriverInfo(drivername, 3);
		}
		if (isTemp) {
			list_show_zJZH.setVisibility(View.GONE);
		}
	}

	/**
	 * TODO 当立户号编辑框变更时执行的操作
	 */
	private void lhhChange() {
		if (hylb.getText().toString().equals(itemOne[1])) {
			return;
		}

		if(licences_lihu.length()==0){
			list_licence.clear();
			list_show_corpName.setVisibility(View.GONE);
			return;
		}

		// 从临时跳转
		if (isTempCpLicense) {
			isTempCpLicense = false;
			return;
		}

		if (isTurn) {
			return;
		}

		if (isDriverLhh) {
			return;
		}

		if (isYHToLH) {
			isYHToLH = false;
			return;
		}
		if (isLHOver) {
			isLHOver = false;
			return;
		}
		list_licence.clear();

		licence = licences_lihu.getText().toString();
		if (licence.length() < 2) {
			return;
		}
		list_show_corpName.setVisibility(View.VISIBLE);
		final CompanyInfo info = new CompanyInfo();
		info.setLhh(licence);
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
				if (result.ok()) {
					if (result.getData().equals("[]")) {

					} else {
						list_licence.clear();
						list_licence.addAll(result.getData());
						adapter.notifyDataSetChanged();
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	/**
	 * TODO 当单位名称编辑框变更时执行的操作
	 */
	private void companyChange() {
		arrayListCompany.clear();
		if (hylb.getText().toString().equals(itemOne[1])) {
			return;
		}
		// 从临时跳转
		if (isTempCpName) {
			isTempCpName = false;
			return;
		}
		if(company_name_et.length()==0){
			arrayListCompany.clear();
			show_corpName_by_corpName.setVisibility(View.GONE);
			return;
		}
		if (isChoose) {
			return;
		}
		if (TextUtils.isEmpty(company_name_et.getText().toString())
				|| company_name_et.getText().toString().length() < 2) {
			return;
		}

		if (isYHOver) {
			isYHOver = true;
		}

		if (isLHToYH) {
			isLHToYH = true;
		}

		if (isTurn) {
			return;
		}

		String compCorpName = company_name_et.getText().toString();
		final CompanyInfo info = new CompanyInfo();
		info.setCompanyName(compCorpName);
		info.setStartIndex(0);
		info.setEndIndex(10);
		AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {
			@Override
			public Result<List<CompanyInfo>> call() throws Exception {
				return WeiZhanData.queryYeHuInfo(info);
			}
		}, new Callback<Result<List<CompanyInfo>>>() {

			@Override
			public void onHandle(Result<List<CompanyInfo>> result) {
				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
						return;
					}
					arrayListCompany.clear();
					arrayListCompany.addAll(result.getData());
					show_corpName_by_corpName.setVisibility(View.VISIBLE);
					adapterCorpName.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	private int currentClickItemIndex;
	private ArrayList<String> ItemNameList;

	/**
	 * TODO 初始化检查项相关的ListView
	 */
	private void initListView() {
		checkDatas = new ArrayList<>();
		checkAdapter = new LawInpsectAdapter(self, checkDatas);
		ItemNameList = new ArrayList<>();
		checkAdapter.setOnSubItemsClickListener(new LawInpsectAdapter.OnSubItemsClickListener() {

			@Override
			public void onCbNormalClick(View view, int position) {
				if (checkDatas.get(position).isCbNormalState()) {
					checkDatas.get(position).setCbNormalState(false);
					if(ItemNameList.contains(checkDatas.get(position).getType())){
						ItemNameList.remove(ItemNameList.indexOf(checkDatas.get(position).getType()));
					}
				} else {
					ItemNameList.add(checkDatas.get(position).getType());
					checkDatas.get(position).setCbNormalState(true);
				}

				if (checkDatas.get(position).isCbBreakLawState()) {
					checkDatas.get(position).setCbBreakLawState(false);
				}

				checkAdapter.notifyDataSetChanged();

				if(currentClickItemIndex == position){
					posiPrefer.clearPreference();
					check_ll.setVisibility(View.GONE);
				}
			}

			@Override
			public void onTvCheckClick(View view, int position) {
				currentClickItemIndex = position;
				Intent intent = new Intent(self, VehicleCheckActivity.class);
				String hhylb = hylb.getText().toString();
				intent.putExtra("HYLB", hhylb);
				intent.putExtra("HYRYLX", checkDatas.get(position).getType());
				startActivityForResult(intent, 1000);
			}

			@Override
			public void onCbBreakLawClick(View view, int position) {
				currentClickItemIndex = position;

				Intent intent = new Intent(self, ChckItemLeiBieActivity.class);
				String s = hylb.getText().toString();
				intent.putExtra("HYLB", s);
				intent.putExtra("HYRYLX", checkDatas.get(position).getType());
				startActivityForResult(intent, 1000);
			}
		});
		lvCheckItem.setAdapter(checkAdapter);
	}

	@Override
	public void onClick(View v) {
		if(v == holder.leftBtn){
			onBackPressed();
		}else if (v == fast_inspect_tv) {
			showLargeLinearLayout(fast_inspect_tv, fast_inspect);

		} else if (v == car_weifa_info) {
			showShortLinearLayout(car_weifa);

		} else if (v == people_weifa_info) {
			showShortLinearLayout(people_weifa);

		} else if (v == yehu_weifa_info) {
			showShortLinearLayout(yehu_weifa);

		} else if (v == register_info) {
			showLargeLinearLayout(register_info, linear_register);

		} else if (v == choose_peopletype_tv) {
			selectPeopleType();

		} else if (v == hylb) {
			selectHylb();

		} else if (v == weifa_count_car) {
			if (CarInfo.size() > 1) {
				Intent intent = new Intent(self, FastInspectWeiFaInfoActivity.class);
				intent.putExtra("weifaInfo", CarInfo);
				startActivity(intent);
			}

		} else if (v == weifa_count_people) {
			if (PeopleInfo.size() > 1) {
				Intent intent = new Intent(self, FastInspectWeiFaInfoActivity.class);
				intent.putExtra("weifaInfo", PeopleInfo);
				startActivity(intent);
			}

		} else if (v == weifa_count_yehu) {
			if (CompanyInfo.size() > 1) {
				Intent intent = new Intent(self, FastInspectWeiFaInfoActivity.class);
				intent.putExtra("weifaInfo", CompanyInfo);
				startActivity(intent);
			}

		} else if (v == shaomiao_name) {
			Intent intent = new Intent(self, IdcardScanActivity.class);
			startActivityForResult(intent, 33);

		} else if (v == shaomiao_id) {
			Intent intent = new Intent(self, QRcodeActivity.class);
			startActivityForResult(intent, 9);

		} else if (v == check_item_tv) {
			showLargeLinearLayout(check_item_tv, check_item);
			scrollView.post(new Runnable() {
				public void run() {
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});

		} else if (v == query_guiji) {
			Intent intent = new Intent(self, PathActivity2.class);
			intent.putExtra("vName", car_number.toUpperCase());
			intent.putExtra("licences_jiandu", licences_jiandu.getText().toString());
			intent.putExtra("driver_name_et", driver_name_et.getText().toString());
			intent.putExtra("licences_lihu", licences_lihu.getText().toString());
			intent.putExtra("company_name_et", company_name_et.getText().toString());
			startActivity(intent);

		} else if (v == cancal_info) {
			onBackPressed();

		} else if (v == save_info) {
			saveInspectInfo();

		} else if (v == turn_waiqing) {
			trunWaiQin();

		} else if (v == iv_setting) {
			Intent intent = new Intent(self, PathActivity2.class);
			intent.putExtra("car_number_history", car_number_history.getText().toString());
			intent.putExtra("tv_start_time", tv_start_time.getText().toString());
			intent.putExtra("tv_end_time", tv_end_time.getText().toString());
			intent.putExtra("licences_jiandu", licences_jiandu.getText().toString());
			intent.putExtra("driver_name_et", driver_name_et.getText().toString());
			intent.putExtra("licences_lihu", licences_lihu.getText().toString());
			intent.putExtra("company_name_et", company_name_et.getText().toString());
			startActivity(intent);

		} else if (v == image_see) {
			checkPhoto();

		} else if (v == carNumber_one){
			Windows.singleChoice(self, "选择车牌", itemfive, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					carNumber_one.setText(itemfive[position]);
				}
			});

		}
	}

	/**
	 * TODO 点击查看照片
	 */
	private void checkPhoto() {
		File file = new File(Config.PATH + "inspect.png");
		if (file.exists()) {
			View view = LayoutInflater.from(self).inflate(R.layout.show_image_view, null);
			LinearLayout ll_main = (LinearLayout) view.findViewById(R.id.show_imageView_main);

			ImageView imageView = (ImageView) view.findViewById(R.id.show_imageView);

			bitmap.display(imageView, Config.PATH + "inspect.png");

			Builder d = new Builder(self);
			final AlertDialog dialog = d.create();

			dialog.setView(view);
			dialog.show();

			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			WindowManager wm = (WindowManager) self.getSystemService(Context.WINDOW_SERVICE);

			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
			params.width = width;
			params.height = height;
			dialog.getWindow().setAttributes(params);
		} else {
			UIUtils.toast(self, "没有保存轨迹图...", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * TODO 转外勤
	 */
	private void trunWaiQin() {
		if(!checkOut()){//如果输入信息有误则不进行下面的操作
			return;
		}
		final String people_weifa = showSaveInfo();
		String[] items = { "警告", "处罚" };
		Windows.singleChoice(self, "请选择案件状态", items, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				switch (position) {
					// 警告
					case 0:
						if (!JingChaNR.contains(people_weifa)) {
							UIUtils.toast(self, "该违法行为不能作为警告处理！", Toast.LENGTH_SHORT);
							return;
						}
						status = "4";
						WeiFaStatus = "警告";
						htmls = html + "<p>" + "检查案件状态为" + "<strong><font color=\"#F3A401\">" + WeiFaStatus
								+ "</font></strong>" + ",是否保存" + "</p>" + "</body></html>";
						break;
					case 1:
						// 处罚
						status = "5";
						WeiFaStatus = "处罚";
						htmls = html + "<p>" + "检查案件状态为" + "<strong><font color=\"#F54359\">" + WeiFaStatus
								+ "</font></strong>" + ",是否保存" + "</p>" + "</body></html>";
						break;
				}
				// 创建
				Windows.confirm2(self, 0, "创建预览", htmls, "创建", new OnClickListener() {

					@Override
					public void onClick(View v) {
						isTurnWaiQin = isHaveWaiqin;

						// 创建后就先执行保存
						Save("成功");

					}
				}, "取消", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				}, 0, null);
			}
		});
	}

	/**
	 * TODO 展示要保存的信息
	 * @return
	 */
	private String showSaveInfo() {
		String hangyeleibie = "行业类别: ";
		String shangyeleibie = hylb.getText().toString();
		String zhifry1 = "执法人员1: ";
		if(TextUtils.isEmpty(people_one)){
			people_one = new UserPreference(self).getString("user_name_update_info", "");
		}
		String szhifry1 = people_one;
		String zhifry2 = "执法人员2: ";
		String szhifry2 = people_two;
		String zhifAdd = "执法地址: ";
		String szhifAdd = isNull.isEmpty(address);
		String zhifSj = "执法时间: ";
		String szhifSj = law_time;
		String CarId = "车牌号码: ";
		String sCarId = carNumber_one.getText().toString() + carNumber_two.getText().toString().toUpperCase();
		String DriverName = "司机姓名: ";
		String sDriverName = isNull.isEmpty(driver_name_et.getText().toString());
		String zjzh = "准驾证号: ";
		String szjzh = isNull.isEmpty(licences_jiandu.getText().toString());
		String jyxk = "立  户  号: ";
		String sjyxk = "无";
		if (TextUtils.isEmpty(licences_lihu.getText().toString())) {
			sjyxk = "无";
		} else {
			sjyxk = licences_lihu.getText().toString();
		}
		String yehuname = "单位名称: ";
		String syehuname = isNull.isEmpty(company_name_et.getText().toString());
		final String people_weifa = isNull.isEmpty(check_context.getText().toString());
		String renyuanweifa = "违法内容: ";
		String srenyuanweifa = people_weifa;

		String over1 = "检查案件";
		String over2 = "正常";
		String over3 = "，是否保存?";

		if (!sjyxk.equals("无")) {
			html = "<html><body>" + "<p><strong>" + hangyeleibie + "</strong>" + shangyeleibie + "</p>"
					+ "<p><strong>" + zhifry1 + "</strong>" + szhifry1 + "</p>" + "<p><strong>" + zhifry2
					+ "</strong>" + szhifry2 + "</p>" + "<p><strong>" + zhifAdd + "</strong>" + szhifAdd + "</p>"
					+ "<p><strong>" + zhifSj + "</strong>" + szhifSj + "</p>" + "<p><strong>" + CarId + "</strong>"
					+ sCarId + "</p>" + "<p><strong>" + DriverName + "</strong>" + sDriverName + "</p>"
					+ "<p><strong>" + zjzh + "</strong>" + szjzh + "</p>" + "<p><strong>" + jyxk + "</strong>"
					+ sjyxk + "</p>" + "<p><strong>" + yehuname + "</strong>" + syehuname + "</p>" + "<p><strong>"
					+ renyuanweifa + "</strong>" + srenyuanweifa + "</p>";
		} else {
			html = "<html><body>" + "<p><strong>" + hangyeleibie + "</strong>" + shangyeleibie + "</p>"
					+ "<p><strong>" + zhifry1 + "</strong>" + szhifry1 + "</p>" + "<p><strong>" + zhifry2
					+ "</strong>" + szhifry2 + "</p>" + "<p><strong>" + zhifAdd + "</strong>" + szhifAdd + "</p>"
					+ "<p><strong>" + zhifSj + "</strong>" + szhifSj + "</p>" + "<p><strong>" + CarId + "</strong>"
					+ sCarId + "</p>" + "<p><strong>" + DriverName + "</strong>" + sDriverName + "</p>"
					+ "<p><strong>" + zjzh + "</strong>" + szjzh + "</p>" + "<p><strong>" + renyuanweifa
					+ "</strong>" + srenyuanweifa + "</p>";
		}
		return people_weifa;
	}

	/**
	 * TODO 检验输入信息是否满足条件
	 * @return 不满足则返回错误
	 */
	private boolean checkOut() {
		if (("").equals(people_two)) {
			ZfryPreference prez = new ZfryPreference(self);
			UserPreference pref = new UserPreference(self);
			// 如果当前账号为上次登录账号，则获取记录的执法人员2
			if (prez.getString("zfry1", null) != null) {
				if (pref.getString("user_name_update_info", null).contains(prez.getString("zfry1", null))) {
					people_one = pref.getString("user_name_update_info", null);
					people_two = prez.getString("zfry2", null);
				}
			}
			time = (System.currentTimeMillis() / 1000) + "";
			law_time = new SimpleDateFormat(datePatterShow).format(new Date(System.currentTimeMillis()));
		}
		if (("").equals(people_two)) {
			UIUtils.toast(self, "请点击右上角确定人员信息！", Toast.LENGTH_SHORT);
			return false;
		}
		if ("法人".equals(choose_peopletype_tv.getText().toString())) {
			if (TextUtils.isEmpty(licences_lihu.getText().toString())
					|| TextUtils.isEmpty(business_phone_et.getText().toString())
					|| TextUtils.isEmpty(company_name_et.getText().toString())) {
				UIUtils.toast(self, "请将经营业户信息填写完整", Toast.LENGTH_SHORT);
				return false;
			}
		}
		if ("administrator".equals(people_one)) {
			UIUtils.toast(self, "管理员账号不能进行保存！", Toast.LENGTH_SHORT);
			return false;
		}
		if ("0000000".equals(people_one)) {
			UIUtils.toast(self, "测试账号不能进行保存！", Toast.LENGTH_SHORT);
			return false;
		}
		if (people_two.equals(people_one)) {
			UIUtils.toast(self, "两个执法人员为同一个人，请更正！", Toast.LENGTH_SHORT);
			return false;
		}
		if (("").equals(address) || address.length() < 2) {
			UIUtils.toast(self, "地址为空，请输入！", Toast.LENGTH_SHORT);
			return false;
		}

		if ("法人".equals(choose_peopletype_tv.getText().toString())) {
			if (TextUtils.isEmpty(licences_lihu.getText().toString())
					|| TextUtils.isEmpty(business_phone_et.getText().toString())
					|| TextUtils.isEmpty(company_name_et.getText().toString())) {
				UIUtils.toast(self, "请将经营业户信息填写完整", Toast.LENGTH_SHORT);
				return false;
			}
		}else{
			if (TextUtils.isEmpty(driver_name_et.getText().toString())) {
				UIUtils.toast(self, "请输入司机姓名", Toast.LENGTH_LONG);
				return false;
			}
			if (TextUtils.isEmpty(driver_phone_et.getText().toString())) {
				UIUtils.toast(self, "司机电话不能为空", Toast.LENGTH_LONG);
				return false;
			}
			if (TextUtils.isEmpty(licences_jiandu.getText().toString())) {
				UIUtils.toast(self, "请输入准驾证号！", Toast.LENGTH_SHORT);
				return false;
			}
			if (TextUtils.isEmpty(carNumber_two.getText().toString()) || carNumber_two.length() <6) {
				UIUtils.toast(self, "请输入正确的车牌号", Toast.LENGTH_SHORT);
				return false;
			}

		}

		if (TextUtils.isEmpty(check_context.getText().toString())) {
			UIUtils.toast(self, "检查案件无违法行为,不能转外勤", Toast.LENGTH_SHORT);
			return false;
		}


		return true;
	}

	private void saveInspectInfo() {
		if(!checkOutNormal()){
			return;
		}
		showNormalInfo();
		String[] items = { "正常", "表扬", "批教" };
		Windows.singleChoice(self, "请选择案件状态", items, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				switch (position) {
					case 0:
						hegeMC = 1;
						status = "0";
						WeiFaStatus = "正常";
						htmls = html + "<p>" + "检查案件状态为" + "<strong><font color=\"#2736f2\">" + WeiFaStatus
								+ "</font></strong>" + ",是否保存" + "</p>" + "</body></html>";
						break;
					case 1:
						hegeMC = 2;
						status = "2";
						WeiFaStatus = "表扬";
						htmls = html + "<p>" + "检查案件状态为" + "<strong><font color=\"#48e523\">" + WeiFaStatus
								+ "</font></strong>" + ",是否保存" + "</p>" + "</body></html>";
						break;
					case 2:
						hegeMC = 3;
						status = "3";
						WeiFaStatus = "批教";
						htmls = html + "<p>" + "检查案件状态为" + "<strong><font color=\"#A90CFD\">" + WeiFaStatus
								+ "</font></strong>" + ",是否保存" + "</p>" + "</body></html>";
						break;
					default:
						break;
				}

				Windows.confirm3(self, 0, "保存预览", htmls, "保存", new OnClickListener() {

					@Override
					public void onClick(View v) {
						final MyProgressDialog pd = Windows.waiting(self);
						String vname = "";
						String car_number = carNumber_two.getText().toString().toUpperCase();
						if (!TextUtils.isEmpty(car_number) && car_number.length() >= 6) {
							vname = carNumber_one.getText().toString() + car_number;
						}
						final RegisterInfoNew info = new RegisterInfoNew();
						info.setZfdwmc(new UserPreference(self).getString("zfdwmc", null));

						info.setDriverName(driver_name_et.getText().toString());
						info.setHylb(hylb.getText().toString());

						info.setJclb(jclb);
						info.setVname(vname);
						if(!TextUtils.isEmpty(people_one)){
							info.setZfry1(people_one);
						}else{
							info.setZfry1(new UserPreference(self).getString("user_name_update_info", ""));
						}
						info.setZfry2(people_two);
						info.setAddress(address);

						if (TextUtils.isEmpty(time)) {
							long time_temp = System.currentTimeMillis();
							time_temp = time_temp / 1000;
							time = time_temp + "";
						}

						info.setZfsj(time);
						info.setId(null);
						info.setLlh(licence);
						info.setJdkh(cyzgz);
						info.setCorpname(company_name_et.getText().toString());
						String people_weifa = check_context.getText().toString();
						info.setYyzh(null);
						info.setJyxkz(JYXKZ);
						info.setSfzh(ID_CARD);
						// String jcx = getJCX().split(":")[1].replace("\"",
						// "").replace("}", "");
						// 上传检查主体
						info.setYehu_check_items(getJCX());
						info.setPerson_check_items(people_weifa);
						info.setVehicle_check_items(yujingNR);
						info.setStatus(status);
						info.setCorpname(corpNameYeHu);

						if (isTemp) {
							info.setLat(Inspectors.getTempLat());
							info.setLon(Inspectors.getTempLng());
						} else if (iscommon) {
							info.setLat(MsgConfig.common_lat);
							info.setLon(MsgConfig.common_lng);
						} else if (MsgConfig.select_lat != 0) {
							info.setLat(MsgConfig.select_lat);
							info.setLon(MsgConfig.select_lng);
						} else {
							info.setLat(MsgConfig.lat);
							info.setLon(MsgConfig.lng);
						}
						info.setDataSource(0);
						info.setNormal(1);
						info.setIllegal(0);
						info.setTotal(1);
						/*
						 * if (TextUtils.isEmpty(people_weifa)) {
						 * info.setNormal(0); info.setIllegal(0);
						 * info.setTotal(0); } else { info.setNormal(0);
						 * info.setIllegal(1); info.setTotal(1); }
						 */
						info.setZfzh(new UserPreference(self).getString("user_name", null));
						info.setZwstatus("未转");

						info.setJcbh(new SimpleDateFormat(datePatter2).format(new Date(System.currentTimeMillis())));

						// 新添加字段

						info.setBrand(BRAND);// 车辆厂牌，未添加数据
						info.setCertificate_no(CERTIFICATE_NO);// 运营证号
						info.setColor(COLOR);// 车辆颜色
						// 车架号 vin
						info.setVin(VIN);
						// 车辆型号 xh
						info.setXh(XH);
						// 车辆类型 model
						info.setModel(MODEL);
						// 人员所属单位 pcompan
						info.setPcompany(CORPNAME);// 未添加数据
						info.setVcompany(NAME);
						// 手机号 phone
						info.setPhone(driver_phone_et.getText().toString());
						// 家庭住址 family_addr
						info.setFamily_addr(FAMILY_ADDR);
						// 公司法人代表 frdb
						info.setFrdb(FRDB);
						// 执法地域（前面有个执法地点，这个地域是传"东城区"这种） CheckPlaceOrgan
						info.setCheckPlaceOrgan(district);//
						// 运营证号
						info.setCertificate_no(CERTIFICATE_NO);
						// 违法行为id wfxwId
						info.setWfxwId(wfxwId);
						// 违法情形id wfqxId
						info.setWfqxId(wfqxId);

						final ZhiFaJianChaModel zhiFaJianChaModel = InfoRetun();
						AsyncUtil.goAsync(new Callable<Result<PutInspectDataReturn>>() {

							@Override
							public Result<PutInspectDataReturn> call() throws Exception {
								return UserData.PutInspectDataNew(info);
							}
						}, new Callback<Result<PutInspectDataReturn>>() {

							@Override
							public void onHandle(Result<PutInspectDataReturn> result) {
								pd.dismiss();
								if (result.ok()) {
									final File file = new File(Config.PATH + "inspect.png");
									// 保存法制办数据接口
									//final MyProgressDialog pd = Windows.waiting(self);
									AsyncUtil.goAsync(new Callable<Result<PutInspectDataReturn>>() {

										@Override
										public Result<PutInspectDataReturn> call() throws Exception {
											return UserData.PutZhiFaBanData(zhiFaJianChaModel);
										}
									}, new Callback<Result<PutInspectDataReturn>>() {

										@Override
										public void onHandle(Result<PutInspectDataReturn> result) {
											//pd.dismiss();
											if (file.exists() && pdr.getImei_code() != null) {

											}else{
												if (result.ok()) {
													UIUtils.toast(self, "执法信息保存成功！", Toast.LENGTH_SHORT);
													finish();
												} else {
													UIUtils.toast(self, "稽查信息保存成功，但法制办信息保存失败！", Toast.LENGTH_SHORT);
													finish();
												}
											}
										}
									});

									pdr = new PutInspectDataReturn();
									pdr = result.getData();
									if (file.exists() && pdr.getImei_code() != null) {
										updateImage(pdr.getImei_code(), file);
									} else {

									}
									/*
									 * UIUtils.toast(self,"成功",Toast.
									 * LENGTH_LONG); if(isTemp){ Intent
									 * intent = new Intent("tempsave");
									 * intent.putExtra("IsTempDelete",
									 * true); sendBroadcast(intent); }
									 * finish();
									 */
								} else {
									UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
								}
							}
						});
					}
				}, "取消", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				}, 0, null);
			}
		});
	}

	/**
	 * TODO 展示要保存的信息
	 */
	private void showNormalInfo() {
		String hangyeleibie = "行业类别: ";
		String shangyeleibie = hylb.getText().toString();
		String zhifry1 = "执法人员1: ";
		if(TextUtils.isEmpty(people_one)){
			people_one = new UserPreference(self).getString("user_name_update_info", "");
		}
		String szhifry1 = people_one;
		String zhifry2 = "执法人员2: ";
		String szhifry2 = people_two;
		String zhifAdd = "执法地址: ";
		String szhifAdd = isNull.isEmpty(address);
		String zhifSj = "执法时间: ";
		String szhifSj = law_time;
		String CarId = "车牌号码: ";
		String sCarId = carNumber_one.getText().toString() + carNumber_two.getText().toString().toUpperCase();
		String DriverName = "司机姓名: ";
		String sDriverName = isNull.isEmpty(driver_name_et.getText().toString());
		String zjzh = "准驾证号: ";
		String szjzh = isNull.isEmpty(licences_jiandu.getText().toString());
		String jyxk = "立   户   号: ";
		String sjyxk = "无";
		if (TextUtils.isEmpty(licences_lihu.getText().toString())) {
			sjyxk = "无";
		} else {
			sjyxk = licences_lihu.getText().toString();
		}
		String yehuname = "单位名称: ";
		String syehuname = isNull.isEmpty(company_name_et.getText().toString());
		String people_weifa = isNull.isEmpty(check_context.getText().toString());
		String renyuanweifa = "违法内容: ";
		String srenyuanweifa = "无";

		String over1 = "检查案件";
		String over2 = "正常";
		String over3 = "，是否保存?";

		if (!sjyxk.equals("无")) {
			html = "<html><body>" + "<p><strong>" + hangyeleibie + "</strong>" + shangyeleibie + "</p>"
					+ "<p><strong>" + zhifry1 + "</strong>" + szhifry1 + "</p>" + "<p><strong>" + zhifry2
					+ "</strong>" + szhifry2 + "</p>" + "<p><strong>" + zhifAdd + "</strong>" + szhifAdd + "</p>"
					+ "<p><strong>" + zhifSj + "</strong>" + szhifSj + "</p>" + "<p><strong>" + CarId + "</strong>"
					+ sCarId + "</p>" + "<p><strong>" + DriverName + "</strong>" + sDriverName + "</p>"
					+ "<p><strong>" + zjzh + "</strong>" + szjzh + "</p>" + "<p><strong>" + jyxk + "</strong>"
					+ sjyxk + "</p>" + "<p><strong>" + yehuname + "</strong>" + syehuname + "</p>" + "<p><strong>"
					+ renyuanweifa + "</strong>" + srenyuanweifa + "</p>";
		} else {
			html = "<html><body>" + "<p><strong>" + hangyeleibie + "</strong>" + shangyeleibie + "</p>"
					+ "<p><strong>" + zhifry1 + "</strong>" + szhifry1 + "</p>" + "<p><strong>" + zhifry2
					+ "</strong>" + szhifry2 + "</p>" + "<p><strong>" + zhifAdd + "</strong>" + szhifAdd + "</p>"
					+ "<p><strong>" + zhifSj + "</strong>" + szhifSj + "</p>" + "<p><strong>" + CarId + "</strong>"
					+ sCarId + "</p>" + "<p><strong>" + DriverName + "</strong>" + sDriverName + "</p>"
					+ "<p><strong>" + zjzh + "</strong>" + szjzh + "</p>" + "<p><strong>" + renyuanweifa
					+ "</strong>" + srenyuanweifa + "</p>";

		}
	}

	/**
	 * TODO 检测要保存的信息是否正确
	 * @return
	 */
	private boolean checkOutNormal() {
		String driver_name_register_info = driver_name_et.getText().toString();
		cyzgz = licences_jiandu.getText().toString();

		boolean isChoice = false;
		for (int i = 0; i < checkDatas.size(); i++) {
			isChoice = (checkDatas.get(i).isCbNormalState() || isChoice);
		}
		if (check_ll.getVisibility() == View.GONE && !isChoice) {
			UIUtils.toast(self, "请选择检查项是否正常！", Toast.LENGTH_SHORT);
			return false;
		}

		if (TextUtils.isEmpty(people_one) || TextUtils.isEmpty(people_two)) {
			ZfryPreference prez = new ZfryPreference(self);
			UserPreference pref = new UserPreference(self);
			// 如果当前账号为上次登录账号，则获取记录的执法人员2
			if (prez.getString("zfry1", null) != null) {
				if (pref.getString("user_name_update_info", null).contains(prez.getString("zfry1", null))) {
					people_one = pref.getString("user_name_update_info", null);
					people_two = prez.getString("zfry2", null);
					zfzh2 = prez.getString("zfzh2", null);
				}
			}
			time = (System.currentTimeMillis() / 1000) + "";
			law_time = new SimpleDateFormat(datePatterShow).format(new Date(System.currentTimeMillis()));
		}
		if (("").equals(people_two)) {
			UIUtils.toast(self, "请点击右上角确定人员信息！", Toast.LENGTH_SHORT);
			return false;
		}
		if ("法人".equals(choose_peopletype_tv.getText().toString())) {
			if (TextUtils.isEmpty(licences_lihu.getText().toString())
					|| TextUtils.isEmpty(business_phone_et.getText().toString())
					|| TextUtils.isEmpty(company_name_et.getText().toString())) {
				UIUtils.toast(self, "请将经营业户信息填写完整", Toast.LENGTH_SHORT);
				return false;
			}
		}else{
			if (TextUtils.isEmpty(driver_name_register_info)) {
				UIUtils.toast(self, "请输入司机姓名", Toast.LENGTH_LONG);
				return false;
			}
			if (TextUtils.isEmpty(driver_phone_et.getText().toString())) {
				UIUtils.toast(self, "司机电话不能为空", Toast.LENGTH_LONG);
				return false;
			}
			if (TextUtils.isEmpty(cyzgz)) {
				UIUtils.toast(self, "请输入准驾证号！", Toast.LENGTH_SHORT);
				return false;
			}
			if (TextUtils.isEmpty(carNumber_two.getText().toString()) || carNumber_two.length() <6) {
				UIUtils.toast(self, "请输入正确的车牌号", Toast.LENGTH_SHORT);
				return false;
			}

		}

		if ("administrator".equals(people_one)) {
			UIUtils.toast(self, "管理员账号不能进行保存！", Toast.LENGTH_SHORT);
			return false;
		}
		if ("0000000".equals(people_one)) {
			UIUtils.toast(self, "测试账号不能进行保存！", Toast.LENGTH_SHORT);
			return false;
		}
		if (people_two.equals(people_one)) {
			UIUtils.toast(self, "两个执法人员为同一个人，请更正！", Toast.LENGTH_SHORT);
			return false;
		}
		if (("").equals(address) || address.length() < 2) {
			UIUtils.toast(self, "地址为空，请输入！", Toast.LENGTH_SHORT);
			return false;
		}
		if (check_ll.getVisibility() == View.VISIBLE && isHaveWaiqin) {
			UIUtils.toast(self, "请转外勤文书", Toast.LENGTH_LONG);
			return false;
		}
		if (check_ll.getVisibility() == View.VISIBLE && !isHaveWaiqin) {
			UIUtils.toast(self, "请转处罚保存", Toast.LENGTH_SHORT);
			return false;
		}

		return true;
	}

	private void selectHylb() {
		Windows.singleChoice(self, "请选择行业类别", itemOne, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				hylb.setText(itemOne[position]);
				check_industry.setText(itemOne[position]);
				if(itemOne[position].contains("网约")){
					licences_jiandu.removeTextChangedListener(LawInpsectActivity.this);
					driver_name_et.removeTextChangedListener(LawInpsectActivity.this);
					licences_jiandu.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});//当为网约车时设置最大长度为18位
					licences_jiandu.setText("");
					driver_name_et.setText("");
					driver_phone_et.setText("");
					driver_weifa.setVisibility(View.GONE);
				}else{
					licences_jiandu.addTextChangedListener(LawInpsectActivity.this);
					driver_name_et.addTextChangedListener(LawInpsectActivity.this);
					licences_jiandu.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
					licences_jiandu.setText("");
					driver_name_et.setText("");
					driver_phone_et.setText("");
					driver_weifa.setVisibility(View.GONE);
				}
				posiPrefer.clearPreference();
				check_ll.setVisibility(View.GONE);
				check_context.setText("");
				queryLX();
			}
		});
	}

	private void selectPeopleType() {
		Windows.singleChoice(self, "选择当事人类型", itemsFour, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				choose_peopletype_tv.setText(itemsFour[position]);
				if (position == 1) {
					Drawable nav_up = getResources().getDrawable(R.drawable.label);
					nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
					xukezheng_number.setCompoundDrawables(null, null, nav_up, null);
					yehu_name.setCompoundDrawables(null, null, nav_up, null);
					ll_yehu_phone.setVisibility(View.VISIBLE);
					ll1.setVisibility(View.GONE);
					ll2.setVisibility(View.GONE);
					ll3.setVisibility(View.GONE);
					ll4.setVisibility(View.GONE);
				} else {
					xukezheng_number.setCompoundDrawables(null, null, null, null);
					yehu_name.setCompoundDrawables(null, null, null, null);
					ll_yehu_phone.setVisibility(View.GONE);
					ll1.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.VISIBLE);
					ll3.setVisibility(View.VISIBLE);
					ll4.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 3) {
				if (isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) || MsgConfig.lat == 0.0) {
					UIUtils.toast(self, "当前GPS信号差，请拿到室外等开阔的地方！", Toast.LENGTH_SHORT);
				}
			}

		};
	};

	private String carNumber = "";
	private String car_number = "";

	private String cyzgz;
	private String corpName;
	private String driverName;
	private String imageId;

	private String corpNameYeHu;
	private String liHuHao;
	private String faRenDaiBiao;

	private String licence;

	boolean isOver = false;
	boolean isTurn = false;
	boolean isChoose = false;
	String address = "";
	String time = "";
	String people_one = "";
	String people_two = "";
	String zfzh2 = "";

	String district = "";
	// 存储出租违法信息的集合
	ArrayList<TaxiCarInfo.Ssp> listSsp;
	ArrayList<FastInspectWeiFaInfo> TaxiInfo = new ArrayList<>();
	ArrayList<TaxiCarInfo.Info> listInfo = new ArrayList<>();

	ArrayList<FastInspectLvYouQueryWeiFaInfo> arrayListLvYouInfo;
	ArrayList<FastInspectLvYouQueryWeiFaInfo> arrayLvYouCompanyInfo;
	ArrayList<FastInspectLvYouQueryWeiFaInfo> arrayLvYouPeopleInfo;

	ArrayList<TaxiWfInfo.Rows> arrayTaxiPeopleInfo;
	ArrayList<TaxiWfInfo.info> arrayPeopleInfo;

	// 出租公司
	TaxiCompany taxiCompany;
	ArrayList<TaxiCompany.Info> taxiCompanyInfo;
	ArrayList<TaxiCompany.Rows> taxiCompanyRows;
	int total = 0;

	TaxiCompanyQ taxiCompanyQ;

	ArrayList<FastInspectWeiFaInfo> CarInfo = new ArrayList<>();
	ArrayList<FastInspectWeiFaInfo> CompanyInfo = new ArrayList<>();
	ArrayList<FastInspectWeiFaInfo> PeopleInfo = new ArrayList<>();
	String startTime;
	String endTime;
	FastInspectLvYouQueryWeiFaInfo infoLvYou;
	FastInspectLvYouQueryWeiFaInfo LvYouPeopleInfo;
	FastInspectLvYouQueryWeiFaInfo lvYouCompanyInfo;
	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";
	private final String datePatter2 = "yyMMddHHmmssSS";

	/**
	 * 快速稽查-车辆信息
	 */
	long t;
	private void fastCarWeiFa() {
		t =System.currentTimeMillis();
		CarInfo.clear();
		Calendar calendar = Calendar.getInstance();
		listSsp = new ArrayList<>();
		calendar.add(Calendar.YEAR, -1);
		startTime = new SimpleDateFormat(datePatter).format(calendar.getTime());
		endTime = new SimpleDateFormat(datePatter).format(System.currentTimeMillis());
		final CarNum carNum = new CarNum();
		carNum.setVname(car_number.toUpperCase());
		AsyncUtil.goAsync(new Callable<Result<TaxiCarInfo>>() {
			@Override
			public Result<TaxiCarInfo> call() throws Exception {
				return WeiZhanData.queryTaxiCarInfo(carNum);
			}
		}, new Callback<Result<TaxiCarInfo>>() {

			@Override
			public void onHandle(Result<TaxiCarInfo> result) {
				if (result.ok()) {
					queryLocation();
					fast_inspect.setVisibility(View.VISIBLE);
					fast_inspect_tv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
					listSsp.addAll(result.getData().getSsp());
					if (0 == result.getData().getTotal()) {
						keyi.setText("为非可疑车辆");
						keyi.setTextColor(getResources().getColor(R.color.unkeyi_color));
						keyi_image.setImageResource(R.drawable.unkeyi_inspect);
					} else {
						yujingNR = result.getData().getAdk();
						keyi.setText("为可疑车辆" + "("
								+ (result.getData().getAdk() == null ? "无" : result.getData().getAdk()) + ")");
						keyi.setTextColor(getResources().getColor(R.color.keyi_color));
						keyi_image.setImageResource(R.drawable.keyi_inspect);
					}
					if (listSsp == null) {
						weifa_data_car.setText("无违法数据");
						checkData();
						showShortLinearLayout(vnumber_weifa);
						if (result.getData().getInfo().size() == 0) {
							UIUtils.toast(self, "查不到该车辆信息", Toast.LENGTH_SHORT);
							initFastInspectCarData(null, listSsp);
						} else {
							initFastInspectCarData(result.getData().getInfo().get(0), listSsp);
						}
						return;
					} else if (listSsp.size() == 0) {
						weifa_data_car.setText("无违法数据");
						checkData();
						showShortLinearLayout(vnumber_weifa);
						if (result.getData().getInfo().size() == 0) {
							UIUtils.toast(self, "查不到该车辆信息", Toast.LENGTH_SHORT);
							initFastInspectCarData(null, listSsp);
						} else {
							initFastInspectCarData(result.getData().getInfo().get(0), listSsp);
						}
						return;
					} else {
						showShortLinearLayout(vnumber_weifa);
						weifa_data_car.setText("有违法数据");
						checkData();
						for (int i = 0; i < listSsp.size(); i++) {
							FastInspectWeiFaInfo fastInfo = new FastInspectWeiFaInfo();
							fastInfo.setCheckTime(listSsp.get(i).getJCSJ());
							fastInfo.setWfxw(listSsp.get(i).getLBMC());
							fastInfo.setMcs(listSsp.get(i).getLBMS());
							CarInfo.add(fastInfo);
							if (listSsp.get(i).getLBMC() == null || listSsp.get(i).getJCSJ() == null) {
								CarInfo.remove(fastInfo);
							}
						}
						if (result.getData().getInfo().size() != 0 && listSsp.size() != 0) {
							initFastInspectCarData(result.getData().getInfo().get(0), listSsp);
						} else if (result.getData().getInfo().size() == 0 && listSsp.size() != 0) {
							initFastInspectCarData(null, listSsp);
						} else if (result.getData().getInfo().size() != 0 && listSsp.size() == 0) {
							initFastInspectCarData(result.getData().getInfo().get(0), listSsp);
						} else {
							initFastInspectCarData(null, listSsp);
						}
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});

	}

	VehiclePositionModex info;

	// 根据车牌获取位置信息
	protected void queryLocation() {
		final String vname = carNumber_two.getText().toString();

		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return WeiZhanData.queryCarPositionInfo("京" + vname.toUpperCase());
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				if (result.ok()) {

					String res = result.getData();
					if (!TextUtils.isEmpty(res)) {
						// quanPing.setClickable(true);
						info = new Gson().fromJson(res, VehiclePositionModex.class);
						LatLng ll = new LatLng(info.getLat(), info.getLon());
						reverseGeoCode(current_location, ll);
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	String driver_name;
	String company_name_fast_info;

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

	private void fastPeopleWeiFa() {
		PeopleInfo.clear();
		final TaxiWfInfoQ info = new TaxiWfInfoQ();
		info.setZjzh(licences_jiandu.getText().toString());
		info.setName(driver_name_et.getText().toString());
		info.setSfzh(sfzh);
		arrayTaxiPeopleInfo = new ArrayList<>();
		arrayPeopleInfo = new ArrayList<>();
		AsyncUtil.goAsync(new Callable<Result<TaxiWfInfo>>() {
			@Override
			public Result<TaxiWfInfo> call() throws Exception {
				return WeiZhanData.queryTaxiPeopleWeiFaInfo(info);
			}
		}, new Callback<Result<TaxiWfInfo>>() {

			@Override
			public void onHandle(Result<TaxiWfInfo> result) {

				if (result.ok()) {
					if (result.getData() == null || result.getData().getInfo() == null) {
						UIUtils.toast(self, "没有该人员对应的信息！", Toast.LENGTH_SHORT);
						return;
					}
					if ("[]".equals(result.getData().toString()) || result.getData().getInfo().size() == 0) {
						UIUtils.toast(self, "没有该人员对应的信息！", Toast.LENGTH_SHORT);
						return;
					} else {
						fast_inspect.setVisibility(View.VISIBLE);
						fast_inspect_tv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
						arrayPeopleInfo.addAll(result.getData().getInfo());
						if (result.getData().getRows() == null) {
							weifa_data_people.setText("无违法数据");
							checkData();
							showShortLinearLayout(driver_weifa);
							initFastPeopleData(arrayPeopleInfo.get(0));
							return;
						} else if (result.getData().getRows().size() == 0) {
							weifa_data_people.setText("无违法数据");
							checkData();
							showShortLinearLayout(driver_weifa);
							initFastPeopleData(arrayPeopleInfo.get(0));
							return;
						} else {
							arrayTaxiPeopleInfo.addAll(result.getData().getRows());
							// Log.e("TaxiWfInfo.Rows",
							// "TaxiWfInfo.Rows"+result.getData().getRows().toString());
							for (int i = 0; i < arrayTaxiPeopleInfo.size(); i++) {
								FastInspectWeiFaInfo fastInfo = new FastInspectWeiFaInfo();
								fastInfo.setCheckTime(arrayTaxiPeopleInfo.get(i).getJCSJ());
								fastInfo.setWfxw(arrayTaxiPeopleInfo.get(i).getLBMC());
								PeopleInfo.add(fastInfo);
								if (arrayTaxiPeopleInfo.get(i).getLBMC() == null
										|| arrayTaxiPeopleInfo.get(i).getJCSJ() == null) {
									PeopleInfo.remove(fastInfo);
								}
							}
							if (arrayPeopleInfo.size() != 0) {
								if (arrayTaxiPeopleInfo.size() == 0) {
									weifa_data_people.setText("无违法数据");
									checkData();
									showShortLinearLayout(driver_weifa);
									initFastPeopleData(arrayPeopleInfo.get(0));
								} else {
									weifa_data_people.setText("有违法数据");
									checkData();
									showShortLinearLayout(driver_weifa);
									initFastPeopleData(arrayPeopleInfo.get(0));
								}
							}
						}
					}

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}


	/**
	 * 快速稽查-公司违法
	 */
	private void fastCompanyWeiFa() {
		isTurn = false;
		isChoose = false;
		isYeHu = false;
		/*
		 * isDriverCompany = false; isDriverLhh = false;
		 */

		CompanyInfo.clear();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		startTime = new SimpleDateFormat(datePatter).format(calendar.getTime());
		endTime = new SimpleDateFormat(datePatter).format(System.currentTimeMillis());
		taxiCompanyQ = new TaxiCompanyQ();
		taxiCompany = new TaxiCompany();
		taxiCompanyInfo = new ArrayList<>();
		taxiCompanyRows = new ArrayList<>();

		if (corpNameYeHu != null) {
			taxiCompanyQ.setName(corpNameYeHu);
		} else {
			taxiCompanyQ.setName(company_name_et.getText().toString());
		}
		if (!TextUtils.isEmpty(licences_lihu.getText().toString())) {
			taxiCompanyQ.setLhh(licences_lihu.getText().toString());
		} else {
			taxiCompanyQ.setLhh("");
		}
		taxiCompanyQ.setStartIndex(0);
		taxiCompanyQ.setEndIndex(10);
		/*
		 * arrayLvYouCompanyInfo = new ArrayList<>(); lvYouCompanyInfo = new
		 * FastInspectLvYouQueryWeiFaInfo(); if(corpNameYeHu != null){
		 * lvYouCompanyInfo.setCorpName(corpNameYeHu); }else{
		 * lvYouCompanyInfo.setCorpName(company_name_et.getText().toString()); }
		 * lvYouCompanyInfo.setHylb(hylb.getText().toString());
		 * lvYouCompanyInfo.setStartIndex(0); lvYouCompanyInfo.setEndIndex(10);
		 * lvYouCompanyInfo.setFrdb(faRenDaiBiao);
		 * lvYouCompanyInfo.setCheckTime(startTime);
		 * lvYouCompanyInfo.setOverTime(endTime);
		 */
		AsyncUtil.goAsync(new Callable<Result<TaxiCompany>>() {

			@Override
			public Result<TaxiCompany> call() throws Exception {
				return WeiZhanData.queryTaxiCompanyInfo(taxiCompanyQ);
			}
		}, new Callback<Result<TaxiCompany>>() {

			@Override
			public void onHandle(Result<TaxiCompany> result) {

				if (result.ok()) {
					fast_inspect.setVisibility(View.VISIBLE);
					fast_inspect_tv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
					taxiCompanyInfo.addAll(result.getData().getInfo());
					if (result.getData().getTotal() != 0) {
						total = result.getData().getTotal();
					}

					if ("[]".equals(result.getData().toString()) || result.getData().getRows().size() == 0) {
						weifa_data_yehu.setText("无违法数据");
						checkData();
						showShortLinearLayout(company_weifa);
						initFastCompanyData(total, null);
						return;
					} else {
						taxiCompanyRows.addAll(result.getData().getRows());
						// Log.e("TaxiWfInfo.Rows",
						// "TaxiWfInfo.Rows"+result.getData().getRows().toString());
						for (int i = 0; i < taxiCompanyRows.size(); i++) {
							FastInspectWeiFaInfo fastInfo = new FastInspectWeiFaInfo();
							fastInfo.setCheckTime(taxiCompanyRows.get(i).getJCSJ());
							fastInfo.setWfxw(taxiCompanyRows.get(i).getLBMC());
							CompanyInfo.add(fastInfo);
							if (taxiCompanyRows.get(i).getLBMC() == null || taxiCompanyRows.get(i).getJCSJ() == null) {
								CompanyInfo.remove(fastInfo);
							}
						}
						if (taxiCompanyInfo.size() != 0) {
							if (taxiCompanyRows.size() == 0) {
								weifa_data_yehu.setText("无违法数据");
								checkData();
								showShortLinearLayout(company_weifa);
								initFastCompanyData(total, taxiCompanyInfo.get(0));
							} else {
								// Log.e("peopelweifa",
								// "peopelweifa:"+arrayTaxiPeopleInfo.toString());
								weifa_data_yehu.setText("有违法数据");
								checkData();
								showShortLinearLayout(company_weifa);
								initFastCompanyData(total, taxiCompanyInfo.get(0));
							}
						}
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	String ADDRESS = "";
	String FRDB = "";
	String JYXKZ = "";

	private void initFastCompanyData(int total, TaxiCompany.Info info) {
		String corpName = "无";
		String lhh = "无";
		String jyxkz = "无";
		String frdb = "无";

		if (info != null) {
			if (!TextUtils.isEmpty(info.getNAME())) {
				corpName = info.getNAME();
			}
			if (!TextUtils.isEmpty(info.getLHH())) {
				lhh = info.getLHH();

			}
			if (!TextUtils.isEmpty(info.getLICENCE())) {
				jyxkz = info.getLICENCE();
				JYXKZ = info.getLICENCE();
			}
			if (!TextUtils.isEmpty(info.getLEGAL())) {
				frdb = info.getLEGAL();
				FRDB = info.getLEGAL();
			}
			ADDRESS = info.getADDRESS();
		}
		/*
		 * driver_name_fast.setText(driver_name==null?driver_name_et.getText().
		 * toString():driver_name);
		 * licences_driver_fast.setText(cyzgz==null?licences_jiandu.getText().
		 * toString():cyzgz); company_name_driver_fast.setText(corpName);
		 */
		if (CompanyInfo.size() != 0) {
			weigui_context_yehu.setText(CompanyInfo.get(0).getWfxw());
			if (TextUtils.isEmpty(CompanyInfo.get(0).getCheckTime())) {
				weigui_time_yehu.setText(isNull.isEmpty(CompanyInfo.get(0).getCheckTime()));
			} else {
				Date d = null;
				SimpleDateFormat sf = new SimpleDateFormat(datePatter);
				try {
					d = sf.parse(CompanyInfo.get(0).getCheckTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				weigui_time_yehu.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
			}
		}
		company_name_yehu_fast.setText(corpName);
		jingying_licences.setText(jyxkz);
		faren.setText(frdb);
		weifa_count_yehu.setText(String.format("%s次违章", total));
		weifa_count_yehu.setTextColor(Color.parseColor("#00a7d9"));
	}

	/**
	 * 快速稽查-人员违法信息
	 *
	 * @param info
	 */

	String ID_CARD = "";// 身份证号
	String CORPNAME = "";// 所属公司
	String LICENSE_NO = "";// 准驾证号
	String PHONE = "";// 手机号
	String FAMILY_ADDR = "";// 家庭住址

	private void initFastPeopleData(TaxiWfInfo.info info) {

		if (info != null) {
			if (!TextUtils.isEmpty(info.getNAME())) {
				driver_name = info.getNAME();
			}
			if (!TextUtils.isEmpty(info.getCORPNAME())) {
				corpName = info.getCORPNAME();
			}
			if (!TextUtils.isEmpty(info.getLICENSE_NO())) {
				cyzgz = info.getLICENSE_NO();
				if (TextUtils.isEmpty(licences_jiandu.getText().toString())) {
					licences_jiandu.setText(info.getLICENSE_NO());
				}
			}
			ID_CARD = info.getID_CARD();
			CORPNAME = info.getCORPNAME();
			LICENSE_NO = info.getLICENSE_NO();
			PHONE = info.getPHONE();
			FAMILY_ADDR = info.getFAMILY_ADDR();
		}
		if (!TextUtils.isEmpty(imageId)) {
			bitmap.display(photo_driver, Config.PHOTO_PATH + imageId);
			photo_driver.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					View view = LayoutInflater.from(self).inflate(R.layout.showimageview, null);
					LinearLayout ll_main = (LinearLayout) view.findViewById(R.id.show_imageView_main);

					ImageView imageView = (ImageView) view.findViewById(R.id.show_imageView);

					bitmap.display(imageView, Config.PHOTO_PATH + imageId);

					Builder d = new Builder(self);
					final AlertDialog dialog = d.create();

					dialog.setView(view);
					dialog.show();

					WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
					params.width = 480;
					params.height = 650;
					dialog.getWindow().setAttributes(params);

					ll_main.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			});
		}
		driver_name_fast.setText(driver_name == null ? driver_name_et.getText().toString() : driver_name);
		licences_driver_fast.setText(cyzgz == null ? licences_jiandu.getText().toString() : cyzgz);
		company_name_driver_fast.setText(corpName);

		if (PeopleInfo.size() != 0) {
			Collections.sort(PeopleInfo, comparator);
			weigui_context_people.setText(PeopleInfo.get(PeopleInfo.size() - 1).getWfxw());
			if (TextUtils.isEmpty(PeopleInfo.get(PeopleInfo.size() - 1).getCheckTime())) {
				weigui_time_people.setText(isNull.isEmpty(PeopleInfo.get(PeopleInfo.size() - 1).getCheckTime()));
			} else {
				Date d = null;
				SimpleDateFormat sf = new SimpleDateFormat(datePatter);
				try {
					d = sf.parse(PeopleInfo.get(PeopleInfo.size() - 1).getCheckTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				weigui_time_people.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
			}
		}
		weifa_count_people.setText(String.format("%s次违章", PeopleInfo.size()));
		weifa_count_people.setTextColor(Color.parseColor("#00a7d9"));
	}

	ArrayList<VehicleInfo> arrayListVehicle;

	/**
	 * 快速稽查-车辆信息
	 *
	 * @param info
	 */

	String CERTIFICATE_NO = "";// 营运证号
	String COLOR = "";// 颜色
	String BRAND = "";// 厂牌
	String VIN = "";// 车架
	String XH = "";// 车辆型号
	String MODEL = "";// 车辆类型
	String NAME = "";// 所属公司

	private void initFastInspectCarData(TaxiCarInfo.Info info, List<TaxiCarInfo.Ssp> ssp) {
		if (info != null) {
			car_number_fast.setText(isNull.isEmpty(info.getPLATE_NO()));
			company_name_fast.setText(isNull.isEmpty(info.getNAME()));
			current_location.setText("无");
			car_type.setText(isNull.isEmpty(info.getMODEL()));
			car_color.setText(isNull.isEmpty(info.getCOLOR()));

			CERTIFICATE_NO = info.getCERTIFICATE_NO();
			COLOR = info.getCOLOR();
			BRAND = info.getBRAND();
			VIN = info.getVIN();
			XH = info.getXH();
			MODEL = info.getMODEL();
			NAME = info.getNAME();
		} else {
			car_number_fast.setText(car_number);
			company_name_fast.setText("无");
			current_location.setText("无");
			car_type.setText("无");
			car_color.setText("无");

			CERTIFICATE_NO = "";
			COLOR = "";
			BRAND = "";
			VIN = "";
			XH = "";
			MODEL = "";
			NAME = "";
		}

		if (CarInfo.size() != 0) {
			Collections.sort(CarInfo, comparator);
			weigui_context_car.setText(CarInfo.get(CarInfo.size() - 1).getWfxw());
			if (TextUtils.isEmpty(CarInfo.get(CarInfo.size() - 1).getCheckTimes() + "")) {
				weigui_time_car.setText(isNull.isEmpty(CarInfo.get(CarInfo.size() - 1).getCheckTime()));
			} else {
				/*
				 * weigui_time_car.setText(new SimpleDateFormat(datePatterShow)
				 * .format(new Date(CarInfo.get(0).getCheckTimes())));
				 */
				Date d = null;
				SimpleDateFormat sf = new SimpleDateFormat(datePatter);
				try {
					d = sf.parse(CarInfo.get(CarInfo.size() - 1).getCheckTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				weigui_time_car.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));
			}
		}
		weifa_count_car.setText(String.format("%s次违章", CarInfo.size()));
		weifa_count_car.setTextColor(Color.parseColor("#00a7d9"));
	}

	private void showShortLinearLayout(LinearLayout ll) {
		if (ll.getVisibility() == View.GONE) {
			ll.setVisibility(View.VISIBLE);
		} else {
			//ll.setVisibility(View.GONE);
		}
	}

	private void showLargeLinearLayout(TextView tv, LinearLayout ll) {
		if (ll.getVisibility() == View.GONE) {
			ll.setVisibility(View.VISIBLE);
			if (tv != null) {
				tv.setBackgroundColor(getResources().getColor(R.color.xuanze_inspect));
				tv.setTextColor(getResources().getColor(R.color.jiean_btn_color));
			}
		} else {
			ll.setVisibility(View.GONE);
			if (tv != null) {
				tv.setBackgroundColor(getResources().getColor(R.color.xuan_inspect));
				tv.setTextColor(getResources().getColor(R.color.result_info_text_color));
			}
		}
	}

	String ajzt = "";
	int ajztPosition;
	Date STARTDATE;

	private ZhiFaJianChaModel InfoRetun() {
		final ZhiFaJianChaModel zhiFaJianChaModel = new ZhiFaJianChaModel();

		zhiFaJianChaModel.setSTARTDATE(new SimpleDateFormat(datePatterShow).format(STARTDATE));
		zhiFaJianChaModel.setENDDATE(new SimpleDateFormat(datePatterShow).format(new Date()));
		zhiFaJianChaModel.setCHECK_PLACE(address + district);
		// zhiFaJianChaModel.setREFORM_RESULT(1+"");
		String vname = "";
		String car_number = carNumber_two.getText().toString().toUpperCase();
		if (!TextUtils.isEmpty(car_number) && car_number.length() >= 6) {
			vname = carNumber_one.getText().toString() + car_number;
		}
		zhiFaJianChaModel.setVNAME(vname);
		zhiFaJianChaModel.setSFZH(ID_CARD);
		// 是否整改
		zhiFaJianChaModel.setIS_CHARGE_REFORM(2 + "");
		zhiFaJianChaModel.setIS_DELETE("1");
		zhiFaJianChaModel.setPUNISHMENT_TYPE("");
		zhiFaJianChaModel.setCHECK_RESULT(1 + "");
		zhiFaJianChaModel.setIS_ENABLE(1);
		zhiFaJianChaModel.setCITIZEN_NAME(driver_name_et.getText().toString());
		// 身份证号，
		zhiFaJianChaModel.setCITIZEN_PHONE(driver_phone_et.getText().toString());
		zhiFaJianChaModel.setUNIT_NAME(company_name_et.getText().toString());
		zhiFaJianChaModel.setLEGAL_PERSON(FRDB);
		zhiFaJianChaModel.setIS_PUNISHMENT("2");

		if ("公民".equals(choose_peopletype_tv.getText().toString())) {
			zhiFaJianChaModel.setPEOPLE_TYPE("1");
		} else if ("法人".equals(choose_peopletype_tv.getText().toString())) {
			zhiFaJianChaModel.setPEOPLE_TYPE("2");
			zhiFaJianChaModel.setLEGAL_PERSON(faRenDaiBiao);
			zhiFaJianChaModel.setLEGAL_PERSON_PHONE(business_phone_et.getText().toString());
			zhiFaJianChaModel.setUNIT_NAME(company_name_et.getText().toString());
		}

		CompanyInfo(zhiFaJianChaModel);
		zhiFaJianChaModel.setHYLB(hylb.getText().toString());
		String jcx = getJCX();
		// jcx = jcx.split(":")[1].replace("\"", "").replace("}", "");
		zhiFaJianChaModel.setMODULE_NAME(hylb.getText().toString());
		zhiFaJianChaModel.setCHECK_ITEM_NAME(jcx);
		zhiFaJianChaModel.setPERSON_NAME(people_one + "," + people_two);
		zhiFaJianChaModel.setPERSON_CODE(new UserPreference(self).getString("user_name", ""));
		zhiFaJianChaModel.setCREATE_USER_CODE(new UserPreference(self).getString("user_name", ""));
		zhiFaJianChaModel.setZFZH2(zfzh2);

		return zhiFaJianChaModel;

	}

	private void CompanyInfo(final ZhiFaJianChaModel zhiFaJianChaModel) {

		final CompanyInfo info = new CompanyInfo();
		info.setCompanyName(company_name_et.getText().toString());
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

				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						// UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
						return;
					}
					zhiFaJianChaModel.setORGANIZATION_PHONE(result.getData().get(0).getYehuTelphone());
					zhiFaJianChaModel.setADDRESS(result.getData().get(0).getYehuAddress());
				} else {
					zhiFaJianChaModel.setORGANIZATION_PHONE("");
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	HashMap<String, String> HMJCX = new HashMap<>();
	String Zfjcx = "";

	private String getJCX() {
		for(int i = 0,len = ItemNameList.size();i<len;i++){
			if(i==0){
				Zfjcx = ItemNameList.get(i)+"检查";
			}else{
				Zfjcx += "、" + ItemNameList.get(i)+"检查";
			}
		}
		return Zfjcx;
	}

	private void queryLX() {
		final MyProgressDialog pd = Windows.waiting(self);
		final HYTypeQ info = new HYTypeQ();
		info.setHylb(hylb.getText().toString());

		AsyncUtil.goAsync(new Callable<Result<List<HyRyFg>>>() {

			@Override
			public Result<List<HyRyFg>> call() throws Exception {
				return WeiZhanData.queryCheckItemLXByHY(info);
			}
		}, new Callback<Result<List<HyRyFg>>>() {

			@Override
			public void onHandle(Result<List<HyRyFg>> result) {
				pd.dismiss();

				if (result.ok()) {
					if (result.getData().toString().equals("[]") || result.getData() == null) {
						return;
					}

					checkDatas.clear();

					for (int i = 0; i < result.getData().size(); i++) {
						CarStateBean bean = new CarStateBean();

						bean.setCbBreakLawState(false);
						bean.setCbNormalState(false);
						bean.setType(result.getData().get(i).getLBMC());

						checkDatas.add(bean);
						checkAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	// 保存轨迹图片
	private void updateImage(final String img_code, final File file) {

		final MyProgressDialog pd = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<Result<Void>>() {
			@Override
			public Result<Void> call() throws Exception {
				Result<Void> r = new Result<Void>(Result.OK, null, null, null);
				HttpRequest.uploadFile(Config.SERVER_XINXI + "/saveCasePhoto/" + img_code, file);
				return r;
			}
		}, new Callback<Result<Void>>() {
			@Override
			public void onHandle(Result<Void> result) {
				pd.dismiss();
				if (result.ok()) {
					if (isTemp) {
						Intent intent = new Intent("tempsave");
						intent.putExtra("IsTempDelete", true);
						sendBroadcast(intent);
					}
					UIUtils.toast(self, "执法信息保存成功！", Toast.LENGTH_SHORT);
					// 图片保存成功，是否转外勤
					if (isTurnWaiQin) {
						turnWaiQin();
					} else {
						finish();
					}

				} else {
					Windows.confirm(self, 0, "图片上传失败...", "轨迹图上传失败，是否重试", "重新上传", new OnClickListener() {
						@Override
						public void onClick(View v) {
							updateImage(img_code, file);
						}
					}, "取消上传", new OnClickListener() {
						@Override
						public void onClick(View v) {
							UIUtils.toast(self, "执法信息保存成功！", Toast.LENGTH_SHORT);
							// 图片保存失败，判断是否转外勤
							if (isTurnWaiQin) {
								turnWaiQin();
							} else {
								finish();
							}
						}
					}, 0, null);
				}
			}
		});
	}

	public void turnWaiQin() {
		Case c = new Case();
		c.setHYLB(hylb.getText().toString());
		c.setVNAME(car_number);
		c.setBJCR(driver_name_et.getText().toString());
		c.setZFRYNAME2(people_two);
		c.setZHZH2(zfzh2);
        c.setJCQY(district);
		c.setJCDD(address);
		if("4".equals(status)){
            c.setCFFS("警告");
        }else {
            c.setCFFS("处罚");
        }
		if (isTemp) {
			c.setLAT((long)(Inspectors.getTempLat()*1E6d)+"");
			c.setLON((long)(Inspectors.getTempLng()*1E6d)+"");
		} else if (iscommon) {
			c.setLAT((long)(MsgConfig.common_lat*1E6d)+"");
			c.setLON((long)(MsgConfig.common_lat*1E6d)+"");
		} else if (MsgConfig.select_lat != 0) {
			c.setLAT((long)(MsgConfig.select_lat*1E6d)+"");
			c.setLON((long)(MsgConfig.select_lng*1E6d)+"");
		} else {
			c.setLAT((long)(MsgConfig.lat*1E6d)+"");
			c.setLON((long)(MsgConfig.lng*1E6d)+"");
		}
		c.setCREATEUTC(time);
		if(driverInfo != null){
			if(TextUtils.isEmpty(driverInfo.getTelphone())){
				driverInfo.setTelphone(driver_phone_et.getText().toString());
			}
		}else{
			driverInfo = new DriverInfo();
			driverInfo.setTelphone(driver_phone_et.getText().toString());
			driverInfo.setJianduNumber(licences_jiandu.getText().toString());
			driverInfo.setDriverName(driver_name_et.getText().toString());
		}
		CacheManager.getInstance().putDriverInfo(driverInfo);
		Intent intent = new Intent(self, CreateCaseActivity.class);
		intent.putExtra("case",c);
		intent.putExtra("lawToLive",new LawToCase(wfxwId,TextUtils.isEmpty(check_context.getText()) ? "" : check_context.getText().toString(),cyzgz));
		ActivityUtils.startActivity(intent);
		finish();
	}

	public void turnWaiQins() {
		try {
			rank = 1;
			Intent intent = new Intent();
			intent.setClassName("com.example.zfzd_project", "com.example.zfzd_project.CreateCaseActivity");// CreateCaseActivity
			// SjLoginActivity
			intent.putExtra("RankID", rank + "");// 传递随机数
			intent.putExtra("CheckTime", law_time);
			intent.putExtra("IndustryType", hylb.getText().toString());// 行业类别
			intent.putExtra("Qualification", cyzgz);// 从业资格证
			// 执法人员1,也就是登陆用户
			intent.putExtra("EnforcementPerson1", people_one);
			intent.putExtra("EnforcementPerson2", people_two);// 执法人员2
			intent.putExtra("IdentityNo", ID_CARD);// 身份证号
			intent.putExtra("UnitName", CORPNAME);// 所属公司
			// intent.putExtra("Qualification", LICENSE_NO);//准驾证号
			intent.putExtra("UnitTel", PHONE);// 手机号
			intent.putExtra("Address", FAMILY_ADDR);// 家庭住址
			intent.putExtra("BusinessLicence", CERTIFICATE_NO);// 营运证号
			intent.putExtra("CarColor", COLOR);// 颜色
			intent.putExtra("LabelType", BRAND);// 厂牌
			intent.putExtra("HisNumber", VIN);// 车架
			intent.putExtra("CarModel", XH);// 车辆型号
			intent.putExtra("CarType", MODEL);// 车辆类型
			// intent.putExtra("", NAME);//所属公司
			intent.putExtra("UnitAddress", ADDRESS);// 公司地址
			intent.putExtra("TheIllegalShape", wfxwId);// 违法行为
			intent.putExtra("TheIllegalSituation", "");// 违法情形
			if (!TextUtils.isEmpty(address)) {
				intent.putExtra("CheckPlaceStreet", address);// 违法地点(区)
				intent.putExtra("CheckPlaceOrgan", district);// 违法地点(区)
			}
			intent.putExtra("ObjectPerso", driver_name_et.getText().toString());// 司机
			// 车牌号
			intent.putExtra("LicenseNo", car_number);

			if (status.contains("4")) {
				intent.putExtra("Inspection", "警告");
			} else {
				intent.putExtra("Inspection", "处罚");
			}
			// 创建人
			intent.putExtra("CreatePerson", new UserPreference(self).getString("user_name", null));
			// 创建时间
			intent.putExtra("CreateTime",
					new SimpleDateFormat(datePatterShow).format(new Date(System.currentTimeMillis())));

			// intent.putExtra("IdentityNo", sfzh_driver);
			// intent.putExtra("WaiQinWenShu", info);
			// isTurnWaiQin = true;
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.toast(self, "请先安装该APP", Toast.LENGTH_SHORT);
		}
	}

	String status;
	int rank;
	boolean isTurnTrue = true;
	String numberTwo;
	String law_time;

	public void registerMsgReceiver() {
		IntentFilter filter = new IntentFilter("com.xiazdong");
		registerReceiver(msgReceiver, filter);
	}

	public void unregisterMsgReceiver() {
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// int ranks = intent.getIntExtra("RankID", 0);
			String ranks = intent.getStringExtra("RankID");
			if (Integer.parseInt(ranks) == rank) {
				String isTrueTurn = "";
				String isIntentString = intent.getStringExtra("isTrue");
				if (isIntentString.equals("1")) {
					isTrueTurn = "成功";
				} else if (isIntentString.equals("0")) {
					isTrueTurn = "失败";
				}
				String IsStatus = "";
				if (isTurnTrue) {
					IsStatus = "0";
				} else {
					IsStatus = "1";
				}

			}
		}
	};

	private void Save(String isTrueTurn) {
		final RegisterInfoNew info = new RegisterInfoNew();
		info.setZfdwmc(new UserPreference(self).getString("zfdwmc", null));
		info.setAddress(address);
		info.setDriverName(driver_name_et.getText().toString());
		info.setHylb(hylb.getText().toString());
		String vname = "";
		String car_number = carNumber_two.getText().toString().toUpperCase();
		if (!TextUtils.isEmpty(car_number) && car_number.length() >= 6) {
			vname = carNumber_one.getText().toString() + car_number;
		}
		info.setVname(vname);
		if(!TextUtils.isEmpty(people_one)){
			info.setZfry1(people_one);
		}else{
			info.setZfry1(new UserPreference(self).getString("user_name_update_info", ""));
		}
		info.setZfry2(people_two);
		if (TextUtils.isEmpty(time)) {
			long time_temp = System.currentTimeMillis();
			time_temp = time_temp / 1000;
			time = time_temp + "";
		}
		info.setZfsj(time);
		info.setId(null);
		info.setCorpname(CORPNAME);
		info.setLlh(licence);
		info.setJdkh(cyzgz);
		String people_weifa = check_context.getText().toString();
		// String jcx = getJCX().split(":")[1].replace("\"", "");
		info.setYehu_check_items(getJCX());
		info.setPerson_check_items(people_weifa);
		info.setVehicle_check_items(yujingNR);
		info.setStatus(status);
		info.setYyzh(null);
		info.setJyxkz(JYXKZ);
		info.setCorpname(corpNameYeHu);
		if (isTemp) {
			info.setLat(Inspectors.getTempLat());
			info.setLon(Inspectors.getTempLng());
		} else if (iscommon) {
			info.setLat(MsgConfig.common_lat);
			info.setLon(MsgConfig.common_lng);
		} else if (MsgConfig.select_lat != 0.0 && !isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng)) {
			info.setLat(MsgConfig.select_lat);
			info.setLon(MsgConfig.select_lng);
		} else if (MsgConfig.lat != 0.0 && !isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng)) {
			info.setLat(MsgConfig.lat);
			info.setLon(MsgConfig.lng);
		} else {
			info.setLat(0.0);
			info.setLon(0.0);
		}
		info.setDataSource(0);
		info.setNormal(0);
		info.setIllegal(1);
		info.setTotal(1);

		// 后面添加的数据
		info.setJcbh(new SimpleDateFormat(datePatter2).format(new Date(System.currentTimeMillis())));
		info.setJclb(jclb);
		info.setZwstatus(isTrueTurn);

		info.setZfzh(new UserPreference(self).getString("user_name", null));
		info.setSfzh(ID_CARD);
		// 新添加字段

		info.setBrand(BRAND);// 车辆厂牌，未添加数据
		info.setCertificate_no(CERTIFICATE_NO);// 运营证号
		info.setColor(COLOR);// 车辆颜色
		// 车架号 vin
		info.setVin(VIN);
		// 车辆型号 xh
		info.setXh(XH);
		// 车辆类型 model
		info.setModel(MODEL);
		// 人员所属单位 pcompan
		info.setPcompany("");// 未添加数据
		// 车辆所属公司
		info.setVcompany(NAME);
		// 手机号 phone
		info.setPhone(driver_phone_et.getText().toString());
		// 家庭住址 family_addr
		info.setFamily_addr(FAMILY_ADDR);
		// 公司法人代表 frdb
		info.setFrdb(FRDB);
		// 执法地域（前面有个执法地点，这个地域是传"东城区"这种） CheckPlaceOrgan
		info.setCheckPlaceOrgan(district);//
		// 运营证号
		info.setCertificate_no(CERTIFICATE_NO);
		// 违法行为id wfxwId
		info.setWfxwId(wfxwId);
		// 违法情形id wfqxId
		info.setWfqxId(wfqxId);
		//final ZhiFaJianChaModel zhiFaJianChaModel = InfoRetun();
		final MyProgressDialog pd = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<Result<PutInspectDataReturn>>() {

			@Override
			public Result<PutInspectDataReturn> call() throws Exception {
				return UserData.PutInspectDataNew(info);
			}
		}, new Callback<Result<PutInspectDataReturn>>() {

			@Override
			public void onHandle(Result<PutInspectDataReturn> result) {
				pd.dismiss();
				if (result.ok()) {
					pdr = new PutInspectDataReturn();
					pdr = result.getData();
					final File file = new File(Config.PATH + "inspect.png");
					if (file.exists() && pdr.getImei_code() != null) {
						updateImage(pdr.getImei_code(), file);
					} else {
						UIUtils.toast(self, "执法信息保存成功！", Toast.LENGTH_SHORT);
						// 没有图片判断是否转外勤
						if (isTurnWaiQin) {
							turnWaiQin();
						} else {
							finish();
						}
					}

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});

	}

	double lat, lng;

	// 初始化定位,用于在没有点击右上角选择人员信息时，也能上传地址到数据库
	private void Position() {
		if (isTemp) {
			if (!isCommon.outOfChina(Inspectors.getTempLat(), Inspectors.getTempLng())
					&& Inspectors.getTempLat() != 0.0) {
				address = Inspectors.getAddress();
				reverseGeoCode1(Inspectors.getTempLat(), Inspectors.getTempLng(), false);
			}
		} else if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
			lat = MsgConfig.select_lat;
			lng = MsgConfig.select_lng;
			address = mapPPrefer.getString("selectPosition", "");
			reverseGeoCode1(lat, lng, false);
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			lat = MsgConfig.lat;
			lng = MsgConfig.lng;
			if (!"".equals(mapPPrefer.getString("selectPosition", ""))) {
				address = mapPPrefer.getString("selectPosition", "");
				reverseGeoCode1(lat, lng, false);
			} else {
				reverseGeoCode1(lat, lng, true);
			}
		} else {
		}
	}

	/**
	 * 反Geo搜索
	 */
	public void reverseGeoCode1(final double lat, final double lng, final boolean isadd) {
		AsyncUtil.goAsync(new Callable<Result<AddressComponent>>() {

			@Override
			public Result<AddressComponent> call() throws Exception {
				return HistoryData.queryAddrByll(lat, lng);
			}
		}, new Callback<Result<AddressComponent>>() {

			@Override
			public void onHandle(Result<AddressComponent> result) {
				if (result.ok()) {
					if (isadd) {
						address = result.getData().getFormatted_address();
					}
					district = result.getData().getDistrict();
				}
			}
		});
	}

	protected void getDistrict() {
		if (isTemp) {
			if (!isCommon.outOfChina(Inspectors.getTempLat(), Inspectors.getTempLng())
					&& Inspectors.getTempLat() != 0.0) {
				address = Inspectors.getAddress();
				reverseGeoCode1(Inspectors.getTempLat(), Inspectors.getTempLng(), false);
			} else {

			}
		} else if (!isCommon.outOfChina(MsgConfig.common_lat, MsgConfig.common_lng) && MsgConfig.common_lat != 0.0) {
			lat = MsgConfig.common_lat;
			lng = MsgConfig.common_lng;
			reverseGeoCode1(lat, lng, false);
		} else if (!isCommon.outOfChina(MsgConfig.select_lat, MsgConfig.select_lng) && MsgConfig.select_lat != 0.0) {
			lat = MsgConfig.select_lat;
			lng = MsgConfig.select_lng;
			reverseGeoCode1(lat, lng, false);
		} else if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
			lat = MsgConfig.lat;
			lng = MsgConfig.lng;
			reverseGeoCode1(lat, lng, false);
		} else {
		}
	}

	String wfqxId = "";
	String wfxwId = "";

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {

		if (arg1 != RESULT_OK) {
			return;
		}

		String content = "";
		if (arg0 == 1000) {
			content = arg2.getStringExtra("resultItem");

			if ("".equals(content)) {
				checkDatas.get(currentClickItemIndex).setCbBreakLawState(false);

				checkAdapter.notifyDataSetChanged();

				check_ll.setVisibility(View.GONE);
				wfqxId = "";
				wfxwId = "";
			} else if ("thischeck".equals(content)) {
				checkDatas.get(currentClickItemIndex).setCbBreakLawState(false);
				checkAdapter.notifyDataSetChanged();
				check_ll.setVisibility(View.GONE);
				wfqxId = "";
				wfxwId = "";
				posiPrefer.clearPreference();
			} else if ("nochange".equals(content)) {
				checkDatas.get(currentClickItemIndex).setCbBreakLawState(false);

				checkAdapter.notifyDataSetChanged();
			} else if ("checkNoChange".equals(content)) {
				checkDatas.get(currentClickItemIndex).setCbBreakLawState(true);

				checkAdapter.notifyDataSetChanged();
			} else {
				// checkDatas.get(currentClickItemIndex).setCbBreakLawState(true);

				for (int i = 0; i < checkDatas.size(); i++) {
					checkDatas.get(i).setCbBreakLawState(false);
				}

				checkDatas.get(currentClickItemIndex).setCbBreakLawState(true);
				checkDatas.get(currentClickItemIndex).setCbNormalState(false);

				checkAdapter.notifyDataSetChanged();

				wfqxId = arg2.getStringExtra("wfqxId");
				wfxwId = arg2.getStringExtra("wfxwId");
				jclb = arg2.getStringExtra("LBMS");
				check_ll.setVisibility(View.VISIBLE);
				check_context.setText(content);
			}
		}

		String checkItem = "";
		if (arg0 == 51 || arg0 == 52 || arg0 == 53 || arg0 == 54 || arg0 == 61 || arg0 == 62 || arg0 == 63
				|| arg0 == 64) {
			checkItem = arg2.getStringExtra("resultItem");

			if ("".equals(checkItem) && checkItem == null) {
				check_ll.setVisibility(View.GONE);
				wfqxId = "";
				wfxwId = "";
			} else if ("nochange".equals(checkItem)) {

			} else if ("thischeck".equals(checkItem)) {

			} else {
				wfqxId = arg2.getStringExtra("wfqxId");
				wfxwId = arg2.getStringExtra("wfxwId");
				jclb = arg2.getStringExtra("LBMS");
				check_ll.setVisibility(View.VISIBLE);
				check_context.setText(checkItem);
			}
		}

		if (arg0 == 9) {
			isOver = arg2.getBooleanExtra("isOver", false);
			if(arg2.getIntExtra("type", 0) == 1 && !hylb.getText().toString().contains("网约")){
				String d = arg2.getStringExtra("result");
				doQRContent(d);
			}else if(arg2.getIntExtra("type", 0) == 2 && hylb.getText().toString().contains("网约")){
				String d = arg2.getStringExtra("result");
				doQRContent2(d);
			}else{
				UIUtils.toast(self, "二维码不正确", Toast.LENGTH_SHORT);
			}


		}

		if (arg0 == 33) {
			isOver = arg2.getBooleanExtra("isOver", true);
			String name = arg2.getStringExtra("driverName");
			sfzh = arg2.getStringExtra("driverID");
			isBeiJia = true;
			driver_name_et.setText(name);
			fastPeopleWeiFa();
		}
		if (arg0 == 13) {
			history_path.setVisibility(View.VISIBLE);
			history_path.requestFocus();
			String s_carNumber = arg2.getStringExtra("car_number_history");
			car_number_history.setText(s_carNumber);
			String s_one = s_carNumber.substring(0, 1);
			String s_two = s_carNumber.substring(1);
			if (TextUtils.isEmpty(carNumber_two.getText().toString())) {
				carNumber_one.setText(s_one);
				carNumber_two.setText(s_two);
			}
			tv_start_time.setText(arg2.getStringExtra("tv_start_time"));
			tv_end_time.setText(arg2.getStringExtra("tv_end_time"));
			tv_start.setText(arg2.getStringExtra("tv_start"));
			tv_end.setText(arg2.getStringExtra("tv_end"));
			PathDistance.setText(arg2.getStringExtra("PathDistance"));
			String li_jdkh = arg2.getStringExtra("licences_jiandu");
			String li_corpName = arg2.getStringExtra("company_name_et");
			String li_driver = arg2.getStringExtra("driver_name_et");
			String li_lhh = arg2.getStringExtra("licences_lihu");
			if (!TextUtils.isEmpty(li_corpName) && !TextUtils.isEmpty(li_lhh)) {
				isTurn = true;
			}
			if (!TextUtils.isEmpty(li_jdkh) && !TextUtils.isEmpty(li_driver)) {
				// isOver = true;
			}
		}
	}

	/**
	 * 网约车二维码信息解析
	 * @param content
	 */
	private void doQRContent2(String content) {
		JSONObject jsona = null;
		JSONArray jsonArray = null;
		try {
			jsona = new JSONObject(content);
			jsonArray = jsona.getJSONArray("info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for(int i=0,len=jsonArray.length();i<len;i++){
			if(jsonArray.optString(i).contains("姓名")){
				driverName = split(jsonArray.optString(i));
			}
		}
		driver_name_et.setText(driverName);

	}

	private void doQRContent(String content) {
		JSONObject jsona = null;
		JSONArray jsonArray = null;
		try {
			jsona = new JSONObject(content);
			jsonArray = jsona.getJSONArray("info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for(int i=0,len=jsonArray.length();i<len;i++){
			if(jsonArray.optString(i).contains("服务监督卡号")){
				cyzgz = split(jsonArray.optString(i));
			}else if(jsonArray.optString(i).contains("驾驶员姓名")){
				driverName = split(jsonArray.optString(i));
			}else if(jsonArray.optString(i).contains("公司名称")){
				corpName = split(jsonArray.optString(i));
			}
		}
		licences_jiandu.setText(cyzgz);
		driver_name_et.setText(driverName);
		UIUtils.toast(self, "扫描成功", Toast.LENGTH_SHORT);
		if (!TextUtils.isEmpty(licences_jiandu.getText())) {
			queryDriverInfo(cyzgz, 0);
		}

	}

	private String split(String s) {
		if(s.split(":").length >=2){
			return s.split(":")[1];
		}
		return "";
	}


	// 设置定时器，每隔一段时间重新保存一下，当出现程序崩溃等意外情况将数据临时保存
	Handler mHandlers;
	Runnable mRunnable;

	void initHandler() {
		mHandlers = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					try {
						File file = new File(Config.PATH);
						if (file.exists()) {
							File files = new File(Config.PATH + Config.FILE_NAME);
							files.delete();
						}
						Store2SdUtil.getInstance(LawInpsectActivity.this).addOut(getInspector(), Config.FILE_NAME);

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("exception...");
					}
				}
			};
		};
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mHandlers.sendEmptyMessage(1);
				mHandlers.postDelayed(mRunnable, 5000);
			}
		};
		new Thread(mRunnable).start();
	}

	public void registerPathReceiver() {
		IntentFilter filter = new IntentFilter("com.miu360.path");
		registerReceiver(pathReceiver, filter);

	}

	public void unregisterPathReceiver() {
		try {
			unregisterReceiver(pathReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BroadcastReceiver pathReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String ranks = intent.getStringExtra("path");
			if ("successful".contains(ranks)) {
				history_path.setVisibility(View.VISIBLE);
				history_path.requestFocus();
				String s_carNumber = intent.getStringExtra("car_number_history");
				car_number_history.setText(s_carNumber);
				String s_one = s_carNumber.substring(0, 1);
				String s_two = s_carNumber.substring(1);
				if (TextUtils.isEmpty(carNumber_two.getText().toString())) {
					carNumber_one.setText(s_one);
					carNumber_two.setText(s_two);
				}
				tv_start_time.setText(intent.getStringExtra("tv_start_time"));
				tv_end_time.setText(intent.getStringExtra("tv_end_time"));
				tv_start.setText(intent.getStringExtra("tv_start"));
				tv_end.setText(intent.getStringExtra("tv_end"));
				PathDistance.setText(intent.getStringExtra("PathDistance"));
				String li_jdkh = intent.getStringExtra("licences_jiandu");
				String li_corpName = intent.getStringExtra("company_name_et");
				String li_driver = intent.getStringExtra("driver_name_et");
				String li_lhh = intent.getStringExtra("licences_lihu");
				if (!TextUtils.isEmpty(li_corpName) && !TextUtils.isEmpty(li_lhh)) {
					isTurn = true;
				}
				if (!TextUtils.isEmpty(li_jdkh) && !TextUtils.isEmpty(li_driver)) {
					// isOver = true;
				}
				if (bitmap != null) {
					bitmap.clearCache();
				}
			}
		}
	};

	@Override
	public void onBackPressed() {
		Windows.confirm(self, "您确定要退出执法稽查？", new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						new InfoPerference(self).setIsNormal(true);
						isRegular = true;
						if (isTemp) {
							Intent intent = new Intent("tempsave");
							intent.putExtra("IsTempDelete", false);
							sendBroadcast(intent);
						}
						finish();
					}
				}, 200);

			}
		});
	}

	protected Inspector getInspector() {
		Inspector inspector = new Inspector();
		inspector.setHylb(hylb.getText().toString());
		inspector.setvNumber(
				carNumber_one.getText().toString() + carNumber_two.getText().toString().toUpperCase(Locale.CHINA));
		inspector.setJdkh(licences_jiandu.getText().toString());
		inspector.setLhh(licences_lihu.getText().toString());
		inspector.setCompanyName(company_name_et.getText().toString());
		inspector.setDriveName(driver_name_et.getText().toString());
		inspector.setZfry1(people_one);
		inspector.setZfry2(people_two);
		inspector.setZfzh2(zfzh2);
		if (TextUtils.isEmpty(law_time)) {
			SimpleDateFormat f = new SimpleDateFormat(datePatterShow);
			Date date = new Date();
			law_time = f.format(date);
		}
		inspector.setZfsj(law_time);
		inspector.setAddress(address);
		inspector.setTempLat(lat);
		inspector.setTempLng(lng);
		return inspector;
	}

	boolean isRegular = false;

	@Override
	protected void onDestroy() {
		unregisterMsgReceiver();
		unregisterPathReceiver();
		posiPrefer.clearPreference();
		if (task != null)
			task.cancel();

		File file = new File(Config.PATH + Config.FILE_NAME);
		if (file.exists() && isRegular) {
			file.delete();
		}
		p.PopunRegisterMsgReceiver();
		File file1 = new File(Config.PATH + "inspect.png");
		if (file1.exists()) {
			file1.delete();
		}
		if (bitmap != null) {
			bitmap.clearCache();
		}
		if (MsgConfig.common_lat != 0) {
			MsgConfig.select_lat = MsgConfig.common_lat;
			MsgConfig.select_lng = MsgConfig.common_lng;
			MsgConfig.common_lat = 0.0;
			MsgConfig.common_lng = 0.0;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	String hyry;// 识别某个行业具体项

	@Override
	protected void onResume() {
		hyry = posiPrefer.getString("HYRYLX", "");
		/*
		 * if(isTurnWaiQin){ finish(); startActivity(new
		 * Intent(self,FindRecordActivity.class)); }
		 */
		if (isTurnWaiQin) {
			/*Intent intent = new Intent(self, FindRecordActivity.class);
			intent.putExtra("isLaw", true);
			startActivity(intent);
			finish();*/
		}

		super.onResume();
	}

	/**
	 * 检查app是否安装
	 */
	boolean isInstallApp(Context context, String packageName) {
		try {
			PackageManager mPackageManager = getPackageManager();
			mPackageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
			case R.id.carNumber_two:
				whoHasFocus = VNAME;
				break;
			case R.id.licences_jiandu:
				whoHasFocus = JDKH;
				break;
			case R.id.driver_name_et:
				whoHasFocus = DRIVER_NAME;
				break;
			case R.id.licences_lihu:
				whoHasFocus = LHH;
				break;
			case R.id.company_name_et:
				whoHasFocus = COMPANY;
				break;
		}
	}

	//1是准驾证，2是司机姓名，3是立户号，4是单位名称
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		switch (whoHasFocus) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		switch (whoHasFocus) {
			case 0:
				vnumber_weifa.setVisibility(View.GONE);
				break;
			case 4://当单位名称发生改变时
				company_weifa.setVisibility(View.GONE);
				String ss = s.toString();
				if (!ss.equals(initTextChanged(ss))) {
					company_name_et.setText(initTextChanged(ss));
					company_name_et.setSelection(initTextChanged(ss).length());
				}
				break;
		}
	}

	//1是准驾证，2是司机姓名，3是立户号，4是单位名称
	@Override
	public void afterTextChanged(Editable s) {
		System.out.println("lawinpsectActivity:afterTextChanged");
		switch (whoHasFocus) {
			case 0:
				vnameChange();
				break;
			case 1:
				zjzhChangeInfo();
				break;
			case 2:
				driverNameChange();
				break;
			case 3:
				lhhChange();
				break;
			case 4:
				companyChange();
				break;
		}
	}

	/*********************************由于这里的适配器太多而且叫简单，就直接写在这个界面上*****************************************/
	private class viewHolderLicence {
		TextView companyName;
	}

	private class companyCorpNameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return arrayListCompany.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arrayListCompany.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolderLicence holder_driver;
			if (arg1 == null) {
				holder_driver = new viewHolderLicence();
				arg1 = LayoutInflater.from(self).inflate(R.layout.listlicenceadapter, arg2, false);
				arg1.setTag(holder_driver);

				holder_driver.companyName = (TextView) arg1.findViewById(R.id.companyName);

			} else {
				holder_driver = (viewHolderLicence) arg1.getTag();
			}
			holder_driver.companyName.setText(arrayListCompany.get(arg0).getCompanyName());
			return arg1;
		}

	}

	private class driverNameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list_driverName.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_driverName.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolderLicence holder_driver;
			if (arg1 == null) {
				holder_driver = new viewHolderLicence();
				arg1 = LayoutInflater.from(self).inflate(R.layout.listlicenceadapter, arg2, false);
				arg1.setTag(holder_driver);

				holder_driver.companyName = (TextView) arg1.findViewById(R.id.companyName);

			} else {
				holder_driver = (viewHolderLicence) arg1.getTag();
			}
			holder_driver.companyName.setText(list_driverName.get(arg0).getDriverName());
			return arg1;
		}

	}

	private class zjzhAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list_zjzh.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_zjzh.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolderLicence holder_driver;
			if (arg1 == null) {
				holder_driver = new viewHolderLicence();
				arg1 = LayoutInflater.from(self).inflate(R.layout.listlicenceadapter, arg2, false);
				arg1.setTag(holder_driver);

				holder_driver.companyName = (TextView) arg1.findViewById(R.id.companyName);

			} else {
				holder_driver = (viewHolderLicence) arg1.getTag();
			}
			holder_driver.companyName.setText(list_zjzh.get(arg0).getJianduNumber());
			return arg1;
		}

	}

	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list_licence.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_licence.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			viewHolderLicence holder = null;

			if (arg1 == null) {
				holder = new viewHolderLicence();
				arg1 = LayoutInflater.from(self).inflate(R.layout.listlicenceadapter, arg2, false);
				arg1.setTag(holder);

				holder.companyName = (TextView) arg1.findViewById(R.id.companyName);

			} else {
				holder = (viewHolderLicence) arg1.getTag();
			}
			holder.companyName.setText(list_licence.get(arg0).getCompanyName());
			return arg1;
		}

	}

}
