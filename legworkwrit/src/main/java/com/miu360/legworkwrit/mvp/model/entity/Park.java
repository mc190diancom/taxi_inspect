package com.miu360.legworkwrit.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 停车场
 */
public class Park {
    @SerializedName("ID")
    private String id;
    @SerializedName("TCCMC")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Park(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Park() {
    }

    @Override
    public String toString() {
        return "Park{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
