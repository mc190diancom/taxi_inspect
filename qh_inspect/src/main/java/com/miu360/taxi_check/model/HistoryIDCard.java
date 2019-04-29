package com.miu360.taxi_check.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryIDCard {
	@JsonProperty
	private String IMAGE_PASH;
	@JsonProperty
	private String DRIVERNAME;
	@JsonProperty
	private String SEX;
	@JsonProperty
	private String JDKH;
	@JsonProperty
	private String COMPNAME;
	@JsonProperty
	private String PHONE;
	@JsonProperty
	private String FWM;
	@JsonProperty
	private String SMSJ;
	@JsonProperty
	private String ZFZH;
	
	@JsonProperty
	private String DRIV_NAME;
	@JsonProperty
	private String DRIV_SEX;
	@JsonProperty
	private String DRIV_AGE;
	@JsonProperty
	private String DRIV_VALIDITY;
	@JsonProperty
	private String DRIV_TYPE;
	@JsonProperty
	private String DRIV_VNAME;
	@JsonProperty
	private String DRIV_FWM;
	@JsonProperty
	private String VEH_VNAME;
	@JsonProperty
	private String VEH_CORPNAME;
	@JsonProperty
	private String VEH_TYPE;
	@JsonProperty
	private String VEH_COLOR;
	@JsonProperty
	private String VEH_PTNAME;
	@JsonProperty
	private String VEH_VALIDITY;
	@JsonProperty
	private String VEH_CJH;
	@JsonProperty
	private String VEH_FDJH;
	@JsonProperty
	private String VEH_STATUS;
	@JsonProperty
	private String VEH_FWM;
	@JsonProperty
	private String FLAG;
	@JsonIgnore
	public String getZFZH() {
		return ZFZH;
	}

	@JsonIgnore
	public void setZFZH(String zFZH) {
		ZFZH = zFZH;
	}

	@JsonIgnore
	public String getIMAGE_PASH() {
		return IMAGE_PASH;
	}

	@JsonIgnore
	public void setIMAGE_PASH(String iMAGE_PASH) {
		IMAGE_PASH = iMAGE_PASH;
	}

	@JsonIgnore
	public String getDRIVERNAME() {
		return DRIVERNAME;
	}

	@JsonIgnore
	public void setDRIVERNAME(String dRIVERNAME) {
		DRIVERNAME = dRIVERNAME;
	}

	@JsonIgnore
	public String getSEX() {
		return SEX;
	}

	@JsonIgnore
	public void setSEX(String sEX) {
		SEX = sEX;
	}

	@JsonIgnore
	public String getJDKH() {
		return JDKH;
	}

	@JsonIgnore
	public void setJDKH(String jDKH) {
		JDKH = jDKH;
	}

	@JsonIgnore
	public String getCOMPNAME() {
		return COMPNAME;
	}

	@JsonIgnore
	public void setCOMPNAME(String cOMPNAME) {
		COMPNAME = cOMPNAME;
	}

	@JsonIgnore
	public String getPHONE() {
		return PHONE;
	}

	@JsonIgnore
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}

	@JsonIgnore
	public String getFWM() {
		return FWM;
	}

	@JsonIgnore
	public void setFWM(String fWM) {
		FWM = fWM;
	}

	@JsonIgnore
	public String getSMSJ() {
		return SMSJ;
	}

	@JsonIgnore
	public void setSMSJ(String sMSJ) {
		SMSJ = sMSJ;
	}

	@JsonIgnore
	public String getDRIV_NAME() {
		return DRIV_NAME;
	}
	@JsonIgnore
	public void setDRIV_NAME(String dRIV_NAME) {
		DRIV_NAME = dRIV_NAME;
	}
	@JsonIgnore
	public String getDRIV_SEX() {
		return DRIV_SEX;
	}
	@JsonIgnore
	public void setDRIV_SEX(String dRIV_SEX) {
		DRIV_SEX = dRIV_SEX;
	}
	@JsonIgnore
	public String getDRIV_AGE() {
		return DRIV_AGE;
	}
	@JsonIgnore
	public void setDRIV_AGE(String dRIV_AGE) {
		DRIV_AGE = dRIV_AGE;
	}
	@JsonIgnore
	public String getDRIV_VALIDITY() {
		return DRIV_VALIDITY;
	}
	@JsonIgnore
	public void setDRIV_VALIDITY(String dRIV_VALIDITY) {
		DRIV_VALIDITY = dRIV_VALIDITY;
	}
	@JsonIgnore
	public String getDRIV_TYPE() {
		return DRIV_TYPE;
	}
	@JsonIgnore
	public void setDRIV_TYPE(String dRIV_TYPE) {
		DRIV_TYPE = dRIV_TYPE;
	}
	@JsonIgnore
	public String getDRIV_VNAME() {
		return DRIV_VNAME;
	}
	@JsonIgnore
	public void setDRIV_VNAME(String dRIV_VNAME) {
		DRIV_VNAME = dRIV_VNAME;
	}
	@JsonIgnore
	public String getDRIV_FWM() {
		return DRIV_FWM;
	}
	@JsonIgnore
	public void setDRIV_FWM(String dRIV_FWM) {
		DRIV_FWM = dRIV_FWM;
	}
	@JsonIgnore
	public String getVEH_VNAME() {
		return VEH_VNAME;
	}
	@JsonIgnore
	public void setVEH_VNAME(String vEH_VNAME) {
		VEH_VNAME = vEH_VNAME;
	}
	@JsonIgnore
	public String getVEH_CORPNAME() {
		return VEH_CORPNAME;
	}
	@JsonIgnore
	public void setVEH_CORPNAME(String vEH_CORPNAME) {
		VEH_CORPNAME = vEH_CORPNAME;
	}
	@JsonIgnore
	public String getVEH_TYPE() {
		return VEH_TYPE;
	}
	@JsonIgnore
	public void setVEH_TYPE(String vEH_TYPE) {
		VEH_TYPE = vEH_TYPE;
	}
	@JsonIgnore
	public String getVEH_COLOR() {
		return VEH_COLOR;
	}
	@JsonIgnore
	public void setVEH_COLOR(String vEH_COLOR) {
		VEH_COLOR = vEH_COLOR;
	}
	@JsonIgnore
	public String getVEH_PTNAME() {
		return VEH_PTNAME;
	}
	@JsonIgnore
	public void setVEH_PTNAME(String vEH_PTNAME) {
		VEH_PTNAME = vEH_PTNAME;
	}
	@JsonIgnore
	public String getVEH_VALIDITY() {
		return VEH_VALIDITY;
	}
	@JsonIgnore
	public void setVEH_VALIDITY(String vEH_VALIDITY) {
		VEH_VALIDITY = vEH_VALIDITY;
	}
	@JsonIgnore
	public String getVEH_CJH() {
		return VEH_CJH;
	}
	@JsonIgnore
	public void setVEH_CJH(String vEH_CJH) {
		VEH_CJH = vEH_CJH;
	}
	@JsonIgnore
	public String getVEH_FDJH() {
		return VEH_FDJH;
	}
	@JsonIgnore
	public void setVEH_FDJH(String vEH_FDJH) {
		VEH_FDJH = vEH_FDJH;
	}
	@JsonIgnore
	public String getVEH_STATUS() {
		return VEH_STATUS;
	}
	@JsonIgnore
	public void setVEH_STATUS(String vEH_STATUS) {
		VEH_STATUS = vEH_STATUS;
	}
	@JsonIgnore
	public String getVEH_FWM() {
		return VEH_FWM;
	}
	@JsonIgnore
	public void setVEH_FWM(String vEH_FWM) {
		VEH_FWM = vEH_FWM;
	}
	@JsonIgnore
	public String getFLAG() {
		return FLAG;
	}
	@JsonIgnore
	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

	
}
