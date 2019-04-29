package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2019/3/8.
 * 邮箱：forwlwork@gmail.com
 */
public class IllegalDetailParams {
    private String HYLB;
    private String CFBZ;

    public IllegalDetailParams() {
    }

    public IllegalDetailParams(String HYLB, String CFBZ) {
        this.HYLB = HYLB;
        this.CFBZ = CFBZ;
    }

    public String getHYLB() {
        return HYLB;
    }

    public void setHYLB(String HYLB) {
        this.HYLB = HYLB;
    }

    public String getCFBZ() {
        return CFBZ;
    }

    public void setCFBZ(String CFBZ) {
        this.CFBZ = CFBZ;
    }
}
