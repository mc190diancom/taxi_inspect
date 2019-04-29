package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/22.
 * 邮箱：forwlwork@gmail.com
 */
public class InquiryRecordPhotoParams {
    /**
     * ZID : 123
     * STARTUTC : 1540182883
     * ENDUTC : 1540182910
     */

    private String ZID;
    private String STARTUTC;
    private String ENDUTC;

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getSTARTUTC() {
        return STARTUTC;
    }

    public void setSTARTUTC(String STARTUTC) {
        this.STARTUTC = STARTUTC;
    }

    public String getENDUTC() {
        return ENDUTC;
    }

    public void setENDUTC(String ENDUTC) {
        this.ENDUTC = ENDUTC;
    }
}
