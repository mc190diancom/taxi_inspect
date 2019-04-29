package com.miu360.taxi_check.data;

import com.miu30.common.base.XPreference;

import android.content.Context;

public class CameraPreference extends XPreference {
	private final static String SP_NAME = "camera";

	public CameraPreference(Context mContext) {
		super(SP_NAME, mContext);
	}

	public void setId(long id) {
		setLong("id", id);
	}

	public long getId() {
		return getLong("id", 0);
	}

	public boolean isValidate() {
		return getId() != 0;
	}

	public void setIsCheckd(boolean isNormal) {
		setBoolean("isCheckd", isNormal);
	}

	public Boolean getIsChecked() {
		return getBoolean("isCheckd", false);
	}

}
