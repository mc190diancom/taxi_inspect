package com.miu360.legworkwrit.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Murphy on 2018/9/29.
 */
public class ViewDrawableOnclick implements View.OnTouchListener {
    private callBack mCallBack;
    public ViewDrawableOnclick(callBack mCallBack){
        this.mCallBack = mCallBack;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof TextView)) {//EditView是textView的子类
            return false;
        }
        TextView tv = (TextView) v;
        // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
        Drawable drawable = tv.getCompoundDrawables()[2];
        //如果右边没有图片，不再处理
        if (drawable == null) {
            return false;
        }
        //如果不是按下事件，不再处理
        if(event.getAction() == MotionEvent.ACTION_DOWN && event.getX() > tv.getWidth() - tv.getPaddingRight() - drawable.getIntrinsicWidth() - 15){//最后的15是为了让下拉图标可点击范围更大
            mCallBack.onClick(v);
            return true;
        }else{
            return false;
        }
    }

    public interface callBack{
        void onClick(View v);
    }
}
