package com.miu360.taxi_check.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxiWfInfo {
	private int total;
	private List<Rows> rows;
	private List<info> info;
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal() {
		return total;
	}

	public void setRows(List<Rows> rows) {
		this.rows = rows;
	}
	public List<Rows> getRows() {
		return rows;
	}

	public void setInfo(List<info> rows) {
		this.info = info;
	}
	public List<info> getInfo() {
		return info;
	}

	public class Rows {
		private String CLPH;
		private String YS;
		private String CX;
		private String JCSJ;
		private String JCDD;
		private String ZFDDMC;
		private String AJZT;
		private String JASJ;
		private String WZSY;
		private String LBMC;
		private String HYLB;
		public String getCLPH() {
			return CLPH;
		}
		public void setCLPH(String cLPH) {
			CLPH = cLPH;
		}

		public String getYS() {
			return YS;
		}
		public void setYS(String yS) {
			YS = yS;
		}
		public String getCX() {
			return CX;
		}
		public void setCX(String cX) {
			CX = cX;
		}

		public String getJCSJ() {
			return JCSJ;
		}
		public void setJCSJ(String jCSJ) {
			JCSJ = jCSJ;
		}
		public String getJCDD() {
			return JCDD;
		}
		public void setJCDD(String jCDD) {
			JCDD = jCDD;
		}
		public String getZFDDMC() {
			return ZFDDMC;
		}
		public void setZFDDMC(String zFDDMC) {
			ZFDDMC = zFDDMC;
		}
		public String getAJZT() {
			return AJZT;
		}
		public void setAJZT(String aJZT) {
			AJZT = aJZT;
		}
		public String getJASJ() {
			return JASJ;
		}
		public void setJASJ(String jASJ) {
			JASJ = jASJ;
		}
		public String getWZSY() {
			return WZSY;
		}
		public void setWZSY(String wZSY) {
			WZSY = wZSY;
		}
		public String getLBMC() {
			return LBMC;
		}
		public void setLBMC(String lBMC) {
			LBMC = lBMC;
		}
		public String getHYLB() {
			return HYLB;
		}
		public void setHYLB(String hYLB) {
			HYLB = hYLB;
		}


	}

	public class info{
		private String NAME;//名字
		private String ID_CARD;//身份证号
		private String CORPNAME;//所属公司
		private String LICENSE_NO;//准驾证号
		private String PHONE;//手机号
		private String FAMILY_ADDR;//家庭住址


		public String getPHONE() {
			return PHONE;
		}
		public void setPHONE(String pHONE) {
			PHONE = pHONE;
		}
		public String getFAMILY_ADDR() {
			return FAMILY_ADDR;
		}
		public void setFAMILY_ADDR(String fAMILY_ADDR) {
			FAMILY_ADDR = fAMILY_ADDR;
		}
		public String getNAME() {
			return NAME;
		}
		public void setNAME(String nAME) {
			NAME = nAME;
		}
		public String getID_CARD() {
			return ID_CARD;
		}
		public void setID_CARD(String iD_CARD) {
			ID_CARD = iD_CARD;
		}
		public String getCORPNAME() {
			return CORPNAME;
		}
		public void setCORPNAME(String cORPNAME) {
			CORPNAME = cORPNAME;
		}
		public String getLICENSE_NO() {
			return LICENSE_NO;
		}
		public void setLICENSE_NO(String lICENSE_NO) {
			LICENSE_NO = lICENSE_NO;
		}
		@Override
		public String toString() {
			return "info [NAME=" + NAME + ", ID_CARD=" + ID_CARD
					+ ", CORPNAME=" + CORPNAME + ", LICENSE_NO=" + LICENSE_NO
					+ "]";
		}



	}
}
