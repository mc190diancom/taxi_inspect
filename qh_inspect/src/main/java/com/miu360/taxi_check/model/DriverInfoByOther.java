package com.miu360.taxi_check.model;

public class DriverInfoByOther {
     private String NAME;
     
     private String ID_CARD;
     
     private String CERTIFICATE_NO;
     
     private String SERVICE_UNIT;
     
     private String VALUE;
     
     private int RN;
     
     private int startIndex;
     
     private int endIndex;
     
     private int totalNum;

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

	public String getCERTIFICATE_NO() {
		return CERTIFICATE_NO;
	}

	public void setCERTIFICATE_NO(String cERTIFICATE_NO) {
		CERTIFICATE_NO = cERTIFICATE_NO;
	}

	public String getSERVICE_UNIT() {
		return SERVICE_UNIT;
	}

	public void setSERVICE_UNIT(String sERVICE_UNIT) {
		SERVICE_UNIT = sERVICE_UNIT;
	}

	public String getVALUE() {
		return VALUE;
	}

	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}

	public int getRN() {
		return RN;
	}

	public void setRN(int rN) {
		RN = rN;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	@Override
	public String toString() {
		return "DriverInfoByOther [NAME=" + NAME + ", ID_CARD=" + ID_CARD
				+ ", CERTIFICATE_NO=" + CERTIFICATE_NO + ", SERVICE_UNIT="
				+ SERVICE_UNIT + ", VALUE=" + VALUE + ", RN=" + RN
				+ ", startIndex=" + startIndex + ", endIndex=" + endIndex
				+ ", totalNum=" + totalNum + "]";
	}
     
     
}
