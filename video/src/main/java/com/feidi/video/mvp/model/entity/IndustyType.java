package com.feidi.video.mvp.model.entity;

public class IndustyType implements ISelector{
    private String HYLX;

    public String getHYLX() {
        return HYLX;
    }

    public void setHYLX(String HYLX) {
        this.HYLX = HYLX;
    }

    public IndustyType(String HYLX) {
        this.HYLX = HYLX;
    }

    public IndustyType() {
    }

    @Override
    public String toString() {
        return "IndustyType{" +
                "HYLX='" + HYLX + '\'' +
                '}';
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }
}
