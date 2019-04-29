package com.miu360.taxi_check.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class FastInspectWeiFaInfo implements Serializable{

	//违法行为
	private String wfxw;
	private String checkTime;
	private long checkTimes;
	private String status;
	private String mcs;

	public String getMcs() {
		return mcs;
	}

	public void setMcs(String mcs) {
		this.mcs = mcs;
	}

	public long getCheckTimes() {
		return checkTimes;
	}

	public void setCheckTimes(long checkTimes) {
		this.checkTimes = checkTimes;
	}

	public String getWfxw() {
		return wfxw;
	}

	public void setWfxw(String wfxw) {
		this.wfxw = wfxw;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
