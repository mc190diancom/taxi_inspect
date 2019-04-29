package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by Murphy on 2018/10/17.
 */
public class AdministrativePenalty extends ParentQ {

    /**
     * ZH : 测试
     * DSR : 测试
     * XB : 测试
     * FWJDKH : 测试
     * WLYYCZJSYZH : 测试
     * DZ : 测试
     * BCSJ : 1539156963
     * BCDD : 测试
     * BCVNAME : 测试
     * WFXW : 测试
     * WFTK : 测试
     * ZFRY1 : 测试
     * ZFZH1 : 0000000
     * ZFRY2 : 测试
     * ZFZH2 : 7777777
     * SDSJ : 1539156963
     * XZJGSJ : 1539156963
     * QSRQZ : 测试
     * QSSJ : 1539156963
     * ZID : 123123
     */
    private String ID;
    private String ZH;
    private String DSR;
    private String XB;
    private String FWJDKH;
    private String WLYYCZJSYZH;
    private String DZ;
    private String BCSJ;
    private String BCDD;
    private String BCVNAME;
    private String WFXW;
    private String WFTK;
    private String ZFRY1;
    private String ZFZH1;
    private String ZFRY2;
    private String ZFZH2;
    private String SDSJ;
    private String XZJGSJ;
    private String QSRQZ;
    private String QSSJ;
    private String ZID;
    private String WFXWID;

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }

    public String getDSR() {
        return DSR;
    }

    public void setDSR(String DSR) {
        this.DSR = DSR;
    }

    public String getXB() {
        return XB;
    }

    public void setXB(String XB) {
        this.XB = XB;
    }

    public String getFWJDKH() {
        return FWJDKH;
    }

    public void setFWJDKH(String FWJDKH) {
        this.FWJDKH = FWJDKH;
    }

    public String getWLYYCZJSYZH() {
        return WLYYCZJSYZH;
    }

    public void setWLYYCZJSYZH(String WLYYCZJSYZH) {
        this.WLYYCZJSYZH = WLYYCZJSYZH;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getBCSJ() {
        return BCSJ;
    }

    public void setBCSJ(String BCSJ) {
        this.BCSJ = BCSJ;
    }

    public String getBCDD() {
        return BCDD;
    }

    public void setBCDD(String BCDD) {
        this.BCDD = BCDD;
    }

    public String getBCVNAME() {
        return BCVNAME;
    }

    public void setBCVNAME(String BCVNAME) {
        this.BCVNAME = BCVNAME;
    }

    public String getWFXW() {
        return WFXW;
    }

    public void setWFXW(String WFXW) {
        this.WFXW = WFXW;
    }

    public String getWFTK() {
        return WFTK;
    }

    public void setWFTK(String WFTK) {
        this.WFTK = WFTK;
    }

    public String getZFRY1() {
        return ZFRY1;
    }

    public void setZFRY1(String ZFRY1) {
        this.ZFRY1 = ZFRY1;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getZFRY2() {
        return ZFRY2;
    }

    public void setZFRY2(String ZFRY2) {
        this.ZFRY2 = ZFRY2;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getSDSJ() {
        return SDSJ;
    }

    public void setSDSJ(String SDSJ) {
        this.SDSJ = SDSJ;
    }

    public String getXZJGSJ() {
        return XZJGSJ;
    }

    public void setXZJGSJ(String XZJGSJ) {
        this.XZJGSJ = XZJGSJ;
    }

    public String getQSRQZ() {
        return QSRQZ;
    }

    public void setQSRQZ(String QSRQZ) {
        this.QSRQZ = QSRQZ;
    }

    public String getQSSJ() {
        return QSSJ;
    }

    public void setQSSJ(String QSSJ) {
        this.QSSJ = QSSJ;
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

    public String getWFXWID() {
        return WFXWID;
    }

    public void setWFXWID(String WFXWID) {
        this.WFXWID = WFXWID;
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
        dest.writeString(this.DSR);
        dest.writeString(this.XB);
        dest.writeString(this.FWJDKH);
        dest.writeString(this.WLYYCZJSYZH);
        dest.writeString(this.DZ);
        dest.writeString(this.BCSJ);
        dest.writeString(this.BCDD);
        dest.writeString(this.BCVNAME);
        dest.writeString(this.WFXW);
        dest.writeString(this.WFTK);
        dest.writeString(this.ZFRY1);
        dest.writeString(this.ZFZH1);
        dest.writeString(this.ZFRY2);
        dest.writeString(this.ZFZH2);
        dest.writeString(this.SDSJ);
        dest.writeString(this.XZJGSJ);
        dest.writeString(this.QSRQZ);
        dest.writeString(this.QSSJ);
        dest.writeString(this.ZID);
        dest.writeString(this.WFXWID);
    }

    public AdministrativePenalty() {
    }

    protected AdministrativePenalty(Parcel in) {
        super(in);
        this.ID = in.readString();
        this.ZH = in.readString();
        this.DSR = in.readString();
        this.XB = in.readString();
        this.FWJDKH = in.readString();
        this.WLYYCZJSYZH = in.readString();
        this.DZ = in.readString();
        this.BCSJ = in.readString();
        this.BCDD = in.readString();
        this.BCVNAME = in.readString();
        this.WFXW = in.readString();
        this.WFTK = in.readString();
        this.ZFRY1 = in.readString();
        this.ZFZH1 = in.readString();
        this.ZFRY2 = in.readString();
        this.ZFZH2 = in.readString();
        this.SDSJ = in.readString();
        this.XZJGSJ = in.readString();
        this.QSRQZ = in.readString();
        this.QSSJ = in.readString();
        this.ZID = in.readString();
        this.WFXWID = in.readString();
    }

    public static final Creator<AdministrativePenalty> CREATOR = new Creator<AdministrativePenalty>() {
        @Override
        public AdministrativePenalty createFromParcel(Parcel source) {
            return new AdministrativePenalty(source);
        }

        @Override
        public AdministrativePenalty[] newArray(int size) {
            return new AdministrativePenalty[size];
        }
    };
}
