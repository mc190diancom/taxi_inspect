package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by Murphy on 2018/10/16.
 * 北京市交通执法总队现场检查笔录
 */
public class LiveCheckRecordQ extends ParentQ {

    /**
     * "JCSJ1": "1539156963",
     * "JCSJ2": "1539156563",
     * "JCQY": "一二三四五六七八九十一二三四五六七八九十",
     * "JCDD": "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十",
     * "BJCR": "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十",
     * "SEX": "男",
     * "TELLPHONE": "18684008179", //
     * "CYZGZ": "18684008179",
     * "SFZH": "测试", //身份证号
     * "VNAME": "BR1107", //车牌号
     * "VCOLOR": "测试", //颜色
     * "VTYPE": "一二三四五六七八九十", //车型
     * "VLPP": "测试测试测试", //品牌
     * "JCNRJL": "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十",
     * //检查内容记录
     * "QZCSSXGZ": "因当事人涉嫌未经许可擅自组织从事网络预约出租汽车客运经营，根据《北京市查处非法客运若干规定》第四条", //?
     * "SFSS": "测试",//?
     * "BJCRQZ": "测试", //被检查人签字？
     * "BJCSJ": "1539156963", //被检查人时间
     * "ZFZH1": "0000000", //执法证号
     * "ZFZH2": "7777777",
     * "ZFRYXM1": "姓名", //执法人员
     * "ZFRYXM2": "姓名",
     * "ZFSJ": "1539156963", //执法时间
     * "ZH": "123123",
     * "ZID": "123123"
     */

    private String ID;
    //检查时间 开始
    private String JCSJ1;
    //检查时间 结束
    private String JCSJ2;
    //检查区域
    private String JCQY;
    //检查地点
    private String JCDD;
    //被检查人
    private String BJCR;
    //性别
    private String SEX;
    //电话
    private String TELLPHONE;
    //从业资格证号
    private String CYZGZ;
    //身份证号
    private String SFZH;
    //车牌号
    private String VNAME;
    //颜色
    private String VCOLOR;
    //车型
    private String VTYPE;
    //品牌
    private String VLPP;
    //检查内容记录
    private String JCNRJL;
    //强制措施事先告知
    private String QZCSSXGZ;

    private String SFSS;
    //签字
    private String BJCRQZ;
    //被检查人时间
    private String BJCSJ;
    //执法证号1
    private String ZFZH1;
    //执法证号2
    private String ZFZH2;
    //执法人姓名1
    private String ZFRYXM1;
    //执法人姓名2
    private String ZFRYXM2;
    //执法时间
    private String ZFSJ;
    //京交法（123123）字
    private String ZH;

    private String ZID;
    //违法行为
    private String WFXW;
    //违法详情
    private String WFQX;
    //地址
    private String DD;

    private String WFXWID;

    public String getJCSJ1() {
        return JCSJ1;
    }

    public void setJCSJ1(String JCSJ1) {
        this.JCSJ1 = JCSJ1;
    }

    public String getJCSJ2() {
        return JCSJ2;
    }

    public void setJCSJ2(String JCSJ2) {
        this.JCSJ2 = JCSJ2;
    }

    public String getJCQY() {
        return JCQY;
    }

    public void setJCQY(String JCQY) {
        this.JCQY = JCQY;
    }

    public String getJCDD() {
        return JCDD;
    }

    public void setJCDD(String JCDD) {
        this.JCDD = JCDD;
    }

    public String getBJCR() {
        return BJCR;
    }

