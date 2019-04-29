package com.miu30.common.ui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class queryZFRYByDWMC {
	@JsonProperty
	private String ZFDWMC;
	
	@JsonProperty
	private String NAME;
	@JsonProperty
	private String ZFZH;

	@JsonIgnore
	public String getZFZH() {
		return ZFZH;
	}

	@JsonIgnore
	public void setZFZH(String zFZH) {
		ZFZH = zFZH;
	}

	@JsonIgnore
	public String getNAME() {
		return NAME;
	}

	@JsonIgnore
	public void setNAME(String nAME) {
		NAME = nAME;
	}

	@JsonIgnore
	public String getZFDWMC() {
		return ZFDWMC;
	}

	@JsonIgnore
	public void setZFDWMC(String zFDWMC) {
		ZFDWMC = zFDWMC;
	}

	@Override
	public String toString() {
		return "queryZFRYByDWMC{" +
				"ZFDWMC='" + ZFDWMC + '\'' +
				", NAME='" + NAME + '\'' +
				", ZFZH='" + ZFZH + '\'' +
				'}';
	}
}
