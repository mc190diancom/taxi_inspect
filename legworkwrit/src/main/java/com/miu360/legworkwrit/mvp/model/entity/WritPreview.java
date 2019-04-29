package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/10/10.
 */
public class WritPreview {
    private String name;

    private String times;

    public WritPreview() {
    }

    public WritPreview(String name, String times) {
        this.name = name;
        this.times = times;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "WritPreview{" +
                "name='" + name + '\'' +
                ", times='" + times + '\'' +
                '}';
    }
}
