package com.miu30.common.data;

public class PositionReq {
	private double lon;
	private double lat;
	private String positionInfo;

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(String positionInfo) {
		this.positionInfo = positionInfo;
	}

	@Override
	public String toString() {
		return "PositionReq [lon=" + lon + ", lat=" + lat + ", positionInfo="
				+ positionInfo + "]";
	}

}
