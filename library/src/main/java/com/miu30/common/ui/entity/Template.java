package com.miu30.common.ui.entity;

/**
 * Created by Murphy on 2018/11/1.
 */
public class Template {

    /**
     * BLID : d6b562595aa04fb6b1d91e288c0f5846
     * ALIASNAME : 北京市交通执法总队现场检查笔录(路检)
     * PATH : /home/taxi/template/index1.html--0.0.2
     * VERSION : 0.0.2
     * BZ : 测试啊
     * CREATEUTC : 2018-11-01 14:26:33
     * TABLENAME : T_BJSJTZFZD_XCJCBL
     * STATE : 0
     */

    private String BLID;
    private String ALIASNAME;
    private String PATH;
    private String VERSION;
    private String BZ;
    private String CREATEUTC;
    private String TABLENAME;
    private int STATE;

    public String getBLID() {
        return BLID;
    }

    public void setBLID(String BLID) {
        this.BLID = BLID;
    }

    public String getALIASNAME() {
        return ALIASNAME;
    }

    public void setALIASNAME(String ALIASNAME) {
        this.ALIASNAME = ALIASNAME;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getCREATEUTC() {
        return CREATEUTC;
    }

    public void setCREATEUTC(String CREATEUTC) {
        this.CREATEUTC = CREATEUTC;
    }

    public String getTABLENAME() {
        return TABLENAME;
    }

    public void setTABLENAME(String TABLENAME) {
        this.TABLENAME = TABLENAME;
    }

    public int getSTATE() {
        return STATE;
    }

    public void setSTATE(int STATE) {
        this.STATE = STATE;
    }
}
