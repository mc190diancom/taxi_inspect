package com.miu360.taxi_check_viewPager;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class BannerViewPager extends ViewPager {

	public BannerViewPager(Context context) {
		super(context);
	}

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			setCurrentItem(getCurrentItem() + 1);
			return false;
		}
	});

	private Thread thread;
	private OnScrollChanged onScrollChanged;
	private boolean pause;
	private int peroid = 5000;

	public interface OnScrollChanged {
		void onScrollChanged(int x, int width);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopScrol();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			startScrol();
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		if (adapter instanceof ViewPagerBannerAdapter)
			setCurrentItem(1000 - (1000 % ((ViewPagerBannerAdapter) adapter).getItemCount()), false);
	}

	public void setChangePeroid(int peroid) {
		this.peroid = peroid;
	}

	public void startScrol() {
		pause = false;
		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							sleep(peroid);
						} catch (InterruptedException e) {
							return;
						}
						if (!pause)
							handler.sendEmptyMessage(0);
					}
				}
			};
			thread.start();
		}
	}

	public void pauseScrol() {
		pause = true;
	}

	public void stopScrol() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
		this.onScrollChanged = onScrollChanged;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (onScrollChanged != null)
			onScrollChanged.onScrollChanged(l, getWidth());
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			startScrol();
			break;
		}
		return super.onTouchEvent(event);
	}

	public void setAnimationSpeed(int speed) {
		changeAnimationSpeed(speed);
	}

	protected void changeAnimationSpeed(int speed) {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new AccelerateInterpolator());
			field.set(this, scroller);
			scroller.setmDuration(speed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class FixedSpeedScroller extends Scroller {
		private int mDuration = 1500;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

	}
}
