package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：wanglei on 2019/1/7.
 * 邮箱：forwlwork@gmail.com
 */
public class PrintTimes implements Parcelable {
    /**
     * TABLENAME : T_BJSJTZFZD_XCJCBL
     * PRINTTIMES : 3
     * ID : 9d01f42a120a40e08b6d9da49f402e80
     */

    private String TABLENAME;
    private String PRINTTIMES;
    private String ID;

    public String getTABLENAME() {
        return TABLENAME;
    }

    public void setTABLENAME(String TABLENAME) {
        this.TABLENAME = TABLENAME;
    }

    public String getPRINTTIMES() {
        return PRINTTIMES;
    }

    public void setPRINTTIMES(String PRINTTIMES) {
        this.PRINTTIMES = PRINTTIMES;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TABLENAME);
        dest.writeString(this.PRINTTIMES);
        dest.writeString(this.ID);
    }

    public PrintTimes() {
    }

    protected PrintTimes(Parcel in) {
        this.TABLENAME = in.readString();
        this.PRINTTIMES = in.readString();
        this.ID = in.readString();
    }

    public static final Parcelable.Creator<PrintTimes> CREATOR = new Parcelable.Creator<PrintTimes>() {
        @Override
        public PrintTimes createFromParcel(Parcel source) {
            return new PrintTimes(source);
        }

        @Override
        public PrintTimes[] newArray(int size) {
            return new PrintTimes[size];
        }
    };

    @Override
    public String toString() {
        return "PrintTimes{" +
                "TABLENAME='" + TABLENAME + '\'' +
                ", PRINTTIMES='" + PRINTTIMES + '\'' +
                ", ID='" + ID + '\'' +
                '}';
    }
}
