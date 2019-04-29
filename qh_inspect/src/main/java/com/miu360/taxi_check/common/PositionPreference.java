package com.miu360.taxi_check.common;

import android.content.Context;

import com.miu30.common.base.XPreference;

public class PositionPreference extends XPreference {

	private final static String mSpName = "PositionPrefeTaxi";

	public PositionPreference(Context mContext) {
		super(mSpName, mContext);
	}

}
