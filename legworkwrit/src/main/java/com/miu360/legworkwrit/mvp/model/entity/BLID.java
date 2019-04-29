package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/10/18.
 */
public class BLID {
    private String ZID;

    private String TYPEID;

    public BLID() {
    }

    public BLID(String ZID) {
        this.ZID = ZID;
    }

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getTYPEID() {
        return TYPEID;
    }

    public void setTYPEID(String TYPEID) {
        this.TYPEID = TYPEID;
    }

    @Override
    public String toString() {
        return "BLID{" +
                "ZID='" + ZID + '\'' +
                ", TYPEID=" + TYPEID +
                '}';
    }
}
