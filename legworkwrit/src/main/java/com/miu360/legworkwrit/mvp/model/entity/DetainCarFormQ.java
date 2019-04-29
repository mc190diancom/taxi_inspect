package com.miu360.legworkwrit.mvp.model.entity;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 扣押车辆交接单请求参数
 */
public class DetainCarFormQ extends ParentQ {
    /**
     * ZH : 测试
     * ZID : 3222253
     * CZXM : 测试
     * CPH : 测试
     * CYS : 测试
     * CX : 1539156963
     * CPXH : 测试
     * CJH : 测试
     * DD : 1
     * JJQ : 1
     * KCP : 1
     * JZJ : 1
     * PJ : 1
     * SRWP : 测试
     * YJTL : 0000000
     * KCSJ : 1539156963
     * YSSJ : 1539156963
     * KCDD :
     * CFTCC :
     * ZFRY1 : 测试
     * ZFRY2 : 测试
     * YSY1 : 测试
     * YSY2 : 测试
     * ZFDWQZSJ : 1539156963
     * CGLS : 测试
     */
    @SerializedName(value = "WSID", alternate = {"ID"})
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ZH;
    private String ZID;
    private String CZXM;
    private String CPH;
    private String CYS;
    private String CX;
    private String CPXH;
    private String CJH;
    private String DD;
    private String JJQ;
    private String KCP;
    private String JZJ;
    private String PJ;
    private String SRWP;
    private String YJTL;
    private String KCSJ;
    private String YSSJ;
    private String KCDD;
    private String CFTCC;
    private String ZFRY1;
    private String ZFRY2;
    private String YSY1;
    private String YSY2;
    private String ZFDWQZSJ;
    private String CGLS;
    private String TPID;
    private String ZPSM;

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

    public String getCZXM() {
        return CZXM;
    }

    public void setCZXM(String CZXM) {
        this.CZXM = CZXM;
    }

    public String getCPH() {
        return CPH;
    }

    public void setCPH(String CPH) {
        this.CPH = CPH;
    }

    public String getCYS() {
        return CYS;
    }

    public void setCYS(String CYS) {
        this.CYS = CYS;
    }

    public String getCX() {
        return CX;
    }

    public void setCX(String CX) {
        this.CX = CX;
    }

    public String getCPXH() {
        return CPXH;
    }

    public void setCPXH(String CPXH) {
        this.CPXH = CPXH;
    }

    public String getCJH() {
        return CJH;
    }

    public void setCJH(String CJH) {
        this.CJH = CJH;
    }

    public String getDD() {
        return DD;
    }

    public void setDD(String DD) {
        this.DD = DD;
    }

    public String getJJQ() {
        return JJQ;
    }

    public void setJJQ(String JJQ) {
        this.JJQ = JJQ;
    }

    public String getKCP() {
        return KCP;
    }

    public void setKCP(String KCP) {
        this.KCP = KCP;
    }

    public String getJZJ() {
        return JZJ;
    }

    public void setJZJ(String JZJ) {
        this.JZJ = JZJ;
    }

    public String getPJ() {
        return PJ;
    }

    public void setPJ(String PJ) {
        this.PJ = PJ;
    }

    public String getSRWP() {
        return SRWP;
    }

    public void setSRWP(String SRWP) {
        this.SRWP = SRWP;
    }

    public String getYJTL() {
        return YJTL;
    }

    public void setYJTL(String YJTL) {
        this.YJTL = YJTL;
    }

    public String getKCSJ() {
        return KCSJ;
    }

    public void setKCSJ(String KCSJ) {
        this.KCSJ = KCSJ;
    }

    public String getYSSJ() {
        return YSSJ;
    }

    public void setYSSJ(String YSSJ) {
        this.YSSJ = YSSJ;
    }

    public String getKCDD() {
        return KCDD;
    }

    public void setKCDD(String KCDD) {
        this.KCDD = KCDD;
    }

