package com.miu30.common.ui.entity;

public class LocationDetial {
	public String name;
	public String uid;
	public String city;
	public String district;
	public String business;
	public String cityid;
	public Location location;

	public static class Location {
		public double lat;
		public double lng;
	}
}
