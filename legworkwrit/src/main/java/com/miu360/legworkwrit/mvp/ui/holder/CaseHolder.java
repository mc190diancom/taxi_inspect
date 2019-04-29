package com.miu360.legworkwrit.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.mvp.model.entity.Case;

import butterknife.BindView;

/**
 * Created by Murphy on 2018/10/10.
 */
public class CaseHolder extends BaseHolder<Case> {
    @BindView(R2.id.sort)
    TextView sort;
    @BindView(R2.id.license)
    TextView license;
    @BindView(R2.id.car_type)
    TextView carType;
    @BindView(R2.id.driver)
    TextView driver;
    @BindView(R2.id.time)
    TextView time;

    public CaseHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Case data, int position) {
        sort.setText(String.valueOf(position + 1));
        license.setText(data.getVNAME());
        carType.setText(data.getHYLB());
        driver.setText(data.getBJCR());
        time.setText(data.getBJCR());
    }
}
