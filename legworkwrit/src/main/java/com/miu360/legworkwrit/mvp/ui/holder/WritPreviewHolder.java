package com.miu360.legworkwrit.mvp.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.mvp.model.entity.WritPreview;

import butterknife.BindView;

/**
 * Created by Murphy on 2018/10/10.
 */
public class WritPreviewHolder extends BaseHolder<WritPreview> {
    @BindView(R2.id.item_tv_case)
    TextView itemTvCase;
    @BindView(R2.id.item_tv_case_times)
    TextView itemTvCaseTimes;

    private Context context;

    public WritPreviewHolder(View itemView,Context context) {
        super(itemView);
        this.context = context;
    }

    @Override
    public void setData(WritPreview data, int position) {
        itemTvCase.setText(data.getName());
        itemTvCaseTimes.setText(context.getString(R.string.writ_print_times,data.getTimes()));
    }
}
