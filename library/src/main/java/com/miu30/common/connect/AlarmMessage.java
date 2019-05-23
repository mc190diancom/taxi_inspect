package com.miu30.common.connect;

/**
 * 作者：wanglei on 2019/5/23.
 * 邮箱：forwlwork@gmail.com
 */
public class AlarmMessage {
    /**
     * alarmUtc : 1558490755
     * alarmType : 50
     * msgType : 3
     * vname : 测12345
     * cameraID : 1111111111112
     * lon : 116.1231
     * lat : 39.3333
     */

    private int alarmUtc;
    /**
     * 检测违法类型
     * <p>
     * 50      套牌车辆
     * 51      存在疑点
     * 52      同天多次抓拍
     */
    private int alarmType;
    private int msgType;
    private String vname;
    private String cameraID;
    private double lon;
    private double lat;

    public int getAlarmUtc() {
        return alarmUtc;
    }

    public void setAlarmUtc(int alarmUtc) {
        this.alarmUtc = alarmUtc;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
