package com.miu30.common.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmDetailInfo implements Parcelable {
    private String vname;
    private String speed;
    private double lat;
    private double lon;
    private String ADK_WFXW;
    private String occurTime;
    private String RKSM;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getADK_WFXW() {
        return ADK_WFXW;
    }

    public void setADK_WFXW(String ADK_WFXW) {
        this.ADK_WFXW = ADK_WFXW;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getRKSM() {
        return RKSM;
    }

    public void setRKSM(String RKSM) {
        this.RKSM = RKSM;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vname);
        dest.writeString(this.speed);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.ADK_WFXW);
        dest.writeString(this.occurTime);
        dest.writeString(this.RKSM);
    }

    public AlarmDetailInfo() {
    }

    protected AlarmDetailInfo(Parcel in) {
        this.vname = in.readString();
        this.speed = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.ADK_WFXW = in.readString();
        this.occurTime = in.readString();
        this.RKSM = in.readString();
    }

    public static final Creator<AlarmDetailInfo> CREATOR = new Creator<AlarmDetailInfo>() {
        @Override
        public AlarmDetailInfo createFromParcel(Parcel source) {
            return new AlarmDetailInfo(source);
        }

        @Override
        public AlarmDetailInfo[] newArray(int size) {
            return new AlarmDetailInfo[size];
        }
    };

    @Override
    public String toString() {
        return "AlarmDetailInfo{" +
                "vname='" + vname + '\'' +
                ", speed='" + speed + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", ADK_WFXW='" + ADK_WFXW + '\'' +
                ", occurTime='" + occurTime + '\'' +
                ", RKSM='" + RKSM + '\'' +
                '}';
    }
}
