package com.miu30.common.ui.entity;

import java.io.Serializable;

public class AlarmReason implements Serializable{
    private String KYX_MCS;

	public String getRKSM() {
		return KYX_MCS;
	}

	public void setRKSM(String rKSM) {
		KYX_MCS = rKSM;
	}

	@Override
	public String toString() {
		return "AlarmReason [RKSM=" + KYX_MCS + "]";
	}

}
