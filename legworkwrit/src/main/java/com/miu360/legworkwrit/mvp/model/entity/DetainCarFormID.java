package com.miu360.legworkwrit.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/18.
 * 邮箱：forwlwork@gmail.com
 */
public class DetainCarFormID {
    @SerializedName("WSID")
    private String id;

    public DetainCarFormID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
