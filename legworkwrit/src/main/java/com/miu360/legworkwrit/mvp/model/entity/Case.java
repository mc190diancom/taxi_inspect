package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Murphy on 2018/10/10.
 */
public class Case implements Parcelable {
    /**
     * ZFZH1 : 0000000
     * HYLB : 巡游车
     * VNAME : 京00001
     * BJCR : 被检查人
     * ZHZH2 : 7777777
     * ZFRYNAME2 : 测试
     * JCQY : 检查区域
     * CFFS : 处罚方式
     * AJLY : 来源
     * BLLISTID : d6b562595aa04fb6b1d91e288c0f5846,575fd68709f246a6b0c068d254a75a04
     */
    private String ID;
    private String ZH;
    private String ZFZH1;
    private String HYLB;
    private String VNAME;
    private String BJCR;
    private String ZHZH2;
    private String ZFRYNAME1;
    private String ZFRYNAME2;
    private String JCQY;
    private String CFFS;
    private String AJLY;
    private String CREATEUTC;
    private String JCDD;
    private String LAT;
    private String LON;
    private String STATE;

    public Case() {
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getHYLB() {
        return HYLB;
    }

    public void setHYLB(String HYLB) {
        this.HYLB = HYLB;
    }

    public String getVNAME() {
        return VNAME;
    }

    public void setVNAME(String VNAME) {
        this.VNAME = VNAME;
    }

    public String getBJCR() {
        return BJCR;
    }

    public void setBJCR(String BJCR) {
        this.BJCR = BJCR;
    }

    public String getZHZH2() {
        return ZHZH2;
    }

    public void setZHZH2(String ZHZH2) {
        this.ZHZH2 = ZHZH2;
    }

    public String getZFRYNAME2() {
        return ZFRYNAME2;
    }

    public void setZFRYNAME2(String ZFRYNAME2) {
        this.ZFRYNAME2 = ZFRYNAME2;
    }

    public String getJCQY() {
        return JCQY;
    }

    public void setJCQY(String JCQY) {
        this.JCQY = JCQY;
    }

    public String getCFFS() {
        return CFFS;
    }

    public void setCFFS(String CFFS) {
        this.CFFS = CFFS;
    }

    public String getAJLY() {
        return AJLY;
    }

    public void setAJLY(String AJLY) {
        this.AJLY = AJLY;
    }

    public String getCREATEUTC() {
        return CREATEUTC;
    }

    public void setCREATEUTC(String CREATEUTC) {
        this.CREATEUTC = CREATEUTC;
    }

    public String getJCDD() {
        return JCDD;
    }

    public void setJCDD(String JCDD) {
        this.JCDD = JCDD;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }

    public String getZFRYNAME1() {
        return ZFRYNAME1;
    }

    public void setZFRYNAME1(String ZFRYNAME1) {
        this.ZFRYNAME1 = ZFRYNAME1;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }



    @Override
    public String toString() {
        return "Case{" +
                "ID='" + ID + '\'' +
                ", ZH='" + ZH + '\'' +
                ", ZFZH1='" + ZFZH1 + '\'' +
                ", HYLB='" + HYLB + '\'' +
                ", VNAME='" + VNAME + '\'' +
                ", BJCR='" + BJCR + '\'' +
                ", ZHZH2='" + ZHZH2 + '\'' +
                ", ZFRYNAME1='" + ZFRYNAME1 + '\'' +
                ", ZFRYNAME2='" + ZFRYNAME2 + '\'' +
                ", JCQY='" + JCQY + '\'' +
                ", CFFS='" + CFFS + '\'' +
                ", AJLY='" + AJLY + '\'' +
                ", CREATEUTC='" + CREATEUTC + '\'' +
                ", JCDD='" + JCDD + '\'' +
                ", LAT='" + LAT + '\'' +
                ", LON='" + LON + '\'' +
                ", STATE='" + STATE + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.ZH);
        dest.writeString(this.ZFZH1);
        dest.writeString(this.HYLB);
        dest.writeString(this.VNAME);
        dest.writeString(this.BJCR);
        dest.writeString(this.ZHZH2);
        dest.writeString(this.ZFRYNAME1);
        dest.writeString(this.ZFRYNAME2);
        dest.writeString(this.JCQY);
        dest.writeString(this.CFFS);
        dest.writeString(this.AJLY);
        dest.writeString(this.CREATEUTC);
        dest.writeString(this.JCDD);
        dest.writeString(this.LAT);
        dest.writeString(this.LON);
        dest.writeString(this.STATE);
    }

    protected Case(Parcel in) {
        this.ID = in.readString();
        this.ZH = in.readString();
        this.ZFZH1 = in.readString();
        this.HYLB = in.readString();
        this.VNAME = in.readString();
        this.BJCR = in.readString();
        this.ZHZH2 = in.readString();
        this.ZFRYNAME1 = in.readString();
        this.ZFRYNAME2 = in.readString();
        this.JCQY = in.readString();
        this.CFFS = in.readString();
        this.AJLY = in.readString();
        this.CREATEUTC = in.readString();
        this.JCDD = in.readString();
        this.LAT = in.readString();
        this.LON = in.readString();
        this.STATE = in.readString();
    }

    public static final Creator<Case> CREATOR = new Creator<Case>() {
        @Override
        public Case createFromParcel(Parcel source) {
            return new Case(source);
        }

        @Override
        public Case[] newArray(int size) {
            return new Case[size];
        }
    };
}
