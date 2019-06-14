package com.miu30.common.ui.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 适用于纵向RecyclerView列表的带margin的分隔线
 */
public class MultiVeriticalItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_HEIGHT = 2;

    /** 分隔线距左边的距离，单位：px */
    @Px
    private int mMarginLeft;
    /** 分隔线距右边的距离，单位：px */
    @Px
    private int mMarginRight;
    /** 分隔线的高度，单位：px */
    @Px
    private int mHeight;
    /** 分隔线的起始颜色 */
    @ColorInt
    private int mStartColor;
    /** 分隔线的中间颜色 */
    @ColorInt
    private int mCenterColor;
    /** 分隔线的结束颜色 */
    @ColorInt
    private int mEndColor;
    /** 颜色变化的方向 */
    private GradientDrawable.Orientation mColorOrientation;
    /** 是否绘制最后一个item的分隔线 */
    private boolean isDrawLastDivider;
    /** 是否绘制第一个item的分隔线 */
    private boolean isDrawFristDivider;

    private Rect mBounds = new Rect();
    private Drawable mDividerDrawable;

    public MultiVeriticalItemDecoration() {
        this(new Builder());
    }

    public MultiVeriticalItemDecoration(Builder builder) {
        this.mMarginLeft = builder.marginLeft;
        this.mMarginRight = builder.marginRight;
        this.mStartColor = builder.startColor;
        this.mCenterColor = builder.centerColor;
        this.mEndColor = builder.endColor;
        this.mColorOrientation = builder.orientation;
        this.mHeight = builder.height;
        this.isDrawFristDivider = builder.isDrawFristDivider;
        this.isDrawLastDivider = builder.isDrawLastDivider;

        int[] colors;
        if (mCenterColor == 0) {
            colors = new int[]{mStartColor, mEndColor};
        } else {
            colors = new int[]{mStartColor, mCenterColor, mEndColor};
        }
        mDividerDrawable = new GradientDrawable(mColorOrientation, colors);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mHeight <= 0) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        outRect.set(0, 0, 0, mHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mHeight <= 0) {
            return;
        }

        drawDivider(c, parent);
    }

    private void drawDivider(Canvas c, RecyclerView parent) {
        c.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mMarginLeft;
            right = parent.getWidth() - parent.getPaddingRight() - mMarginRight;
        } else {
            left = mMarginLeft;
            right = parent.getWidth() - mMarginRight;
        }

        int startIndex = 0;
        int endIndex = parent.getChildCount();
        if (!isDrawFristDivider) {
            //不需要绘制第一个item的分隔线
            startIndex++;
        }
        if (!isDrawLastDivider) {
            //不需要绘制最后一个item的分隔线
            endIndex--;
        }

        for (int i = startIndex; i < endIndex; i++) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mHeight;
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);
        }

        c.restore();
    }

    public static class Builder {
        int marginLeft;
        int marginRight;
        int height;
        int startColor;
        int centerColor;
        int endColor;
        GradientDrawable.Orientation orientation;
        boolean isDrawLastDivider;
        boolean isDrawFristDivider;

        public Builder() {
            this.marginLeft = 0;
            this.marginRight = 0;
            this.height = DEFAULT_HEIGHT;
            this.startColor = Color.parseColor("#DDDDDD");
            this.endColor = Color.parseColor("#DDDDDD");
            this.centerColor = 0;
            this.orientation = GradientDrawable.Orientation.LEFT_RIGHT;
            this.isDrawFristDivider = true;
            this.isDrawLastDivider = true;
        }

        public Builder setMarginLeft(@Px int marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }

        public Builder setMarginRight(@Px int marginRight) {
            this.marginRight = marginRight;
            return this;
        }

        public Builder setDividerHeight(@Px int dividerHeight) {
            this.height = dividerHeight;
            return this;
        }

        public Builder setStartColor(@ColorInt int startColor) {
            this.startColor = startColor;
            return this;
        }

        public Builder setCenterColor(@ColorInt int centerColor) {
            this.centerColor = centerColor;
            return this;
        }

        public Builder setEndColor(@ColorInt int endColor) {
            this.endColor = endColor;
            return this;
        }

        public Builder setColorOrientation(GradientDrawable.Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder isDrawFristDivider(boolean isDrawFristDivider) {
            this.isDrawFristDivider = isDrawFristDivider;
            return this;
        }

        public Builder isDrawLastDivider(boolean isDrawLastDivider) {
            this.isDrawLastDivider = isDrawLastDivider;
            return this;
        }

        public MultiVeriticalItemDecoration build() {
            return new MultiVeriticalItemDecoration(this);
        }

    }

}
