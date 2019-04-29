package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/10/26.
 */
public class IllegalDetail {

    /**
     * ID : 39b0980d65e047379ef7a485617803ed
     * WFXWMC : 专用收费凭证打印项并与目格式不对应但打印内容清晰、不影响辨认、构成收款后未向乘客开具项目填写齐全并与实收金额相符的专用收费凭证
     * WFX : 十二
     */

    private String ID;
    private String WFXWMC;
    private String WFX;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWFXWMC() {
        return WFXWMC;
    }

    public void setWFXWMC(String WFXWMC) {
        this.WFXWMC = WFXWMC;
    }

    public String getWFX() {
        return WFX;
    }

    public void setWFX(String WFX) {
        this.WFX = WFX;
    }

    @Override
    public String toString() {
        return "IllegalDetail{" +
                "ID='" + ID + '\'' +
                ", WFXWMC='" + WFXWMC + '\'' +
                ", WFX='" + WFX + '\'' +
                '}';
    }
}
