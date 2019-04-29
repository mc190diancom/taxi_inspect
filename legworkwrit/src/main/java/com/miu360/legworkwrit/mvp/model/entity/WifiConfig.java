package com.miu360.legworkwrit.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/30.
 * 邮箱：forwlwork@gmail.com
 */
public class WifiConfig {
    @SerializedName("WIFINAME")
    private String ssid;
    @SerializedName("WIFIPASSWORD")
    private String password;
    @SerializedName("SNCODE")
    private String sn;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "WifiConfig{" +
                "ssid='" + ssid + '\'' +
                ", password='" + password + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}
