package com.miu360.taxi_check.data;

import com.miu30.common.base.XPreference;

import android.content.Context;

public class WeiFaCheckPreference2 extends XPreference {
	private final static String SP_NAME = "checkItem2";

	public WeiFaCheckPreference2(Context mContext) {
		super(SP_NAME, mContext);
	}

	public void setId(long id) {
		setLong("id", id);
	}

	public long getId() {
		return getLong("id", 0);
	}
	
	

	public void setIsCheckd(boolean isNormal) {
		setBoolean("isCheckd", isNormal);
	}

	public Boolean getIsChecked() {
		return getBoolean("isCheckd", false);
	}

}
