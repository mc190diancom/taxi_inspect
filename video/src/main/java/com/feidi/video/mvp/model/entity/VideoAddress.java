package com.feidi.video.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoAddress implements Parcelable {

    /**
     * ret : 0
     * msg : 成功
     * rtspUrl : rtsp://10.212.160.158:6333/11000000001325291355-rp098000006
     */

    private int ret;
    private String msg;
    private String rtspUrl;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ret);
        dest.writeString(this.msg);
        dest.writeString(this.rtspUrl);
    }

    public VideoAddress() {
    }

    protected VideoAddress(Parcel in) {
        this.ret = in.readInt();
        this.msg = in.readString();
        this.rtspUrl = in.readString();
    }

    public static final Parcelable.Creator<VideoAddress> CREATOR = new Parcelable.Creator<VideoAddress>() {
        @Override
        public VideoAddress createFromParcel(Parcel source) {
            return new VideoAddress(source);
        }

        @Override
        public VideoAddress[] newArray(int size) {
            return new VideoAddress[size];
        }
    };
}
