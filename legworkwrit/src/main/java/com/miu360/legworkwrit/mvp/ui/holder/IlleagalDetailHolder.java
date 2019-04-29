package com.miu360.legworkwrit.mvp.ui.holder;

import android.view.View;
import android.widget.CheckedTextView;

import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.R2;

import butterknife.BindView;

public class IlleagalDetailHolder extends BaseSelectHolder<JCItem> {

    @BindView(R2.id.ctv_item)
    CheckedTextView ctvItem;

    public IlleagalDetailHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setCheckedData(boolean isChecked, JCItem content, int position) {
        ctvItem.setText(content.getLBMC());
        ctvItem.setChecked(isChecked);
    }
}
