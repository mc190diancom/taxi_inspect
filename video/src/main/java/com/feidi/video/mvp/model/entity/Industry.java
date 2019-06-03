package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public class Industry implements ISelector {
    private String name;
    private boolean selected;

    public Industry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
