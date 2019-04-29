package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/12/5.
 */
public class InstrumentStateReq {

    /**
     * ZID : 79300d54e48440148ac374fa88ba3c20
     * BLID : d6b562595aa04fb6b1d91e288c0f5846
     * FLAG : 1
     */

    private String ZID;
    private String BLID;
    private String FLAG;
    private String CURRENTBLID;
    private String CURRENTFLAG;

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getBLID() {
        return BLID;
    }

    public void setBLID(String BLID) {
        this.BLID = BLID;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getCURRENTBLID() {
        return CURRENTBLID;
    }

    public void setCURRENTBLID(String CURRENTBLID) {
        this.CURRENTBLID = CURRENTBLID;
    }

    public String getCURRENTFLAG() {
        return CURRENTFLAG;
    }

    public void setCURRENTFLAG(String CURRENTFLAG) {
        this.CURRENTFLAG = CURRENTFLAG;
    }

    public InstrumentStateReq(String ZID, String BLID, String FLAG, String CURRENTBLID, String CURRENTFLAG) {
        this.ZID = ZID;
        this.BLID = BLID;
        this.FLAG = FLAG;
        this.CURRENTBLID = CURRENTBLID;
        this.CURRENTFLAG = CURRENTFLAG;
    }
}
