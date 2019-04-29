package com.miu360.legworkwrit.mvp.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class CaseHistoryListAdapter extends DefaultAdapter<Case> {
    private Activity activity;

    public CaseHistoryListAdapter(List<Case> infos,Activity activity) {
        super(infos);
        this.activity = activity;
    }

    @Override
    public BaseHolder<Case> getHolder(final View v, int viewType) {
        return new BaseHolder<Case>(v) {
            @Override
            public void setData(Case data, int position) {
                TextView tvSort = v.findViewById(R.id.sort);
                TextView tvLicense = v.findViewById(R.id.license);
                TextView tvCarType = v.findViewById(R.id.car_type);
                TextView tvDriver = v.findViewById(R.id.driver);
                TextView tvTime = v.findViewById(R.id.time);

                tvSort.setText(String.valueOf(position + 1));
                tvLicense.setText(data.getVNAME());
                tvCarType.setText(data.getHYLB());
                tvDriver.setText(data.getBJCR());

                try {
                    long seconds = Long.valueOf(data.getCREATEUTC());
                    tvTime.setText(TimeTool.yyyyMMdd_HHmm.format(new Date(seconds * 1000)));
                } catch (Exception e) {
                    Timber.w(e);
                    tvTime.setText("");
                }
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.adapter_case_history_item;
    }
}
