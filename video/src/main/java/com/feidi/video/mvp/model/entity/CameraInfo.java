package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class CameraInfo implements ISelector, Parcelable {
    private String name;
    private boolean selected;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CameraInfo(String name) {
        this.name = name;
    }

    public CameraInfo(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
    }

    protected CameraInfo(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
    }

    public static final Parcelable.Creator<CameraInfo> CREATOR = new Parcelable.Creator<CameraInfo>() {
        @Override
        public CameraInfo createFromParcel(Parcel source) {
            return new CameraInfo(source);
        }

        @Override
        public CameraInfo[] newArray(int size) {
            return new CameraInfo[size];
        }
    };
}
