package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/30.
 * 邮箱：forwlwork@gmail.com
 */
public class WifiRequestParams {
    /**
     * ZFZH : 00001
     */
    private String ZFZH;

    public WifiRequestParams(String ZFZH) {
        this.ZFZH = ZFZH;
    }

    public String getZFZH() {
        return ZFZH;
    }

    public void setZFZH(String ZFZH) {
        this.ZFZH = ZFZH;
    }
}
