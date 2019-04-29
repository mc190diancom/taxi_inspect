package com.miu360.taxi_check.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoateInfo {
	@JsonProperty
	private String NAME;
	@JsonProperty
	private String ID;
	@JsonProperty
	private String MARK;


	@JsonIgnore
	public String getID() {
		return ID;
	}

	@JsonIgnore
	public void setID(String iD) {
		ID = iD;
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
	public String getMARK() {
		return MARK;
	}

	@JsonIgnore
	public void setMARK(String mARK) {
		MARK = mARK;
	}

	
}
