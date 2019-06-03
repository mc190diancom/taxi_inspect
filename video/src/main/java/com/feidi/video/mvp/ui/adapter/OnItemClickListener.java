package com.feidi.video.mvp.ui.adapter;

import android.view.View;

/**
 * 作者：wanglei on 2019/6/2.
 * 邮箱：forwlwork@gmail.com
 */
public interface OnItemClickListener<T> {

    void onItemClick(View v, T data, int position);

}
