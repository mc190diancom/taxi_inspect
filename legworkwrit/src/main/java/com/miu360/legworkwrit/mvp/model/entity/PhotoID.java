package com.miu360.legworkwrit.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/29.
 * 邮箱：forwlwork@gmail.com
 */
public class PhotoID {
    @SerializedName("ID")
    private String id;

    public PhotoID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
