package com.miu30.common.ui.entity;

/**
 * Created by Murphy on 2018/10/15.
 */
public class AddMsg {

    public AddMsg() {
    }

    public AddMsg(String addr, boolean isCommon, String type, long lat, long lon) {
        this.addr = addr;
        this.isCommon = isCommon;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    private String addr;

    private boolean isCommon;

    private String type;

    private long lat;

    private long lon;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setCommon(boolean common) {
        isCommon = common;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "AddMsg{" +
                "addr='" + addr + '\'' +
                ", isCommon=" + isCommon +
                ", type='" + type + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
