package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 谈话通知书请求参数
 */
public class TalkNoticePreview extends ParentQ {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
    }

    public TalkNoticePreview() {
    }

    protected TalkNoticePreview(Parcel in) {
        super(in);
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
    }

    public static final Creator<TalkNoticePreview> CREATOR = new Creator<TalkNoticePreview>() {
        @Override
        public TalkNoticePreview createFromParcel(Parcel source) {
            return new TalkNoticePreview(source);
        }

        @Override
        public TalkNoticePreview[] newArray(int size) {
            return new TalkNoticePreview[size];
        }
    };
}
