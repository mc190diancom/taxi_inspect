package com.miu360.legworkwrit.mvp.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.mvp.model.entity.WritPreview;
import com.miu360.legworkwrit.mvp.ui.holder.WritPreviewHolder;

import java.util.List;

import butterknife.BindView;

public class WritPreviewListAdapter extends DefaultAdapter<WritPreview> {


    private Context context;

    public WritPreviewListAdapter(List<WritPreview> infos, Context context) {
        super(infos);
        this.context = context;
    }

    @Override
    public BaseHolder<WritPreview> getHolder(final View v, int viewType) {
        return new WritPreviewHolder(v,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_rv_case_times;
    }
}
