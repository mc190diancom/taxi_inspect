package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by Murphy on 2018/10/18.
 */
public class LiveTranscript extends ParentQ {

    /**
     * ZH : 测试
     * SSSJ1 : 1539156963
     * SSSJ2 : 1539156963
     * SSDD :
     * DSR :
     * DZ : 测试
     * DCRY : 测试
     * XB : 测试
     * DH : 测试
     * SFZH : 测试
     * ZZ : 测试
     * SSQZCSJL : 测试
     * SFSS : 0000000
     * DSRQZ : 测试
     * DSRQZSJ : 1539156963
     * ZFRY1 :
     * ZFRY2 :
     * ZFZH1 : 1539156963
     * ZFZH2 : 1539156963
     * ZFSJ : 1539156963
     * ZID : 123123123
     */
    private String ID;
    private String ZH;
    private String SSSJ1;
    private String SSSJ2;
    private String SSDD;
    private String DSR;
    private String DZ;
    private String DCRY;
    private String XB;
    private String DH;
    private String SFZH;
    private String ZZ;
    private String SSQZCSJL;
    private String SFSS;
    private String DSRQZ;
    private String DSRQZSJ;
    private String ZFRY1;
    private String ZFRY2;
    private String ZFZH1;
    private String ZFZH2;
    private String ZFSJ;
    private String ZID;
    private String READOUT;

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }

    public String getSSSJ1() {
        return SSSJ1;
    }

    public void setSSSJ1(String SSSJ1) {
        this.SSSJ1 = SSSJ1;
    }

    public String getSSSJ2() {
        return SSSJ2;
    }

    public void setSSSJ2(String SSSJ2) {
        this.SSSJ2 = SSSJ2;
    }

    public String getSSDD() {
        return SSDD;
    }

    public void setSSDD(String SSDD) {
        this.SSDD = SSDD;
    }

    public String getDSR() {
        return DSR;
    }

    public void setDSR(String DSR) {
        this.DSR = DSR;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getDCRY() {
        return DCRY;
    }

    public void setDCRY(String DCRY) {
        this.DCRY = DCRY;
    }

    public String getXB() {
        return XB;
    }

    public void setXB(String XB) {
        this.XB = XB;
    }

    public String getDH() {
        return DH;
    }

    public void setDH(String DH) {
        this.DH = DH;
    }

    public String getSFZH() {
        return SFZH;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public String getZZ() {
        return ZZ;
    }

    public void setZZ(String ZZ) {
        this.ZZ = ZZ;
    }

    public String getSSQZCSJL() {
        return SSQZCSJL;
    }

    public void setSSQZCSJL(String SSQZCSJL) {
        this.SSQZCSJL = SSQZCSJL;
    }

    public String getSFSS() {
        return SFSS;
    }

    public void setSFSS(String SFSS) {
        this.SFSS = SFSS;
    }

    public String getDSRQZ() {
        return DSRQZ;
    }

    public void setDSRQZ(String DSRQZ) {
        this.DSRQZ = DSRQZ;
    }

    public String getDSRQZSJ() {
        return DSRQZSJ;
    }

    public void setDSRQZSJ(String DSRQZSJ) {
        this.DSRQZSJ = DSRQZSJ;
    }

    public String getZFRY1() {
        return ZFRY1;
    }

    public void setZFRY1(String ZFRY1) {
        this.ZFRY1 = ZFRY1;
    }

    public String getZFRY2() {
        return ZFRY2;
    }

    public void setZFRY2(String ZFRY2) {
        this.ZFRY2 = ZFRY2;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getZFSJ() {
        return ZFSJ;
    }

    public void setZFSJ(String ZFSJ) {
        this.ZFSJ = ZFSJ;
    }

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getREADOUT() {
        return READOUT;
    }

    public void setREADOUT(String READOUT) {
        this.READOUT = READOUT;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.ID);
        dest.writeString(this.ZH);
        dest.writeString(this.SSSJ1);
        dest.writeString(this.SSSJ2);
        dest.writeString(this.SSDD);
        dest.writeString(this.DSR);
        dest.writeString(this.DZ);
        dest.writeString(this.DCRY);
        dest.writeString(this.XB);
        dest.writeString(this.DH);
        dest.writeString(this.SFZH);
        dest.writeString(this.ZZ);
        dest.writeString(this.SSQZCSJL);
        dest.writeString(this.SFSS);
        dest.writeString(this.DSRQZ);
        dest.writeString(this.DSRQZSJ);
        dest.writeString(this.ZFRY1);
        dest.writeString(this.ZFRY2);
        dest.writeString(this.ZFZH1);
        dest.writeString(this.ZFZH2);
        dest.writeString(this.ZFSJ);
        dest.writeString(this.ZID);
        dest.writeString(this.READOUT);
    }

    public LiveTranscript() {
    }

    protected LiveTranscript(Parcel in) {
        super(in);
        this.ID = in.readString();
        this.ZH = in.readString();
        this.SSSJ1 = in.readString();
        this.SSSJ2 = in.readString();
        this.SSDD = in.readString();
        this.DSR = in.readString();
        this.DZ = in.readString();
        this.DCRY = in.readString();
        this.XB = in.readString();
        this.DH = in.readString();
        this.SFZH = in.readString();
        this.ZZ = in.readString();
        this.SSQZCSJL = in.readString();
        this.SFSS = in.readString();
        this.DSRQZ = in.readString();
        this.DSRQZSJ = in.readString();
        this.ZFRY1 = in.readString();
        this.ZFRY2 = in.readString();
        this.ZFZH1 = in.readString();
        this.ZFZH2 = in.readString();
        this.ZFSJ = in.readString();
        this.ZID = in.readString();
        this.READOUT = in.readString();
    }

    public static final Creator<LiveTranscript> CREATOR = new Creator<LiveTranscript>() {
        @Override
        public LiveTranscript createFromParcel(Parcel source) {
            return new LiveTranscript(source);
        }

        @Override
        public LiveTranscript[] newArray(int size) {
            return new LiveTranscript[size];
        }
    };
}
