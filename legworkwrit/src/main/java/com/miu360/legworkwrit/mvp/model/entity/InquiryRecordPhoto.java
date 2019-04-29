package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：wanglei on 2018/10/22.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 询问笔录照片
 */
public class InquiryRecordPhoto implements Parcelable {
    /**
     * ID : 658130c526b648c092d59a595bf66ce7
     * ZPLJ : "ZPLJ": "\/home\/taxi\/xwbl_photo\/0000000_1540207107148
     * ZID : 123
     * STARTUTC : 2018-10-26 17:43:00
     * ENDUTC : 2018-10-26 17:46:00
     */
    private String ID;
    private String ZPLJ;
    private String ZID;
    private String STARTUTC;
    private String ENDUTC;
    private boolean isChecked = false;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getZPLJ() {
        return ZPLJ;
    }

    public void setZPLJ(String ZPLJ) {
        this.ZPLJ = ZPLJ;
    }

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.ZPLJ);
        dest.writeString(this.ZID);
        dest.writeString(this.STARTUTC);
        dest.writeString(this.ENDUTC);
    }

    public InquiryRecordPhoto() {
    }

    protected InquiryRecordPhoto(Parcel in) {
        this.ID = in.readString();
        this.ZPLJ = in.readString();
        this.ZID = in.readString();
        this.STARTUTC = in.readString();
        this.ENDUTC = in.readString();
    }

    public static final Parcelable.Creator<InquiryRecordPhoto> CREATOR = new Parcelable.Creator<InquiryRecordPhoto>() {
        @Override
        public InquiryRecordPhoto createFromParcel(Parcel source) {
            return new InquiryRecordPhoto(source);
        }

        @Override
        public InquiryRecordPhoto[] newArray(int size) {
            return new InquiryRecordPhoto[size];
        }
    };

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
