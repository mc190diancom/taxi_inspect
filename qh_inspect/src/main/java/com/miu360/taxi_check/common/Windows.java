package com.miu360.taxi_check.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lubao.lubao.async.Callback;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.CommonDialog.OnCompleteListener;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.wheel.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Windows{

	public static void selectDateTime(Context ctx, long time, final Callback<Long> callBack) {
		View root = LayoutInflater.from(ctx).inflate(R.layout.date_timer, null);
		final DatePicker dateP = root.findViewById(R.id.date_picker);
		final TimePicker timeP = root.findViewById(R.id.time_picker);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		timeP.setIs24HourView(true);
		dateP.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
		timeP.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		timeP.setCurrentMinute(c.get(Calendar.MINUTE));

		new AlertDialog.Builder(ctx).setTitle("选择日期").setView(root)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar c = Calendar.getInstance();
						c.set(Calendar.YEAR, dateP.getYear());
						c.set(Calendar.MONTH, dateP.getMonth());
						c.set(Calendar.DATE, dateP.getDayOfMonth());
						c.set(Calendar.HOUR_OF_DAY, timeP.getCurrentHour());
						c.set(Calendar.MINUTE, timeP.getCurrentMinute());
						callBack.onHandle(c.getTimeInMillis());
					}
				}).show();
	}

	public static CommonDialog alert(Context ctx, String title, String msg, String okText, OnClickListener okListener,
									 final OnClickListener cancelListener, int timeCount, OnCompleteListener onCompleteListener) {
		CommonDialog dialog = new CommonDialog(ctx, title, okText, okListener, null, cancelListener, timeCount,
				onCompleteListener, true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
		dialog.setCustom(msgView);
		msgView.setText(msg);
		dialog.show();
		return dialog;
	}

	public static AlertDialog confirmUpDialog(Context ctx, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		AlertDialog dialog = builder.create();
		// 用dialog添加自定义的view
		dialog.setView(view);
		dialog.setCancelable(false);
		return dialog;

	}

	public static CommonDialog alert(Context ctx, String msg, OnClickListener okListener) {
		return alert(ctx, ctx.getString(R.string.dialog_title), msg, ctx.getString(R.string.dialog_ok), okListener,
				null, 0, null);
	}

	public static CommonDialog alert(Context ctx, String msg) {
		return alert(ctx, ctx.getString(R.string.dialog_title), msg, ctx.getString(R.string.dialog_ok), null, null, 0,
				null);
	}

	public static CommonDialog alert(Context ctx, String msg, int timeCount, OnCompleteListener onCompleteListener) {
		return alert(ctx, ctx.getString(R.string.dialog_title), msg, ctx.getString(R.string.dialog_ok), null, null,
				timeCount, onCompleteListener);
	}

	public static CommonDialog alert(Context ctx, String msg, String okText, OnClickListener okListener, int timeCount,
									 OnCompleteListener onCompleteListener) {
		return alert(ctx, ctx.getString(R.string.dialog_title), msg, okText, okListener, null, timeCount,
				onCompleteListener);
	}

	public static CommonDialog getDialog(Context ctx, String title, String okText, OnClickListener okListener,
										 String cancelText, final OnClickListener cancelListener, boolean dismissOnOkClick) {
		return getDialog(ctx, 0, title, okText, okListener, cancelText, cancelListener, dismissOnOkClick);
	}

	public static CommonDialog getDialog(Context ctx, int icon, String title, String okText, OnClickListener okListener,
										 String cancelText, final OnClickListener cancelListener, boolean dismissOnOkClick) {
		CommonDialog commonDialog = new CommonDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener,
				0, null, dismissOnOkClick);
		return commonDialog;
	}

	public static CommonDialog confirm(Context ctx, int icon, String title, String msg, String okText,
									   OnClickListener okListener, String cancelText, final OnClickListener cancelListener, int timeCount,
									   OnCompleteListener onCompleteListener) {
		CommonDialog dialog = new CommonDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener,
				timeCount, onCompleteListener, true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
		dialog.setCustom(msgView);
		msgView.setText(msg);
		dialog.show();
		return dialog;
	}

	public static CommonDialog confirm1(Context ctx, int icon, String title, String msg, int colors, String okText,
										OnClickListener okListener, String cancelText, final OnClickListener cancelListener, int timeCount,
										OnCompleteListener onCompleteListener) {
		CommonDialog dialog = new CommonDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener,
				timeCount, onCompleteListener, true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);

		SpannableStringBuilder builder = new SpannableStringBuilder(msg);
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(colors);
		builder.setSpan(redSpan, 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		builder.setSpan(new AbsoluteSizeSpan(18, true), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //

		dialog.setCustom(msgView);
		msgView.setText(builder);
		dialog.show();
		return dialog;
	}

	public static CommonDialog confirm2(Context ctx, int icon, String title, String msg, String okText,
										OnClickListener okListener, String cancelText, final OnClickListener cancelListener, int timeCount,
										OnCompleteListener onCompleteListener) {
		CommonDialog dialog = new CommonDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener,
				timeCount, onCompleteListener, true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
		dialog.setCustom(msgView);
		msgView.setText(Html.fromHtml(msg));
		msgView.setGravity(Gravity.LEFT);
		msgView.setTextSize(14);
		msgView.setMovementMethod(ScrollingMovementMethod.getInstance());

		dialog.show();
		return dialog;
	}

	public static CommonDialog confirm3(Context ctx, int icon, String title, String msg, String okText,
										OnClickListener okListener, String cancelText, final OnClickListener cancelListener, int timeCount,
										OnCompleteListener onCompleteListener) {
		CommonDialog dialog = new CommonDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener,
				timeCount, onCompleteListener, true);
		LinearLayout View = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.dialog_ll_content, null);
		dialog.setCustom(View);
		TextView msgView = (TextView)View.findViewById(R.id.dialog_content);
		final CheckedTextView select_1 = (CheckedTextView)View.findViewById(R.id.select_1);
		final CheckedTextView select_2 = (CheckedTextView)View.findViewById(R.id.select_2);
		msgView.setText(Html.fromHtml(msg));
		msgView.setGravity(Gravity.LEFT);
		msgView.setTextSize(14);
		msgView.setMovementMethod(ScrollingMovementMethod.getInstance());
		select_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_1.setChecked(!select_1.isChecked());
				select_2.setChecked(!select_2.isChecked());
				if(select_2.isChecked()){
					Config.chk = 2;
				}else{
					Config.chk = 1;
				}
			}
		});
		select_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_1.setChecked(!select_1.isChecked());
				select_2.setChecked(!select_2.isChecked());
				if(select_2.isChecked()){
					Config.chk = 2;
				}else{
					Config.chk = 1;
				}
			}
		});

		dialog.show();
		return dialog;
	}

	public static CommonDialog confirm(Context ctx, String title, String msg, String okText, OnClickListener okListener,
									   String cancelText, final OnClickListener cancelListener, int timeCount,
									   OnCompleteListener onCompleteListener) {
		return confirm(ctx, 0, title, msg, okText, okListener, cancelText, cancelListener, timeCount,
				onCompleteListener);
	}

	public static CommonDialog confirm(Context ctx, String msg, OnClickListener okListener) {
		CommonDialog dialog = new CommonDialog(ctx, ctx.getString(R.string.dialog_title),
				ctx.getString(R.string.dialog_ok), okListener, ctx.getString(R.string.dialog_cancel), null, 0, null,
				true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
		dialog.setCustom(msgView);
		msgView.setText(msg);
		dialog.show();
		return dialog;
	}

	public static CommonDialog Confirm(Context ctx, String msg, OnClickListener okListener) {
		CommonDialog dialog = new CommonDialog(ctx, ctx.getString(R.string.dialog_title),
				ctx.getString(R.string.dialog_ok), okListener, null, null, 0, null, true);
		TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
		dialog.setCustom(msgView);
		msgView.setText(msg);
		dialog.getDialog().setCancelable(false);
		dialog.show();
		return dialog;
	}

	public static MyProgressDialog waiting(final Context context) {
		final MyProgressDialog commonDialog = new MyProgressDialog(context, R.layout.common_waiting,
				R.style.clean_dialog) {
			private ImageView imageView;

			public void initListener() {
				imageView = (ImageView) findViewById(R.id.waiting);
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.wait);
				animation.setInterpolator(new LinearInterpolator());
				imageView.startAnimation(animation);
			}

			@Override
			public void closeListener() {
				imageView.clearAnimation();
			}
		};

		commonDialog.setCancelable(true);
		commonDialog.show();

		/*
		 * //当加载出现超时情况，则取消Dialog加载框 Thread t = new Thread(new Runnable() {
		 *
		 * @Override public void run() { try {
		 * Thread.sleep(8000);//让他显示8秒后，取消ProgressDialog
		 * if(commonDialog.isShowing()){//6秒后还在加载则弹出网络超时提示框，并屏蔽dialog显示
		 * UIUtils.toast(context, "网络超时", Toast.LENGTH_SHORT);
		 * commonDialog.dismiss(); } } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 *
		 * } }); t.start();
		 */
		return commonDialog;
	}

	public static CommonDialog singleChoice(Context ctx, String title, String[] items, String okText,
											final Callback<Integer> okListener, String cancelText, final OnClickListener cancelListener) {
		final ListView listView = (ListView) LayoutInflater.from(ctx).inflate(R.layout.single_choise_list, null);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(new ArrayAdapter<String>(ctx, R.layout.select_dialog_singlechoice, items));
		CommonDialog dialog = new CommonDialog(ctx, title, okText, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (okListener != null) {
					okListener.onHandle(listView.getCheckedItemPosition());
				}
			}
		}, cancelText, cancelListener, 0, null, true);
		dialog.setCustom(listView);
		dialog.show();
		return dialog;
	}

	public static CommonDialog singleChoice(Context ctx, String title, String[] items,
											final OnDialogItemClickListener onDialogItemClickListener) {

		final CommonDialog dialog = new CommonDialog(ctx, title, null, null, null, null, 0, null, true);
		final ListView listView = (ListView) LayoutInflater.from(ctx).inflate(R.layout.single_choise_list, null);
		listView.setAdapter(new ArrayAdapter<String>(ctx, R.layout.dialog_item, items));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dialog.dismiss();
				if (onDialogItemClickListener != null) {
					onDialogItemClickListener.dialogItemClickListener(position);
				}
			}
		});

		// if(items.length==1){
		// listView.setDividerHeight(0);
		// }

		dialog.setCustom(listView);
		dialog.show();
		return dialog;
	}

	public static void actionSheets(final Context ctx, String title, String[] showValues,
									final Callback<Integer> okListener) {
		View root = LayoutInflater.from(ctx).inflate(R.layout.dialog_sheet_content, null);
		final NumberPicker picker = (NumberPicker) root.findViewById(R.id.picker);

		picker.setValue(0);
		picker.setMinValue(0);
		picker.setMaxValue(showValues.length - 1);
		picker.setDisplayedValues(showValues);

		final CommonDialog dialog = new CommonDialog(ctx, title, ctx.getString(R.string.dialog_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (okListener != null) {
							okListener.onHandle(picker.getValue());
						}
					}

				}, ctx.getString(R.string.dialog_cancel), null, 0, null, true, true);
		dialog.setCustom(root);
		dialog.show();
	}

	public static void selectCity(Context ctx, final Callback<Object> callback) {
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.city_select_dialog, null);
		final WheelView cityWheel = (WheelView) view.findViewById(R.id.wheel);
		cityWheel.setVisibleItems(7);
		cityWheel.setViewAdapter(new CitySelectAdapter(ctx, cities));
		CommonDialog dialog = Windows.getDialog(ctx, "请选择所在城市", "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				City city = cities.get(cityWheel.getCurrentItem());
				callback.onHandle(city);
			}
		}, "取消", null, true);
		dialog.setCustom(view);
		dialog.show();
	}

	/**
	 *
	 * @param type
	 *            0天数，1小时数，2分钟数
	 * @param selectHour
	 *            默认-1为当前小时 仅在type为2时有用，即根据小时获取分钟数
	 * @param selectDay
	 *            默认0为今天，1为明天； 仅在type为1时有用，即根据Day获取小时数
	 * @return
	 * @return String[]
	 */
	public synchronized static String[] getTimeValues(int type, int selectHour, int selectDay) {
		String values[] = null;

		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);

		switch (type) {
			case 0:// 天
				values = new String[] { "现在", "今天", "明天", "后天" };
				break;
			case 1:// 小时
				if (selectDay == 1) {
					if (hour < 23) {
						int start = hour;
						int count = 24 - hour;
						values = new String[count];
						for (int i = start, j = 0; i < 24; i++, j++) {
							values[j] = getHourString(i);
						}
					} else {
						if (min >= 55) {
							values = new String[] { "" };
						} else {
							values = new String[] { "23" };
						}
					}
				} else {
					values = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
							"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
				}
				break;
			case 2:// 分钟
				// 当天的该小时
				values = new String[] { "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" };
				break;
			default:
				break;
		}

		return values;
	}

	public static String getHourString(int hour) {
		return getHourString(String.valueOf(hour));
	}

	public static String getHourString(String time) {
		String strTime = "";
		if (Integer.parseInt(time) < 10) {
			strTime = "0" + time;
		} else {
			strTime = time;
		}

		if (strTime.equals("24")) {
			return "00";
		} else {
			return strTime;
		}
	}

	private static List<City> cities = null;

	static {
		cities = new ArrayList<City>();
		cities.add(new City(131, "北京", "北京市"));
		cities.add(new City(340, "深圳", "深圳市"));
		cities.add(new City(75, "四川", "成都市"));
		cities.add(new City(53, "吉林", "长春市"));
	}

}
