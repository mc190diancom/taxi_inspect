package com.miu30.common.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.blankj.utilcode.util.SizeUtils;
import com.miu360.library.R;

/**
 * 作者：wanglei on 2019/5/15.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 实现在开、关两种状态下都带有文本的开关
 * <p>
 *
 * @attr ref android.R.styleable#TextSwitch_textOn                //开关左侧的文字
 * @attr ref android.R.styleable#TextSwitch_textOff               //开关右侧的文字
 * @attr ref android.R.styleable#TextSwitch_thumb                 //开关滑块图片
 * @attr ref android.R.styleable#TextSwitch_checked               //开关选中状态
 * @attr ref android.R.styleable#TextSwitch_textSize              //开关文字大小
 * @attr ref android.R.styleable#TextSwitch_checked_textColor     //开关选中文字的颜色
 * @attr ref android.R.styleable#TextSwitch_unchecked_textColor   //开关未选中文字的颜色
 * @attr ref android.R.styleable.TextSwitch_text_margin           //文字距边界的距离
 */
public class TextSwitch extends View implements Checkable {
    private static final int DEFAULT_ANIMATOR_DURATION = 250;

    private CharSequence mTextOn;
    private CharSequence mTextOff;
    private Drawable mThumbDrawable;
    private boolean mChecked;

    private TextPaint mTextPaint;

    /**
     * 滑块位置距离左边的百分比
     * 0  表示滑块在最左边
     * 1  表示滑块在最右边
     */
    private float mThumbPositionPercent;
    //左边文本的颜色
    private int mLeftTextColor;
    //右边文本的颜色
    private int mRightTextColor;
    //选中时文本的颜色
    private int mCheckedTextColor;
    //未选中时文本的颜色
    private int mUncheckedTextColor;
    /**
     * 动画集合，包括：
     * 滑块滑动的动画
     * 文本颜色渐变的动画
     */
    private AnimatorSet mAnimatorSet;
    //文字距边界的距离
    private float mTextMargin;

    private Layout mOnLayout;
    private Layout mOffLayout;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public TextSwitch(Context context) {
        this(context, null);
    }

