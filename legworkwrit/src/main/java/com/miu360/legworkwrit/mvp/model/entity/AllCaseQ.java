package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by Murphy on 2018/10/17.
 */
public class AllCaseQ extends ParentQ {

    /**
     * STARTINDEX : 0
     * ENDINDEX : 3
     * VNAME :
     * ZFZH1 : 0000000
     * ZFZH2 :
     * SSDD :
     * SSZD :
     * SSCZ :
     * ZFRYNAME1 :
     * ZFRYNAME2 :
     * STARTUTC :
     * ENDUTC :
     */

    private String STARTINDEX;
    private String ENDINDEX;
    private String VNAME;
    private String ZFZH;
    private String SSDD;
    private String SSZD;
    private String SSCZ;
    private String ZFRYNAME1;
    private String ZFRYNAME2;
    private String STARTUTC;
    private String ENDUTC;
    private String HYLB;
    private String STATUS;//1已送内勤，0未送内勤
    private String CFFS;

    public String getSTARTINDEX() {
        return STARTINDEX;
    }

    public void setSTARTINDEX(String STARTINDEX) {
        this.STARTINDEX = STARTINDEX;
    }

    public String getENDINDEX() {
        return ENDINDEX;
    }

    public void setENDINDEX(String ENDINDEX) {
        this.ENDINDEX = ENDINDEX;
    }

    public String getVNAME() {
        return VNAME;
    }

    public void setVNAME(String VNAME) {
        this.VNAME = VNAME;
    }

    public String getZFZH() {
        return ZFZH;
    }

    public void setZFZH(String ZFZH) {
        this.ZFZH = ZFZH;
    }

    public String getSSDD() {
        return SSDD;
    }

    public void setSSDD(String SSDD) {
        this.SSDD = SSDD;
    }

    public String getSSZD() {
        return SSZD;
    }

    public void setSSZD(String SSZD) {
        this.SSZD = SSZD;
    }

    public String getSSCZ() {
        return SSCZ;
    }

    public void setSSCZ(String SSCZ) {
        this.SSCZ = SSCZ;
    }

    public String getZFRYNAME1() {
        return ZFRYNAME1;
    }

    public void setZFRYNAME1(String ZFRYNAME1) {
        this.ZFRYNAME1 = ZFRYNAME1;
    }

    public String getZFRYNAME2() {
        return ZFRYNAME2;
    }

    public void setZFRYNAME2(String ZFRYNAME2) {
        this.ZFRYNAME2 = ZFRYNAME2;
    }

    public String getSTARTUTC() {
        return STARTUTC;
    }

    public void setSTARTUTC(String STARTUTC) {
        this.STARTUTC = STARTUTC;
    }

    public String getENDUTC() {
        return ENDUTC;
    }

    public void setENDUTC(String ENDUTC) {
        this.ENDUTC = ENDUTC;
    }

    public String getHYLB() {
        return HYLB;
    }

    public void setHYLB(String HYLB) {
        this.HYLB = HYLB;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCFFS() {
        return CFFS;
    }

    public void setCFFS(String CFFS) {
        this.CFFS = CFFS;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.STARTINDEX);
        dest.writeString(this.ENDINDEX);
        dest.writeString(this.VNAME);
        dest.writeString(this.ZFZH);
        dest.writeString(this.SSDD);
        dest.writeString(this.SSZD);
        dest.writeString(this.SSCZ);
        dest.writeString(this.ZFRYNAME1);
        dest.writeString(this.ZFRYNAME2);
        dest.writeString(this.STARTUTC);
        dest.writeString(this.ENDUTC);
        dest.writeString(this.HYLB);
        dest.writeString(this.STATUS);
        dest.writeString(this.CFFS);
    }

    public AllCaseQ() {
    }

    protected AllCaseQ(Parcel in) {
        super(in);
        this.STARTINDEX = in.readString();
        this.ENDINDEX = in.readString();
        this.VNAME = in.readString();
        this.ZFZH = in.readString();
        this.SSDD = in.readString();
        this.SSZD = in.readString();
        this.SSCZ = in.readString();
        this.ZFRYNAME1 = in.readString();
        this.ZFRYNAME2 = in.readString();
        this.STARTUTC = in.readString();
        this.ENDUTC = in.readString();
        this.HYLB = in.readString();
        this.STATUS = in.readString();
        this.CFFS = in.readString();
    }

    public static final Creator<AllCaseQ> CREATOR = new Creator<AllCaseQ>() {
        @Override
        public AllCaseQ createFromParcel(Parcel source) {
            return new AllCaseQ(source);
        }

        @Override
        public AllCaseQ[] newArray(int size) {
            return new AllCaseQ[size];
        }
    };
}
