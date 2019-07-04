package com.miu30.common.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class LawToCase implements Parcelable {

    private String wfxwId;

    private String wfxw;

    private String jdkh;

    private String sfzh;

    public LawToCase(String wfxwId, String wfxw, String jdkh, String sfzh) {
        this.wfxwId = wfxwId;
        this.wfxw = wfxw;
        this.jdkh = jdkh;
        this.sfzh = sfzh;
    }

    public LawToCase(){}

    public String getWfxwId() {
        return wfxwId;
    }

    public void setWfxwId(String wfxwId) {
        this.wfxwId = wfxwId;
    }

    public String getWfxw() {
        return wfxw;
    }

    public void setWfxw(String wfxw) {
        this.wfxw = wfxw;
    }

    public String getJdkh() {
        return jdkh;
    }

    public void setJdkh(String jdkh) {
        this.jdkh = jdkh;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.wfxwId);
        dest.writeString(this.wfxw);
        dest.writeString(this.jdkh);
        dest.writeString(this.sfzh);
    }

    protected LawToCase(Parcel in) {
        this.wfxwId = in.readString();
        this.wfxw = in.readString();
        this.jdkh = in.readString();
        this.sfzh = in.readString();
    }

    public static final Creator<LawToCase> CREATOR = new Creator<LawToCase>() {
        @Override
        public LawToCase createFromParcel(Parcel source) {
            return new LawToCase(source);
        }

        @Override
        public LawToCase[] newArray(int size) {
            return new LawToCase[size];
        }
    };
}
