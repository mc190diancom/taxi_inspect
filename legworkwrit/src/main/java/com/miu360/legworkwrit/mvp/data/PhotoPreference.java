package com.miu360.legworkwrit.mvp.data;

import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseData;
import com.miu30.common.base.XPreference;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;

import java.util.HashSet;
import java.util.Set;

/**
 * 作者：wanglei on 2018/10/24.
 * 邮箱：forwlwork@gmail.com
 */
public class PhotoPreference extends XPreference {
    private static final String NAME = "photo_pref";

    private static final String KEY = "photo_info";

    public PhotoPreference() {
        super(NAME, MiuBaseApp.self);
    }

    public void addPhoto(InquiryRecordPhoto photo) {
        Set<String> value = getStringSet(KEY);

        if (value == null) {
            value = new HashSet<>(3);
        }

        value.add(BaseData.gson.toJson(photo));
        setStringSet(KEY, value);
    }

    public Set<String> getPhotos() {
        return getStringSet(KEY);
    }

    public void removePhoto(InquiryRecordPhoto photo) {
        Set<String> value = getStringSet(KEY);

        if (value != null) {
            value.remove(BaseData.gson.toJson(photo));
        }
    }
}
