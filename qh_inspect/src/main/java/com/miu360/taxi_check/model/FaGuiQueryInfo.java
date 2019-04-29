package com.miu360.taxi_check.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FaGuiQueryInfo extends Page {
	@JsonProperty
	private String HYLB;
	@JsonProperty
	private String JCLB;
	@JsonProperty
	private String CODE;
	@JsonProperty
	private String JCNR;
	@JsonProperty
	private String bjczt;

	public String getBjczt() {
		return bjczt;
	}

	public void setBjczt(String bjczt) {
		this.bjczt = bjczt;
	}

	@JsonIgnore
	public String getHYLB() {
		return HYLB;
	}

	@JsonIgnore
	public void setHYLB(String hYLB) {
		HYLB = hYLB;
	}

	@JsonIgnore
	public String getJCLB() {
		return JCLB;
	}

	@JsonIgnore
	public void setJCLB(String jCLB) {
		JCLB = jCLB;
	}

	@JsonIgnore
	public String getCODE() {
		return CODE;
	}

	@JsonIgnore
	public void setCODE(String cODE) {
		CODE = cODE;
	}

	@JsonIgnore
	public String getJCNR() {
		return JCNR;
	}

	@JsonIgnore
	public void setJCNR(String jCNR) {
		JCNR = jCNR;
	}

}
