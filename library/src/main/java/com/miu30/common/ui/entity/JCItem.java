package com.miu30.common.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class JCItem implements Parcelable {
    private String ZDLBID;

    private String PID;

    private String LBBM;

    private String LBMC;

    private String LBMS;

    private String STATE;

    private String OPERATE_ID;

    private String FLAG;

    private String SYNC;

    private String STATUS;

    private String MBGS;

    private String BLMB;

    private String DESC_ID;

    private String LBJC;

    private String NEW_BM;

    private String XMMC;

    public String getZDLBID() {
        return ZDLBID;
    }

    public void setZDLBID(String zDLBID) {
        ZDLBID = zDLBID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String pID) {
        PID = pID;
    }

    public String getLBBM() {
        return LBBM;
    }

    public void setLBBM(String lBBM) {
        LBBM = lBBM;
    }

    public String getLBMC() {
        return LBMC;
    }

    public void setLBMC(String lBMC) {
        LBMC = lBMC;
    }

    public String getLBMS() {
        return LBMS;
    }

    public void setLBMS(String lBMS) {
        LBMS = lBMS;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String sTATE) {
        STATE = sTATE;
    }

    public String getOPERATE_ID() {
        return OPERATE_ID;
    }

    public void setOPERATE_ID(String oPERATE_ID) {
        OPERATE_ID = oPERATE_ID;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String fLAG) {
        FLAG = fLAG;
    }

    public String getSYNC() {
        return SYNC;
    }

    public void setSYNC(String sYNC) {
        SYNC = sYNC;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String sTATUS) {
        STATUS = sTATUS;
    }

    public String getMBGS() {
        return MBGS;
    }

    public void setMBGS(String mBGS) {
        MBGS = mBGS;
    }

    public String getBLMB() {
        return BLMB;
    }

    public void setBLMB(String bLMB) {
        BLMB = bLMB;
    }

    public String getDESC_ID() {
        return DESC_ID;
    }

    public void setDESC_ID(String dESC_ID) {
        DESC_ID = dESC_ID;
    }

    public String getLBJC() {
        return LBJC;
    }

    public void setLBJC(String lBJC) {
        LBJC = lBJC;
    }

    public String getNEW_BM() {
        return NEW_BM;
    }

    public void setNEW_BM(String nEW_BM) {
        NEW_BM = nEW_BM;
    }

    public String getXMMC() {
        return XMMC;
    }

    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }

    @Override
    public String toString() {
        return "JCItem [ZDLBID=" + ZDLBID + ", PID=" + PID + ", LBBM=" + LBBM
                + ", LBMC=" + LBMC + ", LBMS=" + LBMS + ", STATE=" + STATE
                + ", OPERATE_ID=" + OPERATE_ID + ", FLAG=" + FLAG + ", SYNC="
                + SYNC + ", STATUS=" + STATUS + ", MBGS=" + MBGS + ", BLMB="
                + BLMB + ", DESC_ID=" + DESC_ID + ", LBJC=" + LBJC
                + ", NEW_BM=" + NEW_BM + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ZDLBID);
        dest.writeString(this.PID);
        dest.writeString(this.LBBM);
        dest.writeString(this.LBMC);
        dest.writeString(this.LBMS);
        dest.writeString(this.STATE);
        dest.writeString(this.OPERATE_ID);
        dest.writeString(this.FLAG);
        dest.writeString(this.SYNC);
        dest.writeString(this.STATUS);
        dest.writeString(this.MBGS);
        dest.writeString(this.BLMB);
        dest.writeString(this.DESC_ID);
        dest.writeString(this.LBJC);
        dest.writeString(this.NEW_BM);
        dest.writeString(this.XMMC);
    }

    public JCItem() {
    }

    protected JCItem(Parcel in) {
        this.ZDLBID = in.readString();
        this.PID = in.readString();
        this.LBBM = in.readString();
        this.LBMC = in.readString();
        this.LBMS = in.readString();
        this.STATE = in.readString();
        this.OPERATE_ID = in.readString();
        this.FLAG = in.readString();
        this.SYNC = in.readString();
        this.STATUS = in.readString();
        this.MBGS = in.readString();
        this.BLMB = in.readString();
        this.DESC_ID = in.readString();
        this.LBJC = in.readString();
        this.NEW_BM = in.readString();
        this.XMMC = in.readString();
    }

    public static final Parcelable.Creator<JCItem> CREATOR = new Parcelable.Creator<JCItem>() {
        @Override
        public JCItem createFromParcel(Parcel source) {
            return new JCItem(source);
        }

        @Override
        public JCItem[] newArray(int size) {
            return new JCItem[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof JCItem) {
            JCItem item = (JCItem) obj;
            return LBMC != null && LBMC.equals(item.getLBMC());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return LBMC == null ? 0 : LBMC.hashCode();
    }

}
