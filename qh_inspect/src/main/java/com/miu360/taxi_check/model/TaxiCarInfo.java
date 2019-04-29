package com.miu360.taxi_check.model;

import java.util.List;
public class TaxiCarInfo {
	private int total;

	private String adk;

	private List<Ssp> ssp ;

	private List<Info> info ;

	public String getAdk() {
		return adk;
	}
	public void setAdk(String adk) {
		this.adk = adk;
	}
	public void setTotal(int total){
		this.total = total;
	}
	public int getTotal(){
		return this.total;
	}
	public void setSsp(List<Ssp> ssp){
		this.ssp = ssp;
	}
	public List<Ssp> getSsp(){
		return this.ssp;
	}
	public void setInfo(List<Info> info){
		this.info = info;
	}
	public List<Info> getInfo(){
		return this.info;
	}

	@Override
	public String toString() {
		return "TaxiCarInfo [total=" + total + ", ssp=" + ssp + ", info="
				+ info + "]";
	}

	public class Ssp {
		private String JCBH;

		private String JCSJ;

		private String RBRQ;

		private String LBMC;

		private String LBMS;

		private String WZSY;

		private String DSR;

		public String getDSR() {
			return DSR;
		}

		public void setDSR(String dSR) {
			DSR = dSR;
		}

		public String getJCBH() {
			return JCBH;
		}

		public void setJCBH(String jCBH) {
			JCBH = jCBH;
		}

		public String getJCSJ() {
			return JCSJ;
		}

		public void setJCSJ(String jCSJ) {
			JCSJ = jCSJ;
		}

		public String getRBRQ() {
			return RBRQ;
		}

		public void setRBRQ(String rBRQ) {
			RBRQ = rBRQ;
		}

		public String getLBMC() {
			return LBMC;
		}

		public void setLBMC(String lBMC) {
			LBMC = lBMC;
		}

		public String getLBMS() {
			return LBMS;
		}

		public void setLBMS(String lBMS) {
			LBMS = lBMS;
		}

		public String getWZSY() {
			return WZSY;
		}

		public void setWZSY(String wZSY) {
			WZSY = wZSY;
		}

		@Override
		public String toString() {
			return "Ssp [JCBH=" + JCBH + ", JCSJ=" + JCSJ + ", RBRQ=" + RBRQ + ", LBMC=" + LBMC + ", LBMS=" + LBMS
					+ ", WZSY=" + WZSY + "]";
		}


	}
	public class Info {
		private String PLATE_NO;//车牌

		private String CERTIFICATE_NO;//营运证号

		private String COLOR;//颜色

		private String BRAND;//厂牌

		private String VIN;//车架

		private String XH;//车辆型号

		private String MODEL;//车辆类型

		private String NAME;//所属公司



		public String getCERTIFICATE_NO() {
			return CERTIFICATE_NO;
		}
		public void setCERTIFICATE_NO(String cERTIFICATE_NO) {
			CERTIFICATE_NO = cERTIFICATE_NO;
		}
		public String getBRAND() {
			return BRAND;
		}
		public void setBRAND(String bRAND) {
			BRAND = bRAND;
		}
		public String getVIN() {
			return VIN;
		}
		public void setVIN(String vIN) {
			VIN = vIN;
		}
		public void setPLATE_NO(String PLATE_NO){
			this.PLATE_NO = PLATE_NO;
		}
		public String getPLATE_NO(){
			return this.PLATE_NO;
		}
		public void setCOLOR(String COLOR){
			this.COLOR = COLOR;
		}
		public String getCOLOR(){
			return this.COLOR;
		}
		public void setXH(String XH){
			this.XH = XH;
		}
		public String getXH(){
			return this.XH;
		}
		public void setMODEL(String MODEL){
			this.MODEL = MODEL;
		}
		public String getMODEL(){
			return this.MODEL;
		}
		public void setNAME(String NAME){
			this.NAME = NAME;
		}
		@Override
		public String toString() {
			return "Info [PLATE_NO=" + PLATE_NO + ", COLOR=" + COLOR + ", XH="
					+ XH + ", MODEL=" + MODEL + ", NAME=" + NAME + "]";
		}
		public String getNAME(){
			return this.NAME;
		}
	}
}

