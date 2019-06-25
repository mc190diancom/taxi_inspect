package com.feidi.video.mvp.model.entity;


public class AlarmType implements ISelector{
    private String KYX_MCS;

	public String getRKSM() {
		return KYX_MCS;
	}

	public void setRKSM(String rKSM) {
		KYX_MCS = rKSM;
	}

	public AlarmType(String RKSM) {
		this.KYX_MCS = RKSM;
	}

	public AlarmType() {
	}

	@Override
	public String toString() {
		return "AlarmReason [RKSM=" + KYX_MCS + "]";
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void setSelected(boolean selected) {

	}
}
