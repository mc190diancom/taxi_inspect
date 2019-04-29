package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Murphy on 2018/12/3.
 */
public class CaseStatus implements Parcelable {

    private String blType;

    private String id;

    private int status;//0未填写,1已填写,2待确认

    private String printTimes;

    public String getBlType() {
        return blType;
    }

    public void setBlType(String blType) {
        this.blType = blType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(String printTimes) {
        this.printTimes = printTimes;
    }

    public CaseStatus(String blType, String id, int status) {
        this.blType = blType;
        this.id = id;
        this.status = status;
    }

    public CaseStatus(String blType, String id, int status, String printTimes) {
        this.blType = blType;
        this.id = id;
        this.status = status;
        this.printTimes = printTimes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blType);
        dest.writeString(this.id);
        dest.writeInt(this.status);
        dest.writeString(this.printTimes);
    }

    protected CaseStatus(Parcel in) {
        this.blType = in.readString();
        this.id = in.readString();
        this.status = in.readInt();
        this.printTimes = in.readString();
    }

    public static final Parcelable.Creator<CaseStatus> CREATOR = new Parcelable.Creator<CaseStatus>() {
        @Override
        public CaseStatus createFromParcel(Parcel source) {
            return new CaseStatus(source);
        }

        @Override
        public CaseStatus[] newArray(int size) {
            return new CaseStatus[size];
        }
    };
}
