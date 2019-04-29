package com.miu360.taxi_check.model;

import java.io.Serializable;

public class AlarmReason implements Serializable{
    private String RKSM;

	public String getRKSM() {
		return RKSM;
	}

	public void setRKSM(String rKSM) {
		RKSM = rKSM;
	}

	@Override
	public String toString() {
		return "AlarmReason [RKSM=" + RKSM + "]";
	}

}
