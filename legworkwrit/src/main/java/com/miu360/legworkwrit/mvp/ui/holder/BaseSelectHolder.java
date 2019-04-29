package com.miu360.legworkwrit.mvp.ui.holder;

import android.view.View;

import com.jess.arms.base.BaseHolder;

public abstract class BaseSelectHolder<T> extends BaseHolder<T> {

    public BaseSelectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(T data, int position) {

    }


    public abstract void setCheckedData(boolean isChecked, T content,int position);
}
