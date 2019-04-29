package com.miu360.taxi_check.model;

import java.io.Serializable;

public class PersonPosition extends Page implements Serializable{
	
	private String account;


	private double lon;


	private double lat;


	private String personName;


	private String ssdd;
	
	private String time;
	
	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


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


	public String getPersonName() {
		return personName;
	}


	public void setPersonName(String personName) {
		this.personName = personName;
	}


	public String getSsdd() {
		return ssdd;
	}


	public void setSsdd(String ssdd) {
		this.ssdd = ssdd;
	}


	@Override
	public String toString() {
		return "PersonPosition [account=" + account + ", lon=" + lon + ", lat=" + lat + ", personName=" + personName
				+ ", ssdd=" + ssdd + "]";
	}
	

}
