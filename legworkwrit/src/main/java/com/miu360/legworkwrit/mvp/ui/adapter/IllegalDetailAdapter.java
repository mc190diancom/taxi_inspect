package com.miu360.legworkwrit.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.mvp.ui.holder.IlleagalDetailHolder;

import java.util.List;

public class IllegalDetailAdapter extends BaseSelectAdapter<JCItem> {
    public IllegalDetailAdapter(List<JCItem> infos, boolean selectOnly) {
        super(infos, selectOnly);
    }

    @Override
    public BaseHolder<JCItem> getHolder(View v, int viewType) {
        return new IlleagalDetailHolder(v);
    }
}
