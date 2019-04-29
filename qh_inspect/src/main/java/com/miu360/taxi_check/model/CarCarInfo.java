package com.miu360.taxi_check.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CarCarInfo {
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

}
