package com.miu30.common.ui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheZuRequestModel {
	@JsonProperty
	private String SSCZ;
	@JsonProperty
	private String ZFDWMC;
	@JsonProperty
	private String SSZD;

	@JsonIgnore
	public String getSSCZ() {
		return SSCZ;
	}

	@JsonIgnore
	public void setSSCZ(String sSCZ) {
		SSCZ = sSCZ;
	}

	public String getZFDWMC() {
		return ZFDWMC;
	}

	@JsonIgnore
	public void setZFDWMC(String zFDWMC) {
		ZFDWMC = zFDWMC;
	}

	@JsonIgnore
	public String getSSZD() {
		return SSZD;
	}

	@JsonIgnore
	public void setSSZD(String sSZD) {
		SSZD = sSZD;
	}

}
