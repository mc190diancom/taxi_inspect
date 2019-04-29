package com.miu360.taxi_check.model;

public class LuWangOrder {
	private double lat;
	   
	private double lon;
	   
	private int order;

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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "LuWangOrder [lat=" + lat + ", lon=" + lon + ", order=" + order
				+ "]";
	}

}
