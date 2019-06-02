package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public class CrimeInfo {
    private int count;
    private String carLicense;
    private int distance;

    public CrimeInfo(int count, String carLicense, int distance) {
        this.count = count;
        this.carLicense = carLicense;
        this.distance = distance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
