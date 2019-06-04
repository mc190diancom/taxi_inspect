package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public class WarningType implements ISelector {
    private String type;
    private boolean selected;

    public WarningType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
    public String toString() {
        return "WarningType{" +
                "type='" + type + '\'' +
                ", selected=" + selected +
                '}';
    }
}