    public void setBJCR(String BJCR) {
        this.BJCR = BJCR;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getTELLPHONE() {
        return TELLPHONE;
    }

    public void setTELLPHONE(String TELLPHONE) {
        this.TELLPHONE = TELLPHONE;
    }

    public String getCYZGZ() {
        return CYZGZ;
    }

    public void setCYZGZ(String CYZGZ) {
        this.CYZGZ = CYZGZ;
    }

    public String getSFZH() {
        return SFZH;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public String getVNAME() {
        return VNAME;
    }

    public void setVNAME(String VNAME) {
        this.VNAME = VNAME;
    }

    public String getVCOLOR() {
        return VCOLOR;
    }

    public void setVCOLOR(String VCOLOR) {
        this.VCOLOR = VCOLOR;
    }

    public String getVTYPE() {
        return VTYPE;
    }

    public void setVTYPE(String VTYPE) {
        this.VTYPE = VTYPE;
    }

    public String getVLPP() {
        return VLPP;
    }

    public void setVLPP(String VLPP) {
        this.VLPP = VLPP;
    }

    public String getJCNRJL() {
        return JCNRJL;
    }

    public void setJCNRJL(String JCNRJL) {
        this.JCNRJL = JCNRJL;
    }

    public String getQZCSSXGZ() {
        return QZCSSXGZ;
    }

    public void setQZCSSXGZ(String QZCSSXGZ) {
        this.QZCSSXGZ = QZCSSXGZ;
    }

    public String getSFSS() {
        return SFSS;
    }

    public void setSFSS(String SFSS) {
        this.SFSS = SFSS;
    }

    public String getBJCRQZ() {
        return BJCRQZ;
    }

    public void setBJCRQZ(String BJCRQZ) {
        this.BJCRQZ = BJCRQZ;
    }

    public String getBJCSJ() {
        return BJCSJ;
    }

    public void setBJCSJ(String BJCSJ) {
        this.BJCSJ = BJCSJ;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getZFRYXM1() {
        return ZFRYXM1;
    }

    public void setZFRYXM1(String ZFRYXM1) {
        this.ZFRYXM1 = ZFRYXM1;
    }

    public String getZFRYXM2() {
        return ZFRYXM2;
    }

    public void setZFRYXM2(String ZFRYXM2) {
        this.ZFRYXM2 = ZFRYXM2;
    }

    public String getZFSJ() {
        return ZFSJ;
    }

    public void setZFSJ(String ZFSJ) {
        this.ZFSJ = ZFSJ;
    }

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }

    public String getZID() {
        return ZID;
    }

    public void setZID(String ZID) {
        this.ZID = ZID;
    }

    public String getWFXW() {
        return WFXW;
    }

    public void setWFXW(String WFXW) {
        this.WFXW = WFXW;
    }

    public String getWFQX() {
        return WFQX;
    }

    public void setWFQX(String WFQX) {
        this.WFQX = WFQX;
    }

    public String getDD() {
        return DD;
    }

    public void setDD(String DD) {
        this.DD = DD;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWFXWID() {
        return WFXWID;
    }

    public void setWFXWID(String WFXWID) {
        this.WFXWID = WFXWID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.ID);
        dest.writeString(this.JCSJ1);
        dest.writeString(this.JCSJ2);
        dest.writeString(this.JCQY);
        dest.writeString(this.JCDD);
        dest.writeString(this.BJCR);
        dest.writeString(this.SEX);
        dest.writeString(this.TELLPHONE);
        dest.writeString(this.CYZGZ);
        dest.writeString(this.SFZH);
        dest.writeString(this.VNAME);
        dest.writeString(this.VCOLOR);
        dest.writeString(this.VTYPE);
        dest.writeString(this.VLPP);
        dest.writeString(this.JCNRJL);
        dest.writeString(this.QZCSSXGZ);
        dest.writeString(this.SFSS);
        dest.writeString(this.BJCRQZ);
        dest.writeString(this.BJCSJ);
        dest.writeString(this.ZFZH1);
        dest.writeString(this.ZFZH2);
        dest.writeString(this.ZFRYXM1);
        dest.writeString(this.ZFRYXM2);
        dest.writeString(this.ZFSJ);
        dest.writeString(this.ZH);
        dest.writeString(this.ZID);
        dest.writeString(this.WFXW);
        dest.writeString(this.WFQX);
        dest.writeString(this.DD);
        dest.writeString(this.WFXWID);
    }

    public LiveCheckRecordQ() {
    }

    protected LiveCheckRecordQ(Parcel in) {
        super(in);
        this.ID = in.readString();
        this.JCSJ1 = in.readString();
        this.JCSJ2 = in.readString();
        this.JCQY = in.readString();
        this.JCDD = in.readString();
        this.BJCR = in.readString();
        this.SEX = in.readString();
        this.TELLPHONE = in.readString();
        this.CYZGZ = in.readString();
        this.SFZH = in.readString();
        this.VNAME = in.readString();
        this.VCOLOR = in.readString();
        this.VTYPE = in.readString();
        this.VLPP = in.readString();
        this.JCNRJL = in.readString();
        this.QZCSSXGZ = in.readString();
        this.SFSS = in.readString();
        this.BJCRQZ = in.readString();
        this.BJCSJ = in.readString();
        this.ZFZH1 = in.readString();
        this.ZFZH2 = in.readString();
        this.ZFRYXM1 = in.readString();
        this.ZFRYXM2 = in.readString();
        this.ZFSJ = in.readString();
        this.ZH = in.readString();
        this.ZID = in.readString();
        this.WFXW = in.readString();
        this.WFQX = in.readString();
        this.DD = in.readString();
        this.WFXWID = in.readString();
    }

    public static final Creator<LiveCheckRecordQ> CREATOR = new Creator<LiveCheckRecordQ>() {
        @Override
        public LiveCheckRecordQ createFromParcel(Parcel source) {
            return new LiveCheckRecordQ(source);
        }

        @Override
        public LiveCheckRecordQ[] newArray(int size) {
            return new LiveCheckRecordQ[size];
        }
    };
}
