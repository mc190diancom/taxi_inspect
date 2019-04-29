package com.miu360.taxi_check.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miu360.inspect.R;

public class HeaderHolder {
	public View root;

	public TextView titleTv;

	public ImageButton leftBtn;
	public ImageButton rightBtn1;
	public ImageButton rightBtn2;
	public View rightBtn2Pao;
	public View leftBtnPao;
	public Button rightTextBtn;

	public void init(final Activity act, String title) {
		//ViewUtils.inject(this, act);
		root = act.findViewById(R.id.header_layout);
		titleTv = act.findViewById(R.id.title);
		leftBtn = act.findViewById(R.id.left_btn);
		rightBtn1 = act.findViewById(R.id.right_btn_1);
		rightBtn2 = act.findViewById(R.id.right_btn_2);
		rightBtn2Pao = act.findViewById(R.id.right_btn_2_pao);
		leftBtnPao = act.findViewById(R.id.left_btn_pao);
		rightTextBtn = act.findViewById(R.id.right_text_btn2);

		titleTv.setText(title);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				act.finish();
			}
		});
		rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
	}

	public void setTitle(CharSequence charSequence) {
		titleTv.setText(charSequence);
		titleTv.setBackgroundDrawable(null);
		titleTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	public void setTitle(CharSequence charSequence, Drawable drawable) {
		titleTv.setText(charSequence);
		titleTv.setBackgroundDrawable(null);
		titleTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
	}

	public void setTitle(Drawable bg) {
		titleTv.setText("");
		titleTv.setBackgroundDrawable(bg);
	}

	public void setUpLeftBtn(Drawable drawable, OnClickListener onClick) {
		if (onClick != null) {
			leftBtn.setVisibility(View.VISIBLE);
			leftBtn.setImageDrawable(drawable);
			leftBtn.setOnClickListener(onClick);
		} else {
			leftBtn.setVisibility(View.GONE);
			leftBtn.setImageDrawable(null);
			leftBtn.setOnClickListener(null);
		}
	}

	public void setUpRight1Btn(Drawable drawable, OnClickListener onClick) {
		if (onClick != null) {
			rightBtn1.setVisibility(View.VISIBLE);
			rightBtn1.setImageDrawable(drawable);
			rightBtn1.setOnClickListener(onClick);
		} else {
			rightBtn1.setVisibility(View.GONE);
			rightBtn1.setImageDrawable(null);
			rightBtn1.setOnClickListener(null);
		}
	}

	public void setUpRightBtn2(Drawable drawable, OnClickListener onClick) {
		if (onClick != null) {
			rightBtn2.setVisibility(View.VISIBLE);
			rightBtn2.setImageDrawable(drawable);
			rightBtn2.setOnClickListener(onClick);
		} else {
			rightBtn2.setVisibility(View.GONE);
			rightBtn2.setImageDrawable(null);
			rightBtn2.setOnClickListener(null);
		}
	}

	public void setUpRightTextBtn(CharSequence text, OnClickListener onClick) {
		if (onClick != null) {
			rightTextBtn.setVisibility(View.VISIBLE);
			rightTextBtn.setText(text);
			rightTextBtn.setOnClickListener(onClick);
		} else {
			rightTextBtn.setVisibility(View.GONE);
			rightTextBtn.setText("");
			rightTextBtn.setOnClickListener(null);
		}
	}

	public void setUpRightTextBtn(CharSequence text, int size, String color, OnClickListener onClick) {
		if (onClick != null) {
			rightTextBtn.setVisibility(View.VISIBLE);
			rightTextBtn.setTextColor(Color.parseColor(color));
			rightTextBtn.setTextSize(size);
			rightTextBtn.setText(text);
			rightTextBtn.setOnClickListener(onClick);
		} else {
			rightTextBtn.setVisibility(View.GONE);
			rightTextBtn.setText("");
			rightTextBtn.setOnClickListener(null);
		}
	}

	public void showRightBtn2Pao(boolean show) {
		rightBtn2Pao.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public void showLeftBtnPao(boolean show) {
		leftBtnPao.setVisibility(show ? View.VISIBLE : View.GONE);
	}

}
