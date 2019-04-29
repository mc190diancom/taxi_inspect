package com.miu360.taxi_check.common;

public class Gps {
	private double wgLat;
	private double wgLon;

	public Gps(double wgLat, double wgLon) {
		 setWgLat(wgLat);
		 setWgLon(wgLon);
	}

	public double getWgLat() {
		return wgLat;
	}

	@Override
	public String toString() {
		return "Gps [wgLat=" + wgLat + ", wgLon=" + wgLon + "]";
	}

	public void setWgLat(double wgLat) {
		this.wgLat = wgLat;
	}

	public double getWgLon() {
		return wgLon;
	}

	public void setWgLon(double wgLon) {
		this.wgLon = wgLon;
	}

}
