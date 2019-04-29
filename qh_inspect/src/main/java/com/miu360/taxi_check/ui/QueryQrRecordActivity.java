package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.fragment.HistoryQrFragment;
import com.miu360.taxi_check.fragment.MyFragmentPagerAdapter;
import com.miu360.taxi_check.view.HeaderHolder;

public class QueryQrRecordActivity extends BaseActivity {

	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2, view3;
	private TextView separation_one, separation_two, separation_three;
	List<TextView> Views = new ArrayList<>();
	List<TextView> ViewText = new ArrayList<>();
	UserPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_record);

		InitTextView();
		InitViewPager();
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		new HeaderHolder().init(self, "扫描记录");
		separation_one = (TextView) findViewById(R.id.separation_one);
		separation_two = (TextView) findViewById(R.id.separation_two);
		separation_three = (TextView) findViewById(R.id.separation_three);

		ViewText.add(separation_one);
		ViewText.add(separation_two);
		ViewText.add(separation_three);

		view1 = (TextView) findViewById(R.id.tv_record1);
		view2 = (TextView) findViewById(R.id.tv_record2);
		view3 = (TextView) findViewById(R.id.tv_record3);

		Views.add(view1);
		Views.add(view2);
		Views.add(view3);

		view1.setOnClickListener(new txListener(0));
		view2.setOnClickListener(new txListener(1));
		view3.setOnClickListener(new txListener(2));

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
		Fragment btFragment = HistoryQrFragment.newInstance(1);
		Fragment secondFragment = HistoryQrFragment.newInstance(2);
		Fragment temporarySave = HistoryQrFragment.newInstance(3);
		mPager.setOffscreenPageLimit(2);
		fragmentList.add(btFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(temporarySave);

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
			for (int i = 0; i < Views.size(); i++) {

				if (i == arg0) {
					Views.get(i).setTextColor(getResources().getColor(R.color.hangye_basic_textcolor));
					ViewText.get(i).setVisibility(View.VISIBLE);
				} else {
					Views.get(i).setTextColor(getResources().getColor(R.color.hangye_help_textcolor));
					ViewText.get(i).setVisibility(View.INVISIBLE);
				}
			}
		}
	}

}
