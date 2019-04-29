package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 谈话通知书请求参数
 */
public class TalkNoticeQ extends ParentQ {
    /**
     * ZH : 测试
     * DSR : 测试
     * VNAME : 测试
     * SXWFSYID : 2b3c2f87a36542aea4df5cce21f5eb7f,872b0cf3979a4d0eb31a7baeeef6b713
     * JGDZ : 测试
     * XZJGSJ : 1539156963
     * QSRQZ : 测试
     * QSSJ : 1539156963
     * JGDH : 测试
     * SXWFSYBC : 测试
     * ZID : 123123123
     */
    private String ID;
    //京交法()字
    private String ZH;
    //当事人
    private String DSR;
    //车号
    private String VNAME;
    //涉嫌违法事由id
    private String SXWFSYID;
    //机关地址
    private String JGDZ;
    //行政机关时间
    private String XZJGSJ;
    //签字 先不做
    private String QSRQZ;
    //签收人签字时间
    private String QSSJ;
    //机关电话
    private String JGDH;
    //涉嫌违法事由补充
    private String SXWFSYBC;

    private String ZID;

    private String STATUS;

    private String SDSJ;

    private String ZFRY1;
    private String ZFZH1;
    private String ZFRY2;
    private String ZFZH2;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }

    public String getDSR() {
        return DSR;
    }

    public void setDSR(String DSR) {
        this.DSR = DSR;
    }

    public String getVNAME() {
        return VNAME;
    }

    public void setVNAME(String VNAME) {
        this.VNAME = VNAME;
    }

    public String getSXWFSYID() {
        return SXWFSYID;
    }

    public void setSXWFSYID(String SXWFSYID) {
        this.SXWFSYID = SXWFSYID;
    }

    public String getJGDZ() {
        return JGDZ;
    }

    public void setJGDZ(String JGDZ) {
        this.JGDZ = JGDZ;
    }

    public String getXZJGSJ() {
        return XZJGSJ;
    }

    public void setXZJGSJ(String XZJGSJ) {
        this.XZJGSJ = XZJGSJ;
    }

    public String getQSRQZ() {
        return QSRQZ;
    }

    public void setQSRQZ(String QSRQZ) {
        this.QSRQZ = QSRQZ;
    }

    public String getQSSJ() {
        return QSSJ;
    }

    public void setQSSJ(String QSSJ) {
        this.QSSJ = QSSJ;
    }

    public String getJGDH() {
        return JGDH;
    }

    public void setJGDH(String JGDH) {
        this.JGDH = JGDH;
    }

    public String getSXWFSYBC() {
        return SXWFSYBC;
    }

    public void setSXWFSYBC(String SXWFSYBC) {
        this.SXWFSYBC = SXWFSYBC;
    }

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSDSJ() {
        return SDSJ;
    }

    public void setSDSJ(String SDSJ) {
        this.SDSJ = SDSJ;
    }

    public String getZFRY1() {
        return ZFRY1;
    }

    public void setZFRY1(String ZFRY1) {
        this.ZFRY1 = ZFRY1;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getZFRY2() {
        return ZFRY2;
    }

    public void setZFRY2(String ZFRY2) {
        this.ZFRY2 = ZFRY2;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.ID);
        dest.writeString(this.ZH);
        dest.writeString(this.DSR);
        dest.writeString(this.VNAME);
        dest.writeString(this.SXWFSYID);
        dest.writeString(this.JGDZ);
        dest.writeString(this.XZJGSJ);
        dest.writeString(this.QSRQZ);
        dest.writeString(this.QSSJ);
        dest.writeString(this.JGDH);
        dest.writeString(this.SXWFSYBC);
        dest.writeString(this.ZID);
        dest.writeString(this.STATUS);
        dest.writeString(this.SDSJ);
        dest.writeString(this.ZFRY1);
        dest.writeString(this.ZFZH1);
        dest.writeString(this.ZFRY2);
        dest.writeString(this.ZFZH2);
    }

    public TalkNoticeQ() {
    }

    protected TalkNoticeQ(Parcel in) {
        super(in);
        this.ID = in.readString();
        this.ZH = in.readString();
        this.DSR = in.readString();
        this.VNAME = in.readString();
        this.SXWFSYID = in.readString();
        this.JGDZ = in.readString();
        this.XZJGSJ = in.readString();
        this.QSRQZ = in.readString();
        this.QSSJ = in.readString();
        this.JGDH = in.readString();
        this.SXWFSYBC = in.readString();
        this.ZID = in.readString();
        this.STATUS = in.readString();
        this.SDSJ = in.readString();
        this.ZFRY1 = in.readString();
        this.ZFZH1 = in.readString();
        this.ZFRY2 = in.readString();
        this.ZFZH2 = in.readString();
    }

    public static final Creator<TalkNoticeQ> CREATOR = new Creator<TalkNoticeQ>() {
        @Override
        public TalkNoticeQ createFromParcel(Parcel source) {
            return new TalkNoticeQ(source);
        }

        @Override
        public TalkNoticeQ[] newArray(int size) {
            return new TalkNoticeQ[size];
        }
    };
}
