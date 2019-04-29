package com.miu360.taxi_check.data;

/**
 * 轨迹查询请求
 *
 * @author Administrator
 *
 */
public class HistoryQueryReq {
	public String vname;
	public long startTime;
	public long endTime;

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
