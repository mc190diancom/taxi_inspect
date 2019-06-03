package com.feidi.video.mvp.ui.adapter.listener;

import android.view.View;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public interface OnItemContentSelectedChangeListener<T> {

    void onSelectedChange(View v, T data, int position, boolean isSelected);

}
