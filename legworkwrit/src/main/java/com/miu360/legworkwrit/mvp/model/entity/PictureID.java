package com.miu360.legworkwrit.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/30.
 * 邮箱：forwlwork@gmail.com
 */
public class PictureID {
    @SerializedName("TPID")
    private String id;
    @SerializedName("ZPSM")
    private String index;

    public PictureID(String id, String index) {
        this.id = id;
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
