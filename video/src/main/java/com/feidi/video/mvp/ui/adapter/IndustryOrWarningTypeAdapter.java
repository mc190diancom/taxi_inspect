package com.feidi.video.mvp.ui.adapter;

import android.widget.CheckBox;

import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.Industry;
import com.feidi.video.mvp.model.entity.WarningType;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class IndustryOrWarningTypeAdapter extends BaseSingleSelectorAdapter {

    public IndustryOrWarningTypeAdapter(List<ISelector> infos) {
        super(infos);
    }

    @Override
    public void setCheckBoxText(ISelector data, CheckBox checkBox) {
        if (data instanceof Industry) {
            checkBox.setText(((Industry) data).getName());
        } else {
            checkBox.setText(((WarningType) data).getType());
        }
    }
}
