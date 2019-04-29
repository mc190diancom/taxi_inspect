package com.miu360.taxi_check.model;

public class LatLngDir {
	public double dir = -1;
	public double lat = 0;
	public String latS ;
	public double lng = 0;

	public LatLngDir() {

	}

	
	
	public LatLngDir(double dir, double lat, double lng) {
		super();
		this.dir = dir;
		this.lat = lat;
		this.lng = lng;
	}



	@Override
	public String toString() {
		return "LatLngDir [dir=" + dir + ", lat=" + lat + ", lng=" + lng + "]";
	}

	
}
