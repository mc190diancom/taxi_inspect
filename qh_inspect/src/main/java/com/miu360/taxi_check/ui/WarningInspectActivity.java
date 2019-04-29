package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;

import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.fragment.WarningCameraFragment;
import com.miu360.taxi_check.fragment.YujingDistributionFragment;
import com.miu360.taxi_check.view.HeaderHolder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class WarningInspectActivity extends BaseActivity  {

	//private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2;
	//private TextView separation_one, separation_two, separation_three;
	List<TextView> Views = new ArrayList<>();
	Fragment wcFragment;
	public HeaderHolder header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect_warning);
		InitTextView();
		InitViewPager();
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		header = new HeaderHolder();
		header.init(self, "稽查预警");
		view1 = (TextView) findViewById(R.id.tv_warning1);
		view2 = (TextView) findViewById(R.id.tv_warning2);

		Views.add(view1);
		Views.add(view2);
		view1.setOnClickListener(new txListener(0));
		view2.setOnClickListener(new txListener(1));

	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			//mPager.setCurrentItem(index);
			switchTab(index);
		}
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		fragmentList = new ArrayList<Fragment>();
		Fragment cdFragment = new YujingDistributionFragment();// 可疑车辆预警
		wcFragment = new WarningCameraFragment();// 移动摄像头预警

		fragmentList.add(cdFragment);
		fragmentList.add(wcFragment);
		switchTab(0);
	}

	private Fragment mCurrentFragment;
	private void switchTab(int position) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = fragmentList.get(position);
		if (mCurrentFragment == null) {
			transaction.add(R.id.warning_content, fragment, fragment.getClass().getName());
		} else {
			if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null) {
				transaction.add(R.id.warning_content, fragment, fragment.getClass().getName());
				transaction.hide(mCurrentFragment);
			} else {
				transaction.hide(mCurrentFragment);
				transaction.show(fragment);
			}
		}
		mCurrentFragment = fragment;
		transaction.commitAllowingStateLoss();

		if (0 == position) {
			Views.get(0).setTextColor(getResources().getColor(R.color.white));
			Views.get(0).setBackgroundResource(R.drawable.set_textviewbg);
			Views.get(1).setTextColor(getResources().getColor(R.color.hangye_basic_textcolor));
			Views.get(1).setBackgroundResource(R.drawable.set_textviewbg2);
		} else {
			Views.get(1).setTextColor(getResources().getColor(R.color.white));
			Views.get(1).setBackgroundResource(R.drawable.set_textviewbg);
			Views.get(0).setTextColor(getResources().getColor(R.color.hangye_basic_textcolor));
			Views.get(0).setBackgroundResource(R.drawable.set_textviewbg2);
		}
	}
	
	/*public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < Views.size(); i++) {

				if (i == arg0) {
					Views.get(i).setTextColor(getResources().getColor(R.color.white));
					Views.get(i).setBackgroundResource(R.drawable.set_textviewbg);
				} else {
					Views.get(i).setTextColor(getResources().getColor(R.color.hangye_basic_textcolor));
					Views.get(i).setBackgroundResource(R.drawable.set_textviewbg2);
				}
			}
		}
	}*/
}
