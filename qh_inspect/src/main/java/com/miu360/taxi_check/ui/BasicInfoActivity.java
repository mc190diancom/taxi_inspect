package com.miu360.taxi_check.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.fragment.CarRentalFragment;
import com.miu360.taxi_check.fragment.DangerousGoodsTrasportFragmen;
import com.miu360.taxi_check.fragment.MotorVehicleMaintenanceFragment;
import com.miu360.taxi_check.fragment.MyFragmentPagerAdapter;
import com.miu360.taxi_check.fragment.OrdinaryCargoTrasportFragment;
import com.miu360.taxi_check.fragment.ProvincialPassengerTrasportFragment;
import com.miu360.taxi_check.fragment.SightseeingBoatFragment;
import com.miu360.taxi_check.fragment.TaxiBasicInfoFragment;
import com.miu360.taxi_check.fragment.TouristPassengerTrasportFragment;
import com.miu360.taxi_check.view.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

public class BasicInfoActivity extends BaseActivity {
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2, view3, view4, view5, view6, view7, view8;
	private TextView separation_one, separation_two, separation_three, separation_four, separation_five, separation_six,
			separation_seven, separation_eight;
	List<TextView> Views = new ArrayList<>();
	List<TextView> ViewText = new ArrayList<>();
	private HorizontalScrollView scroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		InitTextView();
		InitViewPager();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		new HeaderHolder().init(self, "基础信息查询");
		separation_one = (TextView) findViewById(R.id.separation_one);
		separation_two = (TextView) findViewById(R.id.separation_two);
		separation_three = (TextView) findViewById(R.id.separation_three);
		separation_four = (TextView) findViewById(R.id.separation_four);
		separation_five = (TextView) findViewById(R.id.separation_five);
		separation_six = (TextView) findViewById(R.id.separation_six);
		separation_seven = (TextView) findViewById(R.id.separation_seven);
		separation_eight = (TextView) findViewById(R.id.separation_eight);
		//separation_nine = (TextView) findViewById(R.id.separation_nine);

		ViewText.add(separation_one);
		ViewText.add(separation_two);
		ViewText.add(separation_three);
		ViewText.add(separation_four);
		ViewText.add(separation_five);
		ViewText.add(separation_six);
		ViewText.add(separation_seven);
		ViewText.add(separation_eight);
		//ViewText.add(separation_nine);

		scroll = (HorizontalScrollView) findViewById(R.id.scroll);
		view1 = (TextView) findViewById(R.id.tv_guid);
		view2 = (TextView) findViewById(R.id.tv_guid1);
		view3 = (TextView) findViewById(R.id.tv_guid2);
		view4 = (TextView) findViewById(R.id.tv_guid3);
		view5 = (TextView) findViewById(R.id.tv_guid4);
		view6 = (TextView) findViewById(R.id.tv_guid5);
		view7 = (TextView) findViewById(R.id.tv_guid6);
		view8 = (TextView) findViewById(R.id.tv_guid7);
		//view9 = (TextView) findViewById(R.id.tv_guid8);

		Views.add(view1);
		Views.add(view2);
		Views.add(view3);
		Views.add(view4);
		Views.add(view5);
		Views.add(view6);
		Views.add(view7);
		Views.add(view8);
		//Views.add(view9);

		view1.setOnClickListener(new txListener(0));
		view2.setOnClickListener(new txListener(1));
		view3.setOnClickListener(new txListener(2));
		view4.setOnClickListener(new txListener(3));
		view5.setOnClickListener(new txListener(4));
		view6.setOnClickListener(new txListener(5));
		view7.setOnClickListener(new txListener(6));
		view8.setOnClickListener(new txListener(7));
		//view9.setOnClickListener(new txListener(8));

	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		Fragment btFragment = new TaxiBasicInfoFragment();//
		Fragment secondFragment = new TouristPassengerTrasportFragment();// 旅游
		Fragment thirdFragment = new ProvincialPassengerTrasportFragment();// 省际
		Fragment fourthFragment = new OrdinaryCargoTrasportFragment();// 货运
		Fragment fifthFragment = new DangerousGoodsTrasportFragmen();// 化危
		Fragment sixthFragment = new CarRentalFragment();// 租赁
		Fragment seventhFragment = new MotorVehicleMaintenanceFragment();//维修
		Fragment eighthFragment = new SightseeingBoatFragment();// 水运
		//Fragment nineFragment = new SeaBoatFragment();//海运

		fragmentList.add(btFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		fragmentList.add(fourthFragment);
		fragmentList.add(fifthFragment);
		fragmentList.add(sixthFragment);
		fragmentList.add(seventhFragment);
		fragmentList.add(eighthFragment);
		//fragmentList.add(nineFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			if(arg0<3){
				scroll.post(new Runnable() {
					public void run() {
						scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
					}
				});
			}
			if(arg0>5){
				scroll.post(new Runnable() {
					public void run() {
						scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				});
			}
			for (int i = 0; i < Views.size(); i++) {
				if (i == arg0) {
					Views.get(i).setTextColor(getResources().getColor(R.color.hangye_basic_textcolor));
					ViewText.get(i).setBackgroundResource(R.drawable.separation_line_short);
				} else {
					Views.get(i).setTextColor(getResources().getColor(R.color.hangye_help_textcolor));
					ViewText.get(i).setBackgroundResource(R.drawable.separation_line_basic);
				}
			}
		}
	}
}
