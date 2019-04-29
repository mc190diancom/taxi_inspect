package com.miu360.taxi_check.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class TempSave extends BaseFragment {

	private View root;
	//存放从临时保存里跳转到稽查的list位置
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2;
	private TextView separation_one, separation_two;
	List<TextView> Views = new ArrayList<>();
	List<TextView> ViewText = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.temp_save, null);
		initView(root);
		InitViewPager(root);
		return root;
	}

	private void initView(View root) {
		separation_one = root.findViewById(R.id.separation_one);
		separation_two = root.findViewById(R.id.separation_two);

		ViewText.add(separation_one);
		ViewText.add(separation_two);

		view1 = root.findViewById(R.id.tv_guid1);
		view2 = root.findViewById(R.id.tv_guid2);

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
			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager(View root) {
		mPager = root.findViewById(R.id.viewpager);
		fragmentList = new ArrayList<>();
		Fragment btFragment = new TemporarySave();// 稽查临时保存
		Fragment secondFragment = new CaseTemporarySave();// 处罚临时保存

		fragmentList.add(btFragment);
		fragmentList.add(secondFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

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
