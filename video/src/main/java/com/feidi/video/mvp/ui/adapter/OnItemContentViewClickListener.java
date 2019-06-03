package com.feidi.video.mvp.ui.adapter;

import android.view.View;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public interface OnItemContentViewClickListener<T> {

    void onItemContentViewClick(View v, T data, int position);

}
