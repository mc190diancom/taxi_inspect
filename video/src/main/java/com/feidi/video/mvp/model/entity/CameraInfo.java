package com.feidi.video.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class CameraInfo implements ISelector, Parcelable {

    /**
     * DWMC : 机场队
     * CAMERAID : 11000005001310255301
     * NAME : T1长途车
     * LON : 116.588015850
     * LAT : 40.0802993660
     */

    private String DWMC;
    private String CAMERAID;
    private String NAME;
    private String LON;
    private String LAT;

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getCAMERAID() {
        return CAMERAID;
    }

    public void setCAMERAID(String CAMERAID) {
        this.CAMERAID = CAMERAID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DWMC);
        dest.writeString(this.CAMERAID);
        dest.writeString(this.NAME);
        dest.writeString(this.LON);
        dest.writeString(this.LAT);
    }

    public CameraInfo() {
    }

    protected CameraInfo(Parcel in) {
        this.DWMC = in.readString();
        this.CAMERAID = in.readString();
        this.NAME = in.readString();
        this.LON = in.readString();
        this.LAT = in.readString();
    }

    public static final Parcelable.Creator<CameraInfo> CREATOR = new Parcelable.Creator<CameraInfo>() {
        @Override
        public CameraInfo createFromParcel(Parcel source) {
            return new CameraInfo(source);
        }

        @Override
        public CameraInfo[] newArray(int size) {
            return new CameraInfo[size];
        }
    };
}
