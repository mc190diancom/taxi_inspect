package com.feidi.video.mvp.ui.adapter;

import android.widget.CheckBox;

import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.ISelector;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class CameraListAdapter extends BaseSingleSelectorAdapter {

    public CameraListAdapter(List<ISelector> infos) {
        super(infos);
    }

    @Override
    public void setCheckBoxText(ISelector data, CheckBox checkBox) {
        System.out.println("ISelector:"+data.toString());
        checkBox.setText(((CameraInfo) data).getNAME());
    }
}
