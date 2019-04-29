package com.miu360.taxi_check.bean;

import android.R.bool;

public class CarStateBean {
	private String type;
	private boolean cbNormalState;
	private boolean cbBreakLawState;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCbNormalState() {
		return cbNormalState;
	}

	public void setCbNormalState(boolean cbNormalState) {
		this.cbNormalState = cbNormalState;
	}

	public boolean isCbBreakLawState() {
		return cbBreakLawState;
	}

	public void setCbBreakLawState(boolean cbBreakLawState) {
		this.cbBreakLawState = cbBreakLawState;
	}

}
