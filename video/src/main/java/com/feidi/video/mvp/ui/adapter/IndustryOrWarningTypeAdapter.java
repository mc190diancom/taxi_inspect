package com.feidi.video.mvp.ui.adapter;

import android.widget.CheckBox;

import com.feidi.video.mvp.model.entity.AlarmType;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.model.entity.IndustyType;

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
        if (data instanceof IndustyType) {
            checkBox.setText(((IndustyType) data).getHYLX());
        } else {
            checkBox.setText(((AlarmType) data).getRKSM());
        }
    }
}
