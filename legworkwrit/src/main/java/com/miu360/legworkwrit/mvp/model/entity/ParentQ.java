package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.MiuBaseApp;

public class ParentQ implements Parcelable {
    /**
     * html的本地完整路径
     */
    @Expose
    private String localPath = "";
    /**
     * 保存图片的文件名
     */
    @Expose
    private String fileName = "index.jpg";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public String toString() {
        Gson gson = ArmsUtils.obtainAppComponentFromContext(MiuBaseApp.self).gson();
        return gson.toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.localPath);
        dest.writeString(this.fileName);
    }

    public ParentQ() {
    }

    protected ParentQ(Parcel in) {
        this.localPath = in.readString();
        this.fileName = in.readString();
    }

}
