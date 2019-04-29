package com.miu360.taxi_check_viewPager;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;

public abstract class ViewPagerBannerAdapter extends PagerAdapter {
	private LayoutInflater inflater;
	private ArrayList<View> views;

	public ViewPagerBannerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		views = new ArrayList<View>();
		for (int i = 0; i < getItemCount(); i++) {
			views.add(getView(inflater, i));
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View v = views.get(position % views.size());
		if (v.getParent() != null)
			container.removeView(v);
		container.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemClick(position % views.size());
			}
		});
		return v;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(views.get(position % views.size()));
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public ArrayList<View> getAllViews() {
		return views;
	}

	public abstract void onItemClick(int position);

	public abstract int getItemCount();

	public abstract View getView(LayoutInflater inflater, int position);
}
