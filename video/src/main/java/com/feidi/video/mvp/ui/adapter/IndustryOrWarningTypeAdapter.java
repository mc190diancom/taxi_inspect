package com.feidi.video.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.feidi.video.R;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class IndustryOrWarningTypeAdapter extends DefaultAdapter<String> {

    public IndustryOrWarningTypeAdapter(List<String> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<String> getHolder(final View v, int viewType) {
        return new BaseHolder<String>(v) {
            @Override
            public void setData(final String data, final int position) {
                ((TextView) v.findViewById(R.id.item_tv_des)).setText(data);
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_common;
    }



}
