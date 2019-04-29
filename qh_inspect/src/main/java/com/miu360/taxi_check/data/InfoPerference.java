package com.miu360.taxi_check.data;

import com.miu30.common.base.XPreference;

import android.content.Context;

public class InfoPerference extends XPreference {

	private final static String SP_NAME = "checkinfo";

	public InfoPerference(Context mContext) {
		super(SP_NAME, mContext);

	}

	public void setIsNormal(boolean isNormal) {
		setBoolean("isnoraml", isNormal);
	}

	public Boolean getIsNormal() {
		return getBoolean("isnoraml", false);
	}
}
