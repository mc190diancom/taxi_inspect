package com.miu360.taxi_check.common;

public class VehicleStatus {
	private double lat;
	private double lon;
	public long GPS_TIME;
	public float AZIMUTHS;

	
	public long getGPS_TIME() {
		return GPS_TIME;
	}

	public void setGPS_TIME(long gPS_TIME) {
		GPS_TIME = gPS_TIME;
	}

	public float getAZIMUTHS() {
		return AZIMUTHS;
	}

	public void setAZIMUTHS(float aZIMUTHS) {
		AZIMUTHS = aZIMUTHS;
	}

	@Override
	public String toString() {
		return "VehicleStatus [lat=" + lat + ", lon=" + lon + ", GPS_TIME="
				+ GPS_TIME + ", AZIMUTHS=" + AZIMUTHS + ", VEHICLE_STATUS="
				+ VEHICLE_STATUS + "]";
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	private int VEHICLE_STATUS;

	public int getVEHICLE_STATUS() {
		return VEHICLE_STATUS;
	}

	public void setVEHICLE_STATUS(int vEHICLE_STATUS) {
		VEHICLE_STATUS = vEHICLE_STATUS;
	}

	
}
