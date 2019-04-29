package com.miu360.legworkwrit.mvp.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;

import java.util.List;

/**
 * 作者：wanglei on 2018/9/14.
 * 邮箱：forwlwork@gmail.com
 */
public class  ChooseInstrumentAdapter extends DefaultAdapter<CaseStatus> {
    private Activity activity;

    public ChooseInstrumentAdapter(List<CaseStatus> infos,Activity activity) {
        super(infos);
        this.activity = activity;
    }

    @Override
    public BaseHolder<CaseStatus> getHolder(final View v, int viewType) {
        return new BaseHolder<CaseStatus>(v) {
            @Override
            public void setData(CaseStatus data, int position) {
                TextView tvChooseInstrument = v.findViewById(R.id.item_tv_choose_instrument);
                TextView tvInstrumentStatus = v.findViewById(R.id.item_tv_instrument_status);
                tvChooseInstrument.setText(data.getBlType());
                if(0 == data.getStatus()){
                    tvInstrumentStatus.setTextColor(activity.getResources().getColor(R.color.light_blue));
                    tvInstrumentStatus.setText("未填写");
                }else if(2 == data.getStatus()){
                    tvInstrumentStatus.setTextColor(activity.getResources().getColor(R.color.redfb29));
                    tvInstrumentStatus.setText("待确认");
                }else{
                    tvInstrumentStatus.setText("");
                }
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_rv_choose_instrument;
    }
}
