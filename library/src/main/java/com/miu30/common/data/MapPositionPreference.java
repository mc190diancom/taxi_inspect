package com.miu30.common.data;

import android.content.Context;

import com.miu30.common.base.XPreference;

public class MapPositionPreference extends XPreference {

	private final static String mSpName = "MapPositionPrefe";

	public MapPositionPreference(Context mContext) {
		super(mSpName, mContext);
	}

}