    public String getCFTCC() {
        return CFTCC;
    }

    public void setCFTCC(String CFTCC) {
        this.CFTCC = CFTCC;
    }

    public String getZFRY1() {
        return ZFRY1;
    }

    public void setZFRY1(String ZFRY1) {
        this.ZFRY1 = ZFRY1;
    }

    public String getZFRY2() {
        return ZFRY2;
    }

    public void setZFRY2(String ZFRY2) {
        this.ZFRY2 = ZFRY2;
    }

    public String getYSY1() {
        return YSY1;
    }

    public void setYSY1(String YSY1) {
        this.YSY1 = YSY1;
    }

    public String getYSY2() {
        return YSY2;
    }

    public void setYSY2(String YSY2) {
        this.YSY2 = YSY2;
    }

    public String getZFDWQZSJ() {
        return ZFDWQZSJ;
    }

    public void setZFDWQZSJ(String ZFDWQZSJ) {
        this.ZFDWQZSJ = ZFDWQZSJ;
    }

    public String getCGLS() {
        return CGLS;
    }

    public void setCGLS(String CGLS) {
        this.CGLS = CGLS;
    }

    public String getTPID() {
        return TPID;
    }

    public void setTPID(String TPID) {
        this.TPID = TPID;
    }

    public String getZPSM() {
        return ZPSM;
    }

    public void setZPSM(String ZPSM) {
        this.ZPSM = ZPSM;
    }

    public DetainCarFormQ() {
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
        dest.writeString(this.ZID);
        dest.writeString(this.CZXM);
        dest.writeString(this.CPH);
        dest.writeString(this.CYS);
        dest.writeString(this.CX);
        dest.writeString(this.CPXH);
        dest.writeString(this.CJH);
        dest.writeString(this.DD);
        dest.writeString(this.JJQ);
        dest.writeString(this.KCP);
        dest.writeString(this.JZJ);
        dest.writeString(this.PJ);
        dest.writeString(this.SRWP);
        dest.writeString(this.YJTL);
        dest.writeString(this.KCSJ);
        dest.writeString(this.YSSJ);
        dest.writeString(this.KCDD);
        dest.writeString(this.CFTCC);
        dest.writeString(this.ZFRY1);
        dest.writeString(this.ZFRY2);
        dest.writeString(this.YSY1);
        dest.writeString(this.YSY2);
        dest.writeString(this.ZFDWQZSJ);
        dest.writeString(this.CGLS);
        dest.writeString(this.TPID);
        dest.writeString(this.ZPSM);
    }

    protected DetainCarFormQ(Parcel in) {
        super(in);
        this.ID = in.readString();
        this.ZH = in.readString();
        this.ZID = in.readString();
        this.CZXM = in.readString();
        this.CPH = in.readString();
        this.CYS = in.readString();
        this.CX = in.readString();
        this.CPXH = in.readString();
        this.CJH = in.readString();
        this.DD = in.readString();
        this.JJQ = in.readString();
        this.KCP = in.readString();
        this.JZJ = in.readString();
        this.PJ = in.readString();
        this.SRWP = in.readString();
        this.YJTL = in.readString();
        this.KCSJ = in.readString();
        this.YSSJ = in.readString();
        this.KCDD = in.readString();
        this.CFTCC = in.readString();
        this.ZFRY1 = in.readString();
        this.ZFRY2 = in.readString();
        this.YSY1 = in.readString();
        this.YSY2 = in.readString();
        this.ZFDWQZSJ = in.readString();
        this.CGLS = in.readString();
        this.TPID = in.readString();
        this.ZPSM = in.readString();
    }

    public static final Creator<DetainCarFormQ> CREATOR = new Creator<DetainCarFormQ>() {
        @Override
        public DetainCarFormQ createFromParcel(Parcel source) {
            return new DetainCarFormQ(source);
        }

        @Override
        public DetainCarFormQ[] newArray(int size) {
            return new DetainCarFormQ[size];
        }
    };
}
