package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Murphy on 2018/10/18.
 */
public class BlType implements Parcelable {

    /**
     * "TABLENAME":"T_BJSJTZFZD_XCJCBL",
     * "ALIASNAME":"北京市交通执法总队现场检查笔录(路检)",
     * "COLUMNNAME":"XCJCBL1",
     * "FLAG":0
     */

    private String ID;
    private String TABLENAME;
    private String ALIASNAME;
    private String COLUMNNAME;
    private int FLAG;//0未填写，1已填写，2待确认
    private String printTimes;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTABLENAME() {
        return TABLENAME;
    }

    public void setTABLENAME(String TABLENAME) {
        this.TABLENAME = TABLENAME;
    }

    public String getALIASNAME() {
        return ALIASNAME;
    }

    public void setALIASNAME(String ALIASNAME) {
        this.ALIASNAME = ALIASNAME;
    }

    public String getCOLUMNNAME() {
        return COLUMNNAME;
    }

    public void setCOLUMNNAME(String COLUMNNAME) {
        this.COLUMNNAME = COLUMNNAME;
    }

    public int getFLAG() {
        return FLAG;
    }

    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }

    public String getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(String printTimes) {
        this.printTimes = printTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.TABLENAME);
        dest.writeString(this.ALIASNAME);
        dest.writeString(this.COLUMNNAME);
        dest.writeInt(this.FLAG);
        dest.writeString(this.printTimes);
    }

    public BlType() {
    }

    protected BlType(Parcel in) {
        this.ID = in.readString();
        this.TABLENAME = in.readString();
        this.ALIASNAME = in.readString();
        this.COLUMNNAME = in.readString();
        this.FLAG = in.readInt();
        this.printTimes = in.readString();
    }

    public static final Parcelable.Creator<BlType> CREATOR = new Parcelable.Creator<BlType>() {
        @Override
        public BlType createFromParcel(Parcel source) {
            return new BlType(source);
        }

        @Override
        public BlType[] newArray(int size) {
            return new BlType[size];
        }
    };
}
