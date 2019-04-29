package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 先行登记返回结果
 */
public class FristRegister {
    private String id;
    private FristRegisterQ fristRegisterQ;

    public FristRegister(String id, FristRegisterQ fristRegisterQ) {
        this.id = id;
        this.fristRegisterQ = fristRegisterQ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FristRegisterQ getFristRegisterQ() {
        return fristRegisterQ;
    }

    public void setFristRegisterQ(FristRegisterQ fristRegisterQ) {
        this.fristRegisterQ = fristRegisterQ;
    }
}
