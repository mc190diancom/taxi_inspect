package com.miu360.taxi_check.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.Callback;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.CheckCarNumber;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.DateVer;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.ui.PathActivity;
import com.miu360.taxi_check.ui.PathQueryActivity;
import com.miu360.taxi_check.ui.PathQueryActivity2;
import com.miu360.taxi_check.util.UIUtils;
import com.widget.time.WheelMain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PathQueryFragment2 extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.et_car_number)
	private EditText et_car_number;
	@ViewInject(R.id.et_car_number_one)
	private TextView et_car_number_one;
	@ViewInject(R.id.et_car_number_two)
	private TextView et_car_number_two;
	@ViewInject(R.id.et_date)
	private TextView et_date;
	@ViewInject(R.id.et_start_time)
	private TextView et_start_time;
	@ViewInject(R.id.et_end_time)
	private TextView et_end_time;
	@ViewInject(R.id.bt_query)
	private Button bt_query;
	@ViewInject(R.id.chekBox)
	private CheckBox checkBox;
	@ViewInject(R.id.llDate)
	private LinearLayout llDate;

	String[] itemsOne = {"京","沪","津","渝","黑","吉","辽","蒙","冀","新","甘","青","陕","宁","豫","鲁","晋","皖", "鄂","湘","苏","川","黔","滇","桂","藏","浙","赣","粤","闽","台","琼","港","澳"};

	String[] itemsTwo = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	WheelMain wheelMain;
	View root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_pathquery, null);
		initFindView(root);

		checkChange();
		initTextContent();
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				checkChange();
			}
		});
		return root;
	}

	private void checkChange() {
		if (checkBox.isChecked()) {

			llDate.setVisibility(View.VISIBLE);
			et_date.setClickable(false);
		} else {
			llDate.setVisibility(View.GONE);
			et_date.setClickable(true);
		}
	}

	/**
	 * 初始化文本框中的内容,车牌号、开始时间和结束时间 设置文本框的点击监听
	 */
	private void initTextContent() {

		Intent intent = getActivity().getIntent();

		String vname = intent.getStringExtra("vName");
		if (!TextUtils.isEmpty(vname)) {
			String nameOne = vname.substring(0, 1);
			String nameTwo = vname.substring(1, 2);
			String nameThree = vname.substring(2);
			et_car_number_one.setText(nameOne);
			et_car_number_two.setText(nameTwo);
			et_car_number.setText(nameThree);
		}

		et_date.setOnClickListener(this);
		et_car_number_two.setOnClickListener(this);
		et_car_number_one.setOnClickListener(this);

		et_start_time.setOnClickListener(this);
		et_end_time.setOnClickListener(this);

		String carNumber = intent.getStringExtra("car_number_history");
		if (!TextUtils.isEmpty(carNumber)) {
			String nameOne = carNumber.substring(0, 1);
			String nameTwo = carNumber.substring(1, 2);
			String nameThree = carNumber.substring(2);
			et_car_number_one.setText(nameOne);
			et_car_number_two.setText(nameTwo);
			et_car_number.setText(nameThree);
		}
		String start_time = intent.getStringExtra("tv_start_time");
		String end_time = intent.getStringExtra("tv_end_time");
		if (!TextUtils.isEmpty(start_time)) {
			et_date.setText("选择时间");
			llDate.setVisibility(View.VISIBLE);
			Date d = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				d = sf.parse(start_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			et_start_time.setTag(d.getTime());
			et_start_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(d.getTime())));

		} else {

			long start = System.currentTimeMillis() - (30 * 60 * 1000);
			et_start_time.setTag(start);
			et_start_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(start)));
		}

		if (!TextUtils.isEmpty(end_time)) {
			Date d = null;
			SimpleDateFormat sf = new SimpleDateFormat(datePatterShow);
			try {
				d = sf.parse(end_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			et_end_time.setText(new SimpleDateFormat(datePatterShow).format(d.getTime()));
			et_end_time.setTag(d.getTime());
		} else {

			et_end_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
			et_end_time.setTag(System.currentTimeMillis());
		}

		bt_query.setOnClickListener(this);
	}

	public void initFindView(View root) {
		ViewUtils.inject(this, root);
	}

	// 日期下拉选择 最近10分钟 最近一天...
	private void selectQuickTime() {
		final String[] times = new String[] { "最近10分钟", "最近半小时", "最近1小时", "最近3小时", "选择时间" };

		Windows.singleChoice(act, "选择时间", times, new OnDialogItemClickListener() {

			@Override
			public void dialogItemClickListener(int position) {
				Calendar calendar = Calendar.getInstance();
				switch (position) {

					case 0:
						calendar.add(Calendar.MINUTE, -10);
						llDate.setVisibility(View.GONE);
						break;
					case 1:
						calendar.add(Calendar.MINUTE, -30);
						llDate.setVisibility(View.GONE);
						break;
					case 2:
						calendar.add(Calendar.MINUTE, -60);
						llDate.setVisibility(View.GONE);
						break;
					case 3:
						calendar.add(Calendar.MINUTE, -180);
						llDate.setVisibility(View.GONE);
						break;
					case 4:
						llDate.setVisibility(View.VISIBLE);
						et_date.setText(times[position]);
						return;
					default:

						break;
				}
				et_date.setText(times[position]);
				et_start_time.setText(new SimpleDateFormat(datePatterShow).format(calendar.getTime()));
				et_start_time.setTag(calendar.getTimeInMillis());
				et_end_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
				et_end_time.setTag(new Date().getTime());
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.et_car_number:

				break;
			case R.id.et_date:
				// 下拉选择
				System.out.println("点击了---btn---------");
				selectQuickTime();

				break;
			case R.id.et_start_time:
				// 日期时间选择器
				chooseDate(et_start_time);
				break;
			case R.id.et_end_time:
				// 日期时间选择器
				chooseDate2(et_end_time);
				break;
			case R.id.et_car_number_two:
				Windows.singleChoice(act, "选择车牌", itemsTwo, new OnDialogItemClickListener() {

					@Override
					public void dialogItemClickListener(int position) {
						et_car_number_two.setText(itemsTwo[position]);
					}
				});
				break;
			case R.id.et_car_number_one:
				Windows.singleChoice(act, "选择车牌", itemsOne, new OnDialogItemClickListener() {

					@Override
					public void dialogItemClickListener(int position) {
						et_car_number_one.setText(itemsOne[position]);
					}
				});
				break;
			case R.id.bt_query:
				String car_number_one = et_car_number_one.getText().toString();
				String car_number_two = et_car_number_two.getText().toString();
				String car_number_three = et_car_number.getText().toString();
				String vname = car_number_one + car_number_two + car_number_three;
				if (TextUtils.isEmpty(vname)) {
					UIUtils.toast(act, "车牌不正确", Toast.LENGTH_SHORT);
					return;
				}

				String first = vname.charAt(0) + "";
				if (Pattern.compile("[\u4e00-\u9fa5]").matcher(first).matches()) {// 汉字
					vname = vname.substring(1);
				}

				if (vname.length() != 6 && vname.length() != 7) {
					UIUtils.toast(act, "车牌不正确", Toast.LENGTH_SHORT);
					return;
				}

				long end_time = System.currentTimeMillis();
				long start_time = end_time - 30*60*1000;
				if(et_date.getText().toString().contains("半")){
					start_time = end_time - 30*60*1000;
				}else if(et_date.getText().toString().contains("10")){
					start_time = end_time - 10*60*1000;
				}else if(et_date.getText().toString().contains("1")){
					start_time = end_time - 60*60*1000;
				}else if(et_date.getText().toString().contains("3")){
					start_time = end_time - 3*60*60*1000;
				}else{
					Date d = null;
					SimpleDateFormat sd = new SimpleDateFormat(datePatterShow);
					try {
						d = sd.parse(et_start_time.getText().toString());
						start_time = d.getTime();
						d = sd.parse(et_end_time.getText().toString());
						end_time = d.getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (DateVer.VerEndCurrentDate(act, end_time)) {
					return;
				}
				if (DateVer.VerStartCurrentDate(act, start_time)) {
					return;
				}
				if (DateVer.VerStartDate(act, start_time, end_time)) {
					return;
				}
				if (DateVer.VerOneDate(act, start_time, end_time)) {
					return;
				}

			/*Date d = null;
			SimpleDateFormat sd = new SimpleDateFormat(datePatterShow);
			try {
				d = sd.parse(et_start_time.getText().toString());
				start_time = d.getTime();
				d = sd.parse(et_end_time.getText().toString());
				end_time = d.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

				if (DateVer.IsSame(act, start_time, end_time)) {
					return;
				}
				// 提交,跳转到地图界面
				Intent intent = new Intent(getActivity(), PathQueryActivity2.class);
				intent.putExtra("vname", vname.toUpperCase());
				intent.putExtra("startTime", start_time);
				intent.putExtra("endTime", end_time);
				intent.putExtra("licences_jiandu", getActivity().getIntent().getStringExtra("licences_jiandu"));
				intent.putExtra("driver_name_et", getActivity().getIntent().getStringExtra("driver_name_et"));
				intent.putExtra("licences_lihu", getActivity().getIntent().getStringExtra("licences_lihu"));
				intent.putExtra("company_name_et", getActivity().getIntent().getStringExtra("company_name_et"));
				//startActivityForResult(intent, 12);
				startActivity(intent);
				act.finish();
				break;
			default:
				break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==12){
			act.setResult(13, data);
			act.finish();
			UIUtils.toast(act, "why", Toast.LENGTH_SHORT);
		}
	}

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	private void chooseDate(final View v) {
		Windows.selectDateTime(act, (long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				//如果选择的开始时间超过了结束时间
				if(result>(long)et_end_time.getTag()){
					UIUtils.toast(act, "开始时间不能超过结束时间", Toast.LENGTH_SHORT);
					return;
				}
				if (result < (long)et_end_time.getTag() - 24 * 60 * 60 * 1000) {
					et_end_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(result+24 * 60 * 60 * 1000)));
					et_end_time.setTag(result+24 * 60 * 60 * 1000);
				}
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
				//rb_one.setTextColor(getResources().getColorStateList(R.color.black));
			}
		});
	}

	private void chooseDate2(final View v) {
		Windows.selectDateTime(act, (long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				/* if (result < System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
				    UIUtils.toast(act, "不能输入超过一天的时间", Toast.LENGTH_SHORT);
				    return;
				 }*/
				//如果结束时间超过了当前时间
				if(result>System.currentTimeMillis()){
					UIUtils.toast(act, "结束时间不能超过当前时间", Toast.LENGTH_SHORT);
					return;
				}
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
				//rb_one.setTextColor(getResources().getColorStateList(R.color.black));
			}
		});
	}

}
