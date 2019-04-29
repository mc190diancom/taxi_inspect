package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/10/15.
 */
public class District {

    /**
     * ZDLBID : 朝阳区
     * LBMC : 朝阳区
     * PID : 1
     */

    private String ZDLBID;
    private String LBMC;
    private int PID;

    public String getZDLBID() {
        return ZDLBID;
    }

    public void setZDLBID(String ZDLBID) {
        this.ZDLBID = ZDLBID;
    }

    public String getLBMC() {
        return LBMC;
    }

    public void setLBMC(String LBMC) {
        this.LBMC = LBMC;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    @Override
    public String toString() {
        return "District{" +
                "ZDLBID='" + ZDLBID + '\'' +
                ", LBMC='" + LBMC + '\'' +
                ", PID=" + PID +
                '}';
    }
}
