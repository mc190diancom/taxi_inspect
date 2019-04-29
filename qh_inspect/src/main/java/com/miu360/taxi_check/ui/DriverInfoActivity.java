package com.miu360.taxi_check.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.FooterHolder;
import com.miu360.taxi_check.view.HeaderHolder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DriverInfoActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.number_car)
	private TextView number_car_tv;// 监督卡号
	@ViewInject(R.id.name_driver)
	private TextView name_driver_tv;
	@ViewInject(R.id.ID_card_number)
	private TextView ID_card_number_tv;
	@ViewInject(R.id.telephone)
	private TextView telephone_tv;
	@ViewInject(R.id.address)
	private TextView address_tv;
	@ViewInject(R.id.first_time)
	private TextView first_time_tv;
	@ViewInject(R.id.start_time)
	private TextView start_time_tv;
	@ViewInject(R.id.company_driver)
	private TextView company_driver_tv;
	@ViewInject(R.id.photo)
	private ImageView photo_iv;
	@ViewInject(R.id.left_btn_foot)
	private Button footer_left_btn_foot;

	DriverInfo vehicleInfo;
	BitmapUtils bitmap;
	private final String datePatterShow = "yyyy-MM-dd";
	private final String datePatter = "yyyy-MM-dd HH:mm:ss.S";
	UserPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_info);
		bitmap = new BitmapUtils(self);
		bitmap.configDefaultLoadFailedImage(R.drawable.default_photo);

		initView();
	}

	FooterHolder footer;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "基础信息");
		pref = new UserPreference(self);
		company_driver_tv.setOnClickListener(this);
		Intent intent = getIntent();
		vehicleInfo = (DriverInfo) intent.getSerializableExtra("DriverInfo");
		initData(vehicleInfo);
		footer = new FooterHolder();
		footer.init(self);
		footer_left_btn_foot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(pref.getBoolean("isLaw", false)){
					Intent intent = new Intent(self, LawInpsectActivity.class);
					intent.putExtra("DriverName", vehicleInfo.getDriverName());
					intent.putExtra("JianduNumber", vehicleInfo.getJianduNumber());
					intent.putExtra("imageUrl", vehicleInfo.getImageID());
					intent.putExtra("isOver", true);
					startActivity(intent);
				}else{
					UIUtils.toast(self, "暂无权限使用此功能", Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void initData(final DriverInfo info) {

		company_driver_tv.setText(isNull.isEmpty(info.getCompanyName()));
		company_driver_tv.setTextColor(Color.BLUE);
		company_driver_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		bitmap.display(photo_iv, Config.PHOTO_PATH + info.getImageID());
		// bitmap.display(photo_iv, Config.PHOTO_PATH +
		// "bd4d1c014b924b65a1705a04186a7a80");

		photo_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(self).inflate(R.layout.showimageview, null);
				LinearLayout ll_main = (LinearLayout) view.findViewById(R.id.show_imageView_main);

				ImageView imageView = (ImageView) view.findViewById(R.id.show_imageView);

				bitmap.display(imageView, Config.PHOTO_PATH + info.getImageID());

				Builder d = new AlertDialog.Builder(self);
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

		if (company_driver_tv.getText().toString().equals("无")) {
			company_driver_tv.setClickable(false);
		} else {
			company_driver_tv.setClickable(true);
		}
		company_driver_tv.getPaint().setAntiAlias(true);
		number_car_tv.setText(isNull.isEmpty(info.getJianduNumber()));
		name_driver_tv.setText(isNull.isEmpty(info.getDriverName()));
		ID_card_number_tv.setText(isNull.isEmpty(info.getId()));
		telephone_tv.setText(isNull.isEmpty(info.getTelphone()));
		address_tv.setText(isNull.isEmpty(info.getAddress()));
		if (!TextUtils.isEmpty(info.getFirstSgDate())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(info.getFirstSgDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			first_time_tv.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));

		} else {
			first_time_tv.setText(isNull.isEmpty(info.getFirstSgDate()));
		}
		// first_time_tv.setText(isNull.isEmpty(info.getFirstSgDate()));
		if (!TextUtils.isEmpty(info.getCurrentSgHtStartDate())) {
			Date d = null;
			SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
			try {
				d = sdf.parse(info.getCurrentSgHtStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			start_time_tv.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));

		} else {
			start_time_tv.setText(isNull.isEmpty(info.getCurrentSgHtStartDate()));
		}
		// start_time_tv.setText(isNull.isEmpty(info.getCurrentSgHtStartDate()));
	}

	ArrayList<CompanyInfo> arrayList = new ArrayList<CompanyInfo>();

	@Override
	public void onClick(View v) {
		if (v == company_driver_tv) {
			final MyProgressDialog pd = Windows.waiting(self);
			final CompanyInfo info = new CompanyInfo();
			info.setCompanyName(vehicleInfo.getCompanyName());
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
					if (result.ok()) {
						if (result.getData().toString().equals("[]")) {
							UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
							return;
						}
						arrayList.addAll(result.getData());
						Intent intent = new Intent(getApplicationContext(), CompanyInfoActivity.class);
						intent.putExtra("CompanyInfo", arrayList.get(0));
						startActivity(intent);
					}else{
						UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					}
				}
			});
		}
	}
}
