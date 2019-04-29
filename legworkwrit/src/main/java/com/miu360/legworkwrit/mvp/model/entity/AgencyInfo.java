package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 机关信息
 */
public class AgencyInfo {
    /**
     * ID : 1
     * DDMC : 直属队
     * DZ : 西城区北礼士路22号
     * DH : 68325483
     */
    private String ID;
    private String DDMC;
    private String DZ;
    private String DH;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDDMC() {
        return DDMC;
    }

    public void setDDMC(String DDMC) {
        this.DDMC = DDMC;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getDH() {
        return DH;
    }

    public void setDH(String DH) {
        this.DH = DH;
    }

    @Override
    public String toString() {
        return "AngencyInfo{" +
                "ID='" + ID + '\'' +
                ", DDMC='" + DDMC + '\'' +
                ", DZ='" + DZ + '\'' +
                ", DH='" + DH + '\'' +
                '}';
    }
}