    public TextSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.TextSwitch);
        mTextOn = t.getText(R.styleable.TextSwitch_textOn);
        mTextOff = t.getText(R.styleable.TextSwitch_textOff);
        mThumbDrawable = t.getDrawable(R.styleable.TextSwitch_thumb);
        mChecked = t.getBoolean(R.styleable.TextSwitch_checked, false);
        float textSize = t.getDimension(R.styleable.TextSwitch_textSize
                , SizeUtils.sp2px(14));
        mCheckedTextColor = t.getColor(R.styleable.TextSwitch_checked_textColor
                , Color.parseColor("#FFFFFF"));
        mUncheckedTextColor = t.getColor(R.styleable.TextSwitch_unchecked_textColor
                , Color.parseColor("#666666"));
        mTextMargin = t.getDimension(R.styleable.TextSwitch_text_margin, SizeUtils.dp2px(13));
        t.recycle();

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;
        mTextPaint.setTextSize(textSize);
        mThumbPositionPercent = mChecked ? 0 : 1;
        mLeftTextColor = mChecked ? mCheckedTextColor : mUncheckedTextColor;
        mRightTextColor = mChecked ? mUncheckedTextColor : mCheckedTextColor;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOnLayout == null) {
            mOnLayout = makeLayout(mTextOn);
        }

        if (mOffLayout == null) {
            mOffLayout = makeLayout(mTextOff);
        }

        int widthSize = getWidthSize(widthMeasureSpec);
        int heightSize = getHeightSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    private int getWidthSize(int widthMeasureSpec) {
        int result = 0;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                result = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                Drawable background = getBackground();
                if (background != null) {
                    result = background.getIntrinsicWidth();
                } else {
                    if (mThumbDrawable != null) {
                        result = mThumbDrawable.getIntrinsicWidth();
                    }
                }
                break;
        }
        return result;
    }

    private int getHeightSize(int heightMeasureSpec) {
        int result = 0;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                result = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                Drawable background = getBackground();
                if (background != null) {
                    if (mThumbDrawable != null) {
                        result = Math.max(background.getIntrinsicHeight(), mThumbDrawable.getIntrinsicHeight());
                    } else {
                        result = background.getIntrinsicHeight();
                    }
                } else {
                    if (mThumbDrawable != null) {
                        result = mThumbDrawable.getIntrinsicHeight();
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mThumbDrawable != null) {
            mThumbDrawable.setBounds(0, 0
                    , mThumbDrawable.getIntrinsicWidth(), mThumbDrawable.getIntrinsicHeight());
        }

        //draw background
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制滑块
        if (mThumbDrawable != null) {
            canvas.save();
            int left = (int) ((getMeasuredWidth() - mThumbDrawable.getIntrinsicWidth()) * mThumbPositionPercent);
            canvas.translate(left, 0);
            mThumbDrawable.draw(canvas);
            canvas.restore();
        }

        //绘制左边的文字
        mTextPaint.setColor(mLeftTextColor);
        int left = (int) mTextMargin;
        int top = (getMeasuredHeight() - mOnLayout.getHeight()) / 2;
        canvas.translate(left, top);
        mOnLayout.draw(canvas);

        //绘制右边的文字
        mTextPaint.setColor(mRightTextColor);
        int dx = mOnLayout.getWidth() + (getMeasuredWidth() - 2 * left - mOnLayout.getWidth() - mOffLayout.getWidth());
        int dy = 0;
        canvas.translate(dx, dy);
        mOffLayout.draw(canvas);
    }

    private Layout makeLayout(CharSequence text) {
        int width = (int) Math.ceil(Layout.getDesiredWidth(text, 0,
                text.length(), mTextPaint));
        return new StaticLayout(text, mTextPaint, width,
                Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
    }

    @SuppressLint("NewApi")
    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;

            if (isAttachedToWindow() && isLaidOut()) {
                animateThumbToCheckedState(checked);
            } else {
                // Immediately move the thumb to the new position.
                cancelPositionAnimator();
                setThumbPosition(checked ? 0 : 1);
            }

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @SuppressLint("NewApi")
    private void animateThumbToCheckedState(boolean checked) {
        //准备执行动画之前，开启硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        float targetPosition = checked ? 0 : 1;
        int leftTextTargetColor = checked ? mCheckedTextColor : mUncheckedTextColor;
        int rightTextTargetColor = checked ? mUncheckedTextColor : mCheckedTextColor;

        ValueAnimator thumbPositionAnimator = ValueAnimator.ofFloat(mThumbPositionPercent, targetPosition);
        thumbPositionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setThumbPosition((Float) animation.getAnimatedValue());
            }
        });

        ValueAnimator leftTextColorAnimator = ValueAnimator.ofArgb(mLeftTextColor, leftTextTargetColor);
        leftTextColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setLeftTextColor((Integer) animation.getAnimatedValue());
            }
        });

        ValueAnimator rightTextColorAnimator = ValueAnimator.ofArgb(mRightTextColor, rightTextTargetColor);
        rightTextColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setRightTextColor((Integer) animation.getAnimatedValue());
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(DEFAULT_ANIMATOR_DURATION);
        mAnimatorSet.playTogether(thumbPositionAnimator, leftTextColorAnimator, rightTextColorAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画执行结束以后，关闭硬件加速
                setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
        mAnimatorSet.start();
    }

    private void cancelPositionAnimator() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }

    private void setThumbPosition(float newPosition) {
        mThumbPositionPercent = newPosition;
        invalidate();
    }

    private void setLeftTextColor(int color) {
        mLeftTextColor = color;
        invalidate();
    }

    private void setRightTextColor(int color) {
        mRightTextColor = color;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a TextSwitch has changed.
         */
        void onCheckedChanged(TextSwitch textSwitch, boolean isChecked);
    }
}
