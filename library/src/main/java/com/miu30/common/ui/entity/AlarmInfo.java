package com.miu30.common.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AlarmInfo implements Parcelable {


    /**
     * alarmType : 52
     * cameraIDList : ["11000000001325291355"]
     * deviceID : 11000000001325291355
     * deviceName : T3B1出口
     * eventID : 00054c2cf766f081420da9ea7d06c6a25e8c
     * latitude : 40.05179977416992
     * longitude : 116.6136016845703
     * msgType : 16
     * occurTime : 2019-06-05T12:24:24
     * pictureIDList : ["ftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-06-05/0004a455991977b9447eb3e0edaf2c19d4d3.jpg","ftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-06-05/0004a80d2b5e28724842960f189dd540c1d2.jpg"]
     * vehicleIndustryType : 52
     * vehiclePlatNo : 黄京B10002
     */

    private String alarmType;
    private String deviceID;
    private String deviceName;
    private String eventID;
    private double latitude;
    private double longitude;
    private int msgType;
    private String occurTime;
    private String vehicleIndustryType;
    private String vehiclePlatNo;
    private List<String> cameraIDList;
    private List<String> pictureIDList;

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getVehicleIndustryType() {
        return vehicleIndustryType;
    }

    public void setVehicleIndustryType(String vehicleIndustryType) {
        this.vehicleIndustryType = vehicleIndustryType;
    }

    public String getVehiclePlatNo() {
        return vehiclePlatNo;
    }

    public void setVehiclePlatNo(String vehiclePlatNo) {
        this.vehiclePlatNo = vehiclePlatNo;
    }

    public List<String> getCameraIDList() {
        return cameraIDList;
    }

    public void setCameraIDList(List<String> cameraIDList) {
        this.cameraIDList = cameraIDList;
    }

    public List<String> getPictureIDList() {
        return pictureIDList;
    }

    public void setPictureIDList(List<String> pictureIDList) {
        this.pictureIDList = pictureIDList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alarmType);
        dest.writeString(this.deviceID);
        dest.writeString(this.deviceName);
        dest.writeString(this.eventID);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.msgType);
        dest.writeString(this.occurTime);
        dest.writeString(this.vehicleIndustryType);
        dest.writeString(this.vehiclePlatNo);
        dest.writeStringList(this.cameraIDList);
        dest.writeStringList(this.pictureIDList);
    }

    public AlarmInfo() {
    }

    protected AlarmInfo(Parcel in) {
        this.alarmType = in.readString();
        this.deviceID = in.readString();
        this.deviceName = in.readString();
        this.eventID = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.msgType = in.readInt();
        this.occurTime = in.readString();
        this.vehicleIndustryType = in.readString();
        this.vehiclePlatNo = in.readString();
        this.cameraIDList = in.createStringArrayList();
        this.pictureIDList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<AlarmInfo> CREATOR = new Parcelable.Creator<AlarmInfo>() {
        @Override
        public AlarmInfo createFromParcel(Parcel source) {
            return new AlarmInfo(source);
        }

        @Override
        public AlarmInfo[] newArray(int size) {
            return new AlarmInfo[size];
        }
    };
}
