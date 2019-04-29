package com.miu30.common.data;

import com.miu30.common.base.XPreference;

import android.content.Context;

public class ZfryPreference extends XPreference {
	private final static String SP_NAME = "zfry";

	public ZfryPreference(Context mContext) {
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
