package com.feidi.elecsign.mvp.model.entity;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 */
public class AuthMy {
    private String name;
    private String card;
    private String validity;

    public AuthMy(String name, String card, String validity) {
        this.name = name;
        this.card = card;
        this.validity = validity;
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

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
