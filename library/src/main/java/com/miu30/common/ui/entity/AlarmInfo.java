package com.miu30.common.ui.entity;

import java.util.List;

public class AlarmInfo {
    /**
     * msgType : 3
     * alarmType : 52
     * cameraIDList : ["11000000001325291355"]
     * deviceID : 11000000001325291355
     * deviceName : T3B1出口
     * eventID : 000568151d51c4bb4346a88c52098dcea9c5
     * latitude : 40.05179977416992
     * longitude : 116.6136016845703
     * occurTime : 2019-05-24T17:04:18
     * pictureIDList : ftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-024/000410433fcc1ade47ce9160d764e2ae7377.jpgftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-05-24/0004ed49c13217774fce9de471c41b2f87ba.jpg
     * vehicleIndustryType : 55
     * vehiclePlatNo : 蓝京BT6937
     */

    private int msgType;
    private int alarmType;
    private String deviceID;
    private String deviceName;
    private String eventID;
    private double latitude;
    private double longitude;
    private String occurTime;
    private String pictureIDList;
    private String vehicleIndustryType;
    private String vehiclePlatNo;
    private List<String> cameraIDList;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
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

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getPictureIDList() {
        return pictureIDList;
    }

    public void setPictureIDList(String pictureIDList) {
        this.pictureIDList = pictureIDList;
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

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "msgType=" + msgType +
                ", alarmType=" + alarmType +
                ", deviceID='" + deviceID + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", eventID='" + eventID + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", occurTime='" + occurTime + '\'' +
                ", pictureIDList='" + pictureIDList + '\'' +
                ", vehicleIndustryType='" + vehicleIndustryType + '\'' +
                ", vehiclePlatNo='" + vehiclePlatNo + '\'' +
                ", cameraIDList=" + cameraIDList +
                '}';
    }
}
