package com.miu360.taxi_check.model;

import java.math.BigDecimal;

public class CPosition {
    private String vname;
    
    private BigDecimal  lon;
    
    private BigDecimal lat;

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
    
    /*private String speed;
    
    private String head;
    
    private String utc;
    
    private String strDate;
    
    private String alarmReason;*/
    
    
}
