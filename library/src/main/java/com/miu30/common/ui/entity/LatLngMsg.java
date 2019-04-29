package com.miu30.common.ui.entity;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.miu30.common.MiuBaseApp;

/**
 * Created by Murphy on 2018/10/24.
 */
@Table(name = "location")
public class LatLngMsg {

    public LatLngMsg() {
    }

    public LatLngMsg(double lat, double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    /**
     * 本地存储id
     */
    @Id
    private long id;
    @Column(column = "lat")
    private double lat;
    @Column(column = "lng")
    private double lng;
    @Column(column = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static DbUtils.DaoConfig getDaoConfig() {
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(MiuBaseApp.self);
        config.setDbName("Address");
        config.setDbVersion(1);
        return config;
    }

}
