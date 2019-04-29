package com.miu360.taxi_check_viewPager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PointView extends View {

	private int index, count;
	private int height, width;
	private Paint p1, p2;
	private int backColor = 0xFFCCCCCC;
	private int foreColor = 0xFFFF0033;

	public void setIndex(int index) {
		this.index = index;
		invalidate();
	}

	public void setCount(int count) {
		this.count = count;
		invalidate();
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
		p1.setColor(backColor);
		invalidate();
	}

	public void setForeColor(int foreColor) {
		this.foreColor = foreColor;
		p2.setColor(foreColor);
		invalidate();
	}

	public PointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		p1 = new Paint();
		p2 = new Paint();
		p1.setColor(backColor);
		p2.setColor(foreColor);
		p1.setAntiAlias(true);
		p2.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		height = getHeight();
		width = getWidth();
		int max = count * 3 - 1;
		for (int i = 0; i < count; i++) {
			canvas.drawCircle(width * (1 + (3 * i)) / max, height / 2, height / 2, p1);
		}
		canvas.drawCircle(width * (1 + (3 * index)) / max, height / 2, height / 2, p2);
	}
}
