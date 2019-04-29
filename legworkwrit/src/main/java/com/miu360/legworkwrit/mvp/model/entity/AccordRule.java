package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/22.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 扣押车辆交接单依据条例
 */
public class AccordRule {
    /**
     * SYFLFG : 北京市水域游船安全管理规定
     */
    private String SYFLFG;
    private String NAME;

    public String getSYFLFG() {
        return SYFLFG;
    }

    public void setSYFLFG(String SYFLFG) {
        this.SYFLFG = SYFLFG;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
