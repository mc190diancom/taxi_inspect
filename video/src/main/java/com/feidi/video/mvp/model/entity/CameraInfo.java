package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class CameraInfo implements ISelector {
    private boolean selected;
    /**
     * DWMC : 机场队
     * CAMERAID : 11000005001310255301
     * NAME : T1长途车
     * LON : 116.588015850
     * LAT : 40.0802993660
     */

    private String DWMC;
    private String CAMERAID;
    private String NAME;
    private String LON;
    private String LAT;

    public CameraInfo() {
    }

    public CameraInfo(boolean selected, String DWMC, String CAMERAID, String NAME, String LON, String LAT) {
        this.selected = selected;
        this.DWMC = DWMC;
        this.CAMERAID = CAMERAID;
        this.NAME = NAME;
        this.LON = LON;
        this.LAT = LAT;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getCAMERAID() {
        return CAMERAID;
    }

    public void setCAMERAID(String CAMERAID) {
        this.CAMERAID = CAMERAID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    @Override
    public String toString() {
        return "CameraInfo{" +
                "selected=" + selected +
                ", DWMC='" + DWMC + '\'' +
                ", CAMERAID='" + CAMERAID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", LON='" + LON + '\'' +
                ", LAT='" + LAT + '\'' +
                '}';
    }
}
