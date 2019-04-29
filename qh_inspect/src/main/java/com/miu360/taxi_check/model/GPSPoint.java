package com.miu360.taxi_check.model;

import java.util.ArrayList;

public class GPSPoint {

		private double LAT84;
		private double LON84;
		private String GPS_TIME;	
		private int VEHICLE_STATUS;	
		private int AZIMUTHS;

		public int getAZIMUTHS() {
			return AZIMUTHS;
		}
		public void setAZIMUTHS(int aZIMUTHS) {
			AZIMUTHS = aZIMUTHS;
		}
		public int getVEHICLE_STATUS() {
			return VEHICLE_STATUS;
		}
		public void setVEHICLE_STATUS(int vEHICLE_STATUS) {
			VEHICLE_STATUS = vEHICLE_STATUS;
		}
		public double getLat() {
			return LAT84;
		}
		public void setLat(double lat) {
			this.LAT84 = lat;
		}
		public double getLon() {
			return LON84;
		}
		public void setLon(double lon) {
			this.LON84 = lon;
		}
		public String getGpstime() {
			return GPS_TIME;
		}
		public void setGpstime(String gpstime) {
			this.GPS_TIME = gpstime;
		}
		@Override
		public String toString() {
			return "GPSPoint [lat=" + LAT84 + ", lon=" + LON84 + ", gpstime=" + GPS_TIME + "]";
		}

}
