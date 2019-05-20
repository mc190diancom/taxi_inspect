package com.feidi.elecsign.mvp.model.entity;

import com.feidi.elecsign.mvp.ui.view.ThreeStateSwitch;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 */
public class MyAuth {
    private String name;
    private String card;
    private String overdue;
    private int state;

    public MyAuth(String name, String card, String overdue, @ThreeStateSwitch.State int state) {
        this.name = name;
        this.card = card;
        this.overdue = overdue;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
