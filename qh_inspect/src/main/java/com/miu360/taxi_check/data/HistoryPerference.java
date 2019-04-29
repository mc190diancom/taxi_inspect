package com.miu360.taxi_check.data;

import com.miu30.common.base.XPreference;

import android.content.Context;

public class HistoryPerference extends XPreference {
	private final static String SP_NAME = "history";

	public HistoryPerference(Context mContext) {
		super(SP_NAME, mContext);

	}

}
