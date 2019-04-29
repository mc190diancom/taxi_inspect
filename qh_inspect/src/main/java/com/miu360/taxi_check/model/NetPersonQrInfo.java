package com.miu360.taxi_check.model;

import java.io.Serializable;

public class NetPersonQrInfo implements Serializable{
	private String PCODE;
	private String AGE;
	private String ZCYXQ;
	private String NM;
	private String XB;
	private String GJ;
	private String CELLPHONE;
	private String CHSHRQ;
	private String CHLJSHZHRQ;
	private String PERSONTYPE;
	private String QYPT;
	private String GRZHP;
	private String QYPTPHONE;
	private String CPH;
	private String FWM;
	private String PICTIME;
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getAGE() {
		return AGE;
	}
	public void setAGE(String aGE) {
		AGE = aGE;
	}
	public String getZCYXQ() {
		return ZCYXQ;
	}
	public void setZCYXQ(String zCYXQ) {
		ZCYXQ = zCYXQ;
	}
	public String getNM() {
		return NM;
	}
	public void setNM(String nM) {
		NM = nM;
	}
	public String getXB() {
		return XB;
	}
	public void setXB(String xB) {
		XB = xB;
	}
	public String getGJ() {
		return GJ;
	}
	public void setGJ(String gJ) {
		GJ = gJ;
	}
	public String getCELLPHONE() {
		return CELLPHONE;
	}
	public void setCELLPHONE(String cELLPHONE) {
		CELLPHONE = cELLPHONE;
	}
	public String getCHSHRQ() {
		return CHSHRQ;
	}
	public void setCHSHRQ(String cHSHRQ) {
		CHSHRQ = cHSHRQ;
	}
	public String getCHLJSHZHRQ() {
		return CHLJSHZHRQ;
	}
	public void setCHLJSHZHRQ(String cHLJSHZHRQ) {
		CHLJSHZHRQ = cHLJSHZHRQ;
	}
	public String getPERSONTYPE() {
		return PERSONTYPE;
	}
	public void setPERSONTYPE(String pERSONTYPE) {
		PERSONTYPE = pERSONTYPE;
	}
	public String getQYPT() {
		return QYPT;
	}
	public void setQYPT(String qYPT) {
		QYPT = qYPT;
	}
	public String getGRZHP() {
		return GRZHP;
	}
	public void setGRZHP(String gRZHP) {
		GRZHP = gRZHP;
	}
	public String getQYPTPHONE() {
		return QYPTPHONE;
	}
	public void setQYPTPHONE(String qYPTPHONE) {
		QYPTPHONE = qYPTPHONE;
	}
	public String getCPH() {
		return CPH;
	}
	public void setCPH(String cPH) {
		CPH = cPH;
	}
	public String getFWM() {
		return FWM;
	}
	public void setFWM(String fWM) {
		FWM = fWM;
	}
	public String getPICTIME() {
		return PICTIME;
	}
	public void setPICTIME(String pICTIME) {
		PICTIME = pICTIME;
	}
	@Override
	public String toString() {
		return "NetPersonQrInfo [PCODE=" + PCODE + ", AGE=" + AGE + ", ZCYXQ="
				+ ZCYXQ + ", NM=" + NM + ", XB=" + XB + ", GJ=" + GJ
				+ ", CELLPHONE=" + CELLPHONE + ", CHSHRQ=" + CHSHRQ
				+ ", CHLJSHZHRQ=" + CHLJSHZHRQ + ", PERSONTYPE=" + PERSONTYPE
				+ ", QYPT=" + QYPT + ", GRZHP=" + GRZHP + ", QYPTPHONE="
				+ QYPTPHONE + ", CPH=" + CPH + ", FWM=" + FWM + ", PICTIME="
				+ PICTIME + "]";
	}
	
	
}
